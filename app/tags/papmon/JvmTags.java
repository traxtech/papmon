/*
 * Copyright 2010 by Gedial
 *
 *     This file is part of PAPMON.
 *
 * PAPMON is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PAPMON is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with PAPMON.  If not, see <http://www.gnu.org/licenses/>.
 *
 * If you need support, custom modifications or a different licence that better
 * suits your needs, plase contact <papmon@gedial.com>
 *
 */
package tags.papmon;

import groovy.lang.Closure;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import models.papmon.JvmStat;
import play.templates.FastTags;
import play.templates.GroovyTemplate.ExecutableTemplate;
import utils.papmon.RangeProcessor;
import utils.papmon.ValueProcessor;

/**
 *
 * @author Arnaud Rolly
 */
@FastTags.Namespace("papmon.jvm")
public class JvmTags extends FastTags {

    private static final RangeProcessor RP_FREEMEMORY = new RangeProcessor() {

        @Override
        public void processRange(Date begin, Date end, ValueProcessor vpro) {
            List<JvmStat> stats = JvmStat.find("created BETWEEN ? AND ?", begin, end).fetch();
            for (JvmStat stat : stats) {
                vpro.processValue(stat.freeMemory / 1024d / 1024d);
            }
        }
    };

    private static final RangeProcessor RP_MAXMEMORY = new RangeProcessor() {

        @Override
        public void processRange(Date begin, Date end, ValueProcessor vpro) {
            List<JvmStat> stats = JvmStat.find("created BETWEEN ? AND ?", begin, end).fetch();
            for (JvmStat stat : stats) {
                vpro.processValue(stat.maxMemory / 1024d / 1024d);
            }
        }
    };

    private static final RangeProcessor PROC_USEDMEMORY = new RangeProcessor() {

        @Override
        public void processRange(Date begin, Date end, ValueProcessor vpro) {
            List<JvmStat> stats = JvmStat.find("created BETWEEN ? AND ?", begin, end).fetch();
            for (JvmStat stat : stats) {
                vpro.processValue(stat.getUsedMemory() / 1024d / 1024d);
            }
        }
    };

    public static void _freeMemory(Map<?, ?> args, Closure body, PrintWriter out, ExecutableTemplate template, int fromLine) {
        TagHelper.genTag(args, out, RP_FREEMEMORY);
    }

    public static void _maxMemory(Map<?, ?> args, Closure body, PrintWriter out, ExecutableTemplate template, int fromLine) {
        TagHelper.genTag(args, out, RP_MAXMEMORY);
    }

    public static void _usedMemory(Map<?, ?> args, Closure body, PrintWriter out, ExecutableTemplate template, int fromLine) {
        TagHelper.genTag(args, out, PROC_USEDMEMORY);
    }
}

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
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import models.papmon.JvmStat;
import play.templates.FastTags;
import play.templates.GroovyTemplate.ExecutableTemplate;
import utils.papmon.LineChartUrlBuilder;

/**
 *
 * @author Arnaud Rolly
 */
@FastTags.Namespace("papmon.jvm")
public class JvmTags extends FastTags {

    public static void _freeMemory(Map<?, ?> args, Closure body, PrintWriter out, ExecutableTemplate template, int fromLine) {
        String title = (String)args.get("title");
        int width = (Integer)args.get("width");
        int height = (Integer)args.get("height");
        LineChartUrlBuilder lcBuilder = new LineChartUrlBuilder(title, width, height);
        Calendar beginRange = Calendar.getInstance();
        beginRange.set(Calendar.SECOND, 0);
        beginRange.set(Calendar.MILLISECOND, 0);
        Calendar endRange = (Calendar)beginRange.clone();
        endRange.add(Calendar.HOUR, -1);
        List<JvmStat> stats = JvmStat.find("created BETWEEN ? AND ?", endRange.getTime(), beginRange.getTime()).fetch();
        for (JvmStat stat : stats) {
            long freeMemoryMb = stat.freeMemory/1024/1024;
            lcBuilder.addValue(freeMemoryMb);
        }
        lcBuilder.finish();
        System.err.println(lcBuilder.toString());
        out.print("<img src=\"");
        out.print(lcBuilder.toString());
        out.print("\" alt=\"");
        out.print(title);
        out.print("\"/>");
    }
}

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
import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.Query;
import models.papmon.HibernateStat;
import models.papmon.JvmStat;
import play.db.jpa.JPA;
import play.templates.FastTags;
import play.templates.GroovyTemplate.ExecutableTemplate;
import utils.papmon.RangeProcessor;
import utils.papmon.ValueProcessor;

/**
 *
 * @author Arnaud Rolly
 */
@FastTags.Namespace("papmon")
public class PapmonTags extends FastTags {

    private static final Map<String, RangeProcessor> PROCS;

    private static <T> void genTagScan(final Class<T> clazz, final String statName) {
        for (final Field field : clazz.getFields()) {
            final GenTag genTag = field.getAnnotation(GenTag.class);
            if (genTag != null) {
                StringBuilder queryBuf = new StringBuilder();
                queryBuf.append("SELECT ");
                queryBuf.append(field.getName());
                queryBuf.append(" FROM ");
                queryBuf.append(clazz.getName());
                queryBuf.append(" WHERE created BETWEEN :beginRange AND :endRange");
                final String queryVal = queryBuf.toString();

                PROCS.put(statName + "/" + field.getName(), new RangeProcessor() {

                    @Override
                    public void processRange(Date begin, Date end, ValueProcessor vpro) {

                        System.err.println(queryVal);
                        Query q = JPA.em().createQuery(queryVal);
                        q.setParameter("beginRange", begin);
                        q.setParameter("endRange", end);
                        for (Object o : q.getResultList()) {
                            double value = 0d;
                            if (o instanceof Long) {
                                value = (Long) o;
                            } else if (o instanceof Double) {
                                value = (Double) o;
                            } else if (o instanceof Integer) {
                                value = (Integer) o;
                            }
                            vpro.processValue(value / genTag.divider());
                        }
                    }
                });
            }
        }
    }

    static {
        PROCS = new HashMap<String, RangeProcessor>();
        genTagScan(JvmStat.class, "jvm");
        genTagScan(HibernateStat.class, "hibernate");
    }

    public static void _lineChart(Map<?, ?> args, Closure body, PrintWriter out, ExecutableTemplate template, int fromLine) {
        String stat = (String) args.get("stat");
        TagHelper.genHtmlLineChart(args, out, PROCS.get(stat));
    }
}

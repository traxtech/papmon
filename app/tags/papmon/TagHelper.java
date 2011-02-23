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

import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Map;
import play.db.jpa.Model;
import utils.papmon.LineChartUrlBuilder;
import utils.papmon.RangeProcessor;

/**
 *
 * @author Arnaud Rolly
 */
public class TagHelper {

    public static <T extends Model> void genTag(Map<?, ?> args, PrintWriter out, RangeProcessor rex) {
        String title = (String) args.get("title");
        int width = (Integer) args.get("width");
        int height = (Integer) args.get("height");
        LineChartUrlBuilder lcBuilder = new LineChartUrlBuilder(title, width, height);
        Calendar endRange = Calendar.getInstance();
        endRange.set(Calendar.SECOND, 0);
        endRange.set(Calendar.MILLISECOND, 0);
        Calendar beginRange = (Calendar) endRange.clone();
        String range = (String) args.get("range");
        if ("hour".equals(range)) {
            beginRange.add(Calendar.HOUR, -1);
        } else if ("day".equals(range)) {
            beginRange.add(Calendar.DAY_OF_MONTH, -1);
        } else if ("week".equals(range)) {
            beginRange.add(Calendar.WEEK_OF_MONTH, -1);
        } else if ("month".equals(range)) {
            beginRange.add(Calendar.MONTH, -1);
        } else {
            throw new RuntimeException("Invalid range");
        }
        rex.processRange(beginRange.getTime(), endRange.getTime(), lcBuilder);
        lcBuilder.finish();
        out.print("<img src=\"");
        out.print(lcBuilder.toString());
        out.print("\" alt=\"");
        out.print(title);
        out.print("\"/>");
    }
}

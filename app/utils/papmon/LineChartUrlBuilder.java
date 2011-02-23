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
package utils.papmon;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import play.Logger;

/**
 *
 * @author Arnaud Rolly
 */
public class LineChartUrlBuilder implements ValueProcessor {

    private StringBuilder buffer = new StringBuilder();
    private boolean firstValue = true;
    private double minValue = Double.MAX_VALUE;
    private double maxValue = Double.MIN_VALUE;

    public LineChartUrlBuilder(String title, int width, int height) {
        buffer.append("http://chart.apis.google.com/chart?chxt=y&chs=");
        buffer.append(width);
        buffer.append("x");
        buffer.append(height);
        buffer.append("&cht=lc&chtt=");
        try {
            buffer.append(URLEncoder.encode(title, "UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            Logger.error(ex, "Unknown encoding UTF-8");
        }
        buffer.append("&chd=t:");
    }

    public void processValue(double value) {
        if (firstValue) {
            firstValue = false;
        } else {
            buffer.append(",");
        }
        buffer.append(value);
        if (value < minValue) {
            minValue = value;
        }
        if (value > maxValue) {
            maxValue = value;
        }
    }

    public void finish() {
        buffer.append("&chxr=0,");
        buffer.append(minValue);
        buffer.append(",");
        buffer.append(maxValue);
        buffer.append("&chls=1");
    }

    @Override
    public String toString() {
        return buffer.toString();
    }
}

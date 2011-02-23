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
package jobs.papmon;

import utils.papmon.Log4jAppender;
import play.Logger;
import play.jobs.Job;
import play.jobs.OnApplicationStart;

/**
 *
 * @author Arnaud Rolly
 */
@OnApplicationStart
public class MonitorSetup extends Job {

    @Override
    public void doJob() throws Exception {
        System.err.println("APPENDER ADDED");
        Logger.log4j.addAppender(new Log4jAppender());
    }
}

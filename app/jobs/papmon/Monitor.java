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

import java.util.Date;
import models.papmon.HibernateStat;
import models.papmon.JvmStat;
import play.Logger;
import play.Play;
import play.jobs.Job;
import play.jobs.On;

/**
 * Job that collect statistics each minute.
 * @author arnaud
 */
@On("0 * * * * ?")
public class Monitor extends Job {

    @Override
    public void doJob() throws Exception {
        Date refDate = new Date();
        new JvmStat(refDate).save();
        if ("true".equalsIgnoreCase(Play.configuration.getProperty("hibernate.generate_statistics"))) {
            new HibernateStat(refDate).save();
        }
        Logger.debug("Monitoring statistics grabbed");
    }
    
}

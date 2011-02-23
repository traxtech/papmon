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
package models.papmon;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.apache.log4j.spi.LoggingEvent;
import org.hibernate.annotations.Index;
import play.db.jpa.Model;

/**
 *
 * @author Arnaud Rolly
 */
@Entity
@Table(name = "papmon_log4jevent")
public class Log4jEvent extends Model {

    @Index(name = "idx_papmon_l4j_created")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_date", nullable = false, updatable = false)
    public Date created;
    @Column(name = "level", nullable = false, updatable = false)
    public String level;

    public Log4jEvent(LoggingEvent le) {
        created = new Date(le.timeStamp);
        level = le.getLevel().toString();
    }
}

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
import org.hibernate.Session;
import org.hibernate.stat.Statistics;
import play.db.jpa.JPA;
import play.db.jpa.Model;

/**
 * Aggregates statistics extracted from Hibernate.
 * @author Arnaud Rolly
 */
@Entity
@Table(name = "papmon_hibernatestat")
public class HibernateStat extends Model {

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    public Date created;
    public long queryExecutionCount;
    public long queryExecutionMaxTime;
    public long sessionOpenCount;
    public long sessionCloseCount;
    public long entityLoadCount;
    public long entityInsertCount;
    public long entityUpdateCount;
    public long entityDeleteCount;
    public long entityFetchCount;
    public long queryCacheHitCount;
    public long queryCacheMissCount;
    public long queryCachePutCount;
    public long secondLevelCacheHitCount;
    public long secondLevelCacheMissCount;
    public long secondLevelCachePutCount;

    public HibernateStat(Date refDate) {
        created = refDate;
        Session session = (Session) JPA.em().getDelegate();
        Statistics stats = session.getSessionFactory().getStatistics();
        queryExecutionCount = stats.getQueryExecutionCount();
        queryExecutionMaxTime = stats.getQueryExecutionMaxTime();
        sessionOpenCount = stats.getSessionOpenCount();
        sessionCloseCount = stats.getSessionCloseCount();
        entityLoadCount = stats.getEntityLoadCount();
        entityInsertCount = stats.getEntityInsertCount();
        entityUpdateCount = stats.getEntityUpdateCount();
        entityDeleteCount = stats.getEntityDeleteCount();
        entityFetchCount = stats.getEntityFetchCount();
        queryCacheHitCount = stats.getQueryCacheHitCount();
        queryCacheMissCount = stats.getQueryCacheMissCount();
        queryCachePutCount = stats.getQueryCachePutCount();
        secondLevelCacheHitCount = stats.getSecondLevelCacheHitCount();
        secondLevelCacheMissCount = stats.getSecondLevelCacheMissCount();
        secondLevelCachePutCount = stats.getSecondLevelCachePutCount();
    }
}

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

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.Index;
import play.db.jpa.Model;

/**
 * Aggregates statistics extracted from the JVM.
 * @author Arnaud Rolly
 */
@Entity
@Table(name = "papmon_jvmstat")
public class JvmStat extends Model {

    @Index(name="idx_papmon_jvm_created")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_date", nullable = false, updatable = false)
    public Date created;
    @Column(name = "total_mem", nullable = false, updatable = false)
    public long totalMemory;
    @Column(name = "free_mem", nullable = false, updatable = false)
    public long freeMemory;
    @Column(name = "max_mem", nullable = false, updatable = false)
    public long maxMemory;
    @Column(name = "used_permgen_mem", updatable = false)
    public Long usedPermGenMemory;
    @Column(name = "max_permgen_mem", updatable = false)
    public Long maxPermGenMemory;
    @Column(name = "used_heap_mem", nullable = false, updatable = false)
    public long usedHeapMemory;
    @Column(name = "max_heap_mem", nullable = false, updatable = false)
    public long maxHeapMemory;
    @Column(name = "used_nonheap_mem", nullable = false, updatable = false)
    public long usedNonHeapMemory;
    @Column(name = "max_nonheap_mem", nullable = false, updatable = false)
    public long maxNonHeapMemory;
    @Column(name = "loaded_classes", nullable = false, updatable = false)
    public int loadedClassesCount;
    @Column(name = "gb_time", nullable = false, updatable = false)
    public long garbageCollectionTimeMillis;

    public JvmStat(Date refDate) {
        created = refDate;
        // Get basic memory usage from the Runtime
        Runtime currentRuntime = Runtime.getRuntime();
        totalMemory = currentRuntime.totalMemory();
        freeMemory = currentRuntime.freeMemory();
        maxMemory = currentRuntime.maxMemory();
        // Search for the perm gen MemoryPoolMXBean to get used and max perm gen
        for (MemoryPoolMXBean memoryPool : ManagementFactory.getMemoryPoolMXBeans()) {
            if (memoryPool.getName().toLowerCase().contains("perm gen")) {
                usedPermGenMemory = memoryPool.getUsage().getUsed();
                maxPermGenMemory = memoryPool.getUsage().getMax();
                break;
            }
        }
        // Get used and max memory for heap and non heap
        usedHeapMemory = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed();
        maxHeapMemory = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getMax();
        usedNonHeapMemory = ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage().getUsed();
        maxNonHeapMemory = ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage().getMax();
        // Number of loaded classes
        loadedClassesCount = ManagementFactory.getClassLoadingMXBean().getLoadedClassCount();
        // Time passed by the garbage colelctor
        for (final GarbageCollectorMXBean garbageCollector : ManagementFactory.getGarbageCollectorMXBeans()) {
            garbageCollectionTimeMillis += garbageCollector.getCollectionTime();
        }
    }

    public long getUsedMemory() {
        return totalMemory - freeMemory;
    }
}

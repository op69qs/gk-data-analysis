package org.jeecg.modules.reporting.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface ReportRuntimeLockMapper {
    int acquire(@Param("lockName") String lockName, @Param("owner") String owner,
                @Param("leaseUntil") Date leaseUntil, @Param("now") Date now);
    String lockOwnerForUpdate(@Param("lockName") String lockName);
    int release(@Param("lockName") String lockName, @Param("owner") String owner);
}

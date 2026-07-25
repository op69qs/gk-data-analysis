package org.jeecg.modules.reporting.mapper;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.reporting.legacy.LegacyKeyPending;
import org.jeecg.modules.reporting.legacy.LegacyTimsPending;

public interface LegacyPendingMapper {
    int countKeyPendingByZipBase(@Param("zipBase") String zipBase);
    int insertKeyPending(@Param("record") LegacyKeyPending record);
    int updateKeyPending(@Param("record") LegacyKeyPending record);
    int insertTimsPending(@Param("record") LegacyTimsPending record);
    int updateTimsPending(@Param("record") LegacyTimsPending record);
}

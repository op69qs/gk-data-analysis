package org.jeecg.modules.audit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.sql.Timestamp;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.jeecg.modules.audit.entity.DataAnalysisBizAuditLog;

public interface DataAnalysisBizAuditLogMapper extends BaseMapper<DataAnalysisBizAuditLog> {

    @Select("SELECT * FROM \"jeecg-boot-os\".sys_data_analysis_biz_audit_log "
        + "WHERE source_app = #{sourceApp} "
        + "AND sync_status IN ('PENDING', 'FAILED') "
        + "AND sync_attempts < #{maxRetryAttempts} "
        + "ORDER BY event_time DESC "
        + "LIMIT #{limit}")
    List<DataAnalysisBizAuditLog> findRetryCandidates(@Param("sourceApp") String sourceApp,
                                                      @Param("maxRetryAttempts") int maxRetryAttempts,
                                                      @Param("limit") int limit);

    @Update("UPDATE \"jeecg-boot-os\".sys_data_analysis_biz_audit_log "
        + "SET sync_status = #{syncStatus}, "
        + "last_sync_time = #{syncTime}, "
        + "last_sync_error = #{syncError}, "
        + "sync_attempts = sync_attempts + 1 "
        + "WHERE id = #{id}")
    int updateSyncResult(@Param("id") String id,
                         @Param("syncStatus") String syncStatus,
                         @Param("syncTime") Timestamp syncTime,
                         @Param("syncError") String syncError);
}

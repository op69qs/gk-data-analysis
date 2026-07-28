package org.jeecg.modules.reporting.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("agent_key_file.report_runtime_lock")
public class ReportRuntimeLock implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.INPUT)
    private String lockName;
    private String leaseOwner;
    private Date leaseUntil;
    private Date updateTime;
}

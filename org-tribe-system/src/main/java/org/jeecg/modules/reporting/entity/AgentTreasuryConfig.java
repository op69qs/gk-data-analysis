package org.jeecg.modules.reporting.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("agent_key_file.agent_treatury_config")
public class AgentTreasuryConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    private String treCode;
    private String treName;
    private Date startDate;
    private Date endDate;
    private String state;
    private String addUserid;
    private Date addTime;
    private String modUserid;
    private Date modTime;
}

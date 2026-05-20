CREATE TABLE IF NOT EXISTS "jeecg-boot-os".sys_data_analysis_biz_audit_log (
    id VARCHAR(64) PRIMARY KEY,
    event_id VARCHAR(64) NOT NULL,
    source_app VARCHAR(64) NOT NULL,
    menu_code VARCHAR(128),
    menu_name VARCHAR(255),
    module_code VARCHAR(255),
    module_name VARCHAR(255),
    entity_type VARCHAR(128),
    action_type VARCHAR(64) NOT NULL,
    action_code VARCHAR(64) NOT NULL,
    action_name VARCHAR(255) NOT NULL,
    nexus_user_id VARCHAR(128),
    nexus_username VARCHAR(255),
    local_user_id VARCHAR(128),
    local_username VARCHAR(255),
    request_uri VARCHAR(500),
    request_method VARCHAR(32),
    ip_address VARCHAR(64),
    user_agent VARCHAR(1000),
    result_status VARCHAR(32) NOT NULL,
    error_message VARCHAR(1000),
    duration_ms BIGINT,
    biz_key VARCHAR(255),
    biz_no VARCHAR(255),
    query_summary TEXT,
    extra_data TEXT,
    event_time TIMESTAMPTZ NOT NULL,
    sync_status VARCHAR(32) NOT NULL,
    sync_attempts INTEGER NOT NULL DEFAULT 0,
    last_sync_time TIMESTAMPTZ,
    last_sync_error VARCHAR(1000)
);

CREATE UNIQUE INDEX IF NOT EXISTS ux_data_analysis_biz_audit_log_event_id
    ON "jeecg-boot-os".sys_data_analysis_biz_audit_log (event_id);

CREATE INDEX IF NOT EXISTS idx_data_analysis_biz_audit_log_event_time
    ON "jeecg-boot-os".sys_data_analysis_biz_audit_log (event_time DESC);

CREATE INDEX IF NOT EXISTS idx_data_analysis_biz_audit_log_sync_status_time
    ON "jeecg-boot-os".sys_data_analysis_biz_audit_log (source_app, sync_status, event_time DESC);

CREATE INDEX IF NOT EXISTS idx_data_analysis_biz_audit_log_menu_code
    ON "jeecg-boot-os".sys_data_analysis_biz_audit_log (menu_code);

CREATE INDEX IF NOT EXISTS idx_data_analysis_biz_audit_log_local_user
    ON "jeecg-boot-os".sys_data_analysis_biz_audit_log (local_user_id);

CREATE INDEX IF NOT EXISTS idx_data_analysis_biz_audit_log_nexus_user
    ON "jeecg-boot-os".sys_data_analysis_biz_audit_log (nexus_user_id);

COMMENT ON TABLE "jeecg-boot-os".sys_data_analysis_biz_audit_log IS '数据分析平台业务审计日志表';
COMMENT ON COLUMN "jeecg-boot-os".sys_data_analysis_biz_audit_log.event_id IS '审计事件唯一标识';
COMMENT ON COLUMN "jeecg-boot-os".sys_data_analysis_biz_audit_log.source_app IS '日志来源应用标识';
COMMENT ON COLUMN "jeecg-boot-os".sys_data_analysis_biz_audit_log.menu_code IS '菜单ID';
COMMENT ON COLUMN "jeecg-boot-os".sys_data_analysis_biz_audit_log.menu_name IS '菜单标题';
COMMENT ON COLUMN "jeecg-boot-os".sys_data_analysis_biz_audit_log.module_code IS '模块或菜单路径';
COMMENT ON COLUMN "jeecg-boot-os".sys_data_analysis_biz_audit_log.module_name IS '模块名称';
COMMENT ON COLUMN "jeecg-boot-os".sys_data_analysis_biz_audit_log.entity_type IS '业务实体类型';
COMMENT ON COLUMN "jeecg-boot-os".sys_data_analysis_biz_audit_log.action_type IS '动作分类';
COMMENT ON COLUMN "jeecg-boot-os".sys_data_analysis_biz_audit_log.action_code IS '动作编码';
COMMENT ON COLUMN "jeecg-boot-os".sys_data_analysis_biz_audit_log.action_name IS '动作名称';
COMMENT ON COLUMN "jeecg-boot-os".sys_data_analysis_biz_audit_log.nexus_user_id IS '门户用户ID';
COMMENT ON COLUMN "jeecg-boot-os".sys_data_analysis_biz_audit_log.nexus_username IS '门户用户名';
COMMENT ON COLUMN "jeecg-boot-os".sys_data_analysis_biz_audit_log.local_user_id IS '本地用户ID';
COMMENT ON COLUMN "jeecg-boot-os".sys_data_analysis_biz_audit_log.local_username IS '本地用户名';
COMMENT ON COLUMN "jeecg-boot-os".sys_data_analysis_biz_audit_log.request_uri IS '业务页面路径或请求URI';
COMMENT ON COLUMN "jeecg-boot-os".sys_data_analysis_biz_audit_log.request_method IS '请求方式或业务动作方式';
COMMENT ON COLUMN "jeecg-boot-os".sys_data_analysis_biz_audit_log.result_status IS '执行结果状态';
COMMENT ON COLUMN "jeecg-boot-os".sys_data_analysis_biz_audit_log.extra_data IS '扩展摘要数据';
COMMENT ON COLUMN "jeecg-boot-os".sys_data_analysis_biz_audit_log.sync_status IS '同步状态';
COMMENT ON COLUMN "jeecg-boot-os".sys_data_analysis_biz_audit_log.sync_attempts IS '同步重试次数';

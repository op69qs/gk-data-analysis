CREATE SCHEMA IF NOT EXISTS adm;

CREATE TABLE IF NOT EXISTS adm.exec_shell_task_run_log (
  id varchar(40) NOT NULL,
  task_id varchar(40) NOT NULL,
  task_name varchar(255),
  task_type varchar(2),
  shell_path varchar(255),
  shell_name varchar(255),
  shell_param varchar(1000),
  status varchar(3) NOT NULL,
  start_time timestamp(0) NOT NULL DEFAULT current_timestamp,
  end_time timestamp(0),
  result_message text,
  create_user varchar(128),
  PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS idx_exec_shell_task_run_log_task_time
  ON adm.exec_shell_task_run_log (task_id, start_time DESC);

CREATE INDEX IF NOT EXISTS idx_exec_shell_task_run_log_status
  ON adm.exec_shell_task_run_log (status);

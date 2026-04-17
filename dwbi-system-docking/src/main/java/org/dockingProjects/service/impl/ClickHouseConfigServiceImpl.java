package org.dockingProjects.service.impl;

import org.dockingProjects.mapper.clickhouse.ClickHouseConfigMapper;
import org.dockingProjects.service.ClickhouseConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author haojiang.
 * @Ddate 2020/11/13 17:24
 * @Description
 */
@Service
public class ClickHouseConfigServiceImpl implements ClickhouseConfigService {

    @Value("${ucloudProcess}")
    private String ucloudProcess;

    @Value("${upmProcess}")
    private String upmProcess;

    @Value("${solarwindsProcess}")
    private String solarwindsProcess;

    @Autowired
    private ClickHouseConfigMapper configMapper;

    @Override
    public List<Map<String, Object>> getInterfaceConfig(String processId) {
        return null;
    }

    @Override
    public void insertProcessRecords(Map<String, Object> map) {
        configMapper.insertProcessRecords(map);
    }

    @Override
    public void insertApiRecords(Map<String, Object> map) {
        configMapper.insertApiRecords(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void insertData(Map<String, Object> map) {
        if (ucloudProcess.equals(map.get("process_id"))) { //优云系统
            map.put("tableName", "api_interface_system_data" + map.get("tableMonth"));
            List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("list");
            if (list != null && list.size() > 0) {
                map.put("saveList", list);
                configMapper.insertData(map);
            }
        } else if (upmProcess.equals(map.get("process_id"))) { //UPM系统
            List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("list");
            List<Map<String, Object>> dataList1 = (List<Map<String, Object>>) list.get(0).get("netPerformanceEventLog");//网络性能告警日志
            if (dataList1 != null && dataList1.size() > 0) {
                map.put("saveList", dataList1);
                configMapper.insertNetPerformanceEventLogData(map);
            }
            List<Map<String, Object>> dataList2 = (List<Map<String, Object>>) list.get(0).get("alarmLogAbnormalBehavior"); //异常行为告警日志
            if (dataList2 != null && dataList2.size() > 0) {
                map.put("saveList", dataList2);
                configMapper.insertAlarmLogAbnormalBehaviorData(map);
            }
            List<Map<String, Object>> dataList3 = (List<Map<String, Object>>) list.get(0).get("otherData"); //其他数据表(非日志)
            if (dataList3 != null && dataList3.size() > 0) {
                map.put("tableName", "api_interface_system_data" + map.get("tableMonth"));
                map.put("saveList", dataList3);
                configMapper.insertData(map);
            }
        }
    }

    @Override
    public List<Map<String, Object>> selectFailedApi(Map<String, Object> map) {
        return configMapper.selectFailedApi(map);
    }

    @Override
    public void updateFailedApiData(Map<String, Object> map) {
        configMapper.updateFailedApiData(map);
    }

    @Override
    public void createTable(Map<String, Object> map) {
        if (ucloudProcess.equals(map.get("processId"))) { //优云系统
            map.put("tableName", "api_interface_system_data" + map.get("tableMonth"));
            if (configMapper.queryTableCount(map) == 0) { //表不存在，建表
                configMapper.createTable(map);
            }
        } else if (upmProcess.equals(map.get("processId"))) { //UPM系统
            // TODO: 2020/11/1 创建网络/SDN性能警报日志表
            map.put("tableName", "netperformanceeventlog_" + map.get("tableMonth"));
            if (configMapper.queryTableCount(map) == 0) { //表不存在，建表
                configMapper.createNetPerformanceEventLog(map);
            }
            // TODO: 2020/11/1 创建异常行为告警日志表
            map.put("tableName", "alarmlogabnormalbehavior_" + map.get("tableMonth"));
            if (configMapper.queryTableCount(map) == 0) { //表不存在，建表
                configMapper.createAlarmLogAbnormalBehavior(map);
            }
            // TODO: 2020/11/1 创建业务/应用数据表
            map.put("tableName", "api_interface_system_data" + map.get("tableMonth"));
            if (configMapper.queryTableCount(map) == 0) { //表不存在，建表
                configMapper.createTable(map);
            }
        } else if (solarwindsProcess.equals(map.get("processId"))) { //solarwinds系统

        }
    }

    @Override
    public void insertAlarmData(List<Map<String, Object>> list) {
        configMapper.insertAlarmData(list);
    }
}

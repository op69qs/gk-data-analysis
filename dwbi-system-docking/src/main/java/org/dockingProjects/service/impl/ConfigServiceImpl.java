package org.dockingProjects.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.dockingProjects.mapper.mysql.ConfigMapper;
import org.dockingProjects.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author haojiang.
 * @Ddate 2020/8/14 17:04
 * @Description
 */
@Slf4j
@Service("mysql")
public class ConfigServiceImpl implements ConfigService {

    @Value("${ucloudProcess}")
    private String ucloudProcess;

    @Value("${upmProcess}")
    private String upmProcess;

    @Value("${solarwindsProcess}")
    private String solarwindsProcess;

    @Autowired
    private ConfigMapper configMapper;

    @Override
    public List<Map<String, Object>> getProcessConfig() {
        return configMapper.getProcessConfig();
    }

    @Override
    public List<Map<String, Object>> getProcessThreadConfig(String processId) {
        return configMapper.getProcessThreadConfig(processId);
    }

    @Override
    public List<Map<String, Object>> getSystemConfig(String processId) {
        return configMapper.getSystemConfig(processId);
    }

    @Override
    public List<Map<String, Object>> getPlatformConfig(String systemId) {
        return configMapper.getPlatformConfig(systemId);
    }

    @Override
    public List<Map<String, Object>> getTargetConfig(String platformId) {
        return configMapper.getTargetConfig(platformId);
    }

    @Override
    public List<Map<String, Object>> getResourcesConfig(String platformId) {
        return configMapper.getResourcesConfig(platformId);
    }

    @Override
    public List<Map<String, Object>> getResourcesIPConfig(String resourceId) {
        return configMapper.getResourcesIPConfig(resourceId);
    }

    @Override
    public List<Map<String, Object>> getInterfaceConfig(String processId) {
        List<Map<String, Object>> list = new ArrayList<>();
        if (ucloudProcess.equals(processId)) { //优云系统
            list = configMapper.getUcloudInterfaceConfig(processId);
        } else if (upmProcess.equals(processId)) { //UPM系统
            list = configMapper.getUPMInterfaceConfig(processId);
        } else if (solarwindsProcess.equals(processId)) { //solarwinds系统

        }
        return list;
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
            // 创建网络/SDN性能警报日志表
            map.put("tableName", "netperformanceeventLog_" + map.get("tableMonth"));
            if (configMapper.queryTableCount(map) == 0) { //表不存在，建表
                configMapper.createNetPerformanceEventLog(map);
            }
            // 创建异常行为告警日志表
            map.put("tableName", "alarmlogabnormalbehavior_" + map.get("tableMonth"));
            if (configMapper.queryTableCount(map) == 0) { //表不存在，建表
                configMapper.createAlarmLogAbnormalBehavior(map);
            }
            // 创建业务/应用数据表
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

    @Override
    public void callProcedure(Map<String, Object> map) {
        if ("ucloud".equals(map.get("dataBase"))) { //优云
            //调用优云告警汇总存储过程
            Map<String, Object> data1 = new HashMap<>();
            data1.put("tableName", map.get("alarm_data"));
            data1.put("date", map.get("date"));
            configMapper.callUcloudAlarmProcedure(data1);
            System.out.println("执行优云告警汇总存储过程...." + data1.get("returnVal"));

            //调用优云指标汇总存储过程
            Map<String, Object> data2 = new HashMap<>();
            data2.put("tableName", map.get("index_data"));
            data2.put("date", map.get("date"));
            configMapper.callUcloudIndexProcedure(data2);
            System.out.println("执行优云指标汇总存储过程...." + data1.get("returnVal"));
        } else if ("upm".equals(map.get("dataBase"))) { //UPM
            //调用UPM异常行为告警汇总存储过程
            Map<String, Object> data1 = new HashMap<>();
            data1.put("tableName", map.get("alarmlog"));
            data1.put("date", map.get("date"));
            configMapper.callUPMAlarmLogProcedure(data1);
            System.out.println("执行UPM异常行为告警汇总存储过程...." + data1.get("returnVal"));

            //调用UPM网络性能告警汇总存储过程
            Map<String, Object> data2 = new HashMap<>();
            data2.put("tableName", map.get("netper"));
            data2.put("date", map.get("date"));
            configMapper.callUPMnetperLogProcedure(data2);
            System.out.println("执行UPM网络异常告警汇总存储过程...." + data1.get("returnVal"));

            //调用UPM指标汇总存储过程
            Map<String, Object> data3 = new HashMap<>();
            data3.put("tableName", map.get("index_data"));
            data3.put("date", map.get("date"));
            configMapper.callUPMIndexProcedure(data3);
            System.out.println("执行UPM指标汇总存储过程...." + data1.get("returnVal"));
        }
    }
}

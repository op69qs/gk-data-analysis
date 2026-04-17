package org.dockingProjects.service;

import java.util.List;
import java.util.Map;

/**
 * @author haojiang.
 * @Ddate 2020/8/14 17:04
 * @Description
 */
public interface ClickhouseConfigService {

    /**
     * 获取请求调用接口
     *
     * @return
     */
    List<Map<String, Object>> getInterfaceConfig(String processId);

    /**
     * 插入进程执行状态记录
     *
     * @return
     */
    void insertProcessRecords(Map<String, Object> map);

    /**
     * 插入接口调用记录
     *
     * @return
     */
    void insertApiRecords(Map<String, Object> map);

    /**
     * 插入接口获取数据
     *
     * @return
     */
    void insertData(Map<String, Object> map);

    /**
     * 获取调用失败的Api接口
     *
     * @return
     */
    List<Map<String, Object>> selectFailedApi(Map<String, Object> map);

    /**
     * 修改调用失败的Api接口信息
     *
     * @return
     */
    void updateFailedApiData(Map<String, Object> map);

    /**
     * 动态创建表
     *
     * @return
     */
    void createTable(Map<String, Object> map);

    /**
     * @Author haojiang
     * @Date 2020/11/20 13:26
     * @Description 插入监测器告警数据
     */
    void insertAlarmData(List<Map<String, Object>> list);

}

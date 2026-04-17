package org.dockingProjects.mapper.clickhouse;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author HaoJiang.
 * @Ddate 2020/8/13 15:22
 */
@Repository
public interface ClickHouseConfigMapper {

    /**
     * 获取进程
     *
     * @return
     */
    List<Map<String, Object>> getProcessConfig();

    /**
     * 根据进程ID获取进程的线程
     *
     * @param processId
     * @return
     */
    List<Map<String, Object>> getProcessThreadConfig(@Param("processId") String processId);

    /**
     * 获取对接系统
     *
     * @return
     */
    List<Map<String, Object>> getSystemConfig(@Param("processId") String processId);

    /**
     * 获取对接系统对应的平台
     *
     * @param systemId
     * @return
     */
    List<Map<String, Object>> getPlatformConfig(@Param("systemId") String systemId);

    /**
     * 获取系统平台信息对应的指标
     *
     * @param platformId
     * @return
     */
    List<Map<String, Object>> getTargetConfig(@Param("platformId") String platformId);

    /**
     * 获取系统平台对应的资源
     *
     * @param platformId
     * @return
     */
    List<Map<String, Object>> getResourcesConfig(@Param("platformId") String platformId);

    /**
     * 获取系统平台资源对应的IP
     *
     * @param resourceId
     * @return
     */
    List<Map<String, Object>> getResourcesIPConfig(@Param("resourceId") String resourceId);

    /**
     * 获取优云系统请求调用接口
     *
     * @return
     */
    List<Map<String, Object>> getUcloudInterfaceConfig(@Param("processId") String processId);

    /**
     * 获取UPM系统请求调用接口
     *
     * @return
     */
    List<Map<String, Object>> getUPMInterfaceConfig(@Param("processId") String processId);

    /**
     * 获取调用失败的Api接口
     *
     * @return
     */
    List<Map<String, Object>> selectFailedApi(@Param("params") Map<String, Object> map);

    /**
     * 插入进程执行状态记录
     *
     * @return
     */
    void insertProcessRecords(@Param("params") Map<String, Object> map);

    /**
     * 修改调用失败的Api接口信息
     *
     * @return
     */
    void updateFailedApiData(@Param("params") Map<String, Object> map);

    /**
     * 插入接口调用记录
     *
     * @return
     */
    void insertApiRecords(@Param("params") Map<String, Object> map);

    /**
     * 插入接口获取数据
     *
     * @return
     */
    void insertData(@Param("params") Map<String, Object> map);

    /**
     * 插入网络/SDN性能警报日志表数据
     *
     * @return
     */
    void insertNetPerformanceEventLogData(@Param("params") Map<String, Object> map);

    /**
     * 插入异常行为日志表数据
     *
     * @return
     */
    void insertAlarmLogAbnormalBehaviorData(@Param("params") Map<String, Object> map);

    /**
     * 查询表是否存在
     */
    int queryTableCount(@Param("params") Map<String, Object> map);

    /**
     * 动态建表
     *
     * @return
     */
    void createTable(@Param("params") Map<String, Object> map);

    /**
     * 创建网络/SDN性能警报日志表
     *
     * @return
     */
    void createNetPerformanceEventLog(@Param("params") Map<String, Object> map);

    /**
     * 创建异常行为日志表
     *
     * @return
     */
    void createAlarmLogAbnormalBehavior(@Param("params") Map<String, Object> map);

    /**
     * @Author haojiang
     * @Date 2020/11/20 13:26
     * @Description 插入监测器告警数据
     */
    void insertAlarmData(@Param("list") List<Map<String, Object>> list);
}

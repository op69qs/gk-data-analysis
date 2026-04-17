// InspectionProcessControlService.java

package org.inspect.service;

import java.util.Map;

/**
 * 检查流程控制
 * @author Created by Samer on 2019/10/24.
 */
public interface InspectionProcessControlService {

    /**
     * 标记当前主流程完成
     * @param params
     */
    void finishReportProcessById(Map<String, Object> params);

    /**
     * 查询当前工作底稿完成状态
     * @param params
     */
    int getWorkingPapersState(Map<String, Object> params);

    /**
     * 获取当前模块流程描述
     * @param params
     */
    String getProcDescById(Map<String, Object> params);

    /**
     * 尝试启动后续流程
     * @param params
     */
    boolean activateFollowProc(Map<String, Object> params);

    /**
     * 尝试启动后续流程
     * @param params
     */
    int getProStateAllById(Map<String, Object> params);

    /**
     * 标记当前子流程完成
     * @param params
     */
    void finishCurSubProcessById(Map<String, Object> params);

    /**
     * 标记事后检查当前子流程完成
     * @param params
     */
    void finishPostSVCurSubProcessById(Map<String, Object> params);

    /**
     * 激活当前流程后续流程主表
     * @param params
     */
    void activateFollowProcess(Map<String, Object> params);

    /**
     * 激活当前流程后续流程子表
     * @param params
     */
    void activateFollowProcessSub(Map<String, Object> params);

    /**
     * 事后监督激活当前流程后续流程子表
     * @param params
     */
    void activatePostSvFollowProcessSub(Map<String, Object> params);

    /**
     * 激活指定子流程
     * @param params
     */
    void activateSpecifyProcessSub(Map<String, Object> params);

    /**
     * 标记当前主流程完成
     * @param params
     */
    void finishCurProcessById(Map<String, Object> params);

}///:~

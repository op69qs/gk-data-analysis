// InspectionProcessControlMapper.java

package org.inspect.dao.mapper.inspect;

import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * 检查流程控制
 * @author Created by Samer on 2019/10/24.
 */
public interface InspectionProcessControlMapper {

    /**
     * 标记当前主流程完成
     * @param params
     */
    void finishReportProcessById(@Param(value = "params")Map<String, Object> params);

    /**
     * 标记当前主流程完成
     * @param params
     */
    void finishCurProcessById(@Param(value = "params")Map<String, Object> params);

    /**
     * 查询当前工作底稿完成状态
     * @param params
     */
    int getWorkingPapersState(@Param(value = "params")Map<String, Object> params);

    /**
     * 获取当前模块流程描述
     * @param params
     */
    String getProcDescById(@Param(value = "params")Map<String, Object> params);

    /**
     * 获取当前流程下所有子流程综合状态结果为0时表示全都已经完成
     * @param params
     */
    int getProStateAllById(@Param(value = "params")Map<String, Object> params);

    /**
     * 标记当前子流程完成
     * @param params
     */
    void finishCurSubProcessById(@Param(value = "params")Map<String, Object> params);

    /**
     * 标记事后检查当前子流程完成
     * @param params
     */
    void finishPostSVCurSubProcessById(@Param(value = "params")Map<String, Object> params);

    /**
     * 激活当前流程后续流程主表
     * @param params
     */
    void activateFollowProcess(@Param(value = "params")Map<String, Object> params);

    /**
     * 激活当前流程后主流程中的子流程
     * @param params
     */
    void activateCurFollowProcessSub(@Param(value = "params")Map<String, Object> params);

    /**
     * 激活当前流程后续流程子表
     * @param params
     */
    void activateFollowProcessSub(@Param(value = "params")Map<String, Object> params);

    /**
     * 事后监督激活当前流程后续流程子表
     * @param params
     */
    void activatePostSvFollowProcessSub(@Param(value = "params")Map<String, Object> params);

    /**
     * 激活指定子流程
     * @param params
     */
    void activateSpecifyProcessSub(@Param(value = "params")Map<String, Object> params);

}///:~

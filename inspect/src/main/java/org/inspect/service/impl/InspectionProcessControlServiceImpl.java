// InspectionProcessControlServiceImpl.java

package org.inspect.service.impl;

import org.inspect.dao.mapper.inspect.InspectionProcessControlMapper;
import org.inspect.service.InspectionProcessControlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 检查流程控制
 * @author Created by Samer on 2019/10/24.
 */
@Service
public class InspectionProcessControlServiceImpl implements InspectionProcessControlService {

    @Autowired
    private InspectionProcessControlMapper inspectionProcessControlMapper;

    /**
     * 标记当前主流程完成
     * @param params
     */
    public void finishReportProcessById(Map<String, Object> params){
        inspectionProcessControlMapper.finishReportProcessById(params);
    }

    /**
     * 查询当前工作底稿完成状态
     * @param params
     */
    public int getWorkingPapersState(Map<String, Object> params){
        return inspectionProcessControlMapper.getWorkingPapersState(params);
    }

    /**
     * 获取当前模块流程描述
     * @param params
     */
    @Override
    public String getProcDescById(Map<String, Object> params){
        return inspectionProcessControlMapper.getProcDescById(params);
    }

    /**
     * 尝试启动后续流程
     * @param params
     */
    @Override
    public boolean activateFollowProc(Map<String, Object> params){
        int index = inspectionProcessControlMapper.getProStateAllById(params);
        if( index == 0 ){
            /*标记当前主流程结束*/
            inspectionProcessControlMapper.finishCurProcessById(params);
            /*激活后续主流程*/
            inspectionProcessControlMapper.activateFollowProcess(params);
            /*激活当前流程后主流程中的子流程*/
            inspectionProcessControlMapper.activateCurFollowProcessSub(params);
            return true;
        }
        return false;
    }

    /*小流程完成情况查询*/
    @Override
    public int getProStateAllById(Map<String, Object> params) {
        return inspectionProcessControlMapper.getProStateAllById(params);
    }

    /**
     * 标记当前子流程完成
     * @param params
     */
    @Override
    public void finishCurSubProcessById(Map<String, Object> params) {
        inspectionProcessControlMapper.finishCurSubProcessById(params);
    }

    /**
     * 标记事后检查当前子流程完成
     * @param params
     */
    @Override
    public void finishPostSVCurSubProcessById(Map<String, Object> params){
        inspectionProcessControlMapper.finishPostSVCurSubProcessById(params);
    }

    /**
     * 激活当前流程后续流程主表
     * @param params
     */
    @Override
    public void activateFollowProcess(Map<String, Object> params) {
        inspectionProcessControlMapper.activateFollowProcess(params);
    }

    /**
     * 激活当前流程后续流程子表
     * @param params
     */
    @Override
    public void activateFollowProcessSub(Map<String, Object> params){
        inspectionProcessControlMapper.activateFollowProcessSub(params);
    }

    /**
     * 事后监督激活当前流程后续流程子表
     * @param params
     */
    @Override
    public void activatePostSvFollowProcessSub(Map<String, Object> params){
        inspectionProcessControlMapper.activatePostSvFollowProcessSub(params);
    }

    /**
     * 激活指定子流程
     * @param params
     */
    @Override
    public void activateSpecifyProcessSub(Map<String, Object> params){
        inspectionProcessControlMapper.activateSpecifyProcessSub(params);
    }

    /**
     * 标记当前主流程完成
     * @param params
     */
    @Override
    public void finishCurProcessById(Map<String, Object> params){
        inspectionProcessControlMapper.finishCurProcessById(params);
    }

} ///:~

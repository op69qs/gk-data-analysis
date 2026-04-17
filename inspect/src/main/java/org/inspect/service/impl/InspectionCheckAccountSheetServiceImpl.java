// InspectionCheckAccountSheetServiceImpl.java

package org.inspect.service.impl;

import org.inspect.dao.mapper.inspect.InspectionCheckAccountSheetMapper;
import org.inspect.service.InspectionCheckAccountSheetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 对账登记表
 * @author Created by Samer on 2019/10/21.
 */
@Service
public class InspectionCheckAccountSheetServiceImpl implements InspectionCheckAccountSheetService {

    @Autowired
    private InspectionCheckAccountSheetMapper inspectionCheckAccountSheetMapper;

    /**
     * 获取子表信息
     * @return
     */
    @Override
    public List<Map<String, String>> getSheetSubInfo(Map<String, Object> params){
        return inspectionCheckAccountSheetMapper.getSheetSubInfo(params);
    }

    /**
     * 根据登记表名查找表名
     * @param params
     * @return
     */
    @Override
    public List<Map<String, String>> getSheetNameByName(Map<String, Object> params) {
        return inspectionCheckAccountSheetMapper.getSheetNameByName(params);
    }

    /**
     * 根据被查库ID任务ID查找对账单位ID
     * @param params
     * @return
     */
    @Override
    public List<Map<String, String>> getTaxOrgIdById(Map<String, Object> params) {
        return inspectionCheckAccountSheetMapper.getTaxOrgIdById(params);
    }

    /**
     * 获取征收机构信息
     * @return
     */
    public List<Map<String, String>> getTaxOrgInfo(Map<String, Object> params){
        return inspectionCheckAccountSheetMapper.getTaxOrgInfo(params);
    }

    /**
     * 根据当前任务ID获取核算主体包含的国库
     * @param params
     * @return
     */
    @Override
    public List<Map<String, String>> getCurTreCodeByTaskId(Map<String, Object> params) {
        return inspectionCheckAccountSheetMapper.getCurTreCodeByTaskId(params);
    }

    /**
     * 根据任务ID获取对账登记表信息
     * @return
     */
    @Override
    public List<Map<String, String>> getCheckAccInfoByTaskId(Map<String, Object> params){
        return inspectionCheckAccountSheetMapper.getCheckAccInfoByTaskId(params);
    }

    /**
     * 根据对账登记表ID获取详细信息
     * @return
     */
    @Override
    public List<Map<String, Object>> getCheckAccSubInfoBySheetId(Map<String, Object> params){
        return inspectionCheckAccountSheetMapper.getCheckAccSubInfoBySheetId(params);
    }

    /**
     * 新增对账登记表信息
     * @return
     */
    @Override
    public void addCheckAccountInfo(Map<String, Object> params){
        inspectionCheckAccountSheetMapper.addCheckAccountInfo(params);
    }

    /**
     * 根据SHEET_ID更新对账登记表信息
     * @param params
     */
    @Override
    public void updateCheckAccountInfo(Map<String, Object> params) {
        inspectionCheckAccountSheetMapper.updateCheckAccountInfo(params);
    }

    /**
     * 新增对账登记表子表内容
     * @return
     */
    @Override
    public void addCheckAccSubInfo(Map<String, Object> params){
        inspectionCheckAccountSheetMapper.addCheckAccSubInfo(params);
    }

    /**
     * 根据SHEET_ID删除对账登记表信息
     * @return
     */
    @Override
    public void delCheckAccInfoBySheetId(Map<String, Object> params){
        inspectionCheckAccountSheetMapper.delCheckAccInfoBySheetId(params);
    }

    /**
     * 根据SHEET_ID删除对账登记表子表内容
     * @return
     */
    @Override
    public void delCheckAccSubBySheetId(Map<String, Object> params){
        inspectionCheckAccountSheetMapper.delCheckAccSubBySheetId(params);
    }

} ///:~

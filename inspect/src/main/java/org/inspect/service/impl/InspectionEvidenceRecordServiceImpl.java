// InspectionEvidenceRecordServiceImpl.java

package org.inspect.service.impl;

import com.alibaba.fastjson.JSONArray;
import org.inspect.dao.mapper.inspect.InspectionEvidenceRecordMapper;
import org.inspect.service.InspectionEvidenceRecordService;
import org.inspect.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 执法检查取证记录
 * @author Created by Samer on 2019/11/21.
 */
@Service
public class InspectionEvidenceRecordServiceImpl implements InspectionEvidenceRecordService {

    @Autowired
    private InspectionEvidenceRecordMapper inspectionEvidenceRecordMapper;

    /**
     * 获取主表信息
     * @param params
     * @return
     */
    @Override
    public List<Map<String, Object>> getEvidenceRecordMainInfo(PageData params) {
        return inspectionEvidenceRecordMapper.getEvidenceRecordMainInfo(params);
    }

    /**
     * 获取取证记录子表
     * @param params
     * @return
     */
    @Override
    public List<Map<String, Object>> getEvidenceRecordSheet(PageData params) {
        return inspectionEvidenceRecordMapper.getEvidenceRecordSheet(params);
    }

    /**
     * 获取取证记录
     * @param params
     */
    @Override
    public List<Map<String, Object>> getEvidenceRecord(PageData params) {
        return inspectionEvidenceRecordMapper.getEvidenceRecord(params);
    }

    /**
     * 修改取证记录
     * @param params
     */
    @Override
    public void editEvidenceRecord(PageData params){
        inspectionEvidenceRecordMapper.editEvidenceRecord(params);
        if( ((List<Map<String, String>>)params.get("dataList")).size() > 0){
            inspectionEvidenceRecordMapper.delEvidenceRecordSheet(params);
            inspectionEvidenceRecordMapper.addEvidenceRecordSheet(params);
        }
    }

    /**
     * 新增取证记录
     * @param params
     */
    @Override
    public void addEvidenceRecord(PageData params) {
        inspectionEvidenceRecordMapper.addEvidenceRecord(params);
        if( ((List<Map<String, String>>)params.get("dataList")).size() > 0 ){
            inspectionEvidenceRecordMapper.delEvidenceRecordSheet(params);
            inspectionEvidenceRecordMapper.addEvidenceRecordSheet(params);
        }
    }

    /**
     * 删除取证记录
     * @param params
     */
    @Override
    public void deleteEvidenceRecord(PageData params) {
        inspectionEvidenceRecordMapper.deleteEvidenceRecord(params);
        inspectionEvidenceRecordMapper.delEvidenceRecordSheet(params);
    }

    /**
     * 新增取证记录子表
     * @param params
     */
    @Override
    public void addEvidenceRecordSheet(PageData params) {
        inspectionEvidenceRecordMapper.delEvidenceRecordSheet(params);
        inspectionEvidenceRecordMapper.addEvidenceRecordSheet(params);
    }

    /**
     * 修改取证记录子表
     * @param params
     */
    @Override
    public void editEvidenceRecordSheet(PageData params) {
        inspectionEvidenceRecordMapper.delEvidenceRecordSheet(params);
        inspectionEvidenceRecordMapper.addEvidenceRecordSheet(params);
    }
} ///:~

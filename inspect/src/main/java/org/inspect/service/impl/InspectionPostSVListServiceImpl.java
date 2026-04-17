// InspectionPostSVListServiceImpl.java

package org.inspect.service.impl;

import org.inspect.dao.mapper.inspect.InspectionPostSVListMapper;
import org.inspect.service.InspectionPostSVListService;
import org.inspect.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 事后监督交接清单
 *
 * @author Created by Samer on 2019/11/21.
 */
@Service
public class InspectionPostSVListServiceImpl implements InspectionPostSVListService {

    @Autowired
    private InspectionPostSVListMapper inspectionPostSVListMapper;

    /**
     * 获取任务信息
     * @param params
     */
    public Map<String, String> getTaskInfoByTaskId(PageData params){
        return inspectionPostSVListMapper.getTaskInfoByTaskId(params);
    }

    /**
     * 获取主表信息
     * @param params
     */
    public List<Map<String, Object>> getTemplateInfo(PageData params){
        return inspectionPostSVListMapper.getTemplateInfo(params);
    }

    /**
     * 获取主表信息
     *
     * @param params
     */
    @Override
    public List<Map<String, Object>> getMainInfo(PageData params) {
        return inspectionPostSVListMapper.getMainInfo(params);
    }

    /**
     * 获取子表信息
     *
     * @param params
     */
    @Override
    public List<Map<String, Object>> getSubSheet(PageData params) {
        return inspectionPostSVListMapper.getSubSheet(params);
    }

    /**
     * 获取全部记录
     *
     * @param params
     */
    @Override
    public List<Map<String, Object>> getFullInfo(PageData params) {
        return inspectionPostSVListMapper.getFullInfo(params);
    }

    /**
     * 修改主表信息
     *
     * @param params
     */
    @Override
    public void editMainInfo(PageData params) {
        inspectionPostSVListMapper.editMainInfo(params);
        inspectionPostSVListMapper.delSubInfo(params);
        inspectionPostSVListMapper.addSubInfo(params);
    }

    /**
     * 新增主表信息
     *
     * @param params
     */
    @Override
    public void addMainInfo(PageData params) {
        inspectionPostSVListMapper.addMainInfo(params);
        inspectionPostSVListMapper.addSubInfo(params);
    }

    /**
     * 删除主表信息
     *
     * @param params
     */
    @Override
    public void deleteMainInfo(PageData params) {
        inspectionPostSVListMapper.deleteMainInfo(params);
        inspectionPostSVListMapper.delSubInfo(params);
    }

    /**
     * 新增子表信息
     * @param params
     */
    public void addSubInfo(PageData params){
        inspectionPostSVListMapper.addSubInfo(params);
    }

    /**
     * 删除子表信息
     * @param params
     */
    public void delSubInfo(PageData params){
        inspectionPostSVListMapper.delSubInfo(params);
    }

} ///:~

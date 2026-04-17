package org.jeecg.modules.enumSetting.service.impl;

import org.jeecg.modules.util.PageData;
import org.jeecg.modules.enumSetting.mapper.EnumMapper;
import org.jeecg.modules.enumSetting.service.EnumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("enumService")
public class EnumServiceImpl implements EnumService {

    @Autowired
    private EnumMapper enumMapper;

    @Override
    public List<Map<String, Object>> getData(PageData pd) {
        return enumMapper.getData(pd);
    }


    @Override
    public Integer getCount(PageData pd) {
        return enumMapper.getCount(pd);
    }


    @Override
    public List<Map<String, Object>> checkCode(PageData pd) {
        return enumMapper.checkCode(pd);
    }

    @Override
    public List<Map<String, Object>> getLogicalOperator() {
        return enumMapper.getLogicalOperator();
    }

    @Override
    public void addEnum(PageData pd) {
        enumMapper.addEnum(pd);
    }

    @Override
    public void editEnum(PageData pd) {
        enumMapper.editEnum(pd);
    }

    @Override
    public void delEnum(PageData pd) {
        enumMapper.delEnum(pd);
    }

    @Override
    public void delEnumNo(PageData pd) {
        enumMapper.delEnumNo(pd);
    }


    @Override
    public List<Map<String, Object>> getEnumType(PageData pd) {
        return enumMapper.getEnumType(pd);
    }

    @Override
    public List<Map<String, Object>> getEnumTypeAll(PageData pd) {
        return enumMapper.getEnumTypeAll(pd);
    }

    @Override
    public List<Map<String, Object>> getFirst(PageData pd) {
        return enumMapper.getFirst(pd);
    }

    @Override
    public List<Map<String, Object>> getSecond(PageData pd) {
        return enumMapper.getSecond(pd);
    }

    @Override
    public List<Map<String, Object>> getThird(PageData pd) {
        return enumMapper.getThird(pd);
    }

    @Override
    public String getEnumDscr(PageData pd) {
        return enumMapper.getEnumDscr(pd);
    }

}

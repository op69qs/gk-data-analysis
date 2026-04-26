// IndustryServiceImpl.java

package org.jeecg.modules.dimnsnSetting.service.impl;

import org.jeecg.modules.dimnsnSetting.mapper.IndustryMapper;
import org.jeecg.modules.dimnsnSetting.service.IndustryService;

import org.jeecg.modules.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author Created by Samer on 2020/7/1.
 */
@Service
public class IndustryServiceImpl implements IndustryService {

    @Autowired
    private IndustryMapper industryMapper;

    @Override
    public List<Map<String, Object>> getIndustryLv1(PageData params) {
        return industryMapper.getIndustryLv1(params);
    }

    @Override
    public List<Map<String, Object>> getIndustryLv2(PageData params) {
        return industryMapper.getIndustryLv2(params);
    }
} ///:~

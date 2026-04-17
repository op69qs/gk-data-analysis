// MvcConfigServiceImpl.java

package org.fixedReport.service.mvcconfig.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.fixedReport.dao.mapper.mvcconfig.MvcConfigMapper;
import org.fixedReport.service.mvcconfig.MvcConfigService;

import java.util.List;
import java.util.Map;

/**
 * 静态页面控制
 * @author Created by Samer on 2019/9/23.
 */
@Service
public class MvcConfigServiceImpl implements MvcConfigService {

    @Autowired
    private MvcConfigMapper mvcConfigMapper;

    public List<Map<String, String>> getMvcConfig(Map<String, String> params){
        return mvcConfigMapper.getMvcConfig(params);
    }

} ///:~

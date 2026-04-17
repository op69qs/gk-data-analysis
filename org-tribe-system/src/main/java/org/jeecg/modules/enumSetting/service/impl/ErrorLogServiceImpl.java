package org.jeecg.modules.enumSetting.service.impl;

import org.jeecg.modules.enumSetting.mapper.ErrorLogMapper;
import org.jeecg.modules.enumSetting.service.ErrorLogService;
import org.jeecg.modules.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ErrorLogServiceImpl implements ErrorLogService {

    @Autowired
    private ErrorLogMapper errorLogMapper;

    @Override
    public List<Map<String, Object>> getData(PageData pd) {
        return errorLogMapper.getData(pd);
    }

    @Override
    public void callProc(PageData pd) {
        errorLogMapper.callProc(pd);
    }

    @Override
    public Integer getCount(PageData pd) {
        return errorLogMapper.getCount(pd);
    }
}

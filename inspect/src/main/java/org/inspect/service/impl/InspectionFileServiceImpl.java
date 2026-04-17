package org.inspect.service.impl;

import org.inspect.dao.mapper.inspect.InspectionFileMapper;
import org.inspect.dao.mapper.inspect.InspectionNoticeMapper;
import org.inspect.service.InspectionFileService;
import org.inspect.service.InspectionNoticeService;
import org.inspect.util.DateUtil;
import org.inspect.util.FileUtil;
import org.inspect.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class InspectionFileServiceImpl implements InspectionFileService {

    @Autowired
    private InspectionFileMapper inspectionFileMapper;


    @Override
    public List<Map<String,Object>> getInspectionFileData(PageData pd) {
        return inspectionFileMapper.getInspectionFileData(pd);
    }

    @Override
    public void addInspectionFile(PageData pd) {
        pd.put("UPLOAD_TIME", DateUtil. getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
        inspectionFileMapper.addInspectionFile(pd);
    }

    @Override
    public void editInspectionFile(PageData pd) {
        inspectionFileMapper.editInspectionFile(pd);
    }

    @Override
    public void delInspectionFile(PageData pd) {
        FileUtil.delFile(pd.getString("INSPECTION_FILE_PATH"));
        inspectionFileMapper.delInspectionFile(pd);
    }
}

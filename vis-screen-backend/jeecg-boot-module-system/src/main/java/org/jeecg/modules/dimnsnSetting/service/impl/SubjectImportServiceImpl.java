package org.jeecg.modules.dimnsnSetting.service.impl;

import org.jeecg.modules.dimnsnSetting.mapper.SubjectImportManagerMapper;
import org.jeecg.modules.dimnsnSetting.model.SubjectImport;
import org.jeecg.modules.dimnsnSetting.service.SubjectImportService;
import org.jeecg.modules.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SubjectImportServiceImpl implements SubjectImportService {

    @Autowired
    private SubjectImportManagerMapper sbjectImport;

    @Override
    public List<Map<String, Object>> getPage(PageData pd) {
        return sbjectImport.getPage(pd);
    }

    @Override
    public Integer getCount(PageData pd) {
        return sbjectImport.getCount(pd);
    }

    @Override
    public List<Map<String, Object>> getAll(PageData pd) {
        return sbjectImport.getAll(pd);
    }

    @Override
    public List<SubjectImport> getExport(PageData pd) {
        return sbjectImport.getExport(pd);
    }

    @Override
    public List<Map<String, Object>> checkRepeat(PageData pd) {
        return sbjectImport.checkRepeat(pd);
    }

    @Override
    public void add(PageData pd) {
        sbjectImport.add(pd);
    }

    @Override
    public void edit(PageData pd) {
        sbjectImport.edit(pd);
    }

    @Override
    public void editStat(PageData pd) {
        sbjectImport.editStat(pd);
    }

    @Override
    public void del(PageData pd) {
        sbjectImport.del(pd);
    }

    @Override
    public void editT(PageData pd) {
        sbjectImport.editT(pd);
    }

    @Override
    public void callProc(PageData pd) {
        sbjectImport.callProc(pd);
    }
}

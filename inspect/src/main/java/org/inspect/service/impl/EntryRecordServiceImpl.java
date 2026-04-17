package org.inspect.service.impl;

import org.inspect.dao.mapper.inspect.EntryRecordMapper;
import org.inspect.dao.mapper.inspect.LegalNoticeMapper;
import org.inspect.service.EntryRecordService;
import org.inspect.service.LegalNoticeService;
import org.inspect.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EntryRecordServiceImpl implements EntryRecordService {

    @Autowired
    private EntryRecordMapper entryRecordMapper;


    @Override
    public Map<String, Object> getEntryRecordData(PageData pd) {
        return entryRecordMapper.getEntryRecordData(pd);
    }

    @Override
    public void addEntryRecord(PageData pd) {
        entryRecordMapper.addEntryRecord(pd);
    }

    @Override
    public void editEntryRecord(PageData pd) {
        entryRecordMapper.editEntryRecord(pd);
    }

    @Override
    public void delEntryRecord(PageData pd) {
        entryRecordMapper.delEntryRecord(pd);
    }
}

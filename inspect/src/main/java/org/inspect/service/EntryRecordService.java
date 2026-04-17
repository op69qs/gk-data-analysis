package org.inspect.service;


import org.inspect.util.PageData;

import java.util.Map;

public interface EntryRecordService {

    Map<String,Object> getEntryRecordData(PageData pd);

    void addEntryRecord(PageData pd);

    void editEntryRecord(PageData pd);

    void delEntryRecord(PageData pd);
}

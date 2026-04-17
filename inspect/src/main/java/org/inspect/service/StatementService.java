package org.inspect.service;


import org.inspect.util.PageData;

import java.util.List;
import java.util.Map;

public interface StatementService {

    List<Map<String,Object>> getStatementData(PageData pd);

    void addStatement(PageData pd);

    void editStatement(PageData pd);

    void delStatement(PageData pd);
}

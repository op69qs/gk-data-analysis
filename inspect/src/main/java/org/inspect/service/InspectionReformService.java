package org.inspect.service;


import org.inspect.util.PageData;

import java.util.List;
import java.util.Map;

public interface InspectionReformService {

    List<Map<String,Object>> getInspectionReformPage(PageData pd);

    List<Map<String,Object>> getInspectionReformData(PageData pd);

    List<Map<String,Object>> getInspectionReformSchemeData(PageData pd);

    List<Map<String,Object>> getInspectionReformReplayData(PageData pd);

    List<Map<String,Object>> getQuestionLedgerLvById(PageData pd);

    List<Map<String,Object>> getQuestionLedgerLvOne(PageData pd);

    List<Map<String,Object>> getReformData(PageData pd);

    List<Map<String,Object>> isComplete(PageData pd);

    Boolean updateReform(PageData pd);

    void editIsScheme(PageData pd);

    void editReform(PageData pd);

    void delInspectionReform(PageData pd);

    void delInspectionReformScheme(PageData pd);

    void delInspectionReformReplay(PageData pd);

    Integer countInspectionReform(PageData pd);

    void addInspectionReform(PageData pd);

    void addInspectionScheme(PageData pd);

    void addInspectionReplay(PageData pd);

    List<Map<String,Object>> toAddCase(PageData pd);
}

// InspectionTemporarySumServiceImpl.java

package org.inspect.service.impl;

import org.inspect.dao.mapper.inspect.InspectionQuestionLedgerMapper;
import org.inspect.dao.mapper.inspect.InspectionTemporarySumMapper;
import org.inspect.service.InspectionTemporarySumService;
import org.inspect.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 检查任务-问题台账服务接口实现类
 *
 * @author Created by Samer on 2019/10/17.
 */
@Service
public class InspectionTemporarySumServiceImpl implements InspectionTemporarySumService {

    @Autowired
    private InspectionTemporarySumMapper inspectionTemporarySumMapper;

    @Autowired
    private InspectionQuestionLedgerMapper inspectionQuestionLedgerMapper;

    /**
     * 获取子流程信息
     * @param params
     * @return
     */
    @Override
    public Map<String, String> getProcSubTitle(Map<String, Object> params){
        return inspectionTemporarySumMapper.getProcSubTitle(params);
    }

    /**
     * 制度依据组装
     *
     * @param pd      包含末级QUESTION_ID
     * @param headStr 字符串首部，调整wordXml缩进或者样式
     * @param endStr  字符串末尾，调整wordXml换行
     * @return rulesContent 拼接完成的制度依据
     */
    private String assembleRules(PageData pd, String headStr, String endStr) {
        String rulesContent = "";
        List<Map<String, String>> ruleList = inspectionQuestionLedgerMapper.getRuleById(pd);
        if (null != ruleList && !ruleList.isEmpty()) {
            for (int i = 0, len = ruleList.size(); i < len; i++) {
                rulesContent += headStr + (i != 0 ? "及" : "上述问题与") + "《"
                        + ruleList.get(i).get("RULE_FILE_NAME") + "》（"
                        + ruleList.get(i).get("RULE_FILE_NO") + "）“"
                        + ruleList.get(i).get("RULE_FILE_CONTENT")
                        + (i == (len - 1) ? "”的规定不符。" : "”") + endStr;
            }
        }
        return rulesContent;
    }

    /**
     * 根据任务ID获取台账及整改信息
     *
     * @param params
     * @return
     */
    @Override
    public List<Map<String, Object>> getLedgerReformInfoByTaskId(Map<String, Object> params) {
        PageData pd = new PageData();
        List<Map<String, Object>> dataList = inspectionTemporarySumMapper.getLedgerReformInfoByTaskId(params);
        if( dataList !=null && dataList.size() > 0 ){
            dataList.forEach(e->{
                pd.put("LEDGER_ID", e.get("LEDGER_ID"));
                e.put("RULES", assembleRules(pd, "", "\n"));
            });
        }
        return dataList;
    }

} ///:~

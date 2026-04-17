// InspectionRegisterBookServiceImpl.java

package org.inspect.service.impl;

import org.inspect.dao.mapper.inspect.InspectionRegisterBookMapper;
import org.inspect.service.InspectionRegisterBookService;
import org.inspect.util.DateUtil;
import org.inspect.util.NumberTransCN;
import org.inspect.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Created by Samer on 2019/10/16.
 */
@Service
public class InspectionRegisterBookServiceImpl implements InspectionRegisterBookService {

    @Autowired
    private InspectionRegisterBookMapper inspectionRegisterBookMapper;

    /**
     * 组装整改台账
     *
     * @param pd        查询条件
     * @param headStr   格式化前缀
     * @param endStr    格式化后缀
     * @param queryType 判断页面组装或者导出文件
     *                  "html" 页面
     *                  "doc"  导出文件
     * @return
     */
    @Override
    public String assembleReformScheme(PageData pd, String headStr, String endStr, String queryType) {
        String REFORM_INFO = "";
        Map<String, Object> result = new HashMap<String, Object>();
        List<Map<String, String>> dataList_1 = inspectionRegisterBookMapper.getReformLedgerLv1(pd);
        if (dataList_1.isEmpty()) {
            return "请填写整改台账";
        }
        //发现的问题内容拼接
        pd.put("logic", "notEq");
        for (int i = 0, length = dataList_1.size(); i < length; i++) {
            REFORM_INFO += NumberTransCN.transition(i + 1) + "、" + dataList_1.get(i).get("QUESTION_DSCR_1") + endStr;
            pd.put("QUESTION_ID_1", dataList_1.get(i).get("QUESTION_ID_1"));
            List<Map<String, String>> dataList_2 = inspectionRegisterBookMapper.getReformLedgerLv2(pd);
            for (int k = 0, k_len = dataList_2.size(); k < k_len; k++) {
                REFORM_INFO += headStr + "(" + NumberTransCN.transition(k + 1) + ")" + dataList_2.get(k).get("QUESTION_DSCR_2") + endStr;
                pd.put("QUESTION_ID_2", dataList_2.get(k).get("QUESTION_ID_2"));
                List<Map<String, String>> dataList_3 = inspectionRegisterBookMapper.getReformLedgerContent(pd);
                if (!dataList_3.isEmpty()) {
                /*问题描述*/
                    for (int j = 0, len = dataList_3.size(); j < len; j++) {
                        REFORM_INFO += headStr + (len == 1 ? "" : ("（" + (j + 1) + "）"))
                                + "针对" + dataList_3.get(j).get("QUESTION_CONTENT") + "的问题。" + endStr;
                        pd.put("LEDGER_ID", dataList_3.get(j).get("LEDGER_ID"));
                        Map<String, String> reformSchemeInfo = inspectionRegisterBookMapper.getReformScheme(pd);
                        if (reformSchemeInfo != null && !reformSchemeInfo.isEmpty()) {
                            REFORM_INFO += headStr + "整改方案："
                                    + reformSchemeInfo.get("REFORM_SCHEME") + "。" + endStr;
                            REFORM_INFO += headStr + "说明："
                                    + reformSchemeInfo.get("REFORM_SCHEME") + "。" + endStr;
                        }
                    }
                }
            }
        }
        return REFORM_INFO;
    }

    /**
     * 组装检查内容
     *
     * @param params        查询条件
     * @param headStr   格式化前缀
     * @param endStr    格式化后缀
     * @return
     */
    @Override
    public String assembleInspectionContent(PageData params, String headStr, String endStr) {
        String content = "";
        PageData pdSub = new PageData();
        List<Map<String, String>> dataLv1 = inspectionRegisterBookMapper.getInspectionContentLv1(params);
        if (dataLv1 != null && dataLv1.size() > 0) {
            for (int i = 0; i < dataLv1.size(); i++) {
                if ("insert".equals(params.getString("insertFlag"))) {
                    pdSub.put("BOOK_ID", params.getString("BOOK_ID"));
                    pdSub.put("TASK_ID", params.getString("TASK_ID"));
                    pdSub.put("id", dataLv1.get(i).get("id"));
                    pdSub.put("CONTENT_DSCR", dataLv1.get(i).get("CONTENT_DSCR"));
                    pdSub.put("CONTENT_PID", dataLv1.get(i).get("CONTENT_PID"));
                    pdSub.put("SORT", dataLv1.get(i).get("SORT"));
                    pdSub.put("ADD_USERID", params.getString("ADD_USERID"));
                    pdSub.put("ADD_DATE", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD));
                    inspectionRegisterBookMapper.addInspectionContentRegBook(pdSub);
                }
                content +=  /*headStr + */NumberTransCN.transition(i + 1) + "、" + dataLv1.get(i).get("CONTENT_DSCR") + endStr;
                params.put("CONTENT_PID", dataLv1.get(i).get("id"));
                List<Map<String, String>> dataLv2 = inspectionRegisterBookMapper.getInspectionContentLv2(params);
                if (dataLv2 != null && dataLv2.size() > 0) {
                    for (int j = 0, len = dataLv2.size(); j < len; j++) {
                        if ("insert".equals(params.getString("insertFlag"))) {
                            pdSub.put("id", dataLv2.get(j).get("id"));
                            pdSub.put("CONTENT_DSCR", dataLv2.get(j).get("CONTENT_DSCR"));
                            pdSub.put("CONTENT_PID", dataLv2.get(j).get("CONTENT_PID"));
                            pdSub.put("SORT", dataLv2.get(j).get("SORT"));
                            inspectionRegisterBookMapper.addInspectionContentRegBook(pdSub);
                        }
                        content += headStr + (j + 1) + "、"
                                + dataLv2.get(j).get("CONTENT_DSCR") + "。" + endStr;
                    }
                }
            }
        }
        return content;
    }


    /**
     * 获取被查库信息
     *
     * @param params
     * @return
     */
    @Override
    public Map<String, String> getTreInfoByTaskId(PageData params) {
        return inspectionRegisterBookMapper.getTreInfoByTaskId(params);
    }

    /**
     * 获取登记簿信息
     *
     * @param params
     * @return
     */
    @Override
    public Map<String, Object> getRegisterBookInfo(PageData params) {
        return inspectionRegisterBookMapper.getRegisterBookInfo(params);
    }

    /**
     * 例行检查登记簿新增
     *
     * @param params
     */
    @Override
    public void addRegisterBook(PageData params) {
        inspectionRegisterBookMapper.addRegisterBook(params);
    }

    /**
     * 编辑例行检查登记簿
     *
     * @param params
     */
    @Override
    public void updateRegisterBook(PageData params) {
        inspectionRegisterBookMapper.updateRegisterBook(params);
    }

} ///:~

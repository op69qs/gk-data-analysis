// InspectionQuestionLedgerServiceImpl.java

package org.inspect.service.impl;

import org.inspect.dao.mapper.inspect.InspectionQuestionLedgerMapper;
import org.inspect.service.InspectionQuestionLedgerService;
import org.inspect.util.NumberTransCN;
import org.inspect.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 检查任务-问题台账服务接口实现类
 *
 * @author Created by Samer on 2019/10/17.
 */
@Service
public class InspectionQuestionLedgerServiceImpl implements InspectionQuestionLedgerService {

    @Autowired
    private InspectionQuestionLedgerMapper inspectionQuestionLedgerMapper;

    static final String STRING_CSS =
            "&#12288;&#12288;";
    static final String STRING_CSS_END =
            "<w:br/>";

    /*问题台账内容填充*/
    public Map<String, Object> assembleContent(PageData pd) {
        String notOtherContent = "";
        String otherContent = "";
        Map<String, Object> result = new HashMap<String, Object>();
        pd.put("logic", "notEq");
        List<Map<String, String>> notOtherDataList_1 = inspectionQuestionLedgerMapper.getLedgerLv_1ById(pd);
        pd.put("logic", "Eq");
        List<Map<String, String>> OtherDataList_1 = inspectionQuestionLedgerMapper.getLedgerLv_1ById(pd);
        if (notOtherDataList_1.isEmpty() && OtherDataList_1.isEmpty()) {
            result.put("result", "false");
            result.put("msg", "问题清单生成失败，请先增加问题台账信息");
            return result;
        }
        //发现的问题内容拼接
        pd.put("logic", "notEq");
        for (int i = 0, length = notOtherDataList_1.size(); i < length; i++) {
            notOtherContent += STRING_CSS + "(" + NumberTransCN.transition(i + 1) + ")" + notOtherDataList_1.get(i).get("QUESTION_DSCR_1") + STRING_CSS_END;
            pd.put("QUESTION_ID_1", notOtherDataList_1.get(i).get("QUESTION_ID_1"));
            List<Map<String, String>> notOtherDataList_2 = inspectionQuestionLedgerMapper.getLedgerLv_2ById(pd);
            for (int k = 0, k_len = notOtherDataList_2.size(); k < k_len; k++) {
                notOtherContent += STRING_CSS + (k + 1) + "、" + notOtherDataList_2.get(k).get("QUESTION_DSCR_2") + STRING_CSS_END;
                pd.put("QUESTION_ID_2", notOtherDataList_2.get(k).get("QUESTION_ID_2"));
                pd.put("QUESTION_CONTENT", null);
                List<Map<String, String>> notOtherDataListSub = inspectionQuestionLedgerMapper.getQuestionLedgerById(pd);
                if (!notOtherDataListSub.isEmpty()) {
                /*问题描述*/
                    for (int j = 0, len = notOtherDataListSub.size(); j < len; j++) {
                        String content = notOtherDataListSub.get(j).get("QUESTION_CONTENT");
                        if (pd.getString("QUESTION_CONTENT") != null && (pd.getString("QUESTION_CONTENT")).equals(content)) {
                            continue;
                        }
                        notOtherContent += STRING_CSS + (len == 1 ? "" : ("（" + (j + 1) + "）"))
                                + notOtherDataListSub.get(j).get("QUESTION_CONTENT") + "。" + STRING_CSS_END;
                        pd.put("QUESTION_CONTENT", content);
                        /*拼接制度依据*/
                        notOtherContent += assembleContentRules(pd, STRING_CSS, STRING_CSS_END);
                        if ("workingPaper".equals(pd.getString("queryType"))) {
                            notOtherContent += STRING_CSS + "整改方式："
                                    + notOtherDataListSub.get(j).get("QUESTION_OPINIONS") + "。" + STRING_CSS_END;
                        }
                    }
                }
            }
        }
        pd.put("NOT_OTHER_CONTENT", notOtherContent);
        //其他问题内容拼接
        pd.put("logic", "Eq");
        for (int i = 0, length = OtherDataList_1.size(); i < length; i++) {
            //otherContent += "<w:p><w:rPr><w:b w:val=\"on\"/></w:rPr><w:t>" + OtherDataList.get(i).get("QUESTION_DSCR_1") + "</w:t></w:p>";
            otherContent += STRING_CSS + "(" + NumberTransCN.transition(i + 1) + ")" + OtherDataList_1.get(i).get("QUESTION_DSCR_1") + "<w:br/>";
            pd.put("QUESTION_ID_1", OtherDataList_1.get(i).get("QUESTION_ID_1"));
            List<Map<String, String>> OtherDataList_2 = inspectionQuestionLedgerMapper.getLedgerLv_2ById(pd);
            for (int k = 0, k_len = OtherDataList_2.size(); k < k_len; k++) {
                otherContent += STRING_CSS + (k + 1) + "、" + OtherDataList_2.get(k).get("QUESTION_DSCR_2") + STRING_CSS_END;
                pd.put("QUESTION_ID_2", OtherDataList_2.get(k).get("QUESTION_ID_2"));
                List<Map<String, String>> OtherDataListSub = inspectionQuestionLedgerMapper.getQuestionLedgerById(pd);
                if (!OtherDataListSub.isEmpty()) {
                    for (int j = 0, len = OtherDataListSub.size(); j < len; j++) {
                        otherContent += STRING_CSS
                                + (len == 1 ? "" : ("（" + (j + 1) + "）"))
                                + OtherDataListSub.get(j).get("QUESTION_CONTENT")
                                + STRING_CSS_END;
                    }
                }
            }
        }
        pd.put("OTHER_CONTENT", otherContent);
        return result;
    }

    /*问题台账内容填充HTML*/
    public Map<String, Object> assembleContent_html(PageData pd) {
        String notOtherContent = "";
        String otherContent = "";
        Map<String, Object> result = new HashMap<String, Object>();
        pd.put("logic", "notEq");
        List<Map<String, String>> notOtherDataList_1 = inspectionQuestionLedgerMapper.getLedgerLv_1ById(pd);
        pd.put("logic", "Eq");
        List<Map<String, String>> OtherDataList_1 = inspectionQuestionLedgerMapper.getLedgerLv_1ById(pd);
        if (notOtherDataList_1.isEmpty() && OtherDataList_1.isEmpty()) {
            result.put("result", "false");
            result.put("msg", "问题清单生成失败，请先增加问题台账信息");
            return result;
        }
        //发现的问题内容拼接
        pd.put("logic", "notEq");
        for (int i = 0, length = notOtherDataList_1.size(); i < length; i++) {
            notOtherContent += "    " + "(" + NumberTransCN.transition(i + 1) + ")" + notOtherDataList_1.get(i).get("QUESTION_DSCR_1") + "\n";
            pd.put("QUESTION_ID_1", notOtherDataList_1.get(i).get("QUESTION_ID_1"));
            List<Map<String, String>> notOtherDataList_2 = inspectionQuestionLedgerMapper.getLedgerLv_2ById(pd);
            for (int k = 0, k_len = notOtherDataList_2.size(); k < k_len; k++) {
                notOtherContent += "    " + (k + 1) + "、" + notOtherDataList_2.get(k).get("QUESTION_DSCR_2") + "\n";
                pd.put("QUESTION_ID_2", notOtherDataList_2.get(k).get("QUESTION_ID_2"));
                pd.put("QUESTION_CONTENT", null);
                List<Map<String, String>> notOtherDataListSub = inspectionQuestionLedgerMapper.getQuestionLedgerById(pd);
                if (!notOtherDataListSub.isEmpty()) {
                    for (int j = 0, len = notOtherDataListSub.size(); j < len; j++) {
                        String content = notOtherDataListSub.get(j).get("QUESTION_CONTENT");
                        /*if (pd.getString("QUESTION_CONTENT") != null && (pd.getString("QUESTION_CONTENT")).equals(content)) {
                            continue;
                        }*/
                        notOtherContent += "    " + (len == 1 ? "" : ("（" + (j + 1) + "）"))
                                + notOtherDataListSub.get(j).get("QUESTION_CONTENT") + "。\n";
                        pd.put("QUESTION_CONTENT", content);
                        /*拼接制度依据*/
                        notOtherContent += assembleContentRules(pd, "    ", "\n");
                        if ("workingPaper".equals(pd.getString("queryType"))) {
                            notOtherContent += "    " + "整改方式："
                                    + notOtherDataListSub.get(j).get("QUESTION_OPINIONS") + "。\n";
                        }
                    }
                }
            }
        }
        pd.put("NOT_OTHER_CONTENT", notOtherContent);
        //其他问题内容拼接
        pd.put("logic", "Eq");
        for (int i = 0, length = OtherDataList_1.size(); i < length; i++) {
            otherContent += "    " + "(" + NumberTransCN.transition(i + 1) + ")" + OtherDataList_1.get(i).get("QUESTION_DSCR_1") + "\n";
            pd.put("QUESTION_ID_1", OtherDataList_1.get(i).get("QUESTION_ID_1"));
            List<Map<String, String>> OtherDataList_2 = inspectionQuestionLedgerMapper.getLedgerLv_2ById(pd);
            for (int k = 0, k_len = OtherDataList_2.size(); k < k_len; k++) {
                otherContent += "    " + (k + 1) + "、" + OtherDataList_2.get(k).get("QUESTION_DSCR_2") + "\n";
                pd.put("QUESTION_ID_2", OtherDataList_2.get(k).get("QUESTION_ID_2"));
                List<Map<String, String>> OtherDataListSub = inspectionQuestionLedgerMapper.getQuestionLedgerById(pd);
                if (!OtherDataListSub.isEmpty()) {
                    for (int j = 0, len = OtherDataListSub.size(); j < len; j++) {
                        otherContent += "    "
                                + (len == 1 ? "" : ("（" + (j + 1) + "）"))
                                + OtherDataListSub.get(j).get("QUESTION_CONTENT")
                                + "。\n";
                    }
                }
            }
        }
        pd.put("OTHER_CONTENT", otherContent);
        return result;
    }

    /**
     * 问题台账问题描述与制度依据拼接
     *
     * @param pd
     * @return
     */
    @Override
    public List<Map<String, Object>> assembleContentAndRule(PageData pd) {
        List<Map<String, Object>> contentList = inspectionQuestionLedgerMapper.getQuestionContentInfo(pd);
        if (contentList != null && contentList.size() > 0) {
            contentList.forEach(e -> {
                pd.put("LEDGER_ID", e.get("LEDGER_ID"));
                e.put("QUESTION_CONTENT",
                        e.get("QUESTION_CONTENT") + "\n" + assembleContentRules_debt(pd, "", "\n"));
            });
        }
        return contentList;
    }


    /**
     * 制度依据组装
     *
     * @param pd      包含末级QUESTION_ID
     * @param headStr 字符串首部，调整wordXml缩进或者样式
     * @param endStr  字符串末尾，调整wordXml换行
     * @return rulesContent 拼接完成的制度依据
     */
    private String assembleContentRules_debt(PageData pd, String headStr, String endStr) {
        String rulesContent = "";
        List<Map<String, String>> ruleList = inspectionQuestionLedgerMapper.getRuleById(pd);
        if (null != ruleList && !ruleList.isEmpty()) {
            for (int i = 0, len = ruleList.size(); i < len; i++) {
                rulesContent += headStr + "    " + (i != 0 ? "及" : "上述问题与") + "《"
                        + ruleList.get(i).get("RULE_FILE_NAME") + "》（"
                        + ruleList.get(i).get("RULE_FILE_NO") + "）“"
                        + ruleList.get(i).get("RULE_FILE_CONTENT")
                        + (i == (len - 1) ? "”的规定不符。" : ("”" + endStr));
            }
        }
        return rulesContent;
    }

    /**
     * 制度依据组装
     *
     * @param pd      包含末级QUESTION_ID
     * @param headStr 字符串首部，调整wordXml缩进或者样式
     * @param endStr  字符串末尾，调整wordXml换行
     * @return rulesContent 拼接完成的制度依据
     */
    private String assembleContentRules(PageData pd, String headStr, String endStr) {
        String rulesContent = "";
        //查询二级问题分类下同一问题描述的所有台账ID
        List<Map<String, String>> contentList = inspectionQuestionLedgerMapper.getQuestionLedgerById(pd);
        for (int l = 0, cl = contentList.size(); l < cl; l++) {
            rulesContent += headStr + (l != 0 ? "及" : "上述问题与") + "《";
            pd.put("LEDGER_ID", contentList.get(l).get("LEDGER_ID"));
            List<Map<String, String>> ruleList = inspectionQuestionLedgerMapper.getRuleById(pd);
            if (null != ruleList && !ruleList.isEmpty()) {
                for (int i = 0, len = ruleList.size(); i < len; i++) {
                    rulesContent += ruleList.get(i).get("RULE_FILE_NAME") + "》（"
                            + ruleList.get(i).get("RULE_FILE_NO") + "）“"
                            + ruleList.get(i).get("RULE_FILE_CONTENT");
                }
            }
            rulesContent += (l == (cl - 1) ? "”的规定不符。" : "”") + endStr;
        }
        return rulesContent;
    }

    /**
     * 问题台账问题类型树形
     *
     * @param params
     * @return
     */
    @Override
    public List<Map<String, String>> getQuestionBankTreeForQuestionLedger(Map<String, Object> params) {
        return inspectionQuestionLedgerMapper.getQuestionBankTreeForQuestionLedger(params);
    }

    /**
     * 根据任务ID获取检查分类
     *
     * @param params
     * @return
     */
    @Override
    public String getQuestionTypeByTaskId(Map<String, Object> params) {
        return inspectionQuestionLedgerMapper.getQuestionTypeByTaskId(params);
    }

    /**
     * 根据用户ID任务ID获取问题台账
     *
     * @param params params.TASK_ID 当前检查任务ID
     *               params.ADD_USERID 添加人ID
     * @return
     */
    @Override
    public List<Map<String, String>> getQuestionLedgerByUserIdTaskID(Map<String, Object> params) {
        return inspectionQuestionLedgerMapper.getQuestionLedgerByUserIdTaskID(params);
    }

    /**
     * 根据检查任务ID获取该台账添加人
     *
     * @param params params.TASK_ID 检查任务ID
     * @return
     */
    @Override
    public List<Map<String, String>> getLedgerAddUserByTaskId(Map<String, Object> params) {
        return inspectionQuestionLedgerMapper.getLedgerAddUserByTaskId(params);
    }

    /**
     * 根据台账ID获取台账信息
     *
     * @param params params.LEDGER_ID 问题台账ID
     * @return
     */
    @Override
    public List<Map<String, String>> getQuestionLedgerByLedgerID(Map<String, Object> params) {
        return inspectionQuestionLedgerMapper.getQuestionLedgerByLedgerID(params);
    }

    /**
     * 问题台账新增
     *
     * @param params
     * @throws SQLException
     */
    @Override
    public void addQuestionLedger(Map<String, Object> params) {
        inspectionQuestionLedgerMapper.addQuestionLedger(params);
    }

    /**
     * 根据问题台账ID编辑
     *
     * @param params
     */
    @Override
    public void editQuestionLedgerByLedgerID(Map<String, Object> params) {
        inspectionQuestionLedgerMapper.editQuestionLedgerByLedgerID(params);
    }

    /**
     * 根据台账ID删除
     *
     * @param params
     */
    @Override
    public void delQuestionLedgerByLedgerId(Map<String, Object> params) {
        inspectionQuestionLedgerMapper.delQuestionLedgerByLedgerId(params);
    }

    /**
     * 新增问题台账制度依据
     *
     * @param params
     */
    @Override
    public void addQuestionLedgerRule(Map<String, Object> params) {
        inspectionQuestionLedgerMapper.addQuestionLedgerRule(params);
    }

    /**
     * 编辑问题台账制度依据
     *
     * @param params
     */
    @Override
    public void editQuestionLedgerRule(Map<String, Object> params) {
        inspectionQuestionLedgerMapper.editQuestionLedgerRule(params);
    }

    /**
     * 删除问题台账制度依据
     *
     * @param params
     */
    @Override
    public void delQuestionLedgerRule(Map<String, Object> params) {
        inspectionQuestionLedgerMapper.delQuestionLedgerRule(params);
    }

    /**
     * 根据当前任务ID获取末级台账问题清单制度信息
     *
     * @return
     */
    @Override
    public List<Map<String, String>> getRuleById(Map<String, Object> params) {
        return inspectionQuestionLedgerMapper.getRuleById(params);
    }

    /**
     * 获取整改意见
     *
     * @param params
     */
    @Override
    public List<Map<String, String>> getQuestionOpinionById(Map<String, Object> params) {
        return inspectionQuestionLedgerMapper.getQuestionOpinionById(params);
    }

} ///:~

package org.inspect.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.inspect.dao.mapper.inspect.*;
import org.inspect.service.InspectionGroupService;
import org.inspect.service.InspectionProcService;
import org.inspect.util.DateUtil;
import org.inspect.util.PageData;
import org.inspect.util.UuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class InspectionProcServiceImpl implements InspectionProcService {

    @Autowired
    private InspectionProcMapper inspectionProcMapper;
    @Autowired
    private InspectionProcSubMapper inspectionProcSubMapper;
    @Autowired
    private InspectionTaskTemplateMapper inspectionTaskTemplateMapper;
    @Autowired
    private InspectionBorrowMapper inspectionBorrowMapper;
    @Autowired
    private InspectionUserMapper inspectionUserMapper;
    @Autowired
    private InspectionTaskMapper inspectionTaskMapper;

    @Override
    public List<Map<String, Object>> getInspectionProcData(PageData pd) {
        return inspectionProcMapper.getInspectionProcData(pd);
    }

    @Override
    public void addInspectionProc(PageData pd) {
        //获取主流程模版参数
        String INSPECTION_TASK_TYPE = pd.getString("INSPECTION_TASK_TYPE");
        //获取主流程模版
        List<Map<String, String>> data = inspectionTaskTemplateMapper.getTaskProcessByTypeId(INSPECTION_TASK_TYPE);
        if (null == data || data.size() <= 0) {
            return;
        }
        if (INSPECTION_TASK_TYPE.equals("002") || INSPECTION_TASK_TYPE.equals("003") || INSPECTION_TASK_TYPE.equals("004")) {
            for (int i = 0; i < data.size(); i++) {
                pd.put("ID", UuidUtil.get32UUID());
                pd.put("INSPECTION_PROCESS_ID", data.get(i).get("TEMP_PROC_ID"));
                pd.put("INSPECTION_PROCESS_NAME", data.get(i).get("TEMP_PROC_NAME"));
                if (i == 0) {
                    pd.put("INSPECTION_PROCESS_SIGN", '0');
                    pd.put("IS_ACTIVE", "1");
                    pd.put("FINISH_TIME", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
                } else if (i == 1) {
                    pd.put("INSPECTION_PROCESS_SIGN", '1');
                    pd.put("IS_ACTIVE", "0");
                } else {
                    pd.put("INSPECTION_PROCESS_SIGN", '1');
                    pd.put("IS_ACTIVE", "1");
                }
                inspectionProcMapper.addInspectionProc(pd);
                //获取子流程模版参数
                String TEMP_PROC_ID = data.get(i).get("TEMP_PROC_ID").toString();
                //获取子流程模版
                List<Map<String, String>> data_sub = inspectionTaskTemplateMapper.getSubProcessByProcId(TEMP_PROC_ID);
                if (null == data_sub || data_sub.size() <= 0) {
                    continue;
                }
                PageData pdSub = new PageData();
                for (int j = 0; j < data_sub.size(); j++) {
                    pdSub.put("ID", UuidUtil.get32UUID());
                    pdSub.put("INSPECTION_PROCESS_SUB_ID", data_sub.get(j).get("TEMP_PROC_SUB_ID"));
                    pdSub.put("PROCESS_ID", pd.getString("ID"));
                    pdSub.put("INSPECTION_TASK_ID", pd.getString("INSPECTION_TASK_ID"));
                    pdSub.put("INSPECTION_PROCESS_SUB_NAME", data_sub.get(j).get("TEMP_PROC_SUB_NAME"));
                    if (i == 0 && j == 0) {
                        pdSub.put("IS_ACTIVE", "0");
                        pdSub.put("INSPECTION_PROCESS_SUB_SIGN", "0");
                        pdSub.put("FINISH_TIME", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
                    } else if (i == 1 && j == 0) {
                        pdSub.put("IS_ACTIVE", "0");
                        pdSub.put("INSPECTION_PROCESS_SUB_SIGN", "1");
                    } else {
                        pdSub.put("IS_ACTIVE", "1");
                        pdSub.put("INSPECTION_PROCESS_SUB_SIGN", "1");
                    }
                    pdSub.put("FOR_RECORD", data_sub.get(j).get("FOR_RECORD"));
                    pdSub.put("IS_SKIP", data_sub.get(j).get("IS_SKIP"));
                    pdSub.put("TITLE", data_sub.get(j).get("TITLE"));
                    inspectionProcSubMapper.addInspectionProcSub(pdSub);
                }
            }
        } else if ("008".equals(INSPECTION_TASK_TYPE)) {
            for (int i = 0; i < data.size(); i++) {
                pd.put("ID", UuidUtil.get32UUID());
                pd.put("INSPECTION_PROCESS_ID", data.get(i).get("TEMP_PROC_ID"));
                pd.put("INSPECTION_PROCESS_NAME", data.get(i).get("TEMP_PROC_NAME"));
                pd.put("INSPECTION_PROCESS_SIGN", '1');
                pd.put("IS_ACTIVE", "1");
                inspectionProcMapper.addInspectionProc(pd);
                //获取子流程模版参数
                String TEMP_PROC_ID = data.get(i).get("TEMP_PROC_ID").toString();
                //获取子流程模版
                List<Map<String, String>> data_sub = inspectionTaskTemplateMapper.getSubProcessByProcId(TEMP_PROC_ID);
                if (null == data_sub || data_sub.size() <= 0) {
                    continue;
                }
                PageData pdSub = new PageData();
                for (int j = 0; j < data_sub.size(); j++) {
                    pdSub.put("ID", UuidUtil.get32UUID());
                    pdSub.put("INSPECTION_PROCESS_SUB_ID", data_sub.get(j).get("TEMP_PROC_SUB_ID"));
                    pdSub.put("PROCESS_ID", pd.getString("ID"));
                    pdSub.put("INSPECTION_TASK_ID", pd.getString("INSPECTION_TASK_ID"));
                    pdSub.put("INSPECTION_PROCESS_SUB_NAME", data_sub.get(j).get("TEMP_PROC_SUB_NAME"));
                    String IS_ACTIVE = "0080101".equals(data_sub.get(j).get("TEMP_PROC_SUB_ID")) || "0080102".equals(data_sub.get(j).get("TEMP_PROC_SUB_ID")) ? "0" : "1";
                    pdSub.put("IS_ACTIVE", IS_ACTIVE);
                    pdSub.put("INSPECTION_PROCESS_SUB_SIGN", "1");
                    pdSub.put("FOR_RECORD", data_sub.get(j).get("FOR_RECORD"));
                    pdSub.put("IS_SKIP", data_sub.get(j).get("IS_SKIP"));
                    pdSub.put("TITLE", data_sub.get(j).get("TITLE"));
                    inspectionProcSubMapper.addInspectionProcSub(pdSub);
                }
            }
        } else if ("009".equals(INSPECTION_TASK_TYPE)) {
            for (int i = 0; i < data.size(); i++) {
                pd.put("ID", UuidUtil.get32UUID());
                pd.put("INSPECTION_PROCESS_ID", data.get(i).get("TEMP_PROC_ID"));
                pd.put("INSPECTION_PROCESS_NAME", data.get(i).get("TEMP_PROC_NAME"));
                pd.put("INSPECTION_PROCESS_SIGN", '1');
                //pd.put("IS_ACTIVE", "1");

                //激活前期准备，当前任务为计划创建任务时则不激活
                String PROC_ID = data.get(i).get("TEMP_PROC_ID");
                String procIsActive = "1";
                if ("".equals(pd.getString("INSPECTION_PLAN_ID"))
                        || null == pd.getString("INSPECTION_PLAN_ID")) {
                    procIsActive = PROC_ID.endsWith("01") ? "0" : "1";
                }
                pd.put("IS_ACTIVE", procIsActive);

                inspectionProcMapper.addInspectionProc(pd);
                //获取子流程模版参数
                String TEMP_PROC_ID = data.get(i).get("TEMP_PROC_ID").toString();
                //获取子流程模版
                List<Map<String, String>> data_sub = inspectionTaskTemplateMapper.getSubProcessByProcId(TEMP_PROC_ID);
                if (null == data_sub || data_sub.size() <= 0) {
                    continue;
                }
                PageData pdSub = new PageData();
                for (int j = 0; j < data_sub.size(); j++) {
                    pdSub.put("ID", UuidUtil.get32UUID());
                    pdSub.put("INSPECTION_PROCESS_SUB_ID", data_sub.get(j).get("TEMP_PROC_SUB_ID"));
                    pdSub.put("PROCESS_ID", pd.getString("ID"));
                    pdSub.put("INSPECTION_TASK_ID", pd.getString("INSPECTION_TASK_ID"));
                    pdSub.put("INSPECTION_PROCESS_SUB_NAME", data_sub.get(j).get("TEMP_PROC_SUB_NAME"));
                    String IS_ACTIVE = "0090101".equals(data_sub.get(j).get("TEMP_PROC_SUB_ID")) || "0090102".equals(data_sub.get(j).get("TEMP_PROC_SUB_ID")) ? "0" : "1";
                    /*pdSub.put("IS_ACTIVE", IS_ACTIVE);
                    String INSPECTION_PROCESS_SUB_SIGN = "0090101".equals(data_sub.get(j).get("TEMP_PROC_SUB_ID")) || "0090102".equals(data_sub.get(j).get("TEMP_PROC_SUB_ID")) ? "0" : "1";
                    pdSub.put("INSPECTION_PROCESS_SUB_SIGN", INSPECTION_PROCESS_SUB_SIGN);
                    if ("0090101".equals(data_sub.get(j).get("TEMP_PROC_SUB_ID")) || "0090102".equals(data_sub.get(j).get("TEMP_PROC_SUB_ID"))) {
                        pdSub.put("FINISH_TIME", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
                    }*/
                    String FINISH_TIME = DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS);
                    String PROC_SUB_ID = data_sub.get(j).get("TEMP_PROC_SUB_ID");
                    String procSubIsActive = (PROC_SUB_ID.endsWith("0101") || PROC_SUB_ID.endsWith("0102")) ? "0" : "1";
                    String isFinish = PROC_SUB_ID.endsWith("0101") ? "0" : "1";
                    String finishTime = PROC_SUB_ID.endsWith("0101") ? FINISH_TIME : null;
                    pdSub.put("IS_ACTIVE", procSubIsActive);
                    pdSub.put("INSPECTION_PROCESS_SUB_SIGN", isFinish);
                    pdSub.put("FINISH_TIME", finishTime);
                    pdSub.put("FOR_RECORD", data_sub.get(j).get("FOR_RECORD"));
                    pdSub.put("IS_SKIP", data_sub.get(j).get("IS_SKIP"));
                    pdSub.put("TITLE", data_sub.get(j).get("TITLE"));
                    inspectionProcSubMapper.addInspectionProcSub(pdSub);
                }
            }
        } else {
            for (int i = 0; i < data.size(); i++) {
                pd.put("ID", UuidUtil.get32UUID());
                pd.put("INSPECTION_PROCESS_ID", data.get(i).get("TEMP_PROC_ID"));
                pd.put("INSPECTION_PROCESS_NAME", data.get(i).get("TEMP_PROC_NAME"));
                pd.put("INSPECTION_PROCESS_SIGN", '1');
                //pd.put("IS_ACTIVE","1");

                //激活前期准备，当前任务为计划创建任务时则不激活
                String PROC_ID = data.get(i).get("TEMP_PROC_ID");
                String procIsActive = "1";
                if ("".equals(pd.getString("INSPECTION_PLAN_ID"))
                        || null == pd.getString("INSPECTION_PLAN_ID")) {
                    procIsActive = PROC_ID.endsWith("01") ? "0" : "1";
                }
                pd.put("IS_ACTIVE", procIsActive);

                inspectionProcMapper.addInspectionProc(pd);
                //获取子流程模版参数
                String TEMP_PROC_ID = data.get(i).get("TEMP_PROC_ID").toString();
                //获取子流程模版
                List<Map<String, String>> data_sub = inspectionTaskTemplateMapper.getSubProcessByProcId(TEMP_PROC_ID);
                if (null == data_sub || data_sub.size() <= 0) {
                    continue;
                }
                PageData pdSub = new PageData();
                for (int j = 0; j < data_sub.size(); j++) {
                    pdSub.put("ID", UuidUtil.get32UUID());
                    pdSub.put("INSPECTION_PROCESS_SUB_ID", data_sub.get(j).get("TEMP_PROC_SUB_ID"));
                    pdSub.put("PROCESS_ID", pd.getString("ID"));
                    pdSub.put("INSPECTION_TASK_ID", pd.getString("INSPECTION_TASK_ID"));
                    pdSub.put("INSPECTION_PROCESS_SUB_NAME", data_sub.get(j).get("TEMP_PROC_SUB_NAME"));
                    //pdSub.put("IS_ACTIVE","1");
                    //pdSub.put("INSPECTION_PROCESS_SUB_SIGN","1");
                    String FINISH_TIME = DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS);
                    String PROC_SUB_ID = data_sub.get(j).get("TEMP_PROC_SUB_ID");
                    String procSubIsActive = (PROC_SUB_ID.endsWith("0101") || PROC_SUB_ID.endsWith("0102")) ? "0" : "1";
                    String isFinish = PROC_SUB_ID.endsWith("0101") ? "0" : "1";
                    String finishTime = PROC_SUB_ID.endsWith("0101") ? FINISH_TIME : null;
                    pdSub.put("IS_ACTIVE", procSubIsActive);
                    pdSub.put("INSPECTION_PROCESS_SUB_SIGN", isFinish);
                    pdSub.put("FINISH_TIME", finishTime);
                    pdSub.put("FOR_RECORD", data_sub.get(j).get("FOR_RECORD"));
                    pdSub.put("IS_SKIP", data_sub.get(j).get("IS_SKIP"));
                    pdSub.put("TITLE", data_sub.get(j).get("TITLE"));
                    inspectionProcSubMapper.addInspectionProcSub(pdSub);
                }
            }
        }
    }

    @Override
    public void editInspectionProc(PageData pd) {
        inspectionProcMapper.editInspectionProc(pd);
    }

    @Override
    public void editProcActive(PageData pd) {
        List<Map<String, Object>> procMap = getInspectionProcData(pd);
        if (null != procMap && !procMap.isEmpty()) {
            for (Map<String, Object> proc : procMap) {
                if (!proc.get("IS_ACTIVE").toString().equals("0")) {
                    PageData procPd = new PageData();
                    procPd.put("IS_ACTIVE", "0");
                    procPd.put("ID", procMap.get(0).get("ID"));
                    editInspectionProc(procPd);
                }
            }
            pd.put("PROCESS_ID", procMap.get(0).get("ID"));
            List<Map<String, Object>> procSubMap = inspectionProcSubMapper.getInspectionProcSubData(pd);
            if (null != procSubMap && !procSubMap.isEmpty()) {
                PageData subPd = new PageData();
                subPd.put("IS_ACTIVE", "0");
                for (Map<String, Object> procSub : procSubMap) {
                    subPd.put("ID", procSub.get("ID"));
                    // 重置所有【检查任务】小流程完成时间
                    if (procSub.get("INSPECTION_PROCESS_SUB_ID").toString().endsWith("0101")) {
                        subPd.put("INSPECTION_PROCESS_SUB_SIGN", "0");
                        subPd.put("FINISH_TIME", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
                        inspectionProcSubMapper.editInspectionProcSub(subPd);
                    }
                }
            }
        }
    }

    @Override
    public void editProcInfo(PageData pd) {
        String duties = inspectionUserMapper.getDuties(pd);
        pd.put("INSPECTION_GROUP_DUTIES", "2");
        List<Map<String, Object>> userMap = inspectionUserMapper.getInspectionUserData(pd);
        if (null != duties && !duties.equals("")) {
            if (duties.indexOf("1") > -1 && duties.indexOf("2") > -1 && duties.indexOf("3") > -1) {
                List<Map<String, Object>> procMap = getInspectionProcData(pd);
                if (null != procMap && !procMap.isEmpty()) {
                    pd.put("PROCESS_ID", procMap.get(0).get("ID"));
                    PageData procPd = new PageData();
                    procPd.put("IS_ACTIVE", "0");
                    procPd.put("ID", procMap.get(0).get("ID"));
                    editInspectionProc(procPd);
                    List<Map<String, Object>> procSubMap = inspectionProcSubMapper.getInspectionProcSubData(pd);
                    if (null != procSubMap && !procSubMap.isEmpty()) {
                        PageData subPd = new PageData();
                        subPd.put("IS_ACTIVE", "0");
                        for (Map<String, Object> procSub : procSubMap) {
                            subPd.put("ID", procSub.get("ID"));
                            if (       procSub.get("INSPECTION_PROCESS_SUB_ID").toString().equals("0050101")
                                    || procSub.get("INSPECTION_PROCESS_SUB_ID").toString().equals("0050102")
                                    || procSub.get("INSPECTION_PROCESS_SUB_ID").toString().equals("0010101")
                                    || procSub.get("INSPECTION_PROCESS_SUB_ID").toString().equals("0010102")
                                    || procSub.get("INSPECTION_PROCESS_SUB_ID").toString().equals("0020101")
                                    || procSub.get("INSPECTION_PROCESS_SUB_ID").toString().equals("0030101")
                                    || procSub.get("INSPECTION_PROCESS_SUB_ID").toString().equals("0040101")
                                    || procSub.get("INSPECTION_PROCESS_SUB_ID").toString().equals("0060101")
                                    || procSub.get("INSPECTION_PROCESS_SUB_ID").toString().equals("0060102")
                                    || procSub.get("INSPECTION_PROCESS_SUB_ID").toString().equals("0050104")
                                    || procSub.get("INSPECTION_PROCESS_SUB_ID").toString().equals("0090101")
                                    || procSub.get("INSPECTION_PROCESS_SUB_ID").toString().equals("0090102")
                                    || procSub.get("INSPECTION_PROCESS_SUB_ID").toString().equals("0070101")
                                    || procSub.get("INSPECTION_PROCESS_SUB_ID").toString().equals("0070102")
                                    ) {
                                subPd.put("INSPECTION_PROCESS_SUB_SIGN", "0");
                                subPd.put("FINISH_TIME", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
                                inspectionProcSubMapper.editInspectionProcSub(subPd);
                            } else {
                                subPd.put("INSPECTION_PROCESS_SUB_SIGN", "1");
                                subPd.put("FINISH_TIME", "");
                                inspectionProcSubMapper.editInspectionProcSub(subPd);
                            }
                            if (procSub.get("INSPECTION_PROCESS_SUB_ID").toString().equals("0010104")
                                    || procSub.get("INSPECTION_PROCESS_SUB_ID").toString().equals("0050106")
                                    || procSub.get("INSPECTION_PROCESS_SUB_ID").toString().equals("0060104")) {
                                PageData borrowPd = new PageData();
                                borrowPd.put("TASK_ID", pd.getString("INSPECTION_TASK_ID"));
                                borrowPd.put("PROC_ID", procSub.get("ID"));
                                borrowPd.put("title", procSub.get("TITLE"));
                                List<Map<String, Object>> borrowMap = inspectionBorrowMapper.getInspectionBorrowData(borrowPd);
                                if (null == borrowMap || borrowMap.isEmpty()) {
                                    PageData taskPd = new PageData();
                                    taskPd.put("INSPECTION_TASK_ID", pd.getString("INSPECTION_TASK_ID"));
                                    List<Map<String, Object>> taskMap = inspectionTaskMapper.getInspectionTaskData(taskPd);
                                    if (null != taskMap && !taskMap.isEmpty()) {
                                        borrowPd.put("type", taskMap.get(0).get("INSPECTION_TASK_TYPE"));
                                        borrowPd.put("inspected_guoku", taskMap.get(0).get("INSPECTED_GUOKU_DSCR"));
                                    }
                                    List<Map<String, Object>> borrowTmps = inspectionBorrowMapper.getInspectionBorrowTemp(borrowPd);
                                    if (null != borrowTmps && !borrowTmps.isEmpty()) {
                                        for (Map<String, Object> borrowTmp : borrowTmps) {
                                            borrowPd.put("id", UuidUtil.get32UUID());
                                            borrowPd.putAll(borrowTmp);
                                            if (null != userMap && !userMap.isEmpty()) {
                                                borrowPd.put("borrow_user", userMap.get(0).get("USER_NAME"));
                                            }
                                            inspectionBorrowMapper.addInspectionBorrow(borrowPd);
                                        }
                                    }
                                } else {
                                    if (null != userMap && !userMap.isEmpty()) {
                                        borrowPd.put("borrow_user", userMap.get(0).get("USER_NAME"));
                                    }
                                    inspectionBorrowMapper.editInspectionBorrow(borrowPd);
                                    subPd.put("INSPECTION_PROCESS_SUB_SIGN", "1");
                                    subPd.put("FINISH_TIME", "");
                                    inspectionProcSubMapper.editInspectionProcSub(subPd);
                                }
                            }
                        }
                    }
                }
            }
        }
    }


}

// InspectionSelfSummaryController.java

package org.inspect.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.inspect.BaseController;
import org.inspect.service.InspectionProcessControlService;
import org.inspect.service.InspectionSelfLedgerService;
import org.inspect.service.InspectionSelfSummaryService;
import org.inspect.util.CreateExcel_2;
import org.inspect.util.DateUtil;
import org.inspect.util.FileDownload;
import org.inspect.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自查汇总表
 *
 * @author Created by Samer on 2019/10/17.
 */
@Slf4j
@RestController
@Api(tags = "自查汇总表")
@RequestMapping(value = "/inspectionSelfSummary", produces = MediaType.APPLICATION_JSON_VALUE)
public class InspectionSelfSummaryController extends BaseController {

    @Value("${TEMPLATE_FILE_PATH}")
    private String saveDir;

    @Autowired
    private InspectionProcessControlService inspectionProcessControlService;

    @Autowired
    private InspectionSelfLedgerService inspectionSelfLedgerService;

    @Autowired
    private InspectionSelfSummaryService inspectionSelfSummaryService;

    /**
     * 查询当前任务汇总表信息
     *
     * @return
     */
    @ApiOperation(value = "查询当前任务汇总表信息")
    @PostMapping(value = "/getCurSelfSumInfo")
    public Map<String, Object> getCurSelfSumInfo(
            @ApiParam(value = "当前任务ID：TASK_ID,\n" +
                    "当前任务所处大流程ID：PROC_ID,\n" +
                    "当前任务所处小流程ID：PROC_SUB_ID,\n" +
                    "当前用户所属国库：guoku_id"
            )
            @RequestBody(required = false) JSONObject param
    ) {
        PageData pd = this.getPageData(param);
        PageData subPd = new PageData();
        Map<String, String> taskData = inspectionSelfLedgerService.getTaskInfoById(pd);
        subPd.put("guoku_id", taskData.get("INSPECTED_GUOKU_ID"));
        Map<String, String> guokuData = inspectionSelfLedgerService.getGuokuInfo(subPd);
        pd.put("INSPECTION_TASK_YEAR", taskData.get("INSPECTION_TASK_YEAR"));
        if ("4".equals(String.valueOf(guokuData.get("level"))) || "5".equals(String.valueOf(guokuData.get("level")))) {
            pd.put("COUNTY", taskData.get("INSPECTED_GUOKU_ID"));
        }
        if ("3".equals(String.valueOf(guokuData.get("level")))) {
            pd.put("CITY", taskData.get("INSPECTED_GUOKU_ID"));
        }
        if ("2".equals(String.valueOf(guokuData.get("level")))) {
            pd.put("PROVINCE", taskData.get("INSPECTED_GUOKU_ID"));
        }
        List<Map<String, Object>> dataList = inspectionSelfSummaryService.getCurSelfSumInfo(pd);
        Map<String, Object> res = new HashMap<>();
        res.put("result", "success");
        res.put("rows", dataList);
        return res;
    }

    /**
     * 提交/退回汇总表信息
     *
     * @return
     */
    @ApiOperation(value = "提交/退回汇总表信息")
    @PostMapping(value = "/submitCurSelfSumInfo")
    public Map<String, Object> submitCurSelfSumInfo(
            @ApiParam(value = "当前任务ID：TASK_ID,\n" +
                    "当前任务所处大流程ID：PROC_ID,\n" +
                    "当前任务所处小流程ID：PROC_SUB_ID,\n" +
                    "当前用户所属国库：guoku_id" +
                    "问题台账ID：LEDGER_ID"
                    +
                    "提交状态：STATE（2提交，3退回）" +
                    "锁定状态：IS_LOCK（提交时传值0，退回时传值1）"
            )
            @RequestBody(required = false) JSONObject param
    ) {
        PageData pd = this.getPageData(param);
        String guokuId = pd.getString("guoku_id");
        String timeNow = DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS);
        String state = pd.getString("STATE");
        String IS_LOCK = "2".equals(state) ? "0" : "1";
        List<Map<String, String>> pdList = (List<Map<String, String>>) JSONArray.parse(pd.getString("data_list"));
        for (Map<String, String> pdSub : pdList) {
            String taskId = pdSub.get("TASK_ID");
            if ("2".equals(state)) {
                //提交
                Map<String, String> guokuData = inspectionSelfLedgerService.getGuokuInfo(pd);
                if ("4".equals(String.valueOf(guokuData.get("level"))) || "5".equals(String.valueOf(guokuData.get("level")))) {
                    pdSub.put("COUNTY", pdSub.get("guoku_id"));
                    pdSub.put("COUNTY_STATE", state);
                    if (guokuId.startsWith("2401")) {
                        pdSub.put("CITY_STATE", state);
                    }
                }
                if ("3".equals(String.valueOf(guokuData.get("level")))) {
                    pdSub.put("CITY", pdSub.get("guoku_id"));
                    pdSub.put("CITY_STATE", state);
                }
                if (("2".equals(String.valueOf(guokuData.get("level"))))) {
                    pdSub.put("PROVINCE", pdSub.get("guoku_id"));
                    pdSub.put("PROVINCE_STATE", state);
                }
            }
            if ("3".equals(state)) {
                //退回
                PageData taskPD = new PageData();
                taskPD.put("TASK_ID", taskId);
                Map<String, String> taskData = inspectionSelfLedgerService.getTaskInfoById(taskPD);
                String inspectedGuokuId = taskData.get("INSPECTED_GUOKU_ID");
                taskPD.put("guoku_id", inspectedGuokuId);
                Map<String, String> guokuData = inspectionSelfLedgerService.getGuokuInfo(taskPD);
                if ("4".equals(String.valueOf(guokuData.get("level"))) || "5".equals(String.valueOf(guokuData.get("level")))) {
                    pdSub.put("COUNTY_STATE", "3");
                    pdSub.put("CITY_STATE", "1");
                    pdSub.put("PROVINCE_STATE", "1");
                }
                if ("3".equals(String.valueOf(guokuData.get("level")))) {
                    pdSub.put("COUNTY_STATE", "2");
                    pdSub.put("CITY_STATE", "3");
                    pdSub.put("PROVINCE_STATE", "1");
                }
                if (("2".equals(String.valueOf(guokuData.get("level"))))) {
                    pdSub.put("COUNTY_STATE", "2");
                    pdSub.put("CITY_STATE", "2");
                    pdSub.put("PROVINCE_STATE", "3");
                }
            }
            pdSub.put("IS_LOCK", IS_LOCK);
            String BACK_REASON = "2".equals(state) ? "" :
                    (JSONObject.parseObject(
                            JSONArray.parseArray(pd.get("data_list").toString()).get(0).toString(), new TypeReference<Map<String, String>>() {
                            }
                    )
                    ).get("BACK_REASON");
            pdSub.put("BACK_REASON", BACK_REASON);
            inspectionSelfSummaryService.editSelfLedgerByLedgerID(pdSub);
        }
        Map<String, Object> res = new HashMap<>();
        res.put("result", "success");
        /*完成当前流程，尝试激活后续流程。*/
        int index = inspectionProcessControlService.getProStateAllById(pd);
        if (index != 0) {
            if ("2".equals(pd.getString("STATE"))) {
                pd.put("FINISH_TIME", timeNow);
                inspectionProcessControlService.finishCurSubProcessById(pd);
                if (inspectionProcessControlService.activateFollowProc(pd)) {
                    res.put("msg", "提交成功");
                    /*res.put("msg", "提交成功" + inspectionProcessControlService.getProcDescById(pd) + "res.put(\"msg\", \"提交成功\"流程结束，后续流程已激活");*/
                }
            }
        } else {
            String msg = "2".equals(pd.getString("STATE")) ? "提交成功" : "退回成功";
            res.put("msg", msg);
        }
        return res;
    }

    /**
     * 下载汇总表
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "下载汇总表")
    @GetMapping(value = "/downLoadSelfSumTable")
    public void downLoadSelfSumTable(
            @ApiParam(value = "当前任务ID：TASK_ID,\n" +
                    "当前用户所属国库：guoku_id"
            )
            @RequestBody(required = false) JSONObject param,
            HttpServletResponse response
    ) throws Exception {
        PageData pd = this.getPageData(param);
        Map<String, String> taskData = inspectionSelfLedgerService.getTaskInfoById(pd);
        String fileName = taskData.get("INSPECTION_TASK_YEAR") + "年国库业务管理自查问题清单.xls";
        String title = taskData.get("INSPECTION_TASK_YEAR") + "年国库业务管理自查问题清单";
        Map<String, String> guokuData = inspectionSelfLedgerService.getGuokuInfo(pd);
        pd.put("INSPECTION_TASK_YEAR", taskData.get("INSPECTION_TASK_YEAR"));
        if ("4".equals(String.valueOf(guokuData.get("level"))) || "5".equals(String.valueOf(guokuData.get("level")))) {
            pd.put("COUNTY", pd.getString("guoku_id"));
        }
        if ("3".equals(String.valueOf(guokuData.get("level")))) {
            pd.put("CITY", pd.getString("guoku_id"));
        }
        if ("2".equals(String.valueOf(guokuData.get("level")))) {
            pd.put("PROVINCE", pd.getString("guoku_id"));
        }
        List<Map<String, Object>> dataList = inspectionSelfSummaryService.getCurSelfSumInfo(pd);

        dataList = mergeDataList(dataList);

        String filepath = saveDir + "/" + pd.getString("TASK_ID") + "/";
        String[] queryCols = {
                "QUESTION_DSCR_1",
                "QUESTION_DSCR_2",
                "QUESTION_DSCR_3",
                "QUESTION_CONTENT",
                "MEASURES",
                "PROVINCE_COUNT",
                "CITY_DSCR",
                "CITY_COUNT",
                "COUNTY_DSCR",
                "COUNTY_COUNT"
        };
        CreateExcel_2.createInspectSelfSumTable(title, filepath, fileName, queryCols, dataList);
        FileDownload.fileDownload(response, filepath + fileName, fileName, this.getRequest());
    }

    /* 自查记录合并 */
    private List<Map<String, Object>> mergeDataList(List<Map<String, Object>> dataList) {

        int index = 1;

        List<Map<String, Object>> mergeList = new ArrayList<>();
        if (dataList == null || dataList.isEmpty()) {
            return null;
        }
        mergeList.add(dataList.get(0));

        subMergeList(mergeList, index);

        for (int i = 1; i < dataList.size(); i++) {
            String mergeQuestionID3 = String.valueOf(mergeList.get(mergeList.size() - 1).get("QUESTION_ID_3"));
            String questionId3 = String.valueOf(dataList.get(i).get("QUESTION_ID_3"));
            if (mergeQuestionID3.equals(questionId3)) {
                index++;

                String dataTreDscr = ((dataList.get(i).get("COUNTY_DSCR") != null && !"".equals(String.valueOf(dataList.get(i).get("COUNTY_DSCR"))) && !"null".equals(String.valueOf(dataList.get(i).get("COUNTY_DSCR")))) ?
                        String.valueOf(dataList.get(i).get("COUNTY_DSCR"))
                        : (dataList.get(i).get("CITY_DSCR") != null && !"".equals(String.valueOf(dataList.get(i).get("CITY_DSCR"))) && !"null".equals(String.valueOf(dataList.get(i).get("CITY_DSCR")))) ?
                        String.valueOf(dataList.get(i).get("CITY_DSCR"))
                        : (dataList.get(i).get("PROVINCE_DSCR") != null && !"".equals(String.valueOf(dataList.get(i).get("PROVINCE_DSCR"))) && !"null".equals(String.valueOf(dataList.get(i).get("PROVINCE_DSCR")))) ?
                        String.valueOf(dataList.get(i).get("PROVINCE_DSCR")) : "");

                String dataCountyDscr = (dataList.get(i).get("COUNTY_DSCR") != null && !"".equals(String.valueOf(dataList.get(i).get("COUNTY_DSCR"))) && !"null".equals(String.valueOf(dataList.get(i).get("COUNTY_DSCR")))) ?
                        "\n" + String.valueOf(dataList.get(i).get("COUNTY_DSCR")) : "";


                String DataCityDscr = (dataList.get(i).get("CITY_DSCR") != null && !"".equals(String.valueOf(dataList.get(i).get("CITY_DSCR"))) && !"null".equals(String.valueOf(dataList.get(i).get("CITY_DSCR")))) ?
                        "\n" + String.valueOf(dataList.get(i).get("CITY_DSCR")) : "";

                int dataCountyCount = dataList.get(i).get("COUNTY_COUNT") != null ? ((Long) (dataList.get(i).get("COUNTY_COUNT"))).intValue() : 0;
                int dataCityCount = dataList.get(i).get("CITY_COUNT") != null ? ((Long) (dataList.get(i).get("CITY_COUNT"))).intValue() : 0;
                int dataProvinceCount = dataList.get(i).get("PROVINCE_COUNT") != null ? ((Long) (dataList.get(i).get("PROVINCE_COUNT"))).intValue() : 0;

                String dataContent = index + ".【" + dataTreDscr + "】 " + String.valueOf(dataList.get(i).get("QUESTION_CONTENT"));
                String dataMeasures = index + ".【" + dataTreDscr + "】 " + String.valueOf(dataList.get(i).get("MEASURES"));

                String countyDscr = String.valueOf(mergeList.get(mergeList.size() - 1).get("COUNTY_DSCR"));
                String cityDscr = String.valueOf(mergeList.get(mergeList.size() - 1).get("CITY_DSCR"));
                int countyCount = (int) (mergeList.get(mergeList.size() - 1).get("COUNTY_COUNT"));
                int cityCount = (int) (mergeList.get(mergeList.size() - 1).get("CITY_COUNT"));
                int provinceCount = (int) (mergeList.get(mergeList.size() - 1).get("PROVINCE_COUNT"));
                String QUESTION_CONTENT = String.valueOf(mergeList.get(mergeList.size() - 1).get("QUESTION_CONTENT"));
                String MEASURES = String.valueOf(mergeList.get(mergeList.size() - 1).get("MEASURES"));

                QUESTION_CONTENT += "\n" + dataContent;
                MEASURES += "\n" + dataMeasures;
                countyDscr += dataCountyDscr;
                cityDscr += DataCityDscr;
                countyCount += dataCountyCount;
                cityCount += dataCityCount;
                provinceCount += dataProvinceCount;

                mergeList.get(mergeList.size() - 1).put("QUESTION_CONTENT", QUESTION_CONTENT);
                mergeList.get(mergeList.size() - 1).put("MEASURES", MEASURES);
                mergeList.get(mergeList.size() - 1).put("COUNTY_DSCR", countyDscr);
                mergeList.get(mergeList.size() - 1).put("CITY_DSCR", cityDscr);

                mergeList.get(mergeList.size() - 1).put("COUNTY_COUNT", countyCount);
                mergeList.get(mergeList.size() - 1).put("CITY_COUNT", cityCount);
                mergeList.get(mergeList.size() - 1).put("PROVINCE_COUNT", provinceCount);

            } else {
                index = 1;
                mergeList.add(dataList.get(i));

                subMergeList(mergeList, index);
            }

        }
        return mergeList;
    }

    private void subMergeList(List<Map<String, Object>> mergeList, int index) {
        String treDscr = ((mergeList.get(mergeList.size() - 1).get("COUNTY_DSCR") != null && !"".equals(String.valueOf(mergeList.get(mergeList.size() - 1).get("COUNTY_DSCR"))) && !"null".equals(String.valueOf(mergeList.get(mergeList.size() - 1).get("COUNTY_DSCR")))) ?
                String.valueOf(mergeList.get(mergeList.size() - 1).get("COUNTY_DSCR"))
                : (mergeList.get(mergeList.size() - 1).get("CITY_DSCR") != null && !"".equals(String.valueOf(mergeList.get(mergeList.size() - 1).get("CITY_DSCR"))) && !"null".equals(String.valueOf(mergeList.get(mergeList.size() - 1).get("CITY_DSCR")))) ?
                String.valueOf(mergeList.get(mergeList.size() - 1).get("CITY_DSCR"))
                : (mergeList.get(mergeList.size() - 1).get("PROVINCE_DSCR") != null && !"".equals(String.valueOf(mergeList.get(mergeList.size() - 1).get("PROVINCE_DSCR"))) && !"null".equals(String.valueOf(mergeList.get(mergeList.size() - 1).get("PROVINCE_DSCR")))) ?
                String.valueOf(mergeList.get(mergeList.size() - 1).get("PROVINCE_DSCR")) : "");

        String countyDscr = (mergeList.get(mergeList.size() - 1).get("COUNTY_DSCR") != null && !"".equals(String.valueOf(mergeList.get(mergeList.size() - 1).get("COUNTY_DSCR"))) && !"null".equals(String.valueOf(mergeList.get(mergeList.size() - 1).get("COUNTY_DSCR")))) ?
                String.valueOf(mergeList.get(mergeList.size() - 1).get("COUNTY_DSCR")) : "";

        String cityDscr = (mergeList.get(mergeList.size() - 1).get("CITY_DSCR") != null && !"".equals(String.valueOf(mergeList.get(mergeList.size() - 1).get("CITY_DSCR"))) && !"null".equals(String.valueOf(mergeList.get(mergeList.size() - 1).get("CITY_DSCR")))) ?
                String.valueOf(mergeList.get(mergeList.size() - 1).get("CITY_DSCR")) : "";

        int countyCount = mergeList.get(mergeList.size() - 1).get("COUNTY_COUNT") != null ? ((Long) (mergeList.get(mergeList.size() - 1).get("COUNTY_COUNT"))).intValue() : 0;
        int cityCount = mergeList.get(mergeList.size() - 1).get("CITY_COUNT") != null ? ((Long) (mergeList.get(mergeList.size() - 1).get("CITY_COUNT"))).intValue() : 0;
        int provinceCount = mergeList.get(mergeList.size() - 1).get("PROVINCE_COUNT") != null ? ((Long) (mergeList.get(mergeList.size() - 1).get("PROVINCE_COUNT"))).intValue() : 0;

        String QUESTION_CONTENT = index + ".【" + treDscr + "】 " + String.valueOf(mergeList.get(mergeList.size() - 1).get("QUESTION_CONTENT"));
        String MEASURES = index + ".【" + treDscr + "】 " + String.valueOf(mergeList.get(mergeList.size() - 1).get("MEASURES"));

        mergeList.get(mergeList.size() - 1).put("QUESTION_CONTENT", QUESTION_CONTENT);
        mergeList.get(mergeList.size() - 1).put("MEASURES", MEASURES);
        mergeList.get(mergeList.size() - 1).put("COUNTY_DSCR", countyDscr);
        mergeList.get(mergeList.size() - 1).put("CITY_DSCR", cityDscr);

        mergeList.get(mergeList.size() - 1).put("COUNTY_COUNT", countyCount);
        mergeList.get(mergeList.size() - 1).put("CITY_COUNT", cityCount);
        mergeList.get(mergeList.size() - 1).put("PROVINCE_COUNT", provinceCount);
    }


} ///:~

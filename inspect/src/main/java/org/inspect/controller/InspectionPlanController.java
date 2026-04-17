//: 检查计划访问控制器
package org.inspect.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.inspect.service.*;
import org.inspect.util.CreateExcel_2;
import org.inspect.util.DateUtil;
import org.inspect.util.FileDownload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.inspect.BaseController;
import org.inspect.util.PageData;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by dj on 2019/9/6.
 */
@Slf4j
@RestController
@RequestMapping(value = "/inspection", produces = MediaType.APPLICATION_JSON_VALUE)
public class InspectionPlanController extends BaseController {

    @Value("${TEMPLATE_FILE_PATH}")
    private String saveDir;

    @Autowired
    private InspectionPlanService inspectionPlanService;
    @Autowired
    protected InspectionUserService inspectionUserService;
    @Autowired
    private InspectionGroupService inspectionGroupService;
    @Autowired
    private InspectionTaskService inspectionTaskService;
    @Autowired
    private InspectionProcService inspectionProcService;
    @Autowired
    private InspectionProcSubService inspectionProcSubService;
    @Autowired
    private InspectionNoticeService inspectionNoticeService;
    @Autowired
    private InspectionBorrowService inspectionBorrowService;

    @Autowired
    private InspectionStatisticsTableService inspectionStatisticsTableService;

    /**
     * 查询当前计划问题台账中的一级问题分类
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "查询当前计划问题台账中的一级问题分类")
    @PostMapping(value = "/getCurTaskQuestion_1")
    public List<Map<String, Object>> getCurTaskQuestion_1(
            @ApiParam("当前计划ID：INSPECTION_PLAN_ID \n")
            @RequestBody(required = false) JSONObject param
    ) {
        PageData pd = this.getPageData(param);
        List<Map<String, Object>> dataList = inspectionPlanService.getCurTaskQuestion_1(pd);
        return dataList;
    }

    @RequestMapping(value = {"/getStatisticsTableByPlanId"}, method = RequestMethod.POST, produces = {"application/json"})
    @ApiOperation("查询计划包含任务的汇总表信息")
    public List<Map<String, Object>> getStatisticsTableByPlanId(
            @ApiParam(value = "需要查询的组装列名列表：queryCols[]\n" +
                    "当前计划ID：INSPECTION_PLAN_ID")
            @RequestBody(required = false) JSONObject jsonob) {
        Map<String, Object> result = new HashMap<>();
        PageData pd = new PageData(jsonob);
        pd.put("columns", JSONArray.parse(pd.getString("queryCols")));
        List<Map<String, Object>> data = inspectionPlanService.getStatisticsTableByPlanId(pd);
        return data;
    }

    /**
     * 检查统计表主表
     * 返回值为检查统计表的数据集合
     */
    @RequestMapping(value = {"/getInspectionPlanData"}, method = RequestMethod.POST, produces = {"application/json"})
    @ApiOperation("获取检查统计表主表")
    public Map<String, Object> getInspectionPlanData(@RequestBody(required = false) JSONObject jsonob) {
        Map<String, Object> result = new HashMap<>();
        PageData pd = new PageData(jsonob);
        Integer pageNo = (Integer.parseInt(pd.getString("pageNo")) - 1) * Integer.parseInt(pd.getString("pageSize"));
        pd.put("page", pageNo);
        pd.put("rows", Integer.parseInt(pd.getString("pageSize")));
        try {
            List<Map<String, Object>> data = inspectionPlanService.getInspectionPlanData(pd);
           /* if (null != data && data.size()>0){
                for (int i=0;i<data.size();i++){
                    if (data.get(i).get("INSPECTION_PLAN_TYPE").equals("002")||data.get(i).get("INSPECTION_PLAN_TYPE").equals("003")||data.get(i).get("INSPECTION_PLAN_TYPE").equals("004")){
                        String guoku_id = data.get(i).get("INSPECTION_GUOKU").toString();
                        String[]ids = guoku_id.split(",");
                        String guoku_dscr = "";
                        for (String id:ids){
                            PageData guokuPd = new PageData();
                            guokuPd.put("guoku_id",id);
                            Map<String,Object>guokuMap = inspectionPlanService.getGuokuById(guokuPd);
                            if (null != guokuMap && !guokuMap.isEmpty()){
                                guoku_dscr += guokuMap.get("guoku_dscr").toString()+",";
                            }
                        }
                        data.get(i).put("INSPECTION_GUOKU_DSCR",guoku_dscr.substring(0,guoku_dscr.lastIndexOf(",")));
                    }
                }
            }*/
            Integer count = inspectionPlanService.getInspectionPlanDataCount(pd);
            result.put("total", count);//total键 存放总记录数，必须的
            result.put("rows", data);//rows键 存放每页记录 list
            result.put("msg", "查询成功");
            result.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", e.getMessage());
            result.put("result", "success");
        }
        return result;
    }


    @RequestMapping(value = {"/getInspectionPlanInspected"}, method = RequestMethod.POST, produces = {"application/json"})
    @ApiOperation("获取修改被查库列表")
    public Map<String, Object> getInspectionPlanInspected(@RequestBody(required = false) JSONObject jsonob) {
        Map<String, Object> result = new HashMap<>();
        PageData pd = new PageData(jsonob);
        try {
            List<Map<String, Object>> data = inspectionPlanService.getInspectionPlanInspected(pd);
            result.put("rows", data);//rows键 存放每页记录 list
            result.put("msg", "查询成功");
            result.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", e.getMessage());
            result.put("result", "success");
        }
        return result;
    }

    @RequestMapping(value = "/saveInspectPlan", method = RequestMethod.POST, produces = {"application/json"})
    @ApiOperation("保存计划")
    public Map<String, Object> saveInspectPlan(@RequestBody(required = false) JSONObject jsonob) {
        Map<String, Object> result = new HashMap<>();
        PageData pd = new PageData(jsonob);
        String dateNow = DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS);

        Integer rs = checkRepeat(pd);
        if (null != rs && rs > 0) {
            result.put("msg", "新增失败！该计划名称已存在，请修改后重试。");
            result.put("result", "false");
        } else {
            try {
                String INSPECTION_PLAN_ID = get32UUID();
                pd.put("INSPECTION_PLAN_ID", INSPECTION_PLAN_ID);
                //保存检查计划表信息
                pd.put("UPDATE_TIME", dateNow);
                String type =  pd.getString("INSPECTION_PLAN_TYPE");
                 if (null !=  type && (type.equals("007"))){
                     pd.put("CHECKED_ORG", "");
                     pd.put("CHECKED_ORG_DSCR", pd.get("CHECKED_ORG_DSCR"));
                     pd.put("CHECK_ORG", pd.get("BOOKORG_ID"));
                     pd.put("CHECK_ORG_DSCR", pd.get("BOOKORG_DSCR"));
                     pd.put("INSPECTION_BOOKORG_ID", pd.get("BOOKORG_ID"));
                     pd.put("INSPECTION_BOOKORG_DSCR", pd.get("BOOKORG_DSCR"));
                } else if(null !=  type && (type.equals("002")||type.equals("003")||type.equals("004") ||type.equals("008"))) {
                     pd.put("CHECKED_ORG", pd.get("BOOKORG_ID"));
                     pd.put("CHECKED_ORG_DSCR", pd.get("BOOKORG_DSCR"));
                     pd.put("CHECK_ORG", pd.get("BOOKORG_ID"));
                     pd.put("CHECK_ORG_DSCR", pd.get("BOOKORG_DSCR"));
                 }
                else{
                     pd.put("CHECKED_ORG", pd.get("BOOKORG_ID"));
                     pd.put("CHECKED_ORG_DSCR", pd.get("BOOKORG_DSCR"));
                     pd.put("CHECK_ORG", pd.get("INSPECTION_BOOKORG_ID"));
                     pd.put("CHECK_ORG_DSCR", pd.get("INSPECTION_BOOKORG_DSCR"));
                }
                pd.put("IS_BUILDTASK", "1");
                pd.put("CREATE_ORG", pd.get("GUOKU_ID"));
                inspectionPlanService.saveInspectPlan(pd);
                result.put("msg", "添加成功");
                result.put("result", "success");
            } catch (Exception e) {
                e.getStackTrace();
                result.put("msg", e.getMessage());
                result.put("result", "failed");
            }
        }
        return result;
    }

    @RequestMapping(value = "/buildInspectTaskList", method = RequestMethod.POST, produces = {"application/json"})
    @ApiOperation("组装检查计划的检查任务")
    public Map<String, Object> buildInspectTaskList(@RequestBody(required = false) JSONObject jsonob){
        Map<String, Object> result = new HashMap<>();
        PageData pd = new PageData(jsonob);
        List<Map<String, Object>> data = inspectionPlanService.getInspectionPlanData(pd);
        Map<String, Object> inspectPlan = data.get(0);
        String dataNow = DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS);
        try {
            //保存检查计划表信息
            String INSPECTION_GUOKU = "";
            String type =  String.valueOf(inspectPlan.get("INSPECTION_PLAN_TYPE"));
            if (null !=  type && (type.equals("002")||type.equals("003")||type.equals("004"))){
                //被检查核算主体
                String REVIEW_BOOKORG_ID = String.valueOf(inspectPlan.get("CHECKED_ORG"));
                String REVIEW_BOOKORG_DSCR = String.valueOf(inspectPlan.get("CHECKED_ORG_DSCR"));
                //被检查核算主体数组
                String[] REVIEW_BOOKORG_ID_SUB = REVIEW_BOOKORG_ID.split(",");
                String[] REVIEW_BOOKORG_DSCR_SUB = REVIEW_BOOKORG_DSCR.split(",");

                for (int i = 0;i < REVIEW_BOOKORG_ID_SUB.length;i++){
                    pd.put("TYPE_ID",type);
                    String year = String.valueOf(inspectPlan.get("INSPECTION_PLAN_YEAR"));
                    if (year.equals(DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM).substring(0,4))){
                        pd.put("month",DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS).substring(5,7));
                    }
                    if (Integer.parseInt(year)<Integer.parseInt(DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM).substring(0,4))){
                        result.put("msg", "新增失败！不能输入历史年份");
                        result.put("result", "false");
                        return result;
                    }

                    pd.put("bookorgcode", REVIEW_BOOKORG_ID_SUB[i]);
                    Map<String, Object> temp = inspectionTaskService.getGKbyBook(pd);
                    INSPECTION_GUOKU += temp.get("guoku_id") +",";

                    List<Map<String, Object>> periodList = inspectionTaskService.getPeriod(pd);
                    if (null != periodList && periodList.size()>0){
                        for (int j=0;j<periodList.size();j++){
                            pd.put("INSPECTION_TASK_ID", get32UUID());
                            pd.put("BOOKORG_ID", REVIEW_BOOKORG_ID_SUB[i]);
                            pd.put("BOOKORG_DSCR", REVIEW_BOOKORG_DSCR_SUB[i]);

                            pd.put("INSPECTED_GUOKU_ID", temp.get("guoku_id"));
                            pd.put("INSPECTION_GUOKU", temp.get("guoku_id"));
                            pd.put("INSPECTION_TASK_NAME",  String.valueOf(inspectPlan.get("INSPECTION_PLAN_YEAR"))
                                    +  REVIEW_BOOKORG_DSCR_SUB[i]
                                    +  String.valueOf(inspectPlan.get("INSPECTION_PLAN_TYPE_DSCR"))
                                    +  periodList.get(j).get("PERIOD_DSCR").toString()
                                    +  System.currentTimeMillis());
                            pd.put("CREATE_TIME", dataNow);
                            if (j == 0){
                                if (Integer.parseInt(year) > Integer.parseInt(DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM).substring(0,4))){
                                    pd.put("INSPECTION_TASK_BEGINTIME",year + "-" + periodList.get(j).get("STAR_DATE"));
                                }else{
                                    String time = DateUtil.transformDateToStr(DateUtil.dateAdd(DateUtil.getCurrentDate(),5,1),DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS);
                                    pd.put("INSPECTION_TASK_BEGINTIME", time.substring(0,10));
                                }
                            }else{
                                pd.put("INSPECTION_TASK_BEGINTIME", year + "-" + periodList.get(j).get("STAR_DATE"));
                            }
                            pd.put("INSPECTION_TASK_ENDTIME", year + "-" +periodList.get(j).get("END_DATE"));
                            pd.put("INSPECTION_TASK_TYPE",type);
                            pd.put("INSPECTION_TASK_YEAR", year);
                            pd.put("INSPECTION_BOOKORG_ID", REVIEW_BOOKORG_ID_SUB[i]);
                            pd.put("INSPECTION_BOOKORG_DSCR", REVIEW_BOOKORG_DSCR_SUB[i]);
                            inspectionTaskService.addInspectionTask(pd);
                            inspectionProcService.addInspectionProc(pd);
                        }
                    }
                }
            }else if (null !=  type && (type.equals("007"))){
                pd.put("bookorgcode", String.valueOf(inspectPlan.get("INSPECTION_BOOKORG_ID")));
                Map<String, Object> temp = inspectionTaskService.getGKbyBook(pd);
                String bookOrgName = String.valueOf(inspectPlan.get("INSPECTION_BOOKORG_DSCR"));
                String CHECK_ORG_DSCR = bookOrgName.substring(0, bookOrgName.lastIndexOf("行") + 1);

                pd.put("INSPECTION_GUOKU", temp.get("guoku_id").toString());
                pd.put("CHECK_ORG_DSCR", CHECK_ORG_DSCR);

                pd.put("INSPECTION_TASK_BEGINTIME", String.valueOf(inspectPlan.get("INSPECTION_PLAN_BEGINTIME")));
                pd.put("INSPECTION_TASK_ENDTIME", String.valueOf(inspectPlan.get("INSPECTION_PLAN_ENDTIME")));

                pd.put("INSPECTION_TASK_ID", get32UUID());
                pd.put("INSPECTION_GROUP_ID", get32UUID());
                pd.put("INSPECTION_GROUP_NAME",'1');
                //pd.put("INSPECTION_TASK_NAME", pd.getString("INSPECTION_PLAN_YEAR") + pd.getString("CHECK_ORG_DSCR") + pd.getString("INSPECTION_PLAN_TYPE_NAME"));
                pd.put("INSPECTION_TASK_NAME", String.valueOf(inspectPlan.get("INSPECTION_PLAN_YEAR"))
                        + String.valueOf(inspectPlan.get("CHECKED_ORG_DSCR"))
                        + String.valueOf(inspectPlan.get("INSPECTION_PLAN_TYPE_DSCR"))
                        + System.currentTimeMillis());
                pd.put("CREATE_TIME", dataNow);
                pd.put("INSPECTION_PLAN_YEAR", inspectPlan.get("INSPECTION_PLAN_YEAR"));
                pd.put("INSPECTION_PLAN_TYPE", inspectPlan.get("INSPECTION_PLAN_TYPE"));
                pd.put("INSPECTION_BOOKORG_ID",inspectPlan.get("INSPECTION_BOOKORG_ID"));
                pd.put("INSPECTION_BOOKORG_DSCR", inspectPlan.get("INSPECTION_BOOKORG_DSCR"));
                inspectionPlanService.saveInspectPlanSub(pd);
                inspectionPlanService.saveInspectPlanGroup(pd);
                pd.put("INSPECTION_TASK_TYPE", String.valueOf(inspectPlan.get("INSPECTION_PLAN_TYPE")));
                inspectionProcService.addInspectionProc(pd);
            }else if(null !=  type && (type.equals("008"))) {
                //被检查核算主体
                String REVIEW_BOOKORG_ID = String.valueOf(inspectPlan.get("CHECKED_ORG"));
                String REVIEW_BOOKORG_DSCR = String.valueOf(inspectPlan.get("CHECKED_ORG_DSCR"));
                //被检查核算主体数组
                String[] REVIEW_BOOKORG_ID_SUB = REVIEW_BOOKORG_ID.split(",");
                String[] REVIEW_BOOKORG_DSCR_SUB = REVIEW_BOOKORG_DSCR.split(",");
                //保存任务表信息
                for (int i = 0; i < REVIEW_BOOKORG_ID_SUB.length; i++) {
                    pd.put("INSPECTION_TASK_ID", get32UUID());
                    pd.put("INSPECTION_TASK_BEGINTIME", String.valueOf(inspectPlan.get("INSPECTION_PLAN_BEGINTIME")));
                    pd.put("INSPECTION_TASK_ENDTIME", String.valueOf(inspectPlan.get("INSPECTION_PLAN_ENDTIME")));
                    pd.put("BOOKORG_ID", REVIEW_BOOKORG_ID_SUB[i]);
                    pd.put("BOOKORG_DSCR", REVIEW_BOOKORG_DSCR_SUB[i]);
                    pd.put("bookorgcode", REVIEW_BOOKORG_ID_SUB[i]);
                    Map<String, Object> temp = inspectionTaskService.getGKbyBook(pd);
                    pd.put("INSPECTED_GUOKU_ID", temp.get("guoku_id"));
                    pd.put("INSPECTION_GUOKU", temp.get("guoku_id"));
                    INSPECTION_GUOKU += temp.get("guoku_id") +",";
                    pd.put("INSPECTION_TASK_NAME", String.valueOf(inspectPlan.get("INSPECTION_PLAN_YEAR"))
                            + REVIEW_BOOKORG_DSCR_SUB[i]
                            + String.valueOf(inspectPlan.get("INSPECTION_PLAN_TYPE_DSCR"))
                            + System.currentTimeMillis());
                    pd.put("CREATE_TIME", dataNow);
                    pd.put("INSPECTION_PLAN_YEAR", inspectPlan.get("INSPECTION_PLAN_YEAR"));
                    pd.put("INSPECTION_PLAN_TYPE", inspectPlan.get("INSPECTION_PLAN_TYPE"));
                    pd.put("INSPECTION_BOOKORG_ID", REVIEW_BOOKORG_ID_SUB[i]);
                    pd.put("INSPECTION_BOOKORG_DSCR", REVIEW_BOOKORG_DSCR_SUB[i]);
                    inspectionPlanService.saveInspectPlanSub(pd);
                    pd.put("INSPECTION_TASK_TYPE", String.valueOf(inspectPlan.get("INSPECTION_PLAN_TYPE")));
                    inspectionProcService.addInspectionProc(pd);
                }
            }else{
                //检查核算主体机构
                String INSPECTION_BOOKORG_ID = String.valueOf(inspectPlan.get("INSPECTION_BOOKORG_ID"));
                pd.put("bookorgcode", INSPECTION_BOOKORG_ID);
                Map<String, Object> temp_1 = inspectionTaskService.getGKbyBook(pd);
                pd.put("INSPECTION_GUOKU", temp_1.get("guoku_id"));
                //被检查核算主体
                String REVIEW_BOOKORG_ID = String.valueOf(inspectPlan.get("CHECKED_ORG"));
                String REVIEW_BOOKORG_DSCR = String.valueOf(inspectPlan.get("CHECKED_ORG_DSCR"));
                //被检查核算主体数组
                String[] REVIEW_BOOKORG_ID_SUB = REVIEW_BOOKORG_ID.split(",");
                String[] REVIEW_BOOKORG_DSCR_SUB = REVIEW_BOOKORG_DSCR.split(",");
                //保存任务表信息
                for (int i = 0; i < REVIEW_BOOKORG_ID_SUB.length; i++) {
                    pd.put("INSPECTION_TASK_ID", get32UUID());
                    pd.put("INSPECTION_GROUP_ID", get32UUID() + i);
                    pd.put("INSPECTION_GROUP_NAME", i + 1);
                    pd.put("BOOKORG_ID", REVIEW_BOOKORG_ID_SUB[i]);
                    pd.put("BOOKORG_DSCR", REVIEW_BOOKORG_DSCR_SUB[i]);
                    pd.put("bookorgcode", REVIEW_BOOKORG_ID_SUB[i]);
                    Map<String, Object> temp = inspectionTaskService.getGKbyBook(pd);
                    pd.put("INSPECTED_GUOKU_ID", temp.get("guoku_id"));
                    pd.put("INSPECTION_PLAN_YEAR", inspectPlan.get("INSPECTION_PLAN_YEAR"));
                    pd.put("INSPECTION_PLAN_TYPE", inspectPlan.get("INSPECTION_PLAN_TYPE"));
                    pd.put("INSPECTION_TASK_NAME", String.valueOf(inspectPlan.get("INSPECTION_PLAN_YEAR"))
                            + REVIEW_BOOKORG_DSCR_SUB[i]
                            + String.valueOf(inspectPlan.get("INSPECTION_PLAN_TYPE_DSCR"))
                            +  System.currentTimeMillis());
                    pd.put("CREATE_TIME", dataNow);
                    pd.put("INSPECTION_BOOKORG_ID",inspectPlan.get("INSPECTION_BOOKORG_ID"));
                    pd.put("INSPECTION_BOOKORG_DSCR", inspectPlan.get("INSPECTION_BOOKORG_DSCR"));
                    inspectionPlanService.saveInspectPlanSub(pd);
                    inspectionPlanService.saveInspectPlanGroup(pd);
                    pd.put("INSPECTION_TASK_TYPE", String.valueOf(inspectPlan.get("INSPECTION_PLAN_TYPE")));
                    inspectionProcService.addInspectionProc(pd);
                }
            }
            if (null !=  type && (type.equals("002")||type.equals("003")||type.equals("004")||type.equals("008"))){
                if (INSPECTION_GUOKU.indexOf(",")>-1){
                    pd.put("INSPECTION_GUOKU", INSPECTION_GUOKU.substring(0,INSPECTION_GUOKU.lastIndexOf(",")));
                }else{
                    pd.put("INSPECTION_GUOKU", INSPECTION_GUOKU);
                }
            }
            pd.put("BUILDTASK_DATE", dataNow);
            pd.put("IS_BUILDTASK", "0");
            pd.put("BUILDTASK_DATE", dataNow);
            inspectionPlanService.editInspect(pd);
            result.put("msg", "编辑成功");
            result.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", e.getMessage());
            result.put("result", "failed");
        }
        return result;
    }

    @RequestMapping(value = "/saveInspect", method = RequestMethod.POST, produces = {"application/json"})
    @ApiOperation("保存增加检查统计表接口")
    public Map<String, Object> saveInspect(@RequestBody(required = false) JSONObject jsonob) {
        Map<String, Object> result = new HashMap<>();
        PageData pd = new PageData(jsonob);
        Integer rs = checkRepeat(pd);
        result.put("result", "success");
        result.put("msg", "新增成功！");
        if (null != rs && rs > 0) {
            result.put("msg", "新增失败！该计划名称已存在，请修改后重试。");
            result.put("result", "false");
        } else {
            try {
                String INSPECTION_PLAN_ID = get32UUID();
                pd.put("INSPECTION_PLAN_ID", INSPECTION_PLAN_ID);
                //保存检查计划表信息
                pd.put("UPDATE_TIME", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
                String INSPECTION_GUOKU = "";
                String type =  pd.getString("INSPECTION_PLAN_TYPE");
                if (null !=  type && (type.equals("002")||type.equals("003")||type.equals("004"))){
                    //被检查核算主体
                    String REVIEW_BOOKORG_ID = pd.getString("BOOKORG_ID");
                    String REVIEW_BOOKORG_DSCR = pd.getString("BOOKORG_DSCR");
                    //被检查核算主体数组
                    String[] REVIEW_BOOKORG_ID_SUB = REVIEW_BOOKORG_ID.split(",");
                    String[] REVIEW_BOOKORG_DSCR_SUB = REVIEW_BOOKORG_DSCR.split(",");

                    for (int i = 0;i < REVIEW_BOOKORG_ID_SUB.length;i++){
                        pd.put("TYPE_ID",type);
                        String year = pd.getString("INSPECTION_PLAN_YEAR");
                        List<Map<String, Object>> periodList = new ArrayList<>();
                        if (year.equals(DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM).substring(0,4))){
                            pd.put("month",DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS).substring(5,7));
                        }
                        if (Integer.parseInt(year)<Integer.parseInt(DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM).substring(0,4))){
                            result.put("msg", "新增失败！不能输入历史年份");
                            result.put("result", "false");
                            return result;
                        }

                        pd.put("bookorgcode", REVIEW_BOOKORG_ID_SUB[i]);
                        Map<String, Object> temp = inspectionTaskService.getGKbyBook(pd);
                        INSPECTION_GUOKU += temp.get("guoku_id") +",";

                        periodList = inspectionTaskService.getPeriod(pd);
                        if (null != periodList && periodList.size()>0){
                            for (int j=0;j<periodList.size();j++){
                                pd.put("INSPECTION_TASK_ID", get32UUID());
                                pd.put("BOOKORG_ID", REVIEW_BOOKORG_ID_SUB[i]);
                                pd.put("BOOKORG_DSCR", REVIEW_BOOKORG_DSCR_SUB[i]);

                                pd.put("INSPECTED_GUOKU_ID", temp.get("guoku_id"));
                                pd.put("INSPECTION_GUOKU", temp.get("guoku_id"));
                                pd.put("INSPECTION_TASK_NAME", pd.getString("INSPECTION_PLAN_YEAR") +  REVIEW_BOOKORG_DSCR_SUB[i] + pd.getString("INSPECTION_PLAN_TYPE_NAME") + periodList.get(j).get("PERIOD_DSCR").toString());
                                pd.put("CREATE_TIME", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
                                if (j == 0){
                                    if (Integer.parseInt(year) > Integer.parseInt(DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM).substring(0,4))){
                                        pd.put("INSPECTION_TASK_BEGINTIME",year + "-" + periodList.get(j).get("STAR_DATE"));
                                    }else{
                                        String time = DateUtil.transformDateToStr(DateUtil.dateAdd(DateUtil.getCurrentDate(),5,1),DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS);
                                        pd.put("INSPECTION_TASK_BEGINTIME", time.substring(0,10));
                                    }
                                }else{
                                    pd.put("INSPECTION_TASK_BEGINTIME", year + "-" + periodList.get(j).get("STAR_DATE"));
                                }
                                pd.put("INSPECTION_TASK_ENDTIME", year + "-" +periodList.get(j).get("END_DATE"));
                                pd.put("INSPECTION_TASK_TYPE",type);
                                pd.put("INSPECTION_TASK_YEAR", year);
                                inspectionTaskService.addInspectionTask(pd);
                                inspectionProcService.addInspectionProc(pd);
                            }
                        }
                    }
                }else if (null !=  type && (type.equals("007"))){
                    pd.put("bookorgcode", pd.getString("BOOKORG_ID"));
                    Map<String, Object> temp = inspectionTaskService.getGKbyBook(pd);
                    String bookOrgName = temp.get("bookorgname").toString();
                    String CHECK_ORG_DSCR = bookOrgName.substring(0, bookOrgName.lastIndexOf("行") + 1);

                    pd.put("INSPECTION_GUOKU", temp.get("guoku_id").toString());
                    pd.put("CHECK_ORG_DSCR", CHECK_ORG_DSCR);

                    pd.put("INSPECTION_TASK_BEGINTIME", pd.getString("INSPECTION_PLAN_BEGINTIME"));
                    pd.put("INSPECTION_TASK_ENDTIME", pd.getString("INSPECTION_PLAN_ENDTIME"));

                    pd.put("INSPECTION_TASK_ID", get32UUID());
                    pd.put("INSPECTION_GROUP_ID", get32UUID());
                    pd.put("INSPECTION_GROUP_NAME",'1');
                    //pd.put("INSPECTION_TASK_NAME", pd.getString("INSPECTION_PLAN_YEAR") + pd.getString("CHECK_ORG_DSCR") + pd.getString("INSPECTION_PLAN_TYPE_NAME"));
                    pd.put("INSPECTION_TASK_NAME", pd.getString("INSPECTION_PLAN_YEAR") + pd.getString("CHECKED_ORG_DSCR") + pd.getString("INSPECTION_PLAN_TYPE_NAME"));
                    pd.put("CREATE_TIME", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
                    inspectionPlanService.saveInspectPlanSub(pd);
                    inspectionPlanService.saveInspectPlanGroup(pd);
                    pd.put("INSPECTION_TASK_TYPE", pd.getString("INSPECTION_PLAN_TYPE"));
                    inspectionProcService.addInspectionProc(pd);
                }else if(null !=  type && (type.equals("008"))) {
                    //被检查核算主体
                    String REVIEW_BOOKORG_ID = pd.getString("BOOKORG_ID");
                    String REVIEW_BOOKORG_DSCR = pd.getString("BOOKORG_DSCR");
                    //被检查核算主体数组
                    String[] REVIEW_BOOKORG_ID_SUB = REVIEW_BOOKORG_ID.split(",");
                    String[] REVIEW_BOOKORG_DSCR_SUB = REVIEW_BOOKORG_DSCR.split(",");
                    //保存任务表信息
                    for (int i = 0; i < REVIEW_BOOKORG_ID_SUB.length; i++) {
                        pd.put("INSPECTION_TASK_ID", get32UUID());
                        pd.put("INSPECTION_TASK_BEGINTIME", pd.getString("INSPECTION_PLAN_BEGINTIME"));
                        pd.put("INSPECTION_TASK_ENDTIME", pd.getString("INSPECTION_PLAN_ENDTIME"));
                        pd.put("BOOKORG_ID", REVIEW_BOOKORG_ID_SUB[i]);
                        pd.put("BOOKORG_DSCR", REVIEW_BOOKORG_DSCR_SUB[i]);
                        pd.put("bookorgcode", REVIEW_BOOKORG_ID_SUB[i]);
                        Map<String, Object> temp = inspectionTaskService.getGKbyBook(pd);
                        pd.put("INSPECTED_GUOKU_ID", temp.get("guoku_id"));
                        pd.put("INSPECTION_GUOKU", temp.get("guoku_id"));
                        INSPECTION_GUOKU += temp.get("guoku_id") +",";
                        pd.put("INSPECTION_TASK_NAME", pd.getString("INSPECTION_PLAN_YEAR") + REVIEW_BOOKORG_DSCR_SUB[i] + pd.getString("INSPECTION_PLAN_TYPE_NAME"));
                        pd.put("CREATE_TIME", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
                        inspectionPlanService.saveInspectPlanSub(pd);
                        pd.put("INSPECTION_TASK_TYPE", pd.getString("INSPECTION_PLAN_TYPE"));
                        inspectionProcService.addInspectionProc(pd);
                    }
                }else{
                        //检查核算主体机构
                        String INSPECTION_BOOKORG_ID = pd.getString("INSPECTION_BOOKORG_ID");
                        pd.put("bookorgcode", INSPECTION_BOOKORG_ID);
                        Map<String, Object> temp_1 = inspectionTaskService.getGKbyBook(pd);
                        pd.put("INSPECTION_GUOKU", temp_1.get("guoku_id"));
                        //被检查核算主体
                        String REVIEW_BOOKORG_ID = pd.getString("BOOKORG_ID");
                        String REVIEW_BOOKORG_DSCR = pd.getString("BOOKORG_DSCR");
                        //被检查核算主体数组
                        String[] REVIEW_BOOKORG_ID_SUB = REVIEW_BOOKORG_ID.split(",");
                        String[] REVIEW_BOOKORG_DSCR_SUB = REVIEW_BOOKORG_DSCR.split(",");
                        //保存任务表信息
                        for (int i = 0; i < REVIEW_BOOKORG_ID_SUB.length; i++) {
                            pd.put("INSPECTION_TASK_ID", get32UUID());
                            pd.put("INSPECTION_GROUP_ID", get32UUID() + i);
                            pd.put("INSPECTION_GROUP_NAME", i + 1);
                            pd.put("BOOKORG_ID", REVIEW_BOOKORG_ID_SUB[i]);
                            pd.put("BOOKORG_DSCR", REVIEW_BOOKORG_DSCR_SUB[i]);
                            pd.put("bookorgcode", REVIEW_BOOKORG_ID_SUB[i]);
                            Map<String, Object> temp = inspectionTaskService.getGKbyBook(pd);
                            pd.put("INSPECTED_GUOKU_ID", temp.get("guoku_id"));
                            pd.put("INSPECTION_TASK_NAME", pd.getString("INSPECTION_PLAN_YEAR") + REVIEW_BOOKORG_DSCR_SUB[i] + pd.getString("INSPECTION_PLAN_TYPE_NAME"));
                            pd.put("CREATE_TIME", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
                            inspectionPlanService.saveInspectPlanSub(pd);
                            inspectionPlanService.saveInspectPlanGroup(pd);
                            pd.put("INSPECTION_TASK_TYPE", pd.getString("INSPECTION_PLAN_TYPE"));
                            inspectionProcService.addInspectionProc(pd);
                        }
                }
                if (null !=  type && (type.equals("002")||type.equals("003")||type.equals("004")||type.equals("008"))){
                    if (INSPECTION_GUOKU.indexOf(",")>-1){
                        pd.put("INSPECTION_GUOKU", INSPECTION_GUOKU.substring(0,INSPECTION_GUOKU.lastIndexOf(",")));
                    }else{
                        pd.put("INSPECTION_GUOKU", INSPECTION_GUOKU);
                    }
                }
                inspectionPlanService.saveInspectPlan(pd);
                result.put("msg", "添加成功");
                result.put("result", "success");
            } catch (Exception e) {
                result.put("msg", e.getMessage());
                System.out.println(e.getStackTrace());
                result.put("result", "failed");
            }
        }
        return result;
    }

    @RequestMapping(value = "/delInspect", method = RequestMethod.POST, produces = {"application/json"})
    @ApiOperation("删除检查统计表接口-不用了")
    public Map<String, Object> delInspect(@RequestBody(required = false) JSONObject jsonob
    ) {
        Map<String, Object> result = new HashMap<>();
        PageData pd = new PageData(jsonob);
        try {
//            删除主表信息
            inspectionPlanService.delInspectPlanMain(pd);
//            删除子表信息
            inspectionPlanService.delInspectPlanSub(pd);
////          删除人员表信息
            inspectionPlanService.delInspectPlanUser(pd);
            result.put("msg", "删除成功");
            result.put("result", "success");
        } catch (Exception e) {
            result.put("msg", "删除失败");
            result.put("result", "failed");
        }
        return result;
    }


    @RequestMapping(value = "/abolishInspect", method = RequestMethod.POST, produces = {"application/json"})
    @ApiOperation("废除检查计划表接口")
    public Map<String, Object> abolishInspect(@RequestBody(required = false) JSONObject jsonob
    ) {
        Map<String, Object> result = new HashMap<>();
        PageData pd = new PageData(jsonob);
        try {
//            废除计划表信息
            inspectionPlanService.abolishInspectMain(pd);
//            废除任务表信息
            inspectionPlanService.abolishInspectSub(pd);
            result.put("msg", "废除成功");
            result.put("result", "success");
        } catch (Exception e) {
            result.put("msg", "废除失败");
            result.put("result", "failed");
        }
        return result;
    }


    @RequestMapping(value = "/abolishInspectSub", method = RequestMethod.POST, produces = {"application/json"})
    @ApiOperation("废除任务表接口")
    public Map<String, Object> abolishInspectSub(@RequestBody(required = false) JSONObject jsonob
    ) {
        Map<String, Object> result = new HashMap<>();
        PageData pd = new PageData(jsonob);
        try {
//            废除计划表信息
            inspectionPlanService.abolishInspectTask(pd);
////            废除任务表信息
//            inspectionPlanService.abolishInspectSub(pd);
            result.put("msg", "废除成功");
            result.put("result", "success");
        } catch (Exception e) {
            result.put("msg", "废除失败");
            result.put("result", "failed");
        }
        return result;
    }


    @RequestMapping(value = "/editInspect", method = RequestMethod.POST)
    @ApiOperation("检查计划修改接口")
    public Map<String, Object> editInspect(@RequestBody JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        try {
            pd.put("UPDATE_TIME", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
            inspectionPlanService.editInspect(pd);
        } catch (Exception e) {
            result.put("msg", "修改失败");
            result.put("result", "failed");
        }
        result.put("msg", "修改成功");
        result.put("result", "success");
        return result;
    }


    @RequestMapping(value = "/editInspected", method = RequestMethod.POST)
    @ApiOperation("检查计划修改被查国库接口")
    public Map<String, Object> editInspected(@RequestBody JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        try {
//            inspectionPlanService.editInspected(pd);

            pd.put("bookorgcode", pd.getString("BOOKORG_ID"));
            Map<String, Object> temp = inspectionTaskService.getGKbyBook(pd);
            pd.put("INSPECTED_GUOKU_ID", temp.get("guoku_id"));
            pd.put("BOOKORG_DSCR", temp.get("bookorgname"));
            inspectionTaskService.editInspectionTask(pd);
            inspectionGroupService.editInspectionGroup(pd);
            inspectionBorrowService.editBorrowUser(pd);
        } catch (Exception e) {
            result.put("msg", "修改失败");
            result.put("result", "failed");
        }
        result.put("msg", "修改成功");
        result.put("result", "success");
        return result;
    }


    @RequestMapping(value = "/isAddPerson", method = RequestMethod.POST)
    @ApiOperation("新增检查人员判断是否可以新增")
    public int isAddPerson(@RequestBody(required = false) JSONObject jsonob) {
        PageData pd = new PageData(jsonob);
        int data = inspectionPlanService.isAddPerson(pd);
        return data;
    }


    @RequestMapping(value = {"/getTalentPool"}, method = RequestMethod.POST, produces = {"application/json"})
    @ApiOperation("获取人才库列表")
    public Map<String, Object> getTalentPool(@RequestBody(required = false) JSONObject jsonob) {
        Map<String, Object> result = new HashMap<>();
        PageData pd = new PageData(jsonob);
        try {
            List<Map<String, Object>> data = inspectionPlanService.getTalentPool(pd);
            result.put("rows", data);//rows键 存放每页记录 list
            result.put("msg", "查询成功");
            result.put("result", "success");
        } catch (Exception e) {
            result.put("msg", e.getMessage());
            result.put("result", "success");
        }
        return result;
    }


    @RequestMapping(value = "/saveInspectUser", method = RequestMethod.POST, produces = {"application/json"})
    @ApiOperation("保存增加人员表")
    public Map<String, Object> saveInspectUser(@RequestBody(required = false) JSONObject jsonob
    ) {
        Map<String, Object> result = new HashMap<>();
        PageData pd = new PageData(jsonob);
////        所有检查人员编码
        String INSPECTION_GROUP_USER_ALL = pd.getString("INSPECTION_GROUP_USER");
        String[] INSPECTION_GROUP_USER_SUB = INSPECTION_GROUP_USER_ALL.split(",");
        String msg = "添加成功";
        String inUseName = "";
////        所有人员对应的检查人员职务编码
//        String INSPECTION_GROUP_DUTIES_ALL = pd.getString("INSPECTION_GROUP_DUTIES");
//        String[] INSPECTION_GROUP_DUTIES_SUB = INSPECTION_GROUP_DUTIES_ALL.split(",");
//        JSONArray jsonArray= JSONArray.parseArray(pd.getString("aa"));
//                .fromObject(pd.getString("aa"));
        try {
//            保存任务表信息
            for (int i = 0; i < INSPECTION_GROUP_USER_SUB.length; i++) {

                pd.put("INSPECTION_GROUP_USER", INSPECTION_GROUP_USER_SUB[i]);
//                pd.put("INSPECTION_GROUP_DUTIES", INSPECTION_GROUP_DUTIES_SUB[i]);
//                inspectionPlanService.DelInspectUser(pd);

                List<Map<String,Object>>userMap = inspectionUserService.getUserData(pd);
                if (null != userMap && userMap.size()>0){
                    inUseName += userMap.get(0).get("NAME")+" ";
                    continue;
                }else{
                    pd.put("ID", get32UUID());
                    inspectionPlanService.saveInspectUser(pd);
                }
            }
            pd.put("ID", "");
            inspectionNoticeService.editNoticeUser(pd);
            inspectionBorrowService.editBorrowUser(pd);
            if (null != inUseName && !inUseName.equals("")){
                msg = "添加成功，下列人员：" + inUseName + "已存在与列表中，请勿重复选择";
            }
            result.put("msg", msg);
            result.put("result", "success");
        } catch (Exception e) {
            result.put("msg", "添加失败");
            result.put("result", "failed");
        }
        return result;
    }


    @RequestMapping(value = "/delUser", method = RequestMethod.POST, produces = {"application/json"})
    @ApiOperation("删除人员表")
    public Map<String, Object> delUser(@RequestBody JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        result.put("msg", "删除成功");
        result.put("result", "success");
        try {
            inspectionPlanService.delUser(pd);
            inspectionNoticeService.editNoticeUser(pd);
            inspectionBorrowService.editBorrowUser(pd);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", "删除失败");
            result.put("result", "failed");
        }
        return result;
    }


    @RequestMapping(value = {"/getTalentPoolAll"}, method = RequestMethod.POST, produces = {"application/json"})
    @ApiOperation("获取人才库人员列表")
    public Map<String, Object> getTalentPoolAll(@RequestBody(required = false) JSONObject jsonob) {
        Map<String, Object> result = new HashMap<>();
        PageData pd = new PageData(jsonob);
        try {
            List<Map<String, Object>> data = inspectionPlanService.getTalentPoolAll(pd);
            result.put("rows", data);//rows键 存放每页记录 list
            result.put("msg", "查询成功");
            result.put("result", "success");
        } catch (Exception e) {
            result.put("msg", e.getMessage());
            result.put("result", "success");
        }
        return result;
    }


    @RequestMapping(value = "/editUserDuties", method = RequestMethod.POST, produces = {"application/json"})
    @ApiOperation("修改检查人员职务接口")
    public Map<String, Object> editUserDuties(@RequestBody JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        try {
//            判断是否有重复职务-主要针对组长和主检查
            Integer rs = isEditUserDuties(pd);
            if (null != rs && rs > 0) {
                result.put("msg", "修改失败！有重复人员，请修改后重试。");
                result.put("result", "false");
            } else {
                inspectionPlanService.editUserDuties(pd);
                editProc(pd);
                inspectionNoticeService.editNoticeUser(pd);
                inspectionBorrowService.editBorrowUser(pd);
                result.put("msg", "修改成功");
                result.put("result", "success");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", "修改失败");
            result.put("result", "failed");
        }
        return result;
    }

    private Integer isEditUserDuties(PageData pd) {
        List<Map<String, Object>> data = inspectionPlanService.isEditUserDuties(pd);
        if (null != data && data.size() > 0) {
            return data.size();
        }
        return 0;
    }

    private Integer checkRepeat(PageData pd) {
        List<Map<String, Object>> data = inspectionPlanService.checkRepeat(pd);
        if (null != data && data.size() > 0) {
            return data.size();
        }
        return 0;
    }

    private void editProc(PageData pd) {
        List<Map<String, Object>> groupMap = inspectionGroupService.getInspectionGroupData(pd);
        if (!groupMap.isEmpty()) {
            pd.put("INSPECTION_TASK_ID", groupMap.get(0).get("INSPECTION_TASK_ID"));
            List<Map<String, Object>> taskMap = inspectionTaskService.getInspectionTaskData(pd);
            if (!taskMap.isEmpty()) {
                String INSPECTION_TASK_BEGINTIME = taskMap.get(0).get("INSPECTION_TASK_BEGINTIME").toString();
                String INSPECTION_TASK_ENDTIME = taskMap.get(0).get("INSPECTION_TASK_ENDTIME").toString();
                if (null != INSPECTION_TASK_BEGINTIME && null != INSPECTION_TASK_ENDTIME
                        && !INSPECTION_TASK_BEGINTIME.equals("") && !INSPECTION_TASK_ENDTIME.equals("")) {
                    inspectionProcService.editProcActive(pd);
                }
            }
        }
    }

    /**
     * 下载被查国库统计表
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "下载被查国库统计表")
    @GetMapping(value = "/downLoadStatisticsTable")
    public void downLoadStatisticsTable(
            @ApiParam("当前任务ID：INSPECTION_PLAN_ID \n" +
                    "拼接报表表头字段数组：queryCols[]")
            @RequestBody(required = false) JSONObject param,
            HttpServletResponse response
    ) throws Exception {
        String title = "业务量和发现问题汇总表";
        PageData pd = this.getPageData(param);
        List<Map<String, Object>> planInfoList = inspectionPlanService.getCurPlanName(pd);
        String planTableNamePrefix = planInfoList.get(0).get("INSPECTION_PLAN_NAME").toString();
        String planType = planInfoList.get(0).get("INSPECTION_PLAN_TYPE").toString();
        String fileName = planTableNamePrefix + "_业务量和发现问题汇总表.xls";
        String filepath = saveDir ;
        String[] queryCols = pd.getString("queryCols").split(",|，");
        pd.put("columns", queryCols);
        /*1-7*/
        List<String> titlelist = new ArrayList<>();
        /*问题一类*/
        List<String> titlelist_2 = new ArrayList<>();
        /*问题一类统计项目*/
        List<String> titlelist_3 = new ArrayList<>();

        if( "005".equals(planType)){
            titlelist.add("被查国库");
            titlelist.add("凭证（张）");
            titlelist.add("附件（张）");
            titlelist.add("总账（户）");
            titlelist.add("分户账（户）");
            titlelist.add("报表（份）");
            titlelist.add("登记簿（册）");
            for (int i = 7, len = queryCols.length; i < len; i += 3) {
                String QUESTION_ID = queryCols[i].substring(0, queryCols[i].lastIndexOf("_"));
                String QUESTION_DSCR = inspectionStatisticsTableService.getQuestionDscrById(QUESTION_ID);
                titlelist_2.add(QUESTION_DSCR);
                titlelist_3.add("问题总数");
                titlelist_3.add("现场整改");
                titlelist_3.add("限期整改");
            }
        }
        if( "001".equals(planType) ){
            titlelist.add("被查国库");
            titlelist.add("报表（份）");
            titlelist.add("登记薄（册）");
            titlelist.add("分析报告（份）");
            titlelist.add("其他资料（份）");

            titlelist_2.add("国库收支存统计报表国库收支存统计报表");
            titlelist_2.add("退库报表");
            titlelist_2.add("计息核对");

            titlelist_3.add("一致");
            titlelist_3.add("不一致");
            titlelist_3.add("部分一致");
            titlelist_3.add("一致");
            titlelist_3.add("不一致");
            titlelist_3.add("部分一致");
            titlelist_3.add("一致");
            titlelist_3.add("不一致");
            titlelist_3.add("部分一致");

            for (int i = 14, len = queryCols.length; i < len; i += 3) {
                String QUESTION_ID = queryCols[i].substring(0, queryCols[i].lastIndexOf("_"));
                String QUESTION_DSCR = inspectionStatisticsTableService.getQuestionDscrById(QUESTION_ID);
                titlelist_2.add(QUESTION_DSCR);
                titlelist_3.add("问题总数");
                titlelist_3.add("现场整改");
                titlelist_3.add("限期整改");
            }
        }
        if( "006".equals(planType) ){
            titlelist.add("被查国库");
            for (int i = 1, len = queryCols.length; i < len; i += 3) {
                String QUESTION_ID = queryCols[i].substring(0, queryCols[i].lastIndexOf("_"));
                String QUESTION_DSCR = inspectionStatisticsTableService.getQuestionDscrById(QUESTION_ID);
                titlelist_2.add(QUESTION_DSCR);
                titlelist_3.add("问题总数");
                titlelist_3.add("现场整改");
                titlelist_3.add("限期整改");
            }
        }

        List<Map<String, Object>> data = inspectionPlanService.getStatisticsTableByPlanId(pd);
        CreateExcel_2.createInspectStatisticsTable(title, filepath, fileName, titlelist, titlelist_2, titlelist_3, queryCols, data, planType);
        FileDownload.fileDownload(response, filepath + fileName, fileName, this.getRequest());
    }

}

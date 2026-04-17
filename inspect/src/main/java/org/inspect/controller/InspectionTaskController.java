package org.inspect.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.inspect.BaseController;
import org.inspect.service.*;
import org.inspect.util.DateUtil;
import org.inspect.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@Api(tags="检查任务")
@RequestMapping(value = "/inspectionTaskController", produces = MediaType.APPLICATION_JSON_VALUE)
public class InspectionTaskController extends BaseController {

    @Autowired
    private InspectionTaskService inspectionTaskService;
    @Autowired
    private InspectionGroupService inspectionGroupService;
    @Autowired
    private InspectionProcService inspectionProcService;
    @Autowired
    private InspectionProcSubService inspectionProcSubService;
    @Autowired
    protected InspectionUserService inspectionUserService;

    @RequestMapping(value={"/addInspectionTask"} , method = RequestMethod.POST)
    @ApiOperation("新增检查任务")
    public Map<String,Object> addInspectionTask(@RequestBody(required = false) JSONObject param){
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        Integer rs = checkRepeat(pd);
        result.put("result", "success");
        result.put("msg", "新增成功！");
        if (null != rs && rs>0) {
            result.put("msg", "新增失败！该任务已存在，请修改后重试。");
            result.put("result", "false");
        }else{
            try {
                pd.put("INSPECTION_TASK_ID",get32UUID());
                String type = pd.getString("INSPECTION_TASK_TYPE");
                if (null !=  type && (type.equals("002")||type.equals("003")||type.equals("004")|| type.equals("008"))){
                    pd.put("bookorgcode", pd.getString("BOOKORG_ID"));
                    Map<String, Object> temp = inspectionTaskService.getGKbyBook(pd);
                    pd.put("INSPECTED_GUOKU_ID",temp.get("guoku_id"));
                    pd.put("INSPECTION_GUOKU", temp.get("guoku_id"));
                    pd.put("CREATE_TIME",DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
                    inspectionTaskService.addInspectionTask(pd);
                }else if (null != type && type.equals("007")) {
                    pd.put("bookorgcode", pd.getString("BOOKORG_ID"));
                    Map<String, Object> temp = inspectionTaskService.getGKbyBook(pd);
                    String bookOrgName = temp.get("bookorgname").toString();
                    String CHECK_ORG_DSCR = bookOrgName.substring(0, bookOrgName.lastIndexOf("行") + 1);

                    pd.put("INSPECTION_GUOKU", temp.get("guoku_id").toString());
                    pd.put("BOOKORG_DSCR", bookOrgName);
                    pd.put("CHECK_ORG_DSCR", CHECK_ORG_DSCR);
                    pd.put("CREATE_TIME",DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
                    inspectionTaskService.addInspectionTask(pd);
                    PageData pdGroup = new PageData();
                    pdGroup.put("INSPECTION_TASK_ID",pd.getString("INSPECTION_TASK_ID"));
                    pdGroup.put("INSPECTION_GROUP_ID",get32UUID());
                    pdGroup.put("INSPECTION_GROUP_NAME",'1');
                    pdGroup.put("INSPECTED_GUOKU_ID",pd.getString("INSPECTED_GUOKU_ID"));
                    inspectionGroupService.addInspectionGroup(pdGroup);
                }else {
                    pd.put("bookorgcode", pd.getString("BOOKORG_ID"));
                    Map<String, Object> temp = inspectionTaskService.getGKbyBook(pd);
                    pd.put("INSPECTED_GUOKU_ID",temp.get("guoku_id"));
                    pd.put("bookorgcode", pd.getString("INSPECTION_BOOKORG_ID"));
                    Map<String, Object> temp_2 = inspectionTaskService.getGKbyBook(pd);
                    pd.put("INSPECTION_GUOKU",temp_2.get("guoku_id"));
                    pd.put("CREATE_TIME",DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
                    inspectionTaskService.addInspectionTask(pd);
                    PageData pdGroup = new PageData();
                    pdGroup.put("INSPECTION_TASK_ID",pd.getString("INSPECTION_TASK_ID"));
                    pdGroup.put("INSPECTION_GROUP_ID",get32UUID());
                    pdGroup.put("INSPECTION_GROUP_NAME",'1');
                    pdGroup.put("INSPECTED_GUOKU_ID",pd.getString("INSPECTED_GUOKU_ID"));
                    inspectionGroupService.addInspectionGroup(pdGroup);
                }
                PageData pdProc = new PageData();
                pdProc.put("INSPECTION_TASK_TYPE",pd.getString("INSPECTION_TASK_TYPE"));
                pdProc.put("INSPECTION_TASK_ID",pd.getString("INSPECTION_TASK_ID"));
                inspectionProcService.addInspectionProc(pdProc);

            }catch (Exception e){
                result.put("msg", e.getMessage());
                result.put("result", "false");
                return result;
            }
        }
        return result;
    }

    private Integer checkRepeat(PageData pd){
        List<Map<String, Object>> data = inspectionTaskService.checkRepeat(pd);
        if (null != data && data.size() > 0) {
            return data.size();
        }
        return 0;
    }

    @RequestMapping(value = {"/getInspectionTaskPage"}, method = RequestMethod.POST)
    @ApiOperation("获取任务列表(分页)")
    public Object getInspectionTaskPage(@RequestBody(required = false) JSONObject param){
        Map<String, Object> jsonMap = new HashMap<>();
        try{
            PageData pd = this.getPageData(param);
            Integer pageNo = (Integer.parseInt(pd.getString("pageNo"))-1)*Integer.parseInt(pd.getString("pageSize"));
            pd.put("page",pageNo);
            pd.put("rows",Integer.parseInt(pd.getString("pageSize")));
            List<Map<String, Object>> result = inspectionTaskService.getInspectionTaskPage(pd);
            Integer count = inspectionTaskService.getInspectionTaskCount(pd);
            jsonMap.put("total", count);//total键 存放总记录数，必须的
            jsonMap.put("rows", result);//rows键 存放每页记录 list
            jsonMap.put("result","success");
        }catch (Exception e){
            jsonMap.put("result","false");
            jsonMap.put("msg",e.getMessage());
        }
        return jsonMap;
    }

    @RequestMapping(value={"/getGKbyBook"} , method = RequestMethod.POST)
    @ApiOperation("跟据核算主体取国库")
    public Object getGKbyBook(@RequestBody(required = false) JSONObject param){
        Map<String, Object> jsonMap = new HashMap<>();
        try{
            PageData pd = this.getPageData(param);
            jsonMap.put("rows",inspectionTaskService.getGKbyBook(pd));
            jsonMap.put("result","success");
        }catch (Exception e){
            jsonMap.put("result","false");
            jsonMap.put("msg",e.getMessage());
        }
        return jsonMap;
    }

    @RequestMapping(value={"/getBookbyGuokuId"} , method = RequestMethod.POST)
    @ApiOperation("根据国库ID查询核算主体名称")
    public Object getBookbyGuokuId(@RequestBody(required = false) JSONObject param){
        Map<String, Object> jsonMap = new HashMap<>();
        try{
            PageData pd = this.getPageData(param);
            jsonMap.put("rows",inspectionTaskService.getBookbyGuokuId(pd));
            jsonMap.put("result","success");
        }catch (Exception e){
            jsonMap.put("result","false");
            jsonMap.put("msg",e.getMessage());
        }
        return jsonMap;
    }

    @RequestMapping(value={"/getInspectionTaskData"} , method = RequestMethod.POST)
    @ApiOperation("获取任务列表(不分页)")
    public Object getInspectionTaskData(@RequestBody(required = false) JSONObject param){
        Map<String, Object> jsonMap = new HashMap<>();
        try{
            PageData pd = this.getPageData(param);
            jsonMap.put("rows", inspectionTaskService.getInspectionTaskData(pd));
            jsonMap.put("result","success");
        }catch (Exception e){
            jsonMap.put("result","false");
            jsonMap.put("msg",e.getMessage());
        }
        return jsonMap;
    }


    @RequestMapping(value={"/editInspectionTask"} , method = RequestMethod.POST)
    @ApiOperation("修改任务")
    public Map<String,Object>editInspectionTask(@RequestBody(required = false) JSONObject param){
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        result.put("msg", "修改成功！");
        result.put("result", "success");
        Integer rn = checkRepeat(pd);
        if(null != rn && rn>0){
            result.put("msg", "修改失败！该任务已存在，请修改后重试。");
            result.put("result", "false");
        }else try {
            String INSPECTION_TASK_BEGINTIME = pd.getString("INSPECTION_TASK_BEGINTIME");
            String INSPECTION_TASK_ENDTIME = pd.getString("INSPECTION_TASK_ENDTIME");
            if (null != INSPECTION_TASK_BEGINTIME && null != INSPECTION_TASK_ENDTIME
                    && !INSPECTION_TASK_BEGINTIME.equals("") && !INSPECTION_TASK_ENDTIME.equals("")) {
                inspectionProcService.editProcActive(pd);
            }
            inspectionTaskService.editInspectionTask(pd);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", e.getMessage());
            result.put("result", "false");
        }
        return result;
    }

    @RequestMapping(value={"/editTaskLock"} , method = RequestMethod.POST)
    @ApiOperation("修改任务")
    public Map<String,Object>editTaskLock(@RequestBody(required = false) JSONObject param){
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        result.put("msg", "修改成功！");
        result.put("result", "success");
        try {
            inspectionTaskService.editTaskLock(pd);
        } catch (Exception e) {
            result.put("msg", e.getMessage());
            result.put("result", "false");
        }
        return result;
    }

    @RequestMapping(value={"/delInspectionTask"} , method = RequestMethod.GET)
    @ApiOperation("删除任务")
    public Map<String,Object>delInspectionTask(@RequestBody(required = false) JSONObject param){
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        result.put("msg", "保存成功！");
        result.put("result", "success");
        try{
            String ID = pd.getString("INSPECTION_TASK_ID");
            if (null != ID && !ID.equals("")){
                String []IDS = ID.split(",");
                for (int i = 0;i<IDS.length;i++){
                    PageData temp = new PageData();
                    temp.put("INSPECTION_TASK_ID",IDS[i]);
                    inspectionTaskService.delInspectionTask(temp);
                }
            }
        }catch (Exception e){
            result.put("msg", e.getMessage());
            result.put("result", "false");
        }
        return result;
    }
}

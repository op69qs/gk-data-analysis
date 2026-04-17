package org.inspect.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.inspect.BaseController;
import org.inspect.service.InspectionApprovalService;
import org.inspect.service.InspectionCaseService;
import org.inspect.service.InspectionReformService;
import org.inspect.util.DateUtil;
import org.inspect.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@Api(tags="审批")
@RequestMapping(value = "/inspectionApprovalController", produces = MediaType.APPLICATION_JSON_VALUE)
public class InspectionApprovalController extends BaseController {

    @Autowired
    private InspectionApprovalService inspectionApprovalService;
    @Autowired
    private InspectionCaseService inspectionCaseService;
    @Autowired
    private InspectionReformService inspectionReformService;


    @RequestMapping(value = {"/getInspectionApprovalPage"}, method = RequestMethod.POST)
    @ApiOperation("获取列表(分页)")
    public Object getInspectionApprovalPage(@RequestBody(required = false) JSONObject param){
        Map<String, Object> jsonMap = new HashMap<>();
        try{
            PageData pd = this.getPageData(param);
            Integer pageNo = (Integer.parseInt(pd.getString("pageNo"))-1)*Integer.parseInt(pd.getString("pageSize"));
            pd.put("page",pageNo);
            pd.put("rows",Integer.parseInt(pd.getString("pageSize")));
            List<Map<String, Object>> result = inspectionApprovalService.getInspectionApprovalPage(pd);
            Integer count = inspectionApprovalService.getInspectionApprovalCount(pd);
            jsonMap.put("total", count);//total键 存放总记录数，必须的
            jsonMap.put("rows", result);//rows键 存放每页记录 list
            jsonMap.put("result","success");
        }catch (Exception e){
            jsonMap.put("result","false");
            jsonMap.put("msg",e.getMessage());
        }
        return jsonMap;
    }

    @RequestMapping(value={"/getInspectionApprovalData"} , method = RequestMethod.POST)
    @ApiOperation("获取列表(不分页)")
    public Object getInspectionApprovalData(@RequestBody(required = false) JSONObject param){
        Map<String, Object> jsonMap = new HashMap<>();
        try{
            PageData pd = this.getPageData(param);
            jsonMap.put("rows", inspectionApprovalService.getInspectionApprovalData(pd));
            jsonMap.put("result","success");
        }catch (Exception e){
            jsonMap.put("result","false");
            jsonMap.put("msg",e.getMessage());
        }
        return jsonMap;
    }


    @RequestMapping(value={"/editInspectionApproval"} , method = RequestMethod.POST)
    @ApiOperation("审批")
    public Map<String,Object>editInspectionApproval(@RequestBody(required = false) JSONObject param){
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        result.put("msg", "审批成功！");
        result.put("result", "success");
        String type =  pd.get("type").toString();
        try {
            PageData pd1 = new PageData();
            pd1.put("type", type);
            pd1.put("step", pd.get("step"));
            List<Map<String, Object>> list = inspectionApprovalService.getAppravalProcess(pd1);
            if (!list.isEmpty() && list.size() > 0) {
                pd1.put("id", get32UUID());
                pd1.put("auth_id", pd.get("auth_id"));
                pd1.put("subject_id", pd.get("subject_id"));
                pd1.put("add_user", pd.get("ADD_USER"));
                pd1.put("add_time", pd.get("ADD_TIME"));
                pd1.put("app_role", list.get(0).get("role"));
                pd1.put("app_step", list.get(0).get("step"));
                pd1.put("app_org", list.get(0).get("organ"));
                inspectionApprovalService.addInspectionApproval(pd1);
            }else{
//                 修改 inspection_case  的APPROVAL_STATE    结果  app_result
                inspectionApprovalService.updateApproval(pd);
            }
            pd.put("app_time", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
            inspectionApprovalService.editInspectionApproval(pd);

            if (null != type && type.equals("1")){
                //案例库
                pd.put("ID", pd.get("subject_id"));
                List<Map<String,Object>>caseMap = inspectionCaseService.getInspectionCaseData(pd);
                if (null != caseMap && !caseMap.isEmpty()){
                    String REFORM_ID = caseMap.get(0).get("REFORM_ID").toString();
                    PageData refPd = new PageData();
                    refPd.put("ID",REFORM_ID);
                    if (pd.get("app_result").toString().equals("0")){
                        refPd.put("IS_CASE","0");
                    }else{
                        refPd.put("IS_CASE","1");
                    }
                    inspectionReformService.editReform(refPd);
                }
            }
        } catch (Exception e) {
            result.put("msg", e.getMessage());
            result.put("result", "false");
        }
        return result;
    }

    @RequestMapping(value={"/addInspectionApproval"} , method = RequestMethod.POST)
    @ApiOperation("新增")
    public Map<String,Object>addInspectionApproval(@RequestBody(required = false) JSONObject param){
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        result.put("msg", "新增成功！");
        result.put("result", "success");
        try {
            pd.put("id",get32UUID());
            pd.put("auth_id",get32UUID());
            inspectionApprovalService.addInspectionApproval(pd);
        } catch (Exception e) {
            result.put("msg", e.getMessage());
            result.put("result", "false");
        }
        return result;
    }

    @RequestMapping(value={"/delInspectionApproval"} , method = RequestMethod.POST)
    @ApiOperation("删除任务")
    public Map<String,Object>delInspectionApproval(@RequestBody(required = false) JSONObject param){
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        result.put("msg", "删除成功！");
        result.put("result", "success");
        try {
            inspectionApprovalService.delInspectionApproval(pd);
        } catch (Exception e) {
            result.put("msg", e.getMessage());
            result.put("result", "false");
        }
        return result;
    }
}

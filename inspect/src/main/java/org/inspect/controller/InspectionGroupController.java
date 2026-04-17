package org.inspect.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.inspect.BaseController;
import org.inspect.service.*;
import org.inspect.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@Api(tags="检查组")
@RequestMapping(value = "/inspectionGroupController", produces = MediaType.APPLICATION_JSON_VALUE)
public class InspectionGroupController extends BaseController {

    @Autowired
    private InspectionGroupService inspectionGroupService;

    @Autowired
    private InspectionProcService inspectionProcService;

    @Autowired
    private InspectionProcSubService inspectionProcSubService;


    /**
     * 检查组信息提交
     * @param param
     * @return
     */
    @ApiOperation(value = "检查组信息提交")
    @PostMapping(value = "/submitGroupInfo")
    public Map<String,Object> submitGroupInfo(
            @RequestBody(required = false) JSONObject param
    ){
        Map<String, Object> res = new HashMap<>();
        PageData pd = this.getPageData(param);
        pd.put("INSPECTION_TASK_ID", pd.getString("TASK_ID"));
        inspectionProcService.editProcInfo(pd);
        inspectionProcSubService.editProcBySubProc(pd);
        res.put("result", "success");
        res.put("msg", "提交成功！");
        return res;
    }

    @RequestMapping(value = "addInspectionGroup")
    @ApiOperation("新增检查组")
    public Map<String,Object> addInspectionGroup(@RequestBody(required = false) JSONObject param){
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
                pd.put("INSPECTION_GROUP_ID",get32UUID());
                inspectionGroupService.addInspectionGroup(pd);
            }catch (Exception e){
                result.put("msg", e.getMessage());
                result.put("result", "false");
                return result;
            }
        }
        return result;
    }

    private Integer checkRepeat(PageData pd){
        List<Map<String, Object>> data = inspectionGroupService.checkRepeat(pd);
        if (null != data && data.size() > 0) {
            return data.size();
        }
        return 0;
    }

    @RequestMapping(value = {"/getInspectionGroupPage"}, method = RequestMethod.POST)
    @ApiOperation("获取检查组(分页)")
    public Object getInspectionGroupPage(@RequestBody(required = false) JSONObject param){
        Map<String, Object> jsonMap = new HashMap<>();
        try{
            PageData pd = this.getPageData(param);
            Integer pageNo = (Integer.parseInt(pd.getString("pageNo"))-1)*Integer.parseInt(pd.getString("pageSize"));
            pd.put("page",pageNo);
            pd.put("rows",Integer.parseInt(pd.getString("pageSize")));
            List<Map<String, Object>> result = inspectionGroupService.getInspectionGroupPage(pd);
            Integer count = inspectionGroupService.getInspectionGroupCount(pd);
            jsonMap.put("total", count);//total键 存放总记录数，必须的
            jsonMap.put("rows", result);//rows键 存放每页记录 list
            jsonMap.put("result","success");
        }catch (Exception e){
            jsonMap.put("result","false");
            jsonMap.put("msg",e.getMessage());
        }
        return jsonMap;
    }

    @RequestMapping("/getInspectionGroupData")
    @ApiOperation("获取检查组(不分页)")
    public Object getInspectionGroupData(@RequestBody(required = false) JSONObject param){
        Map<String, Object> jsonMap = new HashMap<>();
        try{
            PageData pd = this.getPageData(param);
            jsonMap.put("rows", inspectionGroupService.getInspectionGroupData(pd));
            jsonMap.put("result","success");
        }catch (Exception e){
            jsonMap.put("result","false");
            jsonMap.put("msg",e.getMessage());
        }
        return jsonMap;
    }


    @RequestMapping(value = "/editInspectionGroup")
    @ApiOperation("修改检查组")
    public Map<String,Object>editInspectionGroup(@RequestBody(required = false) JSONObject param){
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        result.put("msg", "修改成功！");
        result.put("result", "success");
        Integer rn = checkRepeat(pd);
        if(null != rn && rn>0){
            result.put("msg", "修改失败！该任务已存在，请修改后重试。");
            result.put("result", "false");
        }else{
            try{
                inspectionGroupService.editInspectionGroup(pd);
            }catch (Exception e){
                result.put("msg", e.getMessage());
                result.put("result", "false");
            }
        }
        return result;
    }

    @RequestMapping(value = "/delInspectionGroup")
    @ApiOperation("删除检查组")
    public Map<String,Object>delInspectionGroup(@RequestBody(required = false) JSONObject param){
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        result.put("msg", "保存成功！");
        result.put("result", "success");
        try{
            String ID = pd.getString("INSPECTION_GROUP_ID");
            if (null != ID && !ID.equals("")){
                String []IDS = ID.split(",");
                for (int i = 0;i<IDS.length;i++){
                    PageData temp = new PageData();
                    temp.put("INSPECTION_GROUP_ID",IDS[i]);
                    inspectionGroupService.delInspectionGroup(temp);
                }
            }
        }catch (Exception e){
            result.put("msg", e.getMessage());
            result.put("result", "false");
        }
        return result;
    }
}

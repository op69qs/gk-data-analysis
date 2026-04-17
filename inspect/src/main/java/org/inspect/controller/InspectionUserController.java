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
@Api(tags="检查组人员")
@RequestMapping(value = "/inspectionUserController", produces = MediaType.APPLICATION_JSON_VALUE)
public class InspectionUserController extends BaseController {

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


    @RequestMapping(value = "addInspectionUser")
    @ApiOperation("新增检查组人员")
    public Map<String,Object> addInspectionUser(@RequestBody(required = false) JSONObject param){
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
//        Integer rs = checkRepeat(pd);
        result.put("result", "success");
        result.put("msg", "新增成功！");
//        if (null != rs && rs>0) {
//            result.put("msg", "新增失败！有重复人员，请修改后重试。");
//            result.put("result", "false");
//        }else{
            try {
                pd.put("ID",get32UUID());
                inspectionUserService.addInspectionUser(pd);
            }catch (Exception e){
                result.put("msg", e.getMessage());
                result.put("result", "false");
                return result;
            }
//        }
        return result;
    }

    private Integer checkRepeat(PageData pd){
        List<Map<String, Object>> data = inspectionUserService.checkRepeat(pd);
        if (null != data && data.size() > 0) {
            return data.size();
        }
        return 0;
    }

    @RequestMapping(value = {"/getInspectionUserPage"}, method = RequestMethod.POST)
    @ApiOperation("获取检查组人员(分页)")
    public Object getInspectionUserPage(@RequestBody(required = false) JSONObject param){
        Map<String, Object> jsonMap = new HashMap<>();
        try{
            PageData pd = this.getPageData(param);
            Integer pageNo = (Integer.parseInt(pd.getString("pageNo"))-1)*Integer.parseInt(pd.getString("pageSize"));
            pd.put("page",pageNo);
            pd.put("rows",Integer.parseInt(pd.getString("pageSize")));
            List<Map<String, Object>> result = inspectionUserService.getInspectionUserPage(pd);
            Integer count = inspectionUserService.getInspectionUserCount(pd);
            jsonMap.put("total", count);//total键 存放总记录数，必须的
            jsonMap.put("rows", result);//rows键 存放每页记录 list
            jsonMap.put("result","success");
        }catch (Exception e){
            jsonMap.put("result","false");
            jsonMap.put("msg",e.getMessage());
        }
        return jsonMap;
    }

    @RequestMapping("/isRepeat")
    @ApiOperation("检查是否重复")
    public Object isRepeat(@RequestBody(required = false) JSONObject param){
        Map<String, Object> jsonMap = new HashMap<>();
        try{
            PageData pd = this.getPageData(param);
            List<Map<String, Object>> data = inspectionUserService.checkRepeat(pd);
            if (null != data && data.size()>0){
                jsonMap.put("rows", data);
                jsonMap.put("result","false");
            }else{
                jsonMap.put("result","success");
            }
        }catch (Exception e){
            jsonMap.put("result","false");
            jsonMap.put("msg",e.getMessage());
        }
        return jsonMap;
    }

    @RequestMapping("/getInspectionUserData")
    @ApiOperation("获取检查组人员(不分页)")
    public Object getInspectionUserData(@RequestBody(required = false) JSONObject param){
        Map<String, Object> jsonMap = new HashMap<>();
        try{
            PageData pd = this.getPageData(param);
            jsonMap.put("rows", inspectionUserService.getInspectionUserData(pd));
            jsonMap.put("result","success");
        }catch (Exception e){
            jsonMap.put("result","false");
            jsonMap.put("msg",e.getMessage());
        }
        return jsonMap;
    }

    @RequestMapping("/getUserBySysId")
    @ApiOperation("根据系统用户编码获取检查组人员(不分页)")
    public Object getUserBySysId(@RequestBody(required = false) JSONObject param){
        Map<String, Object> jsonMap = new HashMap<>();
        try{
            PageData pd = this.getPageData(param);
            jsonMap.put("rows", inspectionUserService.getUserBySysId(pd));
            jsonMap.put("result","success");
        }catch (Exception e){
            jsonMap.put("result","false");
            jsonMap.put("msg",e.getMessage());
        }
        return jsonMap;
    }

    @RequestMapping("/getRoleBySysId")
    @ApiOperation("根据系统用户编码获取权限")
    public Object getRoleBySysId(@RequestBody(required = false) JSONObject param){
        Map<String, Object> jsonMap = new HashMap<>();
        try{
            PageData pd = this.getPageData(param);
            jsonMap.put("rows", inspectionUserService.getRoleBySysId(pd));
            jsonMap.put("result","success");
        }catch (Exception e){
            jsonMap.put("result","false");
            jsonMap.put("msg",e.getMessage());
        }
        return jsonMap;
    }

    @RequestMapping("/getUserData")
    @ApiOperation("获取检查组人员(不分页)流程中使用")
    public Object getUserData(@RequestBody(required = false) JSONObject param){
        Map<String, Object> jsonMap = new HashMap<>();
        try{
            PageData pd = this.getPageData(param);
            jsonMap.put("rows", inspectionUserService.getUserData(pd));
            jsonMap.put("result","success");
        }catch (Exception e){
            jsonMap.put("result","false");
            jsonMap.put("msg",e.getMessage());
        }
        return jsonMap;
    }


    @RequestMapping(value = "/editInspectionUser")
    @ApiOperation("修改检查组人员")
    public Map<String,Object>editInspectionUser(@RequestBody(required = false) JSONObject param){
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        result.put("msg", "修改成功！");
        result.put("result", "success");
//        Integer rn = checkRepeat(pd);
//        if(null != rn && rn>0){
//            result.put("msg", "修改失败！人员重复，请修改后重试。");
//            result.put("result", "false");
//        }else{
            try{
                inspectionUserService.editInspectionUser(pd);
            }catch (Exception e){
                result.put("msg", e.getMessage());
                result.put("result", "false");
            }
//        }
        return result;
    }
    @RequestMapping(value = "/delInspectionUser")
    @ApiOperation("删除检查组人员")
    public Map<String,Object>delInspectionUser(@RequestBody(required = false) JSONObject param){
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        result.put("msg", "保存成功！");
        result.put("result", "success");
        try{
            String ID = pd.getString("ID");
            String INSPECTION_GROUP_USER = pd.getString("INSPECTION_GROUP_USER");
            if (null != ID && !ID.equals("")){
                String []IDS = ID.split(",");
                for (int i = 0;i<IDS.length;i++){
                    PageData temp = new PageData();
                    temp.put("ID",IDS[i]);
                    temp.put("INSPECTION_GROUP_ID",pd.getString("INSPECTION_GROUP_ID"));
                    inspectionUserService.delInspectionUser(temp);
                }
            }
            if (null != INSPECTION_GROUP_USER && !INSPECTION_GROUP_USER.equals("")){
                String []INSPECTION_GROUP_USERS = INSPECTION_GROUP_USER.split(",");
                for (int i = 0;i<INSPECTION_GROUP_USERS.length;i++){
                    PageData temp = new PageData();
                    temp.put("INSPECTION_GROUP_USER",INSPECTION_GROUP_USERS[i]);
                    temp.put("INSPECTION_GROUP_ID",pd.getString("INSPECTION_GROUP_ID"));
                    inspectionUserService.delInspectionUser(temp);
                }
            }
        }catch (Exception e){
            result.put("msg", e.getMessage());
            result.put("result", "false");
        }
        return result;
    }
}

package org.inspect.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.inspect.BaseController;
import org.inspect.service.InspectionBorrowService;
import org.inspect.service.InspectionProcSubService;
import org.inspect.service.InspectionTaskService;
import org.inspect.service.LegalBorrowService;
import org.inspect.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@Api(tags="执法调阅清单")
@RequestMapping(value = "/legalBorrowController", produces = MediaType.APPLICATION_JSON_VALUE)
public class LegalBorrowController extends BaseController {

    @Autowired
    private LegalBorrowService legalBorrowService;
    @Autowired
    private InspectionProcSubService inspectionProcSubService;

    @Value("${TEMPLATE_FILE_PATH}")
    private  String saveDir;

    /*@PostMapping(value = "/addInspectProjectName")
    @ApiOperation("检查项目")
    public Map<String,Object> addInspectProjectName(
            @RequestBody(required = false) JSONObject param
    ){
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        List<Map<String,Object>> dataList = legalBorrowService.getLegalBorrowData(pd);
        if( dataList != null &&  dataList.size() > 0 ){
            legalBorrowService.editInspectProjectName(pd);
        } else {
            pd.put("id", "9999");
            legalBorrowService.addLegalBorrow(pd);
        }
        result.put("result", "success");
        result.put("msg", "保存成功");
        return result;
    }*/

    @RequestMapping(value={"/addLegalBorrow"} , method = RequestMethod.POST)
    @ApiOperation("新增")
    public Map<String,Object> addLegalBorrow(@RequestBody(required = false) JSONObject param){
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        /*PageData pdSub = new PageData();*/
        result.put("result", "success");
        result.put("msg", "新增成功！");
        try {
            /*pdSub.put("id", "9999");
            legalBorrowService.delLegalBorrow(pd);*/
            pd.put("id",get32UUID());
            legalBorrowService.addLegalBorrow(pd);

            String id = pd.getString("PROC_ID");
            PageData tempPd = new PageData();
            tempPd.put("ID",id);
            tempPd.put("INSPECTION_PROCESS_SUB_SIGN","0");
            tempPd.put("FINISH_TIME",DateUtil. getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
            inspectionProcSubService.editInspectionProcSub(tempPd);

        }catch (Exception e){
            result.put("msg", e.getMessage());
            result.put("result", "false");
               return result;
        }

        return result;
    }

    @RequestMapping(value={"/getLegalBorrowData"} , method = RequestMethod.POST)
    @ApiOperation("查")
    public Object getLegalBorrowData(@RequestBody(required = false) JSONObject param){
        Map<String, Object> jsonMap = new HashMap<>();
        try{
            PageData pd = this.getPageData(param);
            jsonMap.put("rows", legalBorrowService.getLegalBorrowData(pd));
            jsonMap.put("result","success");
        }catch (Exception e){
            jsonMap.put("result","false");
            jsonMap.put("msg",e.getMessage());
        }
        return jsonMap;
    }


    @RequestMapping(value={"/editLegalBorrow"} , method = RequestMethod.POST)
    @ApiOperation("修改")
    public Map<String,Object>editLegalBorrow(@RequestBody(required = false) JSONObject param){
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        result.put("msg", "修改成功！");
        result.put("result", "success");
        try{
            legalBorrowService.editLegalBorrow(pd);
        }catch (Exception e){
            result.put("msg", e.getMessage());
            result.put("result", "false");
        }
        return result;
    }

    @RequestMapping(value={"/delLegalBorrow"} , method = RequestMethod.GET)
    @ApiOperation("删除")
    public Map<String,Object>delLegalBorrow(@RequestBody(required = false) JSONObject param){
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        result.put("msg", "保存成功！");
        result.put("result", "success");
        try{
            String ID = pd.getString("id");
            if (null != ID && !ID.equals("")){
                String []IDS = ID.split(",");
                for (int i = 0;i<IDS.length;i++){
                    PageData temp = new PageData();
                    temp.put("id",IDS[i]);
                    legalBorrowService.delLegalBorrow(temp);
                }
            }
        }catch (Exception e){
            result.put("msg", e.getMessage());
            result.put("result", "false");
        }
        return result;
    }

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    @ApiOperation("下载")
    public void download(@RequestBody(required = false) JSONObject param,HttpServletResponse response) {
        PageData pd = this.getPageData(param);
        List<Map<String,Object>> list = legalBorrowService.getLegalBorrowData(pd);

        String filepath = saveDir+pd.getString("TASK_ID")+"/"+pd.getString("PROC_ID")+"/";
        String filename = "";
        List<String> titlelist = new ArrayList<>();

        titlelist.add("资料名称");
        titlelist.add("数量");
        titlelist.add("资料日期");
        titlelist.add("调阅资料日期");
        titlelist.add("调阅人签名");
        titlelist.add("归还日期");
        titlelist.add("收回人签名");

        List<String> zdlist = new ArrayList<>();

        zdlist.add("data_name");
        zdlist.add("num");
        zdlist.add("data_date");
        zdlist.add("borrow_date");
        zdlist.add("borrow_user");
        zdlist.add("return_date");
        zdlist.add("return_user");
        try{
            filename = "legalBorrowWithName.xls";
            CreateLegalBorrowExcel.createExcel(list.get(0).get("title").toString(),filepath,filename,titlelist,zdlist,list);
        }catch (Exception e){
            e.printStackTrace();
        }
        if (null != list && list.size()>0){
            for (int i =0;i<list.size();i++){
                list.get(i).put("borrow_user","");
            }
        }
        try{
            filename = "legalBorrow.xls";
            CreateLegalBorrowExcel.createExcel(list.get(0).get("title").toString(),filepath,filename,titlelist,zdlist,list);
            FileDownload.fileDownload(response, filepath+filename, "执法检查调阅资料清单.xls",this.getRequest());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

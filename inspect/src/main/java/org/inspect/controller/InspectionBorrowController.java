package org.inspect.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.JdbcType;
import org.inspect.BaseController;
import org.inspect.model.BorrowData;
import org.inspect.service.*;
import org.inspect.util.CreateExcel;
import org.inspect.util.DateUtil;
import org.inspect.util.FileDownload;
import org.inspect.util.PageData;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@Api(tags="调阅清单")
@RequestMapping(value = "/inspectionBorrowController", produces = MediaType.APPLICATION_JSON_VALUE)
public class InspectionBorrowController extends BaseController {

    @Autowired
    private InspectionBorrowService inspectionBorrowService;
    @Autowired
    private InspectionTaskService inspectionTaskService;
    @Autowired
    private InspectionProcSubService inspectionProcSubService;

    @Value("${TEMPLATE_FILE_PATH}")
    private  String saveDir;

    @RequestMapping(value={"/addInspectionBorrow"} , method = RequestMethod.POST)
    @ApiOperation("新增")
    public Map<String,Object> addInspectionBorrow(@RequestBody(required = false) JSONObject param){
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        Integer rs = checkRepeat(pd);
        result.put("result", "success");
        result.put("msg", "新增成功！");
        if (null != rs && rs>0) {
            result.put("msg", "新增失败！该资料已存在，请修改后重试。");
            result.put("result", "false");
        }else{
            try {
                pd.put("id",get32UUID());
                PageData pdTemp = new PageData();
                pdTemp.put("TASK_ID",pd.get("TASK_ID"));
                List<Map<String,Object>>borrowList = inspectionBorrowService.getInspectionBorrowData(pdTemp);
                if (null != borrowList && borrowList.size()>0){
                    pd.put("borrow_index",Integer.parseInt(borrowList.get(borrowList.size()-1).get("borrow_index").toString())+2);
                }
                inspectionBorrowService.addInspectionBorrow(pd);
            }catch (Exception e){
                result.put("msg", e.getMessage());
                result.put("result", "false");
                return result;
            }
        }
        return result;
    }

    private Integer checkRepeat(PageData pd){
        List<Map<String, Object>> data = inspectionBorrowService.checkRepeat(pd);
        if (null != data && data.size() > 0) {
            return data.size();
        }
        return 0;
    }

    @RequestMapping(value={"/getInspectionBorrowData"} , method = RequestMethod.POST)
    @ApiOperation("查")
    public Object getQuestionBankData(@RequestBody(required = false) JSONObject param){
        Map<String, Object> jsonMap = new HashMap<>();
        try{
            PageData pd = this.getPageData(param);
            jsonMap.put("rows", inspectionBorrowService.getInspectionBorrowData(pd));
            jsonMap.put("result","success");
        }catch (Exception e){
            jsonMap.put("result","false");
            jsonMap.put("msg",e.getMessage());
        }
        return jsonMap;
    }


    @RequestMapping(value={"/editInspectionBorrow"} , method = RequestMethod.POST)
    @ApiOperation("修改")
    public Map<String,Object>editInspectionBorrow(@RequestBody(required = false) JSONObject param){
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        result.put("msg", "修改成功！");
        result.put("result", "success");
        Integer rn = checkRepeat(pd);
        if(null != rn && rn>0){
            result.put("msg", "修改失败！该资料已存在，请修改后重试。");
            result.put("result", "false");
        }else{
            try{
                inspectionBorrowService.editInspectionBorrow(pd);
            }catch (Exception e){
                result.put("msg", e.getMessage());
                result.put("result", "false");
            }
        }
        return result;
    }

    @RequestMapping(value={"/editBorrowCharge"} , method = RequestMethod.POST)
    @ApiOperation("修改")
    public Map<String,Object>editBorrowCharge(@RequestBody(required = false) JSONObject param){
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        result.put("msg", "保存成功！");
        result.put("result", "success");
        try{
            inspectionBorrowService.editBorrowCharge(pd);
            pd.put("ID",pd.getString("PROC_ID"));
            pd.put("INSPECTION_PROCESS_SUB_SIGN","0");
            pd.put("FINISH_TIME", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
            inspectionProcSubService.editInspectionProcSub(pd);
        }catch (Exception e){
            result.put("msg", e.getMessage());
            result.put("result", "false");
        }
        return result;
    }

    @RequestMapping(value={"/delInspectionBorrow"} , method = RequestMethod.GET)
    @ApiOperation("删除")
    public Map<String,Object>delInspectionBorrow(@RequestBody(required = false) JSONObject param){
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
                    inspectionBorrowService.delInspectionBorrow(temp);
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
        List<Map<String,Object>> list = inspectionBorrowService.getInspectionBorrowData(pd);

        pd.put("INSPECTION_TASK_ID",pd.getString("TASK_ID"));
        List<Map<String,Object>> mapTask = inspectionTaskService.getInspectionTaskData(pd);

        String type = "";
        if (null != mapTask && !mapTask.isEmpty()) {
            type = mapTask.get(0).get("INSPECTION_TASK_TYPE").toString();
        }

        String filepath = saveDir+pd.getString("TASK_ID")+"/"+pd.getString("PROC_ID")+"/";
        String filename = "";
        List<String> titlelist = new ArrayList<>();
        titlelist.add("序号");
        titlelist.add("资料名称");
        titlelist.add("数量");
        titlelist.add("资料日期");
        titlelist.add("调阅资料日期");
        titlelist.add("调阅人签名");
        titlelist.add("归还日期");
        titlelist.add("收回人签名");

        List<String> zdlist = new ArrayList<>();
        zdlist.add("borrow_index");
        zdlist.add("data_name");
        zdlist.add("num");
        zdlist.add("data_date");
        zdlist.add("borrow_date");
        zdlist.add("borrow_user");
        zdlist.add("return_date");
        zdlist.add("return_user");
        try{
            filename = "borrowListWithName.xls";
            CreateExcel.createExcel(list.get(0).get("title").toString(),filepath,filename,titlelist,zdlist,list,list.get(0).get("inspected_charge").toString(),list.get(0).get("leader").toString(),type);
        }catch (Exception e){
            e.printStackTrace();
        }
        if (null != list && list.size()>0){
            for (int i =0;i<list.size();i++){
                list.get(i).put("borrow_index",i+1);
                list.get(i).put("borrow_user","");
            }
        }
        try{
            filename = "borrowList.xls";
            CreateExcel.createExcel(list.get(0).get("title").toString(),filepath,filename,titlelist,zdlist,list,"","",type);
            FileDownload.fileDownload(response, filepath+filename, "调阅清单.xls",this.getRequest());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

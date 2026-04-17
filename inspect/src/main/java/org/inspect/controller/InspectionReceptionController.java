package org.inspect.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.inspect.BaseController;
import org.inspect.service.InspectionProcSubService;
import org.inspect.service.InspectionReceptionService;
import org.inspect.util.DateUtil;
import org.inspect.util.FileDownload;
import org.inspect.util.PageData;
import org.inspect.util.Xml2XmlDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@Api(tags="公务接待函")
@RequestMapping(value = "/inspectionReceptionController", produces = MediaType.APPLICATION_JSON_VALUE)
public class InspectionReceptionController extends BaseController {

    @Autowired
    private InspectionReceptionService inspectionReceptionService;
    @Autowired
    private InspectionProcSubService inspectionProcSubService;

    @Value("${TEMPLATE_FILE_PATH}")
    private  String saveDir;

    @RequestMapping(value = "addInspectionReception")
    @ApiOperation("新增")
    public Map<String,Object> addInspectionReception(@RequestBody(required = false) JSONObject param){
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        result.put("result", "success");
        result.put("msg", "新增成功！");
        try {
            pd.put("ID",get32UUID());
            pd.put("update_time",DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
            inspectionReceptionService.addInspectionReception(pd);

            //新增文件
            // 模板文件路径：
            String templetFilePath =saveDir+"template/reception.xml";
            // 目标文件存放路径
            String targetFilePath = saveDir+pd.getString("TASK_ID")+"/"+pd.getString("PROC_ID")+"/reception.docx";
            // 将xml模板转换为后缀为doc文件，本质仍是属于xml
            Xml2XmlDoc.xml2XmlDoc(pd,templetFilePath,targetFilePath);

            PageData editPd = new PageData();
            editPd.put("ID",pd.getString("ID"));
            editPd.put("file_path",targetFilePath);
            inspectionReceptionService.editInspectionReception(editPd);

            String id = pd.getString("PROC_ID");
            PageData tempPd = new PageData();
            tempPd.put("ID",id);
            tempPd.put("INSPECTION_PROCESS_SUB_SIGN","0");
            tempPd.put("FINISH_TIME", DateUtil. getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
            inspectionProcSubService.editInspectionProcSub(tempPd);

        }catch (Exception e){
            e.printStackTrace();
            result.put("msg", e.getMessage());
            result.put("result", "false");
            return result;
        }
        return result;
    }

    @RequestMapping(value = "/editInspectionReception")
    @ApiOperation("修改")
    public Map<String,Object>editInspectionReception(@RequestBody(required = false) JSONObject param){
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        result.put("msg", "修改成功！");
        result.put("result", "success");
        try{
            inspectionReceptionService.editInspectionReception(pd);
            //新增文件
            // 模板文件路径：
            String templetFilePath =saveDir+"template/reception.xml";
            // 目标文件存放路径
            String targetFilePath = saveDir+pd.getString("TASK_ID")+"/"+pd.getString("PROC_ID")+"/reception.docx";
            // 将xml模板转换为后缀为doc文件，本质仍是属于xml
            Xml2XmlDoc.xml2XmlDoc(pd,templetFilePath,targetFilePath);
            PageData editPd = new PageData();
            editPd.put("ID",pd.getString("ID"));
            editPd.put("file_path",targetFilePath);
            editPd.put("update_time",DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
            inspectionReceptionService.editInspectionReception(editPd);

            String id = pd.getString("PROC_ID");
            PageData tempPd = new PageData();
            tempPd.put("ID",id);
            tempPd.put("INSPECTION_PROCESS_SUB_SIGN","0");
            tempPd.put("FINISH_TIME", DateUtil. getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
            inspectionProcSubService.editInspectionProcSub(tempPd);
        }catch (Exception e){
            e.printStackTrace();
            result.put("msg", e.getMessage());
            result.put("result", "false");
        }
        return result;
    }

    @RequestMapping("/getInspectionReceptionData")
    @ApiOperation("查")
    public Object getInspectionReceptionData(@RequestBody(required = false) JSONObject param){
        Map<String, Object> jsonMap = new HashMap<>();
        try{
            PageData pd = this.getPageData(param);
            jsonMap.put("rows", inspectionReceptionService.getInspectionReceptionData(pd));
            jsonMap.put("result","success");
        }catch (Exception e){
            jsonMap.put("result","false");
            jsonMap.put("msg",e.getMessage());
        }
        return jsonMap;
    }

    @RequestMapping(value = "/downReception")
    @ApiOperation("下载公务接待函")
    public void downReception(@RequestBody(required = false) JSONObject param,HttpServletResponse response) throws Exception {
        PageData pd = this.getPageData(param);
        if (pd.isEmpty()){
            return;
        }
        // 模板文件路径：
        String templetFilePath =saveDir+"template/reception.xml";
        // 目标文件存放路径
        String targetFilePath = saveDir+pd.getString("TASK_ID")+"/"+pd.getString("PROC_ID")+"/reception.docx";
        // 将xml模板转换为后缀为doc文件，本质仍是属于xml
        Xml2XmlDoc.xml2XmlDoc(pd,templetFilePath,targetFilePath);
        FileDownload.fileDownload(response, targetFilePath, "公务接待函.docx",this.getRequest());
    }
}

package org.inspect.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.inspect.BaseController;
import org.inspect.service.InspectionProcSubService;
import org.inspect.service.LegalNoticeService;
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
@Api(tags="执法检查通知书")
@RequestMapping(value = "/legalNoticeController", produces = MediaType.APPLICATION_JSON_VALUE)
public class LegalNoticeController extends BaseController {

    @Value("${TEMPLATE_FILE_PATH}")
    private  String saveDir;
    @Autowired
    private InspectionProcSubService inspectionProcSubService;
    @Autowired
    private LegalNoticeService legalNoticeService;

    @RequestMapping(value = "addLegalNotice")
    @ApiOperation("新增")
    public Map<String,Object> addLegalNotice(@RequestBody(required = false) JSONObject param){
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        result.put("result", "success");
        result.put("msg", "新增成功！");
        try {
            pd.put("ID",get32UUID());
            legalNoticeService.addLegalNotice(pd);

            //新增文件
            // 模板文件路径：
            String templetFilePath =saveDir+"template/legalNotice.xml";
            // 目标文件存放路径
            String targetFilePath = saveDir+pd.getString("TASK_ID")+"/"+pd.getString("PROC_ID")+"/legalNotice.docx";
            // 将xml模板转换为后缀为doc文件，本质仍是属于xml
            Xml2XmlDoc.xml2XmlDoc(pd,templetFilePath,targetFilePath);

            PageData editPd = new PageData();
            editPd.put("ID",pd.getString("ID"));
            editPd.put("FILE_PATH",targetFilePath);
            legalNoticeService.editLegalNotice(editPd);

            String id = pd.getString("PROC_ID");
            PageData tempPd = new PageData();
            tempPd.put("ID",id);
            tempPd.put("INSPECTION_PROCESS_SUB_SIGN","0");
            tempPd.put("FINISH_TIME",DateUtil. getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
            inspectionProcSubService.editInspectionProcSub(tempPd);

        }catch (Exception e){
            e.printStackTrace();
            result.put("msg", e.getMessage());
            result.put("result", "false");
            return result;
        }
        return result;
    }


    @RequestMapping(value = "/delLegalNotice")
    @ApiOperation("删除")
    public Map<String,Object>delLegalNotice(@RequestBody(required = false) JSONObject param){
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        result.put("msg", "删除成功！");
        result.put("result", "success");
        try{
            legalNoticeService.delLegalNotice(pd);
        }catch (Exception e){
            result.put("msg", e.getMessage());
            result.put("result", "false");
        }
        return result;
    }

    @RequestMapping(value = "/editLegalNotice")
    @ApiOperation("修改")
    public Map<String,Object>editLegalNotice(@RequestBody(required = false) JSONObject param){
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        result.put("msg", "修改成功！");
        result.put("result", "success");
        try{
            legalNoticeService.editLegalNotice(pd);

            //新增文件
            // 模板文件路径：
            String templetFilePath =saveDir+"template/legalNotice.xml";
            // 目标文件存放路径
            String targetFilePath = saveDir+pd.getString("TASK_ID")+"/"+pd.getString("PROC_ID")+"/legalNotice.docx";
            // 将xml模板转换为后缀为doc文件，本质仍是属于xml
            Xml2XmlDoc.xml2XmlDoc(pd,templetFilePath,targetFilePath);

            PageData editPd = new PageData();
            editPd.put("ID",pd.getString("ID"));
            editPd.put("FILE_PATH",targetFilePath);
            legalNoticeService.editLegalNotice(editPd);

        }catch (Exception e){
            result.put("msg", e.getMessage());
            result.put("result", "false");
        }
        return result;
    }

    @RequestMapping("/getLegalNoticeData")
    @ApiOperation("查")
    public Object getLegalNoticeData(@RequestBody(required = false) JSONObject param){
        Map<String, Object> jsonMap = new HashMap<>();
        try{
            PageData pd = this.getPageData(param);
            jsonMap.put("rows", legalNoticeService.getLegalNoticeData(pd));
            jsonMap.put("result","success");
        }catch (Exception e){
            jsonMap.put("result","false");
            jsonMap.put("msg",e.getMessage());
        }
        return jsonMap;
    }

    @RequestMapping(value = "/downLegalNotice")
    @ApiOperation("下载")
    public void downLegalNotice(@RequestBody(required = false) JSONObject param,HttpServletResponse response) throws Exception {
        PageData pd = this.getPageData(param);
        Map<String, Object> dataMap = legalNoticeService.getLegalNoticeData(pd);
        if (null == dataMap || dataMap.isEmpty()){
            return;
        }
        FileDownload.fileDownload(response, dataMap.get("FILE_PATH").toString(), "执法检查通知书.docx",this.getRequest());
    }
}

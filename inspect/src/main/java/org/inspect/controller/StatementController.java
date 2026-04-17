package org.inspect.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.inspect.BaseController;
import org.inspect.service.InspectionProcSubService;
import org.inspect.service.StatementService;
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
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@Api(tags="执法检查询问笔录")
@RequestMapping(value = "/statementController", produces = MediaType.APPLICATION_JSON_VALUE)
public class StatementController extends BaseController {

    @Value("${TEMPLATE_FILE_PATH}")
    private  String saveDir;
    @Autowired
    private InspectionProcSubService inspectionProcSubService;
    @Autowired
    private StatementService statementService;

    @RequestMapping(value = "addStatement")
    @ApiOperation("新增")
    public Map<String,Object> addStatement(@RequestBody(required = false) JSONObject param){
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        result.put("result", "success");
        result.put("msg", "新增成功！");
        try {
            pd.put("ID",get32UUID());
            statementService.addStatement(pd);

            //新增文件
            // 模板文件路径：
            String templetFilePath =saveDir+"template/statement.xml";
            // 目标文件存放路径
            String targetFilePath = saveDir+pd.getString("TASK_ID")+"/"+pd.getString("PROC_ID")+"/"+pd.getString("QUESTIONED_PERSON")+"statement.docx";
            // 将xml模板转换为后缀为doc文件，本质仍是属于xml
            Xml2XmlDoc.xml2XmlDoc(pd,templetFilePath,targetFilePath);

            PageData editPd = new PageData();
            editPd.put("ID",pd.getString("ID"));
            editPd.put("FILE_PATH",targetFilePath);
            statementService.editStatement(editPd);

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


    @RequestMapping(value = "/delStatement")
    @ApiOperation("删除")
    public Map<String,Object>delStatement(@RequestBody(required = false) JSONObject param){
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        result.put("msg", "删除成功！");
        result.put("result", "success");
        try{
            statementService.delStatement(pd);
        }catch (Exception e){
            result.put("msg", e.getMessage());
            result.put("result", "false");
        }
        return result;
    }

    @RequestMapping(value = "/editStatement")
    @ApiOperation("修改")
    public Map<String,Object>editStatement(@RequestBody(required = false) JSONObject param){
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        result.put("msg", "修改成功！");
        result.put("result", "success");
        try{
            statementService.editStatement(pd);

            //新增文件
            // 模板文件路径：
            String templetFilePath =saveDir+"template/statement.xml";
            // 目标文件存放路径
            String targetFilePath = saveDir+pd.getString("TASK_ID")+"/"+pd.getString("PROC_ID")+"/"+pd.getString("QUESTIONED_PERSON")+"statement.docx";
            // 将xml模板转换为后缀为doc文件，本质仍是属于xml
            Xml2XmlDoc.xml2XmlDoc(pd,templetFilePath,targetFilePath);

            PageData editPd = new PageData();
            editPd.put("ID",pd.getString("ID"));
            editPd.put("FILE_PATH",targetFilePath);
            statementService.editStatement(editPd);

        }catch (Exception e){
            result.put("msg", e.getMessage());
            result.put("result", "false");
        }
        return result;
    }

    @RequestMapping("/getStatementData")
    @ApiOperation("查")
    public Object getStatementData(@RequestBody(required = false) JSONObject param){
        Map<String, Object> jsonMap = new HashMap<>();
        try{
            PageData pd = this.getPageData(param);
            jsonMap.put("rows", statementService.getStatementData(pd));
            jsonMap.put("result","success");
        }catch (Exception e){
            jsonMap.put("result","false");
            jsonMap.put("msg",e.getMessage());
        }
        return jsonMap;
    }

    @RequestMapping(value = "/downStatement")
    @ApiOperation("下载")
    public void downStatement(@RequestBody(required = false) JSONObject param,HttpServletResponse response) throws Exception {
        PageData pd = this.getPageData(param);
        List<Map<String,Object>> dataMap = statementService.getStatementData(pd);
        if (null == dataMap || dataMap.isEmpty()){
            return;
        }
        FileDownload.fileDownload(response, dataMap.get(0).get("FILE_PATH").toString(), dataMap.get(0).get("QUESTIONED_PERSON").toString()+"执法检查询问笔录.docx",this.getRequest());
    }
}

package org.inspect.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.inspect.BaseController;
import org.inspect.util.FileDownload;
import org.inspect.util.PageData;
import org.inspect.util.Xml2XmlDoc;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@Api(tags="廉政监督卡")
@RequestMapping(value = "/inspectionSuperviseController", produces = MediaType.APPLICATION_JSON_VALUE)
public class InspectionSuperviseController extends BaseController {

    @Value("${TEMPLATE_FILE_PATH}")
    private  String saveDir;

    @RequestMapping(value = "/downSupervise")
    @ApiOperation("下载廉政监督卡")
    public void downSupervise(@RequestBody(required = false) JSONObject param,HttpServletResponse response) throws Exception {
        PageData pd = this.getPageData(param);
        if (pd.isEmpty()){
            return;
        }
        pd.put("member",pd.getString("member").replace(",","         "));
        pd.put("post",pd.getString("post").replace(",","         "));
        // 模板文件路径：
        String templetFilePath =saveDir+"template/supervise.xml";
        // 目标文件存放路径
        String targetFilePath = saveDir+pd.getString("TASK_ID")+"/"+pd.getString("PROC_ID")+"/supervise.docx";
        // 将xml模板转换为后缀为doc文件，本质仍是属于xml
        Xml2XmlDoc.xml2XmlDoc(pd,templetFilePath,targetFilePath);
        FileDownload.fileDownload(response, targetFilePath, "廉政监督卡.docx",this.getRequest());
    }
}

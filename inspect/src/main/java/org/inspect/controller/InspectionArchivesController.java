package org.inspect.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.inspect.BaseController;
import org.inspect.service.InspectionProcSubService;
import org.inspect.util.FileDownload;
import org.inspect.util.PageData;
import org.inspect.util.Xml2XmlDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@Api(tags="检查档案")
@RequestMapping(value = "/inspectionArchives", produces = MediaType.APPLICATION_JSON_VALUE)
public class InspectionArchivesController extends BaseController {

    @Value("${TEMPLATE_FILE_PATH}")
    private  String saveDir;

    @Autowired
    private InspectionProcSubService inspectionProcSubService;

    @RequestMapping(value = "/downArchives")
    @ApiOperation("下载")
    public void downArchives(HttpServletResponse response) throws Exception {
        PageData pd = this.getPageData();

        pd.put("INSPECTION_TASK_ID",pd.getString("TASK_ID"));
        pd.put("FOR_RECORD","0");

        List<Map<String,Object>> subList = inspectionProcSubService.getInspectionProcSubData(pd);
        if (null != subList && subList.size()>0){
            String content = "";
            for (int i = 0;i<subList.size();i++){
                content += (i+1)+"."+pd.getString("title")+subList.get(i).get("INSPECTION_PROCESS_SUB_NAME").toString()+"<w:br/>";
            }
            if (!content.equals("")){
                content += (subList.size()+1)+"."+"其他"+"<w:br/>";
            }
            pd.put("content",content);
        }
        String templetFilePath =saveDir+"template/archives.xml";
        // 目标文件存放路径
        String targetFilePath = saveDir+pd.getString("TASK_ID")+"/"+pd.getString("PROC_ID")+"/archives.docx";
        // 将xml模板转换为后缀为doc文件，本质仍是属于xml
        Xml2XmlDoc.xml2XmlDoc(pd,templetFilePath,targetFilePath);

        FileDownload.fileDownload(response, targetFilePath, "检查档案.docx",this.getRequest());
    }
}

package org.inspect.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.inspect.BaseController;
import org.inspect.service.InspectionFileService;
import org.inspect.service.InspectionProcService;
import org.inspect.service.InspectionProcSubService;
import org.inspect.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@Api(tags = "附件上传")
@RequestMapping(value = "/inspectionFileController", produces = MediaType.APPLICATION_JSON_VALUE)
public class InspectionFileController extends BaseController {

    @Value("${TEMPLATE_FILE_PATH}")
    private String saveDir;

    @Autowired
    protected InspectionFileService inspectionFileService;
    @Autowired
    private InspectionProcSubService inspectionProcSubService;
    @Autowired
    private InspectionProcService inspectionProcService;

    @RequestMapping(value = "/fileUpload", method = RequestMethod.POST)
    @ApiOperation("上传文件")
    public Map<String, Object> fileUpload(HttpServletRequest request,
                                          @RequestParam(value = "TASK_ID", required = false) String TASK_ID,
                                          @RequestParam(value = "PROC_ID", required = false) String PROC_ID,
                                          @RequestParam(value = "PATH_NAME", required = false) String PATH_NAME) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, String>> rows = new ArrayList<>();
        PageData pd = new PageData();
        result.put("result", "success");
        result.put("msg", "上传成功！");
        List<MultipartFile> mtFiles = ((MultipartHttpServletRequest) request).getFiles("file");
        try {
            String filePath = saveDir + TASK_ID + "/" + PROC_ID + "/";
            for (MultipartFile mtFile : mtFiles) {
                pd.put("INSPECTION_FILE_ID", get32UUID());
                pd.put("TASK_ID", TASK_ID);
                pd.put("INSPECTION_PROCESS_SUB_ID", PROC_ID);
                pd.put("INSPECTION_FILE_NAME", mtFile.getOriginalFilename());
                pd.put("INSPECTION_FILE_PATH", filePath + mtFile.getOriginalFilename());
                pd.put("UPLOAD_TIME", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
                pd.put("UPLOAD_USER", pd.getString("user_id"));
                inspectionFileService.addInspectionFile(pd);
                FileUpload.fileUp(mtFile, filePath, mtFile.getOriginalFilename());
                rows.add(pd);
            }
            result.put("rows", rows);

            PageData procPd = new PageData();
            procPd.put("ID", PROC_ID);
            List<Map<String, Object>> procMap = inspectionProcSubService.getInspectionProcSubData(procPd);
            if (null != procMap && !procMap.isEmpty()) {
                if (procMap.get(0).get("INSPECTION_PROCESS_SUB_NAME").toString().equals("自查报告")
                        || procMap.get(0).get("INSPECTION_PROCESS_SUB_NAME").toString().equals("行政处罚立案审批表")
                        || procMap.get(0).get("INSPECTION_PROCESS_SUB_NAME").toString().equals("意见告知书审核意见表")
                        || procMap.get(0).get("INSPECTION_PROCESS_SUB_NAME").toString().equals("意见告知书")
                        || procMap.get(0).get("INSPECTION_PROCESS_SUB_NAME").toString().equals("决定书审核意见表")
                        || procMap.get(0).get("INSPECTION_PROCESS_SUB_NAME").toString().equals("行政处罚决定书")) {
                    procPd.put("INSPECTION_PROCESS_SUB_SIGN", "0");
                    procPd.put("FINISH_TIME", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
                    inspectionProcSubService.editInspectionProcSub(procPd);
                }
                if (procMap.get(0).get("INSPECTION_PROCESS_SUB_NAME").toString().equals("整改报告")) {
                    procPd.put("INSPECTION_PROCESS_SUB_SIGN", "0");
                    procPd.put("FINISH_TIME", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
                    inspectionProcSubService.editInspectionProcSub(procPd);

                    procPd.put("ID", procMap.get(0).get("PROCESS_ID"));
                    procPd.put("INSPECTION_PROCESS_SIGN", "0");
                    procPd.put("FINISH_TIME", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
                    inspectionProcService.editInspectionProc(pd);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", e.getMessage());
            result.put("result", "false");
            return result;
        }
        return result;
    }

    @RequestMapping(value = "/downFile")
    @ApiOperation("下载文件")
    public void downFile(@RequestBody(required = false) JSONObject param, HttpServletResponse response) throws Exception {
        PageData pd = this.getPageData(param);
//        List<Map<String, Object>> dataMap = inspectionFileService.getInspectionFileData(pd);
//        if (dataMap.isEmpty()){
//            return;
//        }
        FileDownload.fileDownload(response, pd.getString("path"), pd.getString("path").substring(pd.getString("path").lastIndexOf("/") + 1), this.getRequest());
    }

    @RequestMapping(value = "/downFileBatch")
    @ApiOperation("批量下载文件")
    public void downFileBatch(@RequestBody(required = false) JSONObject param, HttpServletResponse response) throws Exception {
        PageData pd = this.getPageData(param);
        List<Map<String, Object>> dataList = inspectionFileService.getInspectionFileData(pd);
        String tempPath = String.valueOf(dataList.get(0).get("INSPECTION_FILE_PATH"));
        String zipFilePath = tempPath.substring(0, tempPath.lastIndexOf("/"));
        String zipFileName = zipFilePath.substring(zipFilePath.lastIndexOf("/") + 1, zipFilePath.length()) + ".zip";
        ZipUtils.toZip(zipFilePath, false);
        FileDownload.fileDownload(response, zipFilePath + ".zip", zipFileName, this.getRequest());
    }

    @RequestMapping(value = "/delFile")
    @ApiOperation("删除文件")
    public Map<String, Object> delFile(@RequestBody(required = false) JSONObject param) throws Exception {
        PageData pd = this.getPageData(param);
        Map<String, Object> result = new HashMap<>();
        result.put("result", "success");
        result.put("msg", "删除成功！");
        try {
            inspectionFileService.delInspectionFile(pd);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", e.getMessage());
            result.put("result", "false");
            return result;
        }
        return result;
    }

    @RequestMapping(value = {"/getFiles"}, method = RequestMethod.POST)
    @ApiOperation("获取文件列表(不分页)")
    public Object getFiles(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> jsonMap = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            jsonMap.put("rows", inspectionFileService.getInspectionFileData(pd));
            jsonMap.put("result", "success");
        } catch (Exception e) {
            jsonMap.put("result", "false");
            jsonMap.put("msg", e.getMessage());
        }
        return jsonMap;
    }


    @RequestMapping(value = {"/viewDoc"})
    public Map viewDoc(@RequestBody(required = false) JSONObject param) throws Exception {
        PageData pd = this.getPageData(param);
        String path = pd.getString("path");
        Map<String, Object> HtmlDocument = new HashMap<>();

        String fileContent = "";
        String filename = path.substring(0, path.lastIndexOf("."));
        String fileType = path.substring(path.lastIndexOf(".") + 1);
        if ("docx".equals(fileType) || "xls".equals(fileType) || "xlsx".equals(fileType)) {
            String htmlPath = "";
            if ("docx".equals(fileType)) {
                htmlPath = OfficeUtils.docxToHtml(path);
            } else if ("xls".equals(fileType) || "xlsx".equals(fileType)) {
                htmlPath = OfficeUtils.excelToHtml(path, true);
            }
            if (!htmlPath.equals("false")) {
                try {
                    File f = new File(htmlPath);
                    if (f.isFile() && f.exists()) {
                        InputStreamReader read = new InputStreamReader(
                                new FileInputStream(f), "utf-8");
                        BufferedReader reader = new BufferedReader(read);
                        String line;
                        while ((line = reader.readLine()) != null) {
                            fileContent += line;
                        }
                        read.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if ("doc".equals(fileType)) {
            fileContent = OfficeUtils.docToHtml(path);
        }
        HtmlDocument.put("result", fileContent);
        HtmlDocument.put("type", fileType);

        return HtmlDocument;
    }
}

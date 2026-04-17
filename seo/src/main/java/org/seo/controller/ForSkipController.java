package org.seo.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.seo.BaseController;
import org.seo.service.ComprehensiveQueryService;
import org.seo.service.ForSkipService;
import org.seo.util.FileDownload;
import org.seo.util.OfficeDocxUtils;
import org.seo.util.OfficeUtils;
import org.seo.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@Api(tags = "跳转")
@RequestMapping(value = "/forSkip", produces = MediaType.APPLICATION_JSON_VALUE)
public class ForSkipController extends BaseController {

    @Autowired
    private ForSkipService forSkipService;
    @Autowired
    private ComprehensiveQueryService comprehensiveQueryService;

    @RequestMapping(value = {"/skipJump"}, method = RequestMethod.POST)
    @ApiOperation("跳转")
    public Object skipJump() {
        Map<String, Object> jsonMap = new HashMap<>();
        try {
            PageData pd = this.getPageData();
            String[] tables = pd.getString("tableName").split("▲");
            pd.put("ID", tables[0]);
            String table ="";
            Map<String, String> type = getType(pd);
            table = type.get("DBNAME") + "." + tables[1];
            String info = tables[2];
            String  S_INFO_ID_Val=pd.getString(info);
            pd.put("table", table);
            pd.put("info", info);
            pd.put("S_INFO_ID", S_INFO_ID_Val);
            List<Map<String, Object>> data = forSkipService.getFileList(pd, tables[0]);

            jsonMap.put("rows", data);
            jsonMap.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            jsonMap.put("result", "false");
            jsonMap.put("msg", e.getMessage());
        }
        return jsonMap;
    }
    private Map<String, String> getType(PageData pd) {
        return comprehensiveQueryService.getType(pd);
    }
    @RequestMapping(value = "/downFile")
    @ApiOperation("下载文件")
    public void downFile(@RequestBody(required = false) JSONObject param, HttpServletResponse response) throws Exception {
        PageData pd = this.getPageData(param);
        FileDownload.fileDownload(response, pd.getString("path"), pd.getString("path").substring(pd.getString("path").lastIndexOf("/") + 1), this.getRequest());
    }

    @RequestMapping(value = {"/viewDoc"})
    public Map viewDoc(@RequestBody(required = false) JSONObject param) throws Exception {
        PageData pd = this.getPageData(param);
        Map<String, Object> HtmlDocument = new HashMap<>();
        try {
            String[] tables = pd.getString("downFileName").split("▲");
            pd.put("ID", tables[0]);
            String table ="";
            Map<String, String> type = getType(pd);
            table = type.get("DBNAME") + "." + tables[1];
            String info = tables[2];
            String  S_INFO_ID_Val=pd.getString(info);
            pd.put("table", table);
            pd.put("info", info);
            pd.put("S_INFO_ID", S_INFO_ID_Val);
            List<Map<String, Object>> fileMap = forSkipService.getFileList(pd, tables[0]);
//        List<Map<String, Object>> fileMap = forSkipService.getFileList(pd,);
            if (null != fileMap && fileMap.size() > 0) {
                String fileContent = "";
                String path = fileMap.get(0).get("PATH").toString().substring(0, fileMap.get(0).get("PATH").toString().lastIndexOf("/") + 1);
                String filename = fileMap.get(0).get("FILE_NAME").toString();
                String fileType = filename.substring(filename.lastIndexOf(".") + 1);
                if ("docx".equals(fileType) || "xls".equals(fileType) || "xlsx".equals(fileType) || "et".equals(fileType)) {
                    String htmlPath = "";
                    if ("docx".equals(fileType)) {
                        String pathNow = path + filename;
                        htmlPath = OfficeDocxUtils.docxToHtml(pathNow);
                    } else if ("xls".equals(fileType) || "xlsx".equals(fileType) || "et".equals(fileType)) {
                        htmlPath = OfficeUtils.excelToHtml(path, filename, true);
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
                    fileContent = OfficeUtils.docToHtml(path, filename);
                } else if ("wps".equals(fileType)) {
                    fileContent = OfficeUtils.docToHtml(path, filename);
                }
                HtmlDocument.put("result", fileContent);
                HtmlDocument.put("type", fileType);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return HtmlDocument;
    }

    @ResponseBody
    @RequestMapping("/viewPdf")
    public void viewPdf(HttpServletResponse response,
                        @RequestBody(required = false) JSONObject param
    ) throws IOException {
        PageData pd = this.getPageData(param);
        String filePath = pd.get("path") + "";
        System.out.println("filePath:" + filePath);
        File f = new File(filePath);
        if (!f.exists()) {
            response.sendError(404, "File not found!");
            return;
        }
        BufferedInputStream br = new BufferedInputStream(new FileInputStream(f));
        byte[] bs = new byte[1024];
        int len = 0;
        response.reset(); // 非常重要
        URL u = new URL("file:///" + filePath);
        String contentType = u.openConnection().getContentType();
        response.setContentType(contentType);
        response.setHeader("Content-Disposition", "inline;filename="
                + pd.get("file_name") + "");
        // 文件名应该编码成utf-8，注意：使用时，我们可忽略这句
        OutputStream out = response.getOutputStream();
        while ((len = br.read(bs)) > 0) {
            out.write(bs, 0, len);
        }
        out.flush();
        out.close();
        br.close();
    }
}

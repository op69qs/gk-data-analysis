// InspectionRegisterBookController.java

package org.inspect.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.inspect.BaseController;
import org.inspect.service.InspectionProcessControlService;
import org.inspect.service.InspectionQuestionLedgerService;
import org.inspect.service.InspectionRegisterBookService;
import org.inspect.service.InspectionWorkingPaperService;
import org.inspect.util.DateUtil;
import org.inspect.util.FileDownload;
import org.inspect.util.PageData;
import org.inspect.util.Xml2XmlDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 例行检查-检查登记簿
 *
 * @author Created by Samer on 2019/10/16.
 */
@Slf4j
@RestController
@Api(tags = "例行检查-检查登记簿")
@RequestMapping(value = "/inspectionRegisterBook", produces = MediaType.APPLICATION_JSON_VALUE)
public class InspectionRegisterBookController extends BaseController {

    @Value("${TEMPLATE_FILE_PATH}")
    private String saveDir;

    @Autowired
    private InspectionRegisterBookService inspectionRegisterBookService;

    @Autowired
    private InspectionQuestionLedgerService inspectionQuestionLedgerService;

    @Autowired
    private InspectionProcessControlService inspectionProcessControlService;

    @Autowired
    private InspectionWorkingPaperService inspectionWorkingPaperService;


    /**
     * 新增检查登记簿
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "新增检查登记簿")
    @PostMapping(value = "/addRegisterBook")
    public Map<String, Object> addRegisterBook(
            @ApiParam(value = "当前任务ID：TASK_ID\n" +
                    "当前检查报告ID：REPORT_ID。\n" +
                    "当前流程ID：PROC_ID。\n" +
                    "当前子流程ID：PROC_SUB_ID。\n" +
                    "被查国库：INSPECTED_GUOKU_DSCR。\n" +
                    "基本情况：BASE_SITUATION。\n" +
                    "国库主任：DIRECTOR。\n" +
                    "国库部门负责人：DEPARTMENT_HEAD。\n" +
                    "其他检查人员：MEMBERS。\n" +
                    "整改建议：REFORM_SUGGEST。\n" +
                    "被查单位意见：BECHICKED_OPINION。\n" +
                    "当前用户ID：ADD_USERID\n")
            @RequestBody(required = false) JSONObject param
    ) {
        String docName = "";
        String targetFilePath = "";
        String targetFilePath_NONAME = "";
        String templetFilePath = "";
        Map<String, Object> res = new HashMap<>();
        String finishTime = DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS);
        String bookId = this.get32UUID();
        PageData pd = this.getPageData(param);
        Map<String, String> taskInfo = inspectionWorkingPaperService.getTaskInfoByTaskId(pd);
        String beginTime = taskInfo.get("INSPECTION_TASK_BEGINTIME");
        String endTime = taskInfo.get("INSPECTION_TASK_ENDTIME")+ " " + DateUtil.getCurrentDateStr(DateUtil.Pattern.HH_MM_SS);
        pd.put("START_END_DATE",
                DateUtil.transformDateToStr(DateUtil.transformStrToDate(beginTime, DateUtil.Pattern.YYYY_MM_DD), DateUtil.Pattern.YYYY_MM_DD_CN)
                        + "至"
                        + DateUtil.transformDateToStr(DateUtil.transformStrToDate(endTime, DateUtil.Pattern.YYYY_MM_DD), DateUtil.Pattern.YYYY_MM_DD_CN)
        );
        Map<String, String> treInfo = inspectionRegisterBookService.getTreInfoByTaskId(pd);
        String level = String.valueOf(treInfo.get("level"));
        if ("2".equals(level) || "3".equals(level)) {
            docName = "国家金库（" + pd.getString("INSPECTED_GUOKU_DSCR") + "）检查登记簿.docx";
            templetFilePath = saveDir + "template/registerBook_city.xml";
        }
        if ("4".equals(level) || "5".equals(level)) {
            docName = "中国人民银行（" + pd.getString("INSPECTED_GUOKU_DSCR") + "）检查登记簿.docx";
            templetFilePath = saveDir + "template/registerBook_county.xml";
        }
        //目标文件存放路径
        targetFilePath = saveDir + pd.getString("TASK_ID") + "/检查登记簿/" + docName;
        targetFilePath_NONAME = saveDir + pd.getString("TASK_ID") + "/检查登记簿/print/" + docName;
        Map<String, Object> bookInfo = inspectionRegisterBookService.getRegisterBookInfo(pd);
        pd.put("BOOK_ID", bookInfo == null || bookInfo.isEmpty() ? bookId : bookInfo.get("BOOK_ID"));
        inspectionQuestionLedgerService.assembleContent(pd);
        String REFORM_INFO = inspectionRegisterBookService.assembleReformScheme(pd, "&#12288;&#12288;", "<w:br/>", "docx");
        pd.put("BOOK_NAME", docName);
        pd.put("ATTACHMENT_NAME", docName);
        /*电子文档路径*/
        pd.put("PAPER_ATTACHMENT", targetFilePath);
        /*导出文档路径*/
        pd.put("PRINT_ATTACHMENT", targetFilePath_NONAME);
        pd.put("NOT_OTHER_CONTENT", pd.getString("NOT_OTHER_CONTENT"));
        pd.put("OTHER_CONTENT", pd.getString("OTHER_CONTENT"));
        pd.put("REFORM_INFO", REFORM_INFO);
        pd.put("INSPECT_DATE", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_CN));
        try {
            if (bookInfo == null || bookInfo.isEmpty()) {
                pd.put("ADD_DATE", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
                pd.put("queryType", "add");
                pd.put("insertFlag", "insert");
                String inspectionContent = inspectionRegisterBookService.assembleInspectionContent(pd, "&#12288;&#12288;", "<w:br/>");
                pd.put("INSPECT_CONTENT", inspectionContent);
                pd.put("ADD_DATE", finishTime);
                inspectionRegisterBookService.addRegisterBook(pd);
                res.put("msg", "检查登记簿新增成功");
            } else {
                pd.put("MODIFY_DATE", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
                pd.put("queryType", "edit");
                String inspectionContent = inspectionRegisterBookService.assembleInspectionContent(pd, "&#12288;&#12288;", "<w:br/>");
                pd.put("INSPECT_CONTENT", inspectionContent);
                pd.put("MODIFY_DATE", finishTime);
                pd.put("MODIFY_USERID", pd.getString("ADD_USERID"));
                inspectionRegisterBookService.updateRegisterBook(pd);
                res.put("msg", "检查登记簿修改成功");
            }
        /*流程控制*/
            pd.put("FINISH_TIME", finishTime);
            inspectionProcessControlService.finishCurSubProcessById(pd);
            if (inspectionProcessControlService.activateFollowProc(pd)) {
                /*res.put("msg", res.get("msg") + inspectionProcessControlService.getProcDescById(pd) + "流程结束，后续流程已激活");*/
            }
            //将xml模板转换为后缀为doc文件
            Xml2XmlDoc.xml2XmlDoc(pd, templetFilePath, targetFilePath);
            pd.put("DIRECTOR", "");
            pd.put("DEPARTMENT_HEAD", "");
            pd.put("MEMBERS", "");
            Xml2XmlDoc.xml2XmlDoc(pd, templetFilePath, targetFilePath_NONAME);
            res.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            if (bookInfo == null || bookInfo.isEmpty()) {
                res.put("msg", "检查登记簿新增失败");
            } else {
                res.put("msg", "检查登记簿修改失败");
            }
            res.put("result", "false");
        }
        return res;
    }

    /**
     * 获取检查登记簿信息
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "获取检查登记簿信息")
    @PostMapping(value = "/getRegisterBookInfo")
    public Map<String, Object> getRegisterBookInfo(
            @ApiParam(value = "当前任务ID：TASK_ID\n" +
                    "当前用户ID：ADD_USERID\n" +
                    "当前登记簿ID：BOOK_ID\n")
            @RequestBody(required = false) JSONObject param
    ) {
        Map<String, Object> res = new HashMap<String, Object>();
        Map<String, Object> object = new HashMap<String, Object>();
        PageData pd = this.getPageData(param);
        Map<String, String> taskInfo = inspectionWorkingPaperService.getTaskInfoByTaskId(pd);
        String beginTime = taskInfo.get("INSPECTION_TASK_BEGINTIME");
        String endTime = taskInfo.get("INSPECTION_TASK_ENDTIME")+ " " + DateUtil.getCurrentDateStr(DateUtil.Pattern.HH_MM_SS);
        String START_END_DATE =
                DateUtil.transformDateToStr(DateUtil.transformStrToDate(beginTime, DateUtil.Pattern.YYYY_MM_DD), DateUtil.Pattern.YYYY_MM_DD_CN)
                        + "至"
                        + DateUtil.transformDateToStr(DateUtil.transformStrToDate(endTime, DateUtil.Pattern.YYYY_MM_DD), DateUtil.Pattern.YYYY_MM_DD_CN);
        Map<String, Object> bookList = inspectionRegisterBookService.getRegisterBookInfo(pd);
        Map<String, Object> contentHtml = inspectionQuestionLedgerService.assembleContent_html(pd);
        String REFORM_INFO = inspectionRegisterBookService.assembleReformScheme(pd, "    ", "\n", "html");
        if ("false".equals(contentHtml.get("result"))) {
            res.put("msg", "信息查询失败");
            return res;
        }
        if (bookList == null || bookList.isEmpty()) {
            object.put("START_END_DATE", START_END_DATE);
            object.put("NOT_OTHER_CONTENT", pd.getString("NOT_OTHER_CONTENT"));
            object.put("OTHER_CONTENT", pd.getString("OTHER_CONTENT"));
            object.put("REFORM_INFO", REFORM_INFO);
            pd.put("queryType", "add");
            String inspectionContent = inspectionRegisterBookService.assembleInspectionContent(pd, "    ", "\n");
            object.put("INSPECT_DATE", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_CN));
            object.put("INSPECT_CONTENT", inspectionContent);
            res.put("rows", object);
        } else {
            bookList.put("NOT_OTHER_CONTENT", pd.getString("NOT_OTHER_CONTENT"));
            bookList.put("OTHER_CONTENT", pd.getString("OTHER_CONTENT"));
            bookList.put("REFORM_INFO", REFORM_INFO);
            bookList.put("START_END_DATE", START_END_DATE);
            pd.put("queryType", "edit");
            pd.put("BOOK_ID", bookList.get("BOOK_ID"));
            String inspectionContent = inspectionRegisterBookService.assembleInspectionContent(pd, "    ", "\n");
            bookList.put("INSPECT_CONTENT", inspectionContent);
            res.put("rows", bookList);
        }
        res.put("result", "success");
        return res;
    }

    /**
     * 获取被查库信息
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "获取被查库信息")
    @PostMapping(value = "/getTreInfoByTaskId")
    public Map<String, Object> getTreInfoByTaskId(
            @ApiParam(value = "当前任务ID：TASK_ID\n")
            @RequestBody(required = false) JSONObject param
    ) {
        Map<String, Object> res = new HashMap<String, Object>();
        PageData pd = this.getPageData(param);
        Map<String, String> treInfo = inspectionRegisterBookService.getTreInfoByTaskId(pd);
        res.put("result", "success");
        res.put("rows", treInfo);
        return res;
    }

    /**
     * 检查登记簿导出
     *
     * @param param
     * @param response
     */
    @ApiOperation(value = "检查登记簿导出")
    @GetMapping(value = "/downLoadRegisterBook")
    public void downLoadRegisterBook(
            @ApiParam(value = "当前登记簿ID：BOOK_ID")
            @RequestBody(required = false) JSONObject param,
            HttpServletResponse response
    ) throws Exception {
        PageData pd = this.getPageData(param);
        Map<String, Object> bookInfo = inspectionRegisterBookService.getRegisterBookInfo(pd);
        String filePath = bookInfo.get("PRINT_ATTACHMENT").toString();
        String fileName = bookInfo.get("ATTACHMENT_NAME").toString();
        FileDownload.fileDownload(response, filePath, fileName, this.getRequest());
    }


} ///:~

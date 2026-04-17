// InspectionPostSVNoticeController.java

package org.inspect.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.inspect.BaseController;
import org.inspect.service.InspectionPostSVLedgerService;
import org.inspect.service.InspectionPostSVListService;
import org.inspect.service.InspectionPostSVNoticeService;
import org.inspect.service.InspectionProcessControlService;
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
 * @author Created by Samer on 2019/11/21.
 */
@Slf4j
@Api(tags = "事后监督监督通知")
@RestController
@RequestMapping(value = "/PostSVNotice", produces = MediaType.APPLICATION_JSON_VALUE)
public class InspectionPostSVNoticeController extends BaseController {

    @Value("${TEMPLATE_FILE_PATH}")
    private String saveDir;

    @Autowired
    protected InspectionPostSVLedgerService inspectionPostSVLedgerService;

    @Autowired
    private InspectionPostSVNoticeService inspectionPostSVNoticeService;

    @Autowired
    private InspectionProcessControlService inspectionProcessControlService;

    @Autowired
    private InspectionPostSVListService inspectionPostSVListService;

    /**
     * 新增监督通知
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "新增监督通知")
    @PostMapping(value = "/addPostSVNotice")
    public Map<String, Object> addPostSVNotice(
            @ApiParam(value = "监督通知ID：NOTICE_ID \n" +
                    "当前任务ID：TASK_ID\n" +
                    "当前流程ID：PROC_ID。\n" +
                    "当前子流程ID：PROC_SUB_ID。\n" +
                    "当前用户ID：ADD_USERID。\n" +
                    "标题：TITLE。\n" +
                    "机构名称：BOOKORG_DSCR\n" +
                    "编号：NOTICE_NO \n" +
                    "核算日期：ACC_DATE\n" +
                    "经办员: MAKERS\n" +
                    "返还时限：BACK_DATE\n" +
                    "国库核算-日期：TRE_ACC_DATE\n" +
                    "国库核算-经办员：TRE_MAKERS\n" +
                    "国库核算-会计主管：TRE_SUPERVISOR\n" +
                    "处理结果：RESULT\n" +
                    "国库部门负责人（监督主管）：AFTER_HEAD\n" +
                    "监督人员：MEMBERS\n"+
                    "监督日期：SUV_DATE\n"
            )
            @RequestBody(required = false) JSONObject param
    ) {
        Map<String, Object> res = new HashMap<>();
        PageData pd = this.getPageData(param);
        String NOTICE_ID = this.get32UUID();
        String endTime = DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS);
        String FILLING_DATE = DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_CN);
        String docName = "",
                targetFilePath = "",
                targetFilePath_NONAME = "",
                templateName = "",
                templetFilePath = "";

        docName = pd.getString("TITLE") + ".docx";
        templateName = "supervisionNotice.xml";
        templetFilePath = saveDir + "template/" + templateName;
        //目标文件存放路径
        targetFilePath = saveDir + pd.getString("TASK_ID") + "/事后监督监督通知/" + docName;
        targetFilePath_NONAME = saveDir + pd.getString("TASK_ID") + "/事后监督监督通知/print/" + docName;

        Map<String, Object> paperList = inspectionPostSVNoticeService.getRecord(pd);
        if (paperList == null || paperList.isEmpty()) {
            pd.put("NOTICE_ID", NOTICE_ID);
        }
        res = inspectionPostSVLedgerService.assembleQuestionContent(pd, "&#12288;&#12288;", "<w:br/>");
        if ("false".equals(res.get("result"))) {
            return res;
        }


        pd.put("FILLING_DATE", FILLING_DATE);
        /*电子档检查人写入*/
        pd.put("PAPER_ATTACHMENT", targetFilePath);
        /*导出文档检查人不写入*/
        pd.put("PRINT_ATTACHMENT", targetFilePath_NONAME);
        pd.put("ATTACHMENT_NAME", docName);
        try {
            //将xml模板转换为后缀为doc文件
            if (paperList == null || paperList.isEmpty()) {
                pd.put("ADD_DATE", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
                inspectionPostSVNoticeService.addRecord(pd);
                res.put("msg", "监督通知新增成功");
            } else {
                pd.put("MODIFY_DATE", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
                pd.put("MODIFY_USERID", pd.getString("ADD_USERID"));
                inspectionPostSVNoticeService.editRecord(pd);
                res.put("msg", "监督通知修改成功");
            }
            //当前流程完成
            pd.put("FINISH_TIME", endTime);
            inspectionProcessControlService.finishPostSVCurSubProcessById(pd);

            Xml2XmlDoc.xml2XmlDoc(pd, templetFilePath, targetFilePath);
            /*导出文件不包含检查人名*/
            pd.put("AFTER_HEAD", "");
            pd.put("MEMBERS", "");
            pd.put("SUV_DATE", "    年  月  日");
            Xml2XmlDoc.xml2XmlDoc(pd, templetFilePath, targetFilePath_NONAME);
            res.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            if (paperList == null || paperList.isEmpty()) {
                res.put("msg", "监督通知新增失败");
            } else {
                res.put("msg", "监督通知修改失败");
            }
            res.put("result", "false");
        }
        return res;
    }

    /**
     * 获取监督通知
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "获取监督通知")
    @PostMapping(value = "/getPostSVNotice")
    public Map<String, Object> getPostSVNotice(
            @ApiParam(value = "监督通知ID：NOTICE_ID \n" +
                    "当前任务ID：TASK_ID\n")
            @RequestBody(required = false) JSONObject param
    ) {
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> dataList = new HashMap<>();
        PageData pd = this.getPageData(param);
        pd.put("IS_REPORT", "0");

        Map<String, String> taskInfo = inspectionPostSVListService.getTaskInfoByTaskId(pd);
        Map<String, Object> contentHtml = inspectionPostSVLedgerService.assembleQuestionContent(pd, "    ", "\n");
        if ("false".equals(contentHtml.get("result"))) {
            res.put("msg", "信息查询失败");
            return res;
        }
        dataList = inspectionPostSVNoticeService.getRecord(pd);
        if (dataList == null || dataList.isEmpty()) {
            dataList = new HashMap<>();
            dataList.put("BOOKORG_DSCR", taskInfo.get("BOOKORG_DSCR"));
        }
        dataList.put("QUESTION_CONTENT",  pd.getString("QUESTION_CONTENT"));
        dataList.put("RULES",  pd.getString("RULES"));
        dataList.put("ERROR_TYPE",  pd.getString("ERROR_TYPE"));

        res.put("result", "success");
        res.put("rows", dataList);
        return res;
    }

    /**
     * 监督通知导出
     *
     * @param param
     * @param response
     */
    @ApiOperation(value = "监督通知导出")
    @GetMapping(value = "/downLoadPostSVNotice")
    public void downLoadPostSVNotice(
            @ApiParam(value = "监督通知ID：NOTICE_ID")
            @RequestBody(required = false) JSONObject param,
            HttpServletResponse response
    ) throws Exception {
        PageData pd = this.getPageData(param);
        Map<String, Object> dataList = inspectionPostSVNoticeService.getRecord(pd);
        String fileName = dataList.get("ATTACHMENT_NAME").toString();
        String filePath = dataList.get("PRINT_ATTACHMENT").toString();
        FileDownload.fileDownload(response, filePath, fileName, this.getRequest());
    }


} ///:~

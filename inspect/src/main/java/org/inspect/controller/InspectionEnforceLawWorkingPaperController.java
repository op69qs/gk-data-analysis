// InspectionEnforceLawWorkingPaperController.java

package org.inspect.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.inspect.BaseController;
import org.inspect.service.InspectionEnforceLawWorkingPaperService;
import org.inspect.service.InspectionProcessControlService;
import org.inspect.service.InspectionQuestionLedgerService;
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
@Api(tags = "执法检查工作底稿")
@RestController
@RequestMapping(value = "/enforceLawWorkingPaper", produces = MediaType.APPLICATION_JSON_VALUE)
public class InspectionEnforceLawWorkingPaperController extends BaseController {

    @Value("${TEMPLATE_FILE_PATH}")
    private String saveDir;

    @Autowired
    protected InspectionQuestionLedgerService inspectionQuestionLedgerService;

    @Autowired
    private InspectionEnforceLawWorkingPaperService inspectionEnforceLawWorkingPaperService;

    @Autowired
    private InspectionProcessControlService inspectionProcessControlService;

    /**
     * 新增工作底稿
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "新增工作底稿")
    @PostMapping(value = "/addEnforceLawWorkingPaper")
    public Map<String, Object> addEnforceLawWorkingPaper(
            @ApiParam(value = "工作底稿ID：PAPER_ID \n" +
                    "当前任务ID：TASK_ID\n" +
                    "用户所在部门ID：DEPARTMENT\n" +
                    "当前流程ID：PROC_ID。\n" +
                    "当前子流程ID：PROC_SUB_ID。\n" +
                    "当前用户ID：ADD_USERID。\n" +
                    "项目名称：INSPECT_ITEM_DSCR。\n" +
                    "被查机构名称：CHECKED_ORG_DSCR \n" +
                    "检查机构名称： CHECK_ORG_DSCR\n" +
                    "检查人：MEMBER\n" +
                    "复核人：REVIEWER\n" +
                    "执法检查基本情况：ENFORCE_LAW_BASE。\n" +
                    "被查机构执行基本情况：CHECKED_EXECUTE。\n"
            )
            @RequestBody(required = false) JSONObject param
    ) {
        Map<String, Object> res = new HashMap<>();
        PageData pd = this.getPageData(param);
        String PAPER_ID = this.get32UUID();
        String endTime = DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS);
        String docName = "",
                targetFilePath = "",
                targetFilePath_NONAME = "",
                templateName = "",
                templetFilePath = "";

        docName = "中国人民银行（" + pd.getString("CHECK_ORG_DSCR") + "）执法检查工作底稿.docx";
        templateName = "enforceLawWorkingPaper.xml";
        templetFilePath = saveDir + "template/" + templateName;
        //目标文件存放路径
        targetFilePath = saveDir + pd.getString("TASK_ID") + "/执法检查工作底稿/" + docName;
        targetFilePath_NONAME = saveDir + pd.getString("TASK_ID") + "/执法检查工作底稿/print/" + docName;

        Map<String, Object> paperList = inspectionEnforceLawWorkingPaperService.getRecord(pd);
        if (paperList == null || paperList.isEmpty()) {
            pd.put("PAPER_ID", PAPER_ID);
        }

        res = inspectionQuestionLedgerService.assembleContent(pd);
        if ("false".equals(res.get("result"))) {
            return res;
        }

        pd.put("FILLING_DATE", endTime);
        /*电子档检查人写入*/
        pd.put("PAPER_ATTACHMENT", targetFilePath);
        /*导出文档检查人不写入*/
        pd.put("PRINT_ATTACHMENT", targetFilePath_NONAME);
        pd.put("ATTACHMENT_NAME", docName);
        try {
            //将xml模板转换为后缀为doc文件
            if (paperList == null || paperList.isEmpty()) {
                pd.put("ADD_DATE", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
                inspectionEnforceLawWorkingPaperService.addRecord(pd);
                res.put("msg", "执法检查工作底稿新增成功");
            } else {
                pd.put("MODIFY_DATE", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
                pd.put("MODIFY_USERID", pd.getString("ADD_USERID"));
                inspectionEnforceLawWorkingPaperService.editRecord(pd);
                res.put("msg", "执法检查工作底稿修改成功");
            }
            //当前流程完成，尝试将大流程状态改为完成。
            pd.put("FINISH_TIME", endTime);
            inspectionProcessControlService.finishCurSubProcessById(pd);
            if (inspectionProcessControlService.activateFollowProc(pd)) {
                /*res.put("msg", res.get("msg") + inspectionProcessControlService.getProcDescById(pd) + "流程结束，后续流程已激活");*/
            }
            Xml2XmlDoc.xml2XmlDoc(pd, templetFilePath, targetFilePath);
            /*导出文件不包含检查人名*/
            pd.put("MEMBER", "");
            pd.put("REVIEWER", "");
            Xml2XmlDoc.xml2XmlDoc(pd, templetFilePath, targetFilePath_NONAME);
            res.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            if (paperList == null || paperList.isEmpty()) {
                res.put("msg", "执法检查工作底稿新增失败");
            } else {
                res.put("msg", "执法检查工作底稿修改失败");
            }
            res.put("result", "false");
        }
        return res;
    }

    /**
     * 获取工作底稿
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "获取工作底稿")
    @PostMapping(value = "/getEnforceLawWorkingPaper")
    public Map<String, Object> getEnforceLawWorkingPaper(
            @ApiParam(value = "工作底稿ID：PAPER_ID \n" +
                    "当前任务ID：TASK_ID\n" +
                    "用户所在部门ID：DEPARTMENT\n")
            @RequestBody(required = false) JSONObject param
    ) {
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> dataList = new HashMap<>();
        PageData pd = this.getPageData(param);
        pd.put("IS_REPORT", "0");
        Map<String, Object> contentHtml = inspectionQuestionLedgerService.assembleContent_html(pd);
        if ("false".equals(contentHtml.get("result"))) {
            res.put("msg", "信息查询失败");
            return res;
        }
        dataList = inspectionEnforceLawWorkingPaperService.getRecord(pd);
        if (dataList == null || dataList.isEmpty()) {
            dataList = new HashMap<>();
        }
        dataList.put("NOT_OTHER_CONTENT", pd.getString("NOT_OTHER_CONTENT"));
        dataList.put("OTHER_CONTENT", pd.getString("OTHER_CONTENT"));
        dataList.put("FILLING_DATE", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_CN));
        res.put("result", "success");
        res.put("rows", dataList);
        return res;
    }

    /**
     * 执法检查工作底稿导出
     *
     * @param param
     * @param response
     */
    @ApiOperation(value = "执法检查工作底稿导出")
    @GetMapping(value = "/downLoadEnforceLawWorkingPaper")
    public void downLoadEnforceLawWorkingPaper(
            @ApiParam(value = "工作底稿ID：PAPER_ID")
            @RequestBody(required = false) JSONObject param,
            HttpServletResponse response
    ) throws Exception {
        PageData pd = this.getPageData(param);
        Map<String, Object> dataList = inspectionEnforceLawWorkingPaperService.getRecord(pd);
        String filePath = dataList.get("PRINT_ATTACHMENT").toString();
        String fileName = dataList.get("ATTACHMENT_NAME").toString();
        FileDownload.fileDownload(response, filePath, fileName, this.getRequest());
    }


} ///:~

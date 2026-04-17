// InspectionPostSVWorkDiaryController.java

package org.inspect.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.inspect.BaseController;
import org.inspect.service.*;
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
@Api(tags = "事后监督工作日志")
@RestController
@RequestMapping(value = "/postSVWorkDiary", produces = MediaType.APPLICATION_JSON_VALUE)
public class InspectionPostSVworkDiaryController extends BaseController {

    @Value("${TEMPLATE_FILE_PATH}")
    private String saveDir;

    @Autowired
    protected InspectionPostSVLedgerService inspectionPostSVLedgerService;

    @Autowired
    private InspectionPostSVWorkDiaryService inspectionPostSVWorkDiaryService;

    @Autowired
    private InspectionProcessControlService inspectionProcessControlService;

    @Autowired
    private InspectionPostSVListService inspectionPostSVListService;

    /**
     * 新增工作日志
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "新增工作日志")
    @PostMapping(value = "/addPostSVWorkDiary")
    public Map<String, Object> addPostSVWorkDiary(
            @ApiParam(value = "工作日志ID：WD_ID \n" +
                    "当前任务ID：TASK_ID\n" +
                    "当前流程ID：PROC_ID。\n" +
                    "当前子流程ID：PROC_SUB_ID。\n" +
                    "当前用户ID：ADD_USERID。\n" +
                    "标题：TITLE。\n" +
                    "机构名称: BOOKORG_DSCR \n" +
                    "监督日期: FILLING_DATE \n" +
                    "国库核算人员: ACC_PER\n" +
                    "核算日期: ACC_DATE\n" +
                    "监督内容: SUV_CONTENT\n" +
                    "被监督的会计资料: SUVD_ACC_SCR\n" +
                    "监督记录: SUV_RECORD\n" +
                    "备注: REMARKS\n" +
                    "国库部门负责人（监督主管）：AFTER_HEAD\n" +
                    "监督人员：MEMBERS\n"
            )
            @RequestBody(required = false) JSONObject param
    ) {
        Map<String, Object> res = new HashMap<>();
        PageData pd = this.getPageData(param);
        String WD_ID = this.get32UUID();
        String endTime = DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS);
        String FILLING_DATE = DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_CN);
        String docName = "",
                targetFilePath = "",
                targetFilePath_NONAME = "",
                templateName = "",
                templetFilePath = "";

        docName = pd.getString("TITLE") + ".docx";
        templateName = "postSVWorkDiary.xml";
        templetFilePath = saveDir + "template/" + templateName;
        //目标文件存放路径
        targetFilePath = saveDir + pd.getString("TASK_ID") + "/事后监督工作日志/" + docName;
        targetFilePath_NONAME = saveDir + pd.getString("TASK_ID") + "/事后监督工作日志/print/" + docName;

        Map<String, Object> paperList = inspectionPostSVWorkDiaryService.getRecord(pd);
        if (paperList == null || paperList.isEmpty()) {
            pd.put("WD_ID", WD_ID);
        }

        res = inspectionPostSVLedgerService.assembleContent(pd);
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
                inspectionPostSVWorkDiaryService.addRecord(pd);
                res.put("msg", "工作日志新增成功");
            } else {
                pd.put("MODIFY_DATE", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
                pd.put("MODIFY_USERID", pd.getString("ADD_USERID"));
                inspectionPostSVWorkDiaryService.editRecord(pd);
                res.put("msg", "工作日志修改成功");
            }
            //当前流程完成
            pd.put("FINISH_TIME", endTime);
            inspectionProcessControlService.finishPostSVCurSubProcessById(pd);

            Xml2XmlDoc.xml2XmlDoc(pd, templetFilePath, targetFilePath);
            /*导出文件不包含检查人名*/
            pd.put("AFTER_HEAD", "");
            pd.put("MEMBERS", "");
            Xml2XmlDoc.xml2XmlDoc(pd, templetFilePath, targetFilePath_NONAME);
            res.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            if (paperList == null || paperList.isEmpty()) {
                res.put("msg", "工作日志新增失败");
            } else {
                res.put("msg", "工作日志修改失败");
            }
            res.put("result", "false");
        }
        return res;
    }

    /**
     * 获取工作日志
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "获取工作日志")
    @PostMapping(value = "/getPostSVWorkDiary")
    public Map<String, Object> getPostSVWorkDiary(
            @ApiParam(value = "工作日志ID：WD_ID \n" +
                    "当前任务ID：TASK_ID\n")
            @RequestBody(required = false) JSONObject param
    ) {
        String SUV_CONTENT = "默认监督内容，请输入具体内容",
                SUVD_ACC_SCR = "默认被监督的会计资料，请输入具体内容";
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> dataList = new HashMap<>();
        PageData pd = this.getPageData(param);

        Map<String, String> taskInfo = inspectionPostSVListService.getTaskInfoByTaskId(pd);
        Map<String, Object> contentHtml = inspectionPostSVLedgerService.assembleContent_html(pd);
        if ("false".equals(contentHtml.get("result"))) {
            res.put("msg", "信息查询失败");
            return res;
        }
        dataList = inspectionPostSVWorkDiaryService.getRecord(pd);
        if (dataList == null || dataList.isEmpty()) {
            dataList = new HashMap<>();
            dataList.put("BOOKORG_DSCR", taskInfo.get("BOOKORG_DSCR"));
            dataList.put("FILLING_DATE", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_CN));
            dataList.put("SUV_CONTENT", SUV_CONTENT);
            dataList.put("SUVD_ACC_SCR", SUVD_ACC_SCR);
        }
        dataList.put("NOT_OTHER_CONTENT", pd.getString("NOT_OTHER_CONTENT"));
        dataList.put("OTHER_CONTENT", pd.getString("OTHER_CONTENT"));
        res.put("result", "success");
        res.put("rows", dataList);
        return res;
    }

    /**
     * 工作日志导出
     *
     * @param param
     * @param response
     */
    @ApiOperation(value = "工作日志导出")
    @GetMapping(value = "/downLoadPostSVWorkDiary")
    public void downLoadPostSVWorkDiary(
            @ApiParam(value = "工作日志ID：WD_ID")
            @RequestBody(required = false) JSONObject param,
            HttpServletResponse response
    ) throws Exception {
        PageData pd = this.getPageData(param);
        Map<String, Object> dataList = inspectionPostSVWorkDiaryService.getRecord(pd);
        String fileName = dataList.get("ATTACHMENT_NAME").toString();
        String filePath = dataList.get("PRINT_ATTACHMENT").toString();
        FileDownload.fileDownload(response, filePath, fileName, this.getRequest());
    }


} ///:~

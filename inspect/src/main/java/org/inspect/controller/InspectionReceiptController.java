package org.inspect.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.inspect.BaseController;
import org.inspect.service.*;
import org.inspect.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 国家债券收款单现场检查账实核对表
 *
 * @author Created by dj on 2019/11/15
 */
@Slf4j
@RestController
@Api(tags = "国家债券收款单现场检查账实核对表")
@RequestMapping(value = "/inspectionReceipt", produces = MediaType.APPLICATION_JSON_VALUE)
public class InspectionReceiptController extends BaseController {

    @Value("${TEMPLATE_FILE_PATH}")
    private String saveDir;

    @Autowired
    private InspectionWorkingPaperService inspectionWorkingPaperService;

    @Autowired
    private InspectionReceiptService inspectionReceiptService;

    @Autowired
    private InspectionIssueListService inspectionIssueListService;

    @Autowired
    private InspectionProcessControlService inspectionProcessControlService;

    @Autowired
    private InspectionStatisticsTableService inspectionStatisticsTableService;
    @Autowired
    private InspectionCheckAccountSheetService inspectionCheckAccountSheetService;

    /**
     * 打包下载国家债券收款单现场检查账实核对表
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "打包下载国家债券收款单现场检查账实核对表")
    @GetMapping(value = "/downLoadCheckList")
    public void downLoadCheckList(
            @ApiParam("当前任务ID：TASK_ID \n")
            @RequestBody(required = false) JSONObject param,
            HttpServletResponse response
    ) throws Exception {
        PageData pd = this.getPageData(param);
        String zipFileName = saveDir + pd.getString("TASK_ID") + "/国家债券收款单现场检查账实核对表";
        ZipUtils.toZip(zipFileName, false);
        FileDownload.fileDownload(response, zipFileName + ".zip", "国家债券收款单现场检查账实核对表.zip", this.getRequest());
    }


    /**
     * 新增国家债券收款单现场检查账实核对表
     *
     * @param param param
     */
    @ApiOperation(value = "新增国家债券收款单现场检查账实核对表")
    @PostMapping(value = "/addReceiptList")
    public Map<String, Object> addDataCheckList(
            @RequestBody(required = false) JSONObject param
    ) throws UnsupportedEncodingException {
        Map<String, Object> result = new HashMap<String, Object>();
        String SHEET_ID = this.get32UUID();
        PageData pd = this.getPageData(param);
        pd.put("SHEET_ID", SHEET_ID);
        pd.put("ADD_DATE", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
        pd.put("MODIFY_USERID", "");
        pd.put("MODIFY_DATE", "");
        String templetFilePath = saveDir + "template/receipt.xml";
        String docName = pd.get("INSPECTED") + "国家债券收款单现场检查账实核对表.docx";
        //目标文件存放路径
        String targetFilePath = saveDir + pd.getString("TASK_ID") + "/国家债券收款单现场检查账实核对表/" + docName;
        String targetFilePath_NONAME = saveDir + pd.getString("TASK_ID") + "/print/" + docName;
        /*电子文档路径*/
        pd.put("PAPER_ATTACHMENT", targetFilePath);
        pd.put("PAPER_ATTACHMENT", targetFilePath_NONAME);
        Integer rs = checkRepeat(pd);
        try {
            if (null != rs && rs > 0) {
                result.put("msg", "新增失败！已存在，请修改后重试。");
                result.put("result", "false");
            } else {
                inspectionReceiptService.addReceiptList(pd);
                result.put("msg", "新增成功");
                result.put("result", "success");
            }
            Xml2XmlDoc.xml2XmlDoc(pd, templetFilePath, targetFilePath);
            pd.put("LEADER", "");
            pd.put("DEPARTMENT_HEAD", "");
            pd.put("MEMBER", "");
            Xml2XmlDoc.xml2XmlDoc(pd, templetFilePath, targetFilePath_NONAME);
            //当前流程完成，尝试将大流程状态改为完成。
            pd.put("FINISH_TIME", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
            pd.put("PROC_ID", pd.get("PROC_SUB_ID").toString().substring(0, 5));
            inspectionProcessControlService.finishCurSubProcessById(pd);
            if (inspectionProcessControlService.activateFollowProc(pd)) {
                /*result.put("msg", result.get("msg") + inspectionProcessControlService.getProcDescById(pd) + "流程结束，后续流程已激活");*/
            }

        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", "新增失败");
            result.put("result", "false");
        }
        return result;
    }

    /**
     * 修改
     *
     * @param param param
     */
    @ApiOperation(value = "修改国家债券收款单现场检查账实核对表")
    @PostMapping(value = "/editReceiptList")
    public Map<String, Object> editReceiptList(
            @RequestBody(required = false) JSONObject param
    ) throws UnsupportedEncodingException {
        Map<String, Object> result = new HashMap<String, Object>();
        PageData pd = this.getPageData(param);
        pd.put("MODIFY_DATE", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
        String templetFilePath = saveDir + "template/receipt.xml";
        String docName = pd.get("INSPECTED") + "国家债券收款单现场检查账实核对表.docx";
        //目标文件存放路径
        String targetFilePath = saveDir + pd.getString("TASK_ID") + "/国家债券收款单现场检查账实核对表/" + docName;
        String targetFilePath_NONAME = saveDir + pd.getString("TASK_ID") + "/print/" + docName;
        /*电子文档路径*/
        pd.put("PAPER_ATTACHMENT", targetFilePath);
        pd.put("PAPER_ATTACHMENT", targetFilePath_NONAME);
        try {

            inspectionReceiptService.updateReceiptById(pd);
            //当前流程完成，尝试将大流程状态改为完成。
            pd.put("FINISH_TIME", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
            pd.put("PROC_ID", pd.get("PROC_SUB_ID").toString().substring(0, 5));
            inspectionProcessControlService.finishCurSubProcessById(pd);
            if (inspectionProcessControlService.activateFollowProc(pd)) {
                /*result.put("msg", result.get("msg") + inspectionProcessControlService.getProcDescById(pd) + "流程结束，后续流程已激活");*/
            }
            result.put("msg", "修改成功");
            result.put("result", "success");
            Xml2XmlDoc.xml2XmlDoc(pd, templetFilePath, targetFilePath);
            pd.put("LEADER", "");
            pd.put("DEPARTMENT_HEAD", "");
            pd.put("MEMBER", "");
            Xml2XmlDoc.xml2XmlDoc(pd, templetFilePath, targetFilePath_NONAME);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", "修改失败");
            result.put("result", "false");
        }

        return result;
    }

    private Integer checkRepeat(PageData pd) {
        List<Map<String, Object>> data = inspectionReceiptService.checkRepeat(pd);
        if (null != data && data.size() > 0) {
            return data.size();
        }
        return 0;
    }

    /**
     * 获取所有国家债券收款单现场检查账实核对表列表
     */
    @RequestMapping(value = {"/getInspectionCheck"}, method = RequestMethod.POST, produces = {"application/json"})
    @ApiOperation("获取所有国家债券收款单现场检查账实核对表列表")
    public Map<String, Object> getInspectionCheck(@RequestBody(required = false) JSONObject jsonob) {
        Map<String, Object> result = new HashMap<>();
        PageData pd = new PageData(jsonob);
        try {
            List<Map<String, Object>> data = inspectionReceiptService.getInspectionCheck(pd);
            result.put("rows", data);//rows键 存放每页记录 list
            result.put("msg", "查询成功");
            result.put("result", "success");
        } catch (Exception e) {
            result.put("msg", e.getMessage());
            result.put("result", "查询失败");
        }
        return result;
    }

    @RequestMapping(value = "/delInspectionCheck", method = RequestMethod.POST, produces = {"application/json"})
    @ApiOperation("删除")
    public Map<String, Object> delInspectionCheck(@RequestBody(required = false) JSONObject jsonob
    ) {
        Map<String, Object> result = new HashMap<>();
        PageData pd = new PageData(jsonob);
        try {
            inspectionReceiptService.delInspectionCheck(pd);
            result.put("msg", "删除成功");
            result.put("result", "success");
        } catch (Exception e) {
            result.put("msg", "删除失败");
            result.put("result", "failed");
        }
        return result;
    }

    @RequestMapping(value = "/skipInspection", method = RequestMethod.POST, produces = {"application/json"})
    @ApiOperation("跳过")
    public Map<String, Object> skipInspection(@RequestBody(required = false) JSONObject jsonob
    ) {
        Map<String, Object> result = new HashMap<>();
        PageData pd = new PageData(jsonob);
        try {
            pd.put("FINISH_TIME", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
            inspectionReceiptService.skipInspection(pd);
            result.put("msg", "跳过成功");
            result.put("result", "success");
        } catch (Exception e) {
            result.put("msg", "跳过失败");
            result.put("result", "failed");
        }
        return result;
    }

}

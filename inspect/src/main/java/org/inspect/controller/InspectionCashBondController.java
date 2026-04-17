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
 * 已兑付国家债券现场检查账实核对表
 *
 * @author Created by dj on 2019/11/15
 */
@Slf4j
@RestController
@Api(tags = "已兑付国家债券现场检查账实核对表")
@RequestMapping(value = "/inspectionCashBond", produces = MediaType.APPLICATION_JSON_VALUE)

public class InspectionCashBondController extends BaseController {

    @Value("${TEMPLATE_FILE_PATH}")
    private String saveDir;


    @Autowired
    private InspectionWorkingPaperService inspectionWorkingPaperService;

    @Autowired
    private InspectionCashBondService inspectionCashBondService;

    @Autowired
    private InspectionIssueListService inspectionIssueListService;

    @Autowired
    private InspectionProcessControlService inspectionProcessControlService;

    @Autowired
    private InspectionCheckAccountSheetService inspectionCheckAccountSheetService;

    /**
     * 打包下载统计数据核对情况检查表
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "打包下载已兑付国家债券现场检查账实核对表")
    @GetMapping(value = "/downLoadCheckList")
    public void downLoadCheckList(
            @ApiParam("当前任务ID：TASK_ID \n")
            @RequestBody(required = false) JSONObject param,
            HttpServletResponse response
    ) throws Exception {
        PageData pd = this.getPageData(param);
        String zipFileName = saveDir + pd.getString("TASK_ID") + "/已兑付国家债券现场检查账实核对表";
        ZipUtils.toZip(zipFileName, false);
        FileDownload.fileDownload(response, zipFileName + ".zip", "已兑付国家债券现场检查账实核对表.zip", this.getRequest());
    }

    /**
     * 新增已兑付国家债券现场检查账实核对表
     *
     * @param param param
     */
    @ApiOperation(value = "新增已兑付国家债券现场检查账实核对表")
    @PostMapping(value = "/addCashBondList")
    public Map<String, Object> addCashBondList(
            @RequestBody(required = false) JSONObject param
    ) throws UnsupportedEncodingException {
        Map<String, Object> result = new HashMap<String, Object>();
        String SHEET_ID = this.get32UUID();
        PageData pd = this.getPageData(param);
        pd.put("SHEET_ID", SHEET_ID);
        pd.put("ADD_DATE", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
        pd.put("MODIFY_USERID", "");
        pd.put("MODIFY_DATE", "");
        String templetFilePath = saveDir + "template/cashBond.xml";
        String docName = pd.get("INSPECTED") + "已兑付国家债券现场检查账实核对表.docx";
        //目标文件存放路径
        String targetFilePath = saveDir + pd.getString("TASK_ID") + "/已兑付国家债券现场检查账实核对表/" + docName;
        String targetFilePath_NONAME = saveDir + pd.getString("TASK_ID") + "/print/" + docName;
        /*电子文档路径*/
        pd.put("PAPER_ATTACHMENT", targetFilePath);
        /*导出文档路径*/
        pd.put("PRINT_ATTACHMENT", targetFilePath_NONAME);
        Integer rs = checkRepeat(pd);
        try {
            if (null != rs && rs > 0) {
                result.put("msg", "新增失败！已存在，请修改后重试。");
                result.put("result", "false");
            } else {
                inspectionCashBondService.addInspectionCashBond(pd);
//                inspectionCashBondService.addInspectionDataCheck(pd);
                result.put("msg", "数据核对新增成功");
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
    @ApiOperation(value = "修改")
    @PostMapping(value = "/editCashBondList")
    public Map<String, Object> editCashBondList(
            @RequestBody(required = false) JSONObject param
    ) throws UnsupportedEncodingException {
        Map<String, Object> result = new HashMap<String, Object>();
        PageData pd = this.getPageData(param);
        pd.put("MODIFY_DATE", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
        String templetFilePath = saveDir + "template/cashBond.xml";
        String docName = pd.get("TASK_TYPE_DSCR") + "已兑付国家债券现场检查账实核对表.docx";
//        //目标文件存放路径
        String targetFilePath = saveDir + pd.getString("TASK_ID") + "/已兑付国家债券现场检查账实核对表/" + docName;
        String targetFilePath_NONAME = saveDir + pd.getString("TASK_ID") + "/print/" + docName;
        try {

            inspectionCashBondService.updateCashBondById(pd);
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
        List<Map<String, Object>> data = inspectionCashBondService.checkRepeat(pd);
        if (null != data && data.size() > 0) {
            return data.size();
        }
        return 0;
    }

    /**
     * 获取所有数据核对列表
     */
    @RequestMapping(value = {"/getInspectionCheck"}, method = RequestMethod.POST, produces = {"application/json"})
    @ApiOperation("获取所有数据核对列表")
    public Map<String, Object> getInspectionCheck(@RequestBody(required = false) JSONObject jsonob) {
        Map<String, Object> result = new HashMap<>();
        PageData pd = new PageData(jsonob);
        try {
            List<Map<String, Object>> data = inspectionCashBondService.getInspectionCheck(pd);
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
            inspectionCashBondService.delInspectionCashBond(pd);
            result.put("msg", "删除成功");
            result.put("result", "success");
        } catch (Exception e) {
            result.put("msg", "删除失败");
            result.put("result", "failed");
        }
        return result;
    }

} ///:~

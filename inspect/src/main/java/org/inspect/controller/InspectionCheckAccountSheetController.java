// InspectionCheckAccountSheetController.java

package org.inspect.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.inspect.BaseController;
import org.inspect.service.InspectionCheckAccountSheetService;
import org.inspect.service.InspectionProcessControlService;
import org.inspect.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * 对账登记表
 *
 * @author Created by Samer on 2019/10/21.
 */

@Slf4j
@RestController
@Api(tags = "对账登记表")
@RequestMapping(value = "/inspectionCheckAccountSheet", produces = MediaType.APPLICATION_JSON_VALUE)
public class InspectionCheckAccountSheetController extends BaseController {

    @Value("${TEMPLATE_FILE_PATH}")
    private String saveDir;

    @Autowired
    private InspectionCheckAccountSheetService inspectionCheckAccountSheetService;

    @Autowired
    private InspectionProcessControlService inspectionProcessControlService;

    /**
     * 编辑对账登记表
     *
     * @return
     */
    @ApiOperation(value = "编辑对账登记表")
    @PostMapping(value = "/editCheckAccountSheet")
    public Map<String, String> editCheckAccountSheet(
            @ApiParam(value =
                    "当前对账登记表ID：SHEET_ID\n" +
                            "登记表名称：SHEET_NAME\n" +
                            "被查库编码：INSPECTED_GUOKU_ID\n" +
                            "对账机构编码：CHECK_ACCOUNT_ORG_ID\n" +
                            /*"修改人ID：MODIFY_USERID\n" +*/
                            "表格内容数组：sheetContentArr[" +
                            "   账务日期：ACC_DATE\n" +
                            "   对账内容：ACC_CONTENT\n" +
                            "   国库金额：TRE_AMT}\n" +
                            "   对账单位金额：CHECK_ORG_AMT\n" +
                            "   差值：DIF_AMT\n" +
                            "   备注：REMARKS\n" +
                            "] ")
            @RequestBody(required = false) JSONObject param
    ) {
        Map<String, String> res = new HashMap<String, String>();
        PageData pd = this.getPageData(param);
        if (checkSheetName(pd)) {
            res.put("msg", "对账登记表新增失败,登记表名称重复。");
            res.put("result", "false");
            return res;
        }
        if (checkInspectGuokuAndTaxOrg(pd)) {
            res.put("msg", "对账登记表新增失败,被查库与对账单位的组合重复。");
            res.put("result", "false");
            return res;
        }
        pd.put("MODIFY_DATE", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
        try {
            /*删除源文件*/
            List<Map<String, Object>> dataList = inspectionCheckAccountSheetService.getCheckAccSubInfoBySheetId(pd);
            String filepath = saveDir + "/" + dataList.get(0).get("TASK_ID").toString() + "/对账登记表/";
            String filename = dataList.get(0).get("SHEET_NAME").toString() + ".xls";
            File del_file = new File(filepath + filename);
            if (del_file.exists()) {
                del_file.delete();
            }
            /*删除对账登记表子表信息*/
            inspectionCheckAccountSheetService.delCheckAccSubBySheetId(pd);
            /*根据SHEET_ID更新对账登记表信息*/
            pd.put("FINISH_TIME", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
            inspectionCheckAccountSheetService.updateCheckAccountInfo(pd);
            List<Map<String, String>> sheetContentList = (List<Map<String, String>>) JSONArray.parse((String) pd.get("sheetContentArr"));
            if (sheetContentList != null && !sheetContentList.isEmpty()) {
                pd.put("sheetContentList", sheetContentList);
                inspectionCheckAccountSheetService.addCheckAccSubInfo(pd);
            }
            createSingleExcel(pd);
            res.put("msg", "对账登记表编辑成功");
            res.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            res.put("msg", "对账登记表编辑失败");
            res.put("result", "false");
        }
        return res;
    }

    /**
     * 根据对账登记表ID获取详细信息
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "根据对账登记表ID获取详细信息")
    @PostMapping(value = "/getCheckAccSubInfoBySheetId")
    public Map<String, Object> getCheckAccSubInfoBySheetId(
            @ApiParam(value = "当前对账登记表ID：SHEET_ID")
            @RequestBody(required = false) JSONObject param
    ) {
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        List<Map<String, Object>> dataList = inspectionCheckAccountSheetService.getCheckAccSubInfoBySheetId(pd);
        for(int i = 0; i < dataList.size(); i++){
            List<Map<String, String>> subDataList = inspectionCheckAccountSheetService.getSheetSubInfo(pd);
            dataList.get(i).put("dataList", subDataList);
        }
        getOrdDscrList(dataList);
        result.put("result", "success");
        result.put("rows", dataList);
        return result;
    }

    /**
     * 根据任务ID获取对账登记表信息
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "根据任务ID获取对账登记表信息")
    @PostMapping(value = "/getCheckAccInfoByTaskId")
    public Map<String, Object> getCheckAccInfoByTaskId(
            @ApiParam(value = "当前任务ID：TASK_ID")
            @RequestBody(required = false) JSONObject param
    ) {
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        List<Map<String, String>> dataList = inspectionCheckAccountSheetService.getCheckAccInfoByTaskId(pd);
        result.put("result", "success");
        result.put("rows", dataList);
        return result;

    }

    /**
     * 新增对账登记表
     *
     * @return
     */
    @ApiOperation(value = "新增对账登记表")
    @PostMapping(value = "/addCheckAccountSheet")
    public Map<String, String> addCheckAccountSheet(
            @ApiParam(value =
                    "所属任务ID：TASK_ID\n" +
                            "当前任务所处大流程ID：PROC_ID,\n" +
                            "当前任务所处小流程ID：PROC_SUB_ID,\n" +
                            "登记表名称：SHEET_NAME\n" +
                            "被查库编码：INSPECTED_GUOKU_ID\n" +
                            "对账机构编码：CHECK_ACCOUNT_ORG_ID\n" +
                            "添加人ID：ADD_USERID\n" +
                            "表格内容数组：sheetContentArr[" +
                            "账务日期：ACC_DATE\n" +
                            "对账内容：ACC_CONTENT\n" +
                            "国库金额：TRE_AMT}\n" +
                            "对账单位金额：CHECK_ORG_AMT\n" +
                            "差值：DIF_AMT\n" +
                            "备注：REMARKS\n" +
                            "] ")
            @RequestBody(required = false) JSONObject param
    ) {
        String uuid = UuidUtil.get32UUID();
        Map<String, String> res = new HashMap<String, String>();
        PageData pd = this.getPageData(param);
        if (checkSheetName(pd)) {
            res.put("msg", "对账登记表新增失败,登记表名称重复。");
            res.put("result", "false");
            return res;
        }
        if (checkInspectGuokuAndTaxOrg(pd)) {
            res.put("msg", "对账登记表新增失败,被查库与对账单位的组合重复。");
            res.put("result", "false");
            return res;
        }
        pd.put("SHEET_ID", uuid);
        pd.put("ADD_DATE", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
        try {
            inspectionCheckAccountSheetService.addCheckAccountInfo(pd);
            List<Map<String, String>> sheetContentList = (List<Map<String, String>>) JSONArray.parse((String) pd.get("sheetContentArr"));
            if (sheetContentList != null && !sheetContentList.isEmpty()) {
                pd.put("sheetContentList", sheetContentList);
                inspectionCheckAccountSheetService.addCheckAccSubInfo(pd);
            }
            createSingleExcel(pd);
            res.put("msg", "对账登记表新增成功");
            //当前流程完成，尝试将大流程状态改为完成。
            pd.put("FINISH_TIME", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
            inspectionProcessControlService.finishCurSubProcessById(pd);
            if (inspectionProcessControlService.activateFollowProc(pd)) {
                /*res.put("msg", res.get("msg") + inspectionProcessControlService.getProcDescById(pd) + "流程结束，后续流程已激活");*/
            }
            res.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            res.put("msg", "对账登记表新增失败");
            res.put("result", "false");
        }
        return res;
    }

    /**
     * 删除对账登记表
     *
     * @return
     */
    @ApiOperation(value = "删除对账登记表")
    @PostMapping(value = "/delCheckAccountSheet")
    public Map<String, String> delCheckAccountSheet(
            @ApiParam(value = "当前对账登记表ID：SHEET_ID")
            @RequestBody(required = false) JSONObject param
    ) {
        Map<String, String> res = new HashMap<String, String>();
        PageData pd = this.getPageData(param);
        try {
            inspectionCheckAccountSheetService.delCheckAccInfoBySheetId(pd);
            inspectionCheckAccountSheetService.delCheckAccSubBySheetId(pd);
            res.put("msg", "对账登记表删除成功");
            res.put("result", "success");
        } catch (Exception e) {
            res.put("msg", "对账登记表删除失败");
            res.put("result", "false");
        }
        return res;
    }

    /**
     * 获取征收机构信息
     *
     * @return
     */
    @ApiOperation(value = "获取征收机构信息")
    @PostMapping(value = "/getTaxOrgInfo")
    public Map<String, Object> getTaxOrgInfo() {
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(null);
        List<Map<String, String>> dataList = inspectionCheckAccountSheetService.getTaxOrgInfo(pd);
        if( dataList == null || dataList.size() ==0 ){
            result.put("result", "false");
            result.put("msg", "查询无数据");
        } else {
            result.put("result", "success");
            result.put("rows", dataList);
        }
        return result;

    }

    /**
     * 根据当前任务ID获取核算主体包含的国库
     *
     * @param param params.TASK_ID 当前任务ID
     * @return
     */
    @ApiOperation(value = "根据当前任务ID获取核算主体包含的国库")
    @PostMapping(value = "/getCurTreCodeByTaskId")
    public Map<String, Object> getCurTreCodeByTaskId(
            @ApiParam(value = "当前任务ID：TASK_ID")
            @RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        List<Map<String, String>> dataList = inspectionCheckAccountSheetService.getCurTreCodeByTaskId(pd);
        if( dataList == null || dataList.size() ==0 ){
            result.put("result", "false");
            result.put("msg", "查询无数据");
        } else {
            result.put("result", "success");
            result.put("rows", dataList);
        }
        return result;
    }

    /**
     * 检查当前登记表是否重名
     *
     * @param pd
     * @return false:没有重复，true:有重复的。
     */
    private boolean checkSheetName(PageData pd) {
        List<Map<String, String>> dataList = inspectionCheckAccountSheetService.getSheetNameByName(pd);
        if (!dataList.isEmpty()) {
            return true;
        }
        return false;
    }


    /**
     * 检查当前被查库与对账单位组合是否重复
     *
     * @param pd
     * @return false:没有重复的组合，true:有重复的组合。
     */
    private boolean checkInspectGuokuAndTaxOrg(PageData pd) {
        List<Map<String, String>> dataList = inspectionCheckAccountSheetService.getTaxOrgIdById(pd);
        if (dataList.isEmpty()) {
            return false;
        }
        if (!dataList.isEmpty()) {
            String[] taxOrgArr = ((String) pd.get("CHECK_ACCOUNT_ORG_ID")).split(",");
            Arrays.sort(taxOrgArr);
            for (int i = 0, len = dataList.size(); i < len; i++) {
                String[] tableTaxOrgArr = dataList.get(i).get("CHECK_ACCOUNT_ORG_ID").split(",");
                Arrays.sort(tableTaxOrgArr);
                if (taxOrgArr.length != tableTaxOrgArr.length) {
                    return false;
                } else {
                    for (int j = 0, length = taxOrgArr.length; j < length; j++) {
                        if (!taxOrgArr[j].equals(tableTaxOrgArr[j])) {
                            return false;
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

    /*创建表*/
    private boolean createSingleExcel(PageData pd) throws IOException {
        List<Map<String, Object>> dataList = inspectionCheckAccountSheetService.getCheckAccSubInfoBySheetId(pd);
        getOrdDscrList(dataList);
        String title = "与财政、征收机关对账情况登记表";
        String ADD_DATE = "填制日期：" + dataList.get(0).get("ADD_DATE").toString();
        String sub_title_1 = "国库：" + dataList.get(0).get("INSPECTED_GUOKU_DSCR");
        String sub_title_2 = "对账单位：" + dataList.get(0).get("CHECK_ACCOUNT_ORG_DSCR");
        String sub_title_3 = "金额单位: 元";

        String DEPARTMENT_HEAD = "被查国库部门负责人：" + dataList.get(0).get("DEPARTMENT_HEAD").toString();
        String GROUP_LEADER_FT = "检查组组长：" + dataList.get(0).get("GROUP_LEADER_FT");
        String NAME = "检查人：" + dataList.get(0).get("NAME");

        String filepath = saveDir + "/" + dataList.get(0).get("TASK_ID") + "/对账登记表/";
        String filename = dataList.get(0).get("SHEET_NAME").toString() + ".xls";
        List<String> titlelist = new ArrayList<String>();
        String[] zdlist = {
                "ACC_DATE",
                "ACC_CONTENT",
                "TRE_AMT",
                "CHECK_ORG_AMT",
                "DIF_AMT",
                "REMARKS"
        };
        titlelist.add("账务日期");
        titlelist.add("对账内容（报表或项目");
        titlelist.add("国库金额（1）");
        titlelist.add("对账单位金额（2）");
        titlelist.add("差额（1）－（2）");
        titlelist.add("备注");
        ExcelCreateHelper.createSimpleExcel(title, ADD_DATE,
                sub_title_1, sub_title_2, sub_title_3,
                filepath, filename,
                titlelist, zdlist, dataList,
                DEPARTMENT_HEAD, GROUP_LEADER_FT, NAME);
        return true;
    }

    /*对账机构拼接*/
    private void getOrdDscrList(List<Map<String, Object>> dataList) {
        if (!dataList.isEmpty()) {
            String[] checkOrgId = ((String) dataList.get(0).get("CHECK_ACCOUNT_ORG_ID")).split(",");
            String checkOrgDscr = "";
            PageData subParam = new PageData();
            for (int i = 0, len = checkOrgId.length; i < len; i++) {
                subParam.put("TAX_ORG_ID", checkOrgId[i]);
                List<Map<String, String>> taxOrgList = inspectionCheckAccountSheetService.getTaxOrgInfo(subParam);
                checkOrgDscr += taxOrgList.get(0).get("name") + ((i == (len - 1)) ? "" : ",");
            }
            for (int j = 0, len = dataList.size(); j < len; j++) {
                dataList.get(j).put("CHECK_ACCOUNT_ORG_DSCR", checkOrgDscr);
            }
        }
    }

    /**
     * 打包下载对账登记表
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "打包下载对账登记表")
    @GetMapping(value = "/downLoadCheckAcctSheet")
    public void downLoadCheckAcctSheet(
            @ApiParam("当前任务ID：TASK_ID \n")
            @RequestBody(required = false) JSONObject param,
            HttpServletResponse response
    ) throws Exception {
        PageData pd = this.getPageData(param);
        String zipFileName = saveDir + "/" + pd.getString("TASK_ID") + "/对账登记表";
        /*String tempName_1 = saveDir + "/" + pd.getString("TASK_ID") + "/对账登记表";
        FileZip.zip(tempName_1, zipFileName);*/
        ZipUtils.toZip(zipFileName,  false);
        FileDownload.fileDownload(response, zipFileName + ".zip", "对账登记表.zip", this.getRequest());
    }

} ///:~

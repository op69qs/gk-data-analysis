// InspectionPostSVReportController.java

package org.inspect.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.inspect.BaseController;
import org.inspect.service.InspectionPostSVLedgerService;
import org.inspect.service.InspectionPostSVReportService;
import org.inspect.util.DateUtil;
import org.inspect.util.FileDownload;
import org.inspect.util.PageData;
import org.inspect.util.Xml2XmlDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Created by Samer on 2019/11/21.
 */
@Slf4j
@Api(tags = "事后监督监督季度报告")
@RestController
@RequestMapping(value = "/postSVReport", produces = MediaType.APPLICATION_JSON_VALUE)
public class InspectionPostSVReportController extends BaseController {

    @Value("${TEMPLATE_FILE_PATH}")
    private String saveDir;

    @Autowired
    protected InspectionPostSVLedgerService inspectionPostSVLedgerService;

    @Autowired
    private InspectionPostSVReportService inspectionPostSVReportService;

    /**
     * 编辑监督季度报告
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "编辑监督季度报告")
    @PostMapping(value = "/editPostSVReport")
    public Map<String, Object> editPostSVReport(
            @ApiParam(value = "监督季度报告ID：REPORT_ID \n" +
                    "当前用户ID：ADD_USERID。\n" +
                    "预算收入业务：INCOME_STATE。\n" +
                    "库款支拨业务：PAYOUT_STATE。\n" +
                    "预算收入退付业务：BACK_STATE。\n" +
                    "更正业务：CORR_STATE。\n" +
                    "国债业务：NB_STATE。\n" +
                    "对账业务：ACCT_STATE。\n" +
                    "记账、复核：REV_ACC_STATE。\n" +
                    "挂账解挂：HANG_STATE。\n" +
                    "退汇转汇：REMIT_STATE。\n" +
                    "查询查复：QUERY_STATE。\n" +
                    "手工填制凭证：HAND_VOU_STATE。\n" +
                    "资金清算-其他：CLEAR_OTHERS。\n" +
                    "账户、凭证、重要空白凭证的使用情况：VOU_USE_STATE。\n" +
                    "业务交接与资料传递的情况：BIZ_HO_STATE。\n" +
                    "会计人员岗位调整情况：ACCT_POS_STATE。\n" +
                    "其他：OTHERS。\n" +
                    "国库部门负责人：AFTER_HEAD。\n" +
                    "制表：LISTER。\n"
            )
            @RequestBody(required = false) JSONObject param
    ) {
        PageData pd = this.getPageData(param);
        return editPostSVReportSub(pd);
    }

    /**
     * 获取监督季度报告
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "获取监督季度报告")
    @PostMapping(value = "/getPostSVReport")
    public Map<String, Object> getPostSVReport(
            @ApiParam(value = "监督季度报告ID：REPORT_ID \n" +
                    "当前用户国库ID：guoku_id \n" +
                    "开始年：START_YEAR \n" +
                    "开始季度：START_QUARTER \n" +
                    "结束年：END_YEAR \n" +
                    "结束季度：END_QUARTER \n" +
                    "被查库ID：INSPECTED_GUOKU_ID \n"
            )
            @RequestBody(required = false) JSONObject param
    ) {
        Map<String, Object> res = new HashMap<>();
        List<Map<String, Object>> dataList = new ArrayList<>();
        PageData pd = this.getPageData(param);
        Integer pageNo = pd.getString("pageNo") == null ? null : (Integer.parseInt(pd.getString("pageNo")) - 1) * Integer.parseInt(pd.getString("pageSize"));
        pd.put("page", pageNo);
        Integer rows = pd.getString("pageSize") == null ? null : Integer.parseInt(pd.getString("pageSize"));
        pd.put("rows", rows);
        pd.put("START_DATE", pd.getString("START_DATE") == null ? "" : pd.getString("START_DATE").replace("Q", "0"));
        pd.put("END_DATE", pd.getString("END_DATE") == null ? "" : pd.getString("END_DATE").replace("Q", "0"));

        dataList = inspectionPostSVReportService.getRecord(pd);

        Integer count = inspectionPostSVReportService.getRecordCount(pd);

        for (int i = 0; i < dataList.size(); i++) {
            String questionContent = dataList.get(i).get("QUESTION_CONTENT") == null ? "" : dataList.get(i).get("QUESTION_CONTENT").toString();
            String memo = dataList.get(i).get("REFORM_MEMO") == null ? "" : dataList.get(i).get("REFORM_MEMO").toString();
            dataList.get(i).put("QUESTION_CONTENT", transStrContent(questionContent, "    ", "\n"));
            dataList.get(i).put("REFORM_MEMO", transStrContent(memo, "    ", "\n"));
            addDscr(dataList.get(i), dataList.get(i));
        }

        res.put("total", count);
        res.put("result", "success");
        res.put("rows", dataList);
        return res;
    }

    /**
     * 监督季度报告导出
     *
     * @param param
     * @param response
     */
    @ApiOperation(value = "监督季度报告导出")
    @GetMapping(value = "/downLoadPostSVReport")
    public void downLoadPostSVReport(
            @ApiParam(value = "监督季度报告ID：REPORT_ID \n"/* +
                    "当前用户ID：ADD_USERID。\n" +
                    "预算收入业务：INCOME_STATE。\n" +
                    "库款支拨业务：PAYOUT_STATE。\n" +
                    "预算收入退付业务：BACK_STATE。\n" +
                    "更正业务：CORR_STATE。\n" +
                    "国债业务：NB_STATE。\n" +
                    "对账业务：ACCT_STATE。\n" +
                    "记账、复核：REV_ACC_STATE。\n" +
                    "挂账解挂：HANG_STATE。\n" +
                    "退汇转汇：REMIT_STATE。\n" +
                    "查询查复：QUERY_STATE。\n" +
                    "手工填制凭证：HAND_VOU_STATE。\n" +
                    "资金清算-其他：CLEAR_OTHERS。\n" +
                    "账户、凭证、重要空白凭证的使用情况：VOU_USE_STATE。\n" +
                    "业务交接与资料传递的情况：BIZ_HO_STATE。\n" +
                    "会计人员岗位调整情况：ACCT_POS_STATE。\n" +
                    "其他：OTHERS。\n" +
                    "国库部门负责人：AFTER_HEAD。\n" +
                    "制表：LISTER。\n"*/
            )
            @RequestBody(required = false) JSONObject param,
            HttpServletResponse response
    ) throws Exception {
        PageData pd = this.getPageData(param);

        //新增
        //editPostSVReportSub(pd);

        List<Map<String, Object>> dataList = inspectionPostSVReportService.getRecord(pd);
        String fileName = dataList.get(0).get("ATTACHMENT_NAME").toString();
        String filePath = dataList.get(0).get("PRINT_ATTACHMENT").toString();

        FileDownload.fileDownload(response, filePath, fileName, this.getRequest());
    }

    /*字符串转置*/
    private String transStrContent(String content, String header, String endStr) {
        String temStr = "";
        String[] strArr = content.split("\n");
        for (int i = 0; i < strArr.length; i++) {
            temStr += strArr[i].replace("    ", header + (i + 1) + "）.") + endStr;
        }
        return temStr;
    }

    /*编辑保存*/
    private Map<String, Object> editPostSVReportSub(PageData pd) {
        Map<String, Object> res = new HashMap<>();
        String endTime = DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS);
        String FILLING_DATE = DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_CN);
        String docName = "",
                targetFilePath = "",
                targetFilePath_NONAME = "",
                templateName = "",
                templetFilePath = "";

        Map<String, Object> dataList = (Map<String, Object>) ((inspectionPostSVReportService.getRecord(pd)).get(0));

        docName = "国库业务事后监督报告.docx";
        templateName = "postSVReport.xml";
        templetFilePath = saveDir + "template/" + templateName;
        //目标文件存放路径
        targetFilePath = saveDir + dataList.get("YEAR").toString() + "国库业务事后监督报告/第" + dataList.get("QUARTER").toString() + "季度/" + docName;
        targetFilePath_NONAME = saveDir + dataList.get("YEAR").toString() + "国库业务事后监督报告/第" + dataList.get("QUARTER").toString() + "季度/print/" + docName;


        /*电子档检查人写入*/
        pd.put("PAPER_ATTACHMENT", targetFilePath);
        /*导出文档检查人不写入*/
        pd.put("PRINT_ATTACHMENT", targetFilePath_NONAME);
        pd.put("ATTACHMENT_NAME", docName);
        try {
            pd.put("MODIFY_DATE", endTime);
            pd.put("MODIFY_USERID", pd.get("ADD_USERID"));
            inspectionPostSVReportService.editRecord(pd);
            res.put("msg", "监督季度报告修改成功");

            dataList = (Map<String, Object>) ((inspectionPostSVReportService.getRecord(pd)).get(0));
            addDscr(dataList, pd);

            pd.put("PERIOD_YEAR", dataList.get("PERIOD_YEAR"));
            pd.put("PERIOD_NUM", dataList.get("PERIOD_NUM"));
            pd.put("PERIOD_AMT", dataList.get("PERIOD_AMT"));
            pd.put("BOOKORG_DSCR", dataList.get("BOOKORG_DSCR"));
            pd.put("FILLING_DATE", FILLING_DATE);
            String questionContent = dataList.get("QUESTION_CONTENT") == null ? "" : dataList.get("QUESTION_CONTENT").toString();
            String memo = dataList.get("REFORM_MEMO") == null ? "" : dataList.get("REFORM_MEMO").toString();
            pd.put("QUESTION_CONTENT", transStrContent(questionContent, "&#12288;&#12288;", "<w:br/>"));
            pd.put("REFORM_MEMO", transStrContent(memo, "&#12288;&#12288;", "<w:br/>"));
            Xml2XmlDoc.xml2XmlDoc(pd, templetFilePath, targetFilePath);
            /*导出文件不包含检查人名*/
            pd.put("AFTER_HEAD", "");
            pd.put("LISTER", "");
            Xml2XmlDoc.xml2XmlDoc(pd, templetFilePath, targetFilePath_NONAME);
            res.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            res.put("msg", "监督季度报告修改失败");
            res.put("result", "false");
        }
        return res;
    }


    /*增加字段*/
    private void addDscr(Map<String, Object> dataList, Map<String, Object> pd) {
        PageData subPd = new PageData();
        subPd.put("ENUM_TYPE_ID", "19");
        subPd.put("ENUM_ID", dataList.get("INCOME_STATE").toString());
        pd.put("INCOME_STATE_DSCR", inspectionPostSVLedgerService.getEnumInfo(subPd).get("ENUM_DSCR"));

        subPd.put("ENUM_ID", dataList.get("PAYOUT_STATE").toString());
        pd.put("PAYOUT_STATE_DSCR", inspectionPostSVLedgerService.getEnumInfo(subPd).get("ENUM_DSCR"));

        subPd.put("ENUM_ID", dataList.get("BACK_STATE").toString());
        pd.put("BACK_STATE_DSCR", inspectionPostSVLedgerService.getEnumInfo(subPd).get("ENUM_DSCR"));

        subPd.put("ENUM_ID", dataList.get("CORR_STATE").toString());
        pd.put("CORR_STATE_DSCR", inspectionPostSVLedgerService.getEnumInfo(subPd).get("ENUM_DSCR"));

        subPd.put("ENUM_ID", dataList.get("NB_STATE").toString());
        pd.put("NB_STATE_DSCR", inspectionPostSVLedgerService.getEnumInfo(subPd).get("ENUM_DSCR"));

        subPd.put("ENUM_ID", dataList.get("ACCT_STATE").toString());
        pd.put("ACCT_STATE_DSCR", inspectionPostSVLedgerService.getEnumInfo(subPd).get("ENUM_DSCR"));

        subPd.put("ENUM_ID", dataList.get("REV_ACC_STATE").toString());
        pd.put("REV_ACC_STATE_DSCR", inspectionPostSVLedgerService.getEnumInfo(subPd).get("ENUM_DSCR"));

        subPd.put("ENUM_ID", dataList.get("HANG_STATE").toString());
        pd.put("HANG_STATE_DSCR", inspectionPostSVLedgerService.getEnumInfo(subPd).get("ENUM_DSCR"));

        subPd.put("ENUM_ID", dataList.get("REMIT_STATE").toString());
        pd.put("REMIT_STATE_DSCR", inspectionPostSVLedgerService.getEnumInfo(subPd).get("ENUM_DSCR"));

        subPd.put("ENUM_ID", dataList.get("QUERY_STATE").toString());
        pd.put("QUERY_STATE_DSCR", inspectionPostSVLedgerService.getEnumInfo(subPd).get("ENUM_DSCR"));

        subPd.put("ENUM_ID", dataList.get("HAND_VOU_STATE").toString());
        pd.put("HAND_VOU_STATE_DSCR", inspectionPostSVLedgerService.getEnumInfo(subPd).get("ENUM_DSCR"));

        subPd.put("ENUM_ID", dataList.get("CLEAR_OTHERS").toString());
        pd.put("CLEAR_OTHERS_DSCR", inspectionPostSVLedgerService.getEnumInfo(subPd).get("ENUM_DSCR"));

        subPd.put("ENUM_ID", dataList.get("VOU_USE_STATE").toString());
        pd.put("VOU_USE_STATE_DSCR", inspectionPostSVLedgerService.getEnumInfo(subPd).get("ENUM_DSCR"));

        subPd.put("ENUM_ID", dataList.get("BIZ_HO_STATE").toString());
        pd.put("BIZ_HO_STATE_DSCR", inspectionPostSVLedgerService.getEnumInfo(subPd).get("ENUM_DSCR"));

        subPd.put("ENUM_ID", dataList.get("ACCT_POS_STATE").toString());
        pd.put("ACCT_POS_STATE_DSCR", inspectionPostSVLedgerService.getEnumInfo(subPd).get("ENUM_DSCR"));

        subPd.put("ENUM_ID", dataList.get("OTHERS").toString());
        pd.put("OTHERS_DSCR", inspectionPostSVLedgerService.getEnumInfo(subPd).get("ENUM_DSCR"));
    }

} ///:~

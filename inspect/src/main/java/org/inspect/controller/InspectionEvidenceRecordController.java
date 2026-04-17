// InspectionEvidenceRecordController.java

package org.inspect.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.inspect.BaseController;
import org.inspect.service.InspectionEvidenceRecordService;
import org.inspect.service.InspectionProcessControlService;
import org.inspect.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Created by Samer on 2019/11/21.
 */
@Slf4j
@Api(tags = "执法检查取证记录")
@RestController
@RequestMapping(value = "/inspectionEvidenceRecord", produces = MediaType.APPLICATION_JSON_VALUE)
public class InspectionEvidenceRecordController extends BaseController {

    @Value("${TEMPLATE_FILE_PATH}")
    private String saveDir;

    @Autowired
    private InspectionEvidenceRecordService inspectionEvidenceRecordService;

    @Autowired
    private InspectionProcessControlService inspectionProcessControlService;

    /**
     * 新增取证记录
     *
     * @param param
     * @return
     */
    @Transactional
    @ApiOperation(value = "新增取证记录")
    @PostMapping(value = "/addEvidenceRecord")
    public Map<String, Object> addEvidenceRecord(
            @ApiParam(value = "记录编号：RECORD_ID \n" +
                    "当前任务ID：TASK_ID\n" +
                    "用户所在部门ID：DEPARTMENT\n" +
                    "记录类型（1外部，2内部）：RECORD_TYPE\n" +
                    "当前流程ID：PROC_ID。\n" +
                    "当前子流程ID：PROC_SUB_ID。\n" +
                    "当前用户ID：ADD_USERID。\n" +
                    "检查机构名称：CHECK_ORG_DSCR \n" +
                    "取证记录编号： RECORD_NUM\n" +
                    "被检查对象名称：CHECKED_ORG_DSCR\n" +
                    "检查项目名称：INSPECT_ITEM_DSCR\n" +
                    "取证日期：OBTAIN_DATE。\n" +
                    "证据来源及原物或原件保存地点：SOURCE_SITE。\n" +
                    "证据清单：dataList[{\n" +
                    "   证据编号: EVIDENCE_CODE\n" +
                    "   证据名称: EVIDENCE_DSCR\n" +
                    "   数量: NUMBER\n" +
                    "   原件或复制件: ORIGINAL_COPY\n" +
                    "}]。\n" +
                    "主查人：CHIEF_MAN。\n" +
                    "检查人员：MEMBER。\n" +
                    "证据提供人：PROVIDER。\n" +
                    "被检查对象负责人：CHECKED_LEADER。\n"
            )
            @RequestBody(required = false) JSONObject param
    ) {
        Map<String, Object> res = new HashMap<>();
        PageData pd = this.getPageData(param);
        String RECORD_ID = this.get32UUID();
        String endTime = DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS);
        String docName = "",
                targetFilePath = "",
                targetFilePath_NONAME = "",
                templateName = "",
                templetFilePath = "",
                PROVIDER = pd.getString("PROVIDER")==null?"":pd.getString("PROVIDER").trim();

        if ("1".equals(pd.getString("RECORD_TYPE"))) {
            docName = "中国人民银行（" + pd.getString("CHECK_ORG_DSCR") + "）执法检查取证记录（外部取证）_"+ PROVIDER +".docx";
            templateName = "evidenceRecord_out.docx";
        }
        if ("2".equals(pd.getString("RECORD_TYPE"))) {
            docName = "中国人民银行（" + pd.getString("CHECK_ORG_DSCR") + "）执法检查取证记录（人民银行内部取证）_"+ PROVIDER +".docx";
            templateName = "evidenceRecord_in.docx";
        }
        templetFilePath = saveDir + "template/" + templateName;
        //目标文件存放路径
        targetFilePath = saveDir + pd.getString("TASK_ID") + "/执法检查取证记录/" + docName;
        targetFilePath_NONAME = saveDir + pd.getString("TASK_ID") + "/执法检查取证记录/print/" + docName;

        pd.put("PROVIDER", PROVIDER);
        List<Map<String, Object>> recordList = inspectionEvidenceRecordService.getEvidenceRecord(pd);
        if (recordList == null || recordList.isEmpty()) {
            pd.put("RECORD_ID", RECORD_ID);
        }
        List<Map<String, String>> arr = (List<Map<String, String>>) JSONArray.parse(pd.getString("dataList"));
        pd.put("dataList", arr);
        List<String[]> tdList = new ArrayList<>();
        for (int i = 0; i < arr.size(); i++) {
            String[] dataStr = new String[4];
            dataStr[0] = arr.get(i).get("EVIDENCE_CODE");
            dataStr[1] = arr.get(i).get("EVIDENCE_DSCR");
            dataStr[2] = arr.get(i).get("NUMBER");
            dataStr[3] = arr.get(i).get("ORIGINAL_COPY");
            tdList.add(dataStr);
        }
        /*电子档检查人写入*/
        pd.put("PAPER_ATTACHMENT", targetFilePath);
        /*导出文档检查人不写入*/
        pd.put("PRINT_ATTACHMENT", targetFilePath_NONAME);
        pd.put("ATTACHMENT_NAME", docName);
        try {
            //将xml模板转换为后缀为doc文件
            if (recordList == null || recordList.isEmpty()) {
                pd.put("ADD_DATE", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
                inspectionEvidenceRecordService.addEvidenceRecord(pd);
                res.put("msg", "执法检查取证记录新增成功");
            } else {
                pd.put("MODIFY_DATE", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
                pd.put("MODIFY_USERID", pd.getString("ADD_USERID"));
                inspectionEvidenceRecordService.editEvidenceRecord(pd);
                res.put("msg", "执法检查取证记录修改成功");
            }
            /*本流程完成状态更新，后续流程激活*/
            pd.put("FINISH_TIME", endTime);
            inspectionProcessControlService.finishCurSubProcessById(pd);

            XWPFDocument template = new XWPFDocument(new FileInputStream(templetFilePath));
            BokeWordUtils.changeText(template, pd);
            if( tdList.size() > 0 ){
                BokeWordUtils.changeTable(template, null, tdList, 1);
                BokeWordUtils.mergeCellVertically(template.getTables().get(0), 0, 0, tdList.size());
            }
            FileUpload.writeToFile(template, targetFilePath);

            XWPFDocument template_2 = new XWPFDocument(new FileInputStream(templetFilePath));
            pd.put("CHIEF_MAN", "");
            pd.put("PROVIDER", "");
            pd.put("MEMBER", "");
            pd.put("CHECKED_LEADER", "");
            BokeWordUtils.changeText(template_2, pd);
            if( tdList.size() > 0 ){
                BokeWordUtils.changeTable(template_2, null, tdList, 1);
                BokeWordUtils.mergeCellVertically(template_2.getTables().get(0), 0, 0, tdList.size());
            }
            FileUpload.writeToFile(template_2, targetFilePath_NONAME);

            res.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            if (recordList == null || recordList.isEmpty()) {
                res.put("msg", "执法检查取证记录新增失败,请完善信息");
            } else {
                res.put("msg", "执法检查取证记录修改失败,请完善信息");
            }
            res.put("result", "false");
        }
        return res;
    }

    /**
     * 获取取证记录
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "获取取证记录")
    @PostMapping(value = "/getEvidenceRecord")
    public Map<String, Object> getEvidenceRecord(
            @ApiParam(value = "记录编号：RECORD_ID \n" +
                    "当前任务ID：TASK_ID\n" +
                    "用户所在部门ID：DEPARTMENT\n" +
                    "记录类型（1外部，2内部）：RECORD_TYPE")
            @RequestBody(required = false) JSONObject param
    ) {
        Map<String, Object> res = new HashMap<>();
        PageData pd = this.getPageData(param);
        List<Map<String, Object>> dataList = inspectionEvidenceRecordService.getEvidenceRecordMainInfo(pd);
        if( dataList != null && !dataList.isEmpty() ){
            for( int i = 0; i <  dataList.size(); i ++ ){
                pd.put("RECORD_ID", dataList.get(i).get("RECORD_ID"));
                dataList.get(i).put("dataList", inspectionEvidenceRecordService.getEvidenceRecordSheet(pd));
            }
        }
        res.put("result", "success");
        res.put("rows", dataList);
        return res;
    }


    /**
     * 获取取证记录主表信息
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "获取取证记录主表信息")
    @PostMapping(value = "/getEvidenceRecordMainInfo")
    public Map<String, Object> getEvidenceRecordMainInfo(
            @ApiParam(value = "记录编号：RECORD_ID \n" +
                    "当前任务ID：TASK_ID\n" +
                    "用户所在部门ID：DEPARTMENT\n" +
                    "记录类型（1外部，2内部）：RECORD_TYPE")
            @RequestBody(required = false) JSONObject param
    ) {
        Map<String, Object> res = new HashMap<>();
        PageData pd = this.getPageData(param);
        List<Map<String, Object>> dataList = inspectionEvidenceRecordService.getEvidenceRecordMainInfo(pd);
        res.put("result", "success");
        res.put("rows", dataList);
        return res;
    }

    /**
     * 删除取证记录
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "删除取证记录")
    @PostMapping(value = "/deleteEvidenceRecord")
    public Map<String, Object> deleteEvidenceRecord(
            @ApiParam(value = "记录编号：RECORD_ID \n" )
            @RequestBody(required = false) JSONObject param
    ) {
        Map<String, Object> res = new HashMap<>();
        PageData pd = this.getPageData(param);
        inspectionEvidenceRecordService.deleteEvidenceRecord(pd);
        res.put("result", "success");
        res.put("msg", "删除成功");
        return res;
    }

    /**
     * 执法检查取证记录导出
     *
     * @param param
     * @param response
     */
    @ApiOperation(value = "执法检查取证记录导出")
    @GetMapping(value = "/downLoadEvidenceRecord")
    public void downLoadEvidenceRecord(
            @ApiParam(value = "当前记录ID：RECORD_ID")
            @RequestBody(required = false) JSONObject param,
            HttpServletResponse response
    ) throws Exception {
        PageData pd = this.getPageData(param);
        List<Map<String, Object>> dataList = inspectionEvidenceRecordService.getEvidenceRecord(pd);
        String filePath = dataList.get(0).get("PRINT_ATTACHMENT").toString();
        String fileName = dataList.get(0).get("ATTACHMENT_NAME").toString();
        FileDownload.fileDownload(response, filePath, fileName, this.getRequest());
    }


} ///:~

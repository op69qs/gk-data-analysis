// InspectionEvidenceNotificationController.java

package org.inspect.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.inspect.BaseController;
import org.inspect.service.InspectionEvidenceNotificationService;
import org.inspect.service.InspectionProcessControlService;
import org.inspect.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
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
@Api(tags = "执法检查证据登记保存通知书")
@RestController
@RequestMapping(value = "/inspectionEvidenceNotification", produces = MediaType.APPLICATION_JSON_VALUE)
public class InspectionEvidenceNotificationController extends BaseController {

    @Value("${TEMPLATE_FILE_PATH}")
    private String saveDir;

    @Autowired
    private InspectionEvidenceNotificationService inspectionEvidenceNotificationService;

    @Autowired
    private InspectionProcessControlService inspectionProcessControlService;

    /**
     * 新增取证记录
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "新增取证记录")
    @PostMapping(value = "/addEvidenceNotification")
    public Map<String, Object> addEvidenceNotification(
            @ApiParam(value = "记录编号：RECORD_ID \n" +
                    "当前任务ID：TASK_ID\n" +
                    "用户所在部门ID：DEPARTMENT\n" +
                    "当前流程ID：PROC_ID。\n" +
                    "当前子流程ID：PROC_SUB_ID。\n" +
                    "当前用户ID：ADD_USERID。\n" +
                    "被查机构名称：CHECKED_ORG_DSCR \n" +
                    "检查机构名称： CHECK_ORG_DSCR\n" +
                    "保存开始时间：SAVE_START_DATE\n" +
                    "保存结束时间：SAVE_END_DATE\n" +
                    "检查组组长：GROUP_LEADER。\n" +
                    "主查人：CHIEF_MAN。\n" +
                    "登记保存材料收回日期：REGAIN_DATE。\n" +
                    "收回人：REGAIN_NAME。\n" +
                    "页脚通知时间：NOTIFICATION_DATE。\n" +
                    "项目清单：dataList[{\n" +
                    "   项目序号: ITEM_NO\n" +
                    "   登记保存证据材料名称: ITEM_DSCR\n" +
                    "   件数: ITEM_COUNT\n" +
                    "   备注: REMARKS\n" +
                    "}]。\n"
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
                templetFilePath = "";

        docName = "中国人民银行（" + pd.getString("CHECK_ORG_DSCR") + "）执法检查证据登记保存通知书.docx";
        templateName = "evidenceNotification.docx";
        templetFilePath = saveDir + "template/" + templateName;
        //目标文件存放路径
        targetFilePath = saveDir + pd.getString("TASK_ID") + "/执法检查证据登记保存通知书/" + docName;
        targetFilePath_NONAME = saveDir + pd.getString("TASK_ID") + "/执法检查证据登记保存通知书/print/" + docName;

        List<Map<String, Object>> NotificationList = inspectionEvidenceNotificationService.getRecord(pd);
        if (NotificationList == null || NotificationList.isEmpty()) {
            pd.put("RECORD_ID", RECORD_ID);
        }
        List<Map<String, String>> arr = (List<Map<String, String>>) JSONArray.parse(pd.getString("dataList"));
        pd.put("dataList", arr);
        List<String[]> tdList = new ArrayList<>();
        for (int i = 0; i < arr.size(); i++) {
            String[] dataStr = new String[4];
            dataStr[0] = arr.get(i).get("ITEM_NO");
            dataStr[1] = arr.get(i).get("ITEM_DSCR");
            dataStr[2] = arr.get(i).get("ITEM_COUNT");
            dataStr[3] = arr.get(i).get("REMARKS");
            tdList.add(dataStr);
        }
        /*电子档检查人写入*/
        pd.put("PAPER_ATTACHMENT", targetFilePath);
        /*导出文档检查人不写入*/
        pd.put("PRINT_ATTACHMENT", targetFilePath_NONAME);
        pd.put("ATTACHMENT_NAME", docName);
        try {
            //将xml模板转换为后缀为doc文件
            if (NotificationList == null || NotificationList.isEmpty()) {
                pd.put("ADD_DATE", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
                inspectionEvidenceNotificationService.addRecord(pd);
                res.put("msg", "执法检查证据登记保存通知书新增成功");
            } else {
                pd.put("MODIFY_DATE", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
                pd.put("MODIFY_USERID", pd.getString("ADD_USERID"));
                inspectionEvidenceNotificationService.editRecord(pd);
                res.put("msg", "执法检查证据登记保存通知书修改成功");
            }
            /*本流程完成状态更新，后续流程激活*/
            pd.put("FINISH_TIME", endTime);
            inspectionProcessControlService.finishCurSubProcessById(pd);

            XWPFDocument template = new XWPFDocument(new FileInputStream(templetFilePath));
            BokeWordUtils.changeText(template, pd);
            if( tdList.size() > 0 ) {
                BokeWordUtils.changeTable(template, null, tdList, 0);
            }
            FileUpload.writeToFile(template, targetFilePath);

            XWPFDocument template_2 = new XWPFDocument(new FileInputStream(templetFilePath));
            pd.put("GROUP_LEADER", "");
            pd.put("CHIEF_MAN", "");
            pd.put("REGAIN_DATE", "  年  月  日");
            pd.put("REGAIN_NAME", "");
            pd.put("NOTIFICATION_DATE", "  年  月  日");
            BokeWordUtils.changeText(template_2, pd);
            if( tdList.size() > 0 ) {
                BokeWordUtils.changeTable(template_2, null, tdList, 0);
            }
            FileUpload.writeToFile(template_2, targetFilePath_NONAME);

            res.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            if (NotificationList == null || NotificationList.isEmpty()) {
                res.put("msg", "执法检查证据登记保存通知书新增失败");
            } else {
                res.put("msg", "执法检查证据登记保存通知书修改失败");
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
    @PostMapping(value = "/getEvidenceNotification")
    public Map<String, Object> getEvidenceNotification(
            @ApiParam(value = "记录编号：RECORD_ID \n" +
                    "当前任务ID：TASK_ID\n" +
                    "用户所在部门ID：DEPARTMENT\n" )
            @RequestBody(required = false) JSONObject param
    ) {
        Map<String, Object> res = new HashMap<>();
        PageData pd = this.getPageData(param);
        List<Map<String, Object>> dataList = inspectionEvidenceNotificationService.getRecord(pd);
        if( dataList !=null && dataList.size() >0 ){
            for( int i =0;i<dataList.size();i++ ){
                List<Map<String, Object>> subDataList = inspectionEvidenceNotificationService.getRecordSub(pd);
                dataList.get(i).put("dataList", subDataList);
            }
        }
        res.put("result", "success");
        res.put("rows", dataList);
        return res;
    }

    /**
     * 执法检查证据登记保存通知书导出
     *
     * @param param
     * @param response
     */
    @ApiOperation(value = "执法检查证据登记保存通知书导出")
    @GetMapping(value = "/downLoadEvidenceNotification")
    public void downLoadEvidenceNotification(
            @ApiParam(value = "当前记录ID：RECORD_ID")
            @RequestBody(required = false) JSONObject param,
            HttpServletResponse response
    ) throws Exception {
        PageData pd = this.getPageData(param);
        List<Map<String, Object>> dataList = inspectionEvidenceNotificationService.getRecord(pd);
        String filePath = dataList.get(0).get("PRINT_ATTACHMENT").toString();
        String fileName = dataList.get(0).get("ATTACHMENT_NAME").toString();
        FileDownload.fileDownload(response, filePath, fileName, this.getRequest());
    }


} ///:~

// InspectionNotificationPushController.java

package org.inspect.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.inspect.BaseController;
import org.inspect.service.*;
import org.inspect.util.DateUtil;
import org.inspect.util.FileUpload;
import org.inspect.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 检查任务通知推送
 *
 * @author Created by Samer on 2019/11/11.
 */
@Slf4j
@RestController
@Api(tags = "检查任务通知推送")
@RequestMapping(value = "inspectionNotificationPush", produces = MediaType.APPLICATION_JSON_VALUE)
public class InspectionNotificationPushController extends BaseController {

    @Value("${TEMPLATE_FILE_PATH}")
    private String saveDir;

    @Autowired
    private InspectionFileService inspectionFileService;

    @Autowired
    private InspectionTaskService inspectionTaskService;

    @Autowired
    private InspectionProcSubService inspectionProcSubService;

    @Autowired
    private InspectionNotificationPushService inspectionNotificationPushService;

    @Autowired
    private InspectionProcessControlService inspectionProcessControlService;

    /**
     * 检查任务通知推送
     *
     * @param param
     * @return
     */
    @Transactional
    @ApiOperation(value = "检查任务通知推送")
    @PostMapping(value = "/pushInspectionNotification")
    public Map<String, Object> pushInspectionNotification(
            @ApiParam(value = "当前任务ID：TASK_ID\n" +
                    "当前流程UUID：PROC_ID\n" +
                    "当前用户国库编码：REL_ORG\n" +
                    "当前用户编码：REL_PEO\n" +
                    "当前流程导出文件名：FILE_NAME\n" +
                    "(检查通知书：notice.docx\n" +
                    "公务接待函: reception.docx\n" +
                    "借阅清单:borrowListWithName.xls)")
            @RequestBody(required = false) JSONObject param
    ) {
        Map<String, Object> result = new HashMap<>();
        String dateNow = DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS);
        PageData pd = this.getPageData(param);
        PageData subPd = new PageData();
        PageData subPd_2 = new PageData();
        String taskId = pd.getString("TASK_ID");
        String procId = pd.getString("PROC_ID");
        String fileName = pd.getString("FILE_NAME");
        String path = saveDir + taskId + "/" + procId + "/" + fileName;
        subPd.put("INSPECTION_TASK_ID", taskId);
        List<Map<String, Object>> taskDataList = inspectionTaskService.getInspectionTaskData(subPd);
        subPd.put("ID", procId);
        List<Map<String, Object>> subProcList = inspectionProcSubService.getInspectionProcSubData(subPd);
        String taskName = taskDataList.get(0).get("INSPECTION_TASK_NAME").toString();
        String taskEndDate = taskDataList.get(0).get("INSPECTION_TASK_ENDTIME").toString();
        String receiveOrgCode = taskDataList.get(0).get("INSPECTED_GUOKU_ID").toString();
        subPd.put("receiveOrgCode", receiveOrgCode);
        String receiveOrgDscr = inspectionNotificationPushService.getReceiveTreDscr(subPd);
        String procSubName = subProcList.get(0).get("INSPECTION_PROCESS_SUB_NAME").toString();
        String fileDscr = "";
        String atteName = "";
        switch (fileName) {
            case ("notice.docx"):
                fileDscr = "检查通知书";
                atteName = "检查通知书.docx";
                break;
            case ("reception.docx"):
                fileDscr = "公务接待函";
                atteName = "公务接待函.docx";
                break;
            case ("borrowListWithName.xls"):
                fileDscr = "借阅清单";
                atteName = "借阅清单.xls";
                break;
            default:
                fileDscr = "";
                atteName = "";
        }
        String INFO_TITLE = taskName + "_" + procSubName;

        String infoId = this.get32UUID();
        /*推送消息信息*/
        pd.put("INFO_ID", infoId);
        pd.put("INFO_TITLE", INFO_TITLE);
        pd.put("INFO_TYPE_ID", "1");
        pd.put("INFO_TYPE", "通知");
        pd.put("ADD_DATE", dateNow);
        pd.put("START_DATE", dateNow);
        pd.put("END_DATE", taskEndDate);
        pd.put("INFO_DETAIL", "<p>详细信息，请下载查看附件文件。<br></p>");
        pd.put("INFO_STATE", "1");
        pd.put("TODO_STATE", "1");
        pd.put("REL_ORG", pd.getString("REL_ORG"));
        pd.put("REL_PEO", pd.getString("REL_PEO"));
        pd.put("CLICK_RATE", null);
        pd.put("DEPARTMENT", null);
        /*附件信息*/
        subPd.put("ATTA_ID", this.get32UUID());
        subPd.put("S_INFO_ID", infoId);
        subPd.put("ADD_DATE", dateNow);
        subPd.put("PATH", path);
        subPd.put("FILE_NAME", atteName);
        /*接受机构信息*/
        subPd_2.put("ORG_ID", receiveOrgCode);
        subPd_2.put("ORG_DSCR", receiveOrgDscr);
        subPd_2.put("INFO_ID", infoId);
        subPd_2.put("ADD_DATE", dateNow);
        subPd_2.put("TODO_STATE", "0");
        subPd_2.put("DEPARTMENT", null);
        try {
            inspectionNotificationPushService.addInfoPub(pd);
            inspectionNotificationPushService.addInfoAtta(subPd);
            inspectionNotificationPushService.addReceiveOrg(subPd_2);
            result.put("msg", "通知发布成功。");
            result.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", "通知发布失败");
            result.put("result", "failed");
        }
        return result;
    }


    /**
     * 自查计划/任务通知文件推送
     *
     * @return
     */
    @Transactional
    @ApiOperation(value = "检查任务通知推送")
    @PostMapping(value = "/pushSelfNotificationAttachment")
    public Map<String, Object> pushSelfNotificationAttachment(
            @ApiParam(value = "当前任务ID：TASK_ID\n" +
                    "当前计划ID：PLAN_ID\n" +
                    "标题：INFO_TITLE\n" +
                    "内容：INFO_DETAIL\n" +
                    "当前用户国库编码：REL_ORG\n" +
                    "当前用户编码：REL_PEO\n" +
                    "上传文件名：file")
                    HttpServletRequest request,
            @RequestParam(value = "TASK_ID", required = false)String TASK_ID,
            @RequestParam(value = "PLAN_ID", required = false)String PLAN_ID,
            @RequestParam(value = "INFO_TITLE", required = false)String INFO_TITLE,
            @RequestParam(value = "INFO_DETAIL", required = false)String INFO_DETAIL,
            @RequestParam(value = "REL_ORG", required = false)String REL_ORG,
            @RequestParam(value = "REL_PEO", required = false)String REL_PEO
    ) {
        List<MultipartFile> mtFiles = ((MultipartHttpServletRequest) request).getFiles("file[]");
        Map<String, Object> result = new HashMap<>();
        String dateNow = DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS);
        PageData pd = this.getPageData();
        PageData subPd = new PageData();
        PageData subPd_2 = new PageData();
        String planId = pd.getString("PLAN_ID");
        String taskId = pd.getString("TASK_ID");
        subPd.put("INSPECTION_TASK_ID", taskId);
        subPd.put("INSPECTION_PLAN_ID", planId);
        List<Map<String, Object>> taskDataList = inspectionTaskService.getInspectionTaskData(subPd);
        try {
            for (Map<String, Object> taskData : taskDataList) {
                String taskEndDate = taskData.get("INSPECTION_TASK_ENDTIME").toString();
                String receiveOrgCode = taskData.get("INSPECTED_GUOKU_ID").toString();

                pd.put("TASK_ID",  taskData.get("INSPECTION_TASK_ID").toString());

                subPd.put("receiveOrgCode", receiveOrgCode);
                String receiveOrgDscr = inspectionNotificationPushService.getReceiveTreDscr(subPd);

                String infoId = this.get32UUID();
                /*推送消息信息*/

                pd.put("INFO_ID", infoId);
                pd.put("INFO_TYPE_ID", "1");
                pd.put("INFO_TYPE", "通知");
                pd.put("ADD_DATE", dateNow);
                pd.put("START_DATE", dateNow);
                pd.put("END_DATE", taskEndDate);
                pd.put("INFO_STATE", "1");
                pd.put("TODO_STATE", "1");
                pd.put("CLICK_RATE", null);
                pd.put("DEPARTMENT", null);

                /*接受机构信息*/
                subPd_2.put("ORG_ID", receiveOrgCode);
                subPd_2.put("ORG_DSCR", receiveOrgDscr);
                subPd_2.put("INFO_ID", infoId);
                subPd_2.put("ADD_DATE", dateNow);
                subPd_2.put("TODO_STATE", "0");
                subPd_2.put("DEPARTMENT", null);
                inspectionNotificationPushService.addInfoPub(pd);
                inspectionNotificationPushService.addReceiveOrg(subPd_2);
                for(MultipartFile mtFile:mtFiles ) {
                    String fileName = mtFile.getOriginalFilename();
                    String fileDir = saveDir + taskId + "/";
                    String path = saveDir + taskId + "/" + fileName;
                /*附件信息*/
                    subPd.put("ATTA_ID", this.get32UUID());
                    subPd.put("S_INFO_ID", infoId);
                    subPd.put("ADD_DATE", dateNow);
                    subPd.put("PATH", path);
                    subPd.put("FILE_NAME", fileName);

                /*检查附件表更新*/
                    pd.put("INSPECTION_FILE_ID", this.get32UUID());
                    pd.put("INSPECTION_PROCESS_SUB_ID", "0080102");
                    pd.put("INSPECTION_FILE_NAME", fileName);
                    pd.put("INSPECTION_FILE_PATH", path);
                    pd.put("UPLOAD_TIME", dateNow);
                    pd.put("UPLOAD_USER", pd.getString("REL_PEO"));
                    inspectionFileService.addInspectionFile(pd);
                    inspectionNotificationPushService.addInfoAtta(subPd);
                    FileUpload.fileUp(mtFile, fileDir, fileName);
                }
                 /*完成当前流程，尝试激活后续流程。*/
                pd.put("FINISH_TIME", dateNow);
                pd.put("PROC_ID", "00801");
                pd.put("PROC_SUB_ID", "0080101");
                inspectionProcessControlService.finishCurSubProcessById(pd);
                pd.put("PROC_SUB_ID", "0080102");
                inspectionProcessControlService.finishCurSubProcessById(pd);
                if (inspectionProcessControlService.activateFollowProc(pd)) {
                    /*result.put("msg", result.get("msg") + inspectionProcessControlService.getProcDescById(pd) + "流程结束，后续流程已激活");*/
                }
            }
            result.put("msg", "通知发布成功。");
            result.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", "通知发布失败");
            result.put("result", "failed");
        }
        return result;
    }


} ///:~

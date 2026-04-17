// InspectionPostSVListController.java

package org.inspect.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.inspect.BaseController;
import org.inspect.service.InspectionPostSVListService;
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
 * 事后监督交接清单
 * @author Created by Samer on 2019/11/21.
 */
@Slf4j
@Api(tags = "事后监督交接清单")
@RestController
@RequestMapping(value = "/inspectionPostSVList", produces = MediaType.APPLICATION_JSON_VALUE)
public class InspectionPostSVListController extends BaseController {

    @Value("${TEMPLATE_FILE_PATH}")
    private String saveDir;

    @Autowired
    private InspectionPostSVListService inspectionPostSVListService;

    @Autowired
    private InspectionProcessControlService inspectionProcessControlService;

    /**
     * 新增资料交接清单
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "新增资料交接清单")
    @PostMapping(value = "/addHandOverList")
    public Map<String, Object> addHandOverList(
            @ApiParam(value = "清单主键ID：LIST_ID \n" +
                    "当前任务ID：TASK_ID\n" +
                    "当前流程ID：PROC_ID。\n" +
                    "当前子流程ID：PROC_SUB_ID。\n" +
                    "当前用户ID：ADD_USERID。\n" +
                    "标题：TITLE。\n" +
                    "机构名称： BOOKORG_DSCR\n" +
                    "交接时间：FILLING_DATE\n" +
                    "移交人：HAND_OVER\n" +
                    "接收人：ACCEPTER。\n" +
                    "证据清单：dataList[{\n" +
                    "   名    称: ITEM_DSCR\n" +
                    "   数    量: ITEM_NUM\n" +
                    "   资料日期: ITEM_DATE\n" +
                    "   备    注: REMARKS\n" +
                    "}]。\n"
            )
            @RequestBody(required = false) JSONObject param
    ) {
        Map<String, Object> res = new HashMap<>();
        PageData pd = this.getPageData(param);
        String LIST_ID = this.get32UUID();
        String endTime = DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS);
        String docName = "",
                targetFilePath = "",
                targetFilePath_NONAME = "",
                templateName = "",
                templetFilePath = "";

        docName = pd.getString("TITLE") + ".docx";
        templateName = "HandOverList.docx";
        templetFilePath = saveDir + "template/" + templateName;
        //目标文件存放路径
        targetFilePath = saveDir + pd.getString("TASK_ID") + "/事后监督交接清单/" + docName;
        targetFilePath_NONAME = saveDir + pd.getString("TASK_ID") + "/事后监督交接清单/print/" + docName;

        List<Map<String, Object>> dataList = inspectionPostSVListService.getMainInfo(pd);
        if (dataList == null || dataList.isEmpty()) {
            pd.put("LIST_ID", LIST_ID);
        }
        List<Map<String, String>> arr = (List<Map<String, String>>) JSONArray.parse(pd.getString("dataList"));
        pd.put("dataList", arr);
        List<String[]> tdList = new ArrayList<>();
        for (int i = 0; i < arr.size(); i++) {
            String[] dataStr = new String[4];
            dataStr[0] = arr.get(i).get("ITEM_DSCR");
            dataStr[1] = arr.get(i).get("ITEM_NUM");
            dataStr[2] = arr.get(i).get("ITEM_DATE")==null?"":arr.get(i).get("ITEM_DATE");
            dataStr[3] = arr.get(i).get("REMARKS")==null?"":arr.get(i).get("REMARKS");
            tdList.add(dataStr);
        }
        /*电子档检查人写入*/
        pd.put("PAPER_ATTACHMENT", targetFilePath);
        /*导出文档检查人不写入*/
        pd.put("PRINT_ATTACHMENT", targetFilePath_NONAME);
        pd.put("ATTACHMENT_NAME", docName);
        try {
            //将xml模板转换为后缀为doc文件
            if (dataList == null || dataList.isEmpty()) {
                pd.put("ADD_DATE", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
                inspectionPostSVListService.addMainInfo(pd);
                res.put("msg", "事后监督交接清单新增成功");
            } else {
                pd.put("MODIFY_DATE", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
                pd.put("MODIFY_USERID", pd.getString("ADD_USERID"));
                inspectionPostSVListService.editMainInfo(pd);
                res.put("msg", "事后监督交接清单修改成功");
            }
            /*本流程完成状态更新*/
            pd.put("FINISH_TIME", endTime);
            inspectionProcessControlService.finishPostSVCurSubProcessById(pd);

            XWPFDocument template = new XWPFDocument(new FileInputStream(templetFilePath));
            BokeWordUtils.changeText(template, pd);
            BokeWordUtils.changeTable(template, null, tdList, 0);
            FileUpload.writeToFile(template, targetFilePath);

            XWPFDocument template_2 = new XWPFDocument(new FileInputStream(templetFilePath));
            pd.put("FILLING_DATE", "  年  月  日  时");
            pd.put("HAND_OVER", "");
            pd.put("ACCEPTER", "");
            BokeWordUtils.changeText(template_2, pd);
            BokeWordUtils.changeTable(template_2, null, tdList, 0);
            FileUpload.writeToFile(template_2, targetFilePath_NONAME);

            res.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            if (dataList == null || dataList.isEmpty()) {
                res.put("msg", "事后监督交接清单新增失败");
            } else {
                res.put("msg", "事后监督交接清单修改失败");
            }
            res.put("result", "false");
        }
        return res;
    }

    /**
     * 获取资料交接清单
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "获取资料交接清单")
    @PostMapping(value = "/getFullInfo")
    public Map<String, Object> getFullInfo(
            @ApiParam(value = "记录编号：LIST_ID \n" +
                    "当前任务ID：TASK_ID\n")
            @RequestBody(required = false) JSONObject param
    ) {
        String dateNow = DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_CN);
        List<Map<String, Object>> subDataList;
        Map<String, Object> dataObj = new HashMap<>();
        Map<String, Object> res = new HashMap<>();
        PageData pd = this.getPageData(param);
        Map<String, String> taskInfo = inspectionPostSVListService.getTaskInfoByTaskId(pd);

        List<Map<String, Object>> dataList = inspectionPostSVListService.getMainInfo(pd);
        if (dataList != null && !dataList.isEmpty()) {
            pd.put("LIST_ID", dataList.get(0).get("LIST_ID").toString());
            subDataList = inspectionPostSVListService.getSubSheet(pd);
            if (subDataList == null || subDataList.isEmpty()) {
                subDataList = inspectionPostSVListService.getTemplateInfo(pd);
            }
        } else {
            dataList = new ArrayList<>();
            Map<String, Object> content = new HashMap<>();
            content.put("BOOKORG_DSCR", taskInfo.get("BOOKORG_DSCR"));
            content.put("FILLING_DATE", dateNow);
            content.put("HAND_OVER", "");
            content.put("ACCEPTER", "");
            dataList.add(content);
            subDataList = inspectionPostSVListService.getTemplateInfo(pd);
        }
        dataObj.put("dataList", dataList);
        dataObj.put("subDataList", subDataList);
        
        res.put("result", "success");
        res.put("rows", dataObj);
        return res;
    }


    /**
     * 获取资料交接清单主表信息
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "获取资料交接清单主表信息")
    @PostMapping(value = "/getMainInfo")
    public Map<String, Object> getMainInfo(
            @ApiParam(value = "记录编号：LIST_ID \n" +
                    "当前任务ID：TASK_ID\n" )
            @RequestBody(required = false) JSONObject param
    ) {
        Map<String, Object> res = new HashMap<>();
        PageData pd = this.getPageData(param);
        List<Map<String, Object>> dataList = inspectionPostSVListService.getMainInfo(pd);
        res.put("result", "success");
        res.put("rows", dataList);
        return res;
    }

    /**
     * 删除资料交接清单
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "删除资料交接清单")
    @PostMapping(value = "/deleteMainInfo")
    public Map<String, Object> deleteMainInfo(
            @ApiParam(value = "记录编号：LIST_ID \n")
            @RequestBody(required = false) JSONObject param
    ) {
        Map<String, Object> res = new HashMap<>();
        PageData pd = this.getPageData(param);
        inspectionPostSVListService.deleteMainInfo(pd);
        res.put("result", "success");
        res.put("msg", "删除成功");
        return res;
    }

    /**
     * 执法检查资料交接清单导出
     *
     * @param param
     * @param response
     */
    @ApiOperation(value = "执法检查资料交接清单导出")
    @GetMapping(value = "/downLoadHandOverList")
    public void downLoadHandOverList(
            @ApiParam(value = "当前记录ID：LIST_ID")
            @RequestBody(required = false) JSONObject param,
            HttpServletResponse response
    ) throws Exception {
        PageData pd = this.getPageData(param);
        List<Map<String, Object>> dataList = inspectionPostSVListService.getMainInfo(pd);
        String filePath = dataList.get(0).get("PRINT_ATTACHMENT").toString();
        String fileName = dataList.get(0).get("ATTACHMENT_NAME").toString();
        FileDownload.fileDownload(response, filePath, fileName, this.getRequest());
    }


} ///:~

// inspectionTemporarySum.java

package org.inspect.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.inspect.BaseController;
import org.inspect.service.InspectionTemporarySumService;
import org.inspect.util.CreateExcel_2;
import org.inspect.util.FileDownload;
import org.inspect.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Created by Samer on 2019/10/17.
 */
@Slf4j
@RestController
@Api(tags = "问题台账控制类")
@RequestMapping(value = "/inspectionTemporarySum", produces = MediaType.APPLICATION_JSON_VALUE)
public class InspectionTemporarySumController extends BaseController {

    @Value("${TEMPLATE_FILE_PATH}")
    private String saveDir;

    @Autowired
    private InspectionTemporarySumService inspectionTemporarySumService;


    /**
     * 根据任务ID获取台账及整改信息
     *
     * @param param params.TASK_ID 当前检查任务ID
     * @return res
     */
    @ApiOperation(value = "根据任务ID获取台账及整改信息")
    @PostMapping(value = "/getLedgerReformInfoByTaskId")
    public Map<String, Object> getLedgerReformInfoByTaskId(
            @ApiParam(value = "TASK_ID：当前检查任务ID")
            @RequestBody(required = false) JSONObject param
    ) {
        PageData pd = this.getPageData(param);
        List<Map<String, String>> dataList = inspectionTemporarySumService.getLedgerReformInfoByTaskId(pd);
        Map<String, Object> res = new HashMap<>();
        res.put("result", "success");
        res.put("rows", dataList);
        return res;
    }

    /**
     * 下载汇总表
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "下载汇总表")
    @GetMapping(value = "/downLoadTemporarySumTable")
    public void downLoadTemporarySumTable(
            @ApiParam(value = "当前任务ID：TASK_ID")
            @RequestBody(required = false) JSONObject param,
            HttpServletResponse response
    ) throws Exception {
        PageData pd = this.getPageData(param);
        pd.put("TEMP_PROC_SUB_ID", "0090402");
        Map<String, String> subProcInfo = inspectionTemporarySumService.getProcSubTitle(pd);
        List<Map<String, Object>> dataList = inspectionTemporarySumService.getLedgerReformInfoByTaskId(pd);
        String title = subProcInfo.get("TITLE");
        String fileName = title + ".xls";
        String filepath = saveDir + pd.getString("TASK_ID") + "/";
        String[] clns = {
                "一级问题分类",
                "二级问题分类",
                "问题描述",
                "制度依据",
                "整改方式",
                "整改方案",
                "整改说明"
        };
        String[] queryCols = {
                "QUESTION_DSCR_1",
                "QUESTION_DSCR_2",
                "QUESTION_CONTENT",
                "RULES",
                "QUESTION_OPINIONS",
                "REFORM_SCHEME",
                "MEMO"
        };
        CreateExcel_2.createTemproraySumTable(title, filepath, fileName, clns, queryCols, dataList);
        FileDownload.fileDownload(response, filepath + fileName, fileName, this.getRequest());
    }

} ///:~

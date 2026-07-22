package org.indicatorsLib.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.indicatorsLib.BaseController;
import org.indicatorsLib.service.IndexRelationService;
import org.indicatorsLib.service.IndexSchemeService;
import org.indicatorsLib.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@Api(tags = "指标方案")
@RequestMapping(value = "indexSchemeController", produces = MediaType.APPLICATION_JSON_VALUE)
public class IndexSchemeController extends BaseController {

    @Value("${TEMPLATE_FILE_PATH}")
    private String saveDir;

    @Autowired
    private IndexSchemeService indexSchemeService;

    @Autowired
    private IndexRelationService indexRelationService;

    @Autowired
    private CreateSchemeSQL createSchemeSQL;


    /**
     * 推送指标库到图库
     * @return
     */
    @ApiOperation(value = "推送指标库到图库")
    @PostMapping(value = "/pushIndexToVS")
    public Map<String, Object> pushIndexToVS(@RequestBody JSONObject jsonObject){
        Map<String, Object> result = new HashMap<>();
        PageData pageData = this.getPageData(jsonObject);
        PageData pd_2 = new PageData();
        indexSchemeService.pushIndexToVS(pageData);

        // 修改推送状态
        pd_2.put("IS_PUSH", "0");
        pd_2.put("schemeId", pageData.getString("ID"));
        indexSchemeService.updateSchemeData(pd_2);

        result.put("result", "success");
        result.put("msg", "推送指标成功");

        return result;
    }



    /**
     * 保存指标方案
     *
     * @param jsonObject
     */
    @ApiOperation(value = "保存指标方案")
    @PostMapping(value = "/saveIndexScheme")
    public Map<String, Object> saveIndexScheme(@RequestBody JSONObject jsonObject) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pageData = this.getPageData(jsonObject);
            //保存方案之前先做【方案描述】字段校验
            if (indexSchemeService.validatySchemeDescr(pageData) > 0) {
                result.put("result", "false");
                result.put("msg", "方案描述重复");
            } else {
                String schemeSql = createSchemeSQL.getSchemeSQL(pageData);
                if (StringUtils.isBlank(schemeSql)) {
                    result.put("result", "false");
                    result.put("msg", "指标组装方案失败");
                    return result;
                }
                pageData.put("schemeSql", schemeSql); //自定义指标方案执行SQL
                if (oConvertUtils.isEmpty(pageData.get("schemeId"))) {
                    pageData.put("schemeId", get32UUID());
                    pageData.put("isPublicScheme", "1");
                    pageData.put("date", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
                    pageData.put("schemeColums", pageData.get("columns").toString());
                    pageData.put("schemeConditon", pageData.get("mainCondition").toString());
                    indexSchemeService.saveIndexScheme(pageData);
                } else {
                    pageData.put("schemeColums", pageData.get("columns").toString());
                    pageData.put("schemeConditon", pageData.get("mainCondition").toString());
                    pageData.put("date", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
                    indexSchemeService.updateSchemeData(pageData);
                }
                result.put("result", "success");
                result.put("msg", "保存指标方案成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", "false");
            result.put("msg", "保存指标方案失败");
            log.error(e.getMessage(), result);
        }
        return result;
    }

    /**
     * 指标方案重命名
     *
     * @param jsonObject
     */
    @ApiOperation(value = "指标方案重命名")
    @PostMapping(value = "/schemeRename")
    public Map<String, Object> schemeRename(@RequestBody JSONObject jsonObject) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pageData = this.getPageData(jsonObject);
            //保存方案之前先做【方案描述】字段校验
            if (indexSchemeService.validatySchemeDescr(pageData) > 0) {
                result.put("result", "false");
                result.put("msg", "方案描述重复");
            } else {
                pageData.put("date", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
                indexSchemeService.updateSchemeData(pageData);
                result.put("result", "success");
                result.put("msg", "方案重命名成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", "false");
            result.put("msg", "方案重命名失败");
            log.error(e.getMessage(), result);
        }
        return result;
    }

    /**
     * 删除指标方案
     *
     * @param jsonObject
     */
    @ApiOperation(value = "删除指标方案")
    @PostMapping(value = "/deleteScheme")
    public Map<String, Object> deleteScheme(@RequestBody JSONObject jsonObject) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pageData = this.getPageData(jsonObject);
            pageData.put("userId", pageData.get("userId").toString());
            pageData.put("date", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
            indexSchemeService.deleteSchemeById(pageData);
            result.put("result", "success");
            result.put("msg", "删除指标方案成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", "false");
            result.put("msg", "删除指标方案失败");
            log.error(e.getMessage(), result);
        }
        return result;
    }

    /**
     * 删除个人常用指标公共方案
     *
     * @param jsonObject
     */
    @ApiOperation(value = "删除个人常用指标公共方案")
    @PostMapping(value = "/deletePublicScheme")
    public Map<String, Object> deletePublicScheme(@RequestBody JSONObject jsonObject) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pageData = this.getPageData(jsonObject);
            pageData.put("userId", pageData.get("userId").toString());
            pageData.put("date", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
            indexSchemeService.deletePublicScheme(pageData);
            result.put("result", "success");
            result.put("msg", "删除指标公共方案成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", "false");
            result.put("msg", "删除指标公共方案失败");
            log.error(e.getMessage(), result);
        }
        return result;
    }


    /**
     * 添加常用公共方案
     *
     * @param jsonObject
     */
    @ApiOperation(value = "添加常用公共方案")
    @PostMapping(value = "/insertPublicScheme")
    public Map<String, Object> insertPublicScheme(@RequestBody JSONObject jsonObject) {
        Map<String, Object> result = new HashMap<>();
        try {

            PageData pageData = this.getPageData(jsonObject);
            if (indexSchemeService.isUsedPublicScheme(pageData)) { //个人已添加公共方案到首页
                result.put("result", "false");
                result.put("msg", "已添加过该方案");
            } else {
                pageData.put("publicId", this.get32UUID());
                pageData.put("date", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
                //保存公共方案到个人常用的公共方案
                indexSchemeService.insertPublicScheme(pageData);
                result.put("result", "success");
                result.put("msg", "添加成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", "false");
            result.put("msg", "添加失败");
            log.error(e.getMessage(), result);
        }
        return result;
    }

    /**
     * 提交指标公共方案
     *
     * @param jsonObject
     */
    @ApiOperation(value = "提交指标方案")
    @PostMapping(value = "/submitScheme")
    public Map<String, Object> submitScheme(@RequestBody JSONObject jsonObject) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pageData = this.getPageData(jsonObject);
            pageData.put("isPublicScheme", "0"); //变为公共方案
            pageData.put("userId", pageData.get("userId").toString());
            pageData.put("date", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
            //提交个人方案变为公共方案
            indexSchemeService.updateSchemeData(pageData);
            result.put("result", "success");
            result.put("msg", "提交指标方案成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", "false");
            result.put("msg", "提交指标方案失败");
            log.error(e.getMessage(), result);
        }
        return result;
    }

    /**
     * 查询指标公共方案
     *
     * @param jsonObject
     */
    @ApiOperation(value = "查询指标公共方案")
    @PostMapping(value = "/selectPublicScheme")
    public Map<String, Object> selectPublicScheme(@RequestBody JSONObject jsonObject) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pageData = this.getPageData(jsonObject);
            Integer pageSize = Integer.parseInt(pageData.getString("pageSize"));
            Integer pageNo = (Integer.parseInt(pageData.getString("pageNo")) - 1) * pageSize;
            pageData.put("page", pageNo);
            pageData.put("rows", pageSize);
            pageData.put("isPublicScheme", "0");
            Integer count = indexSchemeService.getSchemeCount(pageData); //查询公共指标个数
            List<Map<String, Object>> dataList = indexSchemeService.selectPublicScheme(pageData);
            result.put("result", "success");
            result.put("total", count);//total键 存放总记录数，必须的
            result.put("msg", "查询指标公共方案成功");
            result.put("rows", dataList);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", "false");
            result.put("msg", "查询指标公共方案失败");
            log.error(e.getMessage(), result);
        }
        return result;
    }

    /**
     * 查询已保存的指标方案
     *
     * @return
     */
    @ApiOperation(value = "查询保存的指标方案")
    @PostMapping(value = "/selectSchemeTable")
    public Map<String, Object> selectSchemeTable(@RequestBody JSONObject jsonObject) {
        Map<String, Object> result = new HashMap<>();
        PageData pageData = this.getPageData(jsonObject);
        try {
            Integer pageSize = Integer.parseInt(pageData.getString("pageSize"));
            Integer pageNo = (Integer.parseInt(pageData.getString("pageNo")) - 1) * pageSize;
            pageData.put("page", pageNo);
            pageData.put("rows", pageSize);
            String isPublicScheme = pageData.getString("isPublicScheme"); //是否为个人指标方案或个人常用公共指标方案
            //根据userId的参数是否为空判断sql方案的count是条件查询还是全查询
            String userId = oConvertUtils.isEmpty(pageData.get("userId")) ? null : pageData.getString("userId");
            Integer count = "1".equals(isPublicScheme) ? indexSchemeService.getSchemeCount(pageData) : indexSchemeService.getUsedPublicSchemeCount(pageData);
            List<Map<String, Object>> dataList = indexSchemeService.selectSchemeTable(pageData);
            result.put("result", "success");
            result.put("total", count);//total键 存放总记录数，必须的
            result.put("msg", "查询指标方案成功");
            result.put("rows", dataList);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", "false");
            result.put("msg", "查询指标方案失败");
            log.error(e.getMessage(), result);
        }
        return result;
    }

    /**
     * 根据指标方案id查询指标数据
     *
     * @param jsonObject
     * @return
     */
    @ApiOperation(value = "查询保存的指标方案数据")
    @PostMapping(value = "/selectSchemeData")
    public Map<String, Object> selectSchemeData(@RequestBody JSONObject jsonObject) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pageData = this.getPageData(jsonObject);
            String schemeId = pageData.getString("schemeId");
            this.selectSchemeColumns(result, schemeId); //指标方案的表头字段
            Integer pageNo = 1;
            Integer pageSize = 10;
            if (oConvertUtils.isNotEmpty(pageData.get("pageNo"))) {
                pageNo = Integer.parseInt(pageData.getString("pageNo"));
            }
            if (oConvertUtils.isNotEmpty(pageData.get("pageSize"))) {
                pageSize = Integer.parseInt(pageData.getString("pageSize"));
            }
            String schemeSql = indexSchemeService.selectSchemeSQL(schemeId);
            if (StringUtils.isNotBlank(schemeSql)) {
                Integer count = indexRelationService.getIndicatorsCount(schemeSql);
                schemeSql = schemeSql + " LIMIT " + pageSize + " OFFSET " + ((pageNo - 1) * pageSize);
                List<Map<String, Object>> dataList = indexRelationService.getIndicatorsTable(schemeSql);
                result.put("result", "success");
                result.put("total", count);//total键 存放总记录数，必须的
                result.put("msg", "查询指标方案数据成功");
                result.put("rows", dataList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", "false");
            result.put("msg", "查询指标方案数据失败");
            log.error(e.getMessage(), result);
        }
        return result;
    }

    /**
     * 查询以保存的指标方案的表头字段
     *
     * @param result
     * @param schemeId
     */
    private void selectSchemeColumns(Map<String, Object> result, String schemeId) {
        result.put("columns", indexSchemeService.selectSchemeThead(schemeId));
    }

    /**
     * 指标方案数据导出
     *
     * @param params
     * @param request
     * @param response
     * @throws Exception
     */
    @ApiOperation(value = "指标方案数据导出")
    @GetMapping(value = "/downLoadSchemeData")
    public void downLoadIssueList(@RequestParam("params") String params, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = JSON.parseObject(params, new TypeReference<Map<String, Object>>() {
        }, Feature.OrderedField); //传递的json串按照顺序转换
        PageData pageData = this.getPageData(JSONObject.parseObject(params));
        String schemeSQL = createSchemeSQL.getSchemeSQL(pageData);
        List<Map<String, Object>> dataList = indexRelationService.getIndicatorsTable(schemeSQL);
        LinkedHashMap<String, Object> titleMap = JSON.parseObject(map.get("titles").toString(), LinkedHashMap.class);
        String[] titleArray = new String[titleMap.size()];
        String[] columnArray = new String[titleMap.size()];

        //解析页面参数对象
        int i = 0;
        for (String key : titleMap.keySet()) {
            columnArray[i] = key;
            titleArray[i] = titleMap.get(key).toString();
            i++;
        }

        Map<String, Object> excelMap = new HashMap<>();
        String fileName = "指标查询数据" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + ".xlsx";
        excelMap.put("fileName", fileName); //Excel文件名称
        excelMap.put("filePath", saveDir + "IndexFiles"); //Excel文件生成路径
        excelMap.put("titleArray", titleArray); //Excel列头数组
        excelMap.put("columnArray", columnArray); //字段数组
        excelMap.put("datalist", dataList); //Excel数据list
        if (CreateSchemeExcel.exportExcel(excelMap)) {
            //生成的Excel存放路径
            String filePath = excelMap.get("filePath").toString() + "/" + excelMap.get("fileName").toString();
            FileDownload.fileDownload(response, filePath, fileName, request);
        }
    }

}

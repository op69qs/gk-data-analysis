package org.jeecg.modules.indexlib.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.modules.BaseController;
import org.jeecg.modules.indexlib.service.IndexRelationService;
import org.jeecg.modules.indexlib.service.IndexSchemeService;
import org.jeecg.modules.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@Api(tags = "指标方案")
@RequestMapping(value = "indexSchemeController", produces = MediaType.APPLICATION_JSON_VALUE)
public class IndexSchemeController extends BaseController {

    @Autowired
    private IndexSchemeService indexSchemeService;

    @Autowired
    private IndexRelationService indexRelationService;

    @PostMapping(value = "getIndexInfo")
    @ApiOperation(value = "获取指标信息")
    public Map<String, Object> getIndexInfo(@RequestBody JSONObject jsonObject) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pageData = new PageData(jsonObject);
            if (StringUtils.isEmpty(pageData.getString("SCHEME_COLUMS"))) {
                return null;
            }
            result.put("indexInfoList", indexRelationService.getBatchIndexInfo(pageData));
            result.put("result", "success");
            result.put("msg", "删除指标方案成功");
        } catch (Exception e) {
            log.error("指标信息获取失败", e);
            result.put("result", "failed");
            result.put("msg", "指标信息获取失败");
        }
        return result;
    }

    @ApiOperation(value = "删除指标方案")
    @PostMapping(value = "/deleteScheme")
    public Map<String, Object> deleteScheme(@RequestBody JSONObject jsonObject) {
        Map<String, Object> result = new HashMap<>();
        try {
            indexSchemeService.deleteSchemeById(getPageData(jsonObject));
            result.put("result", "success");
            result.put("msg", "删除指标方案成功");
        } catch (Exception e) {
            log.error("删除指标方案失败", e);
            result.put("result", "failed");
            result.put("msg", "删除指标方案失败");
        }
        return result;
    }

    @ApiOperation(value = "查询保存的指标方案")
    @PostMapping(value = "/selectSchemeTable")
    public Map<String, Object> selectSchemeTable(@RequestBody JSONObject jsonObject) {
        Map<String, Object> result = new HashMap<>();
        PageData pageData = getPageData(jsonObject);
        try {
            int pageSize = Integer.parseInt(pageData.getString("pageSize"));
            int page = (Integer.parseInt(pageData.getString("pageNo")) - 1) * pageSize;
            pageData.put("page", page);
            pageData.put("pageSize", pageSize);
            pageData.put("schemeDescr", pageData.getString("name"));
            int count = indexSchemeService.getSchemeCount(pageData);
            List<Map<String, Object>> rows = indexSchemeService.selectSchemeTable(pageData);
            result.put("result", "success");
            result.put("total", count);
            result.put("msg", "查询指标方案成功");
            result.put("rows", rows);
        } catch (Exception e) {
            log.error("查询指标方案失败", e);
            result.put("result", "failed");
            result.put("msg", "查询指标方案失败");
        }
        return result;
    }
}

package org.jeecg.modules.visualScreen.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.BaseController;
import org.jeecg.modules.util.PageData;
import org.jeecg.modules.visualScreen.service.IndexLibrarySchemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@Api(tags = "指标库方案")
@RequestMapping(value = "/indexLibraryScheme", produces = MediaType.APPLICATION_JSON_VALUE)
public class IndexLibrarySchemeController extends BaseController {

    @Autowired
    private IndexLibrarySchemeService indexLibrarySchemeService;

    @RequestMapping(value = "/getPage", method = RequestMethod.POST)
    @ApiOperation("列表")
    public Map<String, Object> getPage(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            Integer pageNo = (Integer.parseInt(pd.getString("pageNo")) - 1) * Integer.parseInt(pd.getString("pageSize"));
            pd.put("page", pageNo);
            pd.put("rows", Integer.parseInt(pd.getString("pageSize")));
            List<Map<String, Object>> data = indexLibrarySchemeService.getPage(pd);
            Integer count = indexLibrarySchemeService.getCount(pd);
            result.put("total", count);
            result.put("rows", data);
            result.put("msg", "查询成功");
            result.put("result", "success");
        } catch (Exception e) {
            log.error("查询指标库方案失败", e);
            result.put("msg", e.getMessage());
            result.put("result", "failed");
        }
        return result;
    }

    @RequestMapping(value = "/del", method = RequestMethod.POST)
    @ApiOperation("删除")
    public Map<String, Object> del(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        try {
            String id = pd.get("id") + "";
            if (id != null && !"".equals(id) && !"null".equals(id)) {
                String[] ids = id.split(",");
                for (String i : ids) {
                    pd.put("id", i);
                    indexLibrarySchemeService.del(pd);
                }
            }
            result.put("msg", "操作成功");
            result.put("result", "success");
        } catch (Exception e) {
            log.error("删除指标库方案失败", e);
            result.put("msg", "操作失败");
            result.put("result", "failed");
        }
        return result;
    }

    @RequestMapping(value = "/toGallery", method = RequestMethod.POST)
    @ApiOperation("转图")
    public Map<String, Object> toGallery(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        try {
            indexLibrarySchemeService.toGallery(pd);
            result.put("msg", "转图成功");
            result.put("result", "success");
        } catch (Exception e) {
            log.error("指标库方案转图失败", e);
            result.put("msg", e.getMessage() == null ? "转图失败" : e.getMessage());
            result.put("result", "failed");
        }
        return result;
    }
}

package org.jeecg.modules.visualScreen.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.BaseController;
import org.jeecg.modules.util.PageData;
import org.jeecg.modules.visualScreen.service.GalleryService;
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
@Api(tags = "图库")
@RequestMapping(value = "/gallery", produces = MediaType.APPLICATION_JSON_VALUE)
public class GalleryController extends BaseController {

    @Autowired
    private GalleryService galleryService;

    @RequestMapping(value = "/getPage", method = RequestMethod.POST)
    @ApiOperation("列表")
    public Map<String, Object> getPage(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            Integer pageNo = (Integer.parseInt(pd.getString("pageNo")) - 1) * Integer.parseInt(pd.getString("pageSize"));
            pd.put("page", pageNo);
            pd.put("rows", Integer.parseInt(pd.getString("pageSize")));
            List<Map<String, Object>> data = galleryService.getPage(pd);
            Integer count = galleryService.getCount(pd);
            result.put("total", count);//total键 存放总记录数，必须的
            result.put("rows", data);//rows键 存放每页记录 list
            result.put("msg", "查询成功");
            result.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", e.getMessage());
            result.put("result", "success");
        }
        return result;
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.POST)
    @ApiOperation("全部")
    public Map<String, Object> getAll() {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData();
            List<Map<String, Object>> data = galleryService.getAll(pd);
            result.put("rows", data);
            result.put("msg", "查询成功");
            result.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", e.getMessage());
            result.put("result", "success");
        }
        return result;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public Map<String, Object> add(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        try {
            pd.put("id",get32UUID());
            galleryService.add(pd);
            result.put("msg", "添加成功");
            result.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", "添加失败");
            result.put("result", "failed");
        }
        return result;
    }
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("修改")
    public Map<String, Object> edit(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        try {
            galleryService.edit(pd);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", "修改失败");
            result.put("result", "failed");
        }
        result.put("msg", "修改成功");
        result.put("result", "success");
        return result;
    }

    @RequestMapping(value = "/del", method = RequestMethod.POST)
    @ApiOperation("删除")
    public Map<String, Object> del(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        try {
            String id = pd.get("id") + "";
            if (null != id && !"".equals(id)) {
                String[] ids = id.split(",");
                for (String i : ids) {
                    pd.put("id", i);
                    galleryService.del(pd);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", "操作失败");
            result.put("result", "failed");
        }
        result.put("msg", "操作成功");
        result.put("result", "success");
        return result;
    }

}

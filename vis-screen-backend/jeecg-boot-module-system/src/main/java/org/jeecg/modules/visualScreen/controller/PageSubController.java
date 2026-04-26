package org.jeecg.modules.visualScreen.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.BaseController;
import org.jeecg.modules.util.PageData;
import org.jeecg.modules.visualScreen.model.PageSub;
import org.jeecg.modules.visualScreen.model.PageWhere;
import org.jeecg.modules.visualScreen.service.PageSubService;
import org.jeecg.modules.visualScreen.service.PageWhereService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@Api(tags = "页面设置子表-图库信息")
@RequestMapping(value = "/pageSub", produces = MediaType.APPLICATION_JSON_VALUE)
public class PageSubController extends BaseController {

    @Autowired
    private PageSubService pageSubService;

    @Autowired
    private PageWhereService pageWhereService;

    @RequestMapping(value = "/getPage", method = RequestMethod.POST)
    @ApiOperation("列表")
    public Map<String, Object> getPage(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            Integer pageNo = (Integer.parseInt(pd.getString("pageNo")) - 1) * Integer.parseInt(pd.getString("pageSize"));
            pd.put("page", pageNo);
            pd.put("rows", Integer.parseInt(pd.getString("pageSize")));
            List<Map<String, Object>> data = pageSubService.getPage(pd);
            result.put("rows", data);//rows键 存放每页记录 list
            Integer count = pageSubService.getCount(pd);
            result.put("total", count);//total键 存放总记录数，必须的
            result.put("msg", "查询成功");
            result.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", "操作失败");
            result.put("result", "success");
        }
        return result;
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.POST)
    @ApiOperation("全部")
    public Map<String, Object> getAll(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            List<Map<String, Object>> data = pageSubService.getAll(pd);
            if (null != data && data.size() > 0) {
                List<PageSub> pss = new ArrayList<>();
                PageData queryPd = new PageData();
                for (Map<String, Object> tmp : data) {
                    PageSub ps = new PageSub();
                    ps.setId(tmp.get("id") + "");
                    ps.setGallery_id(tmp.get("gallery_id") + "");
                    ps.setPage_id(tmp.get("page_id") + "");
                    ps.setTime_type(tmp.get("time_type") + "");
                    ps.setTime_interval(tmp.get("time_interval") + "");
                    ps.setContent(tmp.get("content") + "");
                    ps.setX(tmp.get("x") + "");
                    ps.setY(tmp.get("y") + "");
                    ps.setW(tmp.get("w") + "");
                    ps.setH(tmp.get("h") + "");
                    ps.setI(tmp.get("i") + "");
                    ps.setTitle(tmp.get("title") + "");
                    ps.setType(tmp.get("type") + "");
                    ps.setUnit(tmp.get("unit") + "");
                    ps.setOption(tmp.get("option") + "");
                    ps.setQuery_path(tmp.get("query_path") + "");
                    if (null == tmp.get("gallery_id") || "".equals(tmp.get("gallery_id"))){
                        pss.add(ps);
                        continue;
                    }
                    queryPd.put("gallery_id", tmp.get("gallery_id"));
                    queryPd.put("page_id", tmp.get("page_id"));
                    List<Map<String, Object>> whereData = pageWhereService.getAll(queryPd);
                    if (null != whereData && whereData.size() > 0) {
                        List<PageWhere> pws = new ArrayList<>();
                        for (Map<String, Object> res : whereData) {
                            PageWhere pw = new PageWhere();
                            pw.setWhere_type(res.get("where_type") + "");
                            pw.setWhere_key(res.get("where_key") + "");
                            pw.setWhere_value(res.get("where_value") + "");
                            pws.add(pw);
                        }
                        ps.setPageWhere(pws);
                    }
                    pss.add(ps);
                }
                result.put("rows", pss);//rows键 存放每页记录 list
            } else {
                result.put("rows", "{}");//rows键 存放每页记录 list
            }
            result.put("msg", "查询成功");
            result.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", "操作失败");
            result.put("result", "success");
        }
        return result;
    }
}

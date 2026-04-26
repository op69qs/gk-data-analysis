package org.jeecg.modules.visualScreen.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.BaseController;
import org.jeecg.modules.util.DateUtil;
import org.jeecg.modules.util.PageData;
import org.jeecg.modules.visualScreen.model.PageInfo;
import org.jeecg.modules.visualScreen.model.PageSub;
import org.jeecg.modules.visualScreen.model.PageWhere;
import org.jeecg.modules.visualScreen.service.PageInfoService;
import org.jeecg.modules.visualScreen.service.PageSubService;
import org.jeecg.modules.visualScreen.service.PageWhereService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
@Api(tags = "页面设置")
@RequestMapping(value = "/pageInfo", produces = MediaType.APPLICATION_JSON_VALUE)
public class PageInfoController extends BaseController {

    @Autowired
    private PageInfoService pageInfoService;

    @Autowired
    private PageSubService pageSubService;

    @Autowired
    private PageWhereService pageWhereService;

    @Value("${TEMPLATE_FILE_PATH}")
    private String uploadFileDir;

    @RequestMapping(value = "/getPage", method = RequestMethod.POST)
    @ApiOperation("列表")
    public Map<String, Object> getPage(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            Integer pageNo = (Integer.parseInt(pd.getString("pageNo")) - 1) * Integer.parseInt(pd.getString("pageSize"));
            pd.put("page", pageNo);
            pd.put("rows", Integer.parseInt(pd.getString("pageSize")));
            List<Map<String, Object>> data = pageInfoService.getPage(pd);
            Integer count = pageInfoService.getCount(pd);
            result.put("total", count);//total键 存放总记录数，必须的
            result.put("rows", data);//rows键 存放每页记录 list
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
            List<Map<String, Object>> data = pageInfoService.getAll(pd);
            result.put("rows", data);
            result.put("msg", "查询成功");
            result.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", "操作失败");
            result.put("result", "success");
        }
        return result;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public Map<String, Object> add(@RequestBody(required = false) PageInfo pageInfo) {
        Map<String, Object> result = new HashMap<>();
        PageData pd = new PageData();
        try {
            pd.put("name", pageInfo.getName());
            if (checkRepeat(pd)) {
                result.put("msg", "添加失败,该页面已存在");
                result.put("result", "failed");
                return result;
            }
            String id = get32UUID();
            pd.put("id", id);
            pd.put("template", pageInfo.getTemplate());
            pd.put("background_type", pageInfo.getBackground_type());
            pd.put("colour", pageInfo.getColour());
            pd.put("thumbnail", pageInfo.getThumbnail());
            pd.put("add_user", pageInfo.getAdd_user());
            pd.put("title_background", pageInfo.getTitle_background());
            pd.put("state", pageInfo.getState());
            pd.put("content", pageInfo.getContent());
            pd.put("add_time", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
            pageInfoService.add(pd);

            pageInfo.setId(id);
            addSub(pageInfo);

            result.put("msg", "添加成功");
            result.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", "添加失败");
            result.put("result", "failed");
        }
        return result;
    }

    private boolean checkRepeat(PageData pd) {
        PageData queryPd = new PageData();
        queryPd.put("name", pd.get("name"));
        queryPd.put("id", pd.get("id"));
        List<Map<String, Object>> maps = pageInfoService.checkRepeat(queryPd);
        if (null != maps && maps.size() > 0) {
            return true;
        }
        return false;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("修改")
    public Map<String, Object> edit(@RequestBody(required = false) PageInfo pageInfo) {
        Map<String, Object> result = new HashMap<>();
        PageData pd = new PageData();
        try {
            pd.put("id", pageInfo.getId());
            pd.put("name", pageInfo.getName());
            if (checkRepeat(pd)) {
                result.put("msg", "修改失败,该页面已存在");
                result.put("result", "failed");
                return result;
            }
            pd.put("template", pageInfo.getTemplate());
            pd.put("background_type", pageInfo.getBackground_type());
            pd.put("colour", pageInfo.getColour());
            pd.put("thumbnail", pageInfo.getThumbnail());
            pd.put("add_user", pageInfo.getAdd_user());
            pd.put("title_background", pageInfo.getTitle_background());
            pd.put("state", pageInfo.getState());
            pd.put("content", pageInfo.getContent());
            pageInfoService.edit(pd);

            PageData delPd = new PageData();
            delPd.put("page_id", pageInfo.getId());
            pageSubService.del(delPd);
            pageWhereService.del(delPd);

            addSub(pageInfo);

        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", "修改失败");
            result.put("result", "failed");
        }
        result.put("msg", "修改成功");
        result.put("result", "success");
        return result;
    }

    @RequestMapping(value = "/editState", method = RequestMethod.POST)
    @ApiOperation("修改状态")
    public Map<String, Object> editState(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            pageInfoService.edit(pd);
            result.put("msg", "修改成功");
            result.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", "修改失败");
            result.put("result", "success");
        }
        return result;
    }

    @RequestMapping(value = "/del", method = RequestMethod.POST)
    @ApiOperation("删除")
    public Map<String, Object> del(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        PageData delPd = new PageData();
        try {
            String id = pd.get("id") + "";
            if (null != id && !"".equals(id)) {
                String[] ids = id.split(",");
                for (String i : ids) {
                    pd.put("id", i);
                    pageInfoService.del(pd);
                    delPd.put("page_id", i);
                    pageSubService.del(delPd);
                    pageWhereService.del(delPd);
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

    private void addSub(PageInfo pageInfo) {
        if (null != pageInfo.getPage_sub() && pageInfo.getPage_sub().size() > 0) {
            PageData subPd = new PageData();
            for (int i = 0; i < pageInfo.getPage_sub().size(); i++) {
                PageSub ps = pageInfo.getPage_sub().get(i);
                subPd.put("id", get32UUID());
                subPd.put("gallery_id", ps.getGallery_id());
                subPd.put("page_id", pageInfo.getId());
                subPd.put("time_type", ps.getTime_type());
                subPd.put("time_interval", ps.getTime_interval());
                subPd.put("sort", i + 1);
                subPd.put("content", ps.getContent());
                subPd.put("x", ps.getX());
                subPd.put("y", ps.getY());
                subPd.put("w", ps.getW());
                subPd.put("h", ps.getH());
                subPd.put("i", ps.getI());
                subPd.put("title", ps.getTitle());
                subPd.put("type", ps.getType());
                subPd.put("option", ps.getOption());
                subPd.put("query_path", ps.getQuery_path());
                subPd.put("unit", ps.getUnit());
                pageSubService.add(subPd);
                List<PageWhere> pws = ps.getPageWhere();
                if (null != pws && pws.size() > 0) {
                    PageData wherePd = new PageData();
                    for (PageWhere pw : pws) {
                        wherePd.put("id", get32UUID());
                        wherePd.put("page_id", pageInfo.getId());
                        wherePd.put("gallery_id", ps.getGallery_id());
                        wherePd.put("where_type", pw.getWhere_type());
                        wherePd.put("where_key", pw.getWhere_key());
                        wherePd.put("where_value", pw.getWhere_value());
                        wherePd.put("sub_id", subPd.get("id"));
                        pageWhereService.add(wherePd);
                    }
                }
            }
        }
    }

}

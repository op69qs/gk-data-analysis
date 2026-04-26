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
import org.jeecg.modules.visualScreen.model.SchemeInfo;
import org.jeecg.modules.visualScreen.model.SchemeRel;
import org.jeecg.modules.visualScreen.service.PageInfoService;
import org.jeecg.modules.visualScreen.service.PageSubService;
import org.jeecg.modules.visualScreen.service.SchemeInfoService;
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
@Api(tags = "方案设置")
@RequestMapping(value = "/schemeInfo", produces = MediaType.APPLICATION_JSON_VALUE)
public class SchemeInfoController extends BaseController {

    @Autowired
    private SchemeInfoService schemeInfoService;
    @Autowired
    private PageInfoService pageInfoService;
    @Autowired
    private PageSubService pageSubService;

    @RequestMapping(value = "/getPage", method = RequestMethod.POST)
    @ApiOperation("列表")
    public Map<String, Object> getPage(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            Integer pageNo = (Integer.parseInt(pd.getString("pageNo")) - 1) * Integer.parseInt(pd.getString("pageSize"));
            pd.put("page", pageNo);
            pd.put("rows", Integer.parseInt(pd.getString("pageSize")));
            List<Map<String, Object>> data = schemeInfoService.getPage(pd);
            Integer count = schemeInfoService.getCount(pd);
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
            List<Map<String, Object>> data = schemeInfoService.getAll(pd);
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

    @RequestMapping(value = "/getAllRel", method = RequestMethod.POST)
    @ApiOperation("全部")
    public Map<String, Object> getAllRel(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            List<Map<String, Object>> data = schemeInfoService.getAllRel(pd);
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
    public Map<String, Object> add(@RequestBody(required = false) SchemeInfo schemeInfo) {
        Map<String, Object> result = new HashMap<>();
        PageData pd = new PageData();
        try {
            pd.put("name", schemeInfo.getName());
            if (checkRepeat(pd)) {
                result.put("msg", "添加失败,该方案已存在");
                result.put("result", "failed");
                return result;
            }
            String id = get32UUID();
            pd.put("id", id);
            pd.put("name", schemeInfo.getName());
            pd.put("rotation_interval", schemeInfo.getRotation_interval());
            pd.put("add_user", schemeInfo.getAdd_user());
            pd.put("add_time", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
            schemeInfoService.add(pd);

            schemeInfo.setId(id);
            addRel(schemeInfo);

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
        List<Map<String, Object>> maps = schemeInfoService.checkRepeat(queryPd);
        if (null != maps && maps.size() > 0) {
            return true;
        }
        return false;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("修改")
    public Map<String, Object> edit(@RequestBody(required = false) SchemeInfo schemeInfo) {
        Map<String, Object> result = new HashMap<>();
        PageData pd = new PageData();
        try {
            pd.put("id", schemeInfo.getId());
            pd.put("name", schemeInfo.getName());
            if (checkRepeat(pd)) {
                result.put("msg", "修改失败,该页面已存在");
                result.put("result", "failed");
                return result;
            }
            pd.put("name", schemeInfo.getName());
            pd.put("rotation_interval", schemeInfo.getRotation_interval());
            schemeInfoService.edit(pd);

            PageData delPd = new PageData();
            delPd.put("scheme_id", schemeInfo.getId());
            schemeInfoService.delRel(delPd);

            addRel(schemeInfo);

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
        PageData delPd = new PageData();
        try {
            String id = pd.get("id") + "";
            if (null != id && !"".equals(id)) {
                String[] ids = id.split(",");
                for (String i : ids) {
                    pd.put("id", i);
                    schemeInfoService.del(pd);
                    delPd.put("scheme_id", i);
                    schemeInfoService.delRel(delPd);
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

    private void addRel(SchemeInfo schemeInfo) {
        if (null != schemeInfo.getSchemeRel() && schemeInfo.getSchemeRel().size() > 0) {
            PageData subPd = new PageData();
            for (int i = 0; i < schemeInfo.getSchemeRel().size(); i++) {
                SchemeRel sr = schemeInfo.getSchemeRel().get(i);
                subPd.put("scheme_id", schemeInfo.getId());
                subPd.put("page_id", sr.getPage_id());
                subPd.put("thumbnail", sr.getThumbnail());
                subPd.put("sort", sr.getSort());
                schemeInfoService.addRel(subPd);
            }
        }
    }

    @RequestMapping(value = "/getAllPage", method = RequestMethod.POST)
    @ApiOperation("方案包含的所有页面信息以及页面包含的所有图库信息")
    public Map<String, Object> getAllPage(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            List<Map<String, Object>> data = schemeInfoService.getAllRel(pd);
            if (null != data && data.size() > 0) {
                List<PageInfo> pageInfos = new ArrayList<>();
                PageData queryPd = new PageData();
                int count = 0;
                for (Map<String, Object> d : data) {
                    queryPd.put("id", d.get("page_id"));
                    PageInfo pi = pageInfoService.getPageInfo(queryPd);
                    if (null != pi) {
                        queryPd.put("page_id", d.get("page_id"));
                        List<PageSub> pss = pageSubService.getPageSub(queryPd);
                        if (null != pss && pss.size() > 0) {
                            for (PageSub ps : pss) {
                                if (null != ps.getQuery_path() && !"".equals(ps.getQuery_path()) && !"null".equals(ps.getQuery_path())) {
                                    count++;
                                }
                            }
                            pi.setPage_sub(pss);
                        }
                        pageInfos.add(pi);
                    }
                }
                result.put("data", pageInfos);
                result.put("count", count);
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

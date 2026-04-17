package org.jeecg.modules.dimnsnSetting.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.dimnsnSetting.BaseController;
import org.jeecg.modules.dimnsnSetting.model.LevyingBodies;
import org.jeecg.modules.dimnsnSetting.service.LevyingBodiesService;
import org.jeecg.modules.util.PageData;
import org.jeecg.modules.enumSetting.service.EnumService;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@Api(tags = "征收机关")
@RequestMapping(value = "/levyingBodies", produces = MediaType.APPLICATION_JSON_VALUE)
public class LevyingBodiesController extends BaseController {

    @Autowired
    protected LevyingBodiesService levyingBodies;
    @Autowired
    private EnumService enumService;


    @RequestMapping(value = "/getPage", method = RequestMethod.POST)
    @ApiOperation("列表")
    public Map<String, Object> getPage(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            Integer pageNo = (Integer.parseInt(pd.getString("pageNo")) - 1) * Integer.parseInt(pd.getString("pageSize"));
            pd.put("page", pageNo);
            pd.put("rows", Integer.parseInt(pd.getString("pageSize")));
            List<Map<String, Object>> data = levyingBodies.getPage(pd);
            Integer count = levyingBodies.getCount(pd);
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
    @ApiOperation("所有")
    public Map<String, Object> getAll(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            List<Map<String, Object>> data = levyingBodies.getAll(pd);
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
            List<Map<String, Object>> maps = levyingBodies.checkRepeat(pd);
            if (null != maps && maps.size() > 0) {
                result.put("msg", "添加失败,该征收机关已存在");
                result.put("result", "failed");
                return result;
            }
            PageData enumPd = new PageData();
            enumPd.put("enum_type_id","36");
            enumPd.put("enum_id",pd.get("TYPE_ID"));
            pd.put("TYPE_DSCR",enumService.getEnumDscr(enumPd));
            levyingBodies.add(pd);
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
    @ApiOperation("编辑")
    public Map<String, Object> edit(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        try {
            PageData enumPd = new PageData();
            enumPd.put("enum_type_id","36");
            enumPd.put("enum_id",pd.get("TYPE_ID"));
            pd.put("TYPE_DSCR",enumService.getEnumDscr(enumPd));
            levyingBodies.edit(pd);
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
            String TAX_ORG_ID = pd.get("TAX_ORG_ID") + "";
            if (!"".equals(TAX_ORG_ID)) {
                String[] id = TAX_ORG_ID.split(",");
                for (String code : id) {
                    pd.put("TAX_ORG_ID", code);
                    levyingBodies.del(pd);
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

    @RequestMapping(value = "/exportXls", method = RequestMethod.GET)
    @ApiOperation("导出")
    public ModelAndView exportXls(@RequestBody(required = false) JSONObject param) {
        PageData pd = this.getPageData(param);
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        List<LevyingBodies> list = levyingBodies.getExport(pd);
        mv.addObject(NormalExcelConstants.FILE_NAME, "征收机关");
        mv.addObject(NormalExcelConstants.CLASS, LevyingBodies.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("征收机关", "征收机关"));
        mv.addObject(NormalExcelConstants.DATA_LIST, list);
        return mv;
    }
}

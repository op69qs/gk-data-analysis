package org.jeecg.modules.enumSetting.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.util.PageData;
import org.jeecg.modules.enumSetting.BaseController;
import org.jeecg.modules.enumSetting.model.TreeNode;
import org.jeecg.modules.enumSetting.service.EnumService;
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
@Api(tags = "枚举值")
@RequestMapping(value = "/EnumController", produces = MediaType.APPLICATION_JSON_VALUE)
public class EnumController extends BaseController {

    @Autowired
    private EnumService enumService;

    @RequestMapping(value = "/getData", method = RequestMethod.POST)
    @ApiOperation("获取枚举值列表接口")
    public Map<String, Object> getData(@RequestBody JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            Integer pageNo = (Integer.parseInt(pd.getString("pageNo")) - 1) * Integer.parseInt(pd.getString("pageSize"));
            pd.put("page", pageNo);
            pd.put("rows", Integer.parseInt(pd.getString("pageSize")));
            List<Map<String, Object>> data = enumService.getData(pd);
            Integer count = enumService.getCount(pd);
            result.put("total", count);//total键 存放总记录数，必须的
            result.put("rows", data);//rows键 存放每页记录 list
            result.put("msg", "查询成功");
            result.put("result", "success");
        } catch (Exception e) {
            result.put("msg", e.getMessage());
            result.put("result", "success");
        }
        return result;
    }

    @RequestMapping(value = "/getEnumType", method = RequestMethod.POST)
    @ApiOperation("获取枚举类型接口")
    public Map<String, Object> getEnumType(@RequestBody JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            List<Map<String, Object>> data = enumService.getEnumType(pd);
            result.put("rows", data);
            result.put("msg", "查询成功");
            result.put("result", "success");
        } catch (Exception e) {
            result.put("msg", e.getMessage());
            result.put("result", "success");
        }

        return result;
    }

    @RequestMapping(value = "/getEnumTypeAll", method = RequestMethod.POST)
    @ApiOperation("根据枚举类型获取枚举值接口")
    public Map<String, Object> getEnumTypeAll(@RequestBody JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            List<Map<String, Object>> data = enumService.getEnumTypeAll(pd);
            result.put("rows", data);
            result.put("msg", "查询成功");
            result.put("result", "success");
        } catch (Exception e) {
            result.put("msg", e.getMessage());
            result.put("result", "success");
        }
        return result;
    }

    @RequestMapping(value = "/addEnum", method = RequestMethod.POST)
    @ApiOperation("枚举新增接口")
    public Map<String, Object> addEnum(@RequestBody JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        try {
            Integer rs = checkCode(pd);
            if (null != rs && rs > 0) {
                result.put("msg", "该数据已存在");
                result.put("result", "failed");
            } else {
//                pd.put("ENUM_ID", DateUtil.generateKey());
                enumService.addEnum(pd);
                result.put("msg", "添加成功");
                result.put("result", "success");
            }
        } catch (Exception e) {
            result.put("msg", "添加失败");
            result.put("result", "failed");
        }
        return result;
    }

    @RequestMapping(value = "/editEnum", method = RequestMethod.POST)
    @ApiOperation("枚举值修改接口")
    public Map<String, Object> editEnum(@RequestBody JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        try {
            enumService.editEnum(pd);

        } catch (Exception e) {
            result.put("msg", "修改失败");
            result.put("result", "failed");
        }
        result.put("msg", "修改成功");
        result.put("result", "success");
        return result;
    }

    @RequestMapping(value = "/delEnum", method = RequestMethod.POST)
    @ApiOperation("枚举值删除有效接口")
    public Map<String, Object> delEnum(@RequestBody JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        try {
            enumService.delEnum(pd);

        } catch (Exception e) {
            result.put("msg", "操作失败");
            result.put("result", "failed");
        }
        result.put("msg", "操作成功");
        result.put("result", "success");
        return result;
    }

    @RequestMapping(value = "/delEnumNo", method = RequestMethod.POST)
    @ApiOperation("枚举值删除无效接口")
    public Map<String, Object> delEnumNo(@RequestBody JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        try {
            enumService.delEnumNo(pd);

        } catch (Exception e) {
            result.put("msg", "删除失败");
            result.put("result", "failed");
        }
        result.put("msg", "删除成功");
        result.put("result", "success");
        return result;
    }

    @RequestMapping(value = "/getLogicalOperator", method = RequestMethod.POST)
    @ApiOperation("获取逻辑运算符")
    public Map<String, Object> getLogicalOperator(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Map<String, Object>> dataList = enumService.getLogicalOperator();
            result.put("rows", dataList);
            result.put("msg", "查询成功");
            result.put("result", "success");
        } catch (Exception e) {
            result.put("msg", "查询失败");
            result.put("result", "false");
        }
        return result;
    }

    private Integer checkCode(PageData pd) {
        PageData pdName = new PageData();
        pdName.put("ENUM_TYPE_ID", pd.getString("ENUM_TYPE_ID"));
        pdName.put("ENUM_DSCR", pd.getString("ENUM_DSCR"));
        pdName.put("ENUM_ID", pd.getString("ENUM_ID"));
        List<Map<String, Object>> data = enumService.checkCode(pdName);
        if (null != data && data.size() > 0) {
            return data.size();
        }
        return 0;
    }

    @RequestMapping(value = "/getTree", method = RequestMethod.POST)
    @ApiOperation("枚举树形结构")
    public Map<String, Object> getArea(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            List<Map<String, Object>> first = enumService.getFirst(pd);
            List<Map<String, Object>> second = enumService.getSecond(pd);
//            List<Map<String, Object>> third = enumService.getThird(pd);
            List<TreeNode> treeNodeList = new ArrayList<>();
            if (null != first && first.size() > 0) {
                for (Map<String, Object> f : first) {
                    TreeNode treeNodecomm = new TreeNode();
                    treeNodecomm.setValue(f.get("id") + "");
                    treeNodecomm.setKey(f.get("id") + "");
                    treeNodecomm.setLabel(f.get("name") + "");
                    treeNodecomm.setTitle(f.get("name") + "");
                    treeNodecomm.setDisabled(true);
                    if (null != second && second.size() > 0) {
                        for (Map<String, Object> s : second) {
                            if (null != s.get("pid") && s.get("pid").equals(f.get("id"))) {
                                TreeNode secNode = new TreeNode();
                                secNode.setValue(s.get("id") + "");
                                secNode.setKey(s.get("id") + "");
                                secNode.setLabel(s.get("name") + "");
                                secNode.setTitle(s.get("name") + "");
                                treeNodecomm.addChild(secNode);
                            }
                        }
                    }
                    treeNodeList.add(treeNodecomm);
                }
            }
            result.put("msg", "操作成功");
            result.put("rows", treeNodeList);
            result.put("result", "success");
        } catch (Exception e) {
            result.put("msg", e.getMessage());
            result.put("result", "failed");
        }
        return result;
    }

}

package org.jeecg.modules.dimnsnSetting.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.dimnsnSetting.BaseController;
import org.jeecg.modules.dimnsnSetting.model.TreeAreaNode;
import org.jeecg.modules.dimnsnSetting.model.TreeNode;
import org.jeecg.modules.dimnsnSetting.model.TreeNodeComm;
import org.jeecg.modules.dimnsnSetting.service.AreaService;

import org.jeecg.modules.util.PageData;
import org.jeecg.modules.util.TransUtilComm;
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
@Api(tags = "地区维度表")
@RequestMapping(value = "/areaController", produces = MediaType.APPLICATION_JSON_VALUE)
public class AreaController extends BaseController {

    @Autowired
    private AreaService areaService;


    @RequestMapping(value = "/getArea", method = RequestMethod.POST)
    @ApiOperation("地区树形结构")
    public Map<String, Object> getArea(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            List<Map<String, Object>> data = areaService.getArea(pd);
            TransUtilComm.getRootNode(data, "2");
            TreeNodeComm treeNodecomm = TransUtilComm.treeTrans(data, null, 0);
            List<TreeNodeComm> treeNodeList = new ArrayList<>();
            treeNodeList.add(treeNodecomm);
            result.put("msg", "操作成功");
            result.put("rows", treeNodeList);
            result.put("result", "success");
        } catch (Exception e) {
            result.put("msg", e.getMessage());
            result.put("result", "failed");
        }
        return result;
    }

    private Integer checkCode(PageData pd, String ID, String table) {
        PageData pdName = new PageData();
        pdName.put("TABLENAME", table);
        pdName.put("COlID", ID);
        List<Map<String, Object>> data = areaService.checkCode(pdName);
        if (null != data && data.size() > 0) {
            return data.size();

        }
        return 0;
    }

    @RequestMapping(value = "/addArea", method = RequestMethod.POST)
    @ApiOperation("新增地区")
    public Map<String, Object> addArea(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        try {
            Integer rs = checkCode(pd, "AREA_NO_ID", "dmcode.cm_guoku_area_code");
            if (null != rs && rs > 0) {
                result.put("msg", "该数据已存在");
                result.put("result", "failed");
            } else {
                areaService.addArea(pd);
                areaService.updateAreaPid(pd);
                result.put("msg", "添加成功");
                result.put("result", "success");
            }
        } catch (Exception e) {
            result.put("msg", e.getMessage());
            result.put("result", "failed");
        }
        return result;
    }

    @RequestMapping(value = "/editArea", method = RequestMethod.POST)
    @ApiOperation("修改接口")
    public Map<String, Object> editArea(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        try {
            areaService.editArea(pd);
            result.put("msg", "修改成功");
            result.put("result", "success");
        } catch (Exception e) {
            result.put("msg", e.getMessage());
            result.put("result", "failed");
        }

        return result;
    }

    @RequestMapping(value = "/isDelArea", method = RequestMethod.POST)
    @ApiOperation("地区是否可以进行启用有效接口")
    public Map<String, Object> isDelArea(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        try {
            String aa = "";
            String session = "";
            String area_no_pid = pd.get("area_no_pid") + "";
            if (null != area_no_pid && !"".equals(area_no_pid) && !"null".equals(area_no_pid)) {
                Integer isDel = areaService.isDelArea(pd);
                aa = isDel == 0 ? "failed" : "请先启用对应的上级地区";
                session = isDel == 0 ? "success" : "failed";
            } else {
                aa = "";
                session = "success";
            }
            result.put("msg", aa);
            result.put("result", session);
        } catch (Exception e) {
            result.put("msg", e.getMessage());
            result.put("result", "failed");
        }
        return result;
    }

    @RequestMapping(value = "/delArea", method = RequestMethod.POST)
    @ApiOperation("删除有效接口")
    public Map<String, Object> delArea(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            String state = pd.getString("state");
            if ("1".equals(state)) {
                areaService.delAreaParent(pd);
            }
            areaService.delArea(pd);
            result.put("msg", "操作成功");
            result.put("result", "success");
        } catch (Exception e) {
            result.put("msg", e.getMessage());
            result.put("result", "failed");
        }
        return result;
    }

//  --   公共方法

    @RequestMapping(value = "/getAreaList", method = RequestMethod.POST)
    @ApiOperation("地区List")
    public Map<String, Object> getAreaList(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        List<TreeAreaNode> list = new ArrayList<>();
        String par = "0";
        try {
            PageData pd = this.getPageData(param);
            if ((null == pd.get("parentId") || "".equals(pd.get("parentId"))) && (null == pd.get("area_dscr") || "".equals(pd.get("area_dscr")))) {
                par = "1";
            } else if ((null == pd.get("parentId") || "".equals(pd.get("parentId"))) && (null != pd.get("area_dscr") && !"".equals(pd.get("area_dscr")))) {
                par = "2";
            } else if (null != pd.get("parentId") && !"".equals(pd.get("parentId"))) {
                par = "0";
            }
            pd.put("par", par);
            list = areaService.getAreaList(pd);
            result.put("msg", "操作成功");
            result.put("rows", list);
            result.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", e.getMessage());
            result.put("result", "failed");
        }
        return result;
    }

    //treeList--最终返回给页面的List     list--所有顶级节点      metaList--查询的所有子父级节点
    private void getTreeListNew(List<TreeNode> treeList, List<TreeNode> list, List<TreeNode> metaList) {
        for (int i = 0; i < list.size(); i++) {
            String tempid = list.get(i).getId();
            TreeNode tree1 = new TreeNode(list.get(i));
            for (int j = 0; j < metaList.size(); j++) {
                String tempidChild = metaList.get(j).getId();
                TreeNode tree2 = new TreeNode(metaList.get(j));
                for (int y = 0; y < metaList.size(); y++) {
                    String tempid3 = metaList.get(y).getId();
                    TreeNode tree3 = new TreeNode(metaList.get(y));
                    for (int u = 0; u < metaList.size(); u++) {
                        TreeNode tree4 = new TreeNode(metaList.get(u));
                        if (tempid3.equals(tree4.getParentId())) {
                            tree3.getChildren().add(tree4);
                        }
                    }
                    if (tempidChild.equals(tree3.getParentId())) {
                        tree2.getChildren().add(tree3);
                    }
                }
                if (tempid.equals(tree2.getParentId())) {
                    tree1.getChildren().add(tree2);
                }

            }
            treeList.add(tree1);
        }
    }

    public static List<TreeNode> getRootNodes(List<TreeNode> dataList, int guoKuorBookOrg) {
        List<TreeNode> listhead = new ArrayList();
        for (int i = 0; i < dataList.size(); i++) {
            TreeNode tree = new TreeNode(dataList.get(i));
            listhead.add(tree);
        }
        return listhead;
    }

}

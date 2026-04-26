package org.jeecg.modules.dimnsnSetting.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.modules.BaseController;
import org.jeecg.modules.dimnsnSetting.model.GuoKuTreeNode;
import org.jeecg.modules.dimnsnSetting.model.TreeNode;
import org.jeecg.modules.dimnsnSetting.model.TreeNodeComm;
import org.jeecg.modules.dimnsnSetting.model.TreeNodeNews;
import org.jeecg.modules.dimnsnSetting.service.GuokuService;
import org.jeecg.modules.util.PageData;
import org.jeecg.modules.util.TransUtilComm;
import org.jeecg.modules.util.TreeFilterHeaper;
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
@Api(tags = "国库维度表")
@RequestMapping(value = "/GuokuController", produces = MediaType.APPLICATION_JSON_VALUE)
public class GuokuController extends BaseController {

    @Autowired
    private GuokuService guokuService;

    @RequestMapping(value = "/getOrgTree", method = RequestMethod.POST)
    @ApiOperation("核算主体树形结构")
    public Map<String, Object> getOrgTree(
            @RequestBody(required = false) JSONObject param
    ) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            List<Map<String, Object>> data = guokuService.getOrgTree(pd);
            TransUtilComm.getRootNode(data, "");
            //TransUtilComm.getRootNode(data,"0");
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

    @RequestMapping(value = "/getKeMuTreeName" , method = RequestMethod.POST)
    @ApiOperation("获取科目树名称")
    public Map<String, Object> getKeMuTreeName(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> jsonMap = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            List<Map<String, Object>> data = guokuService.getKeMu(pd);
            List<TreeNodeNews> treeNodeList = TreeFilterHeaper.definedTreeFilter(data);
            jsonMap.put("result", "success");
            jsonMap.put("rows", treeNodeList);
        } catch (Exception e) {
            jsonMap.put("result", "fail");
            jsonMap.put("msg", e.getMessage());
        }
        return jsonMap;
    }

    private Integer checkCode(PageData pd, String ID, String table) {
        PageData pdName = new PageData();
//        pdName.put("ID", pd.getString(ID));
        pdName.put("TABLENAME", table);
        pdName.put("COlID", ID);
        List<Map<String, Object>> data = guokuService.checkCode(pdName);
        if (null != data && data.size() > 0) {
            return data.size();

        }
        return 0;
    }

    @RequestMapping(value = "/addBookOrg", method = RequestMethod.POST)
    @ApiOperation("新增核算主体")
    public Map<String, Object> addBookOrg(@RequestBody JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        try {
            Integer rs = checkCode(pd, "BOOKORGCODE", "dmcode.cm_guoku_bookorg");
            if (null != rs && rs > 0) {
                result.put("msg", "该数据已存在");
                result.put("result", "failed");
            } else {
//                 int  isleaf=Integer.valueOf(pd.getString("isleaf"));
//                 pd.put("isleaf",isleaf==0?1:0);
                guokuService.addBookOrg(pd);
                guokuService.updateAddBookOrg(pd);
                result.put("msg", "添加成功");
                result.put("result", "success");
            }
        } catch (Exception e) {
            result.put("msg", e.getMessage());
            result.put("result", "failed");
        }
        return result;
    }

    @RequestMapping(value = "/editBookOrg", method = RequestMethod.POST)
    @ApiOperation("核算主体修改接口")
    public Map<String, Object> editBookOrg(@RequestBody JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        try {
//            int  isleaf=Integer.valueOf(pd.getString("isleaf"));
//            pd.put("isleaf",isleaf==0?1:0);
            guokuService.editBookOrg(pd);
            result.put("msg", "修改成功");
            result.put("result", "success");
        } catch (Exception e) {
            result.put("msg", e.getMessage());
            result.put("result", "failed");
        }

        return result;
    }

    @RequestMapping(value = "/isDelBookOrg", method = RequestMethod.POST)
    @ApiOperation("核算主体是否可以进行启用有效接口")
    public Map<String, Object> isDelBookOrg(@RequestBody JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        try {
            int isDel = guokuService.isDelBookOrg(pd);
            String aa = isDel == 0 ? "failed" : "请先启用对应的上级核算主体";
            String session = isDel == 0 ? "success" : "failed";
            result.put("msg", aa);
            result.put("result", session);
        } catch (Exception e) {
            result.put("msg", e.getMessage());
            result.put("result", "failed");
        }

        return result;
    }

    @RequestMapping(value = "/delBookOrg", method = RequestMethod.POST)
    @ApiOperation("核算主体删除有效接口")
    public Map<String, Object> delBookOrg(@RequestBody JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            String state = pd.getString("state");
            if ("1".equals(state)) {
                guokuService.delBookOrgParent(pd);
            }
            guokuService.delBookOrg(pd);
            result.put("msg", "操作成功");
            result.put("result", "success");
        } catch (Exception e) {
            result.put("msg", e.getMessage());
            result.put("result", "failed");
        }
        return result;
    }


//  --   国库管理


    @RequestMapping(value = "/getGuokuTree", method = RequestMethod.POST)
    @ApiOperation("国库树形结构")
    public Map<String, Object> getGuokuTree() {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData();
            List<Map<String, Object>> data = guokuService.getGuokuTree(pd);
//            TransUtil.getRootNode(data);
            List<TreeNodeNews> treeNodeList = TreeFilterHeaper.definedTreeFilter(data);
//            TreeNodeArea treeNodecomm = TransUtil.treeTrans(data, null);
//            List<TreeNodeArea> treeNodeList = new ArrayList<>();
//            treeNodeList.add(treeNodecomm);
            result.put("msg", "操作成功");
            result.put("rows", treeNodeList);
            result.put("result", "success");
        } catch (Exception e) {
            result.put("msg", e.getMessage());
            result.put("result", "failed");
        }
        return result;
    }

    @RequestMapping(value = "/getAreaTree", method = RequestMethod.POST)
    @ApiOperation("地区树形结构")
    public Map<String, Object> getAreaTree() {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData();
            List<Map<String, Object>> data = guokuService.getAreaTree(pd);
            List<TreeNodeNews> treeNodeList = TreeFilterHeaper.definedTreeFilter(data);
//            TransUtilArea.getRootNode(data);
//            TreeNodeArea treeNodecomm = TransUtilArea.treeTrans(data, null);
//            List<TreeNodeArea> treeNodeList = new ArrayList<>();
//            treeNodeList.add(treeNodecomm);
            result.put("msg", "操作成功");
            result.put("rows", treeNodeList);
            result.put("result", "success");
        } catch (Exception e) {
            result.put("msg", e.getMessage());
            result.put("result", "failed");
        }
        return result;
    }

    private Integer checkCodegk(PageData pd, String ID, String table) {
        PageData pdName = new PageData();
        pdName.put("TABLENAME", table);
        pdName.put("COlID", ID);
        List<Map<String, Object>> data = guokuService.checkCodegk(pdName);
        if (null != data && data.size() > 0) {
            return data.size();
        }
        return 0;
    }

    @RequestMapping(value = "/addGuoku", method = RequestMethod.POST)
    @ApiOperation("新增国库")
    public Map<String, Object> addGuoku(@RequestBody JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        try {
            Integer rs = checkCode(pd, "guoku_id", "dmcode.cm_guoku_dimnsn");
            if (null != rs && rs > 0) {
                result.put("msg", "该数据已存在");
                result.put("result", "failed");
            } else {
//                int  isleaf=Integer.valueOf(pd.getString("isleaf"));
//                pd.put("isleaf",isleaf==0?1:0);

                guokuService.addGuoku(pd);
                guokuService.updateAddGuoku(pd);
                result.put("msg", "添加成功");
                result.put("result", "success");
            }
        } catch (Exception e) {
            result.put("msg", e.getMessage());
            result.put("result", "failed");
        }
        return result;
    }

    @RequestMapping(value = "/editGuoku", method = RequestMethod.POST)
    @ApiOperation("国库修改接口")
    public Map<String, Object> editGuoku(@RequestBody JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        try {
//            int  isleaf=Integer.valueOf(pd.getString("isleaf"));
//            pd.put("isleaf",isleaf==0?1:0);
            guokuService.editGuoku(pd);
            result.put("msg", "修改成功");
            result.put("result", "success");
        } catch (Exception e) {
            result.put("msg", e.getMessage());
            result.put("result", "failed");
        }

        return result;
    }

    @RequestMapping(value = "/isDelGuoku", method = RequestMethod.POST)
    @ApiOperation("国库是否可以进行启用有效接口")
    public Map<String, Object> isDelGuoku(@RequestBody JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        try {
            int isDel = guokuService.isDelGuoku(pd);
            String aa = isDel == 0 ? "failed" : "请先启用对应的上级核算主体";
            String session = isDel == 0 ? "success" : "failed";
            result.put("msg", aa);
            result.put("result", session);
        } catch (Exception e) {
            result.put("msg", e.getMessage());
            result.put("result", "failed");
        }

        return result;
    }

    @RequestMapping(value = "/delGuoku", method = RequestMethod.POST)
    @ApiOperation("国库删除有效接口")
    public Map<String, Object> delGuoku(@RequestBody JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            String state = pd.getString("state");
            if ("1".equals(state)) {
                guokuService.delGuokuParent(pd);
            }
            guokuService.delGuoku(pd);
            result.put("msg", "操作成功");
            result.put("result", "success");
        } catch (Exception e) {
            result.put("msg", e.getMessage());
            result.put("result", "failed");
        }
        return result;
    }


//  --   公共方法

    @RequestMapping(value = "/getBookOrgList", method = RequestMethod.POST)
    @ApiOperation("核算主体List")
    public Map<String, Object> getBookOrgList(@RequestBody JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        List<TreeNode> list = new ArrayList<>();
        String par = "0";
        try {
            PageData pd = this.getPageData(param);
            if (pd.getString("parentId").isEmpty() && (pd.getString("bookorgname").isEmpty() || pd.getString("bookorgname") == "")) {
                par = "1";
            } else if (pd.getString("parentId").isEmpty() && (!pd.getString("bookorgname").isEmpty() || pd.getString("bookorgname") != "")) {
                par = "2";
            } else if (!pd.getString("parentId").isEmpty()) {
                par = "0";
            }
            pd.put("par", par);
            list = guokuService.getBookOrgList(pd);
            result.put("msg", "操作成功");
            result.put("rows", list);
            result.put("result", "success");
        } catch (Exception e) {
            result.put("msg", e.getMessage());
            result.put("result", "failed");
        }
        return result;
    }

    @RequestMapping(value = "/getGuoKuTreeList", method = RequestMethod.POST)
    @ApiOperation("国库List")
    public Map<String, Object> getGuoKuTreeList(@RequestBody JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        List<GuoKuTreeNode> list = new ArrayList<>();
        String par = "0";
        try {
            PageData pd = this.getPageData(param);
            //            if (pd.getString("parentId").isEmpty() && (pd.getString("guoku_dscr").isEmpty() || pd.getString("guoku_dscr").equals(""))) {
//                par = "1";
//            } else if (pd.getString("parentId").isEmpty() && (!pd.getString("guoku_dscr").isEmpty() || !pd.getString("guoku_dscr").equals(""))) {
//                par = "2";
//            } else if (!pd.getString("parentId").isEmpty()) {
//                par = "0";
//            }
            if (StringUtils.isEmpty(pd.getString("parentId"))
                    && (StringUtils.isEmpty(pd.getString("guoku_dscr")) || "".equals(pd.getString("guoku_dscr")))) {
                par = "1";
            } else if (StringUtils.isEmpty(pd.getString("parentId")) && (!StringUtils.isEmpty(pd.getString("guoku_dscr")) || !"".equals(pd.getString("guoku_dscr")))) {
                par = "2";
            } else if (!StringUtils.isEmpty(pd.getString("parentId"))) {
                par = "0";
            }
            pd.put("par", par);
            list = guokuService.getGuoKuTreeList(pd);
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


//    //list添加子节点
//    private void getTreeList(List<QueTreeNode> treeList, List<Map<String, Object>> metaList, QueTreeNode temp) {
//        for(int i=0;i<metaList.size();i++){
//            String tempPid = metaList.get(i).get("pid").toString();
//            QueTreeNode tree = new QueTreeNode();
//            tree.setValue(metaList.get(i).get("id").toString());
//            tree.setKey(metaList.get(i).get("id").toString());
//            tree.setTitle(metaList.get(i).get("name").toString());
//            tree.setLabel(metaList.get(i).get("name").toString());
//            tree.setIsleaf(metaList.get(i).get("isleaf").toString());
//            if (temp == null ) {
//                if(oConvertUtils.isEmpty(tempPid)){
//                    treeList.add(tree);
//                    if (tree.getIsleaf().equals("0")) {
//                        getTreeList(treeList, metaList, tree);
//                    }
//                }
//            } else   if (temp != null && tempPid != null && tempPid.equals(temp.getValue())) {
//                temp.getChildren().add(tree);
//                if (tree.getIsleaf().equals("0")) {
//                    getTreeList(treeList, metaList, tree);
//                }
//            }
//
//        }
//    }


    //    //list添加子节点
//    private void getTreeList(List<TreeNode> treeList, List<TreeNode> metaList, TreeNode temp) {
//        for(int i=0;i<metaList.size();i++){
//            String tempPid = metaList.get(i).getParentId();
//            TreeNode tree = new TreeNode(metaList.get(i));
//            if (temp == null ) {
//                if(oConvertUtils.isEmpty(tempPid)){
//                    treeList.add(tree);
//                    if (tree.getIsleaf().equals("0")) {
//                        getTreeList(treeList, metaList, tree);
//                    }
//                }
//            } else   if (temp != null && tempPid != null && tempPid.equals(temp.getId())) {
//                temp.getChildren().add(tree);
//                if (tree.getIsleaf().equals("0")) {
//                    getTreeList(treeList, metaList, tree);
//                }
//            }
//
//        }
//    }
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
    @RequestMapping(value = "/getSubjectT" , method = RequestMethod.POST)
    @ApiOperation("获取T科目树")
    public Map<String, Object> getSubjectT(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> jsonMap = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            List<Map<String, Object>> data = guokuService.getSubjectT(pd);
            List<TreeNodeNews> treeNodeList = TreeFilterHeaper.definedTreeFilter(data);
            jsonMap.put("result", "success");
            jsonMap.put("rows", treeNodeList);
        } catch (Exception e) {
            jsonMap.put("result", "fail");
            jsonMap.put("msg", e.getMessage());
        }
        return jsonMap;
    }
}

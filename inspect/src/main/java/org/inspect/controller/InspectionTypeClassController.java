// InspectionTypeClassController.java

package org.inspect.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.inspect.model.TreeNode;
import org.inspect.service.InspectionTypeClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Created by Samer on 2019/11/8.
 */
@Slf4j
@RestController
@Api(tags = "检查分类类型")
@RequestMapping(value = "/inspectionTypeClass", produces = MediaType.APPLICATION_JSON_VALUE)
public class InspectionTypeClassController {

    @Autowired
    private InspectionTypeClassService inspectionTypeClassService;

    @PostMapping("/getInspectionTypeClassTree")
    @ApiOperation("获取检查分类树")
    public Map<String, Object> getInspectionTypeClassTree() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Map<String, Object>> data = inspectionTypeClassService.getInspectionTypeClass();
            List<TreeNode> treeNodeList = new ArrayList<>();
            getTypeClassTree(treeNodeList, data);
            result.put("msg", "操作成功");
            result.put("rows", treeNodeList);
            result.put("result", "success");
        } catch (Exception e) {
            result.put("msg", e.getMessage());
            result.put("result", "failed");
        }
        return result;
    }

    private void getTypeClassTree(
            List<TreeNode> treeNodeList,
            List<Map<String, Object>> data) {
        TreeNode pNode = null;
        TreeNode child = null;
        if (treeNodeList == null || treeNodeList.size() == 0) {
            child = new TreeNode();
            pNode = new TreeNode();
            child.setLabel(data.get(0).get("name").toString());
            child.setValue(data.get(0).get("id").toString());
            pNode.setLabel(data.get(0).get("pName").toString());
            pNode.setValue(data.get(0).get("pid").toString());
            pNode.addChild(child);
            treeNodeList.add(pNode);
        }
        if (data != null && data.size() > 0) {
            for (int i = 1, len = data.size(); i < len; i++) {
                child = new TreeNode();
                child.setLabel(data.get(i).get("name").toString());
                child.setValue(data.get(i).get("id").toString());
                if (!data.get(i).get("pName").equals(pNode.getLabel())) {
                    pNode = new TreeNode();
                    pNode.setLabel(data.get(i).get("pName").toString());
                    pNode.setValue(data.get(i).get("pid").toString());
                    treeNodeList.add(pNode);
                }
                pNode.addChild(child);
            }
        }
    }

} ///:~

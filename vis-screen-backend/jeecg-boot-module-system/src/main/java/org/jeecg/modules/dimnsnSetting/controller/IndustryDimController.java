// IndustryController.java

package org.jeecg.modules.dimnsnSetting.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.BaseController;
import org.jeecg.modules.dimnsnSetting.model.TreeNodeComm;
import org.jeecg.modules.dimnsnSetting.service.IndustryService;

import org.jeecg.modules.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Created by Samer on 2020/7/1.
 */
@Slf4j
@RestController
@Api(tags = "行业维度")
@RequestMapping(value = "/industryDim", produces = MediaType.APPLICATION_JSON_VALUE)
public class IndustryDimController extends BaseController {

    @Autowired
    private IndustryService industryService;

    @PostMapping(value = "/getIndustryTree")
    @ApiOperation("行业维度树形结构")
    public Map<String, Object> getIndustryTree(
            @RequestBody(required = false) JSONObject param
    ) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            List<Map<String, Object>> industryLv1 = industryService.getIndustryLv1(pd);
            List<Map<String, Object>> industryLv2 = industryService.getIndustryLv2(pd);
            List<TreeNodeComm> rootNodes = new ArrayList<>();
            buildIndustryTree(rootNodes, industryLv1, industryLv2);
            result.put("msg", "操作成功");
            result.put("rows", rootNodes);
            result.put("result", "success");
        } catch (Exception e) {
            result.put("msg", e.getMessage());
            result.put("result", "failed");
        }
        return result;
    }

    private static List<TreeNodeComm> buildIndustryTree(List<TreeNodeComm> rootNodes,
                                          List<Map<String, Object>> industryLv1,
                                          List<Map<String, Object>> industryLv2) {
        for (int i = 0; i < industryLv1.size(); i++) {
            TreeNodeComm nodeLv1 = new TreeNodeComm();
            Map<String, Object> TempNode = industryLv1.get(i);
            String id = (String) TempNode.get("id");
            nodeLv1.setValue((String) TempNode.get("id"));
            nodeLv1.setKey((String) TempNode.get("id"));
            nodeLv1.setLabel((String) TempNode.get("name"));
            nodeLv1.setTitle((String) TempNode.get("name"));
            for (int j = 0; j < industryLv2.size(); j++) {
                Map<String, Object> CNode = industryLv2.get(j);
                String pid = (String) CNode.get("pid");
                if( id.equals( pid ) ){
                    TreeNodeComm nodeLv2 = new TreeNodeComm();
                    nodeLv2.setValue((String) CNode.get("id"));
                    nodeLv2.setKey((String) CNode.get("id"));
                    nodeLv2.setLabel((String) CNode.get("name"));
                    nodeLv2.setTitle((String) CNode.get("name"));
                    nodeLv1.addChild(nodeLv2);
                }
            }
            rootNodes.add(nodeLv1);
        }
        return rootNodes;
    }

} ///:~

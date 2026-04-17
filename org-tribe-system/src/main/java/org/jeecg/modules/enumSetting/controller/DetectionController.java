package org.jeecg.modules.enumSetting.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.util.PageData;
import org.jeecg.modules.enumSetting.BaseController;
import org.jeecg.modules.enumSetting.service.DetectionService;
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
@Api(tags = "检测值")
@RequestMapping(value = "/DetectionController", produces = MediaType.APPLICATION_JSON_VALUE)
public class DetectionController extends BaseController {

    @Autowired
    private DetectionService detectionService;

    @RequestMapping(value = "/getData", method = RequestMethod.POST)
    @ApiOperation("获取检测值列表接口")
    public Map<String, Object> getData(@RequestBody JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            Integer pageNo = (Integer.parseInt(pd.getString("pageNo")) - 1) * Integer.parseInt(pd.getString("pageSize"));
            pd.put("page", pageNo);
            pd.put("rows", Integer.parseInt(pd.getString("pageSize")));
            List<Map<String, Object>> data = detectionService.getData(pd);
            if ("04".equals(pd.getString("DETECTION_TYPE_ID"))) { //将字符串改为数组，方便页面回显
                for (int i = 0; i < data.size(); i++) {
                    data.get(i).put("DETECTION_CONNECTOR", JSON.parseObject(data.get(i).get("DETECTION_CONNECTOR").toString(), ArrayList.class));
                }
            }
            Integer count = detectionService.getCount(pd);
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

    @RequestMapping(value = "/getDetectionType", method = RequestMethod.POST)
    @ApiOperation("获取检测类型接口")
    public Map<String, Object> getDetectionType(@RequestBody JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            List<Map<String, Object>> data = detectionService.getDetectionType(pd);
            result.put("rows", data);
            result.put("msg", "查询成功");
            result.put("result", "success");
        } catch (Exception e) {
            result.put("msg", e.getMessage());
            result.put("result", "success");
        }

        return result;
    }

    @RequestMapping(value = "/getDetectionTypeAll", method = RequestMethod.POST)
    @ApiOperation("根据检测类型获取检测值接口")
    public Map<String, Object> getDetectionTypeAll(@RequestBody JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            List<Map<String, Object>> data = detectionService.getDetectionTypeAll(pd);
            result.put("rows", data);
            result.put("msg", "查询成功");
            result.put("result", "success");
        } catch (Exception e) {
            result.put("msg", e.getMessage());
            result.put("result", "success");
        }
        return result;
    }

    @RequestMapping(value = "/addDetection", method = RequestMethod.POST)
    @ApiOperation("检测新增接口")
    public Map<String, Object> addDetection(@RequestBody JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        try {
            Integer rs = checkCode(pd);
            if (null != rs && rs > 0) {
                result.put("msg", "该数据已存在");
                result.put("result", "failed");
            } else {
                if ("04".equals(pd.getString("DETECTION_TYPE_ID"))) { //指标检测值
                    List<Map<String, Object>> list = JSON.parseObject(pd.getString("DETECTION_CONNECTOR"), ArrayList.class);
                    StringBuilder builder = new StringBuilder();
                    int size = 0;
                    for (Map<String, Object> map : list) {
                        size += 1;
                        builder.append(" ROUND(@VALUE/@UNIT,2) " + map.get("operation") + " " + map.get("operationNumber"));
                        if (size < list.size()) { //拼接条件连接符
                            builder.append(" " + map.get("Connector"));
                        }
                    }
                    pd.put("DETECTION_CONNECTOR", pd.getString("DETECTION_CONNECTOR"));
                    pd.put("DETECTION_CONDITIONS", builder.toString()); //组装检测条件
                }
/*               else {
                    String maxDetectionId = detectionService.getMaxDetectionId(pd);
                    if (StringUtils.isBlank(maxDetectionId)) {
                        pd.put("DETECTION_ID", "1");
                    } else {
                        pd.put("DETECTION_ID", String.valueOf(Integer.valueOf(maxDetectionId) + 1));
                    }
                 }*/
                detectionService.addDetection(pd);
                result.put("msg", "添加成功");
                result.put("result", "success");
            }
        } catch (
                Exception e) {
            result.put("msg", "添加失败");
            result.put("result", "failed");
        }
        return result;
    }

    @RequestMapping(value = "/editDetection", method = RequestMethod.POST)
    @ApiOperation("检测值修改接口")
    public Map<String, Object> editDetection(@RequestBody JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        try {
            if ("04".equals(pd.getString("DETECTION_TYPE_ID"))) { //指标检测值
                List<Map<String, Object>> list = JSON.parseObject(pd.getString("DETECTION_CONNECTOR"), ArrayList.class);
                StringBuilder builder = new StringBuilder();
                int size = 0;
                for (Map<String, Object> map : list) {
                    size += 1;
                    builder.append(" ROUND(@VALUE/@UNIT,2) " + map.get("operation") + " " + map.get("operationNumber"));
                    if (size < list.size()) { //拼接条件连接符
                        builder.append(" " + map.get("Connector"));
                    }
                }
                pd.put("DETECTION_CONNECTOR", pd.getString("DETECTION_CONNECTOR"));
                pd.put("DETECTION_CONDITIONS", builder.toString()); //组装检测条件
            }
            detectionService.editDetection(pd);
        } catch (Exception e) {
            result.put("msg", "修改失败");
            result.put("result", "failed");
        }
        result.put("msg", "修改成功");
        result.put("result", "success");
        return result;
    }

    @RequestMapping(value = "/delDetection", method = RequestMethod.POST)
    @ApiOperation("检测值删除有效接口")
    public Map<String, Object> delDetection(@RequestBody JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        try {
            detectionService.delDetection(pd);

        } catch (Exception e) {
            result.put("msg", "操作失败");
            result.put("result", "failed");
        }
        result.put("msg", "操作成功");
        result.put("result", "success");
        return result;
    }

    private Integer checkCode(PageData pd) {
        PageData pdName = new PageData();
        pdName.put("DETECTION_TYPE_ID", pd.getString("DETECTION_TYPE_ID"));
        pdName.put("DETECTION_ID", pd.getString("DETECTION_ID"));
        pdName.put("DETECTION_DSCR", pd.getString("DETECTION_DSCR"));
        List<Map<String, Object>> data = detectionService.checkCode(pdName);
        if (null != data && data.size() > 0) {
            return data.size();
        }
        return 0;
    }

}

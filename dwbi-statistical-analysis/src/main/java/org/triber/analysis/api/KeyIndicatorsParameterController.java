package org.triber.analysis.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.triber.analysis.service.KeyIndicatorsParameterService;
import org.triber.analysis.util.BaseController;
import org.triber.analysis.util.PageData;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author haojiang.
 * @Ddate 2020/9/29 15:36
 * @Description 重点指标参数维护
 */
@Slf4j
@RestController
@RequestMapping(value = "/keyIndicatorsParameter", produces = MediaType.APPLICATION_JSON_VALUE)
public class KeyIndicatorsParameterController extends BaseController {

    @Autowired
    private KeyIndicatorsParameterService parameterService;

    //获取重点指标参数
    @PostMapping("/getKeyIndicatorsParameter")
    public Map<String, Object>  getKeyIndicators() {
        Map<String, Object> map = new HashMap<>();
        try {
            PageData pageData = this.getPageData();
            Integer page = Integer.parseInt(pageData.getString("page"));//页码
            Integer rows = Integer.parseInt(pageData.getString("rows"));//行数
            pageData.put("page", (page - 1) * rows);

            map.put("total", parameterService.getKeyIndicatorsDataTotal(pageData));
            List<Map<String, String>> dataList = parameterService.getKeyIndicatorsData(pageData);
            map.put("rows", dataList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    //新增重点指标参数
    @PostMapping("/addKeyIndicatorsParameter")
    public Map<String, Object> addKeyIndicatorsParameter() {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pageData = this.getPageData();
            pageData.put("indexArray", Arrays.asList(pageData.getString("indexArray").split(",")));
            pageData.put("modifyDate", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            parameterService.updateKeyIndicatorsData(pageData);
            result.put("code", 200);
            result.put("msg", "新增成功!");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "新增失败!");
            e.printStackTrace();
        }
        return result;
    }

    //修改重点指标参数
    @PostMapping("/editKeyIndicatorsParameter")
    public Map<String, Object> editKeyIndicatorsParameter() {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pageData = this.getPageData();
            pageData.put("indexArray", Arrays.asList(pageData.getString("indexArray").split(",")));
            pageData.put("modifyDate", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            parameterService.updateKeyIndicatorsData(pageData);
            result.put("code", 200);
            result.put("msg", "修改成功!");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "修改失败!");
            e.printStackTrace();
        }
        return result;
    }

    //删除重点指标参数
    @PostMapping("/delKeyIndicatorsParameter")
    public Map<String, Object> delKeyIndicatorsParameter() {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pageData = this.getPageData();
            pageData.put("indexArray", Arrays.asList(pageData.getString("indexArray").split(",")));
            pageData.put("modifyDate", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            parameterService.updateKeyIndicatorsData(pageData);
            result.put("code", 200);
            result.put("msg", "删除成功!");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "删除失败!");
            e.printStackTrace();
        }
        return result;
    }
}

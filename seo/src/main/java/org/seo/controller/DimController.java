package org.seo.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.seo.BaseController;
import org.seo.config.DataSourceContextHolder;
import org.seo.service.DataSourceService;
import org.seo.service.DimensionService;
import org.seo.util.FileUpload;
import org.seo.util.ObjectExcelRead;
import org.seo.util.PageData;
import org.seo.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@Api(tags = "查询")
@RequestMapping(value = "/dimensionController", produces = MediaType.APPLICATION_JSON_VALUE)
public class DimController extends BaseController {

    @Autowired
    private DimensionService dimensionService;
    @Autowired
    private DataSourceService dataSourceService;

    private String getClickHouseDataSourceId() {
        PageData pd = new PageData();
        pd.put("TYPE", "Clickhouse");
        List<Map<String, Object>> result = dataSourceService.getDataSource(pd);
        if (null != result && result.size() > 0) {
            return String.valueOf(result.get(0).get("DATABASE_ID"));
        }
        return "";
    }

    @RequestMapping(value = {"/getMainPage"}, method = RequestMethod.POST)
    @ApiOperation("自定义维度信息(分页)")
    public Object getMainPage(@RequestBody(required = false) JSONObject param) {
        //设置数据源
        DataSourceContextHolder.setDBType("default");
        Map<String, Object> jsonMap = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            Integer pageNo = (Integer.parseInt(pd.getString("pageNo")) - 1) * Integer.parseInt(pd.getString("pageSize"));
            pd.put("page", pageNo);
            pd.put("rows", Integer.parseInt(pd.getString("pageSize")));
            List<Map<String, Object>> result = dimensionService.getMainPage(pd);
            Integer count = dimensionService.countMain(pd);
            jsonMap.put("total", count);//total键 存放总记录数，必须的
            jsonMap.put("rows", result);//rows键 存放每页记录 list
            jsonMap.put("result", "success");
        } catch (Exception e) {
            jsonMap.put("result", "false");
            jsonMap.put("msg", e.getMessage());
        }
        return jsonMap;
    }

    @RequestMapping(value = {"/getMainAll"}, method = RequestMethod.POST)
    @ApiOperation("自定义维度信息(不分页)")
    public Object getDataSource(@RequestBody(required = false) JSONObject param) {
        //设置数据源
        DataSourceContextHolder.setDBType("default");
        Map<String, Object> jsonMap = new HashMap<>();
        try {
            PageData pd = new PageData();
            jsonMap.put("rows", dimensionService.getMainAll(pd));
            jsonMap.put("result", "success");
        } catch (Exception e) {
            jsonMap.put("result", "false");
            jsonMap.put("msg", e.getMessage());
        }
        return jsonMap;
    }

    @RequestMapping(value = {"/delMain"}, method = RequestMethod.POST)
    @ApiOperation("删除维度信息")
    public Map<String, Object> delMain(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        result.put("msg", "删除成功！");
        result.put("result", "success");
        try {
            String id = pd.getString("id");
            if (null != id && !id.equals("")) {
                PageData subPd = new PageData();
                String ids[] = id.split(",");
                del(ids, pd, subPd);
            }
        } catch (Exception e) {
            result.put("msg", e.getMessage());
            result.put("result", "false");
        }
        return result;
    }

    private void del(String ids[], PageData pd, PageData subPd) {
        String sql = "";
        for (String i : ids) {
            sql = "delete FROM ods.seo_dimension_main where id = '" + i + "'";
            pd.put("sql", sql);
            dimensionService.delMain(pd, "default");
            sql = "ALTER TABLE ods.seo_dimension_main DELETE  where id = '" + i + "'";
            pd.put("sql", sql);
            dimensionService.delMain(pd, getClickHouseDataSourceId());

            sql = "delete FROM ods.seo_dimension_sub where main_id = '" + i + "'";
            pd.put("sql", sql);
            dimensionService.delSub(pd, "default");
            sql = "ALTER TABLE ods.seo_dimension_sub DELETE where main_id = '" + i + "'";
            pd.put("sql", sql);
            dimensionService.delSub(pd, getClickHouseDataSourceId());
        }
    }

    @RequestMapping(value = {"/addMain"}, method = RequestMethod.POST)
    @ApiOperation("新增")
    public Map<String, Object> addMain(@RequestBody(required = false) JSONObject param) {

        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        result.put("msg", "新增成功！");
        result.put("result", "success");
        try {

            List<Map<String, Object>> pds = dimensionService.checkMain(pd);
            if (null != pds && pds.size() > 0) {
                result.put("msg", "该信息已存在");
                result.put("result", "false");
                return result;
            }

            String id = get32UUID();
            pd.put("id", id);
            dimensionService.addMain(pd, "default");
            dimensionService.addMain(pd, getClickHouseDataSourceId());
        } catch (Exception e) {
            result.put("msg", e.getMessage());
            result.put("result", "false");
        }
        return result;
    }

    @RequestMapping(value = {"/editMain"}, method = RequestMethod.POST)
    @ApiOperation("修改")
    public Map<String, Object> editMain(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        result.put("msg", "修改成功！");
        result.put("result", "success");
        try {
            List<Map<String, Object>> pds = dimensionService.checkMain(pd);
            if (null != pds && pds.size() > 0) {
                result.put("msg", "该信息已存在");
                result.put("result", "false");
                return result;
            }
            String sql = "UPDATE ods.seo_dimension_main SET name = '" + pd.get("name") + "' where id = '" + pd.get("id") + "'";
            pd.put("sql", sql);
            dimensionService.editMain(pd, "default");
            sql = "ALTER TABLE ods.seo_dimension_main UPDATE  name = '" + pd.get("name") + "' where id = '" + pd.get("id") + "'";
            pd.put("sql", sql);
            dimensionService.editMain(pd, getClickHouseDataSourceId());
        } catch (Exception e) {
            result.put("msg", e.getMessage());
            result.put("result", "false");
        }
        return result;
    }

    @RequestMapping(value = {"/getSubPage"}, method = RequestMethod.POST)
    @ApiOperation("自定义维度信息明细(分页)")
    public Object getSubPage(@RequestBody(required = false) JSONObject param) {
        //设置数据源
        DataSourceContextHolder.setDBType("default");
        Map<String, Object> jsonMap = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            Integer pageNo = (Integer.parseInt(pd.getString("pageNo")) - 1) * Integer.parseInt(pd.getString("pageSize"));
            pd.put("page", pageNo);
            pd.put("rows", Integer.parseInt(pd.getString("pageSize")));
            List<Map<String, Object>> result = dimensionService.getSubPage(pd);
            Integer count = dimensionService.countSub(pd);
            jsonMap.put("total", count);//total键 存放总记录数，必须的
            jsonMap.put("rows", result);//rows键 存放每页记录 list
            jsonMap.put("result", "success");
        } catch (Exception e) {
            jsonMap.put("result", "false");
            jsonMap.put("msg", e.getMessage());
        }
        return jsonMap;
    }

    @RequestMapping(value = {"/getSubAll"}, method = RequestMethod.POST)
    @ApiOperation("自定义维度信息明细(不分页)")
    public Object getSubAll(@RequestBody(required = false) JSONObject param) {
        //设置数据源
        DataSourceContextHolder.setDBType("default");
        Map<String, Object> jsonMap = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            jsonMap.put("rows", dimensionService.getSubAll(pd));
            jsonMap.put("result", "success");
        } catch (Exception e) {
            jsonMap.put("result", "false");
            jsonMap.put("msg", e.getMessage());
        }
        return jsonMap;
    }

    @RequestMapping(value = {"/delSub"}, method = RequestMethod.POST)
    @ApiOperation("删除维度信息明细")
    public Map<String, Object> delSub(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        result.put("msg", "删除成功！");
        result.put("result", "success");
        try {
            String id = pd.getString("id");
            if (null != id && !id.equals("")) {
                String ids[] = id.split(",");
                delSub(ids, pd);
            }
        } catch (Exception e) {
            result.put("msg", e.getMessage());
            result.put("result", "false");
        }
        return result;
    }

    private void delSub(String ids[], PageData pd) {
        String sql = "";
        for (String i : ids) {
            sql = "delete FROM ods.seo_dimension_sub where id = '" + i + "'";
            pd.put("sql", sql);
            dimensionService.delSub(pd, "default");
            sql = "ALTER TABLE ods.seo_dimension_sub DELETE where id = '" + i + "'";
            pd.put("sql", sql);
            dimensionService.delSub(pd, getClickHouseDataSourceId());
        }
    }

    @RequestMapping(value = {"/addSub"}, method = RequestMethod.POST)
    @ApiOperation("新增")
    public Map<String, Object> addSub(@RequestBody(required = false) JSONObject param) {

        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        result.put("msg", "新增成功！");
        result.put("result", "success");
        try {
            List<Map<String, Object>> pds = dimensionService.checkSub(pd);
            if (null != pds && pds.size() > 0) {
                result.put("msg", "该信息已存在");
                result.put("result", "false");
                return result;
            }

            String id = get32UUID();
            pd.put("id", id);
            dimensionService.addSub(pd, "default");
            dimensionService.addSub(pd, getClickHouseDataSourceId());
        } catch (Exception e) {
            result.put("msg", e.getMessage());
            result.put("result", "false");
        }
        return result;
    }

    @RequestMapping(value = {"/editSub"}, method = RequestMethod.POST)
    @ApiOperation("修改")
    public Map<String, Object> editSub(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        result.put("msg", "修改成功！");
        result.put("result", "success");
        try {
            List<Map<String, Object>> pds = dimensionService.checkSub(pd);
            if (null != pds && pds.size() > 0) {
                result.put("msg", "该信息已存在");
                result.put("result", "false");
                return result;
            }

            String sql = "UPDATE ods.seo_dimension_sub SET name = '" + pd.get("name") + "',code = '" + pd.get("code") + "' where id = '" + pd.get("id") + "'";
            pd.put("sql", sql);
            dimensionService.editSub(pd, "default");
            sql = "ALTER TABLE ods.seo_dimension_sub UPDATE  name = '" + pd.get("name") + "',code = '" + pd.get("code") + "' where id = '" + pd.get("id") + "'";
            pd.put("sql", sql);
            dimensionService.editSub(pd, getClickHouseDataSourceId());
        } catch (Exception e) {
            result.put("msg", e.getMessage());
            result.put("result", "false");
        }
        return result;
    }

    /**
     * 从EXCEL导入到数据库
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/readExcel", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("导入接口")
    public Map<String, Object> readExcel(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "main_id", required = false) String main_id
    ) {
        Map<String, Object> map = new HashMap<String, Object>();
        PageData pd = new PageData();
        try {
            if (null != file && !file.isEmpty()) {
                String filePath = URLDecoder.decode(PathUtil.getClasspath(), "UTF-8") + "uploadFiles/seo/"; // 文件上传路径
                String fileName = file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf("."));
                fileName = FileUpload.fileUp(file, filePath, fileName); // 执行上传

                pd.put("main_id", main_id);

                // 2:从第2行开始；0:从第A列开始；0:第1个sheet
                List<PageData> listPd = (List) ObjectExcelRead.readExcel(filePath, fileName, 1, 0, 0); // 执行读EXCEL操作,读出的数据导入List
                /* 存入数据库操作====================================== */
                for (int i = 0; i < listPd.size(); i++) {
                    //编码
                    pd.put("code", listPd.get(i).get("var0"));
                    //名称
                    pd.put("name", listPd.get(i).get("var1"));

                    //设置数据源
                    DataSourceContextHolder.setDBType("default");
                    List<Map<String, Object>> pds = dimensionService.checkSub(pd);
                    if (null != pds && pds.size() > 0) {
                        continue;
                    }
                    pd.put("id", get32UUID());
                    dimensionService.addSub(pd, "default");
                    dimensionService.addSub(pd, getClickHouseDataSourceId());
                }
                map.put("result", "success");
                map.put("msg", "上传成功！");
            } else {
                map.put("result", "上传失败，请检查数据");
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("result", "上传失败，请检查数据");
            map.put("result", "false");
            return map;
        }
        return map;
    }
}

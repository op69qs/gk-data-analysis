package org.jeecg.modules.dimnsnSetting.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.BaseController;
import org.jeecg.modules.dimnsnSetting.model.SubjectImport;
import org.jeecg.modules.dimnsnSetting.service.SubjectImportService;

import org.jeecg.modules.util.*;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@Api(tags = "科目导入")
@RequestMapping(value = "/subjectImport", produces = MediaType.APPLICATION_JSON_VALUE)
public class SubjectImportController extends BaseController {

    @Autowired
    protected SubjectImportService subjectImport;


    @RequestMapping(value = "/getPage", method = RequestMethod.POST)
    @ApiOperation("列表")
    public Map<String, Object> getPage(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            Integer pageNo = (Integer.parseInt(pd.getString("pageNo")) - 1) * Integer.parseInt(pd.getString("pageSize"));
            pd.put("page", pageNo);
            pd.put("rows", Integer.parseInt(pd.getString("pageSize")));
            List<Map<String, Object>> data = subjectImport.getPage(pd);
            Integer count = subjectImport.getCount(pd);
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
            List<Map<String, Object>> data = subjectImport.getAll(pd);
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
            List<Map<String, Object>> maps = subjectImport.checkRepeat(pd);
            if (null != maps && maps.size() > 0) {
                result.put("msg", "添加失败,该年度" + pd.get("S_BDGSBTVSION") + "该科目" + pd.get("SUBJECT_CODE_4") + "已存在");
                result.put("result", "failed");
                return result;
            }
            pd.put("ADD_DATE", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD));
            subjectImport.add(pd);
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
            subjectImport.edit(pd);
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
            String SUBJECT_CODE_4S = pd.get("SUBJECT_CODE_4") + "";
            if (!"".equals(SUBJECT_CODE_4S)) {
                String[] SUBJECT_CODE_4 = SUBJECT_CODE_4S.split(",");
                for (String code : SUBJECT_CODE_4) {
                    pd.put("SUBJECT_CODE_4", code);
                    subjectImport.del(pd);
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
        List<SubjectImport> list = subjectImport.getExport(pd);
        mv.addObject(NormalExcelConstants.FILE_NAME, "科目信息");
        mv.addObject(NormalExcelConstants.CLASS, SubjectImport.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("科目信息", "科目信息"));
        mv.addObject(NormalExcelConstants.DATA_LIST, list);
        return mv;
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
            @RequestParam(value = "S_BDGSBTVSION", required = false) String S_BDGSBTVSION,
            @RequestParam(value = "ADD_USER", required = false) String ADD_USER
    ) {
        Map<String, Object> map = new HashMap<String, Object>();
        PageData pd = new PageData();
        try {
            if (null != file && !file.isEmpty()) {
                String filePath = URLDecoder.decode(PathUtil.getClasspath(), "UTF-8") + "uploadFiles/subject/"; // 文件上传路径
                String fileName = FileUpload.fileUp(file, filePath, "subject"); // 执行上传

                pd.put("S_BDGSBTVSION", S_BDGSBTVSION);
                List<Map<String, Object>> maps = subjectImport.getAll(pd);
                if (null != maps) {
                    subjectImport.del(pd);
                }
                pd.put("ADD_USER", ADD_USER);
                pd.put("ADD_DATE", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD));
                // 2:从第2行开始；0:从第A列开始；0:第1个sheet
//                List<PageData> listPd = (List) ObjectExcelRead.readExcel(filePath, fileName, 1, 0, 0); // 执行读EXCEL操作,读出的数据导入List
                File newFile = new File(filePath + fileName);
                List<String> list = CSVUtils.importCsv(newFile);
                /* 存入数据库操作====================================== */
                for (int i = 1; i < list.size(); i++) {
                    String data = list.get(i);
                    String[] datas = data.split(",");
                    if (null != datas && datas.length > 0 && datas[0].indexOf("核算主体代码") < 0) {
                        //预算科目代码
                        pd.put("SUBJECT_CODE_4", datas[1]);
                        //预算科目名称
                        pd.put("SUBJECT_DSCR_4", datas[2]);
                        //预算种类
                        pd.put("BUDGET_TYPE", datas[5]);
                        //调拨标志
                        pd.put("FLITTING_FLAG", datas[10]);
                        //统计科目编码
                        pd.put("STAT_CODE_4", datas[14]);
                        subjectImport.add(pd);
                    }
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

    /**
     * 从EXCEL导入到数据库
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/readExcelStat", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("导入接口")
    public Map<String, Object> readExcelStat(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "S_BDGSBTVSION", required = false) String S_BDGSBTVSION
    ) {
        Map<String, Object> map = new HashMap<String, Object>();
        PageData pd = new PageData();
        try {
            if (null != file && !file.isEmpty()) {
                String filePath = URLDecoder.decode(PathUtil.getClasspath(), "UTF-8") + "uploadFiles/subjectStat/"; // 文件上传路径
                String fileName = FileUpload.fileUp(file, filePath, "subjectStat"); // 执行上传

                pd.put("S_BDGSBTVSION", S_BDGSBTVSION);

                // 2:从第2行开始；0:从第A列开始；0:第1个sheet
                List<PageData> listPd = (List) ObjectExcelRead.readExcel(filePath, fileName, 1, 0, 0); // 执行读EXCEL操作,读出的数据导入List
                /* 存入数据库操作====================================== */
                for (int i = 0; i < listPd.size(); i++) {
                    //统计科目代码
                    pd.put("STAT_CODE_4", listPd.get(i).get("var0"));
                    //统计科目名称
                    pd.put("STAT_DSCR_4", listPd.get(i).get("var1"));
                    subjectImport.editStat(pd);
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

    /**
     * 从EXCEL导入到数据库
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/readExcelT", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("导入接口")
    public Map<String, Object> readExcelT(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "S_BDGSBTVSION", required = false) String S_BDGSBTVSION
    ) {
        Map<String, Object> map = new HashMap<String, Object>();
        PageData pd = new PageData();
        try {
            if (null != file && !file.isEmpty()) {
                String filePath = URLDecoder.decode(PathUtil.getClasspath(), "UTF-8") + "uploadFiles/subjectT/"; // 文件上传路径
                String fileName = FileUpload.fileUp(file, filePath, "subjectT"); // 执行上传

                pd.put("S_BDGSBTVSION", S_BDGSBTVSION);

                // 2:从第2行开始；0:从第A列开始；0:第1个sheet
                List<PageData> listPd = (List) ObjectExcelRead.readExcel(filePath, fileName, 1, 0, 0); // 执行读EXCEL操作,读出的数据导入List
                /* 存入数据库操作====================================== */
                for (int i = 0; i < listPd.size(); i++) {
                    //预算科目代码
                    pd.put("T_SUBJECT_CODE_3", listPd.get(i).get("var0"));
                    //T科目类别
                    pd.put("T_SUBJECT_TYPE", listPd.get(i).get("var1"));
                    //对应统计科目
                    pd.put("STAT_CODE_4", listPd.get(i).get("var2"));
                    subjectImport.editT(pd);
                }
                subjectImport.callProc(pd);
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

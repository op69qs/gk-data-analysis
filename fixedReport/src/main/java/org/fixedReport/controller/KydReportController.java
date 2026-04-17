package org.fixedReport.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.fixedReport.BaseController;
import org.fixedReport.model.TreeNode;
import org.fixedReport.service.KydReportService;
import org.fixedReport.util.ExcelUtil;
import org.fixedReport.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 凯盈达 数据
 */
@Slf4j
@RestController
@Api(tags = "凯盈达 分行业")
@RequestMapping(value = "/kydReportController", produces = MediaType.APPLICATION_JSON_VALUE)
public class KydReportController extends BaseController {
    
    @Autowired
    private KydReportService kydReportService;


    /***
     * 获取分行业
     */
    @RequestMapping(value = "/getIndustry", method = RequestMethod.POST)
    @ApiOperation("获取本月分行业列表")
    public Map<String, Object> getIndustry(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> jsonMap = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            Integer pageNo = (Integer.parseInt(pd.getString("pageNo")) - 1) * Integer.parseInt(pd.getString("pageSize"));
            pd.put("page", pageNo);
            pd.put("rows", Integer.parseInt(pd.getString("pageSize")));
            if(!"".equals(pd.get("PROCODE")) && pd.get("PROCODE") != null){
                String procode="'"+ pd.get("PROCODE").toString().replaceAll(",","','")+"'";
                pd.put("PROCODE", procode);
            }
            if(!"".equals(pd.get("S_TRECODE"))  && pd.get("S_TRECODE") != null){
                String sTrecode="'"+ pd.get("S_TRECODE").toString().replaceAll(",","','")+"'";
                pd.put("S_TRECODE", sTrecode);
            }
//            pd.put("tableName", "adm.trs_kyd_industry_"+pd.get("D_ACCT").toString().replace("-",""));
            pd.put("tableName", "adm.trs_kyd_industry");
            List<Map<String, Object>> result = kydReportService.getIndustryReportAll(pd);
            Integer count = kydReportService.countIndustryReportAll(pd);
            jsonMap.put("total", count);//total键 存放总记录数，必须的
            jsonMap.put("rows", result);
            jsonMap.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            jsonMap.put("result", "fail");
            jsonMap.put("msg", e.getMessage());

        }
        return jsonMap;
    }
    /**
      * 导出分行业列表
      */
    @RequestMapping(value = {"/excelIndustry"}, method = RequestMethod.POST)
    public void excelIndustry(HttpServletRequest request, HttpServletResponse response,
                              @RequestBody(required = false) JSONObject param) throws IOException {

        Map<String, Object> params = new HashMap<String, Object>();

        PageData pd = this.getPageData(param);
        if(!"".equals(pd.get("PROCODE")) && pd.get("PROCODE") != null){
            String procode="'"+ pd.get("PROCODE").toString().replaceAll(",","','")+"'";
            pd.put("PROCODE", procode);
        }
        if(!"".equals(pd.get("S_TRECODE"))  && pd.get("S_TRECODE") != null){
            String sTrecode="'"+ pd.get("S_TRECODE").toString().replaceAll(",","','")+"'";
            pd.put("S_TRECODE", sTrecode);
        }
//        pd.put("tableName", "adm.trs_kyd_industry_"+pd.get("D_ACCT").toString().replace("-",""));
        pd.put("tableName", "adm.trs_kyd_industry");
        List<Map<String, Object>> list = kydReportService.getIndustryReportAll(pd);
        String filename;
        String[] head0 = {"项目","税收收入合计","","国内增值税","","国内消费税","","企业所得税","","个人所得税","","资源税","","城市维护建设税","","房产税","","印花税",""
                ,"城镇土地使用税","","土地增值税","","车辆购置税","","车船税","","耕地占用税","","契税","","烟叶税","","环境保护税","","其他各税",""};
        String[] head1;
        if(pd.get("mark") != null && "0".equals(pd.get("mark")) ){
            filename = pd.get("D_ACCT")+"本期分行业";
            head1 = new String[]{"","本期", "同比(%)", "本期", "同比(%)", "本期", "同比(%)", "本期", "同比(%)", "本期", "同比(%)", "本期", "同比(%)", "本期", "同比(%)", "本期", "同比(%)", "本期"
                    , "同比(%)", "本期", "同比(%)"
                    , "本期", "同比(%)", "本期", "同比(%)", "本期", "同比(%)", "本期", "同比(%)", "本期", "同比(%)", "本期", "同比(%)", "本期", "同比(%)", "本期", "同比(%)"};
        }else{
            filename = pd.get("D_ACCT")+"累计分行业";
            head1 = new String[]{"","累计", "同比(%)", "累计", "同比(%)", "累计", "同比(%)", "累计", "同比(%)", "累计", "同比(%)", "累计", "同比(%)", "累计", "同比(%)", "累计", "同比(%)", "累计"
                    , "同比(%)", "累计", "同比(%)"
                    , "累计", "同比(%)", "累计", "同比(%)", "累计", "同比(%)", "累计", "同比(%)", "累计", "同比(%)", "累计", "同比(%)", "累计", "同比(%)", "累计", "同比(%)"};
        }
        String[] headnum0 = new String[] { "1,2,0,0", "1,1,1,2", "1,1,3,4", "1,1,5,6", "1,1,7,8", "1,1,9,10", "1,1,11,12", "1,1,13,14", "1,1,15,16", "1,1,17,18", "1,1,19,20"
                , "1,1,21,22", "1,1,23,24", "1,1,25,26", "1,1,27,28", "1,1,29,30", "1,1,31,32", "1,1,33,34", "1,1,35,36"};//对应excel中的行和列，下表从0开始{"开始行,结束行,开始列,结束列"}
        String[] headnum1 = new String[] {"3,3,0,0", "3,3,1,1","3,3,2,2", "3,3,3,3","3,3,4,4","3,3,5,5","3,3,6,6","3,3,7,7","3,3,8,8","3,3,9,9","3,3,10,10","3,3,11,11","3,3,12,12"
                ,"3,3,13,13","3,3,14,14","3,3,15,15","3,3,16,16","3,3,17,17","3,3,18,18","3,3,19,19","3,3,20,20","3,3,21,21","3,3,22,22","3,3,23,23","3,3,24,24","3,3,25,25"
                ,"3,3,26,26","3,3,27,27"
                ,"3,3,28,28","3,3,29,29","3,3,30,30","3,3,31,31","3,3,32,32","3,3,33,33","3,3,34,34","3,3,35,35","3,3,36,36"};
        String[] colName = {"PRONAME","F_AMT_101","F_AMT_101_year","F_AMT_1010101","F_AMT_1010101_year","F_AMT_1010201","F_AMT_1010201_year","F_AMT_10104","F_AMT_10104_year"
                ,"F_AMT_10106","F_AMT_10106_year","F_AMT_10107","F_AMT_10107_year","F_AMT_10109","F_AMT_10109_year","F_AMT_10110","F_AMT_10110_year","F_AMT_10111","F_AMT_10111_year"
                ,"F_AMT_10112","F_AMT_10112_year","F_AMT_10113","F_AMT_10113_year","F_AMT_10116","F_AMT_10116_year","F_AMT_10114","F_AMT_10114_year","F_AMT_10118","F_AMT_10118_year"
                ,"F_AMT_10119","F_AMT_10119_year","F_AMT_10120","F_AMT_10120_year","F_AMT_10121","F_AMT_10121_year","F_AMT_99999","F_AMT_99999_year"};
        int[] mergeIndex = {0,1};
        List<Map<String, Object>> headList = new ArrayList();
        Map<String, Object> headMap0 = new HashMap<String, Object>();
        headMap0.put("head", head0);
        headMap0.put("headnum", headnum0);
        headList.add(headMap0);

        Map<String, Object> headMap1 = new HashMap<String, Object>();
        headMap1.put("head", head1);
        headMap1.put("headnum", headnum1);
        headList.add(headMap1);
        try {
            ExcelUtil.reportMergeXls(request, response, list, filename, headList, colName, mergeIndex, false);//utils类需要用到的参数
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /***
     * 获取分企业
     */
    @RequestMapping(value = "/getEnterprise", method = RequestMethod.POST)
    @ApiOperation("获取本月分企业列表")
    public Map<String, Object> getEnterprise(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> jsonMap = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            Integer pageNo = (Integer.parseInt(pd.getString("pageNo")) - 1) * Integer.parseInt(pd.getString("pageSize"));
            pd.put("page", pageNo);
            pd.put("rows", Integer.parseInt(pd.getString("pageSize")));
            if(!"".equals(pd.get("PROCODE")) && pd.get("PROCODE") != null){
                String procode="'"+ pd.get("PROCODE").toString().replaceAll(",","','")+"'";
                pd.put("PROCODE", procode);
            }
            if(!"".equals(pd.get("S_TRECODE"))  && pd.get("S_TRECODE") != null){
                String sTrecode="'"+ pd.get("S_TRECODE").toString().replaceAll(",","','")+"'";
                pd.put("S_TRECODE", sTrecode);
            }
//            pd.put("tableName", "adm.trs_kyd_enterprise_"+pd.get("D_ACCT").toString().replace("-",""));
            pd.put("tableName", "adm.trs_kyd_enterprise");
            List<Map<String, Object>> result = kydReportService.getEnterpriseReportAll(pd);
            Integer count = kydReportService.countEnterpriseReportAll(pd);
            jsonMap.put("total", count);//total键 存放总记录数，必须的
            jsonMap.put("rows", result);
            jsonMap.put("result", "success");
        } catch (Exception e) {
            jsonMap.put("result", "fail");
            jsonMap.put("msg", e.getMessage());
        }
        return jsonMap;
    }

    /**
     * 导出分企业
     */
    @RequestMapping(value = {"/excelEnterprise"}, method = RequestMethod.POST)
    public void excelEnterprise(HttpServletRequest request, HttpServletResponse response,
                                @RequestBody(required = false) JSONObject param) throws IOException {

        Map<String, Object> params = new HashMap<String, Object>();
        PageData pd = this.getPageData(param);
        if(!"".equals(pd.get("PROCODE")) && pd.get("PROCODE") != null){
            String procode="'"+ pd.get("PROCODE").toString().replaceAll(",","','")+"'";
            pd.put("PROCODE", procode);
        }
        if(!"".equals(pd.get("S_TRECODE"))  && pd.get("S_TRECODE") != null){
            String sTrecode="'"+ pd.get("S_TRECODE").toString().replaceAll(",","','")+"'";
            pd.put("S_TRECODE", sTrecode);
        }
//        pd.put("tableName", "adm.trs_kyd_enterprise_"+pd.get("D_ACCT").toString().replace("-",""));
        pd.put("tableName", "adm.trs_kyd_enterprise");
        List<Map<String, Object>> list = kydReportService.getEnterpriseReportAll(pd);
        String filename;
        String[] head0 = {"项目","税收收入合计","","国内增值税","","国内消费税","","企业所得税","","个人所得税","","资源税","","城市维护建设税","","房产税","","印花税",""
                ,"城镇土地使用税","","土地增值税","","车辆购置税","","车船税","","耕地占用税","","契税","","烟叶税","","环境保护税","","其他各税",""};
        // 0 本期
        String[] head1;
        if(pd.get("mark") != null && "0".equals(pd.get("mark")) ){
            filename = pd.get("D_ACCT")+"本期分企业";
            head1 = new String[]{"","本期", "同比(%)", "本期", "同比(%)", "本期", "同比(%)", "本期", "同比(%)", "本期", "同比(%)", "本期", "同比(%)", "本期", "同比(%)", "本期", "同比(%)", "本期"
                    , "同比(%)", "本期", "同比(%)"
                    , "本期", "同比(%)", "本期", "同比(%)", "本期", "同比(%)", "本期", "同比(%)", "本期", "同比(%)", "本期", "同比(%)", "本期", "同比(%)", "本期", "同比(%)"};
        }else{
            filename = pd.get("D_ACCT")+"累计分企业";
            head1 = new String[]{"","累计", "同比(%)", "累计", "同比(%)", "累计", "同比(%)", "累计", "同比(%)", "累计", "同比(%)", "累计", "同比(%)", "累计", "同比(%)", "累计", "同比(%)", "累计"
                    , "同比(%)", "累计", "同比(%)"
                    , "累计", "同比(%)", "累计", "同比(%)", "累计", "同比(%)", "累计", "同比(%)", "累计", "同比(%)", "累计", "同比(%)", "累计", "同比(%)", "累计", "同比(%)"};
        }
        String[] headnum0 = new String[] { "1,2,0,0", "1,1,1,2", "1,1,3,4", "1,1,5,6", "1,1,7,8", "1,1,9,10", "1,1,11,12", "1,1,13,14", "1,1,15,16", "1,1,17,18", "1,1,19,20"
                , "1,1,21,22", "1,1,23,24", "1,1,25,26", "1,1,27,28", "1,1,29,30", "1,1,31,32", "1,1,33,34", "1,1,35,36"};//对应excel中的行和列，下表从0开始{"开始行,结束行,开始列,结束列"}
        String[] headnum1 = new String[] { "3,3,0,0", "3,3,1,1","3,3,2,2", "3,3,3,3","3,3,4,4","3,3,5,5","3,3,6,6","3,3,7,7","3,3,8,8","3,3,9,9","3,3,10,10","3,3,11,11","3,3,12,12"
                ,"3,3,13,13","3,3,14,14","3,3,15,15","3,3,16,16","3,3,17,17","3,3,18,18","3,3,19,19","3,3,20,20","3,3,21,21","3,3,22,22","3,3,23,23","3,3,24,24","3,3,25,25"
                ,"3,3,26,26","3,3,27,27"
                ,"3,3,28,28","3,3,29,29","3,3,30,30","3,3,31,31","3,3,32,32","3,3,33,33","3,3,34,34","3,3,35,35","3,3,36,36"};
        String[] colName = {"PRONAME","F_AMT_101","F_AMT_101_year","F_AMT_1010101","F_AMT_1010101_year","F_AMT_1010201","F_AMT_1010201_year","F_AMT_10104","F_AMT_10104_year"
                ,"F_AMT_10106","F_AMT_10106_year","F_AMT_10107","F_AMT_10107_year","F_AMT_10109","F_AMT_10109_year","F_AMT_10110","F_AMT_10110_year","F_AMT_10111","F_AMT_10111_year"
                ,"F_AMT_10112","F_AMT_10112_year","F_AMT_10113","F_AMT_10113_year","F_AMT_10116","F_AMT_10116_year","F_AMT_10114","F_AMT_10114_year","F_AMT_10118","F_AMT_10118_year"
                ,"F_AMT_10119","F_AMT_10119_year","F_AMT_10120","F_AMT_10120_year","F_AMT_10121","F_AMT_10121_year","F_AMT_99999","F_AMT_99999_year"};
        int[] mergeIndex = {0,1};
        List<Map<String, Object>> headList = new ArrayList();
        Map<String, Object> headMap0 = new HashMap<String, Object>();
        headMap0.put("head", head0);
        headMap0.put("headnum", headnum0);
        headList.add(headMap0);

        Map<String, Object> headMap1 = new HashMap<String, Object>();
        headMap1.put("head", head1);
        headMap1.put("headnum", headnum1);
        headList.add(headMap1);
        try {
            ExcelUtil.reportMergeXls(request, response, list, filename, headList, colName, mergeIndex, false);//utils类需要用到的参数
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /***
     * 获取企业排名
     */
    @RequestMapping(value = "/getEnterpriseRanking", method = RequestMethod.POST)
    @ApiOperation("获取企业排名")
    public Map<String, Object> getEnterpriseRanking(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> jsonMap = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            Integer pageNo = (Integer.parseInt(pd.getString("pageNo")) - 1) * Integer.parseInt(pd.getString("pageSize"));
            pd.put("page", pageNo);
            pd.put("rows", Integer.parseInt(pd.getString("pageSize")));
            if(!"".equals(pd.get("S_TRECODE"))  && pd.get("S_TRECODE") != null){
                String sTrecode="'"+ pd.get("S_TRECODE").toString().replaceAll(",","','")+"'";
                pd.put("S_TRECODE", sTrecode);
            }
            if(!"".equals(pd.get("industryId"))  && pd.get("industryId") != null){
//                String industryId="'"+ pd.get("industryId").toString().replaceAll(",","','")+"'";
//                String industryId="'"+ pd.get("industryId").toString().replaceAll(",","")+"'";
                String industryId="["+ pd.get("industryId").toString().replaceAll("\"","'")+"]";
                List<Map<String,String>> listObjectFir = (List<Map<String,String>>) JSONArray.parse(industryId);
                pd.put("industryId", listObjectFir.get(0).get("value"));
                pd.put("industryName",listObjectFir.get(0).get("label"));
//                pd.put("industryId", industryId);
//                pd.put("industryName", pd.get("industryName"));
                pd.put("industryType", true);
            }else{
                pd.put("industryName", "");
                pd.put("industryType", false);
            }
//            pd.put("tableName", "adm.trs_kyd_enterprise_rank_"+pd.get("D_ACCT").toString().replace("-",""));
            pd.put("tableName", "adm.trs_kyd_enterprise_rank");
            List<Map<String, Object>> result = kydReportService.getEnterpriseRankingReportAll(pd);
            Integer count = kydReportService.countEnterpriseRankingReportAll(pd);
            jsonMap.put("total", count);//total键 存放总记录数，必须的
            jsonMap.put("rows", result);
            jsonMap.put("result", "success");
        } catch (Exception e) {
            jsonMap.put("result", "fail");
            jsonMap.put("msg", e.getMessage());
        }
        return jsonMap;
    }

    /**
     * 导出企业排名
     */
    @RequestMapping(value = {"/excelEnterpriseRanking"}, method = RequestMethod.POST)
    public void excelEnterpriseRanking(HttpServletRequest request, HttpServletResponse response,
                                       @RequestBody(required = false) JSONObject param) throws IOException {

        Map<String, Object> params = new HashMap<String, Object>();

        PageData pd = this.getPageData(param);
        if(!"".equals(pd.get("S_TRECODE"))   && pd.get("S_TRECODE") != null){
            String sTrecode="'"+ pd.get("S_TRECODE").toString().replaceAll(",","','")+"'";
            pd.put("S_TRECODE", sTrecode);
        }
        if(!"".equals(pd.get("industryId"))  && pd.get("industryId") != null){
//            String industryId="'"+ pd.get("industryId").toString().replaceAll(",","','")+"'";
//            String industryId="'"+ pd.get("industryId").toString().replaceAll(",","")+"'";
//            pd.put("industryId", industryId);
//            pd.put("industryName", pd.get("industryName"));
            String industryId="["+ pd.get("industryId").toString().replaceAll("\"","'")+"]";
            List<Map<String,String>> listObjectFir = (List<Map<String,String>>) JSONArray.parse(industryId);
            pd.put("industryId", listObjectFir.get(0).get("value"));
            pd.put("industryName",listObjectFir.get(0).get("label"));
            pd.put("industryType", true);
        }else{
            pd.put("industryName", "");
            pd.put("industryType", false);
        }
//        pd.put("tableName", "adm.trs_kyd_enterprise_rank_"+pd.get("D_ACCT").toString().replace("-",""));
        pd.put("tableName", "adm.trs_kyd_enterprise_rank");
        List<Map<String, Object>> list = kydReportService.getEnterpriseRankingReportAll(pd);
        String filename = pd.get("D_ACCT")+"  企业排名";
        String[] head0 = {"排名","企业名称","累计纳税金额","同比","所属行业","所属国库"};
        String[] headnum0 = new String[] { "1,1,0,0", "1,1,1,1", "1,1,2,2", "1,1,3,3", "1,1,4,4", "1,1,5,5"};//对应excel中的行和列，下表从0开始{"开始行,结束行,开始列,结束列"}
        String[] colName = {"rank","PRONAME","F_AMT_year","F_AMT_year_tb","industry","s_tredscr"};
        int[] mergeIndex = {0,1};
        List<Map<String, Object>> headList = new ArrayList();
        Map<String, Object> headMap0 = new HashMap<String, Object>();
        headMap0.put("head", head0);
        headMap0.put("headnum", headnum0);
        headList.add(headMap0);

        try {
            ExcelUtil.reportMergeXls(request, response, list, filename, headList, colName, mergeIndex, false);//utils类需要用到的参数
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /***
     * 获取行业 下拉
     */
    @RequestMapping(value = "/getIndustryDrop", method = RequestMethod.POST)
    @ApiOperation("获取行业 下拉")
    public Map<String, Object> getIndustryDrop(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> jsonMap = new HashMap<>();
        try {
            List<Map<String, Object>> result = kydReportService.getIndustryDrop();

            List<TreeNode> treeNodeList = definedTreeFilter(result);
            jsonMap.put("rows", treeNodeList);
            jsonMap.put("result", "success");
        } catch (Exception e) {
            jsonMap.put("result", "fail");
            jsonMap.put("msg", e.getMessage());
        }
        return jsonMap;
    }

    /**
     * 处理Tree数据结构
     *
     * @param data
     */
    private List<TreeNode> definedTreeFilter(List<Map<String, Object>> data) {

        String idFiled = "id",
                nameFiled = "name",
                parentField = "pId";

        List<TreeNode> treeData = new ArrayList<>();
        Map<String, TreeNode>  tmpMap = new HashMap<>();

        for (int i = 0, l = data.size(); i < l; i++) {
            TreeNode tempNode = new TreeNode();
            // 主键
            tempNode.setId(String.valueOf(data.get(i).get(idFiled)));
            tempNode.setKey(String.valueOf(data.get(i).get(idFiled)));
            tempNode.setValue(String.valueOf(data.get(i).get(idFiled)));

            //描述
            tempNode.setTitle(String.valueOf(data.get(i).get(nameFiled)));
            tempNode.setLabel(String.valueOf(data.get(i).get(nameFiled)));
            tempNode.setParentId(String.valueOf(data.get(i).get(parentField)));
            tmpMap.put(String.valueOf(data.get(i).get(idFiled)), tempNode);
        }

        for (int i = 0, l = data.size(); i < l; i++) {
            if ( !StringUtils.isEmpty(tmpMap.get(String.valueOf(data.get(i).get(parentField))))
                    &&
                    !( String.valueOf(data.get(i).get(idFiled))).equals( String.valueOf(data.get(i).get(parentField)))
            ) {
                if (CollectionUtils.isEmpty(tmpMap.get(String.valueOf(data.get(i).get(parentField))).getChildren()))
                    tmpMap.get(String.valueOf(data.get(i).get(parentField))).setChildren( new ArrayList() );
                tmpMap.get(String.valueOf(data.get(i).get(parentField))).addChild( tmpMap.get(String.valueOf(data.get(i).get(idFiled))));
            } else {
                treeData.add(tmpMap.get(String.valueOf(data.get(i).get(idFiled))));
            }
        }
        return treeData;
    }

}

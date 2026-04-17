package org.fixedReport.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.fixedReport.BaseController;
import org.fixedReport.model.TreeNodeArea;
import org.fixedReport.service.CentralizedPaymentService;
import org.fixedReport.service.NewsFlashService;
import org.fixedReport.service.ReportService;
import org.fixedReport.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 国库二期月度报告
 *
 * @author Created by dj on 2020/05/11.
 */
@Slf4j
@RestController
@Api(tags = "国库集中支付")
@RequestMapping(value = "/centralizedPaymentController", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReportCenterController extends BaseController {

    @Value("${TEMPLATE_FILE_PATH}")
    private String saveDir;

    @Autowired
    private CentralizedPaymentService centralizedPaymentService;


    /***
     * 获取所有报告列表
     */
    @RequestMapping(value = "/reportDate", method = RequestMethod.POST)
    @ApiOperation("获取集中支付业务明细查询数据")
    public Map<String, Object> reportDate(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> jsonMap = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            if (pd.size() == 0) { //页面初始化查询
                jsonMap.put("total", 0);
                jsonMap.put("rows", new ArrayList<>());
                return jsonMap;
            }
            Integer page = Integer.parseInt(pd.getString("pageNo"));//页码
            Integer rows = Integer.parseInt(pd.getString("pageSize"));//行数
            String  queryTotal="";

            //      执行SQL语句

            String query1=" SELECT\n" +
                    "        S_SEQNO,\n" +
                    "        S_ID,\n" +
                    "        S_ADMDIVCODE,\n" +
                    "        S_STYEAR,\n" +
                    "        S_BOOKORGCODE,\n" +
                    "        t.guoku_dscr AS S_TRENAME,\n" +
                    "        S_TRECODE,\n" +
                    "    CONCAT(SUBSTR(S_ENTRUSTDATE,1,4),'-',SUBSTR(S_ENTRUSTDATE,5,2),'-',SUBSTR(S_ENTRUSTDATE,7,2)) AS  S_ENTRUSTDATE_1,\n" +
                    "       case  S_AGENTBANKCLASS  WHEN '1' THEN '工商银行'   WHEN '2' THEN '农业银行'" +
                    "    WHEN '3' THEN '中国银行'   WHEN '4' THEN '建设银行' WHEN '5' THEN '交通银行'   WHEN '6' THEN '光大银行'" +
                    "    WHEN '7' THEN '中信银行'   WHEN '8' THEN '平安银行' WHEN '9' THEN '民生银行'   WHEN '10' THEN '兴业银行'" +
                    "    WHEN '11' THEN '重庆农商行'   WHEN '12' THEN '重庆银行' WHEN '13' THEN '重庆三峡银行'   WHEN '14' THEN '邮储银行'" +
                    "    WHEN '15' THEN '村镇银行'   ELSE  '其他银行' END AS S_AGENTBANKCLASS_1, " +
                    "        S_AGENTBANKNO,\n" +
                    "        S_AGENTBANKNAME,\n" +
                    "        S_PAYOUTVOUTYPE,\n" +
                    "        CASE S_PAYOUTVOUTYPE WHEN '0' THEN '无纸' WHEN '1' THEN '有纸' ELSE '其他' END AS S_PAYOUTVOUNAME,\n" +
                    "        S_PAYMODE,\n" +
                    "        CASE S_PAYMODE WHEN '1' THEN '直接支付' WHEN '2' THEN '授权转账' WHEN '3' THEN '授权现金'   ELSE '其他' END AS S_PAYMODENAME,\n" +
                    "        D_PAYVOUDATE,\n" +
                    "        S_VOUCHERNO  AS S_VOUCHERNO_1,\n" +
                    "        S_FUNDTYPECODE,\n" +
                    "        CASE S_FUNDTYPECODE WHEN '1' THEN '预算内' WHEN '2' THEN '预算外' ELSE '其他' END AS S_FUNDTYPENAME,\n" +
                    "        S_BDGORGCODE as S_BDGORGCODE_1,\n" +
                    "        S_BDGORGNAME as S_BDGORGNAME_1,\n" +
                    "        S_EXPFUNCCODE as S_EXPFUNCCODE_1,\n" +
                    "        S_EXPFUNCNAME  as S_EXPFUNCNAME_1,\n" +
                    "        S_EXPECOCODE,\n" +
                    "        S_EXPECONAME,\n" +
                    "        S_PROJECTTYPECODE,\n" +
                    "        S_PROJECTTYPENAME,\n" +
                    "        S_ZEROACCTNO,\n" +
                    "        S_ZEROACCTNAME,\n" +
                    "        S_ZEROOPNBNKNAME,\n" +
                    "        S_PAYEEACCTNO,\n" +
                    "        S_PAYEEACCTNAME,\n" +
                    "        S_PAYEEOPNBNKNO,\n" +
                    "        S_PAYEEOPNBNKNAME,\n" +
                    "        S_CLEARACCTNO,\n" +
                    "        S_CLEARACCTNAME,\n" +
                    "        S_CLEARBANKNO,\n" +
                    "        S_CLEARBANKNAME,\n" +
                    "        S_REMARK,\n" +
                    "        round(toFloat32(toFloat32(F_PAYAMT) / "+pd.getString("S_AMTUNIT")+"), 2) as F_PAYAMT,\n" +
                    "        C_AUTOAUDITSTATE,\n" +
                    "        CASE C_AUTOAUDITSTATE WHEN '1' THEN '审核通过' WHEN '2' THEN '审核不通过' WHEN '3' THEN '待审核' ELSE '其他' END AS C_AUTOAUDITNAME,\n" +
                    "        T_AUTOAUDITTIME,\n" +
                    "        S_AUDITREASON,\n" +
                    "        C_HANDAUDITSTATE,\n" +
                    "        CASE C_HANDAUDITSTATE WHEN '1' THEN '审核通过' WHEN '2' THEN '审核不通过' WHEN '3' THEN '待审核' ELSE '其他' END AS C_HANDAUDITNAME,\n" +
                    "        T_HANDAUDITTIME,\n" +
                    "        S_HANDREASON,\n" +
                    "        C_HANDAUDITFLAG,\n" +
                    "        C_ISDEDPLAN,\n" +
                    "        C_CHECKRESULT,\n" +
                    "        S_HOLD1,\n" +
                    "        S_HOLD2,\n" +
                    "        S_HOLD3,\n" +
                    "        S_HOLD4,\n" +
                    "        TS_SYSUPDATE\n" +
                    "        FROM\n" +
                    "        adm.trs_stat_agentbankpay_detail LEFT JOIN dmcode.cm_guoku_dimnsn t ON S_TRECODE = t.guoku_id\n" +
                    "        WHERE\n" +
                    "        concat('1','') = concat('1','')";
            if(!"".equals(pd.containsKey("S_TRECODE")? pd.get("S_TRECODE"):"")){
                query1+=" AND S_TRECODE in (concat('"+pd.getString("S_TRECODE")+"',''))";
            }
            if(!"".equals(pd.containsKey("S_STARTTIME")? pd.get("S_STARTTIME"):"")){
                query1+=" AND toString(S_ENTRUSTDATE)  >= toString(REPLACE('"+pd.getString("S_STARTTIME")+"','-','')) ";
//                query1+="toFloat32(S_ENTRUSTDATE) BETWEEN   toFloat32(REPLACE('2020-02-02','-',''))   AND toFloat32(REPLACE('2020-12-02','-',''))"
            }
            if(!"".equals(pd.containsKey("S_ENDTIME")? pd.get("S_ENDTIME"):"")){
//                query1+=" AND date_format(S_ENTRUSTDATE, '%Y-%m-%d') <= date_format('"+pd.getString("S_ENDTIME")+"', '%Y-%m-%d')";
                query1+=" AND toString(S_ENTRUSTDATE)  <= toString(REPLACE('"+pd.getString("S_ENDTIME")+"','-','')) ";
            }
            if(!"".equals(pd.containsKey("S_PAYMODE")? pd.get("S_PAYMODE"):"")){
                query1+=" AND S_PAYMODE ='"+pd.getString("S_PAYMODE")+"'";
            }
            if(!"".equals(pd.containsKey("S_AGENTBANKCLASS")? pd.get("S_AGENTBANKCLASS"):"")){
                query1+=" AND toString(S_AGENTBANKCLASS) =toString('"+pd.getString("S_AGENTBANKCLASS")+"')";
            }
            if(!"".equals(pd.containsKey("S_VOUCHERNO")? pd.get("S_VOUCHERNO"):"")){
                query1+=" AND S_VOUCHERNO like '%"+pd.getString("S_VOUCHERNO")+"%'";
            }
            if(!"".equals(pd.containsKey("S_BDGORGCODE")? pd.get("S_BDGORGCODE"):"")){
                query1+=" AND S_BDGORGCODE ='"+pd.getString("S_BDGORGCODE")+"'";
            }
            if(!"".equals(pd.containsKey("S_BDGORGNAME")? pd.get("S_BDGORGNAME"):"")){
                query1+=" AND S_BDGORGNAME ='"+pd.getString("S_BDGORGNAME")+"'";
            }
            if(!"".equals(pd.containsKey("S_PAYEEACCTNO")? pd.get("S_PAYEEACCTNO"):"")){
                query1+=" AND S_PAYEEACCTNO like '%"+pd.getString("S_PAYEEACCTNO")+"%'";
            }
            if(!"".equals(pd.containsKey("S_PAYEEACCTNAME")? pd.get("S_PAYEEACCTNAME"):"")){
                query1+=" AND S_PAYEEACCTNAME like '%"+pd.getString("S_PAYEEACCTNAME")+"%'";
            }
            if(!"".equals(pd.containsKey("S_EXPFUNCCODE")? pd.get("S_EXPFUNCCODE"):"")){
//                query1+=" AND S_EXPFUNCCODE in ("+pd.getString("S_EXPFUNCCODE")+")";
                query1+=" AND S_EXPFUNCCODE in ("+pd.getString("S_EXPFUNCCODE").replace("[","").replace("]","").replace("\"","'")+")";
            }
//            if(!"".equals(pd.containsKey("S_EXPFUNCNAME")? pd.get("S_EXPFUNCNAME"):"")){
//                query1+=" AND S_EXPFUNCNAME ='"+pd.getString("S_EXPFUNCNAME")+"'";
//            }
            if(!"".equals(pd.containsKey("S_REMARK")? pd.get("S_REMARK"):"")){
                query1+=" AND S_REMARK like '%"+pd.getString("S_REMARK")+"%'";
            }
            if(!"".equals(pd.containsKey("S_AUDITREASON")? pd.get("S_AUDITREASON"):"")){
                query1+=" AND S_AUDITREASON ='"+pd.getString("S_AUDITREASON")+"'";
            }
            if(!"".equals(pd.containsKey("C_AUTOAUDITSTATE")? pd.get("C_AUTOAUDITSTATE"):"")){
                query1+=" AND C_AUTOAUDITSTATE ='"+pd.getString("C_AUTOAUDITSTATE")+"'";
            }
            if(!"".equals(pd.containsKey("C_HANDAUDITSTATE")? pd.get("C_HANDAUDITSTATE"):"")){
                query1+=" AND C_HANDAUDITSTATE ='"+pd.getString("C_HANDAUDITSTATE")+"'";
            }

            //查询总条数
//            List<Map<String, Object>> dataList = this.getSQLResults(query1); //comprehensiveQueryService.getComprehensiveQueryData(pageData);
//           System.out.println(query1+"----------------------------------");
              queryTotal=" SELECT count(1)" +
                    "        FROM  " +
                    "        adm.trs_stat_agentbankpay_detail  " +
                    "        WHERE " +
                    "        concat('1','') = concat('1','')";
            if(!"".equals(pd.containsKey("S_TRECODE")? pd.get("S_TRECODE"):"")){
                queryTotal+=" AND S_TRECODE in (concat('"+pd.getString("S_TRECODE")+"',''))";
            }
            if(!"".equals(pd.containsKey("S_STARTTIME")? pd.get("S_STARTTIME"):"")){
                queryTotal+=" AND toString(S_ENTRUSTDATE)  >= toString(REPLACE('"+pd.getString("S_STARTTIME")+"','-','')) ";
            }
            if(!"".equals(pd.containsKey("S_ENDTIME")? pd.get("S_ENDTIME"):"")){
                queryTotal+=" AND toString(S_ENTRUSTDATE)  <= toString(REPLACE('"+pd.getString("S_ENDTIME")+"','-','')) ";
            }
            if(!"".equals(pd.containsKey("S_PAYMODE")? pd.get("S_PAYMODE"):"")){
                queryTotal+=" AND S_PAYMODE ='"+pd.getString("S_PAYMODE")+"'";
            }
            if(!"".equals(pd.containsKey("S_AGENTBANKCLASS")? pd.get("S_AGENTBANKCLASS"):"")){
                queryTotal+=" AND toString(S_AGENTBANKCLASS) =toString('"+pd.getString("S_AGENTBANKCLASS")+"')";
            }

            if(!"".equals(pd.containsKey("S_VOUCHERNO")? pd.get("S_VOUCHERNO"):"")){
                queryTotal+=" AND S_VOUCHERNO like '%"+pd.getString("S_VOUCHERNO")+"%'";
            }
            if(!"".equals(pd.containsKey("S_BDGORGCODE")? pd.get("S_BDGORGCODE"):"")){
                queryTotal+=" AND S_BDGORGCODE  like '%"+pd.getString("S_BDGORGCODE")+"%'";
            }
            if(!"".equals(pd.containsKey("S_BDGORGNAME")? pd.get("S_BDGORGNAME"):"")){
                queryTotal+=" AND S_BDGORGNAME   like '%"+pd.getString("S_BDGORGNAME")+"%'";
            }
            if(!"".equals(pd.containsKey("S_PAYEEACCTNO")? pd.get("S_PAYEEACCTNO"):"")){
                queryTotal+=" AND S_PAYEEACCTNO like '%"+pd.getString("S_PAYEEACCTNO")+"%'";
            }
            if(!"".equals(pd.containsKey("S_PAYEEACCTNAME")? pd.get("S_PAYEEACCTNAME"):"")){
                queryTotal+=" AND S_PAYEEACCTNAME like '%"+pd.getString("S_PAYEEACCTNAME")+"%'";
            }
            if(!"".equals(pd.containsKey("S_EXPFUNCCODE")? pd.get("S_EXPFUNCCODE"):"")){
                queryTotal+=" AND S_EXPFUNCCODE in ("+pd.getString("S_EXPFUNCCODE").replace("[","").replace("]","").replace("\"","'")+")";
            }
//            if(!"".equals(pd.containsKey("S_EXPFUNCNAME")? pd.get("S_EXPFUNCNAME"):"")){
//                queryTotal+=" AND S_EXPFUNCNAME ='"+pd.getString("S_EXPFUNCNAME")+"'";
//            }
            if(!"".equals(pd.containsKey("S_REMARK")? pd.get("S_REMARK"):"")){
                queryTotal+=" AND S_REMARK like '%"+pd.getString("S_REMARK")+"%'";
            }
            if(!"".equals(pd.containsKey("S_AUDITREASON")? pd.get("S_AUDITREASON"):"")){
                queryTotal+=" AND S_AUDITREASON ='"+pd.getString("S_AUDITREASON")+"'";
            }
            if(!"".equals(pd.containsKey("C_AUTOAUDITSTATE")? pd.get("C_AUTOAUDITSTATE"):"")){
                queryTotal+=" AND C_AUTOAUDITSTATE ='"+pd.getString("C_AUTOAUDITSTATE")+"'";
            }
            if(!"".equals(pd.containsKey("C_HANDAUDITSTATE")? pd.get("C_HANDAUDITSTATE"):"")){
                queryTotal+=" AND C_HANDAUDITSTATE ='"+pd.getString("C_HANDAUDITSTATE")+"'";
            }
            //按分页查询
            List<Map<String, Object>> dataListTotal ;
            List<Map<String, Object>> dataList ;
            Integer pageNo= page>2? (page - 1) * rows:0;
            Integer rowsNo= page>2? page * rows:10;
            String dataPage= " limit "+ pageNo + "," + rowsNo;
            query1+=dataPage;
            System.out.println(query1);
            dataList = this.getSQLResults(query1); //comprehensiveQueryService.getComprehensiveQueryData(pageData);
            dataListTotal = this.getSQLResults(queryTotal);
            Integer total=Integer.parseInt(dataListTotal.get(0).get("count(1)").toString());
            jsonMap.put("rows", dataList);
            jsonMap.put("total", total);
            jsonMap.put("result", "success");
        } catch (Exception e) {
            jsonMap.put("result", "fail");
            jsonMap.put("msg", e.getMessage());
        }
        return jsonMap;
    }

    /***
     * 获取所有报告列表
     */
    @RequestMapping(value = "/getCountAndSum", method = RequestMethod.POST)
    @ApiOperation("获取集中支付业务明细sum汇总")
    public Map<String, Object> getCountAndSum(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> jsonMap = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            if (pd.size() == 0) { //页面初始化查询
                jsonMap.put("total", 0);
                jsonMap.put("rows", new ArrayList<>());
                return jsonMap;
            }

            //      执行SQL语句

            String query1=" select  round(IFNULL(sum(F_PAYAMT),0)/"+pd.getString("S_AMTUNIT")+",2)"+
                    "        adm.trs_stat_agentbankpay_detail WHERE  1 = 1";
            if(!"".equals(pd.containsKey("S_TRECODE")? pd.get("S_TRECODE"):"")){
                query1+=" AND S_TRECODE in (concat('"+pd.getString("S_TRECODE")+"',''))";
            }
            if(!"".equals(pd.containsKey("S_STARTTIME")? pd.get("S_STARTTIME"):"")){
                query1+=" AND toString(S_ENTRUSTDATE)  >= toString(REPLACE('"+pd.getString("S_STARTTIME")+"','-','')) ";
//                query1+="toFloat32(S_ENTRUSTDATE) BETWEEN   toFloat32(REPLACE('2020-02-02','-',''))   AND toFloat32(REPLACE('2020-12-02','-',''))"
            }
            if(!"".equals(pd.containsKey("S_ENDTIME")? pd.get("S_ENDTIME"):"")){
//                query1+=" AND date_format(S_ENTRUSTDATE, '%Y-%m-%d') <= date_format('"+pd.getString("S_ENDTIME")+"', '%Y-%m-%d')";
                query1+=" AND toString(S_ENTRUSTDATE)  <= toString(REPLACE('"+pd.getString("S_ENDTIME")+"','-','')) ";
            }
            if(!"".equals(pd.containsKey("S_PAYMODE")? pd.get("S_PAYMODE"):"")){
                query1+=" AND S_PAYMODE ='"+pd.getString("S_PAYMODE")+"'";
            }
            if(!"".equals(pd.containsKey("S_AGENTBANKCLASS")? pd.get("S_AGENTBANKCLASS"):"")){
                query1+=" AND toString(S_AGENTBANKCLASS) =toString('"+pd.getString("S_AGENTBANKCLASS")+"')";
            }
            if(!"".equals(pd.containsKey("S_VOUCHERNO")? pd.get("S_VOUCHERNO"):"")){
                query1+=" AND S_VOUCHERNO like '%"+pd.getString("S_VOUCHERNO")+"%'";
            }
            if(!"".equals(pd.containsKey("S_BDGORGCODE")? pd.get("S_BDGORGCODE"):"")){
                query1+=" AND S_BDGORGCODE ='"+pd.getString("S_BDGORGCODE")+"'";
            }
            if(!"".equals(pd.containsKey("S_BDGORGNAME")? pd.get("S_BDGORGNAME"):"")){
                query1+=" AND S_BDGORGNAME ='"+pd.getString("S_BDGORGNAME")+"'";
            }
            if(!"".equals(pd.containsKey("S_PAYEEACCTNO")? pd.get("S_PAYEEACCTNO"):"")){
                query1+=" AND S_PAYEEACCTNO like '%"+pd.getString("S_PAYEEACCTNO")+"%'";
            }
            if(!"".equals(pd.containsKey("S_PAYEEACCTNAME")? pd.get("S_PAYEEACCTNAME"):"")){
                query1+=" AND S_PAYEEACCTNAME like '%"+pd.getString("S_PAYEEACCTNAME")+"%'";
            }
            if(!"".equals(pd.containsKey("S_EXPFUNCCODE")? pd.get("S_EXPFUNCCODE"):"")){
//                query1+=" AND S_EXPFUNCCODE in ("+pd.getString("S_EXPFUNCCODE")+")";
                query1+=" AND S_EXPFUNCCODE in ("+pd.getString("S_EXPFUNCCODE").replace("[","").replace("]","").replace("\"","'")+")";
            }
//            if(!"".equals(pd.containsKey("S_EXPFUNCNAME")? pd.get("S_EXPFUNCNAME"):"")){
//                query1+=" AND S_EXPFUNCNAME ='"+pd.getString("S_EXPFUNCNAME")+"'";
//            }
            if(!"".equals(pd.containsKey("S_REMARK")? pd.get("S_REMARK"):"")){
                query1+=" AND S_REMARK like '%"+pd.getString("S_REMARK")+"%'";
            }
            if(!"".equals(pd.containsKey("S_AUDITREASON")? pd.get("S_AUDITREASON"):"")){
                query1+=" AND S_AUDITREASON ='"+pd.getString("S_AUDITREASON")+"'";
            }
            if(!"".equals(pd.containsKey("C_AUTOAUDITSTATE")? pd.get("C_AUTOAUDITSTATE"):"")){
                query1+=" AND C_AUTOAUDITSTATE ='"+pd.getString("C_AUTOAUDITSTATE")+"'";
            }
            if(!"".equals(pd.containsKey("C_HANDAUDITSTATE")? pd.get("C_HANDAUDITSTATE"):"")){
                query1+=" AND C_HANDAUDITSTATE ='"+pd.getString("C_HANDAUDITSTATE")+"'";
            }

            //查询总条数
            List<Map<String, Object>> dataList ;
            dataList = this.getSQLResults(query1); //comprehensiveQueryService.getComprehensiveQueryData(pageData);
            jsonMap.put("rows", dataList);
            jsonMap.put("result", "success");
        } catch (Exception e) {
            jsonMap.put("result", "fail");
            jsonMap.put("msg", e.getMessage());
        }
        return jsonMap;
    }

    /***
     * 获取所有报告列表
     */
    @RequestMapping(value = "/BackData", method = RequestMethod.POST)
    @ApiOperation("获取集中支付业务明细查询数据")
    public Map<String, Object> BackData(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> jsonMap = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            if (pd.size() == 0) { //页面初始化查询
                jsonMap.put("total", 0);
                jsonMap.put("rows", new ArrayList<>());
                return jsonMap;
            }
            Integer page = Integer.parseInt(pd.getString("pageNo"));//页码
            Integer rows = Integer.parseInt(pd.getString("pageSize"));//行数

            //      执行SQL语句
//            String queryTotal="";
            String query1="select S_SEQNO,\n" +
                    "        S_ID,\n" +
                    "        S_ADMDIVCODE,\n" +
                    "        S_STYEAR,\n" +
                    "        S_BOOKORGCODE,\n" +
                    "        t.guoku_dscr AS S_TRENAME,\n" +
                    "        S_TRECODE,\n" +
                    "        CONCAT(\n" +
                    "        '-',\n" +
                    "       CONCAT(SUBSTR(S_ENTRUSTDATE,1,4),'-',SUBSTR(S_ENTRUSTDATE,5,2),'-',SUBSTR(S_ENTRUSTDATE,7,2)) AS  S_ENTRUSTDATE_1,  " +
                    "       case  S_AGENTBANKCLASS  WHEN '1' THEN '工商银行'   WHEN '2' THEN '农业银行'" +
                    "    WHEN '3' THEN '中国银行'   WHEN '4' THEN '建设银行' WHEN '5' THEN '交通银行'   WHEN '6' THEN '光大银行'" +
                    "    WHEN '7' THEN '中信银行'   WHEN '8' THEN '平安银行' WHEN '9' THEN '民生银行'   WHEN '10' THEN '兴业银行'" +
                    "    WHEN '11' THEN '重庆农商行'   WHEN '12' THEN '重庆银行' WHEN '13' THEN '重庆三峡银行'   WHEN '14' THEN '邮储银行'" +
                    "    WHEN '15' THEN '村镇银行'   ELSE  '其他银行' END AS S_AGENTBANKCLASS_1, " +
                    "        S_AGENTBANKNO,\n" +
                    "        S_AGENTBANKNAME,\n" +
                    "        S_PAYOUTVOUTYPE,\n" +
                    "        CASE\n" +
                    "        S_PAYOUTVOUTYPE\n" +
                    "        WHEN '0'\n" +
                    "        THEN '无纸'\n" +
                    "        WHEN '1'\n" +
                    "        THEN '有纸'\n" +
                    "        ELSE '其他'\n" +
                    "        END AS S_PAYOUTVOUNAME,\n" +
                    "        S_PAYMODE,\n" +
                    "        CASE\n" +
                    "        S_PAYMODE\n" +
                    "        WHEN '1'\n" +
                    "        THEN '直接支付'\n" +
                    "        WHEN '2'\n" +
                    "        THEN '授权转账'\n" +
                    "        WHEN '3'\n" +
                    "        THEN '授权现金'\n" +
                    "        ELSE '其他'\n" +
                    "        END AS S_PAYMODENAME,\n" +
                    "        '退款'as S_BACKTYPE,\n" +
                    "        S_VOUCHERNO  AS S_VOUCHERNO_1,\n" +
                    "        S_ORIVOUCHERNO,\n" +
                    "        S_ORIPAYVOUDATE,\n" +
                    "        S_FUNDTYPECODE,\n" +
                    "        CASE\n" +
                    "        S_FUNDTYPECODE\n" +
                    "        WHEN '1'\n" +
                    "        THEN '预算内'\n" +
                    "        WHEN '2'\n" +
                    "        THEN '预算外'\n" +
                    "        ELSE '其他'\n" +
                    "        END AS S_FUNDTYPENAME,\n" +
                    "        S_BDGORGCODE as S_BDGORGCODE_1,\n" +
                    "        S_BDGORGNAME as S_BDGORGNAME_1,\n" +
                    "        S_EXPFUNCCODE as S_EXPFUNCCODE_1,\n" +
                    "        S_EXPFUNCNAME  as S_EXPFUNCNAME_1,\n" +
                    "        S_EXPECOCODE,\n" +
                    "        S_EXPECONAME,\n" +
                    "        S_PROJECTTYPECODE,\n" +
                    "        S_PROJECTTYPENAME,\n" +
                    "        S_ORIZEROACCTNO,\n" +
                    "        S_ORIZEROACCTNAME,\n" +
                    "        S_ORIZEROOPNBNKNAME,\n" +
                    "        S_ORIPAYEEACCTNO,\n" +
                    "        S_ORIPAYEEACCTNAME,\n" +
                    "        S_ORIPAYEEOPNBNKNAME,\n" +
                    "        S_ORIPAYEEOPNBNKNO,\n" +
                    "        S_ORICLEARACCTNO,\n" +
                    "        S_ORICLEARACCTNAME,\n" +
                    "        S_ORICLEARBANKNO,\n" +
                    "        S_ORICLEARBANKNAME,\n" +
                    "        S_REMARK,\n" +
                    "       round(toFloat32(toFloat32(F_PAYAMT) / "+pd.getString("S_AMTUNIT")+"), 2) as F_PAYAMT,\n" +
                    "        C_ISADDPLAN,\n" +
                    "        C_CHECKRESULT,\n" +
                    "        S_HOLD1,\n" +
                    "        S_HOLD2,\n" +
                    "        S_HOLD3,\n" +
                    "        S_HOLD4,\n" +
                    "        TS_SYSUPDATE,\n" +
                    "        C_AUTOAUDITSTATE,\n" +
                    "        T_AUTOAUDITTIME,\n" +
                    "        S_AUDITREASON,\n" +
                    "        C_HANDAUDITSTATE,\n" +
                    "        T_HANDAUDITTIME,\n" +
                    "        S_HANDREASON\n" +
                    "        FROM\n" +
                    "        adm.trs_stat_agentbankpay_back_detail LEFT JOIN dmcode.cm_guoku_dimnsn t ON S_TRECODE = t.guoku_id\n" +
                    "        WHERE S_BACKTYPE = '2'";
            if(!"".equals(pd.containsKey("S_TRECODE")? pd.get("S_TRECODE"):"")){
                query1+=" AND S_TRECODE in (concat('"+pd.getString("S_TRECODE")+"',''))";
            }
            if(!"".equals(pd.containsKey("S_STARTTIME")? pd.get("S_STARTTIME"):"")){
                query1+=" AND toString(S_ENTRUSTDATE)  >= toString(REPLACE('"+pd.getString("S_STARTTIME")+"','-','')) ";
//                query1+="toFloat32(S_ENTRUSTDATE) BETWEEN   toFloat32(REPLACE('2020-02-02','-',''))   AND toFloat32(REPLACE('2020-12-02','-',''))"
            }
            if(!"".equals(pd.containsKey("S_ENDTIME")? pd.get("S_ENDTIME"):"")){
//                query1+=" AND date_format(S_ENTRUSTDATE, '%Y-%m-%d') <= date_format('"+pd.getString("S_ENDTIME")+"', '%Y-%m-%d')";
                query1+=" AND toString(S_ENTRUSTDATE)  <= toString(REPLACE('"+pd.getString("S_ENDTIME")+"','-','')) ";
            }
            if(!"".equals(pd.containsKey("S_PAYMODE")? pd.get("S_PAYMODE"):"")){
                query1+=" AND S_PAYMODE ='"+pd.getString("S_PAYMODE")+"'";
            }
            if(!"".equals(pd.containsKey("S_AGENTBANKCLASS")? pd.get("S_AGENTBANKCLASS"):"")){
                query1+=" AND toString(S_AGENTBANKCLASS) =toString('"+pd.getString("S_AGENTBANKCLASS")+"')";
            }
            if(!"".equals(pd.containsKey("S_VOUCHERNO")? pd.get("S_VOUCHERNO"):"")){
                query1+=" AND S_VOUCHERNO like '%"+pd.getString("S_VOUCHERNO")+"%'";
            }
            if(!"".equals(pd.containsKey("S_BDGORGCODE")? pd.get("S_BDGORGCODE"):"")){
                query1+=" AND S_BDGORGCODE   like '%"+pd.getString("S_BDGORGCODE")+"%'";
            }
            if(!"".equals(pd.containsKey("S_BDGORGNAME")? pd.get("S_BDGORGNAME"):"")){
                query1+=" AND S_BDGORGNAME   like '%"+pd.getString("S_BDGORGNAME")+"%'";
            }
            if(!"".equals(pd.containsKey("S_ORIPAYEEACCTNO")? pd.get("S_ORIPAYEEACCTNO"):"")){
                query1+=" AND S_ORIPAYEEACCTNO like '%"+pd.getString("S_ORIPAYEEACCTNO")+"%'";
            }
            if(!"".equals(pd.containsKey("S_ORIPAYEEACCTNAME")? pd.get("S_ORIPAYEEACCTNAME"):"")){
                query1+=" AND S_ORIPAYEEACCTNAME like '%"+pd.getString("S_ORIPAYEEACCTNAME")+"%'";
            }
            if(!"".equals(pd.containsKey("S_EXPFUNCCODE")? pd.get("S_EXPFUNCCODE"):"")){
                query1+=" AND S_EXPFUNCCODE in ("+pd.getString("S_EXPFUNCCODE")+")";
            }
            if(!"".equals(pd.containsKey("S_EXPFUNCNAME")? pd.get("S_EXPFUNCNAME"):"")){
                query1+=" AND S_EXPFUNCNAME ='"+pd.getString("S_EXPFUNCNAME")+"'";
            }
            if(!"".equals(pd.containsKey("S_REMARK")? pd.get("S_REMARK"):"")){
                query1+=" AND S_REMARK like '%"+pd.getString("S_REMARK")+"%'";
            }

            String queryTotal="select count(1) FROM\n" +
                    "        adm.trs_stat_agentbankpay_back_detail LEFT JOIN dmcode.cm_guoku_dimnsn t ON S_TRECODE = t.guoku_id\n" +
                    "        WHERE S_BACKTYPE = '2'";
            if(!"".equals(pd.containsKey("S_TRECODE")? pd.get("S_TRECODE"):"")){
                queryTotal+=" AND S_TRECODE in (concat('"+pd.getString("S_TRECODE")+"',''))";
            }
            if(!"".equals(pd.containsKey("S_STARTTIME")? pd.get("S_STARTTIME"):"")){
                queryTotal+=" AND toString(S_ENTRUSTDATE)  >= toString(REPLACE('"+pd.getString("S_STARTTIME")+"','-','')) ";
//                queryTotal+="toFloat32(S_ENTRUSTDATE) BETWEEN   toFloat32(REPLACE('2020-02-02','-',''))   AND toFloat32(REPLACE('2020-12-02','-',''))"
            }
            if(!"".equals(pd.containsKey("S_ENDTIME")? pd.get("S_ENDTIME"):"")){
//                queryTotal+=" AND date_format(S_ENTRUSTDATE, '%Y-%m-%d') <= date_format('"+pd.getString("S_ENDTIME")+"', '%Y-%m-%d')";
                queryTotal+=" AND toString(S_ENTRUSTDATE)  <= toString(REPLACE('"+pd.getString("S_ENDTIME")+"','-','')) ";
            }
            if(!"".equals(pd.containsKey("S_PAYMODE")? pd.get("S_PAYMODE"):"")){
                queryTotal+=" AND S_PAYMODE ='"+pd.getString("S_PAYMODE")+"'";
            }
            if(!"".equals(pd.containsKey("S_AGENTBANKCLASS")? pd.get("S_AGENTBANKCLASS"):"")){
                queryTotal+=" AND toString(S_AGENTBANKCLASS) =toString('"+pd.getString("S_AGENTBANKCLASS")+"')";
            }
            if(!"".equals(pd.containsKey("S_VOUCHERNO")? pd.get("S_VOUCHERNO"):"")){
                queryTotal+=" AND S_VOUCHERNO like '%"+pd.getString("S_VOUCHERNO")+"%'";
            }
            if(!"".equals(pd.containsKey("S_BDGORGCODE")? pd.get("S_BDGORGCODE"):"")){
                queryTotal+=" AND S_BDGORGCODE   like '%"+pd.getString("S_BDGORGCODE")+"%'";
            }
            if(!"".equals(pd.containsKey("S_BDGORGNAME")? pd.get("S_BDGORGNAME"):"")){
                queryTotal+=" AND S_BDGORGNAME   like '%"+pd.getString("S_BDGORGNAME")+"%'";
            }
            if(!"".equals(pd.containsKey("S_ORIPAYEEACCTNO")? pd.get("S_ORIPAYEEACCTNO"):"")){
                queryTotal+=" AND S_ORIPAYEEACCTNO like '%"+pd.getString("S_ORIPAYEEACCTNO")+"%'";
            }
            if(!"".equals(pd.containsKey("S_ORIPAYEEACCTNAME")? pd.get("S_ORIPAYEEACCTNAME"):"")){
                queryTotal+=" AND S_ORIPAYEEACCTNAME like '%"+pd.getString("S_ORIPAYEEACCTNAME")+"%'";
            }
            if(!"".equals(pd.containsKey("S_EXPFUNCCODE")? pd.get("S_EXPFUNCCODE"):"")){
                queryTotal+=" AND S_EXPFUNCCODE in ("+pd.getString("S_EXPFUNCCODE")+")";
            }
            if(!"".equals(pd.containsKey("S_EXPFUNCNAME")? pd.get("S_EXPFUNCNAME"):"")){
                queryTotal+=" AND S_EXPFUNCNAME ='"+pd.getString("S_EXPFUNCNAME")+"'";
            }
            if(!"".equals(pd.containsKey("S_REMARK")? pd.get("S_REMARK"):"")){
                queryTotal+=" AND S_REMARK like '%"+pd.getString("S_REMARK")+"%'";
            }
            //按分页查询
            List<Map<String, Object>> dataListTotal ;
            List<Map<String, Object>> dataList ;
            Integer pageNo= page>2? (page - 1) * rows:0;
            Integer rowsNo= page>2? page * rows:10;
            String dataPage= " limit "+ pageNo + "," + rowsNo;
            query1+=dataPage;
            System.out.println(query1);
            dataList = this.getSQLResults(query1); //comprehensiveQueryService.getComprehensiveQueryData(pageData);
            dataListTotal = this.getSQLResults(queryTotal);
            Integer total=Integer.parseInt(dataListTotal.get(0).get("count(1)").toString());
            jsonMap.put("rows", dataList);
            jsonMap.put("total", total);
            jsonMap.put("result", "success");
        } catch (Exception e) {
            jsonMap.put("result", "fail");
            jsonMap.put("msg", e.getMessage());
        }
        return jsonMap;
    }

    /***
     * 获取所有报告列表
     */
    @RequestMapping(value = "/getBackCountAndSum", method = RequestMethod.POST)
    @ApiOperation("获取集中支付业务退库sum汇总")
    public Map<String, Object> getBackCountAndSum(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> jsonMap = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            if (pd.size() == 0) { //页面初始化查询
                jsonMap.put("total", 0);
                jsonMap.put("rows", new ArrayList<>());
                return jsonMap;
            }
            Integer page = Integer.parseInt(pd.getString("page"));//页码
            Integer rows = Integer.parseInt(pd.getString("rows"));//行数
            //      执行SQL语句
            String query1=" select  round(IFNULL(sum(F_PAYAMT),0)/"+pd.getString("S_AMTUNIT")+",2)"+
                    "        adm.trs_stat_agentbankpay_back_detail WHERE  S_BACKTYPE = '2'";
            if(!"".equals(pd.containsKey("S_TRECODE")? pd.get("S_TRECODE"):"")){
                query1+=" AND S_TRECODE in (concat('"+pd.getString("S_TRECODE")+"',''))";
            }
            if(!"".equals(pd.containsKey("S_STARTTIME")? pd.get("S_STARTTIME"):"")){
                query1+=" AND toString(S_ENTRUSTDATE)  >= toString(REPLACE('"+pd.getString("S_STARTTIME")+"','-','')) ";
//                query1+="toFloat32(S_ENTRUSTDATE) BETWEEN   toFloat32(REPLACE('2020-02-02','-',''))   AND toFloat32(REPLACE('2020-12-02','-',''))"
            }
            if(!"".equals(pd.containsKey("S_ENDTIME")? pd.get("S_ENDTIME"):"")){
//                query1+=" AND date_format(S_ENTRUSTDATE, '%Y-%m-%d') <= date_format('"+pd.getString("S_ENDTIME")+"', '%Y-%m-%d')";
                query1+=" AND toString(S_ENTRUSTDATE)  <= toString(REPLACE('"+pd.getString("S_ENDTIME")+"','-','')) ";
            }
            if(!"".equals(pd.containsKey("S_PAYMODE")? pd.get("S_PAYMODE"):"")){
                query1+=" AND S_PAYMODE ='"+pd.getString("S_PAYMODE")+"'";
            }
            if(!"".equals(pd.containsKey("S_AGENTBANKCLASS")? pd.get("S_AGENTBANKCLASS"):"")){
                query1+=" AND toString(S_AGENTBANKCLASS) =toString('"+pd.getString("S_AGENTBANKCLASS")+"')";
            }
            if(!"".equals(pd.containsKey("S_VOUCHERNO")? pd.get("S_VOUCHERNO"):"")){
                query1+=" AND S_VOUCHERNO like '%"+pd.getString("S_VOUCHERNO")+"%'";
            }
            if(!"".equals(pd.containsKey("S_BDGORGCODE")? pd.get("S_BDGORGCODE"):"")){
                query1+=" AND S_BDGORGCODE   like '%"+pd.getString("S_BDGORGCODE")+"%'";
            }
            if(!"".equals(pd.containsKey("S_BDGORGNAME")? pd.get("S_BDGORGNAME"):"")){
                query1+=" AND S_BDGORGNAME   like '%"+pd.getString("S_BDGORGNAME")+"%'";
            }
            if(!"".equals(pd.containsKey("S_PAYEEACCTNO")? pd.get("S_PAYEEACCTNO"):"")){
                query1+=" AND S_PAYEEACCTNO like '%"+pd.getString("S_PAYEEACCTNO")+"%'";
            }
            if(!"".equals(pd.containsKey("S_PAYEEACCTNAME")? pd.get("S_PAYEEACCTNAME"):"")){
                query1+=" AND S_PAYEEACCTNAME like '%"+pd.getString("S_PAYEEACCTNAME")+"%'";
            }
            if(!"".equals(pd.containsKey("S_EXPFUNCCODE")? pd.get("S_EXPFUNCCODE"):"")){
                query1+=" AND S_EXPFUNCCODE in ("+pd.getString("S_EXPFUNCCODE")+")";
            }
            if(!"".equals(pd.containsKey("S_EXPFUNCNAME")? pd.get("S_EXPFUNCNAME"):"")){
                query1+=" AND S_EXPFUNCNAME ='"+pd.getString("S_EXPFUNCNAME")+"'";
            }
            if(!"".equals(pd.containsKey("S_REMARK")? pd.get("S_REMARK"):"")){
                query1+=" AND S_REMARK like '%"+pd.getString("S_REMARK")+"%'";
            }
            if(!"".equals(pd.containsKey("S_AUDITREASON")? pd.get("S_AUDITREASON"):"")){
                query1+=" AND S_AUDITREASON ='"+pd.getString("S_AUDITREASON")+"'";
            }
            if(!"".equals(pd.containsKey("C_AUTOAUDITSTATE")? pd.get("C_AUTOAUDITSTATE"):"")){
                query1+=" AND C_AUTOAUDITSTATE ='"+pd.getString("C_AUTOAUDITSTATE")+"'";
            }
            if(!"".equals(pd.containsKey("C_HANDAUDITSTATE")? pd.get("C_HANDAUDITSTATE"):"")){
                query1+=" AND C_HANDAUDITSTATE ='"+pd.getString("C_HANDAUDITSTATE")+"'";
            }
            if(!"".equals(pd.containsKey("S_TWOTIMEDIFFERENCE")? pd.get("S_TWOTIMEDIFFERENCE"):"")){
                if("1".equals(pd.get("S_TWOTIMEDIFFERENCE"))){
                    query1+="AND TIMESTAMPDIFF(HOUR, T_AUTOAUDITTIME, T_HANDAUDITTIME) BETWEEN 2 and 4";
                }else if("2".equals(pd.get("S_TWOTIMEDIFFERENCE"))){
                    query1+="AND TIMESTAMPDIFF(HOUR, T_AUTOAUDITTIME, T_HANDAUDITTIME) BETWEEN 4 and 6";
                }else {
                    query1+="AND TIMESTAMPDIFF(HOUR, T_AUTOAUDITTIME, T_HANDAUDITTIME)>=";
                }
            }
            //查询总条数
            List<Map<String, Object>> dataList = this.getSQLResults(query1); //comprehensiveQueryService.getComprehensiveQueryData(pageData);
            //按分页查询
            pd.put("page", (page - 1) * rows);
            String dataPage= "LIMIT "+pd.get("page")+","+pd.get("rows");
            query1+=dataPage;
            dataList = this.getSQLResults(query1); //comprehensiveQueryService.getComprehensiveQueryData(pageData);
            jsonMap.put("rows", dataList);
        } catch (Exception e) {
            jsonMap.put("result", "fail");
            jsonMap.put("msg", e.getMessage());
        }
        return jsonMap;
    }


    public List<Map<String, Object>> getSQLResults(String sql) {
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            List<Entity> dataList = Db.use().query(sql);
            for (Entity e : dataList) {
                Map<String, Object> map1 = new HashMap<>();
                BeanUtil.copyProperties(e, map1);
                list.add(map1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


    @RequestMapping(value = "/getBudgetUnit")
    @ApiOperation("获取预算单位")
    public Map<String, Object> getBudgetUnit(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> jsonMap = new HashMap<>();
        try {
        PageData pd = this.getPageData(param);
        pd = this.getPageData();
        List<Map<String, Object>> data = centralizedPaymentService.getBudgetUnit(pd);
            jsonMap.put("rows", data);
        } catch (Exception e) {
            jsonMap.put("result", "fail");
            jsonMap.put("msg", e.getMessage());
        }
        return jsonMap;
    }

    @RequestMapping(value = "/getAgentBankClass")
    @ApiOperation("获取代理银行")
    public Map<String, Object> getAgentBankClass(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> jsonMap = new HashMap<>();
        try {
        PageData pd = this.getPageData(param);
        List<Map<String, Object>> data = centralizedPaymentService.getAgentBankClass(pd);
            jsonMap.put("rows", data);
        } catch (Exception e) {
            jsonMap.put("result", "fail");
            jsonMap.put("msg", e.getMessage());
        }
        return jsonMap;
    }


    @RequestMapping(value = "/getKeMuTreeName" , method = RequestMethod.POST)
    @ApiOperation("获取科目树名称")
    public Map<String, Object> getKeMuTreeName(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> jsonMap = new HashMap<>();
            try {
        PageData pd = this.getPageData(param);
            List<Map<String, Object>> data = centralizedPaymentService.getKeMu(pd);
                List<TreeNode> treeNodeList = TreeFilterHeaper.definedTreeFilter(data);
                jsonMap.put("result", "success");
                jsonMap.put("rows", treeNodeList);
            } catch (Exception e) {
                jsonMap.put("result", "fail");
                jsonMap.put("msg", e.getMessage());
            }
        return jsonMap;
    }
    @RequestMapping(value = "/getKeMuTreeCode", method = RequestMethod.POST)
    @ApiOperation("获取科目树编码" )
    public Map<String, Object> getKeMuTreeCode(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> jsonMap = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            List<Map<String, Object>> data = centralizedPaymentService.getKeMu2(pd);
            List<TreeNode> treeNodeList = TreeFilterHeaper.definedTreeFilter(data);
            jsonMap.put("result", "success");
            jsonMap.put("rows", treeNodeList);
        } catch (Exception e) {
            jsonMap.put("result", "fail");
            jsonMap.put("msg", e.getMessage());
        }
        return jsonMap;
    }
    @RequestMapping(value = "exportToTxt")
    public void exportToTxt(HttpServletResponse response) {
        PageData pd = new PageData();
        pd = this.getPageData();
//        List<Map<String, Object>> data = centralizedPaymentService.getDataAll(pd);
        String query1=" SELECT\n" +
                "        S_SEQNO,\n" +
                "        S_ID,\n" +
                "        S_ADMDIVCODE,\n" +
                "        S_STYEAR,\n" +
                "        S_BOOKORGCODE,\n" +
                "        t.guoku_dscr AS S_TRENAME,\n" +
                "        S_TRECODE,\n" +
                "    CONCAT(SUBSTR(S_ENTRUSTDATE,1,4),'-',SUBSTR(S_ENTRUSTDATE,5,2),'-',SUBSTR(S_ENTRUSTDATE,7,2)) AS  S_ENTRUSTDATE_1,\n" +
                "       case  S_AGENTBANKCLASS  WHEN '1' THEN '工商银行'   WHEN '2' THEN '农业银行'" +
                "    WHEN '3' THEN '中国银行'   WHEN '4' THEN '建设银行' WHEN '5' THEN '交通银行'   WHEN '6' THEN '光大银行'" +
                "    WHEN '7' THEN '中信银行'   WHEN '8' THEN '平安银行' WHEN '9' THEN '民生银行'   WHEN '10' THEN '兴业银行'" +
                "    WHEN '11' THEN '重庆农商行'   WHEN '12' THEN '重庆银行' WHEN '13' THEN '重庆三峡银行'   WHEN '14' THEN '邮储银行'" +
                "    WHEN '15' THEN '村镇银行'   ELSE  '其他银行' END AS S_AGENTBANKCLASS_1, " +
                "        S_AGENTBANKNO,\n" +
                "        S_AGENTBANKNAME,\n" +
                "        S_PAYOUTVOUTYPE,\n" +
                "        CASE S_PAYOUTVOUTYPE WHEN '0' THEN '无纸' WHEN '1' THEN '有纸' ELSE '其他' END AS S_PAYOUTVOUNAME,\n" +
                "        S_PAYMODE,\n" +
                "        CASE S_PAYMODE WHEN '1' THEN '直接支付' WHEN '2' THEN '授权转账' WHEN '3' THEN '授权现金'   ELSE '其他' END AS S_PAYMODENAME,\n" +
                "        D_PAYVOUDATE,\n" +
                "        S_VOUCHERNO  AS S_VOUCHERNO_1,\n" +
                "        S_FUNDTYPECODE,\n" +
                "        CASE S_FUNDTYPECODE WHEN '1' THEN '预算内' WHEN '2' THEN '预算外' ELSE '其他' END AS S_FUNDTYPENAME,\n" +
                "        S_BDGORGCODE as S_BDGORGCODE_1,\n" +
                "        S_BDGORGNAME as S_BDGORGNAME_1,\n" +
                "        S_EXPFUNCCODE as S_EXPFUNCCODE_1,\n" +
                "        S_EXPFUNCNAME  as S_EXPFUNCNAME_1,\n" +
                "        S_EXPECOCODE,\n" +
                "        S_EXPECONAME,\n" +
                "        S_PROJECTTYPECODE,\n" +
                "        S_PROJECTTYPENAME,\n" +
                "        S_ZEROACCTNO,\n" +
                "        S_ZEROACCTNAME,\n" +
                "        S_ZEROOPNBNKNAME,\n" +
                "        S_PAYEEACCTNO,\n" +
                "        S_PAYEEACCTNAME,\n" +
                "        S_PAYEEOPNBNKNO,\n" +
                "        S_PAYEEOPNBNKNAME,\n" +
                "        S_CLEARACCTNO,\n" +
                "        S_CLEARACCTNAME,\n" +
                "        S_CLEARBANKNO,\n" +
                "        S_CLEARBANKNAME,\n" +
                "        S_REMARK,\n" +
                "        round(toFloat32(toFloat32(F_PAYAMT) / "+pd.getString("S_AMTUNIT")+"), 2) as F_PAYAMT,\n" +
                "        C_AUTOAUDITSTATE,\n" +
                "        CASE C_AUTOAUDITSTATE WHEN '1' THEN '审核通过' WHEN '2' THEN '审核不通过' WHEN '3' THEN '待审核' ELSE '其他' END AS C_AUTOAUDITNAME,\n" +
                "        T_AUTOAUDITTIME,\n" +
                "        S_AUDITREASON,\n" +
                "        C_HANDAUDITSTATE,\n" +
                "        CASE C_HANDAUDITSTATE WHEN '1' THEN '审核通过' WHEN '2' THEN '审核不通过' WHEN '3' THEN '待审核' ELSE '其他' END AS C_HANDAUDITNAME,\n" +
                "        T_HANDAUDITTIME,\n" +
                "        S_HANDREASON,\n" +
                "        C_HANDAUDITFLAG,\n" +
                "        C_ISDEDPLAN,\n" +
                "        C_CHECKRESULT,\n" +
                "        S_HOLD1,\n" +
                "        S_HOLD2,\n" +
                "        S_HOLD3,\n" +
                "        S_HOLD4,\n" +
                "        TS_SYSUPDATE\n" +
                "        FROM\n" +
                "        adm.trs_stat_agentbankpay_detail LEFT JOIN dmcode.cm_guoku_dimnsn t ON S_TRECODE = t.guoku_id\n" +
                "        WHERE\n" +
                "        concat('1','') = concat('1','')";
        if(!"".equals(pd.containsKey("S_TRECODE")? pd.get("S_TRECODE"):"")){
            query1+=" AND S_TRECODE in (concat('"+pd.getString("S_TRECODE")+"',''))";
        }
        if(!"".equals(pd.containsKey("S_STARTTIME")? pd.get("S_STARTTIME"):"")){
            query1+=" AND toString(S_ENTRUSTDATE)  >= toString(REPLACE('"+pd.getString("S_STARTTIME")+"','-','')) ";
//                query1+="toFloat32(S_ENTRUSTDATE) BETWEEN   toFloat32(REPLACE('2020-02-02','-',''))   AND toFloat32(REPLACE('2020-12-02','-',''))"
        }
        if(!"".equals(pd.containsKey("S_ENDTIME")? pd.get("S_ENDTIME"):"")){
//                query1+=" AND date_format(S_ENTRUSTDATE, '%Y-%m-%d') <= date_format('"+pd.getString("S_ENDTIME")+"', '%Y-%m-%d')";
            query1+=" AND toString(S_ENTRUSTDATE)  <= toString(REPLACE('"+pd.getString("S_ENDTIME")+"','-','')) ";
        }
        if(!"".equals(pd.containsKey("S_PAYMODE")? pd.get("S_PAYMODE"):"")){
            query1+=" AND S_PAYMODE ='"+pd.getString("S_PAYMODE")+"'";
        }
        if(!"".equals(pd.containsKey("S_AGENTBANKCLASS")? pd.get("S_AGENTBANKCLASS"):"")){
            query1+=" AND toString(S_AGENTBANKCLASS) =toString('"+pd.getString("S_AGENTBANKCLASS")+"')";
        }
        if(!"".equals(pd.containsKey("S_VOUCHERNO")? pd.get("S_VOUCHERNO"):"")){
            query1+=" AND S_VOUCHERNO like '%"+pd.getString("S_VOUCHERNO")+"%'";
        }
        if(!"".equals(pd.containsKey("S_BDGORGCODE")? pd.get("S_BDGORGCODE"):"")){
            query1+=" AND S_BDGORGCODE   like '%"+pd.getString("S_BDGORGCODE")+"%'";
        }
        if(!"".equals(pd.containsKey("S_BDGORGNAME")? pd.get("S_BDGORGNAME"):"")){
            query1+=" AND S_BDGORGNAME   like '%"+pd.getString("S_BDGORGNAME")+"%'";
        }
        if(!"".equals(pd.containsKey("S_PAYEEACCTNO")? pd.get("S_PAYEEACCTNO"):"")){
            query1+=" AND S_PAYEEACCTNO like '%"+pd.getString("S_PAYEEACCTNO")+"%'";
        }
        if(!"".equals(pd.containsKey("S_PAYEEACCTNAME")? pd.get("S_PAYEEACCTNAME"):"")){
            query1+=" AND S_PAYEEACCTNAME like '%"+pd.getString("S_PAYEEACCTNAME")+"%'";
        }
        if(!"".equals(pd.containsKey("S_EXPFUNCCODE")? pd.get("S_EXPFUNCCODE"):"")){
            query1+=" AND S_EXPFUNCCODE in ("+pd.getString("S_EXPFUNCCODE")+")";
        }
        if(!"".equals(pd.containsKey("S_EXPFUNCNAME")? pd.get("S_EXPFUNCNAME"):"")){
            query1+=" AND S_EXPFUNCNAME ='"+pd.getString("S_EXPFUNCNAME")+"'";
        }
        if(!"".equals(pd.containsKey("S_REMARK")? pd.get("S_REMARK"):"")){
            query1+=" AND S_REMARK like '%"+pd.getString("S_REMARK")+"%'";
        }
        if(!"".equals(pd.containsKey("S_AUDITREASON")? pd.get("S_AUDITREASON"):"")){
            query1+=" AND S_AUDITREASON ='"+pd.getString("S_AUDITREASON")+"'";
        }
        if(!"".equals(pd.containsKey("C_AUTOAUDITSTATE")? pd.get("C_AUTOAUDITSTATE"):"")){
            query1+=" AND C_AUTOAUDITSTATE ='"+pd.getString("C_AUTOAUDITSTATE")+"'";
        }
        if(!"".equals(pd.containsKey("C_HANDAUDITSTATE")? pd.get("C_HANDAUDITSTATE"):"")){
            query1+=" AND C_HANDAUDITSTATE ='"+pd.getString("C_HANDAUDITSTATE")+"'";
        }


        //查询数据
        List<Map<String, Object>> data  = this.getSQLResults(query1);
        StringBuffer text = new StringBuffer();
        text.append("序号");
        text.append(",");
        text.append("委托日期");
        text.append(",");
        text.append("国库代码");
        text.append(",");
        text.append("国库名称");
        text.append(",");
        text.append("代理银行");
        text.append(",");
        text.append("凭证类型");
        text.append(",");
        text.append("支付方式");
        text.append(",");
        text.append("支付凭证日期");
        text.append(",");
        text.append("支付凭证编号");
        text.append(",");
        text.append("预算种类");
        text.append(",");
        text.append("预算单位编码");
        text.append(",");
        text.append("预算单位名称");
        text.append(",");
        text.append("功能科目编码");
        text.append(",");
        text.append("功能科目名称");
        text.append(",");
        text.append("零余额账户账号");
        text.append(",");
        text.append("零余额账户名称");
        text.append(",");
        text.append("收款人账号");
        text.append(",");
        text.append("收款人名称");
        text.append(",");
        text.append("收款人开户行名称");
        text.append(",");
        text.append("支付金额");
        text.append(",");
        text.append("第一审核状态");
        text.append(",");
        text.append("第一审核时间");
        text.append(",");
        text.append("第二确认状态");
        text.append(",");
        text.append("第二确认时间");
        text.append(",");
        text.append("审核不通过原因");
        text.append(",");
        text.append("摘要");
        text.append("\r\n");//换行字符
        if (null != data && data.size() > 0) {
            for (int i = 0; i < data.size(); i++) {
                Map<String, Object> temp = new HashMap<>();
                temp = data.get(i);
                text.append(temp.get("S_SEQNO") == null ? ' ':temp.get("S_SEQNO"));
                text.append(",");
                text.append(temp.get("S_ENTRUSTDATE") == null ? ' ':temp.get("S_ENTRUSTDATE"));
                text.append(",");
                text.append(temp.get("S_TRECODE") == null ? ' ':temp.get("S_TRECODE"));
                text.append(",");
                text.append(temp.get("S_TRENAME") == null ? ' ':temp.get("S_TRENAME"));
                text.append(",");
                text.append(temp.get("S_AGENTBANKNAME") == null ? ' ':temp.get("S_AGENTBANKNAME"));
                text.append(",");
                text.append(temp.get("S_PAYOUTVOUNAME") == null ? ' ':temp.get("S_PAYOUTVOUNAME"));
                text.append(",");
                text.append(temp.get("S_PAYMODENAME") == null ? ' ':temp.get("S_PAYMODENAME"));
                text.append(",");
                text.append(temp.get("D_PAYVOUDATE") == null ? ' ':temp.get("D_PAYVOUDATE"));
                text.append(",");
                text.append(temp.get("S_VOUCHERNO") == null ? ' ':temp.get("S_VOUCHERNO"));
                text.append(",");
                text.append(temp.get("S_FUNDTYPENAME") == null ? ' ':temp.get("S_FUNDTYPENAME"));
                text.append(",");
                text.append(temp.get("S_BDGORGCODE_1") == null ? ' ':temp.get("S_BDGORGCODE_1"));
                text.append(",");
                text.append(temp.get("S_BDGORGNAME_1") == null ? ' ':temp.get("S_BDGORGNAME_1"));
                text.append(",");
                text.append(temp.get("S_EXPFUNCCODE") == null ? ' ':temp.get("S_EXPFUNCCODE"));
                text.append(",");
                text.append(temp.get("S_PAYMODENAME") == null ? ' ':temp.get("S_PAYMODENAME"));
                text.append(",");
                text.append(temp.get("S_EXPFUNCNAME") == null ? ' ':temp.get("S_EXPFUNCNAME"));
                text.append(",");
                text.append(temp.get("S_ZEROACCTNO") == null ? ' ':temp.get("S_ZEROACCTNO"));
                text.append(",");
                text.append(temp.get("S_ZEROACCTNAME") == null ? ' ':temp.get("S_ZEROACCTNAME"));
                text.append(",");
                text.append(temp.get("S_PAYEEACCTNO") == null ? ' ':temp.get("S_PAYEEACCTNO"));
                text.append(",");
                text.append(temp.get("S_PAYEEACCTNAME") == null ? ' ':temp.get("S_PAYEEACCTNAME"));
                text.append(",");
                text.append(temp.get("S_PAYEEOPNBNKNAME") == null ? ' ':temp.get("S_PAYEEOPNBNKNAME"));
                text.append(",");
                text.append(temp.get("F_PAYAMT") == null ? ' ':temp.get("F_PAYAMT"));
                text.append(",");
                text.append(temp.get("C_AUTOAUDITNAME") == null ? ' ':temp.get("C_AUTOAUDITNAME"));
                text.append(",");
                text.append(temp.get("T_AUTOAUDITTIME") == null ? ' ':temp.get("T_AUTOAUDITTIME"));
                text.append(",");
                text.append(temp.get("C_HANDAUDITNAME") == null ? ' ':temp.get("C_HANDAUDITNAME"));
                text.append(",");
                text.append(temp.get("T_HANDAUDITTIME") == null ? ' ':temp.get("T_HANDAUDITTIME"));
                text.append(",");
                text.append(temp.get("S_AUDITREASON") == null ? ' ':temp.get("S_AUDITREASON"));
                text.append(",");
                text.append(temp.get("S_REMARK") == null ? ' ':temp.get("S_REMARK"));
                text.append("\r\n");//换行字符
            }
        }
        exportTxt(response, text.toString(),"集中支付明细");
    }

    @RequestMapping(value = "exportBackToTxt")
    public void exportBackToTxt(HttpServletResponse response) {
        PageData pd = new PageData();
        pd = this.getPageData();
//        List<Map<String, Object>> data = centralizedPaymentService.getBackDataAll(pd);
        String query1="select S_SEQNO,\n" +
                "        S_ID,\n" +
                "        S_ADMDIVCODE,\n" +
                "        S_STYEAR,\n" +
                "        S_BOOKORGCODE,\n" +
                "        t.guoku_dscr AS S_TRENAME,\n" +
                "        S_TRECODE,\n" +
                "        CONCAT(\n" +
                "        '-',\n" +
                "       CONCAT(SUBSTR(S_ENTRUSTDATE,1,4),'-',SUBSTR(S_ENTRUSTDATE,5,2),'-',SUBSTR(S_ENTRUSTDATE,7,2)) AS  S_ENTRUSTDATE_1,  " +
                "       case  S_AGENTBANKCLASS  WHEN '1' THEN '工商银行'   WHEN '2' THEN '农业银行'" +
                "    WHEN '3' THEN '中国银行'   WHEN '4' THEN '建设银行' WHEN '5' THEN '交通银行'   WHEN '6' THEN '光大银行'" +
                "    WHEN '7' THEN '中信银行'   WHEN '8' THEN '平安银行' WHEN '9' THEN '民生银行'   WHEN '10' THEN '兴业银行'" +
                "    WHEN '11' THEN '重庆农商行'   WHEN '12' THEN '重庆银行' WHEN '13' THEN '重庆三峡银行'   WHEN '14' THEN '邮储银行'" +
                "    WHEN '15' THEN '村镇银行'   ELSE  '其他银行' END AS S_AGENTBANKCLASS_1, " +
                "        S_AGENTBANKNO,\n" +
                "        S_AGENTBANKNAME,\n" +
                "        S_PAYOUTVOUTYPE,\n" +
                "        CASE\n" +
                "        S_PAYOUTVOUTYPE\n" +
                "        WHEN '0'\n" +
                "        THEN '无纸'\n" +
                "        WHEN '1'\n" +
                "        THEN '有纸'\n" +
                "        ELSE '其他'\n" +
                "        END AS S_PAYOUTVOUNAME,\n" +
                "        S_PAYMODE,\n" +
                "        CASE\n" +
                "        S_PAYMODE\n" +
                "        WHEN '1'\n" +
                "        THEN '直接支付'\n" +
                "        WHEN '2'\n" +
                "        THEN '授权转账'\n" +
                "        WHEN '3'\n" +
                "        THEN '授权现金'\n" +
                "        ELSE '其他'\n" +
                "        END AS S_PAYMODENAME,\n" +
                "        '退款'as S_BACKTYPE,\n" +
                "        S_VOUCHERNO  AS S_VOUCHERNO_1,\n" +
                "        S_ORIVOUCHERNO,\n" +
                "        S_ORIPAYVOUDATE,\n" +
                "        S_FUNDTYPECODE,\n" +
                "        CASE\n" +
                "        S_FUNDTYPECODE\n" +
                "        WHEN '1'\n" +
                "        THEN '预算内'\n" +
                "        WHEN '2'\n" +
                "        THEN '预算外'\n" +
                "        ELSE '其他'\n" +
                "        END AS S_FUNDTYPENAME,\n" +
                "        S_BDGORGCODE as S_BDGORGCODE_1,\n" +
                "        S_BDGORGNAME as S_BDGORGNAME_1,\n" +
                "        S_EXPFUNCCODE as S_EXPFUNCCODE_1,\n" +
                "        S_EXPFUNCNAME  as S_EXPFUNCNAME_1,\n" +
                "        S_EXPECOCODE,\n" +
                "        S_EXPECONAME,\n" +
                "        S_PROJECTTYPECODE,\n" +
                "        S_PROJECTTYPENAME,\n" +
                "        S_ORIZEROACCTNO,\n" +
                "        S_ORIZEROACCTNAME,\n" +
                "        S_ORIZEROOPNBNKNAME,\n" +
                "        S_ORIPAYEEACCTNO,\n" +
                "        S_ORIPAYEEACCTNAME,\n" +
                "        S_ORIPAYEEOPNBNKNAME,\n" +
                "        S_ORIPAYEEOPNBNKNO,\n" +
                "        S_ORICLEARACCTNO,\n" +
                "        S_ORICLEARACCTNAME,\n" +
                "        S_ORICLEARBANKNO,\n" +
                "        S_ORICLEARBANKNAME,\n" +
                "        S_REMARK,\n" +
                "       round(toFloat32(toFloat32(F_PAYAMT) / "+pd.getString("S_AMTUNIT")+"), 2) as F_PAYAMT,\n" +
                "        C_ISADDPLAN,\n" +
                "        C_CHECKRESULT,\n" +
                "        S_HOLD1,\n" +
                "        S_HOLD2,\n" +
                "        S_HOLD3,\n" +
                "        S_HOLD4,\n" +
                "        TS_SYSUPDATE,\n" +
                "        C_AUTOAUDITSTATE,\n" +
                "        T_AUTOAUDITTIME,\n" +
                "        S_AUDITREASON,\n" +
                "        C_HANDAUDITSTATE,\n" +
                "        T_HANDAUDITTIME,\n" +
                "        S_HANDREASON\n" +
                "        FROM\n" +
                "        adm.trs_stat_agentbankpay_back_detail LEFT JOIN dmcode.cm_guoku_dimnsn t ON S_TRECODE = t.guoku_id\n" +
                "        WHERE S_BACKTYPE = '2'";
        if(!"".equals(pd.containsKey("S_TRECODE")? pd.get("S_TRECODE"):"")){
            query1+=" AND S_TRECODE in (concat('"+pd.getString("S_TRECODE")+"',''))";
        }
        if(!"".equals(pd.containsKey("S_STARTTIME")? pd.get("S_STARTTIME"):"")){
            query1+=" AND toString(S_ENTRUSTDATE)  >= toString(REPLACE('"+pd.getString("S_STARTTIME")+"','-','')) ";
//                query1+="toFloat32(S_ENTRUSTDATE) BETWEEN   toFloat32(REPLACE('2020-02-02','-',''))   AND toFloat32(REPLACE('2020-12-02','-',''))"
        }
        if(!"".equals(pd.containsKey("S_ENDTIME")? pd.get("S_ENDTIME"):"")){
//                query1+=" AND date_format(S_ENTRUSTDATE, '%Y-%m-%d') <= date_format('"+pd.getString("S_ENDTIME")+"', '%Y-%m-%d')";
            query1+=" AND toString(S_ENTRUSTDATE)  <= toString(REPLACE('"+pd.getString("S_ENDTIME")+"','-','')) ";
        }
        if(!"".equals(pd.containsKey("S_PAYMODE")? pd.get("S_PAYMODE"):"")){
            query1+=" AND S_PAYMODE ='"+pd.getString("S_PAYMODE")+"'";
        }
        if(!"".equals(pd.containsKey("S_AGENTBANKCLASS")? pd.get("S_AGENTBANKCLASS"):"")){
            query1+=" AND toString(S_AGENTBANKCLASS) =toString('"+pd.getString("S_AGENTBANKCLASS")+"')";
        }
        if(!"".equals(pd.containsKey("S_VOUCHERNO")? pd.get("S_VOUCHERNO"):"")){
            query1+=" AND S_VOUCHERNO like '%"+pd.getString("S_VOUCHERNO")+"%'";
        }
        if(!"".equals(pd.containsKey("S_BDGORGCODE")? pd.get("S_BDGORGCODE"):"")){
            query1+=" AND S_BDGORGCODE   like '%"+pd.getString("S_BDGORGCODE")+"%'";
        }
        if(!"".equals(pd.containsKey("S_BDGORGNAME")? pd.get("S_BDGORGNAME"):"")){
            query1+=" AND S_BDGORGNAME   like '%"+pd.getString("S_BDGORGNAME")+"%'";
        }
        if(!"".equals(pd.containsKey("S_ORIPAYEEACCTNO")? pd.get("S_ORIPAYEEACCTNO"):"")){
            query1+=" AND S_ORIPAYEEACCTNO like '%"+pd.getString("S_ORIPAYEEACCTNO")+"%'";
        }
        if(!"".equals(pd.containsKey("S_ORIPAYEEACCTNAME")? pd.get("S_ORIPAYEEACCTNAME"):"")){
            query1+=" AND S_ORIPAYEEACCTNAME like '%"+pd.getString("S_ORIPAYEEACCTNAME")+"%'";
        }
        if(!"".equals(pd.containsKey("S_EXPFUNCCODE")? pd.get("S_EXPFUNCCODE"):"")){
            query1+=" AND S_EXPFUNCCODE in ("+pd.getString("S_EXPFUNCCODE")+")";
        }
        if(!"".equals(pd.containsKey("S_EXPFUNCNAME")? pd.get("S_EXPFUNCNAME"):"")){
            query1+=" AND S_EXPFUNCNAME ='"+pd.getString("S_EXPFUNCNAME")+"'";
        }
        if(!"".equals(pd.containsKey("S_REMARK")? pd.get("S_REMARK"):"")){
            query1+=" AND S_REMARK like '%"+pd.getString("S_REMARK")+"%'";
        }

        //查询数据
        List<Map<String, Object>> data  = this.getSQLResults(query1);
        StringBuffer text = new StringBuffer();
        text.append("序号");
        text.append(",");
        text.append("委托日期");
        text.append(",");
        text.append("国库代码");
        text.append(",");
        text.append("国库名称");
        text.append(",");
        text.append("原代理银行");
        text.append(",");
        text.append("原支付方式");
        text.append(",");
        text.append("原支付凭证日期");
        text.append(",");
        text.append("原支付凭证编号");
        text.append(",");
        text.append("凭证编号");
        text.append(",");
        text.append("预算种类");
        text.append(",");
        text.append("原预算单位编码");
        text.append(",");
        text.append("原预算单位名称");
        text.append(",");
        text.append("功能科目编码");
        text.append(",");
        text.append("功能科目名称");
        text.append(",");
        text.append("原零余额账户账号");
        text.append(",");
        text.append("原零余额账户名称");
        text.append(",");
        text.append("原收款人账号");
        text.append(",");
        text.append("原收款人名称");
        text.append(",");
        text.append("原收款人开户行名称");
        text.append(",");
        text.append("退回类型");
        text.append(",");
        text.append("退回金额");
        text.append(",");
        text.append("摘要");
        text.append("\r\n");//换行字符
        if (null != data && data.size() > 0) {
            for (int i = 0; i < data.size(); i++) {
                Map<String, Object> temp = new HashMap<>();
                temp = data.get(i);
                text.append(temp.get("S_SEQNO") == null ? ' ':temp.get("S_SEQNO"));
                text.append(",");
                text.append(temp.get("S_ENTRUSTDATE") == null ? ' ':temp.get("S_ENTRUSTDATE"));
                text.append(",");
                text.append(temp.get("S_TRECODE") == null ? ' ':temp.get("S_TRECODE"));
                text.append(",");
                text.append(temp.get("S_TRENAME") == null ? ' ':temp.get("S_TRENAME"));
                text.append(",");
                text.append(temp.get("S_AGENTBANKNAME") == null ? ' ':temp.get("S_AGENTBANKNAME"));
                text.append(",");
                text.append(temp.get("S_PAYMODENAME") == null ? ' ':temp.get("S_PAYMODENAME"));
                text.append(",");
                text.append(temp.get("S_ORIPAYVOUDATE") == null ? ' ':temp.get("S_ORIPAYVOUDATE"));
                text.append(",");
                text.append(temp.get("S_ORIVOUCHERNO") == null ? ' ':temp.get("S_ORIVOUCHERNO"));
                text.append(",");
                text.append(temp.get("S_VOUCHERNO") == null ? ' ':temp.get("S_VOUCHERNO"));
                text.append(",");
                text.append(temp.get("S_PAYMODENAME") == null ? ' ':temp.get("S_PAYMODENAME"));
                text.append(",");
                text.append(temp.get("S_BDGORGCODE") == null ? ' ':temp.get("S_BDGORGCODE"));
                text.append(",");
                text.append(temp.get("S_BDGORGNAME") == null ? ' ':temp.get("S_BDGORGNAME"));
                text.append(",");
                text.append(temp.get("S_EXPFUNCCODE") == null ? ' ':temp.get("S_EXPFUNCCODE"));
                text.append(",");
                text.append(temp.get("S_EXPFUNCNAME") == null ? ' ':temp.get("S_EXPFUNCNAME"));
                text.append(",");
                text.append(temp.get("S_ORIZEROACCTNO") == null ? ' ':temp.get("S_ORIZEROACCTNO"));
                text.append(",");
                text.append(temp.get("S_ORIZEROACCTNAME") == null ? ' ':temp.get("S_ORIZEROACCTNAME"));
                text.append(",");
                text.append(temp.get("S_ORIPAYEEACCTNO") == null ? ' ':temp.get("S_ORIPAYEEACCTNO"));
                text.append(",");
                text.append(temp.get("S_ORIPAYEEACCTNAME") == null ? ' ':temp.get("S_ORIPAYEEACCTNAME"));
                text.append(",");
                text.append(temp.get("S_ORIPAYEEOPNBNKNO") == null ? ' ':temp.get("S_ORIPAYEEOPNBNKNO"));
                text.append(",");
                text.append("退款");
                text.append(",");
                text.append(temp.get("F_PAYAMT") == null ? ' ':temp.get("F_PAYAMT"));
                text.append(",");
                text.append(temp.get("S_REMARK") == null ? ' ':temp.get("S_REMARK"));
                text.append("\r\n");//换行字符
            }
        }
        exportTxt(response, text.toString(),"集中支付退款");

    }

    /* 导出txt文件
     * @author
     * @param	response
     * @param	text 导出的字符串
     * @return
     */
    public void exportTxt(HttpServletResponse response, String text, String fileName) {
        response.setCharacterEncoding("utf-8");
        //设置响应的内容类型
        response.setContentType("text/plain");
        //设置文件的名称和格式
        response.addHeader("Content-Disposition", "attachment;filename="
                + genAttachmentFileName(fileName, "1")//设置名称格式，没有这个中文名称无法显示
                + ".txt");
        BufferedOutputStream buff = null;
        ServletOutputStream outStr = null;
        try {
            outStr = response.getOutputStream();
            buff = new BufferedOutputStream(outStr);
            buff.write(text.getBytes("UTF-8"));
            buff.flush();
            buff.close();
        } catch (Exception e) {
            //LOGGER.error("导出文件文件出错:{}",e);
        } finally {
            try {
                buff.close();
                outStr.close();
            } catch (Exception e) {
                //LOGGER.error("关闭流对象出错 e:{}",e);
            }
        }
    }

    public String genAttachmentFileName(String cnName, String defaultName) {
        try {
            cnName = new String(cnName.getBytes("gb2312"), "ISO8859-1");
        } catch (Exception e) {
            cnName = defaultName;
        }
        return cnName;
    }



}

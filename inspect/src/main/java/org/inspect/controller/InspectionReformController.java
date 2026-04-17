package org.inspect.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFStyles;
import org.inspect.BaseController;
import org.inspect.service.*;
import org.inspect.util.DateUtil;
import org.inspect.util.FileDownload;
import org.inspect.util.PageData;
import org.inspect.util.TransNum;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@Api(tags="整改台账")
@RequestMapping(value = "/inspectionReformController", produces = MediaType.APPLICATION_JSON_VALUE)
public class InspectionReformController extends BaseController {

    @Autowired
    private InspectionReformService inspectionReformService;
    @Autowired
    private InspectionTaskService inspectionTaskService;
    @Autowired
    private InspectionProcSubService inspectionProcSubService;
    @Autowired
    private InspectionCaseService inspectionCaseService;
    @Autowired
    private InspectionApprovalService inspectionApprovalService;

    @Autowired
    private InspectionProcessControlService inspectionProcessControlService;

    @Value("${TEMPLATE_FILE_PATH}")
    private  String saveDir;

    @RequestMapping(value = {"/getInspectionReformPage"}, method = RequestMethod.POST)
    @ApiOperation("查(不分页)")
    public Object getInspectionReformPage(@RequestBody(required = false) JSONObject param){
        Map<String, Object> jsonMap = new HashMap<>();
        try{
            PageData pd = this.getPageData(param);
            List<Map<String, Object>> result = inspectionReformService.getInspectionReformPage(pd);
            jsonMap.put("rows", result);//rows键 存放每页记录 list
            jsonMap.put("result","success");
        }catch (Exception e){
            jsonMap.put("result","false");
            jsonMap.put("msg",e.getMessage());
        }
        return jsonMap;
    }

    @RequestMapping(value={"/getSchemeReplay"} , method = RequestMethod.POST)
    @ApiOperation("查整改方案以及相应回复")
    public Object getSchemeReplay(@RequestBody(required = false) JSONObject param){
        Map<String, Object> jsonMap = new HashMap<>();
        PageData pd = this.getPageData(param);
        try{
            List<Map<String,Object>> schemeMap  = inspectionReformService.getInspectionReformSchemeData(pd);
            if (null != schemeMap && schemeMap.size()>0){
                for(int i=0;i<schemeMap.size();i++){
                    PageData replayPd = new PageData();
                    replayPd.put("SCHEME_ID",schemeMap.get(i).get("ID"));
                    schemeMap.get(i).put("replayList",inspectionReformService.getInspectionReformReplayData(replayPd));
                }
            }
            jsonMap.put("rows",schemeMap);
            jsonMap.put("result","success");
        }catch (Exception e){
            jsonMap.put("result","false");
            jsonMap.put("msg",e.getMessage());
        }
        return jsonMap;
    }

    @RequestMapping(value={"/getInspectionReformData"} , method = RequestMethod.POST)
    @ApiOperation("查(不分页)")
    public Object getInspectionReformData(@RequestBody(required = false) JSONObject param){
        Map<String, Object> jsonMap = new HashMap<>();
        try{
            PageData pd = this.getPageData(param);
            jsonMap.put("rows", inspectionReformService.getInspectionReformData(pd));
            jsonMap.put("result","success");
        }catch (Exception e){
            jsonMap.put("result","false");
            jsonMap.put("msg",e.getMessage());
        }
        return jsonMap;
    }

    @RequestMapping(value={"/delInspectionReformScheme"} , method = RequestMethod.GET)
    @ApiOperation("删除方案")
    public Map<String,Object>delInspectionReformScheme(@RequestBody(required = false) JSONObject param){
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        result.put("msg", "保存成功！");
        result.put("result", "success");
        try{
            String ID = pd.getString("ID");
            if (null != ID && !ID.equals("")){
                String []IDS = ID.split(",");
                for (int i = 0;i<IDS.length;i++){
                    PageData temp = new PageData();
                    temp.put("ID",IDS[i]);
                    inspectionReformService.delInspectionReformScheme(temp);
                    inspectionReformService.delInspectionReformReplay(temp);
                }
            }
        }catch (Exception e){
            result.put("msg", e.getMessage());
            result.put("result", "false");
        }
        return result;
    }

    @RequestMapping(value={"/delInspectionReformReplay"} , method = RequestMethod.GET)
    @ApiOperation("删除回复")
    public Map<String,Object>delInspectionReformReplay(@RequestBody(required = false) JSONObject param){
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        result.put("msg", "保存成功！");
        result.put("result", "success");
        try{
            String ID = pd.getString("ID");
            if (null != ID && !ID.equals("")){
                String []IDS = ID.split(",");
                for (int i = 0;i<IDS.length;i++){
                    PageData temp = new PageData();
                    temp.put("ID",IDS[i]);
                    inspectionReformService.delInspectionReformReplay(temp);
                }
            }
        }catch (Exception e){
            result.put("msg", e.getMessage());
            result.put("result", "false");
        }
        return result;
    }

    @RequestMapping(value = "/editReform")
    @ApiOperation("修改")
    public Map<String,Object>editReform(@RequestBody(required = false) JSONObject param){
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        result.put("msg", "修改成功！");
        result.put("result", "success");
        try{
            pd.put("COMPLETE_TIME",DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
            String id = pd.getString("ID");
            if (null != id && !id.equals("")){
                String ids[] = id.split(",");
                for (String i :ids){
                    pd.put("ID",i);
                }
            }
            inspectionReformService.editReform(pd);
        }catch (Exception e){
            result.put("msg", e.getMessage());
            result.put("result", "false");
        }
        return result;
    }

    @RequestMapping(value = "addInspectionScheme")
    @ApiOperation("新增方案")
    public Map<String,Object> addInspectionScheme(@RequestBody(required = false) JSONObject param){
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        result.put("result", "success");
        result.put("msg", "新增成功！");
        try {
            String REFORM_ID = null == pd.getString("REFORM_ID")?"":pd.getString("REFORM_ID");
            String [] rids = REFORM_ID.split(",");
            for (String id : rids){
                pd.put("ID",get32UUID());
                pd.put("REFORM_ID",id);
                pd.put("SCHEME_TIME", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
                inspectionReformService.addInspectionScheme(pd);

                PageData refPd = new PageData();
                refPd.put("ID",id);
                refPd.put("IS_SCHEME","0");
                refPd.put("IS_COMPLETE","2");
                inspectionReformService.editIsScheme(refPd);
            }

            List<Map<String,Object>> map = inspectionReformService.isComplete(pd);
            if(null == map || map.isEmpty()){
                String id = pd.getString("PROC_ID");
                pd.put("ID",id);
                pd.put("INSPECTION_PROCESS_SUB_SIGN","0");
                pd.put("IS_ACTIVE","0");
                pd.put("FINISH_TIME",DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
                inspectionProcSubService.editInspectionProcSub(pd);

                String procCode = pd.getString("PROC_CODE").substring(0, 3);
                if( "009".equals(procCode) ){
                    PageData subPd = new PageData();
                    subPd.put("FINISH_TIME",DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
                    subPd.put("PROC_SUB_ID",pd.getString("PROC_CODE"));
                    subPd.put("TASK_ID",pd.getString("TASK_ID"));
                    subPd.put("PROC_ID",pd.getString("PROC_CODE").substring(0, 5));
                    //临时检查整改台账完成
                    inspectionProcessControlService.finishCurSubProcessById(subPd);
                    //激活后续子流程
                    inspectionProcessControlService.activateFollowProcessSub(subPd);
                    //临时检查汇总表激活完成
                    subPd.put("PROC_SUB_ID", "0090402");
                    inspectionProcessControlService.finishCurSubProcessById(subPd);
                    //临时检查整改主流程完成
                    //inspectionProcessControlService.activateFollowProc(subPd);
                } else {
                    //激活整改报告
                    List<Map<String, Object>> subMap = inspectionProcSubService.getInspectionProcSubData(pd);
                    if (null != subMap && !subMap.isEmpty()){
                        PageData procPd = new PageData();
                        procPd.put("PROCESS_ID",subMap.get(0).get("PROCESS_ID"));
                        List<Map<String, Object>> tempMap = inspectionProcSubService.getInspectionProcSubData(procPd);
                        if (tempMap.size() >= 2){
                            if (
                                    (tempMap.get(1).get("INSPECTION_PROCESS_SUB_NAME").toString().indexOf("整改报告")>-1)
                                    ||
                                    (tempMap.get(1).get("INSPECTION_PROCESS_SUB_NAME").toString().indexOf("自查报告")>-1)
                                    ) {
                                PageData tempPd = new PageData();
                                tempPd.put("ID", tempMap.get(1).get("ID"));
                                tempPd.put("IS_ACTIVE", "0");
                                inspectionProcSubService.editInspectionProcSub(tempPd);
                            }
                        }
                    }
                }
            }

        }catch (Exception e){
            result.put("msg", e.getMessage());
            result.put("result", "false");
            return result;
        }
        return result;
    }

    @RequestMapping(value = "addInspectionReplay")
    @ApiOperation("新增回复")
    public Map<String,Object> addInspectionReplay(@RequestBody(required = false) JSONObject param){
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        result.put("result", "success");
        result.put("msg", "新增成功！");
        try {
            pd.put("ID",get32UUID());
            pd.put("REPLY_TIME", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
            inspectionReformService.addInspectionReplay(pd);
        }catch (Exception e){
            result.put("msg", e.getMessage());
            result.put("result", "false");
            return result;
        }
        return result;
    }

    @RequestMapping(value = "/downReform")
    @ApiOperation("下载整改台账")
    public void downReform(@RequestBody(required = false) JSONObject param,HttpServletResponse response) throws Exception {
        PageData pd = this.getPageData(param);
        String outPath = saveDir+pd.getString("TASK_ID")+"/"+pd.getString("PROC_ID")+"/Word.docx";
        String title = "";
        List<Map<String, Object>> dataMap = inspectionReformService.getReformData(pd);
        if (null == dataMap || dataMap.isEmpty()){
            return;
        }
        pd.put("logic", "notEq");
        List<Map<String,Object>> oneMap = inspectionReformService.getQuestionLedgerLvOne(pd);
        List<Map<String,Object>> twoMap = inspectionReformService.getQuestionLedgerLvById(pd);
        if (null == oneMap || oneMap.isEmpty()){
            return;
        }else{
            XWPFDocument template = new XWPFDocument(new FileInputStream(saveDir+"template/Word.docx"));
            CTStyles wordStyles = template.getStyle();
            // 新建的word文档对象
            XWPFDocument doc = new XWPFDocument();
            // 获取新建文档对象的样式
            XWPFStyles newStyles = doc.createStyles();
            // 关键行// 修改设置文档样式为静态块中读取到的样式
            newStyles.setStyles(wordStyles);
            // 开始内容输入
            // 标题
            XWPFParagraph para0 = doc.createParagraph();
            // 关键行// 1级大纲
            para0.setStyle("1");
            XWPFRun run0 = para0.createRun();
            // 标题内容
            pd.put("INSPECTION_TASK_ID",pd.getString("TASK_ID"));
            List<Map<String,Object>> taskMap = inspectionTaskService.getInspectionTaskData(pd);
            if (null == taskMap || taskMap.isEmpty()){
                run0.setText("一、整改情况");
                title = "整改情况";
            }else{
                run0.setText("一、" + taskMap.get(0).get("INSPECTION_TASK_TYPE_DSCR").toString()+"整改情况");
                title = taskMap.get(0).get("INSPECTION_TASK_TYPE_DSCR").toString()+"整改情况";
            }
            for (int i = 0;i < oneMap.size();i++){
                // 标题1，1级大纲
                XWPFParagraph para1 = doc.createParagraph();
                // 关键行// 1级大纲
                para1.setStyle("2");
                XWPFRun run1 = para1.createRun();
                // 标题内容
                TransNum temp = new TransNum();
                String index = temp.cvt(i+1,true);
                run1.setText("("+index+")" + oneMap.get(i).get("QUESTION_DSCR_1").toString());
                int num = 0;
                for (int j=0;j<twoMap.size();j++){
                    if(twoMap.get(j).get("QUESTION_ID_1").toString().equals(oneMap.get(i).get("QUESTION_ID_1").toString())){
                        num = num + 1;
                        // 标题2
                        XWPFParagraph para2 = doc.createParagraph();
                        // 关键行// 2级大纲
                        para2.setStyle("3");
                        XWPFRun run2 = para2.createRun();
                        // 标题内容
                        run2.setText(num+"、" + twoMap.get(j).get("QUESTION_DSCR_2").toString());
                        int num2 = 0;
                        for (int k = 0;k<dataMap.size();k++){
                            if (dataMap.get(k).get("QUESTION_ID_2").toString().equals(twoMap.get(j).get("QUESTION_ID_2").toString())){
                                num2 = num2 + 1;
                                // 标题3
                                XWPFParagraph para3 = doc.createParagraph();
                                // 关键行//
                                para3.setStyle("4");
                                XWPFRun run3 = para3.createRun();
                                // 标题内容
                                run3.setText("("+num2+")"+"针对“" + dataMap.get(k).get("QUESTION_CONTENT").toString() + "”的问题");

                                String content = "";
                                content = "整改方案："+dataMap.get(k).get("REFORM_SCHEME").toString();
                                // 正文
                                XWPFParagraph paraX = doc.createParagraph();
                                XWPFRun runX = paraX.createRun();
                                // 正文内容
                                runX.setText(content);
                                runX.addCarriageReturn();

                                content="说明："+dataMap.get(k).get("MEMO").toString();
                                XWPFParagraph paraY = doc.createParagraph();
                                XWPFRun runY = paraY.createRun();
                                // 正文内容
                                runY.setText(content);
                                runY.addCarriageReturn();

                            }
                        }
                    }
                }
            }
            File file = new File(outPath);
            if (!file.exists()){
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            doc.write(fos);
            fos.close();
        }
        FileDownload.fileDownload(response, outPath, title+".docx",this.getRequest());
    }

    @RequestMapping(value = "toAddCase")
    @ApiOperation("新增案例库")
    public Map<String,Object> toAddCase(@RequestBody(required = false) JSONObject param){
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        result.put("result", "success");
        result.put("msg", "提交成功！");
        try {
            inspectionReformService.editReform(pd);
            List<Map<String,Object>>caseMap = inspectionReformService.toAddCase(pd);
            if (null == caseMap || caseMap.isEmpty()){
                result.put("msg", "未查询到相关信息，提交失败");
                result.put("result", "false");
                return result;
            }
            pd.put("ID",get32UUID());
            pd.put("ADD_TIME", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
            pd.putAll(caseMap.get(0));
            //新增案例库
            inspectionCaseService.addInspectionCase(pd);
            PageData pd1=new PageData();
            pd1.put("type",1);
            List<Map<String,Object>> list=inspectionApprovalService.getAppravalProcess(pd1);
            if(!list.isEmpty() && list.size()>0){
                pd1.put("id",get32UUID());
                pd1.put("auth_id",get32UUID());
                pd1.put("subject_id",pd.get("ID"));
                pd1.put("add_user",pd.get("ADD_USER"));
                pd1.put("add_time",pd.get("ADD_TIME"));
                pd1.put("app_role",list.get(0).get("role"));
                pd1.put("app_step",list.get(0).get("step"));
                pd1.put("app_org",list.get(0).get("organ"));
                inspectionApprovalService.addInspectionApproval(pd1);
            }
        }catch (Exception e){
            e.printStackTrace();
            result.put("msg", e.getMessage());
            result.put("result", "false");
            return result;
        }
        return result;
    }
}

package org.fixedReport.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
//@PropertySource("classpath:root/application.properties")
public class SaticScheduleTask {

    @Autowired
    private TreasuryAccessService treasuryAccessService;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${httpPage}")
    private int httpPage;

    //3.添加定时任务
    //或直接指定时间间隔，例如：5秒
//    @Scheduled(cron = "0 */1 * * * ?")
    //@Scheduled(fixedRate=5000)
//    @Scheduled(cron = "0/1 * * * * ?")
    private void configureTasks() {
//        List<Map<String,Object>> all1 = mysqlService.findAll();
//        List<Map<String,Object>> all2 = houseService.findAll();
        System.err.println("执行静态定时任务时间: " + LocalDateTime.now());
    }
    //  获取token
//    @Scheduled(cron = "0/10 * * * * ?")
    private String accessList(){
        String alarmUrl = "http://11.8.165.25:8000/FDSP/services/getToken?loginName=gkcadmin&loginPassword=gkcadmin";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<Map<String, Object>> alarmList = new ArrayList<>();
        ResponseEntity<String> responseEntity = restTemplate.exchange(alarmUrl, HttpMethod.POST, null, String.class);
        String token="";
        if (responseEntity.getStatusCodeValue() == 200) {
            JSONObject jsonObject = JSONObject.parseObject(responseEntity.getBody());
            Map<String, Object> records = (Map<String, Object>) jsonObject;
            if (records !=  null) {
                String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                token = records.get("message").toString();
            }
        } else {
            log.error("获取token取数失败：[" + alarmUrl + "]");
        }
        return  token;
    }

    // 三证合一_个体开业、注销信息  接口信息
//    @Scheduled(cron = "0/60 * * * * ?") // 0 0 1 * * ?   //  测试  时间
//    @Scheduled(cron = "0 30 16 * * ?") // 0 0 1 * * ?  // 正式跑数  时间
    @Scheduled(cron = "${cronThreeCertificates}") // 0 0 1 * * ?  // 正式跑数  时间
    private void threeCertificatesSyncretic(){
        String token = accessList();
        String alarmUrl = "http://11.8.165.25:8000/FDSP/services/getData/ff8080817490ac9a0174afb669360151/1/1";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar ca1 = Calendar.getInstance();//得到一个Calendar的实例
        ca1.setTime(new Date()); //设置时间为当前时间
//        ca.add(Calendar.YEAR, -1); //年份减1
//        求前一月ca.add(Calendar.MONTH, -1)，
        ca1.add(Calendar.DATE, -2);
        Date lastMonth1 = ca1.getTime(); //结果
        String dataDte1 = dateFormat.format(lastMonth1)+" 00:00:00";
        String dataDte2 = dateFormat.format(lastMonth1)+" 23:59:59";

//        String dataBody = "[{'name':'INFODATE','match':'gte','value':'"+dataDte1+"'},{'name':'INFODATE','match':'lte','value':'"+dataDte2+"'}]";
        // header填充
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE));
        headers.set("Authorization",token);
        //放入body中的json参数
        JSONObject obj = new JSONObject();
//        obj.put("paramList", JSONArray.parseArray(dataBody));
        //组装
        HttpEntity<JSONObject> request = new HttpEntity<>(obj, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(alarmUrl, HttpMethod.GET, request, String.class);
        if (responseEntity.getStatusCodeValue() == 200) {
            int count = 0;
            JSONObject jsonObject = JSONObject.parseObject(responseEntity.getBody());
            Map<String, Object> record = (Map<String, Object>) jsonObject.get("data");

            if (record != null) {
                System.err.println("获取三证合一总条数: " + record.get("total").toString());
                treasuryAccessService.delThreeCertificatesSyncretic();
                for(int i=1;i<=(Integer.parseInt(record.get("total").toString())/httpPage+1);i++){
                    List<Map<String, Object>> alarmList = new ArrayList<>();
//                for(int i=1;i<2;i++){
                    String url = "http://11.8.165.25:8000/FDSP/services/getData/ff8080817490ac9a0174afb669360151/"+i+"/"+httpPage;
//                    ResponseEntity<String> responseEntitys = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
                    ResponseEntity<String> responseEntitys = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
                    if (responseEntitys.getStatusCodeValue() == 200) {
                        JSONObject jsonObjects = JSONObject.parseObject(responseEntitys.getBody());
                        Map<String, Object> records = (Map<String, Object>) jsonObjects.get("data");
                        if(records != null && records.get("list") != null){
                            List<Map<String, Object>> list = (List<Map<String,Object>>) JSONArray.parse(records.get("list").toString());
                            if (list.size() > 0) {
                                list.forEach(map -> {
//                                    System.err.println("获取三证合一总条数: " + map);
                                    alarmList.add(map);
                                });
                                count += alarmList.size();
                                System.err.println("三证合一信息入库"+alarmList.size()+"条：[" + url + "]"+"---token="+token+"===dataDte1="+dataDte1);
                                treasuryAccessService.insertThreeCertificatesSyncretic(alarmList); // mysql  入库
//                            treasuryAccessHouseService.insertThreeCertificatesSyncreticHouse(alarmList); // clickhouse  入库
                            }else{
                                log.error("获取三证合一信息取数失败第"+i+"页：[" + url + "]"+"---token="+token);
                            }
                        }else{
                            log.error("获取三证合一信息取数失败第"+i+"页：[" + url + "]"+"---token="+token);
                        }
                    } else {
                        log.error("获取三证合一信息取数失败：[" + alarmUrl + "]"+"---token="+token);
                    }
                }
                // 调用存储过程
                treasuryAccessService.callThreeCertificatesSyncretic();
                log.debug("获取三证合一信息取数成功：[" + count + "]条");
            }
        } else {
            log.error("三证合一_个体开业、注销信息取数失败：[" + alarmUrl + "]");
        }
    }

    // 企业基本信息-多证合一数据  接口信息
//    @Scheduled(cron = "0/5 * * * * ?")
//    @Scheduled(cron = "0 50 * * * ?") // 测试
//    @Scheduled(cron = "0 30 17 * * ?") // 每天凌晨7点执行一次
    @Scheduled(cron = "${cronEnterprise}") // 每天凌晨7点执行一次
    private void enterpriseBasicInformation(){
        String token = accessList();
        String alarmUrl = "http://11.8.165.25:8000/FDSP/services/getData/ff8080817490ac9a0174afbb00ed018a/1/1";

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar ca1 = Calendar.getInstance();//得到一个Calendar的实例
        ca1.setTime(new Date()); //设置时间为当前时间
//        ca.add(Calendar.YEAR, -1); //年份减1
//        求前一月ca.add(Calendar.MONTH, -1)，
        ca1.add(Calendar.DATE, -2);
        Date lastMonth1 = ca1.getTime(); //结果
        String dataDte1 = dateFormat.format(lastMonth1)+" 00:00:00";
        String dataDte2 = dateFormat.format(lastMonth1)+" 23:59:59";
        // header填充
        String dataBody = "[{'name':'INFODATE','match':'gte','value':'"+dataDte1+"'},{'name':'INFODATE','match':'lte','value':'"+dataDte2+"'}]";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE));
        headers.set("Authorization",token);
        //放入body中的json参数
        JSONObject obj = new JSONObject();
        obj.put("paramList", JSONArray.parseArray(dataBody));
        //组装
        HttpEntity<JSONObject> request = new HttpEntity<>(obj, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(alarmUrl, HttpMethod.GET, request, String.class);
        if (responseEntity.getStatusCodeValue() == 200) {
            int count = 0;
            JSONObject jsonObject = JSONObject.parseObject(responseEntity.getBody());
            Map<String, Object> record = (Map<String, Object>) jsonObject.get("data");

            if (record != null) {
                System.err.println("获取企业信息总条数: " + record.get("total").toString());
                treasuryAccessService.delEnterpriseBasicInformation();
                for(int i=1;i<=(Integer.parseInt(record.get("total").toString())/httpPage+1);i++){
                    List<Map<String, Object>> alarmList = new ArrayList<>();
                    String url = "http://11.8.165.25:8000/FDSP/services/getData/ff8080817490ac9a0174afbb00ed018a/"+i+"/"+httpPage;
                    ResponseEntity<String> responseEntitys = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
                    if (responseEntitys.getStatusCodeValue() == 200) {
                        JSONObject jsonObjects = JSONObject.parseObject(responseEntitys.getBody());
                        Map<String, Object> records = (Map<String, Object>) jsonObjects.get("data");
                        if(records != null && records.get("list") != null){
                            List<Map<String, Object>> list = (List<Map<String,Object>>) JSONArray.parse(records.get("list").toString());
                            if (list.size() > 0) {
                                list.forEach(map -> {
                                    alarmList.add(map);
                                });
                                count += alarmList.size();
                                treasuryAccessService.insertEnterpriseBasicInformation(alarmList);
                                System.err.println("获取企业信息"+alarmList.size()+"条：[" + url + "]"+"---token="+token);
//                                treasuryAccessHouseService.insertEnterpriseBasicInformationHouse(alarmList);
                            }else{
                                log.error("获取企业信息失败第"+i+"页：[" + url + "]"+"---token="+token);
                            }
                        }else{
                            log.error("获取企业信息失败第"+i+"页：[" + url + "]"+"---token="+token);
                        }
                    } else {
                        log.error("获取企业信息失败第：[" + alarmUrl + "]"+"---token="+token);
                    }
                }
                // 调用存储过程
                treasuryAccessService.callEnterpriseBasicInformation();
//                count += alarmList.size();
//                treasuryAccessService.insertEnterpriseBasicInformation(alarmList);
                System.err.println("获取企业信息取数成功：[" + count + "]条");
            }
        } else {
            log.error("企业基本信息-多证合一数据取数失败：[" + alarmUrl + "]");
        }
    }

    // 非企业团体基本信息 接口信息
//    @Scheduled(cron = "0/120 * * * * ?") // 每天凌晨1点执行一次
    @Scheduled(cron = "0 0 13 * * ?") // 每天13点执行一次
    private void nonEnterpriseBasicInformation(){
        String token = accessList();
//        String alarmUrl = "http://11.8.165.25:8000/FDSP/services/getData/ff8080817490ac9a0174afb669360151/1/10000";
        String alarmUrl = "http://11.8.165.25:8000/FDSP/services/getData/ff8080817490ac9a0174afca4ede01b9/1/1";

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // header填充
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization",token);
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(alarmUrl, HttpMethod.GET, entity, String.class);
        if (responseEntity.getStatusCodeValue() == 200) {
            int count=0;
            JSONObject jsonObject = JSONObject.parseObject(responseEntity.getBody());
            Map<String, Object> record = (Map<String, Object>) jsonObject.get("data");
            if (record.size() > 0) {
                System.err.println("获取非企业团体基本信息总条数: " + record.get("total").toString());
                treasuryAccessService.delNonEnterpriseBasicInformation();
                for(int i=1;i<=(Integer.parseInt(record.get("total").toString())/10+1);i++){
                    List<Map<String, Object>> alarmList = new ArrayList<>();
                    String url = "http://11.8.165.25:8000/FDSP/services/getData/ff8080817490ac9a0174afca4ede01b9/"+i+"/10";
                    ResponseEntity<String> responseEntitys = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
                    if (responseEntitys.getStatusCodeValue() == 200) {
                        JSONObject jsonObjects = JSONObject.parseObject(responseEntitys.getBody());
                        Map<String, Object> records = (Map<String, Object>) jsonObjects.get("data");
                        if(records != null && records.get("list") != null){
                            List<Map<String, Object>> list = (List<Map<String,Object>>) JSONArray.parse(records.get("list").toString());
                            if (list != null) {
                                list.forEach(map -> {
                                    alarmList.add(map);
                                });
                                count += alarmList.size();
                                treasuryAccessService.insertNonEnterpriseBasicInformation(alarmList);
//                            treasuryAccessHouseService.insertNonEnterpriseBasicInformationHouse(alarmList);
                            }else{
                                log.error("非企业团体基本信息取数失败第"+i+"页：[" + url + "]"+"---token="+token);
                            }
                        }else{
                            log.error("非企业团体基本信息取数失败第"+i+"页：[" + url + "]"+"---token="+token);
                        }
                    } else {
                        log.error("非企业团体基本信息取数失败：[" + alarmUrl + "]"+"---token="+token);
                    }
                }
                // 调用存储过程
                treasuryAccessService.callNonEnterpriseBasicInformation();
                System.err.println("非企业团体基本信息取数成功：[" + count + "]条");
            }
        } else {
            log.error("非企业团体基本信息取数失败：[" + alarmUrl + "]");
        }
    }

}

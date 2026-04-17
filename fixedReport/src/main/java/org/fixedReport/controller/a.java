package org.fixedReport.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;

public class a {

    // 不验证URL anon：不验证/authc：受控制的
    public static final String NO_INTERCEPTOR_PATH =".*(.css)|.*(.js)";
//    public static final String NO_INTERCEPTOR_PATH =".*.html";

    public static void main(String[] args){

//        String strArr = "[{'0':'zhangsan','1':'lisi','2':'wangwu','3':'maliu'}," +
//                "{'00':'zhangsan','11':'lisi','22':'wangwu','33':'maliu'}]";

        String strArr = "[{'0':'zhangsan','1':'lisi'}]";
//        String industryId="["+ pd.get("industryId").toString().replaceAll("\"","'")+"]";
//        第一种方式
//         String industryId="["+ pd.get("industryId").toString().replaceAll("\"","'")+"]";
         List<Map<String,String>> listObjectFir = (List<Map<String,String>>) JSONArray.parse(strArr);
        System.out.println("第一种方式利用JSONArray中的parse方法来解析json数组字符串");
        for(Map<String,String> mapList : listObjectFir){
            for (Map.Entry entry : mapList.entrySet()){
                System.out.println( entry.getKey()  + "  " +entry.getValue());
            }
        }
        //第二种方式
        List<Map<String,String>> listObjectSec = JSONArray.parseObject(strArr,List.class);
        System.out.println("第2种方式利用JSONArray中的parseObject方法并指定返回类型来解析json数组字符串");
        for(Map<String,String> mapList : listObjectSec){
            for (Map.Entry entry : mapList.entrySet()){
                System.out.println( entry.getKey()  + "  " +entry.getValue());
            }
        }
        //第三种方式
        JSONArray listObjectThir = JSONArray.parseArray(strArr);
        System.out.println("第3种方式利用JSONArray中的parseArray方法来解析json数组字符串");
        for(Object mapList : listObjectThir){
            for (Object entry : ((Map)mapList).entrySet()){
                System.out.println(((Map.Entry)entry).getKey()  + "  " +((Map.Entry)entry).getValue());
            }
        }
        //第四种方式
        List listObjectFour = JSONArray.parseArray(strArr,Map.class);
        System.out.println("第4种方式利用JSONArray中的parseArray方法并指定返回类型来解析json数组字符串");
        for(Object mapList : listObjectFour){
            for (Object entry : ((Map)mapList).entrySet()){
                System.out.println(((Map.Entry)entry).getKey()  + "  " +((Map.Entry)entry).getValue());
            }
        }
        //第五种方式
        JSONArray listObjectFifth = JSONObject.parseArray(strArr);
        System.out.println("第5种方式利用JSONObject中的parseArray方法来解析json数组字符串");
        for(Object mapList : listObjectFifth){
            for (Object entry : ((Map)mapList).entrySet()){
                System.out.println(((Map.Entry)entry).getKey()  + "  " +((Map.Entry)entry).getValue());
            }
        }
        //第六种方式
        List listObjectSix = JSONObject.parseArray(strArr,Map.class);
        System.out.println("第6种方式利用JSONObject中的parseArray方法并指定返回类型来解析json数组字符串");
        for(Object mapList : listObjectSix){
            for (Object entry : ((Map)mapList).entrySet()){
                System.out.println(((Map.Entry)entry).getKey()  + "  " +((Map.Entry)entry).getValue());
            }
        }
        //第八种方式
        List listObjectEigh = JSONObject.parseArray(strArr,Map.class);
        System.out.println("第8种方式利用JSON中的parseArray方法并指定返回类型来解析json数组字符串");
        for(Object mapList : listObjectEigh){
            for (Object entry : ((Map)mapList).entrySet()){
                System.out.println(((Map.Entry)entry).getKey()  + "  " +((Map.Entry)entry).getValue());
            }
        }
    }
}

package org.dockingProjects.utils;

import org.springframework.http.HttpMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * @author haojiang.
 * @Ddate 2020/8/18 18:35
 * @Description
 */
public class HttpClinetUtil {

    /**
     * HttpMethod请求类型
     */
    public static Map<String, HttpMethod> getHttpMethod() {
        Map<String, HttpMethod> methodMap = new HashMap<>();
        methodMap.put("1", HttpMethod.POST);
        methodMap.put("2", HttpMethod.GET);
        methodMap.put("3", HttpMethod.PUT);
        methodMap.put("4", HttpMethod.DELETE);
        return methodMap;
    }

}

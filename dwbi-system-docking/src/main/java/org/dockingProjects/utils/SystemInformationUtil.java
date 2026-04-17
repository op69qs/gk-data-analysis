package org.dockingProjects.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.dockingProjects.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author HaoJiang.
 * @Ddate 2020/8/13 14:18
 * 读取系统配置信息，存放到redis缓存
 */
@Slf4j
@Component
public class SystemInformationUtil implements ApplicationRunner {

    private String processId;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ConfigService configService;

    //创建线程池读取配置信息
    private ExecutorService service = Executors.newFixedThreadPool(4);

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        if (applicationArguments.containsOption("processId")) {
            processId = applicationArguments.getOptionValues("processId").get(0);
            SystemInformationSheduleds(); //读取系统、平台、资源、资源IP配置
            SystemInterfaceSheduleds(); //读取接口配置
        } else {
            processSheduleds(); //读取进程、线程配置
        }
        service.shutdown(); //关闭线程池
    }

    public void processSheduleds() {
        service.execute(() -> {
            saveProcessConfigToRedis();
        });
    }

    public void SystemInformationSheduleds() {
        service.execute(() -> {
            saveSystemInformationToRedis();
        });
    }

    public void SystemInterfaceSheduleds() {
        service.execute(() -> {
            saveInterfaceConfigToRedis();
        });
    }

    /**
     * 将进程相关配置信息放入redis
     */
    public void saveProcessConfigToRedis() {
        log.info("开始读取进程、线程的配置......");
        //查询进程配置信息并放入redis
        List<Map<String, Object>> processList = configService.getProcessConfig();
        Map<String, Object> processMap = new HashMap<>();
        processMap.put("processMap", processList);
        redisUtil.hmset("PROCESS_LIST", processMap);
        processList.forEach(processConfig -> {
            redisUtil.hmset("PROCESS_" + processConfig.get("ID"), processConfig);
            redisUtil.set("CORE_POOL_SIZE_" + processConfig.get("ID"), processConfig.get("task_thread_count")); //设置线程个数

            //根据进程查询进程中的线程配置并放入redis
            configService.getProcessThreadConfig(processConfig.get("ID").toString()).forEach(threadConfig -> {
                redisUtil.hmset("THREAD_" + processConfig.get("ID"), threadConfig);
                redisUtil.set("CRON_" + processConfig.get("ID"), threadConfig.get("task_thread_cron"));
            });
        });
        //读取完进程、线程的配置后设置读取状态
        redisUtil.set("PROCESS_CONFIG", "OK");
        log.info("进程、线程的配置读取完毕......");
    }

    /**
     * 将接口系统、平台、指标、资源、IP配置信息放入redis
     */
    public void saveSystemInformationToRedis() {
        log.info("开始读取接口系统、平台、指标、资源、IP配置......");
        //查询对接系统配置信息并放入redis
        List<Map<String, Object>> systemList = configService.getSystemConfig(processId);
        //判断查出的数据是否为空数据
        if (systemList != null && systemList.size() == 1) {
            systemList = systemList.get(0).get("ID") == null ? new ArrayList<>() : systemList;
        }
        redisUtil.lSet("SYSTEM_" + processId, systemList);
        systemList.forEach(systemConfig -> {
            //根据系统ID查询对应的平台配置信息并放入redis
            List<Map<String, Object>> platformList = configService.getPlatformConfig(systemConfig.get("ID").toString());
            redisUtil.lSet("PLATFORM_" + systemConfig.get("ID"), platformList);
            platformList.forEach(platformConfig -> {
                //根据平台ID查询对应的指标并放入redis
                redisUtil.lSet("TARGET_" + platformConfig.get("ID"), configService.getTargetConfig(platformConfig.get("ID").toString()));

                //根据平台ID查询对应的资源配置信息并放入redis
                List<Map<String, Object>> resourceList = configService.getResourcesConfig(platformConfig.get("ID").toString());
                redisUtil.lSet("RESOURCES_" + platformConfig.get("ID"), resourceList);
                resourceList.forEach(resourceConfig -> {
                    //根据资源ID查询对应的IP配置信息并放入redis
                    redisUtil.lSet("IP_" + resourceConfig.get("ID"), configService.getResourcesIPConfig(resourceConfig.get("ID").toString()));
                });
            });
        });
        //读取完对接系统相关的配置后设置读取状态
        redisUtil.set("SYSTEM_CONFIG_" + processId, "OK");
        log.info("接口系统、平台、指标、资源、资源IP配置读取完毕......");
    }

    /**
     * 将请求接口配置信息放入redis
     */
    public void saveInterfaceConfigToRedis() {
        log.info("开始读取请求接口的配置......");
        //根据进程ID查询对应的接口，并根据不同类型的系统将接口配置并放入redis
        List<Map<String, Object>> interfaceList = configService.getInterfaceConfig(processId);
        //判断查出的数据是否为空数据
        if (interfaceList != null && interfaceList.size() == 1) {
            interfaceList = interfaceList.get(0).get("ID") == null ? new ArrayList<>() : interfaceList;
        }
        for (int i = 0; i < interfaceList.size(); i++) {
            Map<String, Object> map = interfaceList.get(i);
            //封装接口请求类型
            interfaceList.get(i).put("httpMethod", HttpClinetUtil.getHttpMethod().get(map.get("httpMethod")));

            //封装HttpHeaders请求头
            if (!("".equals(map.get("headers")) || map.get("headers") == null)) {
                HttpHeaders headers = new HttpHeaders();
                JSON.parseObject("{" + map.get("headers") + "}").forEach((headerName, headerValue) -> {
                    headers.add(headerName, headerValue.toString());
                });
                interfaceList.get(i).put("headers", headers);
            }

            //封装接口参数
            if (!("".equals(map.get("paramaters")) || map.get("paramaters") == null)) {
                interfaceList.get(i).put("paramaters", JSON.parseObject("{" + map.get("paramaters") + "}"));
            } else {
                interfaceList.get(i).put("paramaters", new JSONObject());
            }
        }
        redisUtil.lSet("INTERFACE_" + processId, interfaceList);
        //读取完进程、线程的配置后设置读取状态
        redisUtil.set("INTERFACE_CONFIG_" + processId, "OK");
        log.info("请求接口的配置读取完毕......");
    }

}


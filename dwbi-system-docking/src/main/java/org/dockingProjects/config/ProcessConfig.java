package org.dockingProjects.config;

import com.sun.jna.Platform;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dockingProjects.utils.ApiTaskUtil;
import org.dockingProjects.utils.ProcessUtils;
import org.dockingProjects.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.*;
import java.net.URLDecoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author HaoJiang.
 * @Ddate 2020/8/4 11:26
 */
@Slf4j
//@Component
@Order(99)
public class ProcessConfig implements ApplicationRunner {

    private String processId;

    @Value("${queryDay}")
    private String queryDay;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ApiTaskUtil apiTaskUtil;

    private String jarPath;

    @Autowired
    private ProcessUtils processUtils;

    private static Map<String, Process> liveProcess = new HashMap();
    private static Map<String, String> procIdMap = new HashMap();

    public void startProcess(Map<String, Object> map) {
        //先查询是否已经运行
        String processId = String.valueOf(map.get("ID"));
        Map<Object, Object> statusMap = redisUtil.hmget("PROCESS_STATUS");

        if (!liveProcess.containsKey(processId) || "-1".equals(statusMap.get(processId))) { //不存在才创建
            try {
                log.info("启动子进程【进程ID：" + processId + "，进程名：" + map.get("task_name") + "】");
                String startParams = map.get("task_start_params") != null ? " " + map.get("task_start_params") : " ";
                String startPostfix = map.get("task_start_postfix") != null ? " " + map.get("task_start_postfix") : " ";
                String command = "";
                if (Platform.isWindows()) {
                    command = "cmd.exe /c java -jar" + startParams + " \"" + jarPath + "\"" + startPostfix + " &";
                } else if (Platform.isLinux() || Platform.isAIX()) {
                    command = "nohup java -jar" + startParams + " /" + jarPath + startPostfix + " &";
                }
                log.info("command:" + command);
                Process proc = Runtime.getRuntime().exec(command);
                read(proc.getInputStream(), System.out);
                read(proc.getErrorStream(), System.err);
                procIdMap.put(processId, processUtils.getProcessID());
                liveProcess.put(processId, proc);
                // 记录进程启动时间，后续方便管理进程存活周期
                redisUtil.set(processId + "_start", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            } catch (Exception e) {
                e.printStackTrace();
                log.error("启动子进程【进程ID：" + processId + "，进程名：" + map.get("task_name") + "】失败：" + e);
            }
        } else {
            System.out.println("已启动子进程【进程ID：" + processId + "，进程名：" + map.get("task_name") + "】");
        }
    }

    /**
     * 每5秒检测一次子进程状态
     **/
    public void checkProcessIsLived() {
        //存放子进程状态代码(-1：离线，0：在线)
        Map<String, Object> livedMap = new HashMap<>();
        ScheduledExecutorService liveScheduler = Executors.newSingleThreadScheduledExecutor();
        liveScheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    liveProcess.forEach((processId, processExample) -> {
                        if (processExample.isAlive()) {
                            livedMap.put(processId, "0");
                        } else {
                            livedMap.put(processId, "-1");
                        }
                    });

                    //将子进程的状态更新到redis
                    redisUtil.hmset("PROCESS_STATUS", livedMap);

                    // 主进程查看各子进程状态，是否重启子进程，读取子进程生成的附件
                    processUtils.processOperation(livedMap);
                } catch (Exception e) {
                    log.error("检测子进程状态出错：" + e.getCause().getMessage());
                }
            }
        }, 5, 10, TimeUnit.SECONDS);
    }

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        this.getPath(); //获取jar所在路径
        if (applicationArguments.containsOption("processId")) {
            processId = applicationArguments.getOptionValues("processId").get(0);
        }

        log.info("检测redis配置是否读取完毕...");
        ScheduledExecutorService scheduledThreadPool = Executors.newSingleThreadScheduledExecutor();
        //processId有值，则表示启动子进程
        if (StringUtils.isNotBlank(processId)) {
            scheduledThreadPool.schedule(new Runnable() {
                @Override
                public void run() {
                    if ("OK".equals(redisUtil.get("SYSTEM_CONFIG_" + processId)) && "OK".equals(redisUtil.get("INTERFACE_CONFIG_" + processId))) {
                        if (redisUtil.get("queryDay") != null) {
                            redisUtil.set("queryDay", LocalDate.parse(String.valueOf(redisUtil.get("queryDay"))).plusDays(1).toString());
                        }
                        log.info("开启线程任务调用...");
                        apiTaskUtil.executeExecutor();
                    }
                }
            }, 5, TimeUnit.SECONDS);
        } else {
            //创建启动子进程定时线程
            ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
            // 跑历史数据
            if (applicationArguments.containsOption("history")) {
                String historyId = applicationArguments.getOptionValues("history").get(0);
                if (redisUtil.get("queryDay") == null) {
                    redisUtil.set("queryDay", LocalDate.parse(queryDay).toString()); //历史数据起始日期
                }
                scheduledThreadPool.schedule(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //确保配置参数读取到redis中后，再执行定时任务调用
                            if ("OK".equals(redisUtil.get("PROCESS_CONFIG"))) {
                                List<Map<String, Object>> processList = (List<Map<String, Object>>) redisUtil.hmget("PROCESS_LIST").get("processMap");
                                threadPoolTaskScheduler.initialize(); //初始化定时线程
                                //循环遍历进程
                                processList.forEach(map -> {
                                    if (historyId.equals(map.get("ID"))) {
                                        threadPoolTaskScheduler.schedule(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    log.info("读取redis配置数据，启动子进程任务...");
                                                    startProcess(map);
                                                } catch (Exception e) {
                                                    log.error("定时任务调用错误：" + e);
                                                }
                                            }
                                        }, new CronTrigger(String.valueOf(redisUtil.get("CRON_" + map.get("ID")))));
                                        //启动检测子进程定时器
                                        checkProcessIsLived();
                                    }
                                });
                            }
                        } catch (Exception e) {
                            log.error("定时任务调用错误：" + e);
                        }
                    }
                }, 5, TimeUnit.SECONDS);
            } else {
                scheduledThreadPool.schedule(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //确保配置参数读取到redis中后，再执行定时任务调用
                            if ("OK".equals(redisUtil.get("PROCESS_CONFIG"))) {
                                List<Map<String, Object>> processList = (List<Map<String, Object>>) redisUtil.hmget("PROCESS_LIST").get("processMap");
                                threadPoolTaskScheduler.setPoolSize(processList.size());
                                threadPoolTaskScheduler.initialize(); //初始化定时线程
                                //循环遍历进程
                                processList.forEach(map -> {
                                    threadPoolTaskScheduler.schedule(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                log.info("读取redis配置数据，启动子进程任务...");
                                                startProcess(map);
                                            } catch (Exception e) {
                                                log.error("定时任务调用错误：" + e);
                                            }
                                        }
                                    }, new CronTrigger(String.valueOf(redisUtil.get("CRON_" + map.get("ID")))));
                                });
                                //启动检测子进程定时器
                                checkProcessIsLived();
                            }
                        } catch (Exception e) {
                            log.error("定时任务调用错误：" + e);
                        }
                    }
                }, 5, TimeUnit.SECONDS);
            }
            //关闭子进程线程池
            threadPoolTaskScheduler.shutdown();
        }
        //关闭线程池
        scheduledThreadPool.shutdown();
    }

    /**
     * 获取jar路径
     */
    public void getPath() {
        try {
            String basePatha1 = this.getClass().getProtectionDomain().getCodeSource().getLocation().getFile().substring(1);
            jarPath = URLDecoder.decode(basePatha1, "utf-8");
            log.info("jarPath:" + jarPath);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void destory() {
        log.info("销毁进程...........");
        procIdMap.forEach((procId, pId) -> {
            processUtils.killProcessByPid(pId);
        });
    }

    // 读取输入流
    private static void read(InputStream inputStream, PrintStream out) {
        ArrayList a = new ArrayList();
        a.add(11);
        LinkedList b = new LinkedList();
        b.add(22);
        b.add(1,2);
        HashMap aa = new HashMap();
        aa.put(1,2);

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

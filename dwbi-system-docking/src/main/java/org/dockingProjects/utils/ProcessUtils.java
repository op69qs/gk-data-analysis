package org.dockingProjects.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sun.jna.Platform;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dockingProjects.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author haojiang.
 * @Ddate 2020/9/25 18:17
 * @Description
 */
@Slf4j
@Component
public class ProcessUtils {

    @Value("${liveTime}")
    private String liveTime;

    @Value("${fileSavePath}")
    private String fileSavePath;

    @Autowired
    private FileUtil fileUtil;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ProcessUtils processUtils;

    @Autowired
    private ConfigService configService;

    /**
     * 处理进程
     */
    public void processOperation(Map<String, Object> liveMap) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        liveMap.forEach((processId, status) -> {
            if ("0".equals(status)) { //判断子进程是否在存活周期内,如超出存活周期则需要kill子进程
                try {
                    // 判断子进程是否在存活周期内,如超出存活周期则需要kill子进程
                    Date startTime = format.parse(LocalDateTime.parse(String.valueOf(redisUtil.get(processId + "_start")), formatter).plusMinutes(Integer.valueOf(liveTime)).format(formatter));
                    Date nowTime = format.parse(format.format(new Date()));
                    if (startTime.compareTo(nowTime) > -1) { //起始时间等于或大于现在时间
                        //记录进程调用接口结果状态JSONObject
                        JSONObject processStatus = new JSONObject();
                        processStatus.put("task_id", processId);
                        processStatus.put("task_status", "-1"); //进程任务执行超出执行周期，强制停止
                        //将进程执行的接口状态写入文本
                        fileUtil.write(fileSavePath, processId + ".txt", processStatus.toJSONString());
                        //任务执行异常，强制关闭子进程
                        processUtils.killProcessByPid(String.valueOf(redisUtil.get("PROCESS_ID_" + processId)));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if ("-1".equals(status)) { //子进程未启动：1.读取子进程生成的文件，插入记录表；2.子进程是否需要重启
                fileSavePath = fileSavePath + processId + ".txt";
                // 子进程未启动：1.读取子进程生成的文件，插入记录表；2.子进程是否需要重启
                List<String> readList = fileUtil.read(fileSavePath);
                if (readList.size() > 0) {
                    JSONObject jsonObject = JSON.parseObject(readList.get(0));
                    jsonObject.put("ID", UuidUtil.get32UUID());
                    jsonObject.put("addTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                    configService.insertProcessRecords(jsonObject); //插入进程状态表
                    fileUtil.delete(fileSavePath); //删除进程状态附件
                }
            }
        });
    }

    /**
     * 获取当前进程的PID
     */
    public static final String getProcessID() {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        return String.valueOf(runtimeMXBean.getName().split("@")[0]);
    }

    /**
     * 关闭Windows/Linux进程
     *
     * @param pid 进程的PID
     */
    public static void killProcessByPid(String pid) {
        if (StringUtils.isEmpty(pid) || "-1".equals(pid)) {
            throw new RuntimeException("Pid ==" + pid);
        }
        Process process = null;
        BufferedReader reader = null;
        String command = "";
        boolean result = false;
        if (Platform.isWindows()) {
            command = "cmd.exe /c taskkill /PID " + pid + " /F /T ";
        } else if (Platform.isLinux() || Platform.isAIX()) {
            command = "kill -9 " + pid;
        }
        try {
            //杀掉进程
            log.info("进程PID：" + pid + "关闭...");
            process = Runtime.getRuntime().exec(command);
            reader = new BufferedReader(new InputStreamReader(process.getInputStream(), "utf-8"));
            String line = null;
            while ((line = reader.readLine()) != null) {
                log.info("kill PID return info -----> " + line);
            }
            log.info("进程PID：" + pid + "已关闭...");
        } catch (Exception e) {
            log.info("关闭进程出错：", e);
        } finally {
            if (process != null) {
                process.destroy();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {

                }
            }
        }
    }
}

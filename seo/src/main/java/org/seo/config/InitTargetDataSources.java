package org.seo.config;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.extern.slf4j.Slf4j;
import org.seo.model.DataSourceInfoEntity;
import org.seo.service.DataSourceInfoService;
import org.seo.util.DataSourceConnectionSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@EnableScheduling
public class InitTargetDataSources {
    @Autowired
    private DataSourceInfoService dataSourceService;


    /**
     * 周期性的读取
     */
    @Scheduled(cron = "0 0/2 * * * ?")
    public void readDataSourcePeriodically() {
        setDataSourceInfo();
    }

    private void setDataSourceInfo() {
        log.info("*********定时任务开始使用默认数据库设置从表中读取用户数据源设置信息*********");
        DataSourceContextHolder.setDBType("default");
        List<DataSourceInfoEntity> list = dataSourceService.getList();
        //使用自己设置的sourceName作为数据源的key，使用这个进行切换
        Map<Object, Object> dataSourceMap = list.stream().collect(Collectors.toMap(x -> x.getID(), x -> {
            DruidDataSource druidDataSource = new DruidDataSource();
            druidDataSource.setDriverClassName(x.getDRIVERCLASS_NAME());
            druidDataSource.setUrl(x.getDATASOURCE_URL());
            druidDataSource.setUsername(x.getDATASOURCE_USERNAME());
            druidDataSource.setPassword(x.getDATASOURCE_PASSWORD());
            druidDataSource.setValidationQuery(
                    DataSourceConnectionSupport.validationQuery(x.getDRIVERCLASS_NAME()));
            druidDataSource.setKeepAlive(true);
            return druidDataSource;
        }));
        DynamicDataSource dynamicDataSource = DynamicDataSource.getInstance();
        Map<Object, Object> map = dynamicDataSource.getDataSourceMap();
        map.putAll(dataSourceMap);
        dynamicDataSource.setTargetDataSources(map);
        dynamicDataSource.afterPropertiesSet();
    }

}

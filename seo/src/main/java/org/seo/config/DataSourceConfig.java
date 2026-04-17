package org.seo.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DataSourceConfig {

    @Value("${spring.datasource.default.url}")
    private String defaultDBUrl;
    @Value("${spring.datasource.default.username}")
    private String defaultDBUser;
    @Value("${spring.datasource.default.password}")
    private String defaultDBPassword;
    @Value("${spring.datasource.default.driverClassName}")
    private String defaultDBDriverName;
    @Value("${spring.datasource.default.validationQuery}")
    private String validationQuery;
    @Value("${spring.datasource.default.testOnBorrow}")
    private boolean testOnBorrow;
    @Value("${spring.datasource.default.testOnReturn}")
    private boolean testOnReturn;
    @Value("${spring.datasource.default.testWhileIdle}")
    private boolean testWhileIdle;
    @Value("${spring.datasource.default.filters}")
    private String filters;
    @Value("${spring.datasource.default.maxActive}")
    private int maxActive;

    @Bean
    public DynamicDataSource dynamicDataSource() {
        DynamicDataSource dynamicDataSource = DynamicDataSource.getInstance();
        try {
            DruidDataSource defaultDataSource = new DruidDataSource();
            defaultDataSource.setUrl(defaultDBUrl);
            defaultDataSource.setUsername(defaultDBUser);
            defaultDataSource.setPassword(defaultDBPassword);
            defaultDataSource.setDriverClassName(defaultDBDriverName);
            defaultDataSource.setValidationQuery(validationQuery);
            defaultDataSource.setMaxActive(maxActive);
            defaultDataSource.setTestOnBorrow(testOnBorrow);
            defaultDataSource.setTestOnReturn(testOnReturn);
            defaultDataSource.setTestWhileIdle(testWhileIdle);
            defaultDataSource.setFilters(filters);
            Map<Object, Object> map = new HashMap<>();
            map.put("default", defaultDataSource);
            dynamicDataSource.setTargetDataSources(map);
            dynamicDataSource.setDefaultTargetDataSource(defaultDataSource);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return dynamicDataSource;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(
            @Qualifier("dynamicDataSource") DataSource dynamicDataSource)
            throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dynamicDataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources("classpath*:mybatis/**/*.xml"));
        return bean.getObject();

    }

    @Bean(name = "sqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(
            @Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory)
            throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
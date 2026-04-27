package org.jeecg.config.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.config.shiro.filters.JwtFilter;
import org.jeecg.config.shiro.filters.LegacyPlatformBlockFilter;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;

import javax.servlet.Filter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author: Scott
 * @date: 2018/2/7
 * @description: shiro 配置类
 */

@Slf4j
@Configuration
public class ShiroConfig {

    @Value("${jeecg.shiro.excludeUrls}")
    private String excludeUrls;
    @Autowired
    private Environment env;


    /**
     * Filter Chain定义说明
     *
     * 1、一个URL可以配置多个Filter，使用逗号分隔
     * 2、当设置多个过滤器时，全部验证通过，才视为通过
     * 3、部分过滤器可指定参数，如perms，roles
     */
    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // 拦截器
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        if(oConvertUtils.isNotEmpty(excludeUrls)){
            String[] permissionUrl = excludeUrls.split(",");
            for(String url : permissionUrl){
                filterChainDefinitionMap.put(url,"anon");
            }
        }

        //cas验证登录
        filterChainDefinitionMap.put("/cas/client/validateLogin", "anon");
        // 配置不会被拦截的链接 顺序判断
        filterChainDefinitionMap.put("/sys/randomImage/**", "anon"); //登录验证码接口排除
        filterChainDefinitionMap.put("/sys/checkCaptcha", "anon"); //登录验证码接口排除
        filterChainDefinitionMap.put("/sys/login", "anon");
        filterChainDefinitionMap.put("/sys/mLogin", "anon");
        filterChainDefinitionMap.put("/sys/logout", "anon");
        filterChainDefinitionMap.put("/sys/phoneLogin", "anon");
        filterChainDefinitionMap.put("/thirdLogin/**", "anon");
        filterChainDefinitionMap.put("/sys/user/**", "anon");
        filterChainDefinitionMap.put("/sys/role/**", "anon");
        filterChainDefinitionMap.put("/sys/permission/**", "anon");
        filterChainDefinitionMap.put("/sys/sysUserAgent/**", "anon");
        filterChainDefinitionMap.put("/sys/sysDepartRole/**", "anon");
        filterChainDefinitionMap.put("/sys/sysDepartPermission/**", "anon");
        filterChainDefinitionMap.put("/sys/getEncryptedString", "anon"); //获取加密串
        filterChainDefinitionMap.put("/sys/sms", "anon");//短信验证码
        filterChainDefinitionMap.put("/auth/2step-code", "anon");//登录验证码
        filterChainDefinitionMap.put("/sys/common/static/**", "anon");//图片预览 &下载文件不限制token
        //filterChainDefinitionMap.put("/sys/common/view/**", "anon");//图片预览不限制token
        //filterChainDefinitionMap.put("/sys/common/download/**", "anon");//文件下载不限制token
        filterChainDefinitionMap.put("/sys/common/pdf/**", "anon");//pdf预览
        filterChainDefinitionMap.put("/generic/**", "anon");//pdf预览需要文件
        filterChainDefinitionMap.put("/", "anon");
        filterChainDefinitionMap.put("/doc.html", "anon");
        filterChainDefinitionMap.put("/**/*.js", "anon");
        filterChainDefinitionMap.put("/**/*.css", "anon");
        filterChainDefinitionMap.put("/**/*.html", "anon");
        filterChainDefinitionMap.put("/**/*.svg", "anon");
        filterChainDefinitionMap.put("/**/*.pdf", "anon");
        filterChainDefinitionMap.put("/**/*.jpg", "anon");
        filterChainDefinitionMap.put("/**/*.png", "anon");
        filterChainDefinitionMap.put("/**/*.ico", "anon");

        // update-begin--Author:sunjianlei Date:20190813 for：排除字体格式的后缀
        filterChainDefinitionMap.put("/**/*.ttf", "anon");
        filterChainDefinitionMap.put("/**/*.woff", "anon");
        filterChainDefinitionMap.put("/**/*.woff2", "anon");
        // update-begin--Author:sunjianlei Date:20190813 for：排除字体格式的后缀

        filterChainDefinitionMap.put("/druid/**", "anon");
        filterChainDefinitionMap.put("/swagger-ui.html", "anon");
        filterChainDefinitionMap.put("/swagger**/**", "anon");
        filterChainDefinitionMap.put("/webjars/**", "anon");
        filterChainDefinitionMap.put("/v2/**", "anon");

        // update-begin--Author:sunjianlei Date:20190813 for：表单设计器不要一次性全排除
        filterChainDefinitionMap.put("/desform/index/**", "anon");
        filterChainDefinitionMap.put("/desform/ext/**", "anon");
        filterChainDefinitionMap.put("/desform/add/**", "anon");
        filterChainDefinitionMap.put("/desform/view/**", "anon");
        filterChainDefinitionMap.put("/desform/edit/**", "anon");
        filterChainDefinitionMap.put("/desform/detail/**", "anon");
        // update-end--Author:sunjianlei Date:20190813 for：表单设计器不要一次性全排除

        //报表设计器排除
        filterChainDefinitionMap.put("/design/report/**", "anon");
        filterChainDefinitionMap.put("/**/*.js.map", "anon");
        filterChainDefinitionMap.put("/**/*.css.map", "anon");

        //在线文档管理
        filterChainDefinitionMap.put("/filemanage/**", "anon");
        //在线聊天排除
        filterChainDefinitionMap.put("/oa/im/**", "anon");
        filterChainDefinitionMap.put("/im/dist/**", "anon");

        //大屏设计器排除
        filterChainDefinitionMap.put("/jmreport/bigscreen/**", "anon");
        filterChainDefinitionMap.put("/test/bigScreen/**", "anon");
        filterChainDefinitionMap.put("/bigscreen/**", "anon");

        //测试示例
        //filterChainDefinitionMap.put("/test/jeecgDemo/html", "anon"); //模板页面
        //filterChainDefinitionMap.put("/test/jeecgDemo/redis/**", "anon"); //redis测试

        //流程模块组件请求
//		filterChainDefinitionMap.put("/act/process/**", "anon");
//		filterChainDefinitionMap.put("/act/task/**", "anon");
//		filterChainDefinitionMap.put("/act/model/**", "anon");
        // 流程图请求地址
        filterChainDefinitionMap.put("/act/task/traceImage", "anon");
        filterChainDefinitionMap.put("/act/process/processPic", "anon");
        filterChainDefinitionMap.put("/act/process/downProcessXml", "anon");
        filterChainDefinitionMap.put("/act/process/resource", "anon");
        filterChainDefinitionMap.put("/service/editor/**", "anon");
        filterChainDefinitionMap.put("/service/model/**", "anon");
        filterChainDefinitionMap.put("/service/model/**/save", "anon");
        filterChainDefinitionMap.put("/editor-app/**", "anon");
        filterChainDefinitionMap.put("/diagram-viewer/**", "anon");
        filterChainDefinitionMap.put("/modeler.html", "anon");
        filterChainDefinitionMap.put("/designer", "anon");
        filterChainDefinitionMap.put("/designer/**", "anon");
        filterChainDefinitionMap.put("/plug-in/**", "anon");

        //排除Online请求
        filterChainDefinitionMap.put("/auto/cgform/**", "anon");
        //FineReport报表
        filterChainDefinitionMap.put("/ReportServer**", "anon");

        //websocket排除
        filterChainDefinitionMap.put("/websocket/**", "anon");
        filterChainDefinitionMap.put("/newsWebsocket/**", "anon");

        //我的聊天排除
        filterChainDefinitionMap.put("/oa/im/**","anon");
        filterChainDefinitionMap.put("/im/dist/**","anon");
        filterChainDefinitionMap.put("/eoaSocket/**","anon");

        // VXE Table WebSocket 排除（前端vxe组件无痕刷新功能）
        filterChainDefinitionMap.put("/vxeSocket/**", "anon");

        //wps
        filterChainDefinitionMap.put("/v1/**","anon");
        //公文路径部分拦截
        filterChainDefinitionMap.put("/officialdoc/oaOfficialdocTemp/editByFiledId","anon");
        filterChainDefinitionMap.put("/officialdoc/oaOfficialdocOrgancode/getTemp","anon");
        filterChainDefinitionMap.put("/officialdoc/oaOfficialdocTemp/getTempByFileId","anon");
        filterChainDefinitionMap.put("/officialdoc/oaOfficialdocTemp/updateNameByFileId","anon");
        //ureport报表拦截排除
        filterChainDefinitionMap.put("/ureport/**","anon");
        //插件商城排除
        filterChainDefinitionMap.put("/pluginMall/**","anon");
        // 添加自己的过滤器并且取名为jwt
        Map<String, Filter> filterMap = new HashMap<>(1);
        //如果cloudServer为空 则说明是单体 需要加载跨域配置
        Object cloudServer = env.getProperty(CommonConstant.CLOUD_SERVER_KEY);
        filterMap.put("legacyBlock", new LegacyPlatformBlockFilter());
        filterMap.put("jwt", new JwtFilter(cloudServer==null));
        shiroFilterFactoryBean.setFilters(filterMap);
        // <!-- 过滤链定义，从上向下顺序执行，一般将/**放在最为下边
        filterChainDefinitionMap.put("/**", "jwt");

        // 未授权界面返回JSON
        shiroFilterFactoryBean.setUnauthorizedUrl("/sys/common/403");
        shiroFilterFactoryBean.setLoginUrl("/sys/common/403");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    @Bean
    public FilterRegistrationBean<LegacyPlatformBlockFilter> legacyPlatformBlockFilterRegistration() {
        FilterRegistrationBean<LegacyPlatformBlockFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new LegacyPlatformBlockFilter());
        registration.setName("legacyPlatformBlockServletFilter");
        registration.setUrlPatterns(Arrays.asList(
            "/sys/login",
            "/sys/logout",
            "/sys/mLogin",
            "/sys/phoneLogin",
            "/thirdLogin/*",
            "/sys/user/*",
            "/sys/role/*",
            "/sys/permission/*",
            "/sys/sysUserAgent/*",
            "/sys/sysDepartRole/*",
            "/sys/sysDepartPermission/*"
        ));
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registration;
    }

    @Bean("securityManager")
    public DefaultWebSecurityManager securityManager(ShiroRealm myRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(myRealm);

        /*
         * 关闭shiro自带的session，详情见文档
         * http://shiro.apache.org/session-management.html#SessionManagement-
         * StatelessApplications%28Sessionless%29
         */
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        securityManager.setSubjectDAO(subjectDAO);
        return securityManager;
    }

    /**
     * 下面的代码是添加注解支持
     * @return
     */
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        /**
         * 解决重复代理问题 github#994
         * 添加前缀判断 不匹配 任何Advisor
         */
        defaultAdvisorAutoProxyCreator.setUsePrefix(true);
        defaultAdvisorAutoProxyCreator.setAdvisorBeanNamePrefix("_no_advisor");
        return defaultAdvisorAutoProxyCreator;
    }

    @Bean
    public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

}

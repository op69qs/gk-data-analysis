package org.seo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.security.CodeSource;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
@Component
public class RequestMappingLogRunner implements ApplicationRunner {

    private final RequestMappingHandlerMapping requestMappingHandlerMapping;
    private final ApplicationContext applicationContext;

    public RequestMappingLogRunner(RequestMappingHandlerMapping requestMappingHandlerMapping,
                                   ApplicationContext applicationContext) {
        this.requestMappingHandlerMapping = requestMappingHandlerMapping;
        this.applicationContext = applicationContext;
    }

    @Override
    public void run(ApplicationArguments args) {
        logClassOrigin("org.seo.controller.DataSourceController");
        logClassOrigin("org.seo.controller.DataTableController");
        log.info("SEO bean dataSourceController present={}, dataTableController present={}",
                applicationContext.containsBean("dataSourceController"),
                applicationContext.containsBean("dataTableController"));

        Map<String, String> targetMappings = new TreeMap<>();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry
                : requestMappingHandlerMapping.getHandlerMethods().entrySet()) {
            HandlerMethod handlerMethod = entry.getValue();
            String beanType = handlerMethod.getBeanType().getName();
            if (!beanType.startsWith("org.seo.controller.")) {
                continue;
            }
            String simpleName = handlerMethod.getBeanType().getSimpleName();
            if (!simpleName.contains("DataSourceController")
                    && !simpleName.contains("DataTableController")
                    && !simpleName.contains("DataAuxiliaryController")) {
                continue;
            }
            targetMappings.put(
                    simpleName + "#" + handlerMethod.getMethod().getName(),
                    entry.getKey().toString()
            );
        }

        log.info("SEO request mappings diagnose begin, matchedCount={}", targetMappings.size());
        for (Map.Entry<String, String> entry : targetMappings.entrySet()) {
            log.info("SEO request mapping {} -> {}", entry.getKey(), entry.getValue());
        }
        log.info("SEO request mappings diagnose end");
    }

    private void logClassOrigin(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            CodeSource codeSource = clazz.getProtectionDomain().getCodeSource();
            log.info("SEO class {} loadedFrom={}", className,
                    codeSource == null ? "unknown" : codeSource.getLocation());
        } catch (Throwable t) {
            log.error("SEO class {} NOT loadable: {}: {}", className,
                    t.getClass().getName(), t.getMessage());
        }
    }
}

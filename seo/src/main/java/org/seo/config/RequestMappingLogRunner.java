package org.seo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Map;
import java.util.TreeMap;

@Slf4j
@Component
public class RequestMappingLogRunner implements ApplicationRunner {

    private final RequestMappingHandlerMapping requestMappingHandlerMapping;

    public RequestMappingLogRunner(RequestMappingHandlerMapping requestMappingHandlerMapping) {
        this.requestMappingHandlerMapping = requestMappingHandlerMapping;
    }

    @Override
    public void run(ApplicationArguments args) {
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
}

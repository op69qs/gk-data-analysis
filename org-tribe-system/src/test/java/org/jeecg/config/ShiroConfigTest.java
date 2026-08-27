package org.jeecg.config;

import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.junit.Test;
import org.springframework.util.AntPathMatcher;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ShiroConfigTest {

    @Test
    public void reportingApisUseJwtInsteadOfSpaAnonymousWildcard() {
        ShiroFilterFactoryBean factory = new ShiroConfig()
                .shiroFilter(new DefaultSecurityManager());

        assertEquals("jwt", matchingChain(
                factory.getFilterChainDefinitionMap(), "/reporting/batches"));
        assertEquals("jwt", matchingChain(
                factory.getFilterChainDefinitionMap(), "/reporting/files/file-1/download"));
    }

    private String matchingChain(Map<String, String> chains, String path) {
        AntPathMatcher pathMatcher = new AntPathMatcher();
        for (Map.Entry<String, String> chain : chains.entrySet()) {
            if (pathMatcher.match(chain.getKey(), path)) {
                return chain.getValue();
            }
        }
        return null;
    }
}

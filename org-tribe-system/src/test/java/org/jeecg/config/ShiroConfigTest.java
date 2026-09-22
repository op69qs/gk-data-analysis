package org.jeecg.config;

import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.junit.Test;
import org.springframework.util.AntPathMatcher;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ShiroConfigTest {

    @Test
    public void retiredSelfServiceApisAreNotAnonymous() {
        Map<String, String> chains = new ShiroConfig()
                .shiroFilter(new DefaultSecurityManager()).getFilterChainDefinitionMap();
        for (String endpoint : new String[]{"register", "checkOnlyUser", "querySysUser",
                "phoneVerification", "passwordChange"}) {
            assertEquals(endpoint, "jwt", matchingChain(chains, "/sys/user/" + endpoint));
        }
        assertEquals("anon", matchingChain(chains, "/sys/login"));
        assertEquals("anon", matchingChain(chains, "/sys/oauth/callback"));
        assertEquals("jwt", matchingChain(chains, "/sys/user/updatePassword"));
    }

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

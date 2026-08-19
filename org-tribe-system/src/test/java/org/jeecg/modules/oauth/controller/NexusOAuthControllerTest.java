package org.jeecg.modules.oauth.controller;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.modules.oauth.NexusOAuthRedirectUriResolver;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

public class NexusOAuthControllerTest {

    @Test
    @SuppressWarnings("unchecked")
    public void sendsTheRequestDerivedCallbackUriDuringTokenExchange() {
        NexusOAuthController controller = new NexusOAuthController();
        ReflectionTestUtils.setField(controller, "clientId", "GK_DATA_ANALYSIS");
        ReflectionTestUtils.setField(controller, "clientSecret", "test-secret");
        ReflectionTestUtils.setField(controller, "tokenUrl", "https://nexus.example.test/auth/oauth/token");
        ReflectionTestUtils.setField(controller, "restTemplate", new RestTemplate());

        RestTemplate restTemplate = (RestTemplate) ReflectionTestUtils.getField(controller, "restTemplate");
        MockRestServiceServer server = MockRestServiceServer.bindTo(restTemplate).build();
        server.expect(requestTo("https://nexus.example.test/auth/oauth/token"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().string(containsString(
                        "redirect_uri=https%3A%2F%2F10.20.8.20%3A9090%2Foauth%2Fcallback")))
                .andRespond(withSuccess("{\"access_token\":\"token\"}", MediaType.APPLICATION_JSON));

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setScheme("https");
        request.setServerName("10.20.8.20");
        request.setServerPort(9090);
        request.setRequestURI("/sys/oauth/callback");
        request.setRemoteAddr("192.168.1.20");

        ReflectionTestUtils.setField(controller, "redirectUriResolver", new NexusOAuthRedirectUriResolver());
        String callbackRedirectUri = ReflectionTestUtils.invokeMethod(
                controller,
                "resolveCallbackRedirectUri",
                request);
        String response = ReflectionTestUtils.invokeMethod(
                controller,
                "exchangeCodeForToken",
                "authorization-code",
                callbackRedirectUri);

        assertEquals("token", response);
        server.verify();
    }
}

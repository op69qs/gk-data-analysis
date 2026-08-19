package org.jeecg.modules.oauth;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.Assert.assertEquals;

public class NexusOAuthRedirectUriResolverTest {

    private final NexusOAuthRedirectUriResolver resolver = new NexusOAuthRedirectUriResolver();

    @Test
    public void derivesTheFrontendCallbackFromTheDirectRequestAddress() {
        MockHttpServletRequest request = request("https", "10.20.8.20", 9090);
        request.setRequestURI("/sys/oauth/callback");

        assertEquals(
                "https://10.20.8.20:9090/oauth/callback",
                resolver.resolve(request, "127.0.0.1/32,::1/128"));
    }

    @Test
    public void preservesTheApplicationContextPathForTheFrontendCallback() {
        MockHttpServletRequest request = request("http", "10.20.8.20", 8080);
        request.setContextPath("/jeecg-boot");
        request.setRequestURI("/jeecg-boot/sys/oauth/callback");

        assertEquals(
                "http://10.20.8.20:8080/jeecg-boot/oauth/callback",
                resolver.resolve(request, "127.0.0.1/32,::1/128"));
    }

    @Test
    public void derivesTheFrontendCallbackFromTrustedForwardedAddress() {
        MockHttpServletRequest request = request("http", "127.0.0.1", 8080);
        request.setRemoteAddr("10.0.0.5");
        request.addHeader("X-Forwarded-Proto", "https");
        request.addHeader("X-Forwarded-Host", "10.20.8.20:9443");

        assertEquals(
                "https://10.20.8.20:9443/oauth/callback",
                resolver.resolve(request, "10.0.0.0/8"));
    }

    @Test
    public void ignoresForwardedAddressFromAnUntrustedClient() {
        MockHttpServletRequest request = request("http", "127.0.0.1", 8080);
        request.setRemoteAddr("192.168.1.20");
        request.addHeader("X-Forwarded-Proto", "https");
        request.addHeader("X-Forwarded-Host", "attacker.example.test:443");

        assertEquals(
                "http://127.0.0.1:8080/oauth/callback",
                resolver.resolve(request, "10.0.0.0/8"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsAnInvalidDirectRequestAddress() {
        MockHttpServletRequest request = request("https", "bad host", 9090);

        resolver.resolve(request, "127.0.0.1/32,::1/128");
    }

    private MockHttpServletRequest request(String scheme, String host, int port) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setScheme(scheme);
        request.setServerName(host);
        request.setServerPort(port);
        request.setRequestURI("/sys/oauth/callback");
        request.setRemoteAddr("192.168.1.10");
        return request;
    }
}

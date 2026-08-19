package org.jeecg.modules.oauth;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Builds the public OAuth callback URI from the request that reached the
 * downstream system. The OAuth client registration in GK-Nexus remains the
 * authoritative allowlist for the resulting URI.
 */
@Component
public class NexusOAuthRedirectUriResolver {

    private static final String CALLBACK_PATH = "/oauth/callback";

    public String resolve(HttpServletRequest request, String trustedProxyCidrs) {
        return requireSafeHttpUri(buildRequestUri(request, trustedProxyCidrs));
    }

    private String buildRequestUri(HttpServletRequest request, String trustedProxyCidrs) {
        if (request == null) {
            throw new IllegalArgumentException("Nexus OAuth callback request is unavailable");
        }

        String scheme = firstHeader(request.getHeader("X-Forwarded-Proto"));
        String authority = firstHeader(request.getHeader("X-Forwarded-Host"));
        boolean trustedProxy = isTrustedProxy(request.getRemoteAddr(), trustedProxyCidrs);

        if (!trustedProxy || !isHttpScheme(scheme) || !isSafeAuthority(authority)) {
            scheme = request.getScheme();
            authority = authority(request.getServerName(), request.getServerPort(), scheme);
        } else if (!hasExplicitPort(authority)) {
            String forwardedPort = firstHeader(request.getHeader("X-Forwarded-Port"));
            if (isPort(forwardedPort)) {
                authority = authority + ":" + forwardedPort;
            }
        }

        if (!isHttpScheme(scheme) || !isSafeAuthority(authority)) {
            throw new IllegalArgumentException("Nexus OAuth callback request address is invalid");
        }

        try {
            return new URI(scheme.toLowerCase(), authority, callbackPath(request), null, null).toString();
        } catch (URISyntaxException exception) {
            throw new IllegalArgumentException("Nexus OAuth callback request address is invalid", exception);
        }
    }

    private String callbackPath(HttpServletRequest request) {
        String contextPath = request.getContextPath();
        if (!StringUtils.hasText(contextPath) || "/".equals(contextPath)) {
            return CALLBACK_PATH;
        }
        if (!contextPath.startsWith("/") || contextPath.endsWith("/")
                || contextPath.indexOf('?') >= 0 || contextPath.indexOf('#') >= 0) {
            throw new IllegalArgumentException("Nexus OAuth callback context path is invalid");
        }
        return contextPath + CALLBACK_PATH;
    }

    private String authority(String host, int port, String scheme) {
        if (!StringUtils.hasText(host)) {
            return null;
        }
        String normalizedHost = host.trim();
        if (normalizedHost.indexOf(':') >= 0 && !normalizedHost.startsWith("[")) {
            normalizedHost = "[" + normalizedHost + "]";
        }
        if (port > 0 && !isDefaultPort(port, scheme)) {
            return normalizedHost + ":" + port;
        }
        return normalizedHost;
    }

    private String requireSafeHttpUri(String value) {
        try {
            URI uri = new URI(value);
            if (!isSafeHttpUri(uri)) {
                throw new IllegalArgumentException("Nexus OAuth redirect URI is invalid");
            }
            if (uri.getRawQuery() != null) {
                throw new IllegalArgumentException(
                        "Nexus OAuth redirect URI must not contain query parameters");
            }
            return value;
        } catch (URISyntaxException exception) {
            throw new IllegalArgumentException("Nexus OAuth redirect URI is invalid", exception);
        }
    }

    private boolean isSafeHttpUri(URI uri) {
        return uri != null
                && isHttpScheme(uri.getScheme())
                && uri.getHost() != null
                && uri.getUserInfo() == null
                && uri.getFragment() == null;
    }

    private boolean isTrustedProxy(String remoteAddress, String cidrs) {
        if (!StringUtils.hasText(remoteAddress) || !StringUtils.hasText(cidrs)) {
            return false;
        }
        byte[] remote = addressBytes(remoteAddress);
        if (remote == null) {
            return false;
        }
        for (String cidr : cidrs.split(",")) {
            String value = cidr.trim();
            int separator = value.lastIndexOf('/');
            if (separator <= 0) {
                continue;
            }
            byte[] network = addressBytes(value.substring(0, separator));
            int prefix;
            try {
                prefix = Integer.parseInt(value.substring(separator + 1));
            } catch (NumberFormatException exception) {
                continue;
            }
            if (network != null && network.length == remote.length
                    && prefix >= 0 && prefix <= remote.length * 8
                    && matchesPrefix(remote, network, prefix)) {
                return true;
            }
        }
        return false;
    }

    private byte[] addressBytes(String value) {
        try {
            return InetAddress.getByName(value.trim()).getAddress();
        } catch (Exception exception) {
            return null;
        }
    }

    private boolean matchesPrefix(byte[] address, byte[] network, int prefix) {
        int fullBytes = prefix / 8;
        int remainingBits = prefix % 8;
        for (int index = 0; index < fullBytes; index++) {
            if (address[index] != network[index]) {
                return false;
            }
        }
        if (remainingBits == 0) {
            return true;
        }
        int mask = 0xFF << (8 - remainingBits);
        return (address[fullBytes] & mask) == (network[fullBytes] & mask);
    }

    private String firstHeader(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        return value.split(",", 2)[0].trim();
    }

    private boolean isSafeAuthority(String value) {
        if (!StringUtils.hasText(value)) {
            return false;
        }
        try {
            URI uri = new URI("http://" + value);
            return uri.getHost() != null
                    && uri.getUserInfo() == null
                    && uri.getRawPath().isEmpty()
                    && uri.getRawQuery() == null
                    && uri.getFragment() == null
                    && (uri.getPort() == -1 || isValidPort(uri.getPort()));
        } catch (URISyntaxException exception) {
            return false;
        }
    }

    private boolean hasExplicitPort(String authority) {
        if (authority.startsWith("[")) {
            int bracket = authority.indexOf(']');
            return bracket >= 0 && authority.length() > bracket + 1 && authority.charAt(bracket + 1) == ':';
        }
        return authority.indexOf(':') >= 0;
    }

    private boolean isHttpScheme(String value) {
        return "http".equalsIgnoreCase(value) || "https".equalsIgnoreCase(value);
    }

    private boolean isPort(String value) {
        if (!StringUtils.hasText(value)) {
            return false;
        }
        try {
            return isValidPort(Integer.parseInt(value));
        } catch (NumberFormatException exception) {
            return false;
        }
    }

    private boolean isValidPort(int port) {
        return port >= 1 && port <= 65535;
    }

    private boolean isDefaultPort(int port, String scheme) {
        return ("http".equalsIgnoreCase(scheme) && port == 80)
                || ("https".equalsIgnoreCase(scheme) && port == 443);
    }
}

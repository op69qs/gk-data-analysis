package org.jeecg.modules.oauth.controller;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysDepartService;
import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * GK-Nexus OAuth2.0 authorization code callback handler.
 * Mirrors the pattern of CasClientController to keep auth flows consistent.
 */
@Slf4j
@RestController
@RequestMapping("/sys/oauth")
public class NexusOAuthController {

    private static final String NEXUS_PORTAL_USER_ID_PREFIX = "PREFIX_NEXUS_PORTAL_USER_ID_";
    private static final String NEXUS_PORTAL_ACCESS_TOKEN_PREFIX = "PREFIX_NEXUS_PORTAL_ACCESS_TOKEN_";

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private ISysDepartService sysDepartService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${gk-nexus.oauth.token-url}")
    private String tokenUrl;

    @Value("${gk-nexus.oauth.client-id}")
    private String clientId;

    @Value("${gk-nexus.oauth.client-secret}")
    private String clientSecret;

    @Value("${gk-nexus.oauth.redirect-uri}")
    private String redirectUri;

    /**
     * OAuth2.0 authorization code callback.
     * Called by the frontend after GK-Nexus redirects back with ?code=xxx&state=xxx.
     * State validation is handled by the frontend (CSRF guard); backend only exchanges code for token.
     */
    @GetMapping("/callback")
    public Object callback(
            @RequestParam(name = "code") String code,
            @RequestParam(name = "state", required = false) String state) {
        Result<JSONObject> result = new Result<>();
        log.info("Nexus OAuth callback, code={}", code);
        try {
            // 1. Exchange authorization code for access_token
            String accessToken = exchangeCodeForToken(code);

            // 2. Decode JWT to extract username claim (no sig verification needed:
            //    token comes directly from a trusted server-to-server call over HTTPS)
            DecodedJWT decoded = JWT.decode(accessToken);
            String username = decoded.getClaim("username").asString();
            if (StringUtils.isEmpty(username)) {
                // fallback: sub claim
                username = decoded.getSubject();
            }
            if (StringUtils.isEmpty(username)) {
                throw new Exception("Cannot extract username from GK-Nexus access_token");
            }
            log.info("Nexus OAuth login, username={}", username);

            // 3. Validate user exists and is active in local DB
            SysUser sysUser = sysUserService.getUserByName(username);
            result = sysUserService.checkUserIsEffective(sysUser);
            if (!result.isSuccess()) {
                return result;
            }

            // 4. Generate JEECG JWT token and cache in Redis (same as CasClientController)
            String token = JwtUtil.sign(sysUser.getUsername(), sysUser.getPassword());
            redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, token);
            redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, JwtUtil.EXPIRE_TIME / 1000);

            // Cache portal identity context for permission sync in /sys/permission/getUserPermissionByToken
            String portalUserId = decoded.getSubject();
            if (StringUtils.isNotEmpty(portalUserId)) {
                redisUtil.set(NEXUS_PORTAL_USER_ID_PREFIX + token, portalUserId);
                redisUtil.expire(NEXUS_PORTAL_USER_ID_PREFIX + token, JwtUtil.EXPIRE_TIME / 1000);
            }
            redisUtil.set(NEXUS_PORTAL_ACCESS_TOKEN_PREFIX + token, accessToken);
            redisUtil.expire(NEXUS_PORTAL_ACCESS_TOKEN_PREFIX + token, JwtUtil.EXPIRE_TIME / 1000);

            // 5. Build response identical to CasClientController
            JSONObject obj = new JSONObject();
            List<SysDepart> departs = sysDepartService.queryUserDeparts(sysUser.getId());
            obj.put("departs", departs);
            if (departs == null || departs.size() == 0) {
                obj.put("multi_depart", 0);
            } else if (departs.size() == 1) {
                sysUserService.updateUserDepart(username, departs.get(0).getOrgCode());
                obj.put("multi_depart", 1);
            } else {
                obj.put("multi_depart", 2);
            }
            obj.put("token", token);
            obj.put("userInfo", sysUser);
            result.setResult(obj);
            result.success("登录成功");

        } catch (Exception e) {
            log.error("Nexus OAuth callback error: {}", e.getMessage(), e);
            result.error500("OAuth登录失败: " + e.getMessage());
        }
        return new HttpEntity<>(result);
    }

    /**
     * Exchanges an OAuth2.0 authorization code for an access_token from GK-Nexus.
     */
    private String exchangeCodeForToken(String code) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("code", code);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<JSONObject> response;
        try {
            response = restTemplate.postForEntity(tokenUrl, request, JSONObject.class);
        } catch (HttpClientErrorException e) {
            String responseBody = e.getResponseBodyAsString();
            if (responseBody != null && responseBody.length() > 500) {
                responseBody = responseBody.substring(0, 500);
            }
            log.error("Nexus token exchange failed, status={}, responseBody={}", e.getStatusCode(), responseBody);
            throw e;
        }

        JSONObject body = response.getBody();
        if (body == null || !body.containsKey("access_token")) {
            throw new Exception("No access_token in GK-Nexus response");
        }
        return body.getString("access_token");
    }
}

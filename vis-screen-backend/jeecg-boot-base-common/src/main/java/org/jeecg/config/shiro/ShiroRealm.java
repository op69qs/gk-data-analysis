package org.jeecg.config.shiro;

import cn.hutool.crypto.SecureUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.jeecg.common.api.CommonAPI;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.common.util.oConvertUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @Description: 用户登录鉴权和获取用户授权
 * @Author: Scott
 * @Date: 2019-4-23 8:13
 * @Version: 1.1
 */
@Component
@Slf4j
public class ShiroRealm extends AuthorizingRealm {
	@Lazy
    @Resource
    private CommonAPI commonAPI;

    @Lazy
    @Resource
    private RedisUtil redisUtil;

    /**
     * 必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 权限信息认证(包括角色以及权限)是用户访问controller的时候才进行验证(redis存储的此处权限信息)
     * 触发检测用户权限时才会调用此方法，例如checkRole,checkPermission
     *
     * @param principals 身份信息
     * @return AuthorizationInfo 权限信息
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        log.info("===============Shiro权限认证开始============ [ roles、permissions]==========");
        String username = null;
        if (principals != null) {
            LoginUser sysUser = (LoginUser) principals.getPrimaryPrincipal();
            username = sysUser.getUsername();
        }
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        String token = getRequestToken();
        if (oConvertUtils.isNotEmpty(token) && applyAuthorizationFromToken(info, token)) {
            log.info("===============Shiro权限认证成功==============(token-claims)");
            return info;
        }

        if (oConvertUtils.isEmpty(username)) {
            log.warn("Shiro权限认证回退本地用户失败：username为空");
            return info;
        }

        // 设置用户拥有的角色集合，比如“admin,test”
        Set<String> roleSet = commonAPI.queryUserRoles(username);
        if (roleSet != null) {
            info.setRoles(roleSet);
        }

        // 设置用户拥有的权限集合，比如“sys:role:add,sys:user:add”
        Set<String> permissionSet = commonAPI.queryUserAuths(username);
        if (permissionSet != null) {
            info.addStringPermissions(permissionSet);
        }
        log.info("===============Shiro权限认证成功==============");
        return info;
    }

    /**
     * 用户信息认证是在用户进行登录的时候进行验证(不存redis)
     * 也就是说验证用户输入的账号和密码是否正确，错误抛出异常
     *
     * @param auth 用户登录的账号密码信息
     * @return 返回封装了用户信息的 AuthenticationInfo 实例
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
        log.debug("===============Shiro身份认证开始============doGetAuthenticationInfo==========");
        String token = (String) auth.getCredentials();
        if (token == null) {
            log.info("————————身份认证失败——————————IP地址:  "+ oConvertUtils.getIpAddrByRequest(SpringContextUtils.getHttpServletRequest()));
            throw new AuthenticationException("token为空!");
        }
        // 校验token有效性
        LoginUser loginUser = this.checkUserTokenIsEffect(token);
        return new SimpleAuthenticationInfo(loginUser, token, getName());
    }

    /**
     * 校验token的有效性
     *
     * @param token
     */
    public LoginUser checkUserTokenIsEffect(String token) throws AuthenticationException {
        // 解密获得username，用于和数据库进行对比
        String username = JwtUtil.getUsername(token);
        if (username == null) {
            throw new AuthenticationException("token非法无效!");
        }

        if (!jwtTokenRefresh(token, username, null)) {
            throw new AuthenticationException("Token失效，请重新登录!");
        }

        LoginUser loginUser = buildLoginUserFromToken(token, username);
        if (loginUser == null) {
            throw new AuthenticationException("Token失效，请重新登录!");
        }

        // 查询用户信息
        log.debug("———校验token是否有效————checkUserTokenIsEffect——————— "+ token);
        LoginUser localUser = commonAPI.getUserByName(username);
        if (localUser != null) {
            if (localUser.getStatus() != 1) {
                throw new AuthenticationException("账号已被锁定,请联系管理员!");
            }
            if (oConvertUtils.isNotEmpty(localUser.getId())) {
                loginUser.setId(localUser.getId());
            }
            if (oConvertUtils.isNotEmpty(localUser.getRealname())) {
                loginUser.setRealname(localUser.getRealname());
            }
            loginUser.setStatus(localUser.getStatus());
        } else {
            log.info("本地用户不存在，使用OAuth token声明完成鉴权, username={}", username);
        }

        return loginUser;
    }

    /**
     * JWTToken刷新生命周期 （实现： 用户在线操作不掉线功能）
     * 1、登录成功后将用户的JWT生成的Token作为k、v存储到cache缓存里面(这时候k、v值一样)，缓存有效期设置为Jwt有效时间的2倍
     * 2、当该用户再次请求时，通过JWTFilter层层校验之后会进入到doGetAuthenticationInfo进行身份验证
     * 3、当该用户这次请求jwt生成的token值已经超时，但该token对应cache中的k还是存在，则表示该用户一直在操作只是JWT的token失效了，程序会给token对应的k映射的v值重新生成JWTToken并覆盖v值，该缓存生命周期重新计算
     * 4、当该用户这次请求jwt在生成的token值已经超时，并在cache中不存在对应的k，则表示该用户账户空闲超时，返回用户信息已失效，请重新登录。
     * 注意： 前端请求Header中设置Authorization保持不变，校验有效性以缓存中的token为准。
     *       用户过期时间 = Jwt有效时间 * 2。
     *
     * @param userName
     * @param passWord
     * @return
     */
    public boolean jwtTokenRefresh(String token, String userName, String passWord) {
        if (!isTokenNotExpired(token)) {
            return false;
        }
        try {
            Object cacheToken = redisUtil.get(CommonConstant.PREFIX_USER_TOKEN + token);
            return cacheToken != null;
        } catch (Exception e) {
            // Keep service available even when Redis is transiently unavailable.
            log.warn("Redis token校验失败，回退为JWT过期时间校验: {}", e.getMessage());
            return true;
        }
    }

    private String getRequestToken() {
        try {
            HttpServletRequest request = SpringContextUtils.getHttpServletRequest();
            if (request == null) {
                return null;
            }
            return request.getHeader(CommonConstant.X_ACCESS_TOKEN);
        } catch (Exception e) {
            return null;
        }
    }

    private boolean applyAuthorizationFromToken(SimpleAuthorizationInfo info, String token) {
        try {
            DecodedJWT decodedJWT = JWT.decode(token);
            Set<String> roles = new LinkedHashSet<>();
            roles.addAll(readClaimAsSet(decodedJWT, "roles"));
            roles.addAll(readClaimAsSet(decodedJWT, "role"));

            Set<String> permissions = new LinkedHashSet<>();
            permissions.addAll(readClaimAsSet(decodedJWT, "permissions"));
            permissions.addAll(readClaimAsSet(decodedJWT, "perms"));
            permissions.addAll(readClaimAsSet(decodedJWT, "authorities"));
            permissions.addAll(readClaimAsSet(decodedJWT, "scope"));

            if (!roles.isEmpty()) {
                info.setRoles(roles);
            }
            if (!permissions.isEmpty()) {
                info.addStringPermissions(permissions);
            }
            return !roles.isEmpty() || !permissions.isEmpty();
        } catch (Exception e) {
            log.warn("从OAuth token解析权限信息失败: {}", e.getMessage());
            return false;
        }
    }

    private Set<String> readClaimAsSet(DecodedJWT decodedJWT, String claimName) {
        Claim claim = decodedJWT.getClaim(claimName);
        if (claim == null || claim.isNull()) {
            return Collections.emptySet();
        }

        LinkedHashSet<String> values = new LinkedHashSet<>();
        List<String> listValues = claim.asList(String.class);
        if (listValues != null) {
            for (String value : listValues) {
                if (oConvertUtils.isNotEmpty(value)) {
                    values.add(value.trim());
                }
            }
        }

        String textValue = claim.asString();
        if (oConvertUtils.isNotEmpty(textValue)) {
            values.addAll(splitClaimValues(textValue));
        }
        return values;
    }

    private Set<String> splitClaimValues(String value) {
        LinkedHashSet<String> values = new LinkedHashSet<>();
        for (String item : Arrays.asList(value.split("[,\\s]+"))) {
            if (oConvertUtils.isNotEmpty(item)) {
                values.add(item.trim());
            }
        }
        return values;
    }

    private LoginUser buildLoginUserFromToken(String token, String username) {
        try {
            if (!isTokenNotExpired(token)) {
                return null;
            }
            DecodedJWT decodedJWT = JWT.decode(token);
            LoginUser loginUser = new LoginUser();
            loginUser.setUsername(username);
            loginUser.setStatus(1);
            loginUser.setId(decodedJWT.getSubject());
            String realname = decodedJWT.getClaim("name").asString();
            if (oConvertUtils.isEmpty(realname)) {
                realname = username;
            }
            loginUser.setRealname(realname);
            return loginUser;
        } catch (Exception e) {
            log.warn("基于token声明构造登录态失败: {}", e.getMessage());
            return null;
        }
    }

    private boolean isTokenNotExpired(String token) {
        try {
            DecodedJWT decodedJWT = JWT.decode(token);
            Date expiresAt = decodedJWT.getExpiresAt();
            return expiresAt != null && expiresAt.after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 清除当前用户的权限认证缓存
     *
     * @param principals 权限信息
     */
    @Override
    public void clearCache(PrincipalCollection principals) {
        super.clearCache(principals);
    }

}

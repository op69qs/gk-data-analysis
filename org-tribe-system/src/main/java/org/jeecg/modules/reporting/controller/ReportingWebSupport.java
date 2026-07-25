package org.jeecg.modules.reporting.controller;

import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.vo.LoginUser;

abstract class ReportingWebSupport {
    protected <T> Result<T> success(T value, String message) {
        Result<T> result = new Result<>();
        result.setSuccess(true);
        result.setCode(200);
        result.setMessage(message);
        result.setResult(value);
        return result;
    }

    protected CurrentUser currentUser() {
        try {
            Object principal = SecurityUtils.getSubject().getPrincipal();
            if (principal instanceof LoginUser) {
                LoginUser login = (LoginUser) principal;
                String username = login.getUsername() == null ? "anonymous" : login.getUsername();
                return new CurrentUser(login.getId() == null ? username : login.getId(), username);
            }
        } catch (RuntimeException ignored) {
            // Standalone controller tests do not install Shiro.
        }
        return new CurrentUser("anonymous", "anonymous");
    }

    protected static final class CurrentUser {
        final String userId;
        final String username;
        CurrentUser(String userId, String username) { this.userId = userId; this.username = username; }
    }
}

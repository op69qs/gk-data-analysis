package org.jeecg.modules.system.controller.advice;

import org.jeecg.common.api.vo.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestControllerAdvice
public class LegacyPlatformOfflineAdvice {

    private static final String OFFLINE_MESSAGE = "vis旧平台入口已停用，请从分析平台访问";

    private static final Set<String> EXACT_BLOCKED_PATHS = new HashSet<>(Arrays.asList(
        "/sys/login",
        "/sys/logout",
        "/sys/mLogin",
        "/sys/phoneLogin"
    ));

    private static final List<String> PREFIX_BLOCKED_PATHS = Arrays.asList(
        "/thirdLogin",
        "/sys/user",
        "/sys/role",
        "/sys/permission",
        "/sys/sysUserAgent",
        "/sys/sysDepartRole",
        "/sys/sysDepartPermission"
    );

    @ModelAttribute
    public void blockLegacyPlatformEntry(HttpServletRequest request) {
        String requestPath = request.getRequestURI();
        if (!shouldBlock(requestPath)) {
            return;
        }
        throw new LegacyPlatformOfflineException();
    }

    @ExceptionHandler(LegacyPlatformOfflineException.class)
    public ResponseEntity<Result<Object>> handleLegacyPlatformOffline() {
        Result<Object> result = Result.error(HttpStatus.GONE.value(), OFFLINE_MESSAGE);
        return ResponseEntity.status(HttpStatus.GONE).body(result);
    }

    private boolean shouldBlock(String requestPath) {
        if (EXACT_BLOCKED_PATHS.contains(requestPath)) {
            return true;
        }
        for (String prefix : PREFIX_BLOCKED_PATHS) {
            if (requestPath.equals(prefix) || requestPath.startsWith(prefix + "/")) {
                return true;
            }
        }
        return false;
    }

    private static final class LegacyPlatformOfflineException extends RuntimeException {
        private static final long serialVersionUID = 1L;
    }
}
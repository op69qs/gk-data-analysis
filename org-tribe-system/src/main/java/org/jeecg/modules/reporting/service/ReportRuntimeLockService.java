package org.jeecg.modules.reporting.service;

import org.jeecg.modules.reporting.config.ReportingProperties;
import org.jeecg.modules.reporting.mapper.ReportRuntimeLockMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class ReportRuntimeLockService {
    static final String TIMS_LOCK = "TIMS_LOAD";
    private final ReportRuntimeLockMapper mapper;
    private final ReportingProperties properties;

    public ReportRuntimeLockService(ReportRuntimeLockMapper mapper, ReportingProperties properties) {
        this.mapper = mapper;
        this.properties = properties;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean acquireTims(String owner) {
        if (owner == null || owner.trim().isEmpty()) throw new IllegalArgumentException("TIMS 租约 owner 不能为空");
        Date now = new Date();
        Date until = new Date(now.getTime() + properties.getTimsLockLeaseMinutes() * 60_000L);
        return mapper.acquire(TIMS_LOCK, owner, until, now) == 1;
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void assertOwnedForUpdate(String owner) {
        String actual = mapper.lockOwnerForUpdate(TIMS_LOCK);
        if (!owner.equals(actual)) throw new IllegalStateException("TIMS 全局执行租约已失效");
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void releaseTims(String owner) {
        if (owner != null) mapper.release(TIMS_LOCK, owner);
    }
}

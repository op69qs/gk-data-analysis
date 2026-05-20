package org.jeecg.modules.audit.service;

import javax.servlet.http.HttpServletRequest;
import org.jeecg.modules.audit.vo.DataAnalysisMenuEntryAuditRequest;
import org.jeecg.modules.system.entity.SysUser;

public interface IDataAnalysisBizAuditService {

    void recordOAuthLoginSuccess(String portalUsername,
                                 String portalUserId,
                                 SysUser localUser,
                                 HttpServletRequest request,
                                 String portalAccessToken);

    void recordOAuthLoginFailure(String portalUsername,
                                 String portalUserId,
                                 String errorMessage,
                                 HttpServletRequest request,
                                 String portalAccessToken);

    void recordMenuEntry(DataAnalysisMenuEntryAuditRequest request,
                         HttpServletRequest servletRequest);

    void retryFailedAudits();
}

package org.indicatorsLib;

import com.alibaba.fastjson.JSONObject;

import org.indicatorsLib.util.PageData;
import org.indicatorsLib.util.IndicatorRequestContext;
import org.indicatorsLib.util.IndicatorDataScopeSql;
import org.indicatorsLib.util.UuidUtil;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;



import javax.servlet.http.HttpServletRequest;

public class BaseController {

    public static final String ANALYSIS_USER_ID_HEADER = "X-Analysis-User-Id";
    public static final String ANALYSIS_SUBJECT_CODE_HEADER = "X-Analysis-Subject-Code";
    public static final String ANALYSIS_GUOKU_ID_HEADER = "X-Analysis-Guoku-Id";

    /** new PageData对象
     * @return
     */
    public PageData getPageData(){
        return new PageData(this.getRequest());
    }

    /** new PageData对象
     * @return
     */
    public PageData getPageData(JSONObject jobj){
        return jobj == null?new PageData(this.getRequest()):new PageData(jobj);
    }


    /**得到ModelAndView
     * @return
     */
    public ModelAndView getModelAndView(){
        return new ModelAndView();
    }

    /**得到request对象
     * @return
     */
    public HttpServletRequest getRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request;
    }

    /**得到服务器根URL
     * @return
     */
    public String getRootURL() {
        HttpServletRequest request = this.getRequest();
        return request.getScheme() + "://" + request.getServerName()
                + ":" +request.getServerPort() + request.getServletContext().getContextPath();
    }

    /**得到32位的uuid
     * @return
     */
    public String get32UUID(){
        return UuidUtil.get32UUID();
    }

    /** Current authenticated identity supplied by the gateway, never by request JSON. */
    protected IndicatorRequestContext getIndicatorRequestContext() {
        HttpServletRequest request = getRequest();
        return new IndicatorRequestContext(
                request.getHeader(ANALYSIS_USER_ID_HEADER),
                request.getHeader(ANALYSIS_SUBJECT_CODE_HEADER),
                request.getHeader(ANALYSIS_GUOKU_ID_HEADER));
    }

    /** Overwrite all legacy creator aliases so callers cannot impersonate another user. */
    protected PageData applyCurrentUser(PageData pageData) {
        String userId = getIndicatorRequestContext().getUserId();
        pageData.put("userId", userId);
        pageData.put("USERID", userId);
        pageData.put("ADD_USERID", userId);
        pageData.put("MODIFY_USERID", userId);
        return pageData;
    }

    protected String applyIndicatorDataScope(String sourceSql, String dimensionFlag) {
        return IndicatorDataScopeSql.apply(sourceSql, dimensionFlag, getIndicatorRequestContext());
    }
}

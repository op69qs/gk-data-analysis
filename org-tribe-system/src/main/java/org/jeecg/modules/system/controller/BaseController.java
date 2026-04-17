package org.jeecg.modules.system.controller;

import com.alibaba.fastjson.JSONObject;

import org.jeecg.modules.system.util.PageData;
import org.jeecg.modules.system.util.UuidUtil;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

public class BaseController {

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
}

package org.triber.analysis.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseController {

    /**
     * new PageData对象
     *
     * @return
     */
    public PageData getPageData() {
        return new PageData(this.getRequest());
    }

    /**
     * 得到ModelAndView
     *
     * @return
     */
    public ModelAndView getModelAndView() {
        return new ModelAndView();
    }

    /**
     * 得到request对象
     *
     * @return
     */
    public HttpServletRequest getRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request;
    }

    /**
     * 得到服务器根URL
     *
     * @return
     */
    public String getRootURL() {
        HttpServletRequest request = this.getRequest();
        return request.getScheme() + "://" + request.getServerName()
                + ":" + request.getServerPort() + request.getServletContext().getContextPath();
    }

    /**
     * 得到32位的uuid
     *
     * @return
     */
    public String get32UUID() {
        return UuidUtil.get32UUID();
    }

    /**
     * @Author haojiang
     * @Date 2020/11/20 17:39
     * @Description 在clickhouse执行自定义的sql
     */
    public List<Map<String, Object>> getSQLResults(String sql) {
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            List<Entity> dataList = Db.use().query(sql);
            for (Entity e : dataList) {
                Map<String, Object> map1 = new HashMap<>();
                BeanUtil.copyProperties(e, map1);
                list.add(map1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * @Author haojiang
     * @Date 2020/11/21 18:17
     * @Description 查询clickhouse数据库中的数据表
     */
    public List<Map<String, Object>> getClickhouseTables(Map<String, Object> map) {
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            String sql = "SELECT name AS table_name FROM system.tables WHERE database='" + map.get("dataBase") + "' AND name " + map.get("table_name");
            List<Entity> dataList = Db.use().query(sql);
            for (Entity e : dataList) {
                Map<String, Object> map1 = new HashMap<>();
                BeanUtil.copyProperties(e, map1);
                list.add(map1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}

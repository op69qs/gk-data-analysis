package org.seo.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.seo.BaseController;
import org.seo.config.DataSourceContextHolder;
import org.seo.service.DataSourceService;
import org.seo.util.DBHelper;
import org.seo.util.DataSourceConnectionSupport;
import org.seo.util.DateUtil;
import org.seo.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@Api(tags="数据源维护")
@RequestMapping(value = "/dataSourceController", produces = MediaType.APPLICATION_JSON_VALUE)
public class DataSourceController extends BaseController {

    @Autowired
    private DataSourceService dataSourceService;

    @RequestMapping(value = {"/getDataSourcePage"}, method = RequestMethod.POST)
    @ApiOperation("获取数据源(分页)")
    public Object getDataSourcePage(@RequestBody(required = false) JSONObject param){
        //设置数据源
        DataSourceContextHolder.setDBType("default");
        Map<String, Object> jsonMap = new HashMap<>();
        try{
            PageData pd = this.getPageData(param);
            Integer pageNo = (Integer.parseInt(pd.getString("pageNo"))-1)*Integer.parseInt(pd.getString("pageSize"));
            pd.put("page",pageNo);
            pd.put("rows",Integer.parseInt(pd.getString("pageSize")));
            List<Map<String, Object>> result = dataSourceService.getDataSourcePage(pd);
            Integer count = dataSourceService.countDataSource(pd);
            jsonMap.put("total", count);//total键 存放总记录数，必须的
            jsonMap.put("rows", result);//rows键 存放每页记录 list
            jsonMap.put("result","success");
        }catch (Exception e){
            jsonMap.put("result","false");
            jsonMap.put("msg",e.getMessage());
        }
        return jsonMap;
    }

    @RequestMapping(value={"/getDataSource"} , method = RequestMethod.POST)
    @ApiOperation("获取数据源(不分页)")
    public Object getDataSource(@RequestBody(required = false) JSONObject param){
        //设置数据源
        DataSourceContextHolder.setDBType("default");
        Map<String, Object> jsonMap = new HashMap<>();
        try{
            PageData pd = this.getPageData(param);
            jsonMap.put("rows", dataSourceService.getDataSource(pd));
            jsonMap.put("result","success");
        }catch (Exception e){
            jsonMap.put("result","false");
            jsonMap.put("msg",e.getMessage());
        }
        return jsonMap;
    }

    @RequestMapping(value={"/delDataSource"} , method = RequestMethod.POST)
    @ApiOperation("删除数据源")
    public Map<String,Object>delDataSource(@RequestBody(required = false) JSONObject param){
        //设置数据源
        DataSourceContextHolder.setDBType("default");
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        result.put("msg", "删除成功！");
        result.put("result", "success");
        try {
            String id = pd.getString("ID");
            if (null != id && !id.equals("")){
                String ids[] = id.split(",");
                for (String i:ids){
                    pd.put("ID",i);
                    dataSourceService.delDataSource(pd);
                    pd.put("SOURCE_ID",i);
                    dataSourceService.delDataBase(pd);
                }
            }
        } catch (Exception e) {
            result.put("msg", e.getMessage());
            result.put("result", "false");
        }
        return result;
    }

    @RequestMapping(value={"/addDataSource"} , method = RequestMethod.POST)
    @ApiOperation("新增")
    public Map<String,Object>addDataSource(@RequestBody(required = false) JSONObject param){
        //设置数据源
        DataSourceContextHolder.setDBType("default");
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        result.put("msg", "新增成功！");
        result.put("result", "success");
        try {
            String id = pd.getString("ID");
            if (null == id || id.equals("")){
                id = get32UUID();
                pd.put("ID",id);
                pd.put("CREATE_TIME", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD));
                dataSourceService.addDataSource(pd);
            }
            pd.put("ID",get32UUID());
            pd.put("SOURCE_ID",id);
            dataSourceService.addDataBase(getEnum(pd));
        } catch (Exception e) {
            result.put("msg", e.getMessage());
            result.put("result", "false");
        }
        return result;
    }

    @RequestMapping(value={"/editDataSource"} , method = RequestMethod.POST)
    @ApiOperation("修改")
    public Map<String,Object>editDataSource(@RequestBody(required = false) JSONObject param){
        //设置数据源
        DataSourceContextHolder.setDBType("default");
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        result.put("msg", "修改成功！");
        result.put("result", "success");
        try {
            String id = pd.getString("ID");
            String DBNAME = pd.getString("DBNAME");
            String USERNAME = pd.getString("USERNAME");
            String PASSWORD = pd.getString("PASSWORD");
            String STATE = pd.getString("STATE");
            String[] dbNames = null;
            String[] schemaNames = null;
            if (null != DBNAME && !DBNAME.equals("")) {
                dbNames = DBNAME.split(",");
                schemaNames = DataSourceConnectionSupport.schemaValues(
                        pd.getString("SCHEMA_NAME"), dbNames.length);
            }
            pd.put("SOURCE_ID",id);
            dataSourceService.delDataBase(pd);
            dataSourceService.editDataSource(pd);
            pd.put("CREATE_TIME", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD));
            if (null != DBNAME && !DBNAME.equals("")){
                String userNames[] = USERNAME.split(",");
                String passWords[] = PASSWORD.split(",");
                String states[] = STATE.split(",");
                for (int i=0;i<dbNames.length;i++){
                    pd.put("ID",get32UUID());
                    pd.put("DBNAME",dbNames[i]);
                    pd.put("SCHEMA_NAME",schemaNames[i]);
                    pd.put("USERNAME",userNames[i]);
                    pd.put("PASSWORD",passWords[i]);
                    pd.put("STATE",states[i]);
                    dataSourceService.addDataBase(getEnum(pd));
                }
            }
        } catch (Exception e) {
            result.put("msg", e.getMessage());
            result.put("result", "false");
        }
        return result;
    }

    private PageData getEnum(PageData pd){
        String url = "";
        List<Map<String,Object>> dataSourceEnum = dataSourceService.getDataSourceEnum(pd);
        if (null != dataSourceEnum && dataSourceEnum.size()>0){
            url = DataSourceConnectionSupport.buildUrl(
                    dataSourceEnum.get(0).get("URL").toString(),
                    pd.getString("IP"),
                    pd.getString("PORT"),
                    pd.getString("DBNAME"),
                    pd.getString("SCHEMA_NAME"));
            pd.put("DATASOURCE_URL",url);
            pd.put("DRIVERCLASS_NAME",dataSourceEnum.get(0).get("DRIVERCLASS").toString());
        }
        return pd;
    }

    @RequestMapping(value={"/getDataSourceEnumSelect"} , method = RequestMethod.POST)
    @ApiOperation("获取数据源枚举表下拉菜单")
    public Object getDataSourceEnumSelect(){
        //设置数据源
        DataSourceContextHolder.setDBType("default");
        Map<String, Object> jsonMap = new HashMap<>();
        try{
            jsonMap.put("rows", dataSourceService.getDataSourceEnumSelect());
            jsonMap.put("result","success");
        }catch (Exception e){
            jsonMap.put("result","false");
            jsonMap.put("msg",e.getMessage());
        }
        return jsonMap;
    }

    @RequestMapping(value={"/getDataBase"} , method = RequestMethod.POST)
    @ApiOperation("获取数据库")
    public Object getDataBase(@RequestBody(required = false) JSONObject param){
        //设置数据源
        DataSourceContextHolder.setDBType("default");
        Map<String, Object> jsonMap = new HashMap<>();
        PageData pd = this.getPageData(param);
        try{
            jsonMap.put("rows", dataSourceService.getDataBase(pd));
            jsonMap.put("result","success");
        }catch (Exception e){
            jsonMap.put("result","false");
            jsonMap.put("msg",e.getMessage());
        }
        return jsonMap;
    }

    @RequestMapping(value={"/getDataSourceName"} , method = RequestMethod.POST)
    @ApiOperation("获取数据库名称")
    public Object getDataSourceName(@RequestBody(required = false) JSONObject param){
        //设置数据源
        DataSourceContextHolder.setDBType("default");
        Map<String, Object> jsonMap = new HashMap<>();
        PageData pd = this.getPageData(param);
        try{
            jsonMap.put("rows", dataSourceService.getDataSourceName(pd));
            jsonMap.put("result","success");
        }catch (Exception e){
            jsonMap.put("result","false");
            jsonMap.put("msg",e.getMessage());
        }
        return jsonMap;
    }

    @RequestMapping(value={"/testConnection"} , method = RequestMethod.POST)
    @ApiOperation("数据源链接测试")
    public Map<String,Object>testConnection(@RequestBody(required = false) JSONObject param){
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> temp = new HashMap<>();
        PageData pd = this.getPageData(param);
        result.put("msg", "链接成功！");
        result.put("result", "success");
        String TYPE = pd.getString("TYPE");
        Connection conn = null;
        if (null != TYPE && !TYPE.equals("")){
            String url = "";
            if (TYPE.equals("Mysql")){
                url = "jdbc:mysql://ip:port/DBNAME?characterEncoding=UTF-8&useUnicode=true&useSSL=false".replace("ip",pd.getString("IP"));
                url = url.replace("port",pd.getString("PORT"));
                url = url.replace("DBNAME",pd.getString("DBNAME"));
                temp = DBHelper.initMysql(url,pd.getString("USERNAME"),pd.getString("PASSWORD"),false);
                conn = (Connection)temp.get("conn");
            }
            if (TYPE.equals("Vastbase") || TYPE.equals("PostgreSQL")){
                String template = TYPE.equals("Vastbase")
                        ? "jdbc:postgresql://ip:port/DBNAME?currentSchema=SCHEMA_NAME"
                        : "jdbc:postgresql://ip:port/DBNAME";
                url = DataSourceConnectionSupport.buildUrl(
                        template,
                        pd.getString("IP"),
                        pd.getString("PORT"),
                        pd.getString("DBNAME"),
                        pd.getString("SCHEMA_NAME"));
                temp = DBHelper.initPostgresql(url,pd.getString("USERNAME"),pd.getString("PASSWORD"),false);
                conn = (Connection)temp.get("conn");
                if (TYPE.equals("Vastbase") && conn != null
                        && !schemaExists(conn, pd.getString("SCHEMA_NAME"))) {
                    result.put("msg", "Vastbase Schema不存在");
                    result.put("result", "false");
                }
            }
            if (TYPE.equals("DB2")){
                url = "jdbc:db2://ip:port/DBNAME".replace("ip",pd.getString("IP"));
                url = url.replace("port",pd.getString("PORT"));
                url = url.replace("DBNAME",pd.getString("DBNAME"));
                temp = DBHelper.initDB2(url,pd.getString("USERNAME"),pd.getString("PASSWORD"),false);
                conn = (Connection)temp.get("conn");
            }
            if (TYPE.equals("Clickhouse")){
                url = "jdbc:clickhouse://ip:port/DBNAME".replace("ip",pd.getString("IP"));
                url = url.replace("port",pd.getString("PORT"));
                url = url.replace("DBNAME",pd.getString("DBNAME"));
                temp = DBHelper.initClickHouse(url,pd.getString("USERNAME"),pd.getString("PASSWORD"),false);
                conn = (Connection)temp.get("conn");
            }
        }
        if (null == conn){
            result.put("msg", "链接失败,错误信息为: "+String.valueOf(temp.get("se")));
            result.put("result","false");
        }
        DBHelper.closeDB(conn,null,null);
        return result;
    }

    private boolean schemaExists(Connection connection, String schemaName) {
        String sql = "SELECT 1 FROM information_schema.schemata WHERE schema_name = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, schemaName);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (Exception e) {
            log.error("校验Vastbase Schema失败", e);
            return false;
        }
    }
}

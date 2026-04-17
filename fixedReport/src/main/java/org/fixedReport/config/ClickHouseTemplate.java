package org.fixedReport.config;

import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import cn.hutool.db.sql.Condition;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.sql.SQLException;
import java.util.List;

//@Configuration      //1.主要用于标记配置类，兼备Component的效果。
//@EnableScheduling   // 2.开启定时任务
public class ClickHouseTemplate {

    //3.添加定时任务
//    @Scheduled(cron = "0/5 * * * * ?")
    //或直接指定时间间隔，例如：5秒
    @Scheduled(cron = "0 */1 * * * ?")
    //@Scheduled(fixedRate=5000)
    private void configureTasks() throws SQLException {
        //     查询全部字段
        //      test.user_local  为表名
        List<Entity> list= Db.use().findAll("test.user_local");
        for(Entity e: list){
            System.err.println(e.get("id"));
            System.err.println(e.get("name"));
        }
        //     条件查询  test.user_local 表名  id 字段  1 值  (备注  值必须和库中字段类型对应 )
        List<Entity> listCondition = Db.use().findAll(Entity.create("test.user_local").set("id", 1));
        for(Entity e: listCondition){
            System.err.println(e.get("id"));
            System.err.println(e.get("name"));
        }
        //     模糊查询  name  字段  to  模糊值
        List<Entity> listLike = Db.use().findLike("test.user_local", "name", "to", Condition.LikeType.Contains);
        for(Entity e: listLike){
            System.err.println(e.get("id"));
            System.err.println(e.get("name"));
        }

        //      执行SQL语句
        //      查询
        List<Entity> listSql = Db.use().query("select * from test.user_local where id < ? and name = ?", 3,"jack");
        for(Entity e: listSql){
            System.err.println(e.get("id"));
            System.err.println(e.get("name"));
        }
    }

}

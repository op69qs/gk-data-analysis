package org.dockingProjects;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@Slf4j
@MapperScan("org.dockingProjects.mapper")
@ComponentScan("org.dockingProjects")
@SpringBootApplication
public class SystemDockingApplication {

    public static void main(String[] args) {
//        args = new String[1];
//        args[0] = "--test=0";
//        args[0] = "--history=0e6a26e43ca740298a793a3dba8c4e90";
//        args[0] = "--processId=0e6a26e43ca740298a793a3dba8c4e90";
//        args[0] = "--processId=6be0cf1f6432447f893efb7e7c1d8a7b";
        SpringApplication.run(SystemDockingApplication.class, args);
    }

}

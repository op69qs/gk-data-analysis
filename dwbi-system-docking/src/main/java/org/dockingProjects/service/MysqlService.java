package org.dockingProjects.service;


import org.dockingProjects.mapper.clickhouse.HouseMapper;
import org.dockingProjects.mapper.mysql.MysqlMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
@SuppressWarnings("all")
public class MysqlService {

    @Autowired
    private MysqlMapper mysqlMapper;


    public List<Map<String,Object>> findAll() {

        return mysqlMapper.findAll();
    }
}

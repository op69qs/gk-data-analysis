package org.dockingProjects.service;

import org.dockingProjects.mapper.clickhouse.HouseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
@SuppressWarnings("all")
public class HouseService {

    @Autowired
    private HouseMapper houseMapper;

    public List<Map<String,Object>> findAll() {

        return houseMapper.findAll();
    }
}

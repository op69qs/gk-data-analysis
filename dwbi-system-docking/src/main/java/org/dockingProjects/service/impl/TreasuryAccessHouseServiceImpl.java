package org.dockingProjects.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.dockingProjects.mapper.clickhouse.HouseMapper;
import org.dockingProjects.mapper.mysql.TreasuryAccessMapper;
import org.dockingProjects.service.TreasuryAccessHouseService;
import org.dockingProjects.service.TreasuryAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author haojiang.
 * @Ddate 2020/8/14 17:04
 * @Description
 */
@Service
public class TreasuryAccessHouseServiceImpl implements TreasuryAccessHouseService {

    @Autowired
    private HouseMapper houseMapper;


    @Override
    public void insertThreeCertificatesSyncreticHouse(List<Map<String, Object>> list) {
        houseMapper.insertThreeCertificatesSyncretic(list);
    }

    @Override
    public void insertEnterpriseBasicInformationHouse(List<Map<String, Object>> list) {
        houseMapper.insertEnterpriseBasicInformation(list);
    }

    @Override
    public void insertNonEnterpriseBasicInformationHouse(List<Map<String, Object>> list) {
        houseMapper.insertNonEnterpriseBasicInformation(list);
    }
}

package org.fixedReport.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.fixedReport.dao.mapper.fixedReport.TreasuryAccessMapper;
import org.fixedReport.service.TreasuryAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author .
 * @Ddate 2020/8/14 17:04
 * @Description
 */
@Slf4j
@Service
public class TreasuryAccessServiceImpl implements TreasuryAccessService {

    @Autowired
    private TreasuryAccessMapper treasuryAccessMapper;


    @Override
    public void insertThreeCertificatesSyncretic(List<Map<String, Object>> list) {
        treasuryAccessMapper.insertThreeCertificatesSyncretic(list);
    }

    @Override
    public void insertEnterpriseBasicInformation(List<Map<String, Object>> list) {
        treasuryAccessMapper.insertEnterpriseBasicInformation(list);
    }

    @Override
    public void insertNonEnterpriseBasicInformation(List<Map<String, Object>> list) {
        treasuryAccessMapper.insertNonEnterpriseBasicInformation(list);
    }

    @Override
    public void delThreeCertificatesSyncretic() {
        treasuryAccessMapper.delThreeCertificatesSyncretic();
    }
    @Override
    public void delEnterpriseBasicInformation() {
        treasuryAccessMapper.delEnterpriseBasicInformation();
    }
    @Override
    public void delNonEnterpriseBasicInformation() {
        treasuryAccessMapper.delNonEnterpriseBasicInformation();
    }



    @Override
    public void callThreeCertificatesSyncretic() {
        treasuryAccessMapper.callThreeCertificatesSyncretic();
    }
    @Override
    public void callEnterpriseBasicInformation() {
        treasuryAccessMapper.callEnterpriseBasicInformation();
    }
    @Override
    public void callNonEnterpriseBasicInformation() {
        treasuryAccessMapper.callNonEnterpriseBasicInformation();
    }

}

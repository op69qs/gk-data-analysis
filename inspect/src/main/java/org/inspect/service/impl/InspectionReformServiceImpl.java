package org.inspect.service.impl;

import org.inspect.dao.mapper.inspect.InspectionReformMapper;
import org.inspect.service.InspectionReformService;
import org.inspect.util.PageData;
import org.inspect.util.UuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class InspectionReformServiceImpl implements InspectionReformService {

    @Autowired
    private InspectionReformMapper inspectionReformMapper;

    @Override
    public List<Map<String, Object>> getInspectionReformPage(PageData pd) {
        return inspectionReformMapper.getInspectionReformPage(pd);
    }

    @Override
    public List<Map<String, Object>> getInspectionReformData(PageData pd) {
        return inspectionReformMapper.getInspectionReformData(pd);
    }

    @Override
    public List<Map<String, Object>> getInspectionReformSchemeData(PageData pd) {
        return inspectionReformMapper.getInspectionReformSchemeData(pd);
    }

    @Override
    public List<Map<String, Object>> getInspectionReformReplayData(PageData pd) {
        return inspectionReformMapper.getInspectionReformReplayData(pd);
    }

    @Override
    public List<Map<String, Object>> getQuestionLedgerLvById(PageData pd) {
        return inspectionReformMapper.getQuestionLedgerLvById(pd);
    }

    @Override
    public List<Map<String, Object>> getQuestionLedgerLvOne(PageData pd) {
        return inspectionReformMapper.getQuestionLedgerLvOne(pd);
    }

    @Override
    public List<Map<String, Object>> getReformData(PageData pd) {
        return inspectionReformMapper.getReformData(pd);
    }

    @Override
    public List<Map<String, Object>> isComplete(PageData pd) {
        return inspectionReformMapper.isComplete(pd);
    }

    @Override
    public Boolean updateReform(PageData pd) {
        String type = pd.getString("type");
        if (null != type && type.equals("add")){
            pd.put("ID", UuidUtil.get32UUID());
            addInspectionReform(pd);
        }
        if (null != type && type.equals("update")){
            inspectionReformMapper.updateReform(pd);
        }
        if (null != type && type.equals("delete")){
            List<Map<String, Object>> map = getInspectionReformData(pd);
            if (null != map && !map.isEmpty()){
                pd.put("ID",map.get(0).get("ID"));
                delInspectionReform(pd);
                PageData schemePd = new PageData();
                schemePd.put("REFORM_ID",map.get(0).get("ID"));
                List<Map<String, Object>> schMap = getInspectionReformSchemeData(schemePd);
                if (null != schMap && !schMap.isEmpty()){
                    delInspectionReformScheme(schemePd);
                    PageData repPd = new PageData();
                    repPd.put("ID",schMap.get(0).get("ID"));
                }
            }
        }
        return true;
    }

    @Override
    public void editIsScheme(PageData pd) {
        inspectionReformMapper.updateReform(pd);
    }

    @Override
    public void editReform(PageData pd) {
        inspectionReformMapper.updateReform(pd);
    }

    @Override
    public void delInspectionReform(PageData pd) {
        inspectionReformMapper.delInspectionReform(pd);
    }

    @Override
    public void delInspectionReformScheme(PageData pd) {
        inspectionReformMapper.delInspectionReformScheme(pd);
    }

    @Override
    public void delInspectionReformReplay(PageData pd) {
        inspectionReformMapper.delInspectionReformReplay(pd);
    }

    @Override
    public Integer countInspectionReform(PageData pd) {
        return inspectionReformMapper.countInspectionReform(pd);
    }

    @Override
    public void addInspectionReform(PageData pd) {
        inspectionReformMapper.addInspectionReform(pd);
    }

    @Override
    public void addInspectionScheme(PageData pd) {
        inspectionReformMapper.addInspectionScheme(pd);
    }

    @Override
    public void addInspectionReplay(PageData pd) {
        inspectionReformMapper.addInspectionReplay(pd);
    }

    @Override
    public List<Map<String, Object>> toAddCase(PageData pd) {
        return inspectionReformMapper.toAddCase(pd);
    }
}

package org.inspect.service.impl;

import org.inspect.dao.mapper.inspect.InspectionPostSVReformMapper;
import org.inspect.service.InspectionPostSVReformService;
import org.inspect.util.PageData;
import org.inspect.util.UuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class InspectionPostSVReformServiceImpl implements InspectionPostSVReformService {

    @Autowired
    private InspectionPostSVReformMapper inspectionPostSVReformMapper;

    @Override
    public List<Map<String, Object>> getInspectionReformPage(PageData pd) {
        return inspectionPostSVReformMapper.getInspectionReformPage(pd);
    }

    @Override
    public List<Map<String, Object>> getInspectionReformData(PageData pd) {
        return inspectionPostSVReformMapper.getInspectionReformData(pd);
    }

    @Override
    public List<Map<String, Object>> getInspectionReformSchemeData(PageData pd) {
        return inspectionPostSVReformMapper.getInspectionReformSchemeData(pd);
    }

    @Override
    public List<Map<String, Object>> getInspectionReformReplayData(PageData pd) {
        return inspectionPostSVReformMapper.getInspectionReformReplayData(pd);
    }

    @Override
    public List<Map<String, Object>> getQuestionLedgerLvById(PageData pd) {
        return inspectionPostSVReformMapper.getQuestionLedgerLvById(pd);
    }

    @Override
    public List<Map<String, Object>> getQuestionLedgerLvOne(PageData pd) {
        return inspectionPostSVReformMapper.getQuestionLedgerLvOne(pd);
    }

    @Override
    public List<Map<String, Object>> getReformData(PageData pd) {
        return inspectionPostSVReformMapper.getReformData(pd);
    }

    @Override
    public List<Map<String, Object>> isComplete(PageData pd) {
        return inspectionPostSVReformMapper.isComplete(pd);
    }

    @Override
    public Boolean updateReform(PageData pd) {
        String type = pd.getString("type");
        if (null != type && type.equals("add")){
            pd.put("ID", UuidUtil.get32UUID());
            addInspectionReform(pd);
        }
        if (null != type && type.equals("update")){
            inspectionPostSVReformMapper.updateReform(pd);
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
        inspectionPostSVReformMapper.updateReform(pd);
    }

    @Override
    public void editReform(PageData pd) {
        inspectionPostSVReformMapper.updateReform(pd);
    }

    @Override
    public void delInspectionReform(PageData pd) {
        inspectionPostSVReformMapper.delInspectionReform(pd);
    }

    @Override
    public void delInspectionReformScheme(PageData pd) {
        inspectionPostSVReformMapper.delInspectionReformScheme(pd);
    }

    @Override
    public void delInspectionReformReplay(PageData pd) {
        inspectionPostSVReformMapper.delInspectionReformReplay(pd);
    }

    @Override
    public Integer countInspectionReform(PageData pd) {
        return inspectionPostSVReformMapper.countInspectionReform(pd);
    }

    @Override
    public void addInspectionReform(PageData pd) {
        inspectionPostSVReformMapper.addInspectionReform(pd);
    }

    @Override
    public void addInspectionScheme(PageData pd) {
        inspectionPostSVReformMapper.addInspectionScheme(pd);
    }

    @Override
    public void addInspectionReplay(PageData pd) {
        inspectionPostSVReformMapper.addInspectionReplay(pd);
    }

    @Override
    public List<Map<String, Object>> toAddCase(PageData pd) {
        return inspectionPostSVReformMapper.toAddCase(pd);
    }
}

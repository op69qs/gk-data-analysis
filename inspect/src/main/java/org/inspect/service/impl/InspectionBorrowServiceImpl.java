package org.inspect.service.impl;

import org.inspect.dao.mapper.inspect.InspectionBorrowMapper;
import org.inspect.dao.mapper.inspect.InspectionUserMapper;
import org.inspect.model.BorrowData;
import org.inspect.service.InspectionBorrowService;
import org.inspect.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class InspectionBorrowServiceImpl implements InspectionBorrowService {

    @Autowired
    private InspectionBorrowMapper inspectionBorrowMapper;
    @Autowired
    private InspectionUserMapper inspectionUserMapper;

    @Override
    public List<Map<String, Object>> getInspectionBorrowData(PageData pd) {
        return inspectionBorrowMapper.getInspectionBorrowData(pd);
    }

    @Override
    public List<Map<String, Object>> getInspectionBorrowTemp(PageData pd) {
        return inspectionBorrowMapper.getInspectionBorrowTemp(pd);
    }

    @Override
    public void addInspectionBorrow(PageData pd) {
        inspectionBorrowMapper.addInspectionBorrow(pd);
    }

    @Override
    public void editInspectionBorrow(PageData pd) {
        inspectionBorrowMapper.editInspectionBorrow(pd);
    }

    @Override
    public void editBorrowUser(PageData pd) {
        String task_id = pd.getString("TASK_ID");
        List<Map<String, Object>> borrowMap = getInspectionBorrowData(pd);
        if (null != borrowMap && !borrowMap.isEmpty()){
            PageData userPd = new PageData();
            userPd.put("INSPECTION_TASK_ID",task_id);
            List<Map<String,Object>>userMap = inspectionUserMapper.getUserData(userPd);
            if (null != userMap && !userMap.isEmpty()){
                PageData borrowPd = new PageData();
                borrowPd.put("TASK_ID",task_id);
                for (Map<String,Object>user:userMap){
                    if (user.get("INSPECTION_GROUP_DUTIES").equals("1")){
                        borrowPd.put("leader",user.get("NAME"));
                    }
                    if (user.get("INSPECTION_GROUP_DUTIES").equals("2")){
                        borrowPd.put("borrow_user",user.get("NAME"));
                    }
                }
                inspectionBorrowMapper.editBorrowUser(borrowPd);
            }
        }
    }

    @Override
    public void editBorrowCharge(PageData pd) {
        inspectionBorrowMapper.editBorrowCharge(pd);
    }

    @Override
    public void delInspectionBorrow(PageData pd) {
        inspectionBorrowMapper.delInspectionBorrow(pd);
    }

    @Override
    public List<Map<String, Object>> checkRepeat(PageData pd) {
        return inspectionBorrowMapper.checkRepeat(pd);
    }

    @Override
    public List<BorrowData> getInspectionBorrow(PageData pd) {
        return inspectionBorrowMapper.getInspectionBorrow(pd);
    }

    @Override
    public List<Map<String, Object>> getEnum(PageData pd) {
        return inspectionBorrowMapper.getEnum(pd);
    }
}

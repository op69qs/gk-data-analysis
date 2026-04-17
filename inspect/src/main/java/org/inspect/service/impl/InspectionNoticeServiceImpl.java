package org.inspect.service.impl;

import org.inspect.dao.mapper.inspect.InspectionGroupMapper;
import org.inspect.dao.mapper.inspect.InspectionNoticeMapper;
import org.inspect.dao.mapper.inspect.InspectionUserMapper;
import org.inspect.service.InspectionGroupService;
import org.inspect.service.InspectionNoticeService;
import org.inspect.service.InspectionUserService;
import org.inspect.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class InspectionNoticeServiceImpl implements InspectionNoticeService {

    @Autowired
    private InspectionNoticeMapper inspectionNoticeMapper;
    @Autowired
    private InspectionUserMapper inspectionUserMapper;


    @Override
    public Map<String, Object> getInspectionNoticeData(PageData pd) {
        return inspectionNoticeMapper.getInspectionNoticeData(pd);
    }

    @Override
    public void addInspectionNotice(PageData pd) {
        inspectionNoticeMapper.addInspectionNotice(pd);
    }

    @Override
    public void editInspectionNotice(PageData pd) {
        inspectionNoticeMapper.editInspectionNotice(pd);
    }

    @Override
    public void editNoticeUser(PageData pd) {
        String task_id = pd.getString("TASK_ID");
        Map<String,Object> noticMap = getInspectionNoticeData(pd);
        if (null != noticMap && !noticMap.isEmpty()){
            PageData userPd = new PageData();
            userPd.put("INSPECTION_TASK_ID",task_id);
            List<Map<String,Object>>userMap = inspectionUserMapper.getUserData(userPd);
            if (null != userMap && !userMap.isEmpty()){
                PageData noticePd = new PageData();
                noticePd.put("TASK_ID",task_id);
                String member = "";
                for (Map<String,Object>user:userMap){
                    if (user.get("INSPECTION_GROUP_DUTIES").equals("1")){
                        noticePd.put("LARDER",user.get("NAME"));
                        noticePd.put("LPOST",user.get("ZHIWU_DSCR"));
                    }
                    if (user.get("INSPECTION_GROUP_DUTIES").equals("2")){
                        noticePd.put("CHIEF",user.get("NAME"));
                        noticePd.put("CPOST",user.get("ZHIWU_DSCR"));
                    }
                    if (user.get("INSPECTION_GROUP_DUTIES").equals("3")){
                        member += user.get("NAME").toString()+",";
                    }
                }
                noticePd.put("MEMBER",member.substring(0,member.lastIndexOf(",")));
                inspectionNoticeMapper.editNoticeUser(noticePd);
            }
        }
    }

    @Override
    public void delInspectionNotice(PageData pd) {
        inspectionNoticeMapper.delInspectionNotice(pd);
    }
}

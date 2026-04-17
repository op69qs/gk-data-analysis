package org.inspect.service.impl;


import org.inspect.dao.mapper.inspect.MeetingMinutesMapper;
import org.inspect.service.MeetingMinutesService;
import org.inspect.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MeetingMinutesServiceImpl implements MeetingMinutesService {

    @Autowired
    private MeetingMinutesMapper meetingMinutesMapper;


    @Override
    public Map<String, Object> getMeetingMinutesData(PageData pd) {
        return meetingMinutesMapper.getMeetingMinutesData(pd);
    }

    @Override
    public void addMeetingMinutes(PageData pd) {
        meetingMinutesMapper.addMeetingMinutes(pd);
    }

    @Override
    public void editMeetingMinutes(PageData pd) {
        meetingMinutesMapper.editMeetingMinutes(pd);
    }

    @Override
    public void delMeetingMinutes(PageData pd) {
        meetingMinutesMapper.delMeetingMinutes(pd);
    }
}

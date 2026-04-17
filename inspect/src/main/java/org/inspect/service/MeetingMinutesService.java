package org.inspect.service;


import org.inspect.util.PageData;

import java.util.Map;

public interface MeetingMinutesService {

    Map<String,Object> getMeetingMinutesData(PageData pd);

    void addMeetingMinutes(PageData pd);

    void editMeetingMinutes(PageData pd);

    void delMeetingMinutes(PageData pd);
}

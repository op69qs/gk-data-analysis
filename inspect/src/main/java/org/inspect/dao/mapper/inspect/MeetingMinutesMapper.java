package org.inspect.dao.mapper.inspect;

import org.apache.ibatis.annotations.Param;
import org.inspect.util.PageData;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface MeetingMinutesMapper {

    Map<String,Object> getMeetingMinutesData(@Param("params") PageData pd);

    void addMeetingMinutes(@Param("params") PageData pd);

    void editMeetingMinutes(@Param("params") PageData pd);

    void delMeetingMinutes(@Param("params") PageData pd);

}

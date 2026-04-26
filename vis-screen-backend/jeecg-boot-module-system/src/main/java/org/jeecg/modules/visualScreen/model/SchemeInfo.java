package org.jeecg.modules.visualScreen.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SchemeInfo {

    private String id;
    private String name;
    private String rotation_interval;
    private String add_user;
    private String add_time;
    private List<SchemeRel> schemeRel;
}

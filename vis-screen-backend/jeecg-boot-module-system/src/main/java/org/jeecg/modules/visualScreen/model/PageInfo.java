package org.jeecg.modules.visualScreen.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PageInfo {

    private String id;
    private String name;
    private String template;
    private String background_type;
    private String colour;
    private String thumbnail;
    private String add_user;
    private String title_background;
    private String content;
    private String state;
    private List<PageSub> page_sub;
}

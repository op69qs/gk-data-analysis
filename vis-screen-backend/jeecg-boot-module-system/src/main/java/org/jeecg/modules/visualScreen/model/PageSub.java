package org.jeecg.modules.visualScreen.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PageSub {

    private String id;
    private String gallery_id;
    private String page_id;
    private String time_type;
    private String time_interval;
    private String content;
    private String x;
    private String y;
    private String w;
    private String h;
    private String i;
    private String title;
    private String type;
    private String option;
    private String query_path;
    private String unit;
    private List<PageWhere> pageWhere;
}

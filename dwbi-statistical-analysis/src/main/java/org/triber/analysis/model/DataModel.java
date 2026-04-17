package org.triber.analysis.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: mill
 * @Description: 数据对象
 * @Date: Create in 2017/12/14 10:13
 * @version: V1.0
 * @Modified:
 */
public class DataModel {

    //类别描述
    @Getter@Setter private String name;
    //数据账期
    @Getter@Setter private String dataDate;
    //值
    @Getter@Setter private String value;

    @Override
    public String toString() {
        return "DataModel{" +
                "name='" + name + '\'' +
                ", dataDate='" + dataDate + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}

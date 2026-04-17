package org.triber.analysis.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

/**
 * @Author: mill
 * @Description: 柱状图对象
 * @Date: Create in 2017/12/13 15:30
 * @version: V1.0
 * @Modified:
 */
public class ColumnModel {

    //类别描述
    @Getter@Setter private String name;
    //数据账期
    @Getter@Setter private String[] dataDate;
    //值
    @Getter@Setter private String[] value;

    @Override
    public String toString() {
        return "ColumnModel{" +
                "name='" + name + '\'' +
                ", dataDate=" + Arrays.toString(dataDate) +
                ", value=" + Arrays.toString(value) +
                '}';
    }
}

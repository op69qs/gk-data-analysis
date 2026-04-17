package org.triber.analysis.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by mill on 2017/11/15
 */
public class MapModel {

    //业务类型
    @Getter@Setter private String businessType;
    //地区名称
    @Getter@Setter private String name;
    //存贷款值
    @Getter@Setter private String value;

    @Override
    public String toString() {
        return "{businessType: '" + businessType
                + "', name: '" + name
                + "', value: " + value + "}";
    }
}

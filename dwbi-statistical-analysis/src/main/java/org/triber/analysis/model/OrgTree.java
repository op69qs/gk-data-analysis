package org.triber.analysis.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: mill
 * @Description:
 * @Date: Create in 2017/12/26 16:06
 * @version: V1.0
 * @Modified:
 */
public class OrgTree {

    //编码
    @Setter@Getter private String id;
    //名称
    @Setter@Getter private String name;
    //父级编码
    @Setter@Getter private String pId;

    @Override
    public String toString() {
        return "{\"id\" : \"" + id + "\""
                + "\"name\" : \"" + name +"\""
                + "\"pId\" : \"" + pId + "\"}";
    }
}

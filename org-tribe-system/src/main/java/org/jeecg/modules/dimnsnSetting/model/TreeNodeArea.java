// TreeNode.java

package org.jeecg.modules.dimnsnSetting.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Created by Samer on 2019/9/2.
 */
public class TreeNodeArea implements Serializable {
    @Getter@Setter
    private String value;
    @Getter@Setter
    private String label;
    @Getter@Setter
    private String title;
    @Getter@Setter
    private String key;
    private List<TreeNodeArea> children = new ArrayList();

    public TreeNodeArea() {
    }

    public void addChild(TreeNodeArea node) {
        this.children.add(node);
    }



    public List<TreeNodeArea> getChildren() {
        return this.children;
    }

    public void setChildren(List<TreeNodeArea> children) {
        this.children = children;
    }


}

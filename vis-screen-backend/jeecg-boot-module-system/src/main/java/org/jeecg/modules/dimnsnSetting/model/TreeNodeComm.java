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
public class TreeNodeComm implements Serializable {

    private String value;
    private String label;
    @Getter@Setter
    private String title;
    @Getter@Setter
    private String key;
    private List<TreeNodeComm> children = new ArrayList();

    public TreeNodeComm() {
    }

    public void addChild(TreeNodeComm node) {
        this.children.add(node);
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<TreeNodeComm> getChildren() {
        return this.children;
    }

    public void setChildren(List<TreeNodeComm> children) {
        this.children = children;
    }

} ///:~

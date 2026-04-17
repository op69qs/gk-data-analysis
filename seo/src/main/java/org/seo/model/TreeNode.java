// TreeNode.java

package org.seo.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 国库树形转置辅助类
 * @author Created by Samer on 2019/9/2.
 */
public class TreeNode implements Serializable {

    private String value;
    private String label;
    @Getter@Setter
    private String title;
    @Getter@Setter
    private String key;
    private List<TreeNode> children = new ArrayList();

    public TreeNode() {
    }

    public void addChild(TreeNode node) {
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

    public List<TreeNode> getChildren() {
        return this.children;
    }

    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }

} ///:~

package org.inspect.model;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Created by Samer on 2019/9/2.
 */
public class QueTreeNode implements Serializable {
    @Getter@Setter
    private String value;
    @Getter@Setter
    private String label;
    @Getter@Setter
    private String title;
    @Getter@Setter
    private String key;
    @Getter@Setter
    private String isleaf;
    @Getter@Setter
    private String type;
    private List<QueTreeNode> children = new ArrayList();

    public QueTreeNode() {
    }

    public void addChild(QueTreeNode node) {
        this.children.add(node);
    }



    public List<QueTreeNode> getChildren() {
        return this.children;
    }

    public void setChildren(List<QueTreeNode> children) {
        this.children = children;
    }


}
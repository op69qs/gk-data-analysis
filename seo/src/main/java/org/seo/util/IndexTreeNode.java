package org.seo.util;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Created by haoj on 2020/02/27.
 */
public class IndexTreeNode implements Serializable {
    @Getter@Setter
    private String id;
    @Getter@Setter
    private String label;
    @Getter@Setter
    private String parentId;
    private List<IndexTreeNode> children = new ArrayList();

    public IndexTreeNode() {
    }

    public void addChild(IndexTreeNode node) {
        this.children.add(node);
    }



    public List<IndexTreeNode> getChildren() {
        return this.children;
    }

    public void setChildren(List<IndexTreeNode> children) {
        this.children = children;
    }


}
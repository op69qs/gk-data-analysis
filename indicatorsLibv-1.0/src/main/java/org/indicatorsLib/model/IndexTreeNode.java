package org.indicatorsLib.model;
import lombok.Getter;
import lombok.Setter;
import org.inspect.model.QueTreeNode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Created by haoj on 2019/12/30.
 */
public class IndexTreeNode implements Serializable {
    @Getter@Setter
    private String id;
    @Getter@Setter
    private String key;
    @Getter@Setter
    private String label;
    @Getter@Setter
    private String value;
    @Getter@Setter
    private String title;
    @Getter@Setter
    private String parentId;
    @Getter@Setter
    private boolean disabled = false;
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
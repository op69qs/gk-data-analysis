package org.indicatorsLib.util;

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

    // element
    /**
     * 主键ID
     */
    @Getter@Setter
    private String id;

    /**
     * 描述
     */
    @Getter@Setter
    private String label;

    // ant
    /**
     * 主键ID
     */
    @Getter@Setter
    private String key;

    /**
     * 主键ID
     */
    @Getter@Setter
    private String value;

    /**
     * 描述
     */
    @Getter@Setter
    private String title;

    // 父级ID
    @Getter@Setter
    private String parentId;

    @Getter@Setter
    private boolean disabled;

    @Getter@Setter
    private List<TreeNode> children = new ArrayList();

    public TreeNode() {
    }

    public void addChild(TreeNode node) {
        this.children.add(node);
    }

} ///:~

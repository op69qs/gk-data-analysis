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
public class TreeNode implements Serializable {
    /**
     * id
     */
    @Getter@Setter
    private String id;
    @Getter@Setter
    private String name;
    @Getter@Setter
    private String key;
    @Getter@Setter
    private String title;

    /**
     * 父id
     */
    @Getter@Setter
    private String parentId;

    /**
     * 核算主体名称
     */
    @Getter@Setter
    private String bookorgcode;
    @Getter@Setter
    private String bookorgname;
    @Getter@Setter
    private String bookorg_level;
    @Getter@Setter
    private String bookorgcodepid;
    @Getter@Setter
    private String state;
    @Getter@Setter
    private String statedesc;
    @Getter@Setter
    private String guoku_id;
    @Getter@Setter
    private String guoku_dscr;
    /**
     * 是否叶子节点: 0:是 1:不是
     */
    @Getter@Setter
    private String  isleaf;
//    private boolean  isleaf;
    private List<TreeNode> children;


    public TreeNode() {
    }
    public TreeNode(TreeNode treenode) {
        this.key = treenode.getBookorgcode();
        this.id = treenode.getBookorgcode();
        this.bookorgcode = treenode.getBookorgcode();
        this.bookorgname = treenode.getBookorgname();
        this.name = treenode.getBookorgname();
        this.isleaf = treenode.getIsleaf();
        this.parentId = treenode.getBookorgcodepid();
        this.bookorgcodepid = treenode.getBookorgcodepid();
        this.statedesc = treenode.getStatedesc();
        this.state = treenode.getState();
        this.bookorg_level = treenode.getBookorg_level();
        this.title=treenode.getBookorgname();
        this.guoku_id=treenode.getGuoku_id();
        this.guoku_dscr=treenode.getGuoku_dscr();
        if (treenode.getIsleaf().equals("0")) {
            this.children = new ArrayList<TreeNode>();
        }
    }
    public List<TreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }


}

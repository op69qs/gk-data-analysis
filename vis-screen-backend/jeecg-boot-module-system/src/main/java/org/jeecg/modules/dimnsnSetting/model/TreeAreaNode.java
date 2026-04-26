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
public class TreeAreaNode implements Serializable {
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
     * 地区名称
     */
    @Getter@Setter
    private String area_no_id;
    @Getter@Setter
    private String area_dscr;
    @Getter@Setter
    private String level;
    @Getter@Setter
    private String level_dscr;
    @Getter@Setter
    private String area_no_pid;
    @Getter@Setter
    private String state;
    @Getter@Setter
    private String statedesc;
    @Getter@Setter
    private String type_id;
    @Getter@Setter
    private String path;
    @Getter@Setter
    private String type_dscr;
    @Getter@Setter
    private String sort;
    @Getter@Setter
    private String area_dscr_s;
    /**
     * 是否叶子节点: 0:是 1:不是
     */
    @Getter@Setter
    private String  isleaf;
//    private boolean  isleaf;
    private List<TreeAreaNode> children;


    public TreeAreaNode() {
    }
    public TreeAreaNode(TreeAreaNode treenode) {
        this.key = treenode.getArea_no_id();
        this.id = treenode.getArea_no_id();
        this.area_no_id = treenode.getArea_no_id();
        this.area_dscr = treenode.getArea_dscr();
        this.name = treenode.getArea_dscr();
        this.isleaf = treenode.getIsleaf();
        this.parentId = treenode.getArea_no_pid();
        this.area_no_pid = treenode.getArea_no_pid();
        this.statedesc = treenode.getStatedesc();
        this.state = treenode.getState();
        this.level = treenode.getLevel();
        this.level_dscr = treenode.getLevel_dscr();
        this.title=treenode.getArea_dscr();
        this.sort=treenode.getSort();
        this.type_id=treenode.getType_id();
        this.type_dscr=treenode.getType_dscr();
        this.path=treenode.getPath();
        this.area_dscr_s = treenode.getArea_dscr_s();
        if (treenode.getIsleaf().equals("0")) {
            this.children = new ArrayList<TreeAreaNode>();
        }
    }
    public List<TreeAreaNode> getChildren() {
        return children;
    }

    public void setChildren(List<TreeAreaNode> children) {
        this.children = children;
    }


}

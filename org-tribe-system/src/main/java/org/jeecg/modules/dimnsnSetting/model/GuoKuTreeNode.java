// TreeNode.java

package org.jeecg.modules.dimnsnSetting.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Created by dj on 2019/9/25
 */
public class GuoKuTreeNode implements Serializable {
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
     * 国库编码
     */
    @Getter@Setter
    private String guoku_id;
    @Getter@Setter
    private String guoku_dscr;
    @Getter@Setter
    private String guoku_shuxing_id;
    @Getter@Setter
    private String guoku_shuxing_dscr;
    @Getter@Setter
    private String level;
    @Getter@Setter
    private String level_dscr;
    @Getter@Setter
    private String guoku_pid;
    @Getter@Setter
    private String area_no_id;
    @Getter@Setter
    private String area_dscr;
    @Getter@Setter
    private String bookorgcode;
    @Getter@Setter
    private String bookorgname;
    @Getter@Setter
    private String pay_bnk_no;
    @Getter@Setter
    private String rcvbnk_name;
    @Getter@Setter
    private String state;
    @Getter@Setter
    private String statedesc;
    @Getter@Setter
    private String  isleaf;

    private List<GuoKuTreeNode> children;


    public GuoKuTreeNode() {
    }
    public GuoKuTreeNode(GuoKuTreeNode guokutreenode) {
        this.key = guokutreenode.getGuoku_id();
        this.id = guokutreenode.getGuoku_id();
        this.name = guokutreenode.getGuoku_dscr();
        this.parentId = guokutreenode.getGuoku_pid();
        this.guoku_id = guokutreenode.getGuoku_id();
        this.guoku_dscr = guokutreenode.getGuoku_dscr();
        this.isleaf = guokutreenode.getIsleaf();
        this.guoku_shuxing_id = guokutreenode.getGuoku_shuxing_id();
        this.guoku_shuxing_dscr = guokutreenode.getGuoku_shuxing_dscr();
        this.level = guokutreenode.getLevel();
        this.level_dscr = guokutreenode.getLevel_dscr();
        this.guoku_pid = guokutreenode.getGuoku_pid();
        this.area_no_id = guokutreenode.getArea_no_id();
        this.area_dscr = guokutreenode.getArea_dscr();
        this.bookorgcode = guokutreenode.getBookorgcode();
        this.bookorgname = guokutreenode.getBookorgname();
        this.pay_bnk_no = guokutreenode.getPay_bnk_no();
        this.rcvbnk_name = guokutreenode.getRcvbnk_name();
        this.statedesc = guokutreenode.getStatedesc();
        this.state = guokutreenode.getState();
        if (guokutreenode.getIsleaf().equals("0")) {
            this.children = new ArrayList<GuoKuTreeNode>();
        }
    }
    public List<GuoKuTreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<GuoKuTreeNode> children) {
        this.children = children;
    }


}

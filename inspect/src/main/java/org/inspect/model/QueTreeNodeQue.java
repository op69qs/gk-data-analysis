package org.inspect.model;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Created by dj on 2020/3/4.
 */
public class QueTreeNodeQue implements Serializable {
    @Getter@Setter
    private String value;
    @Getter@Setter
    private String id;
    @Getter@Setter
    private String label;
    @Getter@Setter
    private String title;
    @Getter@Setter
    private String key;
    @Getter@Setter
    private String isleaf;
    @JsonProperty("QUESTION_RECTIFICATION_PLAN")
    @Getter@Setter
    private String question_rectification_plan;
    @JsonProperty("QUESTION_TYPE")
    @Getter@Setter
    private String question_type;
//    @Getter@Setter
    private String QUESTION_DSCR;
    @JsonProperty("QUESTION_ID")
    @Getter@Setter
    private String QUESTION_ID;
    @JsonProperty("RULE_FILE_CONTENT")
    @Getter@Setter
    private String  rule_file_content;
    @JsonProperty("QUESTION_TYPE_DSCR")
    @Getter@Setter
    private String question_type_dscr;

    @Getter@Setter
    private String type;

    @Getter@Setter
    private String path;

    @JsonProperty("SORT")
    @Getter@Setter
    private String SORT;
    private List<QueTreeNodeQue> children = new ArrayList();

    public QueTreeNodeQue() {
    }
    @JsonProperty("QUESTION_DSCR")
    public String getQUESTION_DSCR() {
        return QUESTION_DSCR;
    }
    public void setQUESTION_DSCR(String QUESTION_DSCR) {
        this.QUESTION_DSCR = QUESTION_DSCR;
    }

    public void addChild(QueTreeNodeQue node) {
        this.children.add(node);
    }


    public List<QueTreeNodeQue> getChildren() {
        return this.children;
    }
    public void setChildren(List<QueTreeNodeQue> children) {
        this.children = children;
    }


}
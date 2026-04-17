// TalentPool.java

package org.inspect.model;

import lombok.Getter;
import lombok.Setter;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @author Created by Samer on 2019/9/5.
 */
@Getter
@Setter
public class TalentPool {

    @Excel(name = "姓名")
    private String NAME;

    @Excel(name = "国库")
    private String GUOKU_DSCR;

    @Excel(name = "国库编码")
    private String GUOKU_ID;

    @Excel(name = "性别")
    private String SEX_DSCR;

    @Excel(name = "出生年月")
    private String BIRTHDAY;

    @Excel(name = "年龄")
    private String AGE;

    @Excel(name = "民族")
    private String NATION_DSCR;

    @Excel(name = "政治面貌")
    private String POLITICS_DSCR;

    @Excel(name = "所学专业")
    private String MAJOR;

    @Excel(name = "学历")
    private String EDUCATION_DSCR;

    @Excel(name = "职务")
    private String DUTIES_DSCR;

    @Excel(name = "当前工作岗位")
    private String CUR_POSITION;

    @Excel(name = "电话")
    private String PHONE;

    @Excel(name = "从事国库年限")
    private String WORK_LIFE;

    private String EXPERTISE;

    @Excel(name = "专长")
    private String EXPERTISE_DSCR;

    @Excel(name = "职称")
    private String TITLE;

    @Excel(name = "是否具有执法证")
    private String LAWCERT_DSCR;

    @Excel(name = "执法证号")
    private String LAWCERT_NO;

    @Excel(name = "参加检查次数")
    private String CHECK_NO;

    @Excel(name = "是否担任过主查")
    private String CHIEF_DSCR;

    @Excel(name = "备注")
    private String MEMO;
} ///:~

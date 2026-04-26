package org.jeecg.modules.dimnsnSetting.model;

import lombok.Getter;
import lombok.Setter;
import org.jeecgframework.poi.excel.annotation.Excel;

@Getter
@Setter
public class SubjectImport {

    @Excel(name = "年度",width = 30)
    private String S_BDGSBTVSION;

    @Excel(name = "预算科目目级代码",width = 30)
    private String SUBJECT_CODE_4;

    @Excel(name = "预算科目目级名称",width = 30)
    private String SUBJECT_DSCR_4;

    @Excel(name = "预算种类",width = 30)
    private String BUDGET_TYPE;

    @Excel(name = "统计科目末级代码",width = 30)
    private String STAT_CODE_4;

    @Excel(name = "T科目分类编码3",width = 30)
    private String T_SUBJECT_CODE_3;

    @Excel(name = "T科目类别",width = 30)
    private String T_SUBJECT_TYPE;

    @Excel(name = "调拨标志",width = 30)
    private String FLITTING_FLAG;

}

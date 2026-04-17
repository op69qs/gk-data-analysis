// TalentPool.java

package org.jeecg.modules.enumSetting.model;

import lombok.Getter;
import lombok.Setter;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @author Created by Samer on 2019/9/5.
 */
@Getter@Setter
public class enumSet {

    @Excel(name = "国库")
    private String guokuDscr;

    @Excel(name = "身份证号")
    private String id;

    @Excel(name = "姓名")
    private String name;

    @Excel(name = "性别")
    private String sexDscr;

    @Excel(name = "学历")
    private String educationDscr;

    @Excel(name = "民族")
    private String nationDscr;

    @Excel(name = "出生年月")
    private String birthday;

    @Excel(name = "年龄")
    private String age;

    @Excel(name = "职务")
    private String dutiesDscr;

    @Excel(name = "职称")
    private String title;

    @Excel(name = "电话")
    private String phone;

    @Excel(name = "是否具有执法证")
    private String lawcertDscr;

    @Excel(name = "执法证号")
    private String lawcertNo;

    private String expertise;

    @Excel(name = "专长")
    private String expertiseDscr;

    @Excel(name = "备注")
    private String memo;


} ///:~

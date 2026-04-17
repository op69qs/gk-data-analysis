// TalentPool.java

package org.fixedReport.model;

import lombok.Getter;
import lombok.Setter;
import org.jeecgframework.poi.excel.annotation.Excel;


@Getter
@Setter
public class BorrowData {

    @Excel(name = "序号")
    private int borrow_index;

    @Excel(name = "资料名称")
    private String data_name;

    @Excel(name = "数量")
    private String num;

    @Excel(name = "资料日期")
    private String data_date;

    @Excel(name = "调阅资料日期")
    private String borrow_date;

    @Excel(name = "被查库负责人")
    private String inspected_charge;

    @Excel(name = "检查组组长")
    private String leader;
}

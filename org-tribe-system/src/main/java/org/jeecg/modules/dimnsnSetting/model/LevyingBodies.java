package org.jeecg.modules.dimnsnSetting.model;

import lombok.Getter;
import lombok.Setter;
import org.jeecgframework.poi.excel.annotation.Excel;

@Getter
@Setter
public class LevyingBodies {

    @Excel(name = "征收机构代码",width = 30)
    private String TAX_ORG_ID;

    @Excel(name = "征收机构名称",width = 30)
    private String TAX_ORG_DSCR;

    @Excel(name = "征收机关类型",width = 30)
    private String TYPE_ID;

    @Excel(name = "征收机关类型描述",width = 30)
    private String TYPE_DSCR;

    @Excel(name = "核算主体代码",width = 30)
    private String BOOK_ORG_CODE;

    @Excel(name = "国库代码",width = 30)
    private String TRECODE;

    @Excel(name = "状态",width = 30)
    private String STATE;

    @Excel(name = "状态描述",width = 30)
    private String STATE_DSCR;

}

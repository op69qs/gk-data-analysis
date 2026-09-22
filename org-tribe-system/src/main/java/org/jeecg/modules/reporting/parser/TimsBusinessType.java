package org.jeecg.modules.reporting.parser;

public enum TimsBusinessType {
    INCOME("1", "收入"),
    PAYOUT("2", "支出"),
    STOCK("3", "库存"),
    /** 快报_收入数据 → stg.trs_tmis_budget_income_provinces（与指标库收入列布局不同） */
    FLASH_INCOME("4", "快报收入");

    private final String code;
    private final String description;

    TimsBusinessType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() { return code; }
    public String getDescription() { return description; }

    public static TimsBusinessType fromCode(String code) {
        for (TimsBusinessType value : values()) {
            if (value.code.equals(code)) return value;
        }
        throw new IllegalArgumentException("不支持的 TIMS 业务类型：" + code);
    }
}

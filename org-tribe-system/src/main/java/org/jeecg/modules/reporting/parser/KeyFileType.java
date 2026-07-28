package org.jeecg.modules.reporting.parser;

import java.util.Optional;

/**
 * KEY 压缩包内四类文本文件。文件名识别规则与原 JAR 保持一致。
 */
public enum KeyFileType {
    INCOME("sr", 8),
    PAYOUT("zc", 8),
    STOCK("kc", 6),
    BACK("tk", 9);

    private final String fileCode;
    private final int fieldCount;

    KeyFileType(String fileCode, int fieldCount) {
        this.fileCode = fileCode;
        this.fieldCount = fieldCount;
    }

    public String getFileCode() {
        return fileCode;
    }

    public int getFieldCount() {
        return fieldCount;
    }

    public static Optional<KeyFileType> detect(String fileName) {
        if (fileName == null || !fileName.endsWith(".txt")) {
            return Optional.empty();
        }
        for (KeyFileType type : values()) {
            if (fileName.contains(type.fileCode)) {
                return Optional.of(type);
            }
        }
        return Optional.empty();
    }
}

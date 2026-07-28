package org.jeecg.modules.reporting.service;

import org.jeecg.modules.reporting.mapper.KeyReportMapper;
import org.jeecg.modules.reporting.parser.KeyFileParseError;
import org.jeecg.modules.reporting.parser.KeyFileParseResult;
import org.jeecg.modules.reporting.parser.KeyFileParser;
import org.jeecg.modules.reporting.parser.KeyFileType;
import org.jeecg.modules.reporting.parser.KeyReportRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class KeyReportProcessingService {
    private final KeyReportMapper mapper;
    private final KeyFileParser parser;

    @Autowired
    public KeyReportProcessingService(KeyReportMapper mapper) {
        this(mapper, new KeyFileParser());
    }

    KeyReportProcessingService(KeyReportMapper mapper, KeyFileParser parser) {
        this.mapper = mapper;
        this.parser = parser;
    }

    /**
     * 递归处理解压目录。每一种被识别的文件类型在同一事务内按 ZIP 名先删后插。
     */
    @Transactional(rollbackFor = Exception.class)
    public KeyReportProcessingResult process(Path extractRoot, String keyZipName) throws IOException {
        return process(extractRoot, keyZipName, null);
    }

    @Transactional(rollbackFor = Exception.class)
    public KeyReportProcessingResult process(Path extractRoot, String keyZipName,
                                             String expectedTreasuryCode) throws IOException {
        Map<KeyFileType, List<Path>> filesByType = findTypedFiles(extractRoot);
        Map<String, KeyReportProcessingResult.TypeResult> typeResults = new LinkedHashMap<>();
        List<KeyFileParseError> allErrors = new ArrayList<>();

        for (KeyFileType type : KeyFileType.values()) {
            List<Path> files = filesByType.get(type);
            List<KeyReportRecord> rows = new ArrayList<>();
            int errorsBefore = allErrors.size();
            if (files != null) {
                for (Path file : files) {
                    KeyFileParseResult result = parser.parse(file, type, keyZipName);
                    rows.addAll(result.getRecords());
                    allErrors.addAll(result.getErrors());
                }
                if (expectedTreasuryCode != null) {
                    java.util.Iterator<KeyReportRecord> iterator = rows.iterator();
                    while (iterator.hasNext()) {
                        KeyReportRecord row = iterator.next();
                        if (!expectedTreasuryCode.equals(row.getTreCode())) {
                            allErrors.add(new KeyFileParseError(files.get(0).getFileName().toString(), 0, null,
                                    "行内国库代码与 KEY 文件名不一致"));
                            iterator.remove();
                        }
                    }
                }
                if (rows.isEmpty() && allErrors.size() == errorsBefore) {
                    allErrors.add(new KeyFileParseError(files.get(0).getFileName().toString(), 0, null,
                            "KEY 文件中没有有效数据行"));
                }
                if (!rows.isEmpty()) replace(type, keyZipName, rows);
            }
            typeResults.put(type.getFileCode(), new KeyReportProcessingResult.TypeResult(
                    files == null ? 0 : files.size(), rows.size(), allErrors.size() - errorsBefore));
        }
        if (filesByType.isEmpty()) {
            allErrors.add(new KeyFileParseError("<ZIP>", 0, null,
                    "压缩包中没有可识别的 sr/zc/kc/tk KEY 文件"));
        }
        return new KeyReportProcessingResult(typeResults, allErrors);
    }

    private Map<KeyFileType, List<Path>> findTypedFiles(Path extractRoot) throws IOException {
        Map<KeyFileType, List<Path>> grouped = new EnumMap<>(KeyFileType.class);
        try (Stream<Path> paths = Files.walk(extractRoot)) {
            List<Path> files = paths.filter(Files::isRegularFile)
                    .sorted(Comparator.comparing(Path::toString))
                    .collect(Collectors.toList());
            for (Path file : files) {
                KeyFileType.detect(file.getFileName().toString())
                        .ifPresent(type -> grouped.computeIfAbsent(type, ignored -> new ArrayList<>()).add(file));
            }
        }
        return grouped;
    }

    private void replace(KeyFileType type, String keyZipName, List<KeyReportRecord> rows) {
        switch (type) {
            case INCOME:
                mapper.deleteIncomeByZipName(keyZipName);
                if (!rows.isEmpty()) mapper.insertIncome(rows);
                break;
            case PAYOUT:
                mapper.deletePayoutByZipName(keyZipName);
                if (!rows.isEmpty()) mapper.insertPayout(rows);
                break;
            case STOCK:
                mapper.deleteStockByZipName(keyZipName);
                if (!rows.isEmpty()) mapper.insertStock(rows);
                break;
            case BACK:
                mapper.deleteBackByZipName(keyZipName);
                if (!rows.isEmpty()) mapper.insertBack(rows);
                break;
            default:
                throw new IllegalArgumentException("不支持的 KEY 文件类型：" + type);
        }
    }
}

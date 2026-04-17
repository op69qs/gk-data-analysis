package org.dockingProjects.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author HaoJiang.
 * @Ddate 2020/8/4 17:35
 */
@Slf4j
@Component
public class FileUtil {

    /**
     * 校验文件路径是否存在
     *
     * @param url
     */
    private static void checkFilePath(String url) {
        if (!Files.exists(Paths.get(url))) {
            try {
                Files.createDirectories(Paths.get(url));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 向磁盘写入文件
     */
    public void write(String fileSavePath, String fileName, String str) {
        checkFilePath(fileSavePath);
        try (BufferedWriter fileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileSavePath + fileName, false), "UTF-8"));) {
            log.info("正在生成文件......");
            fileWriter.write(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取文件
     *
     * @return
     */
    public List<String> read(String fileSavePath) {
        List<String> list = new ArrayList<>();
        try {
            Path path = Paths.get(fileSavePath);
            if (Files.exists(path)) {
                Files.lines(path).forEach(line -> {
                    list.add(line);
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 删除文件
     *
     * @return
     */
    public List<String> delete(String fileSavePath) {
        List<String> list = new ArrayList<>();
        try {
            Path path = Paths.get(fileSavePath);
            Files.deleteIfExists(path);
            log.info("文件【" + path.getFileName() + "】已删除....");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}

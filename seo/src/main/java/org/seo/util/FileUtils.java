package org.seo.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import lombok.NonNull;

public class FileUtils {
    private static final Logger log = LoggerFactory.getLogger(FileUtils.class);
    private static Boolean packagesJar;

    public FileUtils() {
    }

    @Value("${package.isJar}")
    public void setPackagesJar(Boolean packagesJar) {
        packagesJar = packagesJar;
    }

    public static void uploadFile(byte[] file, String filePath, String fileName) throws Exception {
        File targetFile = new File(filePath);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }

        if (!filePath.endsWith("/") && !filePath.endsWith("\\")) {
            filePath = filePath + "\\";
        }

        String fileFullName = filePath + fileName;
        log.info("fileFullName : " + fileFullName);
        FileOutputStream out = new FileOutputStream(fileFullName);

        try {
            out.write(file);
            out.flush();
        } finally {
            if (Collections.singletonList(out).get(0) != null) {
                out.close();
            }

        }

    }

    public static void uploadExcelFile(HSSFWorkbook hssfWorkbook, String filePath, String fileName) throws Exception {
        File targetFile = new File(filePath);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }

        if (!filePath.endsWith("/") && !filePath.endsWith("\\")) {
            filePath = filePath + "\\";
        }

        String fileFullName = filePath + fileName;
        log.info("fileFullName : " + fileFullName);
        FileOutputStream out = new FileOutputStream(fileFullName);

        try {
            hssfWorkbook.write(out);
        } finally {
            if (Collections.singletonList(out).get(0) != null) {
                out.close();
            }

        }

    }

    public static String encodeDownloadFilename(String filename, String agent) throws IOException {
        if (agent.contains("Firefox")) {
            filename = "=?UTF-8?B?" + Base64.getEncoder().encodeToString(filename.getBytes("utf-8")) + "?=";
        } else {
            filename = URLEncoder.encode(filename, "utf-8");
            filename = filename.replace("+", " ");
        }

        return filename;
    }

    public static void toZip(String srcDir, OutputStream out, boolean KeepDirStructure) throws Exception {
        ZipOutputStream zos = null;

        try {
            zos = new ZipOutputStream(out);
            File sourceFile = new File(srcDir);
            compress(sourceFile, zos, sourceFile.getName(), KeepDirStructure);
        } catch (Exception var8) {
            log.error("zip error from ZipUtils", var8);
        } finally {
            if (Collections.singletonList(zos).get(0) != null) {
                zos.close();
            }

        }

    }

    public static void compress(File sourceFile, ZipOutputStream zos, String name, boolean KeepDirStructure) throws Exception {
        byte[] buf = new byte[1024];
        if (sourceFile.isFile()) {
            zos.putNextEntry(new ZipEntry(name));
            FileInputStream in = new FileInputStream(sourceFile);

            int len;
            try {
                while((len = in.read(buf)) != -1) {
                    zos.write(buf, 0, len);
                }
            } finally {
                if (Collections.singletonList(in).get(0) != null) {
                    in.close();
                }

            }
        } else {
            File[] listFiles = sourceFile.listFiles();
            if (listFiles != null && listFiles.length != 0) {
                File[] var13 = listFiles;
                int var7 = listFiles.length;

                for(int var8 = 0; var8 < var7; ++var8) {
                    File file = var13[var8];
                    if (KeepDirStructure) {
                        compress(file, zos, name + "/" + file.getName(), KeepDirStructure);
                    } else {
                        compress(file, zos, file.getName(), KeepDirStructure);
                    }
                }
            } else if (KeepDirStructure) {
                zos.putNextEntry(new ZipEntry(name + "/"));
                zos.closeEntry();
            }
        }

    }

    public static void outputFile(HttpServletResponse response, File file) {
        try {
            int BUFFER_SIZE = 4096;
            response.setContentType("application/octet-stream");
            response.setContentLength((int)file.length());
            response.setHeader("Accept-Ranges", "bytes");
            int readLength = 0;
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(file), BUFFER_SIZE);

            try {
                BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());

                try {
                    byte[] buffer = new byte[BUFFER_SIZE];

                    while((readLength = in.read(buffer)) > 0) {
                        byte[] bytes = new byte[readLength];
                        System.arraycopy(buffer, 0, bytes, 0, readLength);
                        out.write(bytes);
                    }

                    out.flush();
                } finally {
                    if (Collections.singletonList(out).get(0) != null) {
                        out.close();
                    }

                }
            } finally {
                if (Collections.singletonList(in).get(0) != null) {
                    in.close();
                }

            }
        } catch (Exception var18) {
            log.error("", var18);
        }

    }

    public static String getPathFromBasePathFile() throws Exception {
        String bashPath;
        if (packagesJar.booleanValue()) {
            bashPath = (new File("")).getCanonicalPath() + "/";
        } else {
            bashPath = FileUtils.class.getResource("/").getPath();
        }

        return bashPath;
    }

    public static List<String> traverseFolder(@NonNull String path) {
        if (path == null) {
            throw new NullPointerException("path");
        } else {
            log.trace("check folder: [{}]", path);
            List<String> fileList = new ArrayList();
            File file = new File(path);
            if (!file.exists()) {
                log.info("folder: [{}] is not exists", path);
                return fileList;
            } else {
                File[] files = file.listFiles();
                if (null != files && files.length != 0) {
                    File[] var4 = files;
                    int var5 = files.length;

                    for(int var6 = 0; var6 < var5; ++var6) {
                        File file2 = var4[var6];
                        if (file2.isDirectory()) {
                            log.trace("folder:" + file2.getAbsolutePath());
                            List<String> fileListTmp = traverseFolder(file2.getAbsolutePath());
                            fileList.addAll(fileListTmp);
                        } else if (!file2.getName().startsWith(".")) {
                            log.trace("file:" + file2.getAbsolutePath());
                            fileList.add(file2.getAbsolutePath());
                        }
                    }

                    return fileList;
                } else {
                    log.trace("folder is empty");
                    return fileList;
                }
            }
        }
    }

    public static void deleteDir(@NonNull File dir) throws Exception {
        if (dir == null) {
            throw new NullPointerException("dir");
        } else {
            log.info("dir: [{}]", dir);
            if (dir.isDirectory()) {
                File[] files = dir.listFiles();
                File[] var2 = files;
                int var3 = files.length;

                for(int var4 = 0; var4 < var3; ++var4) {
                    File file = var2[var4];
                    if (file.isFile()) {
                        log.debug("delete file: [{}]", file);
                        file.delete();
                    } else {
                        deleteDir(file);
                    }
                }
            }

            dir.delete();
        }
    }

    public static void copyDir2Dir(@NonNull String sourceDir, @NonNull String targetDir, boolean merge) throws Exception {
        if (sourceDir == null) {
            throw new NullPointerException("sourceDir");
        } else if (targetDir == null) {
            throw new NullPointerException("targetDir");
        } else {
            if (!sourceDir.endsWith(File.separator)) {
                sourceDir = sourceDir + File.separator;
            }

            if (!targetDir.endsWith(File.separator)) {
                targetDir = targetDir + File.separator;
            }

            log.info("sourceDir: [{}], targetDir: [{}]", sourceDir, targetDir);
            File filePath = new File(targetDir);
            if (filePath.exists() && !merge) {
                deleteDir(filePath);
            }

            filePath.mkdirs();
            List<String> sourceFiles = traverseFolder(sourceDir);
            Iterator var5 = sourceFiles.iterator();

            while(var5.hasNext()) {
                String sourceFile = (String)var5.next();
                int index = sourceFile.lastIndexOf(File.separator);
                String sourceFileName = sourceFile.substring(index + 1);
                String targetFile = targetDir + sourceFileName;
                log.debug("sourceFile: [{}], targetFile: [{}]", sourceFile, targetFile);
                BufferedInputStream fileInputStream = new BufferedInputStream(new FileInputStream(sourceFile));

                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(targetFile);

                    try {
                        IOUtils.copy(fileInputStream, fileOutputStream);
                    } finally {
                        if (Collections.singletonList(fileOutputStream).get(0) != null) {
                            fileOutputStream.close();
                        }

                    }
                } finally {
                    if (Collections.singletonList(fileInputStream).get(0) != null) {
                        fileInputStream.close();
                    }

                }
            }

        }
    }
}

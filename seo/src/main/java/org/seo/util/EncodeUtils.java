package org.seo.util;

import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.BitSet;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class EncodeUtils {
    private static final Logger log = LoggerFactory.getLogger(EncodeUtils.class);
    private static int BYTE_SIZE = 8;
    public static String CODE_UTF8 = "UTF-8";
    public static String CODE_UTF8_BOM = "UTF-8_BOM";
    public static String CODE_GBK = "GBK";

    public EncodeUtils() {
    }

    public static String getEncode(String fullFileName, boolean ignoreBom) throws Exception {
        log.debug("fullFileName: [{}]", fullFileName);
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fullFileName));
        return getEncode(bis, ignoreBom);
    }

    public static String getEncode4String(@NonNull String text, boolean ignoreBom) throws Exception {
        if (text == null) {
            throw new NullPointerException("text");
        } else {
            InputStream is = new ByteArrayInputStream(text.getBytes());
            return getEncode(new BufferedInputStream(is), ignoreBom);
        }
    }

    public static String getEncode(@NonNull BufferedInputStream bis, boolean ignoreBom) throws Exception {
        if (bis == null) {
            throw new NullPointerException("bis");
        } else {
            bis.mark(0);
            String encodeType = "";
            byte[] head = new byte[3];
            bis.read(head);
            if (head[0] == -1 && head[1] == -2) {
                encodeType = "UTF-16";
            } else if (head[0] == -2 && head[1] == -1) {
                encodeType = "Unicode";
            } else if (head[0] == -17 && head[1] == -69 && head[2] == -65) {
                if (ignoreBom) {
                    encodeType = CODE_UTF8;
                } else {
                    encodeType = CODE_UTF8_BOM;
                }
            } else if ("Unicode".equals(encodeType)) {
                encodeType = "UTF-16";
            } else if (isUTF8(bis)) {
                encodeType = CODE_UTF8;
            } else {
                encodeType = CODE_GBK;
            }

            log.debug("result encode type: [{}]", encodeType);
            return encodeType;
        }
    }

    private static boolean isUTF8(@NonNull BufferedInputStream bis) throws Exception {
        if (bis == null) {
            throw new NullPointerException("bis");
        } else {
            bis.reset();
            int code = bis.read();

            do {
                BitSet bitSet = convert2BitSet(code);
                if (bitSet.get(0) && !checkMultiByte(bis, bitSet)) {
                    return false;
                }

                code = bis.read();
            } while(code != -1);

            return true;
        }
    }

    private static boolean checkMultiByte(@NonNull BufferedInputStream bis, @NonNull BitSet bitSet) throws Exception {
        if (bis == null) {
            throw new NullPointerException("bis");
        } else if (bitSet == null) {
            throw new NullPointerException("bitSet");
        } else {
            int count = getCountOfSequential(bitSet);
            byte[] bytes = new byte[count - 1];
            bis.read(bytes);
            byte[] var4 = bytes;
            int var5 = bytes.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                byte b = var4[var6];
                if (!checkUtf8Byte(b)) {
                    return false;
                }
            }

            return true;
        }
    }

    private static boolean checkUtf8Byte(byte b) throws Exception {
        BitSet bitSet = convert2BitSet(b);
        return bitSet.get(0) && !bitSet.get(1);
    }

    private static int getCountOfSequential(@NonNull BitSet bitSet) {
        if (bitSet == null) {
            throw new NullPointerException("bitSet");
        } else {
            int count = 0;

            for(int i = 0; i < BYTE_SIZE && bitSet.get(i); ++i) {
                ++count;
            }

            return count;
        }
    }

    private static BitSet convert2BitSet(int code) {
        BitSet bitSet = new BitSet(BYTE_SIZE);

        for(int i = 0; i < BYTE_SIZE; ++i) {
            int tmp3 = code >> BYTE_SIZE - i - 1;
            int tmp2 = 1 & tmp3;
            if (tmp2 == 1) {
                bitSet.set(i);
            }
        }

        return bitSet;
    }

    public static void convert(String oldFullFileName, String newFullFileName, String newCharsetName) throws Exception {
        String oldCharsetName = getEncode(oldFullFileName, true);
        convert(oldFullFileName, oldCharsetName, newFullFileName, newCharsetName);
    }

    public static void convert(String oldFullFileName, String oldCharsetName, String newFullFileName, String newCharsetName) throws Exception {
        oldCharsetName = oldCharsetName.toUpperCase();
        newCharsetName = newCharsetName.toUpperCase();
        log.debug("the old file name is: [{}], The oldCharsetName is: [{}]", oldFullFileName, oldCharsetName);
        log.debug("the new file name is: [{}], The newCharsetName is: [{}]", newFullFileName, newCharsetName);
        StringBuffer content = new StringBuffer();
        BufferedReader bin = new BufferedReader(new InputStreamReader(new FileInputStream(oldFullFileName), oldCharsetName));

        try {
            String line;
            while((line = bin.readLine()) != null) {
                content.append(line);
                content.append(System.getProperty("line.separator"));
            }

            newFullFileName = newFullFileName.replace("\\", "/");
            File dir = new File(newFullFileName.substring(0, newFullFileName.lastIndexOf("/")));
            if (!dir.exists()) {
                dir.mkdirs();
            }

            OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(newFullFileName), newCharsetName);

            try {
                out.write(content.toString());
            } finally {
                if (Collections.singletonList(out).get(0) != null) {
                    out.close();
                }

            }
        } finally {
            if (Collections.singletonList(bin).get(0) != null) {
                bin.close();
            }

        }

    }

    public static void convertAll(String rootPath, String newCharsetName) throws Exception {
        String targetRootPath = rootPath + "_target";
        log.info("convert to default target root path: [{}]", targetRootPath);
        convertAll(rootPath, targetRootPath, newCharsetName);
    }

    public static void convertAll(String rootPath, String targetRootPath, String newCharsetName) throws Exception {
        List<String> fileList = FileUtils.traverseFolder(rootPath);
        Iterator var4 = fileList.iterator();

        while(var4.hasNext()) {
            String filePath = (String)var4.next();
            String newFullFileName = filePath.replaceAll(rootPath, targetRootPath);
            convert(filePath, newFullFileName, newCharsetName);
        }

    }
}

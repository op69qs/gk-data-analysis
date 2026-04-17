package org.triber.analysis.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
  * 磁盘工具类 *  * @author gu * @date  2017年12月22日
  */
public class Usage {

    private static DecimalFormat DECIMALFORMAT = new DecimalFormat("#.##");

    /**
     *  获取磁盘使用信息
     *  @return
     */
    public static List<Map<String, String>> getWinDeskUsage() {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        File[] roots = File.listRoots();
        // 获取磁盘分区列表
        for (File file : roots) {
            Map<String, String> map = new HashMap<String, String>();
            long freeSpace=file.getFreeSpace();
            long totalSpace=file.getTotalSpace();
            long usableSpace=totalSpace-freeSpace;
            map.put("path", file.getPath());
            map.put("unUsed", freeSpace / 1024 / 1024 / 1024 + "G");
            // 空闲空间
            map.put("used", usableSpace / 1024 / 1024 / 1024 + "G");
            // 可用空间
            map.put("total",totalSpace / 1024 / 1024 / 1024 + "G");
            // 总空间
            map.put("use_rate", DECIMALFORMAT.format(((double)usableSpace/(double)totalSpace)*100)+"%");
            // 总空间
            list.add(map);
        }
        return list;
    }

    public static Desk getLinuxDeskUsage( String mountDir ) {
        Desk desk = new Desk();
        try {
            Runtime rt = Runtime.getRuntime();
            Process p = rt.exec("df -hl " + mountDir);// df -hl 查看硬盘空间
            BufferedReader in = null;
            try {
                in = new BufferedReader(new InputStreamReader(
                        p.getInputStream()));
                String str = null;
                String[] strArray = null;
                int line = 0;
                while ((str = in.readLine()) != null) {
                    line++;
                    if (line != 2) {
                        continue;
                    }
                    int m = 0;
                    strArray = str.split(" ");
                    System.out.println("strArray: " + strArray);
                    for (String para : strArray) {
                        if (para.trim().length() == 0)
                            continue;
                        ++m;
                        if (para.endsWith("G") || para.endsWith("M") || para.endsWith("K") || para.endsWith("B") ) {
                            // 目前的服务器
                            if (m == 2) {
                                desk.setTotal(para);
                            }
                            if (m == 3) {
                                desk.setUsed(para);
                            }
                            if (m == 4) {
                                desk.setunUsed(para);
                            }
                        }
                        if (para.endsWith("%")) {
                            if (m == 5) {
                                desk.setUse_rate(para);
                            }
                        }
                        System.out.println("desk: " + desk);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                in.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
       return desk;
    }

    public static class Desk {
        private String total;
        private String used;
        private String unUsed;
        private String use_rate;

        public String toString(){
            return "总磁盘空间："+total+"，已使用："+used+"，使用率达："+use_rate;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getUsed() {
            return used;
        }

        public void setUsed(String used) {
            this.used = used;
        }

        public String getunUsed() {
            return unUsed;
        }

        public void setunUsed(String unUsed) {
            this.unUsed = unUsed;
        }

        public String getUse_rate() {
            return use_rate;
        }

        public void setUse_rate(String use_rate) {
            this.use_rate = use_rate;
        }

    }

    public static void main(String[] args) {
        System.out.println(getWinDeskUsage());
        System.out.println(getLinuxDeskUsage(""));
    }
}

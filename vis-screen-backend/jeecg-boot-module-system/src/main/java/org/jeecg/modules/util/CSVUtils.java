package org.jeecg.modules.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVUtils {
    /**
     *
     * 描述：导出
     * @author
     * @created
     * @since
     * @param file csv文件(路径+文件名)，csv文件不存在会自动创建
     * @param dataList 数据（data1,data2,data3...）
     * @return
     */
     public static boolean exportCsv(File file, List<String> dataList){
         FileOutputStream out= null;
         OutputStreamWriter osw = null;
         BufferedWriter bfw= null;
         try {
             out = new FileOutputStream(file);
             osw = new OutputStreamWriter(out, "gbk");
             bfw = new BufferedWriter(osw);
             if(dataList != null && !dataList.isEmpty()){
                 for(String data : dataList){
                     bfw.append(data).append("\r");
                 }
             }
             return true;
         } catch (Exception e) {
             e.printStackTrace();
             return false;
         }finally{
             try {
                 out.close();
                 osw.close();
                 bfw.close();
             } catch (IOException e) {
                 e.printStackTrace();
             }
         }
     }

    /**
     *
     * 描述：导入
     * @author
     * @created
     * @since
     * @param file csv文件(路径+文件名)
     * @return
     */
    public static List<String> importCsv(File file){
        List<String> dataList = new ArrayList<String>();
        BufferedReader br = null;
        try {
            DataInputStream in = new DataInputStream(new FileInputStream(file));
            br = new BufferedReader((new InputStreamReader(in,"GBK")));
            String line = "";
            while ((line = br.readLine()) != null) {
                dataList.add(line);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return dataList;
    }
}

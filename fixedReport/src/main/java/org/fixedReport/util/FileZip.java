package org.fixedReport.util;
import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**java压缩成zip
 * @author zc
 * 创建时间：2015年1月14日
 */
public class FileZip {

	/**
	 * @param inputFileName 你要压缩的文件夹(整个完整路径)
	 * @param zipFileName 压缩后的文件(整个完整路径)
	 * @throws Exception
	 */
	public static Boolean zip(String inputFileName, String zipFileName) throws Exception {
		zip(zipFileName + ".zip", new File(inputFileName));
		return true;
	}

	private static void zip(String zipFileName, File inputFile) throws Exception {
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
		BufferedOutputStream bo = new BufferedOutputStream(out);
		zip(out, inputFile, "",bo);
		bo.close();
		out.flush();
		out.close();
	}

	private static void zip(ZipOutputStream out, File f, String base,BufferedOutputStream bo) throws Exception {
		if (f.isDirectory()) {
			File[] fl = f.listFiles();
			out.putNextEntry(new ZipEntry(base + "/"));
			base = base.length() == 0 ? "" : base + "/";
			for (int i = 0; i < fl.length; i++) {
				zip(out, fl[i], base + fl[i].getName(),bo);
			}
		} else {
			out.putNextEntry(new ZipEntry(base));
			FileInputStream in = new FileInputStream(f);
			byte[] buffer = new byte[1024 * 5];
			BufferedInputStream bi = new BufferedInputStream(in);
			int b;
			//System.out.println(base);
			while ((b = bi.read(buffer)) != -1) {
				bo.write(b);
			}
			bi.close();
			in.close();
		}
	}
	
	 public static void main(String [] temp){       
		 try {           
			 zip("D:\\home\\app\\pbc-file\\transfer\\2019_01_PP3ER","D:\\home\\app\\pbc-file\\transfer\\2019_01_PP3ER");//你要压缩的文件夹      和  压缩后的文件
			 }catch (Exception ex) {       
				 ex.printStackTrace();    
			 }   
		}
}



//=====================文件压缩=========================
/*//把文件压缩成zip
File zipFile = new File("E:/demo.zip");
//定义输入文件流
InputStream input = new FileInputStream(file);
//定义压缩输出流	
ZipOutputStream zipOut = null;
//实例化压缩输出流,并制定压缩文件的输出路径  就是E盘下,名字叫 demo.zip
zipOut = new ZipOutputStream(new FileOutputStream(zipFile));
zipOut.putNextEntry(new ZipEntry(file.getName()));
//设置注释
zipOut.setComment("www.demo.com");
int temp = 0;
while((temp = input.read()) != -1) {
	zipOut.write(temp);	
}		
input.close();
zipOut.close();*/
//==============================================

package org.indicatorsLib.util;

import org.apache.commons.io.FileUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 上传文件
 * zc
 * 创建时间：2014年12月23日
 * @version
 */
public class FileUpload {

	/**
	 * @param file 			//文件对象
	 * @param filePath		//上传路径
	 * @param fileName		//文件名
	 * @return  文件名
	 */
	public static String fileUp(MultipartFile file, String filePath, String fileName){
		try {
			copyFile(file.getInputStream(), filePath, fileName).replaceAll("-", "");
		} catch (IOException e) {
			System.out.println(e);
		}
		return fileName;
	}
	
	/**
	 * 写文件到当前目录的upload目录中
	 * 
	 * @param in
	 * @param realName
	 * @throws IOException
	 */
	private static String copyFile(InputStream in, String dir, String realName)
			throws IOException {
		File file = new File(dir, realName);
		if (!file.exists()) {
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			file.createNewFile();
		}
		FileUtils.copyInputStreamToFile(in, file);
		return realName;
	}

	/**
	 * 文件输出
	 * @param data
	 * @param pathFileName
	 */
	public static void writeToFile(XWPFDocument data, String pathFileName){
		if(null != data){
			try {
				File file = new File(pathFileName);//文件路径（路径+文件名）
				if (!file.exists()) {   //文件不存在则创建文件，先创建目录
					File dir = new File(file.getParent());
					dir.mkdirs();
					file.createNewFile();
				}
				FileOutputStream outStream = new FileOutputStream(file); //文件输出流将数据写入文件
				data.write(outStream);
				outStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
			}
		}
	}

}

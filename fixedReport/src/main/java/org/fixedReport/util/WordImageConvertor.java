package org.fixedReport.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Base64;

import javax.imageio.ImageIO;

/**
 * @Description:WORD 文档图片转换器
 */
public class WordImageConvertor {

    public static String toDocBodyBlock(
            String base64,
            String srcLocationShortName,
            String shapeidPrex,
            String spidPrex,
            int vv,
            int isWandH,
            int MAX_HEIGHT_SIZE,
            int MAX_WIDTH_SIZE,
            int QTR_MAX_HEIGHT_SIZE,
            int QTR_MAX_WIDTH_SIZE
    ) throws IOException {
        //shapeid
        String shapeId = shapeidPrex;
        shapeId += UuidUtil.get32UUID();

        //spid ,同shapeid处理
        String spid = spidPrex;
        spid += UuidUtil.get32UUID();

        StringBuilder sb1 = new StringBuilder();
        sb1.append("<!--[if gte vml 1]>");
        sb1.append("<v:shape id=3D\"" + shapeId + "\"");
        sb1.append("\n");
        sb1.append(" o:spid=3D\"" + spid + "\"");
        sb1.append("  style=3D'" + getBase64ImgHeightWidth(base64,vv,isWandH,MAX_HEIGHT_SIZE,MAX_WIDTH_SIZE,QTR_MAX_HEIGHT_SIZE,QTR_MAX_WIDTH_SIZE) + "visibility:visible;mso-wrap-style:square'>");
        sb1.append("\n");
        sb1.append(" <v:imagedata src=3D\"" + srcLocationShortName + "\" o:title=3D\"\"/>");
        sb1.append("</v:shape>");
        sb1.append("<![endif]-->");

        return sb1.toString();
    }

    /**
     * @param @param  nextPartId
     * @param @param  contextLoacation
     * @param @param  ContentType
     * @param @param  base64Content
     * @param @return
     * @return String
     * @throws
     * @Description: 生成图片的base4块
     */
    public static String generateImageBase64Block(
            String nextPartId,
            String contextLocation,
            String fileTypeName,
            String base64Content
    ) {

        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append("\n");
        sb.append("------=_NextPart_" + nextPartId);
        sb.append("\n");
        sb.append("Content-Location: " + contextLocation);
        sb.append("\n");
        sb.append("Content-Transfer-Encoding: base64");
        sb.append("\n");
        sb.append("Content-Type: " + getImageContentType(fileTypeName));
        sb.append("\n");
        sb.append("\n");
        sb.append(base64Content);

        return sb.toString();
    }

    /**
     * 处理BASE64图片格式
     *
     * @param fileTypeName
     * @return
     */
    private static String getImageContentType(String fileTypeName) {
        String result = "image/jpeg";
        if (fileTypeName.equals("tif") || fileTypeName.equals("tiff")) {
            result = "image/tiff";
        } else if (fileTypeName.equals("fax")) {
            result = "image/fax";
        } else if (fileTypeName.equals("gif")) {
            result = "image/gif";
        } else if (fileTypeName.equals("ico")) {
            result = "image/x-icon";
        } else if (fileTypeName.equals("jfif") || fileTypeName.equals("jpe")
                || fileTypeName.equals("jpeg") || fileTypeName.equals("jpg")) {
            result = "image/jpeg";
        } else if (fileTypeName.equals("net")) {
            result = "image/pnetvue";
        } else if (fileTypeName.equals("png") || fileTypeName.equals("bmp")) {
            result = "image/png";
        } else if (fileTypeName.equals("rp")) {
            result = "image/vnd.rn-realpix";
        } else if (fileTypeName.equals("rp")) {
            result = "image/vnd.rn-realpix";
        }
        return result;
    }

    /**
     * 获取BASE64图形编码图形的高度及宽度
     *
     * @param base64
     * @return
     * @throws IOException
     */
    private static String getBase64ImgHeightWidth(String base64,int vv,int isWandH,int MAX_HEIGHT_SIZE,int MAX_WIDTH_SIZE,int QTR_MAX_HEIGHT_SIZE,int QTR_MAX_WIDTH_SIZE) throws IOException {

        StringBuilder sb = new StringBuilder();
        int heightSize = Integer.valueOf(MAX_HEIGHT_SIZE);
        int MaxWidthSize = Integer.valueOf(MAX_WIDTH_SIZE);
        int qtrHeightSize = Integer.valueOf(QTR_MAX_HEIGHT_SIZE);
        int qtrMaxWidthSize = Integer.valueOf(QTR_MAX_WIDTH_SIZE);


        byte[] base64Bytes = Base64.getDecoder().decode(base64);
        BufferedImage sourceImg;
        sourceImg = ImageIO.read(new ByteArrayInputStream(base64Bytes));
        int height=0;
        int width=0;
//        int vvv=vv;
        if(isWandH==1){
            if(vv==1){
                height = 220;
                width = 550;
            }else {
                height = qtrHeightSize;
                width = qtrMaxWidthSize;
            }
        }else if(isWandH==2){
            if(vv==2){
                height = 220;
                width = 550;
            }else {
                height = qtrHeightSize;
                width = qtrMaxWidthSize;
            }
        }else {
            height = qtrHeightSize;
            width = qtrMaxWidthSize;
        }
        //将像素转化成pt
        BigDecimal heightValue = new BigDecimal(height * 12 / 16);
        heightValue = heightValue.setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal widthValue = new BigDecimal(width * 12 / 16);
        widthValue = widthValue.setScale(2, BigDecimal.ROUND_HALF_UP);

        sb.append("height:" + heightValue + "pt;");
        sb.append("width:" + widthValue + "pt;");

        return sb.toString();
    }

}

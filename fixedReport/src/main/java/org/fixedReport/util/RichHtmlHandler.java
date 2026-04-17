package org.fixedReport.util;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import java.io.IOException;

/**
 * @Description:富文本Html处理器，主要处理图片及编码
 */
public class RichHtmlHandler {

    private String DEFAULT_BASE64 = "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";

    private Document doc = null;
    private String html;

    private String docSrcParent = "";
    private String docSrcLocationPrex = "";
    private String nextPartId;
    private String shapeidPrex;
    private String spidPrex;
    private String typeid;

    private String handledDocBodyBlock;
    private StringBuilder docBase64BlockResults = new StringBuilder();
    private StringBuilder xmlImgRefs = new StringBuilder();

    public String getDocSrcLocationPrex() {
        return docSrcLocationPrex;
    }

    public void setDocSrcLocationPrex(String docSrcLocationPrex) {
        this.docSrcLocationPrex = docSrcLocationPrex;
    }

    public String getNextPartId() {
        return nextPartId;
    }

    public void setNextPartId(String nextPartId) {
        this.nextPartId = nextPartId;
    }

    public String getHandledDocBodyBlock() {
        String raw = WordHtmlGeneratorHelper.string2Ascii(doc.getElementsByTag("body").html());
        return raw.replace("=3D", "=").replace("=", "=3D");
    }

    public String getRawHandledDocBodyBlock() {
        String raw = doc.getElementsByTag("body").html();
        return raw.replace("=3D", "=").replace("=", "=3D");
    }

    public StringBuilder getDocBase64BlockResults() {
        return docBase64BlockResults;
    }

    public StringBuilder getXmlImgRefs() {
        return xmlImgRefs;
    }

    public String getShapeidPrex() {
        return shapeidPrex;
    }

    public void setShapeidPrex(String shapeidPrex) {
        this.shapeidPrex = shapeidPrex;
    }

    public String getSpidPrex() {
        return spidPrex;
    }

    public void setSpidPrex(String spidPrex) {
        this.spidPrex = spidPrex;
    }

    public String getTypeid() {
        return typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }

    public String getDocSrcParent() {
        return docSrcParent;
    }

    public void setDocSrcParent(String docSrcParent) {
        this.docSrcParent = docSrcParent;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public RichHtmlHandler(String html) {
        doc = Jsoup.parse(wrappHtml(html));
    }

    public void re_init(String html) {
        doc = null;
        doc = Jsoup.parse(wrappHtml(html));
        docBase64BlockResults = new StringBuilder();
        xmlImgRefs = new StringBuilder();
    }

    /**
     * @param @return
     * @return String
     * @throws IOException
     * @Description: 获得已经处理过的HTML文件
     */
    public void handledHtml(boolean isWebApplication,int isWandH,int MAX_HEIGHT_SIZE,int MAX_WIDTH_SIZE,int QTR_MAX_HEIGHT_SIZE,int QTR_MAX_WIDTH_SIZE) throws IOException {

        Elements imags = doc.getElementsByTag("img");

int vv=0;

       /* Elements b_texts = doc.getElementsByTag("b");

        if( b_texts != null && b_texts.size() > 0 ){
            for( Element b_text : b_texts ){
                String value = b_text.text();
                value = StringEscapeUtils.unescapeJava(value);
                b_text.parent().append(value);
                b_text.remove();
            }
        }*/

        if (imags == null || imags.size() == 0) {
            return;
        }

        // 转换成word mht 能识别图片标签内容，去替换html中的图片标签
        for (Element item : imags) {
            // base64编码
            String src = item.attr("src").replace("data:image/png;base64,", "");

            if (isWebApplication) {
                String contentPath = getRequest().getContextPath();
                if (!StringUtils.isEmpty(contentPath)) {
                    if (src.startsWith(contentPath)) {
                        src = src.substring(contentPath.length());
                    }
                }

            }

            String docFileName = "image" + UuidUtil.get32UUID() + ".png";
            String srcLocationShortName = docSrcParent + "/" + docFileName;

            // 得到文件的word mht的body块
            if (src == null || "".equals(src) || src.endsWith("jpg") || src.endsWith("png") || src.endsWith("JPG") || src.endsWith("PNG")) {
                src = DEFAULT_BASE64;
            }
            String handledDocBodyBlock = WordImageConvertor.toDocBodyBlock(src, srcLocationShortName, shapeidPrex, spidPrex,vv,isWandH,MAX_HEIGHT_SIZE,MAX_WIDTH_SIZE,QTR_MAX_HEIGHT_SIZE,QTR_MAX_WIDTH_SIZE);
            vv=vv+1;
            item.parent().append(handledDocBodyBlock);
            item.remove();
            // 去替换原生的html中的imag
            String contextLocation = docSrcLocationPrex + "/" + docSrcParent + "/" + docFileName;
            String docBase64BlockResult = WordImageConvertor
                    .generateImageBase64Block(nextPartId, contextLocation,
                            "png", src);

            docBase64BlockResults.append(docBase64BlockResult);
            String imageXMLHref = "<o:File HRef=3D\"" + docFileName + "\"/>";
            xmlImgRefs.append(imageXMLHref);
        }
    }

    private String wrappHtml(String html) {
        // 因为传递过来都是不完整的doc
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append("<body>");
        sb.append(html);
        sb.append("</body>");
        sb.append("</html>");
        return sb.toString();
    }

    /**
     * 得到request对象
     *
     * @return
     */
    public HttpServletRequest getRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request;
    }

}

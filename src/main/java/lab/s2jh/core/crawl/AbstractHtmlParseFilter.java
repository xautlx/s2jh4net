package lab.s2jh.core.crawl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import lab.s2jh.core.util.Digests;
import lab.s2jh.module.crawl.service.CrawlService;
import lab.s2jh.module.crawl.vo.WebPage;
import lab.s2jh.support.service.DynamicConfigService;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract1;
import net.sourceforge.tess4j.util.ImageHelper;
import net.sourceforge.tess4j.util.LoadLibs;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.html.dom.HTMLDocumentImpl;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.xml.utils.DOMBuilder;
import org.cyberneko.html.parsers.DOMFragmentParser;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.DOMException;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.google.common.collect.Lists;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sun.org.apache.xpath.internal.XPathAPI;

/**
 * 
 * @author EMAIL:s2jh-dev@hotmail.com , QQ:2414521719
 *
 */
public abstract class AbstractHtmlParseFilter implements CrawlParseFilter {

    protected static final Logger logger = LoggerFactory.getLogger("crawl.parse");

    private Pattern filterPattern;

    private static String imgSaveRootDir;

    protected CrawlService crawlService;

    protected DynamicConfigService dynamicConfigService;

    /**
     * 基于xpath获取Node列表
     * @param node
     * @param xpath
     * @return
     */
    protected NodeList selectNodeList(Node contextNode, String xpath) {
        try {
            if (contextNode != null && StringUtils.isNotBlank(xpath)) {
                xpath = convertXPath(xpath);
                return XPathAPI.selectNodeList(contextNode, xpath);
            }
        } catch (TransformerException e) {
            logger.warn("Bad 'xpath' expression [{}]", xpath);
        }
        return null;
    }

    /**
     * 基于xpath获取Node节点
     * @param node
     * @param xpath
     * @return
     */
    protected Node selectSingleNode(Node contextNode, String xpath) {
        try {
            if (contextNode != null && StringUtils.isNotBlank(xpath)) {
                xpath = convertXPath(xpath);
                return XPathAPI.selectSingleNode(contextNode, xpath);
            }
        } catch (TransformerException e) {
            logger.warn("Bad 'xpath' expression [{}]", xpath);
        }
        return null;
    }

    /**
     * 基于xpath定义的img元素解析返回完整路径格式的URL字符串
     * @param url 页面URL，有些img的src元素为相对路径，通过此url合并组装图片完整URL路径
     * @param contextNode
     * @param xpaths 多个xpath字符串，主要用于容错处理，有些页面格式不统一，可能一会所需图片在xpath1，有些在xpath2，给定多个可能的xpath列表，按顺序循环找到一个匹配就终止循环
     * @return http开头的完整路径图片URL
     */
    protected String getImgSrcValue(String url, Node contextNode, String... xpaths) {
        for (String xpath : xpaths) {
            Node node = selectSingleNode(contextNode, xpath);
            String imgUrl = null;
            if (node != null) {
                NamedNodeMap atrributes = node.getAttributes();
                Node attr = atrributes.getNamedItem("data-ks-lazyload");
                if (attr == null) {
                    attr = atrributes.getNamedItem("lazy-src");
                }
                if (attr == null) {
                    attr = atrributes.getNamedItem("src");
                }
                if (attr != null) {
                    imgUrl = attr.getTextContent();
                }
            }
            if (StringUtils.isNotBlank(imgUrl)) {
                return parseImgSrc(url, imgUrl);
            }
        }
        return "";
    }

    private String convertXPath(String xpath) {
        String[] paths = xpath.split("/");
        List<String> convertedPaths = Lists.newArrayList();
        for (String path : paths) {
            if ("text()".equalsIgnoreCase(path)) {
                convertedPaths.add(path.toLowerCase());
            } else if (path.indexOf("[") > -1) {
                String[] splits = StringUtils.split(path, "[");
                convertedPaths.add(splits[0].toUpperCase() + "[" + splits[1]);
            } else {
                convertedPaths.add(path.toUpperCase());
            }
        }

        String convertedPath = StringUtils.join(convertedPaths, "/");
        logger.trace("Converted XPath is: {}", convertedPath);
        return convertedPath;
    }

    /**
     * 基于xpath定位返回text内容，如果未找到元素返回null
     * @param contextNode
     * @param xpath
     * @return
     */
    protected String getXPathValue(Node contextNode, String xpath) {
        return getXPathValue(contextNode, xpath, null);
    }

    /**
     * 基于xpath定位返回text内容，如果未找到元素则返回默认defaultVal
     * @param contextNode
     * @param xpath
     * @param defaultVal
     * @return
     */
    protected String getXPathValue(Node contextNode, String xpath, String defaultVal) {
        NodeList nodes = selectNodeList(contextNode, xpath);
        if (nodes == null || nodes.getLength() <= 0) {
            return defaultVal;
        }
        String txt = "";
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node instanceof Text) {
                txt += node.getNodeValue();
            } else {
                txt += node.getTextContent();
            }
        }
        return cleanInvisibleChar(txt);
    }

    /**
     * 基于xpath返回对应的html格式内容
     * @param contextNode
     * @param xpath
     * @return
     */
    protected String getXPathHtml(Node contextNode, String xpath) {
        Node node = selectSingleNode(contextNode, xpath);
        return asString(node);
    }

    /**
     * 基于xpath定位对应attr属性内容，如果未找到元素返回null
     * @param contextNode
     * @param xpath
     * @return
     */
    protected String getXPathAttribute(Node contextNode, String xpath, String attr) {
        Node node = selectSingleNode(contextNode, xpath);
        if (node != null) {
            NamedNodeMap atrributes = node.getAttributes();
            Node attrNode = atrributes.getNamedItem(attr);
            if (attrNode != null) {
                String text = attrNode.getTextContent();
                if (text != null) {
                    return text.trim();
                }
            }
        }
        return null;
    }

    /**
     * 基于xpath定位对应attr属性内容，如果未找到元素返回null
     * @param contextNode
     * @param xpath
     * @return
     */
    protected String getNodeAttribute(Node node, String attr) {
        if (node != null) {
            NamedNodeMap atrributes = node.getAttributes();
            Node attrNode = atrributes.getNamedItem(attr);
            if (attrNode != null) {
                return attrNode.getTextContent();
            }
        }
        return null;
    }

    /**
     * 基于xpath定位对应attr属性内容，如果未找到元素返回null
     * @param contextNode
     * @param xpath
     * @return
     */
    protected String getNodeText(Node node) {
        if (node != null) {
            String txt = "";
            if (node instanceof Text) {
                txt += node.getNodeValue();
            } else {
                txt += node.getTextContent();
            }
            return cleanInvisibleChar(txt);
        }
        return null;
    }

    protected String asString(Node node) {
        if (node == null) {
            return "";
        }
        try {
            StringWriter writer = new StringWriter();
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(new DOMSource(node), new StreamResult(writer));
            String xml = writer.toString();
            xml = StringUtils.substringAfter(xml, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            xml = xml.trim();
            return xml;
        } catch (Exception e) {
            throw new IllegalArgumentException("error for parse node to string.", e);
        }
    }

    /**
     * 处理不同src图片属性格式，返回统一格式的http格式的图片URL
     * @param url
     * @param imgSrc
     * @return
     */
    private String parseImgSrc(String url, String imgSrc) {
        if (StringUtils.isBlank(imgSrc)) {
            return "";
        }
        imgSrc = imgSrc.trim();
        //去掉链接最后的#号
        imgSrc = StringUtils.substringBefore(imgSrc, "#");
        if (imgSrc.startsWith("http")) {
            return imgSrc;
        } else if (imgSrc.startsWith("/")) {
            if (url.indexOf(".com") > -1) {
                return StringUtils.substringBefore(url, ".com/") + ".com" + imgSrc;
            } else if (url.indexOf(".net") > -1) {
                return StringUtils.substringBefore(url, ".net/") + ".net" + imgSrc;
            } else {
                throw new RuntimeException("Undefined site domain suffix");
            }
        } else {
            return StringUtils.substringBeforeLast(url, "/") + "/" + imgSrc;
        }
    }

    /**
     * 清除无关的不可见空白字符
     * @param str
     * @return
     */
    protected String cleanInvisibleChar(String str) {
        return cleanInvisibleChar(str, false);
    }

    /**
     * 清除无关的不可见空白字符
     * @param str
     * @param includingBlank 是否包括移除文本内部的空白字符
     * @return
     */
    protected String cleanInvisibleChar(String str, boolean includingBlank) {
        if (str != null) {
            str = StringUtils.remove(str, (char) 160);
            if (includingBlank) {
                //普通空格
                str = StringUtils.remove(str, " ");
                //全角空格
                str = StringUtils.remove(str, (char) 12288);
            }
            str = StringUtils.remove(str, "\r");
            str = StringUtils.remove(str, "\n");
            str = StringUtils.remove(str, "\t");
            str = StringUtils.remove(str, "\\s*");
            str = StringUtils.remove(str, "◆");
            str = StringUtils.remove(str, "�");
            str = str.trim();
        }
        return str;
    }

    /**
     * 清除无关的Node节点元素
     * @param str
     * @return
     */
    protected void cleanUnusedNodes(Node doc) {
        cleanUnusedNodes(doc, "//STYLE");
        cleanUnusedNodes(doc, "//MAP");
        cleanUnusedNodes(doc, "//SCRIPT");
        cleanUnusedNodes(doc, "//script");
    }

    /**
     * 清除无关的Node节点元素
     * @param str
     * @return
     */
    protected void cleanUnusedNodes(Node node, String xpath) {
        try {
            NodeList nodes = XPathAPI.selectNodeList(node, xpath);
            for (int i = 0; i < nodes.getLength(); i++) {
                Element element = (Element) nodes.item(i);
                element.getParentNode().removeChild(element);
            }
        } catch (DOMException e) {
            throw new IllegalStateException(e);
        } catch (TransformerException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * 用于自定义过滤器时强制设置
     * @param filterPattern
     */
    public void setFilterPattern(Pattern filterPattern) {
        this.filterPattern = filterPattern;
    }

    /**
     * 判断url是否符合自定义解析匹配规则
     * @param url
     * @return
     */
    private boolean isUrlMatchedForParse(String url) {
        if (filterPattern == null) {
            String regex = getUrlFilterRegex();
            if (StringUtils.isBlank(regex)) {
                return false;
            }
            filterPattern = Pattern.compile(regex);
        }
        if (filterPattern.matcher(url).find()) {
            return true;
        }
        return false;
    }

    @Override
    public DBObject filter(String url, WebPage webPage) throws Exception {

        //URL匹配
        if (!isUrlMatchedForParse(url)) {
            logger.trace("Skipped {} as not match regex [{}]", this.getClass().getName(), getUrlFilterRegex());
            return null;
        }

        logger.info("Invoking parse  {} for url: {}", this.getClass().getName(), url);

        if (StringUtils.isBlank(webPage.getPageText())) {
            logger.warn("Skipped as no fetch data found for url: {}", url);
            return null;
        }

        String bizSiteName = webPage.getBizSiteName();
        if (StringUtils.isBlank(bizSiteName)) {
            bizSiteName = getSiteName(url);
        }
        String bizId = webPage.getBizId();
        if (StringUtils.isBlank(bizId)) {
            bizId = getPrimaryKey(url);
        }

        DBObject update = new BasicDBObject();
        update = filterInternal(url, webPage, update);

        if (update == null) {
            logger.trace("Skipped as no data parsed for url: {}", url);
            return null;
        }

        crawlService.saveParseDBObject(url, bizSiteName, bizId, update);
        return update;
    }

    protected DocumentFragment parse(String input) {
        return parse(new InputSource(new StringReader(input)), null);
    }

    /**
     * 目前已知类似：http://www.jumeiglobal.com/deal/ht150312p1286156t1.html
     * 所需采集数据在textarea元素下面，htmlunit在printXml时会对textarea内容进行escape处理，导致得到的doc对象无法直接XPath定位
     * 因此需要先提前textarea元素内容转换为单独的DocumentFragment对象，然后基于此文档对象进行数据解析
     * @see org.apache.nutch.parse.html.HtmlParser#parse
     * @param input
     * @return 
     * @throws Exception
     */
    protected DocumentFragment parse(InputSource input, String parserImpl) {
        try {
            if ("tagsoup".equalsIgnoreCase(parserImpl))
                return parseTagSoup(input);
            else
                return parseNeko(input);
        } catch (Exception e) {
            logger.warn("Parsing error: " + e.getMessage());
            logger.trace(e.getMessage(), e);
        }
        return null;
    }

    /**
     * @see org.apache.nutch.parse.html.HtmlParser#parseTagSoup
     */
    private DocumentFragment parseTagSoup(InputSource input) throws Exception {
        HTMLDocumentImpl doc = new HTMLDocumentImpl();
        DocumentFragment frag = doc.createDocumentFragment();
        DOMBuilder builder = new DOMBuilder(doc, frag);
        org.ccil.cowan.tagsoup.Parser reader = new org.ccil.cowan.tagsoup.Parser();
        reader.setContentHandler(builder);
        reader.setFeature(org.ccil.cowan.tagsoup.Parser.ignoreBogonsFeature, true);
        reader.setFeature(org.ccil.cowan.tagsoup.Parser.bogonsEmptyFeature, false);
        reader.setProperty("http://xml.org/sax/properties/lexical-handler", builder);
        reader.parse(input);
        return frag;
    }

    /**
     * @see org.apache.nutch.parse.html.HtmlParser#parseNeko
     */
    private DocumentFragment parseNeko(InputSource input) throws Exception {
        DOMFragmentParser parser = new DOMFragmentParser();
        try {
            parser.setFeature("http://cyberneko.org/html/features/scanner/allow-selfclosing-iframe", true);
            parser.setFeature("http://cyberneko.org/html/features/augmentations", true);
            parser.setProperty("http://cyberneko.org/html/properties/default-encoding", "UTF-8");
            parser.setFeature("http://cyberneko.org/html/features/scanner/ignore-specified-charset", true);
            parser.setFeature("http://cyberneko.org/html/features/balance-tags/ignore-outside-content", false);
            parser.setFeature("http://cyberneko.org/html/features/balance-tags/document-fragment", true);
            parser.setFeature("http://cyberneko.org/html/features/report-errors", logger.isTraceEnabled());
        } catch (SAXException e) {
        }
        // convert Document to DocumentFragment
        HTMLDocumentImpl doc = new HTMLDocumentImpl();
        doc.setErrorChecking(false);
        DocumentFragment res = doc.createDocumentFragment();
        DocumentFragment frag = doc.createDocumentFragment();
        parser.parse(input, frag);
        res.appendChild(frag);

        try {
            while (true) {
                frag = doc.createDocumentFragment();
                parser.parse(input, frag);
                if (!frag.hasChildNodes())
                    break;
                if (logger.isInfoEnabled()) {
                    logger.info(" - new frag, " + frag.getChildNodes().getLength() + " nodes.");
                }
                res.appendChild(frag);
            }
        } catch (Exception x) {
            logger.error("Failed with the following Exception: ", x);
        }
        return res;
    }

    private static ThreadLocal<ITesseract> tesseractInstanceThreadLocal;

    private static File tessDataFolder;

    private synchronized ITesseract buildTesseractInstance() {
        if (tesseractInstanceThreadLocal == null) {
            tesseractInstanceThreadLocal = new ThreadLocal<ITesseract>();

            tessDataFolder = LoadLibs.extractTessResources("tessdata"); // Maven build only; only English data bundled
            try {
                URL tessResourceUrl = AbstractHtmlParseFilter.class.getResource("/tesseract/chi_sim.traineddata");
                FileUtils.copyFileToDirectory(new File(tessResourceUrl.getPath()), tessDataFolder);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }

        ITesseract tesseractInstance = tesseractInstanceThreadLocal.get();
        if (tesseractInstance == null) {
            //ImageIO.scanForPlugins(); // for server environment
            //ITesseract instance = new Tesseract(); // JNA Interface Mapping
            tesseractInstance = new Tesseract1(); // JNA Direct Mapping

            tesseractInstance.setDatapath(tessDataFolder.getAbsolutePath());
            tesseractInstance.setLanguage("eng");
            tesseractInstanceThreadLocal.set(tesseractInstance);
        }
        return tesseractInstance;
    }

    private static String ocrImageDir;
    private static ITesseract tesseractInstance;

    private synchronized ITesseract buildTesseractSingleInstance() {
        if (tesseractInstance == null) {
            tessDataFolder = LoadLibs.extractTessResources("tessdata"); // Maven build only; only English data bundled
            try {
                URL tessResourceUrl = AbstractHtmlParseFilter.class.getResource("/tesseract/chi_sim.traineddata");
                FileUtils.copyFileToDirectory(new File(tessResourceUrl.getPath()), tessDataFolder);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }

            //ImageIO.scanForPlugins(); // for server environment
            //ITesseract instance = new Tesseract(); // JNA Interface Mapping
            tesseractInstance = new Tesseract1(); // JNA Direct Mapping

            tesseractInstance.setDatapath(tessDataFolder.getAbsolutePath());
            tesseractInstance.setLanguage("eng");

            File dir = new File(FileUtils.getTempDirectoryPath(), "crawl/ocr/");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            ocrImageDir = dir.getAbsolutePath();
        }
        return tesseractInstance;
    }

    public synchronized String processOCR(String url, String src, boolean chinese) {
        String result = null;
        File imageFile = null;

        logger.info("Processing OCR image for URL: {} src: {}", url, src);
        //数据初始化
        ITesseract tesseractInstance = buildTesseractSingleInstance();
        CloseableHttpResponse httpGetResponse = null;
        try {
            String imageFormat = null;
            String fileId = Digests.md5(src);
            File cachedFile = new File(ocrImageDir, fileId);
            if (cachedFile.exists()) {
                ImageInputStream iis = ImageIO.createImageInputStream(cachedFile);
                // Find all image readers that recognize the image format
                Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
                if (iter.hasNext()) {
                    // Use the first reader
                    ImageReader reader = iter.next();
                    imageFormat = reader.getFormatName();
                }
                // Close stream
                iis.close();
                logger.info("Using cached OCR image: {}, format: {}", cachedFile.getAbsolutePath(), imageFormat);
            } else {
                HttpGet httpGet = new HttpGet(src);
                logger.info("Fetching OCR image URL: {} src: {}", url, src);
                httpGetResponse = CrawlService.buildHttpClient().execute(httpGet);
                int statusCode = httpGetResponse.getStatusLine().getStatusCode();
                if (statusCode == HttpStatus.SC_OK) {
                    HttpEntity httpGetResponseEntity = httpGetResponse.getEntity();
                    ContentType contentType = ContentType.get(httpGetResponseEntity);
                    imageFormat = StringUtils.substringAfterLast(contentType.getMimeType(), "/");
                    logger.info("Save cached OCR image: {}, format: {}", cachedFile.getAbsolutePath(), imageFormat);
                    FileUtils.copyInputStreamToFile(httpGetResponseEntity.getContent(), cachedFile);
                } else {
                    logger.warn("HTTP ERROR StatusCode is {} URL: {} src: {}", statusCode, url, src);
                    return null;
                }
            }

            if (imageFormat != null) {
                BufferedImage inImg = ImageIO.read(cachedFile);
                // 图片锐化,自己使用中影响识别率的主要因素是针式打印机字迹不连贯,所以锐化反而降低识别率
                // textImage = ImageHelper.convertImageToBinary(textImage);
                // 图片放大5倍,增强识别率(很多图片本身无法识别,放大5倍时就可以轻易识,但是考滤到客户电脑配置低,针式打印机打印不连贯的问题,这里就放大5倍)
                BufferedImage newImage = ImageHelper.getScaledInstance(inImg, inImg.getWidth() * 5, inImg.getHeight() * 5);

                imageFile = new File(ocrImageDir, fileId + ".temp." + imageFormat);
                ImageIO.write(newImage, imageFormat, imageFile);

                if (chinese) {
                    tesseractInstance.setLanguage("chi_sim");
                    result = tesseractInstance.doOCR(imageFile);
                } else {
                    tesseractInstance.setLanguage("eng");
                    result = tesseractInstance.doOCR(imageFile);
                }
                if (result != null) {
                    result = result.trim();
                }
                logger.debug("OCR result: {} for URL: {} src: {}", result, url, src);
                return result;
            }
        } catch (Exception e) {
            logger.warn("Tesseract OCR Exception, URL: " + url + ", SRC: " + src, e);
        } finally {
            IOUtils.closeQuietly(httpGetResponse);
            if (imageFile != null) {
                imageFile.delete();
            }
        }
        return null;
    }

    protected String saveImage(String url, String src, DBObject parsedDBObject) {
        if (imgSaveRootDir == null) {

            if (dynamicConfigService != null) {
                imgSaveRootDir = dynamicConfigService.getString("cfg_crawl_image_save_root_dir");
            }

            if (StringUtils.isBlank(imgSaveRootDir)) {
                String OS = System.getProperty("os.name").toLowerCase();
                if (OS.indexOf("windows") > -1) {
                    imgSaveRootDir = "c:\\crawl\\images\\";
                } else {
                    imgSaveRootDir = "/crawl/images/";
                }
            }
        }

        CloseableHttpResponse httpGetResponse = null;
        try {
            HttpGet httpGet = new HttpGet(src);
            httpGetResponse = CrawlService.buildHttpClient().execute(httpGet);
            int statusCode = httpGetResponse.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                HttpEntity httpGetResponseEntity = httpGetResponse.getEntity();
                ContentType contentType = ContentType.get(httpGetResponseEntity);
                String format = StringUtils.substringAfterLast(contentType.getMimeType(), "/");
                DateTime now = new DateTime();
                String imagePath = now.getYear() + "/" + now.getMonthOfYear() + "/" + now.getDayOfMonth() + "/" + Digests.md5(src) + "." + format;
                logger.info("Save image file URL: {}, src: {}, path: {}", url, src, imagePath);
                FileUtils.copyInputStreamToFile(httpGetResponseEntity.getContent(), new File(imgSaveRootDir, imagePath));
                return imagePath;
            } else {
                logger.warn("HTTP ERROR StatusCode is {} URL: {} src: {}", statusCode, url, src);
            }
        } catch (Exception e) {
            logger.warn("Fetch Image Exception, URL: " + url + ", SRC: " + src, e);
        } finally {
            IOUtils.closeQuietly(httpGetResponse);
        }
        return null;
    }

    @Override
    public void setCrawlService(CrawlService crawlService) {
        this.crawlService = crawlService;
    }

    /**
     * 设置当前解析过滤器匹配的URL正则表达式
     * 只有匹配的url才调用当前解析处理逻辑
     * @return
     */
    public abstract String getUrlFilterRegex();

    /**
     * 设置当前解析数据更新的主键，有可能多个Filter合并更新同一对象
     * @return
     */
    protected String getPrimaryKey(String url) {
        return url;
    }

    /**
     * 返回页面所属站点名称
     * @return
     */
    protected String getSiteName(String url) {
        return StringUtils.substringBefore(StringUtils.substringAfter(url, "://"), "/");
    }

    /**
     * 子类实现具体的页面数据解析逻辑
     * @return
     */
    public abstract DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) throws Exception;

    protected void putKeyValue(DBObject parsedDBObject, String key, Object value) {
        putKeyValue(parsedDBObject, key, value, false);
    }

    protected void putKeyValue(DBObject parsedDBObject, String key, Object value, boolean forceOverwrite) {
        if (StringUtils.isBlank(key)) {
            return;
        }
        key = key.trim();
        if (value == null) {
            if (forceOverwrite) {
                parsedDBObject.put(key, value);
            }
            return;
        }

        if (value instanceof String) {
            String str = ObjectUtils.toString(value);
            str = str.trim();
            if (StringUtils.isBlank(str) || "-".equals(str)) {
                if (!forceOverwrite) {
                    return;
                }
            }
            parsedDBObject.put(key, str);
        }

        parsedDBObject.put(key, value);
    }

    /**
     * 通过页面内容或DOM节点判断如果可能是页面获取失败需要尝试重新获取解析，则调用此接口把当前页面重新注入
     * @param webPage 当前页面对象
     * @param message 原因说明文本
     */
    protected void injectParseFailureRetry(WebPage webPage, String message) {
        crawlService.injectParseFailureRetry(webPage, message);
    }
}

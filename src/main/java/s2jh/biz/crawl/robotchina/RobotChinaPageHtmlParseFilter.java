package s2jh.biz.crawl.robotchina;

import lab.s2jh.module.crawl.vo.WebPage;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.mongodb.DBObject;

public class RobotChinaPageHtmlParseFilter extends RobotChinaBaseHtmlParseFilter {

    @Override
    public String getUrlFilterRegex() {
        return "^http://www.robot-china.com/company/search-htm-page-\\d+-vip-0.html$";
    }

    @Override
    public DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) throws Exception {
        String pageText = webPage.getPageText();
        DocumentFragment doc = parse(pageText);

        //获取列表信息
        NodeList liNodeList = selectNodeList(doc, "//DIV[@class='xiececoyy']/div[1]/div[@class='list']//li/a");
        if (liNodeList != null && liNodeList.getLength() > 0) {
            for (int i = 0; i < liNodeList.getLength(); i++) {
                Node liNode = liNodeList.item(i);
                String outlink = getXPathAttribute(liNode, "./", "href");
                webPage.addOutlink(outlink);
            }
        }

        //下一页
        Node node = selectSingleNode(doc, "//DIV[@class='pages']/a[@class='next']");
        if (node != null) {
            String suffix = getXPathAttribute(node, "./", "href");
            if (!"/company/search-htm-page-1-vip-0.html".equals(suffix)) {
                String href = StringUtils.substringBefore(url, "/company/") + getXPathAttribute(node, "./", "href");
                webPage.addOutlink(href);
            }
        }
        return null;
    }
}

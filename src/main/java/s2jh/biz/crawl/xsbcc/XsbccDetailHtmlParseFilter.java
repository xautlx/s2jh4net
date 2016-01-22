package s2jh.biz.crawl.xsbcc;

import lab.s2jh.core.crawl.AbstractHtmlParseFilter;
import lab.s2jh.module.crawl.vo.WebPage;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;

import com.mongodb.DBObject;

public class XsbccDetailHtmlParseFilter extends AbstractHtmlParseFilter {

    @Override
    public DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) {
        String pageText = webPage.getPageText();
        DocumentFragment doc = parse(pageText);

        Node node = selectSingleNode(doc, "//DIV[@class='rcon']");
        if (node != null) {
            String content = node.getTextContent();
            putKeyValue(parsedDBObject, "公司名称", StringUtils.substringBetween(content, "公司全称：", "英文名称："));
            putKeyValue(parsedDBObject, "英文全称", StringUtils.substringBetween(content, "英文名称：", "注册地址："));
            putKeyValue(parsedDBObject, "注册地址", StringUtils.substringBetween(content, "注册地址：", "法人代表："));
            putKeyValue(parsedDBObject, "法人代表", StringUtils.substringBetween(content, "法人代表：", "公司董秘："));
            putKeyValue(parsedDBObject, "公司董秘", StringUtils.substringBetween(content, "公司董秘：", "注册资本(万元)："));
            putKeyValue(parsedDBObject, "注册资本(万元)", StringUtils.substringBetween(content, "注册资本(万元)：", "行业分类："));
            putKeyValue(parsedDBObject, "行业分类", StringUtils.substringBetween(content, "行业分类：", "挂牌日期："));
            putKeyValue(parsedDBObject, "挂牌日期", StringUtils.substringBetween(content, "挂牌日期：", "公司网址："));
            putKeyValue(parsedDBObject, "公司网址", StringUtils.substringBetween(content, "公司网址：", "转让方式："));
            putKeyValue(parsedDBObject, "转让方式", StringUtils.substringBetween(content, "转让方式：", "主办券商："));
            putKeyValue(parsedDBObject, "主办券商", StringUtils.substringBetween(content, "主办券商：", "\n\n"));
            return parsedDBObject;
        } else {
            return null;

        }

    }

    @Override
    public String getUrlFilterRegex() {
        return "^http://www.xsbcc.com/company/\\d+.html$";
    }
}

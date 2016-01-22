package s2jh.biz.crawl.xsbcc;

import lab.s2jh.core.crawl.AbstractHtmlParseFilter;
import lab.s2jh.module.crawl.vo.WebPage;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class XsbccPageHtmlParseFilter extends AbstractHtmlParseFilter {

    @Override
    public DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) {
        String pageText = webPage.getPageText();

        String rowsText = StringUtils.substringAfter(pageText, "<tr><td>");
        if (StringUtils.isBlank(rowsText)) {
            injectParseFailureRetry(webPage, "未解析到有效数据,尝试再次抓取解析");
            return null;
        }

        String pageData = "<html><body><table><tr><td>" + rowsText + "</table></body></html>";
        DocumentFragment df = parse(pageData);
        NodeList nodeList = selectNodeList(df, "//TR");
        if (nodeList != null && nodeList.getLength() > 0) {
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                Node codeNode = selectSingleNode(node, "./td[2]/a");
                String code = codeNode.getTextContent().trim();
                DBObject outlinkParsedDBObject = new BasicDBObject();
                putKeyValue(outlinkParsedDBObject, "证券代码", code);
                putKeyValue(outlinkParsedDBObject, "证券简称", getXPathValue(node, "./td[3]"));
                putKeyValue(outlinkParsedDBObject, "转让方式", getXPathValue(node, "./td[4]"));
                putKeyValue(outlinkParsedDBObject, "前收盘价（元/股）", getXPathValue(node, "./td[5]"));
                putKeyValue(outlinkParsedDBObject, "最近成交价（元/股）", getXPathValue(node, "./td[6]"));
                putKeyValue(outlinkParsedDBObject, "成交金额(万元)", getXPathValue(node, "./td[7]"));
                putKeyValue(outlinkParsedDBObject, "成交量(万股)", getXPathValue(node, "./td[8]"));
                putKeyValue(outlinkParsedDBObject, "涨跌", getXPathValue(node, "./td[9]"));
                putKeyValue(outlinkParsedDBObject, "涨跌幅", getXPathValue(node, "./td[10]"));
                putKeyValue(outlinkParsedDBObject, "市盈率", getXPathValue(node, "./td[11]"));
                putKeyValue(outlinkParsedDBObject, "挂牌时间", getXPathValue(node, "./td[12]"));
                putKeyValue(outlinkParsedDBObject, "行业", getXPathValue(node, "./td[13]"));
                putKeyValue(outlinkParsedDBObject, "地区", getXPathValue(node, "./td[14]"));
                putKeyValue(outlinkParsedDBObject, "券商", getXPathValue(node, "./td[15]"));

                webPage.addOutlink(getNodeAttribute(codeNode, "href"), null, (String) outlinkParsedDBObject.get("证券简称"), getSiteName(url),
                        (String) outlinkParsedDBObject.get("证券代码"), outlinkParsedDBObject);

                //主要指标数据：http://stockpage.10jqka.com.cn/basic/834334/main.txt
                webPage.addOutlink("http://stockpage.10jqka.com.cn/basic/" + code + "/main.txt", null, (String) outlinkParsedDBObject.get("证券简称"),
                        getSiteName(url), (String) outlinkParsedDBObject.get("证券代码"), outlinkParsedDBObject);

                //资产负债表：http://stockpage.10jqka.com.cn/basic/834334/debt.txt
                webPage.addOutlink("http://stockpage.10jqka.com.cn/basic/" + code + "/debt.txt", null, (String) outlinkParsedDBObject.get("证券简称"),
                        getSiteName(url), (String) outlinkParsedDBObject.get("证券代码"), outlinkParsedDBObject);

                //股东股本：http://stockpage.10jqka.com.cn/834334/holder/
                webPage.addOutlink("http://stockpage.10jqka.com.cn/" + code + "/holder/", null, (String) outlinkParsedDBObject.get("证券简称"),
                        getSiteName(url), (String) outlinkParsedDBObject.get("证券代码"), outlinkParsedDBObject);

                //三板信息
                webPage.addOutlink("http://www.sanban18.com/stock/" + code + "/profile.html", null, (String) outlinkParsedDBObject.get("证券简称"),
                        getSiteName(url), (String) outlinkParsedDBObject.get("证券代码"), outlinkParsedDBObject);
            }

            //注入下一页
            String pager = StringUtils.substringBetween(url, "pn=", "&");
            if (StringUtils.isBlank(pager)) {
                pager = "1";
            }
            int nextPage = Integer.valueOf(pager) + 1;
            String outlinkURL = "http://www.xsbcc.com/common/company.htm?r=0.1807033922486062&pn=" + nextPage
                    + "&ru=&uid=&hy=&addr=&quan=&tm1=&tm2=&se=&tp=0&tz=&gao=&phoneemail=13124705728&od=txtno%20desc";
            webPage.addOutlink(outlinkURL, true);
        }

        return null;
    }

    @Override
    public String getUrlFilterRegex() {
        return "^http://www.xsbcc.com/common/company.htm\\?r=0.1807033922486062&pn=[0-9]+&ru=&uid=&hy=&addr=&quan=&tm1=&tm2=&se=&tp=0&tz=&gao=&phoneemail=13124705728&od=txtno%20desc$";
    }
}

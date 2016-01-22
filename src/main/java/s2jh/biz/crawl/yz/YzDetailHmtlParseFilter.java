package s2jh.biz.crawl.yz;

import lab.s2jh.module.crawl.vo.WebPage;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.mongodb.DBObject;

public class YzDetailHmtlParseFilter extends YzBaseHtmlParseFilter {

    @Override
    public String getUrlFilterRegex() {
        return "^http://www.dianping.com/shop/[0-9]{1,}(\\?KID=[0-9]{1,})?$";
    }

    @Override
    public DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) throws Exception {
        String pageText = webPage.getPageText();
        DocumentFragment doc = parse(pageText);

        String storeName = getXPathValue(doc, "//*[@id='J_boxDetail']/div/div[1]/h1");
        if (StringUtils.isNotBlank(storeName)) {
            putKeyValue(parsedDBObject, "店铺名称", storeName);
            putKeyValue(parsedDBObject, "地址", StringUtils.remove(getXPathValue(doc, "//*[@id='J_boxDetail']/div/div[3]/span"), "地址："));
            putKeyValue(parsedDBObject, "费用", getXPathValue(doc, "//*[@id='J_boxDetail']/div/div[2]/div/span[2]/strong"));

            putKeyValue(parsedDBObject, "电话", getXPathValue(doc, "//*[@id='J_boxYouhui']/div[2]/p/span[1]"));

            String qqString = getXPathAttribute(doc, "//*[@id='J_boxYouhui']/div[2]/p/span[2]/a", "href");
            if (StringUtils.isNotBlank(qqString)) {
                String qq = StringUtils.substringBetween(qqString, "uin=", "&amp;site=qq");
                if (StringUtils.isNotBlank(qq)) {
                    putKeyValue(parsedDBObject, "QQ", qq);
                }
            }

            NodeList trNodeList = selectNodeList(doc, "//*[@id='J_boxBriefInfo']/div[2]/table/tbody/tr");
            if (trNodeList != null && trNodeList.getLength() > 0) {
                for (int i = 0; i < trNodeList.getLength(); i++) {
                    Node trNode = trNodeList.item(i);
                    NodeList divNodeList = selectNodeList(trNode, "./td/div");
                    if (divNodeList != null && divNodeList.getLength() > 1) {
                        String name = divNodeList.item(0).getTextContent();
                        if ("门店环境".equals(name)) {
                            continue;
                        }
                        String content = divNodeList.item(1).getTextContent();
                        if (StringUtils.isNotBlank(content)) {
                            putKeyValue(parsedDBObject, name, content);
                        }
                    }
                }
            }
        } else {
            storeName = getXPathValue(doc, "//*[@id='top']/div[4]/div[1]/div[1]/div/div[1]/h1");
            putKeyValue(parsedDBObject, "店铺名称", storeName);

            putKeyValue(parsedDBObject, "地址", getXPathValue(doc, "//*[@itemprop='locality region']") + getXPathValue(doc, "//*[@itemprop='street-address']"));
            putKeyValue(parsedDBObject, "电话", getXPathValue(doc, "//*[@itemprop='tel']"));
            putKeyValue(parsedDBObject, "费用", getXPathValue(doc, "//*[@id='top']/div[4]/div[1]/div[1]/div/div[2]/dl/dd"));
            putKeyValue(parsedDBObject, "营业时间", getXPathValue(doc, "//*[@id='top']/div[4]/div[1]/div[3]/div[2]/dl[1]/dd/span[1]"));
        }
        return parsedDBObject;
    }
}

package s2jh.biz.crawl.jmw;

import lab.s2jh.module.crawl.vo.WebPage;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;

import com.mongodb.DBObject;

public class JmwXmHtmlParseFilter extends JmwBaseHtmlParseFilter {

    @Override
    public DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) {
        String pageText = webPage.getPageText();
        DocumentFragment df = parse(pageText);
        Node node = selectSingleNode(df, "//UL[@class='fl much']");
        putKeyValue(parsedDBObject, "最低投资", getXPathValue(node, "./li[2]/a[1]"));
        putKeyValue(parsedDBObject, "所属行业", StringUtils.substringAfter(getXPathValue(node, "./li[3]"), "所属行业:"));
        putKeyValue(parsedDBObject, "品牌名称", getXPathValue(node, "./li[4]/a"));
        putKeyValue(parsedDBObject, "门店数量", getXPathValue(node, "./li[5]/span[2]"));
        putKeyValue(parsedDBObject, "加盟热线", getXPathValue(node, "//*[@id='my_telephone']"));
        putKeyValue(parsedDBObject, "公司名称", getXPathValue(node, "./li[7]/a"));
        webPage.addOutlink(url + "gongsijieshao/");
        return parsedDBObject;
    }

    @Override
    public String getUrlFilterRegex() {
        return "^http://www.jmw.com.cn/xm[0-9]*/$";
    }

    @Override
    protected String getPrimaryKey(String url) {
        return StringUtils.substringBefore(StringUtils.substringAfter(url, "jmw.com.cn/xm"), "/");
    }
}

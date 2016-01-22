package s2jh.biz.crawl.hexun;

import lab.s2jh.module.crawl.vo.WebPage;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.mongodb.DBObject;

public class HeXunStockInfoHtmlParseFilter extends HeXunStockBaseHtmlParseFilter {

    @Override
    public String getUrlFilterRegex() {
        return "^http://stockdata.stock.hexun.com/[0-9]{6}.shtml/?$";
    }

    @Override
    public DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) throws Exception {
        String pageText = webPage.getPageText();
        DocumentFragment df = parse(pageText);
        //公司简介
        {
            Node introductionNode = selectSingleNode(df, "//*[@id='a_leftmenu_dt4_1']");
            NamedNodeMap atrributes = introductionNode.getAttributes();
            Node attrNode = atrributes.getNamedItem("href");
            if (attrNode != null) {
                String introductionUrl = attrNode.getTextContent();
                webPage.addOutlink(introductionUrl);
            }
        }

        //最新财务
        {
            Node financeNode = selectSingleNode(df, "//*[@id='a_leftmenu_dt10_1']");
            NamedNodeMap atrributes = financeNode.getAttributes();
            Node attrNode = atrributes.getNamedItem("href");
            if (attrNode != null) {
                String financeUrl = attrNode.getTextContent();
                webPage.addOutlink(financeUrl);
            }
        }

        //公司高管
        String primaryKey = getPrimaryKey(url);
        webPage.addOutlink("http://stockdata.stock.hexun.com/gszl/data/jsondata/ggml.ashx?no=" + primaryKey
                + "&type=003&count=300&titType=null&page=1&callback=hxbase_json25");

        return null;
    }

    @Override
    protected String getPrimaryKey(String url) {
        return StringUtils.substringBefore(StringUtils.substringAfter(url, "http://stockdata.stock.hexun.com/"), ".shtml");
    }

}

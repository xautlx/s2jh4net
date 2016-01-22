package s2jh.biz.crawl.amac;

import java.util.Arrays;
import java.util.List;

import lab.s2jh.module.crawl.vo.WebPage;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.mongodb.DBObject;

public class AmacDetailHtmlParseFilter extends AmacBaseHtmlParseFilter {

    @Override
    public DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) {
        String pageText = webPage.getPageText();
        DocumentFragment df = parse(pageText);
        StringBuffer sb = new StringBuffer();

        //第三行带二维码的，特殊处理
        String temp = getXPathValue(df, "/html/body/div/div[2]/div/table/tbody/tr[3]/td[1]");
        String key_row2 = temp.substring(0, temp.length() - 1);
        String value_row2 = getXPathValue(df, "//*[@id=\"complaint1\"]");
        putKeyValue(parsedDBObject, key_row2, value_row2);

        NodeList nodeList = selectNodeList(df, "/html/body/div/div[2]/div/table/tbody/tr");
        //每行有两条数据
        Integer[] rows = { 6, 9, 10, 11, 12 };
        List<Integer> list = Arrays.asList(rows);
        if (nodeList != null && nodeList.getLength() > 0) {
            for (int i = 3; i < 13; i++) {
                Node node = nodeList.item(i);
                String key = getXPathValue(node, "./td[1]").substring(0, getXPathValue(node, "./td[1]").length() - 1);
                String value = getXPathValue(node, "./td[2]");
                putKeyValue(parsedDBObject, key, value);
                if (list.contains(i)) {
                    String nextKey = getXPathValue(node, "./td[3]").substring(0, getXPathValue(node, "./td[3]").length() - 1);
                    String nextValue = getXPathValue(node, "./td[4]");
                    putKeyValue(parsedDBObject, nextKey, nextValue);
                }
            }
        }

        sb.append(getXPathValue(df, "/html/body/div/div[2]/div/table/tbody/tr[15]/td[2]"));
        sb.append("(法人)");

        NodeList nodes = selectNodeList(df, "/html/body/div/div[2]/div/table/tbody/tr[18]/td[2]/table[1]/tbody/tr");
        if (nodes != null && nodes.getLength() > 0) {
            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                sb.append("," + getXPathValue(node, "./td[1]"));
                sb.append("(" + getXPathValue(node, "./td[2]") + ")");
            }
        }
        putKeyValue(parsedDBObject, "高管", sb.toString());
        return parsedDBObject;
    }

    @Override
    public String getUrlFilterRegex() {
        return "^http://gs.amac.org.cn/amac-infodisc/res/pof/manager/[0-9]*.html$";
    }

    @Override
    protected String getPrimaryKey(String url) {
        return StringUtils.substringBefore(StringUtils.substringAfter(url, "/amac-infodisc/res/pof/manager/"), ".html");
    }
}

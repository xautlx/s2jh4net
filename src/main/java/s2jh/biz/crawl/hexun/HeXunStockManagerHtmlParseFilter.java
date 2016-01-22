package s2jh.biz.crawl.hexun;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lab.s2jh.module.crawl.vo.WebPage;

import org.apache.commons.lang3.StringUtils;

import com.mongodb.DBObject;

public class HeXunStockManagerHtmlParseFilter extends HeXunStockBaseHtmlParseFilter {

    @Override
    public String getUrlFilterRegex() {
        return "^http://stockdata.stock.hexun.com/gszl/data/jsondata/ggml.ashx\\?no=[0-9]{6}&type=003&count=300&titType=null&page=1&callback=hxbase_json25$";
    }

    @Override
    public DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) throws Exception {
        String pageText = webPage.getPageText();
        String regEx = "\\{panelrate:'(.+?)'\\,StockNameLink:'.+?'\\,industry:'(.+?)'\\,Number:'.+?'\\,Numdate:'\\-\\-'.+?\\}";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(pageText);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String name = matcher.group(1);
            String title = matcher.group(2);
            sb.append(name).append("(").append(title).append(")\r");
        }
        putKeyValue(parsedDBObject, "历届高管成员", sb.toString());
        return parsedDBObject;
    }

    @Override
    protected String getPrimaryKey(String url) {
        return StringUtils.substringBefore(StringUtils.substringAfter(url, "http://stockdata.stock.hexun.com/gszl/data/jsondata/ggml.ashx?no="),
                "&type=003&count=300&titType=null&page=1&callback=hxbase_json25");
    }

}

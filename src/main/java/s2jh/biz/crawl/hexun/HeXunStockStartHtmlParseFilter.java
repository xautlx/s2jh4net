package s2jh.biz.crawl.hexun;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lab.s2jh.core.util.HttpClientUtils;
import lab.s2jh.module.crawl.vo.WebPage;

import com.mongodb.DBObject;

public class HeXunStockStartHtmlParseFilter extends HeXunStockBaseHtmlParseFilter {

    @Override
    public String getUrlFilterRegex() {
        return "^http://data.hexun.com/stock.html/?$";
    }

    @Override
    public DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) throws Exception {
        String pageText = webPage.getPageText();
        String[] urlStrings = new String[] {
                "http://quote.tool.hexun.com/hqzx/quote.aspx?type=2&market=0&sorttype=3&updown=up&page=1&count=3000&callback=hxbase_json",
                "http://quote.tool.hexun.com/hqzx/stocktype.aspx?type_code=Y0002&sorttype=3&updown=up&page=1&count=2000&time=185750",
                "http://quote.tool.hexun.com/hqzx/stocktype.aspx?type_code=Y0003&sorttype=3&updown=up&page=1&count=2000&time=185750" };
        for (String typeUrl : urlStrings) {
            String response = HttpClientUtils.doGet(typeUrl);
            String regEx = "\\['(\\d{6})','(.+?)'.+?\\]";
            Pattern pattern = Pattern.compile(regEx);
            Matcher matcher = pattern.matcher(response);
            while (matcher.find()) {
                String stockCode = matcher.group(1);
                String stockUrl = "http://stockdata.stock.hexun.com/" + stockCode + ".shtml";
                webPage.addOutlink(stockUrl);
            }
        }
        return null;
    }

}

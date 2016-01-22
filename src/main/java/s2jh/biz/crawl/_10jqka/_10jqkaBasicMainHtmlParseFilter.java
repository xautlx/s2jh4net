package s2jh.biz.crawl._10jqka;

import java.util.List;
import java.util.Map;
import java.util.Set;

import lab.s2jh.core.crawl.AbstractHtmlParseFilter;
import lab.s2jh.module.crawl.vo.WebPage;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import com.mongodb.DBObject;

public class _10jqkaBasicMainHtmlParseFilter extends AbstractHtmlParseFilter {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    private final static Set<String> whiteList = Sets.newHashSet(new String[] { "净利润", "营业总收入", "每股净资产", "销售毛利率" });

    private final static Set<String> dateWhiteList = Sets.newHashSet(new String[] { "2015-06-30", "2015-04-30", "2015-03-31", "2015-02-28",
            "2015-01-31", "2014-12-31", "2014-06-30", "2013-12-31" });

    @Override
    protected String getSiteName(String url) {
        return "www.xsbcc.com";
    }

    @Override
    public String getUrlFilterRegex() {
        return "^http://stockpage.10jqka.com.cn/basic/[0-9]+/main.txt$";
    }

    @Override
    protected String getPrimaryKey(String url) {
        return StringUtils.substringBetween(url, "http://stockpage.10jqka.com.cn/basic/", "/main.txt");
    }

    @Override
    public DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) throws Exception {
        String content = webPage.getPageText();

        //如果放回错误提示信息，直接返回null
        if (content.contains("<img src=\"http://i.thsi.cn/images/basic/stock/sub_page_bg.png\" alt=\"提示\" />")) {
            return null;
        }

        Map<String, List<Object>> map = objectMapper.readValue(content, Map.class);
        if (map != null) {
            String[] dates = map.get("report").get(0).toString().replaceAll("\\[|\\]", "").split(",");
            for (int j = 0; j < dates.length; j++) {
                String date = dates[j].trim();
                if (dateWhiteList.contains(date)) {
                    for (int i = 1; i < map.get("report").size(); i++) {
                        String[] key = map.get("title").get(i).toString().replaceAll("\\[|\\]", "").split(",");
                        String[] values = map.get("report").get(i).toString().replaceAll("\\[|\\]", "").split(",");
                        //在白名单里的菜保存
                        if (whiteList.contains(key[0].trim())) {
                            try {
                                double value = Double.parseDouble(values[j]);
                                putKeyValue(parsedDBObject, key[0].trim() + key[1].trim() + date, value);
                            } catch (Exception e) {
                            }
                        }
                    }
                }
            }
            return parsedDBObject;
        } else {
            return null;
        }
    }
}

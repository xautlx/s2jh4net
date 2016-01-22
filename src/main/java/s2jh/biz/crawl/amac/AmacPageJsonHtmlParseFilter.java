package s2jh.biz.crawl.amac;

import java.util.List;
import java.util.Map;

import lab.s2jh.core.util.JsonUtils;
import lab.s2jh.module.crawl.vo.WebPage;

import org.apache.commons.lang3.StringUtils;

import com.mongodb.DBObject;

public class AmacPageJsonHtmlParseFilter extends AmacBaseHtmlParseFilter {

    @Override
    public DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) {
        String pageText = webPage.getPageText();
        String json = StringUtils.substringBefore(StringUtils.substringAfter(pageText, "{\"content\":["), "],\"totalElements");
        logger.debug("JSON string: {}", json);
        List<Map<String, Object>> items = JsonUtils.readListValue("[" + json + "]");
        logger.debug("JSON items : {}", items);

        if (items != null && items.size() > 1) {
            for (Map<String, Object> item : items) {
                webPage.addOutlink("http://gs.amac.org.cn/amac-infodisc/res/pof/manager/" + item.get("id") + ".html");
            }

            String pager = StringUtils.substringBefore(StringUtils.substringAfter(url, "page="), "&");
            if (StringUtils.isBlank(pager)) {
                pager = "1";
            }
            int nextPage = Integer.valueOf(pager) + 1;
            webPage.addOutlink("http://gs.amac.org.cn/amac-infodisc/api/pof/manager?rand=0.04343758721559221&page=" + nextPage + "&size=20");
        }

        return null;
    }

    @Override
    public String getUrlFilterRegex() {
        return "^http://gs.amac.org.cn/amac-infodisc/api/pof/manager";
    }
}

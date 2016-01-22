package lab.s2jh.module.crawl.vo;

import java.util.Map;

import lab.s2jh.core.crawl.CrawlParseFilter;
import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Maps;
import com.mongodb.DBObject;

@Getter
@Setter
public class WebPage {

    private String url;

    private String title;

    private String pageText;

    private String bizSiteName;

    private String bizId;

    private Map<String, Outlink> outlinks = Maps.newLinkedHashMap();

    public Outlink addOutlink(String outlinkURL) {
        if (StringUtils.isBlank(outlinkURL)) {
            return null;
        }
        outlinkURL = outlinkURL.trim();

        //转换URL为绝对路径
        if (outlinkURL.startsWith("http")) {

        } else if (outlinkURL.startsWith("/")) {
            outlinkURL = "http://" + StringUtils.substringBetween(url, "http://", "/") + outlinkURL;
        } else {
            outlinkURL = "http://" + StringUtils.substringBeforeLast(StringUtils.substringAfter(url, "http://"), "/") + "/" + outlinkURL;
        }

        synchronized (this) {
            Outlink outlink = outlinks.get(outlinkURL);
            if (outlink == null) {
                outlink = new Outlink();
                outlink.setUrl(outlinkURL);
                outlink.setSourceUrl(url);
                outlinks.put(outlinkURL, outlink);
            }
            return outlink;
        }
    }

    public Outlink addOutlink(String url, boolean forceRefetch) {
        synchronized (this) {
            Outlink outlink = addOutlink(url);
            if (outlink != null) {
                outlink.setForceRefetch(forceRefetch);
            }
            return outlink;
        }
    }

    public Outlink addOutlink(String url, Class<? extends CrawlParseFilter> crawlParseFilter, String title, String bizSiteName, String bizId,
            DBObject parsedDBObject) {
        synchronized (this) {
            Outlink outlink = addOutlink(url);
            if (outlink != null) {
                if (crawlParseFilter != null) {
                    outlink.getCrawlParseFilters().add(crawlParseFilter.getName());
                }
                outlink.setBizSiteName(bizSiteName);
                outlink.setBizId(bizId);
                outlink.setTitle(title);
                outlink.setParsedDBObject(parsedDBObject);
            }
            return outlink;
        }
    }

    public Outlink addOutlink(String url, Class<? extends CrawlParseFilter> crawlParseFilter) {
        return addOutlink(url, crawlParseFilter, null, this.getBizSiteName(), this.getBizId(), null);
    }
}

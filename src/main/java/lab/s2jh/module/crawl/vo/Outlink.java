package lab.s2jh.module.crawl.vo;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.google.common.collect.Sets;
import com.mongodb.DBObject;

@Getter
@Setter
@Accessors(chain = true)
public class Outlink {

    private String url;

    private String title;

    private String bizSiteName;

    private String bizId;

    private String sourceUrl;

    private Set<String> crawlParseFilters = Sets.newHashSet();

    private DBObject parsedDBObject;

    private boolean forceRefetch = false;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}

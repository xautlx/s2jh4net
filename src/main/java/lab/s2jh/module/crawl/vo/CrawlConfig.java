package lab.s2jh.module.crawl.vo;

import java.util.Date;

import lab.s2jh.core.annotation.MetaData;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Getter
@Setter
@Accessors(chain = true)
public class CrawlConfig {

    private boolean forceRefetch = false;

    private boolean forceReparse = false;

    private Long batchId;

    @MetaData(value = "并发抓取线程数", comments = "对于速度快，没有反爬虫的站点可以根据机器性能设置较大；反正则设置较小一些")
    private int threadNum = 30;

    @MetaData(value = "抓取访问最小间隔(秒)", comments = "有些站点做了一定反爬虫控制，如限制用户请求间隔不得太快，通过合理设置此参数来规避站点封锁")
    private int fetchMinInterval = 0;

    private Date lastFetchTime;

    private Date startTime = new Date();

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}

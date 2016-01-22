package s2jh.biz.crawl.amac;

import lab.s2jh.module.crawl.vo.WebPage;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import com.mongodb.DBObject;

public class AmacSearchHtmlParseFilter extends AmacBaseHtmlParseFilter {

    @Override
    public DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) {
        webPage.addOutlink("http://gs.amac.org.cn/amac-infodisc/api/pof/manager?rand=0.04343758721559221&page=0&size=20");
        return null;
    }

    @Override
    public String getUrlFilterRegex() {
        return "^http://gs.amac.org.cn/amac-infodisc/res/pof/manager/index.html$";
    }

    public static void main(String args[]) throws Exception {
        HttpPost httpPost = new HttpPost("http://gs.amac.org.cn/amac-infodisc/api/pof/manager?rand=0.04343758721559221&page=1&size=20");
        RequestConfig config = RequestConfig.custom().setConnectTimeout(60000).setSocketTimeout(15000).build();
        CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();

        StringEntity entity = new StringEntity("{}", "utf-8");
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);

        CloseableHttpResponse response = httpClient.execute(httpPost);
        int statusCode = response.getStatusLine().getStatusCode();
        logger.debug("statusCode: {}", statusCode);
    }
}

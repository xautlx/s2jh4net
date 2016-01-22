package s2jh.biz.crawl.xsbcc;

import lab.s2jh.core.crawl.AbstractHtmlLoginFilter;
import lab.s2jh.module.crawl.service.CrawlService;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

public class XsbccHtmlLoginFilter extends AbstractHtmlLoginFilter {

    @Override
    public String getUrlFilterRegex() {
        return "^http://www.xsbcc.com/.*";
    }

    @Override
    public Boolean filterInternal(String url) throws Exception {
        HttpGet httpGet = new HttpGet(
                "http://www.xsbcc.com/common/user.htm?r=0.2826763884702226&txtphoneoremail=13124705728&txtpwd=123456&remember=undefined&tp=login");
        httpGet.addHeader("User-Agent", CrawlService.User_Agent);
        CloseableHttpResponse httpResponse = CrawlService.buildHttpClient().execute(httpGet);
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        HttpEntity httpResponseEntity = httpResponse.getEntity();
        String result = EntityUtils.toString(httpResponseEntity, CrawlService.Default_Charset_UTF8);
        EntityUtils.consumeQuietly(httpResponseEntity);
        IOUtils.closeQuietly(httpResponse);
        if (statusCode == 200 && result.indexOf("登录成功") > -1) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public static void main(String[] args) throws Exception {
        HttpGet httpGet = new HttpGet(
                "http://www.xsbcc.com/common/user.htm?r=0.2826763884702226&txtphoneoremail=13124705728&txtpwd=123456&remember=undefined&tp=login");
        httpGet.addHeader("User-Agent", CrawlService.User_Agent);
        CloseableHttpResponse httpResponse = CrawlService.buildHttpClient().execute(httpGet);
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        HttpEntity httpResponseEntity = httpResponse.getEntity();
        String result = EntityUtils.toString(httpResponseEntity, CrawlService.Default_Charset_UTF8);
        System.out.println("StatusCode:" + statusCode + ",HTML:\n" + result);
        EntityUtils.consumeQuietly(httpResponseEntity);
        IOUtils.closeQuietly(httpResponse);
    }
}

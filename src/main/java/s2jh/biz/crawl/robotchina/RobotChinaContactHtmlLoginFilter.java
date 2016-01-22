package s2jh.biz.crawl.robotchina;

import java.util.List;

import lab.s2jh.core.crawl.AbstractHtmlLoginFilter;
import lab.s2jh.module.crawl.service.CrawlService;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.google.common.collect.Lists;

public class RobotChinaContactHtmlLoginFilter extends AbstractHtmlLoginFilter {

    @Override
    public String getUrlFilterRegex() {
        return "^http://.*.robot-china.com/contact/?$";
    }

    @Override
    public Boolean filterInternal(String url) throws Exception {
        HttpPost httpPost = new HttpPost("http://www.robot-china.com/member/login.php");
        httpPost.addHeader("User-Agent", CrawlService.User_Agent);
        List<NameValuePair> pairs = Lists.newArrayList();
        pairs.add(new BasicNameValuePair("username", "youmi_sun"));
        pairs.add(new BasicNameValuePair("password", "8071885"));
        pairs.add(new BasicNameValuePair("submit", "登录"));
        httpPost.setEntity(new UrlEncodedFormEntity(pairs, CrawlService.Default_Charset_UTF8));
        CloseableHttpResponse httpPostResponse = CrawlService.buildHttpClient().execute(httpPost);
        int statusCode = httpPostResponse.getStatusLine().getStatusCode();
        IOUtils.closeQuietly(httpPostResponse);
        if (statusCode == 302) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public static void main(String[] args) throws Exception {

        HttpPost httpPost = new HttpPost("http://www.robot-china.com/member/login.php");
        httpPost.addHeader("User-Agent", CrawlService.User_Agent);

        List<NameValuePair> pairs = Lists.newArrayList();
        pairs.add(new BasicNameValuePair("username", "youmi_sun"));
        pairs.add(new BasicNameValuePair("password", "8071885"));
        pairs.add(new BasicNameValuePair("submit", "登录"));
        httpPost.setEntity(new UrlEncodedFormEntity(pairs, CrawlService.Default_Charset_UTF8));
        CloseableHttpResponse httpPostResponse = CrawlService.buildHttpClient().execute(httpPost);
        int statusCode = httpPostResponse.getStatusLine().getStatusCode();

        Header[] headers = httpPostResponse.getAllHeaders();
        for (Header header : headers) {
            System.out.println("header=" + header);
        }
        HttpEntity httpPostResponseEntity = httpPostResponse.getEntity();
        String result = EntityUtils.toString(httpPostResponseEntity, CrawlService.Default_Charset_UTF8);

        System.out.println("StatusCode:" + statusCode + ",HTML:\n" + result);

        EntityUtils.consumeQuietly(httpPostResponseEntity);
        IOUtils.closeQuietly(httpPostResponse);

    }
}


/**
 * Copyright © 2015 - 2017 EntDIY JavaEE Development Framework
 *
 * Site: https://www.entdiy.com, E-Mail: xautlx@hotmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.entdiy.core.util;

import com.entdiy.core.exception.ServiceException;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

public class HttpClientUtils {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);

    private static final CloseableHttpClient httpClient;

    public static final String CHARSET_UTF8 = "UTF-8";

    static {
        RequestConfig config = RequestConfig.custom().setConnectTimeout(60000).setSocketTimeout(15000).build();
        httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
    }

    public static String doGet(String url) {
        return doGet(url, null, null, CHARSET_UTF8);
    }

    public static String doGet(String url, Map<String, Object> params) {
        return doGet(url, params, null, CHARSET_UTF8);
    }

    public static String doGet(String url, Map<String, Object> params, Map<String, String> headers) {
        return doGet(url, params, headers, CHARSET_UTF8);
    }

    public static String doPost(String url, Map<String, Object> params) {
        return doPost(url, params, null, CHARSET_UTF8);
    }

    public static String doPost(String url, Map<String, Object> params, Map<String, String> headers) {
        return doPost(url, params, headers, CHARSET_UTF8);
    }

    public static String doPost(String url, String sendData) {
        return doPost(url, sendData, null, CHARSET_UTF8);
    }

    public static String doPost(String url, String sendData, Map<String, String> headers) {
        return doPost(url, sendData, headers, CHARSET_UTF8);
    }

    /**
     * HTTP Get 获取内容
     *
     * @param url     请求的url地址 ?之前的地址
     * @param params  请求的参数
     * @param charset 编码格式
     * @return 页面内容
     */
    public static String doGet(String url, Map<String, Object> params, Map<String, String> headers, String charset) {
        if (StringUtils.isBlank(url)) {
            return null;
        }
        if (logger.isInfoEnabled()) {
            logger.info("GET URL : {}", url);
            logger.info(" - GET Data: {}", JsonUtils.writeValueAsString(params));
        }
        try {
            if (params != null && !params.isEmpty()) {
                List<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    Object value = entry.getValue();
                    if (value != null) {
                        pairs.add(new BasicNameValuePair(entry.getKey(), ObjectUtils.toString(value)));
                    }
                }
                url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, charset));
            }

            HttpGet httpGet = new HttpGet(url);

            if (headers != null && !headers.isEmpty()) {
                Set<String> keys = headers.keySet();
                for (Iterator<String> i = keys.iterator(); i.hasNext(); ) {
                    String key = (String) i.next();
                    httpGet.addHeader(key, headers.get(key));
                }
            }
            CloseableHttpResponse response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                httpGet.abort();
                throw new RuntimeException("HttpClient,error status code :" + statusCode);
            }
            HttpEntity entity = response.getEntity();
            String result = null;
            if (entity != null) {
                result = EntityUtils.toString(entity, "utf-8");
            }
            EntityUtils.consume(entity);
            response.close();
            logger.info(" - GET Response: {}", result);
            return result;
        } catch (Exception e) {
            throw new ServiceException("HTTP GET error: " + url, e);
        }
    }

    /**
     * HTTP Post 获取内容
     *
     * @param url     请求的url地址 ?之前的地址
     * @param params  请求的参数
     * @param charset 编码格式
     * @return 页面内容
     */
    public static String doPost(String url, Map<String, Object> params, Map<String, String> headers, String charset) {
        if (StringUtils.isBlank(url)) {
            return null;
        }
        if (logger.isInfoEnabled()) {
            logger.info("POST URL : {}", url);
            logger.info(" - POST Data: {}", JsonUtils.writeValueAsString(params));
        }
        try {
            HttpPost httpPost = new HttpPost(url);
            List<NameValuePair> pairs = null;
            if (params != null && !params.isEmpty()) {
                pairs = new ArrayList<NameValuePair>(params.size());
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    String value = ObjectUtils.toString(entry.getValue());
                    if (value != null) {
                        pairs.add(new BasicNameValuePair(entry.getKey(), value));
                    }
                }
            }
            if (headers != null && !headers.isEmpty()) {
                Set<String> keys = headers.keySet();
                for (Iterator<String> i = keys.iterator(); i.hasNext(); ) {
                    String key = ObjectUtils.toString(i.next());
                    httpPost.addHeader(key, headers.get(key));

                }
            }
            if (pairs != null && pairs.size() > 0) {
                httpPost.setEntity(new UrlEncodedFormEntity(pairs, CHARSET_UTF8));
            }
            CloseableHttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                httpPost.abort();
                throw new RuntimeException("HttpClient,error status code :" + statusCode);
            }
            HttpEntity entity = response.getEntity();
            String result = null;
            if (entity != null) {
                result = EntityUtils.toString(entity, "utf-8");
            }
            EntityUtils.consume(entity);
            response.close();
            logger.info(" - POST Response: {}", result);
            return result;
        } catch (Exception e) {
            throw new ServiceException("HTTP POST error: " + url, e);
        }
    }

    public static String doPost(String url, String sendData, Map<String, String> headers, String charset) {
        if (StringUtils.isBlank(url)) {
            return null;
        }
        logger.info("POST URL : {}", url);
        logger.info(" - POST Data: {}", sendData);
        try {
            HttpPost httpPost = new HttpPost(url);
            if (headers != null && !headers.isEmpty()) {
                Set<String> keys = headers.keySet();
                for (Iterator<String> i = keys.iterator(); i.hasNext(); ) {
                    String key = ObjectUtils.toString(i.next());
                    httpPost.addHeader(key, headers.get(key));

                }
            }

            StringEntity reqEntity = new StringEntity(sendData, CHARSET_UTF8);
            if (sendData.startsWith("<?xml")) {
                reqEntity.setContentType("text/xml;charset=utf-8");
            } else {
                reqEntity.setContentType("application/json;charset=utf-8");
            }
            httpPost.setEntity(reqEntity);

            CloseableHttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                httpPost.abort();
                throw new RuntimeException("HttpClient,error status code :" + statusCode);
            }
            HttpEntity entity = response.getEntity();
            String result = null;
            if (entity != null) {
                result = EntityUtils.toString(entity, "utf-8");
            }
            EntityUtils.consume(entity);
            response.close();
            logger.info(" - POST Response: {}", result);
            return result;
        } catch (Exception e) {
            throw new ServiceException("HTTP POST error: " + url, e);
        }
    }

    public static CloseableHttpClient getHttpClientInstance() {
        return httpClient;
    }

    public static String urlEncode(String url) {
        try {
            return URLEncoder.encode(url, CHARSET_UTF8);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("Unsupported Encoding Exception", e);
        }
    }

    public static void main(String[] args) {

        //System.out.println(EnumUtils.getEnumDataMap(PartnerCategoryEnum.class).get(null));

        String getData = doGet("http://www.oschina.net/", null);
        System.out.println(getData);
        System.out.println("----------------------分割线-----------------------");
        Map<String, Object> paramMap = Maps.newHashMap();
        String postData = doPost("http://www.oschina.net/", paramMap);
        System.out.println(postData);
    }
}

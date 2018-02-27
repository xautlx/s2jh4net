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
package com.entdiy.dev;

import com.entdiy.core.cons.GlobalConstant;
import com.entdiy.core.web.AppContextHolder;
import com.entdiy.core.web.util.ServletUtils;
import com.entdiy.security.DefaultAuthUserDetails;
import com.entdiy.security.api.ClientValidationAuthenticationFilter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.options.MutableDataSet;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/dev")
public class DevelopController {

    private final static Logger logger = LoggerFactory.getLogger(DevelopController.class);

    @Autowired
    private ClientValidationAuthenticationFilter apiClientAuthenticationFilter;

    private String appkey = "default";
    private String appsecret;

    @PostConstruct
    public void init() {
        appsecret = apiClientAuthenticationFilter.getAppKeySecrets().getProperty(appkey);
    }

    @RequiresRoles(DefaultAuthUserDetails.ROLE_MGMT_USER)
    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public String dashboard() {
        return "dev/dashboard";
    }

    @RequiresRoles(DefaultAuthUserDetails.ROLE_MGMT_USER)
    @RequestMapping(value = "/docs/markdown/{name}", method = RequestMethod.GET)
    public String markdown(HttpServletRequest request, @PathVariable("name") String name, Model model) throws Exception {
        Set<String> fileNames = Sets.newTreeSet();
        String text;
        String dir = "/dev/docs/markdown";

        if (!name.endsWith(".md")) {
            name = name + ".md";
        }

        String mdDirPath = AppContextHolder.getWebContextRealPath() + dir;
        File mdDir = new File(mdDirPath);
        if (mdDir.exists()) {
            //直接web目录方式部署资源
            String mdFilePath = mdDirPath + "/" + name;
            text = FileUtils.readFileToString(new File(mdFilePath), "UTF-8");

            String[] files = mdDir.list();
            for (int i = 0; i < files.length; i++) {
                fileNames.add(files[i]);
            }
        } else {
            //如果目录不存在，兼容Servlet3协议从classpath或jar的META-INFO/resources读取
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            dir = "META-INF/resources" + dir;
            text = IOUtils.toString(loader.getResourceAsStream(dir + "/" + name), "UTF-8");

            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources("classpath*:" + dir + "/*.md");
            for (Resource resource : resources) {
                fileNames.add(resource.getFilename());
            }
        }

        model.addAttribute("files", fileNames);

        MutableDataSet options = new MutableDataSet();
        // uncomment to set optional extensions
        options.set(Parser.EXTENSIONS, Arrays.asList(TablesExtension.create(), StrikethroughExtension.create()));
        // uncomment to convert soft-breaks to hard breaks
        //options.set(HtmlRenderer.SOFT_BREAK, "<br />\n");
        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();
        // You can re-use parser and renderer instances
        Node document = parser.parse(text);
        String html = renderer.render(document);
        model.addAttribute("mdHtml", html);

        return "dev/layouts/markdown";
    }

    @RequiresRoles(DefaultAuthUserDetails.ROLE_MGMT_USER)
    @RequestMapping(value = "/api/smoke-test", method = RequestMethod.GET)
    @ResponseBody
    public void apiSmokeTest(HttpServletRequest httpServletRequest,
                             HttpServletResponse httpServletResponse) throws Exception {
        String url = ServletUtils.getRequestFullContextURL(httpServletRequest);
        RestTemplate restTemplate = new RestTemplate();
        List<String> responseList = Lists.newArrayList();

        //开放接口，自由访问
         {
            HttpEntity httpEntity = HttpEntity.EMPTY;
            ResponseEntity<String> response = restTemplate.exchange(url + "/api/pub/ping", HttpMethod.GET, httpEntity, String.class);
            responseList.add(response.getBody());
            Assert.isTrue(response.getBody().indexOf("datetime") > -1, "datetime required");
        }

        //非开放接口，未提供Client鉴权信息，请求抛出401异常
        try {
            HttpEntity httpEntity = HttpEntity.EMPTY;
            restTemplate.exchange(url + "/api/ping", HttpMethod.GET, httpEntity, String.class);
            responseList.add("Client Validation Skipped");
        } catch (Exception e) {
            logger.error("error", e);
            responseList.add(e.getMessage());
            Assert.isTrue(e.getMessage().indexOf("401") > -1, "401 required");
        }

        //动态追加Client鉴权信息，正常返回响应数据
        {
            HttpEntity httpEntity = new HttpEntity(buildApiClientHeaders());
            ResponseEntity<String> response = restTemplate.exchange(url + "/api/ping", HttpMethod.GET, httpEntity, String.class);
            responseList.add(response.getBody());
            Assert.isTrue(response.getBody().indexOf("datetime") > -1, "datetime required");
        }

        //Client鉴权通过，但是访问需要登录访问的接口，请求抛出401异常
        try {
            HttpEntity httpEntity = new HttpEntity(buildApiClientHeaders());
            restTemplate.exchange(url + "/api/ping/customer", HttpMethod.GET, httpEntity, String.class);
        } catch (Exception e) {
            responseList.add(e.getMessage());
            Assert.isTrue(e.getMessage().indexOf("401") > -1, "401 required");
        }

        //customer账号登录：错误密码情况
        {
            MultiValueMap<String, String> postData = new LinkedMultiValueMap();
            postData.add("username", "customer");
            postData.add("password", "123");
            HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity(postData, buildApiClientHeaders());
            ResponseEntity<String> response = restTemplate.exchange(url + "/api/login", HttpMethod.POST, httpEntity, String.class);
            responseList.add(response.getBody());
            Assert.isTrue(response.getBody().indexOf("不正确") > -1, "login error");
        }

        //customer账号登录：正确账号密码
        String customerAccessToken;
        {
            MultiValueMap<String, String> postData = new LinkedMultiValueMap();
            postData.add("username", "customer");
            postData.add("password", "123456");
            HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity(postData, buildApiClientHeaders());
            ResponseEntity<String> response = restTemplate.exchange(url + "/api/login", HttpMethod.POST, httpEntity, String.class);
            responseList.add(response.getBody());
            customerAccessToken = response.getHeaders().getFirst(GlobalConstant.APP_AUTH_ACCESS_TOKEN);
            Assert.isTrue(customerAccessToken != null, "access token error");
        }

        //提供user登录返回的access token，请求权限范围内的接口，正确拿到响应数据
        {
            HttpHeaders httpHeaders = buildApiClientHeaders();
            //追加user登录获取的access token
            httpHeaders.add(GlobalConstant.APP_AUTH_ACCESS_TOKEN, customerAccessToken);

            HttpEntity httpEntity = new HttpEntity(httpHeaders);
            ResponseEntity<String> response = restTemplate.exchange(url + "/api/ping/customer", HttpMethod.GET, httpEntity, String.class);
            responseList.add(response.getBody());
            Assert.isTrue(response.getBody().indexOf("authUid") > -1, "authUid required");
        }

        //提供user登录返回的access token，请求权限之外的接口，返回403
        try {
            HttpHeaders httpHeaders = buildApiClientHeaders();
            //追加user登录获取的access token
            httpHeaders.add(GlobalConstant.APP_AUTH_ACCESS_TOKEN, customerAccessToken);

            HttpEntity httpEntity = new HttpEntity(httpHeaders);
            ResponseEntity<String> response = restTemplate.exchange(url + "/api/ping/super", HttpMethod.GET, httpEntity, String.class);
            responseList.add(response.getBody());
            Assert.isTrue(response.getBody().indexOf("authUid") > -1, "authUid required");
        } catch (Exception e) {
            responseList.add(e.getMessage());
            Assert.isTrue(e.getMessage().indexOf("403") > -1, "403 required");
        }

        //root账号登录：正确账号密码
        String rootAccessToken;
        {
            MultiValueMap<String, String> postData = new LinkedMultiValueMap();
            postData.add("username", "root");
            postData.add("password", "123456");
            postData.add("authType", "admin");
            HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity(postData, buildApiClientHeaders());
            ResponseEntity<String> response = restTemplate.exchange(url + "/api/login", HttpMethod.POST, httpEntity, String.class);
            responseList.add(response.getBody());
            rootAccessToken = response.getHeaders().getFirst(GlobalConstant.APP_AUTH_ACCESS_TOKEN);
            Assert.isTrue(rootAccessToken != null, "access token error");
        }

        //提供root登录返回的access token，请求权限范围内的接口，正确拿到响应数据
        {
            HttpHeaders httpHeaders = buildApiClientHeaders();
            //追加root登录获取的access token
            httpHeaders.add(GlobalConstant.APP_AUTH_ACCESS_TOKEN, rootAccessToken);

            HttpEntity httpEntity = new HttpEntity(httpHeaders);
            ResponseEntity<String> response = restTemplate.exchange(url + "/api/ping/super", HttpMethod.GET, httpEntity, String.class);
            responseList.add(response.getBody());
            Assert.isTrue(response.getBody().indexOf("authUid") > -1, "authUid required");
        }

        //可选的登出操作。一般对于APP无需主动调用登出操作，只需把APP本地存储的access token值删除，则后续请求就会收到401就应该转向登录界面。
        {
            HttpEntity httpEntity = new HttpEntity(buildApiClientHeaders());
            ResponseEntity<String> response = restTemplate.exchange(url + "/api/logout", HttpMethod.POST, httpEntity, String.class);
            responseList.add(response.getBody());
        }

        httpServletResponse.setContentType("text/html;charset=utf-8");
        PrintWriter out = httpServletResponse.getWriter();
        String html = "<ul>" + responseList.stream().map(one -> "<li>PASSED: " + one + "</li>").collect(Collectors.joining("")) + "</ul>";
        out.println("<html><head></head><body>" + html + "</body></html>");
        out.close();
    }

    private HttpHeaders buildApiClientHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        headers.add("appkey", appkey);
        String timestamp = String.valueOf(System.currentTimeMillis());
        headers.add("timestamp", timestamp);
        String nonce = RandomStringUtils.randomAlphanumeric(10);
        headers.add("nonce", nonce);
        String str = "{" + appsecret + "}timestamp=" + timestamp + "&nonce=" + nonce;
        String sign = DigestUtils.sha1Hex(str);
        headers.add("sign", sign);
        return headers;
    }
}


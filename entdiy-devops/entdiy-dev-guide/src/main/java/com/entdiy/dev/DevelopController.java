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

import com.entdiy.core.web.AppContextHolder;
import com.entdiy.security.DefaultAuthUserDetails;
import com.google.common.collect.Sets;
import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.options.MutableDataSet;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Arrays;
import java.util.Set;

@Controller
@RequestMapping(value = "/dev")
public class DevelopController {

    private final static Logger logger = LoggerFactory.getLogger(DevelopController.class);

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
}


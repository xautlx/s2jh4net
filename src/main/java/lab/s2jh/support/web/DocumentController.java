package lab.s2jh.support.web;

import java.io.File;
import java.io.IOException;

import lab.s2jh.core.service.GlobalConfigService;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.markdown4j.Markdown4jProcessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class DocumentController {

    Markdown4jProcessor markdown4jProcessor = new Markdown4jProcessor();

    @RequestMapping(value = "/docs/markdown/{name}", method = RequestMethod.GET)
    public String markdown(@PathVariable("name") String name, Model model) throws IOException {
        //TODO 待优化避免每次查询文件列表
        String mdDirPath = GlobalConfigService.getWebRootRealPath() + "/docs/markdown";
        File mdDir = new File(mdDirPath);
        String[] files = mdDir.list();
        for (int i = 0; i < files.length; i++) {
            files[i] = StringUtils.substringBeforeLast(files[i], ".md");
        }
        model.addAttribute("files", files);

        String mdFilePath = mdDirPath + "/" + name + ".md";
        model.addAttribute("mdHtml",
                markdown4jProcessor.process(FileUtils.readFileToString(new File(mdFilePath), "UTF-8")));
        return "layouts/markdown";
    }
}

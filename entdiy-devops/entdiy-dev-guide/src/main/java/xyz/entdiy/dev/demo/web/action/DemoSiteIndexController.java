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
package xyz.entdiy.dev.demo.web.action;

import com.entdiy.auth.service.UserService;
import com.entdiy.core.util.FileUtils;
import com.entdiy.core.util.ImageUtils;
import com.entdiy.core.web.AppContextHolder;
import com.entdiy.core.web.BaseController;
import com.entdiy.core.web.view.OperationResult;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import xyz.entdiy.dev.demo.entity.DemoSiteUser;
import xyz.entdiy.dev.demo.service.DemoSiteUserService;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

@Controller
public class DemoSiteIndexController extends BaseController<DemoSiteUser, Long> {

    private final Logger logger = LoggerFactory.getLogger(DemoSiteIndexController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private DemoSiteUserService siteUserService;


    @RequestMapping(value = {"/", "/index", "/index/"}, method = RequestMethod.GET)
    public String siteIndex(HttpServletRequest request, Model model) {
        return "site/index";
    }

    @RequestMapping(value = "/password/reset", method = RequestMethod.GET)
    public String restPasswordShow(Model model) {
        return "w/password-reset";
    }

    @RequestMapping(value = "/image/upload/temp", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult imageUploadTemp(@RequestParam("photo") CommonsMultipartFile photo, HttpServletRequest request) {
        if (photo != null && !photo.isEmpty()) {
            try {
                //写入文件到临时目录，以备后续处理
                FileUtils.FileInfo fileInfo = FileUtils.writeFile(photo.getInputStream(), FileUtils.SUB_DIR_TEMP, photo.getName(), photo.getSize());

                //获取原图高宽
                BufferedImage bi = ImageIO.read(new File(fileInfo.getAbsolutePath()));
                int srcWidth = bi.getWidth();
                int srcHeight = bi.getHeight();

                Map<String, Object> userdata = Maps.newHashMap();
                userdata.put("width", srcWidth);
                userdata.put("height", srcHeight);
                userdata.put("src", fileInfo.getRelativePath());
                return OperationResult.buildSuccessResult("图片上传成功", userdata);
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return OperationResult.buildFailureResult("图片上传失败");
    }

    @RequestMapping(value = "/image/crop", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult imageCrop(HttpServletRequest request, @RequestParam("bigImage") String bigImage,
                                     @RequestParam(value = "x", required = false) Integer x, @RequestParam(value = "y", required = false) Integer y,
                                     @RequestParam(value = "w", required = false) Integer w, @RequestParam(value = "h", required = false) Integer h,
                                     @RequestParam(value = "size", required = false) Integer size) throws IOException {
        try {
            String rootDir = AppContextHolder.getFileWriteRootDir();
            String bigImagePath = rootDir + bigImage;
            //判断是否需要先进行裁剪处理
            if (x != null && w != null && w > 0) {
                //裁剪图片
                ImageUtils.cutImage(bigImagePath, bigImagePath, x, y, w, h);
                if (size != null) {
                    //缩放到统一大小
                    ImageUtils.zoomImage(bigImagePath, bigImagePath, size, size);
                }
            }
            File photoFile = new File(bigImagePath);
            FileUtils.FileInfo fileInfo = FileUtils.writeFile(new FileInputStream(photoFile), FileUtils.SUB_DIR_IMAGES, photoFile.getName(), photoFile.length());
            return OperationResult.buildSuccessResult("图片提交成功", fileInfo.getRelativePath());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return OperationResult.buildFailureResult("图片处理失败");
    }
}

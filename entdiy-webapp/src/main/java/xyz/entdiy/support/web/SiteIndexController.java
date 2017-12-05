package xyz.entdiy.support.web;

import xyz.entdiy.shop.entity.SiteUser;
import xyz.entdiy.shop.service.SiteUserService;
import com.entdiy.auth.entity.User;
import com.entdiy.auth.entity.User.AuthTypeEnum;
import com.entdiy.auth.service.UserService;
import com.entdiy.core.exception.ServiceException;
import com.entdiy.core.service.BaseService;
import com.entdiy.core.util.ImageUtils;
import com.entdiy.core.web.BaseController;
import com.entdiy.core.web.filter.WebAppContextInitFilter;
import com.entdiy.core.web.util.ServletUtils;
import com.entdiy.core.web.view.OperationResult;
import com.entdiy.support.service.DynamicConfigService;
import com.entdiy.sys.service.SmsVerifyCodeService;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
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

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping(value = "/w")
public class SiteIndexController extends BaseController<SiteUser, Long> {

    private final Logger logger = LoggerFactory.getLogger(SiteIndexController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private SiteUserService siteUserService;

    @Autowired
    private SmsVerifyCodeService smsVerifyCodeService;

    @Autowired
    private DynamicConfigService dynamicConfigService;

    @Override
    protected BaseService<SiteUser, Long> getEntityService() {
        return siteUserService;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String wwwIndex(Model model) {
        return "w/index";
    }

    @RequestMapping(value = "/password/reset", method = RequestMethod.GET)
    public String restPasswordShow(Model model) {
        return "w/password-reset";
    }

    @RequestMapping(value = "/password/reset", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult passwordResetSmsValidate(HttpServletRequest request, SiteUser entity, Model model, @RequestParam("mobile") String mobile,
                                                    @RequestParam("smsCode") String smsCode, @RequestParam(value = "newpasswd", required = false) String newpasswd) {
        if (smsVerifyCodeService.verifySmsCode(request, mobile, smsCode)) {
            User user = userService.findByAuthTypeAndAuthUid(AuthTypeEnum.SYS, mobile);
            if (user == null) {
                return OperationResult.buildFailureResult("号码尚未注册", "NoUser");
            }
            if (StringUtils.isBlank(newpasswd)) {
                return OperationResult.buildSuccessResult("短信验证码校验成功", "SmsOK");
            } else {
                //更新密码失效日期为6个月后
                user.setCredentialsExpireTime(new DateTime().plusMonths(6).toDate());
                userService.save(user, newpasswd);
                return OperationResult.buildSuccessResult("密码重置成功，您可以马上使用新设定密码登录系统啦", "ResetOK");
            }
        } else {
            return OperationResult.buildFailureResult("短信验证码不正确");
        }
    }

    @RequestMapping(value = "/image/upload", method = RequestMethod.GET)
    public String imageUploadShow() {
        return "w/imageUpload";
    }

    private static final String TEMP_PHOTO_FILE_PATH = "/temp/photo/";

    @RequestMapping(value = "/image/upload/temp", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult imageUploadTemp(@RequestParam("photo") CommonsMultipartFile photo, HttpServletRequest request) {
        if (photo != null && !photo.isEmpty()) {
            try {
                String rootDir = WebAppContextInitFilter.getInitedWebContextRealPath();
                String fileName = UUID.randomUUID() + "_" + photo.getOriginalFilename();
                File dir = new File(rootDir + TEMP_PHOTO_FILE_PATH);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                String photoFilePath = TEMP_PHOTO_FILE_PATH + fileName;
                logger.debug("Saving file: {}", rootDir + photoFilePath);
                File photoFile = new File(rootDir + photoFilePath);
                photo.transferTo(photoFile);

                BufferedImage bi = ImageIO.read(photoFile);
                int srcWidth = bi.getWidth(); // 源图宽度  
                int srcHeight = bi.getHeight(); // 源图高度  

                Map<String, Object> userdata = Maps.newHashMap();
                userdata.put("width", srcWidth);
                userdata.put("height", srcHeight);
                userdata.put("src", photoFilePath);
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
            String rootDir = WebAppContextInitFilter.getInitedWebContextRealPath();
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
            String path = ServletUtils.writeUploadFile(new FileInputStream(photoFile), photoFile.getName(), photoFile.length());
            if (StringUtils.isNotBlank(path)) {
                return OperationResult.buildSuccessResult("图片提交成功", path);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return OperationResult.buildFailureResult("图片处理失败");
    }

    @RequestMapping(value = "/image/upload/kind-editor", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> imageUpload(HttpServletRequest request, @RequestParam("imgFile") CommonsMultipartFile fileUpload) {
        Map<String, Object> retMap = Maps.newHashMap();
        try {
            if (fileUpload != null && !fileUpload.isEmpty()) {
                String path = ServletUtils.writeUploadFile(fileUpload.getInputStream(), fileUpload.getOriginalFilename(), fileUpload.getSize());
                if (StringUtils.isNotBlank(path)) {
                    retMap.put("error", 0);
                    String contextPath = request.getContextPath();
                    if ("/".equals(contextPath)) {
                        retMap.put("url", path);
                    } else {
                        retMap.put("url", contextPath + path);
                    }
                    return retMap;
                }
            }
        } catch (IOException e) {
            throw new ServiceException("Upload file error", e);
        }
        retMap.put("error", 1);
        retMap.put("message", "图片处理失败");
        return retMap;
    }

    @RequestMapping(value = "/file/upload/single", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult singleFileUpload(HttpServletRequest request, @RequestParam("fileUpload") CommonsMultipartFile fileUpload) {
        try {
            if (fileUpload != null && !fileUpload.isEmpty()) {
                String path = ServletUtils.writeUploadFile(fileUpload.getInputStream(), fileUpload.getOriginalFilename(), fileUpload.getSize());
                if (StringUtils.isNotBlank(path)) {
                    return OperationResult.buildSuccessResult("文件提交成功", path);
                }
            }
        } catch (IOException e) {
            throw new ServiceException("Upload file error", e);
        }
        return OperationResult.buildFailureResult("文件处理失败");
    }
}

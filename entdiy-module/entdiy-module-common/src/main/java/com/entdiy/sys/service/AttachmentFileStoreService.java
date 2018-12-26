/**
 * Copyright © 2015 - 2017 EntDIY JavaEE Development Framework
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
package com.entdiy.sys.service;

import com.entdiy.core.exception.ServiceException;
import com.entdiy.sys.entity.AttachmentFile;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.Calendar;

public interface AttachmentFileStoreService {

    String IMAGE_SUBDIR = "image";

    boolean WITHOUT_DATE_PATH = true;

    /**
     * 用于Spring MVC Controller层直接传入附件对象的便捷调用接口
     *
     * @param accessMode 访问模式
     * @param fileUpload Spring MVC文件上传绑定对象
     * @param subDir     存储子目录，一般是文件类型大致分类路径
     * @return
     */
    default AttachmentFile storeFileData(AttachmentFile.AccessModeEnum accessMode, CommonsMultipartFile fileUpload, String subDir) {
        try {
            return storeFileData(null, accessMode, fileUpload.getInputStream(), subDir, fileUpload.getOriginalFilename(), fileUpload.getContentType(), fileUpload.getSize());
        } catch (IOException e) {
            throw new ServiceException("File process error", e);
        }
    }

    /**
     * 用于Spring MVC Controller层直接传入附件对象的便捷调用接口
     *
     * @param fileUpload Spring MVC文件上传绑定对象
     * @param subDir     存储子目录，一般是文件类型大致分类路径
     * @return
     */
    default AttachmentFile storeFileData(CommonsMultipartFile fileUpload, String subDir) {
        return storeFileData(AttachmentFile.AccessModeEnum.PRIVATE, fileUpload, subDir);
    }

    /**
     * 基于文件流进行文件存储
     *
     * @param fis         文件输入流
     * @param subDir      存储子目录，一般是文件类型大致分类路径
     * @param fileName    文件名称
     * @param contentType 文件MIME类型
     * @param fileLength  文件大小
     * @return
     */
    default AttachmentFile storeFileData(InputStream fis, String subDir, String fileName, String contentType, Long fileLength) {
        return storeFileData(null, AttachmentFile.AccessModeEnum.PRIVATE, fis, subDir, fileName, contentType, fileLength);
    }

    default AttachmentFile storePublicImage(CommonsMultipartFile fileUpload) {
        try {
            return storePublicImage(null, fileUpload.getInputStream(), fileUpload.getOriginalFilename());
        } catch (IOException e) {
            throw new ServiceException("store CommonsMultipartFile image error", e);
        }
    }

    default AttachmentFile storePublicImage(Resource imageResource) {
        try {
            return storePublicImage(imageResource.lastModified(),
                    imageResource.getInputStream(), URLDecoder.decode(imageResource.getFilename().toString(), "UTF-8"));
        } catch (IOException e) {
            throw new ServiceException("store Resource image error", e);
        }
    }

    default AttachmentFile storePublicImage(File file) {
        try {
            return storePublicImage(file.lastModified(),
                    new FileInputStream(file), file.getCanonicalPath());
        } catch (IOException e) {
            throw new ServiceException("store File image error", e);
        }
    }

    default AttachmentFile storePublicImage(Long lastModified, InputStream fis, String importFilePath) {
        String fileContentType = "image/" + StringUtils.substringAfterLast(importFilePath, ".");
        return storeFileData(lastModified, AttachmentFile.AccessModeEnum.PUBLIC, fis, IMAGE_SUBDIR, importFilePath, fileContentType, null);
    }

    /**
     * @param lastModified   文件最后修改使用，用于生成hash值判断文件是否有变化
     * @param subDir
     * @param importFilePath 之所以没有直接使用文件名而用文件路径，主要是为了在计算hash值比单纯用文件名重复可能性更小
     * @return
     */
    default String buildFileRelativePath(Long lastModified, InputStream fis, String subDir, String importFilePath) {
        StringBuilder pathData = new StringBuilder();
        pathData.append(subDir);

        /**
         * 加上年月日分组处理，一方面便于直观看出上传文件日期信息以便批量处理，另一方面合理分组控制目录的个数和层级避免单一目录下文件过多
         * 如果传入了fileModifyDate则优先使用，可以避免不必要的文件重复存储
         */

        if (!WITHOUT_DATE_PATH) {
            if (lastModified == null) {
                lastModified = System.currentTimeMillis();
            }
            Calendar lastModifiedDate = Calendar.getInstance();
            lastModifiedDate.setTimeInMillis(lastModified);
            int year = lastModifiedDate.get(Calendar.YEAR);
            String month;
            int monthOfYear = lastModifiedDate.get(Calendar.MONTH) + 1;
            if (monthOfYear < 10) {
                month = "0" + monthOfYear;
            } else {
                month = "" + monthOfYear;
            }
            String day;
            int dayOfMonth = lastModifiedDate.get(Calendar.DAY_OF_MONTH);
            if (dayOfMonth < 10) {
                day = "0" + dayOfMonth;
            } else {
                day = "" + dayOfMonth;
            }
            pathData.append(File.separator + year);
            pathData.append(File.separator + month);
            pathData.append(File.separator + day);
        }

        String fileName = importFilePath;
        if (importFilePath.indexOf(File.separator) > -1) {
            fileName = StringUtils.substringAfterLast(importFilePath, File.separator);
        }

        // 生成一个复杂的hash值，一方面可以防止用户随意猜测访问文件url，另一方面如果有文件流参数传入基于文件流生成hash值，避免重复的文件存储
        String fileHash = String.valueOf(lastModified);

        //如果有文件流则基于文件内容计算hash，否则取文件修改时间
        if (fis != null) {
            try {
                fileHash = DigestUtils.md5Hex(fis);
            } catch (IOException e) {
                throw new ServiceException("build md5hex from inputstream error", e);
            }
        }
        //累加当前路径再次hash
        fileHash = DigestUtils.md5Hex(fileHash + importFilePath);

        //用hash值前几位分散创建目录，避免单一目录下文件夹太多
        pathData.append(File.separator + StringUtils.substring(fileHash, 0, 2) + File.separator + StringUtils.substring(fileHash, 0, 4));

        pathData.append(File.separator + fileHash);
        String relativePath = pathData + File.separator + fileName;
        return relativePath;
    }

    /**
     * 基于文件流进行文件存储
     *
     * @param accessMode  访问模式
     * @param fis         文件输入流
     * @param subDir      存储子目录，一般是文件类型大致分类路径
     * @param fileName    文件名称
     * @param contentType 文件MIME类型
     * @param fileLength  文件大小
     * @return
     */
    AttachmentFile storeFileData(Long lastModified, AttachmentFile.AccessModeEnum accessMode,
                                 InputStream fis, String subDir, String fileName, String contentType, Long fileLength);

    /**
     * 对现有相对路径文件进行等比缩小处理，基于maxWidth和maxHeight动态取长边等比缩小处理
     *
     * @param relativeFilePath 相对路径
     * @param maxWidth         最大宽度
     * @param maxHeight        最大高度
     * @return 返回处理后新创建图片文件的相对路径
     */
    String imageZoomOut(String relativeFilePath, int maxWidth, int maxHeight);

    /**
     * 添加水印，源文件覆盖
     *
     * @param relativeFilePath
     */
    String imageWatermark(String relativeFilePath, BufferedImage watermarkImage);
}

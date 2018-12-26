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
import com.entdiy.core.web.AppContextHolder;
import com.entdiy.sys.entity.AttachmentFile;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * 基于应用本地的文件存储服务，一般相对于CDN网络存储形式。
 * 基于应用属性配置参数，形如：file.write.dir=/etc/entdiy/data
 * 注意在集群部署模式下，要对file.write.dir对应路径做相关共享存储配置，如NFS
 */
public class LocalAttachmentFileStoreService implements AttachmentFileStoreService {

    private final static Logger logger = LoggerFactory.getLogger(LocalAttachmentFileStoreService.class);

    @Override
    public AttachmentFile storeFileData(Long lastModified, AttachmentFile.AccessModeEnum accessMode,
                                        InputStream fis, String subDir, String fileName, String contentType, Long fileLength) {
        try {
            File tempFile = new File(FileUtils.getTempDirectoryPath() + File.separator + UUID.randomUUID() + File.separator + fileName);
            FileUtils.copyInputStreamToFile(fis, tempFile);
            IOUtils.closeQuietly(fis);

            fis = FileUtils.openInputStream(tempFile);
            String relativePath = buildFileRelativePath(lastModified, fis, subDir, fileName);
            String storePrefix = AppContextHolder.getFileWriteRootDir();
            File toFile = new File(storePrefix + relativePath);

            if (!toFile.exists()) {
                logger.debug("Process upload Local File: {}", relativePath);
                FileUtils.forceMkdirParent(toFile);
                FileUtils.moveFile(tempFile, toFile);
            } else {
                logger.debug("Skipped Exist upload Local File: {}", relativePath);
            }

            AttachmentFile attachmentFile = new AttachmentFile();
            attachmentFile.setRelativePath(relativePath);
            attachmentFile.setStorePrefix(storePrefix);
            attachmentFile.setAccessMode(accessMode);
            attachmentFile.setFileRealName(fileName);
            attachmentFile.setFileLength(fileLength);
            attachmentFile.setFileContentType(contentType);
            return attachmentFile;
        } catch (IOException e) {
            throw new ServiceException("file store error", e);
        } finally {
            //关闭输入流
            IOUtils.closeQuietly(fis);
        }
    }

    @Override
    public String imageZoomOut(String relativeFilePath, int maxWidth, int maxHeight) {
        try {
            String storePrefix = AppContextHolder.getFileWriteRootDir();
            String newRelativeFilePath = StringUtils.substringBeforeLast(relativeFilePath, ".") + "_small." + StringUtils.substringAfterLast(relativeFilePath, ".");
            File toFile = new File(storePrefix + newRelativeFilePath);
            //如果路径文件已存在，直接返回
            if (toFile.exists()) {
                logger.debug("Skipped Exist imageZoomOut Local File: {}", newRelativeFilePath);
                return newRelativeFilePath;
            }

            logger.debug("Process imageZoomOut Local File from {} to {}", relativeFilePath, newRelativeFilePath);
            String fromFilePath = storePrefix + relativeFilePath;
            Thumbnails.of(fromFilePath).size(maxWidth, maxHeight).outputQuality(1).toFile(toFile);
            return newRelativeFilePath;
        } catch (IOException e) {
            throw new ServiceException("zoom image error", e);
        }
    }

    @Override
    public String imageWatermark(String relativeFilePath, BufferedImage watermarkImage) {
        FileInputStream fis = null;
        try {
            String storePrefix = AppContextHolder.getFileWriteRootDir();
            String fromFilePath = storePrefix + relativeFilePath;
            File fromFile = new File(fromFilePath);
            //输出文件路径基于输入文件信息稍作修改并确保规则一致，只要输入文件信息未变更则可以直接返回对应输出文件
            fis = FileUtils.openInputStream(fromFile);
            String newRelativeFilePath = buildFileRelativePath(fromFile.lastModified() + 1, fis, IMAGE_SUBDIR, fromFilePath);

            File toFile = new File(storePrefix + newRelativeFilePath);
            //如果路径文件已存在，直接返回
            if (toFile.exists()) {
                logger.debug("Skipped Exist imageWatermark Local File: {}", newRelativeFilePath);
            } else {
                logger.debug("Process imageWatermark Local File from {} to {}", relativeFilePath, newRelativeFilePath);
                FileUtils.forceMkdirParent(toFile);
                Thumbnails.of(fromFilePath)
                        .watermark(Positions.BOTTOM_RIGHT, watermarkImage, 0.2f)
                        .outputQuality(1).scale(1).toFile(toFile);
            }
            return newRelativeFilePath;
        } catch (IOException e) {
            throw new ServiceException("zoom image error", e);
        } finally {
            //关闭输入流
            IOUtils.closeQuietly(fis);
        }
    }
}

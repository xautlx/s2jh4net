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
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 基于应用本地的文件存储服务，一般相对于CDN网络存储形式。
 * 基于应用属性配置参数，形如：file.write.dir=/etc/entdiy/data
 * 注意在集群部署模式下，要对file.write.dir对应路径做相关共享存储配置，如NFS
 */
public class LocalAttachmentFileStoreService implements AttachmentFileStoreService {

    private final static Logger logger = LoggerFactory.getLogger(LocalAttachmentFileStoreService.class);

    @Override
    public AttachmentFile storeFileData(AttachmentFile.AccessModeEnum accessMode,
                                        InputStream fis, String subDir, String fileName, String contentType, long fileLength) {

        String relativePath = buildFileRelativePath(subDir, fileName);
        String storePrefix = AppContextHolder.getFileWriteRootDir();
        String absolutePath = storePrefix + relativePath;
        logger.debug("Saving upload file: {}, size: {}", absolutePath, fileLength);
        try {
            org.apache.commons.io.FileUtils.copyInputStreamToFile(fis, new File(absolutePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        AttachmentFile attachmentFile = new AttachmentFile();
        attachmentFile.setRelativePath(relativePath);
        attachmentFile.setStorePrefix(storePrefix);
        attachmentFile.setAccessMode(accessMode);
        attachmentFile.setFileRealName(fileName);
        attachmentFile.setFileLength(fileLength);
        attachmentFile.setFileContentType(contentType);
        return attachmentFile;
    }

    @Override
    public String zoomImage(String relativeFilePath, int maxWidth, int maxHeight) {
        try {
            String newRelativeFilePath = StringUtils.substringBeforeLast(relativeFilePath, ".") + "_small." + StringUtils.substringAfterLast(relativeFilePath, ".");
            String storePrefix = AppContextHolder.getFileWriteRootDir();
            File fromFile = new File(storePrefix + relativeFilePath);
            File toFile = new File(storePrefix + newRelativeFilePath);
            Thumbnails.of(FileUtils.openInputStream(fromFile)).size(maxWidth, maxHeight).toFile(toFile);
            return newRelativeFilePath;
        } catch (IOException e) {
            throw new ServiceException("zoom image error", e);
        }
    }
}

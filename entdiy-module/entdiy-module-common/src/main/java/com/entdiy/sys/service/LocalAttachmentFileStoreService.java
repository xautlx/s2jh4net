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

import com.entdiy.core.util.DateUtils;
import com.entdiy.core.web.AppContextHolder;
import com.entdiy.sys.entity.AttachmentFile;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 基于应用本地的文件存储服务，一般相对于CDN网络存储形式。
 * 基于应用属性配置参数，形如：file.write.dir=/etc/entdiy/data
 * 注意在集群部署模式下，要对file.write.dir对应路径做相关共享存储配置，如NFS
 */
public class LocalAttachmentFileStoreService implements AttachmentFileStoreService {

    private final static Logger logger = LoggerFactory.getLogger(LocalAttachmentFileStoreService.class);

    @Setter
    private AttachmentFile.AccessModeEnum accessMode = AttachmentFile.AccessModeEnum.LOCAL_READ;

    @Override
    public AttachmentFile storeFileData(InputStream fis, String subDir, String fileName, String contentType, long fileLength) {
        StringBuilder pathData = new StringBuilder();
        pathData.append(subDir);

        // 加上年月日分组处理，一方面便于直观看出上传文件日期信息以便批量处理，另一方面合理分组控制目录的个数和层级避免单一目录下文件过多
        LocalDateTime now = DateUtils.currentDateTime();
        int year = now.getYear();
        String month;
        int monthOfYear = now.getMonthValue();
        if (monthOfYear < 10) {
            month = "0" + monthOfYear;
        } else {
            month = "" + monthOfYear;
        }
        String day;
        int dayOfMonth = now.getDayOfMonth();
        if (dayOfMonth < 10) {
            day = "0" + dayOfMonth;
        } else {
            day = "" + dayOfMonth;
        }
        pathData.append(File.separator + year);
        pathData.append(File.separator + month);
        pathData.append(File.separator + day);

        // 简便的做法用UUID作为主键，每次上传都会创建文件对象和数据记录，便于管理，但是存在相同文件重复保存情况
        String id = UUID.randomUUID().toString();
        pathData.append(File.separator + id);

        String relativePath = pathData + File.separator + fileName;
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
}

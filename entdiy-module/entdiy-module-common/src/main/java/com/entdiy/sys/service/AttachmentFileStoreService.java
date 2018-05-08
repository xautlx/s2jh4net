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
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public interface AttachmentFileStoreService {

    String SUB_DIR_FILES = File.separator + "upload" + File.separator + "files";
    String SUB_DIR_TEMP = File.separator + "upload" + File.separator + "temp";
    String SUB_DIR_IMAGES = File.separator + "upload" + File.separator + "images";

    /**
     * @param fileUpload Spring MVC文件上传绑定对象
     * @param subDir     存储子目录，一般是文件类型大致分类路径
     * @return
     */
    default AttachmentFile storeFileData(CommonsMultipartFile fileUpload, String subDir) {
        try {
            return storeFileData(fileUpload.getInputStream(), subDir, fileUpload.getName(), fileUpload.getContentType(), fileUpload.getSize());
        } catch (IOException e) {
            throw new ServiceException("File process error", e);
        }
    }

    /**
     * @param fis         文件输入流
     * @param subDir      存储子目录，一般是文件类型大致分类路径
     * @param fileName    文件名称
     * @param contentType 文件MIME类型
     * @param fileLength  文件大小
     * @return
     */
    AttachmentFile storeFileData(InputStream fis, String subDir, String fileName, String contentType, long fileLength);
}

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
import com.entdiy.core.util.DateUtils;
import com.entdiy.sys.entity.AttachmentFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.UUID;

public interface AttachmentFileStoreService {

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
            return storeFileData(accessMode, fileUpload.getInputStream(), subDir, fileUpload.getOriginalFilename(), fileUpload.getContentType(), fileUpload.getSize());
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
    default AttachmentFile storeFileData(InputStream fis, String subDir, String fileName, String contentType, long fileLength) {
        return storeFileData(AttachmentFile.AccessModeEnum.PRIVATE, fis, subDir, fileName, contentType, fileLength);
    }

    default String buildFileRelativePath(String subDir,String fileName){
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
    AttachmentFile storeFileData(AttachmentFile.AccessModeEnum accessMode, InputStream fis, String subDir, String fileName, String contentType, long fileLength);

    /**
     * 对现有相对路径文件进行等比缩小处理，基于maxWidth和maxHeight动态取长边等比缩小处理
     * @param relativeFilePath 相对路径
     * @param maxWidth 最大宽度
     * @param maxHeight 最大高度
     * @return 返回处理后新创建图片文件的相对路径
     */
    String zoomImage(String relativeFilePath, int maxWidth, int maxHeight);
}

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
package com.entdiy.core.util;

import com.entdiy.core.web.AppContextHolder;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.UUID;

public class FileUtils {

    private final static Logger logger = LoggerFactory.getLogger(FileUtils.class);

    public final static String SUB_DIR_FILES = File.pathSeparator + "upload" + File.pathSeparator + "files";
    public final static String SUB_DIR_TEMP = File.pathSeparator + "upload" + File.pathSeparator + "temp";
    public final static String SUB_DIR_IMAGES = File.pathSeparator + "upload" + File.pathSeparator + "images";

    /**
     * 获取文件上传根目录：优先取 file.write.dir 参数值，如果没有定义则取webapp解包部署目录，然后追加 /upload
     *
     * @return 返回图片访问相对路径
     */
    public static FileInfo writeFile(InputStream fis, String subDir, String fileName, long fileLength) {
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
        pathData.append(File.pathSeparator + year);
        pathData.append(File.pathSeparator + month);
        pathData.append(File.pathSeparator + day);

        // 简便的做法用UUID作为主键，每次上传都会创建文件对象和数据记录，便于管理，但是存在相同文件重复保存情况
        String id = UUID.randomUUID().toString();
        pathData.append(File.pathSeparator + id);

        String relativePath = pathData + File.pathSeparator + fileName;
        String absolutePath = AppContextHolder.getFileWriteRootDir() + relativePath;
        logger.debug("Saving upload file: {}, size: {}", absolutePath, fileLength);
        try {
            org.apache.commons.io.FileUtils.copyInputStreamToFile(fis, new File(absolutePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new FileInfo(relativePath, absolutePath);
    }

    public static class FileInfo {
        @Getter
        private String relativePath;
        @Getter
        private String absolutePath;

        public FileInfo(String relativePath, String absolutePath) {
            this.relativePath = relativePath;
            this.absolutePath = absolutePath;
        }
    }
}

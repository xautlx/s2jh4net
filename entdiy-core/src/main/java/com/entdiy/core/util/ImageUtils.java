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
package com.entdiy.core.util;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

public class ImageUtils {

    /**
     * 旋转图像
     */
    public static void autoRotateImage(final String src, String dest) throws Exception {
        try {
            File srcFile = new File(src);
            File destFile = new File(dest);
            BufferedImage originalImage = ImageIO.read(srcFile);

            Metadata metadata = ImageMetadataReader.readMetadata(srcFile);
            Directory directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);

            if (directory == null || !directory.containsTag(ExifIFD0Directory.TAG_ORIENTATION)) {
                return;
            }

            int orientation = directory.getInt(ExifIFD0Directory.TAG_ORIENTATION);

            int width = originalImage.getWidth();
            int height = originalImage.getHeight();

            AffineTransform affineTransform = new AffineTransform();

            switch (orientation) {
                case 1:
                    return;
                case 2: // Flip X
                    affineTransform.scale(-1.0, 1.0);
                    affineTransform.translate(-width, 0);
                    break;
                case 3: // PI rotation
                    affineTransform.translate(width, height);
                    affineTransform.rotate(Math.PI);
                    break;
                case 4: // Flip Y
                    affineTransform.scale(1.0, -1.0);
                    affineTransform.translate(0, -height);
                    break;
                case 5: // - PI/2 and Flip X
                    affineTransform.rotate(-Math.PI / 2);
                    affineTransform.scale(-1.0, 1.0);
                    break;
                case 6: // -PI/2 and -width
                    affineTransform.translate(height, 0);
                    affineTransform.rotate(Math.PI / 2);
                    break;
                case 7: // PI/2 and Flip
                    affineTransform.scale(-1.0, 1.0);
                    affineTransform.translate(-height, 0);
                    affineTransform.translate(0, width);
                    affineTransform.rotate(3 * Math.PI / 2);
                    break;
                case 8: // PI / 2
                    affineTransform.translate(0, width);
                    affineTransform.rotate(3 * Math.PI / 2);
                    break;
                default:
                    break;
            }

            AffineTransformOp affineTransformOp = new AffineTransformOp(affineTransform, AffineTransformOp.TYPE_BILINEAR);
            BufferedImage destinationImage = new BufferedImage(originalImage.getHeight(), originalImage.getWidth(), originalImage.getType());
            destinationImage = affineTransformOp.filter(originalImage, destinationImage);
            ImageIO.write(destinationImage, dest.substring(dest.lastIndexOf(".") + 1), destFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据尺寸图片居中裁剪
     */
    public static void cutCenterImage(String src, String dest, int w, int h) throws IOException {
        String format = StringUtils.substringAfterLast(src, ".");
        Iterator iterator = ImageIO.getImageReadersByFormatName(format);
        ImageReader reader = (ImageReader) iterator.next();
        InputStream in = new FileInputStream(src);
        ImageInputStream iis = ImageIO.createImageInputStream(in);
        reader.setInput(iis, true);
        ImageReadParam param = reader.getDefaultReadParam();
        int imageIndex = 0;
        Rectangle rect = new Rectangle((reader.getWidth(imageIndex) - w) / 2, (reader.getHeight(imageIndex) - h) / 2, w, h);
        param.setSourceRegion(rect);
        BufferedImage bi = reader.read(0, param);
        ImageIO.write(bi, format, new File(dest));

    }

    /**
     * 图片裁剪二分之一
     */
    public static void cutHalfImage(String src, String dest) throws IOException {
        String format = StringUtils.substringAfterLast(src, ".");
        Iterator iterator = ImageIO.getImageReadersByFormatName(format);
        ImageReader reader = (ImageReader) iterator.next();
        InputStream in = new FileInputStream(src);
        ImageInputStream iis = ImageIO.createImageInputStream(in);
        reader.setInput(iis, true);
        ImageReadParam param = reader.getDefaultReadParam();
        int imageIndex = 0;
        int width = reader.getWidth(imageIndex) / 2;
        int height = reader.getHeight(imageIndex) / 2;
        Rectangle rect = new Rectangle(width / 2, height / 2, width, height);
        param.setSourceRegion(rect);
        BufferedImage bi = reader.read(0, param);
        ImageIO.write(bi, format, new File(dest));
    }

    /**
     * 图片裁剪通用接口
     */

    public static void cutImage(String src, String dest, int x, int y, int w, int h) throws IOException {
        String format = StringUtils.substringAfterLast(src, ".");
        Iterator iterator = ImageIO.getImageReadersByFormatName(format);
        ImageReader reader = (ImageReader) iterator.next();
        InputStream in = new FileInputStream(src);
        ImageInputStream iis = ImageIO.createImageInputStream(in);
        reader.setInput(iis, true);
        ImageReadParam param = reader.getDefaultReadParam();
        Rectangle rect = new Rectangle(x, y, w, h);
        param.setSourceRegion(rect);
        BufferedImage bi = reader.read(0, param);
        ImageIO.write(bi, format, new File(dest));

    }

    /**
     * @param is     输入流
     * @param dest   输出文件路径字符串
     * @param width
     * @param height
     * @throws IOException
     */
    public static void zoomImage(InputStream is, String dest, int width, int height) throws IOException {
        File destFile = new File(dest);
        BufferedImage bufImg = ImageIO.read(is);
        if (width <= 0) {
            width = bufImg.getWidth();
        }
        if (height <= 0) {
            height = bufImg.getHeight();
        }
        Image itemp = bufImg.getScaledInstance(width, height, BufferedImage.SCALE_SMOOTH);
        double ratio; // 缩放比例
        if ((bufImg.getHeight() > height) || (bufImg.getWidth() > width)) {
            double ratioHeight = (new Integer(height)).doubleValue() / bufImg.getHeight();
            double ratioWhidth = (new Integer(width)).doubleValue() / bufImg.getWidth();
            if (ratioHeight > ratioWhidth) {
                ratio = ratioHeight;
            } else {
                ratio = ratioWhidth;
            }
            AffineTransformOp op = new AffineTransformOp(AffineTransform//仿射转换
                    .getScaleInstance(ratio, ratio), null);//返回表示剪切变换的变换
            itemp = op.filter(bufImg, null);//转换源 BufferedImage 并将结果存储在目标 BufferedImage 中。
        }
        ImageIO.write((BufferedImage) itemp, dest.substring(dest.lastIndexOf(".") + 1), destFile);
    }
}

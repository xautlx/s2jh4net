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
package com.entdiy.sys.service;

import com.entdiy.core.exception.ServiceException;
import com.entdiy.core.service.BaseService;
import com.entdiy.sys.dao.AttachmentFileDao;
import com.entdiy.sys.entity.AttachmentFile;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Persistable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

@Service
@Transactional
public class AttachmentFileService extends BaseService<AttachmentFile, String> {

    @Autowired
    private AttachmentFileDao attachmentFileDao;

    public void injectToSourceEntity(Persistable entity, String... propertyNames) {
        if (entity == null || entity.getId() == null) {
            return;
        }
        Class<?> sourceClass = entity.getClass();
        String sourceType = sourceClass.getName();
        String sourceId = entity.getId().toString();

        for (String propertyName : propertyNames) {
            List<AttachmentFile> attachmentFiles = attachmentFileDao.findBySourceTypeAndSourceIdAndSourceCategoryOrderByOrderIndexAsc(sourceType, sourceId, propertyName);
            try {
                //如果属性是集合类型，则注入集合对象；否则注入单一附件对象
                Method getMethod = MethodUtils.getAccessibleMethod(sourceClass, "get" + StringUtils.capitalize(propertyName));
                Class<?> returnType = getMethod.getReturnType();
                if (Collection.class.isAssignableFrom(returnType)) {
                    MethodUtils.invokeMethod(entity, "set" + StringUtils.capitalize(propertyName), attachmentFiles);
                } else if (AttachmentFile.class.equals(returnType)) {
                    if (CollectionUtils.isEmpty(attachmentFiles)) {
                        MethodUtils.invokeMethod(entity, "set" + StringUtils.capitalize(propertyName), new AttachmentFile[]{null}, new Class[]{AttachmentFile.class});
                    } else if (attachmentFiles.size() == 1) {
                        MethodUtils.invokeMethod(entity, "set" + StringUtils.capitalize(propertyName), attachmentFiles.get(0));
                    } else {
                        throw new ServiceException("Found more than one record");
                    }
                } else {
                    throw new ServiceException("Invalid property class: " + returnType);
                }
            } catch (Exception e) {
                throw new ServiceException("Invoke method error", e);
            }
        }
    }

    public void saveBySourceEntity(Persistable entity, String... propertyNames) {
        Assert.isTrue(entity != null && entity.getId() != null, "Invalid unsaved entity");
        Class<?> sourceClass = entity.getClass();
        String sourceType = sourceClass.getName();
        String sourceId = entity.getId().toString();

        for (String propertyName : propertyNames) {
            try {
                final List<AttachmentFile> attachmentFiles = Lists.newArrayList();
                List<AttachmentFile> dbAttachmentFiles = attachmentFileDao.findBySourceTypeAndSourceIdAndSourceCategoryOrderByOrderIndexAsc(sourceType, sourceId, propertyName);

                //如果属性是集合类型，则注入集合对象；否则注入单一附件对象
                Method getMethod = MethodUtils.getAccessibleMethod(sourceClass, "get" + StringUtils.capitalize(propertyName));
                Class<?> returnType = getMethod.getReturnType();
                if (Collection.class.isAssignableFrom(returnType)) {
                    attachmentFiles.addAll((List<AttachmentFile>) MethodUtils.invokeMethod(entity, "get" + StringUtils.capitalize(propertyName)));
                } else if (AttachmentFile.class.equals(returnType)) {
                    AttachmentFile attachmentFile = (AttachmentFile) MethodUtils.invokeMethod(entity, "get" + StringUtils.capitalize(propertyName));
                    if (attachmentFile != null && attachmentFile.getId() != null) {
                        attachmentFiles.add(attachmentFile);
                    }
                } else {
                    throw new ServiceException("Invalid property class: " + returnType);
                }

                if (CollectionUtils.isEmpty(attachmentFiles)) {
                    //假如最新为空，数据库原先不为空，则删除所有
                    if (!CollectionUtils.isEmpty(dbAttachmentFiles)) {
                        attachmentFileDao.deleteAll(dbAttachmentFiles);
                    }
                } else {
                    //假如最新不为空，则先删除已不存在项目，然后更新新项目
                    if (!CollectionUtils.isEmpty(dbAttachmentFiles)) {
                        dbAttachmentFiles.forEach(one -> {
                            if (attachmentFiles.stream().noneMatch(cur -> cur.getId().equals(one.getId()))) {
                                attachmentFileDao.delete(one);
                            }
                        });
                    }
                    for (int i = 0; i < attachmentFiles.size(); i++) {
                        AttachmentFile one = attachmentFiles.get(i);
                        if (one.getStoreCdnMode() == null) {
                            String storePrefix = one.getStorePrefix().toLowerCase();
                            one.setStoreCdnMode(storePrefix.startsWith("http://") || storePrefix.startsWith("https://"));
                        }
                        one.setSourceType(sourceType);
                        one.setSourceId(sourceId);
                        one.setSourceCategory(propertyName);
                        one.setOrderIndex(i);
                        attachmentFileDao.updateSource(one.getSourceType(), one.getSourceId(), one.getSourceCategory(), one.getOrderIndex(), one.getId());
                    }
                }
            } catch (Exception e) {
                throw new ServiceException("Invoke method error", e);
            }
        }
    }
}

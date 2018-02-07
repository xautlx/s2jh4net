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

import com.entdiy.core.dao.jpa.BaseDao;
import com.entdiy.core.exception.ServiceException;
import com.entdiy.core.service.BaseService;
import com.entdiy.sys.dao.AttachmentFileDao;
import com.entdiy.sys.entity.AttachmentFile;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Persistable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
@Transactional
public class AttachmentFileService extends BaseService<AttachmentFile, String> {

    @Autowired
    private AttachmentFileDao attachmentFileDao;

    @Override
    protected BaseDao<AttachmentFile, String> getEntityDao() {
        return attachmentFileDao;
    }

    public void injectAttachmentFilesToEntity(Persistable entity, String propertyName) {
        if (entity == null || entity.getId() == null) {
            return;
        }
        Class<?> sourceClass = entity.getClass();
        String sourceType = sourceClass.getName();
        String sourceId = entity.getId().toString();
        List<AttachmentFile> attachmentFiles = attachmentFileDao.findBySourceTypeAndSourceIdAndSourceCategory(sourceType, sourceId, propertyName);
        try {
            MethodUtils.invokeMethod(entity, "set" + StringUtils.capitalize(propertyName), attachmentFiles);
        } catch (Exception e) {
            throw new ServiceException("Invoke method error", e);
        }
    }

    public void saveBySource(Persistable entity, String propertyName) {
        Assert.isTrue(entity != null && entity.getId() != null, "Invalid unsaved entity");
        Class<?> sourceClass = entity.getClass();
        String sourceType = sourceClass.getName();
        String sourceId = entity.getId().toString();
        try {
            final List<AttachmentFile> attachmentFiles = (List<AttachmentFile>) MethodUtils.invokeMethod(entity, "get" + StringUtils.capitalize(propertyName));
            List<AttachmentFile> dbAttachmentFiles = attachmentFileDao.findBySourceTypeAndSourceIdAndSourceCategory(sourceType, sourceId, propertyName);
            if (CollectionUtils.isEmpty(attachmentFiles)) {
                //假如最新为空，数据库原先不为空，则删除所有
                if (!CollectionUtils.isEmpty(dbAttachmentFiles)) {
                    attachmentFileDao.deleteAll(dbAttachmentFiles);
                }
            } else {
                //假如最新不为空，则先删除已不存在项目，然后最近新项目
                if (!CollectionUtils.isEmpty(dbAttachmentFiles)) {
                    dbAttachmentFiles.forEach(one -> {
                        if (attachmentFiles.stream().noneMatch(cur -> cur.getId().equals(one.getId()))) {
                            attachmentFileDao.delete(one);
                        }
                    });
                }
                attachmentFiles.forEach(one -> {
                    one.setSourceType(sourceType);
                    one.setSourceId(sourceId);
                    one.setSourceCategory(propertyName);
                    attachmentFileDao.updateSource(one.getSourceType(), one.getSourceId(), one.getSourceCategory(), one.getId());
                });
            }
        } catch (Exception e) {
            throw new ServiceException("Invoke method error", e);
        }

    }
}

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
package com.entdiy.sys.dao;

import com.entdiy.core.dao.jpa.BaseDao;
import com.entdiy.sys.entity.AttachmentFile;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;
import java.util.List;

@Repository
public interface AttachmentFileDao extends BaseDao<AttachmentFile, String> {

    @QueryHints({@QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHEABLE, value = "true")})
    List<AttachmentFile> findBySourceTypeAndSourceIdAndSourceCategoryOrderByOrderIndexAsc(String sourceType, String sourceId, String sourceCategory);

    @Modifying
    @Query("update AttachmentFile set sourceType=:sourceType , sourceId=:sourceId, sourceCategory=:sourceCategory, orderIndex=:orderIndex where id=:id")
    void updateSource(@Param("sourceType") String sourceType,
                      @Param("sourceId") String sourceId,
                      @Param("sourceCategory") String sourceCategory,
                      @Param("orderIndex") Integer orderIndex,
                      @Param("id") String id);
}

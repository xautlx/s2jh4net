/**
 * Copyright © 2015 - 2017 EntDIY JavaEE Development Framework
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.entdiy.core.aud;

import com.entdiy.core.entity.BaseEntity;
import com.entdiy.core.util.DateUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.util.Date;

/**
 * 审计记录记录创建和修改信息
 * @see AuditingEntityListener
 *
 */
public class SaveUpdateAuditListener {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private boolean dateTimeForNow = true;
    private boolean modifyOnCreation = false;
    private boolean skipUpdateAudit = false;

    /**
     * @see BaseEntity#dataGroup
     */
    private static final ThreadLocal<String> DATA_GROUP = new ThreadLocal<String>();

    public static void setDataGroup(String dataGroup) {
        SaveUpdateAuditListener.DATA_GROUP.set(dataGroup);
    }

    public void setDateTimeForNow(boolean dateTimeForNow) {
        this.dateTimeForNow = dateTimeForNow;
    }

    public void setModifyOnCreation(final boolean modifyOnCreation) {
        this.modifyOnCreation = modifyOnCreation;
    }

    /**
     * Sets modification and creation date and auditor on the target object in
     * case it implements {@link DefaultAuditable} on persist events.
     *
     * @param target
     */
    @PrePersist
    public void touchForCreate(Object target) {
        touch(target, true);
    }

    /**
     * Sets modification and creation date and auditor on the target object in
     * case it implements {@link DefaultAuditable} on update events.
     *
     * @param target
     */
    @PreUpdate
    public void touchForUpdate(Object target) {
        if (skipUpdateAudit) {
            return;
        }
        touch(target, false);
    }

    private void touch(Object target, boolean isNew) {

        if (!(target instanceof DefaultAuditable)) {
            return;
        }

        @SuppressWarnings("unchecked")
        DefaultAuditable<String, ?> auditable = (DefaultAuditable<String, ?>) target;

        String auditor = touchAuditor(auditable, isNew);
        Date now = dateTimeForNow ? touchDate(auditable, isNew) : null;

        Object defaultedNow = now == null ? "not set" : now;
        Object defaultedAuditor = auditor == null ? "unknown" : auditor;

        logger.trace("Touched {} - Last modification at {} by {}", new Object[]{auditable, defaultedNow, defaultedAuditor});
    }

    /**
     * Sets modifying and creating auditioner. Creating auditioner is only set
     * on new auditables.
     *
     * @param auditable
     * @return
     */
    private String touchAuditor(final DefaultAuditable<String, ?> auditable, boolean isNew) {

        String auditor = "NA";
        try {
            auditor = ObjectUtils.toString(SecurityUtils.getSubject().getPrincipal());
        } catch (UnavailableSecurityManagerException e) {
            //logger.warn(e.getMessage());
        }

        if (isNew) {

            auditable.setCreatedBy(auditor);

            if (!modifyOnCreation) {
                return auditor;
            }
        }

        auditable.setLastModifiedBy(auditor);
        auditable.setDataGroup(DATA_GROUP.get());

        //回收自定义的ThreadLocal变量，尤其在线程池场景下，线程经常会被复用，
        // 如果不清理自定义的 ThreadLocal变量，可能会影响后续业务逻辑和造成内存泄露等问题
        DATA_GROUP.remove();

        return auditor;
    }

    /**
     * Touches the auditable regarding modification and creation date. Creation
     * date is only set on new auditables.
     *
     * @param auditable
     * @return
     */
    private Date touchDate(final DefaultAuditable<String, ?> auditable, boolean isNew) {

        Date now = DateUtils.currentDate();

        if (isNew) {
            if (auditable.getCreatedDate() == null) {
                auditable.setCreatedDate(now);
            }

            if (!modifyOnCreation) {
                return now;
            }
        }

        auditable.setLastModifiedDate(now);

        return now;
    }
}

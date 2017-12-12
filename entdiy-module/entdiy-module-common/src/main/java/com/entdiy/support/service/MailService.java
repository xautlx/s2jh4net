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
package com.entdiy.support.service;

import com.entdiy.aud.entity.SendMessageLog;
import com.entdiy.aud.entity.SendMessageLog.SendMessageTypeEnum;
import com.entdiy.aud.service.SendMessageLogService;
import com.entdiy.core.annotation.MetaData;
import com.entdiy.core.exception.ServiceException;
import com.entdiy.core.util.DateUtils;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NamedThreadLocal;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;

import javax.mail.internet.MimeMessage;
import java.util.List;

@Service
public class MailService {

    private final Logger logger = LoggerFactory.getLogger(MailService.class);

    @Autowired
    private SendMessageLogService sendMessageLogService;

    @Autowired
    private DynamicConfigService dynamicConfigService;

    @Autowired(required = false)
    private JavaMailSender javaMailSender;

    private ThreadLocal<List<MailMessage>> mimeMessages = new NamedThreadLocal("Transaction Mail MimeMessages");

    public boolean isEnabled() {
        return javaMailSender != null;
    }

    public void sendHtmlMail(String subject, String text, boolean singleMode, String... toAddrs) {
        Assert.isTrue(isEnabled(), "Mail service unavailable");
        if (logger.isDebugEnabled()) {
            logger.debug("Submit tobe send  mail: TO: {} ,Subject: {}", StringUtils.join(toAddrs, ","), subject);
        }
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            logger.debug("Register mails with database Transaction Synchronization...");
            List<MailMessage> mails = mimeMessages.get();
            if (mails == null) {
                //如果为空，则初始化容器集合
                mails = Lists.newArrayList();
                mimeMessages.set(mails);
                //注册事务事件，事务提交后发送邮件
                TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                    @Override
                    public void afterCommit() {
                        logger.debug("Processing afterCommit of TransactionSynchronizationManager...");
                        List<MailMessage> transactionMails = mimeMessages.get();
                        //获取后立即清空线程容器，避免多线程环境干扰
                        mimeMessages.remove();
                        for (MailMessage mail : transactionMails) {
                            sendMail(mail.getSubject(), mail.getText(), mail.getSingleMode(), true, mail.getToAddrs());
                        }
                    }
                });
            }
            MailMessage mail = new MailMessage();
            mail.setSubject(subject);
            mail.setText(text == null ? "NULL" : text);
            mail.setSingleMode(singleMode);
            mail.setToAddrs(toAddrs);
            mails.add(mail);
        } else {
            sendMail(subject, text, singleMode, false, toAddrs);
        }
    }

    private void sendMail(String subject, String text, boolean singleMode, boolean transactional, String... toAddrs) {
        if (logger.isDebugEnabled()) {
            logger.debug("Sending mail: \nTO: {} \nSubject: {} \nSingle Mode: {} \nTransactional Mode: {} \nContent:\n---------\n{}\n----------",
                    StringUtils.join(toAddrs, ","), subject, singleMode, transactional, text);
        }


        //邮件发送
        MimeMessage message = javaMailSender.createMimeMessage();
        String from = dynamicConfigService.getString("mail_username");
        Assert.notNull(from);
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            if (StringUtils.isNotBlank(from)) {
                helper.setFrom(from);
            }
            helper.setSubject(subject);
            helper.setText(text, true);

            if (singleMode == false) {
                helper.setTo(toAddrs);
                javaMailSender.send(message);
            } else {
                for (String to : toAddrs) {
                    helper.setTo(to);
                    javaMailSender.send(message);
                }
            }
        } catch (Exception e) {
            throw new ServiceException(e.getMessage(), e);
        }

        //消息历史记录
        SendMessageLog sml = new SendMessageLog();
        sml.setMessageType(SendMessageTypeEnum.EMAIL);
        sml.setTargets(StringUtils.join(toAddrs));
        sml.setTitle(subject);
        sml.setMessage(text);
        sml.setSendTime(DateUtils.currentDate());
        sendMessageLogService.asyncSave(sml);
    }

    private static class MailMessage {
        private String subject;
        private String text;
        @MetaData("单用户发送模式")
        private boolean singleMode;
        private String[] toAddrs;

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String[] getToAddrs() {
            return toAddrs;
        }

        public void setToAddrs(String[] toAddrs) {
            this.toAddrs = toAddrs;
        }

        public Boolean getSingleMode() {
            return singleMode;
        }

        public void setSingleMode(Boolean singleMode) {
            this.singleMode = singleMode;
        }
    }
}

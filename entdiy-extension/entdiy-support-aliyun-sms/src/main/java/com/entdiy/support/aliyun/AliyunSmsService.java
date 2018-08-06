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
package com.entdiy.support.aliyun;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.entdiy.core.util.JsonUtils;
import com.entdiy.core.web.AppContextHolder;
import com.entdiy.support.service.SmsService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AliyunSmsService extends SmsService {

    private final Logger logger = LoggerFactory.getLogger(AliyunSmsService.class);

    @Value("${sms.signature:}")
    private String smsSignature = "";

    @Value("${sms.aliyun.access.key.id}")
    private String accessKeyId;

    @Value("${sms.aliyun.access.key.secret}")
    private String accessKeySecret;

    private static IAcsClient acsClient;

    protected IAcsClient buildIAcsClient() throws ClientException {
        if (acsClient == null) {
            //设置超时时间-可自行调整
            System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
            System.setProperty("sun.net.client.defaultReadTimeout", "10000");
            //初始化ascClient需要的几个参数
            final String product = "Dysmsapi";//短信API产品名称（短信产品名固定，无需修改）
            final String domain = "dysmsapi.aliyuncs.com";//短信API产品域名（接口地址固定，无需修改）
            //替换成你的AK
            final String accessKeyId = this.accessKeyId;//你的accessKeyId,参考本文档步骤2
            final String accessKeySecret = this.accessKeySecret;//你的accessKeySecret，参考本文档步骤2
            //初始化ascClient,暂时不支持多region（请勿修改）
            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId,
                    accessKeySecret);
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
            acsClient = new DefaultAcsClient(profile);
        }
        return acsClient;
    }

    @Override
    public String sendSMSByVendor(String signature, String templateCode, Map<String, Object> templateParams, String... mobileNum) {
        //开发模式直接返回不做短信接口调用，具体验证码可直接查看日志
        if (AppContextHolder.isDevMode()) {
            logger.info("Aliyun SMS service invoke skipped as DEV mode");
            return null;
        }
        //组装请求对象
        SendSmsRequest request = new SendSmsRequest();
        //使用post提交
        request.setMethod(MethodType.POST);
        //必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式；发送国际/港澳台消息时，接收号码格式为00+国际区号+号码，如“0085200000000”
        request.setPhoneNumbers(StringUtils.join(mobileNum, ","));
        //必填:短信签名-可在短信控制台中找到
        request.setSignName(smsSignature);
        //必填:短信模板-可在短信控制台中找到
        request.setTemplateCode(templateCode);
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        //友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
        request.setTemplateParam(JsonUtils.writeValueAsString(templateParams));
        //可选-上行短信扩展码(扩展码字段控制在7位或以下，无特殊需求用户请忽略此字段)
        //request.setSmsUpExtendCode("90997");
        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        //request.setOutId("yourOutId");

        //请求失败这里会抛ClientException异常
        try {
            SendSmsResponse sendSmsResponse = buildIAcsClient().getAcsResponse(request);
            if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
                return null;
            } else {
                String msg = sendSmsResponse.getCode() + " " + sendSmsResponse.getMessage();
                logger.error("Aliyun SMS Error: {}", msg);
                return msg;
            }
        } catch (ClientException e) {
            logger.error("Aliyun SMS Error", e);
            return e.getMessage();
        }
    }
}

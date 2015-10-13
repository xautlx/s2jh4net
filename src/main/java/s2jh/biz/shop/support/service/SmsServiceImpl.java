package s2jh.biz.shop.support.service;

import javax.annotation.PostConstruct;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.support.service.SmsService;

import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsServiceImpl implements SmsService {

    private final Logger logger = LoggerFactory.getLogger(SmsServiceImpl.class);

    @Value("${sms_signature:}")
    private String smsSignature = "";

    @MetaData(value = "模拟发送模式，此模式下直接在console打印短信内容不进行实际短信接口调用")
    @Value("${sms_mock_mode:false}")
    private String smsMockMode;

    private boolean bSmsMockMode;

    @PostConstruct
    public void init() {
        bSmsMockMode = BooleanUtils.toBoolean(smsMockMode);
        if (bSmsMockMode) {
            logger.warn("---SMS Service running at MOCK mode---");
        }
    }

    /**
     * 短信发送接口
     * @param smsContent 短信内容
     * @param mobileNum 手机号码
     * 
     * @return 如果成功则返回null；否则失败返回异常消息
     */
    @Override
    public String sendSMS(String smsContent, String mobileNum, SmsMessageTypeEnum smsType) {
        if (mobileNum == null || mobileNum.length() != 11 || !mobileNum.startsWith("1")) {
            String message = "Invalid mobile number：" + mobileNum;
            logger.warn(message);
            return message;
        }

        //追加签名信息
        smsContent += smsSignature;

        if (bSmsMockMode) {
            logger.warn("SMS Service running at MOCK mode, just print SMS content:" + smsContent);
            return null;
        }

        logger.debug("Sending SMS to {} ： {}", mobileNum, smsContent);

        //TODO 实际短信通道接口调用发送
        throw new UnsupportedOperationException("SMS API NOT Implements");
    }
}

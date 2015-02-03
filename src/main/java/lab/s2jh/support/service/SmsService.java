package lab.s2jh.support.service;

public interface SmsService {
    /**
     * 短信发送接口
     * @param smsContent 短信内容
     * @param mobileNum 手机号码
     */
    boolean sendSMS(String smsContent, String mobileNum);
}

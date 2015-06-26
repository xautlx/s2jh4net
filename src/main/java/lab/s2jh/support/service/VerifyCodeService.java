package lab.s2jh.support.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import lab.s2jh.core.util.DateUtils;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 简单的基于Http Session进行验证码的存取和判断有效性
 */
@Service
public class VerifyCodeService {

    private static final Logger logger = LoggerFactory.getLogger(VerifyCodeService.class);

    private final static int VERIFY_CODE_MAX_LIVE_TIME = 5 * 60 * 1000;

    private final static String KEY_SESSION_SMS_DATA = "KEY_SESSION_SMS_DATA";

    private static Map<String, SmsCode> smsCodeContainer;

    /**
     * 基于手机号码生成6位随机验证码
     * @param mobileNum 手机号码
     * @return 6位随机数
     */
    public String generateSmsCode(HttpServletRequest request, String mobileNum) {
        String code = RandomStringUtils.randomNumeric(6);
        HttpSession session = request.getSession(false);
        if (!DynamicConfigService.isDevMode() && session != null) {
            session.setAttribute(KEY_SESSION_SMS_DATA, code);
        } else {
            //在没有session情况下，其他容错处理
            if (smsCodeContainer == null) {
                smsCodeContainer = Maps.newHashMap();
            }
            SmsCode smsCode = new SmsCode();
            smsCode.setCode(code);
            smsCode.setMobileNum(mobileNum);
            smsCode.setGenerateTime(DateUtils.currentDate());
            smsCodeContainer.put(mobileNum, smsCode);
        }
        return code;
    }

    /**
     * 验证手机验证码有效性
     * @param mobileNum 手机号码
     * @param code 验证码
     * @return 布尔类型是否有效
     */
    public boolean verifySmsCode(HttpServletRequest request, String mobileNum, String code) {
        String serverCode = null;
        HttpSession session = request.getSession(false);
        if (!DynamicConfigService.isDevMode() && session != null) {
            serverCode = (String) session.getAttribute(KEY_SESSION_SMS_DATA);
        } else {
            //在没有session情况下，其他容错处理
            SmsCode smsCode = smsCodeContainer.get(mobileNum);
            if (smsCode != null) {
                serverCode = smsCode.getCode();
            }
        }

        if (serverCode == null) {
            return false;
        }
        return serverCode.equals(code);
    }

    /**
     * 定时把超时的验证码移除，避免无意义的无限制的消耗内存
     */
    @Scheduled(fixedRate = VERIFY_CODE_MAX_LIVE_TIME)
    public void removeExpiredDataTimely() {
        logger.debug("Timely trigger removed expired verify code cache data...");
        if (smsCodeContainer != null) {
            Long now = DateUtils.currentDate().getTime();
            List<String> toRemoves = Lists.newArrayList();
            for (Map.Entry<String, SmsCode> me : smsCodeContainer.entrySet()) {
                SmsCode smsCode = me.getValue();
                if (now - smsCode.getGenerateTime().getTime() > VERIFY_CODE_MAX_LIVE_TIME) {
                    toRemoves.add(me.getKey());
                }
            }
            for (String key : toRemoves) {
                smsCodeContainer.remove(key);
            }
            logger.debug("Removed expired verify code cache data number: {}", toRemoves.size());
        }
    }

    public static class SmsCode implements Serializable {

        private static final long serialVersionUID = 615208416034164816L;

        private String mobileNum;

        private String code;

        private Date generateTime;

        public String getMobileNum() {
            return mobileNum;
        }

        public void setMobileNum(String mobileNum) {
            this.mobileNum = mobileNum;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public Date getGenerateTime() {
            return generateTime;
        }

        public void setGenerateTime(Date generateTime) {
            this.generateTime = generateTime;
        }
    }
}

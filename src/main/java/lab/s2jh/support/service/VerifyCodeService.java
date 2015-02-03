package lab.s2jh.support.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

/**
 * 简单的基于Http Session进行验证码的存取和判断有效性
 */
@Service
public class VerifyCodeService {

    public final static String KEY_SESSION_SMS_DATA = "KEY_SESSION_SMS_DATA";

    /**
     * 基于手机号码生成6位随机验证码
     * @param mobileNum 手机号码
     * @return 6位随机数
     */
    public String generateSmsCode(HttpServletRequest request, String mobileNum) {
        String code = RandomStringUtils.randomNumeric(6);
        HttpSession session = request.getSession();
        session.setAttribute(KEY_SESSION_SMS_DATA, code);
        return code;
    }

    /**
     * 验证手机验证码有效性
     * @param mobileNum 手机号码
     * @param code 验证码
     * @return 布尔类型是否有效
     */
    public boolean verifySmsCode(HttpServletRequest request, String mobileNum, String code) {
        HttpSession session = request.getSession();
        String serverCode = (String) session.getAttribute(KEY_SESSION_SMS_DATA);
        if (serverCode == null) {
            return false;
        }
        return serverCode.equals(code);
    }
}

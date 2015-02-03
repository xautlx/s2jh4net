package lab.s2jh.core.security;

import lab.s2jh.core.util.Digests;
import lab.s2jh.core.util.Encodes;

import org.springframework.stereotype.Service;

@Service
public class PasswordService {

    public static final String HASH_ALGORITHM = "MD5";
    public static final int SALT_SIZE = 8;

    public String generateSalt() {
        byte[] salt = Digests.generateSalt(SALT_SIZE);
        return Encodes.encodeHex(salt);
    }

    public String entryptPassword(String rawPassword) {
        return entryptPassword(rawPassword, null);
    }

    public String entryptPassword(String rawPassword, String salt) {
        byte[] hashPassword = null;
        if (salt == null) {
            hashPassword = Digests.md5(rawPassword.getBytes());
        } else {
            hashPassword = Digests.md5((injectPasswordSalt(rawPassword, salt)).getBytes());
        }
        return Encodes.encodeHex(hashPassword);
    }

    /**
     * 对原始密码注入盐值
     */
    public String injectPasswordSalt(String rawPassword, String salt) {
        return "{" + salt + "}" + rawPassword;
    }

    public static void main(String args[]) {
        PasswordService ps = new PasswordService();

        System.out.println(ps.entryptPassword("123456", "7db5ace1b3a97edb"));

        System.out.println(ps.entryptPassword("123456", null));

    }
}

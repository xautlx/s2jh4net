package lab.s2jh.core.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class NumberUtils {

    private static final int DEF_DIV_SCALE = 10;

    private NumberUtils() {
    }

    public static BigDecimal round(BigDecimal decimal) {
        return new BigDecimal(Math.round(decimal.doubleValue()));
    }

    public static BigDecimal nullToZero(BigDecimal num) {
        if (num == null) {
            return new BigDecimal("0");
        }
        return num;
    }

    public static Long nullToZero(Long num) {
        if (num == null) {
            return new Long("0");
        }
        return num;
    }

    public static BigDecimal nullToZero(String num) {
        if (num == null) {
            return new BigDecimal("0");
        }
        return new BigDecimal(num);
    }

    public static Integer nullToZero(Integer num) {
        if (num == null) {
            return new Integer("0");
        }
        return num;
    }

    public static boolean isNumber(String num) {
        if (num == null) {
            return false;
        }
        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(num);
        return m.matches();
    }

    public static boolean isNumberForImport(String num) {
        Pattern p = Pattern.compile("(\\d+)|(\\d+.{1}\\d+)");
        Matcher m = p.matcher(num);
        if (m.matches()) {
            /** 是小数且小数位不为0 */
            Float fNum = Float.parseFloat(num);
            return fNum.floatValue() == fNum.longValue();
        }
        return false;
    }

    public static boolean isFloat(String num) {
        Pattern p = Pattern.compile("(\\d+)|(\\d+.{1}\\d+)");
        Matcher m = p.matcher(num);
        return m.matches();
    }

    public static boolean isInteger(String num) {
        Pattern p = Pattern.compile("(\\d+)");
        Matcher m = p.matcher(num);
        return m.matches();
    }

    /**
     * 有指定小数位
     * @param num
     * @param floatnum
     * @return
     */
    public static boolean isFloat(String num, int floatnum) {
        if (floatnum == 0) {
            return isInteger(num);
        }
        Pattern p = Pattern.compile("(\\d+)|(\\d+.{1}\\d{0," + floatnum + "})");
        Matcher m = p.matcher(num);
        return m.matches();
    }

    /**
     * 提供精确的加法运算。
     * 
     * @param v1
     *            被加数
     * @param v2
     *            加数
     * @return 两个参数的和
     */
    public static BigDecimal add(BigDecimal v1, BigDecimal v2) {
        BigDecimal sum = new BigDecimal(0);
        // 设一个不为0的值
        if (v1 == null || v2 == null) {
            if (v1 == null && v2 == null) {
                return sum;
            }
            return v1 == null ? v2 : v1;
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1.doubleValue()));
        BigDecimal b2 = new BigDecimal(Double.toString(v2.doubleValue()));
        return b1.add(b2);
    }

    /**
     * 提供精确的减法运算。
     * 
     * @param v1
     *            被减数
     * @param v2
     *            减数
     * @return 两个参数的差
     */
    public static BigDecimal sub(BigDecimal v1, BigDecimal v2) {
        BigDecimal b1 = new BigDecimal("0");
        if (v1 != null) {
            b1 = new BigDecimal(Double.toString(v1.doubleValue()));
        }
        BigDecimal b2 = new BigDecimal("0");
        if (v2 != null) {
            b2 = new BigDecimal(Double.toString(v2.doubleValue()));
        }
        return b1.subtract(b2);
    }

    /**
     * 比较两个数大小
     * 
     * @param v1
     *            第一个数字
     * @param v2
     *            第二个数字
     * @return 大于 还回 1 、等于还回 0 、小于 还回 -1
     */
    public static int compare(BigDecimal v1, BigDecimal v2) {
        int intValue = 0;
        Double d1 = v1.doubleValue();
        Double d2 = v2.doubleValue();
        if (d1 > d2) {
            intValue = 1;
        }
        if (d1 == d2) {
            intValue = 0;
        }
        if (d1 < d2) {
            intValue = -1;
        }
        return intValue;
    }

    /**
     * 提供精确的乘法运算。
     * 
     * @param v1
     *            被乘数
     * @param v2
     *            乘数
     * @return 两个参数的积
     */
    public static BigDecimal mul(BigDecimal v1, BigDecimal v2) {
        if (v1 == null || v2 == null) {
            return new BigDecimal(0);
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1.doubleValue()));
        BigDecimal b2 = new BigDecimal(Double.toString(v2.doubleValue()));

        return b1.multiply(b2);
    }

    /**
     * 提供精确的乘法运算。
     * 
     * @param v1
     *            被乘数
     * @param v2
     *            乘数
     * @return 两个参数的积
     */
    public static BigDecimal mul(Long v1, BigDecimal v2) {
        if (v1 == null || v2 == null) {
            return new BigDecimal(0);
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1.doubleValue()));
        BigDecimal b2 = new BigDecimal(Double.toString(v2.doubleValue()));
        return b1.multiply(b2);
    }

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入。
     * 
     * @param v1
     *            被除数
     * @param v2
     *            除数
     * @return 两个参数的商
     */
    public static BigDecimal div(BigDecimal v1, BigDecimal v2) {
        return div(v1, v2, DEF_DIV_SCALE);
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
     * 
     * @param v1
     *            被除数
     * @param v2
     *            除数
     * @param scale
     *            表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static BigDecimal div(BigDecimal v1, BigDecimal v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1.doubleValue()));
        BigDecimal b2 = new BigDecimal(Double.toString(v2.doubleValue()));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 提供精确的小数位四舍五入处理。
     * 
     * @param v
     *            需要四舍五入的数字
     * @param scale
     *            小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static BigDecimal round(BigDecimal v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v.doubleValue()));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 数字、货币格式化
     * @version 2011-9-21 下午09:19:39
     * @param pattern
     * @param number
     * @return
     */
    public static String numberFormat(String pattern, BigDecimal number) {
        String numberStr = null;
        if (number == null) {
            return "";
        }
        try {
            if (pattern == null || pattern.equals("")) {
                numberStr = new DecimalFormat("#0.00##").format(number.doubleValue());
            } else {
                numberStr = new DecimalFormat(pattern).format(number.doubleValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return numberStr;
    }

    /**
     * 精确到小数位后两位
     */
    public static String isDouble(String num) {
        if (num.equals("-") || StringUtils.isBlank(num)) {
            return "-";
        } else {
            DecimalFormat decimalFormat = new DecimalFormat("#.00");
            return decimalFormat.format(Double.valueOf(num));
        }
    }

    /**
     * 返回负数
     * @param num
     * @return
     */
    public static BigDecimal negative(BigDecimal num) {
        return NumberUtils.mul(num, new BigDecimal(-1));
    }

    //二进制转16进制
    public static String binaryString2hexString(String bString) {
        if (bString == null || bString.equals("") || bString.length() % 8 != 0)
            return null;
        StringBuffer tmp = new StringBuffer();
        int iTmp = 0;
        for (int i = 0; i < bString.length(); i += 4) {
            iTmp = 0;
            for (int j = 0; j < 4; j++) {
                iTmp += Integer.parseInt(bString.substring(i + j, i + j + 1)) << (4 - j - 1);
            }
            tmp.append(Integer.toHexString(iTmp));
        }
        return tmp.toString();
    }

    //16进制转二进制
    public static String hexString2binaryString(String hexString) {
        if (hexString == null || hexString.length() % 2 != 0)
            return null;
        String bString = "", tmp;
        for (int i = 0; i < hexString.length(); i++) {
            tmp = "0000" + Integer.toBinaryString(Integer.parseInt(hexString.substring(i, i + 1), 16));
            bString += tmp.substring(tmp.length() - 4);
        }
        return bString;
    }

    public static void main(String[] args) {
        // System.out.println((float)Math.ceil(5.5*10)/10);
        System.out.println(NumberUtils.numberFormat("#,##0.0000", new BigDecimal("53232332.3656")));
        System.out.println(NumberUtils.sub(new BigDecimal(6.0), new BigDecimal(5.3)));
        System.out.println(NumberUtils.compare(NumberUtils.sub(new BigDecimal(4900.23), new BigDecimal(4000)), BigDecimal.ZERO));
        /*long lang=1;
        String str1=Long.toBinaryString(lang);//将给定的数转为二进制字串
        
        System.out.println(str1);
        
        String str2=Long.toBinaryString(~lang);//将给定的数取反转为二进制字串  //将取反后的二进制串取最后的   长度与第一个串长度相等  的子串，这是我们需要的二进制串  
        String str=str2.substring(str2.length()-str1.length());
         //将它转换为十进制整数  
        int i=Integer.parseInt(str, 2);  System.out.println(i);
        
        int temp= 1;
        temp=~temp;
        temp=temp&0xFFFFFFFF;
        System.out.println(temp);
        System.out.println(0&0XFF);
        String aa = Long.toBinaryString(~1);
        System.out.println(aa);
        System.out.println(NumberUtil.binaryString2hexString(aa));
        String ad = "";
        String sa = "0000000000000111";
        for(int j = 0;j<sa.length();j++){
            String asdf = sa.substring(j,j+1);
            System.out.println(asdf);;
            //System.out.println(RfM1CardAction.binaryString2hexString(Long.toBinaryString(~Integer.valueOf(asdf))));
            ad = ad +Integer.toHexString(Integer.valueOf(asdf)^0X0F);
            System.out.println(Integer.toHexString(Integer.valueOf(asdf)^0X0F) );
        }
        System.out.println(ad);
        System.out.println(1^0X0F);
        System.out.println(Integer.toHexString(14) );*/

    }

}

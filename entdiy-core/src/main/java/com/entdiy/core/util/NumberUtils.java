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
package com.entdiy.core.util;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
     *
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
     * @param v1 被加数
     * @param v2 加数
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
     * @param v1 被减数
     * @param v2 减数
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
     * @param v1 第一个数字
     * @param v2 第二个数字
     * @return 大于 还回 1 、等于还回 0 、小于 还回 -1
     */
    public static int compare(BigDecimal v1, BigDecimal v2) {
        int intValue = 0;
        double d1 = v1.doubleValue();
        double d2 = v2.doubleValue();
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
     * @param v1 被乘数
     * @param v2 乘数
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
     * @param v1 被乘数
     * @param v2 乘数
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
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     */
    public static BigDecimal div(BigDecimal v1, BigDecimal v2) {
        return div(v1, v2, DEF_DIV_SCALE);
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 表示表示需要精确到小数点以后几位。
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
     * @param v     需要四舍五入的数字
     * @param scale 小数点后保留几位
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
     *
     * @param pattern
     * @param number
     * @return
     * @version 2011-9-21 下午09:19:39
     */
    public static String numberFormat(String pattern, BigDecimal number) {
        String numberStr = null;
        if (number == null) {
            return "";
        }
        try {
            if (StringUtils.isBlank(pattern)) {
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
     * 返回负数
     *
     * @param num
     * @return
     */
    public static BigDecimal negative(BigDecimal num) {
        return NumberUtils.mul(num, new BigDecimal(-1));
    }

    /**
     * 计算百分比
     *
     * @param num
     * @param total
     * @param scale
     * @return
     */
    public static String accuracy(double num, double total, int scale) {
        if (total == 0d) {
            return "0";
        }
        DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
        //可以设置精确几位小数
        df.setMaximumFractionDigits(scale);
        //模式 例如四舍五入
        df.setRoundingMode(RoundingMode.HALF_UP);
        double accuracy_num = num / total * 100;
        return df.format(accuracy_num);
    }

}

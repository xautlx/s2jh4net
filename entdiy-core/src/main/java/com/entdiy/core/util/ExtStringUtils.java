/**
 * Copyright (c) 2012
 */
package com.entdiy.core.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class ExtStringUtils {
    private static final Log logger = LogFactory.getLog(ExtStringUtils.class);

    public static final String WIN_ENTER = "\r\n";

    private final static char CHARS[] = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C',
            'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
            'Y', 'Z' };

    /**
     * 右省略的方式表示字符串，len长度后的字符串表示为“...”
     * 
     * @param oldStr
     * @param len
     * @return
     * @author niez
     * @since May 26, 2010
     */
    public static String cutRedundanceStr(String oldStr, int len) {
        if (oldStr == null) {
            return "";
        }
        if (oldStr.length() <= len) {
            return oldStr;
        } else {
            return (oldStr.substring(0, len) + " ...");
        }
    }

    /**
     * 左省略，len长度前的字符串表示为“...”
     * 
     * @param oldStr
     * @param len
     * @return
     * @author niez
     * @since May 26, 2010
     */
    public static String cutLeftRedundanceStr(String oldStr, int len) {
        if (oldStr == null) {
            return "";
        }
        if (oldStr.length() <= len) {
            return oldStr;
        } else {
            return ("..." + oldStr.substring(oldStr.length() - len, oldStr.length()));
        }
    }

    /**
     * @author zyb
     * @date 2004/6/8
     * 
     * @note : convert a date to string with format as YYYY-MM-DD
     * 
     * 
     * @param value
     * @return
     * @throws Exception
     */
    public static String toYMDString(Date value) throws Exception {
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
        return df1.format(value);
    }

    /**
     * @author zyb
     * @date 2004/6/8
     * 
     * @note : convert a string to date with YYYY-MM-dd
     * 
     * @param value
     * @return
     * @throws Exception
     */
    public static Date toYMDDate(String value) throws Exception {
        try {
            SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
            Date dateResult = df1.parse(value);
            return dateResult;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    /**
     * @author zyb
     * @date 2004/6/8
     * @note :
     * 
     * @param beginDate
     * @param endDate
     * @return
     */
    public static long compareDate(String beginDate, String endDate) throws Exception {
        try {
            Date d1 = ExtStringUtils.toYMDDate(beginDate);
            Date d2 = ExtStringUtils.toYMDDate(endDate);
            System.out.println("d1: " + d1 + "d2 :" + d2);

            long beginTime = d1.getTime();
            long endTime = d2.getTime();
            long betweenDays = (long) ((endTime - beginTime) / (1000 * 60 * 60 * 24) + 0.5);
            System.out.println("mins : " + betweenDays);
            return betweenDays;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    public static boolean isSequenceDate(String dateBefore, String dateAfter) {
        try {
            return 1.0 == compareDate(dateBefore, dateAfter);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * format the date
     */
    public static String format(Date date, String pattern) {
        return new java.text.SimpleDateFormat(pattern).format(date);
    }

    public static String getFileContent(String fileName) throws FileNotFoundException, IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "GB2312"));
        StringBuffer content = null;
        try {
            content = new StringBuffer("");
            String tmp = r.readLine();
            while (tmp != null) {
                content = content.append(tmp).append(WIN_ENTER);
                tmp = r.readLine();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            r.close();
        }
        return content.toString();
    }

    public static String getStringFromStream(InputStream in) throws Exception {
        BufferedReader r = new BufferedReader(new InputStreamReader(in, "GB2312"));
        String tmp = r.readLine();
        StringBuffer returnS = new StringBuffer("");
        while (tmp != null) {
            returnS = returnS.append(tmp).append(WIN_ENTER);
            tmp = r.readLine();
        }
        return returnS.toString();
    }

    public static List getInterContent(String src, String start, String end) {
        List tmp = new ArrayList();
        int i = src.indexOf(start);
        int j = -1;
        while (i >= 0) {
            j = src.indexOf(end, i + start.length());
            if (j >= 0) {
                tmp.add(src.substring(i + start.length(), j));
            }
            i = src.indexOf(start, i + start.length());
        }
        return tmp;
    }

    public static String getRandomNumber(int length) {
        int randomNumber;
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            randomNumber = (int) (Math.random() * 10);
            sb.append(randomNumber);
        }
        return sb.toString();
    }

    public static char getNextChar(char c) {
        for (int i = 0; i < CHARS.length; i++) {
            if (c == CHARS[i] && i != CHARS.length - 1) {
                return CHARS[i + 1];
            }
        }
        throw new RuntimeException("can not get the next char.");
    }

    public static String getExcelColumn(int index) {
        char charA = 'A';
        int i = index / 26;
        int j = index % 26;
        if (i == 0) {
            return "" + (char) (charA + j);
        } else {
            return "" + (char) (charA + i - 1) + (char) (charA + j);
        }
    }

    /**
     * Find the index of the searchStr in the string array strs.
     * Return -1, if not found.
     * 
     * <pre>
     * StringUtil.indexOf(["s1", "s2"], "s1", true) = 0
     * StringUtil.indexOf(["s1", "s2"], "S1", true) = -1
     * </pre>
     * 
     * @param strs
     *            the string array to check, maybe null.
     * @param searchStr
     *            the string to search for, maybe null.
     * @param caseSensive
     *            is it case sensive while finding the searchStr
     *            in the searchStr.
     * @return index of the searchStr found in the strs, -1 if not found.
     * 
     * @author chenke
     */
    public static int indexOf(String[] strs, String searchStr, boolean caseSensive) {

        if (strs == null || strs.length == 0) {
            return -1;
        }
        for (int i = 0; i < strs.length; i++) {
            if (isEqual(strs[i], searchStr, caseSensive)) {
                return i;
            }
        }
        return -1;
    }

    public static int indexOf(String[] strs, String searchStr) {
        return indexOf(strs, searchStr, true);
    }

    private static boolean isEqual(String s1, String s2, boolean caseSensive) {
        if (caseSensive) {
            return s1 == null ? s2 == null : s1.equals(s2);
        } else {
            return s1 == null ? s2 == null : s1.equalsIgnoreCase(s2);
        }
    }

    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    /**
     * Returns a string array contains all the String object in the
     * Collection c.
     * 
     * @param c
     *            Collection of String object.
     * @throws ClassCastException
     *             if the object in the Collection is
     *             not a String object.
     * 
     * @author chenke
     */
    public static String[] toArray(Collection c) {
        if (c == null || c.size() == 0) {
            return EMPTY_STRING_ARRAY;
        }
        String[] result = new String[c.size()];
        int i = 0;
        for (Iterator iter = c.iterator(); iter.hasNext();) {
            result[i++] = (String) iter.next();
        }
        return result;
    }

    /**
     * Return the result of left minus right.
     * 
     * <pre>
     * StringUtil.minus(["s1", "s2"], ["s1"], true) = ["s2"]
     * StringUtil.minus(["s1", "s2"], ["S1"], false) = ["s2"]
     * </pre>
     * 
     * @author chenke
     */
    public static String[] minus(String[] left, String[] right, boolean caseSensive) {
        if (left == null || left.length == 0) {
            return EMPTY_STRING_ARRAY;
        }
        if (right == null || right.length == 0) {
            return left;
        }
        List result = new ArrayList(left.length);
        for (int i = 0; i < left.length; i++) {
            int index = indexOf(right, left[i], caseSensive);
            if (index == -1) {
                result.add(left[i]);
            }
        }
        return toArray(result);
    }

    public static String[] minus(String[] left, String[] right) {
        return minus(left, right, true);
    }

    /**
     * Return a String object join all String object in param c, and add
     * param left and param right to every String object on the left side
     * and right side, separaing with param separator.
     * 
     * <pre>
     * join(["s1", "s2"], "left", "right", ",") = "lefts1right,lefts2right"
     * </pre>
     * 
     * @throws ClassCastException
     *             if the object in the Collection is
     *             not a String object.
     */
    public static String join(Collection c, String left, String right, String separator) {

        if (c == null || c.size() == 0) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        boolean firstFlag = true;
        for (Iterator it = c.iterator(); it.hasNext();) {
            if (firstFlag) {
                firstFlag = false;
            } else if (separator != null) {
                sb.append(separator);
            }
            String s = (String) it.next();
            if (left != null) {
                sb.append(left);
            }
            sb.append(s);
            if (right != null) {
                sb.append(right);
            }
        }
        return sb.toString();
    }

    public static String join(Collection c) {
        return join(c, "<", ">", ",");
    }

    public static String[] plus(String[] s1, String[] s2) {
        if (s1 == null) {
            return s2 == null ? EMPTY_STRING_ARRAY : s2;
        }
        if (s2 == null) {
            return s1;
        }

        String[] result = new String[s1.length + s2.length];

        System.arraycopy(s1, 0, result, 0, s1.length);
        System.arraycopy(s2, 0, result, s1.length, s2.length);
        return result;
    }

    /**
     * converts the specified <code>String</code> array to a token <code>String</code>.
     * the token is a char ','. if <code>array</code> is <code>null</code> or zeor length,
     * return an empty <code>String</code>; if the element in the <code>array</code> is <code>null</code> or empty
     * <code>String</code>, ignores this element.
     * 
     * @param array
     *            to be converted array
     * @return converted token <code>String</code>
     * @author sunf
     * @since AP.308
     */
    public static String convertArrayToString(String[] array) {
        if (array == null || array.length == 0) {
            return "";
        }
        final char token = ',';
        StringBuffer result = new StringBuffer();

        for (int i = 0, size = array.length; i < size; i++) {
            if (array[i] == null || array[i].length() == 0) {
                continue;
            }
            result.append(array[i]);
            result.append(token);
        }
        // deletes the last char of \'
        if (result.length() > 1) {
            result.deleteCharAt(result.length() - 1);
        }

        return result.toString();
    }

    public static boolean isEquals(String s1, String s2) {
        return s1 == null ? s2 == null : s1.equals(s2);
    }

    /**
     * if str starts with a prefix which is included in prefixes collection
     * return true, otherwise return false.
     * 
     * @param str
     * @param prefixes
     *            a collection of String objects
     * 
     * @return if str starts with a prefix which is included in prefixes collection
     *         return true, otherwise return false.
     */
    public static boolean containsPrefix(String str, Collection prefixes) {
        if (str != null && prefixes != null) {
            Iterator it = prefixes.iterator();
            while (it.hasNext()) {
                String prefix = (String) it.next();
                if (str.startsWith(prefix)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Get the next order id according to the order id of current version id
     * order id is last two characters, they indicate a order numeber
     * eg. 00, 01, 02, ...10, 11, ... 99
     * 
     * @param pattern
     * @return next order id - String
     */
    public static String getNextOrderId(String versionIdWithOrder) {
        String nextOrderId;

        if (versionIdWithOrder == null || versionIdWithOrder.length() <= 0) {
            nextOrderId = "00";
        } else {
            char major = versionIdWithOrder.charAt(versionIdWithOrder.length() - 2);
            char minor = versionIdWithOrder.charAt(versionIdWithOrder.length() - 1);
            // if last number equal 9, the order id need to re-arrenge
            if (minor == '9') {
                minor = '0';
                major++;
            } else {
                minor++;
            }
            nextOrderId = "" + major + minor;
        }

        return nextOrderId;
    }

    /**
     * If code value in range [-64, -17], it's a character header with enconding UTF-8
     * 
     * @param codeValue
     * @return boolean
     */
    private static boolean isCharacterHeaderWithUTF8(int codeValue) {
        if (codeValue >= -64 && codeValue <= -17) {
            return true;
        }
        return false;
    }

    /**
     * If code value in range [-128, -65], it's a character body with enconding UTF-8
     * 
     * @param codeValue
     * @return boolean
     */
    private static boolean isCharacterBodyWithUTF8(int codeValue) {
        if (codeValue >= -128 && codeValue <= -65) {
            return true;
        }
        return false;
    }

    /**
     * cut off string which consists of chinese characters and ascii characters
     * with UTF-8 encoding way
     * 
     * UTF-8 encoding mode
     * 0xxx xxxx [0, 127] for ascii
     * 110x xxxx 10xx xxxx [-33, -64] [-65, -128] for chinese character
     * 1110 xxxx 10xx xxxx 10xx xxxx [-32, -17] [-65, -128] [-65, -128] for chinese character
     * 
     * @param originalStr
     *            - original string
     * @param length
     *            - desired length to shape this character
     * 
     * @return string which has been cut off redundant characters
     *         or null if length given is illegal
     * @throws UnsupportedEncodingException
     */
    public static String cutStringWithUTF8(String originalStr, int length) throws UnsupportedEncodingException {
        String result = null;
        byte[] bytes = originalStr.getBytes("UTF-8");
        if (length <= bytes.length) {
            int codeValue = bytes[length - 1];
            if (logger.isDebugEnabled()) {
                logger.debug("codeValue at last place: " + codeValue);
            }
            if (codeValue > 0) {
                result = new String(bytes, 0, length, "UTF-8");

            } else if (isCharacterHeaderWithUTF8(codeValue)) {
                result = new String(bytes, 0, length - 1, "UTF-8");

            } else if (isCharacterBodyWithUTF8(codeValue)) {
                codeValue = bytes[length - 2];
                if (logger.isDebugEnabled()) {
                    logger.debug("codeValue at the second last place: " + codeValue);
                }

                if (isCharacterHeaderWithUTF8(codeValue)) {
                    result = new String(bytes, 0, length - 2, "UTF-8");

                } else if ((isCharacterBodyWithUTF8(codeValue))) {
                    result = new String(bytes, 0, length, "UTF-8");
                }
            }
        }

        return result;
    }

    public static String cutString(String str, int length, String charsetName) throws UnsupportedEncodingException {
        int len = str.getBytes(charsetName).length;
        if (len > length) {
            char[] chars = str.toCharArray();
            int i = 0;
            for (; i < chars.length; i++) {
                String tstr = new String(chars, 0, i + 1);
                if (tstr.getBytes(charsetName).length > length) {
                    i--;
                    break;
                }
            }
            return new String(chars, 0, i + 1);
        }

        return str;
    }

    /**
     * 防止中文乱码.
     * 
     * @param string
     *            the string
     * @return the string
     */
    public static String encodeUTF8(String string) {

        String str = "";

        try {
            str = new String(string.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(), e);
        }
        return str;
    }

    /**
     * 判断字符是否包含中文
     * @param str
     * @return
     */
    public static boolean hasChinese(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (str.substring(i, i + 1).matches("[\\u4e00-\\u9fa5]+")) {
                return true;
            }
        }
        return false;
    }

    /**  
     * 对象属性转换为字段  例如：userName to user_name  
     * @param property 字段名  
     * @return  
     */
    public static String propertyToField(String property) {
        if (null == property) {
            return "";
        }
        char[] chars = property.toCharArray();
        StringBuffer sb = new StringBuffer();
        for (char c : chars) {
            if (CharUtils.isAsciiAlphaUpper(c)) {
                sb.append("_" + StringUtils.lowerCase(CharUtils.toString(c)));
            } else {
                sb.append(c);
            }
        }
        return sb.toString().toUpperCase();
    }

 
    public static String parseExceptionStackString(Throwable e){
        //处理异常堆栈字符串
        StringWriter strWriter = new StringWriter();
        if (e != null) {
            PrintWriter writer = new PrintWriter(new BufferedWriter(strWriter));
            e.printStackTrace(writer);
            writer.flush();
            strWriter.flush();
        }
        String exceptionStack = strWriter.getBuffer().toString();
        return exceptionStack;
    }
  
}

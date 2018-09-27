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
package com.entdiy.support;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class ChineseToPinyinConvertor {

    private static final Logger logger = LoggerFactory.getLogger(ChineseToPinyinConvertor.class);

    static HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();

    static {
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
    }

    public static enum MODE {
        //大写首字母
        CAPTITAL,
        //全拼
        WHOLE,
        //首字母和全拼都输出
        BOTH
    }

    public static List<String> toPinyinNGram(String chinese) {
        return toPinyinNGram(chinese, MODE.WHOLE);
    }

    public static List<String> toPinyinNGram(String chinese, MODE mode) {
        Set<String> pinyins = toPinyin(chinese, mode);
        List<String> ngrams = new ArrayList();
        for (String pinyin : pinyins) {
            logger.debug(" - pinyin: {}", pinyin);
            pinyin = StringUtils.substringBeforeLast(pinyin, ",");
            String[] words = pinyin.split(",");
            int length = words.length;
            for (int i = 0; i < words.length; i++) {
                StringBuilder sb = new StringBuilder();
                for (int j = 0; j < 3; j++) {
                    if (i + j >= length) {
                        sb.append(words[length - 1]);
                        break;
                    } else {
                        sb.append(words[i + j]);
                    }
                    logger.trace("   - pinyin ngram: {}", sb);
                    ngrams.add(sb.toString());
                }
            }
        }
        return ngrams;
    }

    public static Set<String> toPinyin(String chinese, MODE mode) {
        List<String> pinyins = new ArrayList();
        List<String> pinyinHeads = new ArrayList();
        char[] arr = chinese.toCharArray();
        try {
            for (char one : arr) {
                int listSize = pinyins.size();
                String[] charPinyins = null;
                //System.out.println("Char: " + one);
                CharType charType = checkType(one);
                if (CharType.NUM.equals(charType) || CharType.LETTER.equals(charType)) {
                    charPinyins = new String[]{String.valueOf(one)};
                } else if (CharType.CHINESE.equals(charType)) {
                    charPinyins = PinyinHelper.toHanyuPinyinStringArray(one, defaultFormat);
                }

                if (charPinyins == null || charPinyins.length == 0) {
                    continue;
                }

                int pinyinSize = charPinyins.length;
                if (listSize == 0) {
                    for (int i = 0; i < pinyinSize; i++) {
                        String py = charPinyins[i];
                        pinyins.add(py + ",");
                        pinyinHeads.add(String.valueOf(py.charAt(0)) + ",");
                    }
                } else {
                    if (pinyinSize == 1) {
                        for (int i = 0; i < listSize; i++) {
                            String py = charPinyins[0];
                            pinyins.set(i, pinyins.get(i) + py + ",");
                            pinyinHeads.set(i, pinyinHeads.get(i) + String.valueOf(py.charAt(0) + ","));
                        }
                    } else {
                        List<String> newList = new ArrayList<String>();
                        for (String py : pinyins) {
                            for (String c : charPinyins) {
                                newList.add(py + c + ",");
                            }
                        }
                        pinyins = newList;

                        List<String> newHeadList = new ArrayList<String>();
                        for (String py : pinyinHeads) {
                            for (String c : charPinyins) {
                                newHeadList.add(py + c.charAt(0) + ",");
                            }
                        }
                        pinyinHeads = newHeadList;
                    }
                }
            }

        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
        }

        Set<String> filterDups = new LinkedHashSet();
        if (MODE.CAPTITAL.equals(mode)) {
            filterDups.addAll(pinyinHeads);
            return filterDups;
        } else if (MODE.WHOLE.equals(mode)) {
            filterDups.addAll(pinyins);
            return filterDups;
        } else {
            filterDups.addAll(pinyins);
            filterDups.addAll(pinyinHeads);
            return filterDups;
        }
    }

    enum CharType {
        DELIMITER, //非字母截止字符，例如，．）（　等等　（ 包含U0000-U0080）
        NUM, //2字节数字１２３４
        LETTER, //gb2312中的，例如:ＡＢＣ，2字节字符同时包含 1字节能表示的 basic latin and latin-1 OTHER,// 其他字符
        OTHER, // 其他字符
        CHINESE;//中文字
    }

    /**
     * 判断输入char类型变量的字符类型
     *
     * @param c char类型变量
     * @return CharType 字符类型
     */
    private static CharType checkType(char c) {
        CharType ct = null;

        //中文，编码区间0x4e00-0x9fbb
        if ((c >= 0x4e00) && (c <= 0x9fbb)) {
            ct = CharType.CHINESE;
        }
        //Halfwidth and Fullwidth Forms， 编码区间0xff00-0xffef
        else if ((c >= 0xff00) && (c <= 0xffef)) { //        2字节英文字
            if (((c >= 0xff21) && (c <= 0xff3a)) || ((c >= 0xff41) && (c <= 0xff5a))) {
                ct = CharType.LETTER;
            }
            //2字节数字
            else if ((c >= 0xff10) && (c <= 0xff19)) {
                ct = CharType.NUM;
            }
            //其他字符，可以认为是标点符号
            else {
                ct = CharType.DELIMITER;
            }
        }

        //basic latin，编码区间 0000-007f
        else if ((c >= 0x0021) && (c <= 0x007e)) { //1字节数字
            if ((c >= 0x0030) && (c <= 0x0039)) {
                ct = CharType.NUM;
            } //1字节字符
            else if (((c >= 0x0041) && (c <= 0x005a)) || ((c >= 0x0061) && (c <= 0x007a))) {
                ct = CharType.LETTER;
            }
            //其他字符，可以认为是标点符号
            else {
                ct = CharType.DELIMITER;
            }
        }

        //latin-1，编码区间0080-00ff
        else if ((c >= 0x00a1) && (c <= 0x00ff)) {
            if ((c >= 0x00c0) && (c <= 0x00ff)) {
                ct = CharType.LETTER;
            } else {
                ct = CharType.DELIMITER;
            }
        } else {
            ct = CharType.OTHER;
        }

        return ct;
    }

    public static void main(String[] args) {
        String raw = "第1届摩托车AND自行车赛";
        System.out.println("Chinese: " + raw);
        System.out.println("Pinyin: " + toPinyin(raw, MODE.WHOLE));
        System.out.println("Pinyin NGram: " + toPinyinNGram(raw, MODE.WHOLE));
    }
}
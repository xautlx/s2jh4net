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

import java.util.HashMap;
import java.util.Map;

public class WebFormatter {

    public static String html2text(String html) {
        StringBuffer sb = new StringBuffer(html.length());
        char[] data = html.toCharArray();
        int start = 0;
        boolean previousIsPre = false;
        Token token = null;
        for (;;) {
            token = parse(data, start, previousIsPre);
            if (token == null)
                break;
            previousIsPre = token.isPreTag();
            sb = sb.append(token.getText());
            start += token.getLength();
        }
        return sb.toString().trim();
    }

    private static Token parse(char[] data, int start, boolean previousIsPre) {
        if (start >= data.length)
            return null;
        // try to read next char:
        char c = data[start];
        if (c == '<') {
            // this is a tag or comment or script:
            int endIndex = indexOf(data, start + 1, '>');
            if (endIndex == (-1)) {
                // the left is all text!
                return new Token(Token.TOKEN_TEXT, data, start, data.length, previousIsPre);
            }
            String s = new String(data, start, endIndex - start + 1);
            // now we got s="<...>":
            if (s.startsWith("<!--")) { // this is a comment!
                int endCommentIndex = indexOf(data, start + 1, "-->");
                if (endCommentIndex == (-1)) {
                    // illegal end, but treat as comment:
                    return new Token(Token.TOKEN_COMMENT, data, start, data.length, previousIsPre);
                } else
                    return new Token(Token.TOKEN_COMMENT, data, start, endCommentIndex + 3, previousIsPre);
            }
            String sLowerCase = s.toLowerCase();
            if (sLowerCase.startsWith("<script")) { // this is a script:
                int endScriptIndex = indexOf(data, start + 1, "</script>");
                if (endScriptIndex == (-1))
                    // illegal end, but treat as script:
                    return new Token(Token.TOKEN_SCRIPT, data, start, data.length, previousIsPre);
                else
                    return new Token(Token.TOKEN_SCRIPT, data, start, endScriptIndex + 9, previousIsPre);
            } else { // this is a tag:
                return new Token(Token.TOKEN_TAG, data, start, start + s.length(), previousIsPre);
            }
        }
        // this is a text:
        int nextTagIndex = indexOf(data, start + 1, '<');
        if (nextTagIndex == (-1))
            return new Token(Token.TOKEN_TEXT, data, start, data.length, previousIsPre);
        return new Token(Token.TOKEN_TEXT, data, start, nextTagIndex, previousIsPre);
    }

    private static int indexOf(char[] data, int start, String s) {
        char[] ss = s.toCharArray();
        // TODO: performance can improve!
        for (int i = start; i < (data.length - ss.length); i++) {
            // compare from data[i] with ss[0]:
            boolean match = true;
            for (int j = 0; j < ss.length; j++) {
                if (data[i + j] != ss[j]) {
                    match = false;
                    break;
                }
            }
            if (match)
                return i;
        }
        return (-1);
    }

    private static int indexOf(char[] data, int start, char c) {
        for (int i = start; i < data.length; i++) {
            if (data[i] == c)
                return i;
        }
        return (-1);
    }

}

class Token {

    public static final int TOKEN_TEXT = 0; // html text.
    public static final int TOKEN_COMMENT = 1; // comment like <!-- comments... -->
    public static final int TOKEN_TAG = 2; // tag like <pre>, <font>, etc.
    public static final int TOKEN_SCRIPT = 3;

    private static final char[] TAG_BR = "<br".toCharArray();
    private static final char[] TAG_P = "<p".toCharArray();
    private static final char[] TAG_LI = "<li".toCharArray();
    private static final char[] TAG_PRE = "<pre".toCharArray();
    private static final char[] TAG_HR = "<hr".toCharArray();

    private static final char[] END_TAG_TD = "</td>".toCharArray();
    private static final char[] END_TAG_TR = "</tr>".toCharArray();
    private static final char[] END_TAG_LI = "</li>".toCharArray();

    private static final Map<String, String> SPECIAL_CHARS = new HashMap<String, String>();

    private int type;
    private String html; // original html
    private String text = null; // text!
    private int length = 0; // html length
    private boolean isPre = false; // isPre tag?

    static {
        SPECIAL_CHARS.put("&quot;", "\"");
        SPECIAL_CHARS.put("&lt;", "<");
        SPECIAL_CHARS.put("&gt;", ">");
        SPECIAL_CHARS.put("&amp;", "&");
        SPECIAL_CHARS.put("&reg;", "(r)");
        SPECIAL_CHARS.put("&copy;", "(c)");
        SPECIAL_CHARS.put("&nbsp;", " ");
        SPECIAL_CHARS.put("&pound;", "?");
    }

    public Token(int type, char[] data, int start, int end, boolean previousIsPre) {
        this.type = type;
        this.length = end - start;
        this.html = new String(data, start, length);
        //System.out.println("[Token] html=" + html + ".");
        parseText(previousIsPre);
        //System.out.println("[Token] text=" + text + ".");
    }

    public int getLength() {
        return length;
    }

    public boolean isPreTag() {
        return isPre;
    }

    private void parseText(boolean previousIsPre) {
        if (type == TOKEN_TAG) {
            char[] cs = html.toCharArray();
            if (compareTag(TAG_BR, cs) || compareTag(TAG_P, cs))
                text = "\n";
            else if (compareTag(TAG_LI, cs))
                text = "\n* ";
            else if (compareTag(TAG_PRE, cs))
                isPre = true;
            else if (compareTag(TAG_HR, cs))
                text = "\n--------\n";
            else if (compareString(END_TAG_TD, cs))
                text = "\t";
            else if (compareString(END_TAG_TR, cs) || compareString(END_TAG_LI, cs))
                text = "\n";
        }
        // text token:
        else if (type == TOKEN_TEXT) {
            text = toText(html, previousIsPre);
        }
    }

    public String getText() {
        return text == null ? "" : text;
    }

    private String toText(String html, final boolean isPre) {
        char[] cs = html.toCharArray();
        StringBuffer buffer = new StringBuffer(cs.length);
        int start = 0;
        boolean continueSpace = false;
        char current, next;
        for (;;) {
            if (start >= cs.length)
                break;
            current = cs[start]; // read current char
            if (start + 1 < cs.length) // and next char
                next = cs[start + 1];
            else
                next = '\0';
            if (current == ' ') {
                if (isPre || !continueSpace)
                    buffer = buffer.append(' ');
                continueSpace = true;
                // continue loop:
                start++;
                continue;
            }
            // not ' ', so:
            if (current == '\r' && next == '\n') {
                if (isPre)
                    buffer = buffer.append('\n');
                // continue loop:
                start += 2;
                continue;
            }
            if (current == '\n' || current == '\r') {
                if (isPre)
                    buffer = buffer.append('\n');
                // continue loop:
                start++;
                continue;
            }
            // cannot continue space:
            continueSpace = false;
            if (current == '&') {
                // maybe special char:
                int length = readUtil(cs, start, ';', 10);
                if (length == (-1)) { // just '&':
                    buffer = buffer.append('&');
                    // continue loop:
                    start++;
                    continue;
                } else { // check if special character:
                    String spec = new String(cs, start, length);
                    String specChar = (String) SPECIAL_CHARS.get(spec);
                    if (specChar != null) { // special chars!
                        buffer = buffer.append(specChar);
                        // continue loop:
                        start += length;
                        continue;
                    } else { // check if like '&#1234':
                        if (next == '#') { // maybe a char
                            String num = new String(cs, start + 2, length - 3);
                            try {
                                int code = Integer.parseInt(num);
                                if (code > 0 && code < 65536) { // this is a special char:
                                    buffer = buffer.append((char) code);
                                    // continue loop:
                                    start++;
                                    continue;
                                }
                            } catch (Exception e) {
                            }
                            // just normal char:
                            buffer = buffer.append("&#");
                            // continue loop:
                            start += 2;
                            continue;
                        } else { // just '&':
                            buffer = buffer.append('&');
                            // continue loop:
                            start++;
                            continue;
                        }
                    }
                }
            } else { // just a normal char!
                buffer = buffer.append(current);
                // continue loop:
                start++;
                continue;
            }
        }
        return buffer.toString();
    }

    // read from cs[start] util meet the specified char 'util',
    // or null if not found:
    private int readUtil(final char[] cs, final int start, final char util, final int maxLength) {
        int end = start + maxLength;
        if (end > cs.length)
            end = cs.length;
        for (int i = start; i < start + maxLength; i++) {
            if (cs[i] == util) {
                return i - start + 1;
            }
        }
        return (-1);
    }

    // compare standard tag "<input" with tag "<INPUT value=aa>"
    private boolean compareTag(final char[] oriTag, char[] tag) {
        if (oriTag.length >= tag.length)
            return false;
        for (int i = 0; i < oriTag.length; i++) {
            if (Character.toLowerCase(tag[i]) != oriTag[i])
                return false;
        }
        // the following char should not be a-z:
        if (tag.length > oriTag.length) {
            char c = Character.toLowerCase(tag[oriTag.length]);
            if (c < 'a' || c > 'z')
                return true;
            return false;
        }
        return true;
    }

    private boolean compareString(final char[] ori, char[] comp) {
        if (ori.length > comp.length)
            return false;
        for (int i = 0; i < ori.length; i++) {
            if (Character.toLowerCase(comp[i]) != ori[i])
                return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return html;
    }
}

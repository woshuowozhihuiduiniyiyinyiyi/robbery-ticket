package com.hj.tj.gohome.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.*;

/**
 * DFA 算法实现敏感词过滤
 *
 * @author tangj
 * @since 2019/1/4 15:01
 */
@Slf4j
public class SensitiveWordUtil {

    private static final String SENSITIVE_WORD_FILE_PATH = "sensitiveword.txt";
    private static final String DEFAULT_REPLACE_CHAR = "*";
    private static Map<String, Object> sensitiveWordMap;
    private static String IS_END = "isEnd";

    static {
        try {
            InputStream stream = ClassUtils.class.getClassLoader().getResourceAsStream(SENSITIVE_WORD_FILE_PATH);
            List<String> strs = IOUtils.readLines(stream, "utf-8");
            sensitiveWordMap = new HashMap<>(strs.size());
            for (String sensitiveWord : strs) {
                Map<String, Object> nowMap = sensitiveWordMap;
                for (int i = 0; i < sensitiveWord.length(); i++) {
                    char ch = sensitiveWord.charAt(i);

                    Object obj = nowMap.get(String.valueOf(ch));
                    if (Objects.nonNull(obj)) {
                        nowMap = (Map<String, Object>) obj;
                    } else {
                        Map<String, Object> newMap = new HashMap<>();
                        newMap.put(IS_END, false);

                        nowMap.put(String.valueOf(ch), newMap);
                        nowMap = newMap;
                    }

                    if (i == sensitiveWord.length() - 1) {
                        nowMap.put(IS_END, true);
                    }
                }
            }

        } catch (FileNotFoundException e) {
            log.error("敏感词文件不存在，文件路径：" + SENSITIVE_WORD_FILE_PATH);
        } catch (IOException e) {
            log.error("读取文件失败：" + SENSITIVE_WORD_FILE_PATH);
        }
    }


    private SensitiveWordUtil() {
    }

    /**
     * 检测是text 中否包含敏感词，从sensitiveWordSet 下标开始检测，返回最大匹配长度
     *
     * @param text       检测的字符串
     * @param beginIndex 开始检测的下标
     * @return 包含敏感词长度，不存在则返回0
     */
    public static int check(String text, int beginIndex) {
        boolean flag = false;
        int matchLength = 0;
        Map nowMap = sensitiveWordMap;
        for (int i = beginIndex; i < text.length(); i++) {
            char ch = text.charAt(i);

            Map map = (Map) nowMap.get(String.valueOf(ch));
            if (Objects.isNull(map)) {
                map = (Map) nowMap.get(String.valueOf(ch).toLowerCase());
            }

            nowMap = map;
            if (Objects.nonNull(nowMap)) {
                matchLength++;
                if (Objects.equals(nowMap.get(IS_END), true)) {
                    flag = true;
                }
            } else {
                break;
            }
        }

        if (matchLength < 2 || !flag) {
            matchLength = 0;
        }

        return matchLength;
    }

    /**
     * 获取txt 包含的所有的敏感词Set
     *
     * @param txt 检测的字符串
     * @return 字符串包含的敏感Set
     */
    public static Set<String> get(String txt) {
        Set<String> sensitiveWordSet = new HashSet<>();

        for (int i = 0; i < txt.length(); i++) {
            int length = check(txt, i);

            if (length > 0) {
                sensitiveWordSet.add(txt.substring(i, i + length));
                i = i + length - 1;
            }
        }

        return sensitiveWordSet;
    }

    /**
     * 替换敏感词为相应字符，txt 为需要替换的字符串，replaceChar 替换的字符串
     *
     * @param txt         需要替换的字符串
     * @param replaceChar 替换的字符串
     * @return 替换后字符串
     */
    public static String replace(String txt, String replaceChar) {
        String resultTxt = txt;
        Set<String> set = get(txt);

        for (String word : set) {
            String replaceString = getReplaceChars(replaceChar, word.length());
            resultTxt = resultTxt.replaceAll(word, replaceString);
        }

        return resultTxt;
    }

    /**
     * 替换敏感词为*
     *
     * @param txt 需要替换的内容
     * @return 替换后的结果
     */
    public static String replace(String txt) {
        String resultTxt = txt;

        String delSpecialCharStr = delSpecialChar(txt);

        Set<String> set = get(delSpecialCharStr);
        if (!CollectionUtils.isEmpty(set)) {
            resultTxt = delSpecialCharStr;
        }

        for (String word : set) {
            String replaceString = getReplaceChars(DEFAULT_REPLACE_CHAR, word.length());
            resultTxt = resultTxt.replaceAll(word, replaceString);
        }

        return resultTxt;
    }

    /**
     * 产生替换字符串，如replaceChar=*，length=3，则返回：***
     *
     * @param replaceChar 替换的字符
     * @param length      需要替换字符串的长度
     * @return 替换字符串
     */
    private static String getReplaceChars(String replaceChar, int length) {
        StringBuilder resultReplace = new StringBuilder(replaceChar);
        for (int i = 1; i < length; i++) {
            resultReplace.append(replaceChar);
        }

        return resultReplace.toString();
    }

    private static String delSpecialChar(String txt) {
        if (StringUtils.isEmpty(txt)) {
            return txt;
        }

        char[] chars = txt.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if ((chars[i] >= 19968 && chars[i] <= 40869) || (chars[i] >= 97 && chars[i] <= 122)
                    || (chars[i] >= 65 && chars[i] <= 90) || (chars[i] >= 48 && chars[i] <= 57)) {
                continue;
            }

            chars[i] = ' ';
        }

        String str = new String(chars);
        return str.replaceAll(" ", "");
    }

}

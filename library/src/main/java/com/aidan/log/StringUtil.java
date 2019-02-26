package com.aidan.log;

import java.text.DecimalFormat;

/**
 *
 * @author SeungUp. Park
 * @version : 1.0
 */
public class StringUtil {

    ///////////////////////////////////////////////////////////////////////////////
    //                                                                           //
    //                                 문자열 검색                                //
    //                                                                           //
    ///////////////////////////////////////////////////////////////////////////////
    /**
     * <p>문자열 검색
     *
     * @param value  검색대상 문자열
     * @param search 검색어
     * @return 검색대상 문자열이 검색어(초성)을 포함한 경우 포함된 부분.
     */
    public static String getMatchString(String value, String search) {
        if (value == null || search == null)
            return null;

        int t = 0;
        int seof = value.length() - search.length();
        int slen = search.length();
        if (seof < 0)
            return null; // 검색어가 더 길면 false를 리턴한다.
        for (int i = 0; i <= seof; i++) {
            t = 0;
            StringBuffer sb = new StringBuffer();
            while (t < slen) {
                char c = value.charAt(i + t);
                if (isInitialSound(search.charAt(t)) == true && isKorean(c)) {
                    // 만약 현재 char이 초성이고 value가 한글이면
                    if (getInitialSound(c) == search.charAt(t)) {
                        // 각각의 초성끼리 같은지 비교한다
                        t++;
                        sb.append(c);
                    } else {
                        break;
                    }
                } else {
                    // char이 초성이 아니라면
                    if (("" + c).equalsIgnoreCase("" + search.charAt(t))) {
                        // 그냥 같은지 비교한다.
                        t++;
                        sb.append(c);
                    } else {
                        break;
                    }
                }
            }
            if (t == slen)
                return sb.toString(); // 모두 일치한 결과를 찾으면 true를 리턴한다.
        }
        return null; // 일치하는 것을 찾지 못했으면 false를 리턴한다.
    }

    /**
     * <p>해당 문자가 한글 초성인지 검사.
     *
     * @param searchar
     * @return 한글 초성인경우 true.
     */
    public static boolean isInitialSound(char searchar) {
        for (char c : KOREAN_INITIAL) {
            if (c == searchar) {
                return true;
            }
        }
        return false;
    }

    /**
     * <p>해당 문자의 자음을 얻는다.
     *
     * @param c 검사할 문자
     * @return
     */
    public static char getInitialSound(char c) {
        try {
            return KOREAN_INITIAL[(c - KOREAN_UNICODE_START) / KOREAN_UNIT];
        } catch (ArrayIndexOutOfBoundsException e) {
            return 0;
        }
    }

    private final static char[] KOREAN_INITIAL = {'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'};
    private final static char KOREAN_UNIT = '까' - '가';


    ///////////////////////////////////////////////////////////////////////////////
    //                                                                           //
    //                              Korean Dictinary                            //
    //                                                                           //
    ///////////////////////////////////////////////////////////////////////////////

    private final static char KOREAN_UNICODE_START = '가';
    private final static char KOREAN_UNICODE_END = '힣';

    /**
     * <p>해당 문자가 한글인지 검사.
     *
     * @param c
     * @return 한글인경우 true.
     */
    public static boolean isKorean(char c) {
        if (c >= KOREAN_UNICODE_START && c <= KOREAN_UNICODE_END)
            return true;
        return false;
    }

    /**
     * 숫자에 천단위 콤마 찍기
     * @param num
     * @return
     */
    public static String decimalFormat(int num) {
        DecimalFormat df = new DecimalFormat("#,###");
        return df.format(num);
    }
}

package com.questionnaire.util;

/**
 * Created by HY
 * 2018/12/27 10:56
 */
@SuppressWarnings("unused")
public class PrintUtil {

    @SuppressWarnings("FieldCanBeLocal")
    private static boolean isPrint = false;

    public static void println(String content){
        if (isPrint) {
            System.out.println(content);
        }
    }

}

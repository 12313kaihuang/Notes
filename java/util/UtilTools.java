package com.questionnaire.util;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by HY
 * 2018/12/27 15:44
 */
public class UtilTools {

    public static String getCurrentTime() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        return sdf.format(date);
    }

    public static Collection<Integer> parseInt(String key) {
        Collection<Integer> collection = new ArrayList<>();
        String[] split = key.split(",");
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        for (String s : split) {
            Matcher m = p.matcher(s);
            int i = Integer.parseInt(m.replaceAll("").trim());
            collection.add(i);
        }
        return collection;
    }
}

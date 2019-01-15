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

    //获取当前时间
    public static String getCurrentTime() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        return sdf.format(date);
    }

    //正则匹配筛选数字  eg:key=["12","34","56"]  返回的Collection适用于JPA的集合查询
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
    
    //生成一个随机的六位数验证码
    public static String getRandomVerificationCode(){
        char[] verificationCode = new char[6];
        for (int i = 0; i < 6; i++) {
            int num = new Random().nextInt(10);
            verificationCode[i] = (char) (num+48);   //ASCII码0->48
        }
        return String.valueOf(verificationCode);
    }
    
    //将手机号中间四位变成*号
    private static final String FORMAT_PHONE_REGEX = "(\\d{3})\\d{4}(\\d{4})";
    public static String dimPhoneNumber(String phoneNumber){
        return phoneNumebr.replaceAll(FORMAT_PHONE_REGEX,"$1****$2"));
    }
}

package com.questionnaire.util;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Created by HY
 * 2018/12/27 12:59
 */

@SuppressWarnings({"WeakerAccess", "unused"})
@Component
public class CookieUtils {

    public static final int COOKIE_MAX_AGE = 7 * 24 * 3600;
    public static final int COOKIE_HALF_HOUR = 30 * 60;


    private static HttpServletResponse response;

    private final HttpServletResponse response2;

    private static HttpServletRequest request;

    private final HttpServletRequest request2;

    @Autowired
    public CookieUtils(HttpServletResponse response2, HttpServletRequest request2) {
        this.response2 = response2;
        this.request2 = request2;
    }

    @PostConstruct
    public void beforeInit() {
        request=request2;
        response=response2;
    }


    /**
     *
     *
     * @return 根据Cookie名称得到Cookie对象，不存在该对象则返回Null
     */
    public static Cookie getCookie(String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies==null||cookies.length<1) {
            return null;
        }
        Cookie cookie = null;
        for (Cookie c : cookies) {
            if (name.equals(c.getName())) {
                cookie = c;
                break;
            }
        }
        return cookie;
    }

    /**
     *
     *
     * @return 根据Cookie名称直接得到Cookie值
     */
    public static String getCookieValue(String name) {
        Cookie cookie = getCookie(name);
        if(cookie != null){
            return cookie.getValue();
        }
        return null;
    }

    /**
     * 移除cookie
     * @param name 这个是名称，不是值
     */
    public static void removeCookie(String name) {
        if (null == name) {
            return;
        }
        Cookie cookie = getCookie(name);
        if(null != cookie){
            cookie.setPath("/");
            cookie.setValue("");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
    }

    /**
     * 添加一条新的Cookie，可以指定过期时间(单位：秒)
     *
     * @param name name
     * @param value value
     * @param maxValue 过期时间(单位：秒)
     */
    public static void setCookie(String name,
                                 String value, int maxValue) {
        if (StringUtils.isEmpty(name)) {
            return;
        }
        if (null == value) {
            value = "";
        }
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        if (maxValue != 0) {
            cookie.setMaxAge(maxValue);
        } else {
            cookie.setMaxAge(COOKIE_HALF_HOUR);
        }
        response.addCookie(cookie);
//        try {
//            response.flushBuffer();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    /**
     * 添加一条新的Cookie，默认30分钟过期时间
     *
     * @param name name
     * @param value value
     */
    public static void setCookie(String name,
                                 String value) {
        setCookie(name, value, COOKIE_HALF_HOUR);
    }

    /**
     * 将cookie封装到Map里面
     * @return 所有的cookie
     */
    public static Map<String,Cookie> getCookieMap(){
        Map<String,Cookie> cookieMap = new HashMap<>();
        Cookie[] cookies = request.getCookies();
        if(cookies!=null&&cookies.length>1){
            for(Cookie cookie : cookies){
                cookieMap.put(cookie.getName(), cookie);
            }
        }
        return cookieMap;
    }

}

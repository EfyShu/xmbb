package com.efy.util;

/**
 * @Project xmbb
 * @Date 2020/9/13 20:35
 * @Created by Efy
 * @Description TODO
 */
public class StringUtil {
    public static String byteToString(byte b){
        return fillToLen(new String(new byte[]{b}),8);
    }

    public static String fillToLen(String str,int len){
        return fillToLen(str,len,"0");
    }

    public static String fillToLen(String str,int len,String fill){
        if(str == null) return null;
        if(str.length() >= len) return str;
        StringBuffer sb = new StringBuffer();
        for(int i=0;i<len - str.length();i++){
            sb.append(fill);
        }
        sb.append(str);
        return sb.toString();
    }
}

package com.bewd.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 */
public class StringUtil {

    /**
     * 默认时间格式
     */
    public final static String DEFAULT_TIME_FORMAT = "yyyy-MM-dd HH:mm";

    public final static String DATE_FORMAT = "yyyy-MM-dd";

    public final static String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public final static String UN_DATE_FORMAT = "yyyyMMdd";
    public final static String UN_DATE_TIME_FORMAT = "yyyyMMddHHmmss";

    public final static String DATE_TIME_MILLISECOND_FORMAT = "yyyyMMddHHmmssSSS";
    private static Pattern humpPattern = Pattern.compile("\\B(\\p{Upper})(\\p{Lower}*)");
    /**
     * wulianhan
     * 数字金额校验
     */
    public static boolean isNumber(String str) {
        // 判断小数点后2位的数字的正则表达式
        String reg = "^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$";
        Pattern pattern = Pattern.compile(reg);
        Matcher match = pattern.matcher(str.trim());
        return match.matches();
    }

    /**
     * wulianhan
     * 日期格式校验（yyyy-MM-dd）
     */
    public static boolean checkDate(String str) {
        String reg = "(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)";
        Pattern p = Pattern.compile(reg);
        return p.matcher(str).matches();
    }

    /**
     * 格式化当前时间
     *
     * @return
     */
    public static String formatNowDate(String format) {
        return formatDate(getNowDate(), format);
    }

    /**
     * 将对象转换为json字符串
     *
     * @param obj
     * @return
     */
    public static String parseObj2JsonStr(Object obj) {
        if (null == obj) {
            return null;
        }
        if (obj instanceof String) {
            return obj.toString();
        }
        return JSONObject.toJSONString(obj);
    }

    /**
     * 将json字符串转换为JSONObject
     *
     * @param str
     * @return
     */
    public static JSONObject parseStr2Json(String str) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        return JSONObject.parseObject(str);
    }

    /**
     * 将json字符串转换为对象
     *
     * @param str
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> T parseStr2Obj(String str, Class<T> tClass) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        return JSONObject.parseObject(str, tClass);
    }

    /**
     * 解决sendDirect路径参数中文乱码问题
     *
     * @param str
     * @return
     */
    public static String convertToUTF8(String str) {
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 把当前类和父类所有属性转换成路径
     *
     * @param obj 当前类
     * @return 路径
     */
    public static String classToPathStr(Object obj) {
        String path = "?";
        List<Field> fieldList = new ArrayList<>();
        Class tempClass = obj.getClass();
        // 当父类为null的时候说明到达了最上层的父类(Object类).
        while (tempClass != null) {
            fieldList.addAll(Arrays.asList(tempClass.getDeclaredFields()));
            tempClass = tempClass.getSuperclass();
        }

        for (Field field : fieldList) {
            try {
                field.setAccessible(true);
                if (field.get(obj) != null) {
                    path += field.getName() + "=" + field.get(obj) + "&";
                }
            } catch (Exception e) {
            }
        }
        return path;
    }

    /**
     * 格式化日期
     *
     * @param src
     * @param format
     * @return
     */
    public static String formatDate(Date src, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);
        try {
            return sdf.format(src);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 时间字符串转换为Date
     *
     * @param src
     * @param format
     * @return
     */
    public static Date parseDate(String src, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);
        try {
            return sdf.parse(src);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static Date getNowDate() {
        return new Date();
    }

    /**
     * 生成随机数字
     *
     * @param len 长度
     * @return
     */
    public static String getRandomNum(int len) {
        String nums = "1234567890";
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < len; i++) {
            sb.append(nums.charAt((int) (Math.random() * nums.length())));
        }
        return sb.toString();
    }

    /**
     * 将字符串首字母大写
     *
     * @param str
     * @return
     */
    public static String upperHeader(String str) {
        String reg = "_(\\w{1})";
        str = str.substring(0, 1).toUpperCase() + str.substring(1);
        StringBuffer sb = new StringBuffer();
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(str);
        while (m.find()) {
            m.appendReplacement(sb, m.group(1).toUpperCase());
        }
        m.appendTail(sb);
        return sb.toString();
    }

    /**
     * 当前时间超过1小时，且与当前时间为同一天，则显示时间格式为：上午hh：mm或下午hh：mm，
     * 以中午12点为分隔，据当前时间跨日，则显示时间格式为：yyyy-mm-dd hh：mm
     *
     * @param releasetime 格式必须为yyyy-MM-dd HH:mm
     * @param nowDate
     * @return
     */
    public static String formatIntervalTime(String releasetime, Date nowDate) {
        Date releaseDate = parseDate(releasetime, DEFAULT_TIME_FORMAT);
        long time = releaseDate.getTime();
        long nowTime = nowDate.getTime();
        // 间隔时间
        long interval = (nowTime - time) / 1000 / 60;
        if (interval < 0) {
            return releasetime;
        }
        if (interval == 0) {
            return "1分钟之前";
        }
        if (interval <= 60) {
            return interval + "分钟之前";
        }
        String nowDay = formatDate(nowDate, "yyyy-MM-dd");
        if (releasetime.substring(0, 10).equals(nowDay)) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(releaseDate);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            String hourStr = hour + "";
            if (hour < 10) {
                hourStr = "0" + hourStr;
            }
            int minute = calendar.get(Calendar.MINUTE);
            String minuteStr = minute + "";
            if (minute < 10) {
                minuteStr = "0" + minuteStr;
            }
            return hourStr + ":" + minuteStr;
        }
        return releasetime;
    }

    /**
     * 当前时间加上一定分钟后
     *
     * @param minute
     * @return
     */
    public static Date afterMinuteDate(int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getNowDate());
        calendar.add(Calendar.MINUTE, minute);
        return calendar.getTime();
    }

    /**
     * 文字模版替换
     *
     * @param msg
     * @param param
     * @return
     */
    public static String replaceContent(String msg, Map<String, String> param) {
        if (StringUtils.isEmpty(msg) || null == param || param.isEmpty()) {
            return msg;
        }
        for (Map.Entry<String, String> entry : param.entrySet()) {
            if (StringUtils.isNotEmpty(entry.getKey()) && StringUtils.isNotEmpty(entry.getValue())) {
                msg = msg.replace("{" + entry.getKey() + "}", entry.getValue());
            }
        }
        return msg;
    }

    /**
     * 拼接list
     *
     * @param list
     * @return
     */
    public static String formatList(List<String> list) {
        if (null == list || list.isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (String s : list) {
            if (StringUtils.isNotEmpty(s)) {
                sb.append(s);
            }
            sb.append(",");
        }
        return sb.substring(0, sb.length() - 1);
    }

    /**
     * 四舍五入数字
     *
     * @param src
     * @param count
     * @return
     */
    public static BigDecimal formatNum(BigDecimal src, int count) {
        // 防止double类型小数溢出
        src.add(new BigDecimal(0.0000000001));
        return src.setScale(count, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 通过get方法获取字段名
     *
     * @return
     */
    public static String methodToField(String methodName) {
        methodName = methodName.substring(0, 4).toLowerCase() + methodName.substring(4);
        return methodName.replace("get", "");
    }

    /**
     * 判断是否为空
     *
     * @param obj
     * @return true-空 false-非空
     */
    public static boolean isNull(Object obj) {
        if (obj instanceof List) {
            return obj == null || (obj != null && ((List) obj).size() == 0);
        } else {
            return obj == null || "".equals(obj.toString());
        }

    }

    /**
     * 功能：判断字符串是否为正整数
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }


    /**
     * 计算执行时间
     * @param start
     * @return 时间
     */
    public static String calculation(long start) {
        String res = "";
        long time = System.currentTimeMillis() - start;
        if (time == 0){
            return "少于 1 毫秒";
        }
        int d = (int) (time / (24 * 3600 * 1000));
        int h = (int) ((time / 3600000) % 24);
        int m = (int) ((time / 60000) % 60);
        int s = (int) ((time / 1000) % 60);
        int ms = (int) (time % 1000);

        res = (d > 0 ? d + " 天 " : "") + (h > 0 ? h + " 小时 " : "")
                + (m > 0 ? m + " 分 " : "") + (s > 0 ? s + " 秒 " : "")
                + (ms > 0 ? ms + " 毫秒" : "");
        return res;
    }

    /**
     * 获取指定对象中指定字段的内容（通过映射）
     * @param source
     * @param fieldName
     * @param targetValueClass
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T getFieldsValueFromData(Object source,String fieldName,T targetValueClass) throws Exception{
        if(null == source || null == fieldName || "".equals(fieldName)){
            return null;
        }
        String getMethodName = "get"+StringUtil.upperHeader(fieldName);
        try {
            Method getMethod = source.getClass().getDeclaredMethod(getMethodName);
            getMethod.setAccessible(true);
            targetValueClass = (T)getMethod.invoke(source);
        } catch (NoSuchMethodException e) {
            //当找不到方法时，返回null
            return null;
        }
        return targetValueClass;
    }
    //用于查找${}格式数据的正则表达式
    public static final String DATA_REGEX = "\\$\\{[^}${]*\\}";

    /**
     * 对字符串进行正则计算，查找${*}格式的数据并整合成集合返回
     * @param str
     * @return
     * @throws Exception
     */
    public static List<String> getParamFromStr(String str)throws Exception{
        List<String> result = new ArrayList<>();
        Pattern p = Pattern.compile(DATA_REGEX);
        Matcher m = p.matcher(str);
        while (m.find()) {
            result.add(m.group());
        }
        return result;
    }
    /**
     * 对字符串进行正则计算，查找${*}格式的数据并整合成集合返回
     * @param str
     * @return
     * @throws Exception
     */
    public static List<String> getParamFromStrWithoutOperator(String str)throws Exception{
        List<String> result = new ArrayList<>();
        Pattern p = Pattern.compile(DATA_REGEX);
        Matcher m = p.matcher(str);
        String param_name = "";
        while (m.find()) {
            param_name = m.group();
            if (null != param_name) {
                //去除所有${}符号
                param_name = param_name.replaceAll("[\\$\\{\\}]", "");
            }
            result.add(param_name);
        }
        return result;
    }
    /**
     * 如果对象为空，返回""，避免空指针
     *
     * @param obj
     * @return
     */
    public static String StrNullConvert(Object obj) {
        if (null == obj) {
            return "";
        } else {
            String str = obj.toString().trim().replace("'", "\"");
            return str.replace("\r\n", " ");
        }
    }
    /**
     * 将字符串当做逻辑表达式进行执行
     *
     * @param elStr 逻辑表达式的字符串
     * @param map   逻辑表达式中参数变量的对应集合
     * @return
     * @throws Exception
     */
    public static boolean runEl(String elStr, Map<String, Object> map) throws Exception {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        Iterator<String> it = map.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            engine.put(key, map.get(key));
        }
        return (Boolean) engine.eval(elStr);
    }
    /**
     * 将字符串当做逻辑表达式进行执行
     * 无未知变量，直接执行表达式 如： 'aaaa'=='aaaa' && false
     * @param elStr 逻辑表达式的字符串
     * @return
     * @throws Exception
     */
    public static boolean runEl(String elStr) throws Exception {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        return (Boolean) engine.eval(elStr);
    }
    public static String toLine(String camelCase){
        Pattern humpPattern = Pattern.compile("[A-Z]");
        Matcher matcher = humpPattern.matcher(camelCase);
        StringBuffer sb = new StringBuffer();
        while(matcher.find()){
            matcher.appendReplacement(sb, "_"+matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

   public static void main(String[] args) {

        try {
            String aa="fieldName";
           String bb= toLine(aa);
           System.out.println(bb);
//            System.err.println(runEl(" 'aaaa'=='aaaa' && false"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package com.bewd.core;

import com.bewd.annotations.ValidateRule;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 校验规则 返回值为boolean,参数唯一.
 *
 * @author Efy Shu
 * @ClassName ValidMethod
 * @Description 校验规则 返回值为boolean,参数唯一
 * @date 2017年3月7日 下午7:49:21
 */
public class ValidateRules {
    @ApiModelProperty("规则名称（对应方法名）：非空校验")
    public static final String IS_NULL = "isNull";
    @ApiModelProperty("规则名称（对应方法名）：航班号格式校验")
    public static final String IS_FLTNO = "isFltNO";
    @ApiModelProperty("规则名称（对应方法名）：正整数校验")
    public static final String IS_SIGNLESSINT = "isSignlessInt";
    @ApiModelProperty("规则名称（对应方法名）：手机号格式校验")
    public static final String IS_MOBILE = "isMobile";
    @ApiModelProperty("规则名称（对应方法名）：卡号格式校验")
    public static final String IS_BANKNO = "isBankNO";
    @ApiModelProperty("规则名称（对应方法名）：日期格式校验")
    public static final String IS_DATE = "isDate";
    @ApiModelProperty("规则名称（对应方法名）：字段长度校验")
    public static final String IS_Length = "isLength";
    @ApiModelProperty("规则名称（对应方法名）：字段值域范围校验")
    public static final String IS_Range = "isRange";
    @ApiModelProperty("规则名称（对应方法名）：字段值域范围校验")
    public static final String IS_String_Range = "isStringRange";
    @ApiModelProperty("规则名称（对应方法名）：邮箱格式校验")
    public static final String IS_MAIL = "isMail";

    @ApiModelProperty("数字校验正则表达式Pattern类")
    private static final Pattern NUMBER_PATTERN = Pattern.compile("[0-9]*");
    @ApiModelProperty("证件号校验正则表达式Pattern类")
    private static final Pattern IDCARD_PATTERN = Pattern.compile(
            "[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$|^[1-9]\\d{5}[1-9]"
                    + "\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X|x)");
    @ApiModelProperty("防SQL注入正则表达式Pattern类")
    private static final Pattern SQL_PATTERN = Pattern.compile("and|exec|select|insert|update|delete|count|chr|mid|master|truncate|char|declare|or");
    @ApiModelProperty("防XSS注入正则表达式Pattern类")
    private static final Pattern XSS_PATTERN = Pattern.compile("javascript:|eval\\(|alert\\(|typeof\\(|var |let |for\\(");

    /**
     * 检查是否为空.
     *
     * @param obj 对象
     * @return true-非空 false-空
     */
    @ValidateRule("缺少参数字段【@desc】")
    public boolean isNull(Object obj) {
        return obj != null && !"".equals(obj.toString());
    }

    /**
     * 检查集合是否为空
     * @param obj
     * @return true-非空 false-空
     */
    @ValidateRule("集合缺少参数字段")
    public boolean isMusterNull(Object obj) {
        if(obj != null){
            if(obj instanceof Map){
                Map map = (Map)obj;
                return !map.isEmpty();
            }else if(obj instanceof List){
                List list = (List)obj;
                return !list.isEmpty();
            }else if(obj instanceof Set){
                Set set = (Set)obj;
                return !set.isEmpty();
            }
        }
        return false;
    }

    /**
     * 检测是否为航班号
     *
     * @param fltno
     * @return
     */
    @ValidateRule("请输入正确的航班号")
    public boolean isFltNO(String fltno) {
        if (!isNull(fltno)) {
            return true;
        }
        return fltno.matches("^[\\w\\d]{1,6}$");
    }

    /**
     * 检查是否是正整数
     *
     * @param num
     * @return
     */
    @ValidateRule("请输入正整数,最小为1")
    public boolean isSignlessInt(int num) {
        return num > 0;
    }

    /**
     * 检查是否是正整数__wwl
     *
     * @param num
     * @return
     */
    @ValidateRule("请输入正整数,最小为1,最大为9")
    public boolean isCertNums(int num) {
        return num > 0 && num < 10;
    }

    /**
     * 检查是否是正数
     *
     * @return
     */
    @ValidateRule("请输入正数,最小为0.01")
    public boolean isSignlessNum(double num) {
        return num > 0;
    }

    /**
     * 检查是否是正数
     *
     * @return
     */
    @ValidateRule("请输入正数,最小为0.01")
    public boolean isSignlessNum(float num) {
        return num > 0;
    }

    /**
     * 检查是否是正数
     *
     * @return
     */
    @ValidateRule("请输入正数,最小为0.01")
    public boolean isSignlessNum(BigDecimal num) {
        return num.compareTo(new BigDecimal(0.00)) > 0;
    }

    /**
     * 检查手机号格式是否正确
     *
     * @param mobile
     * @return
     */
    @ValidateRule("请输入正确的手机号")
    public boolean isMobile(String mobile) {
        if (!isNull(mobile)) {
            return true;
        }
        String reg =
                "^1\\d{10}$";
//				"^((13[\\d])|"
//				+ "(14[5|7|9])|"
//				+ "(15[^4,\\D])|"
//				+ "(17[^2,^4,^9,\\D])|"
//				+ "(18[\\d]))"
//				+ "\\d{8}$";
        return mobile != null && mobile.matches(reg);
    }

    /**
     * 检查日期格式是否正确
     *
     * @param dateStr
     * @return
     */
    @ValidateRule("日期格式不正确")
    public boolean isDate(String dateStr) {
        if (!isNull(dateStr)) {
            return true;
        }
        return dateStr.matches("[\\d]{4}-[\\d]{2}-[\\d]{2}" +
                "|[\\d]{4}-[\\d]{2}-[\\d]{2} [\\d]{2}:[\\d]{2}:[\\d]{2}");
    }

    /**
     * 检查字符串是否全部是数字
     *
     * @param str
     * @return
     */
    @ValidateRule("请确保字符串都为数字！")
    public boolean isNumber(String str) {
        if (!isNull(str)) {
            return true;
        }
        Pattern pattern = NUMBER_PATTERN;
        return pattern.matcher(str).matches();
    }

    /**
     * 检查证件号是否正确
     *
     * @param idCard
     * @return
     */
    @ValidateRule("请输入正确的证件号")
    public boolean isIdCard(String idCard) {
        if (!isNull(idCard)) {
            return true;
        }
        Pattern pattern = IDCARD_PATTERN;
        return pattern.matcher(idCard).matches();
    }

    @ValidateRule(value = "数组长度超过限制,只能输入@length项内容",params = {"@this","@length"})
    public boolean isArrLength(Object[] arr,int length){
        return arr.length <= length;
    }

    @ValidateRule(value = "集合长度超过限制,只能输入@length项内容",params = {"@this","@length"})
    public boolean isListLength(List list,int length){
        return list.size() <= length;
    }

    @ValidateRule(value = "字段长度超过限制,只能输入@length个字符",params = {"@this","@length"})
    public boolean isStringLength(String str,int length){
        return str.getBytes().length <= length;
    }

    /**
     * 检查字段是否符合长度
     *
     * @param obj    被校验的字段值
     * @param length 自动填入Valid注解中的length字段
     * @return
     */
    @ValidateRule(value = "字段长度超出限制【@length】,请重新输入！",params = {"@this","@length"})
    public boolean isLength(Object obj, int length) throws IllegalArgumentException {
        if (obj == null) {
            return true;
        } else if (obj instanceof Object[]) {
            return isArrLength((Object[]) obj,length);
        } else if (obj instanceof List) {
            return isListLength((List)obj,length);
        } else if (obj instanceof String) {
            return isStringLength((String) obj,length);
        } else {
            throw new IllegalArgumentException("参数不正确,长度校验仅允许String,List,Object[]使用");
        }
    }

    /**
     * 最小集合校验
     *
     * @param obj
     * @param size
     * @return
     * @throws IllegalArgumentException
     */
    @ValidateRule(value = "字段长度超出限制,请重新输入！",params = {"@this","@length"})
    public boolean minSize(Object obj, int size) throws IllegalArgumentException {
        if (obj == null) {
            return false;
        } else if (obj instanceof List) {
            return ((List) obj).size() >= size;
        } else {
            throw new IllegalArgumentException("参数不正确,最小集合校验仅允许List使用");
        }
    }

    /**
     * 检查字段是否符合范围
     *
     * @param obj   被校验的字段值
     * @param range 自动填入Valid注解中的range字段
     * @return
     */
    @ValidateRule(value = "字段值域不在范围内,请重新输入！",params = {"@this","@range"})
    public boolean isRange(Object obj, String[] range) throws IllegalArgumentException {
        if (obj == null) {
            return true;
        } else if (obj instanceof Object[] && (range.length == 1 && range[0].contains("-"))) {
            int length = ((Object[]) obj).length;
            int min = Integer.valueOf(range[0].split("-")[0]);
            int max = Integer.valueOf(range[0].split("-")[1]);
            return length >= min && length <= max;
        } else if (obj instanceof Object[] && range.length >= 1) {
            for (Object o : (Object[]) obj) {
                boolean flag = false;
                for (String ruleRange : range) {
                    if (ruleRange.equals(String.valueOf(o))) {
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    return false;
                }
            }
            return true;
        } else if (obj instanceof List && (range.length == 1 && range[0].contains("-"))) {
            int length = ((List) obj).size();
            int min = Integer.valueOf(range[0].split("-")[0]);
            int max = Integer.valueOf(range[0].split("-")[1]);
            return length >= min && length <= max;
        } else if (obj instanceof List && range.length >= 1) {
            for (Object o : (List) obj) {
                boolean flag = false;
                for (String ruleRange : range) {
                    if (ruleRange.equals(String.valueOf(o))) {
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    return false;
                }
            }
            return true;
        } else if ((obj instanceof String || obj instanceof Integer || obj instanceof Long || obj instanceof Double || obj instanceof Float || obj instanceof BigDecimal)
                && (range.length == 1 && range[0].contains("-"))) {
            int min = Integer.valueOf(range[0].split("-")[0]);
            int max = Integer.valueOf(range[0].split("-")[1]);
            if(obj instanceof String){
                int length = ((String) obj).getBytes().length;
            return length >= min && length <= max;
            }
            if(obj instanceof Integer){
                return (Integer) obj >= min && (Integer) obj <= max;
            }
            if(obj instanceof Long){
                return (Long) obj >= min && (Long) obj <= max;
            }
            if(obj instanceof Double){
                return (Double) obj >= min && (Double) obj <= max;
            }
            if(obj instanceof Float){
                return (Float) obj >= min && (Float) obj <= max;
            }
            if(obj instanceof BigDecimal){
                int minResult = ((BigDecimal) obj).compareTo(BigDecimal.valueOf(min));
                int maxResult = ((BigDecimal) obj).compareTo(BigDecimal.valueOf(max));
                return minResult >= 0 && maxResult <= 0;
            }
        } else if (range.length >= 1 && !range[0].contains("-")) {
            boolean flag = false;
            for (String ruleRange : range) {
                if (ruleRange.equals(String.valueOf(obj))) {
                    flag = true;
                    break;
                }
            }
            return flag;
        }
            throw new IllegalArgumentException("参数不正确,范围校验仅允许int,String,List,Object[]使用");
    }

    /**
     * 检查时间参数是否yyyy-mm-dd格式.
     *
     * @param dataStr 时间
     * @return 校验结果
     */
    @ValidateRule("请输入yyyy-mm-dd时间格式的参数")
    public static boolean dataFormat(String dataStr) {
        if (dataStr == null) {
            return true;
        }
        if (dataStr.length() == 10) {
            //yyyy-mm-dd
            String y = dataStr.substring(4, 5);
            String m = dataStr.substring(7, 8);
            return y.equals("-") && m.equals("-");
        }
        return false;
    }

    public static void main(String[] args) {
        ValidateRules vr = new ValidateRules();
        System.out.println(vr.isIdCard(""));
    }
}

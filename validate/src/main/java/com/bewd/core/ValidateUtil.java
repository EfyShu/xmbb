package com.bewd.core;


import com.bewd.annotations.Valid;
import com.bewd.annotations.ValidateRule;
import io.swagger.annotations.ApiModelProperty;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Efy Shu
 * @ClassName ValidateUtil
 * @Description 校验工具类
 * @date 2017年3月7日 下午6:36:20
 */
public class ValidateUtil {
    /**
     * 字段列表
     * Key : 字段名 Value : 对应字段实体
     */
    private Map<String, FieldBean> validateFields = new HashMap<>();
    /**
     * 校验方法列表
     * Key : 规则名 Value : 对应规则实体
     */
    private Map<String, RuleBean> validateRules = new HashMap<>();
    /**
     * 错误信息(校验失败时会保存相应信息)
     * Key : 字段名 Value : 提示文本
     */
    private Map<String, ErrorBean> errors = new TreeMap<>();
    /**
     * 校验方法类列表(包含校验方法的类)
     */
    private List<Object> rulesClass = new CopyOnWriteArrayList<>();

    /**
     * 需要校验的参数列表(如有值则只校验这些参数对象)
     */
    private String[] requireParam = {};

    /**
     * 排除校验规则名称数组.
     */
    private String[] excludeRules = {};

    /**
     * 是否全部校验 true - 全部校验(有失败也继续) false - 遇失败则终止
     */
    private boolean checkWithError = true;

    public static ValidateUtil build(Object... clazz) {
        ValidateUtil ins = new ValidateUtil();
        ins.addRuleClass(clazz);
        ins.loadRules();
        return ins;
    }

    /**
     * 加载所有规则
     */
    public void loadRules() {
        //初始化校验规则(类中包含ValidateRule注解的方法)
        initRules();
    }

    /**
     * 添加校验元素
     *
     * @param name       name
     * @param value      value
     * @param ownerClass ownerClass
     * @param validRules validRules
     * @param desc       desc
     * @return FieldBean
     */
    public FieldBean addElement(String name, Object value, Object ownerClass, Valid validRules, ApiModelProperty desc) {
        FieldBean field = new FieldBean(name, value, ownerClass, validRules);
        if (desc != null) {
            field.desc = desc.value();
        }
        field.rules = new RuleBean[0];
        //校验规则是否排除（仅针对普通规则，防注入规则不进行校验）
        List<String> excludeRulesList = new ArrayList<>(Arrays.asList(this.excludeRules));
        List<RuleBean> validRulesList = new ArrayList<>();
        //添加规则
        for (String r : validRules.rules()) {
            if (null != excludeRulesList && !excludeRulesList.isEmpty() && excludeRulesList.contains(r)) {
                continue;
            }
            validRulesList.add(validateRules.get(r));
        }
        field.rules = validRulesList.toArray(field.rules);
        //如果字段本身含有替换提示文本
        String[] newMsg = validRules.msg();
        field.errorMsg = new HashMap<>();
        for (String msg : newMsg) {
            String rule = msg.split(":")[0],
                    tip = msg.substring(msg.indexOf(":")>0?msg.indexOf(":")+1:0);
            field.errorMsg.put(rule, tip);
        }
        String fieldName = ownerClass.getClass().getSimpleName() + "." + name;
        if (validateFields.containsKey(fieldName)) {
            int p = 0;
            for (String key : validateFields.keySet()) {
                if (key.startsWith(fieldName)) {
                    p++;
                }
            }
            fieldName += "$" + p;
        }
        validateFields.put(fieldName, field);
        return field;
    }

    /**
     * 注册校验规则
     *
     * @param ruleMethod 方法
     * @param ownerClass 方法所属Class
     * @param rule       规则注解
     */
    public void addRule(Method ruleMethod, Object ownerClass, ValidateRule rule) {
        RuleBean bean = new RuleBean(ruleMethod, ownerClass, rule);
        validateRules.put(bean.name, bean);
    }

    /**
     * 注册校验规则类
     *
     * @param clazz 包含校验规则的类
     */
    public void addRuleClass(Object... clazz) {
        if (clazz == null || clazz.length <= 0) {
            return;
        }
        for (Object clz : clazz) {
            //不添加重复规则,重复规则放末尾
            rulesClass.remove(clz);
            rulesClass.add(clz);
        }
    }

    /**
     * 校验目标元素
     *
     * @param target
     * @return
     */
    public boolean valid(Object target) {
        //系统校验规则为空,初始化
        if (validateRules.isEmpty()) {
            loadRules();
        }
        //初始化目标(实体中包含@Valid注解的字段)
        initTargetField(target);
        boolean checkResult = false;
        //如果请求参数列表为空则校验全部元素
        if (requireParam == null || requireParam.length == 0) {
            for (String eleName : validateFields.keySet()) {
                checkResult = checkElement(eleName);
                if(!checkWithError && !checkResult){
                    break;
                }
            }
        //否则按顺序校验指定元素
        } else {
            for (String eleName : requireParam) {
                checkResult = checkElement(eleName);
                if(!checkWithError && !checkResult){
                    break;
                }
            }
        }
        return errors.isEmpty();
    }

    /**
     * 获取已加载的校验规则
     *
     * @return
     */
    public Map<String, RuleBean> getRules() {
        return validateRules;
    }

    /**
     * 获取已加载的校验字段
     *
     * @return
     */
    public Map<String, FieldBean> getFields() {
        return validateFields;
    }

    /**
     * 获取错误字段列表
     *
     * @return
     */
    public String[] getErrorFields() {
        String[] keys = new String[]{};
        return errors.keySet().toArray(keys);
    }

    /**
     * 获取错误字段列表
     *
     * @return
     */
    public Map<String, ErrorBean> getErrors() {
        return errors;
    }

    /**
     * 获取字段错误信息
     *
     * @param field
     * @return
     */
    public ErrorBean getError(String field) {
        return errors.get(field);
    }

    /**
     * 初始化规则列表
     */
    private void initRules() {
        //如果不存在默认规则,将默认规则添加至首位,重名规则用后添加的覆盖
        if (!rulesClass.contains(new ValidateRules())) {
            rulesClass.add(0, new ValidateRules());
        }
        for (Object clz : rulesClass) {
            for (Class<?> currClazz = clz.getClass(); currClazz != Object.class; currClazz = currClazz.getSuperclass()){
                Method[] ms = currClazz.getDeclaredMethods();
                for (Method m : ms) {
                    ValidateRule rule = m.getAnnotation(ValidateRule.class);
                    if (rule != null) {
                        addRule(m, clz, rule);
                    }
                }
            }
        }
    }

    /**
     * 初始化对象字段所需校验规则并保存值
     *
     * @param target
     */
    private void initTargetField(Object target) {
        //向上遍历
        for (Class<?> clazz = target.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                try {
                    Valid validRules = field.getAnnotation(Valid.class);
                    //未设置校验规则的字段,直接跳过
                    if (validRules == null) {
                        continue;
                    }
                    ApiModelProperty desc = field.getAnnotation(ApiModelProperty.class);
                    //关闭安全性检查
                    field.setAccessible(true);
                    Object value = field.get(target);
                    addElement(field.getName(), value, target, validRules, desc);
                    checkSimpleField(value);
                } catch (IllegalAccessException | IllegalArgumentException | SecurityException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 判断是否复杂类型(Param嵌套,数组Param嵌套)
     *
     * @param value
     */
    private void checkSimpleField(Object value) {
        //向下遍历
        if (!checkFieldType(value)) {
            initTargetField(value);
        } else if (value instanceof Object[]) {
            for (Object temp : (Object[]) value) {
                //向下遍历
                if (!checkFieldType(temp)) {
                    initTargetField(temp);
                }
            }
        } else if (value instanceof List) {
            for (Object temp : (List) value) {
                //向下遍历
                if (!checkFieldType(temp)) {
                    initTargetField(temp);
                }
            }
        }
    }

    /**
     * 判断字段是否是基础类
     *
     * @param obj
     * @return
     */
    private boolean checkFieldType(Object obj) {
        return obj == null || obj.getClass() != null && obj.getClass().getClassLoader() == null;
    }

    /**
     * 校验元素
     *
     * @param eleName
     * @return
     */
    private boolean checkElement(String eleName) {
        boolean result;
        FieldBean field = validateFields.get(eleName);
        if (field.rules == null) {
            return true;
        }
        result = checkFieldElement(field);
        return result;
    }

    /**
     * 校验对象字段
     *
     * @param field
     * @return
     */
    private boolean checkFieldElement(FieldBean field) {
        for (RuleBean rule : field.rules) {
            if (rule == null) {
                continue;
            }
            try {
                List<Object> values = genValues(rule, field);
                Object ruleClazz = rule.ownerClass;
                Method m = rule.ruleMethod;
                if (m == null) {
                    return true;
                }
                //关闭安全性检查
                m.setAccessible(true);
                boolean result = (boolean) m.invoke(ruleClazz, values.toArray());
                if (!result) {
                    saveErrors(field, rule);
                    return false;
                }
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
                    | SecurityException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * 组装规则所需参数
     *
     * @param rule
     * @param field
     * @return
     */
    private List<Object> genValues(RuleBean rule, FieldBean field) {
        List<Object> values = new ArrayList<>();
        //规则使用多参数
        if (rule.params != null && rule.params.length > 0) {
            for (String fieldName : rule.params) {
                //加载插件参数
                if(checkPluginsParam(values,fieldName,field)) continue;
                for(Class<?> currClazz = field.ownerClass.getClass(); currClazz != Object.class;
                    currClazz = currClazz.getSuperclass()) {
                    try {
                        Field field1 = currClazz.getDeclaredField(fieldName);
                        field1.setAccessible(true);
                        values.add(field1.get(field.ownerClass));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    }
                }
            }
        //规则使用单一参数
        } else {
            values.add(field.value);
        }
        return values;
    }

    /**
     * 加载校验框架插件,在@Valid注解中添加的属性可被传入到校验规则中
     * 现在isLength和isRange等带属性规则与普通规则一样可被重写了
     * @param values
     * @param paramName
     * @param field
     * @return
     */
    private boolean checkPluginsParam(List<Object> values,String paramName, FieldBean field){
        //当参数名为@this或@xx时,使用@Valid注解标记的字段值或@Valid本身的属性值
        if("@this".equals(paramName)){
            values.add(field.value);
            return true;
        }else if(paramName.startsWith("@")){
            values.add(getValidProp(field.validRules,paramName));
            return true;
        }
        return false;
    }

    /**
     * 获取@Valid注解的属性值
     * @param validRules
     * @param propName
     * @return
     */
    private Object getValidProp(Valid validRules,String propName){
        try {
            Method prop = validRules.getClass().getDeclaredMethod(propName.replace("@",""));
            prop.setAccessible(true);
            return prop.invoke(validRules);
        }catch (NoSuchMethodException e){
            e = new NoSuchMethodException("没在@Valid注解中找到对应字段【"+propName+"】,检查是不是字段名写错了");
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 替换错误提示中的变量
     * @param errorMsg
     * @param field
     * @return
     */
    private String replaceErrorMsg(String errorMsg,FieldBean field){
        if(errorMsg.contains("@this")){
            errorMsg = errorMsg.replaceAll("@this",String.valueOf(field.value));
        }
        boolean replaceFlag = false;
        if(errorMsg.contains("@desc")){
            errorMsg = errorMsg.replaceAll("@desc",field.desc);
            replaceFlag = true;
        }
        //兼容老代码,大量规则没写字段描述提示,使用新代码会重复显示字段描述
        if(!replaceFlag){
            errorMsg = "【" + field.desc + "】" + errorMsg;
        }
        //如果字段使用了@Valid注解中的属性,则使用该属性的值
        for(Method prop : field.validRules.getClass().getDeclaredMethods()){
            if(errorMsg.contains("@"+prop.getName())){
                Object propValue = getValidProp(field.validRules,prop.getName());
                if(prop != null){
                    errorMsg = errorMsg.replaceAll("@"+prop.getName(),propValue.toString());
                }
            }
        }

        return errorMsg;
    }

    /**
     * 保存错误提示信息.
     *
     * @param field field
     * @param rule  rule
     */
    private void saveErrors(FieldBean field, RuleBean rule) {
        ErrorBean eb = new ErrorBean();
        eb.fieldName = field.ownerClass.getClass().getSimpleName() + "." + field.name;
        eb.fieldDesc = "【" + field.desc + "】";
        eb.retCode = rule.retCode;
        //如果字段使用了自定义提示文本,则使用字段的,否则使用校验方法默认的
        if (field.errorMsg.containsKey(rule.name)) {
            eb.errorMsg = replaceErrorMsg(field.errorMsg.get(rule.name),field);
        } else {
            eb.errorMsg = replaceErrorMsg(rule.errorMsg,field);
        }
        errors.put(eb.fieldName, eb);
    }

    public void setRequireParam(String[] requireParam) {
        this.requireParam = requireParam;
    }

    public void setCheckWithError(boolean checkWithError) {
        this.checkWithError = checkWithError;
    }

    public void setExcludeRules(String[] excludeRules) {
        this.excludeRules = excludeRules;
    }

    public class FieldBean {
        private String name;
        private Object value;
        private Object ownerClass;
        private Map<String, String> errorMsg;
        private RuleBean[] rules;
        private Valid validRules;
        private String desc = "";

        private FieldBean(String name, Object value, Object ownerClass, Valid validRules) {
            this.name = name;
            this.value = value;
            this.ownerClass = ownerClass;
            this.validRules = validRules;
        }

        private FieldBean(String name,Object value, Object ownerClass){
            this.name = name;
            this.value = value;
            this.ownerClass = ownerClass;
        }

        public String getName() {
            return name;
        }

        public Object getValue() {
            return value;
        }

        public Object getOwnerClass() {
            return ownerClass;
        }

        public RuleBean[] getRules() {
            return rules;
        }

        public Valid getValidRules(){return validRules; }

        public Map<String, String> getErrorMsg() {
            return errorMsg;
        }

        public String getDesc() {
            return desc;
        }
    }

    public class RuleBean {
        private String name;
        private Method ruleMethod;
        private Object ownerClass;
        private int retCode;
        private String errorMsg;
        private String[] params;

        private RuleBean(Method ruleMethod, Object ownerClass, ValidateRule rule) {
            this.errorMsg = "".equals(rule.value()) ? rule.code().msg : rule.value();
            this.retCode = rule.code().code;
            this.ruleMethod = ruleMethod;
            this.ownerClass = ownerClass;
            this.params = rule.params();
            this.name = ruleMethod.getName();
        }

        public String getName() {
            return name;
        }

        public Method getRuleMethod() {
            return ruleMethod;
        }

        public Object getOwnerClass() {
            return ownerClass;
        }

        public int getRetCode() {
            return retCode;
        }

        public String getErrorMsg() {
            return errorMsg;
        }

        public String[] getParams() {
            return params;
        }
    }

    public class ErrorBean {
        private String fieldName;
        private String fieldDesc;
        private String errorMsg;
        private int retCode;

        public String getFieldName() {
            return fieldName;
        }

        public String getFieldDesc() {
            return fieldDesc;
        }

        public String getErrorMsg() {
            return errorMsg;
        }

        public int getRetCode() {
            return retCode;
        }
    }
}

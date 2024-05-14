package com.bewd.annotations;


import com.bewd.common.RetCode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName ValidateRule 
 * @author Efy Shu
 * @Description 校验规则注解
 * @date 2017年3月7日 下午6:56:28 
 */
@Retention(RetentionPolicy.RUNTIME) 
@Target(ElementType.METHOD)
public @interface ValidateRule {
    /**提示消息(如果填写表示替换默认提示)*/
	String value() default "";
    /**返回码*/
	RetCode code() default RetCode.FAILED;
    /**关联参数(填入字段名,则自动加载对应参数到校验规则中)*/
	String[] params() default {};
}

package com.bewd.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName Valid
 * @author Efy Shu
 * @Description 校验元素注解
 * @date 2017年3月7日 下午6:56:28 
 */
@Retention(RetentionPolicy.RUNTIME) 
@Target({ElementType.FIELD})
public @interface Valid {
	/**需要校验的规则列表*/
	String[] rules() default {};
	/**需要替换的错误提示 例:isNull:新的提示 isLength:新的提示*/
	String[] msg() default {};
	/**
     * 限制字段长度(默认0 表示不限制长度)
     * 长度校验仅允许String,List,Object[]使用
     */
	int length() default 0;
    /**
     * 限制字段范围(默认空 表示不限制范围)
     * 范围校验仅允许int,long,String,List,Object[]使用
     * 示例:
     * 1.请求参数为基础类型时,使用:["1","ab","3"],表示仅允许这三种值通过
     * 2.请求参数为数组或List类型时,使用[2-10],表示长度范围在2-10,使用:["1","ab","3"],表示仅允许这三种值通过
     */
	String[] range() default {};
}
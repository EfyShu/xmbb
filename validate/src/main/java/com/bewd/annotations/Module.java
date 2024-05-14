package com.bewd.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName Module
 * @author Efy Shu
 * @Description 模块名注解(用于日志输出)
 * @date 2017年9月11日 下午5:07:06 
 */
@Retention(RetentionPolicy.RUNTIME) 
@Target(ElementType.METHOD)
public @interface Module {
    /**模块名 冗余name*/
	String value() default "";
    /**模块名 冗余value*/
	String name() default "";
	/**模块标签,会以【tag1】【tag2】形式添加在name前*/
	String[] tags() default {};
}

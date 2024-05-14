package com.bewd.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName CheckParams 
 * @author Efy Shu
 * @Description 标记需要校验的元素
 * @date 2017年11月17日 上午11:36:23 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.PARAMETER})
public @interface CheckParams {
    //需要被校验的参数列表(默认全部校验,如果填写此值则表示仅校验此值的参数)
	String[] requireParam() default {};
	//需要被排除的校验规则(默认全部规则,如果填写此值则表示仅使用除此值外的规则)
	String[] excludeRules() default {};
	//是否在已经校验出不通过的参数时继续执行校验(用来减少资源开销),默认继续校验
	boolean checkWithError() default true;
}

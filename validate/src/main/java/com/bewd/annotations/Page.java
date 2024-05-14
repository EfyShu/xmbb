package com.bewd.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName Page
 * @author Efy Shu
 * @Description 分页注解,封装分页方法
 * @date 2017年11月17日 上午11:36:23 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Page {
    /**需要导出(true 表示取Integer最大值,false表示只做当前页数量)*/
	boolean export() default false;
    /**使用原始值(true 表示使用方法返回的原始值,false表示使用PageDto封装)*/
	boolean rawData() default false;
}

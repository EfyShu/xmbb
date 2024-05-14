package com.bewd.annotations;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName ValidateRule 
 * @author Efy Shu
 * @Description 校验规则类注解(用来标记声明的哪些对象是规则类)
 * @date 2017年3月7日 下午6:56:28 
 */
@Retention(RetentionPolicy.RUNTIME) 
@Target(ElementType.FIELD)
public @interface RuleClass {
}

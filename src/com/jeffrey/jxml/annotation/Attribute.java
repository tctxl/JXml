package com.jeffrey.jxml.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 此注释标注于类名之上，表示该bean有attribute属性存在
 * 即不需要aliasAttribute
 * */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Attribute{
//attribute类
Class<?> attributeClass();
//attribute别名
String attributeName();
//attribute方法别名
String methodName() default "attribute";
}

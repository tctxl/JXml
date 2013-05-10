package com.jeffrey.jxml.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ��ע�ͱ�ע������֮�ϣ���ʾ��bean��attribute���Դ���
 * ������ҪaliasAttribute
 * */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Attribute{
//attribute��
Class<?> attributeClass();
//attribute����
String attributeName();
//attribute��������
String methodName() default "attribute";
}

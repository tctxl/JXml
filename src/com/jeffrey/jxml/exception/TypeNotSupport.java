package com.jeffrey.jxml.exception;

public class TypeNotSupport extends Exception{
public TypeNotSupport(String element,Class<?> type) {
super(String.format("[%s]�����쳣��[%s]���Ͳ�֧��",element, type));
}
}

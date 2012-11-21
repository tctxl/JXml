package com.jeffrey.jxml.exception;

public class TypeNotSupport extends Exception{
public TypeNotSupport(String element,Class<?> type) {
super(String.format("[%s]解析异常，[%s]类型不支持",element, type));
}
}

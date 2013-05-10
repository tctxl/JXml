package com.jeffrey.jxml.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
/**
 * @author Jeffrey Shi
 * QQ 362116120
 * MAIL to shijunfan@163.com
 */
public class InvokeUtil {
public static String invokeObjtoString(Object obj) throws IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		Class<?> classType = obj.getClass();
		Field[] fields = classType.getDeclaredFields();
		StringBuilder sb = new StringBuilder();
		boolean frist=true;
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			field.setAccessible(true);
			String fieldName = field.getName();
			if (fieldName.equals("serialVersionUID")) {
				continue;
			}
			String stringLetter = fieldName.substring(0, 1).toUpperCase();
			String getName = String.format("get%s%s", stringLetter ,fieldName.substring(1));
			Method getMethod = classType.getMethod(getName, new Class[] {});
			Object value = getMethod.invoke(obj, new Object[] {});
			if(!frist){
				sb.append("&");
			}else{
				frist=false;
			}
			sb.append( fieldName + "=" + value);
		}
		return sb.toString();
}
}

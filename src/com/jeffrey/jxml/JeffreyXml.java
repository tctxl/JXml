package com.jeffrey.jxml;

/**
 * @author Jeffrey Shi
 * QQ 362116120
 * MAIL to shijunfan@163.com
 */

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import android.util.Log;

import com.jeffrey.jxml.exception.GenericErrorException;
import com.jeffrey.jxml.exception.KeyNotFoundException;
import com.jeffrey.jxml.exception.TypeNotSupport;

public class JeffreyXml {
	private HashMap<String, Class<?>> clzMaps = null;
	private boolean isIgnoreNullKey;
	private final String TAG="JXml";
	private enum Type{
		STRING,LIST,OTHER
	}
	
	/**���췽��*/
	public JeffreyXml() {
		clzMaps = new HashMap<String, Class<?>>();
	}
	
	/**���ñ���*/
	public void alias(String elementName,Class<?> clz) {
		clzMaps.put(elementName,clz);
	}
	
	/**��������*/
	public <T> Object parserObject(String xmlString,Class<?> clz) throws IllegalAccessException, InstantiationException, JDOMException, IOException, SecurityException, IllegalArgumentException, NoSuchMethodException, InvocationTargetException, ClassNotFoundException, GenericErrorException, ParseException, KeyNotFoundException, TypeNotSupport{
		return parserObject(StringConvertInputStream(xmlString), clz);
	}

	/**��������*/
	public <T> Object parserObject(InputStream xmlStream,Class<?> clz) throws JDOMException, IOException, SecurityException, IllegalArgumentException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException, ClassNotFoundException, GenericErrorException, ParseException, KeyNotFoundException, TypeNotSupport{
		SAXBuilder sb = new SAXBuilder();
		Document doc=null;
		doc = sb.build(xmlStream);
        Element root = doc.getRootElement();
		return iterateElement(root, clz);
	}

	/**��������*/
	public <T> Object parserObject(File xmlFile,Class<?> clz) throws JDOMException, IOException, SecurityException, IllegalArgumentException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException, ClassNotFoundException, GenericErrorException, ParseException, KeyNotFoundException, TypeNotSupport{
		SAXBuilder sb = new SAXBuilder();
		Document doc=null;
		doc = sb.build(xmlFile);
        Element root = doc.getRootElement();
		return iterateElement(root, clz);
	}
	
	/**����Element*/
	@SuppressWarnings({ "unchecked" })
	private <T> Object iterateElement(Element element,Class<?> clz) throws IllegalAccessException, InstantiationException, SecurityException, IllegalArgumentException, NoSuchMethodException, InvocationTargetException, ClassNotFoundException, GenericErrorException, ParseException, KeyNotFoundException, TypeNotSupport{
		Object obj=clz.newInstance();
		HashMap<String, Field> fields=getMethod(clz);
		List<Element> elements=element.getChildren();
		for(int i=0;i<elements.size();i++){
			Element children=elements.get(i);
			//����element������ʱ����ʾ��element���¼�����
			String childName=children.getName(); 
			String childNameUp=childName.substring(0, 1).toUpperCase()+childName.substring(1,childName.length());
			String childNameLow=childName.substring(0, 1).toLowerCase()+childName.substring(1,childName.length());
			Field field=null;
			//���bean�д��ڸü�ֵ
			if((field=fields.containsKey(childNameUp)?fields.get(childNameUp):null)!=null||
					((field=fields.containsKey(childNameLow)?fields.get(childNameLow):null)!=null)){

				if(!children.getTextTrim().equals("")){
					invokeBaseValue(field, obj, children.getTextTrim());
				}else{
					Type type=getType(field.getType());
					switch (type) {
					case LIST:
						try {
							if(field.getGenericType()==null)
								throw new GenericErrorException(String.format("�ֶ�[%s]ΪList������ָ�����Ͷ���", childName));
							if(field.getGenericType() instanceof ParameterizedType){
								String genericClass=getListGenericClass(field.getGenericType());
								//List��Ƕ��Listδ��ɹ��ܣ��ݻ�
								if(isList(genericClass)){
									
								}else{
									List<?> list=parserList(Class.forName(genericClass),children);
									invokeBaseValue(field, obj, list);
								}
							}
						} catch (GenericErrorException e) {
							throw new GenericErrorException(String.format("�ֶ�[%s]ΪList������ָ�����Ͷ���", childName));
						} 
						break;
					case STRING:
//						Log.w(TAG, String.format("[%s]���ֶ�[%s]Ϊ��",clz.getName(), childName));
						break;
					case OTHER:
						Object otherObject=iterateElement(children, field.getType());
						invokeBaseValue(field, obj, otherObject);
						break;
					default:
						throw new TypeNotSupport(childName,field.getType());
					}
				}
			}else{
				if(!isIgnoreNullKey)
					throw new KeyNotFoundException(clz.getName(), childName);
				else
					Log.w(TAG, String.format("��[%s]�У���[%s]������",clz.getName(), childName));
			}
		}
		return obj;
	}
	
	/**����List�ķ���*/
	private List<Object> parserList(Class<?> genericClass, Element element) throws SecurityException, IllegalArgumentException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException, ClassNotFoundException, GenericErrorException, ParseException, KeyNotFoundException, TypeNotSupport{
		List<Object> list=new ArrayList<Object>();
		List<?> children=element.getChildren();
		for(int i=0;i<children.size();i++){
			Element child=(Element) children.get(i);
			Object object=iterateElement((Element) children.get(i), genericClass);
			list.add(object);
		}
		return list;
	}
	
	/**���List�еķ���
	 * @throws ClassNotFoundException 
	 * @throws GenericErrorException */
	private String getListGenericClass(java.lang.reflect.Type type) throws ClassNotFoundException, GenericErrorException{
		String typeStr=type.toString();
		int start,end;
		start=typeStr.indexOf("<");
		end=typeStr.lastIndexOf(">");
		if(start==-1){
			throw new GenericErrorException();
		}else{
			String classStr=typeStr.substring(start+1, end);
			return classStr;
		}
	}
	
	/**�ж��Ƿ���List*/
	private boolean isList(String classStr){
		if(classStr.contains("java.util.ArrayList")||classStr.contains("java.util.List")||classStr.contains("java.util.LinkedList")){
			return true;
		}
		return false;
	}
	
	/**
	 * �õ�bean�е�����*/
	private Type getType(java.lang.reflect.Type type) throws ClassNotFoundException, IllegalAccessException, InstantiationException{
		Type t=null;
		Object obj=Class.forName(type.toString().substring(6)).newInstance();
		if(obj instanceof List){
			t=Type.LIST;
		}else if(obj instanceof String){
			t=Type.STRING;
		}else{
			t=Type.OTHER;
		}
		return t;
	}
	
	/**����������Է���*/
	private void invokeBaseValue(Field childField,Object object,Object value) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException, ParseException{
		String childName=childField.getName();
		String method="set"+childName.substring(0, 1).toUpperCase()+childName.substring(1,childName.length());
		Method setMethod=object.getClass().getMethod(method, new Class[]{childField.getType()});
		setMethod.invoke(object, castType(value,childField.getType()));
	}
	
	/**����ת��*/
	
	private <T> Object castType(Object value,Class<?> class1) throws IllegalAccessException, InstantiationException, ParseException{
		if(class1.equals(int.class)){
			return Integer.parseInt((String) value);
		}else if(class1.equals(long.class)){
			return Long.parseLong((String) value);
		}else if(class1.equals(double.class)){
			return Double.parseDouble((String) value);
		}else
		return value;
	}
	
	/**���ظ����Fields*/
	private HashMap<String,Field> getMethod(Class<?> clz){
		HashMap<String, Field> map=new HashMap<String, Field>();
		Field[] fields=clz.getDeclaredFields();
		for(Field field:fields){
			map.put(field.getName(), field);
		}
		return map;
	}
	
	/**���ظö����Fields*/
	private HashMap<String,Field> getMethod(Object object){
		return getMethod(object.getClass());
	}
	
	/**InputStreamת����String*/
	public String inputStreamConvertString(InputStream inputStream) throws Exception {
		ByteArrayOutputStream arrayBuffer = new ByteArrayOutputStream();
		byte[] b = new byte[1024];
		int len = -1;
		while ((len = inputStream.read(b)) != -1) {
			arrayBuffer.write(b, 0, len);
		}
		byte[] bytes=arrayBuffer.toByteArray();
		inputStream.close();
		arrayBuffer.close();
		return new String(bytes,"UTF-8");
	}

	/**Stringת��InputStream*/
	public InputStream StringConvertInputStream(String string){
		InputStream is=null;
		try {
			is = new ByteArrayInputStream(string.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return is;
	}
	
	/**����bean��û���ֶεĴ���*/
	public void IgnoreNullKey(){
		isIgnoreNullKey=true;
	}
	
}

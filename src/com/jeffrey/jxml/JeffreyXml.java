package com.jeffrey.jxml;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import android.util.Log;

import com.jeffrey.jxml.exception.GenericErrorException;
import com.jeffrey.jxml.exception.KeyNotFoundException;
import com.jeffrey.jxml.exception.MultiplexListException;
import com.jeffrey.jxml.exception.TypeNotSupport;

/**
 * @author Jeffrey Shi
 * QQ 362116120
 * MAIL to shijunfan@163.com
 */

public class JeffreyXml {
	private HashMap<String, Class<?>> clzMaps = null;
	private HashMap<String, Class<?>> attrClzMaps = null;
	private String attributeMethodName="attribute";
	private boolean isIgnoreNullKey;
	private final String TAG="JXml";
	private InputStream is;
	private Element root;
	
	private enum Type{
		STRING,LIST,OTHER
	}
	
	/**构造方法*/
	public JeffreyXml() {
		clzMaps = new HashMap<String, Class<?>>();
		attrClzMaps=new HashMap<String, Class<?>>();
	}
	
	/**构造方法
	 * @throws IOException 
	 * @throws JDOMException */
	public JeffreyXml(InputStream xmlStream) throws JDOMException, IOException {
		this.is=xmlStream;
		SAXBuilder sb = new SAXBuilder(); 
		Document doc=null;
		doc = sb.build(is);
        root = doc.getRootElement();
		clzMaps = new HashMap<String, Class<?>>();
		attrClzMaps=new HashMap<String, Class<?>>();
	}
	
	public void aliasAttribute(String elementName,Class<?> clz){
		attrClzMaps.put(elementName, clz);
	}

	/**得到ChildString*/
	public String getString(String key) throws JDOMException, IOException{
        String str = root.getChildText(key);
		return str;
	}
	
	/**得到ChildElement*/
	public Element getElement(String key) throws JDOMException, IOException{
        Element child = root.getChild(key);
		return child;
	}
	
	/**设置别名*/
	public void alias(String elementName,Class<?> clz) {
		clzMaps.put(elementName,clz);
	}

	/**解析方法
	 * @throws MultiplexListException */
	public <T> T parserObject(Class<T> clz) throws IllegalAccessException, InstantiationException, JDOMException, IOException, SecurityException, IllegalArgumentException, NoSuchMethodException, InvocationTargetException, ClassNotFoundException, GenericErrorException, ParseException, KeyNotFoundException, TypeNotSupport, MultiplexListException{
		return parserObject(is, clz);
	}
	
	/**解析方法
	 * @throws MultiplexListException */
	public <T> T parserObject(String xmlString,Class<T> clz) throws IllegalAccessException, InstantiationException, JDOMException, IOException, SecurityException, IllegalArgumentException, NoSuchMethodException, InvocationTargetException, ClassNotFoundException, GenericErrorException, ParseException, KeyNotFoundException, TypeNotSupport, MultiplexListException{
		return parserObject(StringConvertInputStream(xmlString), clz);
	}

	/**解析方法
	 * @throws MultiplexListException */
	public <T> T parserObject(InputStream xmlStream,Class<T> clz) throws JDOMException, IOException, SecurityException, IllegalArgumentException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException, ClassNotFoundException, GenericErrorException, ParseException, KeyNotFoundException, TypeNotSupport, MultiplexListException{
		this.is=xmlStream;
		if(root==null){
		SAXBuilder sb = new SAXBuilder(); 
		Document doc=null;
		doc = sb.build(xmlStream);
        root = doc.getRootElement();
        }
		return iterateElement(root, clz);
	}

	/**解析方法
	 * @throws MultiplexListException */
	public <T> Object parserObject(File xmlFile,Class<?> clz) throws JDOMException, IOException, SecurityException, IllegalArgumentException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException, ClassNotFoundException, GenericErrorException, ParseException, KeyNotFoundException, TypeNotSupport, MultiplexListException{
		SAXBuilder sb = new SAXBuilder();
		Document doc=null;
		doc = sb.build(xmlFile);
        Element root = doc.getRootElement();
		return iterateElement(root, clz);
	}
	
	/**遍历Element
	 * @throws MultiplexListException */
	@SuppressWarnings({ "unchecked" })
	private <T> T iterateElement(Element element,Class<T> clz) throws IllegalAccessException, InstantiationException, SecurityException, IllegalArgumentException, NoSuchMethodException, InvocationTargetException, ClassNotFoundException, GenericErrorException, ParseException, KeyNotFoundException, TypeNotSupport, MultiplexListException{
		Object obj=clz.newInstance();
		HashMap<String, Field> fields=getMethod(clz);
		List<Element> elements=element.getChildren();
		com.jeffrey.jxml.annotation.Attribute anotation = clz.getAnnotation(com.jeffrey.jxml.annotation.Attribute.class);
		System.out.println(Arrays.toString(clz.getDeclaredAnnotations()));
		if(anotation!=null){
			if(anotation.attributeName()!=null){
				attributeMethodName = anotation.methodName();
				if(!attrClzMaps.containsKey(anotation.attributeName())){
					attrClzMaps.put(anotation.attributeName(), anotation.attributeClass());
				}
			}
		}
		if(attrClzMaps.containsKey(element.getName())){
			Object o=parserAttribute(element);
			Field field=null;
			if(fields.containsKey(attributeMethodName)){
				field=fields.get(attributeMethodName);
				invokeBaseValue(field, obj, o);
			}else{
				if(!isIgnoreNullKey)
					throw new KeyNotFoundException(clz.getName(), attributeMethodName);
				else
					Log.w(TAG, String.format("类[%s]中，键[%s]不存在",clz.getName(), attributeMethodName));
			}
		}
		
		for(int i=0;i<elements.size();i++){
			Element children=elements.get(i);
			//当该element有内容时，表示该element无下级子项
			String childName=children.getName(); 
			String childNameUp=childName.substring(0, 1).toUpperCase()+childName.substring(1,childName.length());
			String childNameLow=childName.substring(0, 1).toLowerCase()+childName.substring(1,childName.length());
			Field field=null;
			//如果bean中存在该键值
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
								throw new GenericErrorException(String.format("字段[%s]为List，必须指定泛型对象", childName));
							if(field.getGenericType() instanceof ParameterizedType){
								String genericClass=getListGenericClass(field.getGenericType());
								List<?> list = null;
								if(isList(genericClass)){
									try {
										list = parserMulList(genericClass, children);
									} catch (MultiplexListException e) {
										throw new MultiplexListException(genericClass, childName);
									}
								}else{
									list = parserList(Class.forName(genericClass),children);
								}
								invokeBaseValue(field, obj, list);
							}
						} catch (GenericErrorException e) {
							throw new GenericErrorException(String.format("字段[%s]为List，必须指定泛型对象", childName));
						} 
						break;
					case STRING:
//						Log.w(TAG, String.format("[%s]中字段[%s]为空",clz.getName(), childName));
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
					Log.w(TAG, String.format("类[%s]中，键[%s]不存在",clz.getName(), childName));
			}
		}
		return (T) obj;
	}
	
	/**解析Bean附带属性*/
	private Object parserAttribute(Element children) throws KeyNotFoundException, SecurityException, NoSuchMethodException, IllegalAccessException, InstantiationException, IllegalArgumentException, InvocationTargetException, ParseException{
		Class<?> attrCls=attrClzMaps.get(children.getName());
		HashMap<String, Field> fields = getMethod(attrCls);
		Object obj=attrCls.newInstance();
		List<?> attrList=children.getAttributes();
		for(int i=0;i<attrList.size();i++){
			Attribute attribute=(Attribute) attrList.get(i);
			String attrName=attribute.getName();
			String attrValue=attribute.getValue();
			String childNameUp=attrName.substring(0, 1).toUpperCase()+attrName.substring(1,attrName.length());
			String childNameLow=attrName.substring(0, 1).toLowerCase()+attrName.substring(1,attrName.length());
			Field field=null;
			if((field=fields.containsKey(childNameUp)?fields.get(childNameUp):null)!=null||
					((field=fields.containsKey(childNameLow)?fields.get(childNameLow):null)!=null)){
				invokeBaseValue(field, obj, attrValue);
			}else{
				if(!isIgnoreNullKey)
					throw new KeyNotFoundException(attrCls.getName(), attrName);
				else
					Log.w(TAG, String.format("类[%s]中，键[%s]不存在",attrCls.getName(), attrName));
			}
		}
		return obj;
	}
	
	/**toXML */
	@SuppressWarnings("unused")
	private String toXML(Object obj) throws Exception{
		Document doc=new Document();
		Field[] fields = obj.getClass().getDeclaredFields();
		for(Field field:fields){
			String name = field.getName();
			Type type = getType(field.getType());
			switch (type) {
			case LIST:
				
				break;
			case STRING:
				break;
			case OTHER:
				break;
			default:
				break;
			}
			Method getMethod = obj.getClass().getMethod(String.format("get%s%s", name.substring(0,1).toUpperCase(),name.substring(1)), new Class[] {});
			Object value = getMethod.invoke(obj, new Object[]{});
		}
		XMLOutputter XMLOut = new XMLOutputter(getFormat("UTF-8"));
		String result = XMLOut.outputString(doc);
		return result;
	}
	
	/**获取Format*/
	public Format getFormat(String encode){
		  Format format = Format.getCompactFormat();
		  format.setEncoding(encode);
		  format.setIndent(" ");
		  return format;
	}

	/**处理多重List方法*/
	private List<Object> parserMulList(String genericClass , Element element) throws GenericErrorException, SecurityException, IllegalArgumentException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException, ClassNotFoundException, ParseException, KeyNotFoundException, TypeNotSupport, MultiplexListException{
		List<Object> list=new ArrayList<Object>();
		List<?> elements = element.getChildren();
		for(int i=0;i<elements.size();i++){
			if(element.getTextTrim().equals("")){
				Element children=(Element) elements.get(i);
				String typeStr = getListGenericClass(genericClass);
				if(isList(typeStr)){
					parserMulList(typeStr, children);
				}else{
					List<Object> list2=parserList(Class.forName(typeStr), children);
					list.add(list2);
				}
			}else{
				throw new MultiplexListException();
			}
		}
		return list;
	}
	
	/**获取多重List内的泛型
	 * @throws GenericErrorException */
	private String getListGenericClass(String typeStr) throws GenericErrorException{
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
	/**处理List的方法
	 * @throws MultiplexListException */
	private List<Object> parserList(Class<?> genericClass , Element element) throws SecurityException, IllegalArgumentException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException, ClassNotFoundException, GenericErrorException, ParseException, KeyNotFoundException, TypeNotSupport, MultiplexListException{
		List<Object> list=new ArrayList<Object>();
		List<?> children=element.getChildren();
		for(int i=0;i<children.size();i++){
			Element child=(Element) children.get(i);
			Object object=iterateElement(child, genericClass);
			list.add(object);
		}
		return list;
	}
	
	/**获得List中的泛型*/
	private String getListGenericClass(java.lang.reflect.Type type) throws ClassNotFoundException, GenericErrorException{
		return getListGenericClass(type.toString());
	}
	
	/**判断是否是List*/
	private boolean isList(String classStr){
		if(classStr.contains("java.util.ArrayList")||classStr.contains("java.util.List")||classStr.contains("java.util.LinkedList")){
			return true;
		}
		return false;
	}
	
	/**
	 * 得到bean中的类型*/
	private Type getType(java.lang.reflect.Type type) throws ClassNotFoundException, IllegalAccessException, InstantiationException{
		Type t=null;
		Object obj = null;
		if(type.toString().contains("java.util.List")){
			obj = Collections.emptyList();
		}else{
			obj=Class.forName(type.toString().replaceFirst("class ", "")).newInstance();
		}
		if(obj instanceof List){
			t=Type.LIST;
		}else if(obj instanceof String){
			t=Type.STRING;
		}else{
			t=Type.OTHER;
		}
		return t;
	}
	
	/**反射基本属性方法*/
	private void invokeBaseValue(Field childField,Object object,Object value) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException, ParseException{
		String childName=childField.getName();
		String method="set"+childName.substring(0, 1).toUpperCase()+childName.substring(1,childName.length());
		Method setMethod=object.getClass().getMethod(method, new Class[]{childField.getType()});
		setMethod.invoke(object, castType(value,childField.getType()));
	}
	
	/**类型转换*/
	
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
	
	/**返回该类的Fields*/
	private HashMap<String,Field> getMethod(Class<?> clz){
		HashMap<String, Field> map=new HashMap<String, Field>();
		Field[] fields=clz.getDeclaredFields();
		for(Field field:fields){
			field.setAccessible(true);
			map.put(field.getName(), field);
		}
		return map;
	}
	
	/**返回该对象的Fields*/
	@SuppressWarnings("unused")
	private HashMap<String,Field> getMethod(Object object){
		return getMethod(object.getClass());
	}
	
	/**InputStream转换成String*/
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

	/**String转换InputStream*/
	public InputStream StringConvertInputStream(String string){
		InputStream is=null;
		try {
			is = new ByteArrayInputStream(string.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return is;
	}
	
	/**忽略bean中没有字段的错误*/
	public void IgnoreNullKey(){
		isIgnoreNullKey=true;
	}
	
}

package test;

import com.jeffrey.jxml.annotation.Attribute;


@Attribute(attributeClass=CityAttribute.class,attributeName="City2",methodName="cityAttr")
public class CityAttribute {
	private String name;
	private String value;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return "CityAttribute [name=" + name + ", value=" + value + "]";
	}
	
}

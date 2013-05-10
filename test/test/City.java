package test;

import com.jeffrey.jxml.annotation.Attribute;

@Attribute(attributeClass=CityAttribute.class,attributeName="City",methodName="cityAttr")
public class City {
@Override
	public String toString() {
		return String.format(
				"City\ncityTag=%s,isCurrent=%s,cityName=%s,cityCode=%s",
				cityTag, isCurrent, cityName, cityCode);
	}
public String getCityTag() {
		return cityTag;
	}
	public void setCityTag(String cityTag) {
		this.cityTag = cityTag;
	}
	public int getIsCurrent() {
		return isCurrent;
	}
	public void setIsCurrent(int isCurrent) {
		this.isCurrent = isCurrent;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
private String cityTag;
private int isCurrent;
private String cityName;
private String cityCode;
private CityAttribute cityAttr;
public CityAttribute getCityAttr() {
	return cityAttr;
}
public void setCityAttr(CityAttribute cityAttr) {
	this.cityAttr = cityAttr;
}

}

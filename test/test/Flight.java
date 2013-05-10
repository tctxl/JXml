package test;

import java.util.ArrayList;

public class Flight {
	public ArrayList<Cabin> getCabins() {
		return cabins;
	}
	public void setCabins(ArrayList<Cabin> cabins) {
		this.cabins = cabins;
	}
	@Override
	public String toString() {
		return String
				.format("Flight\ndiscount=%s,flightTpm=%s,airCompany=%s,departTerminal=%s,mealType=%s,flightNO=%s,airCompanyCode=%s,depAirPort=%s,arrAirPort=%s,flightdestTime=%s,fuelTax=%s,flightOrgTime=%s,yPrice=%s,arriveTerminal=%s,craftType=%s,airportTax=%s,stop=%s,isCodeShare=%s,cabin=%s",
						discount, flightTpm, airCompany, departTerminal,
						mealType, flightNO, airCompanyCode, depAirPort,
						arrAirPort, flightdestTime, fuelTax, flightOrgTime,
						yPrice, arriveTerminal, craftType, airportTax, stop,
						isCodeShare, cabins);
	}
	public double getDiscount() {
		return discount;
	}
	public void setDiscount(double discount) {
		this.discount = discount;
	}
	public long getFlightTpm() {
		return flightTpm;
	}
	public void setFlightTpm(long flightTpm) {
		this.flightTpm = flightTpm;
	}
	public String getAirCompany() {
		return airCompany;
	}
	public void setAirCompany(String airCompany) {
		this.airCompany = airCompany;
	}
	public String getDepartTerminal() {
		return departTerminal;
	}
	public void setDepartTerminal(String departTerminal) {
		this.departTerminal = departTerminal;
	}
	public String getMealType() {
		return mealType;
	}
	public void setMealType(String mealType) {
		this.mealType = mealType;
	}
	public String getFlightNO() {
		return flightNO;
	}
	public void setFlightNO(String flightNO) {
		this.flightNO = flightNO;
	}
	public String getAirCompanyCode() {
		return airCompanyCode;
	}
	public void setAirCompanyCode(String airCompanyCode) {
		this.airCompanyCode = airCompanyCode;
	}
	public String getDepAirPort() {
		return depAirPort;
	}
	public void setDepAirPort(String depAirPort) {
		this.depAirPort = depAirPort;
	}
	public String getArrAirPort() {
		return arrAirPort;
	}
	public void setArrAirPort(String arrAirPort) {
		this.arrAirPort = arrAirPort;
	}
	public String getFlightdestTime() {
		return flightdestTime;
	}
	public void setFlightdestTime(String flightdestTime) {
		this.flightdestTime = flightdestTime;
	}
	public long getFuelTax() {
		return fuelTax;
	}
	public void setFuelTax(long fuelTax) {
		this.fuelTax = fuelTax;
	}
	public String getFlightOrgTime() {
		return flightOrgTime;
	}
	public void setFlightOrgTime(String flightOrgTime) {
		this.flightOrgTime = flightOrgTime;
	}
	public double getYPrice() {
		return yPrice;
	}
	public void setYPrice(double yPrice) {
		this.yPrice = yPrice;
	}
	public String getArriveTerminal() {
		return arriveTerminal;
	}
	public void setArriveTerminal(String arriveTerminal) {
		this.arriveTerminal = arriveTerminal;
	}
	public long getCraftType() {
		return craftType;
	}
	public void setCraftType(long craftType) {
		this.craftType = craftType;
	}
	public long getAirportTax() {
		return airportTax;
	}
	public void setAirportTax(long airportTax) {
		this.airportTax = airportTax;
	}
	public int getStop() {
		return stop;
	}
	public void setStop(int stop) {
		this.stop = stop;
	}
	public String getIsCodeShare() {
		return isCodeShare;
	}
	public void setIsCodeShare(String isCodeShare) {
		this.isCodeShare = isCodeShare;
	}
	private double discount;
	private long flightTpm;
	private String airCompany;
	private String departTerminal;
	private String mealType;
	private String flightNO;
	private String airCompanyCode;
	private String depAirPort;
	private String arrAirPort;
	private String flightdestTime;
	private long fuelTax;
	private String flightOrgTime;
	private double yPrice;
	private String arriveTerminal;
	private long craftType;
	private long airportTax;
	private int stop;
	private String isCodeShare;
	private ArrayList<Cabin> cabins;
	
}

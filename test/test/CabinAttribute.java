package test;

public class CabinAttribute {

public String getSeats() {
		return seats;
	}
	public void setSeats(String seats) {
		this.seats = seats;
	}
	public String getCabinName() {
		return cabinName;
	}
	public void setCabinName(String cabinName) {
		this.cabinName = cabinName;
	}
	public String getChildCabinName() {
		return childCabinName;
	}
	public void setChildCabinName(String childCabinName) {
		this.childCabinName = childCabinName;
	}
	public String getCabinClass() {
		return CabinClass;
	}
	public void setCabinClass(String cabinClass) {
		CabinClass = cabinClass;
	}
	public String getPatName() {
		return patName;
	}
	public void setPatName(String patName) {
		this.patName = patName;
	}
	public double getFdPrice() {
		return fdPrice;
	}
	public void setFdPrice(double fdPrice) {
		this.fdPrice = fdPrice;
	}
	public String getGetPriceType() {
		return getPriceType;
	}
	public void setGetPriceType(String getPriceType) {
		this.getPriceType = getPriceType;
	}
@Override
	public String toString() {
		return String
				.format("CabinAttribute\nseats=%s,cabinName=%s,childCabinName=%s,CabinClass=%s,patName=%s,fdPrice=%s,getPriceType=%s",
						seats, cabinName, childCabinName, CabinClass, patName,
						fdPrice, getPriceType);
	}
private String seats;
private String cabinName;
private String childCabinName;
private String CabinClass;
private String patName;
private double fdPrice;
private String getPriceType;

}

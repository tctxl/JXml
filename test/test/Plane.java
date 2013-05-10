package test;

public class Plane {
	@Override
	public String toString() {
		return String.format("Plane\nflight=%s", flight);
	}

	public Flight getFlight() {
		return flight;
	}

	public void setFlight(Flight flight) {
		this.flight = flight;
	}

	private Flight flight;
	
}

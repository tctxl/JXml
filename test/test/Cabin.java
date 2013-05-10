package test;

public class Cabin {
public CabinAttribute getAttribute() {
		return attribute;
	}

	public void setAttribute(CabinAttribute attribute) {
		this.attribute = attribute;
	}

@Override
	public String toString() {
		return String.format("Cabin\nattribute=%s", attribute);
	}

private CabinAttribute attribute;
}

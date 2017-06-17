package cn.aurorax.dataunion.model;

public class AddressRecord {
	private String community;
	private String building;
	private String unit;
	private String door;
	private String id;

	public String getCommunity() {
		return community;
	}

	public void setCommunity(String community) {
		this.community = community;
	}

	public String getBuilding() {
		return building;
	}

	public void setBuilding(String building) {
		this.building = building;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getDoor() {
		return door;
	}

	public void setDoor(String door) {
		this.door = door;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "AddressRecord [community=" + community + ", building=" + building + ", unit=" + unit + ", door=" + door
				+ ", id=" + id + "]";
	}
}

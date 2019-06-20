package cn.tedu.store.bean;

public class Area {

	public Area() {
	}
	private Integer id;
	private String cityCode;
	private String areaCode;
	private String areaName;
	public Area(Integer id, String cityCode, String areaCode, String areaName) {
		super();
		this.id = id;
		this.cityCode = cityCode;
		this.areaCode = areaCode;
		this.areaName = areaName;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	@Override
	public String toString() {
		return "Area [id=" + id + ", cityCode=" + cityCode + ", areaCode=" + areaCode + ", areaName=" + areaName + "]";
	}
	
}

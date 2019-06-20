package cn.tedu.store.bean;

public class Province {

	private Integer id;
	private String provinceCode;
	private String provinceName;
	public Province(Integer id, String provinceCode, String provinceName) {
		super();
		this.id = id;
		this.provinceCode = provinceCode;
		this.provinceName = provinceName;
	}
	public Province() {
		super();
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getProvinceCode() {
		return provinceCode;
	}
	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	@Override
	public String toString() {
		return "Province [id=" + id + ", provinceCode=" + provinceCode + ", provinceName=" + provinceName + "]";
	}
	
	
	
}

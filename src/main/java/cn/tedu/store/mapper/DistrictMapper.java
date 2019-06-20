package cn.tedu.store.mapper;

import java.util.List;

import cn.tedu.store.bean.Area;
import cn.tedu.store.bean.City;
import cn.tedu.store.bean.Province;

public interface DistrictMapper {
	/**
	 * 获取所有省的列表 
	 * @return 所有省的列表
	 */
 List<Province> getProvinces();
	
	/**
	 * 获取所有市的列表
	 * @param provinceCode
	 * @return 所有市的列表
	 */
	 List<City> getCities(String provinceCode);
	
	/**
	 * 获取所有区的列表
	 * @param procityCode
	 * @return 所有区的列表 
	 */
	 List<Area> getAreas(String cityCode);
	 
	 
	 //因为字段中有一个recvDict字段，但是数据提交中并未存在，所以需要处理、
	 //拿到省市区的中文名字，拼成recvDict放进Address
	 
	 /**
	  * 根据省的代号去查省的中文名称
	  * @param provinceCode
	  * @return 返回省名
	  */
	 String getProvinceNameByCode(String provinceCode);
	 
	 /**
	  * 根据市的代号去查市的中文名称
	  * @param cityeCode
	  * @return 返回市名
	  */
	 String getCityNameByCode(String cityeCode);
	 
	 /**
	  * 根据区域的代号去查市的中文名称
	  * @param areaCode
	  * @return 返回区名
	  */
	 String getAreaNameByCode(String areaCode);
}
 
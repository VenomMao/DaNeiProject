package cn.tedu.store.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.tedu.store.bean.Address;

public interface IAddressService {
	/**
	 * 
	 * @param address
	 * @return 新增加的收货地址的id
	 */ 
	Integer add(Address address);
	
	
	/**
	 * 根据登录用户的uid查询用户的所有收货人地址
	 * @param uid
	 * @return 返回用户所有收货人地址
	 */
	List<Address> getAddressListByUid(Integer uid);
	
	 
	/**
	 * 获取指定的收货地址数据
	 * @param id 收货地址数据的id
	 * @param uid 收货地址所归属的用户uid
	 * @return 匹配的数据，如果没有则返回null
	 */ 
	Address getAddressByIdAndUid(Integer id,Integer uid);
	
	
	/**
	 * 删除地址信息 （两个参数因为根据id可能会存在uid=1或者为0，所以删除既需要判断id，也要根据uid）
	 * @param id 被删除的数据 id
	 * @param uid 当前登录的用户的id
	 * @return 受影响的行数 0或者1
	 * @throws 当数据不存在时，抛出此异常
	 */
	Integer delete(Integer id,Integer uid);
	
	/**
	 * 修改收货地址
	 * @param address 至少包括被修改数据的id，uid，新数据。
	 * @return 受影响的行数 0或者1 
	 */
	Integer update(Address address);
	
	
	 /** 
	  * 将某用户的某条数据设为默认,设为默认操作
	  * @param uid
	  * @param id
	  * @return
	  */
	Integer setDefault(Integer id,Integer uid);
	
	
	
}

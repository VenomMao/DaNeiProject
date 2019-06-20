package cn.tedu.store.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import cn.tedu.store.bean.Address;

public interface AddressMapper {
	
	/**
	 * 新增收货地址地址
	 * @param address 收货地址信息
	 * @return 受影响的行数
	 */
	Integer insert(Address address);
	
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
	Address getAddressByIdAndUid(@Param("id") Integer id,@Param("uid")Integer uid);
	
	 
	
	/**
	 * 删除地址信息 （两个参数因为根据id可能会存在uid=1或者为0，所以删除既需要判断id，也要根据uid）
	 * @param id 被删除的数据 id
	 * @param uid 当前登录的用户的id
	 * @return 受影响的行数 0或者1
	 * 
	 * 属性名称是对应的，所以上面的Address里面不需要对应。
	 * 当有多个参数时候，
	 * 添加@Param("id") 注解 仅仅用于mybatis接口文件。
	 * 用于和SQL中 id= #{id}  #{id}对应，与原本参数没意义，跟注解有关系
	 */
	Integer delete(@Param("id") Integer id,@Param("uid") Integer uid);
	
	
	/**
	 * 修改收货地址
	 * @param address 至少包括被修改数据的id，uid，新数据。
	 * @return 受影响的行数 0或者1
	 */
	Integer update(Address address);
	
	
	/**
	 * 将某用户的所有收货地址均设置为非默认
	 * 	根据当前用户uid把所有的默认取消
	 * @param uid
	 * @return
	 */
	Integer cancelAllDefault(Integer uid);
	
	/**
	 * 将某用户的某条数据设为默认
	 * @param uid 用户的id
	 * @param id 收货地址的id
	 * @return 受影响的行数
	 */
	Integer setDefault(@Param("uid") Integer uid,@Param("id") Integer id);
	
}

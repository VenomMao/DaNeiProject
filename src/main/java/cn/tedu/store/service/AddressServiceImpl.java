package cn.tedu.store.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.tedu.store.bean.Address;
import cn.tedu.store.bean.User;
import cn.tedu.store.mapper.AddressMapper;
import cn.tedu.store.mapper.UserMapper;
import cn.tedu.store.service.ex.DataNotFoundException;
import cn.tedu.store.service.ex.PasswordNotMatchException;
import cn.tedu.store.service.ex.UserNotFoundException;
import cn.tedu.store.service.ex.UsernameAlreadyExistsException;
import cn.tedu.store.service.ex.UsernameNotFoundException;

@Service("addressService")
@Transactional
public class AddressServiceImpl implements IAddressService{
	  
	@Resource
	private AddressMapper addressMapper;
	
	@Resource(name="districtService")
	private IDistrictService districtService;

	public Integer add(Address address) {
		//执行增加
		//由controller提交的数据并没有recvDistrict字段，
		//此时需要得到该字段应有的值，并封装到address中
		String recvDistrict = getRecvDistrict(address);
		address.setRecvDistrict(recvDistrict);
		
		//确定当前收货地址是否为默认地址,不然出空指针异常
		//仅当当前的收货地址是该用户的第一条时设置为默认1
		List<Address> list = getAddressListByUid(address.getUid());
		if(list.size()==0) {
			address.setIsDefault(1);
		}else {
			address.setIsDefault(0);
		} 
		
		addressMapper.insert(address);
		//获取id
		Integer id = address.getId();
		return id;
	} 
	
	
	/**
	 * 根据登录用户的uid查询用户的所有收货人地址
	 * @param uid
	 * @return 返回用户所有收货人地址
	 */
	public List<Address> getAddressListByUid(Integer uid) {
		return addressMapper.getAddressListByUid(uid);
	}
	
	 
	/**
	 * 获取指定的收货地址数据
	 * @param id 收货地址数据的id
	 * @param uid 收货地址所归属的用户uid
	 * @return 匹配的数据，如果没有则返回null
	 */ 
	public Address getAddressByIdAndUid(Integer id, Integer uid) {
		return addressMapper.getAddressByIdAndUid(id, uid);
	}
	
	
	//事务一般在业务层去处理
	// @Transactional 同一个功能需要多次增删改操作时，需要添加、
	// 删除地址信息 
	@Transactional
	public Integer delete(Integer id, Integer uid) {
		//调整 ：考虑各种情景
		
		//声明返回值
		Integer affc = 0;
		
		//获取此次需要删除的地址信息
		Address addr = getAddressByIdAndUid(id, uid);
		//判断查询结果是否为null
		if(addr==null) {
			throw new DataNotFoundException("要删除的数据不存在，请刷新后再操作");
		}
		//获取数据成功，即将被删除的数据是存在的，执行删除
		affc = addressMapper.delete(id, uid);
		if(affc==0) {
			throw new DataNotFoundException("删除失败");
		}
		
		//判断刚才删除的收货地址是否是默认地址(此时addr还在内存中)，是则
		if(addr.getIsDefault()==1) {
			//是默认地址
			//获取剩余的所有数据，将得到List集合
			List<Address> list =  getAddressListByUid(uid);
			if(list.size()>0) {
				//判断集合长度是否大于0
				//长度大于0，即删除后还有收货地址的数据，则取出第一条数据的id
				Integer newDefaultId =  list.get(0).getId();
				//设为默认收货地址
				affc=  setDefault(newDefaultId, uid);
				if(affc==0) {
					//设置默认地址失败
					throw new DataNotFoundException("设置默认地址失败");
				}
			}else {
				//长度为0，即删除后没有收货地址数据，不做任何操作。
			}
		}else {
			//不是默认地址
		}
		return affc;
	}
	
	/**
	 * 修改收货地址
	 * address 至少包括被修改数据的id，uid，新数据。
	 * 受影响的行数 0或者1
	 */
	public Integer update(Address address) {
		//先获取省市区名称
		String recvDistrict = getRecvDistrict(address);
		//封装省市区名称
		address.setRecvDistrict(recvDistrict);
		//执行修改
		return addressMapper.update(address);
	}
	
	/**
	 *  将某用户的某条数据设为默认
	 *  对于增删改多个调用，用@Transactional注解
	 *  @Transactional 事务 保证操作整体成功或者整体失败 框架自动帮我们回滚
	 *  应该分析出现哪些问题，对于可能出现问题的程序分支使用抛出异常的方式处理
	 */
	@Transactional
	public Integer setDefault(Integer id, Integer uid) {
		//先将该用户的所有收货地址设置为非默认
		addressMapper.cancelAllDefault(uid);
		//将指定的收货地址设置为默认
		Integer affc = addressMapper.setDefault(uid, id);
		
		//判断操作是否成功
		if(affc==0) {
			//受影响行数为0,设置默认数据不存在，
			//应该回滚事务，则抛异常
			throw new DataNotFoundException("设置默认数据失败");
		}else {
			return affc;
		}
	}
	
	  
	/**
	 * 获取收货人地址的省市区的名称
	 * @param address
	 * @return 收货人地址的省市区的名称
	 */
	private String getRecvDistrict(Address address) {
		/**
		 * 因为通过address.getRecvProvince();拿到的是一个Code，
		 * 而getProvinceNameByCode是通过districtService拿到的，所以需要引入一个IDistrictService属性。
		 */
		
		String provinceCode = address.getRecvProvince();//510200
		String provinceName = districtService.getProvinceNameByCode(provinceCode);
		
		String cityCode = address.getRecvCity();
		String cityName = districtService.getCityNameByCode(cityCode);
		
		String areaCode = address.getRecvArea();
		String areaName = districtService.getAreaNameByCode(areaCode);
		
		String recvDistrict = provinceName+cityName+areaName;
		
		return recvDistrict;
	}


	





	


	

	

	
}

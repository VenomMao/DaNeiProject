package cn.tedu.store.mapper;

import cn.tedu.store.bean.User;

public interface UserMapper {
	/**
	 * 增加用户
	 * @param user
	 * @return 受影响的行数，void暂时也行。
	 */
	Integer insert(User user);
	
	/**
	 * 通过用户名查找用户
	 * @param username
	 * @return
	 */
	User findUserByUsername(String username);
	
	/**
	 * 业务层处理修改密码时，需要验证原密码是否正确需要根据id去查询用户信息，
	 * 去判断原来和现在密码是否匹配
	 * @param id
	 * @return  
	 */
	User findUserById(Integer id);
	
	/**
	 * 修改用户数据，可用于修改个人资料，也可以用于修改密码
	 * @param user 传入一个User 包含被修改的id，密码等新数据
	 * @return 受影响的行数 最终表现为0或者1.
	 */
	Integer update(User user);
	
	 /**
	  * 查询电子邮箱是否被占用 获取电子邮箱对应的数据的数量。
	  * @param email
	  * @return 返回的是一个Integer类型，判断0或者1即可、
	  */
	Integer getRecordCountByEmail(String email);
	
	 /**
	  * 查询手机号码是否被占用 获取手机号码对应的数据的数量。
	  * @param phone
	  * @return 返回的是一个Integer类型，判断0或者1即可、
	  */
	Integer getRecordCountByPhone(String phone);
	
	
	
	
	
	
	
	
	
	
}

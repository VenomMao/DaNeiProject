package cn.tedu.store.service;

import cn.tedu.store.bean.User;

public interface IUserService {
	/**
	 * 增加用户
	 * @param user
	 * @return 受影响的行数,返回ID。
	 * @throws 异常抛出 UsernameAlreadyExistsException
	 */
	Integer register(User user);
	
	/**
	 * 用户登录 返回值为三种，
	 * @param username
	 * @param password
	 * @throws UsernameNotFoundException 用户名不存在
	 * @throws PasswordNotMatchException 密码不匹配
	 * 
	 * 选择User因为后期可能去根据User拿ID。用Integer的话，后期0或者1容易乱。
	 * 用Boolean的话，就两种情况，正确不正确。
	 */
	User login(String username,String password);
	
	 
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
	 * 修改密码
	 * @param uid 被修改用户的id
	 * @param oldPassword 原来密码
	 * @param newPassword 新密码
	 * @return 受影响的行数
	 * @throws PassNotMatchException 原密码不正确
	 * @throws UserNotFoundException 用户信息不存在
	 * @throws 如果用户已经登录，在执行修改密码之前，该用户数据被删除，会导致无法存改，
	 * 			抛出UserNotFoundException
	 */
	Integer changePassword(Integer uid,String oldPassword,String newPassword);
	
	/**
	 * 修改用户基本资料
	 * @param id 
	 * @param username
	 * @param phone
	 * @param email
	 * @param gender
	 * @return 受影响的行数
	 * @throws UserNotFoundException 用户信息不存在
	 * @throws 异常抛出 UsernameAlreadyExistsException 用户名被占用 
	 */
	Integer editProfile(Integer id,String username,String phone,String email,Integer gender);
	
	 
	 /**
	  * 查询电子邮箱是否被占用。
	  * @param email
	  * @return 返回的是一个boolean类型，判断0或者1即可、
	  */
	boolean checkEmailExists(String email);
	
	 /**
	  * 查询手机号码是否被占用。
	  * @param phone
	  * @return 返回的是一个boolean类型，判断0或者1即可、
	  */
	boolean checkPhoneExists(String phone);
	
	
	/**
	  * 查询用户名在数据库中是否存在。
	  * @param username
	  * @return 返回的是一个boolean类型，判断0或者1即可、
	  */ 
	boolean checkUsernameExists(String username);
	 
	
	
}

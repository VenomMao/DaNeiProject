package cn.tedu.store.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import cn.tedu.store.bean.User;
import cn.tedu.store.mapper.UserMapper;
import cn.tedu.store.service.ex.PasswordNotMatchException;
import cn.tedu.store.service.ex.UserNotFoundException;
import cn.tedu.store.service.ex.UsernameAlreadyExistsException;
import cn.tedu.store.service.ex.UsernameNotFoundException;

@Service("userService")
public class UserServiceImpl implements IUserService {

	@Resource(name = "userMapper")
	private UserMapper userMapper;
	
	//配置文件里读一个值，注入到属性salt上
	@Value("#{dbConfig.salt}")
	private String salt;

	public Integer register(User user) {
		// 注册之前先查一下用户信息是否已经被占用
		if (checkUsernameExists(user.getUsername())) {
			// if为true，用户存在，则抛出异常
			throw new UsernameAlreadyExistsException("用户名已经被占用");
		} else {
			// 查询结果为false，新用户则插入，返回新用户的id.

			// 对用户密码进行加密
			// 获取密码明文
			String pwd = user.getPassword();
			// 对密码进行加盐

			// 摘要

			String md5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(pwd + salt);

			// 保存密文到数据库(密码明文被丢弃!)
			user.setPassword(md5);
			
			//打桩输出: 
			System.out.println("register::"+salt);
			System.out.println("register::"+user.getPassword());

		 userMapper.insert(user);
			return user.getId();
		}
	}

	/**
	 * 登录成功，返回一个User，后期可以根据User获取id或者通过id进行其他操作
	 */
	public User login(String username, String password) {
		// 根据用户名获取用户数据
		User u = userMapper.findUserByUsername(username);
		if (u != null) {
			// 则表示数据库里面有数据,再去判断密码

			// 计算明文password的加盐摘要
			// 比较密文跟摘要是否一致

			String md5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(password + salt);
			
			//打桩
			System.out.println("login::"+u.getPassword());
			System.out.println("login::"+md5);
			 
			//密文跟密文对比
			if (u.getPassword().equals(md5)) {
				// 登录成功
				return u;
			} else {
				throw new PasswordNotMatchException("密码不匹配");
			}
		} else {
			throw new UsernameNotFoundException("用户不存在");
		}
	}

	public User findUserByUsername(String username) {
		return userMapper.findUserByUsername(username);
	}

	public User findUserById(Integer id) {
		return userMapper.findUserById(id);
	}

	public Integer changePassword(Integer uid, String oldPassword, String newPassword) {
		// 根据uid查询用户数据
		User user = findUserById(uid);
		// 若查询数据为null,则用户数据不存在，抛出UserNotFoundException
		if (user != null) {
			// 查询到匹配数据则用户存在，再去判断密码是否匹配
		
			String md5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(oldPassword + salt);

			if (user.getPassword().equals(md5)) {
				// 原密码匹配，创建User对象，将uid和newPassword封装
				User newUser = new User();
				newUser.setId(uid);
				
				//加密密码
			
				md5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(newPassword+salt);
			
				
				newUser.setPassword(md5);
				// 执行修改 update(User user)，更新新的数据
				return userMapper.update(newUser);
			}
		} else {
			// 用户不存在，抛出UserNotFoundException
			throw new UserNotFoundException("登录已过期，或者用户数据不存在，请重新登陆后尝试");
		}

		return null;
	}

	public Integer editProfile(Integer uid, String username, String phone, String email, Integer gender) {
		// 创建User对象，用于调用持久层方法
		User user = new User();

		// 1，封装用户名
		// 根据uid查询用户数据
		User u = findUserById(uid);
		if (u == null) {
			throw new UserNotFoundException("登录已经过期或者用户不存在");
		}
		// 用户数据已经存在,判断新用户名与现有用户名是否一致

		if (u.getUsername().equals(username)) {
			// 一致，则新用户就是现有用户名
			// 则无需为user中的username赋值
		} else {
			// 提交的是新用户名，判断新用户名是否被占用
			User u2 = findUserByUsername(username);
			if (u2 == null) {
				// 新用户可以修改。
				System.out.println("=====");
				user.setUsername(username);
			} else {
				// 找到新用户名存在的数据，用户名被占用。
				throw new UsernameAlreadyExistsException("用户名已经被占用");

			}
		}
		// 2,封装uid,性别，手机号码，电子邮箱

		user.setId(uid);

		user.setGender(gender);

		if (phone != null && phone.length() >= 11) {
			user.setPhone(phone);
		}

		user.setEmail(email);

		user.setModifiedTime(new Date());

		// 3，执行修改数据
		System.out.println("-----" + user);
		return userMapper.update(user);

	}

	// 根据findUserByUsername衍生出的方法。
	public boolean checkUsernameExists(String username) {
		// 不为null,存在则是true值。
		return findUserByUsername(username) != null;
	}

	public boolean checkEmailExists(String email) {
		return userMapper.getRecordCountByEmail(email) > 0;
	}

	public boolean checkPhoneExists(String phone) {
		return userMapper.getRecordCountByPhone(phone) > 0;
	}

}

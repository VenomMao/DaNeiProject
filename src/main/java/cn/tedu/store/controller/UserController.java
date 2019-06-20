package cn.tedu.store.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.eclipse.jdt.internal.compiler.parser.RecoveredMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.tedu.store.bean.ResponseResult;
import cn.tedu.store.bean.User;
import cn.tedu.store.service.IUserService;
import cn.tedu.store.service.ex.PasswordNotMatchException;
import cn.tedu.store.service.ex.UserNotFoundException;
import cn.tedu.store.service.ex.UsernameAlreadyExistsException;
import cn.tedu.store.service.ex.UsernameNotFoundException;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

	@Resource(name = "userService")
	private IUserService userService;

	@RequestMapping("/register.do")
	public String showRegister() {
		return "register";
	}

	@RequestMapping("/login.do")
	public String showLogin() {
		// 转发
		return "login";
	}

	// 安全管理：修改密码
	@RequestMapping("/password.do")
	public String showPassword() {
		return "user_password";
	}

	// 个人信息修改:
	// 由于需要在页面中默认显示当前登录的用户数据，所以在处理页面请求时，应该查询到用户数据并转发到前端页面
	@RequestMapping("/profile.do")
	public String showProfile(HttpSession session, ModelMap modelMap) {
		// 查询当前用户登录的用户数据
		Integer uid = getUidFromSession(session);

		User user = userService.findUserById(uid);

		// 将用户数据转发到前端页面,使用modelMap
		modelMap.addAttribute("user", user);

		// 转发
		return "user_profile";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/handle_register.do")
	@ResponseBody
	public ResponseResult<Void> handleRegister(@RequestParam("uname") String username,
			@RequestParam("upwd") String password, @RequestParam("phone") String phone,
			@RequestParam("email") String email) {
		// 声明返回值
		ResponseResult<Void> rr;
		// 封装数据
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		user.setPhone(phone);
		user.setEmail(email);
		System.out.println("UserController.handleRegister()");
		System.out.println("\tuser=" + user);
		// 执行注册
		try {
			userService.register(user);
			rr = new ResponseResult<Void>(1, "注册成功");
			return rr;
		} catch (UsernameAlreadyExistsException e) {
			rr = new ResponseResult<Void>(0, e.getMessage());
			return rr;
		}
	}

	/**
	 * 处理登录后请求操作，使用POST提交页面，正确或者错误给出状态码
	 * 
	 * @param username
	 * @param password
	 * @param session
	 *            登录成功给一个session获取id等信息。后期拿数据判断是谁登录
	 * @return 通用状态码ResponseResult
	 */
	@RequestMapping(value = "/handle_login.do", method = RequestMethod.POST)
	@ResponseBody
	public ResponseResult<Void> handleLogin(String username, String password, 
			HttpSession session) {
		// 调用userService完成登录,返回ResponseResult对象
		// 登录成功则在返回之前，调用HttpSession对象的setAttribute(String ,Object)对象封装uid以及username
		// 往session里面放的数据一般都是与用户个人相关，例如ID封装到Session,可通过ID查询任何信息，
		// 或者常用数据常用页面（没必要每次根据Session获取id再去数据库查询），
		// 还有一些是页面之间发生了重定向操作，却还需要传递数据，比如页面发生了切换后仍然需要返回到此页面。
		ResponseResult<Void> rr;
		try {
			User user = userService.login(username, password);
			rr = new ResponseResult<Void>(1, "登录成功");
			// 登录成功，把session信息也带过去
			session.setAttribute("uid", user.getId());
			session.setAttribute("username", user.getUsername());
			// 如果登录成功，在返回之前，调用HttpSession对象的setAttr方法
		} catch (UsernameNotFoundException e) {
			rr = new ResponseResult<Void>(0, e.getMessage());
		} catch (PasswordNotMatchException e) {
			rr = new ResponseResult<Void>(-1, e.getMessage());
		}
		return rr;
	}

	// 修改密码
	@RequestMapping(method = RequestMethod.POST, value = "/handle_change_password.do")
	@ResponseBody
	public ResponseResult<Void> handleChangePassword(HttpSession session,
			@RequestParam("old_password") String oldPassword, @RequestParam("new_password") String newPassword) {
		ResponseResult<Void> rr;
		// 从BaseController里面去去拿session的ID
		Integer uid = getUidFromSession(session);
		try {
			Integer affectrows = userService.changePassword(uid, oldPassword, newPassword);
			rr = new ResponseResult<Void>(1, "修改成功");
		} catch (UserNotFoundException e) {
			rr = new ResponseResult<Void>(0, e);
		} catch (PasswordNotMatchException e) {
			rr = new ResponseResult<Void>(-1, e);
		}

		return rr;
	}

	/**
	 * 设计处理层 设计请求路径 /user/handle_edit_profile.do 请求方式：POST
	 * 请求参数：uid,username,gender,phone,email 响应方式：ResponseBody
	 * 
	 * @return ResponseBody
	 */

	@RequestMapping(method = RequestMethod.POST, value = "/handle_edit_profile.do")
	@ResponseBody
	public ResponseResult<Void> handleEditProfile(HttpSession session, String username, Integer gender, String phone,
			String email) {

		// 声明返回值
		ResponseResult<Void> rr;

		// 从BaseController里面去去拿session的ID
		Integer uid = getUidFromSession(session);

		try {
			// 业务类里面尽量抛出异常（都是runtimeException）,控制层去处理
			Integer id = userService.editProfile(uid, username, phone, email, gender);

			// 更新session中的username
			User u = userService.findUserById(uid);
			session.setAttribute("username", u.getUsername());

			rr = new ResponseResult<Void>(1, "修改成功");

		} catch (UserNotFoundException e) {
			rr = new ResponseResult<Void>(-1, e);
		} catch (UsernameAlreadyExistsException e) {
			rr = new ResponseResult<Void>(0, e);
		}

		return rr;

	}

	/**
	 * 检查用户名是否存在。 检查手机号和邮箱方法类似。
	 * 
	 * @param username
	 * @return 返回的是通用的ResponseResult
	 */
	@RequestMapping("/check_username.do")
	@ResponseBody
	public ResponseResult<Void> checkUsername(String username) {
		// 声明返回值
		ResponseResult<Void> rr;
		// 检查用户名是否存在
		boolean result = userService.checkUsernameExists(username);
		// 判断检查结果
		if (result) {
			// 结果为true,则用户名存在
			rr = new ResponseResult<Void>(0, "用户名已经存在");
		} else {
			// 用户名为新的
			rr = new ResponseResult<Void>(1, "用户名可以注册");
		}
		return rr;
	}

	@RequestMapping("/check_email.do")
	@ResponseBody
	public ResponseResult<Void> checkEmail(String email) {
		ResponseResult<Void> rr;
		boolean result = userService.checkEmailExists(email);
		if (result) {
			// 结果为true,则用户名存在
			rr = new ResponseResult<Void>(0, "email已经存在");
		} else {
			// 用户名为新的
			rr = new ResponseResult<Void>(1, "email可以注册");
		}
		return rr;
	}

	@RequestMapping("/check_phone.do")
	@ResponseBody
	public ResponseResult<Void> checkPhone(String phone) {

		ResponseResult<Void> rr;
		boolean result = userService.checkPhoneExists(phone);
		if (result) {
			// 结果为true,则用户名存在
			rr = new ResponseResult<Void>(0, "phone已经存在");
		} else {
			// 用户名为新的
			rr = new ResponseResult<Void>(1, "phone可以注册");
		}
		return rr;
	}

	@RequestMapping("/logout.do")
	public String handleLogout(HttpSession session, HttpServletRequest request) {
		// 清除session信息
		session.invalidate();
		// 重定向到主页 目前是 /user/logout--->>> /main/index.do
		String url = "../main/index.do";
		return "redirect:" + url;
	}

	// UserController中添加处理图片下载的方法(测试demo)

	@RequestMapping(value = "/demo.do", produces = "image/png")
	// produces用于设定content-type属性
	@ResponseBody // 与返回值byte[]配合填充
	// Response的body部分
	public byte[] pngDemo() throws Exception {
		// 读取png图片，返回图片数据
		String path = "cn/tedu/store/controller/touxiang.png";
		// 从包中读取文件
		InputStream in = getClass().getClassLoader().getResourceAsStream(path);
		// 读成byte数组
		// available()方法可以检查流中一次可以读取的字节个数。小文件就是文件的长度。
		byte[] bytes = new byte[in.available()];
		// 将文件中全部数据读取到byte数组中
		in.read(bytes);

		in.close();

		return bytes; 
	}

	@RequestMapping("/check_code.do")
	@ResponseBody
	//用户提交的code： String code
	public ResponseResult<Void> checkCode(String code, HttpSession session) {
		String c = (String) session.getAttribute("code");
		if (c == null) {
			return new ResponseResult<Void>(0, "验证失败!");
		}
		// equalsIgnoreCase忽略大小写比较两个字符串
		// 是否一致
		if (c.equalsIgnoreCase(code)) {
			return new ResponseResult<Void>(1, "验证通过!");
		}
		return new ResponseResult<Void>(0, "验证失败!");
	}
 
	/**
	 * 生成验证码
	 */
	@RequestMapping(value = "/code.do", produces = "image/png")
	@ResponseBody
	public byte[] code(HttpSession session) throws IOException {

		// 利用算法生成一个动态PNG图片
		String code = createCode(4);// 生成验证码
		session.setAttribute("code", code);
		System.out.println(code);
		byte[] bytes = createPng(code);// 生成图片
		return bytes;
	}

	// 生成图片
	private byte[] createPng(String code) throws IOException {
		// 创建图片对象
		BufferedImage img = new BufferedImage(100, 40, BufferedImage.TYPE_3BYTE_BGR);
		// img.setRGB(0, 0, 0x0000ff);
		// img.setRGB(50, 20, 0xffff00);
		// 绘制5000个随机色点
		Random r = new Random();
		for (int i = 0; i < 5000; i++) {
			int x = r.nextInt(img.getWidth());
			int y = r.nextInt(img.getHeight());
			int rgb = r.nextInt(0xffffff);// 随机色
			img.setRGB(x, y, rgb);
		}
		// 利用API绘制验证码字符串
		Graphics2D g = img.createGraphics();
		Color c = new Color(r.nextInt(0xffffff));
		g.setColor(c);
		Font font = new Font(Font.SANS_SERIF, Font.ITALIC, 35);
		g.setFont(font);
		g.drawString(code, 5, 34);
		// 利用API绘制5条混淆线
		for (int i = 0; i < 5; i++) {
			int x1 = r.nextInt(img.getWidth());
			int y1 = r.nextInt(img.getHeight());
			int x2 = r.nextInt(img.getWidth());
			int y2 = r.nextInt(img.getHeight());
			g.drawLine(x1, y1, x2, y2);
		}

		// 将图片对象编码为 png 数据
		// 创建 数组输出流作为缓存区(酱油瓶)
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		// 将png图片数据(酱油)保存到缓存区
		ImageIO.write(img, "png", out);
		out.close();
		// 获取缓存区中的png数据(酱油)

		// 流转成byte、
		byte[] bytes = out.toByteArray();
		return bytes;
	}

	private String createCode(int n) {
		String chs = "abcdefghijkmnpqxy" + "ABCDEFGHJKLMNPQRSTUV" + "34568";

		char[] code = new char[n];
		Random r = new Random();
		for (int j = 0; j < code.length; j++) {
			// 获取四个随机数
			int index = r.nextInt(chs.length());
			code[j] = chs.charAt(index);
		}
		// 字符数组转成字符串 直接new 一个String
		return new String(code);
	}

}

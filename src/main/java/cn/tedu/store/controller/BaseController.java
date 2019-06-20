package cn.tedu.store.controller;

import javax.servlet.http.HttpSession;

//这个类不是new出来的（abstract）
public abstract class BaseController {

	public BaseController() {
	}
	//权限 保证子类可以用就行
	/**
	 * 从session中获取当前登录的用户ID
	 * @param session
	 * @return
	 */
	protected Integer getUidFromSession(HttpSession session) {
		Object objSession =  session.getAttribute("uid");
		if(objSession==null) {
			return null;
		}else {
			return Integer.valueOf(objSession.toString());
		}
	} 

}

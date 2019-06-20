package cn.tedu.store.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.tedu.store.bean.Cart;
import cn.tedu.store.bean.ResponseResult;
import cn.tedu.store.service.ICartService;
import cn.tedu.store.service.ex.ServiceException;

@Controller
@RequestMapping("/cart")
public class CartController
	extends BaseController {
	
	@Resource(name="cartService")
	private ICartService cartService;
	
	@RequestMapping(value="/add.do",
			method=RequestMethod.POST)
	@ResponseBody
	//ResponseResult<Void>查询写类型，增删改不写类型，写Void
	public ResponseResult<Void> 
		handleAddToCart(
			Cart cart,
			HttpSession session) {
		// 声明返回值
		ResponseResult<Void> rr;
		
		// 获取uid
		Integer uid = getUidFromSession(session);
		// 封装uid
		cart.setUid(uid);
		
		// 执行
		try {
			cartService.add(cart);
			rr = new ResponseResult<Void>(1, "加入购物车成功！");
		} catch (ServiceException e) {
			rr = new ResponseResult<Void>(0, e);
		}
		
		// 返回
		return rr;
	}

}

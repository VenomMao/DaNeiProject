package cn.tedu.store.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.tedu.store.bean.Cart;
import cn.tedu.store.mapper.CartMapper;
import cn.tedu.store.service.ex.ServiceException;

@Service("cartService")
public class CartServiceImpl
	implements ICartService {
	
	//@Resource 用于自动装配，优先byName,如果不成功则byType
	@Resource(name="cartMapper")
	private CartMapper cartMapper;

	public Integer add(Cart cart) {
		Integer affectedRows
			= cartMapper.add(cart);
		if (affectedRows == 1) {
			return cart.getId();
		} else {
			throw new ServiceException(
				"将商品添加到购物车时出现未知错误，请联系管理员！");
		}
	}
	
	
	
	
	
	
	
	
}

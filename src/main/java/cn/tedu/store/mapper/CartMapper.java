package cn.tedu.store.mapper;

import cn.tedu.store.bean.Cart;

public interface CartMapper {

	/**
	 * 将商品加入到购物车
	 * @param cart 购物车信息
	 * @return 受影响的行数
	 */
	Integer add(Cart cart);
	
}

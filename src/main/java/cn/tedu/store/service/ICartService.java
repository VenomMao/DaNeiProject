package cn.tedu.store.service;

import cn.tedu.store.bean.Cart;

public interface ICartService {

	/**
	 * 将商品加入到购物车
	 * @param cart 购物车信息
	 * @return 新增加的记录的ID
	 */
	Integer add(Cart cart);
	
}

package cn.tedu.store.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.tedu.store.bean.Goods;

public interface GoodsMapper {
	
	//绝大多数的查询是不需要考虑业务的。
	
	/**
	 * 根据商品分类获取商品列表
	 * @param categoryId 商品分类的ID
	 * @param orderBy 排序方式，值为SQL代码
	 * @param offset 偏移量 即跳过前面多少条数据，如果从头开始应该设置为0
	 * 	如果不需要分页，则取null值即可
	 * @param count 获取数据的数量
	 * @return 返回商品分类列表，如果没有匹配数据，则返回0的list
	 */
	List<Goods> getGoodsListByCategoryId(
			@Param("categoryId") Integer categoryId,
			@Param("orderBy") String orderBy,
			@Param("offset") Integer offset,
			@Param("count") Integer count);
	
	
	/**
	 * 获取某分类的商品的数量
	 * @param categoryId 分类的id
	 * @return 返回商品数量
	 */
	Integer getGoodsCountByCategoryId(Integer categoryId);
	
	
	
	/**
	 * 根据商品id获取商品信息
	 * @param id 商品的ID
	 * @return
	 */
	Goods getGoodsById(Integer id);
	
	
	/**
	 * 根据商品的itemType获取商品列表，即获取同类商品
	 * @param itemType 商品的类型
	 * @return
	 */
	List<Goods> getGoodsListByItemType(String itemType);
	
}
 
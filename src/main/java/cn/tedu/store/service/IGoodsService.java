package cn.tedu.store.service;

import java.util.ArrayList;
import java.util.List;

import cn.tedu.store.bean.Goods;

public interface IGoodsService {
	/**
	 * 查询商品列表时默认的排序方式
	 */
	String ORDER_BY_DEFAULT = "priority DESC";
	
	/**
	 * 查询商品列表时按照价格的升序排序方式
	 */
	String ORDER_BY_PRICE_ASC = "price ASC";
	
	/**
	 * 查询商品列表时价格的降序排序方式
	 */
	String ORDER_BY_PRICE_DESC = "price DESC";
	
	/**
	 * 排列方案
	 */
	String[] ORDER_BY = {
			ORDER_BY_DEFAULT,
			ORDER_BY_PRICE_ASC,
			ORDER_BY_PRICE_DESC
	};
	 
	/**
	 * 每一页默认显示多少条数据
	 */
	Integer COUNT_PER_PAGE = 20;
	
	/**
	 * 设置每页显示的商品数量
	 * @param countPerPage
	 */
	void setCountPerPage(int countPerPage);
	
	Integer getCountPerPage();
	
	/**
	 * 获取某分类的商品的数量
	 * @param categoryId 分类的id
	 * @return 返回商品数量
	 */
	Integer getGoodsCountByCategoryId(Integer categoryId);

	/**
	 * 根据商品分类获取商品列表
	 * 
	 * @param categoryId
	 *            商品分类的ID
	 * @param orderBy
	 *            排序方式，值为SQL代码
	 * @param offset
	 *            偏移量，即跳过前面的多少条数据，如果从头开始获取数据，应该设置为0，如果不需要分页，该参数取null值即可
	 * @param count
	 *            获取数据的数量
	 * @return 商品列表，如果没有匹配的数据，则返回长度为0的List集合
	 */
	List<Goods> getGoodsListByCategoryId(
			Integer categoryId, 
			String orderBy, 
			Integer offset, 
			Integer count);
	
	/**
	 * 根据商品分类获取商品列表，获取到的商品数据是按照优先级(priority)排序的
	 * 
	 * @param categoryId
	 *            商品分类的ID
	 * @param offset 
	 *            偏移量，即跳过前面的多少条数据，如果从头开始获取数据，应该设置为0，如果不需要分页，该参数取null值即可
	 * @param count
	 *            获取数据的数量
	 * @return 商品列表，如果没有匹配的数据，则返回长度为0的List集合
	 */
	List<Goods> getGoodsListByCategoryId(
			Integer categoryId, 
			Integer offset, 
			Integer count);
	
	
	/**
	 * 根据商品分类获取商品列表
	 * @param categoryId  商品分类的ID
	 * @param page 获取第几页的数据
	 * @return
	 */
	List<Goods> getGoodsListByCategoryId(
			Integer categoryId,
			Integer page);
	
	
	
	/**
	 * 根据商品分类获取商品列表
	 * @param categoryId 商品分类的ID
	 * @param orderBy 根据自己方式进行排列而不是系统排列方式  排序方式
	 * @param page 获取第几页的数据
	 * @return
	 */
	List<Goods> getGoodsListByCategoryId(
			Integer categoryId,
			String orderBy,
			Integer page);
	
	
	/**
	 * 根据商品分类获取商品列表
	 * @param categoryId 商品分类的ID
	 * @return
	 */
	List<Goods> getGoodsListByCategoryId(
			Integer categoryId);
	
	
	
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

package cn.tedu.store.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.tedu.store.bean.Goods;
import cn.tedu.store.mapper.GoodsMapper;

@Service("goodsService")
public class GoodsServiceImpl implements IGoodsService {

	@Resource(name = "goodsMapper")
	private GoodsMapper goodsMapper;
	
	
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
	
	
	
	
	//设置一个默认的页码数=20
	private Integer countPerPage = COUNT_PER_PAGE;
	
	//设置get set方法
	public void setCountPerPage(int countPerPage) {
		
		if(countPerPage>=5 && countPerPage<=30) {
			this.countPerPage = countPerPage;
		}
		
	}
	
	public Integer getCountPerPage() {
		return this.countPerPage;
	}
	 

	public List<Goods> getGoodsListByCategoryId(Integer categoryId, String orderBy, Integer offset, Integer count) {
		return goodsMapper.getGoodsListByCategoryId(categoryId, orderBy, offset, count);
	}

	
	public List<Goods> getGoodsListByCategoryId(Integer categoryId, Integer offset, Integer count) {
		return getGoodsListByCategoryId(categoryId, ORDER_BY_DEFAULT, offset, count);
	}

	
	public List<Goods> getGoodsListByCategoryId(Integer categoryId, Integer page) {
		return getGoodsListByCategoryId(categoryId, ORDER_BY_DEFAULT, page);
	}

	
	public List<Goods> getGoodsListByCategoryId(Integer categoryId, String orderBy, Integer page) {
		Integer offset =(page-1)*20;
		
		Integer count = getCountPerPage();
		return getGoodsListByCategoryId(categoryId, orderBy, offset, count);
	}

	
	public List<Goods> getGoodsListByCategoryId(Integer categoryId,String orderBy) {
		return getGoodsListByCategoryId(categoryId, orderBy, 1);
	}

	
	public List<Goods> getGoodsListByCategoryId(Integer categoryId) {
		return getGoodsListByCategoryId(categoryId, ORDER_BY_DEFAULT, 1);
	}
	
	/**
	 * 获取某分类的商品的数量
	 * @param categoryId 分类的id
	 * @return 返回商品数量
	 */
	public Integer getGoodsCountByCategoryId(Integer categoryId) {
		return goodsMapper.getGoodsCountByCategoryId(categoryId);
	}

	
	/**
	 * 根据商品id获取商品信息 
	 * @param id 商品的ID
	 * @return
	 */
	public Goods getGoodsById(Integer id) {
		
		return goodsMapper.getGoodsById(id);
	}

	
	/**
	 * 根据商品的itemType获取商品列表，即获取同类商品
	 * @param itemType 商品的类型
	 * @return
	 */
	public List<Goods> getGoodsListByItemType(String itemType) {
		return goodsMapper.getGoodsListByItemType(itemType);
	}

} 

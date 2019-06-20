package cn.tedu.store.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.tedu.store.bean.GoodsCategory;

public interface IGoodsCategoryService {
	
	/**
	 * 根据父级分类ID获取商品分类列表
	 * @param parentId 父级分类ID
	 * @param offset 偏移量，即跳过前面多少条数据，如果从头开始应该设置为0
	 * 	如果不需要分页，则取null值即可、
	 * @param count 获取数据的数量 
	 * @return 返回商品分类列表，如果没有匹配数据，则返回0的list
	 */
	List<GoodsCategory> getGoodsCategoryListByParentId(
			@Param("parentId") Integer parentId,
			@Param("offset") Integer offset,
			@Param("count") Integer count);
}

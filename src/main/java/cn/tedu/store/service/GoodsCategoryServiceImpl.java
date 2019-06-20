package cn.tedu.store.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.tedu.store.bean.GoodsCategory;
import cn.tedu.store.mapper.GoodsCategoryMapper;

/**
 * 查询的业务一般不复杂，麻烦的是增删改
 * @author Administrator
 *
 */

@Service("goodsCategoryService")
public class GoodsCategoryServiceImpl implements IGoodsCategoryService {
	
	@Resource(name="goodsCategoryMapper")
	private GoodsCategoryMapper goodsCategoryMapper;
	
	public List<GoodsCategory> getGoodsCategoryListByParentId(Integer parentId, Integer offset, Integer count) {
		return goodsCategoryMapper.getGoodsCategoryListByParentId(parentId, offset, count);
	}

}

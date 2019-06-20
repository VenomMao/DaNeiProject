package cn.tedu.store.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.tedu.store.bean.Goods;
import cn.tedu.store.bean.GoodsCategory;
import cn.tedu.store.service.IGoodsCategoryService;
import cn.tedu.store.service.IGoodsService;

@Controller()
@RequestMapping("/main")
public class MainController extends BaseController{
	
	@Resource(name="goodsCategoryService")
	private IGoodsCategoryService goodsCategoryService;
	
	@Resource(name="goodsService")
	private IGoodsService goodsService;
	
	
	@RequestMapping("/index.do")
	public String showIndex(ModelMap modelMap) {
		//声明电脑分类的数据
		List<List<GoodsCategory>> computerCategories = new 
				ArrayList<List<GoodsCategory>>();
		
		//声明电脑分类中的前三名商品
		List<Goods> computers;
		// 获取电脑分类中排名前3的商品列表
		computers = goodsService.getGoodsListByCategoryId(163, 0, 3);


		//获取161的3个2级分类
		List<GoodsCategory> categories161 = 
				goodsCategoryService.getGoodsCategoryListByParentId(161, 0, 3);
		
		//computerCategories.add(categories);
		
		//获取各二级分类的三级分类
		
		List<GoodsCategory> subCategories;
		
		for (GoodsCategory goodsCategory : categories161) {
			
			subCategories = 
						goodsCategoryService.getGoodsCategoryListByParentId(
								goodsCategory.getId(), null, null);
			
			computerCategories.add(subCategories);
		}
		
		//封装需要转发的数据
		
		modelMap.addAttribute("categories161", categories161);
		modelMap.addAttribute("computerCategories", computerCategories);
		
		// 转发 获取电脑分类中排名前3的商品列表
		modelMap.addAttribute("computers", computers);
		
		//执行转发
		return "index";
	}
}

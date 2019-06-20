package cn.tedu.store.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.tedu.store.bean.Goods;
import cn.tedu.store.service.IGoodsService;

@Controller
@RequestMapping("/goods")
public class GoodsController extends BaseController{
	
	//get请求
	@Resource(name="goodsService")
	private IGoodsService goodsService;
	
	
	/**
	 * 设计请求：/goods/list.do
	 * 请求类型 ：GET
	 * 请求参数 ：category_id=x&page=x&order_by=x
	 * 响应方式 ：转发
	 * 拦截器：不拦截
	 * @param categoryId
	 * @param orderBy
	 * @param page
	 * @param session
	 * @return
	 * 
	 * @RequestParam(value="order_by",required=false)
	 * 这里不需要的，所以前端传过去参数不需要order_by，required=false设置
	 * 
	 * @RequestParam 当请求参数的名称与控制器中的方法的参数不一致时，使用。
	 * 
	 * @RequestParam 使用该注解后，该参数默认情况是必须的，即如果发出的请求中没有
	 * 给出指定的参数，会导致400错误！如果需要设置该参数不是必须的，需要
	 * value="order_by",required=false。
	 */
	@RequestMapping("/list.do")
	public String getGoodsListByCategoryId(
			@RequestParam(value="category_id",required=true) Integer categoryId,
			@RequestParam(value="order_by",required=false) Integer orderBy,
			@RequestParam("page") Integer page,
			HttpSession session,
			ModelMap modelMap) {
		
		//声明 需要转发的数据
		List<Goods> goodsList;
		//声明需要转发的数据 页码数
		Integer goodsCount;
		//总共的页码
		Integer pages;
		//每页显示多少条数据
		Integer countPerPage;
		
		//判断参数 (因为涉及到数据安全和业务规则)
		String orderByStr;
		
		if(categoryId==null || categoryId < 1) {
			modelMap.addAttribute("msg", "请求参数有错误！");
			return "error";
		}
		
		//获取数据 （多少个数，多少页码）
		goodsCount = goodsService.getGoodsCountByCategoryId(categoryId);
		
		
		//判断page		
		if(page==null || page < 1) {
			page = 1;
		}
		
		if(orderBy==null || orderBy > IGoodsService.ORDER_BY.length || orderBy < 0) {
			orderBy = 0;
		}
		orderByStr = IGoodsService.ORDER_BY[orderBy];
		
		//设置每页显示多少条商品
		goodsService.setCountPerPage(IGoodsService.COUNT_PER_PAGE);
		
		//获取数据
		goodsList = goodsService.getGoodsListByCategoryId(categoryId, orderByStr, page);
		
		//获取每页显示的商品数量
		countPerPage = goodsService.getCountPerPage();
		
		//计算总页数
		pages = goodsCount/countPerPage;
		pages +=goodsCount%countPerPage==0?0:1;
		
		
		
		for (Goods goods : goodsList) {
			System.out.println("获取数据----");
			System.out.println("goods"+goods);
		}
		
		
		
		//转发数据
		modelMap.addAttribute("goodsList", goodsList);
		
		
		modelMap.addAttribute("goodsCount", goodsCount);
		
		
		modelMap.addAttribute("countPerPage", countPerPage);
		
		
		modelMap.addAttribute("pages", pages);
		
		//当前页
		modelMap.addAttribute("currentPage", page);
		
		
		modelMap.addAttribute("categoryId", categoryId);
		
		//转发jsp文件名 
		return "goods_list";
		
	}
	
	/**
	 *   / good/details.do?id=xx
	 *   请求方式：GET
	 *   请求参数：id=xx
	 *   响应方式：转发
	 *   登录拦截器：不要求必须登录
	 *   
	 *   @RequestParam(value="id",required=true) 强制要求存在
	 */
	@RequestMapping("details.do")
	public String showGoodsDetails(@RequestParam(value="id",required=true) Integer id,ModelMap map) {
		//声明需要转发到jsp的数据
		Goods goods;
		
		if(id==null) {
			return "error";
		}
		
		//获取需要转发到jsp的数据
		goods = goodsService.getGoodsById(id);
		
		
		
		if(goods!=null) {
			//获取数据后，还要获取同类型商品的列表,然后再转发
			List<Goods> goodsList = 
					goodsService.getGoodsListByItemType(goods.getItemType());	
			
			//封装需要转发到jsp的数据
			map.addAttribute("goods", goods);
			
			//获取同类型商品列表后，再转发出去List
			map.addAttribute("goodsList", goodsList);
			
			//执行转发
			return "goods_details";
		}else {
			//没有获取到数据
			return "error";
		}
		
		
	}
	
	
	
	
	
	
	
	
	
	
}

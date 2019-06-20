package cn.tedu.store.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.tedu.store.bean.Address;
import cn.tedu.store.bean.ResponseResult;
import cn.tedu.store.service.AddressServiceImpl;
import cn.tedu.store.service.IAddressService;
import cn.tedu.store.service.ex.DataNotFoundException;

@Controller
@RequestMapping("/address")
public class AddressController extends BaseController{

	public AddressController() {
	}
	
	@Resource
	private IAddressService addressService;
	
	/**
	 * 设计显示‘地址管理’的请求：
	 * 请求类型：get
	 */
	@RequestMapping("/list.do")
	public String showList() {
		return "address_list";
	}
	
	/**
	 * 设计“获取用户的收货地址列表”的请求：
	 * 请求方式：get 则直接@RequestMapping("/get_list.do")
	 * 请求参数：uid
	 * 响应方式:ResponseBody
	 * 需要拦截
	 */
	
	@RequestMapping("/get_list.do")
	@ResponseBody
	public ResponseResult<List<Address>> getAddressListByUid(HttpSession session) {
		//声明返回值
		ResponseResult<List<Address>> rr;
		//获取数据
		List<Address> list = addressService.getAddressListByUid(getUidFromSession(session));
		//创建返回值对象
		rr = new ResponseResult<List<Address>>(1, list);
		return  rr;
	}
	
	
	/**
	 * 设计处理增加新地址的请求
	 * /address/add.do 请求方式：post
	 *
	 */
	@RequestMapping(value="/add.do",method=RequestMethod.POST)
	@ResponseBody
	public ResponseResult<Void> handleAdd(Address address,HttpSession session){
		//测试
		System.out.println("handleAdd()开始运行了");
		System.out.println("address"+address);
		//获取uid
		Integer uid = getUidFromSession(session);
		//将uid封装到address中
		address.setUid(uid);
		addressService.add(address);
		//创建返回值
		ResponseResult<Void> rr = new ResponseResult<Void>(1,"增加新收货地址成功");
		return rr;
	}
	
	
	/**
	 * 设计处理“删除地址”的请求
	 * GET请求
	 * 请求参数id=xx&uid=xx,其中uid直接从session获取
	 * 不需要调整拦截器配置
	 * 查询时候才ResponseResult<Void>，考虑里面泛型。因为需要给数据。
	 * 
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public ResponseResult<Void> handleDelete(Integer id,HttpSession session){
		ResponseResult<Void> rr ;
		
		try {
			 addressService.delete(id, getUidFromSession(session));
			 rr = new ResponseResult<Void>(1, "删除成功");
		} catch (DataNotFoundException e) {
			rr = new ResponseResult<Void>(0, "删除失败");
		}
		
		return rr;
	}
	
	
	/**
	 * 修改收货地址
	 * @param address 至少包括被修改数据的id，uid，新数据。
	 * @return 受影响的行数 0或者1
	 * 
	 * 请求类型POST
	 * 请求参数：表单中所有参数＋id+uid
	 * 
	 * session处理都是在Controller里面处理
	 */
	@RequestMapping(value="/handle_update.do",method=RequestMethod.POST)
	@ResponseBody
	public ResponseResult<Void> handleUpdate(Address address,HttpSession session){
		ResponseResult<Void> rr;
		
		//将uid取出并封装到address中
		Integer uid= getUidFromSession(session);
		
		address.setUid(uid);
		
		Integer affc =  addressService.update(address);
		if(affc==1) {
			rr = new ResponseResult<Void>(1, "修改成功");
		}else {
			rr = new ResponseResult<Void>(0, "修改不成功");
		}
		
		
		return rr;
	}
	
	
	
	/**
	 * 获取指定的数据的请求
	 * @param id
	 * @param session
	 * @return 
	 * 请求参数:GET
	 * 响应方式：@ResponseBody
	 */
	@RequestMapping("/get.do")
	@ResponseBody 
	public ResponseResult<Address> getAddressByIdAndUid(Integer id,HttpSession session){
		ResponseResult<Address> rr;
		
		Address data = addressService.getAddressByIdAndUid(id, getUidFromSession(session));
		
		rr = new ResponseResult<Address>(1, data);
		
		return rr;
	}
	
	
	/**
	 * 设为默认
	 */
	@RequestMapping("/set_default.do")
	@ResponseBody
	public ResponseResult<Void> handleSetDefault(Integer id,HttpSession session){
		ResponseResult<Void> rr;
		
		try {
			addressService.setDefault(id, getUidFromSession(session));
			rr = new ResponseResult<Void>(1, "设为默认地址成功");
		} catch (Exception e) {
			rr = new ResponseResult<Void>(0, "设为默认地址不成功");
		}
		
		//返回
		return rr;
	}
	
	
}

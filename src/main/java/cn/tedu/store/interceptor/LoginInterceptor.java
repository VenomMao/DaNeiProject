package cn.tedu.store.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 使用了登录拦截后，AJAX请求可能存在问题（ajax发送4和200状态码），
 * 而登录拦截直接重定向（302）到新页面（登录信息过期），所以ajax处理不走success,所以前端页面ajax
 * 需要一个error判断去重新定位到主页。
 *  * @author Administrator
 *
 */
public class LoginInterceptor implements HandlerInterceptor{

	public LoginInterceptor() {
	}

	public boolean preHandle(HttpServletRequest request, 
			HttpServletResponse response, Object handler)
			throws Exception { 
		
		//日志
		System.out.println("LoginInterceptor.preHandle");
		System.out.println("\t reruest:"+request);
		System.out.println("\t reruest:"+response);
		System.out.println("\t reruest:"+handler);
		
		//获取HttpSession对象
		HttpSession session = request.getSession();
		//判断是否登录
		if(session.getAttribute("uid")==null) {
			//确定重定向到的页面
			
			String url = request.getContextPath()+"/main/index.do";
			
			//没登录,重定向(302+网址跳转)到主页
			response.sendRedirect(url);
			//拦截(必须重定向之后给拦截，false)
			return false;
		}
		//放行
		return true; 
	}

	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

}

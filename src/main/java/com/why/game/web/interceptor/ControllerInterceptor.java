package com.why.game.web.interceptor;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class ControllerInterceptor implements HandlerInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(ControllerInterceptor.class);
	
    /** 
     * 在业务处理器处理请求之前被调用
     * 如果返回false
     *     从当前的拦截器往回执行所有拦截器的afterCompletion(),再退出拦截器链 
     *  
     * 如果返回true
     *    执行下一个拦截器,直到所有的拦截器都执行完毕
     *    再执行被拦截的Controller
     *    然后进入拦截器链,
     *    从最后一个拦截器往回执行所有的postHandle()
     *    接着再从最后一个拦截器往回执行所有的afterCompletion()
     */  
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	String queryStr = request.getQueryString();
    	logger.info(handler.getClass().getSimpleName()+", "+request.getRequestURI()+", "+paramMap(request)+(queryStr == null ? "" : ", "+queryStr));
    	
    	//返回json格式，其实mvc.xml的mappingJacksonHttpMessageConverter会自动处理了
    	//response.setContentType("application/json;charset=UTF-8");
    	
    	//跨域许可，支持HTTP方法等信息
    	response.setHeader("access-control-allow-origin", "*");
    	response.setHeader("Access-Control-Allow-Credentials", "true");
    	response.setHeader("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE, OPTIONS");
    	response.setHeader("Access-Control-Max-Age", "1000");
    	response.setHeader("Access-Control-Allow-Headers", "Content-Type, Content-Range, Content-Disposition, Content-Description");
    	
    	return true;
    }
    
    @SuppressWarnings("unchecked")
	private Map<String, String> headerMap(HttpServletRequest request){
    	Enumeration<String> names = request.getHeaderNames();
    	Map<String, String> headerMap = new HashMap<String, String>();
    	while(names.hasMoreElements()){
    		String name = names.nextElement();
    		headerMap.put(name, request.getHeader(name));
    	}
    	//logger.info("header : "+headerMap.toString());
    	return headerMap;
    }
    
    @SuppressWarnings("unchecked")
	private Map<String, String> paramMap(HttpServletRequest request){
    	Map<String, String> paramMap = new HashMap<String, String>();
    	Map<String, String[]> map = request.getParameterMap();
    	for(Object key:map.keySet()){
    		paramMap.put(key.toString(), Arrays.toString(map.get(key)));
    	}
    	return paramMap;
    }

    //在业务处理器处理请求执行完成后,生成视图之前执行的动作   
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    
    }

    /** 
     * 在DispatcherServlet完全处理完请求后被调用  
     *  
     *   当有拦截器抛出异常时,会从当前拦截器往回执行所有的拦截器的afterCompletion() 
     */  
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

}

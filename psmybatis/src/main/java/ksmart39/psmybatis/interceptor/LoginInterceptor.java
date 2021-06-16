package ksmart39.psmybatis.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class LoginInterceptor implements HandlerInterceptor{
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HttpSession session = request.getSession();
		String sessionId = (String) session.getAttribute("SID");									//반환형이 오브젝트이니 스트링으로 다운캐스팅 해야함
		String sessionLevel = (String) session.getAttribute("SLEVEL");
		String requestUri = request.getRequestURI();
		
		if(sessionId == null) {
			response.sendRedirect("/login");
			return false;																			// 무한 루프 방지하고자 return false처리
		}else {
			if(sessionLevel.equals("2")) {															// equals는 null일 경우 무조건 에러남 
				if(requestUri.equals("/memberList") || requestUri.indexOf("addMember") > -1) {		// 하지만 지금 에러가 안나는 이유는 바로 윗부분
					response.sendRedirect("/");														// 세션아이디 널체크를 하기 때문에 널일 경우가 없다.
					return false;																	// 그래서 indexOf를 사용하면 동일한 기능을 한다.
				}
			}
			if(sessionLevel.equals("3")) {
				if(requestUri.equals("/memberList") || requestUri.indexOf("addMember") > -1) {
					response.sendRedirect("/");	
					return false;
				}
			}
		}
		/**
		 * 	상품등록, 상품조회 까지 만들고 나서 합시다! 하핫
		 *  else {
			//1. session의 회원레벨 가져오기
			
			//2. request.getRequestURI() == "memberList" 같다면 관리자를 제외한 나머지 등급은 response.sendRedirect("/"); 로 향할 수 있도록 조건문 만들기
			}
		 */
		return true;
	}
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}
}

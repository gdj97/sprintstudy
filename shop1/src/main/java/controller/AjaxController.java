package controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import service.ShopService;
/*
 * @Controller : @Component + Controller 기능
 *    @Component : 객체화
 *    Controller : 요청 url 맞춰 해당 메서드 호출 기능
 *    
 *    리턴타입 :
 *      - ModelAndView : 뷰이름 + 전달데이터
 *      - String       : 뷰이름, 데이터는 Model 객체에 따로 전송가능
 *      
 * @RestController :  @Component + Controller + 클라이언트로 직접 데이터 전송(뷰없음)
 *     리턴타입 :
 *       - String : 클라이언트에 전송할 문자열데이터
 *       - Object(Map<DTO>,List<DTO>) : 클라이언트로 직접 객체 전달. JSON 형식으로 전달됨
 *
 *  Spring 4.0 이후에 RestController 기능 추가됨.
 *  이전에는 요청메서드마다 @ResponseBody 기능을 추가하여 사용함      
 */
@RestController  //view 없이 직접 데이터를 클라이언트로 전송
@RequestMapping("ajax")
public class AjaxController {
	@Autowired
	private ShopService service;
	
	@RequestMapping("select1")
	public String sidoSelect1(String si, String gu,HttpServletRequest request) {
		return service.sidoSelect1(si,gu,request);
	}
}

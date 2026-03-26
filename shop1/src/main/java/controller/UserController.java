package controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import dto.User;
import service.UserService;


@Controller
@RequestMapping("user")
public class UserController {
	@Autowired
	private UserService service;
	
	//http://localhost:8080/shop1/user/join => /WEB-INF/view/user/join.jsp 
	@GetMapping("*") //Get 방식의 모든 요청
	public ModelAndView form() {
		ModelAndView mav = new ModelAndView();
		mav.addObject(new User());
		return mav;//view: null => url과 같은 위치의 jsp 페이지 요청
	}
	@PostMapping("join")
	public ModelAndView join(@Valid User user,BindingResult bresult) {
		ModelAndView mav = new ModelAndView();
		if(bresult.hasErrors()) { //@Valid에서 검증한 오류 존재?
			//messages.properties 파일에서 코드를 찾아서 메세지를 출력
			bresult.reject("error.input.user"); //글로벌 오류로 등록
			bresult.reject("error.input.check");
			return mav;
		}
		//db에 등록
		try {
		    service.userInsert(user);
		} catch (DataIntegrityViolationException e) {//키값 중복된 경우. 
			e.printStackTrace();
			bresult.reject("error.duplicate.user");
			return mav;
		} catch (Exception e) { //중복 예외의 예외 
			e.printStackTrace();
			return mav;
		}
		mav.setViewName("redirect:login"); //http://localhost:8080/shop1/user/login 페이지를 재요청(redirect)
		return mav;		
	}
	/*
	 * @Valid 어노테이션으로 유효성검증시 User 클래스의 userid,password 외에 name, email,birthday 등의 입력되어야 함
	 * 직접 @Valid 역할 구현해야 함
	 */
	@PostMapping("login")
	public ModelAndView login(User user,BindingResult bresult,HttpSession session) {
		ModelAndView mav = new ModelAndView();
		// 직접 입력값 검증구현하기
		if(user.getUserid()==null || 
			(user.getUserid().trim().length() < 3 || user.getUserid().trim().length() > 10)) {
			bresult.rejectValue("userid", "error.required"); //(프로퍼티,오류코드)
		}
		if(user.getPassword()==null ||
			(user.getPassword().trim().length() < 3 || user.getPassword().trim().length() > 10)) {
			bresult.rejectValue("password", "error.required");
		}		
		if(bresult.hasErrors()) {
			bresult.reject("error.login.check");
			return mav;
		}
		/*
		 * 아이디와 비밀번호가 정상적으로 입력된 경우
		 * 1. userid에 맞는 정보를 db에서 조회하기
		 *    userid가 없으면 아이디가 없습니다. (error.login.userid)
		 * 2. db에 등록된 비밀번호와, 입력된 비밀번호를 비교.
		 *    일치 : session 객체에 loginUser이름으로 User 객체를 속성으로 등록
		 *          페이지를 mypage로 페이지 이동
		 *    불일치 : 비밀번호를 확인하세요. (error.login.password)      
		 */
		User dbUser = service.getUser(user.getUserid());
		if(dbUser == null) {
			bresult.reject("error.login.userid");
			return mav;
		}
		if(dbUser.getPassword().equals(user.getPassword())) {
			session.setAttribute("loginUser", dbUser);
			mav.setViewName("redirect:mypage?userid="+user.getUserid());
		} else {
			bresult.reject("error.login.password");
			return mav;
		}
		return mav;
	}
	
}
package controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import dto.User;
import dto.UserPassword;
import exception.ShopException;
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
	/* AOP 클래스로 설정 
	 * 1. 로그인 상태
	 * 2. 관리자만 제외하고 본인 정보만 조회가능
	 */
	@RequestMapping("mypage")
	public ModelAndView idCheckMypage(String userid,HttpSession session) {
		ModelAndView mav = new ModelAndView();
		User user = service.getUser(userid);
		mav.addObject("user", user);
		return mav;
	}	
	//AOP 설정되도록 메서드의 선언부 구현 필요
	@GetMapping({"update","delete"})
	public ModelAndView idCheckUser(String userid,HttpSession session) {
		ModelAndView mav = new ModelAndView();
		User user = service.getUser(userid);
		mav.addObject("user",user);
		return mav;
	}
	@RequestMapping("logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:login";
	}
	@PostMapping("update")
	public String update(@Valid User user,BindingResult bresult,HttpSession session ) {
		//입력값 검증
		if(bresult.hasErrors()) {
			bresult.reject("error.update.user");
			return null;
		}
		//비밀번호 검증
		User loginUser = (User)session.getAttribute("loginUser");
		if(!loginUser.getPassword().equals(user.getPassword())) {
			bresult.reject("error.update.password");
			return null;
		}
		//비밀번호 일치
		try {
			service.userUpdate(user);
			if(loginUser.getUserid().equals(user.getUserid())) { //본인 정보 수정하는 경우
			   session.setAttribute("loginUser", user);          //로그인 정보 변경
			}
			return "redirect:mypage?userid="+user.getUserid();
		} catch(Exception e) {
			e.printStackTrace();
			throw new ShopException("고객 수정시 오류 발생","update?userid=" + user.getUserid());
		}
	}	
	/*
	 * LoginAspect.userIdCheck() 메서드 실행 설정
	 * 탈퇴 검증
	 * 1. 관리자인 경우 탈퇴 불가
	 * 2. 비밀번호 검증 => 로그인된 비밀번호와 비교
	 *     본인탈퇴시 : 본인 비밀번호로 검증
	 *     관리자타인탈퇴 : 관리자 비밀번호로 검증
	 * 3. 비밀번호 불일치
	 *    메세지 출력 후 delete 페이지로 이동
	 * 4. 비밀번호 일치
	 *    db에서 사용자정보 삭제
	 *    본인탈퇴 : 로그아웃. login페이지로 이동
	 *    관리자 타인 탈퇴 : admin/list 페이지 이동    
	 */
	@PostMapping("delete")
	public String idCheckDelete(String password,String userid,HttpSession session) {
		if(userid.equals("admin")) {
			throw new ShopException("관리자는 탈퇴 불가입니다","mypage?userid=admin");
		}
		//비밀번호 검증. => 로그인 정보로 비교
		User loginUser = (User)session.getAttribute("loginUser");
		//password : 입력된 비밀번호
		//loginUser.getPassword() : db에 등록된 비밀번호
		if(!password.equals(loginUser.getPassword())) {
			throw new ShopException("비밀번호를 확인하세요","delete?userid="+userid);
		}
		//비밀번호가 일치 : db에서 userid에 해당하는 정보 삭제
		try {
			service.userDelete(userid);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ShopException("탈퇴 거래시 오류가 발생했습니다.","delete?userid="+userid);
		}
		//회원 정보 삭제 성공
		if(loginUser.getUserid().equals("admin")) {
			return "redirect:../admin/list";
		} else {
			session.invalidate();
			return "redirect:login";
		}
	}
	/*
	 * 비밀번호 변경 화면 출력
	 * 1. login 검증 => AOP 클래스
	 *    LoginAspect.loginCheck()
	 *     => pointcut : UserController 클래스에서 메서드이름이 loginCheck로 시작하고, 매개변수의 마지막이 HttpSession인
	 *                   메서드로 설정
	 *        advice : Around           
	 */
	@GetMapping({"password","password2"})
	public String loginCheckForm(HttpSession session,Model model) {
		model.addAttribute(new UserPassword());
		return null;
	}
	/*
	 * 1. login 검증 => AOP 클래스
	 * 2. 현재비밀번호와 입력비밀번호 비교
	 *    일치 : db 수정. 로그인정보 수정. mypage로 페이지 이동
	 *    불일치 : 오류메세지 출력. password 페이지 이동
	 */
	@PostMapping("password")
	public String loginCheckPassword(String password, String chgpass,HttpSession session) {
		User loginUser = (User)session.getAttribute("loginUser");
		if(!password.equals(loginUser.getPassword())) {
			throw new ShopException("비밀번호 오류입니다.","password");
		}
		try {
			service.userChgPass(loginUser.getUserid(),chgpass);
			loginUser.setPassword(chgpass);
		} catch(Exception e) {  //db 수정시 오류 발생
			e.printStackTrace();
			throw new ShopException("비밀번호 변경시 db 오류입니다.","password");
		}
		return "redirect:mypage?userid=" + loginUser.getUserid();
	}
	/*
	 * @PathVariable : {url} 값을 매개변수로 전달. url에 해당하는 값을  String url 매개변수로 전달
	 *   idsearch 요청 : url=id
	 *   pwsearch 요청 : url=pw
	 */
	@PostMapping("{url}search") // xxsearch 요청시 호출되는 메서드(idsearch, pwsearch 요청)
	public ModelAndView search(User user, BindingResult bresult, @PathVariable String url) {
		ModelAndView mav = new ModelAndView();
		String code = "error.userid.search";
		if(url.equals("pw")) { //pwsearch 요청인 경우
			code = "error.password.search";
			if(user.getUserid() == null || user.getUserid().trim().equals("")) {
				bresult.rejectValue("userid", "error.required");
			}
		}
		if(user.getEmail() == null || user.getEmail().trim().equals("")) {
			bresult.rejectValue("email", "error.required");
		}
		if(user.getPhoneno() == null || user.getPhoneno().trim().equals("")) {
			bresult.rejectValue("phoneno", "error.required");
		}
		if(bresult.hasErrors()) {
			bresult.reject("error.input.check");
			return mav;
		}
		//입력값이 정상인 경우
		String result = service.getSearch(user);
		
		if(result==null) {
			bresult.reject(code);
			return mav;
		}
		mav.addObject("result",result);
		mav.addObject("title",((url.equals("pw")?"비밀번호":"아이디")));
		mav.setViewName("search");
		return mav;
	}	
	//==================================
	@PostMapping("password2")
	public String loginCheckPassword2(UserPassword userpass,BindingResult bresult,HttpSession session) {
		if(userpass.getPassword() == null || userpass.getPassword().trim().equals("")) {
			bresult.rejectValue("password", "error.required");
		}
		if(userpass.getChgpass() == null || userpass.getChgpass().trim().equals("")) {
			bresult.rejectValue("chgpass", "error.required");
		}
		if(userpass.getChgpass2() == null || userpass.getChgpass2().trim().equals("")) {
			bresult.rejectValue("chgpass2", "error.required");
		}
		if(bresult.hasErrors()) {
			bresult.reject("error.input.check"); //global 오류등록
			return null;
		}
		//변경비밀번호와 변경비밀번호 재입력값이 같은지 검증
		if(!userpass.getChgpass().equals(userpass.getChgpass2())) {
			bresult.reject("error.password.equals");
			return null;
		}
		User loginUser = (User)session.getAttribute("loginUser");
		if(!userpass.getPassword().equals(loginUser.getPassword())) {
			throw new ShopException("비밀번호 오류입니다.","password2");
		}
		try {
			service.userChgPass(loginUser.getUserid(),userpass.getChgpass()); //비밀번호를 db에서 변경
			loginUser.setPassword(userpass.getChgpass());   //세션정보의 비밀번호 변경
		} catch(Exception e) {  //db 수정시 오류 발생
			e.printStackTrace();
			throw new ShopException("비밀번호 변경시 db 오류입니다.","password2");
		}
		return "redirect:mypage?userid=" + loginUser.getUserid();
	}
	
}
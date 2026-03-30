package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.mchange.net.MailSender;

import dto.Mail;
import dto.User;
import exception.ShopException;
import service.UserService;
/*
 * AdminController의 모든 메서드는 관리자 로그인된 경우만 실행됨
 * => AOP 설정 필요(AdminLoginAspect 클래스)
 */
@Controller
@RequestMapping("admin")
public class AdminController {
	@Autowired
	private UserService service;
	
	@RequestMapping("list")
	public ModelAndView list(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		//list : 모든 useraccount 테이블의 정보
		List<User> list = service.userList();
		mav.addObject("list",list);
		return mav;
	}
	/*
	 * String[] idchks : 화면에서 전송된 idchks 파라미터가 여러개인 경우. request.getParameterValues(파라미터이름)
	 */
	@PostMapping("mailform")
	public String mailform(String[] idchks, Model model) {
		if(idchks == null || idchks.length == 0) {
			throw new ShopException("메일을 보낼 대상자를 선택하세요","list");
		}
		//db에서 idchks내의 userid값에 해당하는 User 객체들 조회
		List<User> list = service.getUserList(idchks);
		Mail mail = new Mail();
		StringBuilder recipient = new StringBuilder();
		for (User u : list) {
			recipient.append(u.getUsername())  //테스트1<test1@aaa.bbb>,테스트2<test2@aaa.bbb>,
			         .append("<").append(u.getEmail()).append(">,");
		}
		mail.setRecipient(recipient.toString());
		mail.setGoogleid(""); //본인의 구글 id
		mail.setGooglepw(""); //본인의 앱비밀번호
		model.addAttribute("mail",mail);
		return "admin/mail";
	}
	   /*
	    * 구글 smtp 서버를 이용하여 메일 전송하기
	    * 1. 구글계정에접속하여 2단계 인증 설정하기
	    * 2. 앱비밀번호 생성하기 :
	    * 3. 생성된 앱비밀번호를 메모장을 이용하여 저장하기
	    * 4. pom.xml에 mail 관련 설정 추가
	    * 5. mail.properties 파일 /resources/ 폴더에 생성하기
	    */
	
	@PostMapping("mail")
	public String mail(@Valid Mail mail,BindingResult bresult,Model model,HttpServletRequest request) {
		if(bresult.hasErrors()) {
			return null;
		}
		Properties prop = new Properties();
		try {
			String path = request.getServletContext().getRealPath("/") + "/WEB-INF/classes/mail.properties";
			FileInputStream fis = new FileInputStream(path);
			prop.load(fis);
			prop.put("mail.smtp.user", mail.getGoogleid());
		} catch(IOException e) {
			e.printStackTrace();
		}
		if(mailSend(mail,prop)) {
			model.addAttribute("message","메일 전송이 완료되었습니다.");
		} else {
			model.addAttribute("message","메일 전송을 실패했습니다.");
		}
		model.addAttribute("url","list");
		return "alert";
	}
	private boolean mailSend(Mail mail, Properties prop) {
		MyAuthenticator auth = new MyAuthenticator(mail.getGoogleid(),mail.getGooglepw());
		Session session = Session.getInstance(prop,auth);
		MimeMessage mailmsg = new MimeMessage(session);
		try {
			mailmsg.setFrom(new InternetAddress(mail.getGoogleid() + "@gmail.com"));
			List<InternetAddress> addrs = new ArrayList<InternetAddress>();
			String[] emails = mail.getRecipient().split(",");
			for(String email : emails) {
				try {
					addrs.add(new InternetAddress(new String(email.getBytes("utf-8"),"8859_1")));
				} catch (UnsupportedEncodingException ue) {
					ue.printStackTrace();
				}
			}
			InternetAddress[] arr = new InternetAddress[emails.length];
			for(int i=0;i<addrs.size();i++) {
				arr[i]=addrs.get(i);
			}
			mailmsg.setRecipients(Message.RecipientType.TO,arr);
			mailmsg.setSentDate(new Date());
			mailmsg.setSubject(mail.getTitle());
			MimeMultipart multipart =new MimeMultipart();
			MimeBodyPart message = new MimeBodyPart();
			message.setContent(mail.getContents(),mail.getMtype());
			multipart.addBodyPart(message);
			for(MultipartFile mf : mail.getFile1()) {
				if ((mf != null) && (!mf.isEmpty())) {
					multipart.addBodyPart(bodyPart(mf));
				}
			}
			mailmsg.setContent(multipart);
			Transport.send(mailmsg);
			return true;
		} catch(MessagingException me) {
			me.printStackTrace();
		}
		return false;
	}	
	private BodyPart bodyPart(MultipartFile mf) {
		MimeBodyPart body = new MimeBodyPart();
		String orgFile = mf.getOriginalFilename();
		String path = "c:/mailupload/";
		File f1 = new File(path);
		if(!f1.exists()) f1.mkdirs();
		File f2 = new File(path + orgFile);
		try {
			mf.transferTo(f2);
			body.attachFile(f2);
			body.setFileName(new String(orgFile.getBytes("UTF-8"),"8859_1"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return body;
	}
	//인증객체. AdminController 클래스의 내부클래스로 구현함. 
	private final class MyAuthenticator extends Authenticator {
		private String id;
		private String pw;
		public MyAuthenticator(String id, String pw) {
			this.id = id;
			this.pw = pw;
		}
		@Override
		protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(id,pw);
		}		
	}
}

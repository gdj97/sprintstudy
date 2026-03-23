package main;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import annotation.Article;
import annotation.Member;
import annotation.MemberService;
import annotation.ReadArticleService;
import annotation.UpdateInfo;
import config.AppCtx;

public class Main01 {
	public static void main(String[] args) {
		ApplicationContext ctx = new AnnotationConfigApplicationContext(AppCtx.class);
		//service : ReadArticleServiceImpl 객체
		ReadArticleService service = ctx.getBean("readArticleService",ReadArticleService.class);
		try {
			//aop(LoggingAspect)의 pointcut으로 설정된 메서드
			Article a1 = service.getArticleAndReadCnt(1); //필수메서드.
			System.out.println(a1);
			Article a2 = service.getArticleAndReadCnt(1);
			
			System.out.println("[main] a1==a2 : " + (a1==a2));
			
			service.getArticleAndReadCnt(0);
		} catch (Exception e) {
			System.out.println("[main] " + e.getMessage());
		}
		System.out.println("\n UpdateMemberInfoTraceAspect 연습");
		//ms : MemberService 객체
		MemberService ms = ctx.getBean("memberService",MemberService.class); 
		ms.regist(new Member()); //LoggingAspect 클래스의 pointcut 메서드
		//LoggingAspect 클래스의 pointcut 메서드,UpdateTraceAspect 클래스의 pointcut 메서드
		ms.update("hong", new UpdateInfo());  
		//LoggingAspect 클래스의 pointcut 메서드,UpdateTraceAspect 클래스의 pointcut 메서드
		ms.delete("hong2", "test",new UpdateInfo()); //LoggingAspect 클래스의 pointcut 메서드			
	}
}

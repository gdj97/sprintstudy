package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@ComponentScan(basePackages= {"controller","dto","service","dao","aop"})
@EnableAspectJAutoProxy //AOP 관련 설정 
@EnableWebMvc  //기본 제공되는 web처리 기능 유지
public class MvcConfig implements WebMvcConfigurer{
	@Bean
	public HandlerMapping handlerMapping() { //요청 url과 Controller 연결
		RequestMappingHandlerMapping hm = new RequestMappingHandlerMapping();
		hm.setOrder(0);
		return hm;
	}
	@Bean
	public ViewResolver viewResolver() { //뷰 결정자. 
		InternalResourceViewResolver vr = new InternalResourceViewResolver();
		vr.setPrefix("/WEB-INF/view/"); // item/list => /WEB-INF/view/item/list.jsp 
		vr.setSuffix(".jsp");
		return vr;
	}
	//기본 웹파일 처리를 위한 설정
	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}
	//파일 업로드를 위한 설정. enctype="multipart/form-data" 형식의 요청시 처리. 
	// 파라미터값, 파일정보등을 저장. 
	@Bean
	public MultipartResolver multipartResolver() {
		CommonsMultipartResolver mr = new CommonsMultipartResolver();
		mr.setMaxInMemorySize(1024 * 1024);
		mr.setMaxUploadSize(1024 * 10240);
		return mr;
	}
	
}

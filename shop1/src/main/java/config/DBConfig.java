package config;

import java.beans.PropertyVetoException;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

@Configuration   //스프링 환경 설정 객체
public class DBConfig {
	/*
	 * @Bean : 객체화하여 컨테이너에 저장. "dataSource" 이름으로 ComboPooledDataSource 객체가 저장됨
	 * destroyMethod = "close" : 객체가 소멸될때 close 메서드를 호출하도록 설정
	 */
	@Bean(destroyMethod = "close") 
	public DataSource dataSource() { //Connection 객체
		//Connection Pool : 미리 연결된 객체들 저장  
		ComboPooledDataSource ds = new ComboPooledDataSource();//Connection Pool 객체 생성
		try {
			ds.setDriverClass("org.mariadb.jdbc.Driver");
			ds.setJdbcUrl("jdbc:mariadb://localhost:3306/springdb");
			ds.setUser("gduser");
			ds.setPassword("1234");
			ds.setMaxPoolSize(20); //최대 객체의 갯수
			ds.setMinPoolSize(3);  //최소 객체의 갯수
			ds.setInitialPoolSize(5); //초기 연결 객체의 갯수 
			ds.setAcquireIncrement(5);//증가 단위. 
		} catch(PropertyVetoException e) {
			e.printStackTrace();
		}
		return ds;
	}
	@Bean
	public SqlSessionFactory sqlSessionFactory() throws Exception {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(dataSource()); //Connection 객체
		bean.setConfigLocation(new ClassPathResource("mybatis-config.xml")); //Mapper 정보
		return bean.getObject();
	}
	@Bean
	public SqlSessionTemplate sqlSessionTemplate() throws Exception {
		return new SqlSessionTemplate(sqlSessionFactory()); //session 객체
	}
}
package dao.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import dto.User;

public interface UserMapper {

	@Insert("insert into useraccount (userid, username,password,phoneno,postcode,address,email,birthday)"
	+ " values(#{userid}, #{username},#{password},#{phoneno},#{postcode},#{address},#{email},#{birthday})")
	void insert(User user);

	@Select("select * from useraccount where userid=#{value}")
	User selectOne(String userid);

	@Update("update useraccount set username=#{username},phoneno=#{phoneno},postcode=#{postcode},"
		  + " address=#{address},email=#{email},birthday=#{birthday} where userid=#{userid}")
	void update(User user);

	@Delete("delete from useraccount where userid=#{value}")
	void delete(String userid);

}

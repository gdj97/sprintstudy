package dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import dto.Item;

public interface ItemMapper {

	@Select("select * from item")
	List<Item> selectList();

	@Select("select * from item where id=#{value}")	
	Item selectOne(Integer id);

}

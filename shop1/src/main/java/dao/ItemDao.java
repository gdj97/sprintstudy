package dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import dao.mapper.ItemMapper;
import dto.Item;

@Repository
public class ItemDao {
	@Autowired
	private SqlSessionTemplate template; //DBConfig 클래스에서 @Bean으로 객체화
	private Map<String,Object> param = new HashMap<>();
	private final Class<ItemMapper> cls = ItemMapper.class;
	
	public List<Item> list() {
		return template.getMapper(cls).selectList();
	}

	public Item selectOne(Integer id) {
		return template.getMapper(cls).selectOne(id);
	}
}

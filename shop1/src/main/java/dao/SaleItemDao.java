package dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import dao.mapper.SaleItemMapper;
import dto.SaleItem;


@Repository
public class SaleItemDao {
	@Autowired
	private SqlSessionTemplate template;
	private Class<SaleItemMapper> cls = SaleItemMapper.class;

	public void insert(SaleItem saleItem) {
		template.getMapper(cls).insert(saleItem);
	}

	public List<SaleItem> list(int saleid) {
		return template.getMapper(cls).selectList(saleid);
	}

}

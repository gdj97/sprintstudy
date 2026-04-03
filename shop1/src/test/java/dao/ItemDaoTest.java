package dao;


import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import dto.Item;

public class ItemDaoTest {
	@Autowired
	private ItemDao dao;
	@Test
	void testAdd() {
		List<Item> list = dao.list();
		assertNull(list);
	}

}

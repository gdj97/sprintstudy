package dao;

import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import org.junit.jupiter.api.Test;
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

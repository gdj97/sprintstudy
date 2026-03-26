package service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dao.UserDao;
import dto.User;

@Service
public class UserService {
	@Autowired
	private UserDao dao;

	public void userInsert(User user) {
		dao.insert(user);
	}

	public User getUser(String userid) {
		return dao.selectOne(userid);
	}
}

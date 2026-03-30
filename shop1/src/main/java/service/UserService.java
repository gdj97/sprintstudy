package service;


import java.util.List;

import javax.validation.Valid;

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

	public void userUpdate(User user) {
		dao.update(user);
	}

	public void userDelete(String userid) {
		dao.delete(userid);
	}

	public void userChgPass(String userid, String chgpass) {
		dao.chgPass(userid,chgpass);
	}

	public String getSearch(User user, String url) {
		return dao.search(user,url);
	}

	public List<User> userList() {
		return dao.list();
	}

	public List<User> getUserList(String[] idchks) {
		return dao.list(idchks);  //오버로딩된 메서드
	}
	
}

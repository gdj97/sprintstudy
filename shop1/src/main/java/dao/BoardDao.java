package dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import dao.mapper.BoardMapper;
import dto.Board;

@Repository
public class BoardDao {
	@Autowired
	private SqlSessionTemplate template;
	private Class<BoardMapper> cls = BoardMapper.class;
	private Map<String,Object> param = new HashMap<>();
	
	public int maxNum() { //num 컬럼의 최대값 리턴
		return template.getMapper(cls).maxNum();
	}
	public void insert(Board board) {  //board 내용을 board 테이블에 저장
		template.getMapper(cls).insert(board);
	}
	public int count(String boardid) {
		param.clear();
		param.put("boardid", boardid);
		return template.getMapper(cls).count(param);
	}
	public List<Board> list(Integer pageNum, int limit, String boardid) {
		param.clear();
		param.put("startrow", (pageNum - 1) * limit); //10
		param.put("limit",  limit);		              //10
		param.put("boardid",  boardid);		
		return template.getMapper(cls).selectList(param);
	}
}

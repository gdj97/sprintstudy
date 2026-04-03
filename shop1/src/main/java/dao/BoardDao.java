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
	public int count(String boardid,String searchtype, String searchcontent) {
		param.clear();
		param.put("boardid", boardid);
		param.put("searchtype", searchtype);
		param.put("searchcontent", searchcontent);
		return template.getMapper(cls).count(param);
	}
	public List<Board> list(Integer pageNum, int limit, String boardid,String searchtype, String searchcontent) {
		param.clear();
		param.put("startrow", (pageNum - 1) * limit); //10
		param.put("limit",  limit);		              //10
		param.put("boardid",  boardid);	
		param.put("searchtype", searchtype);
		param.put("searchcontent", searchcontent);
		return template.getMapper(cls).selectList(param);
	}
	public Board selectOne(Integer num) {
		return template.getMapper(cls).selectOne(num);
	}
	public void addReadcnt(Integer num) {
		template.getMapper(cls).addReadcnt(num);
	}
	public void grpStepAdd(Board board) {
		template.getMapper(cls).grpStepAdd(board.getGrp(),board.getGrpstep());
	}
	public void update(Board board) {
		template.getMapper(cls).update(board);		
	}
	public void delete(int num) {
		template.getMapper(cls).delete(num);
	}
}

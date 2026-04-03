package dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import dto.Board;

public interface BoardMapper {
    String select = "select num,writer,pass,title,content,file1 fileurl,"
		+ " regdate, readcnt, grp, grplevel, grpstep, boardid from board";
	
	@Select("select ifnull(max(num),0) from board")
	int maxNum();

	@Insert("insert into board (num,writer,pass,title,content, file1, "
			+ " boardid, regdate, readcnt,grp,grplevel, grpstep) "
			+ " values (#{num},#{writer},#{pass},#{title},#{content}, #{fileurl},"
			+ " #{boardid}, now(), 0,#{grp},#{grplevel}, #{grpstep})")
	void insert(Board board);

	@Select("select count(*) from board where boardid=#{boardid} ")
	int count(Map<String, Object> param);
	/*
	 * limit #{startrow}, #{limit} : 조회된 레코드 중 일부만 리턴. mysql, mariadb 사용가능 예약어
	 * 1페이지 :     0   ,  10    => 첫번째 레코드에서 10개만 리턴
	 * 2페이지 :    10   ,  10    => 11번째 레코드에서 10개만 리턴
	 * 3페이지 :    20   ,  10    => 21번째 레코드에서 10개만 리턴
	 * 
	 * 오라클 : rownum : 레코드의 조회되는 순서를 의미하는 예약어
	 * 
	 */
	@Select(select + " where boardid = #{boardid}"
	   	+ " order by grp desc, grpstep asc limit #{startrow}, #{limit}")
	List<Board> selectList(Map<String, Object> param);

	@Select(select + " where num = #{value}")
	Board selectOne(Integer num);	

    @Update("update board set readcnt = readcnt + 1 where num=#{value}")
	void addReadcnt(Integer num);

    @Update("update board set grpstep=grpstep + 1  where grp = #{grp} and grpstep > #{grpstep}")
	void grpStepAdd(@Param("grp") int grp, @Param("grpstep") int grpstep);
}

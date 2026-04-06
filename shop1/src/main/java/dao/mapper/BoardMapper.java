package dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
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

	@Select({"<script>",
		"select count(*) from board where boardid=#{boardid} ",
		"<if test='col1 != null and col2==null'>and ${col1} like '%${searchcontent}%'</if>",
		"<if test='col1 != null and col2!=null'>and (${col1} like '%${searchcontent}%' or ${col2} like '%${searchcontent}%')</if>",
		"</script>"})
	int count(Map<String, Object> param);
	/*
	 * cols = ["title","writer"]
	 * select count(*) from board where boardid=#{boardid}
	 * and ( title like %구% or writer like %구%)
	 */
	@Select({"<script>",
		"select count(*) from board where boardid=#{boardid} ",
		"<if test='cols != null'> and "
		+ "<foreach collection='cols' item='c' separator='or' open='(' close=')'> ${c} like '%${searchcontent}%'</foreach></if>",
		"</script>"})
	int count2(Map<String, Object> param);
	
	/*
	 * limit #{startrow}, #{limit} : 조회된 레코드 중 일부만 리턴. mysql, mariadb 사용가능 예약어
	 * 1페이지 :     0   ,  10    => 첫번째 레코드에서 10개만 리턴
	 * 2페이지 :    10   ,  10    => 11번째 레코드에서 10개만 리턴
	 * 3페이지 :    20   ,  10    => 21번째 레코드에서 10개만 리턴
	 * 
	 * 오라클 : rownum : 레코드의 조회되는 순서를 의미하는 예약어
	 * 
	 */
	@Select({"<script>",
		   select + " where boardid = #{boardid}",
		"<if test='col1 != null and col2==null'>and ${col1} like '%${searchcontent}%'</if>",
		"<if test='col1 != null and col2!=null'>and (${col1} like '%${searchcontent}%' or ${col2} like '%${searchcontent}%')</if>",
	   	 " order by grp desc, grpstep asc limit #{startrow}, #{limit}",
	   	"</script>"})
	List<Board> selectList(Map<String, Object> param);
	@Select({"<script>",
		   select + " where boardid = #{boardid}",
			"<if test='cols != null'> and "
			+ "<foreach collection='cols' item='c' separator='or' open='(' close=')'> ${c} like '%${searchcontent}%'</foreach></if>",
	   	 " order by grp desc, grpstep asc limit #{startrow}, #{limit}",
	   	"</script>"})
	List<Board> selectList2(Map<String, Object> param);

	@Select(select + " where num = #{value}")
	Board selectOne(Integer num);	

    @Update("update board set readcnt = readcnt + 1 where num=#{value}")
	void addReadcnt(Integer num);

    @Update("update board set grpstep=grpstep + 1  where grp = #{grp} and grpstep > #{grpstep}")
	void grpStepAdd(@Param("grp") int grp, @Param("grpstep") int grpstep);

    @Update("update board set writer=#{writer},title=#{title},content=#{content},"
			 + " file1=#{fileurl} where num=#{num}")
    void update(Board board);

    @Delete("delete from board where num=#{value}")
	void delete(int num);
}

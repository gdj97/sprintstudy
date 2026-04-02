package controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import dto.Board;
import service.BoardService;

@Controller
@RequestMapping("board")
public class BoardController {
	@Autowired
	private BoardService service;
	
	@GetMapping("*")
	public String getForm(Model model) {
		model.addAttribute(new Board());
		return null;
	}
	@PostMapping("write")
	public String write(@Valid Board board,BindingResult bresult, HttpServletRequest request) {
		if(bresult.hasErrors()) {
			return null;
		}
		
		if(board.getBoardid() == null || board.getBoardid().trim().equals("")) board.setBoardid("1");
		service.boardWrite(board,request);
		return "redirect:list?boardid=" + board.getBoardid();
	}
	
	@RequestMapping("list")
	public ModelAndView list(@RequestParam Map<String,String> param, HttpSession session) {
		//@RequestParam : 파라미터값을 Map객체로 파라미터이름=파라미터값의 형태로 전달
		System.out.println(param); //pageNum : 2
		Integer pageNum = null;
		//param.keySet() : 파라미터이름 목록
		for(String key : param.keySet()) {
			if(param.get(key) == null || param.get(key).trim().equals("")) {
			   param.put(key, null);	
			}
		}
		if (param.get("pageNum") != null) { //pageNum 파라미터가 존재하는 경우
			   pageNum = Integer.parseInt(param.get("pageNum"));
		} else {   //pageNum 파라미터가 없는 경우
			pageNum = 1;
		}
		String boardid = param.get("boardid");
		if (boardid == null) boardid = "1";
		
		ModelAndView mav = new ModelAndView();
		String boardName = null;
		switch(boardid) {
		   case "1" : boardName = "공지사항"; break;
		   case "2" : boardName = "자유게시판"; break;
		   case "3" : boardName = "QNA"; break;
		}
		int limit = 10;  //화면에 출력될 게시물 건수. 
		int listcount = service.boardcount(boardid); //게시판 종류별 전체 등록된 게시물 건수
		List<Board> boardlist = service.boardlist(pageNum,limit,boardid); //화면에 출력할 게시글 목록
		int maxpage = (int)((double)listcount/limit + 0.95); //최대페이지
		/*  listcount : 3
		 *   (int)((double)3/10 + 0.95) => (int)(1.25) > 1 
		 *  listcount : 31
		 *   (double)31/10 => 3.1 + 0.95 => 4.05 => (int)(4.05) > 4
		 *  listcount : 40
		 *   (double)40/10 => 4.0 + 0.95 => 4.95 => (int)(4.95) > 4
		 *  listcount : 501
		 *   (double)501/10 => 50.1 + 0.95 => 51.05 => (int)(51.05) > 51 
		 */
		int startpage = (int)((pageNum/10.0 + 0.9) - 1) * 10 + 1;
		/*
		 *  현재 페이지 : 1  : 1 ~ 10
		 *    1/10.0 => 0.1 => 0.1 + 0.9 => 1.0 - 1 => (int)(0.0) => 0 * 10 => 0 + 1 => 1 
		 *  현재 페이지 : 5  : 1 ~ 10
		 *    5/10.0 => 0.5 => 0.5 + 0.9 => 1.4 - 1 => (int)(0.4) => 0 * 10 => 0 + 1 => 1 
		 *  현재 페이지 : 10  : 1 ~ 10
		 *    10/10.0 => 1.0 => 1.0 + 0.9 => 1.9 - 1 => (int)(0.9) => 0 * 10 => 0 + 1 => 1 
		 *  현재 페이지 : 11 : 11 ~ 20
		 *    11/10.0 => 1.1 => 1.1 + 0.9 => 2.0 - 1 => (int)(1.0) => 1 * 10 => 10 + 1 => 11 
		 *  현재 페이지 : 15 : 11 ~ 20
		 *    15/10.0 => 1.5 => 1.5 + 0.9 => 2.4 - 1 => (int)(1.4) => 1 * 10 => 10 + 1 => 11 
		 */
		int endpage = startpage + 9;
		if(endpage > maxpage) endpage = maxpage; //화면에 출력할 페이지는 maxpage값을 넘지 못함
		mav.addObject("boardid",boardid);      //게시판 종류
		mav.addObject("boardName", boardName); //게시판 종류 이름
		mav.addObject("pageNum", pageNum);     //현재 페이지번호
		mav.addObject("maxpage", maxpage);     //최대 페이지 
		mav.addObject("startpage", startpage); //화면에 출력된 시작 페이지
		mav.addObject("endpage", endpage);     //화면에 출력된 종료 페이지
		mav.addObject("listcount", listcount); //전체 등록된 게시물 건수
		mav.addObject("boardlist", boardlist); //출력할 게시물 목록
		int boardno = listcount - (pageNum - 1) * limit;
		/*
		 * 현재페이지 1. 게시물건수 : 21
		 *    21 - 0 * 10 : 21
		 * 현재페이지 2. 게시물건수 : 21
		 *    21 - (2-1) * 10 :  11
		 * 현재페이지 3. 게시물건수 : 21
		 *    21 - (3-1) * 10 :  21
		 *   
		 */
		mav.addObject("boardno", boardno);     //화면에 보여질 게시물 번호의 시작값
		return mav;
	}
}

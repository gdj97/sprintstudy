package service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ShopService {

	public String sidoSelect1(String si, String gu, HttpServletRequest request) {
		BufferedReader fr = null;
		//path : sido.txt 파일의 절대경로. 
		//request.getServletContext().getRealPath("/") => /webapp/ 폴더내를 의미. 
		//D:\20251222\spring\workspace\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\shop1
		String path = request.getServletContext().getRealPath("/")+"file/sido.txt";
		try {
			fr = new BufferedReader(new FileReader(path));
		}catch(Exception e) {
			e.printStackTrace();	
		}
		Set<String> set = new LinkedHashSet<>(); //중복불가 + 순서유지
//		Set<String> set = new TreeSet<>(); //중복불가 + 정렬
		String data= null;
		if(si==null && gu==null) {
			try {
				while((data=fr.readLine()) != null) {
					String[] arr = data.split("\\s+"); //공백으로 문자열 분리
					if(arr.length >= 3) set.add(arr[0].trim()); 
				}
			} catch(IOException e) {   e.printStackTrace();	}
		}
		List<String> list = new ArrayList<>(set);
		return list.toString(); //[서울특별시,경기도,경상북도,....] 		
	}

	public List<String> sidoSelect(String si, String gu, HttpServletRequest request) {
		BufferedReader fr = null;
		String path = request.getServletContext().getRealPath("/")+"file/sido.txt";
		try {
			fr = new BufferedReader(new FileReader(path));
		}catch(Exception e) {
			e.printStackTrace();
		}
		Set<String> set = new LinkedHashSet<>(); // 중복불가 + 순서유지. 
		String data= null;
		if(si==null && gu==null) { //sidoSelect1 처리함. 
			return null;
		} else if(gu == null) { //si 파라미터 존재. 시도선택한 경우. 구군을 검색하여 리턴
		   si = si.trim();
		   try {
			   //fr.readLine() : sido.txt 파일에서 한줄씩 읽기
			  while ((data = fr.readLine()) != null) {
				 String[] arr = data.split("\\s+"); //공백으로 분리
				 //arr[0].equals(si) : 시도가 si파라미터값과 같은경우
				 //!arr[1].contains(arr[0]) : 첫번째 배열의 값을 두번째 배열의 요소의 값이 같은 값을 포함하는 경우.
				 //                 서울특별시 서울특별시 ...
			  	 if(arr.length >= 3 && arr[0].equals(si) && !arr[1].contains(arr[0]) ) {
					 set.add(arr[1].trim()); //구군의 데이터를 set에 추가
				 }
			   }
		   } catch (IOException e) {
			   e.printStackTrace();
		   }
		} else {  //si 파라미터,gu 파라미터값 존재.
		   si = si.trim();
		   gu = gu.trim();
		   try {
			  while ((data = fr.readLine()) != null) {
				  String[] arr = data.split("\\s+");
		          if(arr.length >= 3 && arr[0].equals(si) && arr[1].equals(gu)
			    	  && !arr[0].equals(arr[1]) && !arr[2].contains(arr[1])) {
			          	 if(arr.length > 3 ) {
			          		if(arr[3].contains(arr[1])) continue;
			          		arr[2] += " " + arr[3];
			          	 }
			          	 set.add(arr[2].trim());
			      }
			  }
			} catch (IOException e) {
			    e.printStackTrace();
			}
		}
		List<String> list = new ArrayList<>(set); //Set 객체 => List 객체로 
		return list;		            
	}

	public String exchangeString() {
		Document doc = null;
		List<List<String>> trlist = new ArrayList<>(); 
		String url = "https://www.koreaexim.go.kr/wg/HPHKWG057M01";
		String exdate = null;
		try {
			doc = Jsoup.connect(url).get();
			Elements trs = doc.select("tr"); //tr 태그들
			exdate = doc.select("p.table-unit").html();  //조회기준일 : 2026.04.07
			for(Element tr : trs) {
				List<String> tdlist = new ArrayList<>();
				Elements tds = tr.select("td"); //td 태그들
				for(Element td : tds) {
					tdlist.add(td.html());
				}
			    if (tdlist.size() > 0) {
				   if(tdlist.get(0).equals("USD") //미달러 통화코드
			    	|| tdlist.get(0).equals("CNH") //중국 통화코드
			        || tdlist.get(0).equals("JPY(100)") //일본 통화코드
			        || tdlist.get(0).equals("EUR")) {   //유로 통화코드
				    trlist.add(tdlist); // 미국,중국,일본,유럽 통화에 해당하는 tr 태그들만 저장
				   }
			    }
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		StringBuilder sb = new StringBuilder();
		sb.append("<p class='text-right'>" + exdate + "</p>"); //text-right : 오른쪽 정렬
		sb.append("<table class='table table-sm table-bordered'>");
		sb.append("<tr><th>통화</th><th>기준율</th>"  //text-nowrap : 한줄로 출력
				+ "<th class='text-nowrap'>받을실때</th><th class='text-nowrap'>보내실때</th></tr>");
		for(List<String> tds : trlist) {
			sb.append("<tr><td>"+tds.get(0)+"<br>"+tds.get(1)+"</td><td>"+tds.get(4)+"</td>");
			sb.append("<td>"+tds.get(2)+"</td><td>"+tds.get(3)+"</td></tr>");
		}
		sb.append("</table>");
		return sb.toString();
	}

	public Map<String, Object> exchangeJson() {
		Document doc = null;
		List<List<String>> trlist = new ArrayList<>();
		String url = "https://www.koreaexim.go.kr/wg/HPHKWG057M01";
		String exdate = null;
		try {
			doc = Jsoup.connect(url).get();
			Elements trs = doc.select("tr"); 
			exdate = doc.select("p.table-unit").html();
			for(Element tr : trs) {
				List<String> tdlist = new ArrayList<>();
				Elements tds = tr.select("td");
				for(Element td : tds) {
					tdlist.add(td.html());
				}
			    if (tdlist.size() > 0) {
				   if(tdlist.get(0).equals("USD")
			    	|| tdlist.get(0).equals("CNH")
			        || tdlist.get(0).equals("JPY(100)")
			        || tdlist.get(0).equals("EUR")) {
				    trlist.add(tdlist);
				   }
			    }
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		Map<String,Object> map = new HashMap<>();
		map.put("exdate", exdate);
		map.put("trlist", trlist);
		return map;
	}

	public String summernoteImageUpload(MultipartFile multipartFile, HttpServletRequest request) {
		File dir = new File(request.getServletContext().getRealPath("/") + "board/image");
		if (!dir.exists()) dir.mkdirs();
		String filesystemName = multipartFile.getOriginalFilename();
		File file = new File(dir,filesystemName);
		try {
			multipartFile.transferTo(file); 
		} catch(Exception e) {
			e.printStackTrace();
		}
		return request.getContextPath() + "/board/image/" + filesystemName;	
	}

}

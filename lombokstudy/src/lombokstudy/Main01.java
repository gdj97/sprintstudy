package lombokstudy;
/*
 * 1. 롬복(lombok)
 *    편집기와 빌드 도구에 연결하여 자바기능을 추가.
 *    열려있는 eclipse를 닫기
 * 2. 사이트 : https://projectlombok.org => lombok.jar 파일 다운받기
 * 3. cmd 창에서 다운받은 폴더로 이동.
 * 4. java -jar lombok.jar cmd 창에 명령 입력 
 * 5. 이클립스의 폴더로 찾아가서 lombok.jar 파일 확인, eclipse.ini 파일을 열어 lombok.jar 설정 확인(수정하지 않도록 주의)
 * 6. 이클립스 실행하기 
 * 7. classPath에 추가하기 => Referenced Libraries 폴더에 lombok.jar파일이 보여지면 설정 완료
 *    - 프로젝트 우클릭 >Build Path > configure build path > Librares탭 > ClassPath 선택
 *      > Add External Jars > lombok.jar 파일 선택
 *      
 */
public class Main01 {
	public static void main(String[] args) {
		Ex01_User user = new Ex01_User();
		user.setId("hong");
		user.setPw("1234");
		System.out.println(user);
		System.out.println(user.getId());
		System.out.println(user.getPw());
		Ex01_User user2 = new Ex01_User("kim","5678");
		System.out.println(user2);
	}
}

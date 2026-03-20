package lombokstudy;

public class Main02 {
	public static void main(String[] args) {
		/*
		 * Ex02_User.builder() : new Builder() 생성
		 * Ex02_User.builder().id("hong") : Builder 객체의 id멤버에 "hong" 저장
		 * Ex02_User.builder().id("hong").pw("1234") :
		 *     Builder 객체의 id멤버에 "hong" 저장, pw 멤버에 "1234" 저장.
		 * ....build() :  new Ex02_User(this) 
		 *           => Ex02_User 객체의 멤버 변수값을 초기화  
		 */
		Ex02_User user = Ex02_User.builder()
				.id("hong").pw("1234").build();
		System.out.println(user);
	}
}

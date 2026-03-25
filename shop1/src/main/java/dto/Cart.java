package dto;

import java.util.ArrayList;
import java.util.List;

public class Cart {
	private List<ItemSet> itemSetList = new ArrayList<>();
	public List<ItemSet> getItemSetList() {
		return itemSetList;
	}
	public void push(ItemSet itemSet) {
	   itemSetList.add(itemSet);
	}
	/* 전체 장바구니에 속한 상품가격*수량의 합을 
	 * 출력하도록 수정하기
	*/
	public int getTotal() { //total get프로퍼티
		int sum=0;
		for(ItemSet set : itemSetList) {
			sum += set.getItem().getPrice() * set.getQuantity();
		}
		return sum;
	}
}

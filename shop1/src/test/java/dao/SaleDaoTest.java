package dao;



import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import dao.SaleDao;
import dto.Sale;   

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import config.DBConfig;
import config.MvcConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DBConfig.class,SaleDao.class})
public class SaleDaoTest {
	@Autowired
    private SaleDao saleDao;

    @Test
    public void testSaleList() {
        // 1. 테스트 데이터 준비 (DB에 존재하는 userid 사용)
        String userid = "test1";

        // 2. DAO 메서드 실행
        List<Sale> list = saleDao.listAll(userid);

        // 3. 검증 (Assertion)
        assertNotNull("리스트 객체 자체가 null이면 안됩니다.", list);
        
        System.out.println("조회된 주문 건수: " + list.size());

        if (!list.isEmpty()) {
            for (Sale sa : list) {
                System.out.println("주문번호: " + sa.getSaleid() + ", 사용자: " + sa.getUserid());
                
                // JOIN이 정상적으로 작동했는지 확인
                assertNotNull("주문 상품 목록(itemList)이 null이면 안됩니다.", sa.getItemList());
                assertTrue("주문 상품이 최소 1개 이상 존재해야 합니다.", sa.getItemList().size() > 0);
                
                sa.getItemList().forEach(si -> {
                    System.out.println("  -> 상품명: " + si.getItem().getName() + ", 수량: " + si.getQuantity());
                    assertNotNull("상품 정보(Item)가 매핑되지 않았습니다.", si.getItem());
                });
            }
        } else {
            System.out.println("해당 유저의 주문 내역이 없습니다.");
        }
    }
}

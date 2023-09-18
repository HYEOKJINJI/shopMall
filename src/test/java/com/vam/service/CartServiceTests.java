package com.vam.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.vam.model.CartDTO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
public class CartServiceTests {
	
	@Autowired
	private CartService service;
	
	/* 카트 등록 테스트 */
	@Test
	public void addCartTest() {
		String memberId = "admin";
		int bookId = 1059;
		int count = 4;
		
		CartDTO dto = new CartDTO();
		dto.setMemberId(memberId);
		dto.setBookId(bookId);
		dto.setBookCount(count);
		
		int result = service.addCart(dto);
		
		System.out.println("** result : " + result);
	}

}

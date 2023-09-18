package com.vam.mapper;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.vam.model.CartDTO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
public class CartMapperTests {
	
	@Autowired
	private CartMapper mapper;
	
	/* 카트 추가 
	@Test
	public void addCart() {
		String memberId = "admin";
		int bookid = 1061;
		int count = 2;
		
		CartDTO cart = new CartDTO();
		cart.setMemberId(memberId);
		cart.setBookId(bookid);
		cart.setBookCount(count);
		
		int result = 0;
		result = mapper.addCart(cart);
		
		System.out.println("결과 : " + result);
		
	}
	*/
	
	/* 카트 삭제 
	@Test
	public void deleteCartTeest() {

		CartDTO cart = new CartDTO();
		
		cart.setCartId(11);

		mapper.deleteCart(cart);
	}
	*/
	
	/* 카트 수량 수정 
	@Test
	public void modifyCartTest() {
		int cartId = 4;
		int count = 5;
		
		CartDTO	cart = new CartDTO();
		cart.setCartId(cartId);
		cart.setBookCount(count);
		
		mapper.modifyCount(cart);
		
	}
	*/
	
	/* 카트 목록
	@Test
	public void getCartTest() {
		String memberId = "admin";
		
		List<CartDTO> list = mapper.getCart(memberId);
		for(CartDTO cart : list) {
			System.out.println(cart);
			cart.initSaleTota();
			System.out.println("init cart : " + cart);
		}
	}
	 */
	
	/* 카트 확인 
	@Test
	public void checkCartTEst() {
		String memberId = "admin";
		int bookId = 1062;
		
		CartDTO cart = new CartDTO();
		cart.setMemberId(memberId);
		cart.setBookId(bookId);
		
		CartDTO resultCart = mapper.checkCart(cart);
		System.out.println("결고 : " + resultCart);
	}
	*/
	
	/* 장바구니 제거(주문 처리) 
	@Test
	public void deleteOrderCart() {
		
		String memberId = "admin";
		int bookId = 1062;
		
		CartDTO dto = new CartDTO();
		dto.setMemberId(memberId);
		dto.setBookId(bookId);
		
		mapper.deleteOrderCart(dto);
	}
	*/
	
}
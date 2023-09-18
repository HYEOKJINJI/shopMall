package com.vam.mapper;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.vam.model.BookVO;
import com.vam.model.MemberVO;
import com.vam.model.OrderDTO;
import com.vam.model.OrderItemDTO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
public class OrderMapperTests {
	
	@Autowired
	private OrderMapper orderMapper;
	
	@Autowired
	private CartMapper cartMapper;
	
	/* 상품 정보(주문 처리) 
	@Test 
	public void getOrderInfoTest() {
		
		OrderItemDTO orderInfo = orderMapper.getOrderInfo(1065);
		
		System.out.println("result : " + orderInfo);
	}
	*/
	
	/* vam_order 테이블 등록 
	@Test
	public void enrollOrderTest() {
		
		OrderDTO ord = new OrderDTO();
		List<OrderItemDTO> orders = new ArrayList();
		
		OrderItemDTO order1 = new OrderItemDTO();
		
		order1.setBookId(1065);
		order1.setBookCount(5);
		order1.setBookPrice(30000);
		order1.setBookCount(0);
		order1.initSaleTotal();
		
		ord.setOrders(orders);
		
		ord.setOrderId("2021_test3");
		ord.setAddressee("지혁진");
		ord.setMemberId("admin");
		ord.setMemberAddr1("test");
		ord.setMemberAddr2("test");
		ord.setMemberAddr3("test");
		ord.setOrderState("배송준비");
		ord.getOrderPriceInfo();
		ord.setUsePoint(1000);
		
		orderMapper.enrollOrder(ord);
	}
	*/
	
	/* vam_itemOrder 테이블 등록 
	@Test
	public void enrollOrderrItemTest() {
		
		OrderItemDTO oid = new OrderItemDTO();
		
		oid.setOrderId("2021_test1");
		oid.setBookId(1065);
		oid.setBookCount(1);
		oid.setBookPrice(30000);
		oid.setBookDiscount(0);
		
		oid.initSaleTotal();
		
		orderMapper.enrollOrderItem(oid);
	}.*/
	
	/* 회원 돈, 포인트 정보 변경 
	@Test
	public void deductMoneyTest() {
		
		MemberVO member = new MemberVO();
		
		member.setMemberId("admin");
		member.setMoney(500000);
		member.setPoint(10000);
		
		orderMapper.deductMoney(member);
	}
	*/
	
	/* 상품 재고 변경 
	@Test
	public void deductStockTest() {
		BookVO book = new BookVO();
		
		book.setBookId(1065);
		book.setBookStock(77);
		
		orderMapper.deductStock(book);
	}
	*/
}

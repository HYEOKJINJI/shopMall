package com.vam.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.vam.model.BookVO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
public class BookServiceTests {
	
	@Autowired
	private BookService service;
	
	/*
	@Test
	public void getCateInfoListTest1() {
		Criteria cri = new Criteria();
		
		String type = "TC";
		String keyword = "test";
		String cateCode = "101001";
		
		cri.setType(type);
		cri.setKeyword(keyword);
		cri.setCateCode(cateCode);
		
		System.out.println("List<cateFilterDTO> : " + service.getCateInfoList(cri));
	}
	*/
	
	/*
	@Test
	public void getCateInfoListTest2() {
		Criteria cri = new Criteria();
		
		String type = "AC";
		String keyword = "test";
		String cateCode = "101001";
		
		cri.setType(type);
		cri.setKeyword(keyword);
		cri.setCateCode(cateCode);
		
		System.out.println("Listcatefilterdto : " + service.getCateInfoList(cri));
		
	}
	*/
	
	/*
	@Test
	public void getCateInfoListTest3() {
		Criteria cri = new Criteria();
		
		String type = "T";
		String keyword = "test";
		
		cri.setType(type);
		cri.setKeyword(keyword);
		
		System.out.println("lsit : " + service.getCateInfoList(cri));
	}
	*/
	
	/*
	@Test
	public void getCateInfoTest4() {
		Criteria cri = new Criteria();
		
		String type = "AC";
		String keyword = "test";
		
		cri.setType(type);
		cri.setKeyword(keyword);
		
		System.out.println("lsiefmflksmflkmeslkfmelkmf : "  + service.getCateInfoList(cri));
	}
	*/
	
	/* 상품 상세 정보 */
	@Test
	public void getGoodsInfoTest() {
		int bookId = 1062;
		
		BookVO goodsInfo = service.getGoodsInfo(bookId);
		
		System.out.println("==결과==");
		System.out.println("전체 : " + goodsInfo);
		System.out.println("bookId : " + goodsInfo.getBookId());
		System.out.println("이미지 정보 : " + goodsInfo.getImageList().isEmpty());
	}
	

}

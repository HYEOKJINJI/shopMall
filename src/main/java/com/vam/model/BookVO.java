package com.vam.model;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class BookVO {
	
	/* 상품 id */
	private int bookId;
	
	/* 상품 이름 */
	private String bookName;
	
	/* 작가 Id */
	private int authorId;
	
	/* 작가 이름 */
	private String authorName;
	
	/* 출판일 */
	private String publeYear;
	
	/* 출판사 */
	private String publisher;
	
	/* 카테고리 코드 */
	private String cateCode;
	
	/* 카테고리 이름 */
	private String cateName;
	
	/* 상품 가격 */
	private int bookPrice;
	
	/* 상품 재고 */
	private int bookStock;
	
	/* 상품 할인률 */
	private double bookDiscount;
	
	/* 상품 소개 */
	private String bookIntro;
	
	/* 상품 목차 */
	private String bookContents;
	
	/* 등록 날짜 */
	private Date regDate;
	
	/* 수정 날짜 */
	private Date updateDate;
	
	/* 이미지 정보 */
	private List<AttachImageVO> imageList; 

}

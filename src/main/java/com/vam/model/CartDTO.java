package com.vam.model;

import java.util.List;

import lombok.Data;

@Data
public class CartDTO {
	
	private int cartId;
	
	private String memberId;
	
	private int bookId;
	
	private int bookCount;
	
	// book
	private String bookName;
	
	private int bookPrice;
	
	private double bookDiscount;
	
	// 추가
	private int salePrice;
	
	private int totalPrice;
	
	private int point;
	
	private int totalPoint;
	
	/* 상품 이미지 */
	private List<AttachImageVO> imageList;
	
	public void initSaleTota() {
		this.salePrice = (int)(this.bookPrice * (1-this.bookDiscount));
		this.totalPrice = this.salePrice * this.bookCount;
		this.point = (int)(Math.floor(this.salePrice*0.05));
		this.totalPoint = this.point * this.bookCount;
	}
	
}

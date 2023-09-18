package com.vam.model;

import lombok.Data;

@Data
public class CateFilterDTO {
	
	/* 카테고리 이름 */
	private String cateName;
	
	/* 카테고리 코드 */
	private String cateCode;
	
	/* 카테고리 상품 수 */
	private int cateCount;	
	
	/* 카테고리 상품 수  */
	private String cateGroup;
	
	public void setCateCode(String cateCode) {
		this.cateCode = cateCode;
		this.cateGroup = cateCode.split("")[0];
	}
	
}

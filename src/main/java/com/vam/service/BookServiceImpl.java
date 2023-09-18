package com.vam.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vam.mapper.AdminMapper;
import com.vam.mapper.AttachMapper;
import com.vam.mapper.BookMapper;
import com.vam.model.AttachImageVO;
import com.vam.model.BookVO;
import com.vam.model.CateFilterDTO;
import com.vam.model.CateVO;
import com.vam.model.Criteria;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class BookServiceImpl implements BookService{
	
	@Autowired
	private AdminMapper adminMapper;
	
	@Autowired
	private BookMapper bookMapper;
	
	@Autowired
	private AttachMapper attachMapper;

	@Override
	public List<BookVO> getGoodsList(Criteria cri) {

		log.info("getGoodsList...............");
		
		String type = cri.getType();
		String[] typeArr = type.split("");
		String[] authorArr = bookMapper.getAuthorIdList(cri.getKeyword());
		
		if(type.equals("A")||type.equals("AC") || type.equals("AT") || type.equals("ACT")) {
			if(authorArr.length == 0) {
				return new ArrayList();
			}
		}
		
		for(String t : typeArr) {
			if(t.equals("A")) {
				cri.setAuthorArr(authorArr);
			}
		}
		
		List<BookVO> list = bookMapper.getGoodsList(cri);
		
		for(int i=0; i<list.size(); i++) {
			
			int bookId = list.get(i).getBookId();
			
			List<AttachImageVO> imageList = attachMapper.getAttachList(bookId);
			
			list.get(i).setImageList(imageList);
		}
		
		return list;
	}

	@Override
	public int goodsGetTotal(Criteria cri) {

		log.info("goodsGetTotal............");
		
		return bookMapper.goodsGetTotal(cri);
	}

	@Override
	public List<CateVO> getCateCode1() {

		log.info("getCateCode1().....................");
		return bookMapper.getCateCode1();
	}

	@Override
	public List<CateVO> getCateCode2() {
		
		log.info("getCateCode2().......................");
		return bookMapper.getCateCode2();
	}

	@Override
	public List<CateFilterDTO> getCateInfoList(Criteria cri) {

		List<CateFilterDTO> filterInfoList = new ArrayList<CateFilterDTO>();
		
		String[] typeArr = cri.getType().split("");
		String[] authorArr;
		
		for(String type : typeArr) {
			if(type.equals("A")) {
				authorArr = bookMapper.getAuthorIdList(cri.getKeyword());
				if(authorArr.length == 0) {
					return filterInfoList;
				}
				cri.setAuthorArr(authorArr);
			}
		}
		String[] cateList = bookMapper.getCateList(cri);
		
		String tempCateCode = cri.getCateCode();
		
		for(String cateCode : cateList) {
			cri.setCateCode(cateCode);
			CateFilterDTO filterInfo = bookMapper.getCateInfo(cri);
			filterInfoList.add(filterInfo);
		}
		
		cri.setCateCode(tempCateCode);
		
		return filterInfoList;
	}

	@Override
	public BookVO getGoodsInfo(int bookId) {
		
		BookVO goodsInfo = bookMapper.getGoodsInfo(bookId);
		goodsInfo.setImageList(adminMapper.getAttachInfo(bookId));
		
		return goodsInfo;
	}

	@Override
	public BookVO getBookIdName(int bookId) {
		
		return bookMapper.getBookIdName(bookId);
	}
	
	
	
}

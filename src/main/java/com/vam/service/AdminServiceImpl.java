package com.vam.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vam.mapper.AdminMapper;
import com.vam.model.AttachImageVO;
import com.vam.model.BookVO;
import com.vam.model.CateVO;
import com.vam.model.Criteria;
import com.vam.model.OrderDTO;

import lombok.extern.log4j.Log4j;

@Log4j
@Service
public class AdminServiceImpl implements AdminService {
	
	@Autowired
	private AdminMapper adminMapper;
	
	@Transactional
	@Override
	public void bookEnroll(BookVO book) {
		log.info("(service)bookEnroll...");
		adminMapper.bookEnroll(book);
		
		if(book.getImageList() == null || book.getImageList().size() <= 0) {
			return;
		}
		
		book.getImageList().forEach(attach ->{
			attach.setBookId(book.getBookId());
			adminMapper.imageEnroll(attach);
		});
	}

	@Override
	public List<CateVO> cateList() {
		
		log.info("(service)cateList..........." + adminMapper.cateList());
		
		return adminMapper.cateList();
	}

	@Override
	public List<BookVO> goodsGetList(Criteria cri) {
		log.info("goodsGetTotalList()................");
		return adminMapper.goodsGetList(cri);
	}

	@Override
	public int goodsGetTotal(Criteria cri) {
		log.info("goodsGetTotal().................");
		return adminMapper.goodsGetTotal(cri);
	}

	@Override
	public BookVO goodsGetDetail(int bookId) {
		log.info("goodsGetDetail()............");
		return adminMapper.goodsGetDetail(bookId);
	}

	@Override
	@Transactional
	public int goodsModify(BookVO vo) {
		
		int result = adminMapper.goodsModify(vo);
		
		if(result == 1 && vo.getImageList() != null && vo.getImageList().size() > 0) {
			
			adminMapper.deleteImageAll(vo.getBookId());
			
			vo.getImageList().forEach(attach -> {
				
				attach.setBookId(vo.getBookId());
				adminMapper.imageEnroll(attach);
				
			});
			
		}
		return result;
	}

	@Transactional
	@Override
	public int goodsDelete(int bookId) {
		log.info("goodsDelete................");
		
		adminMapper.deleteImageAll(bookId);
		
		return adminMapper.goodsDelete(bookId);
	}

	@Override
	public List<AttachImageVO> getAttachInfo(int bookId) {

		log.info("getAttachInfo...................");
		
		return adminMapper.getAttachInfo(bookId);
	}
	/* 주문 상품 리스트 */
	@Override
	public List<OrderDTO> getOrderList(Criteria cri) {
		return adminMapper.getOrderList(cri);
	}
	
	/* 주문 총 갯수 */
	@Override
	public int getOrderTotal(Criteria cri) {
		return adminMapper.getOrderTotal(cri);
	}

	
}

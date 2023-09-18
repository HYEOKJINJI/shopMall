package com.vam.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vam.model.Criteria;
import com.vam.model.ReplyDTO;
import com.vam.model.ReplyPageDTO;
import com.vam.service.BookService;
import com.vam.service.ReplyService;

@RestController
@RequestMapping("/reply")
public class ReplyController {
	
	@Autowired
	private ReplyService replyService;
	@Autowired
	private BookService bookService;
	
	/* 댓글 등록 */
	@PostMapping("/enroll")
	public void enrollReplyPOST(ReplyDTO dto) {
		
		replyService.enrollReply(dto);
	}
	
	/* 댓글 체크 */
	/* memberId, bookid 파라미터 */ 
	/* 존재 : 1 / 존재X 0 */
	@PostMapping("/check")
	public String replyCheckPOST(ReplyDTO dto) {
		return replyService.checkReply(dto);
	}
	
	/* 댓글 페이징 */
	@GetMapping(value="/list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ReplyPageDTO replyListPOST(Criteria cri) {
		return replyService.replyList(cri);
	}

}

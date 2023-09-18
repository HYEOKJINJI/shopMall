package com.vam.mapper;

import com.vam.model.MemberVO;

public interface MemberMapper {
	
	// 회원가입
	public void memberJoin(MemberVO member);
	
	// id 중복체크
	public int idCheck(String memberId);
	
	// 로그인
	public MemberVO memberLogin(MemberVO member);
	
	/* 주문자 주소 정보 */
	public MemberVO getMemberInfo(String memberId);

}

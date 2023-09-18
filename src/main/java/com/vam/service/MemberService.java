package com.vam.service;

import com.vam.model.MemberVO;

public interface MemberService {
	
	// 회원가입
	public void memberJoin(MemberVO member) throws Exception;
	
	// Id 중복 체크
	public int idCheck(String memberId) throws Exception;
	
    /* 로그인 */
    public MemberVO memberLogin(MemberVO member) throws Exception;
    
    /* 주문자 정보 */
    public MemberVO getMemberInfo(String memberId);

}

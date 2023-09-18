package com.vam.controller;

import java.util.Random;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.vam.model.MemberVO;
import com.vam.service.MemberService;

@Controller
@RequestMapping("/member")
public class MemberController {
	
	private static final Logger logger = LoggerFactory.getLogger(MemberController.class);
	
	@Autowired
	private BCryptPasswordEncoder pwEncoder;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private MemberService memberService;
	
	@GetMapping("/join")
	public void joinGET() throws Exception {
		logger.info("회원가입 페이지 진입");
		
	}
	@PostMapping("/join")
	public String joinPOST(MemberVO member)throws Exception {
		
		logger.info("join 진입");
		
		String rawPw;		// 인코딩 전 비밀번호
		String encodePw;	// 인코딩 후 비밀번호
		
		rawPw = member.getMemberPw();			// 비밀번호 데이터 얻음
		encodePw = pwEncoder.encode(rawPw);		// 비밀번호 인코딩
		member.setMemberPw(encodePw);			// 인코딩된 비밀번호 member 객체에 다시 저장
		
		memberService.memberJoin(member);
		/*
		// 회원가입 서비스 실행
		memberService.memberJoin(member);
		
		logger.info("join Service 성공");
		*/
		
		return "redirect:/main";
	}
	
	@GetMapping("/login")
	public void loginGET() {
		logger.info("로그인 페이지 진입");
	}
	
	// 아이디 중복 검사
	@ResponseBody
	@PostMapping("/memberIdChk")
	public String memberIdChkPOST(String memberId) throws Exception{
		
		//logger.info("memberIdChk() 진입");
		
		int result =  memberService.idCheck(memberId);
		
		// logger.info("결과값 = " + result);
		
		if(result != 0) {
			return "fail"; // 중복 아이디가 존재
		}else {
			return "success"; // 중복 아이디 x
		}
	}
	
	/* 이메일 인증 */
	@GetMapping("/mailCheck")
	@ResponseBody
	public String mailChekGET(String email) throws Exception{
		
		/* view 로 부터 넘어온 데이터 확인 */
		
		logger.info("이메일 데이터 전송 확인");
		logger.info("인증번호 : " + email);
		
		/* 인증번호(난수) 생성 */
		Random random = new Random();
		int checkNum = random.nextInt(999999);
		logger.info("인증번호" + checkNum);
		
		/* 이메일 보내기 */
		String setFrom = "jhj930215@naver.com";
		String toMail = email;
		String title = "회원가입 인증 이메일 입니다";
		String content = 
				"홈페이지를 방문해주셔서 감사합니다."+
				"<br><br>" + 
				"인증번호는 " + checkNum + "입니다." + 
				"<br>"+
				"해당 인증 번호를 인증번호 확인란에 기입하여 주세요.";
		

		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
			helper.setFrom(setFrom);
			helper.setTo(toMail);
			helper.setSubject(title);
			helper.setText(content,true);
			mailSender.send(message);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		String num = Integer.toString(checkNum);
		return num;
	}
	
	/* 로그인 */
	@PostMapping("/login.do")
	public String loginPOST(HttpServletRequest request, MemberVO member, RedirectAttributes rttr) throws Exception {
		/*
		System.out.println("login 메서드 진입");
		System.out.println("전달된 데이터 : " + member);
		
		
		HttpSession session = request.getSession();
		MemberVO lvo = memberService.memberLogin(member);
		
		if(lvo == null) {									// 일치하지 않는 아이디, 비밀번호 입력 경우
			int result = 0;
			rttr.addFlashAttribute("result", result);
			return "redirect:/member/login";
		}
		
		session.setAttribute("member", lvo);				 // 일치하는 아이디, 비밀번호 경우 (로그인 성공)
        
		rttr.addFlashAttribute("msg", "success");
		return "redirect:/main";
		*/
		
		HttpSession session = request.getSession();
		String rawPw;
		String encodePw;
		
		MemberVO lvo = memberService.memberLogin(member);		// 제출한 아이디와 일치
		
		if(lvo != null) {		// 일치하는 아이디 존재 시
			rawPw = member.getMemberPw();		// 사용자가 제출한 비밀번호
			encodePw = lvo.getMemberPw();		// 데이터베이스에 저장한 인코딩된 비밀번호
			
			if(true == pwEncoder.matches(rawPw, encodePw)) { 	// 비밀번호 이리치여부 판단
				
				lvo.setMemberPw("");		// 인코딩된 비밀번호 정보를 지움
				session.setAttribute("member", lvo);	// session에 사용자의 정보 저장
				rttr.addFlashAttribute("msg","success");
				return "redirect:/main";
				
			}else {
				rttr.addAttribute("result",0);
				return "redirect:/member/login";	
			}
			
		}else {					// 일치하는 아이디가 존재하지 않을 시(로그인 실패)
			rttr.addAttribute("result",0);
			return "redirect:/member/login";	// 로그인 페이지로 이동
		}
		
	}
	
	/* 로그아웃 */
	@GetMapping("/logout.do")
	public String logoutMainGET(HttpServletRequest request) throws Exception{
		
		logger.info("logoutMainGET 메서드 진입");
		
		HttpSession session = request.getSession();	
		
		session.invalidate();
		
		return "redirect:/main";
		
	}
	
	/* 비동기방식의 로그아웃 */
	@ResponseBody
	@PostMapping("/logout.do")
	public void logoutPOSt(HttpServletRequest request) throws Exception{
		
		logger.info("비동기 로그아웃 메서드 진입");
		
		HttpSession session = request.getSession();
		
		session.invalidate();
		
	}
	
}

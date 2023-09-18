package com.vam.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vam.mapper.MemberMapper;
import com.vam.model.AttachImageVO;
import com.vam.model.AuthorVO;
import com.vam.model.BookVO;
import com.vam.model.Criteria;
import com.vam.model.MemberVO;
import com.vam.model.OrderCancelDTO;
import com.vam.model.OrderDTO;
import com.vam.model.PageDTO;
import com.vam.service.AdminService;
import com.vam.service.AuthorService;
import com.vam.service.MemberService;
import com.vam.service.OrderService;

import net.coobird.thumbnailator.Thumbnails;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private AdminService adminService;
	
	@Autowired
	private AuthorService authorService;
	
	/* 관리자 메인 페이지 이동 */
	@GetMapping("/main")
	public void adminMainGET() throws Exception{
		logger.info("관리자 페이지 이동");
	}
	
	/* 상품 관리 페이지 접속 */
	@GetMapping("/goodsManage")
	public void goodManageGET(Criteria cri, Model model) throws Exception {
		logger.info("상품 관리 페이지 접속");
		
		/* 상품 리스트 데이터*/
		List list = adminService.goodsGetList(cri);
		
		if(!list.isEmpty()) {
			model.addAttribute("list", list);
		} else {
			model.addAttribute("listCheck", "empty");
			return;
		}
		
		PageDTO pageDTO = new PageDTO(cri, adminService.goodsGetTotal(cri));
		
		/* 페이지 인터페이스 데이터 */
		model.addAttribute("pageMaker",pageDTO);
	}
	
	/* 상품 등록 페이지 접속 */
	@GetMapping("/goodsEnroll")
	public void GoodsEnrollGET(Model model) throws Exception {
		logger.info("상품 등록 페이지 접속");
		
		ObjectMapper objm = new ObjectMapper();
		
		List list = adminService.cateList();
		
		String cateList = objm.writeValueAsString(list);
		
		model.addAttribute("cateList", cateList);
		
		logger.info("변경 전.................." + list);
		logger.info("변경 후 ..................." + cateList);
	}
	
	/* 상픔 등록 */
	@PostMapping("/goodsEnroll")
	public String goodsEnrollPOST(BookVO book, RedirectAttributes rttr) {
		
		logger.info("goodsEnrollPOST..............."+book);

		adminService.bookEnroll(book);

		rttr.addFlashAttribute("enroll_result", book.getBookName());
		
		return "redirect:/admin/goodsManage";
		
	}
	
	/* 상품 조회 페이지 */
	@GetMapping({"/goodsDetail", "/goodsModify"})
	public void goodsGetInfoGET(int bookId, Criteria cri, Model model)throws Exception {
		
		logger.info("goodsGetInfo().............." + bookId);
		
		ObjectMapper mapper = new ObjectMapper();
		
		/* 카테고리 리스트 데이터 */
		model.addAttribute("cateList", mapper.writeValueAsString(adminService.cateList()));
		
		/* 목록 페이지 조건 정보 */
		model.addAttribute("cri", cri);
		
		
		/* 조회 페이지 정보 */
		model.addAttribute("goodsInfo", adminService.goodsGetDetail(bookId));
	}
	
	/* 상품 정보 수정 */
	@PostMapping("/goodsModify")
	public String goodsModifyPOST(BookVO vo, RedirectAttributes rttr) {
		
		logger.info("goodsModifyPOST.........." + vo);
		
		int result = adminService.goodsModify(vo);
		
		rttr.addFlashAttribute("modify_result", result);
		
		return "redirect:/admin/goodsManage";
		
	}
	 
	/* 상품 정보 삭제 */
	@PostMapping("/goodsDelete")
	public String goodsDeletePOST(int bookId, RedirectAttributes rttr) {
		
		logger.info("goodsDeleteePPOST........");
		
		List<AttachImageVO> fileList = adminService.getAttachInfo(bookId);
		
		if(fileList != null) {
			
			List<Path> pathList = new ArrayList();
			
			fileList.forEach(vo ->{
				
				// 원본 이미지
				Path path = Paths.get("C:\\upload", vo.getUploadPath(), vo.getUuid() + "_" + vo.getFileName());
				pathList.add(path);
				
				// 썸네일 이미지
				path = Paths.get("C:\\upload", vo.getUploadPath(), "s_" + vo.getUuid() + "_" + vo.getFileName());
				pathList.add(path);
			});
			
			pathList.forEach(path ->{
				path.toFile().delete();
			});
			
		}
		
		int result = adminService.goodsDelete(bookId);
		
		rttr.addFlashAttribute("delete_result", result);
		
		return "redirect:/admin/goodsManage";
	}
	
	/* 작가 검색 팝업창 */
	@GetMapping("authorPop")
	public void authorPopGET(Criteria cri, Model model) throws Exception{
		
		logger.info("authorPopGET................");
			
		cri.setAmount(5);
		
		List list = authorService.authorGetList(cri);
		
		/* 페이지 이동 인터페이스 데이터 */
		int total = authorService.authorGetTotal(cri);
		
		PageDTO pageMaker = new PageDTO(cri, total);
		
		if(!list.isEmpty()) {
			model.addAttribute("list", list);	// 존재 경우
		}else {
			model.addAttribute("listCheck", "empty");	//존재하지 않는 경우
		}
		
		model.addAttribute("pageMaker", pageMaker);
		
	}
	
	/* 작가 관리 페이지 접속 */
	@GetMapping("/authorManage")
	public void authorManageGET(Criteria cri, Model model) throws Exception {
		logger.info("작가 관리 페이지 접속" + cri);
		
		List list = authorService.authorGetList(cri);
		
		/* 페이지 이동 인터페이스 데이터 */
		int total = authorService.authorGetTotal(cri);
		
		PageDTO pageMaker = new PageDTO(cri, total);
		
		if(!list.isEmpty()) {
			model.addAttribute("list", list);	// 존재 경우
		}else {
			model.addAttribute("listCheck", "empty");	//존재하지 않는 경우
		}
		
		model.addAttribute("pageMaker", pageMaker);
		
	}
	
	/* 작가 등록 페이지 접속 */
	@GetMapping("/authorEnroll")
	public void authorEnrollGET() throws Exception {
		logger.info("작가 등록 페이지 접속");
	}
	
	/* 작가 등록 */
	@PostMapping("/authorEnroll.do")
	public String authorEnrollPOST(AuthorVO author, RedirectAttributes rttr) throws Exception {
		
		logger.info("authorEnroll : " + author);
		
		authorService.authorEnroll(author);	// 작가 쿼리 수행
		
		rttr.addFlashAttribute("enroll_result", author.getAuthorName());
		
		return "redirect:/admin/authorManage";
	}
	
	/* 작가 상세 */
	@GetMapping({"/authorDetail", "/authorModify"})
	public void authorGetInfoGET(int authorId, Criteria cri, Model model) throws Exception{
		
		logger.info("authorDetail............"+ authorId);
		
		AuthorVO authorInfo = authorService.authorGetDetail(authorId);
		
		/* 작가 관리 페이지 정보 */
		model.addAttribute("cri", cri);
		
		/* 선택 작가 정보 */
		model.addAttribute("authorInfo", authorInfo);
		
	}
	
	/* 작가 정보 수정 */
	@PostMapping("/authorModify")
	public String authorModifyPOST(AuthorVO author, RedirectAttributes rttr)throws Exception{
		
		logger.info("authorModifyPOST..................?"+author);
		
		int result = authorService.authorModify(author);
		
		rttr.addFlashAttribute("modify_result", result);
		
		return "redirect:/admin/authorManage";
	}
	
	/* 작가 정보 삭제 */
	@PostMapping("/authorDelete")
	public String authorDeletePOST(int authorId, RedirectAttributes rttr) {
		
		logger.info("authorDeletePOST...............");
		
		
		int result;
		
		try {
			result = authorService.authorDelete(authorId);
		} catch (Exception e) {
			e.printStackTrace();
			result = 2;
			rttr.addFlashAttribute("delete_result", result);
			
			return "redirect:/admin/authorManage";
		}
		rttr.addFlashAttribute("delete_result", result);
		return "redirect:/admin/authorManage";
	}
	
	/* 첨부 파일 업로드 */
	@PostMapping(value="/uploadAjaxAction", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<List<AttachImageVO>> uploadAjaxActionPOST(MultipartFile[] uploadFile) {
		logger.info("uploadAjaxActionPOST.................");
		
		/* 이미지 파일 체크 */
		for(MultipartFile multipartFile : uploadFile) {
			File checkFile = new File(multipartFile.getOriginalFilename());
			String type = null;
			
			try {
				type = Files.probeContentType(checkFile.toPath());
				logger.info("MIME TYPE : " + type);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(!type.startsWith("image")) {
				List<AttachImageVO> list = null;
				return new ResponseEntity<>(list, HttpStatus.BAD_REQUEST);
			}
		}
		
		String uploadFolder = "C:\\upload";
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String str = sdf.format(date);
		String datePath = str.replace("-", File.separator);
		
		/* 폴더생성 */
		File uploadPath = new File(uploadFolder, datePath);
		if(uploadPath.exists()==false) {
			uploadPath.mkdirs();
		}
		
		/* 이미지 정보 담는 객체 */
		List<AttachImageVO> list = new ArrayList();
		
		for(MultipartFile multipartFile : uploadFile) {
			
			/* 이미지 정보 객체 */
			AttachImageVO vo = new AttachImageVO();
			
			/* 파일 이름 */
			String uploadFileName = multipartFile.getOriginalFilename();
			vo.setFileName(uploadFileName);
			vo.setUploadPath(datePath);
			
			/* uuid 적용 파일 이름 */
			String uuid = UUID.randomUUID().toString();
			vo.setUuid(uuid);
			
			uploadFileName = uuid + "_" + uploadFileName;
			
			/* 파일 위치, 파일 이름을 합친 File 객체 */
			File saveFile = new File(uploadPath, uploadFileName);
			
			/* 파일 저장 */
			try {
				multipartFile.transferTo(saveFile);
				
				/* 썸네일 생성(ImageIO) */
//				File thumbnailFile = new File(uploadPath, "s_" + uploadFileName);
//				
//				BufferedImage bo_image = ImageIO.read(saveFile);
//				
//				/* 비율 */
//				double ratio = 3;
//				/* 넓이 높이 */
//				int width = (int)(bo_image.getWidth()/ratio);
//				int height = (int)(bo_image.getHeight()/ratio);
//				
//				BufferedImage bt_image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
//				
//				Graphics2D graphic = bt_image.createGraphics();
//				graphic.drawImage(bo_image,0,0,width,height,null);
//				
//				ImageIO.write(bt_image, "jpg", thumbnailFile);
				
				/* 방법 2 */
				File thumbnailFile = new File(uploadPath, "S_" + uploadFileName);
				
				BufferedImage bo_image = ImageIO.read(saveFile);
				
				/* 비율 */
				double ratio = 3;
				/* 넓이 높이 */
				int width = (int) (bo_image.getWidth() / ratio);
				int height = (int) (bo_image.getHeight() / ratio);				
				
				Thumbnails.of(saveFile)
				.size(width, height)
				.toFile(thumbnailFile);
				
			}catch(Exception e) {
				e.printStackTrace();
			}
			list.add(vo);
			
		}
		
		ResponseEntity<List<AttachImageVO>> result = new ResponseEntity<List<AttachImageVO>>(list, HttpStatus.OK);
		
		return result;
	}
	
	/* 이미지 파일 삭제 */
	@PostMapping("/deleteFile")
	public ResponseEntity<String> deleteFile(String fileName){
		logger.info("delteFile................" + fileName);
		
		File file = null;
		
		try {
			/* 썸네일 파일 삭제 */
			file = new File("c:\\upload\\" + URLDecoder.decode(fileName, "UTF-8"));
			
			file.delete();
			
			/* 원본 파일 삭제 */
			String originFileName = file.getAbsolutePath().replaceFirst("S_","");
			
			logger.info("originFileName : " + originFileName);
			
			file = new File(originFileName);
			
			file.delete();
			
		}catch(Exception e) {
			e.printStackTrace();
			
			return new ResponseEntity<String>("fail", HttpStatus.NOT_IMPLEMENTED);
		}
		
		return new ResponseEntity<String>("success", HttpStatus.OK);
	}
	
	/* 주문 현황 페이지 */
	@GetMapping("/orderList")
	public String orderListGET(Criteria cri, Model model) {
		
		List<OrderDTO> list = adminService.getOrderList(cri);
		
		if(!list.isEmpty()) {
			model.addAttribute("list", list);
			PageDTO dto = new PageDTO(cri, adminService.getOrderTotal(cri));
			
			model.addAttribute("pageMaker", dto);
		} else {
			model.addAttribute("listCheck", "empty");
		}
		
		
		
		return "/admin/orderList";
	}
	
	/* 주문 삭제 */
	@PostMapping("/orderCancel")
	public String orderCancelPOST(OrderCancelDTO dto, HttpServletRequest request) {
		
		orderService.orderCancel(dto);
		
		MemberVO member = new MemberVO();
		member.setMemberId(dto.getMemberId());
		
		HttpSession session = request.getSession();
		
		MemberVO memberLogin;
		
		try {
			memberLogin = memberService.memberLogin(member);
			memberLogin.setMemberPw("");
			session.setAttribute("member", memberLogin);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		
		return "redirect:/admin/orderList?keywor=" + dto.getKeyword() + "&amount=" + dto.getAmount() + "&pageNum=" + dto.getPageNum();
	}

}

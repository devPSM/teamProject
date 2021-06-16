package ksmart39.psmybatis.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ksmart39.psmybatis.domain.Member;
import ksmart39.psmybatis.service.MemberService;

@Controller
public class MemberController {
	
	
	private static final Logger log = LoggerFactory.getLogger(MemberController.class);

	
	/* 생성자 주입 방식 */
	private final MemberService memberService;
	
	@Autowired
	public MemberController(MemberService memberService) {
		this.memberService = memberService;
	}
	/**
	 * @ResponseBody 있어야 json방식으로 받을 수 있음
	 * @ResponseBody는 메서드 위에 postmapping처럼 위에다 붙여서 쓸 수 있음. 
	 */
	@PostMapping("/memberIdCheck")
	public @ResponseBody boolean memberIdCheck(@RequestParam(name="memberId", required = false)String memberId) {
		boolean idCheck = true;
		log.info("memberIdCheck     memberId  :::::  {}"+memberId);
		// 아이디체크 중복된 아이디 있는 경우 false로 바꿔주기
		
		
		Member memberCheck = memberService.modifyMember(memberId);
		
		if(memberCheck != null && memberCheck.getMemberId().equals(memberId)) {
			log.info("중복아이디 있음");
			idCheck = false;
		}else if(memberCheck == null){
			log.info("중복아이디 없음");
			idCheck = true;
		};
		
		return idCheck;
	}
	
	/*-------------------------------------------------------------------로그아웃 시작---------------------------------------------------------------*/
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
	/*-------------------------------------------------------------------로그아웃 끝---------------------------------------------------------------*/
	
	/*-------------------------------------------------------------------로그인 시작---------------------------------------------------------------*/
	/* HttpServletRequest request -> request.getSession
	 * 어차피 session을 요청하는거면 더 쉽게 바로 접근하자
	 * HttpSession session
	 * */
	@PostMapping("/login")
	public String login(@RequestParam(value = "memberId", required = false) String memberId
					  , @RequestParam(value = "memberPw", required = false) String memberPw
					  , HttpSession session
					  , RedirectAttributes reAttr) {
		
		if(memberId != null && !"".equals(memberId) && memberPw != null && !"".equals(memberPw)) {
			Map<String, Object> resultMap = memberService.loginMember(memberId, memberPw);
			
			// Object 로 오기 때문에 다운케스트 해줘야함
			boolean loginCheck = (boolean) resultMap.get("loginCheck");
			Member loginMember = (Member) resultMap.get("loginMember");
			
			if(loginCheck) {
				session.setAttribute("SID", 		loginMember.getMemberId());
				session.setAttribute("SNAME", 		loginMember.getMemberName());
				session.setAttribute("SLEVEL", 		loginMember.getMemberLevel());
				if(loginMember.getMemberLevel().equals("1")) {
					session.setAttribute("SLEVELNAME", "관리자");
				}else if(loginMember.getMemberLevel().equals("2")) {
					session.setAttribute("SLEVELNAME", "판매자");
				}else if(loginMember.getMemberLevel().equals("3")) {
					session.setAttribute("SLEVELNAME", "구매자");
				}
				return "redirect:/";
			}
		}
		//login?loginResult=등록된 회원이 없습니다. 와 같은방식으로 넘어감 requestAttributes 로 해야 자동 인코딩이 됨.
		reAttr.addAttribute("loginResult", "등록된 회원이 없습니다.");
		
		return "redirect:/login";
	}
	
	@GetMapping("/login")
	public String login(Model model, @RequestParam(name = "loginResult", required = false) String loginResult) {
		
		model.addAttribute("title", "로그인화면");
		if(loginResult != null) model.addAttribute("loginResult", loginResult);
		
		return "login/login";
	}
	/*-------------------------------------------------------------------로그인 끝---------------------------------------------------------------*/
	/*-------------------------------------------------------------- 전체 회원 조회 시작 ------------------------------------------------------------*/
	@GetMapping("/memberList")
	public String memberList(Model model) {
		List<Member> memberList = memberService.getMemberList();
		
		log.info("★ - 전체회원 조회 결과 : {}"+memberList);
		
		model.addAttribute("memberList", memberList);
		model.addAttribute("title", "memberList practice");
		return "member/memberList";
	}
	/*-------------------------------------------------------------- 전체 회원 조회 끝 --------------------------------------------------------------*/

	/*-------------------------------------------------------------- 회원 가입 시작 ------------------------------------------------------------*/
	@GetMapping("/addMember")
	public String addMember(Model model) {
		model.addAttribute("title", "addMember practice");
		return "member/addMember";
	}
	
	@PostMapping("/addMember")
	public String addMember(Member member) {
		log.info("★ - 가입 화면에서 입력받은 회원가입 정보 : {}" + member);
		memberService.addMember(member);
		
		return "redirect:/memberList";
	}
	/*-------------------------------------------------------------- 회원 가입 끝 --------------------------------------------------------------*/
	
	/*-------------------------------------------------------------- 회원 수정 시작 --------------------------------------------------------------*/
	@GetMapping("/updateMember")
	public String updateMember(@RequestParam(name = "memberId", required = false) String memberId
							  ,@RequestParam(name = "agreeTerms", required = false) boolean agreeTerms
							  ,Model model) {
		log.info("★ - 수정 동의 여부 체크 : {}" + agreeTerms);
		log.info("★ - 수정할 회원 아이디 : {}" + memberId);
		Member member = memberService.modifyMember(memberId);
		
		model.addAttribute("member", member);
		model.addAttribute("title", "updateMember practice");
		return "member/updateMember";
	}
	
	@PostMapping("/updateMember")
	public String updateMember(Member member) {
		log.info("★ - 수정화면에서 입력받은 회원수정 정보 : {}" + member);
		memberService.updateMember(member);
		return "redirect:/memberList";
	}
	/*-------------------------------------------------------------- 회원 수정 끝 --------------------------------------------------------------*/
	
	/*-------------------------------------------------------------- 회원 삭제 시작 --------------------------------------------------------------*/
	@GetMapping("/deleteMember")
	public String deleteMember(@RequestParam(name = "memberId", required = false) String memberId, Model model
							  ,@RequestParam(name = "fail", required = false) String fail
							  ,Member member) {
		log.info("★ - 삭제할 회원 아이디 : {}" + memberId);
		log.info("member에 담긴 값 {}"+member);
		model.addAttribute("memberId", memberId);
		model.addAttribute("title", "DeleteMember practice");
		
		if(fail != null) {
			model.addAttribute("fail", fail);
		}
		
		return "member/deleteMember";
	}
	@PostMapping("/deleteMember")
	public String deleteMember(@RequestParam(name = "memberId", required = false) String memberId
							  ,@RequestParam(name = "memberPw", required = false) String memberPw
							  ,@RequestParam(name = "memberPwCheck", required = false) String memberPwCheck
							  ,RedirectAttributes redirectAttr) {
		log.info("★ - 삭제화면에서 입력받은 회원삭제 정보 : [아이디 : {}"+memberId+"][패스워드 : {}"+memberPw+"][패스워드확인 : {}"+memberPwCheck+"]");
		if(memberPw != null && !"".equals(memberPw)) {
			if(memberPwCheck != null && !"".equals(memberPwCheck)) {
				if(memberPw.equals(memberPwCheck)) {
					boolean fail = memberService.deleteMember(memberId, memberPw);
					if(fail) {
						return "redirect:/memberList";
					}
				}
			}
		}
		redirectAttr.addAttribute("memberId", memberId);
		redirectAttr.addAttribute("fail", "비밀번호 불일치");
		return "redirect:/deleteMember";
	}
	/*-------------------------------------------------------------- 회원 삭제 끝 --------------------------------------------------------------*/
	
}

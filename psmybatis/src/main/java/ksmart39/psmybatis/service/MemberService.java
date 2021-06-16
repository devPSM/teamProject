package ksmart39.psmybatis.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ksmart39.psmybatis.dao.MemberMapper;
import ksmart39.psmybatis.domain.Member;

@Service
@Transactional
public class MemberService {
	
	@Autowired
	private MemberMapper memberMapper;

	public List<Member> getMemberList() {
		return memberMapper.getMemberList();
	}
	
	public int addMember(Member member) {
		return memberMapper.addMember(member);
	}
	
	public int updateMember(Member member) {
		return memberMapper.updateMember(member);
	}
	
	public Member modifyMember(String memberId) {
		return memberMapper.modifyMember(memberId);
	}
	
	public boolean deleteMember(String memberId, String memberPw) {
		//삭제 상태
		boolean deleteCheck = false;
		
		//한명 조회 DTO (아이디 비번 맞으면 삭제프로세스 시작)
		Member member = memberMapper.modifyMember(memberId);
		if(member != null & memberPw.equals(member.getMemberPw())) {
			System.out.println("☢☢☢☢    삭제 프로세스를 시작합니다    ☢☢☢☢");
			//판매자 정보 삭제
			System.out.println("회원 권한 체크 : [ "+member.getMemberLevel()+" ]");
			if(member.getMemberLevel().equals("2")) {
				System.out.println("☢☢☢☢     판매자 정보를 삭제합니다    ☢☢ ☢☢ ");
				memberMapper.orderDeleteBySellerId(memberId);
				memberMapper.goodsDelete(memberId);
			//구매자 정보 삭제
			}else if(member.getMemberLevel().equals("3")) {
				System.out.println("☢☢☢☢     구매자 정보를 삭제합니다    ☢☢☢☢ ");
				memberMapper.orderDeleteById(memberId);
			}
			// 삭제프로세스 공통 && 나중처리 프로세스 실행
			memberMapper.loginDelete(memberId);
			memberMapper.memberDelete(memberId);
			deleteCheck = true;
		}else {
			System.out.println("❗❕❗❕    삭제 프로세스를 실패하였습니다    ❗❕❗❕");
		}
		return deleteCheck;
	}
	
	//로그인 체크
	public Map<String, Object> loginMember(String memberId, String memberPw) {
		//로그인 여부
		boolean loginCheck = false;
		
		//로그인 결과를 담을 곳
		Map<String, Object> memberInfoMap = new HashMap<String, Object>();
		
		//로그인 처리
		// 입력받은 아이디로 한명 회원 조회
		Member member = memberMapper.modifyMember(memberId);
		// 입력받은 아이디로 조회한 정보와 입력받은 비밀번호 일치 확인
		if(member != null && memberPw.equals(member.getMemberPw())) {
			//조회결과 입력받은 정보와 조회한 정보가 같으면 로그인체크 상태 변경
			loginCheck = true;
			memberInfoMap.put("loginMember", member);
		}
		
		// 맵에 로그인 체크 상태가 담긴 변수 담기
		memberInfoMap.put("loginCheck", loginCheck);
		
		return memberInfoMap;
	}
	
}

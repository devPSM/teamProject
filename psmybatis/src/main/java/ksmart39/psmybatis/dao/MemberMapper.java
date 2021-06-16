package ksmart39.psmybatis.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import ksmart39.psmybatis.domain.Member;

@Mapper
public interface MemberMapper {
	//회원 가입												- INSERT
	public int addMember(Member member);
	
	//전체 회원 조회											- SELECT
	public List<Member> getMemberList();
	
	//한명 회원 조회 (수정 폼)									- SELECT
	public Member modifyMember(String member);
	
	//회원 수정												- UPDATE
	public int updateMember(Member member);
	
	//로그인 테이블 삭제										- DELETE
	public int loginDelete(String memberId);
	
	//상품 테이블 삭제										- DELETE
	public int goodsDelete(String memberId);
	
	//주문 테이블(구매자) 삭제									- DELETE
	public int orderDeleteById(String memberId);
	
	//주문 테이블(판매자) 삭제									- DELETE
	public int orderDeleteBySellerId(String memberId);
	
	//회원 테이블 삭제										- DELETE
	public int memberDelete(String memberId);
}

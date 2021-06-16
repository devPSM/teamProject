package ksmart39.psmybatis.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ksmart39.psmybatis.dao.GoodsMapper;
import ksmart39.psmybatis.dao.MemberMapper;
import ksmart39.psmybatis.domain.Goods;
import ksmart39.psmybatis.domain.Member;

@Service
@Transactional
public class GoodsService {
	
	
	private static final Logger log = LoggerFactory.getLogger(GoodsService.class);

	
	private final MemberMapper memberMapper;
	private final GoodsMapper goodsMapper;
	
	@Autowired
	public GoodsService(GoodsMapper goodsMapper, MemberMapper memberMapper) {
		this.goodsMapper = goodsMapper;
		this.memberMapper = memberMapper;
	}
	
	
	// 상품 목록 조회
	public List<Goods> getGoodsList(Map<String, Object> paramMap){
		return goodsMapper.getGoodsList(paramMap);
	}
	
	// 상품등록
	public int addGoods(Goods goods) {
		return goodsMapper.addGoods(goods);
	}
	
	// 상품 한개 조회
	public Goods getGoods(String goodsCode) {
		return goodsMapper.getGoods(goodsCode);
	}
	
	// 상품수정
	public int modifyGoods(Goods goods) {
		return goodsMapper.modifyGoods(goods);
	}
	
	// 상품삭제
	public boolean deleteGoods(String goodsSellerId, String memberPw, String goodsCode) {
		boolean deleteGoodsCheck = false;
		
		Member member = memberMapper.modifyMember(goodsSellerId);
		Goods goods = goodsMapper.getGoods(goodsCode);
		
		log.info("현위치 goodsService : " + goodsSellerId +" / "+ memberPw +" / "+ goodsCode);
		
		if(member != null && memberPw.equals(member.getMemberPw())) {
			log.info("========= 아이디패스워드 일치 :: 상품 삭제 프로세스 시작 ==========");
			if(goods != null && goodsSellerId.equals(goods.getGoodsSellerId())) {
				
				goodsMapper.deleteGoods(goodsCode);
				log.info("========= 상품 삭제 프로세스 완료 ==========");
				deleteGoodsCheck = true;
			}
		}else {
			log.info("========= 상품 삭제 프로세스 실패 ==========");
		}
		return deleteGoodsCheck;
	}
}














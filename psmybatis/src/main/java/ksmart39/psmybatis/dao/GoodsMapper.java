package ksmart39.psmybatis.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import ksmart39.psmybatis.domain.Goods;

@Mapper
public interface GoodsMapper {
	
	//상품 목록 조회
	public List<Goods> getGoodsList(Map<String, Object> paramMap);
	
	//상품 하나 조회
	public Goods getGoods(String goodsCode);
	
	//상품 등록
	public int addGoods(Goods goods);
	
	//상품 수정
	public int modifyGoods(Goods goods);
	
	//상품 삭제
	public int deleteGoods(String goodsCode);
}

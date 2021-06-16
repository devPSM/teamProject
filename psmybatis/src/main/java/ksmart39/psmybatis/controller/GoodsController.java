package ksmart39.psmybatis.controller;

import java.util.HashMap;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ksmart39.psmybatis.domain.Goods;
import ksmart39.psmybatis.service.GoodsService;

@Controller
public class GoodsController {
	
	
	private static final Logger log = LoggerFactory.getLogger(GoodsController.class);

	
	private final GoodsService goodsService;
	
	@Autowired
	public GoodsController(GoodsService goodsService) {
		this.goodsService = goodsService;
	}
	
	
	
	
/*---------------------------------------------------------------------------------------------[전체 상품 조회 시작]-----*/
	@GetMapping("/goodsList")
	public String getGoodsList(Model model
							 , @RequestParam(name="goodsSellerId", required = false)String goodsSellerId) {
		log.info("goodsSellerId 에 담긴 값 : {}", goodsSellerId);
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if(goodsSellerId != null) paramMap.put("goodsSellerId", goodsSellerId);
		
		
		List<Goods> goodsList = goodsService.getGoodsList(paramMap);
		
		model.addAttribute("title", "상품목록");
		model.addAttribute("goodsList", goodsList);
		return "goods/goodsList";
	}
/*----------------------------------------------------------------------------------------------[전체 상품 조회 끝]------*/
/*----------------------------------------------------------------------------------------------[ 상품 등록 시작 ]------*/
	@PostMapping("/addGoods")
	public String addGoods(Goods goods, HttpSession session,RedirectAttributes reAttr) {
		String goodsSellerId = (String) session.getAttribute("SID");
		
		//1. 등록
		if(goodsSellerId != null) {
			if(goods.getGoodsName() != null && goods.getGoodsPrice() != null) {
				goods.setGoodsSellerId(goodsSellerId);
				goodsService.addGoods(goods);
				
				//2. 등록 후에
				reAttr.addAttribute("goodsSellerId", goodsSellerId);
				return "redirect:/goodsList";
			}
		}
		
		reAttr.addAttribute("fail", "상품등록실패");
		return "redirect:/addGoods";
		
	}
	
	@GetMapping("/addGoods")
	public String addGoods(Model model, @RequestParam(name = "fail", required = false)String fail) {
		model.addAttribute("fail", fail);
		model.addAttribute("title", "상품등록");
		return "goods/addGoods";
	}
/*----------------------------------------------------------------------------------------------[ 상품 등록 끝 ]------*/
	
/*----------------------------------------------------------------------------------------------[ 상품 수정 시작 ]------*/
	@GetMapping("/modifyGoods")
	public String modifyGoods(@RequestParam(name = "goodsCode", required = false) String goodsCode
							  ,Model model) {
		log.info("★ - 수정할 상품 코드 : {}" + goodsCode);
		Goods goods = goodsService.getGoods(goodsCode);
		
		model.addAttribute("goods", goods);
		model.addAttribute("title", "상품수정");
		return "Goods/modifyGoods";
	}
	
	@PostMapping("/modifyGoods")
	public String modifyGoods(@RequestParam(name="goodsSellerId", required = false)String goodsSellerId
							 , Goods goods, RedirectAttributes reAttr) {
		log.info("★ - 수정화면에서 입력받은 상품수정 정보 : {}" + goods);
		goodsService.modifyGoods(goods);
		reAttr.addAttribute("goodsSellerId", goodsSellerId);
		return "redirect:/goodsList";
	}
/*----------------------------------------------------------------------------------------------[ 상품 수정 끝 ]------*/
	
/*----------------------------------------------------------------------------------------------[ 상품 삭제 시작 ]------*/
	@PostMapping("/deleteGoods")
	public String deleteGoods(@RequestParam(name = "goodsCode", required = false)String goodsCode
							 ,@RequestParam(name = "goodsSellerId", required = false)String goodsSellerId
							 ,@RequestParam(name = "memberPw", required = false) String memberPw
							 ,@RequestParam(name = "memberPwCheck", required = false) String memberPwCheck
							 ,RedirectAttributes redirectAttr) {
		log.info("★ - Delete폼에서 입력받은 삭세할 상품 정보 : [상품코드 : {}",goodsCode,"][판매자아이디 : {}",goodsSellerId,"][패스워드 : {}",memberPw,"][패스워드확인 : {}",memberPwCheck+"]");
		if(memberPw != null && !"".equals(memberPw)) {
			if(memberPwCheck != null && !"".equals(memberPwCheck)) {
				if(memberPw.equals(memberPwCheck)) {
					boolean fail = goodsService.deleteGoods(goodsSellerId, memberPw, goodsCode);
					if(fail) {
						redirectAttr.addAttribute("goodsSellerId", goodsSellerId);
						return "redirect:/goodsList";
					}
				}
			}
		}
		
		redirectAttr.addAttribute("goodsCode", goodsCode);
		redirectAttr.addAttribute("fail", "상품등록실패");
		return "redirect:/deleteGoods";
	}
	
	@GetMapping("/deleteGoods")
	public String deleteGoods(@RequestParam(name="goodsCode", required = false)String goodsCode, Model model
							, @RequestParam(name = "fail", required = false) String fail) {
		log.info("★ - goodsList 화면에서 가져온 삭제 요청 할 상품 코드 : {}",goodsCode);
		Goods goods = goodsService.getGoods(goodsCode);
		log.info("★ - 삭제할 상품코드를 통해 조회한 goods 도메인 : {}",goods);
		model.addAttribute("goods", goods);
		model.addAttribute("fail", fail);
		model.addAttribute("title", "상품삭제");
		return "Goods/deleteGoods";
	}
/*----------------------------------------------------------------------------------------------[ 상품 삭제 끝 ]------*/
	
	
}

package team.hunter.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import team.hunter.model.dto.Funding;
import team.hunter.model.dto.FundingAnswer;
import team.hunter.model.dto.FundingQuestion;
import team.hunter.model.dto.Member;
import team.hunter.model.dto.Paging;
import team.hunter.model.dto.PersonalQuestion;
import team.hunter.model.dto.Purchase;
import team.hunter.model.service.FundingAnswerService;
import team.hunter.model.service.FundingQuestionService;
import team.hunter.model.service.FundingRequestService;
import team.hunter.model.service.FundingService;
import team.hunter.model.service.MemberService;
import team.hunter.model.service.PersonalQuestionService;
import team.hunter.util.Constants;

@Controller
@RequestMapping("mypage")
public class myPageController {
	@Autowired private PersonalQuestionService personalQs;
	@Autowired private FundingQuestionService fundingQs;
	@Autowired private FundingAnswerService fundingAs;
	@Autowired private MemberService memberService;
	@Autowired private FundingRequestService fundingReqService;
	@Autowired private FundingService fundingService;
	
	@RequestMapping("myQuestion")
	public ModelAndView personalQuestionList(@RequestParam(defaultValue = "1") int curPage) {
		ModelAndView mv = new ModelAndView();
		Member member =(Member)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		int listCnt = personalQs.listCount(member.getCode());
		Paging paging = new Paging(listCnt, curPage);
		
		member.setStartIndex(paging.getStartIndex());
		member.setCntPerPage(paging.getPageSize());
		List<PersonalQuestion> list = personalQs.personalQuestionList(member);
		
		mv.addObject("list", list);
		mv.addObject("listCnt", listCnt);
		mv.addObject("paging", paging);
		mv.setViewName("mypage/personalQuestionList");
		
		return mv;
	}
	
	@RequestMapping("personalQuestionForm")
	public String personalQuestionForm() {
		return "form/personalQuestionForm";
	}
	
	@RequestMapping("personalQuestionInsert")
	public String personalQuestionInsert(PersonalQuestion personalQuestion) {
		Member member =(Member)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		personalQuestion.setMemberCode(member.getCode());
		personalQuestion.setState(Constants.BEFORE_ANSWER);
		personalQs.personalQuestionInsert(personalQuestion);
		return "redirect:/mypage/myQuestion";
	}

	
	@RequestMapping("fundingQuestion")
	public ModelAndView fundingQuestion(@RequestParam(defaultValue = "1") int curPage) {
		ModelAndView mv = new ModelAndView();
		Member member =(Member)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		int listCnt = fundingQs.listCount(member.getCode());
		Paging paging = new Paging(listCnt, curPage);
		System.out.println(paging.getListCnt());
		System.out.println(paging.getCurPage());
		System.out.println(paging.getStartIndex());
		System.out.println(paging.getPageSize());
		
		member.setStartIndex(paging.getStartIndex());
		member.setCntPerPage(paging.getPageSize());
		List<FundingQuestion> list = fundingQs.fundingQuestionList(member);
		System.out.println(list);
		
		mv.addObject("list", list);
		mv.addObject("listCnt", listCnt);
		mv.addObject("paging", paging);
		mv.setViewName("mypage/fundingQuestionList");
		
		return mv;
	}
	
	@RequestMapping("fundingQuestionDetailPage/{code}")
	public String fundingQuestionDetail(@PathVariable int code, Model model) {
		
		FundingQuestion question = fundingAs.selectByCodeQuestion(code);
		System.out.println(question.getMember().getId());
		System.out.println("question.getPersonalAnswer().getContent()"+question.getFunding().getMdCode());
		System.out.println("question.getPersonalAnswer().getContent()"+question.getFundingAnswer().getContent());
		System.out.println("question.getContent()" + question.getContent());
		
		
		model.addAttribute("question", question);
		
		return "mypage/fundingQuestionDetail";
	}
	
	@RequestMapping("myInfoMenu")
	public ModelAndView myInfoMenu() {
		Member member =(Member)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		member = memberService.selectMemberByCode(member.getCode());
		ModelAndView mv = new ModelAndView();
		mv.addObject("member", member);
		mv.setViewName("mypage/chooseMyInfoMenu");
		return mv;
	}
	
	/**
	 * ?????? ????????? ?????? ????????? 
	 * */
	@RequestMapping("/myOpenFunding")
	public ModelAndView myOpenFunding() {
		
		ModelAndView mv = new ModelAndView();
		try {
			Member member =(Member)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			List<Funding> myOpenFundingList = fundingReqService.myFundingOpenList(member.getCode());
			
			if(myOpenFundingList.size() > 9)
				myOpenFundingList = myOpenFundingList.subList(0, 9);
			
			
			mv.addObject("myOpenFundingList", myOpenFundingList);
			mv.addObject("mdCode", myOpenFundingList.get(0).getMdCode());
			mv.setViewName("mypage/myOpenFundingList");
		}catch (Exception e) {
			
			mv.setViewName("mypage/myOpenFundingListEmpty");
		}
		
		return mv;
	}
	
	@PostMapping("/changeMyInfo")
	public ModelAndView changeMyInfo(Member member) {
		if(member.getEmailAccept()==null) {
			member.setEmailAccept("0");
		}
		member = memberService.changeMyInfo(member);
		Member pvo=(Member)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		pvo.setName(member.getName());
		ModelAndView mv = new ModelAndView();
		mv.addObject("member", member);
		mv.setViewName("mypage/chooseMyInfoMenu");
		return mv;
	}
	
	/**
	 * ?????? ????????? ?????? ???????????????
	 * */
	@RequestMapping("/myOpenFunding/{fundingCode}")
	public String myOpenDetail(@PathVariable int fundingCode, Model model, @RequestParam(defaultValue = "1") int curPage) {
//		Member member = null;
//		if(!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser"))
//			member =(Member)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		System.out.println("fundingCode"+fundingCode);
		int listCnt = fundingReqService.listCount(fundingCode);
		Paging paging = new Paging(listCnt, curPage);

		
		int startIndex = paging.getStartIndex();
		int cntPerPage = paging.getPageSize();
		System.out.println("listCnt"+listCnt);
		System.out.println("startIndex"+startIndex+"cntPerPage"+cntPerPage+"????????? ?????????????");
		System.out.println("paging.prevPage"+paging.getPrevPage()+"paging.startPage"+paging.getStartPage()+"paging.endPage"+paging.getEndPage()+"paging.nextPage"+paging.getNextPage()+"????????? 222???????????");
		
		List<FundingQuestion> fundingReqManage = fundingReqService.myFundingOpenDetail(startIndex, cntPerPage, fundingCode);
		
		
		//List<FundingQuestion> fundingReqManage = fundingReqService.myFundingOpenDetail(fundingCode);
		List<Member> fundingOpenPeople = fundingReqService.myFundingOpenDetailSecond(fundingCode);
		Funding funding = fundingReqService.fundingState(fundingCode);
		Funding fundingInfo = fundingService.selectByCode(fundingCode);
		
		//?????? ?????? ?????????
		model.addAttribute("fundingCode", fundingCode);
		
		//?????? ?????? ??????
		model.addAttribute("fundingReqManage", fundingReqManage);
		//????????? ?????? ?????????
		model.addAttribute("listCnt", listCnt);
		model.addAttribute("paging", paging);
		
		//?????? ????????? ?????????
		model.addAttribute("fundingOpenPeople", fundingOpenPeople);
		
		//?????? ????????? ?????? ????????? ????????? ??? ?????? ???????????? ?????? ?????? ?????? ????????????
		model.addAttribute("funding", funding);
		model.addAttribute("fundingInfo", fundingInfo);
		return "mypage/myOpenFundingDetail";
	}
	
	/**
	 * ?????? ????????? ?????? ??????????????? - ?????? ????????? ??????(???????????? ???????????? + ????????????)
	 * */
	@RequestMapping("/myOpenFundingReqManage/{questionCode}")
	public String myOpenFundingReqManage(@PathVariable int questionCode, Model model) {
		FundingQuestion fundingQuestion = fundingReqService.myOpenFundingReqManage(questionCode);
		FundingAnswer fundingAnswer = fundingReqService.myOpenFundingAnswerManage(questionCode);
		
		model.addAttribute("fundingQuestion", fundingQuestion);
		model.addAttribute("fundingAnswer", fundingAnswer);
		
		
		return "mypage/myOpenFundingReqManage";
	}
	
	
	
	
	
	
	
//	
//	@ExceptionHandler({Exception.class})
//	public String error() {
//		return "error/errorPage";
//	}
	
	
}

package com.polus.fibicomp.grantcall.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.polus.fibicomp.grantcall.service.GrantCallService;
import com.polus.fibicomp.grantcall.vo.GrantCallVO;

@RestController
public class GrantCallController {

	protected static Logger logger = Logger.getLogger(GrantCallController.class.getName());

	@Autowired
	@Qualifier(value = "grantCallService")
	private GrantCallService grantCallService;

	@RequestMapping(value = "/loadGrantCallById", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String loadGrantCallById(@RequestBody GrantCallVO vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for loadGrantCallById");
		logger.info("grantCallId : " + vo.getGrantCallId());
		String grantCallDatas = grantCallService.loadGrantCallById(vo.getGrantCallId());
		return grantCallDatas;
	}

	@RequestMapping(value = "/createGrantCall", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String createGrantCall(@RequestBody GrantCallVO vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for createGrantCall");
		GrantCallVO grantCall = new GrantCallVO();
		String grantCallDatas = grantCallService.createGrantCall(grantCall);
		return grantCallDatas;
	}

	@RequestMapping(value = "/saveUpdateGrantCall", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String saveOrUpdateGrantCall(@RequestBody GrantCallVO vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for saveOrUpdateGrantCall");
		String grantCallDatas = grantCallService.saveOrUpdateGrantCall(vo);
		return grantCallDatas;
	}

	@RequestMapping(value = "/publishGrantCall", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String publishGrantCall(@RequestBody GrantCallVO vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for publishGrantCall");
		String grantCallDatas = grantCallService.publishGrantCall(vo);
		return grantCallDatas;
	}

	@RequestMapping(value = "/fetchSponsorsBySponsorType", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String fetchSponsorsBySponsorType(@RequestBody GrantCallVO vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for fetchSponsorsBySponsorType");
		logger.info("SponsorTypeCode : " + vo.getSponsorTypeCode());
		String grantCallDatas = grantCallService.fetchSponsorsBySponsorType(vo);
		return grantCallDatas;
	}

	@RequestMapping(value = "/saveOrUpdateGrantCall", method = RequestMethod.POST)
	public String saveUpdateGrantCall(@RequestParam(value = "files", required = false) MultipartFile[] files, @RequestParam("formDataJson") String formDataJson) {
		logger.info("Requesting for saveUpdateGrantCall");
		String grantCallDatas = grantCallService.saveUpdateGrantCall(files, formDataJson);
		return grantCallDatas;
	}

	@RequestMapping(value = "/deleteGrantCallKeyword", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String deleteGrantCallKeyword(@RequestBody GrantCallVO vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for deleteGrantCallKeyword");
		logger.info("grantCallId : " + vo.getGrantCallId());
		logger.info("grantKeywordId : " + vo.getGrantKeywordId());
		return grantCallService.deleteGrantCallKeyword(vo);
	}

	@RequestMapping(value = "/deleteGrantCallContact", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String deleteGrantCallContact(@RequestBody GrantCallVO vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for deleteGrantCallContact");
		logger.info("grantCallId : " + vo.getGrantCallId());
		logger.info("grantContactId : " + vo.getGrantContactId());
		return grantCallService.deleteGrantCallContact(vo);
	}

	@RequestMapping(value = "/deleteGrantCallAreaOfResearch", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String deleteGrantCallAreaOfResearch(@RequestBody GrantCallVO vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for deleteGrantCallAreaOfResearch");
		logger.info("grantCallId : " + vo.getGrantCallId());
		logger.info("grantResearchAreaId : " + vo.getGrantResearchAreaId());
		return grantCallService.deleteGrantCallAreaOfResearch(vo);
	}

	@RequestMapping(value = "/deleteGrantCallEligibility", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String deleteGrantCallEligibility(@RequestBody GrantCallVO vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for deleteGrantCallEligibility");
		logger.info("grantCallId : " + vo.getGrantCallId());
		logger.info("grantEligibilityId : " + vo.getGrantEligibilityId());
		return grantCallService.deleteGrantCallEligibility(vo);
	}

	@RequestMapping(value = "/deleteGrantCallAttachment", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String deleteGrantCallAttachment(@RequestBody GrantCallVO vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for deleteGrantCallAttachment");
		logger.info("grantCallId : " + vo.getGrantCallId());
		logger.info("attachmentId : " + vo.getAttachmentId());
		return grantCallService.deleteGrantCallAttachment(vo);
	}

	@RequestMapping(value = "/addGrantCallAttachment", method = RequestMethod.POST)
	public String addGrantCallAttachment(@RequestParam(value = "files", required = false) MultipartFile[] files, @RequestParam("formDataJson") String formDataJson) {
		logger.info("Requesting for addGrantCallAttachment");
		return grantCallService.addGrantCallAttachment(files, formDataJson);
	}

}

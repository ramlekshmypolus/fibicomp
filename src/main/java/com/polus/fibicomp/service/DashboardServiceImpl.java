package com.polus.fibicomp.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.polus.fibicomp.dao.DashboardDao;
import com.polus.fibicomp.pojo.ActionItem;
import com.polus.fibicomp.pojo.DashBoardProfile;
import com.polus.fibicomp.view.FibiActionList;
import com.polus.fibicomp.view.MobilePersonnelDetails;
import com.polus.fibicomp.view.MobileProfile;
import com.polus.fibicomp.view.MobileProposalBudget;
import com.polus.fibicomp.view.MobileProposalSummary;
import com.polus.fibicomp.view.MobileProposalView;
import com.polus.fibicomp.view.Question;
import com.polus.fibicomp.vo.CommonVO;

@Transactional
@Service(value = "dashboardService")
public class DashboardServiceImpl implements DashboardService {

	protected static Logger logger = Logger.getLogger(DashboardServiceImpl.class.getName());

	@Autowired
	private DashboardDao dashboardDao;

	@Override
	public String getDashBoardResearchSummary(String personId) throws Exception {
		return dashboardDao.getDashBoardResearchSummary(personId);
	}

	@Override
	public String getDashBoardData(CommonVO vo) throws Exception {
		DashBoardProfile dashBoardProfile = new DashBoardProfile();
		String requestType = vo.getTabIndex();
		try {
			if (requestType.equals("AWARD")) {
				dashBoardProfile = dashboardDao.getDashBoardDataForAward(vo);
			}
			if (requestType.equals("PROPOSAL")) {
				dashBoardProfile = dashboardDao.getDashBoardDataForProposal(vo);
			}
			if (requestType.equals("IRB")) {
				dashBoardProfile = dashboardDao.getProtocolDashboardData(vo);
			}
			if (requestType.equals("IACUC")) {
				dashBoardProfile = dashboardDao.getDashBoardDataForIacuc(vo);
			}
			if (requestType.equals("DISCLOSURE")) {
				dashBoardProfile = dashboardDao.getDashBoardDataForDisclosures(vo);
			}
			// dashBoardProfile.setPersonDTO(personDTO);
		} catch (Exception e) {
			logger.error("Error in methord getDashBoardData", e);
		}
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(dashBoardProfile);
	}

	@Override
	public List<ActionItem> getUserNotification(String personId) {
		return dashboardDao.getUserNotification(personId);
	}

	@Override
	public String getPieChartDataByType(CommonVO vo) throws Exception {
		String pieChartDataByType = null;
		String personId = vo.getPersonId();
		String sponsorCode = vo.getSponsorCode();
		String pieChartIndex = vo.getPieChartIndex();
		try {
			if (pieChartIndex.equals("AWARD")) {
				pieChartDataByType = dashboardDao.getAwardBySponsorTypes(personId, sponsorCode);
			}
			if (pieChartIndex.equals("PROPOSAL")) {
				pieChartDataByType = dashboardDao.getProposalBySponsorTypes(personId, sponsorCode);
			}
		} catch (Exception e) {
			logger.error("Error in method getPieChartDataByType", e);
		}
		return pieChartDataByType;
	}

	@Override
	public String getDetailedSummaryData(String personId, String researchSummaryIndex) throws Exception {
		DashBoardProfile dashBoardProfile = new DashBoardProfile();
		try {
			if (researchSummaryIndex.equals("PROPOSALSINPROGRESS")) {
				dashBoardProfile = dashboardDao.getProposalsInProgress(personId);
			}
			if (researchSummaryIndex.equals("PROPOSALSSUBMITTED")) {
				dashBoardProfile = dashboardDao.getSubmittedProposals(personId);
			}
			if (researchSummaryIndex.equals("AWARDSACTIVE")) {
				dashBoardProfile = dashboardDao.getActiveAwards(personId);
			}
		} catch (Exception e) {
			logger.error("Error in method getDetailedSummaryData", e);
		}
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(dashBoardProfile);
	}


	@Override
	public String getDonutChartDataBySponsor(CommonVO vo) {
		String donutChartData = null;
		String personId = vo.getPersonId();
		String sponsorCode = vo.getSponsorCode();
		String donutChartIndex = vo.getDonutChartIndex();
		try {
			if (donutChartIndex.equals("INPROGRESS")) {
				donutChartData = dashboardDao.getInProgressProposalsBySponsorExpanded(personId, sponsorCode);
			}
			if (donutChartIndex.equals("AWARDED")) {
				donutChartData = dashboardDao.getAwardedProposalsBySponsorExpanded(personId, sponsorCode);
			}
		} catch (Exception e) {
			logger.error("Error in method getPieChartDataByType", e);
		}
		return donutChartData;
	}

	@Override
	public String getFibiResearchSummary(String personId) throws Exception {
		MobileProfile mobileProfile = new MobileProfile();
		mobileProfile.setStatus(false);
		mobileProfile.setMessage("Failed");
		List<Object[]> summaryTable = new ArrayList<Object[]>();
		List<Object[]> summaryResponse = new ArrayList<Object[]>();

		try {
			summaryTable = dashboardDao.getFibiSummaryTable(personId, summaryTable);
			Integer documentCount = 0;
			Integer iterator = 0;
			for (Object[] view : summaryTable) {
				documentCount = ((BigDecimal) view[1]).intValueExact();
				if (view[2] == null) {
					view[2] = 0;
				}
				summaryResponse.add(view);
				if (documentCount == 0 && view[2] == null) {
					iterator++;
				}
			}
			if (iterator < 3) {
				mobileProfile.setData(summaryResponse);
				mobileProfile.setStatus(true);
				mobileProfile.setMessage("Data found");
				logger.info("summaryResponse : " + summaryResponse);
			}
		} catch (Exception e) {
			logger.error("Error in method DashboardServiceImpl.getFibiResearchSummary", e);
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(mobileProfile);
	}

	@Override
	public String getProposalsBySearchCriteria(CommonVO vo) throws Exception {
		MobileProfile mobileProfile = new MobileProfile();
		String requestType = vo.getTabIndex();
		mobileProfile.setStatus(false);
		mobileProfile.setMessage("No Search Results Found");
		try {
			if (requestType.equals("AWARD")) {
			}
			if (requestType.equals("PROPOSAL")) {
				List<MobileProposalView> proposalViews = dashboardDao.getProposalsByParams(vo);
				if (proposalViews != null && !proposalViews.isEmpty()) {
					mobileProfile.setData(proposalViews);
					mobileProfile.setStatus(true);
					mobileProfile.setMessage("Data found for the given search key");
					logger.info("searched proposal datas : " + proposalViews);
				}
			}
			if (requestType.equals("IRB")) {
			}
			if (requestType.equals("IACUC")) {
			}
		} catch (Exception e) {
			logger.error("Error in methord DashboardServiceImpl.getFibiSearch", e);
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(mobileProfile);
	}

	@Override
	public String getFibiActionList(CommonVO vo) throws Exception {
		//String personId = vo.getPersonId();
		MobileProfile mobileProfile = new MobileProfile();
		mobileProfile.setStatus(false);
		mobileProfile.setMessage("No action items found");
		try {
			List<FibiActionList> fibiActionLists = new ArrayList<FibiActionList>();
			// dashboardDao.getFibiActionList(personId)
			//if(fibiActionList.size() > 0) {
			//static data starts
			if(vo.getActionType().equalsIgnoreCase("C")) {
				for(int i = 0; i<4 ; ++i) {
					FibiActionList fibiActionList = new FibiActionList();
					fibiActionList.setActionId("465445");
					fibiActionList.setProposalNo("14");
					fibiActionList.setTitle("Proposal/Award Title1");
					fibiActionList.setDocumentId("3016");
					fibiActionList.setStatus("");
					fibiActionList.setPi_Id("admin");
					fibiActionList.setPiFullName("admin admin");
					fibiActionList.setLeadUnit("University");
					fibiActionList.setSponsor("UAI");
					fibiActionList.setUserType("PI");
					fibiActionList.setUserName("admin");
					fibiActionList.setActionKey("CER");
					fibiActionLists.add(fibiActionList);
				}
			} else
				if(vo.getActionType().equalsIgnoreCase("A")) {
					for(int i = 0; i<7 ; ++i) {
						FibiActionList fibiActionList = new FibiActionList();
						fibiActionList.setActionId("465445");
						fibiActionList.setProposalNo("14");
						fibiActionList.setTitle("Proposal/Award Title1");
						fibiActionList.setDocumentId("3016");
						fibiActionList.setStatus("");
						fibiActionList.setPi_Id("admin");
						fibiActionList.setPiFullName("admin admin");
						fibiActionList.setLeadUnit("University");
						fibiActionList.setSponsor("UAI");
						fibiActionList.setUserType("PI");
						fibiActionList.setUserName("admin");
						fibiActionList.setActionKey("APR");
						fibiActionLists.add(fibiActionList);
					}
				} else
					if(vo.getActionType().equalsIgnoreCase("F")) {
						for(int i = 0; i<5 ; ++i) {
							FibiActionList fibiActionList = new FibiActionList();
							fibiActionList.setActionId("465445");
							fibiActionList.setProposalNo("14");
							fibiActionList.setTitle("Proposal/Award Title1");
							fibiActionList.setDocumentId("3016");
							fibiActionList.setStatus("");
							fibiActionList.setPi_Id("admin");
							fibiActionList.setPiFullName("admin admin");
							fibiActionList.setLeadUnit("University");
							fibiActionList.setSponsor("UAI");
							fibiActionList.setUserType("PI");
							fibiActionList.setUserName("admin");
							fibiActionList.setActionKey("FYI");
							fibiActionLists.add(fibiActionList);
						}
					}
			// static data ends
				mobileProfile.setData(fibiActionLists);
				mobileProfile.setMessage("List of actions found");
				mobileProfile.setStatus(true);
				logger.info("fibiActionLists : " + fibiActionLists);
			
		} catch(Exception e) {
			logger.error("Error in method DashboardServiceImpl.getFibiActionList.", e);
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(mobileProfile);
	}

	@Override
	public String getFibiProposalDetails(String proposalNumber) throws Exception {
		MobileProfile mobileProfile = new MobileProfile();
		Map<String, Object> proposalDataMap = new HashMap<String, Object>();
		Map<String, Object> personnel = new HashMap<String, Object>();
		MobileProposalSummary summary = new MobileProposalSummary();
		MobilePersonnelDetails personnelDetails = new MobilePersonnelDetails();
		List<MobilePersonnelDetails> coi = new ArrayList<MobilePersonnelDetails>();
		MobileProposalBudget budget = new MobileProposalBudget();
		mobileProfile.setStatus(false);
		mobileProfile.setMessage("Error in fetching proposal data");
		try {
			// static data starts
			summary.setProposalNo("11");
			summary.setDocumentNo("3176");
			summary.setVersionNo("0");
			summary.setProposalType("New");
			summary.setTitle("Sample Clinical Trial");
			summary.setLeadUnitNumber("00001");
			summary.setLeadUnit("University");
			summary.setSponsorCode("101");
			summary.setSponsor("UIA");
			summary.setPrimeSponsor("UIA-Prime");
			summary.setStatus("Pending Aproval");
			summary.setActivityType("No Activity");
			summary.setActionKey("CER");
			summary.setProjectStartDate("10/10/2017");
			summary.setProjectEndDate("10/01/2021");
			
			personnelDetails.setPiFullName("admin admin");
			personnelDetails.setEmail("hello@yahoo.com");
			personnelDetails.setCertificationStatus("Certification Completed");
			personnelDetails.setLeadUnit("University");

			MobilePersonnelDetails details = new MobilePersonnelDetails();
			details.setPiFullName("Michal Joseph");
			details.setEmail("mich@sdcu.edu.in");
			details.setCertificationStatus("Certification Completed");
			details.setLeadUnit("School of Physics");
			coi.add(details);
			MobilePersonnelDetails detail = new MobilePersonnelDetails();
			detail.setPiFullName("Sam Troger");
			detail.setEmail("smat@sdcu.edu.in");
			detail.setCertificationStatus("Certification Completed");
			detail.setLeadUnit("School of Physics");
			coi.add(detail);
			MobilePersonnelDetails detail2 = new MobilePersonnelDetails();
			detail2.setPiFullName("Roger Federer");
			detail2.setEmail("rogrf@sdcu.edu.in");
			detail2.setCertificationStatus("Certification Pending");
			detail2.setLeadUnit("School of Tennis");
			coi.add(detail2);
			
			budget.setTotalCost("58725");
			budget.setCostSharing("9568");
			budget.setDirectCost("25245");
			budget.setIndirectCost("21224");
			budget.setUnderRecovery("12256");
			//static data ends
			personnel.put("pi", personnelDetails);
			personnel.put("coi", coi);
			proposalDataMap.put("summary", summary);
			proposalDataMap.put("personnel", personnel);
			proposalDataMap.put("budget", budget);
			mobileProfile.setStatus(true);
			mobileProfile.setMessage("Details of Proposal Data");
			mobileProfile.setData(proposalDataMap);
		} catch (Exception e) {
			logger.error("Error in method DashboardServiceImpl.getFibiProposalDetails.", e);
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(mobileProfile);
	}

	@Override
	public String manageFibiProposal(String documentNo) throws Exception {
		MobileProfile mobileProfile = new MobileProfile();
		mobileProfile.setStatus(false);
		mobileProfile.setMessage("Error performing handling proposal");
		try {
			mobileProfile.setStatus(true);
			mobileProfile.setMessage("Action Performed successfully");
			mobileProfile.setData("ACCEPTED/REJECTED");
		} catch (Exception e) {
			logger.error("Error in method DashboardServiceImpl.manageFibiProposal.", e);
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(mobileProfile);
	}

	@Override
	public String getFibiQuestionnaire(CommonVO vo) throws Exception {
		MobileProfile mobileProfile = new MobileProfile();
		List<Question> questions = new ArrayList<Question>();
		// String questionnaireId = "";
		mobileProfile.setStatus(false);
		mobileProfile.setMessage("Error fetching questions");
		try {
			// questions = dashboardDao.fetchQuestions(questionnaireId);
				Question question1 = new Question();
				Question question2 = new Question();
				Question question3 = new Question();
				Question question4 = new Question();
				Question question5 = new Question();
				Question question6 = new Question();
				Question question7 = new Question();
				Question question8 = new Question();
				Question question9 = new Question();
				Question question10 = new Question();
				question1.setQuestionId("12345");
				question1.setQuestionnaireID("5678");
				question1.setQuestionDesc("What is your Name.?");
				question1.setParentQuestionId("#1596");
				question1.setAnswerType("YesNo");
				questions.add(question1);
				
				question2.setQuestionId("54123");
				question2.setQuestionnaireID("5678");
				question2.setQuestionDesc("Have you already submitted any proposals before.?");
				question2.setParentQuestionId("#1596");
				question2.setAnswerType("YesNo");
				questions.add(question2);
				
				question3.setQuestionId("12321");
				question3.setQuestionnaireID("5678");
				question3.setQuestionDesc("How was your previous research experience.?");
				question3.setParentQuestionId("#1596");
				question3.setAnswerType("Text");
				questions.add(question3);
				
				question4.setQuestionId("12359");
				question4.setQuestionnaireID("5678");
				question4.setQuestionDesc("How do you know the proposal announcement.?");
				question4.setParentQuestionId("#1596");
				question4.setAnswerType("Text");
				questions.add(question4);
				
				question5.setQuestionId("21586");
				question5.setQuestionnaireID("5678");
				question5.setQuestionDesc("How do you know the awarded sponsor.?");
				question5.setParentQuestionId("#1596");
				question5.setAnswerType("Text");
				questions.add(question5);
				
				question6.setQuestionId("21547");
				question6.setQuestionnaireID("5678");
				question6.setQuestionDesc("How do you know the proposal announcement.?");
				question6.setParentQuestionId("#1596");
				question6.setAnswerType("Text");
				questions.add(question6);
				
				question7.setQuestionId("42536");
				question7.setQuestionnaireID("5678");
				question7.setQuestionDesc("Are you willing to do the regular research.?");
				question7.setParentQuestionId("#1596");
				question7.setAnswerType("YesNo");
				questions.add(question7);
				
				question8.setQuestionId("32514");
				question8.setQuestionnaireID("5678");
				question8.setQuestionDesc("Are aware of the IRB and IACUC.?");
				question8.setParentQuestionId("#1596");
				question8.setAnswerType("YesNo");
				questions.add(question8);
				
				question9.setQuestionId("78451");
				question9.setQuestionnaireID("5678");
				question9.setQuestionDesc("Are you concerned with the ongoing delay in OSP tracking.?");
				question9.setParentQuestionId("#1596");
				question9.setAnswerType("YesNo");
				questions.add(question9);
				
				question10.setQuestionId("75169");
				question10.setQuestionnaireID("5678");
				question10.setQuestionDesc("What could we do to make your research experience better, from your previous experience.?");
				question10.setParentQuestionId("#1596");
				question10.setAnswerType("Text");
				questions.add(question10);
				
			mobileProfile.setStatus(true);
			mobileProfile.setMessage("Questions fetched successfully");
			mobileProfile.setData(questions);
		} catch (Exception e) {
			logger.error("Error in method DashboardServiceImpl.getFibiQuestionnaire.", e);
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(mobileProfile);
	}

	@Override
	public String submitQuestionnaire(CommonVO vo) throws Exception {
		MobileProfile mobileProfile = new MobileProfile();
		String questionnaireId = vo.getQuestionnaireId();
		List<HashMap<String, String>> answerList = vo.getAnswerList();
		logger.info("Id is : " + questionnaireId);
		logger.info("List is : " + answerList);
		mobileProfile.setStatus(false);
		mobileProfile.setMessage("Error submitting questionnaire");
		try {
			mobileProfile.setStatus(true);
			mobileProfile.setMessage("Submitted Successfully");
			mobileProfile.setData("SUCCESS");
		} catch (Exception e) {
			logger.error("Error in method DashboardServiceImpl.submitQuestionnaire.", e);
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(mobileProfile);
	}
	
	@Override
	public String getFibiResearchSummary(String personId, String researchSummaryIndex) throws Exception {
		DashBoardProfile dashBoardProfile = new DashBoardProfile();
		MobileProfile mobileProfile = new MobileProfile();
		mobileProfile.setStatus(false);
		mobileProfile.setMessage("Error fetching research summary");
		try {
			if (researchSummaryIndex.equals("PROPOSALSINPROGRESS")) {
				dashBoardProfile = dashboardDao.getProposalsInProgress(personId);
				mobileProfile.setStatus(true);
				mobileProfile.setMessage("Research summary details fetched successfully");
			}
			if (researchSummaryIndex.equals("PROPOSALSSUBMITTED")) {
				dashBoardProfile = dashboardDao.getSubmittedProposals(personId);
				mobileProfile.setStatus(true);
				mobileProfile.setMessage("Research summary details fetched successfully");
			}
			mobileProfile.setData(dashBoardProfile);
		} catch (Exception e) {
			logger.error("Error in method getFibiDetailedResearchSummary", e);
		}
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(mobileProfile);
	}
}

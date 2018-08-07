package com.polus.fibicomp.budget.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.polus.fibicomp.budget.common.pojo.InstituteRate;
import com.polus.fibicomp.budget.common.pojo.RateType;
import com.polus.fibicomp.budget.common.pojo.ValidCeRateType;
import com.polus.fibicomp.budget.dao.BudgetDao;
import com.polus.fibicomp.budget.pojo.BudgetDetail;
import com.polus.fibicomp.budget.pojo.BudgetDetailCalcAmount;
import com.polus.fibicomp.budget.pojo.BudgetHeader;
import com.polus.fibicomp.budget.pojo.BudgetPeriod;
import com.polus.fibicomp.budget.pojo.CostElement;
import com.polus.fibicomp.budget.pojo.FibiProposalRate;
import com.polus.fibicomp.committee.dao.CommitteeDao;
import com.polus.fibicomp.common.dao.CommonDao;
import com.polus.fibicomp.common.service.DateTimeService;
import com.polus.fibicomp.constants.Constants;
import com.polus.fibicomp.proposal.dao.ProposalDao;
import com.polus.fibicomp.proposal.pojo.Proposal;
import com.polus.fibicomp.proposal.vo.ProposalVO;

@Transactional
@Service(value = "budgetService")
public class BudgetServiceImpl implements BudgetService {

	protected static Logger logger = Logger.getLogger(BudgetServiceImpl.class.getName());

	@Autowired
	private BudgetDao budgetDao;

	@Autowired
	private CommitteeDao committeeDao;

	@Autowired
	@Qualifier(value = "proposalDao")
	private ProposalDao proposalDao;

	@Autowired
	public CommonDao commonDao;

	@Autowired
    @Qualifier("budgetCalculationService")
    private BudgetCalculationService budgetCalculationService;

	@Autowired
    @Qualifier("dateTimeService")
    private DateTimeService dateTimeService;

	@Override
	public String createProposalBudget(ProposalVO vo) {
		Proposal proposal = vo.getProposal();
		BudgetHeader budget = new BudgetHeader();
		budget.setStartDate(proposal.getStartDate());
		budget.setEndDate(proposal.getEndDate());
		budget.setCreateTimeStamp(committeeDao.getCurrentTimestamp());
		budget.setCreateUser(vo.getUserName());
		budget.setCreateUserName(vo.getUserFullName());
		budget.setUpdateTimeStamp(committeeDao.getCurrentTimestamp());
		budget.setUpdateUser(vo.getUserName());
		budget.setUpdateUserName(vo.getUserFullName());
		String rateClassCode = commonDao.getParameterValueAsString(Constants.KC_B_PARAMETER_NAMESPACE, Constants.KC_DOC_PARAMETER_DETAIL_TYPE_CODE, Constants.DEFAULT_RATE_CLASS_CODE);
		String rateTypeCode = commonDao.getParameterValueAsString(Constants.KC_B_PARAMETER_NAMESPACE, Constants.KC_DOC_PARAMETER_DETAIL_TYPE_CODE, Constants.DEFAULT_RATE_TYPE_CODE);
		RateType rateType = budgetDao.getOHRateTypeByParams(rateClassCode, rateTypeCode);
		budget.setRateType(rateType);
		budget.setRateClassCode(rateType.getRateClassCode());
		budget.setRateTypeCode(rateType.getRateTypeCode());
		if (budget.getStartDate() != null) {
            budget.setBudgetPeriods(generateBudgetPeriods(budget));
        }
		budget.setIsAutoCalc(false);
		proposal.setBudgetHeader(budget);
		List<FibiProposalRate> fibiProposalRates = budget.getProposalRates();
		if (fibiProposalRates == null || fibiProposalRates.isEmpty()) {
			Set<String> rateClassTypes = new HashSet<>();
			fibiProposalRates = fetchFilteredProposalRates(proposal, rateClassTypes);
			budget.setProposalRates(fibiProposalRates);
			vo.setRateClassTypes(rateClassTypes);
		}
		proposal = proposalDao.saveOrUpdateProposal(proposal);
		loadBudgetInitialData(vo);
		vo.setProposal(proposal);
		vo.setSysGeneratedCostElements(fetchSysGeneratedCostElements(proposal.getActivityTypeCode()));
		return committeeDao.convertObjectToJSON(vo);
	}

	private void loadBudgetInitialData(ProposalVO vo) {
		List<CostElement> costElements = budgetDao.getAllCostElements();
		vo.setCostElements(costElements);
	}

	@Override
	public Proposal saveOrUpdateProposalBudget(ProposalVO vo) {
		Proposal proposal = vo.getProposal();
		proposalDao.saveOrUpdateProposal(proposal);
		if (proposal.getBudgetHeader() != null && proposal.getBudgetHeader().getIsAutoCalc() != null && proposal.getBudgetHeader().getIsAutoCalc()) {
			proposal = calculateBudget(proposal);
		}
		return proposal;
	}

	public Proposal calculateBudget(Proposal proposal) {
		calculate(proposal, null);
		return proposal;
	}

	private void calculate(Proposal proposal, Integer period) {
		List<BudgetPeriod> budgetPeriodsList = proposal.getBudgetHeader().getBudgetPeriods();
		for (BudgetPeriod budgetPeriod : budgetPeriodsList) {
			BigDecimal totalFringeCost = BigDecimal.ZERO;
			BigDecimal totalFandACost = BigDecimal.ZERO;
			BigDecimal totalLineItemCost = BigDecimal.ZERO;
			List<BudgetDetail> budgetDetailsList = budgetPeriod.getBudgetDetails();
			if (budgetDetailsList != null && !budgetDetailsList.isEmpty()) {
				for (BudgetDetail budgetItemDetail : budgetDetailsList) {
					if (!budgetItemDetail.getIsSystemGeneratedCostElement()) {
						BigDecimal fringeCostForCE = BigDecimal.ZERO;
						BigDecimal fandACostForCE = BigDecimal.ZERO;
						BigDecimal lineItemCost = budgetItemDetail.getLineItemCost();
						totalLineItemCost = totalLineItemCost.add(lineItemCost);
						fringeCostForCE = calculateFringCostForCE(proposal.getBudgetHeader().getBudgetId(), budgetPeriod, budgetItemDetail, lineItemCost, proposal.getActivityTypeCode());
						fandACostForCE = calculateFandACostForCE(proposal.getBudgetHeader().getBudgetId(), budgetPeriod, budgetItemDetail, fringeCostForCE.add(lineItemCost), proposal.getActivityTypeCode());
						totalFringeCost = totalFringeCost.add(fringeCostForCE);
						totalFandACost = totalFandACost.add(fandACostForCE);
					}
				}
				for (BudgetDetail budgetItemDetail : budgetDetailsList) {
					if (budgetItemDetail.getIsSystemGeneratedCostElement()) {
						if (Constants.BUDGET_FRINGE_ON.equals(budgetItemDetail.getSystemGeneratedCEType())
								|| Constants.BUDGET_FRINGE_OFF.equals(budgetItemDetail.getSystemGeneratedCEType())) {
							budgetItemDetail.setLineItemCost(totalFringeCost);

						} else if (Constants.BUDGET_OH_ON.equals(budgetItemDetail.getSystemGeneratedCEType())
								|| Constants.BUDGET_OH_OFF.equals(budgetItemDetail.getSystemGeneratedCEType())) {
							budgetItemDetail.setLineItemCost(totalFandACost);
						}
					}
				}
				budgetPeriod.setTotalDirectCost(totalLineItemCost.add(totalFringeCost));
				budgetPeriod.setTotalIndirectCost(totalFandACost);
				budgetPeriod.setTotalCost(totalLineItemCost.add(totalFringeCost).add(totalFandACost));
			}
		}
		updateBudgetHeader(proposal.getBudgetHeader());
	}

	private void updateBudgetHeader(BudgetHeader budget) {
		List<BudgetPeriod> budgetPeriodList = budget.getBudgetPeriods();
		BigDecimal totalDirectCost = BigDecimal.ZERO;
		BigDecimal totalIndirectCost = BigDecimal.ZERO;
		BigDecimal totalCost = BigDecimal.ZERO;
		if (budgetPeriodList != null && !budgetPeriodList.isEmpty()) {
			for (BudgetPeriod period : budgetPeriodList) {
				if (period.getTotalDirectCost() != null) {
					totalDirectCost = totalDirectCost.add(period.getTotalDirectCost());
				}
				if (period.getTotalIndirectCost() != null) {
					totalIndirectCost = totalIndirectCost.add(period.getTotalIndirectCost());
				}
				if (period.getTotalCost() != null) {
					totalCost = totalCost.add(period.getTotalCost());
				}
			}
			budget.setTotalDirectCost(totalDirectCost);
			budget.setTotalIndirectCost(totalIndirectCost);
			budget.setTotalCost(totalCost);
		}
	}

	private BigDecimal calculateFringCostForCE(Integer budgetId, BudgetPeriod budgetPeriod, BudgetDetail budgetDetail,
			BigDecimal lineItemCost, String activityTypeCode) {
		BigDecimal fringeCost = BigDecimal.ZERO;
		Date budgetPeriodStartDate = budgetPeriod.getStartDate();
		Date budgetPeriodEndDate = budgetPeriod.getEndDate();
		CostElement costElement = budgetDetail.getCostElement();
		// Rounding mode is used to remove an exception thrown in BigDecimal division to get rounding up to 2 precision
		BigDecimal perDayCost = lineItemCost.divide(new BigDecimal(((budgetPeriodEndDate.getTime() - budgetPeriodStartDate.getTime()) / 86400000 + 1)), 2, RoundingMode.HALF_UP);
		BigDecimal validRate = BigDecimal.ZERO;
		BudgetDetailCalcAmount budgetCalculatedAmount = null;
		List<ValidCeRateType> ceRateTypes = costElement.getValidCeRateTypes();
		if (ceRateTypes != null && !ceRateTypes.isEmpty()) {
			for (ValidCeRateType ceRateType : ceRateTypes) {
				FibiProposalRate applicableRate = budgetDao.fetchApplicableProposalRate(budgetId, budgetPeriodStartDate,
						ceRateType.getRateClassCode(), ceRateType.getRateTypeCode(), activityTypeCode);
				if (applicableRate != null
						&& ((applicableRate.getRateClass().getRateClassTypeCode().equals("E") && "5".equals(applicableRate.getRateClassCode())
								&& !"3".equals(applicableRate.getRateTypeCode()))
						|| (applicableRate.getRateClass().getRateClassTypeCode().equals("V") && "8".equals(applicableRate.getRateClassCode())
								&& !"2".equals(applicableRate.getRateTypeCode())))) {
					validRate = validRate.add(applicableRate.getApplicableRate());
					budgetCalculatedAmount = getNewBudgetCalculatedAmount(budgetPeriod, budgetDetail, applicableRate);
				}
			}
		}
		if (validRate.compareTo(BigDecimal.ZERO) > 0) {
			int numberOfDays = (int) ((budgetPeriodEndDate.getTime() - budgetPeriodStartDate.getTime()) / 86400000);
			BigDecimal hundred = new BigDecimal(100);
			BigDecimal percentageFactor = validRate.divide(hundred, 2, BigDecimal.ROUND_HALF_UP);
			fringeCost = fringeCost.add((perDayCost.multiply(percentageFactor)).multiply(new BigDecimal(numberOfDays)));
			budgetCalculatedAmount.setCalculatedCost(fringeCost);
			//budgetDetail.getBudgetDetailCalcAmounts().add(budgetCalculatedAmount);
		}
		return fringeCost;
	}

	private BigDecimal calculateFandACostForCE(Integer budgetId, BudgetPeriod budgetPeriod, BudgetDetail budgetDetail,
			BigDecimal fringeWithLineItemCost, String activityTypeCode) {
		BigDecimal fandACost = BigDecimal.ZERO;
		Date budgetPeriodStartDate = budgetPeriod.getStartDate();
		CostElement costElement = budgetDetail.getCostElement();
		// Rounding mode is used to remove an exception thrown in BigDecimal division to get rounding up to 2 precision);
		BigDecimal validRate = BigDecimal.ZERO;
		BudgetDetailCalcAmount budgetCalculatedAmount = null;
		String ohRateClassTypeCode = commonDao.getParameterValueAsString(Constants.KC_B_PARAMETER_NAMESPACE,
				Constants.KC_DOC_PARAMETER_DETAIL_TYPE_CODE, Constants.DEFAULT_RATE_CLASS_TYPE_CODE);
		String rateTypeCode = commonDao.getParameterValueAsString(Constants.KC_B_PARAMETER_NAMESPACE,
				Constants.KC_DOC_PARAMETER_DETAIL_TYPE_CODE, Constants.DEFAULT_RATE_TYPE_CODE);
		List<ValidCeRateType> ceRateTypes = costElement.getValidCeRateTypes();
		if (ceRateTypes != null && !ceRateTypes.isEmpty()) {
			for (ValidCeRateType ceRateType : ceRateTypes) {
				FibiProposalRate applicableRate = budgetDao.fetchApplicableProposalRate(budgetId, budgetPeriodStartDate,
						ceRateType.getRateClassCode(), ceRateType.getRateTypeCode(), activityTypeCode);
				if (applicableRate != null
						&& applicableRate.getRateClass().getRateClassTypeCode().equals(ohRateClassTypeCode)
						&& applicableRate.getRateTypeCode().equals(rateTypeCode)) {
					validRate = validRate.add(applicableRate.getApplicableRate());
					budgetCalculatedAmount = getNewBudgetCalculatedAmount(budgetPeriod, budgetDetail, applicableRate);
				}
			}
		}
		if (validRate.compareTo(BigDecimal.ZERO) > 0) {
			BigDecimal hundred = new BigDecimal(100);
			BigDecimal percentageFactor = validRate.divide(hundred, 2, BigDecimal.ROUND_HALF_UP);
			fandACost = fandACost.add((fringeWithLineItemCost.multiply(percentageFactor)));
			budgetCalculatedAmount.setCalculatedCost(fandACost);
			//budgetDetail.getBudgetDetailCalcAmounts().add(budgetCalculatedAmount);
		}
		return fandACost;
	}

	protected BudgetDetailCalcAmount getNewBudgetCalculatedAmount(BudgetPeriod budgetPeriod, BudgetDetail budgetDetail,
			FibiProposalRate proposalRate) {
		BudgetDetailCalcAmount budgetCalculatedAmount = new BudgetDetailCalcAmount();
		budgetCalculatedAmount.setBudgetId(budgetPeriod.getBudget().getBudgetId());
		budgetCalculatedAmount.setBudgetPeriod(budgetDetail.getBudgetPeriod());
		budgetCalculatedAmount.setBudgetPeriodId(budgetPeriod.getBudgetPeriodId());
		budgetCalculatedAmount.setLineItemNumber(budgetDetail.getLineItemNumber());
		budgetCalculatedAmount.setRateClassCode(proposalRate.getRateClassCode());
		budgetCalculatedAmount.setRateClass(proposalRate.getRateClass());
		budgetCalculatedAmount.setRateTypeCode(proposalRate.getRateTypeCode());
		budgetCalculatedAmount.setRateType(proposalRate.getRateType());
		budgetCalculatedAmount.setApplyRateFlag(true);
		budgetCalculatedAmount.setRateTypeDescription(proposalRate.getRateClass().getDescription());
		// budgetCalculatedAmount.setCalculatedCost(calculatedCost);
		// budgetCalculatedAmount.setCalculatedCostSharing(calculatedCostSharing);
		return budgetCalculatedAmount;
	}

	@Override
	public String autoCalculate(ProposalVO proposalVO) {
		Proposal proposal = proposalVO.getProposal();
		if (proposal.getBudgetHeader().getIsAutoCalc() != null && proposal.getBudgetHeader().getIsAutoCalc()) {
			proposalDao.saveOrUpdateProposal(proposal);
			calculate(proposal, null);
		}
		proposal = proposalDao.saveOrUpdateProposal(proposal);
		return committeeDao.convertObjectToJSON(proposalVO);
	}

	@Override
	public List<BudgetPeriod> generateBudgetPeriods(BudgetHeader budget) {
		List<BudgetPeriod> budgetPeriods = new ArrayList<BudgetPeriod>();
		Date projectStartDate = budget.getStartDate();
		Date projectEndDate = budget.getEndDate();
		boolean budgetPeriodExists = true;

		Calendar cl = Calendar.getInstance();

		Date periodStartDate = projectStartDate;
		int budgetPeriodNum = 1;
		while (budgetPeriodExists) {
			cl.setTime(periodStartDate);
			cl.add(Calendar.YEAR, 1);
			Date nextPeriodStartDate = new Date(cl.getTime().getTime());
			cl.add(Calendar.DATE, -1);
			Date periodEndDate = new Date(cl.getTime().getTime());
			/* check period end date gt project end date */
			switch (periodEndDate.compareTo(projectEndDate)) {
			case 1:
				periodEndDate = projectEndDate;
				// the break statement is purposefully missing.
			case 0:
				budgetPeriodExists = false;
				break;
			}
			BudgetPeriod budgetPeriod = new BudgetPeriod();
			budgetPeriod.setBudgetPeriod(budgetPeriodNum);
			budgetPeriod.setStartDate(periodStartDate);
			budgetPeriod.setEndDate(periodEndDate);
			budgetPeriod.setBudget(budget);

			budgetPeriods.add(budgetPeriod);
			periodStartDate = nextPeriodStartDate;
			budgetPeriodNum++;
		}
		return budgetPeriods;
	}

	@Override
	public String addBudgetPeriod(ProposalVO proposalVO) {
		Proposal proposal = proposalVO.getProposal();
		BudgetHeader budget = proposal.getBudgetHeader();
		List<BudgetPeriod> budgetPeriods = budget.getBudgetPeriods();
		BudgetPeriod lastPeriod = budgetDao.getMaxBudgetPeriodByBudgetId(budget.getBudgetId());

		BudgetPeriod newBudgetPeriod = new BudgetPeriod();
		newBudgetPeriod.setBudget(budget);
		newBudgetPeriod.setBudgetPeriod(lastPeriod.getBudgetPeriod() + 1);
		newBudgetPeriod.setEndDate(lastPeriod.getEndDate());
		newBudgetPeriod.setStartDate(lastPeriod.getStartDate());
		newBudgetPeriod.setTotalCost(lastPeriod.getTotalCost());
		newBudgetPeriod.setTotalDirectCost(lastPeriod.getTotalDirectCost());
		newBudgetPeriod.setTotalIndirectCost(lastPeriod.getTotalIndirectCost());
		newBudgetPeriod.setUpdateTimeStamp(committeeDao.getCurrentTimestamp());
		newBudgetPeriod.setUpdateUser(proposal.getUpdateUser());

		List<BudgetDetail> budgetDetails = lastPeriod.getBudgetDetails();
		if (budgetDetails != null && !budgetDetails.isEmpty()) {
			List<BudgetDetail> newLineItems = lastPeriod.getBudgetDetails();
			for (BudgetDetail budgetDetail : budgetDetails) {
				BudgetDetail detail = new BudgetDetail();
				detail.setBudgetCategory(budgetDetail.getBudgetCategory());
				detail.setBudgetCategoryCode(budgetDetail.getBudgetCategoryCode());
				detail.setBudgetJustification(budgetDetail.getBudgetJustification());
				detail.setBudgetPeriod(budgetDetail.getBudgetPeriod());
				detail.setCostElement(budgetDetail.getCostElement());
				detail.setCostElementCode(budgetDetail.getCostElementCode());
				detail.setEndDate(budgetDetail.getEndDate());
				detail.setIsSystemGeneratedCostElement(budgetDetail.getIsSystemGeneratedCostElement());
				detail.setSystemGeneratedCEType(budgetDetail.getSystemGeneratedCEType());
				detail.setLineItemCost(budgetDetail.getLineItemCost());
				detail.setLineItemDescription(budgetDetail.getLineItemDescription());
				detail.setLineItemNumber(budgetDetail.getLineItemNumber());
				detail.setOnOffCampusFlag(budgetDetail.getOnOffCampusFlag());
				detail.setPeriod(lastPeriod);
				detail.setPrevLineItemCost(budgetDetail.getPrevLineItemCost());
				detail.setStartDate(budgetDetail.getStartDate());
				detail.setUpdateTimeStamp(committeeDao.getCurrentTimestamp());
				detail.setUpdateUser(newBudgetPeriod.getUpdateUser());
				newLineItems.add(detail);
			}
			newBudgetPeriod.setBudgetDetails(newLineItems);
		}
        budgetPeriods.add(newBudgetPeriod);
        proposal = proposalDao.saveOrUpdateProposal(proposal);
        proposalVO.setProposal(proposal);
        return committeeDao.convertObjectToJSON(proposalVO);
	}

	@Override
	public boolean budgetLineItemExists(BudgetHeader budget, Integer budgetPeriod) {
		boolean lineItemExists = false;

		//List<BudgetDetail> budgetLineItems = budget.getBudgetPeriod(budgetPeriod).getBudgetDetails();
		List<BudgetDetail> budgetLineItems = budget.getBudgetPeriods().get(budgetPeriod).getBudgetDetails();
		/* check budget line item */
		for (BudgetDetail periodLineItem : budgetLineItems) {
			Integer lineItemPeriod = periodLineItem.getBudgetPeriod();
			if (budgetPeriod + 1 == lineItemPeriod) {
				lineItemExists = true;
				break;
			}
		}
		return lineItemExists;
	}

	@Override
	public void generateAllPeriods(BudgetHeader budget) {
		// calculate first period - only period 1 exists at this point
		calculateBudget(budget);

		List<BudgetPeriod> budgetPeriods = budget.getBudgetPeriods();

		/* get all period one line items */

		List<BudgetDetail> budgetLineItems = new ArrayList<BudgetDetail>();
		int period1Duration = 0;
		BudgetPeriod budgetPeriod1 = null;
		for (BudgetPeriod budgetPeriod : budgetPeriods) {
			Integer budPeriod = budgetPeriod.getBudgetPeriod();
			//Integer budgetPeriodId = budgetPeriod.getBudgetPeriodId();
			int lineDuration = 0;
			int currentPeriodDuration = 0;
			int gap = 0;
			List<Date> startEndDates = new ArrayList<Date>();
			switch (budPeriod) {
			case 1:
				// get line items for first period
				budgetPeriod1 = budgetPeriod;
				budgetLineItems = budgetPeriod.getBudgetDetails();
				period1Duration = dateTimeService.dateDiff(budgetPeriod.getStartDate(), budgetPeriod.getEndDate(), false);
				break;
			default:
				/* add line items for following periods */
				for (BudgetDetail periodLineItem : budgetLineItems) {
					//BudgetDetail budgetLineItem = getDataObjectService().copyInstance(periodLineItem, CopyOption.RESET_PK_FIELDS, CopyOption.RESET_VERSION_NUMBER, CopyOption.RESET_OBJECT_ID);
					BudgetDetail budgetLineItem = new BudgetDetail();
					//budgetLineItem.setBudgetId(budget.getBudgetId());
					//budgetLineItem.getBudgetCalculatedAmounts().clear();
					budgetLineItem.setBudgetPeriod(budPeriod);
					//budgetLineItem.setBudgetPeriodId(budgetPeriodId);
					budgetLineItem.setPeriod(budgetPeriod);
					boolean isLeapDateInPeriod = isLeapDaysInPeriod(budgetLineItem.getStartDate(), budgetLineItem.getEndDate());
					gap = dateTimeService.dateDiff(budgetPeriod1.getStartDate(), budgetLineItem.getStartDate(), false);
					boolean isLeapDayInGap = isLeapDaysInPeriod(budgetPeriod1.getStartDate(), budgetLineItem.getStartDate());
					lineDuration = dateTimeService.dateDiff(budgetLineItem.getStartDate(), budgetLineItem.getEndDate(), false);
					currentPeriodDuration = dateTimeService.dateDiff(budgetPeriod.getStartDate(), budgetPeriod.getEndDate(), false);
					if (period1Duration == lineDuration || lineDuration > currentPeriodDuration) {
						budgetLineItem.setStartDate(budgetPeriod.getStartDate());
						budgetLineItem.setEndDate(budgetPeriod.getEndDate());
					} else {
						startEndDates.add(0, budgetPeriod.getStartDate());
						startEndDates.add(1, budgetPeriod.getEndDate());
						List<Date> dates = getNewStartEndDates(startEndDates, gap, lineDuration, budgetLineItem.getStartDate(), isLeapDateInPeriod, isLeapDayInGap);
						budgetLineItem.setStartDate(dates.get(0));
						budgetLineItem.setEndDate(dates.get(1));
					}
					budgetLineItem.setLineItemNumber(budgetLineItem.getLineItemNumber());
					lineDuration = dateTimeService.dateDiff(periodLineItem.getStartDate(), periodLineItem.getEndDate(), false);
					budgetPeriod.getBudgetDetails().add(budgetLineItem);

				}
			}
		}

		/*BudgetPeriod firstPeriod = budgetPeriods.get(0);
		for (BudgetDetail budgetLineItem : new ArrayList<>(firstPeriod.getBudgetDetails())) {
			budgetCalculationService.applyToLaterPeriods(budget, firstPeriod, budgetLineItem);
		}*/

		// now we have generated all periods, calculate all periods
		calculateBudget(budget);
		// reset the old start/end date
		//setupOldStartEndDate(budget, true);
	}

	/* call budget calculation service to calculate budget */
	@Override
	public void calculateBudget(BudgetHeader budget) {
		recalculateBudget(budget);
	}

	@Override
	public void recalculateBudget(BudgetHeader budget) {
		budgetCalculationService.calculateBudget(budget);
	}

	@Override
	public boolean isRateOverridden(BudgetPeriod budgetPeriod) {
		return false;
	}

	@Override
	public boolean isLeapDaysInPeriod(Date sDate, Date eDate) {
		Date leapDate;
        int sYear = getYear(sDate);
        int eYear = getYear(eDate);
        if (isLeapYear(sDate)) {            
            Calendar c1 = Calendar.getInstance(); 
            c1.clear();
            c1.set(sYear, 1, 29);
            leapDate = new java.sql.Date(c1.getTime().getTime());
            // start date is before 2/29 & enddate >= 2/29
            if (sDate.before(leapDate)) {
                if (eDate.compareTo(leapDate) >= 0) {
                    return true;
                }           
            } else if (sDate.equals(leapDate)) {
                return true;
            }
        } else if (isLeapYear(eDate)) {
            Calendar c1 = Calendar.getInstance(); 
            c1.set(eYear, 1, 29);
            leapDate = new java.sql.Date(c1.getTime().getTime());
            if (eDate.compareTo(leapDate) >= 0) {
                return true;
            }
        } else {
            sYear++;
            while (eYear > sYear) {
                if (isLeapYear(sYear)) {
                    return true;
                }
                sYear++;
            }
        }
        return false;
	}

	protected int getYear(Date date) {
        Calendar c1 = Calendar.getInstance(); 
        c1.setTime(new java.util.Date(date.getTime()));
        return c1.get(Calendar.YEAR);

    }

	protected boolean isLeapYear(Date date) {
        int year = getYear(date);
              
        return isLeapYear(year);
    }

	protected boolean isLeapYear(int year) {
        boolean isLeapYear;

        isLeapYear = (year % 4 == 0);
        isLeapYear = isLeapYear && (year % 100 != 0);
        isLeapYear = isLeapYear || (year % 400 == 0);        
        return isLeapYear;

    }

	@Override
	public List<Date> getNewStartEndDates(List<Date> startEndDates, int gap, int duration, Date prevDate,
			boolean leapDayInPeriod, boolean leapDayInGap) {
		Date startDate = startEndDates.get(0);
        Date newStartDate = add(startDate, gap);
        Date newEndDate = add(newStartDate,duration);

        boolean isLeapDayInNewPeriod = isLeapDaysInPeriod(startDate, newEndDate);
        boolean isLeapDayInNewGap = isLeapDaysInPeriod(startDate,newStartDate);
        boolean isLeapDayInInitialPeriod = leapDayInPeriod || leapDayInGap;

        if (isLeapDayInInitialPeriod && !isLeapDayInNewPeriod) {
            newEndDate = add(newEndDate, -1);
        } else if (!isLeapDayInInitialPeriod && isLeapDayInNewPeriod) {
            newEndDate = add(newEndDate, 1);
        }

        if (!isLeapDayInNewGap && leapDayInGap) {
            newStartDate = add(newStartDate, -1);
        } else if (isLeapDayInNewGap && !leapDayInGap) {
            newStartDate = add(newStartDate, 1);
        }

        List<Date> newStartEndDates = new ArrayList<>();
        newStartEndDates.add(0,newStartDate);
        newStartEndDates.add(1,newEndDate);
        return newStartEndDates;
	}

	protected Date add(Date date, int days) {
        Calendar c1 = Calendar.getInstance(); 
        c1.setTime(new java.util.Date(date.getTime()));
        c1.add(Calendar.DATE,days);
        return new java.sql.Date(c1.getTime().getTime());
        
    }

	@Override
	public List<CostElement> fetchSysGeneratedCostElements(String activityTypeCode) {
		List<CostElement> systemGeneratedCE = new ArrayList<>();
		if (activityTypeCode != null && activityTypeCode.equals("1")) {
			CostElement BUDGET_RESEARCH_OH_ON = budgetDao
					.fetchCostElementsById(commonDao.getParameterValueAsString(Constants.KC_GENERIC_PARAMETER_NAMESPACE,
							Constants.KC_ALL_PARAMETER_DETAIL_TYPE_CODE, Constants.BUDGET_RESEARCH_OH_ON));
			BUDGET_RESEARCH_OH_ON.setSystemGeneratedCEType(Constants.BUDGET_RESEARCH_OH_ON);
			/*CostElement BUDGET_RESEARCH_OH_OFF = budgetDao
					.fetchCostElementsById(commonDao.getParameterValueAsString(Constants.KC_GENERIC_PARAMETER_NAMESPACE,
							Constants.KC_ALL_PARAMETER_DETAIL_TYPE_CODE, Constants.BUDGET_RESEARCH_OH_OFF));
			BUDGET_RESEARCH_OH_OFF.setSystemGeneratedCEType(Constants.BUDGET_RESEARCH_OH_OFF);*/
			systemGeneratedCE.add(BUDGET_RESEARCH_OH_ON);
			//systemGeneratedCE.add(BUDGET_RESEARCH_OH_OFF);
		} else {
			CostElement BUDGET_OH_ON = budgetDao
					.fetchCostElementsById(commonDao.getParameterValueAsString(Constants.KC_GENERIC_PARAMETER_NAMESPACE,
							Constants.KC_ALL_PARAMETER_DETAIL_TYPE_CODE, Constants.BUDGET_OH_ON));
			BUDGET_OH_ON.setSystemGeneratedCEType(Constants.BUDGET_OH_ON);
			/*CostElement BUDGET_OH_OFF = budgetDao
					.fetchCostElementsById(commonDao.getParameterValueAsString(Constants.KC_GENERIC_PARAMETER_NAMESPACE,
							Constants.KC_ALL_PARAMETER_DETAIL_TYPE_CODE, Constants.BUDGET_OH_OFF));
			BUDGET_OH_OFF.setSystemGeneratedCEType(Constants.BUDGET_OH_OFF);*/
			systemGeneratedCE.add(BUDGET_OH_ON);
			//systemGeneratedCE.add(BUDGET_OH_OFF);
		}
		CostElement BUDGET_FRINGE_ON = budgetDao
				.fetchCostElementsById(commonDao.getParameterValueAsString(Constants.KC_GENERIC_PARAMETER_NAMESPACE,
						Constants.KC_ALL_PARAMETER_DETAIL_TYPE_CODE, Constants.BUDGET_FRINGE_ON));
		BUDGET_FRINGE_ON.setSystemGeneratedCEType(Constants.BUDGET_FRINGE_ON);
		/*CostElement BUDGET_FRINGE_OFF = budgetDao
				.fetchCostElementsById(commonDao.getParameterValueAsString(Constants.KC_GENERIC_PARAMETER_NAMESPACE,
						Constants.KC_ALL_PARAMETER_DETAIL_TYPE_CODE, Constants.BUDGET_FRINGE_OFF));
		BUDGET_FRINGE_OFF.setSystemGeneratedCEType(Constants.BUDGET_FRINGE_OFF);*/
		systemGeneratedCE.add(BUDGET_FRINGE_ON);
		//systemGeneratedCE.add(BUDGET_FRINGE_OFF);
		return systemGeneratedCE;
	}

	@Override
	public String getSyncBudgetRates(ProposalVO proposalVO) {
		Proposal proposal = proposalVO.getProposal();
		proposal.getBudgetHeader().getProposalRates().clear();
		Set<String> rateClassTypes = new HashSet<>();
		List<FibiProposalRate> proposalRates = fetchFilteredProposalRates(proposal, rateClassTypes);
		proposal.getBudgetHeader().getProposalRates().addAll(proposalRates);
		proposal = proposalDao.saveOrUpdateProposal(proposal);
		proposalVO.setProposal(proposal);
		proposalVO.setRateClassTypes(rateClassTypes);
		return committeeDao.convertObjectToJSON(proposalVO);
	}

	@Override
	public String resetProposalRates(ProposalVO vo) {
		Proposal proposal = vo.getProposal();
		List<FibiProposalRate> proposalRates = proposal.getBudgetHeader().getProposalRates();
		if (proposalRates != null && !proposalRates.isEmpty()) {
			for (FibiProposalRate proposalRate : proposalRates) {
				proposalRate.setApplicableRate(proposalRate.getInstituteRate());
			}
		}
		proposal = proposalDao.saveOrUpdateProposal(proposal);
		vo.setProposal(proposal);
		return committeeDao.convertObjectToJSON(vo);
	}

	@Override
	public List<FibiProposalRate> fetchFilteredProposalRates(Proposal proposal, Set<String> rateClassTypes) {
		BudgetHeader budget = proposal.getBudgetHeader();
		List<FibiProposalRate> proposalRates = new ArrayList<FibiProposalRate>();
		Date startDate = budget.getStartDate();
		InstituteRate rate = budgetDao.fetchInstituteRateByDateLessthanMax(startDate, proposal.getActivityTypeCode());
		if (rate != null) {
			proposalRates.add(prepareProposalRate(rate, budget, rateClassTypes));
		}
		List<InstituteRate> instituteRates = budgetDao.filterInstituteRateByDateRange(startDate, budget.getEndDate(), proposal.getActivityTypeCode());
		if (instituteRates != null && !instituteRates.isEmpty()) {
			for (InstituteRate instituteRate : instituteRates) {
				proposalRates.add(prepareProposalRate(instituteRate, budget, rateClassTypes));
			}
			return proposalRates;
		}
		return proposalRates;
	}

	public FibiProposalRate prepareProposalRate(InstituteRate instituteRate, BudgetHeader budget, Set<String> rateClassTypes) {
		FibiProposalRate proposalRate = new FibiProposalRate();
		proposalRate.setApplicableRate(instituteRate.getInstituteRate());
		proposalRate.setFiscalYear(instituteRate.getFiscalYear());
		proposalRate.setInstituteRate(instituteRate.getInstituteRate());
		proposalRate.setOnOffCampusFlag(instituteRate.getOnOffCampusFlag());
		proposalRate.setBudgetHeader(budget);
		proposalRate.setRateClassCode(instituteRate.getRateClassCode());
		proposalRate.setRateTypeCode(instituteRate.getRateTypeCode());
		proposalRate.setStartDate(instituteRate.getStartDate());
		proposalRate.setUpdateTimeStamp(committeeDao.getCurrentTimestamp());
		proposalRate.setUpdateUser(budget.getUpdateUser());
		proposalRate.setActivityTypeCode(instituteRate.getActivityTypeCode());
		proposalRate.setRateClass(instituteRate.getRateClass());
		proposalRate.setRateType(instituteRate.getRateType());
		proposalRate.setActivityType(instituteRate.getActivityType());
		rateClassTypes.add(instituteRate.getRateClass().getDescription());
		return proposalRate;
	}

	@Override
	public String deleteBudgetPeriod(ProposalVO proposalVO) {
		Proposal proposal = proposalVO.getProposal();
		List<BudgetPeriod> budgetPeriods = proposal.getBudgetHeader().getBudgetPeriods();
		List<BudgetPeriod> updatedlist = new ArrayList<BudgetPeriod>(budgetPeriods);
		Collections.copy(updatedlist, budgetPeriods);
		int budgetPeriodNumber = 0;
		for (BudgetPeriod budgetPeriod : budgetPeriods) {
			if (budgetPeriod.getBudgetPeriodId().equals(proposalVO.getBudgetPeriodId())) {
				budgetPeriodNumber = budgetPeriod.getBudgetPeriod();
				updatedlist.remove(budgetPeriod);
			}
		}
		proposal.getBudgetHeader().getBudgetPeriods().clear();
		proposal.getBudgetHeader().getBudgetPeriods().addAll(updatedlist);
        if (budgetPeriodNumber > 0) {
        	updateBudgetPeriods(budgetPeriods, budgetPeriodNumber, true);
        }
		proposal = saveOrUpdateProposalBudget(proposalVO);
		proposalVO.setProposal(proposal);
		proposalVO.setStatus(true);
		proposalVO.setMessage("Budget period deleted successfully");
		return committeeDao.convertObjectToJSON(proposalVO);
	}

	@Override
	public String deleteBudgetLineItem(ProposalVO proposalVO) {
		Proposal proposal = proposalVO.getProposal();
		List<BudgetPeriod> budgetPeriods = proposal.getBudgetHeader().getBudgetPeriods();
		for (BudgetPeriod budgetPeriod : budgetPeriods) {
			if (budgetPeriod.getBudgetPeriodId().equals(proposalVO.getBudgetPeriodId())) {
				List<BudgetDetail> budgetDetails = budgetPeriod.getBudgetDetails();
				List<BudgetDetail> updatedlist = new ArrayList<BudgetDetail>(budgetDetails);
				Collections.copy(updatedlist, budgetDetails);
				for (BudgetDetail budgetDetail : budgetDetails) {
					if (budgetDetail.getBudgetDetailId().equals(proposalVO.getBudgetDetailId())) {
						updatedlist.remove(budgetDetail);
					}
				}
				budgetPeriod.getBudgetDetails().clear();
				budgetPeriod.getBudgetDetails().addAll(updatedlist);
			}
		}
		proposal = saveOrUpdateProposalBudget(proposalVO);
		proposalVO.setProposal(proposal);
		proposalVO.setStatus(true);
		proposalVO.setMessage("Budget detail deleted successfully");
		return committeeDao.convertObjectToJSON(proposalVO);
	}

	protected void updateBudgetPeriods(List<BudgetPeriod> budgetPeriods, int checkPeriod, boolean deletePeriod) {
		for (BudgetPeriod budgetPeriod : budgetPeriods) {
			Integer budPeriod = budgetPeriod.getBudgetPeriod();
			if (budPeriod >= checkPeriod) {
				int newPeriod = 0;
				if (deletePeriod) {
					newPeriod = budPeriod - 1;
				} else {
					newPeriod = budPeriod + 1;
				}
				budgetPeriod.setBudgetPeriod(newPeriod);
				List<BudgetDetail> budgetLineItems = budgetPeriod.getBudgetDetails();
				for (BudgetDetail periodLineItem : budgetLineItems) {
					periodLineItem.setBudgetPeriod(newPeriod);
				}
			}
		}
	}

}

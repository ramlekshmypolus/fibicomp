package com.polus.fibicomp.committee.service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.polus.fibicomp.committee.dao.CommitteeDao;
import com.polus.fibicomp.committee.pojo.Committee;
import com.polus.fibicomp.committee.pojo.CommitteeResearchAreas;
import com.polus.fibicomp.committee.pojo.CommitteeSchedule;
import com.polus.fibicomp.committee.pojo.CommitteeType;
import com.polus.fibicomp.committee.pojo.ProtocolReviewType;
import com.polus.fibicomp.committee.pojo.ResearchArea;
import com.polus.fibicomp.committee.pojo.ScheduleStatus;
import com.polus.fibicomp.committee.schedule.CronSpecialChars;
import com.polus.fibicomp.committee.schedule.DailyScheduleDetails;
import com.polus.fibicomp.committee.schedule.DefaultScheduleSequence;
import com.polus.fibicomp.committee.schedule.MonthlyScheduleDetails;
import com.polus.fibicomp.committee.schedule.ScheduleData;
import com.polus.fibicomp.committee.schedule.ScheduleSequence;
import com.polus.fibicomp.committee.schedule.StyleKey;
import com.polus.fibicomp.committee.schedule.Time24HrFmt;
import com.polus.fibicomp.committee.schedule.TrimDatesScheduleSequenceDecorator;
import com.polus.fibicomp.committee.schedule.WeekScheduleSequenceDecorator;
import com.polus.fibicomp.committee.schedule.YearlyScheduleDetails;
import com.polus.fibicomp.committee.vo.CommitteeVo;
import com.polus.fibicomp.pojo.Unit;

@Transactional
@Service(value = "committeeService")
public class CommitteeServiceImpl implements CommitteeService {

	protected static Logger logger = Logger.getLogger(CommitteeServiceImpl.class.getName());

	private static final String COLON = ":";
	private static final String SCHEDULED = "Scheduled";

	@Autowired
	private CommitteeDao committeeDao;

	@Autowired
	private ScheduleService scheduleService;

	@Override
	public String fetchInitialDatas() {
		CommitteeVo committeeVo = new CommitteeVo();
		List<ProtocolReviewType> reviewTypes = committeeDao.fetchAllReviewType();
		committeeVo.setReviewTypes(reviewTypes);
		List<Unit> units = committeeDao.fetchAllHomeUnits();
		committeeVo.setHomeUnits(units);
		List<ResearchArea> researchAreas = committeeDao.fetchAllResearchAreas();
		committeeVo.setResearchAreas(researchAreas);

		String response = committeeDao.convertObjectToJSON(committeeVo);
		return response;
	}

	@Override
	public String createCommittee(Integer committeeTypeCode) {
		CommitteeVo committeeVo = new CommitteeVo();
		Committee committee = new Committee();
		committeeVo.setCommitteeTypeCode(committeeTypeCode);
		CommitteeType committeeType = committeeDao.fetchCommitteeType(committeeTypeCode);
		// committee.setCommitteeTypeCode(committeeType.getCommitteeTypeCode());
		committee.setCommitteeType(committeeType);
		// committee.setResearchAreas(new ArrayList<CommitteeResearchAreas>());
		committeeVo.setCommittee(committee);

		List<ProtocolReviewType> reviewTypes = committeeDao.fetchAllReviewType();
		committeeVo.setReviewTypes(reviewTypes);
		List<Unit> units = committeeDao.fetchAllHomeUnits();
		committeeVo.setHomeUnits(units);
		List<ResearchArea> researchAreas = committeeDao.fetchAllResearchAreas();
		committeeVo.setResearchAreas(researchAreas);

		String response = committeeDao.convertObjectToJSON(committeeVo);
		return response;
	}

	@Override
	public String saveCommittee(CommitteeVo vo) {
		Committee committee = vo.getCommittee();
		String updateType = vo.getUpdateType();
		if (updateType != null && updateType.equals("SAVE")) {
			committee.setCreateTimestamp(committeeDao.getCurrentTimestamp());
			committee.setCreateUser(vo.getCurrentUser());
			committee.setObjectId(UUID.randomUUID().toString());
			committee.setUpdateTimestamp(committeeDao.getCurrentTimestamp());
			committee.setUpdateUser(vo.getCurrentUser());
			List<CommitteeResearchAreas> researchAreas = committee.getResearchAreas();
			if (researchAreas != null && !researchAreas.isEmpty()) {
				for (CommitteeResearchAreas researchArea : researchAreas) {
					researchArea.setUpdateTimestamp(committeeDao.getCurrentTimestamp());
					researchArea.setUpdateUser(vo.getCurrentUser());
					researchArea.setObjectId(UUID.randomUUID().toString());
				}
			}
			List<CommitteeSchedule> committeeSchedules = committee.getCommitteeSchedules();
			if (committeeSchedules != null && !committeeSchedules.isEmpty()) {
				for (CommitteeSchedule schedule : committeeSchedules) {
					schedule.setUpdateTimestamp(committeeDao.getCurrentTimestamp());
					schedule.setUpdateUser(vo.getCurrentUser());
					schedule.setObjectId(UUID.randomUUID().toString());
				}
			}
		} else {
			committee.setUpdateTimestamp(committeeDao.getCurrentTimestamp());
			committee.setUpdateUser(vo.getCurrentUser());
		}
		
		// committee = committeeDao.saveCommittee(committee);
		vo.setCommittee(committee);
		String response = committeeDao.convertObjectToJSON(vo);
		logger.info("response : " + response);
		return response;
	}

	@Override
	public String loadCommitteeById(String committeeId) {
		CommitteeVo committeeVo = new CommitteeVo();
		Committee committee = committeeDao.fetchCommitteeById(committeeId);
		committeeVo.setCommittee(committee);

		List<ProtocolReviewType> reviewTypes = committeeDao.fetchAllReviewType();
		committeeVo.setReviewTypes(reviewTypes);
		List<Unit> units = committeeDao.fetchAllHomeUnits();
		committeeVo.setHomeUnits(units);
		List<ResearchArea> researchAreas = committeeDao.fetchAllResearchAreas();
		committeeVo.setResearchAreas(researchAreas);

		String response = committeeDao.convertObjectToJSON(committeeVo);
		return response;
	}

	@Override
	public String addSchedule(CommitteeVo committeeVo) throws ParseException {

		ScheduleData scheduleData = committeeVo.getScheduleData();
		Committee committee = committeeVo.getCommittee();
		List<Date> dates = null;
		Date dtEnd = null;
		int frequency = 0;
		int day = 0;
		CronSpecialChars[] weekdays = null;
		CronSpecialChars weekOfMonth = null;
		CronSpecialChars dayOfWeek = null;
		CronSpecialChars month = null;

		Time24HrFmt time = getTime24hFmt(scheduleData.getScheduleStartDate(), scheduleData.getTime().findMinutes());
		Date dt = scheduleData.getScheduleStartDate();

		StyleKey key = StyleKey.valueOf(scheduleData.getRecurrenceType());
		switch (key) {
		case NEVER:
			dates = scheduleService.getScheduledDates(dt, dt, time, null);
			break;
		case DAILY:
			DailyScheduleDetails.optionValues dailyoption = DailyScheduleDetails.optionValues
					.valueOf(scheduleData.getDailySchedule().getDayOption());
			switch (dailyoption) {
			case XDAY:
				dtEnd = scheduleData.getDailySchedule().getScheduleEndDate();
				day = scheduleData.getDailySchedule().getDay();
				dates = scheduleService.getIntervalInDaysScheduledDates(dt, dtEnd, time, day);
				break;
			case WEEKDAY:
				dtEnd = scheduleData.getDailySchedule().getScheduleEndDate();
				weekdays = ScheduleData.convertToWeekdays(scheduleData.getDailySchedule().getDaysOfWeek());
				ScheduleSequence scheduleSequence = new WeekScheduleSequenceDecorator(
						new TrimDatesScheduleSequenceDecorator(new DefaultScheduleSequence()), 1, weekdays.length);
				dates = scheduleService.getScheduledDates(dt, dtEnd, time, weekdays, scheduleSequence);
				break;
			}
			break;
		case WEEKLY:
			dtEnd = scheduleData.getWeeklySchedule().getScheduleEndDate();
			if (CollectionUtils.isNotEmpty(scheduleData.getWeeklySchedule().getDaysOfWeek())) {
				weekdays = ScheduleData.convertToWeekdays(scheduleData.getWeeklySchedule().getDaysOfWeek()
						.toArray(new String[scheduleData.getWeeklySchedule().getDaysOfWeek().size()]));
			}

			ScheduleSequence scheduleSequence = new WeekScheduleSequenceDecorator(
					new TrimDatesScheduleSequenceDecorator(new DefaultScheduleSequence()),
					scheduleData.getWeeklySchedule().getWeek(), weekdays.length);
			dates = scheduleService.getScheduledDates(dt, dtEnd, time, weekdays, scheduleSequence);
			break;
		case MONTHLY:
			MonthlyScheduleDetails.optionValues monthOption = MonthlyScheduleDetails.optionValues
					.valueOf(scheduleData.getMonthlySchedule().getMonthOption());
			switch (monthOption) {
			case XDAYANDXMONTH:
				dtEnd = scheduleData.getMonthlySchedule().getScheduleEndDate();
				day = scheduleData.getMonthlySchedule().getDay();
				frequency = scheduleData.getMonthlySchedule().getOption1Month();
				dates = scheduleService.getScheduledDates(dt, dtEnd, time, day, frequency, null);
				break;
			case XDAYOFWEEKANDXMONTH:
				dtEnd = scheduleData.getMonthlySchedule().getScheduleEndDate();
				weekOfMonth = ScheduleData.getWeekOfMonth(scheduleData.getMonthlySchedule().getSelectedMonthsWeek());
				dayOfWeek = ScheduleData.getDayOfWeek(scheduleData.getMonthlySchedule().getSelectedDayOfWeek());
				frequency = scheduleData.getMonthlySchedule().getOption2Month();
				dates = scheduleService.getScheduledDates(dt, dtEnd, time, dayOfWeek, weekOfMonth, frequency, null);
				break;
			}
			break;
		case YEARLY:
			YearlyScheduleDetails.yearOptionValues yearOption = YearlyScheduleDetails.yearOptionValues
					.valueOf(scheduleData.getYearlySchedule().getYearOption());
			switch (yearOption) {
			case XDAY:
				dtEnd = scheduleData.getYearlySchedule().getScheduleEndDate();
				month = ScheduleData.getMonthOfWeek(scheduleData.getYearlySchedule().getSelectedOption1Month());
				day = scheduleData.getYearlySchedule().getDay();
				frequency = scheduleData.getYearlySchedule().getOption1Year();
				dates = scheduleService.getScheduledDates(dt, dtEnd, time, month, day, frequency, null);
				break;
			case CMPLX:
				dtEnd = scheduleData.getYearlySchedule().getScheduleEndDate();
				weekOfMonth = ScheduleData.getWeekOfMonth(scheduleData.getYearlySchedule().getSelectedMonthsWeek());
				dayOfWeek = ScheduleData.getDayOfWeek(scheduleData.getYearlySchedule().getSelectedDayOfWeek());
				month = ScheduleData.getMonthOfWeek(scheduleData.getYearlySchedule().getSelectedOption2Month());
				frequency = scheduleData.getYearlySchedule().getOption2Year();
				dates = scheduleService.getScheduledDates(dt, dtEnd, time, weekOfMonth, dayOfWeek, month, frequency,
						null);
				break;
			}
			break;
		}
		List<java.sql.Date> skippedDates = new ArrayList<java.sql.Date>();
		scheduleData.setDatesInConflict(skippedDates);
		addScheduleDatesToCommittee(dates, committee, scheduleData.getPlace(), skippedDates, committeeVo);
		//committee = committeeDao.saveCommittee(committee);
		committeeVo.setCommittee(committee);
		String response = committeeDao.convertObjectToJSON(committeeVo);
		return response;
	}

	/**
	 * @param date
	 * @param min
	 * @return
	 * @throws ParseException
	 */
	protected Time24HrFmt getTime24hFmt(Date date, int min) throws ParseException {
		Date dt = DateUtils.round(date, Calendar.DAY_OF_MONTH);
		dt = DateUtils.addMinutes(dt, min);
		Calendar cl = new GregorianCalendar();
		cl.setTime(dt);
		StringBuffer sb = new StringBuffer();
		String str = sb.append(cl.get(Calendar.HOUR_OF_DAY)).append(COLON).append(cl.get(Calendar.MINUTE)).toString();
		return new Time24HrFmt(str);
	}

	/**
	 * Helper method to add schedule to list.
	 * 
	 * @param dates
	 * @param committee
	 * @param location
	 * @param skippedDates
	 */
	protected void addScheduleDatesToCommittee(List<Date> dates, Committee committee, String location,
			List<java.sql.Date> skippedDates, CommitteeVo committeeVo) {
		for (Date date : dates) {
			java.sql.Date sqldate = new java.sql.Date(date.getTime());

			if (!isDateAvailable(committee.getCommitteeSchedules(), sqldate)) {
				skippedDates.add(sqldate);
				continue;
			}

			CommitteeSchedule committeeSchedule = new CommitteeSchedule();
			committeeSchedule.setCommittee(committee);
			committeeSchedule.setScheduledDate(sqldate);
			committeeSchedule.setPlace(location);
			committeeSchedule.setTime(new Timestamp(date.getTime()));

			int daysToAdd = committee.getAdvSubmissionDaysReq();
			java.sql.Date sqlDate = calculateAdvancedSubmissionDays(date, daysToAdd);
			committeeSchedule.setProtocolSubDeadline(sqlDate);

			committeeSchedule.setCommittee(committee);

			ScheduleStatus defaultStatus = getDefaultScheduleStatus();
			committeeSchedule.setScheduleStatusCode(defaultStatus.getScheduleStatusCode());
			committeeSchedule.setScheduleStatus(defaultStatus);

			committeeSchedule.setUpdateTimestamp(committeeDao.getCurrentTimestamp());
			committeeSchedule.setUpdateUser(committeeVo.getCurrentUser());
			committeeSchedule.setObjectId(UUID.randomUUID().toString());

			committee.getCommitteeSchedules().add(committeeSchedule);
		}
	}

	/**
	 * Helper method to test if date is available (non conflicting).
	 * 
	 * @param committeeSchedules
	 * @param date
	 * @return
	 */
	protected Boolean isDateAvailable(List<CommitteeSchedule> committeeSchedules, java.sql.Date date) {
		boolean retVal = true;
		for (CommitteeSchedule committeeSchedule : committeeSchedules) {
			Date scheduledDate = committeeSchedule.getScheduledDate();
			if ((scheduledDate != null) && DateUtils.isSameDay(scheduledDate, date)) {
				retVal = false;
				break;
			}
		}
		return retVal;
	}

	/**
	 * Helper method to calculate advanced submission days.
	 * 
	 * @param startDate
	 * @param days
	 * @return
	 */
	protected java.sql.Date calculateAdvancedSubmissionDays(Date startDate, Integer days) {
		Date deadlineDate = DateUtils.addDays(startDate, -days);
		return new java.sql.Date(deadlineDate.getTime());
	}

	/**
	 * Helper method to retrieve default ScheduleStatus object.
	 * 
	 * @return
	 */
	protected ScheduleStatus getDefaultScheduleStatus() {
		return committeeDao.fetchScheduleStatusByStatus(SCHEDULED);
	}

}

package com.polus.fibicomp.dao;

import java.util.List;

import org.springframework.stereotype.Service;

/*import com.polus.fibicomp.pojo.AwardView;*/
import com.polus.fibicomp.pojo.DashBoardProfile;
import com.polus.fibicomp.pojo.PersonDTO;
import com.polus.fibicomp.pojo.PrincipalBo;

@Service
public interface DashboardDao {

	/* public List<PrincipalBo> getAllUsers(); */

	public String getDashBoardResearchSummary(PersonDTO personDTO) throws Exception;

	public DashBoardProfile getDashBoardDataForAward(PersonDTO personDTO, String requestType, Integer pageNumber,
			String sortBy, String reverse);

	public DashBoardProfile getDashBoardDataForProposal(PersonDTO personDTO, String requestType, Integer pageNumber,
			String sortBy, String reverse);

	public DashBoardProfile getProtocolDashboardData(PersonDTO personDTO, String requestType, Integer pageNumber,
			String sortBy, String reverse);

	public DashBoardProfile getDashBoardDataForIacuc(PersonDTO personDTO, String requestType, Integer pageNumber,
			String sortBy, String reverse);

	public DashBoardProfile getDashBoardDataForDisclosures(PersonDTO personDTO, String requestType, Integer pageNumber,
			String sortBy, String reverse);

	public Integer getDashBoardCount(PersonDTO personDTO, String requestType, Integer pageNumber);

	/* public List<AwardView> getAllAwardViews(); */
}

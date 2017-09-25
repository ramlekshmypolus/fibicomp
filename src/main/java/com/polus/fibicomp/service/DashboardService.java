package com.polus.fibicomp.service;

import java.util.List;

import org.springframework.stereotype.Service;

/*import com.polus.fibicomp.pojo.AwardView;*/
import com.polus.fibicomp.pojo.PersonDTO;
import com.polus.fibicomp.pojo.PrincipalBo;

@Service
public interface DashboardService {

	/* public List<PrincipalBo> getAllUsers(); */

	public String getDashBoardResearchSummary(PersonDTO personDTO) throws Exception;

	public String getDashBoardData(PersonDTO personDTO, String requestType, Integer pageNumber, String sortBy,
			String reverse) throws Exception;
	/* public List<AwardView> getAllAwardViews(); */
}

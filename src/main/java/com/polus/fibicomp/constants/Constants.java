package com.polus.fibicomp.constants;

public interface Constants {

	String MODULE_NAMESPACE_PROPOSAL_DEVELOPMENT = "KC-PD";
	String PARAMETER_COMPONENT_DOCUMENT = "Document";
	String ALL_SPONSOR_HIERARCHY_NIH_MULTI_PI = "ALL_SPONSOR_HIERARCHY_NIH_MULTI_PI";
	String KC_GENERIC_PARAMETER_NAMESPACE = "KC-GEN";
	String KC = "KC";
	String KC_ALL_PARAMETER_DETAIL_TYPE_CODE = "All";
	String SPONSOR_HIERARCHIES_PARM = "PERSON_ROLE_SPONSOR_HIERARCHIES";
	String NIH_MULTIPLE_PI_HIERARCHY = "NIH Multiple PI";
	String DEFAULT_SPONSOR_HIERARCHY_NAME = "DEFAULT";
	String INVALID_TIME = "Invalid Time";
	String ALTERNATE_ROLE = "12";
	String INACTIVE_ROLE = "14";
	String DEFAULTTIME = "12:00";
	String NAME = "t";
	String GROUP = "g";
	String JOBNAME = "j";
	String JOBGROUP = "g";
	String COLON = ":";
	String ZERO = "0";
	String PRINCIPAL_INVESTIGATOR = "PI";
	String MULTI_PI = "MPI";
	String CO_INVESTIGATOR = "COI";
	String KEY_PERSON = "KP";
	String DATABASE_BOOLEAN_TRUE_STRING_REPRESENTATION = "Y";
	String DATABASE_BOOLEAN_FALSE_STRING_REPRESENTATION = "N";
	String ATTENDANCE = "2";
	String PROTOCOL = "3";
	String ACTION_ITEM = "4";
	String PROTOCOL_REVIEWER_COMMENT = "6";
	String DESCRIPTION = "description";
	String HASH_ALGORITHM = "SHA";
	String CHARSET = "UTF-8";

	// Security constants
	String SECRET = "SecretKeyToGenJWTs";
    long EXPIRATION_TIME = 864_000_000; // 10 days
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
    String SIGN_UP_URL = "/fibi-comp/login";

}

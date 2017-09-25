package com.polus.fibicomp.util;

import javax.persistence.AttributeConverter;

public class JpaCharBooleanConversion implements AttributeConverter<Boolean, String> {

	public static final String DATABASE_BOOLEAN_TRUE_STRING_REPRESENTATION = "Y";
	public static final String DATABASE_BOOLEAN_FALSE_STRING_REPRESENTATION = "N";

	@Override
	public String convertToDatabaseColumn(Boolean b) {
		if (b == null) {
			return null;
		}
		if (b.booleanValue()) {
			return DATABASE_BOOLEAN_TRUE_STRING_REPRESENTATION;
		}
		return DATABASE_BOOLEAN_FALSE_STRING_REPRESENTATION;
	}

	@Override
	public Boolean convertToEntityAttribute(String s) {
		if (s == null) {
			return null;
		}
		if (s.equals("Y") || s.equals("y")) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

}

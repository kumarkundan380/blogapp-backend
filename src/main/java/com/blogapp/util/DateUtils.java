package com.blogapp.util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class DateUtils {
	
	private DateUtils() {}
	
	public static  Date convertLocalDateToDate(LocalDate date) {
		return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

}

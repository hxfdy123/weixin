package com.dingzhi.util;

import java.util.Date;
import java.text.SimpleDateFormat;
public class DateUtil{
	
	public static String getCurrentDateStr() {
		Date date=new Date();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddhhmmssSSS");
		return sdf.format(date);
	}
	
	public static void main (String[] args) throws Exception{
		System.out.println(getCurrentDateStr());
		
	}
	
}
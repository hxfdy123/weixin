package com.dingzhi.util;

import java.util.Random;


public class StringUtil{
	//判断是否为空
       public static boolean isEmpty(String str) {
	       
    	   if(str==null||"".equals(str.trim())) {
    		   return true;
    	   }else {
    	   
    	   return false;   
       }	
	
       }
	//判断是否不是空
       public static boolean isNotEmpty(String str) {
    	   if((str!=null)&&!"".equals(str.trim())) {
    		   return true;
    	   }else {
    	   
    	   return false;   
       }
    	   
       }
       
       //生成由A-Z，0-9组成的随机字符串（length）字符串长度
       public static String getRandomString(int length) {
    	   Random random=new Random();
    	   
    	   StringBuffer sb = new StringBuffer();
    	   
    	   for(int i = 0;i<length;++i) {
    		   int number = random.nextInt(2);
    		   long result = 0;
    		   switch(number) {
    	    	 case 0:
    	    		 result= Math.round(Math.random()*25+65);
    	    		 sb.append(String.valueOf((char)result));
    	    	     break;
    	    	 case 1:
    	    		 sb.append(String.valueOf(new Random().nextInt(10)));
    	    		 break;
    	    	 }
    		   
    	   }
    	 
    	   return sb.toString();
    	   }
    	   
       
       
       
       
}
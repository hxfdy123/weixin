package com.dingzhi.util;

import java.util.Random;


public class StringUtil{
	//�ж��Ƿ�Ϊ��
       public static boolean isEmpty(String str) {
	       
    	   if(str==null||"".equals(str.trim())) {
    		   return true;
    	   }else {
    	   
    	   return false;   
       }	
	
       }
	//�ж��Ƿ��ǿ�
       public static boolean isNotEmpty(String str) {
    	   if((str!=null)&&!"".equals(str.trim())) {
    		   return true;
    	   }else {
    	   
    	   return false;   
       }
    	   
       }
       
       //������A-Z��0-9��ɵ�����ַ�����length���ַ�������
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
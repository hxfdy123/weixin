package com.dingzhi.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom.JDOMException;

import com.dingzhi.config.WeiXinPayconfig;
import com.dingzhi.util.Md5Util;
import com.dingzhi.util.XmlUtil;

public class PayNotifyUrlServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doPost(req, resp);	
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//��ȡ����    
        InputStream inputStream ;    
        StringBuffer sb = new StringBuffer();    
        inputStream = req.getInputStream();    
        String s ;    
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));    
        while ((s = in.readLine()) != null){    
            sb.append(s);    
        }    
        in.close();    
        inputStream.close();  
        System.out.println("sb:"+sb.toString());
        
        //����xml��map    
        Map<String, String> m = new HashMap<String, String>();    
        try {
			m = XmlUtil.doXMLParse(sb.toString());
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    
            
        //���˿� ���� TreeMap    
        SortedMap<Object,Object> packageParams = new TreeMap<Object,Object>();          
        Iterator<String> it = m.keySet().iterator();    
        while (it.hasNext()) {    
            String parameter = it.next();    
            String parameterValue = m.get(parameter);    
                
            String v = "";    
            if(null != parameterValue) {    
                v = parameterValue.trim();    
            } 
            packageParams.put(parameter, v);    
        }  
        
        // ΢��֧����API��Կ    
        String key = WeiXinPayconfig.key;
        
        if(isTenpaySign("UTF-8", packageParams, key)){ // ��֤ͨ��
        	if("SUCCESS".equals((String)packageParams.get("result_code"))){  
        		System.out.println("��֤ͨ��");
        	}
        }else{
        	System.out.println("��֤δͨ��");
        }

	}
	/**  
     * �Ƿ�ǩ����ȷ,������:����������a-z����,������ֵ�Ĳ������μ�ǩ����  
     * @return boolean  
     */    
    public static boolean isTenpaySign(String characterEncoding, SortedMap<Object, Object> packageParams, String API_KEY) {    
        StringBuffer sb = new StringBuffer();    
        Set es = packageParams.entrySet();    
        Iterator it = es.iterator();    
        while(it.hasNext()) {    
            Map.Entry entry = (Map.Entry)it.next();    
            String k = (String)entry.getKey();    
            String v = (String)entry.getValue();    
            if(!"sign".equals(k) && null != v && !"".equals(v)) {    
                sb.append(k + "=" + v + "&");    
            }    
        }    
        sb.append("key=" + API_KEY);    
            
        //���ժҪ    
        String mysign = Md5Util.MD5Encode(sb.toString(), characterEncoding).toLowerCase();    
        String tenpaySign = ((String)packageParams.get("sign")).toLowerCase();    
            
        return tenpaySign.equals(mysign);    
    }  
  

}

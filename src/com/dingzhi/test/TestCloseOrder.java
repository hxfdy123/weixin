package com.dingzhi.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.dingzhi.config.WeiXinPayconfig;
import com.dingzhi.util.HttpClientUtil;
import com.dingzhi.util.Md5Util;
import com.dingzhi.util.StringUtil;
import com.dingzhi.util.XmlUtil;

/**
 * @author Huangxf
 * ��Ŀ���ƣ�WeiXinPay  
 * ���Զ����رսӿ�
 * �޸�ʱ�䣺2020��1��8�� ����9:33:29  
 */
public class TestCloseOrder {

	private static String url = "https://api.mch.weixin.qq.com/pay/closeorder";
		
			
			public static void main(String[] args) throws UnsupportedOperationException, ClientProtocolException, IOException {
				Map<String,Object>map=new HashMap<String,Object>();
				   map.put("appid", WeiXinPayconfig.appid);  //���ں�ID
				   map.put("mch_id", WeiXinPayconfig.mch_id);  //�̻���
				 //  map.put("transaction_id", "");  //΢�Ŷ����� ���̻������Ŷ�ѡһ��
				   map.put("out_trade_no", "");  //�̻�������
				   map.put("nonce_str", StringUtil.getRandomString(30));//����ַ���
				   map.put("sign",getSign(map));  //ǩ��
				   String xml=XmlUtil.genXml(map);
				   System.out.println(xml);
				   InputStream in=HttpClientUtil.sendXMLDataByPost(url, xml).getEntity().getContent(); //����xml��Ϣ;
				   getElementValue(in);
				   
			
			}
			//����io��,��ȡ֧����ַ	
			private static void getElementValue(InputStream in){
				SAXReader reader = new SAXReader();
		        Document document=null;
				try {
					document = reader.read(in);
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        Element root = document.getRootElement();
		        List<Element> childElements = root.elements();
		        for (Element child : childElements) {
		        	System.out.println(child.getName()+":"+child.getStringValue());
		    
		        }
		        
			}
	
			private static String getSign(Map<String,Object>map) {
				StringBuffer sb = new StringBuffer();
				String[] keyArr = (String[])map.keySet().toArray(new String[map.keySet().size()]);//��ȡmap�е�keyתΪarray
				Arrays.sort(keyArr);//��arr����
				for(int i = 0,size = keyArr.length;i<size;++i) {
					if("sign".equals(keyArr[i])) {
						continue;
					}
					sb.append(keyArr[i]+"="+map.get(keyArr[i])+"&");
				}
				sb.append("key="+WeiXinPayconfig.key);
				String sign = Md5Util.string2MD5(sb.toString());
				return sign;
			}
}

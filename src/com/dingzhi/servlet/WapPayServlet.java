package com.dingzhi.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.message.callback.PrivateKeyCallback.Request;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.dingzhi.config.WeiXinPayconfig;
import com.dingzhi.util.DateUtil;
import com.dingzhi.util.HttpClientUtil;
import com.dingzhi.util.Md5Util;
import com.dingzhi.util.StringUtil;
import com.dingzhi.util.XmlUtil;

public class WapPayServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doPost(req,resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String ordernum = DateUtil.getCurrentDateStr();//���ɶ����� 
		   Map<String,Object>map=new HashMap<String,Object>();
		   map.put("appid", WeiXinPayconfig.appid);  //���ں�ID
		   map.put("mch_id", WeiXinPayconfig.mch_id);  //�̻���
		   map.put("device_info", WeiXinPayconfig.device_info);  //�豸��
		   map.put("nonce_str", StringUtil.getRandomString(30));  //����ַ���
		   map.put("notify_url", WeiXinPayconfig.notify_url);  //�첽֪ͨ��ַ
		   map.put("trade_type", "MWEB");  //��������
		   map.put("out_trade_no", ordernum);  //������
		   map.put("body", "yudingxingcheng");  //��Ʒ����
		   map.put("total_fee", 100);  //֧������ȷ���֣�
		   map.put("spbill_create_ip", getRemortIP(req));  //�û�IP
		  // String sign = PayCommonUtil.createSign("UTF-8", packageParams, key);
		   map.put("sign",getSign(map));  //ǩ��
		   map.put("scene_info", "{\"h5_info\": {\"type\":\"Wap\",\"wap_url\": \"http://custombus.zhenqingbus.com\",\"wap_name\": \"tencense\"}}");
		   String xml=XmlUtil.genXml(map);
		   System.out.println(xml);
		   InputStream in=HttpClientUtil.sendXMLDataByPost(WeiXinPayconfig.url, xml).getEntity().getContent(); //����xml��Ϣ;
		   String mweb_url=getElementValue(in,"mweb_url");//֧����ת������΢�ſͻ���
		   
		  // mweb_url+="&redirect_url="+URLEncoder.encode("https://dzpay.zhenqingbus.com","GBK");
		   mweb_url+="&redirect_url="+URLEncoder.encode("http://custombus.zhenqingbus.com","GBK");//֧������תҳ��
		   resp.sendRedirect(mweb_url);
		   System.out.println("mweb_url::"+mweb_url);
		   
	}
	//��ȡIP
		private static String getRemortIP(HttpServletRequest request) {
			if(request.getHeader("x-forwarded-for")==null) {
				
				return request.getRemoteAddr();
			}
			return request.getHeader("x-forwarded-for");
		}
		//��ȡsign
		private String getSign(Map<String,Object>map) {
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
		//����io��,��ȡ֧����ַ	
		private String getElementValue(InputStream in,String key){
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
	        	if(key.equals(child.getName())){
	        		return child.getStringValue();
	        	}
	        }
	        return null;
		}
	
}


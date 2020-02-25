package com.dingzhi.servlet;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
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
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

/**
 * Servlet implementation class LoadPayimageServlet
 */


/**
 * @author Huangxf
 * ��Ŀ���ƣ�WeiXinPay 
 * ����֧�������ɶ�ά�� 
 * �޸�ʱ�䣺2020��1��8�� ����9:02:55  
 */
public class LoadPayimageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
  
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	  this.doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	   String ordernum = DateUtil.getCurrentDateStr();//���ɶ����� 
	   Map<String,Object>map=new HashMap<String,Object>();
	   map.put("appid", WeiXinPayconfig.appid);  //���ں�ID
	   map.put("mch_id", WeiXinPayconfig.mch_id);  //�̻���
	   map.put("device_info", WeiXinPayconfig.device_info);  //�豸��
	   map.put("nonce_str", StringUtil.getRandomString(30));  //����ַ���
	   map.put("notify_url", WeiXinPayconfig.notify_url);  //�첽֪ͨ��ַ
	   map.put("trade_type", "NATIVE");  //��������
	   map.put("out_trade_no", ordernum);  //������
	   map.put("body", "dingzhiceshi");  //��Ʒ����
	   map.put("total_fee", 100);  //֧������ȷ���֣�
	   map.put("spbill_create_ip", getRemortIP(request));  //�û�IP
	   map.put("sign",getSign(map));  //ǩ��
	   String xml=XmlUtil.genXml(map);
	   System.out.println(xml);
	   InputStream in=HttpClientUtil.sendXMLDataByPost(WeiXinPayconfig.url, xml).getEntity().getContent(); //����xml��Ϣ;
	   String code_url=getElementValue(in,"code_url");//��ά���ַ
	   MultiFormatWriter multiFormatWriter = new MultiFormatWriter();  
	    Map hints = new HashMap();  
	    BitMatrix bitMatrix = null;  
	    try {  
	         bitMatrix = multiFormatWriter.encode(code_url, BarcodeFormat.QR_CODE, 250, 250,hints);  
	         BufferedImage image = toBufferedImage(bitMatrix);  
	         //�����ά��ͼƬ��  
	         ImageIO.write(image, "png", response.getOutputStream());  
	     } catch (WriterException e1) {  
	         e1.printStackTrace();  
	     }   
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
//����ת��  
	 public static BufferedImage toBufferedImage(BitMatrix matrix) {  
      int width = matrix.getWidth();  
      int height = matrix.getHeight();  
      BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);  
      for (int x = 0; x < width; x++) {  
          for (int y = 0; y < height; y++) {  
              image.setRGB(x, y, matrix.get(x, y) == true ? 0xff000000 : 0xFFFFFFFF);  
          }  
      }  
      return image;  
	 }

}

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
 * 项目名称：WeiXinPay 
 * 发起支付，生成二维码 
 * 修改时间：2020年1月8日 上午9:02:55  
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
	   String ordernum = DateUtil.getCurrentDateStr();//生成订单号 
	   Map<String,Object>map=new HashMap<String,Object>();
	   map.put("appid", WeiXinPayconfig.appid);  //公众号ID
	   map.put("mch_id", WeiXinPayconfig.mch_id);  //商户号
	   map.put("device_info", WeiXinPayconfig.device_info);  //设备号
	   map.put("nonce_str", StringUtil.getRandomString(30));  //随机字符串
	   map.put("notify_url", WeiXinPayconfig.notify_url);  //异步通知地址
	   map.put("trade_type", "NATIVE");  //交易类型
	   map.put("out_trade_no", ordernum);  //订单号
	   map.put("body", "dingzhiceshi");  //商品描述
	   map.put("total_fee", 100);  //支付金额（精确到分）
	   map.put("spbill_create_ip", getRemortIP(request));  //用户IP
	   map.put("sign",getSign(map));  //签名
	   String xml=XmlUtil.genXml(map);
	   System.out.println(xml);
	   InputStream in=HttpClientUtil.sendXMLDataByPost(WeiXinPayconfig.url, xml).getEntity().getContent(); //发送xml消息;
	   String code_url=getElementValue(in,"code_url");//二维码地址
	   MultiFormatWriter multiFormatWriter = new MultiFormatWriter();  
	    Map hints = new HashMap();  
	    BitMatrix bitMatrix = null;  
	    try {  
	         bitMatrix = multiFormatWriter.encode(code_url, BarcodeFormat.QR_CODE, 250, 250,hints);  
	         BufferedImage image = toBufferedImage(bitMatrix);  
	         //输出二维码图片流  
	         ImageIO.write(image, "png", response.getOutputStream());  
	     } catch (WriterException e1) {  
	         e1.printStackTrace();  
	     }   
	}

	//获取IP
	private static String getRemortIP(HttpServletRequest request) {
		if(request.getHeader("x-forwarded-for")==null) {
			
			return request.getRemoteAddr();
		}
		return request.getHeader("x-forwarded-for");
	}
	
	//获取sign
	private String getSign(Map<String,Object>map) {
		StringBuffer sb = new StringBuffer();
		String[] keyArr = (String[])map.keySet().toArray(new String[map.keySet().size()]);//获取map中的key转为array
		Arrays.sort(keyArr);//对arr排序
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
	//解析io流,获取支付地址	
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
//类型转换  
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

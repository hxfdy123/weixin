package com.dingzhi.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Huangxf 
 * ��Ŀ���ƣ�WeiXinPay 
 * ģ���ȡ֧��״̬ ��������ݿ�֧��״̬ 
 * return_url
 * �޸�ʱ�䣺2020��1��8�� ����8:44:02
 */
public class LoadPayStateServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private int i = 0;
	private int result = 0;//ģ��0δ֧����1��֧��

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doPost(req, resp);

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		i++;
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (i == 10) {
			result = 1;

		}
		System.out.println("i=" + i);
		System.out.println("result" + result);
		resp.setContentType("text/html;charset=utf-8");
		PrintWriter write = resp.getWriter();
		write.println(result);
		write.flush();
		write.close();
	}

}

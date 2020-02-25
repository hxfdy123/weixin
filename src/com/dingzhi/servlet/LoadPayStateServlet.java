package com.dingzhi.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Huangxf 
 * 项目名称：WeiXinPay 
 * 模拟获取支付状态 ，监控数据库支付状态 
 * return_url
 * 修改时间：2020年1月8日 上午8:44:02
 */
public class LoadPayStateServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private int i = 0;
	private int result = 0;//模拟0未支付，1已支付

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

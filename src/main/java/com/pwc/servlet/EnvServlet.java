package com.pwc.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class EnvServlet
 * 
 */
public class EnvServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EnvServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String env = System.getProperty("env");
		PrintWriter writer = response.getWriter();
		if(env == null || env.equalsIgnoreCase("")|| env.equalsIgnoreCase("dev")){
			writer.print("{\"env\":\"dev\"}");
		}else if(env.equalsIgnoreCase("prod")){
			writer.print("{\"env\":\"prod\"}");
		}
		response.setContentType("application/json;charset=UTF-8"); 
		
		writer.flush();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);
	}

}

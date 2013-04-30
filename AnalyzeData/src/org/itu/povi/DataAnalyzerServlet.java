package org.itu.povi;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.http.*;

import org.mortbay.log.Log;

@SuppressWarnings("serial")
public class DataAnalyzerServlet extends HttpServlet {
	private static final Logger log = Logger.getLogger(DataAnalyzerServlet.class.getName());

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/plain");
		resp.getWriter().println("Hello, browser");
		log.info("hello browser");
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/plain");
		resp.getWriter().println("Hello, Povilas");
		log.info("hello");
	}
}
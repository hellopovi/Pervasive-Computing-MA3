package org.itu.povi;

import java.io.IOException;
import javax.servlet.http.*;

@SuppressWarnings("serial")
public class AnalyzeDataServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		resp.getWriter().println("Hello, world");
		//check povi!
		// balaba
	}
}

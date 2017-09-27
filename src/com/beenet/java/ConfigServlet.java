package com.beenet.java;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import blackboard.platform.plugin.PlugInException;

@WebServlet(name = "configServlet", urlPatterns = {"/links/config"})
public final class ConfigServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final String PAGE_PATH = "/links/config.jsp";

	@Override
	protected final void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {

		final Config config = new Config(request.getParameter("imports"));
		try {
			config.save();
		} catch (final PlugInException e) {
			throw new ServletException(e);
		}

		request.setAttribute("successMessage", "Configuration saved successfully.");

		final RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(PAGE_PATH);
		dispatcher.forward(request, response);
	}
}
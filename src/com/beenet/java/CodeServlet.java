package com.beenet.java;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "codeServlet", urlPatterns = {"/links/execute"})
public final class CodeServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final String PAGE_PATH = "/links/developer.jsp";

	@Override
	protected final void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {

		final String user = new RoleValidator().getValidatedUserName();

		if (user == null) {

			request.setAttribute("errorMessage", "Sorry, you don't have permission to submit codes. Please contact your system administrator.");

		}  else {

			final String methods = request.getParameter("methods");
			final String codes = request.getParameter("codes");
			final String arguments = request.getParameter("arguments");
			final String imports = request.getParameter("imports");
			final String uuid = UUID.randomUUID().toString();
			final CodeFile codeFile = new CodeFile(uuid, methods, codes);
			codeFile.addArguments(arguments);
			codeFile.addImports(imports);

			if (codeFile.saveCode()) {
				request.setAttribute("successMessage", "Program executes successfully.");
			} else {
				request.setAttribute("errorMessage", "Error in saving codes");
			}

			request.setAttribute("link", codeFile.getLink());
			request.setAttribute("uuid", uuid);
			request.setAttribute("methods", methods);
			request.setAttribute("codes", codes);
			request.setAttribute("arguments", arguments);
			request.setAttribute("imports", imports);
		}

		final RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(PAGE_PATH);
		dispatcher.forward(request, response);
	}
}
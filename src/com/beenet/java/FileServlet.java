package com.beenet.java;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "fileServlet", urlPatterns = {"/links/download"})
public final class FileServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected final void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {

		final String user = new RoleValidator().getValidatedUserName();

		if (user != null) {

			final String methods = request.getParameter("methods");
			final String codes = request.getParameter("codes");
			final String arguments = request.getParameter("arguments");
			final String imports = request.getParameter("imports");
			final String uuid = request.getParameter("uuid");

			final CodeFile codeFile = new CodeFile(uuid, methods, codes);
			codeFile.addArguments(arguments);
			codeFile.addImports(imports);

			response.setHeader("Content-Disposition", "attachment; filename=\"MainClass.java\"");
			final OutputStream outputStream = response.getOutputStream();
			final PrintWriter writer = new PrintWriter(outputStream);
			codeFile.writerJavaFile(writer);
			writer.close();
			outputStream.flush();
		}
	}
}
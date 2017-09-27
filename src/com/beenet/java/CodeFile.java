package com.beenet.java;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import blackboard.db.BbDatabase;
import blackboard.platform.db.JdbcService;
import blackboard.platform.db.JdbcServiceFactory;

public final class CodeFile {

	private static final String BLACKBOARD_PATH = "/usr/local/blackboard";
	private static final String JSP_PATH = BLACKBOARD_PATH + "/%s/vi/%s/plugins/bnhk-java/webapp/links/%s.jsp";
	private final String jspPath;
	private final String code;
	private final String methods;
	private final String schema;
	private final String uuid;
	private final List<String> imports;
	private final List<String> arguments;

	public CodeFile(final String uuid, final String methods, final String code) {

		this.uuid = uuid;
		this.schema = getSchemaName();
		this.jspPath = String.format(JSP_PATH, getPluginFolder(), schema, uuid);
		this.code = code;
		this.methods = methods;
		this.imports = new ArrayList<>();
		this.arguments = new ArrayList<>();
	}

	private static final String getPluginFolder() {

		return new File(BLACKBOARD_PATH + "/cache/vi").exists() ? "cache" : "content";
	}

	public final boolean saveCode() {

		try (final PrintWriter writer = new PrintWriter(new FileWriter(jspPath, true))) {

			writer.println("<%@ page import=\"" + String.join(",", imports) + "\"%>");
			writeMethods(writer);
			writer.println("<%");
			writeArguments(writer);
			writer.println(code.replaceAll("System\\.out\\.println", "out.println"));
			writer.println("%>");
			writer.close();
			return true;

		} catch (final IOException e) {

			System.err.println(e);
			return false;
		}
	}

	private final void writeMethods(final PrintWriter writer) {

		if (methods != null && !methods.isEmpty()) {
			writer.println("<%!");
			writer.println("private static class MainClass {");
			writer.print(methods);
			writer.println("}");
			writer.println("%>");
		}
	}

	private final void writeArguments(final PrintWriter writer) {

		if (arguments != null && !arguments.isEmpty()) {
			final List<String> quoted = arguments.stream().map(argument -> "\"" + argument.replace("\"", "\\\"") + "\"").collect(Collectors.toList());
			writer.println("final String[] args = new String[] {" + String.join(",", quoted) + "};");
		}
	}

	public final void writerJavaFile(final PrintWriter writer) {

		imports.stream().map(line -> String.format("import %s;", line)).forEach(writer::println);

		writer.println();
		writer.println("public class MainClass {");

		if (methods != null && !methods.isEmpty()) {

			writer.println();
			Arrays.stream(methods.split("\\r?\\n")).map(line -> "\t" + line).forEach(writer::println);
			writer.println();
		}

		writer.println("\tpublic static void main(String[] args) throws Exception {");
		writer.println();
		Arrays.stream(code.split("\\r?\\n")).map(line -> "\t\t" + line).forEach(writer::println);
		writer.println("\t}");
		writer.println("}");
	}

	public final void printCodes() {

		try (final BufferedReader reader = new BufferedReader(new FileReader(jspPath))) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}
		} catch (final FileNotFoundException e) {
			System.err.println("File not found in print code");
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	public final boolean deleteCode() {

		return new File(jspPath).delete();
	}

	public final List<String> getImports() {

		return imports;
	}

	public final void addImports(final String imports) {

		if (imports != null && !imports.isEmpty()) {
			Arrays.stream(imports.split("\\s+")).forEach(this.imports::add);
		}
	}

	public final void addArguments(final String arguments) {

		if (arguments != null && !arguments.isEmpty()) {
			Arrays.stream(arguments.split("\\s+")).forEach(this.arguments::add);
		}
	}

	public final String getLink() {

		return String.format("/webapps/bnhk-java-%s/links/%s.jsp", schema, uuid);
	}

	public final String getCodePath() {

		return jspPath;
	}

	private static final String getSchemaName() {

		final JdbcService jdbcService = JdbcServiceFactory.getInstance();
		final BbDatabase database = jdbcService.getDefaultDatabase();
		return database.getSchema().getSchemaName();
	}

}
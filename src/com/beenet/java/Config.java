package com.beenet.java;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import blackboard.platform.plugin.PlugInException;
import blackboard.platform.plugin.PlugInUtil;

public final class Config {

	private static final Logger logger = Logger.getLogger(Config.class);
	private static final String CONFIG_FILE_NAME = "config.properties";
	private static final String VENDOR_ID = "bnhk";
	private static final String HANDLE_ID = "java";
	private static final String IMPORTS_PARAM = "IMPORTS";
	private static final String IMPORTS_DEFAULT = "java.util.*,blackboard.data.user.User,blackboard.data.course.Course,blackboard.persist.user.UserDbLoader,blackboard.persist.course.CourseDbLoader,blackboard.persist.Id,blackboard.data.content.Content";
	private final String imports;

	public Config(final String imports) {

		this.imports = imports;
	}

	public Config() {

		this(IMPORTS_DEFAULT);
	}

	public final String getImports() {

		return imports;
	}

	public final String getImportContents() {

		return imports.replace(',', '\n');
	}

	public final String[] getImportsAsList() {

		return imports == null || imports.isEmpty() ? new String[] {} : imports.split(",");
	}

	public static final Config load() throws PlugInException {

		final File file = getConfigFile();

		if (file.exists()) {
			try (final InputStream inputStream = new FileInputStream(file)) {
				final Properties properties = new Properties();
				properties.load(inputStream);
				return new Config(properties.getProperty(IMPORTS_PARAM));
			} catch (final IOException e) {
				logger.warn("Unable to read config file", e);
			}
		}

		persist(file, IMPORTS_DEFAULT);
		return new Config(IMPORTS_DEFAULT);
	}

	public final void save() throws PlugInException {

		persist(getConfigFile(), imports);
	}

	private static final void persist(final File file, final String imports) {

		try (final OutputStream outputStream = new FileOutputStream(file)) {
			final Properties properties = new Properties();
			properties.setProperty(IMPORTS_PARAM, imports);
			properties.store(outputStream, "Building block configuration file");
		} catch (final IOException e) {
			logger.warn("Unable to save config file", e);
		}
	}

	private static final File getConfigFile() throws PlugInException {

		final File folder = PlugInUtil.getConfigDirectory(VENDOR_ID, HANDLE_ID);
		return new File(folder, CONFIG_FILE_NAME);
	}

}
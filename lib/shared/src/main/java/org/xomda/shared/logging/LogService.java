package org.xomda.shared.logging;

import java.util.function.Function;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogService {

	private static final Level DEFAULT_LOG_LEVEL = Level.ALL;

	private static Function<Class<?>, Logger> logProvider = LoggerFactory::getLogger;

	static {
		Configurator.setRootLevel(DEFAULT_LOG_LEVEL);
	}

	public static void setLogProvider(final Function<Class<?>, Logger> provider) {
		logProvider = provider;
	}

	public static void setDefaultLogLevel(final Level level) {
		Configurator.setRootLevel(level);
	}

	public static Logger getLogger(final Class<?> clz) {
		return logProvider.apply(clz);
	}

}

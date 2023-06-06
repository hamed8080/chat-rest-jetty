package config;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.nio.charset.Charset;

public class LoggerContextConfiguration {
    public static void config(Boolean enable) {
        if (enable) {
            LoggerContext loggerContext = (LoggerContext) LogManager.getContext();
            Configuration configuration = loggerContext.getConfiguration();

            PatternLayout layout = PatternLayout.createLayout("%m%n", null, null, null, Charset.defaultCharset(), false, false, null, null);
            Appender appender = ConsoleAppender.createAppender(layout, null, null, "CONSOLE_APPENDER", null, null);
            appender.start();
            AppenderRef ref = AppenderRef.createAppenderRef("CONSOLE_APPENDER", null, null);
            AppenderRef[] refs = new AppenderRef[]{ref};
            LoggerConfig loggerConfig = LoggerConfig.createLogger("false", Level.INFO, "SDK_LOGGER", "SDK_LOGGER", refs, null, configuration, null);
            loggerConfig.addAppender(appender, null, null);

            configuration.addAppender(appender);
            configuration.addLogger("SDK_LOGGER", loggerConfig);
            loggerContext.updateLoggers(configuration);
        }
    }
}

package engine.core.logger;

import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public class Logger {
    public static final org.slf4j.Logger ENGINE = LoggerFactory.getLogger(Logger.class);

    public static final Marker INJECT = MarkerFactory.getMarker("inject");
}

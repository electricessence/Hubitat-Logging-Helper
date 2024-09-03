/* groovylint-disable IfStatementBraces, UnusedPrivateMethod */
/* groovylint-disable-next-line CompileStatic */
library(
    name: 'LoggingHelper',
    namespace: 'Electrified',
    author: 'electricessence',
    description: 'A simple logging helper library for Hubitat.',
    version: '1.0.2'
)

import groovy.transform.Field

@Field final static Map LOG_LEVELS = [
    trace: 0,
    debug: 1,
    info : 2,
    warn : 3,
    error: 4
]

@Field final static String TRACE = 'trace'
@Field final static String DEBUG = 'debug'
@Field final static String INFO = 'info'
@Field final static String WARN = 'warn'
@Field final static String ERROR = 'error'
/* groovylint-disable-next-line DuplicateStringLiteral */
@Field final static String DEFAULT_LOG_LEVEL = 'info'

private boolean isLogLevelEnabled(String level) {
    /* groovylint-disable-next-line NoDef, VariableTypeRequired */
    def currentLevel = settings?.logLevel
    if (currentLevel == null || currentLevel == '') {
        // Default to INFO if not set.
        currentLevel = DEFAULT_LOG_LEVEL
    }
    else if (!LOG_LEVELS.containsKey(currentLevel)) {
        log.warn LOG_LEVELS.containsKey(currentLevel.toLowerCase())
            ? "${device.Name}: Log level must be lower-case: ${currentLevel}. Defaulting to '${DEFAULT_LOG_LEVEL}'."
            : "${device.Name}: Unknown log level: ${currentLevel}. Defaulting to '${DEFAULT_LOG_LEVEL}'."
        currentLevel = DEFAULT_LOG_LEVEL
    }
    return LOG_LEVELS[level] >= LOG_LEVELS[currentLevel]
}

private void logTrace(String message) {
    logLevel(TRACE, message)
}

private void logDebug(String message) {
    logLevel(DEBUG, message)
}

private void logInfo(String message) {
    logLevel(INFO, message)
}

private void logWarn(String message) {
    logLevel(WARN, message)
}

private void logError(String message) {
    logLevel(ERROR, message)
}

private void logTrace(Closure message) {
    logLevel(TRACE, message)
}

private void logDebug(Closure message) {
    logLevel(DEBUG, message)
}

private void logInfo(Closure message) {
    logLevel(INFO, message)
}

private void logWarn(Closure message) {
    logLevel(WARN, message)
}

private void logError(Closure message) {
    logLevel(ERROR, message)
}

private void logLevel(String level, String message, boolean force = false) {
    if (!force && !isLogLevelEnabled(level)) return
    String msg = "${device.displayName}: ${message}"
    switch (level) {
        case TRACE:
        case DEBUG:
            log.debug msg
            break
        case INFO:
            log.info msg
            break
        case WARN:
            log.warn msg
            break
        case ERROR:
            log.error msg
            break
        default:
            log.warn "${msg} (Invalid log level: ${level})"
            break
    }
}

private void logLevel(String level, Closure message, boolean force = false) {
    if (!force && !isLogLevelEnabled(level)) return
    logLevel(level, message(), true)
}

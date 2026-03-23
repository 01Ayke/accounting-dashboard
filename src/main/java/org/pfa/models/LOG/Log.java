package org.pfa.models.LOG;

import java.awt.Desktop;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class Log {

    private static final String ENV_LOG_PATH = "PFA_LOG_PATH";
    private static final String ENV_HTML_LOG_PATH = "PFA_HTML_LOG_PATH";
    private static final String logFilePath = resolveLogFilePath();
    private static final String htmlLogFilePath = resolveHtmlLogFilePath();
    private static boolean logPathDisplayed = false;
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_CYAN = "\u001B[36m";

    private static String resolveLogFilePath() {
        String configuredPath = System.getenv(ENV_LOG_PATH);
        if (configuredPath != null && !configuredPath.isBlank()) {
            return configuredPath;
        }

        Path current = Paths.get(System.getProperty("user.dir")).toAbsolutePath();
        while (current != null) {
            if (Files.exists(current.resolve("pom.xml"))) {
                return current.resolve("LOG.txt").toString();
            }
            current = current.getParent();
        }

        return Paths.get(System.getProperty("user.dir"), "LOG.txt").toAbsolutePath().toString();
    }

    private static String resolveHtmlLogFilePath() {
        String configuredPath = System.getenv(ENV_HTML_LOG_PATH);
        if (configuredPath != null && !configuredPath.isBlank()) {
            return configuredPath;
        }

        Path textPath = Paths.get(logFilePath).toAbsolutePath();
        Path parent = textPath.getParent();
        if (parent == null) {
            return Paths.get("LOG.html").toAbsolutePath().toString();
        }
        return parent.resolve("LOG.html").toString();
    }

    static public synchronized void log(String message, Log_Type type, LOG_Location location) {
        if (!logPathDisplayed) {
            System.out.println(ANSI_CYAN + "[LOG] Using file: " + logFilePath + ANSI_RESET);
            System.out.println(ANSI_CYAN + "[LOG] Using html: " + htmlLogFilePath + ANSI_RESET);
            logPathDisplayed = true;
        }

        String line = "[" + type + "] [" + location + "] " + message;
        System.out.println(getColorForType(type) + line + ANSI_RESET);
        try {
            Path logPath = Paths.get(logFilePath).toAbsolutePath();
            if (logPath.getParent() != null) {
                Files.createDirectories(logPath.getParent());
            }
            try (FileWriter fw = new FileWriter(logPath.toFile(), true)) {
                fw.write(line + "\n");
            }

            appendHtmlLogLine(line, type);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void appendHtmlLogLine(String line, Log_Type type) throws IOException {
        Path htmlPath = Paths.get(htmlLogFilePath).toAbsolutePath();
        if (htmlPath.getParent() != null) {
            Files.createDirectories(htmlPath.getParent());
        }
        if (!Files.exists(htmlPath)) {
            Files.writeString(htmlPath, buildHtmlHeader() + buildHtmlFooter());
        }

        String content = Files.readString(htmlPath);
        String marker = buildHtmlFooter();
        String htmlLine = "  <div class=\"log-entry " + type.name().toLowerCase() + "\">" + escapeHtml(line) + "</div>\n";

        if (content.endsWith(marker)) {
            String updated = content.substring(0, content.length() - marker.length()) + htmlLine + marker;
            Files.writeString(htmlPath, updated);
        } else {
            Files.writeString(htmlPath, content + htmlLine);
        }
    }

    private static String escapeHtml(String text) {
        return text
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#39;");
    }

    private static String buildHtmlHeader() {
        return "<!doctype html>\n"
            + "<html lang=\"en\">\n"
            + "<head>\n"
            + "  <meta charset=\"utf-8\">\n"
            + "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n"
            + "  <meta http-equiv=\"refresh\" content=\"1\">\n"
            + "  <title>Application Logs</title>\n"
            + "  <style>\n"
            + "    body { font-family: Consolas, Monaco, monospace; background:#0f172a; color:#e2e8f0; margin:0; padding:20px; }\n"
            + "    h1 { margin:0 0 16px 0; font-size:20px; }\n"
            + "    .log-entry { padding:6px 10px; border-radius:6px; margin:6px 0; white-space:pre-wrap; }\n"
            + "    .log-entry.info { background:#052e16; color:#86efac; }\n"
            + "    .log-entry.warning { background:#422006; color:#fde68a; }\n"
            + "    .log-entry.error { background:#450a0a; color:#fca5a5; }\n"
            + "  </style>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <h1>Application Logs</h1>\n";
    }

    private static String buildHtmlFooter() {
        return "</body>\n</html>\n";
    }

    private static String getColorForType(Log_Type type) {
        switch (type) {
            case ERROR:
                return ANSI_RED;
            case WARNING:
                return ANSI_YELLOW;
            case INFO:
            default:
                return ANSI_GREEN;
        }
    }

    public static String getLogFilePath() {
        return logFilePath;
    }

    public static String getHtmlLogFilePath() {
        return htmlLogFilePath;
    }

    public static void openHtmlLogInBrowser() {
        try {
            Path htmlPath = Paths.get(htmlLogFilePath).toAbsolutePath();
            if (!Files.exists(htmlPath)) {
                Files.writeString(htmlPath, buildHtmlHeader() + buildHtmlFooter());
            }
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(htmlPath.toUri());
            } else {
                System.out.println("[LOG] Browser opening is not supported in this environment.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void clearLogFile() {
        try {
            Path logPath = Paths.get(logFilePath).toAbsolutePath();
            if (logPath.getParent() != null) {
                Files.createDirectories(logPath.getParent());
            }
            Files.write(logPath, new byte[0]);

            Path htmlPath = Paths.get(htmlLogFilePath).toAbsolutePath();
            if (htmlPath.getParent() != null) {
                Files.createDirectories(htmlPath.getParent());
            }
            Files.writeString(htmlPath, buildHtmlHeader() + buildHtmlFooter());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

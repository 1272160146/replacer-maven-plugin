package pers.huangguangjian.replacer_maven_plugin;

import org.apache.maven.plugin.logging.Log;

public class SummaryBuilder {

    private static final String FILE_DEBUG_FORMAT = "Replacement run on %s and writing to %s with encoding %s";
    private static final String SUMMARY_FORMAT = "Replacement run on %d file%s.";

    private int filesReplacedCount;

    private Log log;

    public void add(String inputFile, String outputFile, String encoding) {
        String encodingUsed = encoding == null ? "(default)" : encoding;
        log.debug(String.format(FILE_DEBUG_FORMAT, inputFile, outputFile, encodingUsed));
        filesReplacedCount++;
    }

    public void print() {
        log.info(String.format(SUMMARY_FORMAT, filesReplacedCount, filesReplacedCount > 1 ? "s" : ""));
    }

    public int getFilesReplacedCount() {
        return filesReplacedCount;
    }

    public void setLog(Log log) {
        this.log = log;
    }
}

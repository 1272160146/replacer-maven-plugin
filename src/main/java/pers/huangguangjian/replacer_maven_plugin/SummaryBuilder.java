package pers.huangguangjian.replacer_maven_plugin;

import org.apache.maven.plugin.logging.Log;

public class SummaryBuilder {

    private static final String FILE_DEBUG_FORMAT = "%s:Replacement run on %s and writing to %s with encoding %s";
    private static final String SUMMARY_FORMAT = "%s:Replacement run on %d file%s.";

    private int filesReplacedCount;

    public void add(String replaceFileSetId, String inputFile, String outputFile, String encoding, Log log) {
        String encodingUsed = encoding == null ? "(default)" : encoding;
        log.debug(String.format(FILE_DEBUG_FORMAT, replaceFileSetId, inputFile, outputFile, encodingUsed));
        filesReplacedCount++;
    }

    public void print(String replaceFileSetId, Log log) {
        log.info(String.format(SUMMARY_FORMAT, replaceFileSetId, filesReplacedCount, filesReplacedCount > 1 ? "s" : ""));
    }

    public int getFilesReplacedCount() {
        return filesReplacedCount;
    }
}

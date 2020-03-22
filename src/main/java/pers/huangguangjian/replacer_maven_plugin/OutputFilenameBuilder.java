package pers.huangguangjian.replacer_maven_plugin;

import pers.huangguangjian.replacer_maven_plugin.utils.FileUtils;

public class OutputFilenameBuilder {

    public OutputFilenameBuilder() {
    }

    public String buildFrom(String inputFilename, ReplaceFileSet replaceFileSet) {
        return buildOutputFile(inputFilename, replaceFileSet);
    }

    private String buildOutputFile(String inputFilename, ReplaceFileSet replaceFileSet) {
        String basedir = FileUtils.isAbsolutePath(inputFilename) ? "" : replaceFileSet.getBasedir();
        return FileUtils.createFullPath(basedir, inputFilename);
    }

}

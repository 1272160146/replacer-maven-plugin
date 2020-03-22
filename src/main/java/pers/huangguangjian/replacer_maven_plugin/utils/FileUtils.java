package pers.huangguangjian.replacer_maven_plugin.utils;

import java.io.File;
import java.io.IOException;

import static org.apache.commons.io.FileUtils.readFileToString;
import static org.apache.commons.io.FileUtils.writeStringToFile;
import static org.apache.commons.lang.StringUtils.isNotBlank;

public class FileUtils {

    public static void ensureDirStructureExists(String file) {
        File outputFile = new File(file);
        if (outputFile.getParent() == null) {
            return;
        }

        if (!outputFile.isDirectory()) {
            File parentPath = new File(outputFile.getParent());
            if (!parentPath.exists() && !parentPath.mkdirs()) {
                throw new IllegalStateException("Error creating directory: " + parentPath);
            }
        } else {
            throw new IllegalArgumentException("outputFile cannot be a directory: " + file);
        }
    }

    public static String readFile(String file, String encoding) throws IOException {
        if (encoding != null) {
            return readFileToString(new File(file), encoding);
        }
        return readFileToString(new File(file));
    }

    public static void writeToFile(String outputFile, String content, String encoding) throws IOException {
        ensureDirStructureExists(outputFile);
        if (encoding != null) {
            writeStringToFile(new File(outputFile), content, encoding);
        } else {
            writeStringToFile(new File(outputFile), content);
        }
    }

    public static String createFullPath(String... dirsAndFilename) {
        StringBuilder fullPath = new StringBuilder();
        for (int i = 0; i < dirsAndFilename.length - 1; i++) {
            if (isNotBlank(dirsAndFilename[i])) {
                fullPath.append(dirsAndFilename[i]);
                fullPath.append(File.separator);
            }
        }
        String last = dirsAndFilename[dirsAndFilename.length - 1];
        if (last != null) {
            fullPath.append(last);
        }

        return fullPath.toString();
    }

    public static boolean isAbsolutePath(String file) {
        return new File(file).isAbsolute();
    }
}

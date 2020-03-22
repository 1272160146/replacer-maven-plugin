package pers.huangguangjian.replacer_maven_plugin;

import pers.huangguangjian.replacer_maven_plugin.utils.FileUtils;

import java.io.IOException;
import java.util.List;

import static org.apache.commons.lang.StringUtils.isEmpty;

public class ReplacementProcessor {

    public ReplacementProcessor() {
    }

    public void replace(List<Replacement> replacements, String file,
                        String outputFile, String encoding) throws IOException {
        String content = FileUtils.readFile(file, encoding);
        for (Replacement replacement : replacements) {
            content = replaceContent(content, replacement);
        }

        FileUtils.writeToFile(outputFile, content, encoding);
    }

    private String replaceContent(String content, Replacement replacement) {
        if (isEmpty(replacement.getToken())) {
            throw new IllegalArgumentException("Token or token file required");
        }

        TokenReplacer replacer = new TokenReplacer();
        return replacer.replace(content, replacement);
    }
}

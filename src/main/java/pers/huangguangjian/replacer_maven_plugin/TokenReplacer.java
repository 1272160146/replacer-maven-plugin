package pers.huangguangjian.replacer_maven_plugin;

import org.apache.commons.lang.StringEscapeUtils;
import pers.huangguangjian.replacer_maven_plugin.utils.PatternFlagUtils;

import java.util.regex.Pattern;

import static org.apache.commons.lang.StringUtils.defaultString;
import static org.apache.commons.lang.StringUtils.isEmpty;

/**
 * token 替换器
 */
public class TokenReplacer {

    public String replace(String content, Replacement replacement) {
        if (replacement.isRegex()) {
            return replaceRegex(content, replacement.getToken(), replacement.getValue(), replacement.getRegexFlagValue());
        }
        return replaceNonRegex(content, replacement.getToken(), replacement.getValue());
    }

    private String replaceRegex(String content, String token, String value, int flags) {
        final Pattern compiledPattern;
        if (flags == PatternFlagUtils.NO_FLAGS) {
            compiledPattern = Pattern.compile(token);
        } else {
            compiledPattern = Pattern.compile(token, flags);
        }
        value= StringEscapeUtils.unescapeJava(defaultString(value));
        return compiledPattern.matcher(content).replaceAll(value);
    }

    private String replaceNonRegex(String content, String token, String value) {
        if (isEmpty(content)) {
            return content;
        }
        value= StringEscapeUtils.unescapeJava(defaultString(value));
        return content.replace(token, value);
    }
}

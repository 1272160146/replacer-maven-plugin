package pers.huangguangjian.replacer_maven_plugin;

import java.util.ArrayList;
import java.util.List;

public class Replacement {

    public static final String PATTERN_HANDLER_PREFIX = "[";
    public static final String PATTERN_HANDLER_SUFFIX = "]";
    public static final String REGEX_HANDLER_PREFIX = "%regex" + PATTERN_HANDLER_PREFIX;

    /**
     * 需要替换tken
     * 正则：%regex[expr]
     * 非正则：expr
     */
    private String token;

    /**
     * 需要替换的value
     */
    private String value;

    /**
     * 标准JAVA正则匹配模式集合
     * CANON_EQ
     * CASE_INSENSITIVE
     * COMMENTS
     * DOTALL
     * LITERAL
     * MULTILINE
     * UNICODE_CASE
     * UNIX_LINES
     */
    private List<String> regexFlags = new ArrayList<String>();

    /**
     * 标准JAVA正则匹配模式value 值
     */
    private int regexFlagValue;

    /**
     * 是否正则
     */
    private boolean regex = false;

    public Replacement() {

    }

    public Replacement(String token, String value, List<String> regexFlags) {
        setToken(token);
        setValue(value);
        setRegexFlags(regexFlags);
    }


    private boolean isRegexPrefixedPattern(String pattern) {
        return pattern.length() > (REGEX_HANDLER_PREFIX.length() + PATTERN_HANDLER_SUFFIX.length() + 1)
                && pattern.startsWith(REGEX_HANDLER_PREFIX) && pattern.endsWith(PATTERN_HANDLER_SUFFIX);
    }

    private String subRegexToken(String token) {
        return token.substring(REGEX_HANDLER_PREFIX.length(), token.length() - 1);
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        boolean isRegex = isRegexPrefixedPattern(token);
        if (isRegex) {
            this.token = subRegexToken(token);
        } else {
            this.token = token;
        }
        setRegex(isRegex);
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    public List<String> getRegexFlags() {
        return regexFlags;
    }

    public void setRegexFlags(List<String> regexFlags) {
        this.regexFlags = regexFlags;
    }

    public int getRegexFlagValue() {
        return regexFlagValue;
    }

    public void setRegexFlagValue(int regexFlagValue) {
        this.regexFlagValue = regexFlagValue;
    }

    public boolean isRegex() {
        return regex;
    }

    public void setRegex(boolean regex) {
        this.regex = regex;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Replacement{");
        sb.append("token='").append(token).append('\'');
        sb.append(", value='").append(value).append('\'');
        sb.append(", regexFlags=").append(regexFlags);
        sb.append(", regexFlagValue=").append(regexFlagValue);
        sb.append(", regex=").append(regex);
        sb.append('}');
        return sb.toString();
    }
}

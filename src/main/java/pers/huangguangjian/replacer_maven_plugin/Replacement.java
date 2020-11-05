package pers.huangguangjian.replacer_maven_plugin;

import org.apache.commons.lang.StringEscapeUtils;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang.StringUtils.defaultString;

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
     * 是否正则
     */
    private boolean regex = false;

    /**
     * value 是否转义
     */
    private boolean valueUnescapeJava=false;

    /**
     * token 是否转义
     */
    private boolean tokenUnescapeJava=false;


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
        if(isTokenUnescapeJava()){
            return StringEscapeUtils.unescapeJava(this.token);
        }
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
        if(isValueUnescapeJava()){
           return StringEscapeUtils.unescapeJava(this.value);
        }
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

    public boolean isRegex() {
        return regex;
    }

    public void setRegex(boolean regex) {
        this.regex = regex;
    }

    public boolean isValueUnescapeJava() {
        return valueUnescapeJava;
    }

    public void setValueUnescapeJava(boolean valueUnescapeJava) {
        this.valueUnescapeJava = valueUnescapeJava;
    }

    public boolean isTokenUnescapeJava() {
        return tokenUnescapeJava;
    }

    public void setTokenUnescapeJava(boolean tokenUnescapeJava) {
        this.tokenUnescapeJava = tokenUnescapeJava;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Replacement{");
        sb.append("token='").append(token).append('\'');
        sb.append(", value='").append(value).append('\'');
        sb.append(", regexFlags=").append(regexFlags);
        sb.append(", regex=").append(regex);
        sb.append(", valueUnescapeJava=").append(valueUnescapeJava);
        sb.append(", tokenUnescapeJava=").append(tokenUnescapeJava);
        sb.append('}');
        return sb.toString();
    }
}

package pers.huangguangjian.replacer_maven_plugin;

import org.apache.maven.plugin.logging.Log;
import pers.huangguangjian.replacer_maven_plugin.include.FileSelector;
import pers.huangguangjian.replacer_maven_plugin.utils.FileUtils;
import pers.huangguangjian.replacer_maven_plugin.utils.PatternFlagUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.PatternSyntaxException;

import static org.apache.commons.lang.StringUtils.isBlank;

/**
 * @Author: HuangGuangJian
 * @Email:1272160146@qq.com
 * @Date: Created in 2020/3/22 0:02
 * @Description:待替换文件集
 */
public class ReplaceFileSet {

    private static final String CONFIG_FORMAT = "%s config:%s";

    private final FileSelector fileSelector;
    private final OutputFilenameBuilder outputFilenameBuilder;
    private final SummaryBuilder summaryBuilder;
    private final ReplacementProcessor processor;

    /**
     * replaceFileSet 标识
     */
    private String replaceFileSetId;
    /*
        基础目录路劲 ，默认.
     */
    private String basedir = ".";

    /**
     * 替换相对于(basedir)路劲下包含的文件表达式列表
     * (*\/directory/**.properties)
     */
    private List<String> includes = new ArrayList<String>();

    /**
     * 相对于(basedir)路劲下排除的文件表达式列表
     * (*\/directory/**.properties)
     */
    private List<String> excludes = new ArrayList<String>();

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
     * 每个文件都会遍历替换
     * 即一个文件会对应多个替换项
     */
    private List<Replacement> replacements = new ArrayList<>();

    /**
     * 字符编码
     */
    private String encoding;

    /**
     * 遇到异常实惠忽略 ，默认不忽略
     * true:忽略
     * false:不忽略
     */
    private boolean ignoreErrors;

    /**
     * maven log 对象
     */
    private Log log;

    public ReplaceFileSet() {
        super();
        this.fileSelector = new FileSelector();
        this.outputFilenameBuilder = new OutputFilenameBuilder();
        this.summaryBuilder = new SummaryBuilder();
        this.processor = new ReplacementProcessor();
    }

    public void execute() throws IOException {
        //replaceFileSetId 必填
        if (getReplaceFileSetId() == null) {
            throw new RuntimeException("replaceFileSetId is not null");
        }
        initReplacements(replacements, regexFlags);

        //打印配置信息
        printConfig();

        List<String> includes = fileSelector.listIncludes(basedir, this.includes, excludes);
        for (String file : includes) {
            replaceContents(processor, replacements, file);
        }
        //打印替换统计信息
        summaryBuilder.print(getReplaceFileSetId(), log);
    }


    private void replaceContents(ReplacementProcessor processor, List<Replacement> replacements, String inputFile) throws IOException {
        String outputFileName = outputFilenameBuilder.buildFrom(inputFile, this);
        try {
            processor.replace(replacements, getBaseDirPrefixedFilename(inputFile),
                    outputFileName, encoding);
        } catch (PatternSyntaxException e) {
            log.error(e.getMessage());
            log.error(e);
            throw e;
        }
        summaryBuilder.add(getReplaceFileSetId(), getBaseDirPrefixedFilename(inputFile), outputFileName, encoding, log);
    }


    private String getBaseDirPrefixedFilename(String file) {
        if (isBlank(basedir) || FileUtils.isAbsolutePath(file)) {
            return file;
        }
        return basedir + File.separator + file;
    }

    private void initReplacements(List<Replacement> replacements, List<String> regexFlags) {
        for (Replacement replacement : replacements) {
            if (replacement.getRegexFlags().isEmpty() && !regexFlags.isEmpty()) {
                replacement.setRegexFlags(regexFlags);
            }
            replacement.setRegexFlagValue(PatternFlagUtils.buildFlags(replacement.getRegexFlags()));
        }
    }

    public void printConfig() {
        log.info(String.format(CONFIG_FORMAT, this.getReplaceFileSetId(), this.toString()));
    }

    public String getBasedir() {
        return basedir;
    }

    public void setBasedir(String basedir) {
        this.basedir = basedir;
    }

    public List<String> getIncludes() {
        return includes;
    }

    public void setIncludes(List<String> includes) {
        this.includes = includes;
    }

    public List<String> getExcludes() {
        return excludes;
    }

    public void setExcludes(List<String> excludes) {
        this.excludes = excludes;
    }

    public List<String> getRegexFlags() {
        return regexFlags;
    }

    public void setRegexFlags(List<String> regexFlags) {
        this.regexFlags = regexFlags;
    }

    public List<Replacement> getReplacements() {
        return replacements;
    }

    public void setReplacements(List<Replacement> replacements) {
        this.replacements = replacements;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public boolean isIgnoreErrors() {
        return ignoreErrors;
    }

    public void setIgnoreErrors(boolean ignoreErrors) {
        this.ignoreErrors = ignoreErrors;
    }

    public Log getLog() {
        return this.log;
    }

    public void setLog(Log log) {
        this.log = log;
    }

    public String getReplaceFileSetId() {
        return replaceFileSetId;
    }

    public void setReplaceFileSetId(String replaceFileSetId) {
        this.replaceFileSetId = replaceFileSetId;
    }

    public SummaryBuilder getSummaryBuilder() {
        return summaryBuilder;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ReplaceFileSet{");
        sb.append("replaceFileSetId='").append(replaceFileSetId).append('\'');
        sb.append(", basedir='").append(basedir).append('\'');
        sb.append(", includes=").append(includes);
        sb.append(", excludes=").append(excludes);
        sb.append(", regexFlags=").append(regexFlags);
        sb.append(", replacements=").append(replacements);
        sb.append(", encoding='").append(encoding).append('\'');
        sb.append(", ignoreErrors=").append(ignoreErrors);
        sb.append('}');
        return sb.toString();
    }

}

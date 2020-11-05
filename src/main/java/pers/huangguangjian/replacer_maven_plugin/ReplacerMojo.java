package pers.huangguangjian.replacer_maven_plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import pers.huangguangjian.replacer_maven_plugin.include.FileSelector;
import pers.huangguangjian.replacer_maven_plugin.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang.StringUtils.isBlank;


/**
 * @Author: HuangGuangJian
 * @Email:1272160146@qq.com
 * @Date: Created in 2020/3/21 23:23
 * 文件内容替换插件，支持多组文件使用不同的替换策略
 * 具体插件配置如下：
 * <per>
 * &lt;plugin&gt;
 * &lt;groupId&gt;pers.huangguangjian.replacer-maven-plugin&lt;/groupId&gt;
 * &lt;artifactId&gt;replacer-maven-plugin&lt;/artifactId&gt;
 * &lt;version&gt;1.0-SNAPSHOT&lt;/version&gt;
 * &lt;executions&gt;
 * &lt;execution&gt;
 * &lt;id&gt;1&lt;/id&gt;
 * &lt;goals&gt;
 * &lt;goal&gt;file-replacer&lt;/goal&gt;
 * &lt;/goals&gt;
 * &lt;phase&gt;compile&lt;/phase&gt;
 * &lt;/execution&gt;
 * &lt;/executions&gt;
 * &lt;configuration&gt;
 * &lt;basedir&gt;D:\dev\xxxx\src&lt;/basedir&gt; &lt;!--基础路径,，必填写--&gt;
 * &lt;includes&gt; &lt;!--相对基础路径 ，包含以下patterns文件，非填写--&gt;
 * &lt;include&gt;**\&lt;/include&gt; &lt;!--模糊匹配--&gt;
 * &lt;include&gt;con/db.properties&lt;/include&gt; &lt;!--绝对匹配--&gt;
 * &lt;include&gt;%regex[expr]&lt;/include&gt; &lt;!--正则匹配--&gt;
 * &lt;/includes&gt;
 * &lt;excludes&gt; &lt;!--相对基础路径 ，排除包含以下patterns包含文件，非填写--&gt;
 * &lt;exclude&gt;
 * &lt;/include&gt; &lt;!--模糊匹配--&gt;
 *            &lt;include&gt;con/db.properties&lt;/include&gt; &lt;!--绝对匹配--&gt;
 *            &lt;include&gt;%regex[expr]&lt;/include&gt; &lt;!--正则匹配--&gt;
 *        &lt;/includes&gt;
 *        &lt;excludes&gt; &lt;!--相对基础路径 ，排除包含以下patterns包含文件，非填写--&gt;
 *            &lt;exclude&gt;**/

/***&lt;/exclude&gt; &lt;!--模糊匹配--&gt;
 *            &lt;exclude&gt;con/db.properties&lt;/exclude&gt; &lt;!--绝对匹配--&gt;
 *            &lt;exclude&gt;%regex[expr]&lt;/exclude&gt; &lt;!--正则匹配--&gt;
 *        &lt;/excludes&gt;
 *        &lt;replacements&gt; &lt;!--替换项列表--&gt;
 *            &lt;replacement&gt;
 *                &lt;!--正则替换--&gt;
 *                &lt;token&gt;%regex[.*xxxx\d+]&lt;/token&gt;
 *                &lt;value&gt;huang&lt;/value&gt;
 *                &lt;!--标准JAVA正则,匹配模式列表,详细请看java doc--&gt;
 *                &lt;regexFlags&gt;
 *                    &lt;regexFlag&gt;CANON_EQ&lt;/regexFlag&gt;
 *                    &lt;regexFlag&gt;CASE_INSENSITIVE&lt;/regexFlag&gt;
 *                    &lt;regexFlag&gt;COMMENTS&lt;/regexFlag&gt;
 *                    &lt;regexFlag&gt;DOTALL&lt;/regexFlag&gt;
 *                    &lt;regexFlag&gt;LITERAL&lt;/regexFlag&gt;
 *                    &lt;regexFlag&gt;MULTILINE&lt;/regexFlag&gt;
 *                    &lt;regexFlag&gt;UNICODE_CASE&lt;/regexFlag&gt;
 *                    &lt;regexFlag&gt;UNIX_LINES&lt;/regexFlag&gt;
 *                &lt;/regexFlags&gt;
 *            &lt;/replacement&gt;
 *            &lt;replacement&gt;
 *                &lt;!--普通token替换--&gt;
 *                &lt;token&gt;user&lt;/token&gt;
 *                &lt;value&gt;huang&lt;/value&gt;
 *            &lt;/replacement&gt;
 *        &lt;/replacements&gt;
 *        &lt;ignoreErrors&gt;false&lt;/ignoreErrors&gt; &lt;!--插件报错时是否忽略，默认false--&gt;
 *        &lt;skip&gt;false&lt;/skip&gt; &lt;!--是否跳过插件 ，默认不跳过--&gt;
 *        &lt;encoding&gt;UTF-8&lt;/encoding&gt; &lt;!--字符集编码，默认${project.build.sourceEn--&gt;
 *    &lt;/configuration&gt;
 *&lt;/plugin&gt;
 * </pre>
 */

@Mojo(name = "file-replacer", defaultPhase = LifecyclePhase.COMPILE)
public class ReplacerMojo extends AbstractMojo {

    private static final String TOTAL_SUMMARY_FORMAT = "total:Replacement run on %d file";
    private static final String CONFIG_FORMAT = "config:%s";

    private final FileSelector fileSelector;
    private final SummaryBuilder summaryBuilder;
    private final ReplacementProcessor processor;

    /*
        基础路劲 ，默认.
     */
    @Parameter
    private String basedir = ".";

    /**
     * 相对于(basedir)文件包含表达式列表
     * (*\/directory/**.properties)
     */
    @Parameter
    private List<String> includes = new ArrayList<String>();

    /**
     * 相对于(basedir)文件排除表达式列表
     * (*\/directory/**.properties)
     */
    @Parameter
    private List<String> excludes = new ArrayList<String>();

    /**
     * token/value 替换对列表
     */
    @Parameter
    private List<Replacement> replacements = new ArrayList<>();

    /**
     * 字符编码 ，默认${project.build.sourceEncoding}
     */
    @Parameter(defaultValue = "${project.build.sourceEncoding}", required = true)
    private String encoding;

    /**
     * 是否忽略执行错误，默认不忽略
     * true:忽略
     * false:不忽略
     */
    @Parameter
    private boolean ignoreErrors = false;

    /**
     * 是否跳过
     * true:是
     * false:否
     */
    @Parameter
    private boolean skip = false;

    public ReplacerMojo() {
        this.fileSelector = new FileSelector();
        this.summaryBuilder = new SummaryBuilder();
        this.processor = new ReplacementProcessor();
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (skip) {
            getLog().warn("skipping execute as per configuration");
            return;
        }
        this.summaryBuilder.setLog(getLog());
        try {
            //打印配置信息
            printConfig();
            List<String> includes = fileSelector.listIncludes(basedir, this.includes, excludes);
            for (String file : includes) {
                replaceContents(processor, replacements, file);
            }
            //打印替换统计信息
            summaryBuilder.print();
        } catch (Exception e) {
            getLog().error(e.getMessage());
            getLog().error(e);
            if (!isIgnoreErrors()) {
                throw new MojoExecutionException(e.getMessage(), e);
            }
        }
    }

    private void replaceContents(ReplacementProcessor processor, List<Replacement> replacements, String inputFile) throws IOException {
        String outputFileName = FileUtils.createFullPath(basedir, inputFile);
        processor.replace(replacements, getBaseDirPrefixedFilename(inputFile),
                outputFileName, encoding);
        summaryBuilder.add(getBaseDirPrefixedFilename(inputFile), outputFileName, encoding);
    }

    private String getBaseDirPrefixedFilename(String file) {
        if (isBlank(basedir) || FileUtils.isAbsolutePath(file)) {
            return file;
        }
        return basedir + File.separator + file;
    }

    public void printConfig() {
        getLog().info(String.format(CONFIG_FORMAT, this.toString()));
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

    public boolean isSkip() {
        return skip;
    }

    public void setSkip(boolean skip) {
        this.skip = skip;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ReplacerMojo{");
        sb.append("basedir='").append(basedir).append('\'');
        sb.append(", includes=").append(includes);
        sb.append(", excludes=").append(excludes);
        sb.append(", replacements=").append(replacements);
        sb.append(", encoding='").append(encoding).append('\'');
        sb.append(", ignoreErrors=").append(ignoreErrors);
        sb.append(", skip=").append(skip);
        sb.append('}');
        return sb.toString();
    }
}

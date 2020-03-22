package pers.huangguangjian.replacer_maven_plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: HuangGuangJian
 * @Email:1272160146@qq.com
 * @Date: Created in 2020/3/21 23:23
 * 文件内容替换插件，支持多组文件使用不同的替换策略
 * 具体插件配置如下：
 * <pre>
 * <configuration>
 *      <replaceFileSets>
 *          <replaceFileSet>
 *              <replaceFileSetId>xxxxx</replaceFileSetId>  唯一表示，必填写
 *              <basedir>D:\dev\xxxx\src</basedir> 基础路径,，必填写
 *              <includes> 相对基础路径 ，包含以下patterns文件，非填写
 *                   <include>**\/**</include> 模糊匹配
 *                   <include>con/db.properties</include> 绝对匹配
 *                   <include>%regex[expr]</include> 正则匹配
 *              </includes>
 *              <excludes> 相对基础路径 ，排除包含以下patterns包含文件，非填写
 *                   <exclude>**\/**</exclude> 模糊匹配
 *                   <exclude>con/db.properties</exclude> 绝对匹配
 *                   <exclude>%regex[expr]</exclude> 正则匹配
 *              </excludes>
 *              <regexFlags> 非填写
 *                      标准JAVA正则,匹配模式列表,详细请看java doc
 *                     <regexFlag>CANON_EQ</regexFlag>
 *                     <regexFlag>CASE_INSENSITIVE</regexFlag>
 *                     <regexFlag>COMMENTS</regexFlag>
 *                     <regexFlag>DOTALL</regexFlag>
 *                     <regexFlag>LITERAL</regexFlag>
 *                     <regexFlag>MULTILINE</regexFlag>
 *                     <regexFlag>UNICODE_CASE</regexFlag>
 *                     <regexFlag>UNIX_LINES</regexFlag>
 *              </regexFlags>
 *              <replacements> 替换项列表
 *                  <replacement> 正则%regex[.*xxxx\d+]
 *                      <token>%regex[.*xxxx\d+]</token>
 *                      <value>huang</value>
 *                      <regexFlags>
 *                              标准JAVA正则,匹配模式列表,详细请看java doc优先使用替换项的匹配模
 *                              式 ，替换项没有就取configuration/replaceFileSet/regexFlags,如果都
 *                              没有配置 ，那就磨人
 *                             <regexFlag>CANON_EQ</regexFlag>
 *                             <regexFlag>CASE_INSENSITIVE</regexFlag>
 *                             <regexFlag>COMMENTS</regexFlag>
 *                             <regexFlag>DOTALL</regexFlag>
 *                             <regexFlag>LITERAL</regexFlag>
 *                             <regexFlag>MULTILINE</regexFlag>
 *                             <regexFlag>UNICODE_CASE</regexFlag>
 *                             <regexFlag>UNIX_LINES</regexFlag>
 *                      </regexFlags>
 *                  </replacement>
 *                  <replacement> 普通token替换
 *                      <token>user</token>
 *                      <value>huang</value>
 *                  </replacement>
 *              </replacements>
 *          </replaceFileSet>
 *      </replaceFileSets>
 *      <ignoreErrors>false</ignoreErrors> -- 插件报错时是否忽略，默认false
 *      <encoding>UTF-8</encoding> 字符集编码，默认${project.build.sourceEncoding}
 * </configuration>
 * </pre>
 */
@Mojo(name = "file-replacer", defaultPhase = LifecyclePhase.COMPILE)
public class ReplacerMojo extends AbstractMojo {

    private static final String TOTAL_SUMMARY_FORMAT = "total:Replacement run on %d file";

    /**
     * 待替换的文件集列表
     */
    @Parameter
    List<ReplaceFileSet> replaceFileSets = new ArrayList<>();

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

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        //替换文件数量
        int filesReplacedCount = 0;
        try {
            for (ReplaceFileSet replaceFileSet : replaceFileSets) {
                replaceFileSet.setEncoding(encoding);
                replaceFileSet.setIgnoreErrors(ignoreErrors);
                replaceFileSet.setLog(getLog());
                replaceFileSet.execute();
                //获取文件集替换的文件数量
                filesReplacedCount += replaceFileSet.getSummaryBuilder().getFilesReplacedCount();
            }
        } catch (Exception e) {
            getLog().error(e.getMessage());
            getLog().error(e);
            if (!isIgnoreErrors()) {
                throw new MojoExecutionException(e.getMessage(), e);
            }
        } finally {
            getLog().info(String.format(TOTAL_SUMMARY_FORMAT, filesReplacedCount));
        }
    }

    public List<ReplaceFileSet> getReplaceFileSets() {
        return replaceFileSets;
    }

    public void setReplaceFileSets(List<ReplaceFileSet> replaceFileSets) {
        this.replaceFileSets = replaceFileSets;
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

}

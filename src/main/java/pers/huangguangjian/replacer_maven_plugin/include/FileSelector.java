package pers.huangguangjian.replacer_maven_plugin.include;

import org.codehaus.plexus.util.DirectoryScanner;

import java.io.File;
import java.util.Arrays;
import java.util.List;
/**
 * @Author: HuangGuangJian
 * @Email:1272160146@qq.com
 * @Date: Created in 2020/3/23 09:10
 * @Description:
 */
public class FileSelector {

    public List<String> listIncludes(String basedir, List<String> includes, List<String> excludes) {

        DirectoryScanner directoryScanner = new DirectoryScanner();
        directoryScanner.addDefaultExcludes();
        directoryScanner.setBasedir(new File(basedir));
        if (!includes.isEmpty()) {
            directoryScanner.setIncludes(includes.toArray(new String[]{}));
        }
        if (!excludes.isEmpty()) {
            directoryScanner.setExcludes(excludes.toArray(new String[]{}));
        }
        //不忽略大小写
        directoryScanner.setCaseSensitive(false);
        directoryScanner.scan();
        return Arrays.asList(directoryScanner.getIncludedFiles());
    }

}

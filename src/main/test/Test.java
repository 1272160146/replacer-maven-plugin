import org.apache.commons.lang.StringEscapeUtils;
import pers.huangguangjian.replacer_maven_plugin.utils.PatternFlagUtils;

import java.util.regex.Pattern;

import static org.apache.commons.lang.StringUtils.defaultString;

/**
 * @Author: HuangGuangJian
 * @Description:
 * @Date: Created in 2020/05/08 15:00
 * @Modified By:
 */
public class Test {
    public static void main(String[] args) {
        String token="`?\\s*?\\b(idm)\\b\\s*?`?\\s*?\\.\\s*?`?\\s*?\\b(\\w+)\\b\\s*?`?";
        String value= StringEscapeUtils.unescapeJava("\\s$1.$2\\u0020\\u0020\\u0020\\u0020");
        Pattern compiledPattern = Pattern.compile(token);

        String replaceAll = compiledPattern.matcher("idm.xxx ad").replaceAll(defaultString(value));
        System.out.println(replaceAll);
        System.out.println(" ".equals("\u0020"));
        System.out.println("\u0008");
        System.out.println("idm.xxx idm".replace("idm","idm1"));
    }
}

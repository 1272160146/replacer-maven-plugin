# replacer-maven-plugin
文件内容替换插件，支持多组文件使用不同的替换策略
# 配置方式
    <plugin>
        <groupId>pers.huangguangjian.replacer-maven-plugin</groupId>
        <artifactId>replacer-maven-plugin</artifactId>
        <version>1.0-SNAPSHOT</version>
        <executions>
            <execution>
                <phase>compile</phase>
                <goals>
                    <goal>file-replacer</goal>
                </goals>
            </execution>
        </executions>
        <configuration>
            <replaceFileSets>
                <replaceFileSet>
                    <replaceFileSetId>xxxxx</replaceFileSetId>  <!--唯一表示，必填写-->
                    <basedir>D:\dev\xxxx\src</basedir> <!--基础路径,，必填写-->
                    <includes> <!--相对基础路径 ，包含以下patterns文件，非填写-->
                        <include>**\/**</include><!-- 模糊匹配-->
                        <include>con/db.properties</include> <!--绝对匹配-->
                        <include>%regex[expr]</include> <!--正则匹配-->
                    </includes>
                    <excludes> <!--相对基础路径 ，排除包含以下patterns包含文件，非填写-->
                        <exclude>**\/**</exclude> <!--模糊匹配-->
                        <exclude>con/db.properties</exclude> <!--绝对匹配-->
                        <exclude>%regex[expr]</exclude> <!--正则匹配-->
                    </excludes>
                    <regexFlags> <!--非填写
                             标准JAVA正则,匹配模式列表,详细请看java doc-->
                        <regexFlag>CANON_EQ</regexFlag>
                        <regexFlag>CASE_INSENSITIVE</regexFlag>
                        <regexFlag>COMMENTS</regexFlag>
                        <regexFlag>DOTALL</regexFlag>
                        <regexFlag>LITERAL</regexFlag>
                        <regexFlag>MULTILINE</regexFlag>
                        <regexFlag>UNICODE_CASE</regexFlag>
                        <regexFlag>UNIX_LINES</regexFlag>
                    </regexFlags>
                    <replacements> <!--替换项列表-->
                        <replacement><!-- 正则%regex[.*xxxx\d+]-->
                            <token>%regex[.*xxxx\d+]</token>
                            <value>huang</value>
                            <regexFlags>
                                <!--标准JAVA正则,匹配模式列表,详细请看java doc优先使用替换项的匹配模
                                式 ，替换项没有就取configuration/replaceFileSet/regexFlags,如果都
                                没有配置 ，那就默认-->
                                <regexFlag>CANON_EQ</regexFlag>
                                <regexFlag>CASE_INSENSITIVE</regexFlag>
                                <regexFlag>COMMENTS</regexFlag>
                                <regexFlag>DOTALL</regexFlag>
                                <regexFlag>LITERAL</regexFlag>
                                <regexFlag>MULTILINE</regexFlag>
                                <regexFlag>UNICODE_CASE</regexFlag>
                                <regexFlag>UNIX_LINES</regexFlag>
                            </regexFlags>
                        </replacement>
                        <replacement> <!--普通token替换-->
                            <token>user</token>
                            <value>huang</value>
                            *
                        </replacement>
                    </replacements>
                </replaceFileSet>
            </replaceFileSets>
            <ignoreErrors>false</ignoreErrors> <!-- 插件报错时是否忽略，默认false-->
            <encoding>UTF-8</encoding> <!--字符集编码，默认${project.build.sourceEncoding}-->
        </configuration>
    </plugin>

package com.github.hcsp.regex;

public class LogProcessor {
    // 传入日志字符串，将每行开头的时间戳删除
    // 返回删除时间戳后的字符串
    // 例如，输入字符串：
    //
    // [2019-08-01 21:24:41] bt3102 (11m:21s)
    // [2019-08-01 21:24:42] TeamCity server version is 2019.1.1 (build 66192)
    // [2019-08-01 21:24:43] Collecting changes in 2 VCS roots (22s)
    //
    // 返回结果：
    //
    // bt3102 (11m:21s)
    // TeamCity server version is 2019.1.1 (build 66192)
    // Collecting changes in 2 VCS roots (22s)
    private static final String REGEX = "(?m)^\\[.*?\\]";

    /*
     * 1、.*是贪婪匹配，第1个出现"["的地方，和最后一个出现"]"的地方
     * 2、.*?是惰性匹配，第1个出现"["的地方，和跟着最近的出现"]"的地方
     *
     * 3、"(?m)^ +" 的意思是匹配以1个多多个空格开始字符
     * 4、(?m) 在这种模式下，'^'和'$'分别匹配一行的开始和结束。
     *   此外，'^'仍然匹配字符串的开始，'$'也匹配字符串的结束。
     *   默认情况下，这两个表达式仅仅匹配字符串的开始和结束。
     */

    public static String process(String log) {
        return log.replaceAll(REGEX, "");
    }

    public static void main(String[] args) {
        String str =
                "[2019-08-01 21:24:41] bt3102 (11m:21s)\n"
                        + "[2019-08-01 21:24:42] TeamCity server version is 2019.1.1 (build 66192)\n"
                        + "[2019-08-01 21:24:43] Collecting changes in 2 VCS roots (22s)\n";

        System.out.println(process(str));
    }
}

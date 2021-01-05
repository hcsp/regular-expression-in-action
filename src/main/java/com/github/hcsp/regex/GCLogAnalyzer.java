package com.github.hcsp.regex;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GCLogAnalyzer {
    // 在本项目的根目录下有一个gc.log文件，是JVM的GC日志
    // 请从中提取GC活动的信息，每行提取出一个GCActivity对象
    //
    // 2019-08-21T07:48:17.401+0200: 2.924: [GC (Allocation Failure) [PSYoungGen:
    // 393216K->6384K(458752K)] 416282K->29459K(1507328K), 0.0051622 secs] [Times: user=0.02
    // sys=0.00, real=0.01 secs]
    // 例如，对于上面这行GC日志，
    // [PSYoungGen: 393216K->6384K(458752K)] 代表JVM的年轻代总内存为458752，经过GC后已用内存从393216下降到了6384
    // 416282K->29459K(1507328K) 代表JVM总堆内存1507328，经过GC后已用内存从416282下降到了29459
    // user=0.02 sys=0.00, real=0.01 分别代表用户态消耗的时间、系统调用消耗的时间和物理世界真实流逝的时间
    // 请将这些信息解析成一个GCActivity类的实例
    // 如果某行中不包含这些数据，请直接忽略该行

    private static final String GCACTIVITY_REGEX = "\\[PSYoungGen:\\s" +
            "(?<youngGenBefore>\\d+)K->(?<youngGenAfter>\\d+)K\\((?<youngGenTotal>\\d+)K\\)" +
            "\\]\\s" +
            "(\\[ParOldGen:.*\\)]\\s)?" +
            "(?<heapBefore>\\d+)K->(?<heapAfter>\\d+)K\\((?<heapTotal>\\d+)K\\)" +
            ",\\s" +
            "(\\[Metaspace:.*\\)]\\,\\s)?" +
            "\\d+(\\.\\d+)?" +
            "\\ssecs\\]\\s\\[Times:\\suser=" +
            "(?<user>\\d+(\\.\\d+)?)" +
            "\\ssys=" +
            "(?<sys>\\d+(\\.\\d+)?)" +
            ",\\sreal=" +
            "(?<real>\\d+(\\.\\d+)?)";
    private static final Pattern GCACTIVITY_PATTERN = Pattern.compile(GCACTIVITY_REGEX);

    public static List<GCActivity> parse(File gcLog) {
        List<GCActivity> list = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(gcLog.toPath());
            for (String line : lines) {
                GCActivity activity = lineToActivity(line);
                if (activity != null) {
                    list.add(activity);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

    private static GCActivity lineToActivity(String line) {
        GCActivity gcActivity = null;
        Matcher matcher = GCACTIVITY_PATTERN.matcher(line);
        while (matcher.find()) {
            int youngGenBefore = new Integer(matcher.group("youngGenBefore"));
            int youngGenAfter = new Integer(matcher.group("youngGenAfter"));
            int youngGenTotal = new Integer(matcher.group("youngGenTotal"));
            int heapBefore = new Integer(matcher.group("heapBefore"));
            int heapAfter = new Integer(matcher.group("heapAfter"));
            int heapTotal = new Integer(matcher.group("heapTotal"));
            double user = new Double(matcher.group("user"));
            double sys = new Double(matcher.group("sys"));
            double real = new Double(matcher.group("real"));
            gcActivity = new GCActivity(youngGenBefore, youngGenAfter, youngGenTotal, heapBefore, heapAfter, heapTotal, user, sys, real);
        }
        return gcActivity;
    }


    public static void main(String[] args) {
        String line = "2019-08-21T07:48:16.617+0200: 2.140: [Full GC (Metadata GC Threshold) [PSYoungGen: 16486K->0K(458752K)] [ParOldGen: 19458K->23066K(1048576K)] 35944K->23066K(1507328K), [Metaspace: 34726K->34723K(1079296K)], 0.0324194 secs] [Times: user=0.09 sys=0.00, real=0.03 secs]";

        System.out.println(GCACTIVITY_REGEX);
        System.out.println(GCACTIVITY_PATTERN.matcher(line).find());
//        List<GCActivity> activities = parse(new File("gc.log"));
//        activities.forEach(System.out::println);
    }

    public static class GCActivity {
        // 年轻代GC前内存占用，单位K
        int youngGenBefore;
        // 年轻代GC后内存占用，单位K
        int youngGenAfter;
        // 年轻代总内存，单位K
        int youngGenTotal;
        // JVM堆GC前内存占用，单位K
        int heapBefore;
        // JVM堆GC后内存占用，单位K
        int heapAfter;
        // JVM堆总内存，单位K
        int heapTotal;
        // 用户态时间
        double user;
        // 系统调用消耗时间
        double sys;
        // 物理世界流逝的时间
        double real;

        public GCActivity(
                int youngGenBefore,
                int youngGenAfter,
                int youngGenTotal,
                int heapBefore,
                int heapAfter,
                int heapTotal,
                double user,
                double sys,
                double real) {
            this.youngGenBefore = youngGenBefore;
            this.youngGenAfter = youngGenAfter;
            this.youngGenTotal = youngGenTotal;
            this.heapBefore = heapBefore;
            this.heapAfter = heapAfter;
            this.heapTotal = heapTotal;
            this.user = user;
            this.sys = sys;
            this.real = real;
        }

        @Override
        public String toString() {
            return "GCActivity{"
                    + "youngGenBefore="
                    + youngGenBefore
                    + ", youngGenAfter="
                    + youngGenAfter
                    + ", youngGenTotal="
                    + youngGenTotal
                    + ", heapBefore="
                    + heapBefore
                    + ", heapAfter="
                    + heapAfter
                    + ", heapTotal="
                    + heapTotal
                    + ", user="
                    + user
                    + ", sys="
                    + sys
                    + ", real="
                    + real
                    + '}';
        }

        public int getYoungGenBefore() {
            return youngGenBefore;
        }

        public int getYoungGenAfter() {
            return youngGenAfter;
        }

        public int getYoungGenTotal() {
            return youngGenTotal;
        }

        public int getHeapBefore() {
            return heapBefore;
        }

        public int getHeapAfter() {
            return heapAfter;
        }

        public int getHeapTotal() {
            return heapTotal;
        }

        public double getUser() {
            return user;
        }

        public double getSys() {
            return sys;
        }

        public double getReal() {
            return real;
        }
    }
}

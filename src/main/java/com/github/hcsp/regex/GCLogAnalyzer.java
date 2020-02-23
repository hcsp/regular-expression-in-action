package com.github.hcsp.regex;


import java.io.*;
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
    public static List<GCActivity> parse(File gcLog) throws Exception {
        byte[] bytes = Files.readAllBytes(gcLog.toPath());
        String content = new String(bytes);
//        content = "2019-08-21T07:48:15.631+0200: 1.155: [Full GC (Metadata GC Threshold) [PSYoungGen: 20213K->0K(458752K)] [ParOldGen: 16K->19450K(1048576K)] 20229K->19450K(1507328K), [Metaspace: 20743K->20743K(1067008K)], 0.0461462 secs] [Times: user=0.16 sys=0.01, real=0.05 secs]\n";
//        content = "2019-08-21T07:48:19.378+0200: 4.902: [GC (GCLocker Initiated GC) [PSYoungGen: 488720K->12496K(503808K)] 511810K->35594K(1552384K), 0.0070491 secs] [Times: user=0.02 sys=0.00, real=0.01 secs]";

        return parse(content);
    }

    private static List<GCActivity> parse(String content)throws Exception{
        // 命名捕获组的语法格式：(?<自定义名>expr)
        String pattern = String.join("", new String[]{
                "\\[PSYoungGen: (?<youngGCBefore>\\d+)K->(?<youngGCAfter>\\d+)K\\((?<youngGenTotal>\\d+)K\\)\\]",
                "( \\[.*\\])?", //其他gc信息
                " (?<heapBefore>\\d+)K->(?<heapAfter>\\d+)K\\((?<heapTotal>\\d+)K\\)",
                "(, \\[.*\\])?",  //其他gc信息
                ", \\d+\\.\\d+ secs\\]",
                " \\[Times: user=(?<user>\\d+\\.\\d+) sys=(?<sys>\\d+\\.\\d+), real=(?<real>\\d+\\.\\d+) secs\\]"

        });

        Matcher matcher = Pattern.compile(pattern, Pattern.MULTILINE).matcher(content);
        List<GCActivity> list = new ArrayList<>();
        while (matcher.find()){


            int youngGCBefore = Integer.parseInt(matcher.group("youngGCBefore") == null ? "0" : matcher.group("youngGCBefore"));
            int youngGCAfter = Integer.parseInt(matcher.group("youngGCAfter") == null ? "0" : matcher.group("youngGCAfter"));
            int youngGenTotal = Integer.parseInt(matcher.group("youngGenTotal") == null ? "0" : matcher.group("youngGenTotal"));
            int heapBefore = Integer.parseInt(matcher.group("heapBefore") == null ? "0" : matcher.group("heapBefore"));
            int heapAfter = Integer.parseInt(matcher.group("heapAfter") == null ? "0" : matcher.group("heapAfter"));
            int heapTotal = Integer.parseInt(matcher.group("heapTotal") == null ? "0" : matcher.group("heapTotal"));
            double user = Double.parseDouble(matcher.group("user") == null ? "0.0" : matcher.group("user"));
            double sys = Double.parseDouble(matcher.group("sys") == null ? "0.0" : matcher.group("sys"));
            double real = Double.parseDouble(matcher.group("real") == null ? "0.0" : matcher.group("real"));

            GCActivity gcActivity = new GCActivity(youngGCBefore, youngGCAfter, youngGenTotal, heapBefore, heapAfter, heapTotal, user, sys, real);
            list.add(gcActivity);
        }

        return list;
    }

    public static void main(String[] args) throws Exception {
        List<GCActivity> activities = parse(new File("gc.log"));
        activities.forEach(System.out::println);
        System.out.println(activities.size());
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

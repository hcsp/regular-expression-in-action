package com.github.hcsp.regex;

import java.io.File;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GCLogAnalyzerTest {
    @Test
    public void test() throws Exception {
        List<GCLogAnalyzer.GCActivity> activities = GCLogAnalyzer.parse(new File("gc.log"));
        Assertions.assertEquals(203, activities.size());
        Assertions.assertEquals(163389, activities.get(2).getYoungGenBefore());
        Assertions.assertEquals(16486, activities.get(2).getYoungGenAfter());
        Assertions.assertEquals(458752, activities.get(2).getYoungGenTotal());

        Assertions.assertEquals(16486, activities.get(3).getYoungGenBefore());
        Assertions.assertEquals(0, activities.get(3).getYoungGenAfter());
        Assertions.assertEquals(458752, activities.get(3).getYoungGenTotal());

        Assertions.assertEquals(650575, activities.get(201).getHeapBefore());
        Assertions.assertEquals(272660, activities.get(201).getHeapAfter());
        Assertions.assertEquals(1505792, activities.get(201).getHeapTotal());

        Assertions.assertEquals(Double.parseDouble("0.08"), activities.get(202).getUser());
        Assertions.assertEquals(Double.parseDouble("0.00"), activities.get(202).getSys());
        Assertions.assertEquals(Double.parseDouble("0.03"), activities.get(202).getReal());
    }
}

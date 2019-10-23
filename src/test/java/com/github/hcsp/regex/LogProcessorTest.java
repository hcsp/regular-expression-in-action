package com.github.hcsp.regex;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LogProcessorTest {
    @Test
    public void test() {
        String str =
                "[2019-12-01 01:24:41] bt3102 (11m:21s)\n"
                        + "[2019-01-01 00:00:00] TeamCity server version is 2019.1.1 [2019-01-01 00:00:00] (build 66192)\n"
                        + "[2019-01-31 23:59:59] Collecting changes in 2 VCS roots (22s)\n";

        String[] result = LogProcessor.process(str).split("\\n");
        Assertions.assertEquals("bt3102 (11m:21s)", result[0].trim());
        Assertions.assertEquals(
                "TeamCity server version is 2019.1.1 [2019-01-01 00:00:00] (build 66192)",
                result[1].trim());
        Assertions.assertEquals("Collecting changes in 2 VCS roots (22s)", result[2].trim());
    }
}

package com.github.hcsp.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneNumberMatcher {
    // 请编写一个函数，判断一个字符串是不是合法的固定电话号码
    // 合法的固定电话号码为：区号-号码
    // 其中区号以0开头，三位或者四位
    // 号码以非零开头，七位或者八位
    // 三位区号后面只能跟八位电话号码
    // 合法的电话号码示例：
    // 021-12345678
    // 0571-12345678
    // 0373-1234567
    // 不合法的电话号码示例：
    // 02134-1234 位数不对
    // 123-45678901 区号必须以0开头
    // 021-1234567 三位区号后面只能跟八位电话号码
    public static boolean isPhoneNumber(String str) {
        Pattern pattern = Pattern.compile("^[0](\\d{2}-[1-9]{8}|\\d{3}-[1-9]{7,8})");
        Matcher matcher = pattern.matcher(str);
        return matcher.find();
    }
}

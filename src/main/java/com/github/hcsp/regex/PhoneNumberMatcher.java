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
    private static final String Regex = "0(\\d{2,3})-[1-9](\\d{6,7})";
    private static final Pattern pattern= Pattern.compile(Regex);
    public static boolean isPhoneNumber(String str) {
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()){
            if ((matcher.group(1).length() == 2) && matcher.group(2).length() == 7){
                return true;
            }
            if ((matcher.group(1).length() == 3)){
                return true;
            }
        }
        return false;
    }


    public static void main(String[] args) {
        System.out.println(isPhoneNumber("0575-84502563"));
    }
}

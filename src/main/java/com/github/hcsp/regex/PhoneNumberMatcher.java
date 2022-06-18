package com.github.hcsp.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneNumberMatcher {

    public static void main(String[] args) {
        System.out.println(isPhoneNumber("021-12345678"));
        System.out.println(isPhoneNumber("0571-12345678"));
        System.out.println(isPhoneNumber("0373-1234567"));
        System.out.println(isPhoneNumber(" 02134-1234"));
        System.out.println(isPhoneNumber("123-45678901"));
        System.out.println(isPhoneNumber("021-1234567"));
    }

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
        // 创建 Pattern 对象
        Pattern r = Pattern.compile("^(0\\d{2,3})-([^0]\\d{6,7})$");

        // 现在创建 matcher 对象
        Matcher m = r.matcher(str);
        if (m.find()) {
            System.out.println("Found value: " + m.group(0));
            System.out.println("Found value: " + m.group(1));
            if (m.group(1).length() == 3) {
                return m.group(2).length() == 8;
            } else {
                return true;
            }
        } else {
            return false;
        }

    }
}

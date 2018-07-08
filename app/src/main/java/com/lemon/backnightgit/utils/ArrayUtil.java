package com.lemon.backnightgit.utils;

import java.util.Arrays;

/**
 * @author lemon
 * @version v1.0
 * @function <描述功能>
 * @date: 2017/9/15 15:54
 */

public class ArrayUtil {
    public static <T> T[] concat(T[] first, T[] second) {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }
}
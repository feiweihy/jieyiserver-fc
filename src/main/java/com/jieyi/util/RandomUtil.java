package com.jieyi.util;

import java.util.Random;

public class RandomUtil {

    /**
     * 产生16位随机数
     *
     * @return
     */
    public static String random16() {
        Random random = new Random(); // 使用默认System.currentTimeMillis()作为种子

        long value = Math.abs(random.nextLong()) % 10000000000000000l;
        String randomStr = NSUtil.addRightZero(value + "", 16);
        return randomStr;
    }

    /**
     * 获取随机数
     *
     * @param length
     * @return
     */
    public static String getRandom(int length) {
        int temp = 0;
        int min = (int) Math.pow(10, length - 1);
        int max = (int) Math.pow(10, length);
        Random rand = new Random();

        while (true) {
            temp = rand.nextInt(max);
            if (temp >= min)
                break;
        }
        return temp + "";
    }
}

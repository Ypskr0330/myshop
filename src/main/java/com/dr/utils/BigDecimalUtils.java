package com.dr.utils;

import java.math.BigDecimal;

/**
 * bigDecimal运算工具类
 * */
public class BigDecimalUtils {

    /**
     * 加法运算
     * */
    public static BigDecimal add(double d1,double d2) {
        BigDecimal b1 = new BigDecimal(String.valueOf(d1));
        BigDecimal b2 = new BigDecimal(String.valueOf(d2));
        return b1.add(b2);
    }
    /**
     * 乘法运算
     * */
    public static BigDecimal mul(double d1,double d2) {
        BigDecimal b1 = new BigDecimal(String.valueOf(d1));
        BigDecimal b2 = new BigDecimal(String.valueOf(d2));
        return b1.multiply(b2);
    }

    /**
     * 减法运算
     * */
    public static BigDecimal sub(double d1,double d2){
        BigDecimal b1 = new BigDecimal(String.valueOf(d1));
        BigDecimal b2 = new BigDecimal(String.valueOf(d2));
        return b1.subtract(b2);
    }

    /**
     * 除法运算
     * */
    public static BigDecimal div(double d1,double d2) {
        BigDecimal b1 = new BigDecimal(String.valueOf(d1));
        BigDecimal b2 = new BigDecimal(String.valueOf(d2));
        return b1.divide(b2,2,BigDecimal.ROUND_HALF_UP);
    }

}

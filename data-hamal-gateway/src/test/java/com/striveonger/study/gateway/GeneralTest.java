package com.striveonger.study.gateway;

import org.junit.jupiter.api.Test;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-02-24 11:11
 */
public class GeneralTest {


    @Test
    public void test() {
        Integer x = Integer.MIN_VALUE, y = Integer.MAX_VALUE;
        System.out.println(x - 1 == y);
        System.out.println(x + 1 == y + 2);
        System.out.println(Integer.valueOf(x + 1).equals(y + 2));
        System.out.println(7 * 24 * 3600);
    }

}

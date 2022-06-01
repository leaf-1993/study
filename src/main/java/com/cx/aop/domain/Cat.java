package com.cx.aop.domain;

import com.cx.aop.domain.Animal;

/**
 * @author chenxiang
 * @create 2022-04-17 22:24
 */
public class Cat implements Animal {
    @Override
    public void eat() {
        System.out.println("猫吃猫粮");
    }
}

package com.cx.aop.solution.intercept;

import java.lang.reflect.InvocationTargetException;

/**
 * @author chenxiang
 * @create 2022-04-18 20:06
 */
public interface MyMethodInterceptor {

    /**
     * 方法拦截器接口，增强逻辑
     */
    Object invoke(MyMethodInvocation methodInvocation) throws InvocationTargetException, IllegalAccessException;

}

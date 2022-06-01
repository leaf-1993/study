package com.cx.aop.solution.intercept;

import java.lang.reflect.InvocationTargetException;

/**
 * @author chenxiang
 * @create 2022-04-18 20:05
 */
public interface MyMethodInvocation {

    /**
     * 驱动拦截器链，执行增强逻辑以及被代理方法调用
     * @return
     */
    Object proceed() throws InvocationTargetException, IllegalAccessException;


}

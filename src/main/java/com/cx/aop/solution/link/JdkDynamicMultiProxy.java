package com.cx.aop.solution.link;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author chenxiang
 * @create 2022-04-17 23:08
 * 多重增强
 */
public class JdkDynamicMultiProxy implements InvocationHandler {

    private Object target;

    private AbstractHandler headHandler;

    public JdkDynamicMultiProxy(Object target, AbstractHandler headHandler) {
        this.target = target;
        this.headHandler = headHandler;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        TargetMethod targetMethod = new TargetMethod(method, target, args);
        return headHandler.proceed(targetMethod);
    }

    public Object getProxy() {
        return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), this);
    }

}

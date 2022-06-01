package com.cx.aop.solution.normal;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author chenxiang
 * @create 2022-04-17 22:19
 */
public class JdkDynamicProxy implements InvocationHandler {

    /**
     * 被代理对象
     */
    private Object target;

    public JdkDynamicProxy(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("开始时间：" + System.currentTimeMillis());
        Object result = method.invoke(target, args);
        System.out.println("结束时间：" + System.currentTimeMillis());
        return result;
    }

    /**
     * @return 代理后的对象
     */
    public Object getProxy() {
        // 参数1：类加载器
        // 参数2：代理类需要实现的接口集合
        // 参数3：代理需要依赖InvocationHandler处理
        return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), this);
    }
}

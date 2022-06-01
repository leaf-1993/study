package com.cx.aop.solution.intercept;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chenxiang
 * @create 2022-04-17 22:19
 */
public class JdkDynamicProxy implements InvocationHandler {

    /**
     * 被代理对象
     */
    private Object target;

    private List<MyMethodInterceptor> interceptorList = new ArrayList<>();

    public JdkDynamicProxy(Object target) {
        this.target = target;
    }

    public void addInterceptor(MyMethodInterceptor interceptor){
        interceptorList.add(interceptor);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        TargetMethod targetMethod = new TargetMethod(method, target, args);
        MyMethodInvocationImpl invocation = new MyMethodInvocationImpl();
        invocation.setTargetMethod(targetMethod);
        invocation.setInterceptorList(interceptorList);
        return invocation.proceed();
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

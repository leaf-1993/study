package com.cx.aop.solution.intercept;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @author chenxiang
 * @create 2022-04-18 20:09
 */
public class MyMethodInvocationImpl implements MyMethodInvocation {

    private TargetMethod targetMethod;

    private List<MyMethodInterceptor> interceptorList;

    private int index;

    @Override
    public Object proceed() throws InvocationTargetException, IllegalAccessException {
        if (index == interceptorList.size()) {
            return targetMethod.invoke();
        }
        MyMethodInterceptor interceptor = interceptorList.get(index++);
        return interceptor.invoke(this);
    }

    public void setInterceptorList(List<MyMethodInterceptor> interceptorList) {
        this.interceptorList = interceptorList;
    }

    public void setTargetMethod(TargetMethod targetMethod){
        this.targetMethod = targetMethod;
    }
}

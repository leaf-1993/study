package com.cx.aop.solution.link;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author chenxiang
 * @create 2022-04-18 17:59
 */

public class TargetMethod {
    /**
     * 方法
     */
    private Method method;

    /**
     * 目标对象
     */
    private Object target;

    /**
     * 方法参数
     */
    private Object[] args;

    public TargetMethod(Method method, Object target, Object[] args) {
        this.method = method;
        this.target = target;
        this.args = args;
    }

    public Object invoke() throws InvocationTargetException, IllegalAccessException {
        return method.invoke(target, args);
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }
}

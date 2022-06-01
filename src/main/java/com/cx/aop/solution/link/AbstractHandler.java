package com.cx.aop.solution.link;

import java.lang.reflect.InvocationTargetException;

/**
 * @author chenxiang
 * @create 2022-04-18 17:58
 */
public abstract class AbstractHandler {

    private AbstractHandler nextHandler;

    protected abstract Object invoke(TargetMethod targetMethod) throws InvocationTargetException, IllegalAccessException;

    public Object proceed(TargetMethod targetMethod) throws InvocationTargetException, IllegalAccessException {
        if (!hasNext()) {
            return targetMethod.invoke();
        }
        return nextHandler.invoke(targetMethod);
    }

    public void setNextHandler(AbstractHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    public boolean hasNext() {
        return nextHandler != null;
    }

    public static class HeadHandler extends AbstractHandler{

        @Override
        protected Object invoke(TargetMethod targetMethod) {
            return null;
        }
    }
}

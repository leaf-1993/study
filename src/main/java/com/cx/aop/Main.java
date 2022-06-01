package com.cx.aop;

import com.cx.aop.domain.Animal;
import com.cx.aop.domain.Cat;
import com.cx.aop.solution.link.AbstractHandler;
import com.cx.aop.solution.link.JdkDynamicMultiProxy;
import com.cx.aop.solution.link.TargetMethod;

import java.lang.reflect.InvocationTargetException;

/**
 * @author chenxiang
 * @create 2022-04-17 22:23
 */
public class Main {
    public static void main(String[] args) {
//        JdkDynamicProxy proxy = new JdkDynamicProxy(new Cat());
//        Animal animal = (Animal)proxy.getProxy();
//        ProxyUtils.generateClassFile(animal.getClass(), "$Proxy1");
//        animal.eat();

        OneHandler oneHandler = new OneHandler();

        oneHandler.setNextHandler(new TwoHandler());
        AbstractHandler.HeadHandler headHandler = new AbstractHandler.HeadHandler();
        headHandler.setNextHandler(oneHandler);
        JdkDynamicMultiProxy proxy = new JdkDynamicMultiProxy(new Cat(), headHandler);
        Animal animal = (Animal)proxy.getProxy();
        animal.eat();
    }

    private static class OneHandler extends AbstractHandler{

        @Override
        public Object invoke(TargetMethod targetMethod) throws InvocationTargetException, IllegalAccessException {
            System.out.println("one before");
            Object ret = proceed(targetMethod);
            System.out.println("one after");
            return ret;
        }
    }

    private static class TwoHandler extends AbstractHandler{

        @Override
        public Object invoke(TargetMethod targetMethod) throws InvocationTargetException, IllegalAccessException {
            System.out.println("two before");
            Object ret = proceed(targetMethod);
            System.out.println("two after");
            return ret;
        }
    }
}

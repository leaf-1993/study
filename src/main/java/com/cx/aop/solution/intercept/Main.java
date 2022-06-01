package com.cx.aop.solution.intercept;

import com.cx.aop.domain.Animal;
import com.cx.aop.domain.Cat;

import java.lang.reflect.InvocationTargetException;
import java.util.TooManyListenersException;

/**
 * @author chenxiang
 * @create 2022-04-18 20:14
 */
public class Main {
    public static void main(String[] args) {
//        JdkDynamicProxy proxy = new JdkDynamicProxy(new Cat());
//        proxy.addInterceptor(new OneMethodInterceptor());
//        proxy.addInterceptor(new TwoMethodInterceptor());
//        Animal animal = (Animal) proxy.getProxy();
//        animal.eat();
        int[] arr = new int[]{5,3,8};
        for(int i : arr){
            System.out.println(i);
        }
    }


    private static class OneMethodInterceptor implements MyMethodInterceptor{

        @Override
        public Object invoke(MyMethodInvocation methodInvocation) throws InvocationTargetException, IllegalAccessException {
            System.out.println("one before");
            Object ret = methodInvocation.proceed();
            System.out.println("one after");
            return ret;
        }
    }

    private static class TwoMethodInterceptor implements MyMethodInterceptor{

        @Override
        public Object invoke(MyMethodInvocation methodInvocation) throws InvocationTargetException, IllegalAccessException {
            System.out.println("two before");
            Object ret = methodInvocation.proceed();
            System.out.println("two after");
            return ret;
        }
    }

}

package com.cx.reactor.single;

import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author chenxiang
 * @create 2022-02-08 17:52
 */
public class Acceptor implements Runnable{
    private ServerSocketChannel ssc;
    private Selector selector;

    public Acceptor(ServerSocketChannel ssc, Selector selector) {
        this.ssc = ssc;
        this.selector = selector;
    }

    @Override
    public void run() {
        try {
            SocketChannel sc = ssc.accept();
            new Handler(sc, selector);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

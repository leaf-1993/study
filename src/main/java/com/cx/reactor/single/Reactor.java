package com.cx.reactor.single;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author chenxiang
 * @create 2022-02-08 17:49
 */
public class Reactor implements Runnable{

    public static void main(String[] args) throws IOException {
        Reactor r = new Reactor();
        r.run();
    }

    private Selector selector;

    private ServerSocketChannel ssc;

    public Reactor() throws IOException {
        ssc = ServerSocketChannel.open();
        selector = Selector.open();
        ssc.configureBlocking(false);
        ssc.bind(new InetSocketAddress("127.0.0.1", 8080));
        SelectionKey selectionKey = ssc.register(selector, SelectionKey.OP_ACCEPT);
        // 绑定 相应的时间处理器
        selectionKey.attach(new Acceptor(ssc, selector));
    }

    @Override
    public void run() {
        while (true){
            try {
                selector.select();
                Set<SelectionKey> keys = selector.selectedKeys();
                System.out.println("-------------------");
                for(Iterator<SelectionKey> lt = keys.iterator(); lt.hasNext();){
                    SelectionKey key = lt.next();
                    lt.remove();
                    dispatch(key);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void dispatch(SelectionKey key) {
        Runnable r = (Runnable) key.attachment();
        if(r != null){
            r.run();
        }
    }
}

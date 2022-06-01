package com.cx.reactor.single;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * @author chenxiang
 * @create 2022-02-08 17:53
 */
public class Handler implements Runnable{

    private SocketChannel sc;
    private Selector selector;
    private SelectionKey sk;

    static final int READING = 0, WRITING = 1;
    private ByteBuffer inputBuf = ByteBuffer.allocate(1024);
    private ByteBuffer outputBuf = ByteBuffer.allocate(1024);

    private int state = READING;

    public Handler(SocketChannel sc, Selector selector) throws IOException {
        this.sc = sc;
        this.selector = selector;
        this.sc.configureBlocking(false);
        this.sk = this.sc.register(selector, SelectionKey.OP_READ);
        this.sk.attach(this);
        this.selector.wakeup();
    }

    @Override
    public void run() {
        if(state == READING){
            read();
        } else {
            write();
        }
    }

    private void read(){
        try {
            if(sc.isOpen()){
                sc.read(inputBuf);
                process();
                state = WRITING;
                sk.interestOps(SelectionKey.OP_WRITE);
            }else{
                sc.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            sk.cancel();
        }
    }

    private void write(){
        try {
            sc.write(outputBuf);
            sk.interestOps(SelectionKey.OP_READ);
            state = READING;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void process(){}
}

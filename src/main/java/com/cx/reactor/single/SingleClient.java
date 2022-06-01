package com.cx.reactor.single;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

/**
 * @author chenxiang
 * @create 2022-02-08 18:17
 */
public class SingleClient {
    public static void main(String[] args) throws IOException, InterruptedException {
        SocketChannel sc = SocketChannel.open(new InetSocketAddress("127.0.0.1", 8080));
        sc.write(ByteBuffer.wrap("我的世界".getBytes(StandardCharsets.UTF_8)));
        Thread.sleep(1000L);
    }
}

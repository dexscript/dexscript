package com.dexscript.transpile.ioloop;

import org.junit.Test;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class DemoTest {

    @Test
    public void echo() throws Exception {
        Selector selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress addr = new InetSocketAddress("localhost", 2515);
        serverSocketChannel.bind(addr);
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, serverSocketChannel.validOps(), null);
        while (true) {
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();
                if (key.isAcceptable()) {
                    System.out.println("accept: " + key.channel());
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    if (socketChannel == null) {
                        continue;
                    }
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ);

                } else if (key.isReadable()) {
                    System.out.println("read: " + key.channel());
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    ByteBuffer buf = ByteBuffer.allocate(256);
                    int read = socketChannel.read(buf);
                    if (read == -1) {
                        System.out.println("remote hung up");
                        socketChannel.close();
                        continue;
                    }
                    String result = new String(buf.array()).trim();
                    System.out.println("message received");
                    System.out.println(result);
                    buf.flip();
                    SelectionKey writeKey = socketChannel.register(selector, SelectionKey.OP_WRITE);
                    writeKey.attach(buf);
                } else if (key.isWritable()) {
                    System.out.println("write: " + key.channel());
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    socketChannel.write((ByteBuffer) key.attachment());
                    key.cancel(); // do not need it anymore
                }
                keyIterator.remove();
            }
        }
    }
}

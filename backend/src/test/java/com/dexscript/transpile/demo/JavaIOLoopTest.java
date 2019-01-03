package com.dexscript.transpile.demo;

import org.junit.Test;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class JavaIOLoopTest {

    public interface Actor {
        void onReady(SelectionKey eky) throws Exception;
    }

    public static class ServerActor implements Actor {

        private final ServerSocketChannel serverSocketChannel;

        public ServerActor(Selector selector) throws Exception {
            serverSocketChannel = ServerSocketChannel.open();
            InetSocketAddress addr = new InetSocketAddress("localhost", 2515);
            serverSocketChannel.bind(addr);
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT, this);
        }

        @Override
        public void onReady(SelectionKey key) throws Exception {
            SocketChannel session = serverSocketChannel.accept();
            session.configureBlocking(false);
            new ReadMessage(key.selector(), session);
        }
    }

    public static class ReadMessage implements Actor {

        private SocketChannel session;

        public ReadMessage(Selector selector, SocketChannel session) throws Exception {
            this.session = session;
            session.register(selector, SelectionKey.OP_READ, this);
//            for(int i = 0; i < 10240; i++) {
//                System.out.println(session.write(ByteBuffer.wrap(new byte[4096])));
//            }
            System.out.println(session.read(ByteBuffer.allocate(256)));
        }

        @Override
        public void onReady(SelectionKey key) throws Exception {
            key.interestOps(key.interestOps() & ~SelectionKey.OP_READ);
            key.attach(null);
            System.out.println("read: " + session);
            ByteBuffer buf = ByteBuffer.allocate(256);
            int read = session.read(buf);
            if (read == -1) {
                System.out.println("remote hung up");
                session.close();
                return;
            }
            String result = new String(buf.array()).trim();
            System.out.println("message received");
            System.out.println(result);
            buf.flip();
            new WriteMessage(key, session, buf);
        }
    }

    public static class WriteMessage implements Actor {

        private SocketChannel session;
        private final ByteBuffer buf;

        public WriteMessage(SelectionKey key, SocketChannel session, ByteBuffer buf) throws Exception {
            this.session = session;
            this.buf = buf;
            key.attach(this);
            key.interestOps(key.interestOps() | SelectionKey.OP_WRITE);
        }

        @Override
        public void onReady(SelectionKey key) throws Exception {
            System.out.println("write: " + session);
            session.write(buf);
            key.cancel();
        }
    }

    @Test
    public void echo() throws Exception {
        Selector selector = Selector.open();
        new ServerActor(selector);
        while (true) {
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();
                Actor actor = (Actor) key.attachment();
                actor.onReady(key);
                keyIterator.remove();
            }
        }
    }
}

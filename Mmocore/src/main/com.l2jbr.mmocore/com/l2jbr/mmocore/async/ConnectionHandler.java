package com.l2jbr.mmocore.async;

import com.l2jbr.mmocore.ClientFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.*;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class ConnectionHandler<T extends AsyncMMOClient> extends Thread {

    private static final int HEADER_SIZE = 2;
    private final AsynchronousChannelGroup group;
    private final AsynchronousServerSocketChannel listener;
    private boolean shutdown;
    private ClientFactory<T> clientFactory;
    private ReadHandler readHandler;
    private WriteHandler writeHandler;
    private PacketHandler<T>  packetHandler;


    public ConnectionHandler(InetSocketAddress address, int threadPoolSize, ClientFactory<T> clientFactory, PacketHandler<T> packetHandler)
            throws IOException {
        this.clientFactory = clientFactory;
        this.packetHandler = packetHandler;

        group = createChannelGroup(threadPoolSize);
        listener = group.provider().openAsynchronousServerSocketChannel(group);
        listener.bind(address);
    }

    private AsynchronousChannelGroup createChannelGroup(int threadPoolSize) throws IOException {
        if(threadPoolSize <= 0 || threadPoolSize >= Integer.MAX_VALUE) {
            return AsynchronousChannelGroup.withCachedThreadPool(Executors.newCachedThreadPool(), 5);
        }
        return AsynchronousChannelGroup.withFixedThreadPool(threadPoolSize, Executors.defaultThreadFactory());
    }

    @Override
    public void run() {
        listener.accept(null, new AcceptConnectionHandler());

        while (!shutdown) {
            try  {
                Thread.sleep(50000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        closeConnection();
    }

    private void closeConnection() {
        try {
            listener.close();
            group.shutdownNow();
            group.awaitTermination(3600, TimeUnit.SECONDS);
        } catch (Exception e) {  }
    }

    private void acceptConnection(AsynchronousSocketChannel channel) {
        if(nonNull(channel) && channel.isOpen()) {
            try {
                channel.setOption(StandardSocketOptions.TCP_NODELAY, false);
            } catch (Exception e) {

            }

            var con = new AsyncMMOConnection<T>(channel, getReadHandler(), getWriteHandler());
            T client = clientFactory.create(con);
            client.read();
        } else {
            System.out.println("Channel Closed");
        }
    }

    private WriteHandler getWriteHandler() {
        if(isNull(writeHandler)) {
            writeHandler = new WriteHandler();
        }
        return writeHandler;
    }

    private ReadHandler getReadHandler() {
        if(isNull(readHandler)) {
            readHandler = new ReadHandler();
        }
        return  readHandler;
    }

    private void handleDisconnection(T client) {
        client.onDisconnection();
    }

    private void onFinishedIO(T client) {
        ByteBufferPool.recycleBuffer(client.getReadingBuffer());
    }

    private class AcceptConnectionHandler implements CompletionHandler<AsynchronousSocketChannel, Void> {
        @Override
        public void completed(AsynchronousSocketChannel clientChannel, Void attachment) {
            if(!shutdown && listener.isOpen()) {
                listener.accept(null, this);
            }
            acceptConnection(clientChannel);
        }

        @Override
        public void failed(Throwable exc, Void attachment) {
            System.out.println(exc.getLocalizedMessage());
        }
    }

    class WriteHandler implements  CompletionHandler<Integer, T> {

        @Override
        public void completed(Integer bytesWrited, T client) {

            onFinishedIO(client);
        }

        @Override
        public void failed(Throwable exc, T client) {
            onFinishedIO(client);
        }
    }

    class ReadHandler implements CompletionHandler<Integer, T> {

        @Override
        public void completed(Integer bytesRead, T client) {
            if(bytesRead < 0 ) {
                //Client disconnected.
                onFinishedIO(client);
                handleDisconnection(client);
                return;
            }

            if (bytesRead < HEADER_SIZE){
                // no enough data to read Header
                client.read();
                return;
            }

            var buffer = client.getReadingBuffer();

            buffer.flip();

            var dataSize = (buffer.getShort() & 0xFFFF) - HEADER_SIZE;

            if(dataSize > buffer.remaining()) {
                // No enough data yet, read more. Prepare buffer to writing without data loss.
                buffer.position(buffer.position() - HEADER_SIZE);
                buffer.compact();
                client.read();
                return;
            }

            if(dataSize > 0) {
                final int pos = buffer.position();
                parseAndExecutePacket(client, dataSize);
            }

            packetHandler.handler(client.getReadingBuffer(), client);


            onFinishedIO(client);
        }

        private void parseAndExecutePacket(T client, int dataSize) {
            var buffer = client.getReadingBuffer();
            client.decrypt(buffer, dataSize);
        }

        @Override
        public void failed(Throwable exc, T client) {
            onFinishedIO(client);
            handleDisconnection(client);
        }
    }


}

package com.l2jbr.mmocore;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.nonNull;

public class ConnectionHandler<T extends AsynchronousMMOClient> extends Thread {

    private final AsynchronousChannelGroup group;
    private final AsynchronousServerSocketChannel listener;
    private boolean shutdown;
    private ClientFactory<T> clientFactory;

    public ConnectionHandler(InetSocketAddress address, int threadPoolSize) throws IOException {
        group = createChannelGroup(threadPoolSize);
        listener = group.provider().openAsynchronousServerSocketChannel(group);
        listener.bind(address);
    }

    private AsynchronousChannelGroup createChannelGroup(int threadPoolSize) throws IOException {
        ExecutorService executor;
        if(threadPoolSize <= 0 || threadPoolSize >= Integer.MAX_VALUE) {
            executor = Executors.newCachedThreadPool();
        } else {
            executor = Executors.newFixedThreadPool(threadPoolSize);
        }
        return AsynchronousChannelGroup.withThreadPool(executor);
    }

    @Override
    public void run() {
        listener.accept(null, new AcceptConnectionHandler());

        while (! shutdown) {
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
                channel.setOption(StandardSocketOptions.TCP_NODELAY, true);
            } catch (Exception e) {

            }

            AsyncronousMMOConnection con  = new AsyncronousMMOConnection(channel);
            T client = clientFactory.create(con);
            channel.read(ByteBuffer.allocate(1024), client, new CompletionHandler<>() {

                @Override
                public void completed(Integer result, T attachment) {

                }

                @Override
                public void failed(Throwable exc, T attachment) {

                }
            });


        } else {
            System.out.println("Channel Closed");
        }
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

        }
    }
}

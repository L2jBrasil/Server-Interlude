package com.l2jbr.mmocore;

import java.nio.channels.AsynchronousSocketChannel;

public class AsyncronousMMOConnection {

    private final AsynchronousSocketChannel channel;

    public AsyncronousMMOConnection(AsynchronousSocketChannel channel) {
        this.channel = channel;
    }
}

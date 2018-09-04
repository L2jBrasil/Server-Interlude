package com.l2jbr.mmocore;

import java.nio.channels.CompletionHandler;

public class WriteHandler<T extends  AsyncMMOClient<AsyncMMOConnection<T>>> implements CompletionHandler<Integer, T> {
    @Override
    public void completed(Integer result, T attachment) {

    }

    @Override
    public void failed(Throwable exc, T attachment) {

    }
}

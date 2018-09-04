package com.l2jbr.mmocore;

public interface ClientFactory<T extends AsyncMMOClient<AsyncMMOConnection<T>>> {

    T create(AsyncMMOConnection<T> channel);
}

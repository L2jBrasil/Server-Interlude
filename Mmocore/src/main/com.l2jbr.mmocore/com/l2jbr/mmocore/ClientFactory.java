package com.l2jbr.mmocore;

public interface ClientFactory<T extends  AsynchronousMMOClient> {

    T create(AsyncronousMMOConnection con);
}

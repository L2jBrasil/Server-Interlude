package org.l2j.mmocore;

import java.net.SocketAddress;

import static java.lang.Math.max;
import static java.lang.Runtime.getRuntime;

class ConnectionConfig<T extends Client<Connection<T>>> {

    ClientFactory<T> clientFactory;
    boolean useNagle;
    ConnectionFilter acceptFilter;
    ReadHandler<T> readHandler;
    WriteHandler<T> writeHandler;
    int threadPoolSize;
    SocketAddress address;

    ConnectionConfig(SocketAddress address, ClientFactory<T> factory, ReadHandler<T> readHandler) {
        this.address = address;
        this.clientFactory = factory;
        this.readHandler = readHandler;
        this.writeHandler = new WriteHandler<>();
        threadPoolSize = max(1, getRuntime().availableProcessors() -2);
    }
}

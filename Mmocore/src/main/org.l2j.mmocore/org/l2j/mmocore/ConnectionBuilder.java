package org.l2j.mmocore;

import java.io.IOException;
import java.net.InetSocketAddress;

public class ConnectionBuilder<T extends Client<Connection<T>>> {

    private ConnectionConfig<T> config;

    public static <T extends Client<Connection<T>>> ConnectionBuilder<T> create(InetSocketAddress address, ClientFactory<T> clientFactory, PacketHandler<T> packetHandler, PacketExecutor<T> executor)  {
        ConnectionBuilder<T> builder = new ConnectionBuilder<>();
        builder.config = new ConnectionConfig<>(address, clientFactory, new ReadHandler<T>(packetHandler, executor));
        return builder;
    }

    public ConnectionBuilder<T> filter(ConnectionFilter filter) {
        this.config.acceptFilter = filter;
        return this;
    }

    public ConnectionBuilder<T> threadPoolSize(int size) {
        this.config.threadPoolSize = size;
        return this;
    }

    public ConnectionBuilder<T> useNagle(boolean  useNagle) {
        this.config.useNagle = useNagle;
        return  this;
    }

    public ConnectionHandler<T> build() throws IOException {
        return new ConnectionHandler<>(config);
    }

}

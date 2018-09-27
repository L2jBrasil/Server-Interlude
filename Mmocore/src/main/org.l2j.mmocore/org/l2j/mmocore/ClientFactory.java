package org.l2j.mmocore;

public interface ClientFactory<T extends AsyncMMOClient<AsyncMMOConnection<T>>> {

    T create(AsyncMMOConnection<T> connection);
}

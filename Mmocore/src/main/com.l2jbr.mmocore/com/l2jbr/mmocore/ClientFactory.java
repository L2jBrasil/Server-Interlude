package com.l2jbr.mmocore;

import com.l2jbr.mmocore.async.AsyncMMOConnection;
import com.l2jbr.mmocore.async.AsyncMMOClient;

public interface ClientFactory<T extends AsyncMMOClient> {

    T create(AsyncMMOConnection<T> con);
}

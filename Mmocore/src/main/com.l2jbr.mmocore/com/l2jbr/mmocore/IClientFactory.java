package com.l2jbr.mmocore;

/**
 * @author KenM
 * @param <T>
 */
public interface IClientFactory<T extends MMOClient<?>>
{
	T create(final MMOConnection<T> con);
}

/* This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package com.l2jbr.mmocore;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public abstract class AbstractPacket<T> {

    static boolean isBigEndian = ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN;
    byte[] data;
    int dataIndex;

    ByteBuffer writingBuffer;
	
	protected T _client;
	
	public final T getClient()
	{
		return _client;
	}

    static int pickShift(int top, int pos) { return isBigEndian ? top - pos : pos; }

    static short convertEndian(short n) { return !isBigEndian ? n : Short.reverseBytes(n); }

    static char convertEndian(char n) { return !isBigEndian ? n : Character.reverseBytes(n); }

    static int convertEndian(int n) { return !isBigEndian ? n : Integer.reverseBytes(n); }

    static long convertEndian(long n) { return !isBigEndian ? n : Long.reverseBytes(n); }
}

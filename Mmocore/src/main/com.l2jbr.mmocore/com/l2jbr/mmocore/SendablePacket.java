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


/**
 * @author KenM
 * @param <T>
 */
public abstract class SendablePacket<T> extends AbstractPacket<T> {

	/**
	 * Write <B>byte</B> to the buffer. <BR>
	 * 8bit integer (00)
	 * @param data
	 */
	protected final void writeC(final int data)
	{
		writingBuffer.put((byte) data);
	}
	
	
	/**
	 * Write <B>double</B> to the buffer. <BR>
	 * 64bit double precision float (00 00 00 00 00 00 00 00)
	 * @param value
	 */
	protected final void writeF(final double value)
	{
		writingBuffer.putDouble(value);
	}
	
	/**
	 * Write <B>short</B> to the buffer. <BR>
	 * 16bit integer (00 00)
	 * @param value
	 */
	protected final void writeH(final int value)
	{
		writingBuffer.putShort((short) value);
	}
	
	/**
	 * Write <B>int</B> to the buffer. <BR>
	 * 32bit integer (00 00 00 00)
	 * @param value
	 */
	protected final void writeD(final int value)
	{
		writingBuffer.putInt(value);
	}
	
	/**
	 * Write <B>long</B> to the buffer. <BR>
	 * 64bit integer (00 00 00 00 00 00 00 00)
	 * @param value
	 */
	protected final void writeQ(final long value)
	{
		writingBuffer.putLong(value);
	}
	
	/**
	 * Write <B>byte[]</B> to the buffer. <BR>
	 * 8bit integer array (00 ...)
	 * @param data
	 */
	protected final void writeB(final byte[] data)
	{
		writingBuffer.put(data);
	}
	
	/**
	 * Write <B>String</B> to the buffer.
	 * @param text
	 */
	protected final void writeS(final String text)
	{
		if (text != null)
		{
			final int len = text.length();
			for (int i = 0; i < len; i++)
			{
				writingBuffer.putChar(text.charAt(i));
			}
		}
		
		writingBuffer.putChar('\000');
	}
	
	protected abstract void write();
}

package com.dingj.chatjar.util;

import java.util.LinkedList;

import com.dingj.chatjar.content.DataPacket;

/**
 *数据队列控制
 * 
 */
public class PacketQueue
{
	private static LinkedList<DataPacket> packetQueue = new LinkedList();

	/**
	 * 弹出数据包队列对象
	 * 
	 * @return 最前面的数据包
	 */
	public static DataPacket popPacket()
	{
		synchronized (packetQueue)
		{
			return packetQueue.removeFirst();
		}
	}

	/**
	 * 将数据包压入数据队列
	 * 
	 * @param packet
	 *            数据包
	 */
	public static void pushPacket(DataPacket packet)
	{
		synchronized (packetQueue)
		{
			packetQueue.add(packet);
		}
	}
}

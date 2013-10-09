package com.dingj.chatjar.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.dingj.chatjar.content.DataPacket;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import jding.debug.JDingDebug;

public class NetUtil
{
	private static final String TAG = "NetUtil";
	private static boolean DEBUG = true;

	/**
	 * 判断端口是否被占用
	 * 
	 * @return
	 */
	public static boolean checkPort()
	{
		try
		{
			new DatagramSocket(IpMsgConstant.IPMSG_DEFAULT_PORT).close();
			return true;
		} catch (SocketException ex)
		{
			ex.printStackTrace();
			return false;
		}
	}

	/**
	 * 发送UDP数据包
	 * 
	 * @param dataPacket
	 *            封装的数据包
	 * @param targetIp
	 *            目标IP
	 */
	public static void sendUdpPacket(DataPacket dataPacket, String targetIp)
	{
		try
		{
			byte[] dataBit = dataPacket.toString().getBytes(
					SystemVar.DEFAULT_CHARACT);
			DatagramPacket sendPacket = new DatagramPacket(dataBit,
					dataBit.length, InetAddress.getByName(targetIp),
					IpMsgConstant.IPMSG_DEFAULT_PORT);
			if (DEBUG)
			{
				JDingDebug.printfD(TAG, "dataBit: " + dataPacket.toString());
			}
			SocketManage.getInstance().getUdpSocket().send(sendPacket);
		} catch (UnsupportedEncodingException ex)
		{
		} catch (IOException ex)
		{
		}
	}

	
	 /**
	 * 发送局域网内广播
	 * @param dataPacket
	 */
	public static void broadcastUdpPacket(DataPacket dataPacket)
	{
		sendUdpPacket(dataPacket, "255.255.255.255");
	}

	public static void broadcastUdpPacketToSecond(DataPacket dataPacket)
	{
		int i = 0;
		for (i = 0; i < 255; i++)
		{
			String ip = "192.168.123." + i;
			sendUdpPacket(dataPacket, ip);
		}
	}

	/**
	 * 获得本机ip
	 * 
	 * @return 本机ip
	 */
	public static String getLocalHostIp()
	{
		try
		{
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException ex)
		{
			return null;
		}
	}
}

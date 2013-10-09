package com.dingj.chatjar;

import java.io.IOException;
import java.net.ServerSocket;

import jding.debug.JDingDebug;

import com.dingj.chatjar.content.DataPacket;
import com.dingj.chatjar.thread.DataPacketHandler;
import com.dingj.chatjar.thread.RecvPacketThread;
import com.dingj.chatjar.thread.SendFileHandler;
import com.dingj.chatjar.util.IpMsgConstant;
import com.dingj.chatjar.util.NetUtil;
import com.dingj.chatjar.util.SystemVar;
import com.dingj.chatjar.util.Util;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

public class ChatService extends Service
{
	private ServerSocket mServerSocket = null;	
	private Context mContext;
	private final boolean DEBUG = true;
	private final String TAG = "CharService";
	private IBinder mIbinder = new CharBind();
			
	public class CharBind extends Binder
	{
		public ChatService getService()
		{
			return ChatService.this;
		}
	}
	
	@Override
	public IBinder onBind(Intent intent)
	{
		return mIbinder;
	}
	
	/**
	 * 建立一个新的连接
	 */
	public void connect()
	{
		SystemVar.init();
//		if(isrun == false)
		{		
			try {
				mServerSocket = new ServerSocket(IpMsgConstant.IPMSG_DEFAULT_PORT);
			} catch (IOException e) 
			{
				e.printStackTrace();
			}
			new Thread(new RecvPacketThread()).start();//recive packet 
			new Thread(new DataPacketHandler(this)).start();
//			new Thread(new SendFileHandler(mServerSocket)).start();//
			logn();
//			isrun = true;
		}
	}
	
	private void logn() 
	{
		if(DEBUG)
		{
			JDingDebug.printfD(TAG,"wifiIP:" + SystemVar.WIFIIP);
		}
    	WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        //判断wifi是否开启
        if (!wifiManager.isWifiEnabled()) 
        {
        	Toast.makeText(this, "wifi is not work", Toast.LENGTH_SHORT).show();
//        	wifiManager.setWifiEnabled(true);
        }
    	WifiInfo wifiInfo = wifiManager.getConnectionInfo();     
    	int ipAddress = wifiInfo.getIpAddress(); 
    	SystemVar.WIFIIP = Util.intToIp(ipAddress);
    	JDingDebug.printfSystem("wifiIP:" + SystemVar.WIFIIP);
		 //发送登录信息包
        DataPacket dp=new DataPacket(IpMsgConstant.IPMSG_BR_ENTRY);
        dp.setAdditional(SystemVar.USER_NAME);
        dp.setIp(NetUtil.getLocalHostIp());
        if(DEBUG)
        {
        	JDingDebug.printfD(TAG,"hostIp:" + dp.getIp());
        }
        NetUtil.broadcastUdpPacket(dp);	
//        NetUtil.broadcastUdpPacketToSecond(dp);
	}
}

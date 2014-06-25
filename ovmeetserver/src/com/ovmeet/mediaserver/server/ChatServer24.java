//Author:lihong QQ:1410919373
package com.ovmeet.mediaserver.server;

//import com.ovmeet.mediaserver.io.utils.MemThread;
//import com.ovmeet.mediaserver.server.adapter.ApplicationAdapter;
//import com.ovmeet.mediaserver.server.api.IClient;
//import com.ovmeet.mediaserver.server.api.IConnection;
//import com.ovmeet.mediaserver.server.api.IScope;
//import com.ovmeet.mediaserver.server.api.Red5;
//import com.ovmeet.mediaserver.server.api.service.IServiceCapableConnection;
//import com.ovmeet.mediaserver.server.api.so.ISharedObject;
//import com.ovmeet.mediaserver.server.api.stream.IBroadcastStream;
//import com.ovmeet.mediaserver.server.api.stream.IStreamCapableConnection;
//import com.ovmeet.mediaserver.server.api.stream.support.SimpleConnectionBWConfig;
//import com.ovmeet.mediaserver.server.stream.ClientBroadcastStream;

import org.red5.server.adapter.ApplicationAdapter;
import org.red5.server.api.IClient;
import org.red5.server.api.IConnection;
import org.red5.server.api.IScope;
import org.red5.server.api.Red5;
import org.red5.server.api.service.IServiceCapableConnection;
import org.red5.server.api.so.ISharedObject;
import org.red5.server.api.stream.IBroadcastStream;
import org.red5.server.stream.ClientBroadcastStream;

import java.io.PrintStream;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Referenced classes of package com.ovmeet.mediaserver.server:
//			Server

public class ChatServer24 extends ApplicationAdapter
{

	public static final Logger log = LoggerFactory.getLogger("com.ovmeet.mediaserver.server.ChatServer24");
	private static int appMaxUser = 100000;
	private int currentUser;
	private HashMap connList;

	public ChatServer24()
	{
		currentUser = 0;
		
		connList = new HashMap();
		/**
		try
		{
			MemThread m = new MemThread();
			m.init();
			appMaxUser = MemThread.maxUser;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		*/
		//System.out.println((new StringBuilder("�����ڷ�����License����Ϊ:")).append(appMaxUser).toString());
		
		
		
		//if (MemThread.hasBuy)
		//	System.out.println("License����ʽ�汾!");
		//else
		//	System.out.println("License�����ð汾!");
	}

	public boolean appStart(IScope iscope)
	{
		super.appStart(iscope);
		setGhostConnsCleanupPeriod(2000);
		//log.info((new StringBuilder("��Ƶ��������� start-")).append(appMaxUser).append(" concurrence user version").toString());
		return true;
	}

	public void appStop(IScope iscope)
	{
		super.appStop(iscope);
		//log.info("��Ƶ��������� stop");
	}

	public boolean roomStart(IScope iscope)
	{
		try
		{
			super.roomStart(iscope);
			ISharedObject isharedobject = getSharedObject(iscope, "textChat", false);
			ISharedObject isharedobject1 = getSharedObject(iscope, "userList", false);
			ISharedObject isharedobject2 = getSharedObject(iscope, "speakList", false);
			ISharedObject isharedobject3 = getSharedObject(iscope, "videoList", false);
			ISharedObject isharedobject4 = getSharedObject(iscope, "roomInfo", false);
			ISharedObject userList2 = getSharedObject(iscope, "userList2", false);
			ISharedObject isharedobject5 = getSharedObject(iscope, "chatHistory", false);
			isharedobject5.beginUpdate();
			isharedobject5.setAttribute("history", "");
			isharedobject5.endUpdate();
			isharedobject4.beginUpdate();
			isharedobject4.setAttribute("txtRoomInfo", "������ʾ���棬�����˿����޸ĸ��£�");
			isharedobject4.setAttribute("maxVideoSeat", new Integer(4));
			isharedobject4.setAttribute("curVideoSeat", new Integer(0));
			isharedobject4.setAttribute("applyVideo", Boolean.valueOf(false));
			HashMap hashmap = new HashMap();
			//hashmap.put("w", new Integer(240));
			//hashmap.put("h", new Integer(180));
			//hashmap.put("f", new Integer(10));
			hashmap.put("w", new Integer(320));
			hashmap.put("h", new Integer(240));
			hashmap.put("f", new Integer(15));			
			isharedobject4.setAttribute("v_mode", hashmap);
			isharedobject4.setAttribute("v_q", new Integer(90));
			isharedobject4.setAttribute("a_rate", new Integer(22));
			isharedobject4.setAttribute("a_s", new Integer(10));
			isharedobject4.setAttribute("g_ds", Boolean.valueOf(true));//���ڽ�ֹ������
			isharedobject4.setAttribute("g_dp", Boolean.valueOf(false));//���ڽ�ֹ˽��
			isharedobject4.setAttribute("g_dt", Boolean.valueOf(false));//���ڽ�ֹ�ı�����
			isharedobject4.setAttribute("a_dv", Boolean.valueOf(false));//��ֹ������Ƶ
			isharedobject4.setAttribute("a_da", Boolean.valueOf(false));//��ֹ������Ƶ
			isharedobject4.setAttribute("sync_ui", Boolean.valueOf(false));
			isharedobject4.setAttribute("lock", Boolean.valueOf(false));
			isharedobject4.endUpdate();
			ISharedObject isharedobject6 = getSharedObject(iscope, "whiteboard", false);
			isharedobject6.beginUpdate();
			isharedobject6.setAttribute("totalPage", new Integer(1));
			isharedobject6.setAttribute("indexPage", new Integer(1));
			isharedobject6.endUpdate();
			ISharedObject page1 = getSharedObject(iscope, "page1", false);
			page1.beginUpdate();
			page1.setAttribute("lastID", new Integer(1000));
			page1.setAttribute("indexPage", new Integer(1));
			page1.endUpdate();
		}
		catch (Exception e)
		{
			log.error("roomStart(...) Exception!", e);
			return false;
		}
		return true;
	}

	public boolean roomConnect(IConnection iconnection, Object aobj[])
	{
		/**ȡ�޴�������
		if (iconnection instanceof IStreamCapableConnection)
		{
			IStreamCapableConnection streamConn = (IStreamCapableConnection)iconnection;
			SimpleConnectionBWConfig bwConfig = new SimpleConnectionBWConfig();
			bwConfig.getChannelBandwidth()[3] = 0x100000L;
			bwConfig.getChannelInitialBurst()[3] = 0x20000L;
			streamConn.setBandwidthConfigure(bwConfig);
		}
		**/
		super.roomConnect(iconnection, aobj);
		/**
		if (aobj.length == 5)
			break MISSING_BLOCK_LABEL_69;
		rejectClient();
		return false;
		String domain = (String)aobj[3];
		if (checkDomain(domain))
			break MISSING_BLOCK_LABEL_91;
		rejectClient();
		return false;
		**/
		//�ͻ��˲������벻����5������������
		
		if(aobj.length != 5)
		{
			rejectClient();
			return false;			
			
		}
		//����������ϸ񣬲�����
		/*
		String domain = (String)aobj[3];
		if (!checkDomain(domain)){
			rejectClient();
			return false;		
		}	
		*/
		IScope iscope = iconnection.getScope();
		ISharedObject isharedobject = getSharedObject(iscope, "userList", false);
		ISharedObject isharedobject1 = getSharedObject(iscope, "roomInfo", false);
		ISharedObject userList2 = getSharedObject(iscope, "userList2", false);
		boolean flag = ((Boolean)isharedobject1.getAttribute("lock")).booleanValue();//����������־
		int i = iconnection.getScope().getClients().size();//���볤��
		int j = Integer.parseInt((String)aobj[1]);//Ȩ�ޱ�־
		/**ȡ�������ȴ���0  aobj.length<1���ǲ��ǹ���Ա j!=2 ��־2Ϊ����Ա��������������־flag,��MemThread.isExpired()��Ȩ��֤�ǲ��ǵ���
		if (aobj.length < 1 || MemThread.isExpired() || j != 2 && flag)
			break MISSING_BLOCK_LABEL_608;
		**/
		
		String userName = (String)aobj[0];
		
		 
		if (userList2.getAttribute(userName) != null)
		{
			rejectClient();
			return false;		
		}	
		 
		//System.out.println("����������"+aobj.length+"������־��"+flag+"�û�����"+j+"�û�����"+userName);
		
		//if (aobj.length > 0  && (!flag || j == 2) )
		if (aobj.length > 0)	
		{	
		
		//String userName = (String)aobj[0];
		//if (currentUser >= appMaxUser)
		//	break MISSING_BLOCK_LABEL_608;
			if (currentUser < appMaxUser)
			{ 
			
				currentUser++;
				IServiceCapableConnection iservicecapableconnection = (IServiceCapableConnection)iconnection;
				String s = iconnection.getClient().getId();
				
				//���½����û����ӵ��û��ɣĺţ��ɣĺ��Ƿ�����Զ����ɵģ�������÷��ظ��ͻ���
				iservicecapableconnection.invoke("setUserID", new Object[] {
					s
				});
				//System.out.println("���·�����Ϣ��");
				
				//���¿ͻ��˵ķ�����Ϣ
				iservicecapableconnection.invoke("updateRoomInfo", new Object[] {
						//�����������MemThread.maxUser���Ƿ��ѹ���MemThread.hasBuy��true����flase�ǹ�����ʾ��Ϣ��MemThread.companyName
						//Integer.valueOf(MemThread.maxUser), Boolean.valueOf(MemThread.hasBuy), MemThread.companyName
						Integer.valueOf(appMaxUser), Boolean.valueOf(true), "�Ƽ�����δ��������������"
				});
				
				
				
				iconnection.setAttribute("userID", s);
				iconnection.setAttribute("userName", userName);
				isharedobject.beginUpdate();
				HashMap hashmap = new HashMap();
				hashmap.put("userID", s);
				hashmap.put("userName", aobj[0]);
				hashmap.put("role", aobj[1]);
				hashmap.put("ip", new String("127.0.0.1"));
				hashmap.put("videoSeat", new Integer(-1));
				hashmap.put("hasCam", aobj[2]);
				hashmap.put("realName", aobj[4]);
				hashmap.put("videoSeat", new Integer(-2));
				/**��Ȩͨ������flash�ͻ��� showTryWin������������ӭ���� MemThread.isTria���ñ�־,true���ã�flase������
				if (MemThread.isTria)
				{
					String strWelcome = "��ӭ����CeCallMeet,��ַhttp://www.cecall.cc!";
					iservicecapableconnection.invoke("showTryWin", new Object[] {
						strWelcome
					});
				}
				**/
				//System.out.println("�鿴�û���������Ϣ��");
				
				if (userList2.getAttribute(userName) != null)
				{
					String s2 = (String)userList2.getAttribute(userName);
					kickUser(s2);//�߳���һ�εĻ����¼
					isharedobject.removeAttribute(s2);
					isharedobject.setAttribute(s, hashmap);
					isharedobject.endUpdate();
				} else
				{
					isharedobject.setAttribute(s, hashmap);
					isharedobject.endUpdate();
					userList2.beginUpdate();
					userList2.setAttribute((String)aobj[0], s);
					userList2.endUpdate();
				}
				connList.put(s, iconnection);
				return true; 
				
			}
		}
		try
		{
			rejectClient();
		}
		catch (Exception e)
		{
			log.error("roomConnect(...) Exception!", e);
			currentUser--;
			return false;
		}
		return false;
	}

	public void roomDisconnect(IConnection iconnection)
	{
		try
		{
			String s = (String)iconnection.getAttribute("userID");
			String userName = (String)iconnection.getAttribute("userName");
			if (s != null)
				currentUser--;
			ISharedObject isharedobject = getSharedObject(iconnection.getScope(), "userList", false);
			ISharedObject isharedobject1 = getSharedObject(iconnection.getScope(), "speakList", false);
			ISharedObject isharedobject2 = getSharedObject(iconnection.getScope(), "roomInfo", false);
			ISharedObject userList2 = getSharedObject(iconnection.getScope(), "userList2", false);
			userList2.beginUpdate();
			userList2.setAttribute(userName, null);
			userList2.endUpdate();
			HashMap hashmap = (HashMap)isharedobject.getAttribute(s);
			if (isharedobject2 != null)
			{
				Object curVideoSeat = isharedobject2.getAttribute("curVideoSeat");
				int i = curVideoSeat == null ? 0 : ((Integer)curVideoSeat).intValue();
				int videoSeat = 0;
				if (hashmap != null && hashmap.get("videoSeat") != null)
					videoSeat = ((Integer)hashmap.get("videoSeat")).intValue();
				if (videoSeat > 0)
				{
					i--;
					isharedobject2.beginUpdate();
					isharedobject2.setAttribute("curVideoSeat", new Integer(i));
					isharedobject2.endUpdate();
				}
			}
			ArrayList arraylist = new ArrayList();
			arraylist.add(hashmap);
			isharedobject1.sendMessage("delSpeaker", arraylist);
			isharedobject1.beginUpdate();
			isharedobject1.removeAttribute(s);
			isharedobject1.endUpdate();
			
			isharedobject.beginUpdate();
			isharedobject.removeAttribute(s);
			isharedobject.endUpdate();
			
			connList.remove(s);
			super.roomDisconnect(iconnection);
		}
		catch (Exception e)
		{
			log.error("roomDisconnect exception", e);
			currentUser--;
			try
			{
				if (iconnection != null)
				{
					String s1 = (String)iconnection.getAttribute("userID");
					connList.remove(s1);
					super.roomDisconnect(iconnection);
				}
			}
			catch (Exception ex)
			{
				log.error("roomDisconnect exception 2");
			}
		}
	}
	//��������ǲ�����֤�ķ�����
	protected boolean checkDomain(String domain)
	{
		/**
		int len = Server.hostnames.length;
		if (domain.equals(""))
			return false;
		if (domain.equalsIgnoreCase("www.ovmeet.com"))
			return true;
		for (int i = 0; i < len; i++)
			if (Server.hostnames[i].equals("") || Server.hostnames[i].equalsIgnoreCase(domain))
				return true;

		return false;
		**/
		return true;
	}

	public void applySpeaker(String s)
	{
		try
		{
			ISharedObject isharedobject = getSharedObject(Red5.getConnectionLocal().getScope(), "userList", false);
			ISharedObject isharedobject1 = getSharedObject(Red5.getConnectionLocal().getScope(), "speakList", false);
			ISharedObject isharedobject2 = getSharedObject(Red5.getConnectionLocal().getScope(), "roomInfo", false);
			HashMap hashmap = (HashMap)isharedobject.getAttribute(s);
			boolean flag = ((Boolean)isharedobject2.getAttribute("applyVideo")).booleanValue();
			if (flag)
			{
				isharedobject1.beginUpdate();
				hashmap.put("videoSeat", new Integer(-1));
				isharedobject1.setAttribute(s, hashmap);
				isharedobject1.endUpdate();
			} else
			{
				publishVideo(s);
			}
		}
		catch (Exception e)
		{
			log.error("applySpeaker Exception!", e);
		}
	}  
	public void publishVideo(String s)
	{
		try
		{
			ISharedObject isharedobject = getSharedObject(Red5.getConnectionLocal().getScope(), "roomInfo", false);
			int i = ((Integer)isharedobject.getAttribute("maxVideoSeat")).intValue();
			int j = ((Integer)isharedobject.getAttribute("curVideoSeat")).intValue();
			byte byte0 = -1;
			IServiceCapableConnection iservicecapableconnection = (IServiceCapableConnection)connList.get(s);
			if (i > j)
			{
				if (iservicecapableconnection != null)
				{
					iservicecapableconnection.invoke("publishVideo", new Object[] {
						Boolean.valueOf(true)
					});
					j++;
					byte0 = 1;
				}
			} else
			if (iservicecapableconnection != null)
				iservicecapableconnection.invoke("noSeat", new Object[] {
					Boolean.valueOf(true)
				});
			isharedobject.beginUpdate();
			isharedobject.setAttribute("curVideoSeat", new Integer(j));
			isharedobject.endUpdate();
			ISharedObject isharedobject1 = getSharedObject(Red5.getConnectionLocal().getScope(), "userList", false);
			ISharedObject isharedobject2 = getSharedObject(Red5.getConnectionLocal().getScope(), "speakList", false);
			HashMap hashmap = (HashMap)isharedobject1.getAttribute(s);
			hashmap.put("videoSeat", new Integer(byte0));
			isharedobject2.beginUpdate();
			isharedobject2.setAttribute(s, hashmap);
			isharedobject2.endUpdate();
		}
		catch (Exception e)
		{
			log.error("publishVideo(...) Exception!", e);
		}
	}
	public void closeSpeaker(String s)
	{
		try
		{
			ISharedObject isharedobject = getSharedObject(Red5.getConnectionLocal().getScope(), "userList", false);
			ISharedObject isharedobject1 = getSharedObject(Red5.getConnectionLocal().getScope(), "speakList", false);
			HashMap hashmap = (HashMap)isharedobject.getAttribute(s);
			stopVideo(s);
			isharedobject1.beginUpdate();
			hashmap.put("videoSeat", new Integer(-2));
			isharedobject1.setAttribute(s, hashmap);
			isharedobject1.endUpdate();
		}
		catch (Exception e)
		{
			log.error("closeSpeaker(...) Exception!", e);
		}
	}
	public void stopVideo(String s)
	{
		try
		{
			ISharedObject isharedobject = getSharedObject(Red5.getConnectionLocal().getScope(), "roomInfo", false);
			int i = ((Integer)isharedobject.getAttribute("curVideoSeat")).intValue();
			IServiceCapableConnection iservicecapableconnection = (IServiceCapableConnection)connList.get(s);
			if (iservicecapableconnection != null)
				iservicecapableconnection.invoke("stopVideo", new Object[] {
					Boolean.valueOf(true)
				});
			if (--i < 0)
				i = 0;
			isharedobject.beginUpdate();
			isharedobject.setAttribute("curVideoSeat", new Integer(i));
			isharedobject.endUpdate();
			ISharedObject isharedobject1 = getSharedObject(Red5.getConnectionLocal().getScope(), "speakList", false);
			HashMap hashmap = (HashMap)isharedobject1.getAttribute(s);
			ArrayList arraylist = new ArrayList();
			arraylist.add(hashmap);
			isharedobject1.sendMessage("delSpeaker", arraylist);
			hashmap.put("videoSeat", new Integer(-1));
			isharedobject1.beginUpdate();
			isharedobject1.setAttribute(s, hashmap);
			isharedobject1.endUpdate();
		}
		catch (Exception e)
		{
			log.error("publishVideo(...)", e);
		}
	}

	public void kickUser(String s)
	{
		try
		{
			IServiceCapableConnection iservicecapableconnection = (IServiceCapableConnection)connList.get(s);
			if (iservicecapableconnection != null)
			{
				iservicecapableconnection.invoke("onKick", new Object[] {
					Boolean.valueOf(true)
				});
				iservicecapableconnection.close();
			}
		}
		catch (Exception e)
		{
			log.error("", e);
		}
	}

	public void setRole(String s, String s1)
	{
		try
		{
			IServiceCapableConnection iservicecapableconnection = (IServiceCapableConnection)connList.get(s1);
			ISharedObject isharedobject = getSharedObject(Red5.getConnectionLocal().getScope(), "userList", false);
			HashMap hashmap = (HashMap)isharedobject.getAttribute(s1);
			hashmap.put("role", s);
			isharedobject.beginUpdate();
			isharedobject.setAttribute(s1, hashmap);
			isharedobject.endUpdate();
			if (iservicecapableconnection != null)
				iservicecapableconnection.invoke("onSetRole", new Object[] {
					s
				});
		}
		catch (Exception e)
		{
			log.error("", e);
		}
	}

	public void updateRoomInfo(String s, String s1)
	{
		try
		{
			ISharedObject isharedobject = getSharedObject(Red5.getConnectionLocal().getScope(), "roomInfo", false);
			isharedobject.beginUpdate();
			isharedobject.setAttribute(s, s1);
			isharedobject.endUpdate();
		}
		catch (Exception e)
		{
			log.error("", e);
		}
	}

	public void sendTextMsg(String s)
	{
		try
		{
			ISharedObject isharedobject = getSharedObject(Red5.getConnectionLocal().getScope(), "textChat", false);
			//ISharedObject isharedobject1 = getSharedObject(Red5.getConnectionLocal().getScope(), "chatHistory", false);
			ArrayList arraylist = new ArrayList();
			arraylist.add(s);
			isharedobject.sendMessage("sendMessage", arraylist);
			
			//String s1 = (String)isharedobject1.getAttribute("history");
			//s1 = (new StringBuilder()).append(s1).append(s).toString();
			
			
			//isharedobject1.beginUpdate();
			//isharedobject1.setAttribute("history", s1);
			//isharedobject1.endUpdate();
		}
		catch (Exception e)
		{
			log.error("", e);
		}
	}

	public String getHistory()
	{
		String s;
		ISharedObject isharedobject = getSharedObject(Red5.getConnectionLocal().getScope(), "chatHistory", false);
		s = (String)isharedobject.getAttribute("history");
		return s;
		//Exception e;
		//e;
		//log.error("", e);
		//return "";
	}

	public void newShape(Object obj, String page)
	{
		try
		{
			ISharedObject isharedobject = getSharedObject(Red5.getConnectionLocal().getScope(), page, false);
			int i = 1000;
			if (isharedobject.getAttribute("lastID") != null)
			{
				i = ((Integer)isharedobject.getAttribute("lastID")).intValue();
			} else
			{
				isharedobject.beginUpdate();
				isharedobject.setAttribute("lastID", new Integer(1000));
				isharedobject.setAttribute("indexPage", Integer.valueOf(Integer.parseInt(page.substring(4))));
				isharedobject.endUpdate();
			}
			String s = String.valueOf(i);
			i++;
			isharedobject.beginUpdate();
			isharedobject.setAttribute("lastID", new Integer(i));
			isharedobject.setAttribute((new StringBuilder(String.valueOf(page))).append(s).toString(), obj);
			isharedobject.endUpdate();
		}
		catch (Exception e)
		{
			log.error("", e);
		}
	}

	public void updatePages(int indexPage, int totalPage)
	{
		try
		{
			ISharedObject isharedobject = getSharedObject(Red5.getConnectionLocal().getScope(), "whiteboard", false);
			isharedobject.beginUpdate();
			isharedobject.setAttribute("totalPage", new Integer(totalPage));
			isharedobject.setAttribute("indexPage", new Integer(indexPage));
			isharedobject.endUpdate();
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
	}

	public void editShape(String name, Object obj, String page)
	{
		try
		{
			ISharedObject isharedobject = getSharedObject(Red5.getConnectionLocal().getScope(), page, false);
			isharedobject.beginUpdate();
			isharedobject.setAttribute(name, obj);
			isharedobject.endUpdate();
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
	}

	public void clearWB(String page)
	{
		try
		{
			ISharedObject isharedobject = getSharedObject(Red5.getConnectionLocal().getScope(), page, false);
			int i = 1000;
			if (isharedobject.getAttribute("lastID") != null)
			{
				i = ((Integer)isharedobject.getAttribute("lastID")).intValue();
				for (int j = 1000; j < i; j++)
					isharedobject.removeAttribute((new StringBuilder(String.valueOf(page))).append(String.valueOf(j)).toString());

				isharedobject.beginUpdate();
				isharedobject.setAttribute("lastID", new Integer(1000));
				isharedobject.setAttribute("indexPage", Integer.valueOf(Integer.parseInt(page.substring(4))));
				isharedobject.endUpdate();
			}
		}
		catch (Exception e)
		{
			log.error("clear witeboard error", e);
		}
	}

	public void startRecord(String userID, String flvName)
	{
		try
		{
			System.out.println("user:"+userID+"--"+"flvname:"+flvName);
			IBroadcastStream s = this.getBroadcastStream(Red5.getConnectionLocal().getScope(), userID);
			s.saveAs(flvName, true);
			ISharedObject speakListSo = getSharedObject(Red5.getConnectionLocal().getScope(), "speakList", false);
			HashMap speaker = (HashMap)speakListSo.getAttribute(userID);
			speaker.put("isRecord", new Boolean(true));
			speakListSo.beginUpdate();
			speakListSo.setAttribute(userID, speaker);
			speakListSo.endUpdate();
		}
		catch (Exception e)
		{
			log.error("startRecord(...) Exception", e);
		}
	}

	public void stopRecord(String userID)
	{
		try
		{
			ClientBroadcastStream s = (ClientBroadcastStream)getBroadcastStream(Red5.getConnectionLocal().getScope(), userID);
			s.stopRecording();
			ISharedObject isharedobject1 = getSharedObject(Red5.getConnectionLocal().getScope(), "speakList", false);
			ArrayList arraylist = new ArrayList();
			isharedobject1.sendMessage("getVodList", arraylist);
			ISharedObject speakListSo = getSharedObject(Red5.getConnectionLocal().getScope(), "speakList", false);
			HashMap speaker = (HashMap)speakListSo.getAttribute(userID);
			speaker.put("isRecord", new Boolean(false));
			speakListSo.beginUpdate();
			speakListSo.setAttribute(userID, speaker);
			speakListSo.endUpdate();
		}
		catch (Exception e)
		{
			log.error("stopRecord(...) Exception", e);
		}
	}

	public void setBandwidth(int b)
	{
		/**
		try
		{
			IConnection iconnection = Red5.getConnectionLocal();
			if (iconnection instanceof IStreamCapableConnection)
			{
				IStreamCapableConnection streamConn = (IStreamCapableConnection)iconnection;
				SimpleConnectionBWConfig bwConfig = new SimpleConnectionBWConfig();
				bwConfig.getChannelBandwidth()[3] = b * 1024;
				bwConfig.getChannelInitialBurst()[3] = b * 1024;
				streamConn.setBandwidthConfigure(bwConfig);
			}
		}
		catch (Exception e)
		{
			log.error("stopRecord(...) Exception", e);
		}
		**/
	}
	

}

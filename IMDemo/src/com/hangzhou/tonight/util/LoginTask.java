package com.hangzhou.tonight.util;

import java.util.Collection;

import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.XMPPError;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.hangzhou.tonight.IActivitySupport;
import com.hangzhou.tonight.MainActivity1;
import com.hangzhou.tonight.R;
import com.hangzhou.tonight.comm.Constant;
import com.hangzhou.tonight.manager.XmppConnectionManager;
import com.hangzhou.tonight.model.LoginConfig;

/**
 * 
 * ��¼�첽����.
 * 
 * @author shimiso
 */
public class LoginTask extends AsyncTask<String, Integer, Integer> {
	private ProgressDialog pd;
	private Context context;
	private IActivitySupport activitySupport;
	private LoginConfig loginConfig;

	public LoginTask(IActivitySupport activitySupport, LoginConfig loginConfig) {
		this.activitySupport = activitySupport;
		this.loginConfig = loginConfig;
		this.pd = activitySupport.getProgressDialog();
		this.context = activitySupport.getContext();
	}

	@Override
	protected void onPreExecute() {
		pd.setTitle("���Ե�");
		pd.setMessage("���ڵ�¼...");
		pd.show();
		super.onPreExecute();
	}

	@Override
	protected Integer doInBackground(String... params) {
		return login();
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
	}

	@Override
	protected void onPostExecute(Integer result) {
		pd.dismiss();
		switch (result) {
		case Constant.LOGIN_SECCESS: // ��¼�ɹ�
			Toast.makeText(context, "��½�ɹ�", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent();
			if (loginConfig.isFirstStart()) {// ������״�����
				//intent.setClass(context, GuideViewActivity.class);
				//loginConfig.setFirstStart(false);
			} else {
				intent.setClass(context, MainActivity1.class);
			}
			intent.setClass(context, MainActivity1.class);
			activitySupport.saveLoginConfig(loginConfig);// �����û�������Ϣ
			activitySupport.startService(); // ��ʼ���������
			context.startActivity(intent);
			break;
		case Constant.LOGIN_ERROR_ACCOUNT_PASS:// �˻������������
			Toast.makeText(
					context,
					context.getResources().getString(
							R.string.message_invalid_username_password),
					Toast.LENGTH_SHORT).show();
			break;
		case Constant.SERVER_UNAVAILABLE:// ����������ʧ��
			Toast.makeText(
					context,
					context.getResources().getString(
							R.string.message_server_unavailable),
					Toast.LENGTH_SHORT).show();
			break;
		case Constant.LOGIN_ERROR:// δ֪�쳣
			Toast.makeText(
					context,
					context.getResources().getString(
							R.string.unrecoverable_error), Toast.LENGTH_SHORT)
					.show();
			break;
		}
		super.onPostExecute(result);
	}

	// ��¼
	private Integer login() {
		String username = loginConfig.getUsername();
		String password = loginConfig.getPassword();
		//String username = "1000000";
		//String password="ee3618e08444dba8ebd6e4dbd706b0c9";
		try {
			XMPPConnection connection = XmppConnectionManager.getInstance()
					.getConnection();
				if (connection.isConnected()) {// �����ж��Ƿ������ŷ���������Ҫ�ȶϿ�
						connection.disconnect();
				}
				
				
				//SmackConfiguration.setPacketReplyTimeout(30000);// ���ó�ʱʱ��
			//	SmackConfiguration.setKeepAliveInterval(-1);
				//SmackConfiguration.setDefaultPingInterval(0);
				
			connection.connect();
			
			
			
			
			
			
		//	initServiceDiscovery();// �������������Ϣ����,������Ϣ��Ҫ��ִ���ж��Ƿ��ͳɹ�
			
			//if(connection.isConnected()){
				connection.login(username, password); // ��¼
			//}	
			// OfflineMsgManager.getInstance(activitySupport).dealOfflineMsg(connection);//����������Ϣ
			connection.sendPacket(new Presence(Presence.Type.available));
			if (loginConfig.isNovisible()) {// �����¼
				Presence presence = new Presence(Presence.Type.unavailable);
				Collection<RosterEntry> rosters = connection.getRoster()
						.getEntries();
				for (RosterEntry rosterEntry : rosters) {
					presence.setTo(rosterEntry.getUser());
					connection.sendPacket(presence);
				}
			}
			loginConfig.setUsername(username);
			if (loginConfig.isRemember()) {// ��������
				loginConfig.setPassword(password);
			} else {
				loginConfig.setPassword("");
			}
			loginConfig.setOnline(true);
			return Constant.LOGIN_SECCESS;
		} catch (Exception xee) {
			if (xee instanceof XMPPException) {
				XMPPException xe = (XMPPException) xee;
				final XMPPError error = xe.getXMPPError();
				int errorCode = 0;
				if (error != null) {
					errorCode = error.getCode();
				}
				if (errorCode == 401) {
					return Constant.LOGIN_ERROR_ACCOUNT_PASS;
				}else if (errorCode == 403) {
					return Constant.LOGIN_ERROR_ACCOUNT_PASS;
				} else {
					return Constant.SERVER_UNAVAILABLE;
				}
			} else {
				return Constant.LOGIN_ERROR;
			}
		}
	}
	
	
	
	/**
	 * �������������Ϣ����,������Ϣ��Ҫ��ִ���ж϶Է��Ƿ��Ѷ�����Ϣ
	 */
	private void initServiceDiscovery() {
		// register connection features
	/*	
		XMPPConnection connection = XmppConnectionManager.getInstance()
				.getConnection();
		
		
		ServiceDiscoveryManager sdm = ServiceDiscoveryManager
				.getInstanceFor(connection);
		if (sdm == null)
			sdm = new ServiceDiscoveryManager(connection);

		sdm.addFeature("http://jabber.org/protocol/disco#info");

		// reference PingManager, set ping flood protection to 10s
		PingManager.getInstanceFor(connection).setPingMinimumInterval(
				10 * 1000);
		// reference DeliveryReceiptManager, add listener

		DeliveryReceiptManager dm = DeliveryReceiptManager
				.getInstanceFor(connection);
		dm.enableAutoReceipts();
		dm.registerReceiptReceivedListener(new DeliveryReceiptManager.ReceiptReceivedListener() {
			public void onReceiptReceived(String fromJid, String toJid,
					String receiptId) {
				//changeMessageDeliveryStatus(receiptId, ChatConstants.DS_ACKED);// ���Ϊ�Է��Ѷ���ʵ���������˵����⣬������ʵû�����ϴ�״̬
				System.out.println(receiptId);
			}
		});*/
	}
	
	
	
	
	
	
}

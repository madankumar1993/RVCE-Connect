package com.rvceconnect.main;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;
import java.util.List;
import java.util.Properties;
import javolution.util.FastList;
import org.jwebsocket.api.WebSocketClientEvent;
import org.jwebsocket.api.WebSocketClientTokenListener;
import org.jwebsocket.api.WebSocketPacket;
import org.jwebsocket.client.plugins.rpc.Rpc;
import org.jwebsocket.client.plugins.rpc.RpcListener;
import org.jwebsocket.client.plugins.rpc.Rrpc;
import org.jwebsocket.client.token.BaseTokenClient;
import org.jwebsocket.kit.RawPacket;
import org.jwebsocket.kit.WebSocketException;
import org.jwebsocket.token.Token;


public class JWC {

	private final static int MT_OPENED = 0;
	private final static int MT_PACKET = 1;
	private final static int MT_CLOSED = 2;
	private final static int MT_TOKEN = 3;
	private final static String CONFIG_FILE = "jWebSocket";
	private static String mURL = "ws://10.0.0.4:8787/jWebSocket/jWebSocket";
	private static String username = "guest";
	private static String password = "guest";
	private static BaseTokenClient mJWC;
	private static List<WebSocketClientTokenListener> mListeners = new FastList<WebSocketClientTokenListener>();
	private static String DEF_ENCODING = "UTF-8";

	public static void init() {
		mJWC = new BaseTokenClient();
		mJWC.addListener(new Listener());
		mJWC.addListener(new RpcListener());
		//TODO: this could be improve if we use client plugins.
		Rpc.setDefaultBaseTokenClient(mJWC);
		Rrpc.setDefaultBaseTokenClient(mJWC);
	}

	public static void loadSettings(Activity aActivity) {
		Properties lProps = new Properties();
		try {
			lProps.load(aActivity.openFileInput(CONFIG_FILE));
		} catch (Exception ex) {
			Toast.makeText(aActivity.getApplicationContext(),
					ex.getClass().getSimpleName() + ":" + ex.getMessage(),
					Toast.LENGTH_SHORT).show();
		}
		mURL = (String) lProps.getProperty("url", "ws://10.0.0.4:8787/jWebSocket/jWebSocket");
		username = (String) lProps.getProperty("username", "guest");
		password = (String) lProps.getProperty("password", "guest");
	}

	@SuppressWarnings("deprecation")
	public static void saveSettings(Activity aActivity) {
		Properties lProps = new Properties();
		try {
			lProps.put("url", mURL);
			lProps.put("username", username);
			lProps.put("password", password);
			lProps.save(aActivity.openFileOutput(CONFIG_FILE, Context.MODE_PRIVATE), "jWebSocketClient Configuration");
		} catch (Exception ex) {
			Toast.makeText(aActivity.getApplicationContext(),
					ex.getClass().getSimpleName() + ":" + ex.getMessage(),
					Toast.LENGTH_SHORT).show();
		}
	}


	public static void login() throws WebSocketException {
		mJWC.login(username, password);
		}

	public static void logout() throws WebSocketException {
		mJWC.logout();
		}
	public static void open() throws WebSocketException {
		mJWC.open(mURL);
	}

	public static void close() throws WebSocketException {
		mJWC.close();
	}

	public static void send(String aString) throws WebSocketException {
		mJWC.send(mURL, DEF_ENCODING);
	}

	public static void sendToken(Token aToken) throws WebSocketException {
		mJWC.sendToken(aToken);
	}

	public static void sendText(String aTarget, String aData) throws WebSocketException {
		mJWC.sendText(aTarget, aData);

	}

	public static void broadcastText(String aData) throws WebSocketException {
		mJWC.broadcastText(aData);
	}

	public static void saveFile(byte[] aData, String aFilename, String aScope,
			Boolean aNotify) throws WebSocketException {
		mJWC.saveFile(aData, aFilename, aScope, aNotify);
	}

	public static void sendFile(String aHeader, byte[] aData, String aFilename, String aTarget)
			throws WebSocketException {
		mJWC.sendFile(aHeader, aData, aFilename, aTarget);
	}

	public static void addListener(WebSocketClientTokenListener aListener) {
		mListeners.add(aListener);
	}

	public static void removeListener(WebSocketClientTokenListener aListener) {
		mListeners.remove(aListener);
	}
	private static Handler messageHandler = new Handler() {

		@Override
		public void handleMessage(Message aMessage) {

			switch (aMessage.what) {
				case MT_OPENED:
					notifyOpened(null);
					break;
				case MT_PACKET:
					notifyPacket(null, (RawPacket) aMessage.obj);
					break;
				case MT_TOKEN:
					notifyToken(null, (Token) aMessage.obj);
					break;
				case MT_CLOSED:
					notifyClosed(null);
					break;
			}
		}
	};

	public static void notifyOpened(WebSocketClientEvent aEvent) {
		for (WebSocketClientTokenListener lListener : mListeners) {
			lListener.processOpened(aEvent);
		}
	}

	public static void notifyPacket(WebSocketClientEvent aEvent, WebSocketPacket aPacket) {
		for (WebSocketClientTokenListener lListener : mListeners) {
			lListener.processPacket(aEvent, aPacket);
		}
	}

	public static void notifyToken(WebSocketClientEvent aEvent, Token aToken) {
		for (WebSocketClientTokenListener lListener : mListeners) {
			lListener.processToken(aEvent, aToken);
		}
	}

	public static void notifyClosed(WebSocketClientEvent aEvent) {
		for (WebSocketClientTokenListener lListener : mListeners) {
			lListener.processClosed(aEvent);
		}
	}

	/**
	 * @return the URL
	 */
	public static String getURL() {
		return mURL;
	}

	/**
	 * @param aURL the URL to set
	 */
	public static void setURL(String aURL) {
		mURL = aURL;
	}

	/**
	 * @return the Username
	 */
	public static String getUsername() {
		return username;
	}

	/**
	 * @param aUser the username to set
	 */
	public static void setUsername(String aUser) {
		username = aUser;
	}

	/**
	 * @return the password
	 */
	public static String getPassword() {
		return password;
	}

	/**
	 * @param aPass the password to set
	 */
	
	public static void setPassword(String aPass) {
		password = aPass;
	}

	/**
	 * @return the mJWC
	 */
	public static BaseTokenClient getClient() {
		return mJWC;
	}

	static class Listener implements WebSocketClientTokenListener {

		@Override
		public void processOpened(WebSocketClientEvent aEvent) {
			Message lMsg = new Message();
			lMsg.what = MT_OPENED;
			messageHandler.sendMessage(lMsg);
		}

		@Override
		public void processPacket(WebSocketClientEvent aEvent, WebSocketPacket aPacket) {
			Message lMsg = new Message();
			lMsg.what = MT_PACKET;
			lMsg.obj = aPacket;
			messageHandler.sendMessage(lMsg);
		}

		@Override
		public void processToken(WebSocketClientEvent aEvent, Token aToken) {
			Message lMsg = new Message();
			lMsg.what = MT_TOKEN;
			lMsg.obj = aToken;
			messageHandler.sendMessage(lMsg);
		}

		@Override
		public void processClosed(WebSocketClientEvent aEvent) {
			Message lMsg = new Message();
			lMsg.what = MT_CLOSED;
			messageHandler.sendMessage(lMsg);
		}

		@Override
		public void processOpening(WebSocketClientEvent aEvent) {
		}

		@Override
		public void processReconnecting(WebSocketClientEvent aEvent) {
		}
	}
}

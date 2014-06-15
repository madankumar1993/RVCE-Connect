package com.rvceconnect.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jwebsocket.api.WebSocketClientEvent;
import org.jwebsocket.api.WebSocketClientTokenListener;
import org.jwebsocket.api.WebSocketPacket;
import org.jwebsocket.kit.WebSocketException;
import org.jwebsocket.packetProcessors.JSONProcessor;
import org.jwebsocket.token.Token;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;



public class ChatScreen extends Activity implements WebSocketClientTokenListener {

	private int ADD_USER = 1;
	private int REMOVE_USER=0;
	private Button lBtnSend;
	private Button lBtnBroadcast;
	private Button lBtnClearLog;
	private EditText lMessage;
	private Spinner lTarget;
	private TextView lLog;
	private Map<String, String> uidmap;
	private List<String> ulist;
	private ArrayAdapter<String> adap;
	// private SamplePlugIn lSamplePlugIn = null;

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.chatscreen_activity);
		lBtnSend = (Button) findViewById(R.id.btnFundSend);
		lBtnBroadcast = (Button) findViewById(R.id.btnFundBroadcast);
		lBtnClearLog = (Button) findViewById(R.id.btnFundClearLog);
		lMessage = (EditText) findViewById(R.id.txfFundMessage);
		lTarget = (Spinner) findViewById(R.id.lblFundTarget);
		lLog = (EditText) findViewById(R.id.lblFundLog);
		uidmap = new HashMap<String, String>();
		ulist = new ArrayList<String>();
		ulist.add("All");
		adap = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ulist);
		adap.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		lTarget.setAdapter(adap);
		
		// lSamplePlugIn = new SamplePlugIn(JWC.getClient());
		
		
		
		lBtnSend.setOnClickListener(new OnClickListener() {

			public void onClick(View aView) {
				try {
					// lSamplePlugIn.getRandom();
					String message = lMessage.getText().toString();
					String target = lTarget.getSelectedItem().toString();
					if(!message.equals("")){
						if(!target.equals("All")){ 
						//get userid by username as key
						JWC.sendText(uidmap.get(target), message);
					}
					else
						JWC.broadcastText(lMessage.getText().toString());
					}	
					lMessage.setText("");
				} catch (WebSocketException ex) {
					// TODO: handle exception
				}
			}
		});

		lBtnBroadcast.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View aView) {
				try {
					
					String message = lMessage.getText().toString();
					if(!message.equals(""))
						JWC.broadcastText(lMessage.getText().toString());

					lMessage.setText("");
				} catch (WebSocketException ex) {
					// TODO: handle exception
				}

			}
		});

		lBtnClearLog.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View aView) {
				lLog.setText("");
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		log("* opening... ");
		try {
			JWC.addListener(this);
			JWC.open();
			JWC.login();
		} catch (WebSocketException ex) {
			log("* exception: " + ex.getMessage());
		}
	}

	@Override
	protected void onPause() {
		log("* closing... ");
		try {
			JWC.logout();
			Toast.makeText(this, "Logged Out", Toast.LENGTH_SHORT).show();
			JWC.close();
			JWC.removeListener(this);
		} catch (WebSocketException ex) {
			log("* exception: " + ex.getMessage());
		}
		super.onPause();
	}

	private void log(CharSequence aString) {
		try {
			lLog.append(aString);
		} catch (Exception ex) {
			Toast.makeText(getApplicationContext(), ex.getClass().getSimpleName(),
					Toast.LENGTH_SHORT).show();
		}
	}

	public void processOpened(WebSocketClientEvent aEvent) {
		Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
		log("opened\n");
		ImageView lImgView = (ImageView) findViewById(R.id.fundImgStatus);
		if (lImgView != null) {
			// TODO: in fact it is only connected, not yet authenticated!
			lImgView.setImageResource(R.drawable.authenticated);
		}
		
	}

	@Override
	public void processPacket(WebSocketClientEvent aEvent, WebSocketPacket aPacket) {
		Token t = JSONProcessor.packetToToken(aPacket);
		if(t.getString("type").equalsIgnoreCase("send")){
			log(t.getString("sender") + " > " + t.getString("data")+" \n");
			updateUsers(t.getString("sender"), t.getString("sourceId"), ADD_USER);
		}
		else if(t.getString("type").equalsIgnoreCase("broadcast")){
			log("Broadcasted message by "+t.getString("sender") + "  > " + t.getString("data")+" \n");
			updateUsers(t.getString("sender"), t.getString("sourceId"), ADD_USER);
		}
		else if(t.getString("type").equalsIgnoreCase("event")){
			if(t.getString("name").equalsIgnoreCase("login"))
				updateUsers(t.getString("username"), t.getString("sourceId"), ADD_USER);
				//log("New User > " + t.getString("username") + " logged in with userid" + t.getString("sourceId")+" \n");
			if(t.getString("name").equalsIgnoreCase("logout"))
				updateUsers(t.getString("username"), t.getString("sourceId"), REMOVE_USER);	
				//log(t.getString("username") + " logged out with userid " + t.getString("sourceId")+" \n");
		}
		}

	
	/**
	 * Need to Change key if username conflicts
	 * @param username
	 * @param Userid
	 * @param opCode
	 */
	public void updateUsers(String username, String Userid,int opCode){
		boolean exist = false;
		for(String S:ulist){
			if(S.contentEquals(username))
				exist = true;
			}
		if(!exist && opCode==ADD_USER){
				ulist.add(username);
				uidmap.put(username, Userid);
			}
		else if(exist && opCode == REMOVE_USER){
				ulist.remove(username);
				uidmap.remove(username);
				
			}
		}
	@Override
	public void processToken(WebSocketClientEvent aEvent, Token aToken) {
		// log("> " + aToken.toString() + "\n");
	}

	@Override
	public void processClosed(WebSocketClientEvent aEvent) {
		log("closed\n");
		
		ImageView lImgView = (ImageView) findViewById(R.id.fundImgStatus);
		if (lImgView != null) {
			lImgView.setImageResource(R.drawable.disconnected);
		}
	}

	@Override
	public void processOpening(WebSocketClientEvent aEvent) {
		log("* opening... ");
		
	}

	@Override
	public void processReconnecting(WebSocketClientEvent aEvent) {
		log("* reconnecting... ");
	}
}

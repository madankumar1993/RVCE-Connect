package com.rvceconnect.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class StartScreenActivity extends Activity {
    /** Called when the activity is first created. */
    long m_dwSplashTime = 3000;
    boolean m_bPaused = false;
    boolean m_bSplashActive = true;
    @Override
    public void onCreate(Bundle icicle)
    {
     super.onCreate(icicle);
     //Draw the splash screen
     setContentView(R.layout.startscreen_activity);
     //Very simple timer thread
     Thread splashTimer = new Thread()
     {
    	 public void run()
    	  {
    		 try
    		 	{
    			 //Wait loop
    			 long ms = 0;
    			 while(m_bSplashActive && ms < m_dwSplashTime)
    			 {
    				 sleep(100);
    				 //Advance the timer only if we're running.
    				 if(!m_bPaused)
    					 ms += 100;
    			 }
    			 //Advance to the next screen.
    			 startActivity(new Intent(StartScreenActivity.this,MainActivity.class));
    		 	}
    		 catch(Exception e)
    		 	{
    			 Log.e("Splash", e.toString());
    		 	}
    		 finally
    		 {
    			 finish();
    		 }
    	  }
    };
    splashTimer.start();
    }
}
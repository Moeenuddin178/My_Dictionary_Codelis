package com.my.dictionary;

import java.util.Timer;
import java.util.TimerTask;
import com.my.dictionary.data.GlobalVariable;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Window;
import android.widget.RelativeLayout;

public class ActivitySplash extends Activity {
	
	private RelativeLayout lyt_splash; 
	private GlobalVariable global; 
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// remove title bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		//this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_splash);
		
		global = (GlobalVariable) getApplication();
		
		lyt_splash =(RelativeLayout)findViewById(R.id.lyt_splash);
		lyt_splash.setBackgroundColor(global.getIntColor());
		
		TimerTask task = new TimerTask(){
			@Override
			public void run() {

				// go to the main activity
				Intent i = new Intent(ActivitySplash.this, ActivityMain.class);
				startActivity(i);
				finish();

			}

		};

		// Schedule a task for single execution after a specified delay.
		// Show splash screen for 2 seconds
		new Timer().schedule(task, 2000);
	
	}

}

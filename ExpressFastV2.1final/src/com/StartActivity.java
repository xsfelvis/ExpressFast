package com;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.kaiji.Viewpage_Activity;
import com.weimeijing.feigeshudi.R;
public class StartActivity  extends Activity{
	    private final int SPLASH_DISPLAY_LENGHT = 2000; //延迟二秒 

	    @Override 
	    public void onCreate(Bundle savedInstanceState) { 
	        super.onCreate(savedInstanceState); 
	        setContentView(R.layout.start); 
	        //延时2秒执行run方法
	        new Handler().postDelayed(new Runnable(){ 
	         @Override 
	         public void run() { 
	        	 SharedPreferences setting = getSharedPreferences("CitiGame.ini", 0);
	        	 Boolean user_first = setting.getBoolean("FIRST",true);
	        	 if(user_first){
	        		 setting.edit().putBoolean("FIRST", false).commit();
	        		 Intent intent = new Intent(StartActivity.this,Viewpage_Activity.class);
	        		 StartActivity.this.startActivity(intent);
	 				StartActivity.this.finish();
	        	 }else{
	        		 Intent mainIntent = new Intent(StartActivity.this,MainTabActivity.class); 
		             StartActivity.this.startActivity(mainIntent); 
		             StartActivity.this.finish(); 
	        	 }
	        	 
	        	 
	        	 
	        	 
	            
	         } 
	            
	        }, SPLASH_DISPLAY_LENGHT); 
	    } 
}


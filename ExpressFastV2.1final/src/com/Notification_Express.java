package com;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bean.ExpressHistory;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.util.QueryExpressUtil;

import com.weimeijing.feigeshudi.R;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class Notification_Express extends Activity{
	TextView express_info;
	String info;
	DbUtils db;
	static List<ExpressHistory> infoList;
	  public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        //setContentView(R.layout.update);
	      //  express_info = (TextView) findViewById(R.id.info);
	        db = DbUtils.create(this);
	        try {
	        	infoList = db.findAll(Selector.from(ExpressHistory.class));
			} catch (DbException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	//Log.v("size", infoList.size());
	    	if(infoList == null || infoList.size() == 0){
				Toast.makeText(this, "当前还没有保存任何快递哦,保存后自动更新才有用哦！", Toast.LENGTH_SHORT).show();
			}else{
				for(int i= 0;i <infoList.size();i++){           //遍历快递列表
					info = infoList.get(i).getExpressCode() +"  "+infoList.get(i).getExpressNumber();
					UpdateUilt(infoList.get(i).getExpressNumber(), infoList.get(i).getExpressName(), infoList.get(i).getExpressCode(),this);
					Log.v("info", info);
				}
			}
	  }
	  public static void UpdateUilt(final String number, final String name,final String code,final Context context){
			String url;
			url = "http://api.ickd.cn/?id=102616&secret=16135ea51cb60246eff620f130a005bd&com=";
			url += code;
			url += "&nu=";
			url += number;
			url += "&type=json&encode=utf8&ord=asc";
			Log.v("tag", url);
			//HttpUtils使用方法：使用普通get方法
			HttpUtils http = new HttpUtils();
			http.send(HttpRequest.HttpMethod.GET,
					url,
				    new RequestCallBack<String>(){
				        @Override
				        public void onLoading(long total, long current, boolean isUploading) {
				        	Log.v("tag", "查询中" + current);
				        }

				        @Override
				        public void onSuccess(ResponseInfo<String> responseInfo) {
				        	try {
				        		JSONObject all = new JSONObject(responseInfo.result);
				        		String status = all.getString("status");
				        		
				        		if(status.equals("0")){ //0代表查找失败
				        			String message = all.getString("message");  //message中存储着错误消息
				        			Toast.makeText(context, message, Toast.LENGTH_LONG).show();
				        			return;
				        		}
				        		
								JSONArray jsonArray = all.getJSONArray("data");
								int length = jsonArray.length();
								Log.v("tag","长度是"+length);
								String time = jsonArray.getJSONObject(length-1).getString("time");
								Log.v("time", time);
								if(time.equals(infoList.get(infoList.size() - 1).getNewDate())){
									/*
									Bitmap btm = BitmapFactory.decodeResource(this.getResources(),
											R.drawable.msg);
									NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
											Update_Express.this).setSmallIcon(R.drawable.msg)
											.setContentTitle("您收藏的快递有更新")
											.setContentText("申通快递 已收获");
									mBuilder.setTicker("");//第一次提示消息的时候显示在通知栏上
									mBuilder.setNumber(12);
									mBuilder.setLargeIcon(btm);
									mBuilder.setAutoCancel(true);//自己维护通知的消失
									mBuilder.setDefaults(Notification.DEFAULT_ALL);
									//获取通知管理器对象
									NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
									mNotificationManager.notify(0, mBuilder.build());*/
								} 
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
				        }

						@Override
						public void onFailure(HttpException arg0, String arg1) {
							// TODO Auto-generated method stub
							
						}
			});
		}

}
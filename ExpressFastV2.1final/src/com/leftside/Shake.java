package com.leftside;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.weimeijing.feigeshudi.R;

public class Shake extends Activity {
	private SensorManager sensorManager;
	private Vibrator vibrator;
	ImageView iv;
	MediaPlayer mp;
	boolean isPlay = false;
	private static final String TAG = "TestSensorActivity";
	private static final int SENSOR_SHAKE = 10;

	private TextView tv_shake1;
	private TextView tv_shake2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shake);
		mp = MediaPlayer.create(this, R.raw.shake);
		iv = (ImageView) findViewById(R.id.imageview);
		// 获取传感器管理服务
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

		/* tv_shake1 = (TextView) findViewById(R.id.tv_shake1); */
		tv_shake2 = (TextView) findViewById(R.id.tv_shake2);

	}



	@Override
	protected void onResume() {
		super.onResume();
		if (sensorManager != null) {
			// 为系统的方向传感注册监听器
			sensorManager.registerListener(sensorEventListener,
					sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
					SensorManager.SENSOR_DELAY_NORMAL);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (sensorManager != null) {
			// 取消监听器
			sensorManager.unregisterListener(sensorEventListener);
		}
		if (isPlay) {
			mp.stop();
			mp.release();
		}
	}

	/*
	 * 重力感应监听
	 */
	private SensorEventListener sensorEventListener = new SensorEventListener() {

		@Override
		public void onSensorChanged(SensorEvent event) {
			// 传感器信息改变时执行该方法
			float[] values = event.values;
			float x = values[0];// x轴方向的重力加速度,向右为正
			float y = values[1];// y轴方向的重力加速度,向前为正
			float z = values[2];// y轴方向的重力加速度,向前为正
			// 根据结果执行操作
			if (Math.abs(x) > 14 || Math.abs(y) > 16 || Math.abs(z) > 16) {
				isPlay = true;
				// mp.seekTo(0);
				vibrator.vibrate(200);
				// Handler 异步处理技术
				Message msg = new Message();
				msg.what = SENSOR_SHAKE;
				//mp.start();// 加这个会死机？
				handler.sendMessage(msg);
				Log.i(TAG, "x轴方向的重力加速度" + x + "；y轴方向的重力加速度" + y + "；z轴方向的重力加速度"
						+ z);
			}

		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {

		}
	};
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			//mp.start();// 加这个会死机?
			switch (msg.what) {
			case SENSOR_SHAKE:
				int i = 0;
				while (true) {
					i++;
					if (i == 800)
						break;
				}
				shake_action();
				break;

			default:
				break;
			}
		}

		private void shake_action() {
			/* tv_shake1.setVisibility(View.VISIBLE); */
			int id = (int) (Math.random() * 13 + 1);
			switch (id) {
			case 1:

				tv_shake2.setText(R.string.shake1);
				break;
			case 2:
				tv_shake2.setText(R.string.shake2);
				break;
			case 3:
				tv_shake2.setText(R.string.shake3);
				break;
			case 4:
				tv_shake2.setText(R.string.shake4);
				break;
			case 5:
				tv_shake2.setText(R.string.shake5);
				break;
			case 6:
				tv_shake2.setText(R.string.shake6);
				break;
			case 7:
				tv_shake2.setText(R.string.shake7);
				break;
			case 8:
				tv_shake2.setText(R.string.shake8);
				break;
			case 9:
				tv_shake2.setText(R.string.shake9);
				break;
			case 10:
				tv_shake2.setText(R.string.shake10);
				break;
			case 11:
				tv_shake2.setText(R.string.shake11);
				break;
			case 12:
				tv_shake2.setText(R.string.shake12);
				break;
			case 13:
				tv_shake2.setText(R.string.shake13);
				break;

			default:
				break;
			}
			tv_shake2.setVisibility(View.VISIBLE);

		}
	};

}

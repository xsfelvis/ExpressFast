package com;

import com.adapter.ListViewAdapter;

import com.weimeijing.feigeshudi.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 快递列表list使用listView 适配器选用baseAdapter
 * 
 * @author ELVIS
 * 
 */
public class ExpressList extends Activity {
	private ListView lv_common;
	//图标
	private final int[] images = new int[] { R.drawable.shengtong,
			R.drawable.ems1, R.drawable.shunfeng, R.drawable.yuantong,
			R.drawable.zhongtong, R.drawable.yunda, R.drawable.tiantian,
			R.drawable.huitong, R.drawable.quanfeng, R.drawable.debang,
			R.drawable.zhaijisong };

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_all);
		// 通过getResources获取系统资源
		final String[] all_name = getResources().getStringArray(R.array.common);
		final String[] all_code = getResources().getStringArray(
				R.array.common_code);
		
		// 获取listView控件
		lv_common = (ListView) findViewById(R.id.lv_all);
		// 设置adapter
		lv_common.setAdapter(new ListViewAdapter(this, all_name,images));
		// 添加监听器
		lv_common.setOnItemClickListener(new OnItemClickListener() {
			// 点击listview条目选择，并将对应位置的名称和快递拼音放到intent中作为回传
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 隐式intent需要解析
				Intent data = new Intent();
				data.putExtra("code", all_code[position]);
				data.putExtra("name", all_name[position]);
				// 回调函数setResut(int resultCode, Intent intent)
				setResult(0, data);
				/*
				 * 在setResult后，要调用finish()销毁当前的Activity，否则无法返回到原来的Activity，
				 * 就无法执行原来Activity的onActivityResult函数，看到当前的Activity没反应。
				 */
				finish();
			}

		});

	}

}

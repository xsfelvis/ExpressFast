package com.leftside;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.weimeijing.feigeshudi.R;

public class ConnectKefu extends Activity {
	// 描述
	/*
	 * private String[] names = new String[] { "申通", "EMS", "顺丰", "圆通", "中通",
	 * "韵达", "天天", "汇通", "全峰", "德邦", "宅急送" };
	 */
	private final String[] names = new String[] { "申通  官方客服", "EMS 官方客服", "顺丰  官方客服",
			"圆通  官方客服", "中通  官方客服","韵达  官方客服","天天  官方客服","汇通  官方客服","全峰  官方客服","德邦  官方客服","宅急送  官方客服" };
	private final String[] phonenum = new String[] { "95548", "11183", "95338",
			"021-69777888", "400-827-0270","400-821-6789","400-188-8888","400-956-5656","400-100-0001","95353","400-6789-0000" };
	// 图标
	private final int[] imageIDs = new int[] { R.drawable.shengtong,
			R.drawable.ems1,
			R.drawable.shunfeng,
			R.drawable.yuantong,
			R.drawable.zhongtong,
			R.drawable.yunda,
			R.drawable.tiantian,
			R.drawable.huitong,
			R.drawable.quanfeng,
			R.drawable.debang,
			R.drawable.zhaijisong};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.kefu_listview);
		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
		// for循环添加
		for (int i = 0; i < names.length; i++) {
			Map<String, Object> listItem = new HashMap<String, Object>();
			listItem.put("header", imageIDs[i]);
			// listItem.put("name", names[i]);
			listItem.put("name", names[i]);
			listItem.put("num", phonenum[i]);
			listItems.add(listItem);
		}
		// 创建一个SimpleAdapter
		SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems,
				R.layout.connectkefu_simplelist, new String[] { "name",
						"header", "num" }, new int[] { R.id.name, R.id.header,
						R.id.phonenum });
		ListView list = (ListView) findViewById(R.id.kefu_list);
		// 为ListView设置Adapter
		list.setAdapter(simpleAdapter);
	}
}

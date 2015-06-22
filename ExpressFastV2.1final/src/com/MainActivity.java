package com;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.baidu.mapapi.map.BaiduMap;
import com.leftside.AboutUs;
import com.leftside.ConnectKefu;
import com.leftside.Shake;
import com.map.Baidu;
import com.util.NetUilt;
import com.util.QueryExpressUtil;
import com.weimeijing.feigeshudi.R;
import com.zxing.activity.CaptureActivity;

//在初始完成对监听器的继承，这样便于统一管理控件监听处理
public class MainActivity extends Fragment implements OnClickListener {
	private Button btn_main_reset;
	private EditText tv_main_express_number;
	private TextView tv_main_express_name;
	private Button btn_main_search;
	private String code = "";
	private ProgressDialog progressDialog;
	private ImageView tv_scane_express_number;
	// 切换侧滑选项按钮
	/* private SlidingMenu mLeftMenu ; */
	// 快递模块
	// 描述
	private final String[] names = new String[] { "附近快递点", "摇一摇", "联系客服",
			"关于我们" };
	// test
	private final int[] namesIM = new int[] { R.drawable.left_kuaidi1,
			R.drawable.left_shake1, R.drawable.left_kefu1,
			R.drawable.left_aboutus1, };
	// 图标
	private final int[] imageIDs = new int[] { R.drawable.left_map2,
			R.drawable.left_shake2, R.drawable.left_connekefy2, R.drawable.left_aboutus2};

	/* 实现fragment需要使用Fragment时，需要继承Fragment或者Fragment的子类 */
	// onCreateView()中container参数代表该Fragment在Activity中的父控件；savedInstanceState提供了上一个实例的数据
	// 为Fragment加载布局时调用
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// 为这个 fragment加载 activity_main布局文件
		/*
		 * 第一个是resource ID，指明了当前的Fragment对应的资源文件；第二个参数是父容器控件；
		 * 第三个布尔值参数表明是否连接该布局和其父容器控件
		 * ，在这里的情况设置为false，因为系统已经插入了这个布局到父控件，设置为true将会产生多余的一个View Group。
		 */
		return inflater.inflate(R.layout.activity_main, null);

	}

	// 当Activity中的onCreate方法执行时调用
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		/* getActivity()获取当前fragment所附件的Activity */
		boolean isNetwork = NetUilt.checkNet(getActivity());// 获取网络信息
		if (!isNetwork) {
			// setNetwork();
			Toast.makeText(getActivity(), "网络连接不可用，请检查您的网络！", Toast.LENGTH_LONG)
					.show();
		}

		tv_main_express_number = (EditText) getView().findViewById(
				R.id.tv_main_express_number);// activity_main的输入快递单号

		btn_main_reset = (Button) getView().findViewById(R.id.btn_main_reset);// activity_main的重置按钮
		btn_main_reset.setOnClickListener(MainActivity.this);// 设置监听
		// tv_main_express_number.setText("");

		tv_scane_express_number = (ImageView) getView().findViewById(
				R.id.scane_express_number);// activity_main扫一扫图案
		tv_scane_express_number.setOnClickListener(this);// 设置监听

		btn_main_search = (Button) getView().findViewById(R.id.btn_main_search);// activity_main查询按钮
		btn_main_search.setOnClickListener(this);// 设置监听

		tv_main_express_name = (TextView) getView().findViewById(
				R.id.tv_main_express_name);// 输入快递名称
		tv_main_express_name.setOnClickListener(this);

		// 获取点击切换控件(也可以侧滑)
		/*
		 * mLeftMenu = (SlidingMenu) getView().findViewById(R.id.id_menu);
		 * mLeftMenu.setOnClickListener(this);
		 */
		// listview部分

		// 创建一个List集合,元素为map
		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
		// for循环添加
		for (int i = 0; i < names.length; i++) {
			Map<String, Object> listItem = new HashMap<String, Object>();
			listItem.put("header", imageIDs[i]);
			// listItem.put("name", names[i]);
			listItem.put("name", namesIM[i]);
			listItems.add(listItem);
		}
		// 创建一个SimpleAdapter
		SimpleAdapter simpleAdapter = new SimpleAdapter(this.getActivity(),
				listItems, R.layout.simple_item, new String[] { "name",
						"header" }, new int[] { R.id.name, R.id.header });
		ListView list = (ListView) getView().findViewById(R.id.leftslide_list);
		// 为ListView设置Adapter
		list.setAdapter(simpleAdapter);
		list.setOnItemClickListener(new OnItemClickListener() {

			@SuppressLint("NewApi") @Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:
					Intent intent0 = new Intent(
							MainActivity.this.getActivity(), Baidu.class);
					startActivity(intent0);
					break;
				case 1:
					Intent intent1 = new Intent(
							MainActivity.this.getActivity(), Shake.class);
					startActivity(intent1);
					/*MainActivity.this.getActivity().overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);*/
					break;
				case 2:
					Intent intent2 = new Intent(
							MainActivity.this.getActivity(), ConnectKefu.class);
					startActivity(intent2);
					
					break;
				case 3:
					Intent intent3 = new Intent(
							MainActivity.this.getActivity(), AboutUs.class);
					startActivity(intent3);
					break;
				/*
				 * case 4: mLeftMenu.toggle(); break;
				 */

				default:
					break;
				}

			}
		});

	}

	/* 将监听放在一起统一处理 */
	public void onClick(View v) {

		switch (v.getId()) {
		// 获取视图的ID进行判断从而做出相应的跳转，获取name参数
		case R.id.scane_express_number:
			Intent openCameraIntent = new Intent(getActivity(),
					CaptureActivity.class);
			startActivityForResult(openCameraIntent, 0); // 0跟下面的onActivityResult方法对应,表明触发类型

		case R.id.btn_main_reset:
			// 重置按钮，将快递单号重置为空
			tv_main_express_number.setText("");
			break;
		case R.id.tv_main_express_name:
			// 跳转到快递公司选择界面Activity，获取name参数
			/*
			 * ExpressList中通过getResources()获取string_city_code.xml文件中的定义好的快递公司名称数组
			 * 监听listview的选项item，获取快递公司名称name和对应拼音code封装在intent中回传过来
			 * 在onActivityResult根据其requestcode处理提取
			 */
			Intent intent = new Intent(getActivity(), ExpressList.class);
			startActivityForResult(intent, 1);
			break;

		case R.id.btn_main_search:
			// 装化成为字符串进行比较判断
			String name = tv_main_express_name.getText().toString();
			if (name.equals("")) {
				Toast.makeText(getActivity(), "请选择快递公司", Toast.LENGTH_SHORT)
						.show();
			} else {
				String number = tv_main_express_number.getText().toString();
				if (number.equals("")) {
					Toast.makeText(getActivity(), "快递号码不能为空",
							Toast.LENGTH_SHORT).show();
				} else {
					// searchForJson(number,name);
					// 在对应activity中创建ProgressDialog
					progressDialog = new ProgressDialog(getActivity());
					// 核心所在！！！
					/*
					 * 输入参数含义分别为： number：快递单号 name：快递名称 code：快递拼音名称
					 * getActivity()当前Activity/fragment progressDialog控件的名称
					 */
					/*
					 * 使用Xutil框架中的HttpUtils
					 * 解析爱查快递提供信息使用Parcelable序列化方式将data和context保存到listInfo
					 * ,使用intent跳转到ExpressInfoActivity
					 */
					QueryExpressUtil.queryExpressForNumber(number, name, code,
							getActivity(), progressDialog);
				}

			}
			break;
		default:
			break;
		}
	}

	/**
	 * onActivityResult接收返回的数据/结果的处理函数
	 * 这里的requestCode就是startActivityForResult的requestCode，
	 * resultCode就是setResult里面的resultCode， 返回的数据在data里面。
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data == null) {
			return;
		}
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) { // 说明是快递选择触发的
			/*
			 * 处理ExpressList回传来的点击选择的对应的code/name 将回传信息填写到对应的文本控件中
			 */
			code = data.getStringExtra("code");
			String name = data.getStringExtra("name");
			tv_main_express_name.setText(name);
			tv_main_express_number.setText("");

		} else if (requestCode == 0) { // 扫描按钮触发的
			Bundle bundle = data.getExtras();
			String scanResult = bundle.getString("result");
			tv_main_express_number.setText(scanResult);
		}

	}

	// 设置网络的函数
	public void setNetwork() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setTitle(R.string.netstate);
		builder.setMessage(R.string.setnetwork);
		builder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Intent mIntent = new Intent("/");
						ComponentName comp = new ComponentName(
								"com.android.settings",
								"com.android.settings.WirelessSettings");
						mIntent.setComponent(comp);
						mIntent.setAction("android.intent.action.VIEW");
						startActivity(mIntent);
					}
				});
		builder.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		builder.create();
		builder.show();
	}
}

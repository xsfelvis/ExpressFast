package com;

import java.util.ArrayList;
import java.util.List;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.weimeijing.feigeshudi.R;
import com.adapter.MyExpressAdapter;
import com.bean.ExpressHistory;
import com.util.QueryExpressUtil;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MyExpressActivity extends Fragment {
	private ListView myExpress;
	private List<ExpressHistory> infoList;
	private ProgressDialog progressDialog;
	MyExpressAdapter adapter;
	private ExpressHistory isExist;
	DbUtils db;
	private String number;
	private TextView count;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.activity_my_express, container, false);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		db = DbUtils.create(getActivity());
		myExpress = (ListView) getView().findViewById(R.id.lv_my_express);
		// count计数
		count = (TextView) getView().findViewById(R.id.count);
		try {
			infoList = db.findAll(Selector.from(ExpressHistory.class));
			if (infoList == null) {
				count.setText("0");
			} else {
				count.setText("" + infoList.size());
			}
		} catch (DbException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		myExpress.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Log.v("setOnItemClickListener", "setOnItemClickListener");
				progressDialog = new ProgressDialog(getActivity());
				number = infoList.get(position).getExpressNumber();
				String name = infoList.get(position).getExpressName();
				String code = infoList.get(position).getExpressCode();
				QueryExpressUtil.queryExpressForNumber(number, name, code,
						getActivity(), progressDialog);

			}
		});
		myExpress.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				// TODO Auto-generated method stub
				Log.v("setOnItemLongClickListener",
						"setOnItemLongClickListener");
				new AlertDialog.Builder(getActivity())
						.setTitle("操作")
						.setItems(R.array.arrcontent,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										final String[] PK = getResources()
												.getStringArray(
														R.array.arrcontent);
										if (PK[which].equals("修改备注")) {
											final EditText editText = new EditText(
													getActivity());
											new AlertDialog.Builder(
													getActivity())
													.setTitle("请输入备注名称")
													.setIcon(
															android.R.drawable.ic_dialog_info)
													.setView(editText)
													.setPositiveButton(
															"确定",
															new DialogInterface.OnClickListener() {

																@Override
																public void onClick(
																		DialogInterface dialog,
																		int which) {
																	number = infoList
																			.get(position)
																			.getExpressNumber();
																	// TODO
																	// Auto-generated
																	// method
																	// stub
																	try {
																		isExist = db
																				.findFirst(Selector
																						.from(ExpressHistory.class)
																						.where("expressnumber",
																								"=",
																								number));
																	} catch (DbException e1) {
																		// TODO
																		// Auto-generated
																		// catch
																		// block
																		e1.printStackTrace();
																	}
																	isExist.setName(editText
																			.getText()
																			.toString());
																	try {
																		db.update(isExist);
																	} catch (DbException e1) {
																		// TODO
																		// Auto-generated
																		// catch
																		// block
																		e1.printStackTrace();
																	}
																	adapter = (MyExpressAdapter) myExpress
																			.getAdapter();
																	adapter.notifyDataSetChanged();// 实现数据的实时刷新
																}
															})
													.setNegativeButton(
															"取消",
															new DialogInterface.OnClickListener() {
																@Override
																public void onClick(
																		DialogInterface dialog,
																		int which) {
																	// TODO
																	// Auto-generated
																	// method
																	// stub
																	dialog.cancel();
																}
															}).create().show();

										}
										if (PK[which].equals("删除")) {
											String number = infoList.get(
													position)
													.getExpressNumber();
											Log.v("number", number);
											try {
												isExist = db
														.findFirst(Selector
																.from(ExpressHistory.class)
																.where("expressnumber",
																		"=",
																		number));
											} catch (DbException e1) {
												// TODO Auto-generated catch
												// block
												e1.printStackTrace();
											}
											try {
												db.deleteById(
														ExpressHistory.class,
														isExist.getId());
											} catch (DbException e) {
												// TODO Auto-generated catch
												// block
												e.printStackTrace();
											}
											infoList.remove(position);
											adapter = (MyExpressAdapter) myExpress
													.getAdapter();
											if (!adapter.isEmpty()) {
												adapter.notifyDataSetChanged(); // 实现数据的实时刷新
											}
											Toast.makeText(getActivity(),
													PK[which] + "成功",
													Toast.LENGTH_LONG).show();
										}
										if (PK[which].equals("通过短信发送")) {
											Uri uri = Uri.parse("smsto:");

											Intent intent = new Intent(
													Intent.ACTION_SENDTO, uri);

											intent.putExtra(
													"sms_body",
													infoList.get(position)
															.getNewDate()
															+ "  "
															+ infoList
																	.get(position)
																	.getNewInfo());

											startActivity(intent);
										}
									}
								})
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub

									}
								}).show();
				return true;

			}

		});
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		DbUtils db = DbUtils.create(getActivity());
		// List<ExpressHistory> infoList = null;
		try {
			infoList = db.findAll(ExpressHistory.class);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (infoList == null || infoList.size() == 0) {
			Toast.makeText(getActivity(), "当前还没有保存任何快递哦,保存后再来这里查看吧！",
					Toast.LENGTH_SHORT).show();
		} else {
			MyExpressAdapter adapter = new MyExpressAdapter(getActivity(),
					(ArrayList<ExpressHistory>) infoList);
			myExpress.setAdapter(adapter);
		}

	}

	@Override
	public void onPause() {
		super.onPause();
	}

}
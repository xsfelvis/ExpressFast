package com.util;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.ExpressInfoActivity;
import com.bean.ExpressInfo;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.weimeijing.feigeshudi.R;

/*快递查询的核心所在*/
/*使用xUtil开发框架
 * DbUtils模块
 * ViewUtils模块
 * HttpUtils模块
 * BitmapUtils模块
 * */
public class QueryExpressUtil {
	/*
	 * 这里采用了爱查快递的api 按照其标准输入关键字段code/number 返回数据位json格式
	 */
	private static final String STATE_RECEIVED = "3";
	private static final String STATE_ON_PASSAGE = "2";
	private static final String STATE_FAIL = "0";

	public static void queryExpressForNumber(final String number,
			final String name, final String code, final Context context,
			final ProgressDialog progressDialog) {
		String url;
		url = "http://api.ickd.cn/?id=102616&secret=16135ea51cb60246eff620f130a005bd&com=";
		url += code;
		url += "&nu=";
		url += number;
		url += "&type=json&encode=utf8&ord=asc";
		Log.v("tag", url);// 测试log
		// HttpUtils使用方法：使用普通get方法
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.GET, url,
				new RequestCallBack<String>() {
					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						Log.v("tag", "查询中" + current);
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						try {
							JSONObject all = new JSONObject(responseInfo.result);// 获取所有的json对象
							String status = all.getString("status");// 快递状态信息
																	// 0失败,1正常,2派送中,3已签收,4退回

							if (status.equals(STATE_FAIL)) { // 0代表查找失败
								String message = all.getString("message"); // message中存储着错误消息
								Toast.makeText(context, message,
										Toast.LENGTH_LONG).show();
								// 关闭ProgressDialog
								progressDialog.dismiss();
								return;
							}
							// 通过inflate加载aty_express_info文件的控件
						
							// jsonArray按照爱查快递解析出来data(time/context)其中context就是最重要的快递中转信息
							JSONArray jsonArray = all.getJSONArray("data");
							int length = jsonArray.length();
							Log.v("tag", "长度是" + length);
							// 定义调转意图为快递详细信息Activity
							Intent intent = new Intent(context,
									ExpressInfoActivity.class);

							// 将快递信息封装到序列化的对象信息中
							ArrayList<ExpressInfo> infoList = new ArrayList<ExpressInfo>();
							/*
							 * 扫描数据信息，按照json格式读取快递时间和快递信息，填入到infoList中，
							 * ExpressInfo采用android常用的Parcelable来进行序列化
							 * 将序列化的数据存放到infolist中
							 */
							for (int i = 0; i < length; i++) {
								ExpressInfo expressInfo = new ExpressInfo();
								String time = jsonArray.getJSONObject(i)
										.getString("time");
								String context = jsonArray.getJSONObject(i)
										.getString("context");

								expressInfo.time = time;
								expressInfo.context = context;

								infoList.add(expressInfo);
							}

							// 利用Bundle回传数据
							Bundle bundle = new Bundle();
							bundle.putParcelableArrayList("infos", infoList);// intent中传递序列化对象
							
							intent.putExtras(bundle);// 将序列化的bundle信息放入intent中进行传递
							intent.putExtra("state", status);//快递状态
							intent.putExtra("name", name);// 快递名
							intent.putExtra("number", number);// 快递编号
							intent.putExtra("code", code);// 快递拼音
							// 关闭ProgressDialog
							progressDialog.dismiss();

							// 将回传数据
							context.startActivity(intent);// 跳转到ExpressInfoActivity.class

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onStart() {
						progressDialog.setMessage("正在查询，请稍候.....");
						progressDialog.setTitle("提示");
						progressDialog.setCancelable(false);// 设置进度条是否可以按退回键取消
						progressDialog
								.setProgressStyle(ProgressDialog.STYLE_SPINNER);
						progressDialog.show();
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						Log.v("tag", "卧槽，失败了" + msg);
						progressDialog.dismiss();
						Toast.makeText(context, "网络状况不好，检查个网络先。。。",
								Toast.LENGTH_SHORT).show();
					}
				});
	}
}

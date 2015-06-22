package com.adapter;

import java.util.ArrayList;

import com.weimeijing.feigeshudi.R;
import com.bean.ExpressHistory;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
/**
 * 收藏快递信息的保存处理
 * @author ELVIS
 *
 */
public class MyExpressAdapter extends BaseAdapter {
	private ArrayList<ExpressHistory> lists;
	private Context mContext;  
    private LayoutInflater mInflater;
	public MyExpressAdapter(Context mContext,ArrayList<ExpressHistory> lists) {
		// TODO Auto-generated constructor stub
		this.mContext = mContext;
		mInflater = LayoutInflater.from(this.mContext);
		this.lists = lists;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lists.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return lists.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	static class ViewHolder {
		TextView remarkName;
		TextView expressNameAndNumber;
		TextView newInfo;
		TextView newTime;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		 ViewHolder holder;
		 
		 if (convertView == null) {
			 convertView = mInflater.inflate(R.layout.my_express_item,parent, false);
			 holder = new ViewHolder();
			 holder.remarkName = (TextView) convertView.findViewById(R.id.tv_remark_name);
			 holder.expressNameAndNumber = (TextView) convertView.findViewById(R.id.tv_express_name_number);
			 holder.newInfo = (TextView) convertView.findViewById(R.id.tv_new_context);
			 holder.newTime = (TextView) convertView.findViewById(R.id.tv_new_time);
			 convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.remarkName.setText(lists.get(position).getName());
		holder.expressNameAndNumber.setText(lists.get(position).getExpressName() + "  " + lists.get(position).getExpressNumber());
		holder.newInfo.setText(lists.get(position).getNewInfo());
		holder.newTime.setText(lists.get(position).getNewDate());
		return convertView;
	}
}

package com.adapter;

import com.weimeijing.feigeshudi.R;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListViewAdapter extends BaseAdapter {
	private String[] names;
	private int[] imgs;
	private Context mContext;  
    private LayoutInflater mInflater;
	public ListViewAdapter(Context mContext,String[] names,int[] imgs) {
		// TODO Auto-generated constructor stub
		this.mContext = mContext;
		mInflater = LayoutInflater.from(this.mContext);
		this.names = names;
		this.imgs = imgs;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return names.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	static class ViewHolder {
		TextView text;
		ImageView img;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		 ViewHolder holder;

		 if (convertView == null) {
			 convertView = mInflater.inflate(R.layout.list_view_item,parent, false);
			 holder = new ViewHolder();
			 holder.text = (TextView) convertView.findViewById(R.id.tv_list_item_title);
			 holder.img = (ImageView) convertView.findViewById(R.id.list_img);
			 convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.text.setText(names[position]);
		holder.img.setImageResource(imgs[position]);
		holder.text.setTextSize(30);
		return convertView;
	}

}

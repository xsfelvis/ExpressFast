package com.kaiji;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageView;

import com.MainTabActivity;
import com.weimeijing.feigeshudi.R;


//导航界面
public class Viewpage_Activity extends Activity{
	private ViewPager viewPager;
	private ImageView imageView;
	//创建一个数组用来存放页面显示的view
	private ArrayList<View> pageViews;
	//创建一个数组存放导航小圆点
	private ImageView[] imageViews;
	//装显示图片的viewgroup
	private ViewGroup viewPictures;
	//装显示小圆点的viewgroup
	private ViewGroup viewPoints;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//启用窗体的扩展特性此处为没有标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//用来获取整个View并返回
		LayoutInflater inflater = getLayoutInflater();
		pageViews = new ArrayList<View>();
		pageViews.add(inflater.inflate(R.layout.activity_viewpage1, null));
		pageViews.add(inflater.inflate(R.layout.activity_viewpage2, null));
		pageViews.add(inflater.inflate(R.layout.activity_viewpage3, null));
		pageViews.add(inflater.inflate(R.layout.activity_viewpage4, null));
		pageViews.add(inflater.inflate(R.layout.activity_viewpage5, null));
		pageViews.add(inflater.inflate(R.layout.activity_viewpage6, null));
		//小圆点数大小是图片的个数
		imageViews = new ImageView[pageViews.size()];
		//从指定xml文件中加载视图
		viewPictures = (ViewGroup) inflater.inflate(R.layout.activity_viewpagers, null);
		viewPager = (ViewPager) viewPictures.findViewById(R.id.guidePagers);
		viewPoints = (ViewGroup) viewPictures.findViewById(R.id.viewPoints);
		//添加小圆点导航图片
		for(int i=0;i<pageViews.size();i++){
			imageView = new ImageView(Viewpage_Activity.this);
			imageView.setLayoutParams(new LayoutParams(20, 20));
			imageView.setPadding(5, 0, 5, 0);
			//把小圆点放进数组
			imageViews[i] = imageView;
			// 默认选中的是第一张图片，此时第一个小圆点是选中状态，其他不是
			if(i==0)
				imageViews[i].setImageDrawable(getResources().getDrawable
						      (R.drawable.page_indicator_focused));
			else
				imageViews[i].setImageDrawable(getResources().getDrawable
					      (R.drawable.page_indicator_unfocused));
			//将imageViews添加到小圆点视图组
			viewPoints.addView(imageViews[i]);
		}
		   setContentView(viewPictures);
		   viewPager.setAdapter(new NavigationAdapter());
		// 为viewpager添加监听，当view发生变化时的响应
		   viewPager.setOnPageChangeListener(new NavigationPageChangeListener());
	}
		//导航view的适配器
	class NavigationAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			return pageViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		// 初始化每个Item
		@Override
		public Object instantiateItem(View container, int position) {
			((ViewPager) container).addView(pageViews.get(position));
			return pageViews.get(position);
		}

		// 销毁每个Item
		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView(pageViews.get(position));
		}
	}
	// viewpager的监听器，主要是onPageSelected要实现
	class NavigationPageChangeListener implements OnPageChangeListener{

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int position) {
			// 循环主要是控制导航中每个小圆点的状态
			for (int i = 0; i < imageViews.length; i++) {
				// 当前view下设置小圆点为选中状态
				imageViews[i].setImageDrawable(getResources().getDrawable(
						R.drawable.page_indicator_focused));
				// 其余设置为飞选中状态
				if (position != i)
					imageViews[i].setImageDrawable(getResources().getDrawable(
							R.drawable.page_indicator_unfocused));
			}
		}
		
	}
	
	// 开始按钮方法，开始按钮在XML文件中onClick属性设置；
		// 我试图把按钮在本activity中实例化并设置点击监听，但总是报错，使用这个方法后没有报错，原因没找到
		public void startbutton(View v) {
			Intent intent = new Intent(Viewpage_Activity.this,MainTabActivity.class);
			startActivity(intent);
			Viewpage_Activity.this.finish();
		}

}

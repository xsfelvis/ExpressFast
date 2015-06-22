package com.map;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;
import com.map.MyOrientationListener.OnOrientationListener;
import com.weimeijing.feigeshudi.R;

public class Baidu extends Activity {
	// 定义所需要的控件
	private MapView mMapView = null;
	private BaiduMap mBaiduMap = null;
	private Context context = null;
	// 定位相关的控件
	private LocationClient mLocationClient;
	private MyLocationListener mLocationListener;
	private boolean isFirstIn = true;
	// 记录经纬度
	private double mLatitude;
	private double mLongtitude;
	// 自定义定位图标
	private BitmapDescriptor mIconLocation;
	// 方向传感器
	private MyOrientationListener myOrientationListener;
	private float mCurrentX;
	private LocationMode mLocationMode;
	// poi
	private PoiSearch mPoiSearch = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 去除titlebar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 在使用SDK各组件之前初始化context信息，传入ApplicationContext
		// 注意该方法要再setContentView方法之前实现
		SDKInitializer.initialize(getApplicationContext());
		// 加载地图布局文件
		setContentView(R.layout.baidumap);
		this.context = this;
		// 初始化定位
		iniLocation();
		// 获取地图控件引用
		initView();

		mBaiduMap.setMyLocationEnabled(true);
		if (!mLocationClient.isStarted())
			// 开启定位
			mLocationClient.start();
		// 开启方向传感器
		myOrientationListener.start();

	}

	private void iniLocation() {
		// 默认是普通模式
		mLocationMode = LocationMode.NORMAL;
		mLocationClient = new LocationClient(this);
		mLocationListener = new MyLocationListener();
		// 注册监听
		mLocationClient.registerLocationListener(mLocationListener);
		// 设置配置
		LocationClientOption option = new LocationClientOption();
		Toast.makeText(Baidu.this, "将显示您附近3000米内的快递点", Toast.LENGTH_LONG)
				.show();
		// 设置左边类型
		option.setCoorType("bd09ll");
		option.setIsNeedAddress(true);// 返回定位结果信息
		option.setOpenGps(true);
		// 设置一秒产生一次请求
		option.setScanSpan(2000);
		mLocationClient.setLocOption(option);
		// 初始化定位图标
		mIconLocation = BitmapDescriptorFactory
				.fromResource(R.drawable.navi_map_gps_locked);
		// 方向箭头监听
		myOrientationListener = new MyOrientationListener(context);
		myOrientationListener
				.setOnOrientationListener(new OnOrientationListener() {
					@Override
					public void onOrientationChanged(float x) {
						mCurrentX = x;
					}
				});

	}

	private void initView() {
		mMapView = (MapView) findViewById(R.id.id_bmapView);
		// 获得到地图
		mBaiduMap = mMapView.getMap();
		// 设置地图显示比例
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomBy(5.0f);// 100米左右
		mBaiduMap.setMapStatus(msu);

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		mPoiSearch.destroy();
		super.onDestroy();
		mMapView.onDestroy();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mMapView.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		/*
		 * mBaiduMap.setMyLocationEnabled(true); if
		 * (!mLocationClient.isStarted()) // 开启定位 mLocationClient.start(); //
		 * 开启方向传感器 myOrientationListener.start();
		 */
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mMapView.onPause();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		// 停止定位
		mBaiduMap.setMyLocationEnabled(false);
		mLocationClient.stop();
		// 停止方向传感器
		myOrientationListener.stop();

	}

	// 按钮菜单
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	// 菜单按钮的选择
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// 通过Item的ID选择用户选择按钮
		switch (item.getItemId()) {
		case R.id.id_map_commom:
			// 加载地图为普通模式
			mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
			break;

		case R.id.id_map_site:
			mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
			break;
		/*
		 * case R.id.id_map_location: centerToMyLocation(); break;
		 */
		case R.id.id_map_mode_common:
			mLocationMode = LocationMode.NORMAL;
			break;
		/*
		 * case R.id.id_map_mode_following: mLocationMode =
		 * LocationMode.FOLLOWING; break;
		 */
		case R.id.id_map_mode_compass:
			mLocationMode = LocationMode.COMPASS;
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	// 定位到我的位置
	private void centerToMyLocation() {
		LatLng latLng = new LatLng(mLatitude, mLongtitude);
		MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
		mBaiduMap.animateMapStatus(msu);
	}

	private class MyLocationListener implements BDLocationListener {

		// 定位成功之后的回调函数
		@Override
		public void onReceiveLocation(BDLocation location) {
			MyLocationData data = new MyLocationData.Builder() // 建造者模式
					.direction(mCurrentX)//
					.accuracy(location.getRadius())//
					.latitude(location.getLatitude())//
					.longitude(location.getLongitude())//
					.build();

			mBaiduMap.setMyLocationData(data);
			// 设置自定义图标
			MyLocationConfiguration config = new MyLocationConfiguration(
					mLocationMode, true, mIconLocation);
			mBaiduMap.setMyLocationConfigeration(config);

			// 更新经纬度
			mLatitude = location.getLatitude();
			mLongtitude = location.getLongitude();
			// 初始化Poi搜索
			mPoiSearch = PoiSearch.newInstance();
			mPoiSearch
					.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
						@Override
						public void onGetPoiResult(PoiResult result) {
							if (result == null
									|| result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
								return;
							}
							// 找到则显示覆盖物
							if (result.error == SearchResult.ERRORNO.NO_ERROR) {
								mBaiduMap.clear();
								PoiOverlay overlay = new MyPoiOverlay(mBaiduMap);
								mBaiduMap.setOnMarkerClickListener(overlay);
								overlay.setData(result);
								overlay.addToMap();
								overlay.zoomToSpan();
								return;
							}
						}

						// 点击覆盖物
						@Override
						public void onGetPoiDetailResult(PoiDetailResult result) {
							if (result.error != SearchResult.ERRORNO.NO_ERROR) {
								Toast.makeText(Baidu.this, "抱歉，未找到结果",
										Toast.LENGTH_SHORT).show();
							} else {
								Toast.makeText(
										Baidu.this,
										result.getName() + ": "
												+ result.getAddress(),
										Toast.LENGTH_SHORT).show();

								/*
								 * Toast.makeText(Baidu.this,
								 * "将显示您附近5000米内的快递点",
								 * Toast.LENGTH_LONG).show();
								 */

							}
						}
					});
			// mPoiSearch.setOnGetPoiSearchResultListener(this);
			/*
			 * LatLng latLng1 = new LatLng(location.getLatitude(),
			 * location.getLongitude());// 获取经纬度
			 */

			// 判断用户是否第一次进入地图
			if (isFirstIn) {
				LatLng latLng = new LatLng(location.getLatitude(),
						location.getLongitude());// 获取经纬度
				MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
				mBaiduMap.animateMapStatus(msu);
				isFirstIn = false;
				Toast.makeText(context, location.getAddrStr(),
						Toast.LENGTH_SHORT).show();
				mPoiSearch
						.searchNearby((new PoiNearbySearchOption())
								.location(latLng).keyword("快递").radius(3000)
								.pageNum(0));
			}
		}
	}

	private class MyPoiOverlay extends PoiOverlay {

		public MyPoiOverlay(BaiduMap baiduMap) {
			super(baiduMap);
			// TODO Auto-generated constructor stub
		}

	}

}

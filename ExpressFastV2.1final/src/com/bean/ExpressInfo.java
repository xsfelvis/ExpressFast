package com.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
/*将快递信息序列化*/
public class ExpressInfo implements Parcelable {    //声明实现接口Parcelable
	public String time;
	public String context;

	 //实例化静态内部对象CREATOR实现接口Parcelable.Creator
	public static final Parcelable.Creator<ExpressInfo> CREATOR = new Creator<ExpressInfo>() {

		@Override
		 //从Parcel容器中读取传递数据值，封装成Parcelable对象返回逻辑层
		public ExpressInfo createFromParcel(Parcel source) {
			
			//先读取time，再读取context 
			ExpressInfo expressInfo = new ExpressInfo();
			expressInfo.time = source.readString();
			expressInfo.context = source.readString();
			
			return expressInfo;
		}

		@Override
		//创建一个类型为T，长度为size的数组，仅一句话（return new T[size])即可。方法是供外部类反序列化本类数组使用。
		public ExpressInfo[] newArray(int size) {
			
			return new ExpressInfo[size];
		}

	};
	//内容接口描述，默认返回0就可以;
	@Override
	public int describeContents() {
		return 0;
	}
	//该方法将类的数据写入外部提供的Parcel中.即打包需要传递的数据到Parcel容器保存，以便从parcel容器获取数据
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		
		   //先写入time，再写入context
		dest.writeString(time);
		dest.writeString(context);
		
	}
}

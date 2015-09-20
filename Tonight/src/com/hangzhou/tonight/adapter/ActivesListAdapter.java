package com.hangzhou.tonight.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.hangzhou.tonight.R;
import com.hangzhou.tonight.base.BaseApplication;
import com.hangzhou.tonight.base.BaseObjectListAdapter;
import com.hangzhou.tonight.base.Config;
import com.hangzhou.tonight.entity.ActivesEntity;
import com.hangzhou.tonight.entity.Entity;
import com.hangzhou.tonight.entity.NearByPeople;
import com.hangzhou.tonight.util.MyPreference;
import com.hangzhou.tonight.util.PhotoUtils;
import com.hangzhou.tonight.util.ScreenUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class ActivesListAdapter extends BaseObjectListAdapter {

	
	ImageLoader imageLoader;
	DisplayImageOptions options;
	public ActivesListAdapter(BaseApplication application, Context context,
			List<? extends Entity> datas) {
		super(application, context, datas);
		
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.kc_picture)// 正在加载
				.showImageForEmptyUri(R.drawable.kc_picture)// 空图片
				.showImageOnFail(R.drawable.kc_picture)// 错误图片
				.cacheInMemory(true)//设置 内存缓存
				.cacheOnDisk(true)//设置硬盘缓存
				.imageScaleType(ImageScaleType.EXACTLY) // default 推荐.imageScaleType(ImageScaleType.EXACTLY) 节省内存
				.considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_promotion_list, null);
			holder = new ViewHolder();

			holder.mIvAvatar = (ImageView) convertView
					.findViewById(R.id.kc_Img);

			holder.mTvtitle = (TextView) convertView
					.findViewById(R.id.tv_title);
			holder.mTvdescribe = (TextView) convertView
					.findViewById(R.id.tv_describe);
			holder.mTvdistance = (TextView) convertView
					.findViewById(R.id.tv_distance);
			holder.mTvcharge = (TextView) convertView
					.findViewById(R.id.tv_charge);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		//畅销套餐
		//290元享价值总价值460元的雪花纯生啤酒套餐
		ActivesEntity people = (ActivesEntity) getItem(position);
		holder.mTvtitle.setTag(people);
		holder.mTvtitle.setText(people.getTitle()+"");
		holder.mTvdescribe.setText(people.getDes()+"");
		
		//LatLng pt_start = new LatLng(Double.parseDouble(people.getLat()), Double.parseDouble(people.getLon()));
    	
    	
    	//LatLng pt_end = new LatLng(Double.parseDouble(MyPreference.getInstance(mContext).getLocation_w()), Double.parseDouble(MyPreference.getInstance(mContext).getLocation_j()));
		double distance = ScreenUtils.gps2m(Double.parseDouble(people.getLat()), Double.parseDouble(people.getLon()), Double.parseDouble(MyPreference.getInstance(mContext).getLocation_w()), Double.parseDouble(MyPreference.getInstance(mContext).getLocation_j()));
    	
    	//Toast.makeText(mContext, lati+""+lonti+""+addr, 1).show();
    	
    	holder.mTvdistance.setText((int)(distance/1000)+"km");
		//holder.mTvdistance.setText("1k");
		holder.mTvcharge.setText(people.getPrice()+"元");
		//holder.mIvAvatar.setBackgroundResource(R.drawable.kc_picture);
		imageLoader.displayImage(Config.ACT_IMG+people.getImg(), holder.mIvAvatar,options);
	//	holder.mIvAvatar.setImageBitmap(mApplication.getAvatar(people.getImg()));
		return convertView;
	}

	class ViewHolder {

		ImageView mIvAvatar;
		TextView mTvtitle;
		TextView mTvdescribe;
		TextView mTvdistance;
		TextView mTvcharge;
	}
}

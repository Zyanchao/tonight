package com.hangzhou.tonight.adapter;

import java.util.List;

import android.content.Context;
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
import com.hangzhou.tonight.entity.ActivesEntity;
import com.hangzhou.tonight.entity.Entity;
import com.hangzhou.tonight.entity.NearByPeople;
import com.hangzhou.tonight.util.PhotoUtils;

public class ActivesListAdapter extends BaseObjectListAdapter {

	public ActivesListAdapter(BaseApplication application, Context context,
			List<? extends Entity> datas) {
		super(application, context, datas);
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
		
		LatLng pt_start = new LatLng(Double.parseDouble(people.getLat()), Double.parseDouble(people.getLon()));
    	
    	
    	//LatLng pt_end = new LatLng(w, j);
		//double distance = DistanceUtil.getDistance(pt_start, pt_end);
    	
    	//Toast.makeText(mContext, lati+""+lonti+""+addr, 1).show();
    	
    	//holder.distance.setText((int)(distance/1000)+"km");
		holder.mTvdistance.setText("1k");
		holder.mTvcharge.setText(people.getPrice()+"元");
		holder.mIvAvatar.setBackgroundResource(R.drawable.kc_picture);
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

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
import com.hangzhou.tonight.entity.MerchantEntity;
import com.hangzhou.tonight.entity.NearByPeople;
import com.hangzhou.tonight.util.PhotoUtils;

public class MerchantListAdapter extends BaseObjectListAdapter {

	public MerchantListAdapter(BaseApplication application, Context context,
			List<? extends Entity> datas) {
		super(application, context, datas);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_merchants_list, null);
			holder = new ViewHolder();

			holder.mIvAvatar = (ImageView) convertView
					.findViewById(R.id.img_merchant);

			holder.mTvtitle = (TextView) convertView
					.findViewById(R.id.tv_merchant_name);
			holder.mTvdescribe = (TextView) convertView
					.findViewById(R.id.tv_merchant_desc);
			holder.mTvdistance = (TextView) convertView
					.findViewById(R.id.tv_merchant_address);
			holder.mTvcharge = (TextView) convertView
					.findViewById(R.id.tv_merchant_price);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		//畅销套餐
		//290元享价值总价值460元的雪花纯生啤酒套餐
		MerchantEntity people = (MerchantEntity) getItem(position);
		holder.mTvtitle.setTag(people);
		holder.mTvtitle.setText(people.getName()+"");
		holder.mTvdescribe.setText(people.getTitle()+"");
		holder.mTvdistance.setText(people.getAddress()+"");
		
		LatLng pt_start = new LatLng(Double.parseDouble(people.getLat()), Double.parseDouble(people.getLon()));
    	
    	
    	//LatLng pt_end = new LatLng(w, j);
		//double distance = DistanceUtil.getDistance(pt_start, pt_end);
    	
    	//Toast.makeText(mContext, lati+""+lonti+""+addr, 1).show();
    	
    	//holder.distance.setText((int)(distance/1000)+"km");
		holder.mTvcharge.setText(people.getPrice()+"元");
		holder.mIvAvatar.setBackgroundResource(R.drawable.kcxq_pic);
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

package com.hangzhou.tonight.module.individual.fragment;

import java.util.ArrayList;
import java.util.List;

import com.hangzhou.tonight.R;
import com.hangzhou.tonight.module.base.fragment.BEmptyListviewFragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 我的收藏
 * @author hank
 */
public class MyCollectionFragment extends BEmptyListviewFragment {

	CollectionAdapter adapter;
	List<CollectionDataModel> listData = null;
	
	@Override protected void doListeners() {
		
	}
	
	@Override protected void doHandler() {

		listData = new ArrayList<CollectionDataModel>();
		
		String[] strs = {
				 "蜜桃酒吧夜色派对"
				,"8090迷幻系小聚"
				,"活动时间：周一至周五"
				,"已售220"};
		for(int i=0,len = 10;i<len;i++){
			CollectionDataModel m = new CollectionDataModel();
			m.title	 = strs[0];
			m.content= strs[1];
			m.time 	 = strs[2];
			m.sale 	 = strs[3];
			listData.add(m);
		}
		adapter = new CollectionAdapter();
		mListView.setAdapter(adapter);

	}
	
	class CollectionAdapter extends BaseAdapter {

		@Override public int getCount() {
			return listData.size();
		}

		@Override public Object getItem(int position) {
			return listData.get(position);
		}

		@Override public long getItemId(int position) {
			return 0;
		}

		@Override public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			CollectionDataModel model = listData.get(position);
			if (convertView == null) {
				convertView = View.inflate(getActivity(),R.layout.item_individual_fragment_mycollection, null);
				holder = new ViewHolder(convertView);
			}
			holder = (ViewHolder) convertView.getTag();
			holder.tv_title.setText(model.title);
			holder.tv_content.setText(model.content);
			holder.tv_time.setText(model.time);
			holder.tv_sale.setText(model.sale);
			// holder.iv_image.setB

			return convertView;
		}

		class ViewHolder {
			public ImageView iv_image;
			public TextView tv_title, tv_content, tv_time, tv_sale;
			public ViewHolder(View view) {
				iv_image 	= (ImageView)view.findViewById(R.id.individual_mycollection_image);
				tv_title 	= (TextView) view.findViewById(R.id.individual_mycollection_title);
				tv_content 	= (TextView) view.findViewById(R.id.individual_mycollection_content);
				tv_time 	= (TextView) view.findViewById(R.id.individual_mycollection_activity_time);
				tv_sale 	= (TextView) view.findViewById(R.id.individual_mycollection_sale_detail);
				view.setTag(this);
			}
		}
	}

	class CollectionDataModel {
		String title, content, time, sale, image;
	}
}

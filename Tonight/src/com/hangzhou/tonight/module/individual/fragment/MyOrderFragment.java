package com.hangzhou.tonight.module.individual.fragment;

import java.util.ArrayList;
import java.util.List;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.hangzhou.tonight.R;
import com.hangzhou.tonight.module.base.util.ViewUtil;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 我的订单,依据状态码标识 已支付Paid、未支付
 * @author hank
 */
public class MyOrderFragment extends Fragment {
	private List<OrderModel> mOrderList;
	private ModelAdapter mAdapter;
	
	SwipeMenuListView swipeListview;
	/*** 已支付、未支付 */
	public static final int STATE_PAID = 1,STATE_NOT_PAID = -1;
	
	@Override public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_individual_myorder, container, false);
	}
	
	@Override public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		swipeListview = (SwipeMenuListView) getView().findViewById(R.id.empty_swipe_listview);
		
		mOrderList = new ArrayList<MyOrderFragment.OrderModel>();
		
		
		OrderModel om = new OrderModel();
		om.title = "订单号：776534";
		om.content = "蜜桃酒吧迷情派对活动，200元单人代金券， 仅限活动当日使用…蜜桃酒吧迷情派对活动，200元单人代金券， 仅限活动当日使用…";
		mOrderList.add(om);
		
		om = new OrderModel();
		om.title = "订单号：789531";
		om.content = "蜜桃酒吧迷情派对活动，200元单人代金券， 仅限活动当日使用…蜜桃酒吧迷情派对活动，200元单人代金券， 仅限活动当日使用";
		mOrderList.add(om);
		
		om = new OrderModel();
		om.title = "订单号：789531";
		om.content = "蜜桃酒吧迷情派对活动，200元单人代金券， 仅限活动当日使用…蜜桃酒吧迷情派对活动，200元单人代金券， 仅限活动当日使用";
		mOrderList.add(om);
		
		om = new OrderModel();
		om.title = "订单号：789531";
		om.content = "蜜桃酒吧迷情派对活动，200元单人代金券， 仅限活动当日使用…蜜桃酒吧迷情派对活动，200元单人代金券， 仅限活动当日使用";
		mOrderList.add(om);
		
		om = new OrderModel();
		om.title = "订单号：789531";
		om.content = "蜜桃酒吧迷情派对活动，200元单人代金券， 仅限活动当日使用…蜜桃酒吧迷情派对活动，200元单人代金券， 仅限活动当日使用";
		mOrderList.add(om);
		
		om = new OrderModel();
		om.title = "订单号：789531";
		om.content = "蜜桃酒吧迷情派对活动，200元单人代金券， 仅限活动当日使用…蜜桃酒吧迷情派对活动，200元单人代金券， 仅限活动当日使用";
		mOrderList.add(om);
		
		swipeListview.setDividerHeight(ViewUtil.dp2px(getActivity(), 12f));
		mAdapter = new ModelAdapter();
		swipeListview.setAdapter(mAdapter);
		
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
				SwipeMenuItem openItem = new SwipeMenuItem(getActivity().getApplicationContext());
				//openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,0xCE)));
				openItem.setBackground(new ColorDrawable(Color.parseColor("#FFEB416C")));
				openItem.setWidth(ViewUtil.dp2px(getActivity().getBaseContext(),90));
				openItem.setTitle("评价");
				openItem.setTitleSize(20);
				openItem.setTitleColor(Color.WHITE);
				menu.addMenuItem(openItem);

				//SwipeMenuItem deleteItem = new SwipeMenuItem(getActivity().getApplicationContext());
				//deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,0x3F, 0x25)));
				//deleteItem.setTitle("退款");
				//deleteItem.setTitleSize(18);
				//deleteItem.setTitleColor(Color.WHITE);
				
				//deleteItem.setWidth(ViewUtil.dp2px(getActivity().getBaseContext(),90));
				//menu.addMenuItem(deleteItem);
			}
		};
		swipeListview.setMenuCreator(creator);
		swipeListview.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override public void onMenuItemClick(int position, SwipeMenu menu, int index) {
				Toast.makeText(getActivity(), "____" + index, Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	class ModelAdapter extends BaseAdapter {

		@Override public int getCount() {
			return mOrderList.size();
		}

		@Override public OrderModel getItem(int position) {
			return mOrderList.get(position);
		}

		@Override public long getItemId(int position) {
			return position;
		}

		@Override public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(getActivity().getApplicationContext(),R.layout.item_individual_fragment_myorder, null);
				new ViewHolder(convertView);
			}
			ViewHolder holder = (ViewHolder) convertView.getTag();
			OrderModel item = mOrderList.get(position);
			holder.tv_title.setText(item.title);
			holder.tv_content.setText(item.content);
			return convertView;
		}

		class ViewHolder {
			TextView tv_title;
			TextView tv_content;
			public ViewHolder(View view) {
				tv_title   = (TextView) view.findViewById(R.id.item_fragment_myorder_title);
				tv_content = (TextView) view.findViewById(R.id.item_fragment_myorder_content);
				view.setTag(this);
			}
		}
	}
	
	class OrderModel{
		String title;
		String content;
	}
}

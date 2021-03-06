package com.hangzhou.tonight.module.social.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.hangzhou.tonight.R;
import com.hangzhou.tonight.module.base.constant.SysModuleConstant;
import com.hangzhou.tonight.module.base.util.AsyncTaskUtil;
import com.hangzhou.tonight.module.base.util.inter.Callback;
import com.hangzhou.tonight.module.social.fragment.FriendCityWideFragment.DataModel;

/**
 * 我的好友
 * @author hank
 *
 */
public class MyFriendFragment extends FriendCityWideFragment {
	
	@Override public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		return inflater.inflate(R.layout.empty_swipe_listview, container, false);
	}
	
	@Override protected void doView() {
		super.doView();
		mListView = (ListView) findViewById(R.id.empty_swipe_listview);
	}
	
	@Override protected void doHandler() {
		super.doHandler();
		SwipeMenuCreator creator = new SwipeMenuCreator() {
			@Override public void create(SwipeMenu menu) {
				menu.addMenuItem(getMenuItem());
			}
		};
		((SwipeMenuListView)mListView).setMenuCreator(creator);
		((SwipeMenuListView)mListView).setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override public void onMenuItemClick(final int position, SwipeMenu menu, int index) {
				JSONObject params = new JSONObject();
				params.put("tuid", listData.get(position).uid);
				AsyncTaskUtil.postData(getActivity(), "delFriend", params, new Callback() {
					@Override public void onSuccess(JSONObject result) {
						listData.remove(position);
						adapter.notifyDataSetChanged();
					}
					@Override public void onFail(String msg) {}
				});
			}
		});
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				
			}
		});
		
	}
	
	@Override protected void doPostData() {
		super.doPostData();
		AsyncTaskUtil.postData(getActivity(), "getFriendList", null, new Callback() {
			@Override public void onSuccess(JSONObject result) {
				 listData.addAll(JSONArray.parseArray(result.getString("friendList"), DataModel.class));
				 adapter.notifyDataSetChanged();
			}
			@Override public void onFail(String msg) {
				if(SysModuleConstant.VALUE_DEV_MODEL){
					String[] strs = {
							 "蜜桃"
							,"8090迷幻系小聚"
							,"19:01"
							,"已售220"};
					for(int i=0,len = 20;i<len;i++){
						DataModel m = new DataModel();
						m.nick = strs[0] + i;
						listData.add(m);
					}
				    adapter.notifyDataSetChanged();
				}
			}
		});
	}
}

package com.hangzhou.tonight.module.message.fragment;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hangzhou.tonight.R;
import com.hangzhou.tonight.module.base.fragment.BEmptyListviewFragment;

/**
 * 好友消息
 * @author hank
 */
public class FriendMessageFragment extends BEmptyListviewFragment {

	List<DataModel> listData = null;
	BaseAdapter adapter;

	@Override protected void doListeners() {
		
	}

	@Override protected void doHandler() {
		listData = new ArrayList<DataModel>();
		String[] strs  = {"学习天天向上智力时时下降","凌晨一点写东东"};
		String[] times = {"昨天","7月1日"};
		String content = "今天，去了蜜桃酒吧，很不错的酒吧啊今天，去了蜜桃酒吧，很不错的酒吧啊今天，去了蜜桃酒吧，很不错的酒吧啊";
		for(int i=0,len = strs.length;i<len;i++){
			String str = strs[i];
			DataModel m = new DataModel();
			m.username  = str;
			m.content   = content;
			m.time      = times[i];
			listData.add(m);
		}
		adapter = new EntityAdapter();
		mListView.setAdapter(adapter);
	}

	class EntityAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return listData.size();
		}

		@Override
		public Object getItem(int position) {
			return listData.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			DataModel model = listData.get(position);
			ViewHolder hodler = null;
			if(convertView == null){
				convertView = View.inflate(getActivity(), R.layout.item_message_fragment_friend_message, null);
				hodler 		= new ViewHolder(convertView);
			}
			hodler = (ViewHolder) convertView.getTag();
			hodler.tv_username.setText(model.username);
			hodler.tv_content .setText(model.content);
			hodler.tv_time.setText(model.time);
			return convertView;
		}
		
		class ViewHolder{
			TextView   tv_username,tv_content,tv_time;
			public ViewHolder(View view){
				this.tv_username= (TextView) view.findViewById(R.id.message_friend_message_username);
				this.tv_content = (TextView) view.findViewById(R.id.message_friend_message_content);
				this.tv_time    = (TextView) view.findViewById(R.id.message_friend_message_time);
				view.setTag(this);
			}
		}
	}
	
	class DataModel{
		String username,content,time;
	}
}

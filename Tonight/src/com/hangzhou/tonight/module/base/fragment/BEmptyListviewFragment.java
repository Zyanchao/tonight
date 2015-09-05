package com.hangzhou.tonight.module.base.fragment;

import com.hangzhou.tonight.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * 空 ListView
 * @author hank
 */
public abstract class BEmptyListviewFragment extends BFragment {
	
	protected ListView mListView;
	
	@Override public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		return inflater.inflate(R.layout.empty_listview, container, false);
	}
	
	@Override protected void doView() {
		mListView = (ListView) getView().findViewById(R.id.common_tbar_listview);
	}
	
	
}

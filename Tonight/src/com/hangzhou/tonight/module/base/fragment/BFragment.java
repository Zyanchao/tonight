package com.hangzhou.tonight.module.base.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

public abstract class BFragment extends Fragment {

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		doView();
		doListeners();
		doHandler();
	}
	
	public View findViewById(int id){
		return getView().findViewById(id);
	}
	
	protected abstract void doView();
	protected abstract void doListeners();
	protected abstract void doHandler();
}

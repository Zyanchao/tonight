package com.hangzhou.tonight.activity;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.PopupWindow.OnDismissListener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hangzhou.tonight.LoginActivity;
import com.hangzhou.tonight.R;
import com.hangzhou.tonight.adapter.ActivesListAdapter;
import com.hangzhou.tonight.adapter.OtherActsListAdapter;
import com.hangzhou.tonight.adapter.PinglunListAdapter;
import com.hangzhou.tonight.entity.ActivesEntity;
import com.hangzhou.tonight.entity.ActivesInfo;
import com.hangzhou.tonight.entity.MerchantInfo;
import com.hangzhou.tonight.entity.OtherActsEntity;
import com.hangzhou.tonight.entity.PinglunEntity;
import com.hangzhou.tonight.entity.ReviewsEntity;
import com.hangzhou.tonight.maintabs.TabItemActivity;
import com.hangzhou.tonight.util.Base64Utils;
import com.hangzhou.tonight.util.HttpRequest;
import com.hangzhou.tonight.util.JsonResolveUtils;
import com.hangzhou.tonight.util.JsonUtils;
import com.hangzhou.tonight.util.PreferenceConstants;
import com.hangzhou.tonight.util.RC4Utils;
import com.hangzhou.tonight.util.ScreenUtils;
import com.hangzhou.tonight.view.HeaderLayout;
import com.hangzhou.tonight.view.HeaderLayout.HeaderStyle;
import com.hangzhou.tonight.view.HeaderLayout.SearchState;
import com.hangzhou.tonight.view.HeaderLayout.onMiddleImageButtonClickListener;
import com.hangzhou.tonight.view.HeaderLayout.onSearchListener;
import com.hangzhou.tonight.view.HeaderSpinner;
import com.hangzhou.tonight.view.HeaderSpinner.onSpinnerClickListener;
import com.hangzhou.tonight.view.XListView;
import com.hangzhou.tonight.view.XListView.IXListViewListener;


/**
* @ClassName: 活动 主页 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author yanchao 
* @date 2015-8-30 下午5:17:58 
*
 */
public class MerchantDetailActivity extends TabItemActivity implements OnClickListener
{

	private TextView tvBack,tvTitle;
	private Context mContext;
	private HeaderLayout mHeaderLayout;
	private HeaderSpinner mHeaderSpinner;
	//private PromotionListFragment mPeopleFragment;
	private Handler mHander;
	private MerchantInfo sellerInfo;
	private int currentPage=1,//当期页码
            pageCount = 1,//总页数
            pageSize = 15;//每页数据量
	
	private ActivesListAdapter mAdapter;
	public List<ActivesEntity> mActives = new ArrayList<ActivesEntity>();
	public List<ReviewsEntity> mpingluns = new ArrayList<ReviewsEntity>();
	public List<OtherActsEntity> motheracts = new ArrayList<OtherActsEntity>();
	private String seller_id,name;
	private ListView lvacts,lvpinglun;
	private PinglunListAdapter pinglunAdapter;
	private OtherActsListAdapter otherActsListAdapter;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_merchant_detail);
		mContext = this;
		seller_id = getIntent().getStringExtra("id");
		name = getIntent().getStringExtra("name");
		initViews();
		initEvents();
		init();
		getDataDetail(currentPage);
	}
	
	@Override
	protected void initViews() {
		tvBack = (TextView) findViewById(R.id.tv_header_back);
		tvTitle = (TextView) findViewById(R.id.tv_title);
		lvacts = (ListView) findViewById(R.id.lv_ohter_acts);
		lvpinglun = (ListView) findViewById(R.id.lv_pinglun);
		//mHeaderLayout = (HeaderLayout) findViewById(R.id.nearby_header);
	}

	@Override
	protected void initEvents() {
		tvBack.setOnClickListener(this);
	}

	@Override
	protected void init() {
		mHander = new Handler();
		tvTitle.setText(name);
		
		pinglunAdapter = new PinglunListAdapter(mApplication, mContext, mpingluns);
		lvpinglun.setAdapter(pinglunAdapter);
		pinglunAdapter.notifyDataSetInvalidated();
		ScreenUtils.setListViewHeightBasedOnChildren(lvpinglun);
		
		otherActsListAdapter = new OtherActsListAdapter(mApplication, mContext, motheracts);
		lvacts.setAdapter(otherActsListAdapter);
		otherActsListAdapter.notifyDataSetInvalidated();
		ScreenUtils.setListViewHeightBasedOnChildren(lvacts);
		/*mPeopleFragment = new PromotionListFragment(mApplication, this, this);
		//mGroupFragment = new NearByGroupFragment(mApplication, this, this);
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.nearby_layout_content, mPeopleFragment).commit();*/
	}

	private void initPopupWindow() {
		/*mPopupWindow = new NearByPopupWindow(this);
		mPopupWindow.setOnSubmitClickListener(new onSubmitClickListener() {

			@Override
			public void onClick() {
				mPeopleFragment.onManualRefresh();
			}
		});
		mPopupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				mHeaderSpinner.initSpinnerState(false);
			}
		});*/
	}

	/*public class OnSpinnerClickListener implements onSpinnerClickListener {

		@Override
		public void onClick(boolean isSelect) {
			if (isSelect) {
				mPopupWindow
						.showViewTopCenter(findViewById(R.id.nearby_layout_root));
			} else {
				mPopupWindow.dismiss();
			}
		}
	}*/

	public class OnSearchClickListener implements onSearchListener {

		@Override
		public void onSearch(EditText et) {
			String s = et.getText().toString().trim();
			if (TextUtils.isEmpty(s)) {
				showCustomToast("请输入搜索关键字");
				et.requestFocus();
			} else {
				((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
						.hideSoftInputFromWindow(MerchantDetailActivity.this
								.getCurrentFocus().getWindowToken(),
								InputMethodManager.HIDE_NOT_ALWAYS);
				putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

					@Override
					protected void onPreExecute() {
						super.onPreExecute();
						mHeaderLayout.changeSearchState(SearchState.SEARCH);
					}

					@Override
					protected Boolean doInBackground(Void... params) {
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						return false;
					}

					@Override
					protected void onPostExecute(Boolean result) {
						super.onPostExecute(result);
						mHeaderLayout.changeSearchState(SearchState.INPUT);
						showCustomToast("未找到搜索的群");
					}
				});
			}
		}

	}

	
	/**
	
	* @Title: getDataList 
	
	* @Description: TODO(网络请求 活动列表数据) 
	
	* @param     设定文件 
	
	* @return void    返回类型 
	
	* @throws
	 */
	private void getDataDetail(final int currentPage) {
		new AsyncTask<Void, Void, String>() {

			

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				showLoadingDialog("正在登录,请稍后...");
			}

			@Override
			protected String doInBackground(Void... params) {

				Map<String, String> param =setParams(currentPage);
				
				return HttpRequest.submitPostData(PreferenceConstants.TONIGHT_SERVER,
						param, "UTF-8");
			}

			//[{"act_id":"46","address":"杭州市下城区再行路298号","des":"290元享价值总价值460元的雪花纯生啤酒套餐","endtime":"1451491200","img":"[\"0_0%E9%97%A8%E5%A4%B4.jpg\",\"0_1%E6%A0%BC%E5%B1%80.jpg\",\"0_2%E7%89%B9%E5%86%99.jpg\",\"0_3%E9%85%92%E6%B0%B4.jpg\",\"0_4%E5%A5%97%E9%A4%90.jpg\",\"0_5%E4%BA%BA%E7%89%A9.jpg\"]","lat":"30.312652","lon":"120.177033","mark":"0.0","name":"豪斯酒吧","price":"0.00","sales_num":"0","starttime":"1439827200","title":"畅销套餐","value":"0.00"}
			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				dismissLoadingDialog();
				JSONObject jsonObject = JSON.parseObject(result);
				
				
				JSONObject seller = jsonObject.getJSONObject("sellerInfo"); 
				sellerInfo = JSON.parseObject(jsonObject.toString(), MerchantInfo.class);
				try {
					com.alibaba.fastjson.JSONArray jsonArrayacts = jsonObject.getJSONArray("acts");
					motheracts = JSON.parseArray(jsonArrayacts.toString(),OtherActsEntity.class);
					
					com.alibaba.fastjson.JSONArray jsonArray = jsonObject.getJSONArray("reviews");
					mpingluns = JSON.parseArray(jsonArray.toString(),ReviewsEntity.class);
					showCustomToast(result);
				} catch (Exception e) {
					// TODO: handle exception
				}
				
			}
		}.execute();
		
	}

	private Map<String, String> setParams(int currentPage){
		
		Map<String, String> map = new HashMap<String, String>();
		Map<String, Object> parms = new HashMap<String, Object>();
		parms.put("seller_id", seller_id);
		ArrayList<Object> arry = new ArrayList<Object>();
		arry.add(0, "getSellerInfo");
		arry.add(1, 0);
		arry.add(2, parms);
		String data0 = RC4Utils.RC4("mdwi5uh2p41nd4ae23qy4",JsonUtils.list2json(arry));
		String encoded1 = "";
		try {
			encoded1 = new String(Base64Utils.encode(
					data0.getBytes("ISO-8859-1"), 0, data0.length()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		System.out.println("base64编码后：     " + encoded1);
		String decode = "";
		try {
			if(!encoded1.equals("")){
				decode = new String(
						Base64.decode(encoded1, Base64.DEFAULT),
						"ISO-8859-1");
			}		
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String data7 = RC4Utils.RC4("mdwi5uh2p41nd4ae23qy4", decode);
		map.put("d", encoded1);
		return map;
		
	}

	public class OnMiddleImageButtonClickListener implements
			onMiddleImageButtonClickListener {

		@Override
		public void onClick() {
			mHeaderLayout.showSearch();
		}
	}

	/*public class OnSwitcherButtonClickListener implements
			onSwitcherButtonClickListener {

		@Override
		public void onClick(SwitcherButtonState state) {
			FragmentTransaction ft = getSupportFragmentManager()
					.beginTransaction();
			ft.setCustomAnimations(R.anim.fragment_fadein,
					R.anim.fragment_fadeout);
			switch (state) {
			case LEFT:
				mHeaderLayout.init(HeaderStyle.TITLE_NEARBY_PEOPLE);
				ft.replace(R.id.nearby_layout_content, mPeopleFragment)
						.commit();
				break;

			case RIGHT:
				mHeaderLayout.init(HeaderStyle.TITLE_NEARBY_GROUP);
				ft.replace(R.id.nearby_layout_content, mGroupFragment).commit();
				break;
			}
		}

	}*/

	@Override
	public void onBackPressed() {
		if (mHeaderLayout.searchIsShowing()) {
			clearAsyncTask();
			mHeaderLayout.dismissSearch();
			mHeaderLayout.clearSearch();
			mHeaderLayout.changeSearchState(SearchState.INPUT);
		} else {
			finish();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_header_back:
			finish();
			break;
			
		default:
			break;
		}
	}

}

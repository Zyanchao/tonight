package com.hangzhou.tonight.activity;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import u.aly.bu;

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
import android.widget.RelativeLayout;
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
import com.hangzhou.tonight.util.IntentJumpUtils;
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
	private MerchantInfo sellerInfo;
	
	private ActivesListAdapter mAdapter;
	public List<ActivesEntity> mActives = new ArrayList<ActivesEntity>();
	public List<ReviewsEntity> mpingluns = new ArrayList<ReviewsEntity>();
	public List<OtherActsEntity> motheracts = new ArrayList<OtherActsEntity>();
	private String seller_id,name;
	private ListView lvacts,lvpinglun;
	private PinglunListAdapter pinglunAdapter;
	private OtherActsListAdapter otherActsListAdapter;
	private RelativeLayout re_fuwu1,re_fuwu2;
	
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
		getDataDetail();
	}
	
	@Override
	protected void initViews() {
		tvBack = (TextView) findViewById(R.id.tv_header_back);
		tvTitle = (TextView) findViewById(R.id.tv_title);
		lvacts = (ListView) findViewById(R.id.lv_ohter_acts);
		lvpinglun = (ListView) findViewById(R.id.lv_pinglun);
		re_fuwu1 = (RelativeLayout) findViewById(R.id.rl_fuwu1);
		re_fuwu2 = (RelativeLayout) findViewById(R.id.rl_fuwu2);
	}

	@Override
	protected void initEvents() {
		tvBack.setOnClickListener(this);
		re_fuwu1.setOnClickListener(this);
		re_fuwu2.setOnClickListener(this);
	}

	@Override
	protected void init() {
		tvTitle.setText(name);
	}


	
	/**
	
	* @Title: getDataList 
	* @Description: TODO(网络请求 活动列表数据) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	private void getDataDetail() {
		new AsyncTask<Void, Void, String>() {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				showLoadingDialog("正在登录,请稍后...");
			}

			@Override
			protected String doInBackground(Void... params) {

				Map<String, String> param =setParams();
				
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
				
				com.alibaba.fastjson.JSONArray jsonArrayacts = jsonObject.getJSONArray("acts");
				motheracts = JSON.parseArray(jsonArrayacts.toString(),OtherActsEntity.class);
				
				otherActsListAdapter = new OtherActsListAdapter(mApplication, mContext, motheracts);
				lvacts.setAdapter(otherActsListAdapter);
				otherActsListAdapter.notifyDataSetInvalidated();
				ScreenUtils.setListViewHeightBasedOnChildren(lvacts);
				
				com.alibaba.fastjson.JSONArray jsonArray = jsonObject.getJSONArray("reviews");
				mpingluns = JSON.parseArray(jsonArray.toString(),ReviewsEntity.class);
				
				
				pinglunAdapter = new PinglunListAdapter(mApplication, mContext, mpingluns);
				lvpinglun.setAdapter(pinglunAdapter);
				pinglunAdapter.notifyDataSetInvalidated();
				ScreenUtils.setListViewHeightBasedOnChildren(lvpinglun);
			}
		}.execute();
		
	}

	private Map<String, String> setParams(){
		
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




	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_header_back:
			finish();
			break;
		case R.id.rl_fuwu1:
			Bundle bundle = new Bundle();
			bundle.putString("id",seller_id);
			IntentJumpUtils.nextActivity(BrDayServerActivity.class, MerchantDetailActivity.this,bundle);
			break;
		case R.id.rl_fuwu2:
			Bundle bundle2 = new Bundle();
			bundle2.putString("id",seller_id);
			IntentJumpUtils.nextActivity(DriverServerActivity.class, MerchantDetailActivity.this,bundle2);
			break;
			
		default:
			break;
		}
	}

}

package com.hangzhou.tonight;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hangzhou.tonight.base.BaseActivity;
import com.hangzhou.tonight.maintabs.MainActivity;
import com.hangzhou.tonight.util.Base64Utils;
import com.hangzhou.tonight.util.FileUtils;
import com.hangzhou.tonight.util.HttpRequest;
import com.hangzhou.tonight.util.JsonResolveUtils;
import com.hangzhou.tonight.util.JsonUtils;
import com.hangzhou.tonight.util.PreferenceConstants;
import com.hangzhou.tonight.util.RC4Utils;

/**
 * 
 * @author WYH
 *
 */
public class RegisterActivity extends BaseActivity implements OnClickListener{

	private TextView tvBack;
	private EditText etPhone,etPass,etRePass,etYzm;
	private Button btHuoquyzm,btDone;
	
	String phoneNum,pwd,repwd,code;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		initViews();
		initEvents();
	}

	@Override
	protected void initViews() {
		tvBack = (TextView) findViewById(R.id.tv_header_back);
		etPhone = (EditText) findViewById(R.id.et_register_account);
		etPass = (EditText) findViewById(R.id.et_register_pwd);
		etRePass = (EditText) findViewById(R.id.et_re_pwd);
		etYzm = (EditText) findViewById(R.id.et_yzm);
		btHuoquyzm = (Button) findViewById(R.id.bt_huoqu_yzm);
		btDone = (Button) findViewById(R.id.bt_jinru);
	}

	@Override
	protected void initEvents() {
		tvBack.setOnClickListener(this);
		btHuoquyzm.setOnClickListener(this);
		btDone.setOnClickListener(this);
		
	}

	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_header_back:
			finish();
			break;
		case R.id.bt_huoqu_yzm:
	
			phoneNum = etPhone.getText().toString();
			if(FileUtils.matchPhone(phoneNum)){
				sendMessage();
			}else {
				showCustomToast("请重新输入手机号码");
			}
			
			break;
		case R.id.bt_jinru:
			
			checkDatas();
			registerUser();
			break;

		default:
			break;
		}
	}

	private void registerUser() {
		new AsyncTask<Void, Void, String>() {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				showLoadingDialog("注册中,请稍后...");
			}

			@Override
			protected String doInBackground(Void... params) {

				Map<String, String> param =setParamsUser();
				return HttpRequest.submitPostData(PreferenceConstants.TONIGHT_SERVER,
						param, "UTF-8");
			}

			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				if(JsonResolveUtils.resolveuserResult(result)){
					JsonResolveUtils.resolveuserLogin(result);
					Intent intent = new Intent(RegisterActivity.this, MainActivity.class); 
					startActivity(intent);
					finish();
				}else{
					showCustomToast("注册失败，请稍后重试");
				}
			}
		}.execute();
		
	}

	private Map<String, String> setParamsUser(){
		Map<String, String> map = new HashMap<String, String>();
		Map<String, Object> parms = new HashMap<String, Object>();
		parms.put("phone", phoneNum);
		parms.put("password", pwd);
		parms.put("code", code);
		ArrayList<Object> arry = new ArrayList<Object>();
		arry.add(0, "userSignUp");
		arry.add(1, 0);
		arry.add(2, parms);
		String data0 = RC4Utils.RC4("mdwi5uh2p41nd4ae23qy4",
				JsonUtils.list2json(arry));
		
		String encoded1 = "";
		try {
			encoded1 = new String(Base64Utils.encode(
					data0.getBytes("ISO-8859-1"), 0, data0.length()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
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
	
	
	
	
	/**
	 * 判断输入 数据 是否合法
	 */
	private void checkDatas() {
		phoneNum = etPhone.getText().toString();
		code = etYzm.getText().toString();
		pwd = etPass.getText().toString();
		repwd = etRePass.getText().toString();
		
		if(TextUtils.isEmpty(phoneNum)){
			showCustomToast("手机号不能为空");
			return ;
		}
		if(TextUtils.isEmpty(code)){
			showCustomToast("验证码不能为空");
			return ;
		}
		
		if(TextUtils.isEmpty(pwd)||pwd.length()<6){
			showCustomToast("密码长度最低为6位");
			return ;
		}
		if(!pwd.equals(repwd)){
			showCustomToast("两次密码输入不一致！");
			return ;
		}
	}

	/**
	 * 
	* @Title: sendMessage 
	* @Description: TODO(获取验证码) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	private void sendMessage() {
		new AsyncTask<Void, Void, String>() {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				showLoadingDialog("获取验证码,请稍后...");
			}

			@Override
			protected String doInBackground(Void... params) {

				Map<String, String> param =setParams();
				return HttpRequest.submitPostData(PreferenceConstants.TONIGHT_SERVER,
						param, "UTF-8");
			}

			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				dismissLoadingDialog();
				
			}
		}.execute();
		
	}

	
	private Map<String, String> setParams(){
		Map<String, String> map = new HashMap<String, String>();
		Map<String, Object> parms = new HashMap<String, Object>();
		parms.put("phone", phoneNum);
		//type(1:注册或绑定⼿手机时 2:忘记密码时)
		parms.put("type", "1");
		ArrayList<Object> arry = new ArrayList<Object>();
		arry.add(0, "getPhoneCode");
		arry.add(1, 0);
		arry.add(2, parms);
		String data0 = RC4Utils.RC4("mdwi5uh2p41nd4ae23qy4",
				JsonUtils.list2json(arry));
		
		String encoded1 = "";
		try {
			encoded1 = new String(Base64Utils.encode(
					data0.getBytes("ISO-8859-1"), 0, data0.length()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
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
	protected void init() {
		// TODO Auto-generated method stub
		
	}
	
	

}

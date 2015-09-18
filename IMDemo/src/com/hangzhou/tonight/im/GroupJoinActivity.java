package com.hangzhou.tonight.im;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hangzhou.tonight.BaseRoomActivity;
import com.hangzhou.tonight.EimApplication;
import com.hangzhou.tonight.R;
import com.hangzhou.tonight.manager.XmppConnectionManager;

public class GroupJoinActivity extends BaseRoomActivity implements OnClickListener {

	private Button mBackBtn;
	private EditText mGroupNameEdit;
	private EditText mGroupPwdEdit;
	private Button mJoinBtn;
	private XMPPConnection con;
	private EimApplication app;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_join);
		app = EimApplication.getInstance();
		con = XmppConnectionManager.getInstance().getConnection();
		mBackBtn = (Button)findViewById(R.id.add_reback_btn);
		mBackBtn.setOnClickListener(this);
		
		mGroupNameEdit = (EditText)findViewById(R.id.muc_name);
//		mGroupNameEdit.setOnClickListener(this);
		mGroupPwdEdit = (EditText)findViewById(R.id.muc_pwd);
//		mGroupPwdEdit.setOnClickListener(this);
		
		mJoinBtn = (Button)findViewById(R.id.join_btn);
		mJoinBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int viewId = v.getId();
		if(viewId== R.id.add_reback_btn){
			finish();
		}else if(viewId== R.id.join_btn){
			String qunNickName = mGroupNameEdit.getText().toString();
			String qunNickPwd = mGroupPwdEdit.getText().toString();
			if ("".equals(qunNickName)||"".equals(qunNickPwd)) {
				showToast();
			}else {
				joinRoom();
			}
		}
		/*switch(viewId) {
		case R.id.add_reback_btn:
			finish();
			break;
		case R.id.join_btn:
			String qunNickName = mGroupNameEdit.getText().toString();
			String qunNickPwd = mGroupPwdEdit.getText().toString();
			if ("".equals(qunNickName)||"".equals(qunNickPwd)) {
				showToast();
			}else {
				joinRoom();
			}
			
			break;
		default:
			break;
		}*/
	}

	public void joinRoom() {
		app.execRunnable(new Runnable() {
			@Override
			public void run() {
				join(GroupJoinActivity.this, con.getUser(),mGroupPwdEdit.getText().toString(),
						mGroupNameEdit.getText().toString());
			}
		});
	}

	private void showToast() {
		Toast toast = Toast.makeText(getApplicationContext(), "���϶�������������ô����Ⱥ��......",
				Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		LinearLayout toastView = (LinearLayout) toast.getView();
		ImageView imageCodeProject = new ImageView(getApplicationContext());
		imageCodeProject.setImageResource(R.drawable.tips_error);
		toastView.addView(imageCodeProject, 0);
		toast.show();
	}

	@Override
	public void sendMessage(Message message) {
		// TODO Auto-generated method stub
		
	}


}














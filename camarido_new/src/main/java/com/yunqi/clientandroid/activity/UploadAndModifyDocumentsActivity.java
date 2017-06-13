package com.yunqi.clientandroid.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.fragment.ModifyDocumentFragment;
import com.yunqi.clientandroid.fragment.UploadDocumentFragment;

/**
 * @author chenrenyi
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 当前运单下上传单据与修改单据
 * @date 15/12/2
 */
public class UploadAndModifyDocumentsActivity extends BaseActivity implements
		RadioGroup.OnCheckedChangeListener {

	private String ticketStatus;// 执行状态
	private String ticketId;// 订单id
	private String packageBeginName;// 出发地名称
	private String packageEndName;// 目的地名称
	private String createTime;// 创建时间
	private View parentView;
	private RadioGroup mRadioGroup;
	public RadioButton mUpload;
	public RadioButton mModify;
	public UploadDocumentFragment uploadDocumentFragment;
	public ModifyDocumentFragment modifyDocumentFragment;
	private FragmentManager supportFragmentManager;
	public static int cInsuranceType;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_upload_modify_document;
	}

	@Override
	protected void initView() {
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		// 获取从当前运单界面传过来的数据
		if (bundle != null && bundle.containsKey("ticketStatus")) {
			ticketStatus = bundle.getString("ticketStatus");
		}
		if (bundle != null && bundle.containsKey("ticketId")) {
			ticketId = bundle.getString("ticketId");
		}
		if (bundle != null && bundle.containsKey("packageBeginName")) {
			packageBeginName = bundle.getString("packageBeginName");
		}
		if (bundle != null && bundle.containsKey("packageEndName")) {
			packageEndName = bundle.getString("packageEndName");
		}
		if (bundle != null && bundle.containsKey("createTime")) {
			createTime = bundle.getString("createTime");
		}
		if (bundle != null && bundle.containsKey("cInsuranceType")) {
			cInsuranceType = bundle.getInt("cInsuranceType");
		}

		// 初始化titileBar
		initActionBar();

		mRadioGroup = (RadioGroup) findViewById(R.id.rg_uploadmodify_radio);
		mUpload = (RadioButton) findViewById(R.id.rb_uploadmodify_upload);
		mModify = (RadioButton) findViewById(R.id.rb_uploadmodify_modify);

		// 进入界面当前上传单据默认选中颜色设置为蓝色
		if (ticketStatus != null && ticketStatus.equals("5")) {
			mModify.setChecked(true);
			mUpload.setEnabled(false);
		} else {
			mUpload.setChecked(true);
		}

	}

	// 初始化titileBar的方法
	private void initActionBar() {
		if (ticketStatus != null && ticketStatus.equals("2")) {
			setActionBarTitle("待换票");
		} else if (ticketStatus != null && ticketStatus.equals("3")) {
			setActionBarTitle("待装运");
		} else if (ticketStatus != null && ticketStatus.equals("4")) {
			setActionBarTitle("待收货");
		} else if (ticketStatus != null && ticketStatus.equals("5")) {
			setActionBarTitle("待审核");
		}

		setActionBarLeft(R.drawable.nav_back);
		setOnActionBarLeftClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UploadAndModifyDocumentsActivity.this.finish();
			}
		});

	}

	@Override
	protected void initData() {
		uploadDocumentFragment = new UploadDocumentFragment();
		modifyDocumentFragment = new ModifyDocumentFragment();
		supportFragmentManager = getSupportFragmentManager();
		// 当是待审核状态
		if (ticketStatus != null && ticketStatus.equals("5")) {
			getSupportFragmentManager()
					.beginTransaction()
					.replace(R.id.fl_uploadmodify_container,
							modifyDocumentFragment).commit();
		} else {
			getSupportFragmentManager()
					.beginTransaction()
					.replace(R.id.fl_uploadmodify_container,
							uploadDocumentFragment).commit();
		}

	}

	@Override
	protected void setListener() {
		// 初始化按钮选中事件
		intiOnChecked();
	}

	// 初始化点击事件
	private void intiOnChecked() {
		mRadioGroup.setOnCheckedChangeListener(this);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		FragmentTransaction beginTransaction = supportFragmentManager
				.beginTransaction();
		switch (checkedId) {
		case R.id.rb_uploadmodify_upload:
			beginTransaction.replace(R.id.fl_uploadmodify_container,
					uploadDocumentFragment).commit();

			break;

		case R.id.rb_uploadmodify_modify:
			beginTransaction.replace(R.id.fl_uploadmodify_container,
					modifyDocumentFragment).commit();
			break;

		default:
			break;
		}
	}

	// 当前界面的跳转方法
	public static void invoke(Context activity, String ticketId,
			String ticketStatus, String packageBeginName,
			String packageEndName, String createTime, int insuranceType) {
		Intent intent = new Intent();
		intent.setClass(activity, UploadAndModifyDocumentsActivity.class);
		intent.putExtra("ticketId", ticketId);
		intent.putExtra("ticketStatus", ticketStatus);
		intent.putExtra("packageBeginName", packageBeginName);
		intent.putExtra("packageEndName", packageEndName);
		intent.putExtra("createTime", createTime);
		intent.putExtra("cInsuranceType", insuranceType);
		activity.startActivity(intent);
	}

	public String getTicketId() {
		return ticketId;
	}

	public void setTicketId(String ticketId) {
		this.ticketId = ticketId;
	}

	public String getTicketStatus() {
		return ticketStatus;
	}

	public void setTicketStatus(String ticketStatus) {
		this.ticketStatus = ticketStatus;
	}

	public String getPackageBeginName() {
		return packageBeginName;
	}

	public void setPackageBeginName(String packageBeginName) {
		this.packageBeginName = packageBeginName;
	}

	public String getPackageEndName() {
		return packageEndName;
	}

	public void setPackageEndName(String packageEndName) {
		this.packageEndName = packageEndName;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}

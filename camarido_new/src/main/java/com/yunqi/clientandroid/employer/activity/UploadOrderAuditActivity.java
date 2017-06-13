package com.yunqi.clientandroid.employer.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.activity.BaseActivity;
import com.yunqi.clientandroid.employer.fragment.ModifyOrderAuditFragment;
import com.yunqi.clientandroid.employer.fragment.UploadOrderAuditFragment;
import com.yunqi.clientandroid.utils.CacheUtils;
import com.yunqi.clientandroid.utils.L;
import com.yunqi.clientandroid.utils.StringUtils;

/**
 * @author chenrenyi
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 发包方待审核结算界面
 * @date 16/3/1
 */

public class UploadOrderAuditActivity extends BaseActivity implements
		RadioGroup.OnCheckedChangeListener {
	// ---------------------控件----------------------
	public RadioGroup mRadioGroup;
	public RadioButton mUpload;
	public RadioButton mModify;
	public UploadOrderAuditFragment uploadDocumentFragment;
	public ModifyOrderAuditFragment modifyDocumentFragment;
	private FragmentManager supportFragmentManager;
	// 存放SP的key
	public static final String TICKETSTATUS = "TICKET_STATUS";

	// ----------传过来的参数key---------------------
	private static final String TICKET_ID = "TICKET_ID";
	private static final String IS_TURN_TO_RECEIPT = "IS_TURN_TO_RECEIPT";
	private static final String TICKET_STATUS = "TICKET_STATUS";
	private static final String TICKET_CODE = "TICKET_CODE";
	// -----------传过来的参数------------------------
	private boolean isTurnToReceipt = false;// 是否跳转到单据Tab
	private String ticketStatus;// 执行状态
	private String ticketId;// 订单id
	private String ticketCode;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_upload_modify_document_audit_employer;
	}

	@Override
	protected void initView() {
		Intent intent = getIntent();
		// 获取参数
		isTurnToReceipt = intent.getBooleanExtra(IS_TURN_TO_RECEIPT, false);// 是否跳转到单据Tab
		ticketStatus = intent.getStringExtra(TICKET_STATUS);// 执行状态
		ticketId = intent.getStringExtra(TICKET_ID);// 订单id
		ticketCode = intent.getStringExtra(TICKET_CODE);//订单编号

		// TODO--缓存执行状态
		CacheUtils.putString(getApplicationContext(), TICKETSTATUS,
				ticketStatus + "");

		// 初始化titleBar
		initActionBar();

		mRadioGroup = (RadioGroup) findViewById(R.id.rg_uploadmodify_audit_radio_employer);
		mUpload = (RadioButton) findViewById(R.id.rb_uploadmodify_audit_upload_employer);
		mModify = (RadioButton) findViewById(R.id.rb_uploadmodify_audit_modify_employer);

	}

	@Override
	protected void initData() {
		uploadDocumentFragment = new UploadOrderAuditFragment();
		modifyDocumentFragment = new ModifyOrderAuditFragment();
		supportFragmentManager = getSupportFragmentManager();

		Log.e("TAG", "--------当前订单所处的状态----------" + ticketStatus);

		Log.e("TAG", "-------isTurnToReceipt-----------" + isTurnToReceipt + "");

		if (!isTurnToReceipt) {// 运单详情
			//友盟统计首页
			mUmeng.setCalculateEvents("waybill_settlement_details");

			mUpload.setChecked(true);
			supportFragmentManager
					.beginTransaction()
					.replace(R.id.fl_uploadmodify_audit_container_employer,
							uploadDocumentFragment).commit();
		} else {// 收据
			//友盟统计首页
			mUmeng.setCalculateEvents("waybill_document_information");

			mModify.setChecked(true);
			supportFragmentManager
					.beginTransaction()
					.replace(R.id.fl_uploadmodify_audit_container_employer,
							modifyDocumentFragment).commit();
		}

	}

	@Override
	protected void setListener() {
		// 初始化按钮选中事件
		intiOnChecked();
	}

	// 初始化titileBar的方法
	private void initActionBar() {
		if (StringUtils.isStrNotNull(ticketStatus)) {
			/*
			 * if (ticketStatus.equals("5")) { setActionBarTitle("待审核"); } else
			 */if (ticketStatus.equals("8")) {
				setActionBarTitle("已结算");
			} else if (ticketStatus.equals("6")) {
				setActionBarTitle("待结算");
			}
		}
		setActionBarLeft(R.drawable.nav_back);
		setActionBarRight(true,0,"地图");
		setOnActionBarLeftClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UploadOrderAuditActivity.this.finish();
			}
		});
		setOnActionBarRightClickListener(false, new OnClickListener() {
			@Override
			public void onClick(View v) {
				//友盟统计首页
				mUmeng.setCalculateEvents("waybill_click_map_enter");

				L.e("-----------ticketCode--------------"+ticketCode);
				if (StringUtils.isStrNotNull(ticketCode)){
					//进入地图导航页面
					EmployerMapActivity.invoke(mContext,ticketCode,ticketId);
				}
			}
		});

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
		case R.id.rb_uploadmodify_audit_upload_employer:
			beginTransaction.replace(
					R.id.fl_uploadmodify_audit_container_employer,
					uploadDocumentFragment).commit();

			break;

		case R.id.rb_uploadmodify_audit_modify_employer:
			beginTransaction.replace(
					R.id.fl_uploadmodify_audit_container_employer,
					modifyDocumentFragment).commit();
			break;

		default:
			break;
		}
	}

	/**
	 * 
	 * @Description:
	 * @Title:invokeNewTask
	 * @param context
	 *            上下文
	 * @param ticketId
	 *            运单ID
	 * @param isTurnToReceipt
	 *            是否跳到单据tab true为是，false为否
	 * @param ticketStatus
	 *            运单状态
	 * @return:void
	 * @throws @Create: Aug 30, 2016 11:54:55 AM
	 * @Author : chengtao
	 */
	public static void invokeNewTask(Context context, String ticketId,
			boolean isTurnToReceipt, String ticketStatus, String ticketCode) {
		Intent intent = new Intent(context, UploadOrderAuditActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra(TICKET_ID, ticketId);
		intent.putExtra(IS_TURN_TO_RECEIPT, isTurnToReceipt);
		intent.putExtra(TICKET_STATUS, ticketStatus);
		intent.putExtra(TICKET_CODE, ticketCode);
		context.startActivity(intent);
	}

	/**
	 * 
	 * @Description:
	 * @Title:newInvoke
	 * @param context
	 *            上下文
	 * @param ticketId
	 *            运单ID
	 * @param isTurnToReceipt
	 *            是否跳到单据tab true为是，false为否
	 * @param ticketStatus
	 *            运单状态
	 * @return:void
	 * @throws @Create: Aug 30, 2016 11:55:32 AM
	 * @Author : chengtao
	 */
	public static void newInvoke(Context context, String ticketId,
			boolean isTurnToReceipt, String ticketStatus, String ticketCode) {
		Intent intent = new Intent(context, UploadOrderAuditActivity.class);
		intent.putExtra(TICKET_ID, ticketId);
		intent.putExtra(IS_TURN_TO_RECEIPT, isTurnToReceipt);
		intent.putExtra(TICKET_STATUS, ticketStatus);
		intent.putExtra(TICKET_CODE, ticketCode);
		context.startActivity(intent);
	}

	/*
	 * public String getTicketId() { return ticketId; }
	 * 
	 * public void setTicketId(String ticketId) { this.ticketId = ticketId; }
	 * 
	 * public String getPackageBeginName() { return packageBeginName; }
	 * 
	 * public void setPackageBeginName(String packageBeginName) {
	 * this.packageBeginName = packageBeginName; }
	 * 
	 * public String getPackageBeginAddress() { return packageBeginAddress; }
	 * 
	 * public void setPackageBeginAddress(String packageBeginAddress) {
	 * this.packageBeginAddress = packageBeginAddress; }
	 * 
	 * public String getPackageEndName() { return packageEndName; }
	 * 
	 * public void setPackageEndName(String packageEndName) {
	 * this.packageEndName = packageEndName; }
	 * 
	 * public String getPackageEndAddress() { return packageEndAddress; }
	 * 
	 * public void setPackageEndAddress(String packageEndAddress) {
	 * this.packageEndAddress = packageEndAddress; }
	 * 
	 * public String getCreateTime() { return createTime; }
	 * 
	 * public void setCreateTime(String createTime) { this.createTime =
	 * createTime; }
	 * 
	 * public int getpInsuranceType() { return pInsuranceType; }
	 * 
	 * public void setpInsuranceType(int pInsuranceType) { this.pInsuranceType =
	 * pInsuranceType; }
	 * 
	 * public String getBeginCity() { return beginCity; }
	 * 
	 * public void setBeginCity(String beginCity) { this.beginCity = beginCity;
	 * }
	 * 
	 * public String getBeginCounty() { return beginCounty; }
	 * 
	 * public void setBeginCounty(String beginCounty) { this.beginCounty =
	 * beginCounty; }
	 * 
	 * public String getEndCity() { return endCity; }
	 * 
	 * public void setEndCity(String endCity) { this.endCity = endCity; }
	 * 
	 * public String getEndCounty() { return endCounty; }
	 * 
	 * public void setEndCounty(String endCounty) { this.endCounty = endCounty;
	 * }
	 */
	public String getTicketId() {
		return ticketId;
	}

	public String getTicketStatus() {
		return ticketStatus;
	}

}

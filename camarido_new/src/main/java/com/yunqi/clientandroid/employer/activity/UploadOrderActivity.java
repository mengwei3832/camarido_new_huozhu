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
import com.yunqi.clientandroid.employer.fragment.BillingDetailFragment;
import com.yunqi.clientandroid.employer.fragment.ModifyOrderAuditFragment;
import com.yunqi.clientandroid.employer.fragment.ModifyOrderFragment;
import com.yunqi.clientandroid.employer.fragment.UploadOrderFragment;
import com.yunqi.clientandroid.utils.L;
import com.yunqi.clientandroid.utils.StringUtils;

/**
 * @author zhangwb
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 发包方当前运单下上传单据与修改单据
 * @date 16/1/19
 */
public class UploadOrderActivity extends BaseActivity implements
		RadioGroup.OnCheckedChangeListener {

	private String ticketStatus;// 执行状态
	private String ticketId;// 订单id
	private String packageBeginName;// 出发地名称
	private String packageEndName;// 目的地名称
	private String createTime;// 创建时间
	public RadioGroup mRadioGroup;
	public RadioButton mUpload;
	public RadioButton mModify;
	public UploadOrderFragment uploadDocumentFragment;
	public ModifyOrderFragment modifyDocumentFragment;
	public ModifyOrderAuditFragment modifyOrderAuditFragment;
	public BillingDetailFragment billingDetailFragment;
	private FragmentManager supportFragmentManager;

	// 传递过来的二、三级地址
	private String beginCity;
	private String beginCounty;
	private String endCity;
	private String endCounty;
	private String ticketCode;

	// 新增
	private boolean isTurnToReceipt = false;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_upload_modify_document_employer;
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
		if (bundle != null && bundle.containsKey("isTurnToReceipt")) {
			isTurnToReceipt = bundle.getBoolean("isTurnToReceipt", false);
		}
		if (bundle != null && bundle.containsKey("ticketCode")){
			ticketCode = bundle.getString("ticketCode");
		}

		// 初始化titleBar
		initActionBar();

		mRadioGroup = (RadioGroup) findViewById(R.id.rg_uploadmodify_radio_employer);
		mUpload = (RadioButton) findViewById(R.id.rb_uploadmodify_upload_employer);
		mModify = (RadioButton) findViewById(R.id.rb_uploadmodify_modify_employer);

	}

	// 初始化titileBar的方法
	private void initActionBar() {
		Log.e("TAG", "-----------ticketStatus-------------" + ticketStatus);
		if (ticketStatus != null && ticketStatus.equals("1")) {
			setActionBarTitle("待执行");
		} else if (ticketStatus != null && ticketStatus.equals("2")) {
			setActionBarTitle("待执行");
		} else if (ticketStatus != null && ticketStatus.equals("3")) {
			setActionBarTitle("待执行");
		} else if (ticketStatus != null && ticketStatus.equals("4")) {
			setActionBarTitle("待收货");
		} else if (ticketStatus != null && ticketStatus.equals("8")) {
			setActionBarTitle("已结算");
		} /*
		 * else if (ticketStatus != null && ticketStatus.equals("9") ||
		 * (ticketStatus != null && ticketStatus.equals("10"))) {
		 * setActionBarTitle("已取消"); }
		 */
		// else if (ticketStatus != null && ticketStatus.equals("5")) {
		// setActionBarTitle("待审核");
		// }

		setActionBarLeft(R.drawable.nav_back);
		setActionBarRight(true,0,"地图");
		setOnActionBarLeftClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UploadOrderActivity.this.finish();
			}
		});
		setOnActionBarRightClickListener(false, new OnClickListener() {
			@Override
			public void onClick(View v) {
				L.e("----------TicketCode----------"+ticketCode);
				//进入地图导航页面
				if (StringUtils.isStrNotNull(ticketCode)){
					EmployerMapActivity.invoke(mContext,ticketCode,ticketId);
				}
			}
		});

	}

	@Override
	protected void initData() {
		uploadDocumentFragment = new UploadOrderFragment();
		modifyDocumentFragment = new ModifyOrderFragment();
		billingDetailFragment = new BillingDetailFragment();
		supportFragmentManager = getSupportFragmentManager();

		// 进入界面当前上传单据默认选中颜色设置为蓝色
		if (isTurnToReceipt) {
			mModify.setChecked(true);
		}
		try {
			switch (Integer.parseInt(ticketStatus)) {
			case 1:
			case 2:
			case 3:
			case 4:
				// mModify.setChecked(true);
				// mUpload.setEnabled(false);
				// getSupportFragmentManager().beginTransaction()
				// .replace(R.id.fl_uploadmodify_container_employer,
				// modifyDocumentFragment).commit();
				// break;
			case 8:
				mUpload.setChecked(true);
				getSupportFragmentManager()
						.beginTransaction()
						.replace(R.id.fl_uploadmodify_container_employer,
								billingDetailFragment).commit();
				break;
			default:
				break;
			}
		} catch (Exception e) {

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
		case R.id.rb_uploadmodify_upload_employer:
			beginTransaction.replace(R.id.fl_uploadmodify_container_employer,
					billingDetailFragment).commit();

			break;

		case R.id.rb_uploadmodify_modify_employer:
			beginTransaction.replace(R.id.fl_uploadmodify_container_employer,
					modifyDocumentFragment).commit();
			break;

		default:
			break;
		}
	}

	/**
	 * 
	 * @Description:本界面的跳转方法
	 * @Title:invoke
	 * @param activity
	 *            上下文
	 * @param ticketId
	 *            运单id
	 * @param ticketStatus
	 *            运单状态
	 * @param isTurnToReceipt
	 *            是否跳转单据界面
	 * @return:void
	 * @throws @Create: Aug 30, 2016 5:02:45 PM
	 * @Author : chengtao
	 */
	public static void invoke(Context activity, String ticketId,
			String ticketStatus, boolean isTurnToReceipt, String ticketCode) {
		Intent intent = new Intent();
		intent.setClass(activity, UploadOrderActivity.class);
		intent.putExtra("ticketId", ticketId);
		intent.putExtra("ticketStatus", ticketStatus);
		intent.putExtra("isTurnToReceipt", isTurnToReceipt);
		intent.putExtra("ticketCode",ticketCode);
		activity.startActivity(intent);
	}

	/**
	 * 
	 * @Description: 推送跳转
	 * @Title:invokeNewTask
	 * @param activity
	 *            上下文
	 * @param ticketId
	 *            运单id
	 * @param ticketStatus
	 *            运单状态
	 * @param isTurnToReceipt
	 *            是否跳转单据界面
	 * @return:void
	 * @throws @Create: Aug 30, 2016 5:04:13 PM
	 * @Author : chengtao
	 */
	public static void invokeNewTask(Context activity, String ticketId,
			String ticketStatus, boolean isTurnToReceipt) {
		Intent intent = new Intent();
		intent.setClass(activity, UploadOrderActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("ticketId", ticketId);
		intent.putExtra("ticketStatus", ticketStatus);
		intent.putExtra("isTurnToReceipt", isTurnToReceipt);
		activity.startActivity(intent);
	}

	public String getTicketId() {
		return ticketId;
	}

	public String getTicketStatus() {
		return ticketStatus;
	}

	/*
	 * public void setTicketId(String ticketId) { this.ticketId = ticketId; }
	 * 
	 * 
	 * public void setTicketStatus(String ticketStatus) { this.ticketStatus =
	 * ticketStatus; }
	 * 
	 * public String getPackageBeginName() { return packageBeginName; }
	 * 
	 * public void setPackageBeginName(String packageBeginName) {
	 * this.packageBeginName = packageBeginName; }
	 * 
	 * public String getPackageEndName() { return packageEndName; }
	 * 
	 * public void setPackageEndName(String packageEndName) {
	 * this.packageEndName = packageEndName; }
	 * 
	 * public String getCreateTime() { return createTime; }
	 * 
	 * public void setCreateTime(String createTime) { this.createTime =
	 * createTime; }
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

}

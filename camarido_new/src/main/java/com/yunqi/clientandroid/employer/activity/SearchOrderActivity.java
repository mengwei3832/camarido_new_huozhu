package com.yunqi.clientandroid.employer.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.activity.BaseActivity;
import com.yunqi.clientandroid.employer.adapter.KeyboardsAdapter;
import com.yunqi.clientandroid.employer.entity.TenantList;
import com.yunqi.clientandroid.employer.request.GetTenantListRequest;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.L;
import com.yunqi.clientandroid.utils.StringUtils;
import com.yunqi.clientandroid.view.SimplePopupWindow;
import com.yunqi.clientandroid.view.SimplePopupWindow.OnSimplePopupWindowItemClickListener;
import com.yunqi.clientandroid.view.wheel.NewChangeTimePopWin;
import com.yunqi.clientandroid.view.wheel.NewChangeTimePopWin.OnChangeTimeListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 
 * @Description:订单搜索界面
 * @ClassName: SearchOrderActivity
 * @author: chengtao
 * @date: Aug 13, 2016 10:21:46 AM
 * 
 */
public class SearchOrderActivity extends BaseActivity implements
		OnClickListener, OnChangeTimeListener,
		OnSimplePopupWindowItemClickListener {
	// 控件------------------------------------------

	// 主布局
	private LinearLayout llMain;
	// 派车时间
	private RelativeLayout rlTime;
	private TextView tvTime;
	// 车牌号
	private EditText etCarNumber;
	// 执行状态
	private RelativeLayout rlStatus;
	private TextView tvStaus;
	// 经纪人
	private RelativeLayout rlJingJiRen;
	private TextView tvJingJiRen;
	// 搜索
	private Button btnSearch;
	// 取消
	private Button btnCanle;
	// 进度条
	private ProgressBar progressBar;

	// 参数-------------------------------------------

	// 包ID
	private String packageId;
	private final static String PACKAGE_ID = "PACKAGE_ID";
	// 开始时间
	private String startTime = null;
	private final static String START_TIME = "START_TIME";
	// 结束时间
	private String endTime;
	private final static String END_TIME = "END_TIME";
	// 车牌号
	private String carNumber;
	private final static String CAR_NUMBER = "CAR_NUMBER";
	// 执行状态
	private String orderStatus;
	private final static String ORDER_STATUS = "ORDER_STATUS";
	// 经纪人
	private String jingJiRen;
	private final static String JING_JI_REN = "JING_JI_REN";

	// 请求--------------------------------------------
	private GetTenantListRequest getTenantListRequest;
	private final static int GET_TENANT_LIST_REQUEST = 1;
	// 其他-------------------------------------------

	// 时间弹窗
	private NewChangeTimePopWin timPop;
	// 执行状态弹窗
	private SimplePopupWindow orderStatusPop;
	// 经纪人弹窗
	private SimplePopupWindow jingJiRenPop;
	// 执行状态数据
	private List<String> orderStatusList = new ArrayList<String>();
	// 经纪人数据
	private List<TenantList> tenantLists = new ArrayList<TenantList>();
	private List<String> jingJiRenList = new ArrayList<String>();
	// 执行状态弹窗ID
	private final static int OREDER_STATUS_POP = 1;
	// 经纪人弹窗ID
	private final static int JING_JI_REN_POP = 2;

	@Override
	protected int getLayoutId() {
		return R.layout.employer_activity_search_order;
	}

	@Override
	protected void initView() {

		initActionBar();
		// 获取包ID
		packageId = getIntent().getStringExtra(PACKAGE_ID);
		// 主布局
		llMain = obtainView(R.id.ll_main);
		// 签收时间
		rlTime = obtainView(R.id.rl_new_time);
		tvTime = obtainView(R.id.tv_time);
		// 车牌号
		etCarNumber = obtainView(R.id.et_car_number);
		// 执行状态
		rlStatus = obtainView(R.id.rl_new_status);
		tvStaus = obtainView(R.id._tv_status);
		// 经纪人
		rlJingJiRen = obtainView(R.id.rl_new_jing_ji_ren);
		tvJingJiRen = obtainView(R.id._tv_jing_ji_ren);
		// 搜索
		btnSearch = obtainView(R.id.btn_search);
		// 取消
		btnCanle = obtainView(R.id.btn_cancle);
		// 进度条
		progressBar = obtainView(R.id.progress_bar);
		// 时间弹窗
		timPop = new NewChangeTimePopWin(mContext, this, true);

		displayLayout(false);

		Calendar calendar = Calendar.getInstance();

		// DatePickerDialog dialog = new DatePickerDialog(mContext, new
		// OnDateSetListener() {
		//
		// @Override
		// public void onDateSet(DatePicker view, int year, int monthOfYear,
		// int dayOfMonth) {
		// // TODO Auto-generated method stub
		//
		// }
		// }, calendar.YEAR, calendar.MONTH, calendar.DAY_OF_MONTH);
		//
		// dialog.show();

		addOrderStatusData();
		orderStatusPop = new SimplePopupWindow(mContext, orderStatusList,
				OREDER_STATUS_POP, this);
	}

	/**
	 * 
	 * @Description:添加执行状态列表数据
	 * @Title:addOrderStatusData
	 * @return:void
	 * @throws @Create: Aug 13, 2016 1:14:38 PM
	 * @Author : chengtao
	 */
	private void addOrderStatusData() {
		orderStatusList.add("待结算");
		orderStatusList.add("已结算");
		orderStatusList.add("待执行");
		orderStatusList.add("待收货");
		orderStatusList.add("全部");
	}

	@Override
	protected void initData() {
		getTenantListRequest = new GetTenantListRequest(mContext, packageId);
		getTenantListRequest.setRequestId(GET_TENANT_LIST_REQUEST);
		httpGet(getTenantListRequest);
	}

	/**
	 * 
	 * @Description:
	 * @Title:handleIntentArg
	 * @return:void
	 * @throws @Create: Aug 14, 2016 5:56:08 PM
	 * @Author : chengtao
	 */
	private void handleIntentArg() {
		// 开始时间
		if (StringUtils.isStrNotNull(getIntent().getStringExtra(START_TIME))) {
			startTime = getIntent().getStringExtra(START_TIME);
		}
		// 结束时间
		if (StringUtils.isStrNotNull(getIntent().getStringExtra(END_TIME))) {
			endTime = getIntent().getStringExtra(END_TIME);
		}
		// 车牌号
		carNumber = getIntent().getStringExtra(CAR_NUMBER);
		// 执行状态
		switch (getIntent().getIntExtra(ORDER_STATUS, -1)) {
		case 1:
			orderStatus = "待执行";
			break;
		case 4:
			orderStatus = "待收货";
			break;
		case 6:
			orderStatus = "待结算";
			break;
		case 8:
			orderStatus = "已结算";
			break;
		default:
			break;
		}
		// 经纪人
		for (TenantList list : tenantLists) {
			if (getIntent().getIntExtra(JING_JI_REN, -1) == list.Id) {
				jingJiRen = list.TenantAliasesname;
			}
		}
		// 设置控件信息mEndTime = "32503651200";
		// mStartTime = "946656000";
		if (StringUtils.isStrNotNull(startTime)
				&& StringUtils.isStrNotNull(endTime)) {
			if (startTime.equals("946656000") && endTime.equals("32503651200")) {
				startTime = StringUtils.formatModify(startTime);
				endTime = StringUtils.formatModify(endTime);
				tvTime.setText("");
			} else {
				startTime = StringUtils.formatModify(startTime);
				endTime = StringUtils.formatModify(endTime);
				tvTime.setText(startTime + "~" + endTime);
			}
		}
		if (StringUtils.isStrNotNull(carNumber)) {
			etCarNumber.setText(carNumber);
		}
		if (StringUtils.isStrNotNull(orderStatus)) {
			tvStaus.setText(orderStatus);
		}
		if (StringUtils.isStrNotNull(jingJiRen)) {
			tvJingJiRen.setText(jingJiRen);
		}
	}

	@Override
	protected void setListener() {
		// 签收时间
		rlTime.setOnClickListener(this);
		// 执行状态
		rlStatus.setOnClickListener(this);
		// 经纪人
		rlJingJiRen.setOnClickListener(this);
		// 取消
		btnCanle.setOnClickListener(this);
		// 搜索
		btnSearch.setOnClickListener(this);
		//车牌号
		etCarNumber.setOnClickListener(this);
	}

	/**
	 * 
	 * @Description:隐藏布局和现实布局
	 * @Title:displayLayout
	 * @param bool
	 * @return:void
	 * @throws @Create: Aug 13, 2016 11:39:30 AM
	 * @Author : chengtao
	 */
	private void displayLayout(boolean bool) {
		if (bool) {
			llMain.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.GONE);
		} else {
			llMain.setVisibility(View.GONE);
			progressBar.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 
	 * @Description:初始化ActionBar
	 * @Title:initActionBar
	 * @return:void
	 * @throws @Create: Aug 13, 2016 10:46:52 AM
	 * @Author : chengtao
	 */
	private void initActionBar() {
		setActionBarTitle("搜索");
		setActionBarLeft(R.drawable.fanhui);
		setActionBarRight(true, 0, "清空");
		setOnActionBarLeftClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		setOnActionBarRightClickListener(false, new OnClickListener() {

			@Override
			public void onClick(View v) {
				//友盟统计首页
				mUmeng.setCalculateEvents("search_click_emptied");

				clearSearchInfo();
			}
		});
	}

	/**
	 * 
	 * @Description:清空搜索条件
	 * @Title:clearSearchInfo
	 * @return:void
	 * @throws @Create: Aug 13, 2016 10:51:06 AM
	 * @Author : chengtao
	 */
	private void clearSearchInfo() {
		startTime = null;
		endTime = null;
		carNumber = null;
		orderStatus = null;
		jingJiRen = null;
		tvTime.setText("");
		etCarNumber.setText("");
		tvStaus.setText("");
		tvJingJiRen.setText("");
	}

	private boolean isCompleted() {
		/*
		 * if (!StringUtils.isStrNotNull(startTime) ||
		 * !StringUtils.isStrNotNull(endTime)) {
		 * showToast(getResources().getString(R.string._tv_car_time)); return
		 * false; } if (!StringUtils.isStrNotNull(carNumber)) {
		 * showToast(getResources().getString(R.string.employer_assign_addCar));
		 * return false; } if (!StringUtils.isStrNotNull(orderStatus)) {
		 * showToast(getResources().getString(R.string._tv_status)); return
		 * false; } if (!StringUtils.isStrNotNull(jingJiRen)) {
		 * showToast(getResources().getString(R.string._tv_jing_ji_ren)); return
		 * false; }
		 */
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.et_car_number://车牌号
				inputVehicleDialog();
				break;
		case R.id.rl_new_time:// 签收时间
			//友盟统计首页
			mUmeng.setCalculateEvents("search_click_signup_date");

			timPop.showPopWin(SearchOrderActivity.this);
			break;
		case R.id.rl_new_status:// 执行状态
			//友盟统计首页
			mUmeng.setCalculateEvents("search_click_execution_state");

			orderStatusPop.showPop(SearchOrderActivity.this);
			break;
		case R.id.rl_new_jing_ji_ren:// 经纪人
			//友盟统计首页
			mUmeng.setCalculateEvents("search_click_broker");

			jingJiRenPop.showPop(SearchOrderActivity.this);
			break;
		case R.id.btn_search:// 搜索
			//友盟统计首页
			mUmeng.setCalculateEvents("search_click_sure");

			if (isCompleted()) {

				handleInfoAndGo();
			}
			break;
		case R.id.btn_cancle:// 取消
			//友盟统计首页
			mUmeng.setCalculateEvents("search_click_cancel");

			finish();
			break;

		default:
			break;
		}

	}

	/**
	 * 
	 * @Description:整理相关信息并跳转承运情况统计界面
	 * @Title:handleInfoAndGo
	 * @return:void
	 * @throws @Create: Aug 13, 2016 2:08:11 PM
	 * @Author : chengtao
	 */
	private void handleInfoAndGo() {
		String mStartTime = null;
		String mEndTime = null;
		String mCarNumber = "";
		int mOrderStatus = -1;
		int mJingJiRen = -1;

		Log.e("TAG", "---------startTime开始------------" + startTime);
		Log.e("TAG", "---------endTime结束------------" + endTime);

		if (StringUtils.isStrNotNull(startTime)
				&& StringUtils.isStrNotNull(endTime)) {
			if (startTime.equals("1999-12-31")&& endTime.equals("2999-12-31")) {
				mEndTime = "32503651200";
				mStartTime = "946656000";
			} else {
				mStartTime = StringUtils.StringDateToDateLong(startTime);
				mEndTime = StringUtils.StringDateToDateLong(endTime);
			}
		} else {
			mEndTime = "32503651200";
			mStartTime = "946656000";
		}

		Log.e("TAG", "---------mstartTime开始LONG------------" + mStartTime);
		Log.e("TAG", "---------mendTime结束LONG------------" + mEndTime);

		carNumber = etCarNumber.getText().toString();

		if (StringUtils.isStrNotNull(carNumber)) {
			mCarNumber = carNumber;
		}

		carNumber = etCarNumber.getText().toString();
		if (StringUtils.isStrNotNull(carNumber)) {
			mCarNumber = carNumber;
		}

		if (StringUtils.isStrNotNull(orderStatus)) {
			if (orderStatus.equals("待结算")) {
				mOrderStatus = 6;
			} else if (orderStatus.equals("已结算")) {
				mOrderStatus = 8;
			} else if (orderStatus.equals("待执行")) {
				mOrderStatus = 1;
			} else if (orderStatus.equals("待收货")) {
				mOrderStatus = 4;
			}
		}
		
		L.e("---------jingJiRen判断-----------"+jingJiRen);
		
		if (StringUtils.isStrNotNull(jingJiRen)) {
			for (TenantList list : tenantLists) {
				L.e("---------jingJiRen便利-----------"+list.TenantAliasesname);
				if (StringUtils.isStrNotNull(list.TenantAliasesname)) {
					if (list.TenantAliasesname.equals(jingJiRen)) {
						mJingJiRen = list.Id;
						break;
					}
				} else{
					if(list.TenantName.equals(jingJiRen)) {
						mJingJiRen = list.Id;
						break;
					}
				}
			}
		}

		Log.e("TAG", "----------mStartTime--------------" + mStartTime);
		Log.e("TAG", "----------mEndTime--------------" + mEndTime);
		Log.e("TAG", "----------mCarNumber--------------" + mCarNumber);
		Log.e("TAG", "----------mOrderStatus--------------" + mOrderStatus);
		Log.e("TAG", "----------mJingJiRen+++++++++++++++++++" + mJingJiRen);
		ChengYunTongJiActivity.invoke(mContext, packageId, mStartTime,
				mEndTime, carNumber, mJingJiRen, mOrderStatus, 0);
		ChengYunTongJiActivity.isBack = true;
		finish();
	}

	@Override
	public void onTime(String startTime, String endTime) {
		tvTime.setText(startTime + " ~ " + endTime);
		this.startTime = startTime;
		this.endTime = endTime;
	}

	@Override
	public void onSimplePopupWindowItemClick(int id, String value) {
		switch (id) {
		case OREDER_STATUS_POP:// 执行状态
			orderStatus = value;
			tvStaus.setText(value);
			break;
		case JING_JI_REN_POP:// 经纪人
			jingJiRen = value;
			tvJingJiRen.setText(value);
			break;
		default:
			break;
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void onSuccess(int requestId, Response response) {
		super.onSuccess(requestId, response);
		boolean isSuccess = response.isSuccess;
		String message = response.message;
		switch (requestId) {
		case GET_TENANT_LIST_REQUEST:
			if (isSuccess) {
				tenantLists = response.data;
				if (tenantLists != null) {
					for (TenantList list : tenantLists) {
						
						if (StringUtils.isStrNotNull(list.TenantAliasesname)) {
							jingJiRenList.add(list.TenantAliasesname);
						} else {
							jingJiRenList.add(list.TenantName);
						}
					}
				}
				jingJiRenPop = new SimplePopupWindow(mContext, jingJiRenList,
						JING_JI_REN_POP, this);
				displayLayout(true);
				handleIntentArg();
			} else {
				showToast(message);
			}
			break;

		default:
			break;
		}

	}

	@Override
	public void onFailure(int requestId, int httpCode, Throwable error) {
		super.onFailure(requestId, httpCode, error);
		showToast(getResources().getString(R.string.net_error_toast));
	}

	/**
	 * 
	 * @Description:本界面跳转
	 * @Title:invoke
	 * @return:void
	 * @throws @Create: Aug 13, 2016 1:30:47 PM
	 * @Author : chengtao
	 */
	public static void invoke(Activity activity, String packageId) {
		Intent intent = new Intent(activity, SearchOrderActivity.class);
		intent.putExtra(PACKAGE_ID, packageId);
		activity.startActivityForResult(intent, 1);
	}

	/**
	 * 
	 * @Description:
	 * @Title:invoke
	 * @param activity
	 * @param packageId
	 * @param startTime
	 * @param endTime
	 * @param carNumber
	 * @param orderStatus
	 * @param jingJiRen
	 * @return:void
	 * @throws @Create: Aug 14, 2016 5:52:30 PM
	 * @Author : chengtao
	 */
	public static void invoke(Activity activity, String packageId,
			String startTime, String endTime, String carNumber,
			int orderStatus, int jingJiRen) {
		Intent intent = new Intent(activity, SearchOrderActivity.class);
		intent.putExtra(PACKAGE_ID, packageId);
		intent.putExtra(START_TIME, startTime);
		intent.putExtra(END_TIME, endTime);
		intent.putExtra(CAR_NUMBER, carNumber);
		intent.putExtra(ORDER_STATUS, orderStatus);
		intent.putExtra(JING_JI_REN, jingJiRen);
		activity.startActivity(intent);
	}

	public void inputVehicleDialog() {
		String[] provides = new String[]{
				"陕", "晋", "鲁", "豫", "新", "蒙", "青", "津", "辽", "吉", "黑"
				, "沪", "苏", "浙", "皖", "闽", "赣", "冀", "鄂", "湘"
				, "粤", "桂", "渝", "川", "贵", "云", "藏", "京", "甘"
				, "琼", "港", "澳", "台", "宁"
		};
		String[] texts = new String[]{
				"0","1", "2", "3", "4", "5", "6", "7", "8", "9"
				, "A", "B", "C", "D", "E", "F", "G", "H", "J"
				, "K", "L", "M", "N", "P", "Q", "R", "S"
				, "T", "U", "V", "W", "X", "Y", "Z"
		};
		// 实例化键盘集合
		final ArrayList<String> providesList = new ArrayList<>();
		final ArrayList<String> textsList = new ArrayList<>();

		// 把键盘的数据添加进集合中
		for (int i = 0, len = provides.length; i < len; i++) {
			providesList.add(provides[i]);
		}
		for (int i = 0, len = texts.length; i < len; i++) {
			textsList.add(texts[i]);
		}

		LayoutInflater layoutInflater = LayoutInflater.from(mContext);
		View net_view = layoutInflater.inflate(R.layout.dialog_vehicle_input, null);

		final EditText etInputBox1 = (EditText) net_view.findViewById(R.id.et_input_box1);
		final EditText etInputBox2 = (EditText) net_view.findViewById(R.id.et_input_box2);
		final EditText etInputBox3 = (EditText) net_view.findViewById(R.id.et_input_box3);
		final EditText etInputBox4 = (EditText) net_view.findViewById(R.id.et_input_box4);
		final EditText etInputBox5 = (EditText) net_view.findViewById(R.id.et_input_box5);
		final EditText etInputBox6 = (EditText) net_view.findViewById(R.id.et_input_box6);
		final EditText etInputBox7 = (EditText) net_view.findViewById(R.id.et_input_box7);
		final GridView gvView = (GridView) net_view.findViewById(R.id.gv_view);
		final GridView gvText = (GridView) net_view.findViewById(R.id.gv_view_text);
		final Button btCancel = (Button) net_view.findViewById(R.id.bt_cancel);
		final Button btSure = (Button) net_view.findViewById(R.id.bt_sure);
		final ImageView ivDelete = (ImageView) net_view.findViewById(R.id.iv_input_delete);

		KeyboardsAdapter mAdapter = new KeyboardsAdapter(providesList,mContext);
		gvView.setAdapter(mAdapter);

		KeyboardsAdapter mAdapterText = new KeyboardsAdapter(textsList,mContext);
		gvText.setAdapter(mAdapterText);

		AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		final AlertDialog builder = dialog.create();
		builder.setView(net_view);
		builder.setCancelable(true);
		builder.show();

		btCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				builder.dismiss();
			}
		});

		btSure.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//获取输入框的值
				String input1 = etInputBox1.getText().toString().trim();
				String input2 = etInputBox2.getText().toString().trim();
				String input3 = etInputBox3.getText().toString().trim();
				String input4 = etInputBox4.getText().toString().trim();
				String input5 = etInputBox5.getText().toString().trim();
				String input6 = etInputBox6.getText().toString().trim();
				String input7 = etInputBox7.getText().toString().trim();
				if (!TextUtils.isEmpty(input1) && !TextUtils.isEmpty(input2)
						&& !TextUtils.isEmpty(input3) && !TextUtils.isEmpty(input4)
						&& !TextUtils.isEmpty(input5) && !TextUtils.isEmpty(input6)
						&& !TextUtils.isEmpty(input7)){
					etCarNumber.setText(input1+input2+input3+input4+input5
							+input6+input7);
					builder.dismiss();
				} else {
					showToast("车牌号不完整");
				}
			}
		});

		gvView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String dCar = providesList.get(position).toString();
				etInputBox1.setText(dCar);
				gvView.setVisibility(View.GONE);
				gvText.setVisibility(View.VISIBLE);
				ivDelete.setVisibility(View.VISIBLE);
			}
		});
		gvText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String dVehicle = textsList.get(position).toString();
				//获取输入框的值
				String input1 = etInputBox1.getText().toString().trim();
				String input2 = etInputBox2.getText().toString().trim();
				String input3 = etInputBox3.getText().toString().trim();
				String input4 = etInputBox4.getText().toString().trim();
				String input5 = etInputBox5.getText().toString().trim();
				String input6 = etInputBox6.getText().toString().trim();
				String input7 = etInputBox7.getText().toString().trim();
				if (!dVehicle.equals("←")){
					if (!TextUtils.isEmpty(input1) && TextUtils.isEmpty(input2)){
						Pattern pattern = Pattern.compile("[0-9]*");
						Matcher isNum = pattern.matcher(dVehicle);
						if( !isNum.matches() ){
							etInputBox2.setText(dVehicle);
						}
					} else if (!TextUtils.isEmpty(input2) && TextUtils.isEmpty(input3)){
						etInputBox3.setText(dVehicle);
					} else if (!TextUtils.isEmpty(input3) && TextUtils.isEmpty(input4)){
						etInputBox4.setText(dVehicle);
					} else if (!TextUtils.isEmpty(input4) && TextUtils.isEmpty(input5)){
						etInputBox5.setText(dVehicle);
					} else if (!TextUtils.isEmpty(input5) && TextUtils.isEmpty(input6)){
						etInputBox6.setText(dVehicle);
					} else if (!TextUtils.isEmpty(input6) && TextUtils.isEmpty(input7)){
						etInputBox7.setText(dVehicle);
					}
				}
			}
		});
		ivDelete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//获取输入框的值
				String input1 = etInputBox1.getText().toString().trim();
				String input2 = etInputBox2.getText().toString().trim();
				String input3 = etInputBox3.getText().toString().trim();
				String input4 = etInputBox4.getText().toString().trim();
				String input5 = etInputBox5.getText().toString().trim();
				String input6 = etInputBox6.getText().toString().trim();
				String input7 = etInputBox7.getText().toString().trim();
				if (!TextUtils.isEmpty(input7)){
					etInputBox7.setText("");
				} else if (!TextUtils.isEmpty(input6)) {
					etInputBox6.setText("");
				} else if (!TextUtils.isEmpty(input5)) {
					etInputBox5.setText("");
				} else if (!TextUtils.isEmpty(input4)) {
					etInputBox4.setText("");
				} else if (!TextUtils.isEmpty(input3)) {
					etInputBox3.setText("");
				} else if (!TextUtils.isEmpty(input2)) {
					etInputBox2.setText("");
				} else if (!TextUtils.isEmpty(input1)) {
					etInputBox1.setText("");
					gvText.setVisibility(View.GONE);
					gvView.setVisibility(View.VISIBLE);
					ivDelete.setVisibility(View.GONE);
				}
			}
		});
	}
}

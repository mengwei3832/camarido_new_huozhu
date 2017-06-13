package com.yunqi.clientandroid.fragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.activity.DriverCertifiedActivity;
import com.yunqi.clientandroid.activity.FormerPayPassActivity;
import com.yunqi.clientandroid.activity.HelpActivity;
import com.yunqi.clientandroid.activity.LoginActicity;
import com.yunqi.clientandroid.activity.MainActivity;
import com.yunqi.clientandroid.activity.MyPromotionActivity;
import com.yunqi.clientandroid.activity.MyTicketActivity;
import com.yunqi.clientandroid.activity.MyWalletActivity;
import com.yunqi.clientandroid.activity.RealNameActivity;
import com.yunqi.clientandroid.activity.RecommendActivity;
import com.yunqi.clientandroid.activity.TagManagerActivity;
import com.yunqi.clientandroid.activity.TaskAwardActivity;
import com.yunqi.clientandroid.activity.VehicleListActivity;
import com.yunqi.clientandroid.adapter.MineListAdapter;
import com.yunqi.clientandroid.entity.MineListItem;
import com.yunqi.clientandroid.entity.MineTicketCount;
import com.yunqi.clientandroid.entity.MineWalletData;
import com.yunqi.clientandroid.entity.UserInfo;
import com.yunqi.clientandroid.http.request.GetCurrentTicketCountRequest;
import com.yunqi.clientandroid.http.request.GetUserInfoRequest;
import com.yunqi.clientandroid.http.request.GetWalletDataRequest;
import com.yunqi.clientandroid.http.request.SetUserInfoRequest;
import com.yunqi.clientandroid.http.request.UploadHeadImageRequest;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.CacheUtils;
import com.yunqi.clientandroid.utils.ImageUtil;
import com.yunqi.clientandroid.utils.MinelistHelper;
import com.yunqi.clientandroid.utils.PreManager;
import com.yunqi.clientandroid.utils.StringUtils;
import com.yunqi.clientandroid.utils.UserUtil;
import com.yunqi.clientandroid.view.MMAlert;
import com.yunqi.clientandroid.view.RoundImageView;
import com.yunqi.clientandroid.view.wheel.adapters.AbstractWheelTextAdapter;
import com.yunqi.clientandroid.view.wheel.views.OnWheelChangedListener;
import com.yunqi.clientandroid.view.wheel.views.OnWheelScrollListener;
import com.yunqi.clientandroid.view.wheel.views.WheelView;

/**
 * @author zhangwb
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 个人中心
 * @date 15/11/20
 */
public class MineFragment extends BaseFragment implements View.OnClickListener,
		AdapterView.OnItemClickListener {

	private MainActivity activity;
	private ListView lvMine;
	private View viewHeader;
	private View parentView;

	private EditText etNickNamePerson;

	private RelativeLayout rlPerform;
	private RelativeLayout rlCurrent;
	private RelativeLayout rlCompleted;
	private RelativeLayout rlPersonal;
	private RelativeLayout rlIncome;
	private RelativeLayout rlAge;
	private RelativeLayout rlSex;
	private RelativeLayout rlParent;
	private LinearLayout llParent;
	private RelativeLayout rlNickName;

	private TextView tvAllIncomeCount;// 累计收入数目
	private TextView tvSettlementCount;// 待结算数目
	private TextView tvRemainCount;// 账户余额
	private TextView tvNickName;// 用户昵称
	private TextView tvAge;
	private TextView tvSex;
	private TextView tvPhone;
	private TextView tvOrderFirst;
	private TextView tvOrderSecond;
	private TextView tvOrderThird;
	private TextView tvSexPerson;
	private TextView tvAgePerson;
	private TextView tvCancle;
	private TextView tvSure;

	// 头像
	private RoundImageView ivHead;

	private MineListAdapter mMineListAdapter;
	private DisplayImageOptions mOption;
	private List<MineListItem> mMinelist;

	// 本页面请求
	private GetCurrentTicketCountRequest mGetCurrentTicketCountRequest;
	private GetWalletDataRequest mGetWalletDataRequest;
	private UploadHeadImageRequest mUploadHeadImageRequest;
	private GetUserInfoRequest mGetUserInfoRequest;
	private SetUserInfoRequest mSetUserInfoRequest;

	// 本页面请求id
	private final int GET_CURRENT_TICKETCOUNT_REQUEST = 3;
	private final int GET_COUNT_WALLETDATA_REQUEST = 4;
	private final int UPLOAD_HEAD_IMAGE_REQUEST = 5;
	private final int GET_USER_INFO_REQUEST = 6;
	private final int SET_USER_INFO_REQUEST = 7;

	// flag标记
	public static final String STARTNEWCAR = "STARTNEWCAR";
	public static final String PERSONAL = "PERSONAL";
	// SP缓存key
	private final String GENDER = "GENDER";
	private final String USER_NAME = "USER_NAME";
	private final String NICKNAME = "NICKNAME";
	private final String BIRTHDAY = "BIRTHDAY";
	private final String AGE = "AGE";

	private Uri imageFileUri;
	private File profileImgFile;
	private PreManager mPreManager;
	private PopupWindow personalPopupWindow;
	private PopupWindow agePopupWindow;
	private PopupWindow sexPopupWindow;

	private ArrayList<String> listAge = new ArrayList<String>();// 存放年龄
	private ArrayList<String> listSex = new ArrayList<String>();// 存放性别
	private int maxsize = 24;// 设置字体的大小
	private int minsize = 14;// 设置字体的大小
	private String isDriver;
	private String isReal;
	private String ageName;// 选中的年龄
	private String sexName;// 选中的性别
	private String userName;// 账号
	private String name; // 真实姓名
	private String iDCode;// 身份证
	private String usernumber;// 隐藏后的电话号码

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_mine;
	}

	@Override
	protected void initData() {
		mMinelist = MinelistHelper.getMineList("-1", "-1");
		mMineListAdapter = new MineListAdapter(this.getActivity(), mMinelist);
		lvMine.setAdapter(mMineListAdapter);
		profileImgFile = new File(getActivity().getCacheDir(), "headphoto_");
		// 设置头像
		mOption = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisk(true).showImageOnLoading(R.drawable.mine_avatar)
				.showImageForEmptyUri(R.drawable.mine_avatar)
				.showImageOnFail(R.drawable.mine_avatar).build();

	}

	/**
	 * 获取用户信息
	 */
	private void getUserInfo() {
		// 获取用户信息
		mGetUserInfoRequest = new GetUserInfoRequest(getActivity());
		mGetUserInfoRequest.setRequestId(GET_USER_INFO_REQUEST);
		httpPost(mGetUserInfoRequest);
	}

	// 获取从服务器返回的订单数量
	private void getDataFromServiceTicketCount() {
		mGetCurrentTicketCountRequest = new GetCurrentTicketCountRequest(
				getActivity());
		mGetCurrentTicketCountRequest
				.setRequestId(GET_CURRENT_TICKETCOUNT_REQUEST);
		httpPost(mGetCurrentTicketCountRequest);
	}

	// 从服务器获取钱包数据
	private void getDataFromServiceWalletData() {
		mGetWalletDataRequest = new GetWalletDataRequest(getActivity());
		mGetWalletDataRequest.setRequestId(GET_COUNT_WALLETDATA_REQUEST);
		httpPost(mGetWalletDataRequest);
	}

	@Override
	protected void initView(View _rootView) {
		parentView = getActivity().getLayoutInflater().inflate(
				R.layout.fragment_mine, null);

		llParent = (LinearLayout) _rootView.findViewById(R.id.ll_mine_parent);
		lvMine = (ListView) _rootView.findViewById(R.id.lv_mine);

		viewHeader = LayoutInflater.from(this.getActivity()).inflate(
				R.layout.fragment_mine_header, null);
		rlPersonal = (RelativeLayout) viewHeader
				.findViewById(R.id.rl_to_personal);
		rlPerform = (RelativeLayout) viewHeader
				.findViewById(R.id.rl_order_perform);
		rlCurrent = (RelativeLayout) viewHeader
				.findViewById(R.id.rl_order_current);
		rlCompleted = (RelativeLayout) viewHeader
				.findViewById(R.id.rl_order_completed);
		rlIncome = (RelativeLayout) viewHeader.findViewById(R.id.rl_income);
		tvOrderFirst = (TextView) viewHeader.findViewById(R.id.tv_order_first);
		tvOrderSecond = (TextView) viewHeader
				.findViewById(R.id.tv_order_second);
		tvOrderThird = (TextView) viewHeader.findViewById(R.id.tv_order_third);
		tvAllIncomeCount = (TextView) viewHeader
				.findViewById(R.id.tv_all_income_count);
		tvSettlementCount = (TextView) viewHeader
				.findViewById(R.id.tv_settlement_count);
		tvRemainCount = (TextView) viewHeader
				.findViewById(R.id.tv_remain_count);
		tvNickName = (TextView) viewHeader.findViewById(R.id.tv_mine_nickName);
		tvAge = (TextView) viewHeader.findViewById(R.id.tv_mine_age);
		tvSex = (TextView) viewHeader.findViewById(R.id.tv_mine_sex);
		tvPhone = (TextView) viewHeader.findViewById(R.id.tv_mine_phonenumber);
		ivHead = (RoundImageView) viewHeader.findViewById(R.id.iv_mine_avatar);

		// 初始化个人信息弹窗
		settingPersonal();

		// 给listView添加一个head
		lvMine.addHeaderView(viewHeader);

		// PreManager类实例化
		mPreManager = PreManager.instance(getActivity());
		// 显示个人头像
		ImageLoader.getInstance().displayImage(mPreManager.getAvatar(), ivHead,
				mOption);

	}

	@Override
	protected void setListener() {
		lvMine.setOnItemClickListener(this);
		rlPerform.setOnClickListener(this);
		rlCurrent.setOnClickListener(this);
		rlCompleted.setOnClickListener(this);
		ivHead.setOnClickListener(this);
		rlPersonal.setOnClickListener(this);
		rlIncome.setOnClickListener(this);
	}

	/**
	 * 初始化ActionBar
	 */
	private void initActionBar() {
		activity = (MainActivity) getActivity();
		activity.getActionBar().show();
		activity.setActionBarTitle(getString(R.string.fragment_mine));
		activity.setActionBarLeft(0);
		// activity.setActionBarRight(true, R.drawable.mine_setting, "");
		activity.setOnActionBarLeftClickListener(null);
		// 不需要个人设置单独开一个 界面
		// activity.setOnActionBarRightClickListener(true,
		// new View.OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// activity.ibRightImage.setEnabled(false);
		// // 跳转到个人设置界面
		// PersonalActivity.invoke(getActivity());
		// activity.ibRightImage.setEnabled(true);
		// }
		// });
	}

	@Override
	public void onResume() {
		super.onResume();
		// 设置标题栏
		initActionBar();

		// 从服务器获取用户信息
		getUserInfo();

		// 从服务器获取订单个数
		getDataFromServiceTicketCount();
		// 从服务器获取钱包数据
		getDataFromServiceWalletData();

		// 设置个人设置按钮可点击
		// activity.ibRightImage.setEnabled(true);
		lvMine.setEnabled(true);

	}

	@Override
	public void onClick(View v) {
		// TODO--我的订单
		switch (v.getId()) {
		case R.id.rl_order_perform:
			// 跳转到待执行订单列表
			MyTicketActivity.invoke(getActivity(), "PERFORM");
			break;

		case R.id.rl_order_current:
			// 跳转到执行中订单列表
			MyTicketActivity.invoke(getActivity(), "CURRENT");
			break;

		case R.id.rl_order_completed:
			// 跳转到待结算订单列表
			MyTicketActivity.invoke(getActivity(), "COMPLETED");
			break;
		case R.id.iv_mine_avatar:
			// 上传头像
			showPickDialog();
			break;

		case R.id.rl_to_personal:
			// 点击设置显示个人信息
			showSettingPersonal();
			break;

		case R.id.rl_income:
			// 点击进入钱包
			MyWalletActivity.invoke(getActivity(), name, iDCode);
			break;

		}
	}

	// 设置个人信息的方法
	private void settingPersonal() {
		personalPopupWindow = new PopupWindow(getActivity());
		View personal_view = getActivity().getLayoutInflater().inflate(
				R.layout.mine_personal_ppw, null);

		personalPopupWindow.setWidth(LayoutParams.MATCH_PARENT);
		personalPopupWindow.setHeight(LayoutParams.WRAP_CONTENT);
		personalPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		personalPopupWindow.setContentView(personal_view);
		personalPopupWindow.setOutsideTouchable(true);
		personalPopupWindow.setFocusable(true);
		personalPopupWindow.setTouchable(true); // 设置PopupWindow可触摸
		personalPopupWindow
				.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

		rlParent = (RelativeLayout) personal_view
				.findViewById(R.id.rl_selvehicle_parent_mine);
		tvCancle = (TextView) personal_view
				.findViewById(R.id.tv_personal_cancle_mine);
		tvSure = (TextView) personal_view
				.findViewById(R.id.tv_personal_sure_mine);
		rlNickName = (RelativeLayout) personal_view
				.findViewById(R.id.rl_personal_nickname_mine);
		rlAge = (RelativeLayout) personal_view
				.findViewById(R.id.rl_personal_age_mine);
		rlSex = (RelativeLayout) personal_view
				.findViewById(R.id.rl_personal_sex_mine);
		etNickNamePerson = (EditText) personal_view
				.findViewById(R.id.et_personal_nickName_mine);
		tvSexPerson = (TextView) personal_view
				.findViewById(R.id.tv_personal_sex_mine);
		tvAgePerson = (TextView) personal_view
				.findViewById(R.id.tv_personal_age_mine);
	}

	// 设置显示个人信息的方法
	private void showSettingPersonal() {
		if (personalPopupWindow == null) {
			settingPersonal();
		}
		personalPopupWindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);

		// 点击取消
		tvCancle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				personalPopupWindow.dismiss();
			}
		});

		// 点击确定
		tvSure.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 点击提交个人信息
				String gender;
				String nickNamePerson = etNickNamePerson.getText().toString()
						.trim();
				String sexPerson = tvSexPerson.getText().toString().trim();
				String agePerson = tvAgePerson.getText().toString().trim();

				String userName = CacheUtils.getString(getActivity(),
						USER_NAME, "");// 从缓存中获取用户

				if (sexPerson.equals("男")) {
					gender = "1";
				} else if (sexPerson.equals("女")) {
					gender = "2";
				} else {
					gender = "0";
				}

				// 提交设置用户信息
				mSetUserInfoRequest = new SetUserInfoRequest(getActivity(),
						userName, nickNamePerson, gender, agePerson);
				mSetUserInfoRequest.setRequestId(SET_USER_INFO_REQUEST);
				httpPost(mSetUserInfoRequest);

				// 取消弹窗
				personalPopupWindow.dismiss();
			}
		});

		// 选择年龄
		rlAge.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showAgePopupWindow();
			}
		});

		// 选择性别
		rlSex.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showSexPopupWindow();
			}
		});

	}

	// 选择性别弹窗
	protected void showSexPopupWindow() {
		// 清空存放性别的集合
		listSex.clear();
		sexPopupWindow = new PopupWindow(getActivity());
		View minesex_view = getActivity().getLayoutInflater().inflate(
				R.layout.mine_sex_ppw, null);

		TextView tvCancle = (TextView) minesex_view
				.findViewById(R.id.tv_minesex_cancle);
		TextView tvSure = (TextView) minesex_view
				.findViewById(R.id.tv_minesex_sure);
		// 点击取消
		tvCancle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sexPopupWindow.dismiss();
			}
		});

		// 点击确定
		tvSure.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 选择年龄
				if (!TextUtils.isEmpty(sexName) && sexName != null) {
					tvSexPerson.setText(sexName);
				}
				// 取消弹窗
				sexPopupWindow.dismiss();
			}
		});

		WheelView mvSexList = (WheelView) minesex_view
				.findViewById(R.id.wv_minesex_show);

		// 添加年龄
		listSex.add("男");
		listSex.add("女");

		final SexTextAdapter sexTextAdapter = new SexTextAdapter(getActivity(),
				listSex, 0, maxsize, minsize);

		mvSexList.setVisibleItems(5);
		mvSexList.setViewAdapter(sexTextAdapter);
		mvSexList.setCurrentItem(0);
		mvSexList.addChangingListener(new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				String currentText = (String) sexTextAdapter.getItemText(wheel
						.getCurrentItem());
				sexName = currentText;
				setTextviewSize(currentText, sexTextAdapter);

			}
		});
		mvSexList.addScrollingListener(new OnWheelScrollListener() {
			@Override
			public void onScrollingStarted(WheelView wheel) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				String currentText = (String) sexTextAdapter.getItemText(wheel
						.getCurrentItem());
				setTextviewSize(currentText, sexTextAdapter);
			}
		});

		sexPopupWindow.setWidth(LayoutParams.MATCH_PARENT);
		sexPopupWindow.setHeight(LayoutParams.WRAP_CONTENT);
		sexPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		sexPopupWindow.setContentView(minesex_view);
		sexPopupWindow.setOutsideTouchable(true);
		sexPopupWindow.setFocusable(true);
		sexPopupWindow.setTouchable(true); // 设置PopupWindow可触摸
		sexPopupWindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);

	}

	// 选择年龄弹窗
	protected void showAgePopupWindow() {
		// 清空存放年龄的集合
		listAge.clear();
		agePopupWindow = new PopupWindow(getActivity());
		View mineage_view = getActivity().getLayoutInflater().inflate(
				R.layout.mine_age_ppw, null);

		TextView tvCancle = (TextView) mineage_view
				.findViewById(R.id.tv_mineage_cancle);
		TextView tvSure = (TextView) mineage_view
				.findViewById(R.id.tv_mineage_sure);
		// 点击取消
		tvCancle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				agePopupWindow.dismiss();
			}
		});

		// 点击确定
		tvSure.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 选择年龄
				if (!TextUtils.isEmpty(ageName) && ageName != null) {
					tvAgePerson.setText(ageName);
				}
				// 取消弹窗
				agePopupWindow.dismiss();
			}
		});

		WheelView mvAgeList = (WheelView) mineage_view
				.findViewById(R.id.wv_mineage_show);

		// 添加年龄
		listAge.add("60后");
		listAge.add("70后");
		listAge.add("80后");
		listAge.add("90后");

		final AgeTextAdapter ageTextAdapter = new AgeTextAdapter(getActivity(),
				listAge, 0, maxsize, minsize);

		mvAgeList.setVisibleItems(5);
		mvAgeList.setViewAdapter(ageTextAdapter);
		mvAgeList.setCurrentItem(0);
		mvAgeList.addChangingListener(new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				String currentText = (String) ageTextAdapter.getItemText(wheel
						.getCurrentItem());
				ageName = currentText;
				setTextviewSizeTwo(currentText, ageTextAdapter);

			}
		});
		mvAgeList.addScrollingListener(new OnWheelScrollListener() {
			@Override
			public void onScrollingStarted(WheelView wheel) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				String currentText = (String) ageTextAdapter.getItemText(wheel
						.getCurrentItem());
				setTextviewSizeTwo(currentText, ageTextAdapter);
			}
		});

		agePopupWindow.setWidth(LayoutParams.MATCH_PARENT);
		agePopupWindow.setHeight(LayoutParams.WRAP_CONTENT);
		agePopupWindow.setBackgroundDrawable(new BitmapDrawable());
		agePopupWindow.setContentView(mineage_view);
		agePopupWindow.setOutsideTouchable(true);
		agePopupWindow.setFocusable(true);
		agePopupWindow.setTouchable(true); // 设置PopupWindow可触摸
		agePopupWindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);

	}

	class AgeTextAdapter extends AbstractWheelTextAdapter {
		ArrayList<String> list;

		protected AgeTextAdapter(Context context, ArrayList<String> list,
				int currentItem, int maxsize, int minsize) {
			super(context, R.layout.item_birth_year, NO_RESOURCE, currentItem,
					maxsize, minsize);
			this.list = list;

			// 判断集合是否有数据
			if (listAge.size() > 0 && list.size() > 0) {

				ageName = list.get(0);// 初始化为第一个年龄

			}

			setItemTextResource(R.id.tempValue);
		}

		@Override
		public int getItemsCount() {
			return list.size();
		}

		@Override
		protected CharSequence getItemText(int index) {
			return list.get(index);
		}

	}

	class SexTextAdapter extends AbstractWheelTextAdapter {
		ArrayList<String> list;

		protected SexTextAdapter(Context context, ArrayList<String> list,
				int currentItem, int maxsize, int minsize) {
			super(context, R.layout.item_birth_year, NO_RESOURCE, currentItem,
					maxsize, minsize);
			this.list = list;

			// 判断集合是否有数据
			if (listSex.size() > 0 && list.size() > 0) {

				sexName = list.get(0);// 初始化为第一个性别

			}

			setItemTextResource(R.id.tempValue);
		}

		@Override
		public int getItemsCount() {
			return list.size();
		}

		@Override
		protected CharSequence getItemText(int index) {
			return list.get(index);
		}

	}

	/**
	 * 设置性别字体大小
	 * 
	 * @param curriteItemText
	 * @param adapter
	 */
	public void setTextviewSize(String curriteItemText, SexTextAdapter adapter) {
		ArrayList<View> arrayList = adapter.getTestViews();
		int size = arrayList.size();
		String currentText;
		for (int i = 0; i < size; i++) {
			TextView textvew = (TextView) arrayList.get(i);
			currentText = textvew.getText().toString();
			if (curriteItemText.equals(currentText)) {
				textvew.setTextSize(maxsize);
			} else {
				textvew.setTextSize(minsize);
			}
		}
	}

	/**
	 * 设置年龄字体大小
	 * 
	 * @param curriteItemText
	 * @param adapter
	 */
	public void setTextviewSizeTwo(String curriteItemText,
			AgeTextAdapter adapter) {
		ArrayList<View> arrayList = adapter.getTestViews();
		int size = arrayList.size();
		String currentText;
		for (int i = 0; i < size; i++) {
			TextView textvew = (TextView) arrayList.get(i);
			currentText = textvew.getText().toString();
			if (curriteItemText.equals(currentText)) {
				textvew.setTextSize(maxsize);
			} else {
				textvew.setTextSize(minsize);
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		if (position == 0)
			return;
		MineListItem mineListItem = mMineListAdapter.getItem(position - 1);

		switch (mineListItem.mineType) {
		case MinelistHelper.TYPE_MY_WALLET: // 我的钱包
			MyWalletActivity.invoke(getActivity(), name, iDCode);
			break;
		case MinelistHelper.TYPE_MY_VEHICLE: // 我的车辆
			// 跳转到车辆列表
			VehicleListActivity.invoke(getActivity(), name);
			break;

		case MinelistHelper.TYPE_ISREAL:// 实名认证
			// TODO--点击按钮进入实名认证界面
			if (userName != null && !TextUtils.isEmpty(userName)
					&& isReal != null && !TextUtils.isEmpty(isReal)) {
				RealNameActivity.invoke(getActivity(), userName, isReal);
			}

			break;

		case MinelistHelper.TYPE_TASK:// 任务奖励
			// TODO--任务奖励
			TaskAwardActivity.invoke(getActivity());
			break;

		case MinelistHelper.TYPE_RECOMMENDED:// 推荐奖励
			// TODO--推荐奖励
			RecommendActivity.invoke(getActivity());
			break;

		case MinelistHelper.TYPE_ISDRIVER:// 司机认证

			// 传真实姓名和身份证号码
			if (!TextUtils.isEmpty(name) && name != null
					&& !TextUtils.isEmpty(iDCode) && iDCode != null
					&& !TextUtils.isEmpty(isDriver) && isDriver != null) {
				DriverCertifiedActivity.invoke(getActivity(), name, iDCode,
						isDriver);
			} else {
				// 不传真实姓名
				if (!TextUtils.isEmpty(isDriver) && isDriver != null) {
					DriverCertifiedActivity.invokeTwo(getActivity(), isDriver);
				}
			}

			break;

		case MinelistHelper.TYPE_ATTENTION: // 我的关注
			TagManagerActivity.invoke(getActivity());
			break;
		case MinelistHelper.TYPE_MY_PROMOTION: // 我的推广
			MyPromotionActivity.invoke(getActivity());
			break;
		case MinelistHelper.TYPE_HELP: // 帮助
			HelpActivity.invoke(getActivity(), "all");
			break;
		case MinelistHelper.TYPE_LOGOUT: // 退出登录
			// 删除token过期时间
			PreManager.instance(getActivity()).removeTokenExpires();
			// 清空userId
			UserUtil.unSetUserId(getActivity());
			// 跳转到登录界面
			LoginActicity.invoke(getActivity());
			// 用户退出统计
			MobclickAgent.onProfileSignOff();
			// finish主界面
			getActivity().finish();
			break;
		}
	}

	@Override
	public void onSuccess(int requestId, Response response) {
		super.onSuccess(requestId, response);
		boolean isSuccess;
		String message;
		switch (requestId) {
		case GET_CURRENT_TICKETCOUNT_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				// 获取订单数据成功
				MineTicketCount mineTicketCount = (MineTicketCount) response.singleData;
				String beforeExecuteCount = mineTicketCount.beforeExecuteCount;
				String executingCount = mineTicketCount.executingCount;
				String executedCount = mineTicketCount.executedCount;

				// 设置待执行订单数量
				if (!TextUtils.isEmpty(beforeExecuteCount)
						&& beforeExecuteCount != null) {
					tvOrderFirst.setText(beforeExecuteCount + "单");
				}
				// 设置执行中订单数量
				if (!TextUtils.isEmpty(executingCount)
						&& executingCount != null) {
					tvOrderSecond.setText(executingCount + "单");
				}
				// 设置已完成订单数量
				if (!TextUtils.isEmpty(executedCount) && executedCount != null) {
					tvOrderThird.setText(executedCount + "单");
				}

			}
			break;

		case GET_COUNT_WALLETDATA_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				// 获取钱包数据成功
				MineWalletData mineWalletData = (MineWalletData) response.singleData;
				String accountBalance = mineWalletData.accountBalance;
				String freight = mineWalletData.freight;
				String totalIncome = mineWalletData.totalIncome;

				// 设置账号余额
				if (!TextUtils.isEmpty(accountBalance)
						&& accountBalance != null) {
					tvRemainCount.setText(accountBalance + "元");
				}
				// 设置待结算运费
				if (!TextUtils.isEmpty(freight) && freight != null) {
					tvSettlementCount.setText(freight + "元");
				}
				// 设置总收入
				if (!TextUtils.isEmpty(totalIncome) && totalIncome != null) {
					tvAllIncomeCount.setText(totalIncome + "元");
				}

			}

			break;

		case UPLOAD_HEAD_IMAGE_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				// 获取用户信息
				getUserInfo();
			} else {
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}
			}
			break;
		case GET_USER_INFO_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				// 获取用户信息成功
				UserInfo userInfo = (UserInfo) response.singleData;
				String nickname = userInfo.nickname;// 昵称
				String headPortraitUrl = userInfo.headPortraitUrl;// 头像Url
				String gender = userInfo.gender;// 性别：0：未知，1：男，2：女
				String years = userInfo.years;// 出生年代
				userName = userInfo.userName;// 账号--手机号
				name = userInfo.name;// 真实姓名
				iDCode = userInfo.iDCode;// 身份证号码
				isDriver = userInfo.isDriver;// 是否司机认证：0：未认证，1：认证中，2：已认证，3：认证失败
				isReal = userInfo.isReal;// 是否实名认证：0：未认证，1：认证中，2：已认证，3：认证失败
				String birthday = userInfo.birthday;// 生日

				// 缓存生日
				if (!TextUtils.isEmpty(birthday) && birthday != null) {
					CacheUtils.putString(getActivity(), BIRTHDAY, birthday);
				}

				// 缓存姓名
				if (!TextUtils.isEmpty(name) && name != null) {
					CacheUtils.putString(getActivity(), "Name", name);
				}

				// 设置昵称
				if (!TextUtils.isEmpty(nickname) && nickname != null) {
					tvNickName.setText(nickname);
					etNickNamePerson.setText(nickname);
				} else {
					tvNickName.setText("请设置昵称");
				}

				// 设置年龄
				if (!TextUtils.isEmpty(years) && years != null) {
					tvAge.setText(years);
					tvAgePerson.setText(years);
					CacheUtils.putString(getActivity(), AGE, years);
					tvAge.setVisibility(View.VISIBLE);
				} else {
					tvAge.setVisibility(View.INVISIBLE);
				}

				// 显示性别
				if (gender != null && gender.equals("1")) {
					tvSex.setText("男");
					tvSexPerson.setText("男");
					CacheUtils.putString(getActivity(), GENDER, "男");
					tvSex.setVisibility(View.VISIBLE);
				} else if (gender != null && gender.equals("2")) {
					tvSex.setText("女");
					tvSexPerson.setText("女");
					CacheUtils.putString(getActivity(), GENDER, "女");
					tvSex.setVisibility(View.VISIBLE);
				} else {
					tvSex.setVisibility(View.INVISIBLE);
				}

				// 显示电话号码
				if (!TextUtils.isEmpty(userName) && userName != null
						&& userName.length() >= 10) {
					usernumber = userName.substring(0, 3) + "*****"
							+ userName.substring(8, userName.length());
					tvPhone.setText(usernumber);

					CacheUtils.putString(getActivity(), USER_NAME, userName);
				}

				// 显示头像
				if (!TextUtils.isEmpty(headPortraitUrl)
						&& headPortraitUrl != null) {
					mPreManager.setAvatar(headPortraitUrl);
					ImageLoader.getInstance().displayImage(headPortraitUrl,
							ivHead, mOption);
				}

				// 设置认证状态
				mMinelist = MinelistHelper.getMineList(isReal, isDriver);
				mMineListAdapter.notifyDataSetChanged();
			} else {
				// 获取个人头像和昵称失败
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}
			}
			break;

		case SET_USER_INFO_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				UserInfo userInfo = (UserInfo) response.singleData;
				// 设置个人信息成功
				String nickname = userInfo.nickname;// 昵称
				String gender = userInfo.gender;// 性别：0：未知，1：男，2：女
				String years = userInfo.years;// 出生年代
				String birthday = userInfo.birthday;// 生日

				// 缓存生日
				if (!TextUtils.isEmpty(birthday) && birthday != null) {
					CacheUtils.putString(getActivity(), BIRTHDAY, birthday);
				}

				// 缓存年龄
				if (!TextUtils.isEmpty(years) && years != null) {
					CacheUtils.putString(getActivity(), AGE, years);
				}

				// 缓存昵称
				if (!TextUtils.isEmpty(nickname) && nickname != null) {
					CacheUtils.putString(getActivity(), NICKNAME, nickname);
				}

				// 缓存性别
				if (gender != null && gender.equals("1")) {
					CacheUtils.putString(getActivity(), GENDER, "男");
				} else if (gender != null && gender.equals("2")) {
					CacheUtils.putString(getActivity(), GENDER, "女");
				}

				// 设置昵称
				if (!TextUtils.isEmpty(nickname) && nickname != null) {
					tvNickName.setText(nickname);
					etNickNamePerson.setText(nickname);
				}

				// 设置年龄
				if (!TextUtils.isEmpty(years) && years != null) {
					tvAge.setText(years);
					tvAgePerson.setText(years);
					CacheUtils.putString(getActivity(), AGE, years);
					tvAge.setVisibility(View.VISIBLE);
				} else {
					tvAge.setVisibility(View.INVISIBLE);
				}

				// 显示性别
				if (gender != null && gender.equals("1")) {
					tvSex.setText("男");
					tvSexPerson.setText("男");
					CacheUtils.putString(getActivity(), GENDER, "男");
					tvSex.setVisibility(View.VISIBLE);
				} else if (gender != null && gender.equals("2")) {
					tvSex.setText("女");
					tvSexPerson.setText("女");
					CacheUtils.putString(getActivity(), GENDER, "女");
					tvSex.setVisibility(View.VISIBLE);
				} else {
					tvSex.setVisibility(View.INVISIBLE);
				}

			} else {
				// 设置个人信息失败
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}
			}

			break;

		default:
			break;
		}
	}

	@Override
	public void onFailure(int requestId, int httpCode, Throwable error) {
		switch (requestId) {
		case GET_USER_INFO_REQUEST:
			showToast("连接超时,请检查网络");
			break;
		case SET_USER_INFO_REQUEST:
			showToast("连接超时,请检查网络");
			break;

		}
	}

	/**
	 * 选择提示对话框
	 */
	public void showPickDialog() {
		String shareDialogTitle = getString(R.string.pick_dialog_title);
		MMAlert.showAlert(getActivity(), shareDialogTitle, getResources()
				.getStringArray(R.array.picks_item), null,
				new MMAlert.OnAlertSelectId() {
					@Override
					public void onClick(int whichButton) {
						switch (whichButton) {
						case 0: // 拍照
							try {
								imageFileUri = getActivity()
										.getContentResolver()
										.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
												new ContentValues());
								if (imageFileUri != null) {
									Intent i = new Intent(
											android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
									i.putExtra(
											android.provider.MediaStore.EXTRA_OUTPUT,
											imageFileUri);
									if (StringUtils.isIntentSafe(getActivity(),
											i)) {
										startActivityForResult(i, 2);
									} else {
										Toast.makeText(
												getActivity(),
												getString(R.string.dont_have_camera_app),
												Toast.LENGTH_SHORT).show();
									}
								} else {
									Toast.makeText(
											getActivity(),
											getString(R.string.cant_insert_album),
											Toast.LENGTH_SHORT).show();
								}
							} catch (Exception e) {
								Toast.makeText(getActivity(),
										getString(R.string.cant_insert_album),
										Toast.LENGTH_SHORT).show();
							}
							break;
						case 1: // 相册
							Intent intent = new Intent(Intent.ACTION_PICK);
							intent.setDataAndType(
									android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
									"image/*");
							startActivityForResult(intent, 1);
							break;
						default:
							break;
						}
					}

				});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case 1:// 如果是直接从相册获取
				if (data != null) {
					Uri uri = data.getData();
					startPhotoZoom(uri);
				}
				break;
			case 2:// 如果是调用相机拍照时
				String picPath = null;
				if (imageFileUri != null) {
					picPath = ImageUtil.getPicPathFromUri(imageFileUri,
							getActivity());
					int degree = 0;
					if (!StringUtils.isEmpty(picPath))
						degree = ImageUtil.readPictureDegree(picPath);
					Matrix matrix = new Matrix();
					if (degree != 0) {// 解决旋转问题
						matrix.preRotate(degree);
					}
					Uri uri = Uri.fromFile(new File(picPath));
					startPhotoZoom(uri);
				} else {
					Toast.makeText(getActivity(), "图片获取异常", Toast.LENGTH_SHORT)
							.show();
				}
				break;
			case 3:// 取得裁剪后的图片
				if (data != null) {
					setPicToView(data);
				}
				break;
			}
		}
	}

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {
		try {
			Intent intent = new Intent("com.android.camera.action.CROP");
			intent.setDataAndType(uri, "image/*");
			// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
			intent.putExtra("crop", "true");
			// aspectX aspectY 是宽高的比例
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			// outputX outputY 是裁剪图片宽高
			intent.putExtra("outputX", 300);
			intent.putExtra("outputY", 300);
			intent.putExtra("return-data", true);
			startActivityForResult(intent, 3);
		} catch (Exception e) {
			Toast.makeText(getActivity(), "图片裁剪异常", Toast.LENGTH_SHORT).show();
		}

	}

	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param picdata
	 */
	private void setPicToView(Intent picdata) {
		Bundle extras = picdata.getExtras();
		String headBase64 = "";
		String imageName = "";
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			int newWidth = 100;
			if (photo.getWidth() >= 100) {
				newWidth = photo.getWidth();
			}
			int newHeight = 100;
			if (photo.getHeight() >= 100) {
				newHeight = photo.getHeight();
			}
			if (photo.getWidth() < 100 || photo.getHeight() < 100) {
				photo = Bitmap.createScaledBitmap(photo, newWidth, newHeight,
						false);
			}
			try {
				photo.compress(Bitmap.CompressFormat.JPEG, 80,
						new FileOutputStream(profileImgFile));
				headBase64 = ImageUtil.bitmapToBase64(photo);
				imageName = profileImgFile.getName();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			boolean exit = profileImgFile.exists();
			if (!exit) {
				Toast.makeText(getActivity(), R.string.upload_photo_fail,
						Toast.LENGTH_SHORT).show();
				return;
			}

			mUploadHeadImageRequest = new UploadHeadImageRequest(getActivity(),
					imageName, headBase64);
			mUploadHeadImageRequest.setRequestId(UPLOAD_HEAD_IMAGE_REQUEST);
			httpPost(mUploadHeadImageRequest);
		}
	}
}

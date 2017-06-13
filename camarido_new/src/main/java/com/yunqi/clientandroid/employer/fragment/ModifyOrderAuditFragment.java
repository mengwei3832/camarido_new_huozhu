package com.yunqi.clientandroid.employer.fragment;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.employer.activity.CurrentTicketActivity;
import com.yunqi.clientandroid.employer.activity.EmployerMapActivity;
import com.yunqi.clientandroid.employer.activity.UploadOrderAuditActivity;
import com.yunqi.clientandroid.employer.adapter.AuditModifyOrderAdapter;
import com.yunqi.clientandroid.employer.request.BoHuiTicketRequest;
import com.yunqi.clientandroid.employer.request.CancleTicketRequest;
import com.yunqi.clientandroid.employer.request.GetTicketExecutedRequest;
import com.yunqi.clientandroid.employer.request.GetTicketSettlePreviewRequest;
import com.yunqi.clientandroid.employer.request.ShenHeTicketPassRequest;
import com.yunqi.clientandroid.employer.request.ShenHeTicketRequest;
import com.yunqi.clientandroid.employer.util.ModifyTicketBoHuiOnClick;
import com.yunqi.clientandroid.employer.util.ModifyTicketListItemOnClick;
import com.yunqi.clientandroid.entity.GetVehicleListInfo;
import com.yunqi.clientandroid.entity.ModifyListItem;
import com.yunqi.clientandroid.entity.OrderAuditInfo;
import com.yunqi.clientandroid.fragment.BaseFragment;
import com.yunqi.clientandroid.http.request.AgainQiangRequest;
import com.yunqi.clientandroid.http.request.PackVehicleListRequest;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.CacheUtils;
import com.yunqi.clientandroid.utils.StringUtils;

/**
 * @author zhangwb
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 发包方待审核过程界面
 * @date 16/1/19
 */
public class ModifyOrderAuditFragment extends BaseFragment implements
		PullToRefreshBase.OnRefreshListener2<ListView>, View.OnClickListener {

	private String ticketStatus;// 执行状态
	private String ticketId;// 订单id
	private String packageBeginName;// 起始地名称
	private String packageBeginAddress;
	private String packageEndName;// 目的地名称
	private String packageEndAddress;
	private int pInsuranceType;
	private String createTime;// 订单创建时间
	private Button btAuditModifyStopTicket;
	private boolean isEnd;// 是否服务器无数据返回
	private Handler handler = new Handler();
	private ArrayList<ModifyListItem> modifyListItem = new ArrayList<ModifyListItem>();
	private PullToRefreshListView mAuditRefreshlistview;
	private ListView auditModifyListView;
	private AuditModifyOrderAdapter auditModifyAdapter;
	private LinearLayout mLlGlobal;
	private View mProgress;
	private TextView tvProvenance;
	private TextView tvDestination;
	private LinearLayout llAddress;
	private Button bt_modifyAudit_again_qiang;
	private Button btModifyAuditShen;
	private Button btModifyAuditJie;
	private AlertDialog alertDialog;
	private AlertDialog alertDialogJie;
	private String ticketOperationStatus;
	private ImageView ivModifyBlank;

	private String vehicleId;
	// 存放SP的key
	public static final String TICKETSTATUS = "TICKET_STATUS";

	// 本页请求
	private GetTicketExecutedRequest mGetTicketExecutedHisRequest;
	private PackVehicleListRequest mPackVehicleListRequest;
	private AgainQiangRequest againQiangRequest;
	private ShenHeTicketRequest mShenHeTicketRequest;
	private BoHuiTicketRequest mBoHuiTicketRequest;
	private ShenHeTicketPassRequest mShenHeTicketPassRequest;
	private CancleTicketRequest cancleTicketRequest;

	// 本页请求ID
	private final int GET_EXECUTING_EXECUTEDHIS_REQUEST = 1;
	private final int GET_EXECUTING_AGAIN_QIANG = 2;
	private final int GET_SHENHE_TICKET = 3;
	private final int GET_BOHUI_TICKET = 4;
	private final int GET_TICKET_PASS = 5;
	private final int CANCLE_TICKET_REQUEST = 7;

	// 新增
	// ----------请求----------------
	private GetTicketSettlePreviewRequest getAddressRequest;
	// ---------请求id-------------------
	private static final int GET_ADRRESS_REQUEST = 6;

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		// 获取地址
		getAddress();
	}

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_modifyaudit_employer;
	}

	@Override
	protected void initView(View modifyView) {
		ticketId = ((UploadOrderAuditActivity) getActivity()).getTicketId();

		// TODO--获取缓存状态
		ticketStatus = CacheUtils.getString(getActivity(), TICKETSTATUS, "");

		mAuditRefreshlistview = (PullToRefreshListView) modifyView
				.findViewById(R.id.modifyAudit_refreshlistview_employer);

		llAddress = (LinearLayout) modifyView
				.findViewById(R.id.ll_modifyAudit_address_employer);
		tvProvenance = (TextView) modifyView
				.findViewById(R.id.tv_modifyAudit_provenance_employer);
		tvDestination = (TextView) modifyView
				.findViewById(R.id.tv_modifyAudit_destination_employer);

		mLlGlobal = (LinearLayout) modifyView
				.findViewById(R.id.ll_modifyAudit_global_employer);
		mProgress = (View) modifyView
				.findViewById(R.id.pb_modifyAudit_progress_employer);
		ivModifyBlank = (ImageView) modifyView
				.findViewById(R.id.iv_modifyAudit_blank);
		btAuditModifyStopTicket = (Button) modifyView
				.findViewById(R.id.bt_auditmodify_stop_ticket);

		// 设置起点和终点
		/*
		 * if (!TextUtils.isEmpty(packageBeginName) && packageBeginName != null
		 * && !TextUtils.isEmpty(packageEndName) && packageEndName != null) {
		 * tvProvenance.setText(packageBeginName);
		 * tvDestination.setText(packageEndName); } else {
		 * llAddress.setVisibility(View.GONE); }
		 */

		Log.e("TAG", "--------ticketStatus+++++++++++++" + ticketStatus);

	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		// bt_modifyAudit_again_qiang.setOnClickListener(this);
		// btModifyAuditShen.setOnClickListener(this);
		// btModifyAuditJie.setOnClickListener(this);
		// llAddress.setOnClickListener(this);
		btAuditModifyStopTicket.setOnClickListener(this);
	}

	@Override
	public void onResume() {
		super.onResume();
		mLlGlobal.setVisibility(View.INVISIBLE);
		mProgress.setVisibility(View.VISIBLE);
		// 清空存放数据的集合
		modifyListItem.clear();

		ticketStatus = ((UploadOrderAuditActivity) getActivity())
				.getTicketStatus();

		Log.e("TAG", "--------ticketStatus+++++++++++++" + ticketStatus);

		// 设置取消本次运单按钮的显示
		if (ticketStatus.equals("8")) {
			btAuditModifyStopTicket.setVisibility(View.GONE);
		}

		if (!TextUtils.isEmpty(ticketId) && ticketId != null) {
			// 访问服务器获取过程列表数据
			getDataFromService(ticketId);

		}
		// 判断审核、结算、再派一车按钮的显示
		// showButtonHiddle();

		// 初始化刷新view
		initPullToRefreshView();
	}

	/**
	 * 判断审核、结算、再派一车按钮的显示
	 */
	// private void showButtonHiddle(){
	// if (ticketOperationStatus != null && ticketOperationStatus.equals("0")) {
	// btModifyAuditShen.setVisibility(View.VISIBLE);
	// bt_modifyAudit_again_qiang.setVisibility(View.GONE);
	// } else if (ticketOperationStatus != null &&
	// ticketOperationStatus.equals("1")) {
	// btModifyAuditShen.setVisibility(View.VISIBLE);
	// bt_modifyAudit_again_qiang.setVisibility(View.GONE);
	// } else if ((ticketOperationStatus != null &&
	// ticketOperationStatus.equals("2"))
	// && (ticketStatus != null && ticketStatus.equals("8"))) {
	// btModifyAuditShen.setVisibility(View.GONE);
	// bt_modifyAudit_again_qiang.setVisibility(View.VISIBLE);
	// }
	// }

	// 访问服务器获取过程列表数据的方法
	private void getDataFromService(String ticketId) {

		mGetTicketExecutedHisRequest = new GetTicketExecutedRequest(
				getActivity(), ticketId);
		mGetTicketExecutedHisRequest
				.setRequestId(GET_EXECUTING_EXECUTEDHIS_REQUEST);
		httpPost(mGetTicketExecutedHisRequest);

	}

	// 初始化刷新view的方法
	private void initPullToRefreshView() {
		mAuditRefreshlistview.setMode(PullToRefreshBase.Mode.BOTH);
		mAuditRefreshlistview.getLoadingLayoutProxy(false, true).setPullLabel(
				getString(R.string.pull_to_loadmore));
		mAuditRefreshlistview.getLoadingLayoutProxy(false, true)
				.setRefreshingLabel(getString(R.string.pull_to_loading));
		mAuditRefreshlistview.getLoadingLayoutProxy(false, true)
				.setReleaseLabel(getString(R.string.pull_to_release));
		auditModifyListView = mAuditRefreshlistview.getRefreshableView();
		auditModifyListView.setDivider(new ColorDrawable(getResources()
				.getColor(R.color.carlistBackground)));
		// modifyListView.setDividerHeight(20);
		auditModifyListView.setSelector(android.R.color.transparent);// 隐藏listview默认的selector
		mAuditRefreshlistview.setOnRefreshListener(this);

		mAuditRefreshlistview
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO--条目点击方法

					}
				});

		auditModifyAdapter = new AuditModifyOrderAdapter(getActivity(),
				modifyListItem);
		auditModifyListView.setAdapter(auditModifyAdapter);

	}

	/**
	 * 审核订单
	 */
	// private void showBoHuiDialog(final String id){
	// AlertDialog.Builder builder = new Builder(getActivity());
	// // 设置对话框不能被取消
	// builder.setCancelable(false);
	//
	// View view = View.inflate(getActivity(),
	// R.layout.employer_dialog_ticket_bohui, null);
	// TextView tvCancle = (TextView) view
	// .findViewById(R.id.tv_dialog_quxiao);
	// TextView tvConfirm = (TextView) view
	// .findViewById(R.id.tv_dialog_queding);
	// final EditText etInputYuan = (EditText)
	// view.findViewById(R.id.et_dialog_bohui);
	//
	// tvCancle.setOnClickListener(new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// alertDialog.dismiss();
	// }
	// });
	//
	// tvConfirm.setOnClickListener(new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// //获得驳回的原因
	// String boYuanString =etInputYuan.getText().toString().trim();
	// getBoHuiRequest(id, boYuanString);
	// }
	// });
	//
	// alertDialog = builder.create();
	// alertDialog.setView(view, 0, 0, 0, 0);
	// alertDialog.show();
	// }
	//
	/**
	 * 审核订单
	 */
	// private void getShenHeRequest(String id){
	// mShenHeTicketRequest = new ShenHeTicketRequest(getActivity(), id);
	// mShenHeTicketRequest.setRequestId(GET_SHENHE_TICKET);
	// httpPost(mShenHeTicketRequest);
	// }

	/**
	 * 驳回订单
	 */
	// private void getBoHuiRequest(String id, String ticketMemo){
	// mBoHuiTicketRequest = new BoHuiTicketRequest(getActivity(), id,
	// ticketMemo);
	// mBoHuiTicketRequest.setRequestId(GET_BOHUI_TICKET);
	// httpPost(mBoHuiTicketRequest);
	// }

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		if (modifyListItem == null) {
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					mAuditRefreshlistview.onRefreshComplete();
				}
			}, 100);

			return;
		}

		if (modifyListItem != null) {
			auditModifyAdapter.notifyDataSetChanged();
		}

		// 清空存放过程数据的集合
		modifyListItem.clear();

		if (!TextUtils.isEmpty(ticketId) && ticketId != null) {
			// 访问服务器获取过程列表数据
			getDataFromService(ticketId);

		}
	}

	// 新增
	private void getAddress() {
		getAddressRequest = new GetTicketSettlePreviewRequest(getActivity(),
				ticketId);
		getAddressRequest.setRequestId(GET_ADRRESS_REQUEST);
		httpPost(getAddressRequest);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		if (!isEnd) {
		} else {
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					mAuditRefreshlistview.onRefreshComplete();
					showToast("已经是最后一页了");
				}
			}, 100);
		}
	}

	@Override
	public void onSuccess(int requestId, Response response) {
		boolean isSuccess;
		String message;

		switch (requestId) {
		// case GET_SHENHE_TICKET:
		// isSuccess = response.isSuccess;
		// message = response.message;
		// if (isSuccess) {
		// showToast(message);
		//
		// onPullDownToRefresh(mAuditRefreshlistview);
		// } else {
		// showToast(message);
		// }
		// break;
		//
		// case GET_BOHUI_TICKET:
		// isSuccess = response.isSuccess;
		// message = response.message;
		// if (isSuccess) {
		// showToast(message);
		//
		// CurrentTicketActivity.invoke(getActivity(), "");
		// } else {
		// showToast(message);
		// }
		// break;
		case GET_EXECUTING_EXECUTEDHIS_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;
			if (response.data == null) {
				showToast("暂无单据的信息");
				ivModifyBlank.setVisibility(View.VISIBLE);
			} else {
				ivModifyBlank.setVisibility(View.GONE);
			}

			if (isSuccess) {
				// 获取过程列表数据成功
				isEnd = true;// 服务器没有数据要返回
				ArrayList<ModifyListItem> modifyData = response.data;

				// 判断数据是否为空
				if (modifyData != null) {
					modifyListItem.addAll(modifyData);
				}

				if (modifyData.size() == 0) {
					showToast("暂无单据的信息");
					ivModifyBlank.setVisibility(View.VISIBLE);
				} else {
					ivModifyBlank.setVisibility(View.GONE);
				}

//				String statusString = String.valueOf(modifyData
//						.get(modifyListItem.size() - 1).TicketOperationType);
//
//				ticketOperationStatus = String.valueOf(modifyData
//						.get(modifyListItem.size() - 1).TicketOperationStatus);

				auditModifyAdapter.notifyDataSetChanged();
				mAuditRefreshlistview.onRefreshComplete();// 结束刷新的方法

			} else {
				// 获取过程列表数据失败
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}
			}

			mLlGlobal.setVisibility(View.VISIBLE);
			mProgress.setVisibility(View.INVISIBLE);

			break;

		// case GET_EXECUTING_AGAIN_QIANG:// 再派一车
		// isSuccess = response.isSuccess;
		// message = response.message;
		// if (isSuccess) {
		// showToast(message);
		// // 设置再派一车按钮不可点击
		// setViewAgainEnable(false);
		// bt_modifyAudit_again_qiang.setBackgroundColor(Color.GRAY);
		// } else {
		// showToast(message);
		// // 设置再派一车按钮可点击
		// setViewAgainEnable(true);
		// }
		// break;

		// case GET_TICKET_PASS:
		// isSuccess = response.isSuccess;
		// message = response.message;
		// if (isSuccess) {
		// showToast(message);
		//
		// //是否结算
		// showJieSuanDialog();
		// } else {
		// showToast(message);
		// setViewShenEnable(true);
		// }
		// break;
		// 新增
		case GET_ADRRESS_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				OrderAuditInfo orderAuditInfo = (OrderAuditInfo) response.singleData;
				String StartAddressName = orderAuditInfo.StartAddressName; // 起始地址名称
				String StartCityName = orderAuditInfo.StartCityName; // 起始市级名称
				String StartSubName = orderAuditInfo.StartSubName; // 起始区县名称
				String EndAddressName = orderAuditInfo.EndAddressName; // 目的地址名称
				String EndCityName = orderAuditInfo.EndCityName; // 目的市级名称
				String EndSubName = orderAuditInfo.EndSubName; // 目的区县名称
				// 添加地址
				// 开始地址
				if (StringUtils.isStrNotNull(StartAddressName)) {
					tvProvenance.setText(StartAddressName);
				}
				// 目的地
				if (StringUtils.isStrNotNull(EndAddressName)) {
					tvDestination.setText(EndAddressName);
				}

			} else {
				showToast(message);
			}
			break;

		case CANCLE_TICKET_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				showToast(message);
				getActivity().finish();
			} else {
				showToast(message);
				setEnabled(true);
			}
			break;

		default:
			break;
		}

	}

	@Override
	public void onFailure(int requestId, int httpCode, Throwable error) {
		showToast("连接超时,请检查网络");
		switch (requestId) {
		case CANCLE_TICKET_REQUEST:
			setEnabled(true);
			break;

		default:
			break;
		}
	}

	/**
	 * 是否直接结算
	 */
	// private void showJieSuanDialog(){
	// AlertDialog.Builder builder = new Builder(getActivity());
	// // 设置对话框不能被取消
	// builder.setCancelable(false);
	//
	// View view = View.inflate(getActivity(),
	// R.layout.employer_dialog_ticket_jiesuan, null);
	// TextView tvCancle = (TextView) view
	// .findViewById(R.id.tv_dialog_quxiao);
	// TextView tvConfirm = (TextView) view
	// .findViewById(R.id.tv_dialog_queding);
	//
	// tvCancle.setOnClickListener(new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// alertDialogJie.dismiss();
	// CurrentTicketActivity.invoke(getActivity(), "");
	// }
	// });
	//
	// tvConfirm.setOnClickListener(new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// UploadOrderAuditActivity.invoke(getActivity(), ticketId, TICKETSTATUS,
	// packageBeginName, packageBeginAddress, packageEndName,
	// packageEndAddress, createTime, pInsuranceType);
	// }
	// });
	//
	// alertDialogJie = builder.create();
	// alertDialogJie.setView(view, 0, 0, 0, 0);
	// alertDialogJie.show();
	// }

	private void setEnabled(boolean enabled) {
		if (enabled) {
			btAuditModifyStopTicket.setEnabled(enabled);
			btAuditModifyStopTicket
					.setBackgroundResource(R.drawable.sendbao_btn_background);
		} else {
			btAuditModifyStopTicket.setEnabled(enabled);
			btAuditModifyStopTicket
					.setBackgroundResource(R.drawable.btn_zhihui);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_auditmodify_stop_ticket:
			//友盟统计首页
			mUmeng.setCalculateEvents("waybill_click_cancel_current");

			// 弹出取消运单框
			showLogoutDialog();
			setEnabled(false);
			break;

		default:
			break;
		}
	}

	/**
	 * 
	 * @Description:退出对话框
	 * @Title:showLogoutDialog
	 * @return:void
	 * @throws
	 * @Create: 2016年6月15日 下午4:30:37
	 * @Author : chengtao
	 */
	private void showLogoutDialog() {
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		View view = inflater
				.inflate(R.layout.employer_dialog_stop_yundan, null);
		RelativeLayout btnCancle = (RelativeLayout) view
				.findViewById(R.id.rl_ticket_stop_cancle);
		RelativeLayout btnSure = (RelativeLayout) view
				.findViewById(R.id.rl_ticket_stop_sure);
		AlertDialog.Builder builder = new Builder(getActivity());
		final AlertDialog dialog = builder.create();
		dialog.setView(view);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		btnCancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//友盟统计首页
				mUmeng.setCalculateEvents("waybill_click_cancel_current_cancel");

				dialog.dismiss();
				setEnabled(true);
			}
		});
		btnSure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//友盟统计首页
				mUmeng.setCalculateEvents("waybill_click_cancel_current_sure");

				// 取消运单
				canclePackage();
				dialog.dismiss();
			}
		});
	}

	/**
	 * 
	 * @Description:申请取消订单
	 * @Title:canclePackage
	 * @return:void
	 * @throws
	 * @Create: 2016年6月28日 下午7:43:17
	 * @Author : chengtao
	 */
	private void canclePackage() {
		cancleTicketRequest = new CancleTicketRequest(getActivity(), ticketId,
				"");
		cancleTicketRequest.setRequestId(CANCLE_TICKET_REQUEST);
		httpPost(cancleTicketRequest);
	}

	// 再派一车
	private void againQiangCar() {
		againQiangRequest = new AgainQiangRequest(getActivity(), ticketId);
		againQiangRequest.setRequestId(GET_EXECUTING_AGAIN_QIANG);
		httpPost(againQiangRequest);

		// 设置再派一车按钮不可点击
		setViewAgainEnable(false);
	}

	/**
	 * 设置再派一车按钮不可点击
	 */
	private void setViewAgainEnable(boolean bEnable) {
		bt_modifyAudit_again_qiang.setEnabled(bEnable);
	}

	/**
	 * 设置运单审核按钮不可点击
	 */
	private void setViewShenEnable(boolean bEnable) {
		btModifyAuditShen.setEnabled(bEnable);
	}

}

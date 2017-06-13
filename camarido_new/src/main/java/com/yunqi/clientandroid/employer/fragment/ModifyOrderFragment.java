package com.yunqi.clientandroid.employer.fragment;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
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
import com.yunqi.clientandroid.employer.activity.UploadOrderActivity;
import com.yunqi.clientandroid.employer.adapter.AuditModifyOrderAdapter;
import com.yunqi.clientandroid.employer.adapter.ModifyOrderAdapter;
import com.yunqi.clientandroid.employer.request.BoHuiTicketRequest;
import com.yunqi.clientandroid.employer.request.CanclePackageRequest;
import com.yunqi.clientandroid.employer.request.CancleTicketRequest;
import com.yunqi.clientandroid.employer.request.GetTicketExecutedRequest;
import com.yunqi.clientandroid.employer.request.GetTicketSettlePreviewRequest;
import com.yunqi.clientandroid.employer.request.ShenHeTicketRequest;
import com.yunqi.clientandroid.employer.util.ModifyTicketBoHuiOnClick;
import com.yunqi.clientandroid.employer.util.ModifyTicketListItemOnClick;
import com.yunqi.clientandroid.entity.ModifyListItem;
import com.yunqi.clientandroid.entity.OrderAuditInfo;
import com.yunqi.clientandroid.fragment.BaseFragment;
import com.yunqi.clientandroid.http.response.Response;

/**
 * @author zhangwb
 * @version version_code (e.g, V1.0)
 * @Copyright (c) 2015
 * @Description class 发包方订单执行过程列表
 * @date 16/1/19
 */
public class ModifyOrderFragment extends BaseFragment implements
		PullToRefreshBase.OnRefreshListener2<ListView>, View.OnClickListener {

	private String ticketStatus;// 执行状态
	private String ticketId;// 订单id
	private String packageBeginName;// 起始地名称
	private String packageEndName;// 目的地名称
	private String createTime;// 订单创建时间
	private boolean isEnd;// 是否服务器无数据返回
	private Handler handler = new Handler();
	private ArrayList<ModifyListItem> modifyListItem = new ArrayList<ModifyListItem>();
	private PullToRefreshListView mModifyRefreshlistview;
	private ListView modifyListView;
	// private ModifyOrderAdapter modifyAdapter;
	private AuditModifyOrderAdapter auditModifyAdapter;
	private LinearLayout mLlGlobal;
	private ProgressBar mProgress;
	private TextView tvProvenance;
	private TextView tvDestination;
	private LinearLayout llAddress;
	private AlertDialog alertDialog;
	private ImageView ivModifyBlank;
	private Button btModifyStopTicket;

	// 本页请求
	private GetTicketExecutedRequest mGetTicketExecutedHisRequest;
	private ShenHeTicketRequest mShenHeTicketRequest;
	private BoHuiTicketRequest mBoHuiTicketRequest;
	private CancleTicketRequest cancleTicketRequest;

	// 本页请求ID
	private final int GET_EXECUTING_EXECUTEDHIS_REQUEST = 1;
	private final int GET_SHENHE_TICKET = 2;
	private final int GET_BOHUI_TICKET = 3;
	private final int CANCLE_TICKET_REQUEST = 4;

	@Override
	protected void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_modify_employer;
	}

	@Override
	protected void initView(View modifyView) {
		ticketId = ((UploadOrderActivity) getActivity()).getTicketId();
		ticketStatus = ((UploadOrderActivity) getActivity()).getTicketStatus();

		/*
		 * packageBeginName = ((UploadOrderActivity) getActivity())
		 * .getPackageBeginName();
		 * 
		 * packageEndName = ((UploadOrderActivity) getActivity())
		 * .getPackageEndName();
		 * 
		 * createTime = ((UploadOrderActivity) getActivity()).getCreateTime();
		 */
		mModifyRefreshlistview = (PullToRefreshListView) modifyView
				.findViewById(R.id.modifyDocument_refreshlistview_employer);

		llAddress = (LinearLayout) modifyView
				.findViewById(R.id.ll_modifyDocument_address_employer);
		tvProvenance = (TextView) modifyView
				.findViewById(R.id.tv_modifyDocument_provenance_employer);
		tvDestination = (TextView) modifyView
				.findViewById(R.id.tv_modifyDocument_destination_employer);

		mLlGlobal = (LinearLayout) modifyView
				.findViewById(R.id.ll_modifyDocument_global_employer);
		mProgress = (ProgressBar) modifyView
				.findViewById(R.id.pb_modifyDocument_progress_employer);
		ivModifyBlank = (ImageView) modifyView
				.findViewById(R.id.iv_modify_blank);
		btModifyStopTicket = (Button) modifyView
				.findViewById(R.id.bt_modify_stop_ticket);

		// 设置起点和终点
		if (!TextUtils.isEmpty(packageBeginName) && packageBeginName != null
				&& !TextUtils.isEmpty(packageEndName) && packageEndName != null) {
			tvProvenance.setText(packageBeginName);
			tvDestination.setText(packageEndName);
		} else {
			llAddress.setVisibility(View.GONE);
		}

		// 设置取消本次运单按钮的显示
		if (ticketStatus.equals("8")) {
			btModifyStopTicket.setVisibility(View.GONE);
		}

	}

	@Override
	protected void setListener() {
		llAddress.setOnClickListener(this);
		btModifyStopTicket.setOnClickListener(this);
	}

	@Override
	public void onResume() {
		super.onResume();
		mLlGlobal.setVisibility(View.INVISIBLE);
		mProgress.setVisibility(View.VISIBLE);
		// 清空存放数据的集合
		modifyListItem.clear();

		if (!TextUtils.isEmpty(ticketId) && ticketId != null) {
			// 访问服务器获取过程列表数据
			getDataFromService(ticketId);

		}
		// 初始化刷新view
		initPullToRefreshView();
	}

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
		mModifyRefreshlistview.setMode(PullToRefreshBase.Mode.BOTH);
		mModifyRefreshlistview.getLoadingLayoutProxy(false, true).setPullLabel(
				getString(R.string.pull_to_loadmore));
		mModifyRefreshlistview.getLoadingLayoutProxy(false, true)
				.setRefreshingLabel(getString(R.string.pull_to_loading));
		mModifyRefreshlistview.getLoadingLayoutProxy(false, true)
				.setReleaseLabel(getString(R.string.pull_to_release));
		modifyListView = mModifyRefreshlistview.getRefreshableView();
		modifyListView.setDivider(new ColorDrawable(getResources().getColor(
				R.color.carlistBackground)));
		// modifyListView.setDividerHeight(20);
		modifyListView.setSelector(android.R.color.transparent);// 隐藏listview默认的selector
		mModifyRefreshlistview.setOnRefreshListener(this);

		mModifyRefreshlistview
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO--条目点击方法

					}
				});

		// modifyAdapter = new ModifyOrderAdapter(getActivity(), modifyListItem,
		// packageBeginName, packageEndName, createTime,
		// new ModifyTicketListItemOnClick() {
		// @Override
		// public void onClick(View item, int position, String id) {
		// //审核按钮
		// getShenHeRequest(id);
		// }
		// }, new ModifyTicketBoHuiOnClick() {
		// @Override
		// public void onClick(View item, int position, String id) {
		// //驳回按钮
		// showBoHuiDialog(id);
		// }
		// });
		auditModifyAdapter = new AuditModifyOrderAdapter(getActivity(),
				modifyListItem);
		modifyListView.setAdapter(auditModifyAdapter);

	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		if (modifyListItem == null) {
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					mModifyRefreshlistview.onRefreshComplete();
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

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		if (!isEnd) {
		} else {
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					mModifyRefreshlistview.onRefreshComplete();
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
		case GET_SHENHE_TICKET:
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				showToast(message);

				onPullDownToRefresh(mModifyRefreshlistview);
			} else {
				showToast(message);
			}
			break;

		case GET_BOHUI_TICKET:
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				showToast(message);

				CurrentTicketActivity.invoke(getActivity(), "", 1, -1);
			} else {
				showToast(message);
			}
			break;

		case GET_EXECUTING_EXECUTEDHIS_REQUEST:
			isSuccess = response.isSuccess;
			message = response.message;
			if (response.data == null) {
				showToast("暂无执行过程列表的信息");
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

				if (modifyListItem.size() == 0) {
					showToast("暂无执行过程列表的信息");
					ivModifyBlank.setVisibility(View.VISIBLE);
				} else {
					ivModifyBlank.setVisibility(View.GONE);
				}

				auditModifyAdapter.notifyDataSetChanged();
				mModifyRefreshlistview.onRefreshComplete();// 结束刷新的方法

			} else {
				// 获取过程列表数据失败
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}
			}

			mLlGlobal.setVisibility(View.VISIBLE);
			mProgress.setVisibility(View.INVISIBLE);

			break;

		case CANCLE_TICKET_REQUEST:// 取消运单
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				showToast(message);

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
		super.onFailure(requestId, httpCode, error);
		showToast("网络超时，请检查网络");
		switch (requestId) {
		case CANCLE_TICKET_REQUEST:
			setEnabled(true);
			break;

		default:
			break;
		}
	}

	private void setEnabled(boolean enabled) {
		if (enabled) {
			btModifyStopTicket.setEnabled(enabled);
			btModifyStopTicket
					.setBackgroundResource(R.drawable.sendbao_btn_background);
		} else {
			btModifyStopTicket.setEnabled(enabled);
			btModifyStopTicket.setBackgroundResource(R.drawable.btn_zhihui);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_modifyDocument_address_employer:
			Log.d("TAG", "------------运单ID---------------" + ticketId);
//			EmployerMapActivity.invoke(getActivity(), ticketId);
			break;

		case R.id.bt_modify_stop_ticket:
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
				dialog.dismiss();
				setEnabled(true);
			}
		});
		btnSure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
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

}

package com.yunqi.clientandroid.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.adapter.CompanyImageAdapter;
import com.yunqi.clientandroid.entity.CompanyDetail;
import com.yunqi.clientandroid.entity.CompanyDetailImage;
import com.yunqi.clientandroid.http.request.CompanyDetailImageRequest;
import com.yunqi.clientandroid.http.request.CompanyDetailRequest;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.L;
import com.yunqi.clientandroid.utils.StringUtils;
import com.yunqi.clientandroid.view.MyCompanyImage;

/**
 * 
 * @Description:class 公司详情
 * @ClassName: CompanyDetailActivity
 * @author: zhm
 * @date: 2016-3-31 下午5:16:15
 * 
 */
public class CompanyDetailActivity extends BaseActivity implements
		OnItemClickListener {
	private TextView tv_company_name;// 公司简称
	private TextView tv_company_register_time;// 注册时间
	private TextView tv_company_register_money;// 注册资金
	private TextView tv_company_register_address;// 公司地址
	private TextView tv_company_explain;// 公司说明
	private TextView tv_company_contract_sum_input;// 发包总数
	private TextView tv_company_waybill_sum_input;// 运单总数
	private TextView tv_company_pay_sum_input;// 结款总金额
	private TextView tv_company_waybill_finish_sum_input;// 运单完成总数
	private TextView tv_company_waybill_running_sum_input;// 运单执行中总数
	private MyCompanyImage gv_company_list;
	private CompanyImageAdapter companyImageAdapter;
	private List<String> imageUrlMin; // 存放小图片的集合
	private List<String> imageUrlMax; // 存放大图片的集合

	private int packageId; // 包ID

	private CompanyDetailRequest companyDetailRequest;
	private CompanyDetailImageRequest companyDetailImageRequest;

	// 请求ID
	private final int GET_COMPANY_DETAIL = 1;
	private final int GET_COMPANY_IMAGEVIEW = 2;

	// 获取到的数据
	private int id; // 包ID
	private int tenantId;// 租户ID
	private String tenantName;// 公司全称
	private String tenantShortname;// 公司简称
	private String creatTime;// 注册时间
	private String tenantLegalMoney;// 注册资金
	private String tenantRegisterRegion;// 地址
	private String tenantMemo;// 公司说明
	private int packageCount;// 发包总数
	private int ticketCount;// 运单总数
	private int ticketAmount;// 结款总金额
	private int ticketOverCount;// 运单完成总数
	private int ticketDoingCount;// 运单执行总数

	// 获取到的图片数据
	private String url;// 原图路径
	private String urlImg800;// 大图路径
	private String urlImg256;// 小图路径

	@Override
	protected int getLayoutId() {
		return R.layout.activity_company_detail;
	}

	@Override
	protected void initView() {
		L.e("TAG", "进来了，开始请求数据++++++++++++++++++++++++++++++++");
		// 初始化titileBar的方法
		initActionBar();

		tv_company_name = (TextView) findViewById(R.id.tv_company_name);
		tv_company_register_time = (TextView) findViewById(R.id.tv_company_register_time);
		tv_company_register_money = (TextView) findViewById(R.id.tv_company_register_money);
		tv_company_register_address = (TextView) findViewById(R.id.tv_company_register_address);
		tv_company_explain = (TextView) findViewById(R.id.tv_company_explain);
		tv_company_contract_sum_input = (TextView) findViewById(R.id.tv_company_contract_sum_input);
		tv_company_waybill_sum_input = (TextView) findViewById(R.id.tv_company_waybill_sum_input);
		tv_company_pay_sum_input = (TextView) findViewById(R.id.tv_company_pay_sum_input);
		tv_company_waybill_finish_sum_input = (TextView) findViewById(R.id.tv_company_waybill_finish_sum_input);
		tv_company_waybill_running_sum_input = (TextView) findViewById(R.id.tv_company_waybill_running_sum_input);
		gv_company_list = (MyCompanyImage) findViewById(R.id.gv_company_list);

	}

	// 初始化titileBar的方法
	private void initActionBar() {
		// 设置titileBar的标题
		setActionBarTitle(this.getResources().getString(
				R.string.company_detail_title));
		// 设置左边的返回箭头
		setActionBarLeft(R.drawable.nav_back);
		// 给左边的返回箭头加监听
		setOnActionBarLeftClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 关闭当前的Activity页面
				CompanyDetailActivity.this.finish();
			}
		});

	}

	@Override
	protected void initData() {
		L.e("TAG", "进来了，开始请求数据---------------------------------");

		// 得到传过来的包的ID
		packageId = Integer.valueOf(getIntent().getStringExtra("packageId"));

		L.e("TAG", "传过来的包的ID-------------------" + packageId);

		imageUrlMin = new ArrayList<String>();
		imageUrlMax = new ArrayList<String>();

		// gv_company_list.setAdapter(companyImageAdapter);

		// 获取公司详情
		getCompanyDetail();
		// 获取图片的请求
		getComPanyImage();
	}

	// 获取图片的请求
	private void getComPanyImage() {
		companyDetailImageRequest = new CompanyDetailImageRequest(
				CompanyDetailActivity.this, packageId);
		companyDetailImageRequest.setRequestId(GET_COMPANY_IMAGEVIEW);
		httpPost(companyDetailImageRequest);
	}

	// 获取公司详情
	private void getCompanyDetail() {
		L.e("TAG", "开始请求公司详情数据----------------------------");

		companyDetailRequest = new CompanyDetailRequest(
				CompanyDetailActivity.this, packageId);
		companyDetailRequest.setRequestId(GET_COMPANY_DETAIL);
		httpPost(companyDetailRequest);
	}

	@Override
	protected void setListener() {
		gv_company_list.setOnItemClickListener(this);

	}

	@Override
	public void onStart(int requestId) {
		super.onStart(requestId);
		L.e("TAG", "开始请求公司详情数据-------++++++------------++++++---------");
	}

	@Override
	public void onSuccess(int requestId, Response response) {
		super.onSuccess(requestId, response);

		boolean isSuccess;
		String message;

		switch (requestId) {
		case GET_COMPANY_DETAIL:
			isSuccess = response.isSuccess;
			message = response.message;
			if (isSuccess) {
				// 获取公司详细数据成功
				CompanyDetail cDetail = (CompanyDetail) response.singleData;
				id = cDetail.id;// 包ID
				tenantId = cDetail.tenantId;// 租户ID
				tenantName = cDetail.tenantName;// 公司全称
				tenantShortname = cDetail.tenantShortname;// 公司简称
				creatTime = cDetail.creatTime;// 注册时间
				tenantLegalMoney = cDetail.tenantLegalMoney;// 注册资金
				tenantRegisterRegion = cDetail.tenantRegisterRegion;// 地址
				tenantMemo = cDetail.tenantMemo;// 公司说明
				packageCount = cDetail.packageCount;// 发包总数
				ticketCount = cDetail.ticketCount;// 运单总数
				ticketAmount = cDetail.ticketAmount;// 结款总金额
				ticketOverCount = cDetail.ticketOverCount;// 运单完成总数
				ticketDoingCount = cDetail.ticketDoingCount;// 运单执行总数

				L.e("TAG", "公司的说明--------------" + tenantMemo);
				// 公司的名称
				if (!TextUtils.isEmpty(tenantShortname)
						&& tenantShortname != null) {
					tv_company_name.setText(tenantShortname);
				}

				// 注册时间
				if (!TextUtils.isEmpty(creatTime) && creatTime != null) {
					tv_company_register_time.setText("注册时间:"
							+ StringUtils.formatCanReceive(creatTime));
				}

				// 注册资金
				if (!TextUtils.isEmpty(tenantLegalMoney)
						&& tenantLegalMoney != null) {
					tv_company_register_money.setText("资金：" + tenantLegalMoney);
				}

				// 地址
				if (!TextUtils.isEmpty(tenantRegisterRegion)
						&& tenantRegisterRegion != null) {
					tv_company_register_address.setText("地点："
							+ tenantRegisterRegion);
				}

				// 公司说明
				if (!TextUtils.isEmpty(tenantMemo) && tenantMemo != null) {
					tv_company_explain
							.setText(Html
									.fromHtml("<span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</sapn>"
											+ tenantMemo));
				}

				if (packageCount != 0) {
					// 发包总数
					tv_company_contract_sum_input.setText(packageCount + "包");
				}

				if (ticketCount != 0) {
					// 运单总数
					tv_company_waybill_sum_input.setText(ticketCount + "包");
				}

				if (ticketAmount != 0) {
					// 结款总金额
					tv_company_pay_sum_input.setText(ticketAmount + "元");
				}

				if (ticketOverCount != 0) {
					// 运单完成总数
					tv_company_waybill_finish_sum_input.setText(ticketOverCount
							+ "包");
				}

				if (ticketDoingCount != 0) {
					// 运单执行总数
					tv_company_waybill_running_sum_input
							.setText(ticketDoingCount + "包");
				}
			} else {
				// 获取数据失败
				if (!TextUtils.isEmpty(message) && message != null) {
					showToast(message);
				}
			}

			break;

		case GET_COMPANY_IMAGEVIEW: // 图片的请求
			// Toast.makeText(CompanyDetailActivity.this,response.data.toString(),
			// 1).show();
			isSuccess = response.isSuccess;
			message = response.message;

			L.e("TAG", "返回值:" + response.toString());

			// Log.e("TAG", "sing++++++++" + response.data.toString());
			// response.data

			if (isSuccess) {

				if (response.data == null) {
					return;
				}
				if (imageUrlMin != null || imageUrlMax != null) {
					imageUrlMax.clear();
					imageUrlMin.clear();
				}

				ArrayList<CompanyDetailImage> cDetailImage = response.data;

				if (cDetailImage != null) {
					// 获取集合中的图片路径
					for (int i = 0; i < cDetailImage.size(); i++) {
						url = cDetailImage.get(i).url;
						urlImg256 = cDetailImage.get(i).urlImg256;
						urlImg800 = cDetailImage.get(i).urlImg800;

						imageUrlMin.add(urlImg256);
						imageUrlMax.add(urlImg800);
					}

					// 图片的加载
					if (imageUrlMin != null) {
						companyImageAdapter = new CompanyImageAdapter(
								imageUrlMin, CompanyDetailActivity.this);
						gv_company_list.setAdapter(companyImageAdapter);
					}

				} else {
					showToast(message);
					return;
				}

			} else {
				// 获取数据失败
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
		super.onFailure(requestId, httpCode, error);
		showToast(this.getResources().getString(R.string.net_error_toast));
	}

	// // 本界面的跳转方法
	// public static void invoke(Context activity, String packageId) {
	// Intent intent = new Intent();
	//
	// intent.putExtra("packageId", packageId);// 包的id
	// activity.startActivity(intent);
	// }

	// 本界面的跳转方法
	public static void invoke_intent(Context activity, String packageId,
			Intent intent) {
		// Intent intent = new Intent();
		intent.putExtra("packageId", packageId);// 包的id
		activity.startActivity(intent);
	}

	// GridView的监听
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// 获取当前点击的图片
		String maxUrlImg800 = imageUrlMax.get(position);
		// 把大图片的路径传过去进行放大处理
		ImageScaleActivity.invoke(CompanyDetailActivity.this, maxUrlImg800);
	}

}

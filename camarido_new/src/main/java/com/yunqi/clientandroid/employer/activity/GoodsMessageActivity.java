package com.yunqi.clientandroid.employer.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.activity.BaseActivity;
import com.yunqi.clientandroid.employer.adapter.GoodsMessageAdapter;
import com.yunqi.clientandroid.employer.entity.PackagePickersInfo;
import com.yunqi.clientandroid.employer.request.GetPakagePickersRequest;
import com.yunqi.clientandroid.http.response.Response;

/**
 * @Description:货物信息界面
 * @ClassName: GoodsMessageActivity
 * @author: mengwei
 * @date: 2016-6-22 上午10:27:48
 * 
 */
public class GoodsMessageActivity extends BaseActivity implements
		AdapterView.OnItemClickListener, View.OnClickListener {
	// 界面控件对象
	private TextView tvEmployerText1, tvEmployerText2, tvEmployerText3;
	private EditText etGoodsMessage;
	private ListView lvGoodsMessage;
	private Button btGoodsMessage;

	// 请求类
	private GetPakagePickersRequest getPakagePickersRequest;

	// 请求ID
	private final int GET_CATEGORYNAME = 1;

	// 保存货品种类的集合
	private ArrayList<PackagePickersInfo> cateGoryList = new ArrayList<PackagePickersInfo>();
	private GoodsMessageAdapter goodsMessageAdapter;

	public static int requestCode;
	private int priceCate;
	private int selectPosition = -1;// 用于记录用户选择的变量
	private int checkId;
	private String name;

	@Override
	protected int getLayoutId() {
		return R.layout.employer_activity_car_type;
	}

	@Override
	protected void initView() {
		priceCate = getIntent().getIntExtra("price", 0);

		tvEmployerText1 = obtainView(R.id.tv_employer_text1);
		tvEmployerText2 = obtainView(R.id.tv_employer_text2);
		tvEmployerText3 = obtainView(R.id.tv_employer_text3);
		etGoodsMessage = obtainView(R.id.et_cartype_number);
		lvGoodsMessage = obtainView(R.id.lv_cartype_listview);
		btGoodsMessage = obtainView(R.id.bt_cartype_finish);

		tvEmployerText1.setText("货物单价");
		tvEmployerText2.setText("元/吨");
		tvEmployerText3.setText("货物种类");

		if (priceCate != 0) {
			etGoodsMessage.setText(priceCate);
		}

		// 适配器
		goodsMessageAdapter = new GoodsMessageAdapter(mContext, cateGoryList);
		lvGoodsMessage.setAdapter(goodsMessageAdapter);
	}

	@Override
	protected void initData() {
		//
		getPakagePickersRequest = new GetPakagePickersRequest(mContext);
		getPakagePickersRequest.setRequestId(GET_CATEGORYNAME);
		httpPost(getPakagePickersRequest);
	}

	@Override
	public void onStart(int requestId) {
		super.onStart(requestId);
	}

	@Override
	public void onSuccess(int requestId, Response response) {
		super.onSuccess(requestId, response);
		boolean isSuccess;
		String message;

		switch (requestId) {
		case GET_CATEGORYNAME:
			isSuccess = response.isSuccess;
			message = response.message;

			if (isSuccess) {
				ArrayList<PackagePickersInfo> pList = response.data;

				if (pList != null) {
					cateGoryList.addAll(pList);
				}

				goodsMessageAdapter.notifyDataSetChanged();
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
		showToast(getString(R.string.net_error_toast));
	}

	@Override
	protected void setListener() {
		btGoodsMessage.setOnClickListener(this);
		lvGoodsMessage.setOnItemClickListener(this);
	}

	/**
	 * 
	 * @Description:ChooseAddressActivity页面跳转
	 * @Title:invoke
	 * @param context
	 * @param requestCode
	 * @return:void
	 * @throws
	 * @Create: 2016年5月17日 下午8:40:22
	 * @Author : chengtao
	 */
	public static void invoke(Activity activity, int mRequestCode, int price,
			String cateNameId) {
		Intent intent = new Intent(activity, GoodsMessageActivity.class);
		requestCode = mRequestCode;
		intent.putExtra("price", price);
		intent.putExtra("cateNameId", cateNameId);
		activity.startActivityForResult(intent, mRequestCode);
	}

	@Override
	public void onClick(View v) {
		// 获取输入框的值
		String catePrice = etGoodsMessage.getText().toString().trim();

		Intent mIntent = new Intent();

		mIntent.putExtra("catePrice", catePrice);
		mIntent.putExtra("checkId", checkId);
		mIntent.putExtra("categoryName", name);
		setResult(requestCode, mIntent);
		GoodsMessageActivity.this.finish();

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		selectPosition = position;

		goodsMessageAdapter.notifyDataSetChanged();

		PackagePickersInfo goods = cateGoryList.get(position);

		checkId = goods.Id;
		name = goods.CategoryName;

	}

}

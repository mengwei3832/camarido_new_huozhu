package com.yunqi.clientandroid.employer.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

//import com.baidu.a.a.a.b;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.activity.BaseActivity;
import com.yunqi.clientandroid.employer.adapter.CarTypeAdapter;
import com.yunqi.clientandroid.employer.adapter.CarTypeAdapter.ViewHolder;
import com.yunqi.clientandroid.employer.entity.CarType;
import com.yunqi.clientandroid.employer.request.CarTypeRequest;
import com.yunqi.clientandroid.http.response.Response;

/**
 * @Description:用车信息界面
 * @ClassName: CarTypeActivity
 * @author: mengwei
 * @date: 2016-6-21 下午6:01:56
 * 
 */
public class CarTypeActivity extends BaseActivity implements
		AdapterView.OnItemClickListener, View.OnClickListener {
	// 界面控件对象
	private EditText etCarTypeNumber;
	private ListView lvCarTypeListView;
	private Button btCarTypeFinish;

	// 保存车型数据的集合
	private ArrayList<CarType> carTypeList = new ArrayList<CarType>();
	private CarTypeAdapter carTypeAdapter;

	// 保存选中车型的id,name
	private ArrayList<String> VehicleId = new ArrayList<String>();
	private ArrayList<String> vehicleName = new ArrayList<String>();

	// 车型请求类
	private CarTypeRequest carTypeRequest;

	// 请求ID
	private final int GET_CAR_TYPE = 1;

	// 返回的请求码Code
	public static int requestCode;
	private int carNumber;
	Bundle b = new Bundle();

	@Override
	protected int getLayoutId() {
		return R.layout.employer_activity_car_type;
	}

	@Override
	protected void initView() {
		// 获取传递过来的数据
		carNumber = getIntent().getIntExtra("carNumber", 0);

		etCarTypeNumber = obtainView(R.id.et_cartype_number);
		lvCarTypeListView = obtainView(R.id.lv_cartype_listview);
		btCarTypeFinish = obtainView(R.id.bt_cartype_finish);

		if (carNumber != 0) {
			etCarTypeNumber.setText(carNumber);
		}

		// carTypeAdapter = new CarTypeAdapter(carTypeList,
		// CarTypeActivity.this);
		// lvCarTypeListView.setAdapter(carTypeAdapter);
	}

	@Override
	protected void initData() {
		// 请求车型的数据
		carTypeRequest = new CarTypeRequest(CarTypeActivity.this);
		carTypeRequest.setRequestId(GET_CAR_TYPE);
		httpPost(carTypeRequest);
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
		case GET_CAR_TYPE:
			isSuccess = response.isSuccess;
			message = response.message;

			if (isSuccess) {
				ArrayList<CarType> carlis = response.data;

				if (carlis != null) {
					carTypeList.addAll(carlis);
				}

				carTypeAdapter.notifyDataSetChanged();
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
		lvCarTypeListView.setOnItemClickListener(this);
		btCarTypeFinish.setOnClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		ViewHolder vh = (ViewHolder) view.getTag();

		CarType carType = carTypeList.get(position);

		// int cid = carType.id;
		// String cname = carType.carTypeName;

		vh.cbCarCheck.toggle();

		// carTypeAdapter.getIsSelected().put(position,
		// vh.cbCarCheck.isChecked());

		// if (vh.cbCarCheck.isChecked() == true) {
		// VehicleId.add(position, cid+"");
		// vehicleName.add(position, cname);
		// } else {
		// VehicleId.remove(position);
		// vehicleName.remove(position);
		// }
	}

	@Override
	public void onClick(View v) {
		// 获取输入框的值
		String carNumber = etCarTypeNumber.getText().toString().trim();
		// 将车型ID集合转化为数组

		if (carNumber == null && TextUtils.isEmpty(carNumber)) {
			showToast("需求车数不可为空");
			return;
		}

		Intent mIntent = new Intent();

		mIntent.putExtra("carNumber", carNumber);

		// 传递数组
		b.putStringArrayList("VehicleId", VehicleId);
		b.putStringArrayList("vehicleName", vehicleName);
		mIntent.putExtras(b);

		setResult(requestCode, mIntent);
		CarTypeActivity.this.finish();

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
	public static void invoke(Activity activity, int mRequestCode,
			int carNumber, ArrayList<String> vehicleId) {
		Intent intent = new Intent(activity, CarTypeActivity.class);
		requestCode = mRequestCode;
		intent.putExtra("carNumber", carNumber);
		intent.putStringArrayListExtra("vehicleId", vehicleId);
		activity.startActivityForResult(intent, mRequestCode);
	}

}

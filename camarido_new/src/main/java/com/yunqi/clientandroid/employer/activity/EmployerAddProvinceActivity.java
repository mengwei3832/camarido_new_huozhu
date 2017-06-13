package com.yunqi.clientandroid.employer.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.activity.BaseActivity;
import com.yunqi.clientandroid.employer.adapter.EmployerAddressAdapter;
import com.yunqi.clientandroid.employer.entity.GetProvince;
import com.yunqi.clientandroid.employer.request.GetProvinceRequest;
import com.yunqi.clientandroid.employer.util.AddressUtils;
import com.yunqi.clientandroid.employer.util.SaveProvinceUtils;
import com.yunqi.clientandroid.employer.util.SaveSetUtils;
import com.yunqi.clientandroid.employer.util.SpManager;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.FilterManager;
import com.yunqi.clientandroid.utils.L;
import com.yunqi.clientandroid.utils.StringUtils;

/**
 * @Description:class 添加省的界面
 * @ClassName: EmployerAddProvinceActivity
 * @author: chengtao
 * @date: 2016-6-7 下午7:38:02
 * 
 */
public class EmployerAddProvinceActivity extends BaseActivity implements
		OnItemClickListener {
	private ListView lvAddProvince;
	private View pbAddProgress;
	private List<GetProvince> mProvinceList = new ArrayList<>();
	private EmployerAddressAdapter mEmployerAddressAdapter;
	private SpManager mSpManager;
	String provinceStr;

	@Override
	protected int getLayoutId() {
		return R.layout.employer_activity_add_province;
	}

	@Override
	protected void initView() {
		initActionBar();

		mSpManager = SpManager.instance(mContext);

//		provinceStr = mSpManager.getProvince();

		mProvinceList = SaveProvinceUtils.readObjectList("Provinces");

		lvAddProvince = obtainView(R.id.lv_add_province);
		pbAddProgress = obtainView(R.id.pb_add_progress);

		L.e("--------mProvinceList---------"+mProvinceList.toString());

		if (mProvinceList.size() != 0){
//			try {
//				mProvinceList = SaveSetUtils.String2SceneList(provinceStr);
				mEmployerAddressAdapter = new EmployerAddressAdapter(
						EmployerAddProvinceActivity.this, mProvinceList);
				lvAddProvince.setAdapter(mEmployerAddressAdapter);
//			} catch (IOException e) {
//				e.printStackTrace();
//			} catch (ClassNotFoundException e) {
//				e.printStackTrace();
//			}
		} else {
			getPrvinceList();
			mEmployerAddressAdapter = new EmployerAddressAdapter(
					EmployerAddProvinceActivity.this, mProvinceList);
			lvAddProvince.setAdapter(mEmployerAddressAdapter);

//			pbAddProgress.setVisibility(View.VISIBLE);

//			mProvinceList.clear();
//
//			mEmployerAddressAdapter.notifyDataSetChanged();
//			// 获得省的数据
//			getProvinceText();
		}

	}

	// 初始化titileBar的方法
	private void initActionBar() {
		setActionBarTitle(this.getResources().getString(
				R.string.employer_address_tianjiadizhi));
		setActionBarLeft(R.drawable.nav_back);
		setOnActionBarLeftClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				EmployerAddProvinceActivity.this.finish();
			}
		});
	}

	@Override
	protected void initData() {


	}

	private void getPrvinceList(){
		for (int i = 0; i < AddressUtils.provinceName.length; i++){
			GetProvince mGetProvince = new GetProvince();
			mGetProvince.setName(AddressUtils.provinceName[i]);
			mGetProvince.setId(AddressUtils.provinceId[i]);
			mProvinceList.add(mGetProvince);
		}
	}

	/**
	 * 获得省的数据
	 */
	private void getProvinceText() {
		httpPost(new GetProvinceRequest(EmployerAddProvinceActivity.this));
	}

	@Override
	public void onStart(int requestId) {
		super.onStart(requestId);
	}

	@Override
	public void onSuccess(int requestId, Response response) {
		super.onSuccess(requestId, response);
		boolean isSuccess = response.isSuccess;
		String message = response.message;
		if (isSuccess) {
			ArrayList<GetProvince> provinceList = response.data;

			if (provinceList != null) {
				mProvinceList.addAll(provinceList);
				try {
					String str = SaveSetUtils.SceneList2String(mProvinceList);
					mSpManager.setProvince(str);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			showToast(message);

			pbAddProgress.setVisibility(View.INVISIBLE);

			mEmployerAddressAdapter.notifyDataSetChanged();
		} else {
			showToast(message);
		}
	}

	@Override
	public void onFailure(int requestId, int httpCode, Throwable error) {
		super.onFailure(requestId, httpCode, error);
		showToast(getString(R.string.net_error_toast));
	}

	@Override
	protected void setListener() {
		lvAddProvince.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		GetProvince mGetProvince = mProvinceList.get(position);

		int provinceId = mGetProvince.id;

		String provinceName = mGetProvince.name;

		Log.e("TAG", "----------选中的省份------------" + provinceName);
		Log.e("TAG", "----------选中的省份ID------------" + provinceId);

		// TODO 跳转到市的页面
		EmployerAddCityActivity.invoke(EmployerAddProvinceActivity.this,
				provinceId, provinceName);
	}

	/**
	 * 本界面invoke跳转方法
	 */
	public static void invoke(Context context) {
		Intent intent = new Intent(context, EmployerAddProvinceActivity.class);
		context.startActivity(intent);
	}

}

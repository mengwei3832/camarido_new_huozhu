package com.yunqi.clientandroid.employer.adapter;

import java.util.List;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.employer.adapter.ChengYunAdapter.ViewHolder;
import com.yunqi.clientandroid.employer.entity.AccountEntity;
import com.yunqi.clientandroid.employer.entity.ChengYunTongJi;
import com.yunqi.clientandroid.utils.StringUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AccountAdapter extends BaseAdapter {

	private List<AccountEntity> chengList;
	private Context context;

	public AccountAdapter(List<AccountEntity> chengList, Context context) {
		super();
		this.chengList = chengList;
		this.context = context;
	}

	@Override
	public int getCount() {
		return chengList != null ? chengList.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return chengList != null ? chengList.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return chengList != null ? position : 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;
		if (convertView == null) {
			vh = new ViewHolder();

			convertView = LayoutInflater.from(context).inflate(
					R.layout.employer_dialog_item_account, null);
			
			vh.tvUserName = (TextView) convertView.findViewById(R.id.tv_item_account_user);
			vh.tvTenantName = (TextView) convertView.findViewById(R.id.tv_item_account_tenant);
			
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		
		AccountEntity mAccountEntity = chengList.get(position);
		
		int mViceUserId = mAccountEntity.ViceUserId;
		String mUserName = mAccountEntity.UserName;
		String mTenantName = mAccountEntity.TenantName;
		
		if (StringUtils.isStrNotNull(mUserName)) {
			vh.tvUserName.setText(mUserName);
		} else {
			vh.tvUserName.setText("");
		}
		
		if (StringUtils.isStrNotNull(mTenantName)) {
			vh.tvTenantName.setText(mTenantName);
		} else {
			vh.tvTenantName.setText("");
		}
		
		return convertView;
	}
	
	class ViewHolder {
		TextView tvUserName;
		TextView tvTenantName;
	}

}

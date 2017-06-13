package com.yunqi.clientandroid.employer.adapter;

import java.util.List;

//import com.baidu.a.a.a.c;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.employer.adapter.ChengYunAdapter.ViewHolder;
import com.yunqi.clientandroid.employer.entity.YiJiaHistory;
import com.yunqi.clientandroid.utils.StringUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class QuoteYijiaAdapter extends BaseAdapter {
	private Context context;
	private List<YiJiaHistory> chengList;

	public QuoteYijiaAdapter(Context context, List<YiJiaHistory> chengList) {
		super();
		this.context = context;
		this.chengList = chengList;
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
					R.layout.employer_item_yijia, null);

			vh.tvDate = (TextView) convertView
					.findViewById(R.id.tv_item_yijia_date);
			vh.tvPerson = (TextView) convertView
					.findViewById(R.id.tv_item_yijia_person);
			vh.tvPrice = (TextView) convertView
					.findViewById(R.id.tv_item_yijia_price);
			vh.tvSuccess = (TextView) convertView
					.findViewById(R.id.tv_item_yijia_success);

			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		YiJiaHistory yiJiaHistory = chengList.get(position);

		String mCreateTime = yiJiaHistory.createTime;
		String mPrice = yiJiaHistory.price;
		int mType = yiJiaHistory.type;

		// 议价时间
		if (StringUtils.isStrNotNull(mCreateTime)) {
			String mTime = StringUtils.formatDianType(mCreateTime);
			vh.tvDate.setText(mTime);
		} else {
			vh.tvDate.setText("");
		}

		// 议价身份
		if (mType == 0) {
			vh.tvPerson.setText("经纪人");
			vh.tvSuccess.setText("");
		} else if (mType == 1) {
			vh.tvPerson.setText("货主");
			vh.tvSuccess.setText("");
		} else if (mType == 2) {
			vh.tvPerson.setText("货主");
			vh.tvSuccess.setText("成功协商价格");
		} else {
			vh.tvPerson.setText("");
			vh.tvSuccess.setText("");
		}

		// 议价价格
		if (StringUtils.isStrNotNull(mPrice)) {
			vh.tvPrice.setText(mPrice + "元/吨");
		} else {
			vh.tvPrice.setText("0.00元/吨");
		}

		return convertView;
	}

	class ViewHolder {
		TextView tvDate;
		TextView tvPerson;
		TextView tvPrice;
		TextView tvSuccess;
	}
}

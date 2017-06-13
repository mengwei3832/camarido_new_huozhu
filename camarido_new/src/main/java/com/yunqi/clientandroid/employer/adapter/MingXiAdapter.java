package com.yunqi.clientandroid.employer.adapter;

import java.util.List;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.employer.entity.MingXiList;
import com.yunqi.clientandroid.utils.StringUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

@SuppressLint({ "SimpleDateFormat", "InflateParams" })
public class MingXiAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<MingXiList> mList;
	private Context context;

	public MingXiAdapter(Context context, List<MingXiList> list) {
		super();
		this.mList = list;
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		MingXiList list = mList.get(position);
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.employer_ming_xi_adapter,
					null);
			viewHolder.tvDanhao = (TextView) convertView
					.findViewById(R.id.tv_operate_danhao);
			viewHolder.tvType = (TextView) convertView
					.findViewById(R.id.tv_danhao_style);
			viewHolder.tvID = (TextView) convertView
					.findViewById(R.id.tv_operate_id);
			viewHolder.tvMOney = (TextView) convertView
					.findViewById(R.id.tv_danhao_money);
			viewHolder.tvTime = (TextView) convertView
					.findViewById(R.id.tv_operate_time);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		String Amount = list.Amount;// 操作金额
		float mAmount = Float.valueOf(Amount);
		String TypeDescr = list.TypeDescr;// 类型描述
		String CodeDescr = list.CodeDescr;// 单号描述
		String CreateTime = list.CreateTime;// 创建时间
		String AccountBizId = list.AccountBizId;// 操作ID
		// int AccountRecordType = list.AccountRecordType;// 0+钱；1-钱;

		// 操作单号
		if (StringUtils.isStrNotNull(CodeDescr)) {
			// String codeDescr[] = CodeDescr.split("\\：");
			// int startPos = CodeDescr.indexOf("(");
			// int endPos = CodeDescr.indexOf(")");
			viewHolder.tvDanhao.setText(context
					.getString(R.string.tv_operate_danhao) + CodeDescr);
			// viewHolder.tvDanhao.setText(CodeDescr);
		}
		// 操作ID
		if (StringUtils.isStrNotNull(AccountBizId)) {
			viewHolder.tvID.setText(context.getString(R.string.tv_operate_id)
					+ AccountBizId);
		}
		// 类型
		if (StringUtils.isStrNotNull(TypeDescr)) {
			viewHolder.tvType.setText(TypeDescr);
		}
		// 时间
		if (StringUtils.isStrNotNull(CreateTime)) {
			viewHolder.tvTime.setText(StringUtils.formatMingXi(CreateTime));
		}
		// 金额
		if (mAmount > 0) {
			viewHolder.tvMOney.setText(Html.fromHtml("<font color='#16B630'>+"
					+ Amount + "</font>"));
		} else {
			viewHolder.tvMOney.setText(Html.fromHtml("<font color='#FF0000'>"
					+ Amount + "</font>"));
		}

		return convertView;
	}

	class ViewHolder {
		TextView tvDanhao;// 操作单号
		TextView tvID;// 操作ID
		TextView tvType;// 类型
		TextView tvTime;// 时间
		TextView tvMOney;// 金额
	}
}

package com.yunqi.clientandroid.employer.adapter;

import java.util.List;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.employer.entity.HangYeZiXun;
import com.yunqi.clientandroid.utils.StringUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @Description:行业资讯列表适配器
 * @ClassName: HangYeAdapter
 * @author: chengtao
 * @date: 2016-7-7 下午3:39:38
 * 
 */
public class HangYeAdapter extends BaseAdapter {
	private List<HangYeZiXun> ticketList;
	private Context context;
	private LayoutInflater layoutInflater;

	public HangYeAdapter(List<HangYeZiXun> ticketList, Context context) {
		super();
		this.ticketList = ticketList;
		this.context = context;
		layoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return ticketList != null ? ticketList.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return ticketList != null ? ticketList.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return ticketList != null ? position : 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();

			convertView = layoutInflater.inflate(R.layout.employer_item_hangye,
					null);

			viewHolder.tvDate = (TextView) convertView
					.findViewById(R.id.tv_hangye_lie_date);
			viewHolder.tvYue = (TextView) convertView
					.findViewById(R.id.tv_hangye_lie_yue);
			viewHolder.tvContent = (TextView) convertView
					.findViewById(R.id.tv_hangye_lie_content);
			viewHolder.tvAuthor = (TextView) convertView
					.findViewById(R.id.tv_hangye_lie_autor);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		HangYeZiXun hangYeZiXun = ticketList.get(position);

		String dateTime = StringUtils
				.formatDate(hangYeZiXun.messageShowTimeBegin);
		int yue = hangYeZiXun.MessageReadCount;
		String content = hangYeZiXun.MessageAbstract;
		String author = hangYeZiXun.MessageAuthor;

		viewHolder.tvYue.setText(yue + "阅");

		if (StringUtils.isStrNotNull(dateTime)) {
			viewHolder.tvDate.setText(dateTime);
		}

		if (StringUtils.isStrNotNull(content)) {
			viewHolder.tvContent.setText(content);
		}

		if (StringUtils.isStrNotNull(author)) {
			viewHolder.tvAuthor.setText(author);
		}

		return convertView;
	}

	class ViewHolder {
		TextView tvDate;
		TextView tvYue;
		TextView tvContent;
		TextView tvAuthor;
	}

}

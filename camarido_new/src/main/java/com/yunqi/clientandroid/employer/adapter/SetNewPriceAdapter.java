package com.yunqi.clientandroid.employer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.employer.entity.SetNewPrice;
import com.yunqi.clientandroid.utils.StringUtils;

import java.util.List;

/**
 * 设定最新价格历史适配器
 * Created by mengwei on 2017/1/17.
 */

public class SetNewPriceAdapter extends BaseAdapter {
    private Context context;
    private List<SetNewPrice> chengList;

    public SetNewPriceAdapter(Context context, List<SetNewPrice> chengList) {
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
        ViewHolder viewHolder = null;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.employer_dialog_item_set_newprice,null);
            viewHolder.tvPrice = (TextView) convertView.findViewById(R.id.tv_item_newprice_price);
            viewHolder.tvDate = (TextView) convertView.findViewById(R.id.tv_item_newprice_date);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        SetNewPrice mSetNewPrice = chengList.get(position);

        String mPrice = mSetNewPrice.Price;
        String mDate = mSetNewPrice.CreateTime;

        if (StringUtils.isStrNotNull(mPrice)){
            viewHolder.tvPrice.setText("历史运费："+mPrice+"元");
        } else {
            viewHolder.tvPrice.setText("历史运费：0元");
        }

        if (StringUtils.isStrNotNull(mDate)){
            String mTime = StringUtils.formatModify(mDate);
            viewHolder.tvDate.setText("开始时间："+mTime);
        } else {
            viewHolder.tvDate.setText("开始时间：");
        }

        return convertView;
    }

    class ViewHolder{
        TextView tvPrice;
        TextView tvDate;
    }
}

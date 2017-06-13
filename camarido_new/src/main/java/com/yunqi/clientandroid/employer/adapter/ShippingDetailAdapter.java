package com.yunqi.clientandroid.employer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.employer.entity.TicketCurrentBean;

import java.util.List;

/**
 * 运费明细适配器
 * Created by mengwei on 2017/4/7.
 */

public class ShippingDetailAdapter extends BaseAdapter {
    private List<String> ticketList;
    private Context context;

    public ShippingDetailAdapter(List<String> ticketList,
                                Context context) {
        super();
        this.ticketList = ticketList;
        this.context = context;
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
        ViewHoler viewHoler = null;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_ship_detaila,null);
            viewHoler.tvBegin = (TextView) convertView.findViewById(R.id.tv_item_ship_begin);
            viewHoler.tvEnd = (TextView) convertView.findViewById(R.id.tv_item_ship_end);
            viewHoler.tvSumTon = (TextView) convertView.findViewById(R.id.tv_item_ship_sum_ton);
            viewHoler.tvTon = (TextView) convertView.findViewById(R.id.tv_item_ship_ton);
            viewHoler.tvVehicle = (TextView) convertView.findViewById(R.id.tv_item_ship_vehicle);
            viewHoler.tvPrice = (TextView) convertView.findViewById(R.id.tv_item_ship_price);
            viewHoler.tvDate = (TextView) convertView.findViewById(R.id.tv_item_ship_new_date);
            viewHoler.tvCoal = (TextView) convertView.findViewById(R.id.tv_item_ship_coal);
            viewHoler.tvFeright = (TextView) convertView.findViewById(R.id.tv_item_ship_feright);
            viewHoler.ivInsurance = (ImageView) convertView.findViewById(R.id.iv_item_ship_insurance);

            convertView.setTag(viewHoler);
        } else {
            viewHoler = (ViewHoler) convertView.getTag();
        }



        return convertView;
    }

    class ViewHoler {
        TextView tvBegin,tvEnd,tvSumTon,tvTon,tvVehicle,tvPrice,tvDate,tvCoal,tvFeright;
        ImageView ivInsurance;
    }
}

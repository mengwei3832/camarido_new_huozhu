package com.yunqi.clientandroid.employer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.employer.entity.ExternalEntity;
import com.yunqi.clientandroid.employer.util.ExternalDeletePhone;
import com.yunqi.clientandroid.employer.util.ExternalSendDuanxin;
import com.yunqi.clientandroid.utils.L;
import com.yunqi.clientandroid.utils.StringUtils;
import com.yunqi.clientandroid.utils.T;

import java.util.List;

/**
 * 外部监控人员适配器
 * Created by mengwei on 2016/12/27.
 */

public class ExternalAdapter extends BaseAdapter {
    private Context context;
    private List<ExternalEntity> externalList;
    private ExternalSendDuanxin externalSendDuanxin;
    private ExternalDeletePhone externalDeletePhone;
    private boolean isCheck = false;

    public ExternalAdapter(Context context, List<ExternalEntity> externalList, ExternalSendDuanxin externalSendDuanxin, ExternalDeletePhone externalDeletePhone) {
        this.context = context;
        this.externalList = externalList;
        this.externalSendDuanxin = externalSendDuanxin;
        this.externalDeletePhone = externalDeletePhone;
    }

    @Override
    public int getCount() {
        return externalList != null ? externalList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return externalList != null ? externalList.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return externalList != null ? position : 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null){
           viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.employer_item_external,null);
            viewHolder.tvPhone = (TextView) convertView.findViewById(R.id.tv_item_external_phone);
            viewHolder.btSend = (Button) convertView.findViewById(R.id.bt_item_external_duanxin);
            viewHolder.rlDelete = (RelativeLayout) convertView.findViewById(R.id.rl_item_external_delete);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ExternalEntity externalEntity = externalList.get(position);

        final String phone = externalEntity.userPhone;
        final String phId = externalEntity.id;

        if (StringUtils.isStrNotNull(phone)){
            viewHolder.tvPhone.setText(phone);
        } else {
            viewHolder.tvPhone.setText("");
        }

        final int posi = position;
        final View view = convertView;

        viewHolder.btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    externalSendDuanxin.onClick(view, position, phone);
            }
        });

        viewHolder.rlDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                L.e("------phId------"+phId);
                    externalDeletePhone.onClick(view,position,phId);
            }
        });

        return convertView;
    }

    class ViewHolder {
        TextView tvPhone;
        Button btSend;
        RelativeLayout rlDelete;
    }
}

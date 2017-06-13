package com.yunqi.clientandroid.employer.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.employer.util.interfaces.DefaultPriceSure;

/**
 * 对话框封装类
 * Created by mengwei on 2016/12/27.
 */

public class DialogTool {
    public static final int NO_ICON = -1;  //无图标



    /**
     * 创建消息对话框
     *
     * @param context 上下文 必填
     * @param message 显示内容 必填
     * @return
     */
    public static Dialog createMessageDialog(Context context, String message){
        Dialog dialog = null;
        View view_messgae = LayoutInflater.from(context).inflate(R.layout.dialog_tool_utils,null);
        TextView tvMessgae = (TextView) view_messgae.findViewById(R.id.tv_dialog_message);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view_messgae);
        tvMessgae.setText(message);
        //创建一个消息对话框
        dialog = builder.create();
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }

    /**
     * 显示预设价格
     * @param context
     * @param str1 价格
     * @param str2 生效时间
     * @param defaultPriceSure 是否修改预设价格
     */
    public static void showDefaultPriceDialog(final Activity context, String str1, String str2, final DefaultPriceSure defaultPriceSure){
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View net_view = layoutInflater.inflate(R.layout.dialog_default_price, null);

        Button btCancel = (Button) net_view.findViewById(R.id.bt_dialog_default_cancel);
        Button btSure = (Button) net_view.findViewById(R.id.bt_dialog_default_sure);
        TextView tvPrice = (TextView) net_view.findViewById(R.id.tv_dialog_default_price);
        TextView tvDate = (TextView) net_view.findViewById(R.id.tv_dialog_default_date);
        tvPrice.setText("价格："+str1+"元");
        tvDate.setText("生效时间："+str2);

        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        final AlertDialog builder = dialog.create();
        builder.setView(net_view);
        builder.setCancelable(true);
        builder.setCanceledOnTouchOutside(false);
        builder.show();

        btSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
                defaultPriceSure.onNextRequest(1);
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
                defaultPriceSure.onNextRequest(0);
            }
        });

    }

    /**
     * 显示异常退出弹窗
     * @param context
     */
    public static void showExceptionDialog(final Context context){
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View net_view = layoutInflater.inflate(R.layout.dialog_default_price, null);

        Button btsure = (Button) net_view.findViewById(R.id.bt_dialog_exception_sure);

        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        final AlertDialog builder = dialog.create();
        builder.setView(net_view);
        builder.setCancelable(true);
        builder.setCanceledOnTouchOutside(false);
        builder.show();

        btsure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });

    }

    /**
     * 显示设定最新价格
     * @param context
     * @param str1 价格
     * @param str2 生效时间
     * @param defaultPriceSure 是否修改预设价格
     */
    public static void showSetNewPriceDialog(final Activity context, String str1, String str2, final DefaultPriceSure defaultPriceSure){
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View net_view = layoutInflater.inflate(R.layout.dialog_default_price, null);

        Button btCancel = (Button) net_view.findViewById(R.id.bt_dialog_default_cancel);
        Button btSure = (Button) net_view.findViewById(R.id.bt_dialog_default_sure);
        TextView tvPrice = (TextView) net_view.findViewById(R.id.tv_dialog_default_price);
        TextView tvDate = (TextView) net_view.findViewById(R.id.tv_dialog_default_date);
        tvPrice.setText("价格："+str1+"元");
        tvDate.setText("生效时间："+str2);

        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        final AlertDialog builder = dialog.create();
        builder.setView(net_view);
        builder.setCancelable(true);
        builder.setCanceledOnTouchOutside(false);
        builder.show();

        btSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
                defaultPriceSure.onNextRequest(1);
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
                defaultPriceSure.onNextRequest(0);
            }
        });

    }

}

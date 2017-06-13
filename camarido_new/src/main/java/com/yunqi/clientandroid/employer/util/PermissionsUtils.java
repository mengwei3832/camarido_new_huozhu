package com.yunqi.clientandroid.employer.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.activity.SplashActivity;

/**
 * Created by mengwei on 2017/1/23.
 */

public class PermissionsUtils {
    private PermissionsChecker mChecker;
    private Context mContext;
    private static final String PACKAGE_URL_SCHEME = "package:";

    public PermissionsUtils(Context context) {
        mContext = context;
        mChecker = new PermissionsChecker(mContext);
    }

    public boolean isPermissionsChecker(String[] permissions){
        return mChecker.lacksPermissions(permissions);
    }

    // 显示缺失权限提示
    public void showMissingPermissionDialog(String text) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.dialog_permission_tishi);
        builder.setMessage(mContext.getString(R.string.dialog_permission_help_txt1)+text
                +mContext.getString(R.string.string_help_text));

        // 拒绝, 退出应用
        builder.setNegativeButton(R.string.btn_cancle, new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setPositiveButton(R.string.settings, new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                startAppSettings();
                dialog.dismiss();
            }
        });

        builder.setCancelable(false);

        builder.show();
    }

    // 启动应用的设置
    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse(PACKAGE_URL_SCHEME + mContext.getPackageName()));
        mContext.startActivity(intent);
    }
}

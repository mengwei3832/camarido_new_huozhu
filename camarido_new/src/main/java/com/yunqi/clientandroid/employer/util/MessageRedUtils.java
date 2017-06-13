package com.yunqi.clientandroid.employer.util;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.yunqi.clientandroid.utils.L;
import com.yunqi.clientandroid.utils.PreManager;
import com.yunqi.clientandroid.utils.StringUtils;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by mengwei on 2017/3/30.
 */

public class MessageRedUtils {

    private Context mContext;
    private PreManager mPreManager;
    private ImageView mIvRed;

    public MessageRedUtils(Context context, ImageView ivRed) {
        mContext = context;
        mIvRed = ivRed;
        mPreManager = PreManager.instance(mContext);
    }

    public void get() {
        HttpURLConnection conn = null;
        try {
            URL url = new URL("http://mp.weixin.qq.com/mp/homepage?__biz=MjM5MDIyMTk2NA==&hid=1&sn=6b3aad87acb9514103e59b5e8f9e3fa2&uin=MTY0MzM5OTU1&key=133c70a3e904573d3cb935f96c4e65449f83943f34a0be1d3dcbed1e0bf50fb20b46e1472fb0d9fe71e4b531d43db54f38c448fda851d8d5a2eadcf099dff2dd63da840dfbc2271628365d40b756c734&devicetype=android-23&version=26050630&lang=zh_CN&nettype=cmnet&pass_ticket=9n4dpZzHCYD6JlrGH0hAEJy0cpQVknrzGf7RWT1HOYU%3D&wx_header=1&scene=1");
            /** 得到HttpURLConnection实例化对象 */
            conn = (HttpURLConnection) url.openConnection();
            /** 设置请求方式和响应时间 */
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            /** 不使用缓存 */
            conn.setUseCaches(false);
            /** 先将服务器得到的流对象 包装 存入缓存区，忽略了正在缓冲时间 */
            InputStream in = new BufferedInputStream(conn.getInputStream());
            String result = readInStream(in);
            String md5 = StringUtils.md5(result);
            String md5Str = mPreManager.getMessageRed();

            L.e("===========md5==========="+md5);
            L.e("===========md5Str==========="+md5Str);

            if (StringUtils.isStrNotNull(md5Str)){
                if (md5 == md5Str || md5.equals(md5Str)){
                    mPreManager.setShowRed(false);
                } else {
                    mPreManager.setShowRed(true);
                    mIvRed.setVisibility(View.VISIBLE);
                    mPreManager.setMessageRed(md5);
                }
            } else {
                mPreManager.setMessageRed(md5);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            /** 释放资源 */
            if (conn != null) {
                /** 关闭连接 */
                conn.disconnect();
            }
        }
    }

    public String readInStream(InputStream in) {
        Scanner scanner = new Scanner(in).useDelimiter("\\A");
        return scanner.hasNext() ? scanner.next() : "";
    }

}

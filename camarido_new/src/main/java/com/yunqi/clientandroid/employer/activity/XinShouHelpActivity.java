package com.yunqi.clientandroid.employer.activity;

import com.yunqi.clientandroid.CamaridoApp;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.activity.RecommendActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

/**
 * @Description:新手帮助
 * @ClassName: XinShouHelpActivity
 * @author: mengwei
 * @date: 2016-8-11 下午1:36:57
 */
public class XinShouHelpActivity extends FragmentActivity implements
        View.OnClickListener {
    private ImageView ivHelpYindao;
    private int i = 0;// 点击图片的标记

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
        setContentView(R.layout.employer_activity_xinshou_yindao);

        ivHelpYindao = (ImageView) findViewById(R.id.iv_employer_help_yindao);
        ivHelpYindao.setImageResource(R.drawable.fahuo_yindao);
        ivHelpYindao.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        i++;
        switch (i) {
            case 0:
                ivHelpYindao.setImageResource(R.drawable.fahuo_yindao);
                break;
            case 1:
                ivHelpYindao.setImageResource(R.drawable.fabu_yindao);
                break;
            case 2:
                ivHelpYindao.setImageResource(R.drawable.send_package_yindao);
                break;
            case 3:
                ivHelpYindao.setImageResource(R.drawable.baojiadan_yindao);
                break;
            case 4:
                ivHelpYindao.setImageResource(R.drawable.jiesuan_yindao);
                break;
            case 5:
                ivHelpYindao.setImageResource(R.drawable.daochu_yindao);
                break;
            case 6:// 跳转到主页面
                EmployerMainActivity.invoke(XinShouHelpActivity.this);
                XinShouHelpActivity.this.finish();
                break;

            default:
                break;
        }
    }

    // 本界面的跳转方法
    public static void invoke(Activity activity) {
        Intent intent = new Intent(activity, XinShouHelpActivity.class);
        activity.startActivity(intent);
    }
}

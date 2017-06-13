package com.yunqi.clientandroid.employer.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.utils.StringUtils;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 自动更新App
 */
public class UpdateManager {
	/* 下载中 */
	private static final int DOWNLOAD = 1;
	/* 下载结束 */
	private static final int DOWNLOAD_FINISH = 2;
	/* 下载保存路径 */
	private String mSavePath;
	/* 记载进度条数量 */
	private int progress;

	private Context mContext;
	private String mVersion;
	private String mUri;
	/* 更新进度条 */
	private ProgressBar mProgress;
	private Dialog mDownLoadDialog;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			// 正在下载
			case DOWNLOAD:
				// 设置进度条位置
				mProgress.setProgress(progress);
				break;
			case DOWNLOAD_FINISH:
				// 安装文件
				installApk();
				break;

			default:
				break;
			}
		};
	};

	public UpdateManager(Context mContext, String mVersion, String mUri) {
		this.mContext = mContext;
		this.mVersion = mVersion;
		this.mUri = mUri;
	}

	/**
	 * 检测软件更新
	 */
	public void checkUpdate() {
		if (isUpdate(mVersion)) {
			// 显示提示对话框
			showNoticeDialog();
		} else {
			Toast.makeText(mContext, "", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 检查软件是否有更新版本
	 * 
	 * @param serviceCode
	 * @return:boolean
	 */
	private boolean isUpdate(String mServiceCode) {
		// 获取当前软件版本
		int versionCode = getVersionCode(mContext);
		if (StringUtils.isStrNotNull(mServiceCode)) {
			int serviceCode = Integer.valueOf(mServiceCode);
			// 版本判断
			if (serviceCode > versionCode) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取软件版本号
	 * 
	 * @param context
	 * @return:int
	 */
	private int getVersionCode(Context context) {
		int versionCode = 0;
		// 获取软件版本号，对应AndroidManifest.xml下android:versionCode
		try {
			versionCode = context.getPackageManager().getPackageInfo(
					"com.yunqi.clientandroid", 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionCode;
	}

	/**
	 * 显示软件更新对话框
	 */
	private void showNoticeDialog() {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View view = inflater.inflate(R.layout.auto_update_apk, null);
		TextView tvVersion = (TextView) view
				.findViewById(R.id.tv_update_version);
		TextView tvMessage = (TextView) view
				.findViewById(R.id.tv_update_message);
		Button btDown = (Button) view.findViewById(R.id.bt_update_down);
		// 构建对话框
		AlertDialog.Builder builder = new Builder(mContext);
		final AlertDialog dialog = builder.create();
		dialog.setView(view);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		// 立即更新
		btDown.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				// 显示下载对话框
				showDownLoadDialog();
			}
		});
	}

	/**
	 * 显示软件下载对话框
	 */
	private void showDownLoadDialog() {
		// 构造软件下载对话框
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle(R.string.tv_update_zhengzai);
		// 给下载对话框增加进度条
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.auto_update_progress, null);
		mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
		builder.setView(v);

		mDownLoadDialog = builder.create();
		mDownLoadDialog.show();
		// 下载文件
		downLoadApk();
	}

	/**
	 * 下载Apk文件
	 */
	private void downLoadApk() {
		// 启动新线程下载软件
		new downloadApkThread().start();
	}

	/**
	 * 下载文件线程
	 */
	private class downloadApkThread extends Thread {
		@Override
		public void run() {
			try {
				// 判断SD卡是否存在，并且是否具有读写权限
				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					// 获得存储卡的路径
					String sdPath = Environment.getExternalStorageDirectory()
							+ "/";
					mSavePath = sdPath + "download";
					URL url = new URL(mUri);
					// 创建连接
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.connect();
					// 获取文件大小
					int length = conn.getContentLength();
					// 创建输入流
					InputStream is = conn.getInputStream();

					File file = new File(mSavePath);
					// 判断文件目录是否存在
					if (!file.exists()) {
						file.mkdirs();
					}
					File apkFile = new File(mSavePath,
							"camarido_yunqitech_release.apk");
					FileOutputStream fos = new FileOutputStream(apkFile);
					int count = 0;
					// 缓存
					byte buf[] = new byte[1024];
					// 写入到文件中
					do {
						int numread = is.read(buf);
						count += numread;
						// 计算进度条位置
						progress = (int) (((float) count / length) * 100);
						// 更新进度
						mHandler.sendEmptyMessage(DOWNLOAD);
						if (numread <= 0) {
							// 下载完成
							mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
							break;
						}
						// 写入文件
						fos.write(buf, 0, numread);
					} while (true);
					fos.close();
					is.close();
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// 取消下载对话框显示
			mDownLoadDialog.dismiss();
		}
	}

	/**
	 * 安装Apk文件
	 */
	private void installApk() {
		File apkFile = new File(mSavePath, "");
		if (!apkFile.exists()) {
			return;
		}
		// 通过Intent安装APK文件
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.parse("file://" + apkFile.toString()),
				"application/vnd.android.package-archive");
		mContext.startActivity(intent);
	}
}

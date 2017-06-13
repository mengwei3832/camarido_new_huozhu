package com.yunqi.clientandroid.employer.util;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.Toast;

import com.yunqi.clientandroid.R;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by ChengTao on 2016-08-27.
 */

public class FileDownloadUtils {
	private String fileUrl;
	private Context context;
	private static FileDownloadUtils instance;
	private NotificationManager manager;
	private NotificationCompat.Builder builder;
	private static final int NOTIFICATION_ID = 1;
	private static final int PROGRESS = 1;
	private String startTime = null;
	private String endTime = null;
	private Button btDaoChu;
	private static final String fileDir = "/sdcard/Download/";
	private File file;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case PROGRESS:
				int progress = (Integer) msg.obj;
				builder.setContentText("正在下载" + progress + "%").setProgress(
						100, progress, false);
				manager.notify(NOTIFICATION_ID, builder.build());
				if (progress == 100) {
					showToast(context, "下载完成");
					btDaoChu.setEnabled(true);
					btDaoChu.setBackgroundResource(R.drawable.sendbao_btn_background);
					OpenFile(file);
				}
				break;
			}
		}
	};

	public FileDownloadUtils(String fileUrl, Context context, Button btDaoChu) {
		this.fileUrl = fileUrl;
		this.context = context;
		this.btDaoChu = btDaoChu;
	}

//	public static FileDownloadUtils getInstance(String fileUrl,
//			Context context) {
//		if (instance == null) {
//			instance = new FileDownloadUtils(fileUrl, context);
//		}
//		return instance;
//	}

	public void downloadFile() {
		createNotification();
		new DownLoadThread().start();
	}

	private void createNotification() {
		manager = (NotificationManager) ((Activity) context)
				.getSystemService(Context.NOTIFICATION_SERVICE);
		builder = new NotificationCompat.Builder(context)
				.setSmallIcon(getApplicationIcon(context))
				.setContentTitle("统计报表").setContentText("正在下载0%")
				.setProgress(100, 0, false).setAutoCancel(false);
		manager.notify(NOTIFICATION_ID, builder.build());
	}

	class DownLoadThread extends Thread {
		@Override
		public void run() {
			Looper.prepare();
			InputStream is = null;
			RandomAccessFile raf = null;
			HttpURLConnection con = null;
			boolean isFinish = false;
			try {
				String murl = URLEncoder.encode(fileUrl, "utf-8").replaceAll(
						"\\+", "%20");
				String surl = murl.replaceAll("%3A", ":")
						.replaceAll("%2F", "/");
				Log.e("TAG", "-------surl---------" + surl);

				URL url = new URL(surl);
				Log.e("TAG", "--------url------------" + url);
				// java.net.URLEncoder.encode(url,"UTF-8");
				con = (HttpURLConnection) url.openConnection();
				con.setConnectTimeout(10 * 1000);
				con.setReadTimeout(10 * 1000);
				con.setRequestMethod("GET");
				con.setRequestProperty("Connection", "Keep-Alive");
				con.setRequestProperty("Charset", "UTF-8");
				File dir = new File(fileDir);
				if (!dir.exists()) {
					dir.mkdir();
				}

				Log.e("TAG", "----------pathurl-------------" + fileUrl);

				file = new File(fileDir + getFileName());
				if (!file.exists()) {
					file.createNewFile();
				}
				raf = new RandomAccessFile(file, "rwd");
				int length = -1;
				long time = System.currentTimeMillis();
				int fileLength;
				int finishProgress = 0;
				if (con.getResponseCode() == 200) {
					fileLength = con.getContentLength();
					if (fileLength <= 0) {
						showToast(context, "下载文件有问题");
						return;
					}
					is = con.getInputStream();
					byte[] buffer = new byte[4 * 1024];
					while ((length = is.read(buffer)) != -1) {
						finishProgress += length;
						raf.write(buffer, 0, length);
						if (System.currentTimeMillis() - time > 500) {
							sendMessage(Integer
									.parseInt((finishProgress * 100L / fileLength)
											+ ""));
							if (finishProgress == fileLength) {
								isFinish = true;
							}
						}
					}
					if (!isFinish) {
						sendMessage(100);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (raf != null) {
						raf.close();
					}
					if (is != null) {
						is.close();
					}
					if (con != null) {
						con.disconnect();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
	}

	private int getApplicationIcon(Context context) {
		return context.getApplicationInfo().icon;
	}

	private void showToast(Context context, String s) {
		Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
	}

	private void sendMessage(int progress) {
		Message msg = Message.obtain();
		msg.what = PROGRESS;
		msg.obj = progress;
		handler.sendMessage(msg);
	}

	private String fileExt(String url) {
		if (url.contains("?")) {
			url = url.substring(0, url.indexOf("?"));
		}
		if (url.lastIndexOf(".") == -1) {
			return null;
		} else {
			String ext = url.substring(url.lastIndexOf(".") + 1);
			if (ext.contains("%")) {
				ext = ext.substring(0, ext.indexOf("%"));
			}
			if (ext.contains("/")) {
				ext = ext.substring(0, ext.indexOf("/"));
			}
			return ext.toLowerCase();

		}
	}

	private void OpenFile(File file) {
		MimeTypeMap myMime = MimeTypeMap.getSingleton();
		Intent newIntent = new Intent(Intent.ACTION_VIEW);
		String mimeType = myMime.getMimeTypeFromExtension(fileExt(file
				.getName()));
		newIntent.setDataAndType(Uri.fromFile(file), mimeType);
		newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent resultPendingIntent = PendingIntent.getActivity(context,
				0, newIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		builder.setContentIntent(resultPendingIntent).setAutoCancel(true);
		manager.notify(NOTIFICATION_ID, builder.build());
	}

	/**
	 * 
	 * @Description:从下载路径中获取文件名字
	 * @Title:getFileName
	 * @return
	 * @return:String
	 * @throws
	 * @Create: Sep 2, 2016 11:34:10 AM
	 * @Author : chengtao
	 */
	private String getFileName() {
		String name = null;
		name = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
		Log.e("TAG", "==========getFileName=========" + name);
		return name;
	}
}

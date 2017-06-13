package com.yunqi.clientandroid;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsListener;

//import com.baidu.location.BDLocation;
//import com.baidu.location.BDLocationListener;
//import com.baidu.location.LocationClient;
//import com.baidu.location.LocationClientOption;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.yunqi.clientandroid.utils.L;
import com.yunqi.clientandroid.utils.MiPushUtil;
import com.yunqi.clientandroid.utils.PreManager;

public class CamaridoApp extends Application {

	// 云启新增销毁队列
	private static Map<String, Activity> destoryMap = new HashMap<String, Activity>();

	private static Context context;// 全局Context变量
	private static Handler mainHandler;// 主线程的handler
	public static CamaridoApp instance;
//	private LocationClient mLocClient;
	private PreManager mPreManager;

	@Override
	public void onCreate() {
		super.onCreate();
		// 全局异常处理函数
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(getApplicationContext());

		context = this;
		mainHandler = new Handler();
		mPreManager = PreManager.instance(this);
		// 小米推送初始化
		MiPushUtil.initPush(this);

		//webview内核初始化
		initX5Webview();

		//Log初始化
		L.isDebug = true;

		// 在使用SDK各组件之前初始化context信息，传入ApplicationContext
		// 注意该方法要再setContentView方法之前实现
		// SDKInitializer.initialize(getApplicationContext());

		instance = this;
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				this)
				.threadPriority(Thread.NORM_PRIORITY)
				.denyCacheImageMultipleSizesInMemory()
				.memoryCache(new LruMemoryCache(2 * 1024 * 1024))
				.memoryCacheSize(2 * 1024 * 1024)
				.threadPoolSize(10)
				.diskCache(
						new UnlimitedDiscCache(
								getExternalCacheDir() == null ? getCacheDir()
										: getExternalCacheDir()))
				.tasksProcessingOrder(QueueProcessingType.LIFO).build();
		ImageLoader.getInstance().init(config);

		// getLocation();
	}

	private void initX5Webview(){
		QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
			@Override
			public void onCoreInitFinished() {
			}

			@Override
			public void onViewInitFinished(boolean b) {
				Log.e("app", " onViewInitFinished is " + b);
			}
		};

		QbSdk.setTbsListener(new TbsListener() {
			@Override
			public void onDownloadFinish(int i) {
				Log.d("app","onDownloadFinish");
			}

			@Override
			public void onInstallFinish(int i) {
				Log.d("app","onInstallFinish");
			}

			@Override
			public void onDownloadProgress(int i) {
				Log.d("app","onDownloadProgress:"+i);
			}
		});

		QbSdk.initX5Environment(getApplicationContext(),cb);
	}

	/**
	 * 添加到销毁队列
	 * 
	 * @param activity
	 *            要销毁的activity
	 */

	public static void addDestoryActivity(Activity activity, String activityName) {
		destoryMap.put(activityName, activity);
	}

	/**
	 * 销毁指定Activity
	 */
	public static void destoryActivity(String activityName) {
		Set<String> keySet = destoryMap.keySet();
		for (String key : keySet) {
			destoryMap.get(key).finish();
		}
	}

	/**
	 * 获取指定Activity
	 */
	public static Activity getActivity(String activityName) {
		return destoryMap.get(activityName);
	}

	// 获取手机当前所在位置的经纬度
//	public void getLocation() {
//		// 实例化位置客户端
//		mLocClient = new LocationClient(getApplicationContext());
//		// 创建一个位置option对象
//		LocationClientOption locOption = new LocationClientOption();
//
//		// 设置option的属性
//		locOption.setCoorType("bd09II"); // bd09II表示返回的结果是百度的经纬度
//		locOption.setIsNeedAddress(true); // 返回的信息包含当前的地址
//		locOption.setNeedDeviceDirect(true); // 返回的内容包含手机机头的方向
//		locOption.setScanSpan(10000); // 每10秒发起一次定位请求
//		locOption
//				.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy); // 高精度模式，网络和GPS
//
//		// 将封装的参数设置到位置客户端
//		mLocClient.setLocOption(locOption);
//
//		// 给位置客户端注册位置监听器
//		mLocClient.registerLocationListener(new BDLocationListener() {
//
//			@Override
//			public void onReceiveLocation(BDLocation loc) {
//				double latitude = loc.getLatitude();
//				double longitude = loc.getLongitude();
//				mPreManager.setLatitude(latitude + "");
//				mPreManager.setLongitude(longitude + "");
//			}
//		});
//
//		// 启动位置客户端
//		mLocClient.start();
//	}

	// 获取上下文对象
	public static Context getContext() {
		return context;
	}

	public static Handler getHandler() {
		return mainHandler;
	}

}

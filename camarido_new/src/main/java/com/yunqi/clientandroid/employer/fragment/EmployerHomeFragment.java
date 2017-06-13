package com.yunqi.clientandroid.employer.fragment;

import java.util.ArrayList;
import java.util.List;

import com.yunqi.clientandroid.CamaridoApp;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.adapter.EmployerHomeViewPagerAdapter;
import com.yunqi.clientandroid.adapter.HomeViewPagerAdapter;
import com.yunqi.clientandroid.employer.activity.CurrentTicketActivity;
import com.yunqi.clientandroid.employer.activity.EmployerCompanyActicity;
import com.yunqi.clientandroid.employer.activity.EmployerCompanyRenZhengActivity;
import com.yunqi.clientandroid.employer.activity.EmployerMainActivity;
import com.yunqi.clientandroid.employer.activity.EmployerWebviewActivity;
import com.yunqi.clientandroid.employer.activity.GetMessageActivity;
import com.yunqi.clientandroid.employer.activity.HelpPkgActivity;
import com.yunqi.clientandroid.employer.activity.NewSendPackageActivity;
import com.yunqi.clientandroid.employer.activity.XinShouHelpActivity;
import com.yunqi.clientandroid.employer.adapter.HomeFragmentNewAdapter;
import com.yunqi.clientandroid.employer.adapter.HomeFragmentZiAdapter;
import com.yunqi.clientandroid.employer.entity.HomeFragmentNew;
import com.yunqi.clientandroid.employer.entity.HomeFragmentZixun;
import com.yunqi.clientandroid.employer.entity.ProvinceNameModel;
import com.yunqi.clientandroid.employer.request.CurrentBaoExistRequest;
import com.yunqi.clientandroid.employer.request.GetHomeNewRequest;
import com.yunqi.clientandroid.employer.request.GetHomeNewZiRequest;
import com.yunqi.clientandroid.employer.request.HostPkgUtil;
import com.yunqi.clientandroid.employer.util.ArrayToListUtil;
import com.yunqi.clientandroid.employer.util.UmengStatisticsUtils;
import com.yunqi.clientandroid.entity.BannerInfo;
import com.yunqi.clientandroid.fragment.BaseFragment;
import com.yunqi.clientandroid.http.request.GetBannerRequest;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.CacheUtils;
import com.yunqi.clientandroid.utils.L;
import com.yunqi.clientandroid.utils.PreManager;
import com.yunqi.clientandroid.utils.StringUtils;
import com.yunqi.clientandroid.view.CircleIndicator;
import com.yunqi.clientandroid.view.ViewPagerExpand;

import android.content.Intent;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

/**
 * 
 * @Description:class 发包方首页
 * @ClassName: EmployerHomeFragment
 * @author: zhm
 * @date: 2016-5-13 上午11:10:39
 * 
 */
public class EmployerHomeFragment extends BaseFragment implements
		OnClickListener {
	private RelativeLayout rFabaoHomeFragment;
	private RelativeLayout rZhixingHomeFragment;
	private ViewPagerExpand vpEmployerHome;
	private CircleIndicator circleEmployerIndicator;
	private ListView lvEmployerList;
	private ImageView ivEmptyView;
	private LinearLayout llEmployerHome;

	private PreManager mPreManager;
	private EmployerMainActivity employerMainActivity;

	private List<HomeFragmentNew> mBaoList = new ArrayList<HomeFragmentNew>();// 最新动态的集合
	private List<HomeFragmentZixun> mZiList = new ArrayList<HomeFragmentZixun>();// 最新资讯的集合
	private List<BannerInfo> mPagerList = new ArrayList<BannerInfo>();
	private EmployerHomeViewPagerAdapter mPagerAdapter;
	private HomeFragmentNewAdapter mHomeFragmentNewAdapter;
	private HomeFragmentZiAdapter mHomeFragmentZiAdapter;

	// 获取首页banner接口
	private GetBannerRequest mGetBannerRequest;
	private GetHomeNewRequest mGetHomeNewRequest;
	private CurrentBaoExistRequest mCurrentBaoExistRequest;
	private GetHomeNewZiRequest mGetHomeNewZiRequest;
	private final int GET_BANNER_REQUEST = 3;
	private final int GET_DONGTAI_LIEBIAO = 1;
	private final int GET_CURRENT_BAO_EXIST = 2;
	private final int GET_DONGTAI_ZIXUN = 4;

	private boolean bExist;

	private int PageSize = 4;
	private int PageIndex = 1;
	private int i = 1;

	/** 最新资讯数组 */
	private String[] provinces;
	private ArrayToListUtil mArrayToListUtil;
	private ArrayList<String> mLastPriceList = new ArrayList<>();

	/** 最新运价链接 */
	private String mHighwayIndexUrl;

	/* 友盟统计 */
	private UmengStatisticsUtils mUmeng;

	@Override
	protected void initData() {
		employerMainActivity = (EmployerMainActivity) getActivity();

		mPreManager.clearHomeCache();

		// 判断当前用户所属公司是否有包存在
		// mCurrentBaoExistRequest = new CurrentBaoExistRequest(getActivity());
		// mCurrentBaoExistRequest.setRequestId(GET_CURRENT_BAO_EXIST);
		// httpPost(mCurrentBaoExistRequest);

		// ---------------------------------首页轮播------------------------------------------------//
		if (mPreManager.isBannerCacheExist()) {
			Log.e("TAG", "mPreManager.isBannerCacheExist()");
			// 从缓存获取首页轮播图
			String[] bannerImage = mPreManager.getBanner();
			String[] bannerArtical = mPreManager.getBannerArticalUrls();
			// int[] bannerArray = {R.drawable.banner1, R.drawable.banner2,
			// R.drawable.banner3};
			for (int i = 0; i < bannerImage.length; i++) {
				if (!bannerImage[i].equals("") && !bannerArtical[i].equals("")) {
					mPagerList.add(new BannerInfo(bannerImage[i],
							bannerArtical[i]));
				}
			}
		} else {
			// 获取首页banner
			mGetBannerRequest = new GetBannerRequest(employerMainActivity);
			mGetBannerRequest.setRequestId(GET_BANNER_REQUEST);
			httpPost(mGetBannerRequest);
		}
		mPagerAdapter = new EmployerHomeViewPagerAdapter(mPagerList,
				employerMainActivity);
		vpEmployerHome.setAdapter(mPagerAdapter);
		circleEmployerIndicator.setIndicatorMode(CircleIndicator.Mode.INSIDE);
		circleEmployerIndicator.setViewPager(vpEmployerHome);
		Log.e("TAG", "--------mPagerList---------" + mPagerList.size());
		// ---------------------------------首页轮播------------------------------------------------//

		// -----------------------------------获取资讯列表-----------------------------------------//
		if (mPreManager.isEmployerZiXunCacheExist()) {
			Log.e("TAG", "mPreManager.isEmployerZiXunCacheExist()");
			mZiList = mPreManager.getEmployerZiXun();

			// 首页资讯的数据
//			mHomeFragmentZiAdapter = new HomeFragmentZiAdapter(getActivity(),
//					mZiList);
//			lvEmployerList.setAdapter(mHomeFragmentZiAdapter);
//			mHomeFragmentZiAdapter.notifyDataSetChanged();

		} else {
			mGetHomeNewZiRequest = new GetHomeNewZiRequest(getActivity(),
					PageIndex, PageSize);
			mGetHomeNewZiRequest.setRequestId(GET_DONGTAI_ZIXUN);
			httpGet(mGetHomeNewZiRequest);
		}

		// -----------------------------------获取资讯列表-----------------------------------------//

		// 对listview的Item的监听
		lvEmployerList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
//				HomeFragmentZixun mFragmentZixun = mZiList.get(position);
//
//				String messageId = String.valueOf(mFragmentZixun.Id);
//
//				GetMessageActivity.invoke(getActivity(), messageId, 0);
				if (StringUtils.isStrNotNull(mHighwayIndexUrl)){
					//友盟统计
					mUmeng.setCalculateEvents("home_click_new_tariffs");
					if (mHighwayIndexUrl.startsWith("http")){
						Intent intent = new Intent(getActivity(), EmployerWebviewActivity.class);
						intent.putExtra("Url",mHighwayIndexUrl);
						intent.putExtra("Title","线路询价");
						getActivity().startActivity(intent);
					} else {
						Intent intent = new Intent(getActivity(), EmployerWebviewActivity.class);
						intent.putExtra("Url","http://"+mHighwayIndexUrl);
						intent.putExtra("Title","线路询价");
						getActivity().startActivity(intent);
					}
				} else {
					showToast("当前页面正在加载，请稍候");
				}
			}
		});

	}

	@Override
	protected int getLayoutId() {
		return R.layout.employer_fragment_home;
	}

	@Override
	protected void initView(View _rootView) {
		mPreManager = PreManager.instance(getActivity());
		mUmeng = UmengStatisticsUtils.instance(getActivity());
		mArrayToListUtil = new ArrayToListUtil(getActivity());
		// 初始化titileBar
		initActionBar();

		/** 获取保存的最新资讯内容 */
		provinces = mPreManager.getHomeCrash();
		mLastPriceList = mArrayToListUtil.getHomeCrashList(provinces);

		L.e("---------第一次获取集合内容---------"+mLastPriceList.toString());

		rFabaoHomeFragment = obtainView(R.id.rl_employer_home_fabao);
		rZhixingHomeFragment = obtainView(R.id.rl_employer_home_zhixing);
		vpEmployerHome = obtainView(R.id.vp_employer_home);
		circleEmployerIndicator = obtainView(R.id.employer_indicator);
		lvEmployerList = obtainView(R.id.lv_employer_header_new);
		ivEmptyView = obtainView(R.id.iv_employer_empty);
		llEmployerHome = obtainView(R.id.ll_employer_home);

		/** 最新资讯（最新运价） */
		mHomeFragmentZiAdapter = new HomeFragmentZiAdapter(
				getActivity(), mLastPriceList);
		lvEmployerList.setAdapter(mHomeFragmentZiAdapter);
	}

	@Override
	protected void setListener() {
		rFabaoHomeFragment.setOnClickListener(this);
		rZhixingHomeFragment.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_employer_home_fabao: // TODO 点击发包按钮
			// SendPackageActivity.invoke(getActivity(), "");
			NewSendPackageActivity.invoke(getActivity(), "");
			//友盟统计
			mUmeng.setCalculateEvents("ship_home");
			break;

		case R.id.rl_employer_home_zhixing: // TODO 点击执行按钮
			//友盟统计
			mUmeng.setCalculateEvents("home_click_sign_up");

			CurrentTicketActivity.invoke(getActivity(), "", 1, -1);
			break;

		default:
			break;
		}

	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onSuccess(int requestId, Response response) {
		super.onSuccess(requestId, response);
		boolean isSuccess;
		String message;
		int totalCount;
		switch (requestId) {
		case GET_BANNER_REQUEST:
			totalCount = response.totalCount;
			mPagerList = response.data;
			View view;
			StringBuilder sbImage = new StringBuilder();
			StringBuilder sbArtical = new StringBuilder();
			// llpagePoint.removeAllViews();
			for (int i = 0; i < totalCount; i++) {
				// 每循环一次, 向LinearLayout布局中添加一个View.
				view = new View(CamaridoApp.instance);
				view.setBackgroundResource(R.drawable.point_normal);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						13, 13);
				if (i != 0) {
					params.leftMargin = 20;
				}
				view.setLayoutParams(params);
				// llpagePoint.addView(view);
				sbImage.append(i == (totalCount - 1) ? mPagerList.get(i).ImageUrl
						: mPagerList.get(i).ImageUrl + ",");
				sbArtical
						.append(i == (totalCount - 1) ? mPagerList.get(i).ArticleUrl
								: mPagerList.get(i).ArticleUrl + ",");
			}
			mPagerAdapter.setList(mPagerList);
			mPagerAdapter.notifyDataSetChanged();
			circleEmployerIndicator.setViewPager(vpEmployerHome);
			// 设置首页轮播缓存
			Log.e("TAG", "设置首页轮播缓存");
			mPreManager.setEmployerBannerInfo(sbImage.toString(),
					sbArtical.toString());

			Log.e("TAG", "--------mPagerList---------" + mPagerList.size());
			break;

		// case GET_DONGTAI_LIEBIAO: //首页最新动态数据
		// isSuccess = response.isSuccess;
		// message = response.message;
		// if (isSuccess) {
		// ArrayList<HomeFragmentNew> mNewList = response.data;
		// if (mNewList.size() != 0) {
		// mBaoList.addAll(mNewList);
		// }
		// //首页动态的数据
		// mHomeFragmentNewAdapter = new HomeFragmentNewAdapter(getActivity(),
		// mBaoList, bExist);
		// lvEmployerList.setAdapter(mHomeFragmentNewAdapter);
		//
		// mHomeFragmentNewAdapter.notifyDataSetChanged();
		// }
		// break;

		// case GET_CURRENT_BAO_EXIST://判断当前用户所属公司是否有包存在
		// isSuccess = response.isSuccess;
		// message = response.message;
		// if (isSuccess) {
		// //获取最新动态数据
		// mGetHomeNewRequest = new
		// GetHomeNewRequest(getActivity(),PageIndex,PageSize);
		// mGetHomeNewRequest.setRequestId(GET_DONGTAI_LIEBIAO);
		// httpPost(mGetHomeNewRequest);
		// bExist = true;
		// } else {
		// //获取最新资讯
		// mGetHomeNewZiRequest = new GetHomeNewZiRequest(getActivity(),
		// PageIndex, PageSize);
		// mGetHomeNewZiRequest.setRequestId(GET_DONGTAI_ZIXUN);
		// httpPost(mGetHomeNewZiRequest);
		// bExist = false;
		// }
		// break;

		case GET_DONGTAI_ZIXUN: // 获取首页最新资讯
			isSuccess = response.isSuccess;
			message = response.message;
//			if (response.data != null) {
//				if (response.data.size() == 0) {
//					ivEmptyView.setVisibility(View.VISIBLE);
//				} else {
//					ivEmptyView.setVisibility(View.GONE);
//				}
				if (isSuccess) {
//					ArrayList<HomeFragmentZixun> mNewList = response.data;
//					if (mNewList.size() != 0) {
//						mZiList.addAll(mNewList);
//						ivEmptyView.setVisibility(View.GONE);
//					} else {
//						ivEmptyView.setVisibility(View.VISIBLE);
//					}
					ProvinceNameModel provinceNameModel = (ProvinceNameModel) response.singleData;

					String mSXProvinceName = provinceNameModel.SXProvinceName;
					String mNMGProvinceName = provinceNameModel.NMGProvinceName;
					String mSHXProvinceName = provinceNameModel.SHXProvinceName;
					String mSXHisCount = provinceNameModel.SXHisCount;
					String mNMGHisCount = provinceNameModel.NMGHisCount;
					String mSHXHisCount = provinceNameModel.SHXHisCount;
					mHighwayIndexUrl = provinceNameModel.HighwayIndexUrl;

					mPreManager.setHomeCrash(mSXProvinceName,mNMGProvinceName,mSHXProvinceName,
							mSXHisCount,mNMGHisCount,mSHXHisCount,mHighwayIndexUrl);

					mLastPriceList.clear();

					provinces = new String[]{
							mSXProvinceName+"省最新运价<span>&nbsp;&nbsp;&nbsp;&nbsp;</sapn>"+mSXHisCount+"<span>&nbsp;&nbsp;&nbsp;&nbsp;</sapn>条",
							mNMGProvinceName+"省最新运价<span>&nbsp;&nbsp;&nbsp;&nbsp;</sapn>"+mNMGHisCount+"<span>&nbsp;&nbsp;&nbsp;&nbsp;</sapn>条",
							mSHXProvinceName+"省最新运价<span>&nbsp;&nbsp;&nbsp;&nbsp;</sapn>"+mSHXHisCount+"<span>&nbsp;&nbsp;&nbsp;&nbsp;</sapn>条"
					};

					mLastPriceList = mArrayToListUtil.getHomeCrashList(provinces);

					L.e("---------第二次获取集合内容---------"+mLastPriceList.toString());

					// 首页资讯的数据
					mHomeFragmentZiAdapter = new HomeFragmentZiAdapter(
							getActivity(), mLastPriceList);
					lvEmployerList.setAdapter(mHomeFragmentZiAdapter);

					mHomeFragmentZiAdapter.notifyDataSetChanged();

					// 设置资讯缓存
					Log.e("TAG", "设置资讯缓存");
					mPreManager.setEmployerZiXun(mZiList);
				}
//			} else {
//				ivEmptyView.setVisibility(View.VISIBLE);
//			}
			break;
		}
	}

	@Override
	public void onFailure(int requestId, int httpCode, Throwable error) {
		super.onFailure(requestId, httpCode, error);
		showToast("连接超时,请检查网络");
		switch (requestId){
			case GET_DONGTAI_ZIXUN:

				break;
		}
	}

	// 初始化TitleBar
	private void initActionBar() {
		EmployerMainActivity eActivity = (EmployerMainActivity) getActivity();
		eActivity.getActionBar().show();
		eActivity.setActionBarTitle("物流平台");
		eActivity.setActionBarRight(true, 0,
				getActivity().getString(R.string.employer_fragment_home_help));

		int type = CacheUtils.getInt(getActivity(), "type", 0);

		if (type == 1) {
			eActivity.setActionBarLeft(R.drawable.test);
		} else if (type == 2) {
			eActivity.setActionBarLeft(R.drawable.demo);
		} else {
			eActivity.setActionBarLeft(0);
		}

		String surroundings = HostPkgUtil.getApiHost();
		if (surroundings.equals("http://qa.pkgapi.yqtms.com/")){
			eActivity.setActionBarLeft(R.drawable.test);
		} else if(surroundings.equals("http://demo.pkgapi.yqtms.com/")){
			eActivity.setActionBarLeft(R.drawable.demo);
		} else {
			eActivity.setActionBarLeft(0);
		}

		eActivity.setOnActionBarLeftClickListener(null);

		eActivity.setOnActionBarRightClickListener(false,
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						//友盟统计
						mUmeng.setCalculateEvents("home_click_novice_help");

						// TODO 进入帮助页面
						XinShouHelpActivity.invoke(getActivity());
						CamaridoApp.addDestoryActivity(getActivity(),
								"EmployerMainActivity");
					}
				});
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);
		initActionBar();
	}

	public interface IChangeMain {
		void changeTab(int tabIndex);
	}
}

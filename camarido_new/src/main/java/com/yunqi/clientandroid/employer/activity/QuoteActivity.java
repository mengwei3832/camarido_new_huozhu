package com.yunqi.clientandroid.employer.activity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.zip.GZIPInputStream;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.j256.ormlite.field.types.DateTimeType;
import com.yunqi.clientandroid.R;
import com.yunqi.clientandroid.activity.BaseActivity;
import com.yunqi.clientandroid.employer.adapter.QuoteAdapter;
import com.yunqi.clientandroid.employer.adapter.SetNewPriceAdapter;
import com.yunqi.clientandroid.employer.entity.DefaultPriceEntity;
import com.yunqi.clientandroid.employer.entity.GetBiaoLieBiao;
import com.yunqi.clientandroid.employer.entity.GetCurrentCarNumber;
import com.yunqi.clientandroid.employer.entity.QuoteOrder;
import com.yunqi.clientandroid.employer.entity.QuoteSetNewPrice;
import com.yunqi.clientandroid.employer.entity.SetNewPrice;
import com.yunqi.clientandroid.employer.entity.TimeCollection;
import com.yunqi.clientandroid.employer.fragment.EmploterBaoBiaoFragment;
import com.yunqi.clientandroid.employer.request.AddYuanYinRequest;
import com.yunqi.clientandroid.employer.request.CanclePackageRequest;
import com.yunqi.clientandroid.employer.request.DefaultPriceRequest;
import com.yunqi.clientandroid.employer.request.GetBaoDetailContentRequest;
import com.yunqi.clientandroid.employer.request.GetCurrentCarNumberRequest;
import com.yunqi.clientandroid.employer.request.NewPriceHistoryRequest;
import com.yunqi.clientandroid.employer.request.PingTaiKeFuRequest;
import com.yunqi.clientandroid.employer.request.QuoteAllOrderRequest;
import com.yunqi.clientandroid.employer.request.QuoteHistoryOrderRequest;
import com.yunqi.clientandroid.employer.request.QuoteNoOrderRequest;
import com.yunqi.clientandroid.employer.request.QuoteOrderedRequest;
import com.yunqi.clientandroid.employer.request.QuoteSuccessFulOrderRequest;
import com.yunqi.clientandroid.employer.request.SetCurrentNewPrice;
import com.yunqi.clientandroid.employer.request.SetYiJiaPrice;
import com.yunqi.clientandroid.employer.request.StopPackageRequest;
import com.yunqi.clientandroid.employer.request.TimeCollectionRequest;
import com.yunqi.clientandroid.employer.request.XiaDanOrderRequest;
import com.yunqi.clientandroid.employer.util.DialogTool;
import com.yunqi.clientandroid.employer.util.QuoteQuXiaoItemOnClick;
import com.yunqi.clientandroid.employer.util.QuoteXiadanItemOnClick;
import com.yunqi.clientandroid.employer.util.SpManager;
import com.yunqi.clientandroid.employer.util.interfaces.DefaultPriceSure;
import com.yunqi.clientandroid.http.response.Response;
import com.yunqi.clientandroid.utils.CacheUtils;
import com.yunqi.clientandroid.utils.L;
import com.yunqi.clientandroid.utils.PayTimeUtils;
import com.yunqi.clientandroid.utils.PerformListItemOnClick;
import com.yunqi.clientandroid.utils.ProgressWheel;
import com.yunqi.clientandroid.utils.StringUtils;
import com.yunqi.clientandroid.utils.T;

/**
 * @Description:报价单页面
 * @ClassName: QuoteActivity
 * @author: mengwei
 * @date: 2016-6-29 上午9:09:08
 */
public class QuoteActivity extends BaseActivity implements PullToRefreshBase.OnRefreshListener2<ListView>,
        View.OnClickListener, AdapterView.OnItemClickListener {
    /* 界面控件对象 */
    private TextView tvQuoteBegin;
    private TextView tvQuoteBeginCity;
    private TextView tvQuoteEnd;
    private TextView tvQuoteEndCity;
    private TextView tvQuoteDate;
    private TextView tvQuoteVehicleNumber;
    private TextView tvQuoteCocal;
    private RelativeLayout rlQuoteAll;
    private RelativeLayout rlQuoteNoOrders;
    private RelativeLayout rlQuoteOrdered;
    private RelativeLayout rlQuoteSuccessFul;
    private PullToRefreshListView lvQuoteView;
    private Button rlQuoteCallCustomerService;
    private ProgressWheel pbQuoteBar;
    private ImageView ivQuoteBlank;
    private TextView tvZongdun;
    private TextView tvCurrentPrice;
    private TextView tvZhixingzhong, tvZaituzhong, tvDaijie, tvJieCar;
    private ImageView ivBaoxian;
    private Button btSetCurrentPrice;
    private RelativeLayout btShowRili;
    private RelativeLayout rlShowRili;
    private RelativeLayout rlYesterDay;
    private RelativeLayout rlLastDay;
    private RelativeLayout rlCurrentDay;
    private TextView tvCurrentDate;
    private TextView tvKuangVehicle, tvKuangDun;
    private TextView tvQianVehicle, tvQianDun;
    private RelativeLayout rlShowDatePicter;
    private Button btCurrentTongji;
    private LinearLayout llRili;
    private ImageView ivDrop;
    // private Button btStopPackage;
    private TextView tvDaichuli, tvZhixing, tvHistory;
    private TextView tvDaichuliLine, tvZhixingLine, tvHistoryLine;
    private LinearLayout llShowHide;
    private ProgressWheel pbShowHideBar;
    private ImageView ivLeft, ivRight;
    private Button btSetDefaultPrice;

    /* 传递过来的数据 */
    private String packageId;
    private String beginCompany;
    private String beginCity;
    private String beginCounty;
    private String endComapny;
    private String endCity;
    private String endCounty;
    private String dateTime;
    private int orderCount;
    private int mInsuranceType;
    private int mAnNiuHui = 0;
    private String mYiPai;
    private String mDaiJie;
    private String mYiJie;
    private String mPrice;
    private String mPackagePrice;
    private String mPackageWeight;
    private String mBeforeExcute;// 待执行车辆
    private String mOnTheWayCount;// 在途中车辆

    /* 请求类 */
    private QuoteAllOrderRequest quoteAllOrderRequest;
    private QuoteNoOrderRequest quoteNoOrderRequest;
    private QuoteOrderedRequest quoteOrderedRequest;
    private QuoteSuccessFulOrderRequest quoteSuccessFulOrderRequest;
    private SetYiJiaPrice setYiJiaPriceRequest;
    private AddYuanYinRequest addYuanYinRequest;
    private XiaDanOrderRequest xiaDanOrderRequest;
    private PingTaiKeFuRequest pingTaiKeFuRequest;
    private CanclePackageRequest canclePackageRequest;
    private QuoteHistoryOrderRequest quoteHistoryOrderRequest;
    private StopPackageRequest stopPackageRequest;
    private GetCurrentCarNumberRequest getCurrentCarNumberRequest;
    private SetCurrentNewPrice setCurrentNewPrice;
    private GetBaoDetailContentRequest mGetBaoDetailContentRequest; // 包的详细信息
    private TimeCollectionRequest mTimeCollectionRequest;// 请求有数据的天数
    private NewPriceHistoryRequest mNewPriceHistoryRequest;//请求最新价格历史记录
    private DefaultPriceRequest mDefaultPriceRequest;//请求是否有预设价格

    /* 请求ID */
    private final int GET_ALL_ORDER = 1;
    private final int GET_NO_ORDER = 2;
    private final int GET_ORDERED = 3;
    private final int GET_SUCCESS_FUL = 4;
    private final int SET_YIJIA = 5;
    private final int ADD_YUANYIN_QUXIAO = 6;
    private final int GET_ORDER_XIADAN = 7;
    private final int GET_PINGTAI_KEFU = 8;
    private final int STOP_PACKAGE = 9;
    private final int GET_HISTORY_ORDER = 10;
    private final int GET_CURRENT_CAR_NUMBER = 11;
    private final int SET_CURRENT_NEW_PRICE = 12;
    private final int GET_ISEXIT_NEWPRICE = 13;
    private final int GET_TIME_COLLECTION = 14;
    private final int GET_NEW_PRICE_HISTORY = 15;
    private final int GET_DEFAULT_PRICE = 16;

    /* 分页请求参数 */
    private int pageIndex = 1;// 起始页
    private int pageSize = 10;// 每页显示数量
    private int totalCount;// 返回数据的总数量
    private int count;// 实际返回的数据数量
    private boolean isEnd = false;// 是否服务器无数据返回
    private Handler handler = new Handler();

    // 标记ID
    private int checkId = 1;

    // 保存报价单列表数据的集合
    private ArrayList<QuoteOrder> quoteList = new ArrayList<QuoteOrder>();
    private ArrayList<QuoteOrder> nolist;
    private ArrayList<QuoteOrder> yilist;
    private ArrayList<QuoteOrder> fulList;
    private QuoteAdapter quoteAdapter;

    //最新价格的集合
    private ArrayList<SetNewPrice> mNewPriceList = new ArrayList<>();
    private SetNewPriceAdapter mSetNewPriceAdapter;
    private LinearLayout llShow;

    // 有数据的日期集和
    private ArrayList<TimeCollection> mTimeList = new ArrayList<TimeCollection>();
    private int mIndex = 0;// 今天的下标索引
    private String mYesterDate;// 昨天日期
    private String mCurrentDate;// 今天日期
    private String mTomorrowDate;// 明天日期
    private boolean isLast = false;// 判断下一个承运日期

    // 弹出框
    private PopupWindow yiJiaPop;
    private PopupWindow quXiaoPop;
    private View preView;
    private Button btYijia;
    // 议价弹窗控件对象
    private TextView tvPopYuanJia;
    private EditText etPopCheShu;
    private EditText etPopYiJia;
    private TextView tvPopCancal;
    private TextView tvPopSure;
    // 取消弹框控件对象
    private Button tvPopQuCancal;
    private Button tvPopQuSure;
    private EditText etPopQuYuanYin;
    private AlertDialog dialog;
    private AlertDialog date_dialog;

    // 设定最新价格弹窗
    private Button btSetNew;
    private String mNewPrice;

    // 提示弹出框
    private AlertDialog tishiDialog;

    // 判断显示日历
    private boolean isShowRili = false;
    private String mQianDayText;
    private String mQianDay;
    private String mYesterDayText;
    private String mYesterDay;
    private String mCurrentDay;
    private String mCurrentDayText;
    private String mMingDay;
    private String mQianDayLong;
    private String mYesterDayLong;
    private String mCurrentDayLong;
    private String mMingDayLong;
    private PopupWindow mCalendarViewPop;
    private CalendarView calendarView;
    private View parentView;
    private TextView tvCalendarCancle, tvCalendarSure;
    private String mSelectDate;// 选中的要显示的日期
    private String mSelectRequest;// 选中的要请求的日期
    private int year, month, day;// 今天的年月日
    private int mStartYear, mStartMonth, mStartDay;// 日历开始的日期
    private int mEndYear, mEndMonth, mEndDay;// 日历结束时间
    private long jieshu;
    private String mRequestDate;
    private String mLeftDate = null, mRightDate = null;

    private int dateCheckId = 0;

    private boolean isRequest = false;

    private SpManager mSpManager;

    private String infoId;

    /* 接口返回数据 */
    private String mPackageStartTime;// 包开始时间
    private String mPackageEndTime;// 包结束时间
    private int mPackageStatus;// 包的状态

    /* 标记进度条结束 */
    private boolean isFinish = false;

    private boolean isFirstRequest = true;

    //生效时间弹窗
    TextView tvDate;

    @Override
    protected int getLayoutId() {
        return R.layout.employer_activity_quote;
    }

    @Override
    protected void initView() {

        mSpManager = SpManager.instance(mContext);
        parentView = LayoutInflater.from(mContext).inflate(R.layout.employer_activity_quote, null);

        Log.e("TAG", "---------进入报价单-------------");
        // 获取传递过来的数据
        getTransFer();
        // 初始化控件对象
        initFindView();
        // 给控件赋值
        setControlsView();
    }

    /**
     * @throws @Create: 2016-6-29 下午1:40:27
     * @Description:给控件赋值
     * @Title:setControlsView
     * @return:void
     * @Author : mengwei
     */
    private void setControlsView() {
        /**
         * 获取当天日期
         */
        Calendar calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);
        month = (calendar.get(Calendar.MONTH) + 1);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        // 获取昨日、前日
        mCurrentDay = year + "-" + month + "-" + day;
        mCurrentDayText = month + "月" + day + "日";

        Log.d("TAG", "--------获取到当天日期-----------" + mCurrentDay);

        mQianDayText = StringUtils.getQianDayText(mCurrentDay);
        mYesterDayText = StringUtils.getYesterDayText(mCurrentDay);
        mQianDay = StringUtils.getQianDay(mCurrentDay);
        mYesterDay = StringUtils.getYesterDay(mCurrentDay);
        mMingDay = StringUtils.getMingDay(mCurrentDay);

        mQianDayLong = StringUtils.StringDateToDateLong(mQianDay);
        mYesterDayLong = StringUtils.StringDateToDateLong(mYesterDay);
        mCurrentDayLong = StringUtils.StringDateToDateLong(mCurrentDay);
        mMingDayLong = StringUtils.StringDateToDateLong(mMingDay);

        //TODO
        getDefaultPrice();


        /**
         * 设置tab日期栏
         */
        tvCurrentDate.setText(mCurrentDay);

        Log.e("TAG", "------dateTime-------" + dateTime);
        String vehicleNumber = "<font color='#ffff00'>" + orderCount + "</font>";
        tvQuoteBegin.setText(beginCompany);
        tvQuoteBeginCity.setText(beginCity + "·" + beginCounty);
        tvQuoteEnd.setText(endComapny);
        tvQuoteEndCity.setText(endCity + "·" + endCounty);
        tvQuoteDate.setText(dateTime);
        if (mInsuranceType == 0) {
            ivBaoxian.setVisibility(View.GONE);
        } else if (mInsuranceType == 1 || mInsuranceType == 2) {
            ivBaoxian.setVisibility(View.VISIBLE);
        }
        Log.d("TAG", "---------按钮置灰-----------" + mAnNiuHui);
        // if (mAnNiuHui != 0) {
        // setStopPackageEnabled(false);
        // }

        // 对已派、待结、结算车辆赋值
        if (StringUtils.isStrNotNull(mBeforeExcute)) {
            tvZhixingzhong.setText(Html.fromHtml("待执行：<font color='#ff4444'>" + mBeforeExcute + "</font>"));
        } else {
            tvZhixingzhong.setText(Html.fromHtml("待执行：<font color='#ff4444'>" + "0" + "</font>"));
        }

        if (StringUtils.isStrNotNull(mOnTheWayCount)) {
            tvZaituzhong.setText(Html.fromHtml("在途中：<font color='#ff4444'>" + mOnTheWayCount + "</font>"));
        } else {
            tvZaituzhong.setText(Html.fromHtml("在途中：<font color='#ff4444'>" + "0" + "</font>"));
        }

        if (StringUtils.isStrNotNull(mDaiJie)) {
            tvDaijie.setText(Html.fromHtml("待结算：<font color='#ff4444'>" + mDaiJie + "</font>"));
        } else {
            tvDaijie.setText(Html.fromHtml("待结算：<font color='#ff4444'>" + "0" + "</font>"));
        }
        if (StringUtils.isStrNotNull(mYiJie)) {
            tvJieCar.setText(Html.fromHtml("已结算：<font color='#ff4444'>" + mYiJie + "</font>"));
        } else {
            tvJieCar.setText(Html.fromHtml("已结算：<font color='#ff4444'>" + "0" + "</font>"));
        }

        // 给总吨数赋值
        if (StringUtils.isStrNotNull(mPackageWeight)) {
            tvZongdun.setText(mPackageWeight + "吨");
        } else {
            tvZongdun.setText("0.00吨");
        }
    }

    /**
     * @throws @Create: 2016-6-29 下午1:27:24
     * @Description:获取传递过来的数据
     * @Title:getTransFer
     * @return:void
     * @Author : mengwei
     */
    private void getTransFer() {
        packageId = getIntent().getStringExtra("packageId");
        beginCompany = getIntent().getStringExtra("beginCompany");
        beginCity = getIntent().getStringExtra("beginCity");
        beginCounty = getIntent().getStringExtra("beginCounty");
        endComapny = getIntent().getStringExtra("endComapny");
        endCity = getIntent().getStringExtra("endCity");
        endCounty = getIntent().getStringExtra("endCounty");
        dateTime = getIntent().getStringExtra("dateTime");
        mInsuranceType = getIntent().getIntExtra("insuranceType", 0);
        mAnNiuHui = getIntent().getIntExtra("anNiuHui", 0);
        mYiPai = getIntent().getStringExtra("mYiPai");
        mDaiJie = getIntent().getStringExtra("mDaiJie");
        mYiJie = getIntent().getStringExtra("mYiJie");
        mPackageWeight = getIntent().getStringExtra("packageWeight");
        mBeforeExcute = getIntent().getStringExtra("mZhiXing");
        mOnTheWayCount = getIntent().getStringExtra("mZaiTu");
    }

    // 初始化titileBar的方法
    private void initActionBar() {
        // 设置titileBar的标题
        setActionBarTitle(this.getResources().getString(R.string.employer_activity_quote_title));
        // 设置左边的返回箭头
        setActionBarLeft(R.drawable.nav_back);
        setActionBarRight(true, R.drawable.quote_stop_package, null);

        // 给左边的返回箭头加监听
        setOnActionBarLeftClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 关闭当前的Activity页面
                // 再按一次退出程序
                // EmployerMainActivity.newInvoke(mContext,"FROM_PRE_VIEW");
                // CamaridoApp.destoryActivity("EmployerMainActivity");
                QuoteActivity.this.finish();
            }
        });

        setOnActionBarRightClickListener(true, new OnClickListener() {
            @Override
            public void onClick(View v) {
                //友盟统计首页
                mUmeng.setCalculateEvents("quote_click_setting");
                // 进入运单管理页面
                // CurrentTicketActivity.invoke(mContext, "", 1, -1);
                StopPackageActivity.invoke(QuoteActivity.this, packageId);
            }
        });

    }

    /**
     * @throws @Create: 2016-6-29 上午10:39:39
     * @Description:初始化控件对象
     * @Title:initFindView
     * @return:void
     * @Author : mengwei
     */
    private void initFindView() {
        initActionBar();
        preView = QuoteActivity.this.getLayoutInflater().inflate(R.layout.employer_activity_quote, null);

        tvQuoteBegin = obtainView(R.id.tv_quote_begin);
        tvQuoteBeginCity = obtainView(R.id.tv_quote_begin_city);
        tvQuoteEnd = obtainView(R.id.tv_quote_end);
        tvQuoteEndCity = obtainView(R.id.tv_quote_end_city);
        tvQuoteDate = obtainView(R.id.tv_quote_date);
        rlQuoteNoOrders = obtainView(R.id.rl_quote_noorders);
        rlQuoteOrdered = obtainView(R.id.rl_quote_ordered);
        rlQuoteSuccessFul = obtainView(R.id.rl_quote_successful);
        lvQuoteView = obtainView(R.id.lv_quote_view);
        rlQuoteCallCustomerService = obtainView(R.id.rl_quote_call_customer_service);
        pbQuoteBar = obtainView(R.id.pb_quote);
        ivQuoteBlank = obtainView(R.id.iv_quote_blank);
        tvZongdun = obtainView(R.id.tv_quote_zongdun);
        tvCurrentPrice = obtainView(R.id.tv_quote_current_yunfei);
        tvZhixingzhong = obtainView(R.id.tv_quote_daizhixing);
        tvZaituzhong = obtainView(R.id.tv_quote_zaituzhhong);
        tvDaijie = obtainView(R.id.tv_quote_daijie);
        tvJieCar = obtainView(R.id.tv_quote_yijie);
        ivBaoxian = obtainView(R.id.iv_quote_baoxian);
        btSetCurrentPrice = obtainView(R.id.bt_quote_set_current_yunfei);
        btShowRili = obtainView(R.id.bt_quote_set_current_showrili);
        rlYesterDay = obtainView(R.id.rl_quote_yesterday);
        rlLastDay = obtainView(R.id.rl_quote_last);
        rlCurrentDay = obtainView(R.id.rl_quote_current);
        tvCurrentDate = obtainView(R.id.tv_quote_current_date);
        tvKuangVehicle = obtainView(R.id.tv_quote_kuang_car);
        tvKuangDun = obtainView(R.id.tv_quote_kuang_dun);
        tvQianVehicle = obtainView(R.id.tv_quote_qian_car);
        tvQianDun = obtainView(R.id.tv_quote_qian_dun);
        btCurrentTongji = obtainView(R.id.bt_quote_current_tongji);
        llRili = obtainView(R.id.ll_quote_rili);
        ivDrop = obtainView(R.id.iv_quote_drop);
        // btStopPackage = obtainView(R.id.bt_quote_stop_package);
        tvDaichuli = obtainView(R.id.tv_quote_daichuli);
        tvDaichuliLine = obtainView(R.id.v_quote_daichuli_line);
        tvZhixing = obtainView(R.id.tv_quote_zhingxing);
        tvZhixingLine = obtainView(R.id.v_quote_zhixing_line);
        tvHistory = obtainView(R.id.tv_quote_history);
        tvHistoryLine = obtainView(R.id.v_quote_history_line);
        llShowHide = obtainView(R.id.ll_quote_show);
        pbShowHideBar = obtainView(R.id.pb_quote_show_bar);
        ivLeft = obtainView(R.id.iv_quote_left_arrow);
        ivRight = obtainView(R.id.iv_quote_right_arrow);

        lvQuoteView.setMode(PullToRefreshBase.Mode.BOTH);
        lvQuoteView.setOnRefreshListener(this);

        quoteAdapter = new QuoteAdapter(quoteList, mContext, new PerformListItemOnClick() {
            @Override
            public void onClick(View item, int position, String id) {
                Log.e("TAG", "----------浸入议价-----------");
                QuoteOrder quoteOrder = quoteList.get(position);

                String yuanPrice = quoteOrder.price;
                // 显示要修改价格弹出框
                showUpdatePrice(yuanPrice, id);
            }
        }, new QuoteXiadanItemOnClick() {
            @Override
            public void onClick(View item, int position, String id) {
//                Log.e("TAG", "----------浸入下单-----------");
//                if (PayTimeUtils.isFastClick()) {
//                    showToast("正在处理，请不要频繁点击...");
//                    return;
//                }

                infoId = id;

                // 请求看是否设置了最新价格
                if (StringUtils.isStrNotNull(mPackagePrice)) {
                    if (mPackagePrice.equals("-1.00") || mPackagePrice.equals("0.00")) {
                        //友盟统计首页
                        mUmeng.setCalculateEvents("quote_click_dialog_prompt");
                        // 弹出提示框
                        showTishiSetPrice();
                    } else {
                        //友盟统计首页
                        mUmeng.setCalculateEvents("quote_click_item_place_order");
                        // 显示下单弹出框
                        showXiaDanDialog(infoId);
                    }
                }
            }
        }, new QuoteQuXiaoItemOnClick() {
            @Override
            public void onClick(View item, int position, String id, String mCarName) {
                Log.e("TAG", "----------浸入取消-----------");
                // 显示取消的弹窗
                showCancalPop(id, mCarName);
            }
        });
        lvQuoteView.setAdapter(quoteAdapter);
    }

    /**
     * 是否设置了最新价格
     */
    private void getIsExitNewPrice() {
        mGetBaoDetailContentRequest = new GetBaoDetailContentRequest(mContext, packageId);
        mGetBaoDetailContentRequest.setRequestId(GET_ISEXIT_NEWPRICE);
        httpPost(mGetBaoDetailContentRequest);
    }


    /**
     * @Description:弹窗提示设置最新价格
     */
    private void showTishiSetPrice() {
        LayoutInflater inflater = LayoutInflater.from(QuoteActivity.this);
        View view = inflater.inflate(R.layout.employer_dialog_tishi_setprice, null);
        Button btnCancle = (Button) view.findViewById(R.id.btn_tishi_cancle);
        Button btnSure = (Button) view.findViewById(R.id.btn_tishi_sure);
        AlertDialog.Builder builder = new Builder(QuoteActivity.this);
        tishiDialog = builder.create();
        tishiDialog.setView(view);
        tishiDialog.setCancelable(true);
        tishiDialog.show();

        btnCancle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //友盟统计首页
                mUmeng.setCalculateEvents("quote_click_dialog_prompt_cancel");

                tishiDialog.dismiss();
            }
        });

        btnSure.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //友盟统计首页
                mUmeng.setCalculateEvents("quote_click_dialog_prompt_sure");

                // 弹出设定最新价格弹窗\

                showSetCurrentNewPrice();
                tishiDialog.dismiss();

            }
        });
    }

    /**
     * 下单的弹窗
     */
    private void showXiaDanDialog(final String infoId) {
        LayoutInflater inflater = LayoutInflater.from(QuoteActivity.this);
        View view = inflater.inflate(R.layout.employer_dialog_xiadan, null);
        TextView tvPrice = (TextView) view.findViewById(R.id.tv_xiadan_current_price);
        Button btnCancle = (Button) view.findViewById(R.id.btn_xiadan_cancle);
        Button btnSure = (Button) view.findViewById(R.id.btn_xiadan_sure);
        AlertDialog.Builder builder = new Builder(QuoteActivity.this);
        dialog = builder.create();
        dialog.setView(view);
        Log.e("TAG", "---------packagePrice------------" + mPackagePrice);
        tvPrice.setText(Html.fromHtml("当前下单价格为：<font color='#ff4444'>" + mPackagePrice + "</font>元/吨"));
        dialog.setCancelable(true);

        dialog.show();
        btnCancle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnSure.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showProgressDialog("正在下单，请稍候...");
                // 下单的请求
                getXianDanOrder(infoId, mPackagePrice);
                dialog.dismiss();
            }
        });
    }

    /**
     * @Description:报价单下单的请求
     */
    private void getXianDanOrder(String infoId, String priceNew) {
        int infoID = Integer.parseInt(infoId);
        xiaDanOrderRequest = new XiaDanOrderRequest(mContext, infoID, priceNew);
        xiaDanOrderRequest.setRequestId(GET_ORDER_XIADAN);
        httpPost(xiaDanOrderRequest);
    }

    /**
     * @Description:显示取消的弹窗
     */
    private void showCancalPop(final String infoId, String mCarName) {
        LayoutInflater inflater = LayoutInflater.from(QuoteActivity.this);
        View view = inflater.inflate(R.layout.employer_popupwindow_quxiao, null);
        TextView tvZhong = (TextView) view.findViewById(R.id.tv_quote_zhongzhi);
        TextView btCancle = (TextView) view.findViewById(R.id.btn_zhongzhi_cancle);
        TextView btSure = (TextView) view.findViewById(R.id.btn_zhongzhi_sure);
        AlertDialog.Builder builder = new Builder(QuoteActivity.this);
        dialog = builder.create();
        dialog.setView(view);
        dialog.setCancelable(true);
        tvZhong.setText("订单终止之后," + mCarName + "将不会为您继续承运。请谨慎操作!");
        dialog.show();

        btCancle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btSure.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                addYuanYin(infoId, "");
                dialog.dismiss();
            }
        });
    }

    /**
     * @param infoId
     * @param yuanYin
     * @throws @Create: 2016-6-30 下午1:46:08
     * @Description:报价单取消的请求
     * @Title:addYuanYin
     * @return:void
     * @Author : mengwei
     */
    private void addYuanYin(String infoId, String yuanYin) {
        int infoID = Integer.parseInt(infoId);
        addYuanYinRequest = new AddYuanYinRequest(mContext, infoID, yuanYin);
        addYuanYinRequest.setRequestId(ADD_YUANYIN_QUXIAO);
        httpPost(addYuanYinRequest);
    }

    /**
     * @throws @Create: 2016-6-29 下午7:45:50
     * @Description:议价的请求
     * @Title:setYiJiaPrice
     * @return:void
     * @Author : mengwei
     */
    private void setYiJiaPrice(String infoId, String cheshuString, String yiJiaString) {
        setYiJiaPriceRequest = new SetYiJiaPrice(mContext, infoId, yiJiaString, cheshuString);
        setYiJiaPriceRequest.setRequestId(SET_YIJIA);
        httpPost(setYiJiaPriceRequest);
    }

    /**
     * 请求有数据的日期
     */
    private void getTimeCollection(String dateTxt) {
        mTimeList.clear();
        mRequestDate = dateTxt;
        mTimeCollectionRequest = new TimeCollectionRequest(mContext, packageId, dateTxt);
        mTimeCollectionRequest.setRequestId(GET_TIME_COLLECTION);
        httpPost(mTimeCollectionRequest);
    }

    /**
     * @throws @Create: 2016-6-29 下午8:16:12
     * @Description:显示要修改价格弹出框
     * @Title:showUpdatePrice
     * @return:void
     * @Author : mengwei
     */
    private void showUpdatePrice(String yuanPrice, final String infoId) {
        LayoutInflater inflater = LayoutInflater.from(QuoteActivity.this);
        View view = inflater.inflate(R.layout.employer_popupwindow_yijia, null);
        btYijia = (Button) view.findViewById(R.id.bt_dialog_yijia);
        final EditText etPrice = (EditText) view.findViewById(R.id.et_dialog_yijia_price);
        final EditText etVehicle = (EditText) view.findViewById(R.id.et_dialog_yijia_car);
        AlertDialog.Builder builder = new Builder(QuoteActivity.this);
        dialog = builder.create();
        dialog.setView(view);
        dialog.setCancelable(true);
        dialog.show();

        btYijia.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获得输入框的值
                String cheShuString = etVehicle.getText().toString().trim();
                String yiJiaString = etPrice.getText().toString().trim();
                if (cheShuString == null || TextUtils.isEmpty(cheShuString)) {
                    showToast("车数不可为空");
                    return;
                }
                if (Integer.parseInt(cheShuString) == 0) {
                    showToast("车数不可为0");
                    return;
                }
                if (yiJiaString == null || TextUtils.isEmpty(yiJiaString)) {
                    showToast("议价不可为空");
                    return;
                }
                if (Float.parseFloat(yiJiaString) == 0) {
                    showToast("议价不可为0");
                    return;
                }

                // 议价的请求
                setYiJiaPrice(infoId, cheShuString, yiJiaString);
                btYijia.setEnabled(false);
                dialog.dismiss();
            }
        });
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void setListener() {
        // 初始化点击事件
        initOnClick();
    }

    @Override
    public void onStart(int requestId) {
        super.onStart(requestId);
        setQuoteShowHide(true);
    }

    @Override
    public void onSuccess(int requestId, Response response) {
        super.onSuccess(requestId, response);
        boolean isSuccess;
        String message;
        switch (requestId) {
            case GET_TIME_COLLECTION:// 请求有数据的天数日期
                isSuccess = response.isSuccess;
                message = response.message;
                if (isSuccess) {
                    ArrayList<TimeCollection> mTClist = response.data;

                    String firstDate = StringUtils.getFanHuiBiao(mTClist.get(0).EffectiveTime);
                    String lastDate = StringUtils.getFanHuiBiao(mTClist.get(mTClist.size() - 1).EffectiveTime);

                    if (dateCheckId == 1) {
                        if (StringUtils.isStrNotNull(mLeftDate)) {
                            if (firstDate.equals(mLeftDate)) {
                                ivLeft.setImageResource(R.drawable.quote_arrow_left_gray);
                                ivRight.setImageResource(R.drawable.quote_arrow_right);
                                T.showShort(mContext, "没有更多数据");
                            }
                        }
                    } else if (dateCheckId == 2) {
                        if (StringUtils.isStrNotNull(mRightDate)) {
                            if (lastDate.equals(mRightDate)) {
                                ivLeft.setImageResource(R.drawable.quote_arrow_left);
                                ivRight.setImageResource(R.drawable.quote_arrow_right_gray);
                                T.showShort(mContext, "没有更多数据");
                            }
                        }
                    } else {
                        ivLeft.setImageResource(R.drawable.quote_arrow_left);
                        ivRight.setImageResource(R.drawable.quote_arrow_right);
                    }

                    if (mTClist != null) {
                        mTimeList.addAll(mTClist);
                    } else {
                        T.showShort(mContext, "无数据");
                        return;
                    }

                    // dsdsada

                    L.e("-----------------------");

                    // 去掉集合中的重复数据
                    mTimeList = StringUtils.getChongFu(mTimeList);
                    L.e("---------mTimeList1-----------" + mTimeList.toString());
                    // 给集合中数据排序
                    mTimeList = StringUtils.getPaiXu(mTimeList);

                    L.e("---------mTimeList2-----------" + mTimeList.toString());

                    for (int i = 0; i < mTimeList.size(); i++) {
                        if (mRequestDate.equals(StringUtils.getFanHuiBiao(mTimeList.get(i).EffectiveTime))) {
                            mIndex = i;
                            break;
                        } else {
                            mIndex = mTimeList.size() - 1;
                        }
                    }
//                    L.e("---------mIndexbijia-----------" + mIndex);
                }
                break;

            case GET_ISEXIT_NEWPRICE:
                isSuccess = response.isSuccess;
                message = response.message;
                if (isSuccess) {
                    GetBiaoLieBiao mGetBiaoLieBiao = (GetBiaoLieBiao) response.singleData;
                    mPackagePrice = mGetBiaoLieBiao.PackagePrice;
                    mPackageStartTime = StringUtils.formatModify(mGetBiaoLieBiao.PackageStartTime);
                    mPackageEndTime = StringUtils.formatModify(mGetBiaoLieBiao.PackageEndTime);
                    mPackageStatus = mGetBiaoLieBiao.PackageStatus;

                    // 给当前运价赋值
                    if (StringUtils.isStrNotNull(mPackagePrice)) {
                        if (mPackagePrice.equals("-1.00") || mPackagePrice.equals("0.00")) {
                            tvCurrentPrice.setText(Html.fromHtml("<font color='#ff4444'>" + "未设置" + "</font>"));
                        } else {
                            tvCurrentPrice.setText(Html.fromHtml("<font color='#ff4444'>" + mPackagePrice + "</font>元"));
                        }
                    }
                }

                break;
            case GET_ORDER_XIADAN:// 下单的请求
                isSuccess = response.isSuccess;
                message = response.message;

                if (isSuccess) {
                    showToast(message);

                    viewRequest(checkId);
                } else {
                    showToast(message);
                }
                hideProgressDialog();
                break;

            case ADD_YUANYIN_QUXIAO:// 取消的请求
                isSuccess = response.isSuccess;
                message = response.message;

                if (isSuccess) {
                    showToast(message);

                    viewRequest(checkId);
                } else {
                    showToast(message);
                }

                break;

            case SET_YIJIA:// 议价的请求
                isSuccess = response.isSuccess;
                message = response.message;

                if (isSuccess) {
                    showToast(message);
                } else {
                    showToast(message);
                }
                btYijia.setEnabled(true);
                break;
            case GET_ALL_ORDER:// 全部报价单的请求
                isSuccess = response.isSuccess;
                message = response.message;
                totalCount = response.totalCount;
                if (isSuccess) {
                    ArrayList<QuoteOrder> alllist = response.data;
                    if (alllist != null) {
                        quoteList.addAll(alllist);
                    }
                    if (alllist.size() == 0) {
                        showToast(message);
                        ivQuoteBlank.setVisibility(View.VISIBLE);
                    } else {
                        ivQuoteBlank.setVisibility(View.GONE);
                    }
                    if (totalCount <= count) {
                        isEnd = true;
                    }

                    quoteAdapter.notifyDataSetChanged();
                    lvQuoteView.onRefreshComplete();
                }
                break;
            case GET_NO_ORDER:// 未下单的请求
                isSuccess = response.isSuccess;
                message = response.message;
                totalCount = response.totalCount;
                if (isSuccess) {
                    nolist = response.data;
                    if (nolist != null) {
                        quoteList.addAll(nolist);
                    }
                    if (nolist.size() == 0) {
                        showToast(message);
                        ivQuoteBlank.setVisibility(View.VISIBLE);
                    } else {
                        ivQuoteBlank.setVisibility(View.GONE);
                    }
                    if (totalCount <= count) {
                        isEnd = true;
                    }
                    if (!isRequest) {
                        if (nolist.size() == 0 && yilist.size() != 0) {
                            quoteList.addAll(yilist);
                            hiddleView(2);
                            ivQuoteBlank.setVisibility(View.GONE);
                            checkId = 2;
                        }
                        isRequest = true;
                    }

                    // 对有数据日期的请求
                    getTimeCollection(mCurrentDay);

                    quoteAdapter.notifyDataSetChanged();
                    lvQuoteView.onRefreshComplete();
                }
                if (!isFinish) {
                    isFinish = true;
                }
                break;
            case GET_ORDERED:// 已下单的请求
                isSuccess = response.isSuccess;
                message = response.message;
                totalCount = response.totalCount;
                if (isSuccess) {
                    yilist = response.data;
                    if (!isRequest) {
                        viewRequest(1);
                        // isRequest = true;
                    } else {
                        if (yilist != null) {
                            quoteList.addAll(yilist);
                        }
                        if (yilist.size() == 0) {
                            showToast(message);
                            ivQuoteBlank.setVisibility(View.VISIBLE);
                        } else {
                            ivQuoteBlank.setVisibility(View.GONE);
                        }
                        if (totalCount <= count) {
                            isEnd = true;
                        }
                    }

                    quoteAdapter.notifyDataSetChanged();
                    lvQuoteView.onRefreshComplete();
                }
                break;
            case GET_HISTORY_ORDER:
                isSuccess = response.isSuccess;
                message = response.message;
                totalCount = response.totalCount;
                if (isSuccess) {
                    ArrayList<QuoteOrder> yilist = response.data;
                    if (yilist != null) {
                        quoteList.addAll(yilist);
                    }
                    if (yilist.size() == 0) {
                        showToast(message);
                        ivQuoteBlank.setVisibility(View.VISIBLE);
                    } else {
                        ivQuoteBlank.setVisibility(View.GONE);
                    }
                    if (totalCount <= count) {
                        isEnd = true;
                    }

                    quoteAdapter.notifyDataSetChanged();
                    lvQuoteView.onRefreshComplete();
                }
                break;
            case GET_SUCCESS_FUL:// 已中标的请求
                isSuccess = response.isSuccess;
                message = response.message;
                totalCount = response.totalCount;
                if (isSuccess) {
                    fulList = response.data;
                    if (fulList != null) {
                        quoteList.addAll(fulList);
                    }
                    if (fulList.size() == 0) {
                        showToast(message);
                        ivQuoteBlank.setVisibility(View.VISIBLE);
                    } else {
                        ivQuoteBlank.setVisibility(View.GONE);
                    }
                    if (totalCount <= count) {
                        isEnd = true;
                    }

                    quoteAdapter.notifyDataSetChanged();
                    lvQuoteView.onRefreshComplete();
                }
                break;

            // case STOP_PACKAGE:// 取消整个包
            // isSuccess = response.isSuccess;
            // message = response.message;
            // if (isSuccess) {
            // showToast(message);
            // setStopPackageEnabled(false);
            // EmploterBaoBiaoFragment.isBack = true;
            // finish();
            // } else {
            // showToast(message);
            // setStopPackageEnabled(true);
            // }
            // break;

            case GET_CURRENT_CAR_NUMBER:// 获取当日统计的车数
                isSuccess = response.isSuccess;
                message = response.message;
                if (isSuccess) {
                    GetCurrentCarNumber getCurrentCarNumber = (GetCurrentCarNumber) response.singleData;

                    String mLoadTicketCount = getCurrentCarNumber.LoadTicketCount;
                    String mLoadWeight = getCurrentCarNumber.LoadWeight;
                    String mSignTicketCount = getCurrentCarNumber.SignTicketCount;
                    String mSignWeight = getCurrentCarNumber.SignWeight;

                    if (mSignTicketCount.equals("0")) {
                        btCurrentTongji.setVisibility(View.GONE);
                    } else {
                        btCurrentTongji.setVisibility(View.VISIBLE);
                    }

                    if (StringUtils.isStrNotNull(mLoadTicketCount)) {
                        tvKuangVehicle.setText(Html.fromHtml("<font color='#ff4444'>" + mLoadTicketCount + "</font>辆"));
                    }
                    if (StringUtils.isStrNotNull(mLoadWeight)) {
                        tvKuangDun.setText(Html.fromHtml("<font color='#ff4444'>" + mLoadWeight + "</font>吨"));
                    }
                    if (StringUtils.isStrNotNull(mSignTicketCount)) {
                        tvQianVehicle.setText(Html.fromHtml("<font color='#ff4444'>" + mSignTicketCount + "</font>辆"));
                    }
                    if (StringUtils.isStrNotNull(mSignWeight)) {
                        tvQianDun.setText(Html.fromHtml("<font color='#ff4444'>" + mSignWeight + "</font>吨"));
                    }
                } else {
                    showToast(message);

                }
                viewRequest(2);
                getIsExitNewPrice();
                break;

            case SET_CURRENT_NEW_PRICE:// 设定当日最新价格
                isSuccess = response.isSuccess;
                message = response.message;
                if (isSuccess) {
                    QuoteSetNewPrice mQuote = (QuoteSetNewPrice) response.singleData;

                    boolean mIsEffect = mQuote.IsEffect;
                    String mPackagePriceNEW = mQuote.PackagePrice;
                    if (StringUtils.isStrNotNull(message)){
                        showToast(message);
                    } else {
                        showToast("设置成功");
                    }

                    if (mIsEffect){
                        mPackagePrice = mPackagePriceNEW;
                        tvCurrentPrice.setText(
                             Html.fromHtml("<font color='#ff4444'>" + StringUtils.saveTwoNumber(mPackagePrice) + "</font>元"));
                    }
//
//                    tvCurrentPrice.setText(
//                            Html.fromHtml("<font color='#ff4444'>" + StringUtils.saveTwoNumber(mPrice) + "</font>元"));
                } else {
                    showToast(message);
                }
                break;
            case GET_NEW_PRICE_HISTORY://最新价格历史记录
                isSuccess = response.isSuccess;
                message = response.message;
                if (isSuccess) {
                    ArrayList<SetNewPrice> priceList = response.data;

                    if (priceList != null) {
                        mNewPriceList.addAll(priceList);
                    }

                    showSetCurrentNewPrice();
                    mSetNewPriceAdapter.notifyDataSetChanged();
                    btSetCurrentPrice.setEnabled(true);
                    hideProgressDialog();
                } else {
                    btSetCurrentPrice.setEnabled(true);
                    hideProgressDialog();
                }
                break;

            case GET_DEFAULT_PRICE://判断是否有预设价格
                isSuccess = response.isSuccess;
                message = response.message;
                if (isSuccess){
                    if (!isFirstRequest){
                        hideProgressDialog();
                        DefaultPriceEntity defaultPriceEntity = (DefaultPriceEntity) response.singleData;

                        boolean mHasAdvancePrice = defaultPriceEntity.HasAdvancePrice;
                        String mPrice = defaultPriceEntity.Price;
                        String mEffectTime = defaultPriceEntity.EffectTime;

                        if (mHasAdvancePrice){//有预设价格，弹窗显示
                            //隐藏进度条弹窗
                            hideProgressDialog();
                            //弹出预设价格提示窗
                            DialogTool.showDefaultPriceDialog(QuoteActivity.this, mPrice, mEffectTime, new DefaultPriceSure() {
                                @Override
                                public void onNextRequest(int id) {

                                    L.e("---------id-------"+id);

                                    if (id == 1){
                                        //显示进度条
                                        showProgressDialog("请稍候...");
                                        //设置最新价格
                                        getNewPriceHistoryRequest();
                                    } else {
                                        btSetCurrentPrice.setEnabled(true);
                                    }
                                }
                            });
                        } else {//没有预设价格，请求运价历史
                            //设置最新价格
                            getNewPriceHistoryRequest();
                        }
                    } else {
                        isFirstRequest = false;
                        getCurrentCarNumber(mCurrentDay);
                    }
                } else {
                    if (!isFirstRequest){
                        //设置最新价格
                        getNewPriceHistoryRequest();
                    } else {
                        isFirstRequest = false;
                        getCurrentCarNumber(mCurrentDay);
                    }
                }
//                getCurrentCarNumber(mCurrentDay);
                break;

            default:
                break;
        }
        setQuoteShowHide(false);
    }

    @Override
    public void onFailure(int requestId, int httpCode, Throwable error) {
        super.onFailure(requestId, httpCode, error);
        showToast(getString(R.string.net_error_toast));
        lvQuoteView.onRefreshComplete();
        setQuoteShowHide(false);
        // tvPopQuSure.setEnabled(true);
        switch (requestId) {
            case GET_NEW_PRICE_HISTORY:
                showSetCurrentNewPrice();
                btSetCurrentPrice.setEnabled(true);
                hideProgressDialog();
                break;
            case GET_CURRENT_CAR_NUMBER:
                getIsExitNewPrice();
                break;

            case GET_DEFAULT_PRICE:
                if (!isFirstRequest){
                    //设置最新价格
                    getNewPriceHistoryRequest();
                } else {
                    isFirstRequest = false;
                    getCurrentCarNumber(mCurrentDay);
                }
                break;
            case GET_ORDER_XIADAN:
                hideProgressDialog();
                break;
        }
    }

    /**
     * 判断屏幕是否隐藏，并显示进度条
     *
     * @param isShow
     */
    private void setQuoteShowHide(boolean isShow) {
        if (isShow) {
//            llShowHide.setVisibility(View.VISIBLE);
//            pbShowHideBar.setVisibility(View.GONE);
            showProgressDialog("请稍候...");

        } else {
//            llShowHide.setVisibility(View.GONE);
//            pbShowHideBar.setVisibility(View.VISIBLE);
            hideProgressDialog();
        }
    }

    /**
     * @throws @Create: 2016-6-29 下午3:35:18
     * @Description:初始化点击事件
     * @Title:initOnClick
     * @return:void
     * @Author : mengwei
     */
    private void initOnClick() {
        rlQuoteNoOrders.setOnClickListener(this);
        rlQuoteOrdered.setOnClickListener(this);
        rlQuoteSuccessFul.setOnClickListener(this);
        rlQuoteCallCustomerService.setOnClickListener(this);
        btCurrentTongji.setOnClickListener(this);
        btSetCurrentPrice.setOnClickListener(this);
        btShowRili.setOnClickListener(this);
        // rlShowDatePicter.setOnClickListener(this);// 显示时间选择空间
        rlYesterDay.setOnClickListener(this);// 点击前天
        rlCurrentDay.setOnClickListener(this);// 点击昨天
        rlLastDay.setOnClickListener(this);// 点击今天
        // btStopPackage.setOnClickListener(this);// 点击终止包
        // 列表item点击事件
        lvQuoteView.setOnItemClickListener(this);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        // 清空存放当前订单列表的集合
        quoteList.clear();
        quoteAdapter.notifyDataSetChanged();
        // 起始页置为1
        pageIndex = 1;
        // count = 0;
        // isEnd = false;
        // 请求服务器获取当前运单的数据列表
        viewRequest(checkId);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        if (!isEnd) {
            ++pageIndex;
            viewRequest(checkId);
        } else {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    lvQuoteView.onRefreshComplete();
                    showToast("已经是最后一页了");
                }
            }, 100);
        }
    }

    /**
     * @param context
     * @param packageId    订单ID
     * @param beginCompany 始发地
     * @param beginCity    始发地市级
     * @param beginCounty  始发地区或县
     * @param endComapny   目的地
     * @param endCity      目的地市级
     * @param endCounty    目的地区或县
     * @param dateTime     开始时间和结束时间
     * @throws @Create: 2016-6-29 上午11:17:35
     * @Description:报价单页面跳转方法
     * @Title:invoke
     * @return:void
     * @Author : mengwei
     */
    public static void invoke(Context context, String packageId, String beginCompany, String beginCity,
                              String beginCounty, String endComapny, String endCity, String endCounty, String dateTime, int insuranceType,
                              int anNiuHui, String mZhiXing, String mZaiTu, String mDaiJie, String mYiJie, String currentPrice,
                              String packageWeight) {
        Intent intent = new Intent(context, QuoteActivity.class);
        intent.putExtra("packageId", packageId);
        intent.putExtra("beginCompany", beginCompany);
        intent.putExtra("beginCity", beginCity);
        intent.putExtra("beginCounty", beginCounty);
        intent.putExtra("endComapny", endComapny);
        intent.putExtra("endCity", endCity);
        intent.putExtra("endCounty", endCounty);
        intent.putExtra("dateTime", dateTime);
        intent.putExtra("insuranceType", insuranceType);
        intent.putExtra("anNiuHui", anNiuHui);
        intent.putExtra("mZhiXing", mZhiXing);
        intent.putExtra("mZaiTu", mZaiTu);
        intent.putExtra("mDaiJie", mDaiJie);
        intent.putExtra("mYiJie", mYiJie);
        intent.putExtra("currentPrice", currentPrice);
        intent.putExtra("packageWeight", packageWeight);
        context.startActivity(intent);
    }

    // /**
    // * 终止包按钮的变化
    // */
    // private void setStopPackageEnabled(boolean enabled) {
    // if (!enabled) {
    // btStopPackage.setEnabled(enabled);
    // btStopPackage.setBackgroundResource(R.drawable.btn_zhihui);
    // } else {
    // btStopPackage.setEnabled(enabled);
    // btStopPackage.setBackgroundResource(R.drawable.quote_btn_red);
    // }
    // }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // case R.id.bt_quote_stop_package:// 终止整个包
            // showStopPackageDialog();
            // setStopPackageEnabled(false);
            // break;
            case R.id.rl_quote_current:// 显示日历选择
                //友盟统计首页
                mUmeng.setCalculateEvents("quote_click_calendar");

                showCalendarViewDialog();
                break;
            case R.id.rl_quote_yesterday:// 点击昨天
                //友盟统计首页
                mUmeng.setCalculateEvents("quote_click_carrier_calendar_yesterday");

                Log.e("TAG", "-----选昨天日期------" + mYesterDay);

                mIndex--;

                dateCheckId = 1;

                if (mIndex < 0) {
                    mLeftDate = StringUtils.getFanHuiBiao(mTimeList.get(0).EffectiveTime);
                    mIndex++;
                    if (StringUtils.isStrNotNull(mLeftDate)) {
                        // 请求有数据的日期
                        getTimeCollection(mLeftDate);
                    }

                    L.e("---------mLeftDate----------" + mLeftDate);

                    return;
                } else {
                    // 获取昨天日期
                    mYesterDate = StringUtils.getFanHuiBiao(mTimeList.get(mIndex).EffectiveTime);
                    // 赋值日期
                    tvCurrentDate.setText(mYesterDate);
                    ivLeft.setImageResource(R.drawable.quote_arrow_left);
                    ivRight.setImageResource(R.drawable.quote_arrow_right);
                }

                // 获取指定日期的矿发和签收车数
                getCurrentCarNumber(mYesterDate);

                break;
            case R.id.rl_quote_last:// 点击明天
                //友盟统计首页
                mUmeng.setCalculateEvents("quote_click_carrier_calendar_tomorrow");

                Log.e("TAG", "-----index------" + mIndex);

                mIndex++;
                dateCheckId = 2;
                if (mIndex >= mTimeList.size()) {
                    mIndex--;
                    mRightDate = StringUtils.getFanHuiBiao(mTimeList.get(mTimeList.size() - 1).EffectiveTime);
                    if (mCurrentDay == mRightDate) {
                        T.showShort(mContext, "没有更多数据了");
                    } else if (StringUtils.isStrNotNull(mRightDate)) {
                        // 请求有数据的日期
                        getTimeCollection(mRightDate);
                    }

                    L.e("---------mRightDate----------" + mRightDate);

                    return;
                } else {
                    // 获取明天日期
                    mTomorrowDate = StringUtils.getFanHuiBiao(mTimeList.get(mIndex).EffectiveTime);

                    L.e("----------mTomorrowDate-----------" + mTomorrowDate);

                    // 赋值日期
                    tvCurrentDate.setText(mTomorrowDate);
                    ivLeft.setImageResource(R.drawable.quote_arrow_left);
                    ivRight.setImageResource(R.drawable.quote_arrow_right);
                }

                // 获取指定日期的矿发和签收车数
                getCurrentCarNumber(mTomorrowDate);

                break;

            case R.id.bt_quote_current_tongji:// 当日统计
                //友盟统计首页
                mUmeng.setCalculateEvents("quote_click_current_signup_statistics");

                String currentTxt = tvCurrentDate.getText().toString().trim();
                String mingTxt = StringUtils.getMingDay(currentTxt);
                String currentLong = StringUtils.StringDateToDateLong(currentTxt);
                String mingLong = StringUtils.StringDateToDateLong(mingTxt);

                L.e("--------currentTxt----------" + currentTxt);
                L.e("--------mingTxt----------" + mingTxt);
                L.e("--------currentLong----------" + currentLong);
                L.e("--------mingLong----------" + mingLong);

                ChengYunTongJiActivity.invoke(mContext, packageId, currentLong, currentLong, "", -1, -1, 1);
                break;

            case R.id.bt_quote_set_current_showrili:// 点击显示日历
                //友盟统计首页
                mUmeng.setCalculateEvents("quote_click_carrier_calendar");

                if (!isShowRili) {
                    llRili.setVisibility(View.VISIBLE);
                    isShowRili = true;
                    ivDrop.setImageResource(R.drawable.quote_drop_up);
                } else {
                    llRili.setVisibility(View.GONE);
                    isShowRili = false;
                    ivDrop.setImageResource(R.drawable.quote_drop_down);
                }
                break;
            case R.id.bt_quote_set_current_yunfei:// 设定最新价格
                //友盟统计首页
                mUmeng.setCalculateEvents("quote_click_setting_new_price");

                // 显示设定最新价格弹窗
                if (nolist.size() == 0 && yilist.size() == 0) {
                    showToast("您的订单当前无人报价");
                } else {
                    //显示进度条
                    showProgressDialog("请稍候...");
                    //判断是否有预设价格
                    getDefaultPrice();

                    btSetCurrentPrice.setEnabled(false);
//                    showSetCurrentNewPrice();
                }
                break;
            case R.id.rl_quote_noorders:// 未下单
                //友盟统计首页
                mUmeng.setCalculateEvents("quote_click_pending");

                checkId = 1;
                viewRequest(checkId);

                hiddleView(checkId);
                break;
            case R.id.rl_quote_ordered: // 已下单
                //友盟统计首页
                mUmeng.setCalculateEvents("quote_click_inexecution");

                checkId = 2;
                viewRequest(checkId);
                hiddleView(checkId);
                break;
            case R.id.rl_quote_successful:// 已中标
                //友盟统计首页
                mUmeng.setCalculateEvents("quote_click_history");

                checkId = 3;
                viewRequest(checkId);
                hiddleView(checkId);
                break;

            case R.id.rl_quote_call_customer_service:// TODO 拨打客服电话
                // if (StringUtils.isStrNotNull(pcsPhone)) {
                // CommonUtil.callPhoneIndirect(mContext, pcsPhone);
                // }

                //友盟统计首页
                mUmeng.setCalculateEvents("quote_click_carrier_statistics");

                Log.e("TAG", "--------点击承运-------");

                String maxString = "32503651200";
                String minString = "946656000";

                ChengYunTongJiActivity.invoke(mContext, packageId, minString, maxString, "", -1, -1, 0);
                break;

            default:
                break;
        }
    }

    /**
     * 判断是否有预设价格
     */
    private void getDefaultPrice(){
        mDefaultPriceRequest = new DefaultPriceRequest(mContext,packageId);
        mDefaultPriceRequest.setRequestId(GET_DEFAULT_PRICE);
        httpGet(mDefaultPriceRequest);
    }

    /**
     * 设置日历的弹出框
     *
     * @Title:setCalendarViewPop
     * @Create: 2016-11-9 下午5:31:41
     */
    private void setCalendarViewPop() {
        mCalendarViewPop = new PopupWindow(mContext);
        View mCView = QuoteActivity.this.getLayoutInflater().inflate(R.layout.employer_dialog_carlendarview, null);
        mCalendarViewPop.setWidth(LayoutParams.MATCH_PARENT);
        mCalendarViewPop.setHeight(LayoutParams.WRAP_CONTENT);
        mCalendarViewPop.setBackgroundDrawable(new BitmapDrawable());
        mCalendarViewPop.setContentView(mCView);
        mCalendarViewPop.setOutsideTouchable(true);
        mCalendarViewPop.setFocusable(true);
        mCalendarViewPop.setTouchable(true); // 设置PopupWindow可触摸
        mCalendarViewPop.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        //		Calendar calendar = Calendar.getInstance();
        //
        //		// 获取日历开始日期
        //		String[] start = mPackageStartTime.split("-");
        //		mStartYear = Integer.valueOf(start[0]);
        //		mStartMonth = Integer.valueOf(start[1]);
        //		mStartDay = Integer.valueOf(start[2]);
        //
        //		// 获取日历结束日期
        //		String[] end = mPackageEndTime.split("-");
        //		mEndYear = Integer.valueOf(end[0]);
        //		mEndMonth = Integer.valueOf(end[1]);
        //		mEndDay = Integer.valueOf(end[2]);
        //
        //		year = calendar.get(Calendar.YEAR);
        //		month = (calendar.get(Calendar.MONTH) + 1);
        //		day = calendar.get(Calendar.DAY_OF_MONTH);
        //
        //
        //		if (mPackageStatus == 5) {
        //			mEndYear = year;
        //			mEndMonth = month;
        //			mEndDay = day;
        //		}
        //
        //		L.e("-----包开始时间-----" + mEndYear + "-" + mEndMonth + "-" + mEndDay);
        //		L.e("-----包截止时间-----" + year + "-" + month + "-" + day);
        //		calendar = Calendar.getInstance();
        //		calendar.set(Calendar.YEAR, year);
        //		calendar.set(Calendar.MONTH, month);
        //		calendar.set(Calendar.DATE, day);
        //		calendar.set(Calendar.HOUR_OF_DAY, 0);
        //		long startOfMonth = calendar.getTimeInMillis();


        calendarView = (CalendarView) mCView.findViewById(R.id.cv_quote_dialog);
        tvCalendarCancle = (TextView) mCView.findViewById(R.id.tv_calendar_cancle);
        tvCalendarSure = (TextView) mCView.findViewById(R.id.tv_calendar_sure);
        calendarView.setShowWeekNumber(false);
        // calendarView.setMinDate(startOfMonth);
        // calendarView.setMaxDate(startOfMonth);

        fixBuggyCalendarview(calendarView);

    }

    private void fixBuggyCalendarview(CalendarView cv) {
        //获取当天时间long类型
        long current = cv.getDate();
        //获取结束时间long类型
        long end = StringUtils.getDateType(mPackageEndTime);
        //获取开始时间long类型
        long currents = StringUtils.getDateType(mCurrentDay);
        cv.setDate(cv.getMaxDate(), false, true);
        L.e("------current-------" + current);
        L.e("------bijiao---current-------" + currents);
        if (mPackageStatus == 5) {
            cv.setMaxDate(end);
        } else {
            cv.setMaxDate(current);
        }
    }

    /**
     * 日历的弹出框
     *
     * @Title:showCalendarViewDialog
     * @Create: 2016-11-9 下午5:33:28
     */
    private void showCalendarViewDialog() {
        if (mCalendarViewPop == null) {
            // 设置日历弹出框
            setCalendarViewPop();
        }

        mCalendarViewPop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);

        calendarView.setOnDateChangeListener(new OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                mSelectDate = year + "年" + (month + 1) + "月" + dayOfMonth + "日";
                mSelectRequest = year + "-" + (month + 1) + "-" + dayOfMonth;
                T.showShort(mContext, mSelectDate);
            }
        });

        tvCalendarCancle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendarViewPop.dismiss();
            }
        });

        tvCalendarSure.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                L.e("-----mCurrentDay--------" + mCurrentDay);
                L.e("------mSelectRequest-------" + mSelectRequest);
                L.e("-----mPackageEndTime--------" + mPackageEndTime);


                if (!StringUtils.isStrNotNull(mSelectDate)) {
                    mSelectRequest = mCurrentDay;
                }

                long select = Long.valueOf(StringUtils.StringDateToDateLong(mSelectRequest));
                long current = Long.valueOf(StringUtils.StringDateToDateLong(mCurrentDay));
                long start = Long.valueOf(StringUtils.StringDateToDateLong(mPackageStartTime));

                if (start > select) {
                    T.showShort(mContext, "当前发货开始日期为" + mPackageStartTime + "，请选择运输期间内的时间！");
                    return;
                }

                tvCurrentDate.setText(mSelectRequest);

                mCalendarViewPop.dismiss();
                // 请求选中日期的矿发和签收
                getCurrentCarNumber(mSelectRequest);
                // 请求有数据的日期
                getTimeCollection(mSelectRequest);
            }
        });
    }

    /**
     * 获取今日统计的车辆数
     */
    public void getCurrentCarNumber(String dayStr) {
        getCurrentCarNumberRequest = new GetCurrentCarNumberRequest(mContext, packageId, dayStr);
        getCurrentCarNumberRequest.setRequestId(GET_CURRENT_CAR_NUMBER);
        httpGet(getCurrentCarNumberRequest);
    }

    /**
     * 获取最新价格历史记录请求
     */
    private void getNewPriceHistoryRequest() {
        mNewPriceList.clear();
        mNewPriceHistoryRequest = new NewPriceHistoryRequest(mContext, packageId);
        mNewPriceHistoryRequest.setRequestId(GET_NEW_PRICE_HISTORY);
        httpGet(mNewPriceHistoryRequest);
    }

    /**
     * @Description:设定最新价格弹窗
     */
    private void showSetCurrentNewPrice() {
        LayoutInflater inflater = LayoutInflater.from(QuoteActivity.this);
        View view = inflater.inflate(R.layout.employer_dialog_setnewprice, null);
        ListView lvHistoryprice = (ListView) view.findViewById(R.id.lv_set_newprice);
        llShow = (LinearLayout) view.findViewById(R.id.ll_newprice_isshow);
        btSetNew = (Button) view.findViewById(R.id.bt_quote_newprice_sure);
        LinearLayout llDialog = (LinearLayout) view.findViewById(R.id.ll_show_dateDialog);
        tvDate = (TextView) view.findViewById(R.id.tv_dialog_setnew_date);
        Button btCancal = (Button) view.findViewById(R.id.bt_quote_newprice_cancal);
        final EditText etPrice = (EditText) view.findViewById(R.id.et_dialog_setnew_price);

        if (mNewPriceList.size() == 0) {
            llShow.setVisibility(View.GONE);
        } else {
            llShow.setVisibility(View.VISIBLE);
        }
        //设置当前时间
        Calendar cal = Calendar.getInstance();
        String MONTH = String.format("%02d",(cal.get(Calendar.MONTH) + 1));
        String DAY_OF_MONTH = String.format("%02d",cal.get(Calendar.DAY_OF_MONTH));
        String HOUR_OF_DAY = String.format("%02d",cal.get(Calendar.HOUR_OF_DAY));
        String MINUTE = String.format("%02d",cal.get(Calendar.MINUTE));
        String mDate = cal.get(Calendar.YEAR) + "-" + MONTH + "-" + DAY_OF_MONTH
                + " " + HOUR_OF_DAY + ":" + MINUTE;
        final long mDateLong = StringUtils.getDateTypeLong(mDate);
        L.e("--------mDateLong---------"+mDateLong);
        tvDate.setText(cal.get(Calendar.YEAR) + "-" + MONTH + "-" + DAY_OF_MONTH
        + " " + HOUR_OF_DAY + ":" + MINUTE);

        mSetNewPriceAdapter = new SetNewPriceAdapter(mContext, mNewPriceList);
        lvHistoryprice.setAdapter(mSetNewPriceAdapter);

        AlertDialog.Builder builder = new Builder(QuoteActivity.this);
        dialog = builder.create();
        dialog.setView(view);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        llDialog.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog(mDateLong);
            }
        });

        btCancal.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //友盟统计首页
                mUmeng.setCalculateEvents("quote_click_setting_new_price_cancel");

                dialog.dismiss();
            }
        });
        btSetNew.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //友盟统计首页
                mUmeng.setCalculateEvents("quote_click_setting_new_price_sure");

                mPrice = etPrice.getText().toString().trim();
                String dateStr = tvDate.getText().toString().trim()+":00";
                L.e("---------dateStr-----------"+dateStr);
                if (!StringUtils.isStrNotNull(mPrice)) {
                    showToast("请输入金额");
                    return;
                }
                double newPrice = Double.valueOf(mPrice);
                if (newPrice == 0) {
                    showToast("金额不可为0");
                    return;
                }

                // 请求设定最新价格接口
                setCurrentNewPrice = new SetCurrentNewPrice(mContext, packageId, mPrice,dateStr);
                setCurrentNewPrice.setRequestId(SET_CURRENT_NEW_PRICE);
                httpGet(setCurrentNewPrice);

                dialog.dismiss();
            }
        });
    }

    /**
     * 显示选择生效时间
     */
    private void showDateDialog(final long mDateLong){
        LayoutInflater inflater = LayoutInflater.from(QuoteActivity.this);
        View view = inflater.inflate(R.layout.dialog_choose_date, null);

        final DatePicker datePicker = (DatePicker) view.findViewById(R.id.choose_datepicker);
        final TimePicker timePicker = (TimePicker) view.findViewById(R.id.choose_timepicker);
        TextView tvSure = (TextView) view.findViewById(R.id.dialog_cancel_date);

//        resizePikcer(datePicker);
//        resizePikcer(timePicker);

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), null);

        timePicker.setIs24HourView(true);
        timePicker.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY));
        timePicker.setCurrentMinute(cal.get(Calendar.MINUTE));

        AlertDialog.Builder builder = new Builder(QuoteActivity.this);
        date_dialog = builder.create();
        date_dialog.setView(view);
        date_dialog.setCancelable(true);
        date_dialog.setCanceledOnTouchOutside(true);
        date_dialog.show();

        tvSure.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuffer sb = new StringBuffer();
                sb.append(String.format("%d-%02d-%02d",
                        datePicker.getYear(),
                        datePicker.getMonth() + 1,
                        datePicker.getDayOfMonth()));
                sb.append(" ");
                sb.append(String.format("%02d",timePicker.getCurrentHour()))
                        .append(":").append(String.format("%02d",timePicker.getCurrentMinute()));

//                String strb = String.valueOf(sb);
//                long mCurrentLong = StringUtils.getDateTypeLong(strb);
//                L.e("---------mCurrentLong----------"+mCurrentLong);
//                if (mCurrentLong < mDateLong){
//                    showToast("生效时间不能小于当前时间");
//                } else {
                    tvDate.setText(sb);
                    date_dialog.dismiss();
//                }
            }
        });
    }

    /**
     * 调整FrameLayout大小
     * @param tp
     */
    private void resizePikcer(FrameLayout tp){
        List<NumberPicker> npList = findNumberPicker(tp);
        for(NumberPicker np:npList){
            resizeNumberPicker(np);
        }
    }

    /*
		 * 调整numberpicker大小
		 */
    private void resizeNumberPicker(NumberPicker np){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(150, LayoutParams.WRAP_CONTENT);
        params.setMargins(10, 0, 10, 0);
        np.setLayoutParams(params);
    }

    /**
     * 得到viewGroup里面的numberpicker组件
     * @param viewGroup
     * @return
     */
    private List<NumberPicker> findNumberPicker(ViewGroup viewGroup){
        List<NumberPicker> npList = new ArrayList<NumberPicker>();
        View child = null;
        if(null != viewGroup){
            for(int i = 0;i<viewGroup.getChildCount();i++){
                child = viewGroup.getChildAt(i);
                if(child instanceof NumberPicker){
                    npList.add((NumberPicker)child);
                }
                else if(child instanceof LinearLayout){
                    List<NumberPicker> result = findNumberPicker((ViewGroup)child);
                    if(result.size()>0){
                        return result;
                    }
                }
            }
        }
        return npList;
    }

    /**
     * @param checkID 标记ID
     * @throws @Create: 2016-6-30 上午10:46:33
     * @Description:报价单的请求
     * @Title:viewRequest
     * @return:void
     * @Author : mengwei
     */
    private void viewRequest(int checkID) {

        // 清空集合
        quoteList.clear();
        quoteAdapter.notifyDataSetChanged();
        count = pageIndex * pageSize;

        switch (checkID) {
            case 1:// 待处理
                quoteNoOrderRequest = new QuoteNoOrderRequest(mContext, pageIndex, pageSize, packageId);
                quoteNoOrderRequest.setRequestId(GET_NO_ORDER);
                httpPost(quoteNoOrderRequest);
                break;
            case 2:// 执行中
                quoteOrderedRequest = new QuoteOrderedRequest(mContext, pageIndex, pageSize, packageId);
                quoteOrderedRequest.setRequestId(GET_ORDERED);
                httpPost(quoteOrderedRequest);
                // quoteSuccessFulOrderRequest = new QuoteSuccessFulOrderRequest(
                // mContext, pageIndex, pageSize, packageId);
                // quoteSuccessFulOrderRequest.setRequestId(GET_SUCCESS_FUL);
                // httpPost(quoteSuccessFulOrderRequest);
                break;
            case 3:// 历史
                quoteHistoryOrderRequest = new QuoteHistoryOrderRequest(mContext, pageIndex, pageSize, packageId);
                quoteHistoryOrderRequest.setRequestId(GET_HISTORY_ORDER);
                httpPost(quoteHistoryOrderRequest);
                break;

            default:
                break;
        }
    }

    /**
     * @param checkID
     * @throws @Create: 2016-6-29 下午2:34:32
     * @Description:字体颜色变动、tab隐藏
     * @Title:hiddleView
     * @return:void
     * @Author : mengwei
     */
    private void hiddleView(int checkID) {
        switch (checkID) {
            case 1: // 点击未下单
                // 字体变红
                tvDaichuli.setTextColor(this.getResources().getColor(R.color.color_00bbff));
                tvDaichuliLine.setBackgroundColor(this.getResources().getColor(R.color.color_00bbff));
                tvZhixing.setTextColor(this.getResources().getColor(R.color.color_303030));
                tvZhixingLine.setBackgroundColor(this.getResources().getColor(R.color.color_cfcfcf));
                tvHistory.setTextColor(this.getResources().getColor(R.color.color_303030));
                tvHistoryLine.setBackgroundColor(this.getResources().getColor(R.color.color_cfcfcf));
                break;
            case 2: // 点击执行中
                // 字体变红
                tvDaichuli.setTextColor(this.getResources().getColor(R.color.color_303030));
                tvDaichuliLine.setBackgroundColor(this.getResources().getColor(R.color.color_cfcfcf));
                tvZhixing.setTextColor(this.getResources().getColor(R.color.color_00bbff));
                tvZhixingLine.setBackgroundColor(this.getResources().getColor(R.color.color_00bbff));
                tvHistory.setTextColor(this.getResources().getColor(R.color.color_303030));
                tvHistoryLine.setBackgroundColor(this.getResources().getColor(R.color.color_cfcfcf));
                break;
            case 3: // 点击历史
                // 字体变红
                tvDaichuli.setTextColor(this.getResources().getColor(R.color.color_303030));
                tvDaichuliLine.setBackgroundColor(this.getResources().getColor(R.color.color_cfcfcf));
                tvZhixing.setTextColor(this.getResources().getColor(R.color.color_303030));
                tvZhixingLine.setBackgroundColor(this.getResources().getColor(R.color.color_cfcfcf));
                tvHistory.setTextColor(this.getResources().getColor(R.color.color_00bbff));
                tvHistoryLine.setBackgroundColor(this.getResources().getColor(R.color.color_00bbff));
                break;

            default:
                break;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 再按一次退出程序
            // EmployerMainActivity.newInvoke(mContext,"FROM_PRE_VIEW");
            // CamaridoApp.destoryActivity("EmployerMainActivity");
            QuoteActivity.this.finish();
        }
        return false;
    }

    /**
     * 点击item跳转到经纪人承运统计页面
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //友盟统计首页
        mUmeng.setCalculateEvents("quote_click_item_quotes");

        QuoteOrder mQuoteOrder = (QuoteOrder) quoteAdapter.getItem(position - 1);
        // 获取经纪人Id
        int mDepartId = Integer.valueOf(mQuoteOrder.departmentId);
        String maxString = "32503651200";
        String minString = "946656000";
        // 跳转到承运统计页面
        ChengYunTongJiActivity.invoke(mContext, packageId, minString, maxString, "", mDepartId, -1, 0);
    }

}

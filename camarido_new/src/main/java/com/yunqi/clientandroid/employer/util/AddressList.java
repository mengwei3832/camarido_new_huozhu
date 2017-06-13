package com.yunqi.clientandroid.employer.util;

import com.yunqi.clientandroid.employer.entity.GetProvince;

import java.util.ArrayList;
import java.util.List;

/**
 * 所有省市区的集合
 * Created by mengwei on 2017/5/16.
 */

public class AddressList {
    /**
     * 获取各省份市的集合
     * @param mProvinceId 省份Id
     * @return 省份市的集合
     */
    public static List<GetProvince> getCityList(int mProvinceId){
        List<GetProvince> cityList = new ArrayList<>();
        switch (mProvinceId){
            case 109000238:
                for (int i = 0; i < AddressUtils.shanxiName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.shanxiName[i]);
                    mGetProvince.setId(AddressUtils.shanxiId[i]);
                    cityList.add(mGetProvince);
                }
                break;
            case 109000380:
                for (int i = 0; i < AddressUtils.neimengguName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.neimengguName[i]);
                    mGetProvince.setId(AddressUtils.neimengguId[i]);
                    cityList.add(mGetProvince);
                }
                break;
            case 109003078:
                for (int i = 0; i < AddressUtils.shanbeiName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.shanbeiName[i]);
                    mGetProvince.setId(AddressUtils.shanbeiId[i]);
                    cityList.add(mGetProvince);
                }
                break;
            case 109000022:
                for (int i = 0; i < AddressUtils.tianjinName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.tianjinName[i]);
                    mGetProvince.setId(AddressUtils.tianjinId[i]);
                    cityList.add(mGetProvince);
                }
                break;
            case 109001499:
                for (int i = 0; i < AddressUtils.shandongName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.shandongName[i]);
                    mGetProvince.setId(AddressUtils.shandongId[i]);
                    cityList.add(mGetProvince);
                }
                break;
            case 109000043:
                for (int i = 0; i < AddressUtils.hebeiName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.hebeiName[i]);
                    mGetProvince.setId(AddressUtils.hebeiId[i]);
                    cityList.add(mGetProvince);
                }
                break;
            case 109001674:
                for (int i = 0; i < AddressUtils.henanName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.henanName[i]);
                    mGetProvince.setId(AddressUtils.henanId[i]);
                    cityList.add(mGetProvince);
                }
                break;
            case 109003404:
                for (int i = 0; i < AddressUtils.xinjiangName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.xinjiangName[i]);
                    mGetProvince.setId(AddressUtils.xinjiangId[i]);
                    cityList.add(mGetProvince);
                }
                break;
            case 109001868:
                for (int i = 0; i < AddressUtils.hubeiName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.hubeiName[i]);
                    mGetProvince.setId(AddressUtils.hubeiId[i]);
                    cityList.add(mGetProvince);
                }
                break;
            case 109001997:
                for (int i = 0; i < AddressUtils.hunanName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.hunanName[i]);
                    mGetProvince.setId(AddressUtils.hunanId[i]);
                    cityList.add(mGetProvince);
                }
                break;
            case 109000887:
                for (int i = 0; i < AddressUtils.jiangsuName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.jiangsuName[i]);
                    mGetProvince.setId(AddressUtils.jiangsuId[i]);
                    cityList.add(mGetProvince);
                }
                break;
            case 109003372:
                for (int i = 0; i < AddressUtils.ningxiaName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.ningxiaName[i]);
                    mGetProvince.setId(AddressUtils.ningxiaId[i]);
                    cityList.add(mGetProvince);
                }
                break;
            case 109002520:
                for (int i = 0; i < AddressUtils.sichuanName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.sichuanName[i]);
                    mGetProvince.setId(AddressUtils.sichuanId[i]);
                    cityList.add(mGetProvince);
                }
                break;
            case 109001133:
                for (int i = 0; i < AddressUtils.anhuiName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.anhuiName[i]);
                    mGetProvince.setId(AddressUtils.anhuiId[i]);
                    cityList.add(mGetProvince);
                }
                break;
            case 109001273:
                for (int i = 0; i < AddressUtils.fujianName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.fujianName[i]);
                    mGetProvince.setId(AddressUtils.fujianId[i]);
                    cityList.add(mGetProvince);
                }
                break;
            case 109001377:
                for (int i = 0; i < AddressUtils.jiangxiName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.jiangxiName[i]);
                    mGetProvince.setId(AddressUtils.jiangxiId[i]);
                    cityList.add(mGetProvince);
                }
                break;
            case 109003206:
                for (int i = 0; i < AddressUtils.gansuName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.gansuName[i]);
                    mGetProvince.setId(AddressUtils.gansuId[i]);
                    cityList.add(mGetProvince);
                }
                break;
            case 109003319:
                for (int i = 0; i < AddressUtils.qinghaiName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.qinghaiName[i]);
                    mGetProvince.setId(AddressUtils.qinghaiId[i]);
                    cityList.add(mGetProvince);
                }
                break;
            case 109002147:
                for (int i = 0; i < AddressUtils.guangdongName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.guangdongName[i]);
                    mGetProvince.setId(AddressUtils.guangdongId[i]);
                    cityList.add(mGetProvince);
                }
                break;
            case 109000001:
                for (int i = 0; i < AddressUtils.beijingName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.beijingName[i]);
                    mGetProvince.setId(AddressUtils.beijingId[i]);
                    cityList.add(mGetProvince);
                }
                break;
            case 109003521:
                for (int i = 0; i < AddressUtils.taiwanName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.taiwanName[i]);
                    mGetProvince.setId(AddressUtils.taiwanId[i]);
                    cityList.add(mGetProvince);
                }
                break;
            case 109003522:
                for (int i = 0; i < AddressUtils.xianggangName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.xianggangName[i]);
                    mGetProvince.setId(AddressUtils.xianggangId[i]);
                    cityList.add(mGetProvince);
                }
                break;
            case 109003523:
                for (int i = 0; i < AddressUtils.aomenName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.aomenName[i]);
                    mGetProvince.setId(AddressUtils.aomenId[i]);
                    cityList.add(mGetProvince);
                }
                break;
            case 109002741:
                for (int i = 0; i < AddressUtils.guizhouName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.guizhouName[i]);
                    mGetProvince.setId(AddressUtils.guizhouId[i]);
                    cityList.add(mGetProvince);
                }
                break;
            case 109002842:
                for (int i = 0; i < AddressUtils.yunnanName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.yunnanName[i]);
                    mGetProvince.setId(AddressUtils.yunnanId[i]);
                    cityList.add(mGetProvince);
                }
                break;
            case 109002996:
                for (int i = 0; i < AddressUtils.xizangName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.xizangName[i]);
                    mGetProvince.setId(AddressUtils.xizangId[i]);
                    cityList.add(mGetProvince);
                }
                break;
            case 109000865:
                for (int i = 0; i < AddressUtils.shanghaiName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.shanghaiName[i]);
                    mGetProvince.setId(AddressUtils.shanghaiId[i]);
                    cityList.add(mGetProvince);
                }
                break;
            case 109001020:
                for (int i = 0; i < AddressUtils.zhejiangName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.zhejiangName[i]);
                    mGetProvince.setId(AddressUtils.zhejiangId[i]);
                    cityList.add(mGetProvince);
                }
                break;
            case 109000502:
                for (int i = 0; i < AddressUtils.liaoningName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.liaoningName[i]);
                    mGetProvince.setId(AddressUtils.liaoningId[i]);
                    cityList.add(mGetProvince);
                }
                break;
            case 109000631:
                for (int i = 0; i < AddressUtils.jilinName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.jilinName[i]);
                    mGetProvince.setId(AddressUtils.jilinId[i]);
                    cityList.add(mGetProvince);
                }
                break;
            case 109000709:
                for (int i = 0; i < AddressUtils.heilongjiangName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.heilongjiangName[i]);
                    mGetProvince.setId(AddressUtils.heilongjiangId[i]);
                    cityList.add(mGetProvince);
                }
                break;
            case 109002309:
                for (int i = 0; i < AddressUtils.guangxiName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.guangxiName[i]);
                    mGetProvince.setId(AddressUtils.guangxiId[i]);
                    cityList.add(mGetProvince);
                }
                break;
            case 109002447:
                for (int i = 0; i < AddressUtils.hainanName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.hainanName[i]);
                    mGetProvince.setId(AddressUtils.hainanId[i]);
                    cityList.add(mGetProvince);
                }
                break;
            case 109002476:
                for (int i = 0; i < AddressUtils.chongqingName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.chongqingName[i]);
                    mGetProvince.setId(AddressUtils.chongqingId[i]);
                    cityList.add(mGetProvince);
                }
                break;
        }
        return cityList;
    }

    /**
     * 获取各市县（区）的集合
     * @param mCityId 市的Id
     * @return 市县（区）的集合
     */
    public static List<GetProvince> getCountryList(int mCityId){
        List<GetProvince> countryList = new ArrayList<>();
        switch (mCityId){
            case 109000239:
                for (int i = 0; i < AddressUtils.shanxiTaiyuanName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.shanxiTaiyuanName[i]);
                    mGetProvince.setId(AddressUtils.shanxiTaiyuanId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000264:
                for (int i = 0; i < AddressUtils.shanxiYangquanName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.shanxiYangquanName[i]);
                    mGetProvince.setId(AddressUtils.shanxiYangquanId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000251:
                for (int i = 0; i < AddressUtils.shanxiDatongName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.shanxiDatongName[i]);
                    mGetProvince.setId(AddressUtils.shanxiDatongId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000271:
                for (int i = 0; i < AddressUtils.shanxiChangzhiName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.shanxiChangzhiName[i]);
                    mGetProvince.setId(AddressUtils.shanxiChangzhiId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000286:
                for (int i = 0; i < AddressUtils.shanxiJinchengName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.shanxiJinchengName[i]);
                    mGetProvince.setId(AddressUtils.shanxiJinchengId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000294:
                for (int i = 0; i < AddressUtils.shanxiShuozhouName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.shanxiShuozhouName[i]);
                    mGetProvince.setId(AddressUtils.shanxiShuozhouId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000302:
                for (int i = 0; i < AddressUtils.shanxiJinzhongName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.shanxiJinzhongName[i]);
                    mGetProvince.setId(AddressUtils.shanxiJinzhongId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000315:
                for (int i = 0; i < AddressUtils.shanxiYunchengName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.shanxiYunchengName[i]);
                    mGetProvince.setId(AddressUtils.shanxiYunchengId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000330:
                for (int i = 0; i < AddressUtils.shanxiXinzhouName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.shanxiXinzhouName[i]);
                    mGetProvince.setId(AddressUtils.shanxiXinzhouId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000346:
                for (int i = 0; i < AddressUtils.shanxiLinfenName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.shanxiLinfenName[i]);
                    mGetProvince.setId(AddressUtils.shanxiLinfenId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000365:
                for (int i = 0; i < AddressUtils.shanxiLvliangName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.shanxiLvliangName[i]);
                    mGetProvince.setId(AddressUtils.shanxiLvliangId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000381:
                for (int i = 0; i < AddressUtils.neimengguHuhehaoteName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.neimengguHuhehaoteName[i]);
                    mGetProvince.setId(AddressUtils.neimengguHuhehaoteId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000392:
                for (int i = 0; i < AddressUtils.neimengguBaotouName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.neimengguBaotouName[i]);
                    mGetProvince.setId(AddressUtils.neimengguBaotouId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000403:
                for (int i = 0; i < AddressUtils.neimengguWuhaiName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.neimengguWuhaiName[i]);
                    mGetProvince.setId(AddressUtils.neimengguWuhaiId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000408:
                for (int i = 0; i < AddressUtils.neimengguChifengName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.neimengguChifengName[i]);
                    mGetProvince.setId(AddressUtils.neimengguChifengId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000422:
                for (int i = 0; i < AddressUtils.neimengguTongliaoName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.neimengguTongliaoName[i]);
                    mGetProvince.setId(AddressUtils.neimengguTongliaoId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000432:
                for (int i = 0; i < AddressUtils.neimengguEerduosiName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.neimengguEerduosiName[i]);
                    mGetProvince.setId(AddressUtils.neimengguEerduosiId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000441:
                for (int i = 0; i < AddressUtils.neimengguHulunbeierName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.neimengguHulunbeierName[i]);
                    mGetProvince.setId(AddressUtils.neimengguHulunbeierId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000456:
                for (int i = 0; i < AddressUtils.neimengguBayanzhuoerName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.neimengguBayanzhuoerName[i]);
                    mGetProvince.setId(AddressUtils.neimengguBayanzhuoerId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000465:
                for (int i = 0; i < AddressUtils.neimengguWulanchabuName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.neimengguWulanchabuName[i]);
                    mGetProvince.setId(AddressUtils.neimengguWulanchabuId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000478:
                for (int i = 0; i < AddressUtils.neimengguXinganName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.neimengguXinganName[i]);
                    mGetProvince.setId(AddressUtils.neimengguXinganId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000485:
                for (int i = 0; i < AddressUtils.neimengguXilinguoleName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.neimengguXilinguoleName[i]);
                    mGetProvince.setId(AddressUtils.neimengguXilinguoleId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000498:
                for (int i = 0; i < AddressUtils.neimengguAlashanName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.neimengguAlashanName[i]);
                    mGetProvince.setId(AddressUtils.neimengguAlashanId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109003079:
                for (int i = 0; i < AddressUtils.shanbeiXianName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.shanbeiXianName[i]);
                    mGetProvince.setId(AddressUtils.shanbeiXianId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109003094:
                for (int i = 0; i < AddressUtils.shanbeiTongchuanName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.shanbeiTongchuanName[i]);
                    mGetProvince.setId(AddressUtils.shanbeiTongchuanId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109003100:
                for (int i = 0; i < AddressUtils.shanbeiBaojiName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.shanbeiBaojiName[i]);
                    mGetProvince.setId(AddressUtils.shanbeiBaojiId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109003114:
                for (int i = 0; i < AddressUtils.shanbeiXianyangName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.shanbeiXianyangName[i]);
                    mGetProvince.setId(AddressUtils.shanbeiXianyangId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109003130:
                for (int i = 0; i < AddressUtils.shanbeiWeinanName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.shanbeiWeinanName[i]);
                    mGetProvince.setId(AddressUtils.shanbeiWeinanId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109003143:
                for (int i = 0; i < AddressUtils.shanbeiYananName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.shanbeiYananName[i]);
                    mGetProvince.setId(AddressUtils.shanbeiYananId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109003158:
                for (int i = 0; i < AddressUtils.shanbeiHanzhongName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.shanbeiHanzhongName[i]);
                    mGetProvince.setId(AddressUtils.shanbeiHanzhongId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109003171:
                for (int i = 0; i < AddressUtils.shanbeiYulinName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.shanbeiYulinName[i]);
                    mGetProvince.setId(AddressUtils.shanbeiYulinId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109003185:
                for (int i = 0; i < AddressUtils.shanbeiAnkangName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.shanbeiAnkangName[i]);
                    mGetProvince.setId(AddressUtils.shanbeiAnkangId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109003197:
                for (int i = 0; i < AddressUtils.shanbeiShangluoName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.shanbeiShangluoName[i]);
                    mGetProvince.setId(AddressUtils.shanbeiShangluoId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000023:
                for (int i = 0; i < AddressUtils.tianjinShiquName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.tianjinShiquName[i]);
                    mGetProvince.setId(AddressUtils.tianjinShiquId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000039:
                for (int i = 0; i < AddressUtils.tianjinXianquName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.tianjinXianquName[i]);
                    mGetProvince.setId(AddressUtils.tianjinXianquId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001500:
                for (int i = 0; i < AddressUtils.shandongJinanName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.shandongJinanName[i]);
                    mGetProvince.setId(AddressUtils.shandongJinanId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001512:
                for (int i = 0; i < AddressUtils.shandongQingdaoName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.shandongQingdaoName[i]);
                    mGetProvince.setId(AddressUtils.shandongQingdaoId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001526:
                for (int i = 0; i < AddressUtils.shandongZiboName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.shandongZiboName[i]);
                    mGetProvince.setId(AddressUtils.shandongZiboId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001536:
                for (int i = 0; i < AddressUtils.shandongZaozhuangName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.shandongZaozhuangName[i]);
                    mGetProvince.setId(AddressUtils.shandongZaozhuangId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001544:
                for (int i = 0; i < AddressUtils.shandongDongyingName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.shandongDongyingName[i]);
                    mGetProvince.setId(AddressUtils.shandongDongyingId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001551:
                for (int i = 0; i < AddressUtils.shandongYantaiName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.shandongYantaiName[i]);
                    mGetProvince.setId(AddressUtils.shandongYantaiId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001565:
                for (int i = 0; i < AddressUtils.shandongWeifangName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.shandongWeifangName[i]);
                    mGetProvince.setId(AddressUtils.shandongWeifangId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001579:
                for (int i = 0; i < AddressUtils.shandongJiningName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.shandongJiningName[i]);
                    mGetProvince.setId(AddressUtils.shandongJiningId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001593:
                for (int i = 0; i < AddressUtils.shandongTaianName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.shandongTaianName[i]);
                    mGetProvince.setId(AddressUtils.shandongTaianId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001601:
                for (int i = 0; i < AddressUtils.shandongWeihaiName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.shandongWeihaiName[i]);
                    mGetProvince.setId(AddressUtils.shandongWeihaiId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001607:
                for (int i = 0; i < AddressUtils.shandongRizhaoName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.shandongRizhaoName[i]);
                    mGetProvince.setId(AddressUtils.shandongRizhaoId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001613:
                for (int i = 0; i < AddressUtils.shandongLaiwuName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.shandongLaiwuName[i]);
                    mGetProvince.setId(AddressUtils.shandongLaiwuId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001617:
                for (int i = 0; i < AddressUtils.shandongLinyiName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.shandongLinyiName[i]);
                    mGetProvince.setId(AddressUtils.shandongLinyiId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001631:
                for (int i = 0; i < AddressUtils.shandongDezhouName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.shandongDezhouName[i]);
                    mGetProvince.setId(AddressUtils.shandongDezhouId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001644:
                for (int i = 0; i < AddressUtils.shandongLiaochengName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.shandongLiaochengName[i]);
                    mGetProvince.setId(AddressUtils.shandongLiaochengId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001654:
                for (int i = 0; i < AddressUtils.shandongBinzhouName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.shandongBinzhouName[i]);
                    mGetProvince.setId(AddressUtils.shandongBinzhouId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001663:
                for (int i = 0; i < AddressUtils.shandongHezeName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.shandongHezeName[i]);
                    mGetProvince.setId(AddressUtils.shandongHezeId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000044:
                for (int i = 0; i < AddressUtils.hebeiShijiazhuangName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.hebeiShijiazhuangName[i]);
                    mGetProvince.setId(AddressUtils.hebeiShijiazhuangId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000069:
                for (int i = 0; i < AddressUtils.hebeiTangshanName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.hebeiTangshanName[i]);
                    mGetProvince.setId(AddressUtils.hebeiTangshanId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000085:
                for (int i = 0; i < AddressUtils.hebeiQinhuandaoName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.hebeiQinhuandaoName[i]);
                    mGetProvince.setId(AddressUtils.hebeiQinhuandaoId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000094:
                for (int i = 0; i < AddressUtils.hebeiHandanName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.hebeiHandanName[i]);
                    mGetProvince.setId(AddressUtils.hebeiHandanId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000115:
                for (int i = 0; i < AddressUtils.hebeiXingtaiName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.hebeiXingtaiName[i]);
                    mGetProvince.setId(AddressUtils.hebeiXingtaiId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000136:
                for (int i = 0; i < AddressUtils.hebeiBaodingName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.hebeiBaodingName[i]);
                    mGetProvince.setId(AddressUtils.hebeiBaodingId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000163:
                for (int i = 0; i < AddressUtils.hebeiZhangjiakouName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.hebeiZhangjiakouName[i]);
                    mGetProvince.setId(AddressUtils.hebeiZhangjiakouId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000182:
                for (int i = 0; i < AddressUtils.hebeiChengdeName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.hebeiChengdeName[i]);
                    mGetProvince.setId(AddressUtils.hebeiChengdeId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000195:
                for (int i = 0; i < AddressUtils.hebeiCangzhouName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.hebeiCangzhouName[i]);
                    mGetProvince.setId(AddressUtils.hebeiCangzhouId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000213:
                for (int i = 0; i < AddressUtils.hebeiLangfangName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.hebeiLangfangName[i]);
                    mGetProvince.setId(AddressUtils.hebeiLangfangId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000225:
                for (int i = 0; i < AddressUtils.hebeiHengshuiName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.hebeiHengshuiName[i]);
                    mGetProvince.setId(AddressUtils.hebeiHengshuiId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001675:
                for (int i = 0; i < AddressUtils.henanZhengzhouName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.henanZhengzhouName[i]);
                    mGetProvince.setId(AddressUtils.henanZhengzhouId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001689:
                for (int i = 0; i < AddressUtils.henanKaifengName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.henanKaifengName[i]);
                    mGetProvince.setId(AddressUtils.henanKaifengId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001701:
                for (int i = 0; i < AddressUtils.henanLuoyangName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.henanLuoyangName[i]);
                    mGetProvince.setId(AddressUtils.henanLuoyangId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001718:
                for (int i = 0; i < AddressUtils.henanPingdingshanName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.henanPingdingshanName[i]);
                    mGetProvince.setId(AddressUtils.henanPingdingshanId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001730:
                for (int i = 0; i < AddressUtils.henanAnyangName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.henanAnyangName[i]);
                    mGetProvince.setId(AddressUtils.henanAnyangId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001741:
                for (int i = 0; i < AddressUtils.henanHebiName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.henanHebiName[i]);
                    mGetProvince.setId(AddressUtils.henanHebiId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001748:
                for (int i = 0; i < AddressUtils.henanXinxiangName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.henanXinxiangName[i]);
                    mGetProvince.setId(AddressUtils.henanXinxiangId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001762:
                for (int i = 0; i < AddressUtils.henanJiaozuoName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.henanJiaozuoName[i]);
                    mGetProvince.setId(AddressUtils.henanJiaozuoId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001775:
                for (int i = 0; i < AddressUtils.henanPuyangName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.henanPuyangName[i]);
                    mGetProvince.setId(AddressUtils.henanPuyangId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001783:
                for (int i = 0; i < AddressUtils.henanXuchangName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.henanXuchangName[i]);
                    mGetProvince.setId(AddressUtils.henanXuchangId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001791:
                for (int i = 0; i < AddressUtils.henanLuoheName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.henanLuoheName[i]);
                    mGetProvince.setId(AddressUtils.henanLuoheId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001798:
                for (int i = 0; i < AddressUtils.henanSanmenxiaName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.henanSanmenxiaName[i]);
                    mGetProvince.setId(AddressUtils.henanSanmenxiaId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001806:
                for (int i = 0; i < AddressUtils.henanNanyangName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.henanNanyangName[i]);
                    mGetProvince.setId(AddressUtils.henanNanyangId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001821:
                for (int i = 0; i < AddressUtils.henanShangqiuName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.henanShangqiuName[i]);
                    mGetProvince.setId(AddressUtils.henanShangqiuId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001832:
                for (int i = 0; i < AddressUtils.henanXinyangName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.henanXinyangName[i]);
                    mGetProvince.setId(AddressUtils.henanXinyangId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001844:
                for (int i = 0; i < AddressUtils.henanZhoukouName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.henanZhoukouName[i]);
                    mGetProvince.setId(AddressUtils.henanZhoukouId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001856:
                for (int i = 0; i < AddressUtils.henanZhumadianName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.henanZhumadianName[i]);
                    mGetProvince.setId(AddressUtils.henanZhumadianId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109003405:
                for (int i = 0; i < AddressUtils.xinjiangWulumuqiName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.xinjiangWulumuqiName[i]);
                    mGetProvince.setId(AddressUtils.xinjiangWulumuqiId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109003415:
                for (int i = 0; i < AddressUtils.xinjiangKelamayiName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.xinjiangKelamayiName[i]);
                    mGetProvince.setId(AddressUtils.xinjiangKelamayiId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109003421:
                for (int i = 0; i < AddressUtils.xinjiangTulufanName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.xinjiangTulufanName[i]);
                    mGetProvince.setId(AddressUtils.xinjiangTulufanId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109003425:
                for (int i = 0; i < AddressUtils.xinjiangHamiName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.xinjiangHamiName[i]);
                    mGetProvince.setId(AddressUtils.xinjiangHamiId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109003429:
                for (int i = 0; i < AddressUtils.xinjiangChangjiName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.xinjiangChangjiName[i]);
                    mGetProvince.setId(AddressUtils.xinjiangChangjiId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109003438:
                for (int i = 0; i < AddressUtils.xinjiangBoertalaName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.xinjiangBoertalaName[i]);
                    mGetProvince.setId(AddressUtils.xinjiangBoertalaId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109003442:
                for (int i = 0; i < AddressUtils.xinjiangBayinguolengName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.xinjiangBayinguolengName[i]);
                    mGetProvince.setId(AddressUtils.xinjiangBayinguolengId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109003452:
                for (int i = 0; i < AddressUtils.xinjiangAkesuName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.xinjiangAkesuName[i]);
                    mGetProvince.setId(AddressUtils.xinjiangAkesuId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109003462:
                for (int i = 0; i < AddressUtils.xinjiangKezilesuName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.xinjiangKezilesuName[i]);
                    mGetProvince.setId(AddressUtils.xinjiangKezilesuId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109003467:
                for (int i = 0; i < AddressUtils.xinjiangKashiName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.xinjiangKashiName[i]);
                    mGetProvince.setId(AddressUtils.xinjiangKashiId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109003480:
                for (int i = 0; i < AddressUtils.xinjiangHetianName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.xinjiangHetianName[i]);
                    mGetProvince.setId(AddressUtils.xinjiangHetianId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109003489:
                for (int i = 0; i < AddressUtils.xinjiangYiliName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.xinjiangYiliName[i]);
                    mGetProvince.setId(AddressUtils.xinjiangYiliId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109003500:
                for (int i = 0; i < AddressUtils.xinjiangTachengName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.xinjiangTachengName[i]);
                    mGetProvince.setId(AddressUtils.xinjiangTachengId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109003508:
                for (int i = 0; i < AddressUtils.xinjiangAletaiName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.xinjiangAletaiName[i]);
                    mGetProvince.setId(AddressUtils.xinjiangAletaiId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109003516:
                for (int i = 0; i < AddressUtils.xinjiangShengzhixiangName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.xinjiangShengzhixiangName[i]);
                    mGetProvince.setId(AddressUtils.xinjiangShengzhixiangId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001869:
                for (int i = 0; i < AddressUtils.hubeiWuhanName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.hubeiWuhanName[i]);
                    mGetProvince.setId(AddressUtils.hubeiWuhanId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001884:
                for (int i = 0; i < AddressUtils.hubeiHuangshiName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.hubeiHuangshiName[i]);
                    mGetProvince.setId(AddressUtils.hubeiHuangshiId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001892:
                for (int i = 0; i < AddressUtils.hubeiShiyanName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.hubeiShiyanName[i]);
                    mGetProvince.setId(AddressUtils.hubeiShiyanId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001902:
                for (int i = 0; i < AddressUtils.hubeiYichangName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.hubeiYichangName[i]);
                    mGetProvince.setId(AddressUtils.hubeiYichangId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001917:
                for (int i = 0; i < AddressUtils.hubeiXiangyangName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.hubeiXiangyangName[i]);
                    mGetProvince.setId(AddressUtils.hubeiXiangyangId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001928:
                for (int i = 0; i < AddressUtils.hubeiEzhouName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.hubeiEzhouName[i]);
                    mGetProvince.setId(AddressUtils.hubeiEzhouId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001933:
                for (int i = 0; i < AddressUtils.hubeiJingmenName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.hubeiJingmenName[i]);
                    mGetProvince.setId(AddressUtils.hubeiJingmenId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001940:
                for (int i = 0; i < AddressUtils.hubeiXiaoganName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.hubeiXiaoganName[i]);
                    mGetProvince.setId(AddressUtils.hubeiXiaoganId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001949:
                for (int i = 0; i < AddressUtils.hubeiJingzhouName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.hubeiJingzhouName[i]);
                    mGetProvince.setId(AddressUtils.hubeiJingzhouId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001959:
                for (int i = 0; i < AddressUtils.hubeiHuanggangName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.hubeiHuanggangName[i]);
                    mGetProvince.setId(AddressUtils.hubeiHuanggangId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001971:
                for (int i = 0; i < AddressUtils.hubeiXianningName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.hubeiXianningName[i]);
                    mGetProvince.setId(AddressUtils.hubeiXianningId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001979:
                for (int i = 0; i < AddressUtils.hubeiSuizhouName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.hubeiSuizhouName[i]);
                    mGetProvince.setId(AddressUtils.hubeiSuizhouId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001983:
                for (int i = 0; i < AddressUtils.hubeiEnshiName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.hubeiEnshiName[i]);
                    mGetProvince.setId(AddressUtils.hubeiEnshiId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001992:
                for (int i = 0; i < AddressUtils.hubeiShengzhixiaName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.hubeiShengzhixiaName[i]);
                    mGetProvince.setId(AddressUtils.hubeiShengzhixiaId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001998:
                for (int i = 0; i < AddressUtils.hunanChangshaName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.hunanChangshaName[i]);
                    mGetProvince.setId(AddressUtils.hunanChangshaId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002009:
                for (int i = 0; i < AddressUtils.hunanZhuzhouName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.hunanZhuzhouName[i]);
                    mGetProvince.setId(AddressUtils.hunanZhuzhouId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002020:
                for (int i = 0; i < AddressUtils.hunanXiangtanName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.hunanXiangtanName[i]);
                    mGetProvince.setId(AddressUtils.hunanXiangtanId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002027:
                for (int i = 0; i < AddressUtils.hunanHengyangName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.hunanHengyangName[i]);
                    mGetProvince.setId(AddressUtils.hunanHengyangId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002041:
                for (int i = 0; i < AddressUtils.hunanShaoyangName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.hunanShaoyangName[i]);
                    mGetProvince.setId(AddressUtils.hunanShaoyangId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002055:
                for (int i = 0; i < AddressUtils.hunanYueyangName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.hunanYueyangName[i]);
                    mGetProvince.setId(AddressUtils.hunanYueyangId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002066:
                for (int i = 0; i < AddressUtils.hunanChangdeName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.hunanChangdeName[i]);
                    mGetProvince.setId(AddressUtils.hunanChangdeId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002077:
                for (int i = 0; i < AddressUtils.hunanZhangjiajieName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.hunanZhangjiajieName[i]);
                    mGetProvince.setId(AddressUtils.hunanZhangjiajieId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002083:
                for (int i = 0; i < AddressUtils.hunanYiyangName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.hunanYiyangName[i]);
                    mGetProvince.setId(AddressUtils.hunanYiyangId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002091:
                for (int i = 0; i < AddressUtils.hunanBinzhouName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.hunanBinzhouName[i]);
                    mGetProvince.setId(AddressUtils.hunanBinzhouId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002104:
                for (int i = 0; i < AddressUtils.hunanYongzhouName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.hunanYongzhouName[i]);
                    mGetProvince.setId(AddressUtils.hunanYongzhouId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002117:
                for (int i = 0; i < AddressUtils.hunanHuaihuaName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.hunanHuaihuaName[i]);
                    mGetProvince.setId(AddressUtils.hunanHuaihuaId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002131:
                for (int i = 0; i < AddressUtils.hunanLoudiName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.hunanLoudiName[i]);
                    mGetProvince.setId(AddressUtils.hunanLoudiId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002138:
                for (int i = 0; i < AddressUtils.hunanXiangxiName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.hunanXiangxiName[i]);
                    mGetProvince.setId(AddressUtils.hunanXiangxiId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000888:
                for (int i = 0; i < AddressUtils.jiangsuNanjingName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.jiangsuNanjingName[i]);
                    mGetProvince.setId(AddressUtils.jiangsuNanjingId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000903:
                for (int i = 0; i < AddressUtils.jiangsuWuxiName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.jiangsuWuxiName[i]);
                    mGetProvince.setId(AddressUtils.jiangsuWuxiId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000913:
                for (int i = 0; i < AddressUtils.jiangsuXuzhouName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.jiangsuXuzhouName[i]);
                    mGetProvince.setId(AddressUtils.jiangsuXuzhouId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000926:
                for (int i = 0; i < AddressUtils.jiangsuChangzhouName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.jiangsuChangzhouName[i]);
                    mGetProvince.setId(AddressUtils.jiangsuChangzhouId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000935:
                for (int i = 0; i < AddressUtils.jiangsuSuzhouName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.jiangsuSuzhouName[i]);
                    mGetProvince.setId(AddressUtils.jiangsuSuzhouId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000948:
                for (int i = 0; i < AddressUtils.jiangsuNantongName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.jiangsuNantongName[i]);
                    mGetProvince.setId(AddressUtils.jiangsuNantongId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000958:
                for (int i = 0; i < AddressUtils.jiangsuLianyungangName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.jiangsuLianyungangName[i]);
                    mGetProvince.setId(AddressUtils.jiangsuLianyungangId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000967:
                for (int i = 0; i < AddressUtils.jiangsuHuaianName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.jiangsuHuaianName[i]);
                    mGetProvince.setId(AddressUtils.jiangsuHuaianId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000977:
                for (int i = 0; i < AddressUtils.jiangsuYanchengName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.jiangsuYanchengName[i]);
                    mGetProvince.setId(AddressUtils.jiangsuYanchengId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000988:
                for (int i = 0; i < AddressUtils.jiangsuYangzhouName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.jiangsuYangzhouName[i]);
                    mGetProvince.setId(AddressUtils.jiangsuYangzhouId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000997:
                for (int i = 0; i < AddressUtils.jiangsuZhenjiangName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.jiangsuZhenjiangName[i]);
                    mGetProvince.setId(AddressUtils.jiangsuZhenjiangId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001005:
                for (int i = 0; i < AddressUtils.jiangsuTaizhouName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.jiangsuTaizhouName[i]);
                    mGetProvince.setId(AddressUtils.jiangsuTaizhouId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001013:
                for (int i = 0; i < AddressUtils.jiangsuSuqianName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.jiangsuSuqianName[i]);
                    mGetProvince.setId(AddressUtils.jiangsuSuqianId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109003373:
                for (int i = 0; i < AddressUtils.ningxiaYinchuanName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.ningxiaYinchuanName[i]);
                    mGetProvince.setId(AddressUtils.ningxiaYinchuanId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109003381:
                for (int i = 0; i < AddressUtils.ningxiaShizuishanName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.ningxiaShizuishanName[i]);
                    mGetProvince.setId(AddressUtils.ningxiaShizuishanId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109003386:
                for (int i = 0; i < AddressUtils.ningxiaWuzhongName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.ningxiaWuzhongName[i]);
                    mGetProvince.setId(AddressUtils.ningxiaWuzhongId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109003392:
                for (int i = 0; i < AddressUtils.ningxiaGuyuanName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.ningxiaGuyuanName[i]);
                    mGetProvince.setId(AddressUtils.ningxiaGuyuanId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109003399:
                for (int i = 0; i < AddressUtils.ningxiaZhongweiName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.ningxiaZhongweiName[i]);
                    mGetProvince.setId(AddressUtils.ningxiaZhongweiId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002521:
                for (int i = 0; i < AddressUtils.sichuanChengduName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.sichuanChengduName[i]);
                    mGetProvince.setId(AddressUtils.sichuanChengduId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002542:
                for (int i = 0; i < AddressUtils.sichuanZigongName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.sichuanZigongName[i]);
                    mGetProvince.setId(AddressUtils.sichuanZigongId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002550:
                for (int i = 0; i < AddressUtils.sichuanPanzhihuaName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.sichuanPanzhihuaName[i]);
                    mGetProvince.setId(AddressUtils.sichuanPanzhihuaId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002557:
                for (int i = 0; i < AddressUtils.sichuanLuzhouName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.sichuanLuzhouName[i]);
                    mGetProvince.setId(AddressUtils.sichuanLuzhouId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002566:
                for (int i = 0; i < AddressUtils.sichuanDeyangName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.sichuanDeyangName[i]);
                    mGetProvince.setId(AddressUtils.sichuanDeyangId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002574:
                for (int i = 0; i < AddressUtils.sichuanMianyangName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.sichuanMianyangName[i]);
                    mGetProvince.setId(AddressUtils.sichuanMianyangId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002585:
                for (int i = 0; i < AddressUtils.sichuanGuangyuanName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.sichuanGuangyuanName[i]);
                    mGetProvince.setId(AddressUtils.sichuanGuangyuanId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002594:
                for (int i = 0; i < AddressUtils.sichuanSuiningName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.sichuanSuiningName[i]);
                    mGetProvince.setId(AddressUtils.sichuanSuiningId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002601:
                for (int i = 0; i < AddressUtils.sichuanNeijiangName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.sichuanNeijiangName[i]);
                    mGetProvince.setId(AddressUtils.sichuanNeijiangId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002608:
                for (int i = 0; i < AddressUtils.sichuanLeshanName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.sichuanLeshanName[i]);
                    mGetProvince.setId(AddressUtils.sichuanLeshanId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002621:
                for (int i = 0; i < AddressUtils.sichuanNanchongName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.sichuanNanchongName[i]);
                    mGetProvince.setId(AddressUtils.sichuanNanchongId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002632:
                for (int i = 0; i < AddressUtils.sichuanMeishanName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.sichuanMeishanName[i]);
                    mGetProvince.setId(AddressUtils.sichuanMeishanId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002640:
                for (int i = 0; i < AddressUtils.sichuanYibinName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.sichuanYibinName[i]);
                    mGetProvince.setId(AddressUtils.sichuanYibinId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002652:
                for (int i = 0; i < AddressUtils.sichuanGuanganName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.sichuanGuanganName[i]);
                    mGetProvince.setId(AddressUtils.sichuanGuanganId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002659:
                for (int i = 0; i < AddressUtils.sichuanDazhouName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.sichuanDazhouName[i]);
                    mGetProvince.setId(AddressUtils.sichuanDazhouId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002668:
                for (int i = 0; i < AddressUtils.sichuanYaanName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.sichuanYaanName[i]);
                    mGetProvince.setId(AddressUtils.sichuanYaanId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002678:
                for (int i = 0; i < AddressUtils.sichuanBazhongName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.sichuanBazhongName[i]);
                    mGetProvince.setId(AddressUtils.sichuanBazhongId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002684:
                for (int i = 0; i < AddressUtils.sichuanZiyangName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.sichuanZiyangName[i]);
                    mGetProvince.setId(AddressUtils.sichuanZiyangId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002690:
                for (int i = 0; i < AddressUtils.sichuanAbeiName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.sichuanAbeiName[i]);
                    mGetProvince.setId(AddressUtils.sichuanAbeiId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002704:
                for (int i = 0; i < AddressUtils.sichuanGanziName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.sichuanGanziName[i]);
                    mGetProvince.setId(AddressUtils.sichuanGanziId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002723:
                for (int i = 0; i < AddressUtils.sichuanLiangshanName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.sichuanLiangshanName[i]);
                    mGetProvince.setId(AddressUtils.sichuanLiangshanId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001134:
                for (int i = 0; i < AddressUtils.anhuiHefenName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.anhuiHefenName[i]);
                    mGetProvince.setId(AddressUtils.anhuiHefenId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001143:
                for (int i = 0; i < AddressUtils.anhuiWuhuName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.anhuiWuhuName[i]);
                    mGetProvince.setId(AddressUtils.anhuiWuhuId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001152:
                for (int i = 0; i < AddressUtils.anhuiBangbuName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.anhuiBangbuName[i]);
                    mGetProvince.setId(AddressUtils.anhuiBangbuId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001161:
                for (int i = 0; i < AddressUtils.anhuiHuainanName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.anhuiHuainanName[i]);
                    mGetProvince.setId(AddressUtils.anhuiHuainanId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001169:
                for (int i = 0; i < AddressUtils.anhuiMaanshanName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.anhuiMaanshanName[i]);
                    mGetProvince.setId(AddressUtils.anhuiMaanshanId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001175:
                for (int i = 0; i < AddressUtils.anhuiHuaibeiName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.anhuiHuaibeiName[i]);
                    mGetProvince.setId(AddressUtils.anhuiHuaibeiId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001181:
                for (int i = 0; i < AddressUtils.anhuiTonglingName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.anhuiTonglingName[i]);
                    mGetProvince.setId(AddressUtils.anhuiTonglingId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001187:
                for (int i = 0; i < AddressUtils.anhuiAnqingName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.anhuiAnqingName[i]);
                    mGetProvince.setId(AddressUtils.anhuiAnqingId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001200:
                for (int i = 0; i < AddressUtils.anhuiHuanshanName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.anhuiHuanshanName[i]);
                    mGetProvince.setId(AddressUtils.anhuiHuanshanId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001209:
                for (int i = 0; i < AddressUtils.anhuiChuzhouName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.anhuiChuzhouName[i]);
                    mGetProvince.setId(AddressUtils.anhuiChuzhouId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001219:
                for (int i = 0; i < AddressUtils.anhuiBuyangName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.anhuiBuyangName[i]);
                    mGetProvince.setId(AddressUtils.anhuiBuyangId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001229:
                for (int i = 0; i < AddressUtils.anhuiSuzhouName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.anhuiSuzhouName[i]);
                    mGetProvince.setId(AddressUtils.anhuiSuzhouId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001236:
                for (int i = 0; i < AddressUtils.anhuiChaohuName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.anhuiChaohuName[i]);
                    mGetProvince.setId(AddressUtils.anhuiChaohuId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001243:
                for (int i = 0; i < AddressUtils.anhuiLiuanName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.anhuiLiuanName[i]);
                    mGetProvince.setId(AddressUtils.anhuiLiuanId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001252:
                for (int i = 0; i < AddressUtils.anhuiHaozhouName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.anhuiHaozhouName[i]);
                    mGetProvince.setId(AddressUtils.anhuiHaozhouId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001258:
                for (int i = 0; i < AddressUtils.anhuiChizhouName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.anhuiChizhouName[i]);
                    mGetProvince.setId(AddressUtils.anhuiChizhouId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001264:
                for (int i = 0; i < AddressUtils.anhuiXuanchengName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.anhuiXuanchengName[i]);
                    mGetProvince.setId(AddressUtils.anhuiXuanchengId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001274:
                for (int i = 0; i < AddressUtils.fujianFuzhouName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.fujianFuzhouName[i]);
                    mGetProvince.setId(AddressUtils.fujianFuzhouId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001289:
                for (int i = 0; i < AddressUtils.fujianXiamenName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.fujianXiamenName[i]);
                    mGetProvince.setId(AddressUtils.fujianXiamenId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001297:
                for (int i = 0; i < AddressUtils.fujianPutianName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.fujianPutianName[i]);
                    mGetProvince.setId(AddressUtils.fujianPutianId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001304:
                for (int i = 0; i < AddressUtils.fujianSanmingName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.fujianSanmingName[i]);
                    mGetProvince.setId(AddressUtils.fujianSanmingId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001318:
                for (int i = 0; i < AddressUtils.fujianQuanzhouName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.fujianQuanzhouName[i]);
                    mGetProvince.setId(AddressUtils.fujianQuanzhouId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001332:
                for (int i = 0; i < AddressUtils.fujianZhangzhouName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.fujianZhangzhouName[i]);
                    mGetProvince.setId(AddressUtils.fujianZhangzhouId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001345:
                for (int i = 0; i < AddressUtils.fujianNanpingName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.fujianNanpingName[i]);
                    mGetProvince.setId(AddressUtils.fujianNanpingId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001357:
                for (int i = 0; i < AddressUtils.fujianLongyanName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.fujianLongyanName[i]);
                    mGetProvince.setId(AddressUtils.fujianLongyanId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001366:
                for (int i = 0; i < AddressUtils.fujianNingdeName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.fujianNingdeName[i]);
                    mGetProvince.setId(AddressUtils.fujianNingdeId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001378:
                for (int i = 0; i < AddressUtils.jiangxiJingdezhenName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.jiangxiJingdezhenName[i]);
                    mGetProvince.setId(AddressUtils.jiangxiJingdezhenId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001389:
                for (int i = 0; i < AddressUtils.jiangxiNanchangName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.jiangxiNanchangName[i]);
                    mGetProvince.setId(AddressUtils.jiangxiNanchangId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001395:
                for (int i = 0; i < AddressUtils.jiangxiPinngxiangName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.jiangxiPinngxiangName[i]);
                    mGetProvince.setId(AddressUtils.jiangxiPinngxiangId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001402:
                for (int i = 0; i < AddressUtils.jiangxiJiujiangName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.jiangxiJiujiangName[i]);
                    mGetProvince.setId(AddressUtils.jiangxiJiujiangId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001416:
                for (int i = 0; i < AddressUtils.jiangxiXinyuName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.jiangxiXinyuName[i]);
                    mGetProvince.setId(AddressUtils.jiangxiXinyuId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001420:
                for (int i = 0; i < AddressUtils.jiangxiYingtanName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.jiangxiYingtanName[i]);
                    mGetProvince.setId(AddressUtils.jiangxiYingtanId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001425:
                for (int i = 0; i < AddressUtils.jiangxiGanzhouName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.jiangxiGanzhouName[i]);
                    mGetProvince.setId(AddressUtils.jiangxiGanzhouId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001445:
                for (int i = 0; i < AddressUtils.jiangxiJianName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.jiangxiJianName[i]);
                    mGetProvince.setId(AddressUtils.jiangxiJianId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001460:
                for (int i = 0; i < AddressUtils.jiangxiYichunName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.jiangxiYichunName[i]);
                    mGetProvince.setId(AddressUtils.jiangxiYichunId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001472:
                for (int i = 0; i < AddressUtils.jiangxiFuzhouName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.jiangxiFuzhouName[i]);
                    mGetProvince.setId(AddressUtils.jiangxiFuzhouId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001485:
                for (int i = 0; i < AddressUtils.jiangxiShangraoName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.jiangxiShangraoName[i]);
                    mGetProvince.setId(AddressUtils.jiangxiShangraoId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109003207:
                for (int i = 0; i < AddressUtils.gansuLanzhouName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.gansuLanzhouName[i]);
                    mGetProvince.setId(AddressUtils.gansuLanzhouId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109003217:
                for (int i = 0; i < AddressUtils.gansuJiayuguanName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.gansuJiayuguanName[i]);
                    mGetProvince.setId(AddressUtils.gansuJiayuguanId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109003219:
                for (int i = 0; i < AddressUtils.gansuJinchangName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.gansuJinchangName[i]);
                    mGetProvince.setId(AddressUtils.gansuJinchangId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109003223:
                for (int i = 0; i < AddressUtils.gansuBaiyinName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.gansuBaiyinName[i]);
                    mGetProvince.setId(AddressUtils.gansuBaiyinId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109003230:
                for (int i = 0; i < AddressUtils.gansuTianshuiName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.gansuTianshuiName[i]);
                    mGetProvince.setId(AddressUtils.gansuTianshuiId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109003239:
                for (int i = 0; i < AddressUtils.gansuWeiwuName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.gansuWeiwuName[i]);
                    mGetProvince.setId(AddressUtils.gansuWeiwuId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109003245:
                for (int i = 0; i < AddressUtils.gansuZhangyeName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.gansuZhangyeName[i]);
                    mGetProvince.setId(AddressUtils.gansuZhangyeId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109003253:
                for (int i = 0; i < AddressUtils.gansuPingliangName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.gansuPingliangName[i]);
                    mGetProvince.setId(AddressUtils.gansuPingliangId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109003262:
                for (int i = 0; i < AddressUtils.gansuJiuquanName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.gansuJiuquanName[i]);
                    mGetProvince.setId(AddressUtils.gansuJiuquanId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109003271:
                for (int i = 0; i < AddressUtils.gansuQingyangName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.gansuQingyangName[i]);
                    mGetProvince.setId(AddressUtils.gansuQingyangId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109003281:
                for (int i = 0; i < AddressUtils.gansuDingxiName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.gansuDingxiName[i]);
                    mGetProvince.setId(AddressUtils.gansuDingxiId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109003290:
                for (int i = 0; i < AddressUtils.gansuLongnanName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.gansuLongnanName[i]);
                    mGetProvince.setId(AddressUtils.gansuLongnanId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109003301:
                for (int i = 0; i < AddressUtils.gansuLinxiaName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.gansuLinxiaName[i]);
                    mGetProvince.setId(AddressUtils.gansuLinxiaId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109003310:
                for (int i = 0; i < AddressUtils.gansuGannanName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.gansuGannanName[i]);
                    mGetProvince.setId(AddressUtils.gansuGannanId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109003320:
                for (int i = 0; i < AddressUtils.qinghaiXiningName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.qinghaiXiningName[i]);
                    mGetProvince.setId(AddressUtils.qinghaiXiningId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109003329:
                for (int i = 0; i < AddressUtils.qinghaiHaidongName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.qinghaiHaidongName[i]);
                    mGetProvince.setId(AddressUtils.qinghaiHaidongId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109003336:
                for (int i = 0; i < AddressUtils.qinghaiHaibeiName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.qinghaiHaibeiName[i]);
                    mGetProvince.setId(AddressUtils.qinghaiHaibeiId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109003341:
                for (int i = 0; i < AddressUtils.qinghaiHuannanName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.qinghaiHuannanName[i]);
                    mGetProvince.setId(AddressUtils.qinghaiHuannanId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109003346:
                for (int i = 0; i < AddressUtils.qinghaiHainanName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.qinghaiHainanName[i]);
                    mGetProvince.setId(AddressUtils.qinghaiHainanId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109003352:
                for (int i = 0; i < AddressUtils.qinghaiGuoluoName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.qinghaiGuoluoName[i]);
                    mGetProvince.setId(AddressUtils.qinghaiGuoluoId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109003359:
                for (int i = 0; i < AddressUtils.qinghaiYushuName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.qinghaiYushuName[i]);
                    mGetProvince.setId(AddressUtils.qinghaiYushuId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109003366:
                for (int i = 0; i < AddressUtils.qinghaiHaixiName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.qinghaiHaixiName[i]);
                    mGetProvince.setId(AddressUtils.qinghaiHaixiId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002148:
                for (int i = 0; i < AddressUtils.guangdongGuangzhouName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.guangdongGuangzhouName[i]);
                    mGetProvince.setId(AddressUtils.guangdongGuangzhouId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002162:
                for (int i = 0; i < AddressUtils.guangdongShaoguanName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.guangdongShaoguanName[i]);
                    mGetProvince.setId(AddressUtils.guangdongShaoguanId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002174:
                for (int i = 0; i < AddressUtils.guangdongShenzhenName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.guangdongShenzhenName[i]);
                    mGetProvince.setId(AddressUtils.guangdongShenzhenId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002182:
                for (int i = 0; i < AddressUtils.guangdongZhuhaiName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.guangdongZhuhaiName[i]);
                    mGetProvince.setId(AddressUtils.guangdongZhuhaiId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002187:
                for (int i = 0; i < AddressUtils.guangdongShantouName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.guangdongShantouName[i]);
                    mGetProvince.setId(AddressUtils.guangdongShantouId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002196:
                for (int i = 0; i < AddressUtils.guangdongFoshanName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.guangdongFoshanName[i]);
                    mGetProvince.setId(AddressUtils.guangdongFoshanId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002203:
                for (int i = 0; i < AddressUtils.guangdongJiangmenName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.guangdongJiangmenName[i]);
                    mGetProvince.setId(AddressUtils.guangdongJiangmenId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002212:
                for (int i = 0; i < AddressUtils.guangdongZhanjiangName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.guangdongZhanjiangName[i]);
                    mGetProvince.setId(AddressUtils.guangdongZhanjiangId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002223:
                for (int i = 0; i < AddressUtils.guangdongMaomingName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.guangdongMaomingName[i]);
                    mGetProvince.setId(AddressUtils.guangdongMaomingId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002231:
                for (int i = 0; i < AddressUtils.guangdongZhaoqingName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.guangdongZhaoqingName[i]);
                    mGetProvince.setId(AddressUtils.guangdongZhaoqingId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002241:
                for (int i = 0; i < AddressUtils.guangdongHuizhouName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.guangdongHuizhouName[i]);
                    mGetProvince.setId(AddressUtils.guangdongHuizhouId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002248:
                for (int i = 0; i < AddressUtils.guangdongMeizhouName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.guangdongMeizhouName[i]);
                    mGetProvince.setId(AddressUtils.guangdongMeizhouId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002258:
                for (int i = 0; i < AddressUtils.guangdongShanweiName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.guangdongShanweiName[i]);
                    mGetProvince.setId(AddressUtils.guangdongShanweiId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002264:
                for (int i = 0; i < AddressUtils.guangdongHeyuanName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.guangdongHeyuanName[i]);
                    mGetProvince.setId(AddressUtils.guangdongHeyuanId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002272:
                for (int i = 0; i < AddressUtils.guangdongYangjiangName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.guangdongYangjiangName[i]);
                    mGetProvince.setId(AddressUtils.guangdongYangjiangId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002278:
                for (int i = 0; i < AddressUtils.guangdongQingyuanName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.guangdongQingyuanName[i]);
                    mGetProvince.setId(AddressUtils.guangdongQingyuanId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002288:
                for (int i = 0; i < AddressUtils.guangdongDongguanName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.guangdongDongguanName[i]);
                    mGetProvince.setId(AddressUtils.guangdongDongguanId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002289:
                for (int i = 0; i < AddressUtils.guangdongZhongshanName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.guangdongZhongshanName[i]);
                    mGetProvince.setId(AddressUtils.guangdongZhongshanId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002290:
                for (int i = 0; i < AddressUtils.guangdongChaozhouName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.guangdongChaozhouName[i]);
                    mGetProvince.setId(AddressUtils.guangdongChaozhouId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002295:
                for (int i = 0; i < AddressUtils.guangdongJieyangName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.guangdongJieyangName[i]);
                    mGetProvince.setId(AddressUtils.guangdongJieyangId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002302:
                for (int i = 0; i < AddressUtils.guangdongYunfuName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.guangdongYunfuName[i]);
                    mGetProvince.setId(AddressUtils.guangdongYunfuId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000002:
                for (int i = 0; i < AddressUtils.beijingShiquName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.beijingShiquName[i]);
                    mGetProvince.setId(AddressUtils.beijingShiquId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000019:
                for (int i = 0; i < AddressUtils.beijingXianquName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.beijingXianquName[i]);
                    mGetProvince.setId(AddressUtils.beijingXianquId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002742:
                for (int i = 0; i < AddressUtils.guizhouGuiyangName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.guizhouGuiyangName[i]);
                    mGetProvince.setId(AddressUtils.guizhouGuiyangId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002754:
                for (int i = 0; i < AddressUtils.guizhouLiupanshuiName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.guizhouLiupanshuiName[i]);
                    mGetProvince.setId(AddressUtils.guizhouLiupanshuiId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002759:
                for (int i = 0; i < AddressUtils.guizhouZuiyiName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.guizhouZuiyiName[i]);
                    mGetProvince.setId(AddressUtils.guizhouZuiyiId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002775:
                for (int i = 0; i < AddressUtils.guizhouAnshuiName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.guizhouAnshuiName[i]);
                    mGetProvince.setId(AddressUtils.guizhouAnshuiId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002783:
                for (int i = 0; i < AddressUtils.guizhouTongrenName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.guizhouTongrenName[i]);
                    mGetProvince.setId(AddressUtils.guizhouTongrenId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002794:
                for (int i = 0; i < AddressUtils.guizhouQianxinanName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.guizhouQianxinanName[i]);
                    mGetProvince.setId(AddressUtils.guizhouQianxinanId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002803:
                for (int i = 0; i < AddressUtils.guizhouBijieName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.guizhouBijieName[i]);
                    mGetProvince.setId(AddressUtils.guizhouBijieId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002812:
                for (int i = 0; i < AddressUtils.guizhouQiandongnanName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.guizhouQiandongnanName[i]);
                    mGetProvince.setId(AddressUtils.guizhouQiandongnanId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002829:
                for (int i = 0; i < AddressUtils.guizhouQiannanName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.guizhouQiannanName[i]);
                    mGetProvince.setId(AddressUtils.guizhouQiannanId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002843:
                for (int i = 0; i < AddressUtils.yunnanKuimingName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.yunnanKuimingName[i]);
                    mGetProvince.setId(AddressUtils.yunnanKuimingId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002859:
                for (int i = 0; i < AddressUtils.yunnanQujingName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.yunnanQujingName[i]);
                    mGetProvince.setId(AddressUtils.yunnanQujingId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002870:
                for (int i = 0; i < AddressUtils.yunnanYuxiName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.yunnanYuxiName[i]);
                    mGetProvince.setId(AddressUtils.yunnanYuxiId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002881:
                for (int i = 0; i < AddressUtils.yunnanBaoshanName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.yunnanBaoshanName[i]);
                    mGetProvince.setId(AddressUtils.yunnanBaoshanId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002888:
                for (int i = 0; i < AddressUtils.yunnanZhaotongName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.yunnanZhaotongName[i]);
                    mGetProvince.setId(AddressUtils.yunnanZhaotongId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002901:
                for (int i = 0; i < AddressUtils.yunnanLijiangName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.yunnanLijiangName[i]);
                    mGetProvince.setId(AddressUtils.yunnanLijiangId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002908:
                for (int i = 0; i < AddressUtils.yunnanSimaoName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.yunnanSimaoName[i]);
                    mGetProvince.setId(AddressUtils.yunnanSimaoId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002920:
                for (int i = 0; i < AddressUtils.yunnanLincangName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.yunnanLincangName[i]);
                    mGetProvince.setId(AddressUtils.yunnanLincangId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002930:
                for (int i = 0; i < AddressUtils.yunnanChuxiongName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.yunnanChuxiongName[i]);
                    mGetProvince.setId(AddressUtils.yunnanChuxiongId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002941:
                for (int i = 0; i < AddressUtils.yunnanHongheName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.yunnanHongheName[i]);
                    mGetProvince.setId(AddressUtils.yunnanHongheId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002955:
                for (int i = 0; i < AddressUtils.yunnanWenshanName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.yunnanWenshanName[i]);
                    mGetProvince.setId(AddressUtils.yunnanWenshanId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002964:
                for (int i = 0; i < AddressUtils.yunnanXishuangbannaName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.yunnanXishuangbannaName[i]);
                    mGetProvince.setId(AddressUtils.yunnanXishuangbannaId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002968:
                for (int i = 0; i < AddressUtils.yunnanDaliName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.yunnanDaliName[i]);
                    mGetProvince.setId(AddressUtils.yunnanDaliId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002981:
                for (int i = 0; i < AddressUtils.yunnanDehongName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.yunnanDehongName[i]);
                    mGetProvince.setId(AddressUtils.yunnanDehongId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002987:
                for (int i = 0; i < AddressUtils.yunnanNvjiangName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.yunnanNvjiangName[i]);
                    mGetProvince.setId(AddressUtils.yunnanNvjiangId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002992:
                for (int i = 0; i < AddressUtils.yunnanDiqingName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.yunnanDiqingName[i]);
                    mGetProvince.setId(AddressUtils.yunnanDiqingId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002997:
                for (int i = 0; i < AddressUtils.xizangLasaName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.xizangLasaName[i]);
                    mGetProvince.setId(AddressUtils.xizangLasaId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109003007:
                for (int i = 0; i < AddressUtils.xizangChangduName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.xizangChangduName[i]);
                    mGetProvince.setId(AddressUtils.xizangChangduId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109003019:
                for (int i = 0; i < AddressUtils.xizangShannanName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.xizangShannanName[i]);
                    mGetProvince.setId(AddressUtils.xizangShannanId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109003032:
                for (int i = 0; i < AddressUtils.xizangRikazeName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.xizangRikazeName[i]);
                    mGetProvince.setId(AddressUtils.xizangRikazeId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109003051:
                for (int i = 0; i < AddressUtils.xizangNaquName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.xizangNaquName[i]);
                    mGetProvince.setId(AddressUtils.xizangNaquId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109003062:
                for (int i = 0; i < AddressUtils.xizangAliName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.xizangAliName[i]);
                    mGetProvince.setId(AddressUtils.xizangAliId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109003070:
                for (int i = 0; i < AddressUtils.xizangLinzhiName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.xizangLinzhiName[i]);
                    mGetProvince.setId(AddressUtils.xizangLinzhiId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000866:
                for (int i = 0; i < AddressUtils.shanghaiShiquName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.shanghaiShiquName[i]);
                    mGetProvince.setId(AddressUtils.shanghaiShiquId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000885:
                for (int i = 0; i < AddressUtils.shanghaiXianquName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.shanghaiXianquName[i]);
                    mGetProvince.setId(AddressUtils.shanghaiXianquId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001021:
                for (int i = 0; i < AddressUtils.zhejiangHangzhouName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.zhejiangHangzhouName[i]);
                    mGetProvince.setId(AddressUtils.zhejiangHangzhouId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001036:
                for (int i = 0; i < AddressUtils.zhejiangNingboName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.zhejiangNingboName[i]);
                    mGetProvince.setId(AddressUtils.zhejiangNingboId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001049:
                for (int i = 0; i < AddressUtils.zhejiangWenzhouName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.zhejiangWenzhouName[i]);
                    mGetProvince.setId(AddressUtils.zhejiangWenzhouId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001062:
                for (int i = 0; i < AddressUtils.zhejiangJiaxingName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.zhejiangJiaxingName[i]);
                    mGetProvince.setId(AddressUtils.zhejiangJiaxingId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001071:
                for (int i = 0; i < AddressUtils.zhejiangHuzhouName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.zhejiangHuzhouName[i]);
                    mGetProvince.setId(AddressUtils.zhejiangHuzhouId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001078:
                for (int i = 0; i < AddressUtils.zhejiangShaoxingName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.zhejiangShaoxingName[i]);
                    mGetProvince.setId(AddressUtils.zhejiangShaoxingId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001086:
                for (int i = 0; i < AddressUtils.zhejiangJinhuaName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.zhejiangJinhuaName[i]);
                    mGetProvince.setId(AddressUtils.zhejiangJinhuaId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001097:
                for (int i = 0; i < AddressUtils.zhejiangQuzhouName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.zhejiangQuzhouName[i]);
                    mGetProvince.setId(AddressUtils.zhejiangQuzhouId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001105:
                for (int i = 0; i < AddressUtils.zhejiangZhoushanName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.zhejiangZhoushanName[i]);
                    mGetProvince.setId(AddressUtils.zhejiangZhoushanId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001111:
                for (int i = 0; i < AddressUtils.zhejiangTaizhouName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.zhejiangTaizhouName[i]);
                    mGetProvince.setId(AddressUtils.zhejiangTaizhouId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109001122:
                for (int i = 0; i < AddressUtils.zhejiangLishuiName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.zhejiangLishuiName[i]);
                    mGetProvince.setId(AddressUtils.zhejiangLishuiId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000503:
                for (int i = 0; i < AddressUtils.liaoningShenyangName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.liaoningShenyangName[i]);
                    mGetProvince.setId(AddressUtils.liaoningShenyangId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000518:
                for (int i = 0; i < AddressUtils.liaoningDalianName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.liaoningDalianName[i]);
                    mGetProvince.setId(AddressUtils.liaoningDalianId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000530:
                for (int i = 0; i < AddressUtils.liaoningAnshanName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.liaoningAnshanName[i]);
                    mGetProvince.setId(AddressUtils.liaoningAnshanId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000539:
                for (int i = 0; i < AddressUtils.liaoningFushuiName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.liaoningFushuiName[i]);
                    mGetProvince.setId(AddressUtils.liaoningFushuiId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000548:
                for (int i = 0; i < AddressUtils.liaoningBenxiName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.liaoningBenxiName[i]);
                    mGetProvince.setId(AddressUtils.liaoningBenxiId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000556:
                for (int i = 0; i < AddressUtils.liaoningDandongName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.liaoningDandongName[i]);
                    mGetProvince.setId(AddressUtils.liaoningDandongId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000564:
                for (int i = 0; i < AddressUtils.liaoningJinzhouName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.liaoningJinzhouName[i]);
                    mGetProvince.setId(AddressUtils.liaoningJinzhouId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000573:
                for (int i = 0; i < AddressUtils.liaoningYingkouName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.liaoningYingkouName[i]);
                    mGetProvince.setId(AddressUtils.liaoningYingkouId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000581:
                for (int i = 0; i < AddressUtils.liaoningBuxinName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.liaoningBuxinName[i]);
                    mGetProvince.setId(AddressUtils.liaoningBuxinId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000590:
                for (int i = 0; i < AddressUtils.liaoningLiaoyangName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.liaoningLiaoyangName[i]);
                    mGetProvince.setId(AddressUtils.liaoningLiaoyangId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000599:
                for (int i = 0; i < AddressUtils.liaoningPanjinName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.liaoningPanjinName[i]);
                    mGetProvince.setId(AddressUtils.liaoningPanjinId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000605:
                for (int i = 0; i < AddressUtils.liaoningTielingName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.liaoningTielingName[i]);
                    mGetProvince.setId(AddressUtils.liaoningTielingId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000614:
                for (int i = 0; i < AddressUtils.liaoningChaoyangName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.liaoningChaoyangName[i]);
                    mGetProvince.setId(AddressUtils.liaoningChaoyangId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000623:
                for (int i = 0; i < AddressUtils.liaoningHuludaoName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.liaoningHuludaoName[i]);
                    mGetProvince.setId(AddressUtils.liaoningHuludaoId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000632:
                for (int i = 0; i < AddressUtils.jilinChangchunName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.jilinChangchunName[i]);
                    mGetProvince.setId(AddressUtils.jilinChangchunId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000644:
                for (int i = 0; i < AddressUtils.jilinJilinName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.jilinJilinName[i]);
                    mGetProvince.setId(AddressUtils.jilinJilinId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000655:
                for (int i = 0; i < AddressUtils.jilinSipingName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.jilinSipingName[i]);
                    mGetProvince.setId(AddressUtils.jilinSipingId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000663:
                for (int i = 0; i < AddressUtils.jilinLiaoyuanName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.jilinLiaoyuanName[i]);
                    mGetProvince.setId(AddressUtils.jilinLiaoyuanId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000669:
                for (int i = 0; i < AddressUtils.jilinTonghuaName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.jilinTonghuaName[i]);
                    mGetProvince.setId(AddressUtils.jilinTonghuaId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000678:
                for (int i = 0; i < AddressUtils.jilinBaishanName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.jilinBaishanName[i]);
                    mGetProvince.setId(AddressUtils.jilinBaishanId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000686:
                for (int i = 0; i < AddressUtils.jilinSongyuanName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.jilinSongyuanName[i]);
                    mGetProvince.setId(AddressUtils.jilinSongyuanId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000693:
                for (int i = 0; i < AddressUtils.jilinBaichengName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.jilinBaichengName[i]);
                    mGetProvince.setId(AddressUtils.jilinBaichengId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000700:
                for (int i = 0; i < AddressUtils.jilinYanbianName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.jilinYanbianName[i]);
                    mGetProvince.setId(AddressUtils.jilinYanbianId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000710:
                for (int i = 0; i < AddressUtils.heilongjingHaerbinName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.heilongjingHaerbinName[i]);
                    mGetProvince.setId(AddressUtils.heilongjingHaerbinId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000731:
                for (int i = 0; i < AddressUtils.heilongjiangQiqihaerName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.heilongjiangQiqihaerName[i]);
                    mGetProvince.setId(AddressUtils.heilongjiangQiqihaerId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000749:
                for (int i = 0; i < AddressUtils.heilongjiangJixiName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.heilongjiangJixiName[i]);
                    mGetProvince.setId(AddressUtils.heilongjiangJixiId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000760:
                for (int i = 0; i < AddressUtils.heilongjiangHegangName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.heilongjiangHegangName[i]);
                    mGetProvince.setId(AddressUtils.heilongjiangHegangId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000770:
                for (int i = 0; i < AddressUtils.heilongjiangShuangyashanName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.heilongjiangShuangyashanName[i]);
                    mGetProvince.setId(AddressUtils.heilongjiangShuangyashanId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000780:
                for (int i = 0; i < AddressUtils.heilongjiangDaqingName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.heilongjiangDaqingName[i]);
                    mGetProvince.setId(AddressUtils.heilongjiangDaqingId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000791:
                for (int i = 0; i < AddressUtils.heilongjiangYichunName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.heilongjiangYichunName[i]);
                    mGetProvince.setId(AddressUtils.heilongjiangYichunId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000810:
                for (int i = 0; i < AddressUtils.heilongjiangJiamusiName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.heilongjiangJiamusiName[i]);
                    mGetProvince.setId(AddressUtils.heilongjiangJiamusiId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000823:
                for (int i = 0; i < AddressUtils.heilongjiangQitaiheName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.heilongjiangQitaiheName[i]);
                    mGetProvince.setId(AddressUtils.heilongjiangQitaiheId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000829:
                for (int i = 0; i < AddressUtils.heilongjiangMudanjiangName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.heilongjiangMudanjiangName[i]);
                    mGetProvince.setId(AddressUtils.heilongjiangMudanjiangId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000841:
                for (int i = 0; i < AddressUtils.heilongjiangHeiheName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.heilongjiangHeiheName[i]);
                    mGetProvince.setId(AddressUtils.heilongjiangHeiheId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000849:
                for (int i = 0; i < AddressUtils.heilongjiangSuihuaName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.heilongjiangSuihuaName[i]);
                    mGetProvince.setId(AddressUtils.heilongjiangSuihuaId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109000861:
                for (int i = 0; i < AddressUtils.heilongjiangDaxinganlingName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.heilongjiangDaxinganlingName[i]);
                    mGetProvince.setId(AddressUtils.heilongjiangDaxinganlingId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002310:
                for (int i = 0; i < AddressUtils.guangxiNanningId.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.guangxiNanningName[i]);
                    mGetProvince.setId(AddressUtils.guangxiNanningId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002324:
                for (int i = 0; i < AddressUtils.guangxiLiuzhouName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.guangxiLiuzhouName[i]);
                    mGetProvince.setId(AddressUtils.guangxiLiuzhouId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002336:
                for (int i = 0; i < AddressUtils.guangxiGuilinName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.guangxiGuilinName[i]);
                    mGetProvince.setId(AddressUtils.guangxiGuilinId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002355:
                for (int i = 0; i < AddressUtils.guangxiWuzhouName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.guangxiWuzhouName[i]);
                    mGetProvince.setId(AddressUtils.guangxiWuzhouId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002364:
                for (int i = 0; i < AddressUtils.guangxiBeihaiName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.guangxiBeihaiName[i]);
                    mGetProvince.setId(AddressUtils.guangxiBeihaiId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002370:
                for (int i = 0; i < AddressUtils.guangxiFangchenggangName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.guangxiFangchenggangName[i]);
                    mGetProvince.setId(AddressUtils.guangxiFangchenggangId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002376:
                for (int i = 0; i < AddressUtils.guangxiQinzhouName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.guangxiQinzhouName[i]);
                    mGetProvince.setId(AddressUtils.guangxiQinzhouId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002382:
                for (int i = 0; i < AddressUtils.guangxiGuigangName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.guangxiGuigangName[i]);
                    mGetProvince.setId(AddressUtils.guangxiGuigangId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002389:
                for (int i = 0; i < AddressUtils.guangxiYulinName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.guangxiYulinName[i]);
                    mGetProvince.setId(AddressUtils.guangxiYulinId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002397:
                for (int i = 0; i < AddressUtils.guangxiBaiseName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.guangxiBaiseName[i]);
                    mGetProvince.setId(AddressUtils.guangxiBaiseId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002411:
                for (int i = 0; i < AddressUtils.guangxiHezhouName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.guangxiHezhouName[i]);
                    mGetProvince.setId(AddressUtils.guangxiHezhouId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002417:
                for (int i = 0; i < AddressUtils.guangxiHechiName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.guangxiHechiName[i]);
                    mGetProvince.setId(AddressUtils.guangxiHechiId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002430:
                for (int i = 0; i < AddressUtils.guangxiLaibinName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.guangxiLaibinName[i]);
                    mGetProvince.setId(AddressUtils.guangxiLaibinId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002438:
                for (int i = 0; i < AddressUtils.guangxiChongzuoName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.guangxiChongzuoName[i]);
                    mGetProvince.setId(AddressUtils.guangxiChongzuoId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002448:
                for (int i = 0; i < AddressUtils.hainanHaikouName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.hainanHaikouName[i]);
                    mGetProvince.setId(AddressUtils.hainanHaikouId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002454:
                for (int i = 0; i < AddressUtils.hainanSanyaName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.hainanSanyaName[i]);
                    mGetProvince.setId(AddressUtils.hainanSanyaId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002456:
                for (int i = 0; i < AddressUtils.hainanShengzhixiaxianName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.hainanShengzhixiaxianName[i]);
                    mGetProvince.setId(AddressUtils.hainanShengzhixiaxianId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002477:
                for (int i = 0; i < AddressUtils.chongqingShiquName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.chongqingShiquName[i]);
                    mGetProvince.setId(AddressUtils.chongqingShiquId[i]);
                    countryList.add(mGetProvince);
                }
                break;
            case 109002493:
                for (int i = 0; i < AddressUtils.chongqingXianquName.length; i++){
                    GetProvince mGetProvince = new GetProvince();
                    mGetProvince.setName(AddressUtils.chongqingXianquName[i]);
                    mGetProvince.setId(AddressUtils.chongqingXianquId[i]);
                    countryList.add(mGetProvince);
                }
                break;
        }
        return countryList;
    }
}

package com.yunqi.clientandroid.employer.entity;

import com.yunqi.clientandroid.entity.IDontObfuscate;

/**
 * 预设价格
 * Created by mengwei on 2017/5/5.
 */

public class DefaultPriceEntity extends IDontObfuscate {
    public boolean HasAdvancePrice;//是否有预设价格
    public boolean IsPassAdvancePriceEffectTime;
    public String Price;//预设的价格
    public String EffectTime;//生效时间
}

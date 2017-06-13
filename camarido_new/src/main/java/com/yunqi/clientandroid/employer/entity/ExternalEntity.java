package com.yunqi.clientandroid.employer.entity;

import com.yunqi.clientandroid.entity.IDontObfuscate;

/**
 * 外部监控人员返回的实体类
 * Created by mengwei on 2016/12/27.
 */

public class ExternalEntity extends IDontObfuscate {
    public String userPhone;
    public String id;

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ExternalEntity{" +
                "userPhone='" + userPhone + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}

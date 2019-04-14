package com.feng.englishlistening.entity;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2019/4/12.
 */
public class UserInfo extends BmobObject {
    private String phone;
    private String nickname;
    private String password;

    public UserInfo() {
    }

    public UserInfo(String phone, String nickname, String password) {
        this.phone = phone;
        this.nickname = nickname;
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

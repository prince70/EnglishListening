package com.feng.englishlistening;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.feng.englishlistening.entity.UserInfo;
import com.feng.englishlistening.utils.SharedPreferencesTools;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


/**
 * 登录界面
 */
public class LoginActivity extends BaseActivity {
    private static final String TAG = "LoginActivity";
    private TextInputEditText et_user;
    private TextInputEditText et_password;
    private ImageView iv_delete;
    private LinearLayout ll_submit;
    private TextView tv_forget;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData();
    }

    private void initView() {
        et_user = findViewById(R.id.et_user);
        et_password = findViewById(R.id.et_password);
        iv_delete = findViewById(R.id.iv_delete);
        ll_submit = findViewById(R.id.ll_submit);
        tv_forget = findViewById(R.id.tv_forget);

        tv_forget.setOnClickListener(singleListener);
        iv_delete.setOnClickListener(singleListener);
        ll_submit.setOnClickListener(singleListener);


    }

    private void initData() {

        et_user.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    iv_delete.setVisibility(View.VISIBLE);
                } else if (s.length() == 0) {
                    iv_delete.setVisibility(View.GONE);
                }
            }
        });
    }


    @Override
    protected boolean handClickEvent(View v) {
        switch (v.getId()) {
            case R.id.iv_delete:
                et_user.setText("");
                break;
            case R.id.ll_submit:
                String phone = et_user.getText().toString().trim();
                String password = et_password.getText().toString().trim();
                Log.e(TAG, "handClickEvent: " + phone + "    " + password);
                if (phone.equals("") || password.equals("")) {
                    Toast.makeText(this, "账号或密码不能为空", Toast.LENGTH_SHORT).show();
                    return false;
                }

                BmobQuery<UserInfo> query = new BmobQuery<>();
                query.addWhereEqualTo("phone", phone);
                query.addWhereEqualTo("password", password);
                query.findObjects(new FindListener<UserInfo>() {
                    @Override
                    public void done(List<UserInfo> list, BmobException e) {
                        if (e == null) {
                            if (list.size() != 0) {
                                String phone1 = list.get(0).getPhone();
                                String nickname = list.get(0).getNickname();
                                SharedPreferencesTools.saveSysMap(LoginActivity.this, "phone",phone1);
                                SharedPreferencesTools.saveSysMap(LoginActivity.this, "nickname",nickname);
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            }
                        } else {
                            Log.e(TAG, "done: " + e.getMessage());
                        }
                    }
                });

                break;
            case R.id.tv_forget:

                break;
        }
        return true;
    }

    private void login(String phone, String password) {

    }

    @Override
    public View getContentView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_login, null);
    }

    @Override
    public String getHeadTitle() {
        headView.hideLeftView();
        headView.setBackgroundColor(getResources().getColor(R.color.abus_white));
        headView.setRightText("马上注册");
        headView.setRightTextColor(getResources().getColor(R.color.abus_light_grey));
        headView.setListenerRight(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        return "";
    }


}

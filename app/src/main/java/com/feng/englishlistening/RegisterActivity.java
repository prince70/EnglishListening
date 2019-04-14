package com.feng.englishlistening;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.feng.englishlistening.entity.UserInfo;
import com.feng.englishlistening.view.TimingButton;

import java.util.Random;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * 注册
 */
public class RegisterActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {
    private TextView tv_agreement, tv_next_step;
    private TextInputEditText et_sms, et_nickname, et_password;
    private TextInputEditText et_phoneNum;
    private CheckBox cb_agreement;
    private ImageView iv_delete;
    private TimingButton timeBtn;
    String array[] = {"241465", "412725", "312455", "854127", "965441", "134775", "247241", "236354", "641275", "412358"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    private void initView() {
        et_nickname = findViewById(R.id.et_nickname);
        tv_agreement = findViewById(R.id.tv_agreement);
        tv_next_step = findViewById(R.id.tv_next_step);
        et_phoneNum = findViewById(R.id.et_phoneNum);
        et_sms = findViewById(R.id.et_sms);
        cb_agreement = findViewById(R.id.cb_agreement);
        iv_delete = findViewById(R.id.iv_delete);
        timeBtn = findViewById(R.id.timeBtn);

        iv_delete.setOnClickListener(singleListener);
        timeBtn.setOnClickListener(singleListener);
        cb_agreement.setOnCheckedChangeListener(this);
        tv_next_step.setOnClickListener(singleListener);

        et_password = findViewById(R.id.et_password);

    }

    private void initData() {
        setWordListener();
        et_phoneNum.addTextChangedListener(new TextWatcher() {
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
                et_phoneNum.setText("");
                break;
            case R.id.timeBtn:
                String phone = et_phoneNum.getText().toString();
                if (phone.equals("")) {
                    Toast.makeText(this, "请输入11位正确的手机号", Toast.LENGTH_SHORT).show();
                    return false;
                }
                Random rnd = new Random();
                int index = rnd.nextInt(10);
                et_sms.setText(array[index]);
//                在请求成功时
                timeBtn.startTiming();
                break;
            case R.id.tv_next_step:
                String phone2 = et_phoneNum.getText().toString();
                if (phone2.equals("")) {
                    Toast.makeText(this, "手机号码不能为空", Toast.LENGTH_SHORT).show();
                    return false;
                }

                String sms = et_sms.getText().toString();
                if (sms.equals("")) {
                    Toast.makeText(this, "验证码不能为空", Toast.LENGTH_SHORT).show();
                    return false;
                }

                String nickname = et_nickname.getText().toString();
                if (nickname.equals("")) {
                    Toast.makeText(this, "昵称不能为空", Toast.LENGTH_SHORT).show();
                    return false;
                }

                String password = et_password.getText().toString();

                if (password.equals("")){
                    Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    return false;
                }


                UserInfo info = new UserInfo(phone2,nickname,password);
                info.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    }
                });

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 1000);



                break;
        }
        return super.handClickEvent(v);
    }

    /**
     * 设置用户协议文字的点击事件
     */
    private void setWordListener() {
        final SpannableStringBuilder style = new SpannableStringBuilder();

        //设置文字
//        style.append(getResources().getString(R.string.user_agreement));
        style.append("同意《用户注册协议》");

        //设置部分文字点击事件
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
//                Toast.makeText(RegisterAndLoginActivity.this, "触发点击事件!", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(RegisterActivity.this, AgreementActivity.class));
            }

            @Override
            public void updateDrawState(TextPaint ds) {
//                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        style.setSpan(clickableSpan, 2, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_agreement.setText(style);

        //设置部分文字颜色
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#F79729"));
        style.setSpan(foregroundColorSpan, 2, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        //配置给TextView
        tv_agreement.setMovementMethod(LinkMovementMethod.getInstance());
        tv_agreement.setText(style);

    }

    @Override
    public View getContentView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_register, null);
    }

    @Override
    public String getHeadTitle() {
        headView.setVisibility(View.GONE);
        return "";
    }

    /**
     * 是否同意协议
     *
     * @param buttonView
     * @param isChecked
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            tv_next_step.setEnabled(true);
        } else {
            tv_next_step.setEnabled(false);
        }
    }
}

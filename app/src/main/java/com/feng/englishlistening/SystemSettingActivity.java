package com.feng.englishlistening;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import com.feng.englishlistening.entity.ViewConfig;
import com.feng.englishlistening.utils.SharedPreferencesTools;
import com.feng.englishlistening.view.MessageArrowView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2019/4/12.
 */
public class SystemSettingActivity extends BaseActivity {
    private MessageArrowView ma_setting;
    ArrayList<ViewConfig> list = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    private void initView() {

        ma_setting = findViewById(R.id.ma_setting);
    }

    private void initData() {
        ViewConfig viewConfig = new ViewConfig("当前版本", getVersionName(this), true);
        list.add(viewConfig);
        ma_setting.setData(list);

    }


    /**
     * 获取软件版本号
     *
     * @param context
     * @return
     */
    private String getVersionName(Context context) {
        String versionName = "";
        try {
            // 获取软件版本号，对应AndroidManifest.xml下android:versionName
            versionName = context.getPackageManager().getPackageInfo(
                    "com.feng.englishlistening", 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    public void SignOut(View view) {
        SharedPreferencesTools.clearAll(this);
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public View getContentView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_system_setting, null);
    }

    @Override
    public String getHeadTitle() {
        return "系统设置";
    }
}

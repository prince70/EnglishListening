package com.feng.englishlistening;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.feng.englishlistening.interfaces.SingleClickListener;
import com.feng.englishlistening.view.ActivityHeaderView;


/**
 * Created by prince70 on 2019/3/11.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected ActivityHeaderView headView;
    protected View contentLayout;


    protected SingleClickListener singleListener = new SingleClickListener() {
        @Override
        public void onSingleClick(View v) {
            if (handClickEvent(v)) {
                shieldClickEvent();
            }
        }

    };

    /**
     * @param v
     * @return true : 短时间内限制点击， false ： 无限制
     */
    protected boolean handClickEvent(View v) {
        return false;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_base);
        MyApplication.addActivity(this);

        headView = findViewById(R.id.title);
        FrameLayout baseContentLayout = (FrameLayout) findViewById(R.id.base_content_layout);
        contentLayout = getContentView();
        if (contentLayout != null) {
            Drawable backDrawable = contentLayout.getBackground();
            if (backDrawable == null) {
                contentLayout.setBackgroundColor(getResources().getColor(
                        R.color.abus_white));
            }
            FrameLayout.LayoutParams p = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            baseContentLayout.addView(contentLayout, p);
        }
        String title = getHeadTitle();
        if (title == null) {
            title = "";
        }
        headView.setTitle(title);
    }


    public abstract View getContentView();

    public abstract String getHeadTitle();

    /**
     * 设置字体不随系统而改变
     *
     * @return
     */
    @Override
    public Resources getResources() {
        Resources resources = super.getResources();
        Configuration configuration = new Configuration();
        configuration.setToDefaults();
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        return resources;
    }
}

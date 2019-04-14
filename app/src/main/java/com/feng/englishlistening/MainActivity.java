package com.feng.englishlistening;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.feng.englishlistening.fragment.HomeFragment;
import com.feng.englishlistening.fragment.LearningFragment;
import com.feng.englishlistening.fragment.MineFragment;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
     private boolean exit = false;


    private RadioGroup radioGroup;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    //    private FragmentWallet fragmentWallet = new FragmentWallet();
    private HomeFragment homeFragment = new HomeFragment();
    private LearningFragment learningFragment = new LearningFragment();
    private MineFragment mineFragment = new MineFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        radioGroup = findViewById(R.id.rg_main);
        radioGroup.setOnCheckedChangeListener(this);

        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        transaction.add(R.id.ll_content, new HomeFragment());
        transaction.commit();


    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        /**
         * 再次启动transaction事务
         */
        transaction = manager.beginTransaction();
        switch (checkedId) {
            case R.id.rb_home:
                if (homeFragment != null) {
                    transaction.replace(R.id.ll_content, new HomeFragment());
                }

                break;
            case R.id.rb_learning:
                if (learningFragment != null) {
                    transaction.replace(R.id.ll_content, new LearningFragment());
                }
                break;
            case R.id.rb_mine:
                if (mineFragment != null) {
                    transaction.replace(R.id.ll_content, new MineFragment());
                }
                break;
        }
        transaction.commit();
    }




    private Handler hand = new Handler() {
        public void handleMessage(Message msg) {

        }
    };

    Runnable run = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            exit = false;
        }
    };

    /**
     * 退出程序
     */
    private void backPress() {
        if (exit) {
            hand.removeCallbacksAndMessages(null);
            MyApplication.exitApp(this);
        } else {
            exit = true;
            Toast.makeText(this, "再按一次退出应用", Toast.LENGTH_SHORT).show();
            hand.postDelayed(run, 2500);
        }
    }

    @Override
    public void onBackPressed() {
        backPress();
    }
}

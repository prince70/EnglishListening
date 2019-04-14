package com.feng.englishlistening.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.feng.englishlistening.R;
import com.feng.englishlistening.SystemSettingActivity;
import com.feng.englishlistening.entity.ViewConfig;
import com.feng.englishlistening.utils.SharedPreferencesTools;
import com.feng.englishlistening.view.MessageArrowView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2019/4/12.
 */
public class MineFragment extends Fragment implements MessageArrowView.ItemClickListener {
    private View mView;
    private Activity mActivity;

    private MessageArrowView ma_user;
    ArrayList<ViewConfig> list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_mine, container, false);
        initView();
        initData();
        return mView;
    }


    private void initView() {
        ma_user = mView.findViewById(R.id.ma_user);
        ma_user.setItemClickListener(this);
    }

    private void initData() {
        String phone = SharedPreferencesTools.getSysMapStringValue(mActivity, "phone");
        String nickname = SharedPreferencesTools.getSysMapStringValue(mActivity, "nickname");
        ViewConfig viewConfig = new ViewConfig("手机号码", phone, true);
        list.add(viewConfig);
        viewConfig = new ViewConfig("昵称", nickname, true);
        list.add(viewConfig);
        viewConfig = new ViewConfig("系统设置", "", true);
        list.add(viewConfig);
        ma_user.setData(list);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public void onItemClick(MessageArrowView mv, View v, int position) {
        switch (position) {
            case 2:
                startActivity(new Intent(mActivity, SystemSettingActivity.class));
                break;
        }
    }
}

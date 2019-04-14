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
import android.widget.Button;

import com.feng.englishlistening.CompoundDicationActivity;
import com.feng.englishlistening.MainActivity;
import com.feng.englishlistening.R;
import com.feng.englishlistening.SinglePracticeActivity;
import com.feng.englishlistening.SingleTestActivity;

/**
 * Created by Administrator on 2019/4/12.
 */
public class LearningFragment extends Fragment {
    private View mView;
    private Activity mActivity;

    private Button btnMAsp = null;
    private Button btnMAst = null;
    private Button btnMAcd = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.fragment_learning,container,false);
        initView();
        initData();
        return mView;
    }


    private void initView() {
        btnMAsp = (Button) mView.findViewById(R.id.btnMAsp);
        btnMAst = (Button) mView.findViewById(R.id.btnMAst);
        btnMAcd = (Button) mView.findViewById(R.id.btnMAcd);


        btnMAsp.setOnClickListener(new MyListener());
        btnMAst.setOnClickListener(new MyListener());
        btnMAcd.setOnClickListener(new MyListener());
    }


    class MyListener implements View.OnClickListener {

        //User Defined----start


        //User Defined----End

        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.btnMAsp:
                    Intent intentsp = new Intent(mActivity,  SinglePracticeActivity.class);
                    startActivity(intentsp);
                    break;
                case R.id.btnMAst:
                    Intent intentst = new Intent(mActivity, SingleTestActivity.class);
                    startActivity(intentst);
                    break;
                case R.id.btnMAcd:
                    Intent intentcd = new Intent(mActivity, CompoundDicationActivity.class);
                    startActivity(intentcd);
                    break;
                default:
                    break;

            }
        }//onClick
    }
    private void initData() {

    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }
}

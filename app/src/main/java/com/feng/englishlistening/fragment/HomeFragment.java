package com.feng.englishlistening.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.feng.englishlistening.ListenDetailActivity;
import com.feng.englishlistening.MainActivity;
import com.feng.englishlistening.R;
import com.feng.englishlistening.adapter.ListenAdapter;
import com.feng.englishlistening.entity.ListenInfo;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019/4/12.
 */
public class HomeFragment extends Fragment implements AdapterView.OnItemClickListener {
    private View mView;
    private Activity mActivity;

    private Banner banner;

    private List<String> images = new ArrayList<>();

    private ListView lv_listen;
    private List<ListenInfo> infos = new ArrayList<>();
    private ListenAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        initView();
        initData();
        return mView;
    }

    private void initView() {
        images.add("http://img2.3lian.com/2014/f2/37/d/40.jpg");
        images.add("http://f.hiphotos.baidu.com/image/h%3D200/sign=1478eb74d5a20cf45990f9df460b4b0c/d058ccbf6c81800a5422e5fdb43533fa838b4779.jpg");
        images.add("http://img2.3lian.com/2014/f2/37/d/39.jpg");
        images.add("http://f.hiphotos.baidu.com/image/pic/item/09fa513d269759ee50f1971ab6fb43166c22dfba.jpg");
        banner = mView.findViewById(R.id.banner);
        lv_listen = mView.findViewById(R.id.lv_listen);

        banner.setImages(images).setImageLoader(new GlideImageLoader()).start();
    }

    private void initData() {
        infos.add(new ListenInfo("http://img2.3lian.com/2014/f2/37/d/40.jpg", "Far Away From Home", "Far Away From Home-Groove Coverage"));
        infos.add(new ListenInfo("http://img2.3lian.com/2014/f2/37/d/40.jpg", "You Raise Me Up", "Written by James Horner/Brendan Graham/Rolf ; vland/Dave Arch"));
        infos.add(new ListenInfo("http://img2.3lian.com/2014/f2/37/d/40.jpg", "Rolling In the Deep", "Written by Paul Epworth/Adele Adkins"));
        infos.add(new ListenInfo("http://img2.3lian.com/2014/f2/37/d/40.jpg", "Yesterday Once More", "When I was young"));
        infos.add(new ListenInfo("http://img2.3lian.com/2014/f2/37/d/40.jpg", "God Is A Girl", "Written by Axel Konrad/Ole Wierk/Bega Low"));

        adapter = new ListenAdapter(infos, mActivity);
        lv_listen.setAdapter(adapter);
        lv_listen.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                Intent intent = new Intent(mActivity, ListenDetailActivity.class);
                intent.putExtra("first", 0);
                startActivity(intent);
                break;

            case 1:
                Intent intent1 = new Intent(mActivity, ListenDetailActivity.class);
                intent1.putExtra("second", 1);
                startActivity(intent1);
                break;
            case 2:
                Intent intent2 = new Intent(mActivity, ListenDetailActivity.class);
                intent2.putExtra("third", 2);
                startActivity(intent2);
                break;
            case 3:
                Intent intent3 = new Intent(mActivity, ListenDetailActivity.class);
                intent3.putExtra("fourth", 3);
                startActivity(intent3);
                break;
            case 4:
                Intent intent4 = new Intent(mActivity, ListenDetailActivity.class);
                intent4.putExtra("fifth", 4);
                startActivity(intent4);
                break;

        }
//        startActivity(new Intent(mActivity, ListenDetailActivity.class));
    }

    class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            /**
             注意：
             1.图片加载器由自己选择，这里不限制，只是提供几种使用方法
             2.返回的图片路径为Object类型，由于不能确定你到底使用的那种图片加载器，
             传输的到的是什么格式，那么这种就使用Object接收和返回，你只需要强转成你传输的类型就行，
             切记不要胡乱强转！
             */

            //Glide 加载图片简单用法
            Glide.with(context).load(path).into(imageView);


        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }
}

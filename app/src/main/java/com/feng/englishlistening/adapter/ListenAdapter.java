package com.feng.englishlistening.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.feng.englishlistening.R;
import com.feng.englishlistening.entity.ListenInfo;

import com.bumptech.glide.Glide;
import java.util.List;

/**
 * Created by prince70 on 2019/4/13.
 */
public class ListenAdapter extends BaseAdapter {
    private List<ListenInfo> mDatas;
    private Activity mActivity;

    public ListenAdapter(List<ListenInfo> mDatas, Activity mActivity) {
        this.mDatas = mDatas;
        this.mActivity = mActivity;
    }

    @Override
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mActivity).inflate(R.layout.listen_item, null);
            holder.iv_url = convertView.findViewById(R.id.iv_url);
            holder.tv_title = convertView.findViewById(R.id.tv_title);
            holder.tv_content = convertView.findViewById(R.id.tv_content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Glide.with(mActivity).load(mDatas.get(position).getUrl()).into(holder.iv_url);
        holder.tv_title.setText(mDatas.get(position).getTitle());
        holder.tv_content.setText(mDatas.get(position).getContent());
        return convertView;
    }

    class ViewHolder {
        ImageView iv_url;
        TextView tv_title;
        TextView tv_content;
    }
}

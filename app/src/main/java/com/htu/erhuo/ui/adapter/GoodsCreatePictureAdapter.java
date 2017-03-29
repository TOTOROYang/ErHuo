package com.htu.erhuo.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.htu.erhuo.R;
import com.htu.erhuo.utils.ImageUtils;

import java.util.List;

/**
 * Description
 * Created by yzw on 2017/3/29.
 */

public class GoodsCreatePictureAdapter extends BaseAdapter {
    private Context context;

    private List<String> data;

    public GoodsCreatePictureAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public String getItem(int position) {
        return data == null ? "" : data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_goods_create_picture, parent, false);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.iv_goods_create_picture);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (data.get(position).equals("add")) {
            ImageUtils.showImageRes((Activity) context, holder.imageView, R.drawable.main_icon);
        }

        return convertView;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public static class ViewHolder {
        ImageView imageView;
    }
}

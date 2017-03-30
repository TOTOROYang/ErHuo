package com.htu.erhuo.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.htu.erhuo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description
 * Created by yzw on 2017/2/19.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyHolder> {

    private Context mContext;
    private String[] strs = new String[100];

    public RecyclerAdapter(Context context) {
        this.mContext = context;
        //为测试给Recycler添加数据
        for (int i = 0; i < 100; i++) {
            strs[i] = "";
            for (int j = 0; j <= i % 5; j++)
                strs[i] += "open";
        }
    }

    //这里返回一个ViewHolder
    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_item, null);
        return new MyHolder(view);
    }

    //为ViewHolder中的布局绑定数据
    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        holder.tvTextItem.setText(strs[position]);
    }

    @Override
    public int getItemCount() {
        return strs.length;
    }

    static class MyHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_goods_avatar_item)
        ImageView ivGoodsAvatarItem;
        @BindView(R.id.tv_text_item)
        TextView tvTextItem;
        @BindView(R.id.tv_goods_time_item)
        TextView tvGoodsTimeItem;
        @BindView(R.id.tv_goods_description_item)
        TextView tvGoodsDescriptionItem;
        @BindView(R.id.iv_goods_img_item)
        ImageView ivGoodsImgItem;
        @BindView(R.id.iv_goods_img2_item)
        ImageView ivGoodsImg2Item;

        MyHolder(View itemView) {
            super(itemView);
            //ButterKnife也可以用于ViewHolder中
            ButterKnife.bind(this, itemView);
        }
    }
}

package com.htu.erhuo.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.htu.erhuo.R;
import com.htu.erhuo.entity.ItemInfo;
import com.htu.erhuo.utils.DateUtil;
import com.htu.erhuo.utils.ImageUtils;

/**
 * Description
 * Created by yzw on 2017/3/30.
 */

public class GoodsListAdapter extends BaseQuickAdapter<ItemInfo, BaseViewHolder> {

    public GoodsListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, ItemInfo item) {
        helper.setText(R.id.tv_text_item, item.getItemTitle());
        helper.setText(R.id.tv_goods_time_item, DateUtil.getDateFormat(item.getCreateTime()));
        helper.setText(R.id.tv_goods_description_item, item.getItemDesc());
        helper.setText(R.id.tv_goods_price_item, String.format("价格：￥%s", item.getPrice().toString()));
        if (item.getPhotoList() != null) {
            String[] images = item.getPhotoList().split(",");
            ImageUtils.showImage((Activity) mContext, (ImageView) helper.getView(R.id.iv_goods_avatar_item), images[0]);
            if (images.length == 1) {
                helper.getView(R.id.iv_goods_img_item).setVisibility(View.GONE);
                helper.getView(R.id.iv_goods_img2_item).setVisibility(View.GONE);
            }
            if (images.length == 2) {
                helper.getView(R.id.iv_goods_img_item).setVisibility(View.VISIBLE);
                helper.getView(R.id.iv_goods_img2_item).setVisibility(View.GONE);
                ImageUtils.showImage((Activity) mContext, (ImageView) helper.getView(R.id.iv_goods_img_item), images[1]);
            }
            if (images.length > 2) {
                helper.getView(R.id.iv_goods_img_item).setVisibility(View.VISIBLE);
                helper.getView(R.id.iv_goods_img2_item).setVisibility(View.VISIBLE);
                ImageUtils.showImage((Activity) mContext, (ImageView) helper.getView(R.id.iv_goods_img_item), images[1]);
                ImageUtils.showImage((Activity) mContext, (ImageView) helper.getView(R.id.iv_goods_img2_item), images[2]);

            }
        }
        switch (item.getStatus()){
            case 0:
                helper.setText(R.id.tv_goods_status_item, "状态：待售");
                break;
            case 1:
                helper.setText(R.id.tv_goods_status_item, "状态：出售中");
                break;
            case 2:
                helper.setText(R.id.tv_goods_status_item, "状态：已出售");
                break;
            default:
                break;
        }
    }
}

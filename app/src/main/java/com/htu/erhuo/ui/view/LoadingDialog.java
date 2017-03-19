package com.htu.erhuo.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.htu.erhuo.R;

/**
 * Description
 * Created by yzw on 2017/2/5.
 */

public class LoadingDialog extends Dialog {

    private TextView tvLoading;

    public LoadingDialog(Context context) {
        super(context, R.style.loadingDialogStyle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_layout);
        LinearLayout linearLayout = (LinearLayout) this.findViewById(R.id.ll_loading_layout);
        tvLoading = (TextView) this.findViewById(R.id.tv_loading);
        linearLayout.getBackground().setAlpha(210);
    }

    public void setLoadingContent(String content) {
        if (tvLoading != null)
            if (!TextUtils.isEmpty(content)) {
                tvLoading.setVisibility(View.VISIBLE);
                tvLoading.setText(content);
            } else {
                tvLoading.setVisibility(View.INVISIBLE);
            }
    }

    @Override
    public void dismiss() {
        try {
            if (isShowing()) {
                super.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

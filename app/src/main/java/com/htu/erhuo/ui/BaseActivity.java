package com.htu.erhuo.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.htu.erhuo.ui.view.LoadingDialog;

/**
 * Description
 * Created by yzw on 2017/2/5.
 */

public class BaseActivity extends AppCompatActivity {
    public Context mContext;
    protected LoadingDialog loadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        initDialog();
    }

    private void initDialog() {
        if (loadingDialog == null) {
            synchronized (this) {
                if (loadingDialog == null) {
                    loadingDialog = new LoadingDialog(mContext);
                    loadingDialog.setCanceledOnTouchOutside(false);
                }
            }
        }
    }

    /**
     * 显示加载框
     */
    public void showLoadingDialog() {
        if (loadingDialog == null || loadingDialog.isShowing()) {
            return;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadingDialog.setLoadingContent(null);
                loadingDialog.show();
            }
        });
    }

    /**
     * 显示加载框
     */
    public void showLoadingDialog(final String content) {
        if (loadingDialog == null || loadingDialog.isShowing()) {
            return;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadingDialog.setCancelable(false);
                loadingDialog.setLoadingContent(content);
                loadingDialog.show();
            }
        });
    }

    /**
     * 关闭加载框
     */
    public void hideLoadingDialog() {
        if (loadingDialog == null || !loadingDialog.isShowing()) {
            return;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (loadingDialog != null) {
                    loadingDialog.setCancelable(true);
                    loadingDialog.dismiss();
                }
            }
        });
    }
}

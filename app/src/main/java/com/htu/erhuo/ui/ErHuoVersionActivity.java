package com.htu.erhuo.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.htu.erhuo.R;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ErHuoVersionActivity extends BaseActivity {

    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.tv_version)
    TextView tvVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_er_huo_version);
        ButterKnife.bind(this);
        toolBar.setTitleTextColor(Color.WHITE);
        toolBar.setTitle(R.string.str_about_version);
        toolBar.setNavigationIcon(R.drawable.ic_arrow_back_white_36dp);
        setSupportActionBar(toolBar);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        showVersionState(getStringFromAssert("version.txt"));
    }

    private void showVersionState(String versionStateText) {
        tvVersion.setText(versionStateText);
    }

    private String getStringFromAssert(String fileName) {
        String content = null; //结果字符串
        try {
            InputStream is = this.getResources().getAssets().open(fileName); //打开文件
            int ch = 0;
            ByteArrayOutputStream out = new ByteArrayOutputStream(); //实现了一个输出流
            while ((ch = is.read()) != -1) {
                out.write(ch); //将指定的字节写入此 byte 数组输出流
            }
            byte[] buff = out.toByteArray();//以 byte 数组的形式返回此输出流的当前内容
            out.close(); //关闭流
            is.close(); //关闭流
            content = new String(buff, "UTF-8"); //设置字符串编码
        } catch (Exception e) {

        }
        return content;
    }
}

package com.htu.erhuo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.htu.erhuo.entity.MovieEntity;
import com.htu.erhuo.network.Network;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        test();
    }

    private void test(){

        Subscriber<MovieEntity> subscriber = new Subscriber<MovieEntity>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(MovieEntity movieEntity) {
                Gson gson = new Gson();
                Log.d("yzw",gson.toJson(movieEntity));
            }
        };
        Network.getInstance().getTopMovie(1,20).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }
}

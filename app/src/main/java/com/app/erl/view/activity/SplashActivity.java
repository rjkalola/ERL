package com.app.erl.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.app.erl.R;
import com.app.erl.databinding.ActivitySplashBinding;

public class SplashActivity extends BaseActivity {
    private ActivitySplashBinding binding;
    private int SPLASH_TIME_OUT = 2500;
    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        mContext = this;
        callTimerCount();
    }

    public void callTimerCount() {
        new Handler().postDelayed(() -> {
            moveActivity(mContext, LoginActivity.class, true, false, null);
        }, SPLASH_TIME_OUT);
    }
}

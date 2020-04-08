package com.app.erl.view.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.app.erl.R;
import com.app.erl.databinding.ActivitySplashBinding;
import com.app.utilities.utils.Constant;
import com.app.utilities.utils.GlideUtil;

public class SplashActivity extends BaseActivity {
    private ActivitySplashBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);

        GlideUtil.loadGifImageFromFile(binding.imgLogo, R.raw.ic_tea_logo, null, null, Constant.ImageScaleType.CENTER_CROP, null);
    }
}

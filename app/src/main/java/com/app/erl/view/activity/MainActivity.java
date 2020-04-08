package com.app.erl.view.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.app.erl.R;
import com.app.erl.databinding.ActivityMainBinding;
import com.app.utilities.utils.Constant;
import com.app.utilities.utils.GlideUtil;

public class MainActivity extends BaseActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        if (binding != null)
            GlideUtil.loadGifImageFromFile(binding.imgLogo, R.raw.ic_tea_logo, null, null, Constant.ImageScaleType.CENTER_CROP, null);
        else
            Log.e("test", "null");
    }
}

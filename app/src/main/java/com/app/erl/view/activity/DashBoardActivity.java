package com.app.erl.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.app.erl.R;
import com.app.erl.databinding.ActivityDashboardBinding;

public class DashBoardActivity extends BaseActivity implements View.OnClickListener {
    private ActivityDashboardBinding binding;
    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTransparentStatusBar();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);
        mContext = this;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtVerify:
//                moveActivity(mContext, SignUpActivity.class, false, false, null);
                break;
        }
    }
}

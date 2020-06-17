package com.app.erl.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.app.erl.R;
import com.app.erl.databinding.ActivityOrderCompletedBinding;
import com.app.erl.util.AppConstant;

public class OrderCompletedActivity extends BaseActivity implements View.OnClickListener {
    private ActivityOrderCompletedBinding binding;
    private Context mContext;
    private boolean isFromPay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_completed);
        mContext = this;
        binding.txtHome.setOnClickListener(this);
        getIntentData();
    }

    public void getIntentData() {
        if (getIntent().hasExtra(AppConstant.IntentKey.FROM_PAY)) {
            isFromPay = getIntent().getBooleanExtra(AppConstant.IntentKey.FROM_PAY, false);
            binding.txtOrderSuccessHint.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtHome:
                moveActivity(mContext, DashBoardActivity.class, true, true, null);
                break;
        }
    }

    @Override
    public void onBackPressed() {

    }

}

package com.app.erl.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.app.erl.R;
import com.app.erl.databinding.ActivityLoginBinding;
import com.app.erl.databinding.ActivityResetPasswordBinding;

public class ResetPasswordActivity extends BaseActivity implements View.OnClickListener {
    private ActivityResetPasswordBinding binding;
    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarColor();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reset_password);
        mContext = this;

        binding.txtResetPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtResetPassword:
//                moveActivity(mContext, SignUpActivity.class, false, false, null);
                break;
        }
    }
}

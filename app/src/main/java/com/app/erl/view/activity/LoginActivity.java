package com.app.erl.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.app.erl.R;
import com.app.erl.databinding.ActivityLoginBinding;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private ActivityLoginBinding binding;
    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarColor();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        mContext = this;

        binding.txtRegisterHere.setOnClickListener(this);
        binding.txtForgotPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtRegisterHere:
                moveActivity(mContext, SignUpActivity.class, false, false, null);
                break;
            case R.id.txtForgotPassword:
                moveActivity(mContext, ResetPasswordActivity.class, false, false, null);
                break;
        }
    }
}

package com.app.erl.view.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.app.erl.R;
import com.app.erl.databinding.ActivityLoginBinding;
import com.app.erl.databinding.ActivitySignUpBinding;

public class SignUpActivity extends BaseActivity implements View.OnClickListener {
    private ActivitySignUpBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarColor();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);

        binding.txtSignUp.setOnClickListener(this);
        binding.txtLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtLogin:
                finish();
                break;
        }
    }
}

package com.app.erl.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.app.erl.R;
import com.app.erl.databinding.ActivityChangePasswordBinding;
import com.app.erl.model.entity.response.BaseResponse;
import com.app.erl.util.AppUtils;
import com.app.erl.util.LoginViewModelFactory;
import com.app.erl.util.ResourceProvider;
import com.app.erl.viewModel.UserAuthenticationViewModel;
import com.app.utilities.utils.AlertDialogHelper;
import com.app.utilities.utils.ToastHelper;
import com.app.utilities.utils.ValidationUtil;

public class ChangePasswordActivity extends BaseActivity implements View.OnClickListener {
    private ActivityChangePasswordBinding binding;
    private Context mContext;
    private UserAuthenticationViewModel userAuthenticationViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_password);
        mContext = this;
        userAuthenticationViewModel = ViewModelProviders.of(this, new LoginViewModelFactory(new ResourceProvider(getResources()))).get(UserAuthenticationViewModel.class);
        userAuthenticationViewModel.createView(this);
        userAuthenticationViewModel.mBaseResponse()
                .observe(this, mBaseResponse());

        binding.txtResetPassword.setOnClickListener(this);
        binding.imgBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtResetPassword:
                if (isValid()) {
                    userAuthenticationViewModel.changePassword(binding.edtCurrentPassword.getText().toString().trim(), binding.edtPassword.getText().toString().trim());
                }
                break;
            case R.id.imgBack:
                finish();
                break;
        }
    }

    public Observer mBaseResponse() {
        return (Observer<BaseResponse>) response -> {
            try {
                if (response == null) {
                    AlertDialogHelper.showDialog(mContext, null,
                            mContext.getString(R.string.error_unknown), mContext.getString(R.string.ok),
                            null, false, null, 0);
                    return;
                }
                if (response.isSuccess()) {
                    ToastHelper.success(mContext, getString(R.string.msg_password_changed_successfully), Toast.LENGTH_SHORT, false);
                    finish();
                } else {
                    AppUtils.handleUnauthorized(mContext, response);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    public boolean isValid() {
        boolean isValid = true;
        if (!ValidationUtil.isEmptyEditText(binding.edtConfirmPassword.getText().toString().trim())) {
            if (ValidationUtil.isValidConfirmPasswrod(binding.edtConfirmPassword.getText().toString().trim(), binding.edtPassword.getText().toString().trim())) {
                binding.edtConfirmPassword.setError(null);
            } else {
                ValidationUtil.setErrorIntoEditext(binding.edtConfirmPassword, mContext.getString(R.string.error_not_match_password));
                isValid = false;
            }
        } else {
            ValidationUtil.setErrorIntoEditext(binding.edtConfirmPassword, mContext.getString(R.string.error_empty_confirm_password));
            isValid = false;
        }

        if (!ValidationUtil.isEmptyEditText(binding.edtPassword.getText().toString().trim())) {
            binding.edtPassword.setError(null);
        } else {
            ValidationUtil.setErrorIntoEditext(binding.edtPassword, mContext.getString(R.string.error_empty_password));
            isValid = false;
        }

        if (!ValidationUtil.isEmptyEditText(binding.edtCurrentPassword.getText().toString().trim())) {
            binding.edtCurrentPassword.setError(null);
        } else {
            ValidationUtil.setErrorIntoEditext(binding.edtCurrentPassword, mContext.getString(R.string.error_empty_current_password));
            isValid = false;
        }

        return isValid;
    }
}

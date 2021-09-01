package com.app.erl.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.app.erl.R;
import com.app.erl.databinding.ActivitySignUpBinding;
import com.app.erl.model.entity.response.UserResponse;
import com.app.erl.util.AppConstant;
import com.app.erl.util.AppUtils;
import com.app.erl.util.LoginViewModelFactory;
import com.app.erl.util.ResourceProvider;
import com.app.erl.viewModel.UserAuthenticationViewModel;
import com.app.utilities.utils.AlertDialogHelper;
import com.app.utilities.utils.ToastHelper;
import com.app.utilities.utils.ValidationUtil;

public class SignUpActivity extends BaseActivity implements View.OnClickListener {
    private ActivitySignUpBinding binding;
    private UserAuthenticationViewModel userAuthenticationViewModel;
    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarColor();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        mContext = this;
        userAuthenticationViewModel = ViewModelProviders.of(this, new LoginViewModelFactory(new ResourceProvider(getResources()))).get(UserAuthenticationViewModel.class);
        userAuthenticationViewModel.createView(this);
        userAuthenticationViewModel.mUserResponse()
                .observe(this, getUserResponse());
        binding.setUserAuthenticationViewModel(userAuthenticationViewModel);

        binding.txtSignUp.setOnClickListener(this);
        binding.txtLogin.setOnClickListener(this);

//        binding.edtName.addTextChangedListener(this);
//        binding.edtEmail.addTextChangedListener(this);
//        binding.edtPassword.addTextChangedListener(this);
//        binding.edtPhoneNumber.addTextChangedListener(this);
//        binding.edtConfirmPassword.addTextChangedListener(this);

        customTextView(binding.cbTermsConditions);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtLogin:
                finish();
                break;
            case R.id.txtSignUp:
                if (isValid()) {
                    if (AppUtils.isNetworkConnected(mContext)) {
                        if (binding.cbTermsConditions.isChecked())
                            userAuthenticationViewModel.sendSignUpRequest();
                        else
                            ToastHelper.error(mContext, getString(R.string.error_accept_terms_conditions), Toast.LENGTH_SHORT, false);
                    } else {
                        ToastHelper.error(mContext, getString(R.string.error_internet_connection), Toast.LENGTH_SHORT, false);
                    }
                }
                break;
        }
    }

    public Observer getUserResponse() {
        return (Observer<UserResponse>) response -> {
            try {
                if (response == null) {
                    AlertDialogHelper.showDialog(mContext, null,
                            mContext.getString(R.string.error_unknown), mContext.getString(R.string.ok),
                            null, false, null, 0);
                    return;
                }
                if (response.isSuccess()) {
//                    AppUtils.setUserPrefrence(mContext, response.getInfo());
//                    moveActivity(mContext, DashBoardActivity.class, true, true, null);

                    Bundle bundle = new Bundle();
                    bundle.putInt(AppConstant.IntentKey.USER_ID, response.getInfo().getId());
                    bundle.putInt(AppConstant.IntentKey.OTP_TYPE, AppConstant.Type.SIGN_UP);
//                    bundle.putString(AppConstant.IntentKey.VERIFICATION_CODE, response.getAccess_code());
                    bundle.putString(AppConstant.IntentKey.EMAIL, binding.edtEmail.getText().toString().trim());
                    moveActivity(mContext, VerificationActivity.class, false, false, bundle);
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

        if (!ValidationUtil.isEmptyEditText(userAuthenticationViewModel.getSignUpRequest().getConfirmPassword())) {
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

        if (!ValidationUtil.isEmptyEditText(userAuthenticationViewModel.getSignUpRequest().getPassword())) {
            binding.edtPassword.setError(null);
        } else {
            ValidationUtil.setErrorIntoEditext(binding.edtPassword, mContext.getString(R.string.error_empty_password));
            isValid = false;
        }

        if (!ValidationUtil.isEmptyEditText(userAuthenticationViewModel.getSignUpRequest().getPhone())) {
            binding.edtPhoneNumber.setError(null);
        } else {
            ValidationUtil.setErrorIntoEditext(binding.edtPhoneNumber, mContext.getString(R.string.error_empty_phone));
            isValid = false;
        }

        if (!ValidationUtil.isEmptyEditText(userAuthenticationViewModel.getSignUpRequest().getEmail())) {
            if (ValidationUtil.isValidEmail(binding.edtEmail.getText().toString())) {
                binding.edtEmail.setError(null);
            } else {
                ValidationUtil.setErrorIntoEditext(binding.edtEmail, mContext.getString(R.string.error_invalid_email));
                isValid = false;
            }
        } else {
            ValidationUtil.setErrorIntoEditext(binding.edtEmail, mContext.getString(R.string.error_empty_email));
            isValid = false;
        }

        if (!ValidationUtil.isEmptyEditText(userAuthenticationViewModel.getSignUpRequest().getName())) {
            binding.edtName.setError(null);
        } else {
            ValidationUtil.setErrorIntoEditext(binding.edtName, mContext.getString(R.string.error_empty_name));
            isValid = false;
        }

        return isValid;
    }

    public void isEnableButton() {
        boolean isValid = true;

        if (!ValidationUtil.isEmptyEditText(userAuthenticationViewModel.getSignUpRequest().getConfirmPassword())) {
            if (!ValidationUtil.isValidConfirmPasswrod(binding.edtConfirmPassword.getText().toString().trim(), binding.edtPassword.getText().toString().trim())) {
                isValid = false;
            }
        } else {
            isValid = false;
        }

        if (ValidationUtil.isEmptyEditText(userAuthenticationViewModel.getSignUpRequest().getPassword())) {
            isValid = false;
        }

        if (ValidationUtil.isEmptyEditText(userAuthenticationViewModel.getSignUpRequest().getPhone())) {
            isValid = false;
        }

        if (!ValidationUtil.isEmptyEditText(userAuthenticationViewModel.getSignUpRequest().getEmail())) {
            if (!ValidationUtil.isValidEmail(binding.edtEmail.getText().toString())) {
                isValid = false;
            }
        } else {
            isValid = false;
        }

        if (ValidationUtil.isEmptyEditText(userAuthenticationViewModel.getSignUpRequest().getName())) {
            isValid = false;
        }

        if (isValid)
            binding.btnSignUp.setAlpha(1f);
        else
            binding.btnSignUp.setAlpha(0.5f);
    }

    public void customTextView(CheckBox view) {
        String value = "<html> By continuing, you agree to ERL’s <font color='red'><a href=\"http://app.erl.ae/terms-condition\">Terms of Service</a></font> and acknowledge ERL’s <font color='red'><a href=\"http://app.erl.ae/privacy-policy\">Privacy Policy</a></font>.</html>";
        Spannable spannedText = (Spannable)
                Html.fromHtml(value);
        view.setMovementMethod(LinkMovementMethod.getInstance());

        Spannable processedText = removeUnderlines(spannedText);
        view.setText(processedText);
    }

    public Spannable removeUnderlines(Spannable p_Text) {
        URLSpan[] spans = p_Text.getSpans(0, p_Text.length(), URLSpan.class);
        for (URLSpan span : spans) {
            int start = p_Text.getSpanStart(span);
            int end = p_Text.getSpanEnd(span);
            p_Text.removeSpan(span);
            span = new URLSpanNoUnderline(span.getURL());
            p_Text.setSpan(span, start, end, 0);
        }
        return p_Text;
    }

    private class URLSpanNoUnderline extends URLSpan {
        public URLSpanNoUnderline(String url) {
            super(url);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
            ds.setColor(getResources().getColor(R.color.colorRed));
        }
    }
}

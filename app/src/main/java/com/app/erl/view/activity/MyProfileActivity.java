package com.app.erl.view.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.app.erl.R;
import com.app.erl.databinding.ActivityMyProfileBinding;
import com.app.erl.model.entity.response.ProfileResponse;
import com.app.erl.model.entity.response.User;
import com.app.erl.util.AppConstant;
import com.app.erl.util.AppUtils;
import com.app.erl.util.LoginViewModelFactory;
import com.app.erl.util.ResourceProvider;
import com.app.erl.viewModel.UserAuthenticationViewModel;
import com.app.utilities.utils.AlertDialogHelper;
import com.app.utilities.utils.Constant;
import com.app.utilities.utils.GlideUtil;
import com.app.utilities.utils.StringHelper;
import com.app.utilities.utils.ValidationUtil;

public class MyProfileActivity extends BaseActivity implements View.OnClickListener {
    private ActivityMyProfileBinding binding;
    private Context mContext;
    private UserAuthenticationViewModel userAuthenticationViewModel;
    private ProfileResponse profileData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_profile);
        mContext = this;

        userAuthenticationViewModel = ViewModelProviders.of(this, new LoginViewModelFactory(new ResourceProvider(getResources()))).get(UserAuthenticationViewModel.class);
        userAuthenticationViewModel.createView(this);
        userAuthenticationViewModel.mProfileResponse()
                .observe(this, getProfileResponse());
        userAuthenticationViewModel.mSaveProfileResponse()
                .observe(this, saveProfileResponse());

        binding.imgBack.setOnClickListener(this);
        binding.txtSave.setOnClickListener(this);
        binding.routSelectImageView.setOnClickListener(this);

        userAuthenticationViewModel.getProfileRequest();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.txtSave:
                if (isValid()) {
                    userAuthenticationViewModel.saveProfile(getProfileData());
                }
                break;
            case R.id.routSelectImageView:
                showSelectImageFrom();
                break;
        }
    }

    public Observer getProfileResponse() {
        return (Observer<ProfileResponse>) response -> {
            try {
                if (response == null) {
                    AlertDialogHelper.showDialog(mContext, null,
                            mContext.getString(R.string.error_unknown), mContext.getString(R.string.ok),
                            null, false, null, 0);
                    return;
                }
                if (response.isSuccess()) {
                    setProfileData(response);
                    binding.setInfo(getProfileData());
                    if (!StringHelper.isEmpty(getProfileData().getImage())) {
                        GlideUtil.loadImageUsingGlideTransformation(getProfileData().getImage(), binding.imgUserImage, Constant.TransformationType.CIRCLECROP_TRANSFORM, null, null, Constant.ImageScaleType.CENTER_CROP, 0, 0, "", 0, null);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    public Observer saveProfileResponse() {
        return (Observer<ProfileResponse>) response -> {
            try {
                if (response == null) {
                    AlertDialogHelper.showDialog(mContext, null,
                            mContext.getString(R.string.error_unknown), mContext.getString(R.string.ok),
                            null, false, null, 0);
                    return;
                }
                if (response.isSuccess()) {
                    User user = AppUtils.getUserPrefrence(mContext);
                    user.setName(response.getName());
                    user.setEmail(response.getEmail());
                    user.setPhone(response.getPhone());
                    user.setImage(response.getImage());
                    AppUtils.setUserPrefrence(mContext, user);
                    setResult(1);
                    finish();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    public boolean isValid() {
        boolean isValid = true;

        if (!ValidationUtil.isEmptyEditText(getProfileData().getPhone())) {
            binding.edtMobileNUmber.setError(null);
        } else {
            ValidationUtil.setErrorIntoEditext(binding.edtMobileNUmber, mContext.getString(R.string.error_empty_phone));
            isValid = false;
        }

        if (!ValidationUtil.isEmptyEditText(getProfileData().getEmail())) {
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

        if (!ValidationUtil.isEmptyEditText(getProfileData().getName())) {
            binding.edtFullName.setError(null);
        } else {
            ValidationUtil.setErrorIntoEditext(binding.edtFullName, mContext.getString(R.string.error_empty_name));
            isValid = false;
        }

        return isValid;
    }

    public void showSelectImageFrom() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(getString(R.string.lbl_select_image_from));
        String[] items = {getString(R.string.lbl_camera), getString(R.string.lbl_gallery)};
        builder.setItems(items, (dialog, which) -> {
            switch (which) {
                case 0:

                    break;
                case 1:
                    moveActivityForResult(mContext, AlbumsActivity.class, false, false, AppConstant.IntentKey.SELECT_IMAGE_FROM_GALLERY, null);
                    break;
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public ProfileResponse getProfileData() {
        return profileData;
    }

    public void setProfileData(ProfileResponse profileData) {
        this.profileData = profileData;
    }
}

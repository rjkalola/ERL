package com.app.erl.view.activity;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.erl.R;
import com.app.erl.adapter.ServiceSelectedItemsTitleListAdapter;
import com.app.erl.callback.SelectTimeListener;
import com.app.erl.callback.SelectedServiceItemListener;
import com.app.erl.databinding.ActivityCreateOrderBinding;
import com.app.erl.model.entity.response.ClientDashBoardResponse;
import com.app.erl.util.AppConstant;
import com.app.erl.view.dialog.SelectTimeDialog;
import com.app.utilities.callbacks.OnDateSetListener;
import com.app.utilities.utils.DateFormatsConstants;
import com.app.utilities.utils.DateHelper;
import com.app.utilities.utils.StringHelper;
import com.app.utilities.view.fragments.DatePickerFragment;

import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class CreateOrderActivity extends BaseActivity implements View.OnClickListener
        , SelectedServiceItemListener, OnDateSetListener
        , SelectTimeListener, EasyPermissions.PermissionCallbacks {
    private ActivityCreateOrderBinding binding;
    private Context mContext;
    String fromTime, toTime;
    private ClientDashBoardResponse dashBoardData;
    private ServiceSelectedItemsTitleListAdapter adapter;
    private String DATE_PICKER = "DATE_PICKER";
    private String[] LOCATION_PERMISSION = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_order);
        mContext = this;

        binding.txtNext.setOnClickListener(this);
        binding.imgBack.setOnClickListener(this);
        binding.edtSelectDate.setOnClickListener(this);
        binding.edtSelectTime.setOnClickListener(this);
        binding.edtSelectTimeType.setOnClickListener(this);
        binding.txtChangeAddress.setOnClickListener(this);

        getIntentData();
    }

    public void getIntentData() {
        if (getIntent().getExtras() != null
                && Parcels.unwrap(getIntent().getParcelableExtra(AppConstant.IntentKey.DASHBOARD_DATA)) != null) {
            setDashBoardData(Parcels.unwrap(getIntent().getParcelableExtra(AppConstant.IntentKey.DASHBOARD_DATA)));
            setSelectedItemsAdapter();
        } else {
            finish();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtNext:

                break;
            case R.id.imgBack:
                finish();
                break;
            case R.id.edtSelectDate:
                Calendar c = Calendar.getInstance();
                if (!StringHelper.isEmpty(binding.edtSelectDate.getText().toString())) {
                    String date = DateHelper.changeDateFormat(binding.edtSelectDate.getText().toString(), DateFormatsConstants.DD_MMM_YYYY_SPACE, DateFormatsConstants.DD_MM_YYYY_DASH);
                    showDatePicker(c.getTime().getTime(), 0, DATE_PICKER, date);
                } else {
                    showDatePicker(c.getTime().getTime(), 0, DATE_PICKER, null);
                }
                break;
            case R.id.edtSelectTime:
                selectTimeDialog();
                break;
            case R.id.edtSelectTimeType:

                break;
            case R.id.txtChangeAddress:
                checkPermission();
                break;
        }
    }

    private void setSelectedItemsAdapter() {
        if (getDashBoardData() != null && getDashBoardData().getInfo() != null && getDashBoardData().getInfo().size() > 0) {
            binding.rvSelectedItems.setVisibility(View.VISIBLE);
            binding.rvSelectedItems.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            binding.rvSelectedItems.setHasFixedSize(true);
            adapter = new ServiceSelectedItemsTitleListAdapter(mContext, getDashBoardData().getInfo(), this);
            binding.rvSelectedItems.setAdapter(adapter);
        } else {
            binding.rvSelectedItems.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSelectServiceItem(int rootPosition, int itemPosition, int quantity) {
        Log.e("test", "rootPosition:" + rootPosition);
        Log.e("test", "itemPosition:" + itemPosition);
        Log.e("test", "quantity:" + quantity);
        getDashBoardData().getInfo().get(rootPosition).getService_item().get(itemPosition).setQuantity(quantity);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        if (view.getTag().toString().equals(DATE_PICKER)) {
            Calendar dobDate = Calendar.getInstance();
            dobDate.set(year, month, day);
            SimpleDateFormat dateFormat = new SimpleDateFormat(DateFormatsConstants.DD_MMMM_YYYY_SPACE, Locale.US);
            binding.edtSelectDate.setText(dateFormat.format(dobDate.getTime()));
            SimpleDateFormat dateFormat1 = new SimpleDateFormat(DateFormatsConstants.YYYY_MM_DD_DASH, Locale.US);
//            manageProfileViewModel.getProfileInfoRequest().setBirth_date(dateFormat1.format(dobDate.getTime()));
        }
    }

    @Override
    public void onSelectTime(String fromTime, String toTime, int identifier) {
        this.fromTime = fromTime;
        this.toTime = toTime;
        binding.edtSelectTime.setText(String.format(getString(R.string.lbl_display_time), fromTime, toTime));
    }

    public void showDatePicker(long minDate, long maxDate, String tag, String selDate) {
        DialogFragment newFragment = DatePickerFragment.newInstance(minDate, maxDate, selDate, DateFormatsConstants.DD_MM_YYYY_DASH, tag);
        newFragment.show(getSupportFragmentManager(), tag);
    }

    public void selectTimeDialog() {
        FragmentManager fm = ((BaseActivity) mContext).getSupportFragmentManager();
        SelectTimeDialog selectTimeDialog = SelectTimeDialog.newInstance(mContext, fromTime
                , toTime, this);
        selectTimeDialog.show(fm, "selectTimeDialog");
    }

    private boolean hasPermission() {
        return EasyPermissions.hasPermissions(this, LOCATION_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    public void checkPermission() {
        if (hasPermission()) {
            moveActivityForResult(mContext, AddAddressActivity.class, false, false, AppConstant.IntentKey.CHANGE_ADDRESS, null);
        } else {
            EasyPermissions.requestPermissions(
                    this,
                    getString(R.string.msg_location_permission),
                    AppConstant.IntentKey.RC_LOCATION_PERM,
                    LOCATION_PERMISSION);
        }
    }


    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        moveActivityForResult(mContext, AddAddressActivity.class, false, false, AppConstant.IntentKey.CHANGE_ADDRESS, null);
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    public ClientDashBoardResponse getDashBoardData() {
        return dashBoardData;
    }

    public void setDashBoardData(ClientDashBoardResponse dashBoardData) {
        this.dashBoardData = dashBoardData;
    }
}

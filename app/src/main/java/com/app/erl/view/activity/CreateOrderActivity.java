package com.app.erl.view.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.erl.R;
import com.app.erl.adapter.ServiceSelectedItemsTitleListAdapter;
import com.app.erl.callback.SelectTimeListener;
import com.app.erl.callback.SelectedServiceItemListener;
import com.app.erl.databinding.ActivityCreateOrderBinding;
import com.app.erl.model.entity.info.ClientDashBoardInfo;
import com.app.erl.model.entity.info.ModuleInfo;
import com.app.erl.model.entity.info.ModuleSelection;
import com.app.erl.model.entity.info.PickUpTimeInfo;
import com.app.erl.model.entity.info.ServiceItemInfo;
import com.app.erl.model.entity.response.AddressResourcesResponse;
import com.app.erl.model.entity.response.BaseResponse;
import com.app.erl.model.entity.response.ClientDashBoardResponse;
import com.app.erl.model.entity.response.OrderResourcesResponse;
import com.app.erl.util.AppConstant;
import com.app.erl.util.AppUtils;
import com.app.erl.util.LoginViewModelFactory;
import com.app.erl.util.PopupMenuHelper;
import com.app.erl.util.ResourceProvider;
import com.app.erl.view.dialog.SelectTimeDialog;
import com.app.erl.viewModel.ManageAddressViewModel;
import com.app.erl.viewModel.ManageOrderViewModel;
import com.app.utilities.callbacks.OnDateSetListener;
import com.app.utilities.utils.AlertDialogHelper;
import com.app.utilities.utils.DateFormatsConstants;
import com.app.utilities.utils.DateHelper;
import com.app.utilities.utils.StringHelper;
import com.app.utilities.utils.ToastHelper;
import com.app.utilities.utils.ValidationUtil;
import com.app.utilities.view.fragments.DatePickerFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    private ManageOrderViewModel manageOrderViewModel;
    private OrderResourcesResponse orderData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_order);
        mContext = this;

        manageOrderViewModel = ViewModelProviders.of(this, new LoginViewModelFactory(new ResourceProvider(getResources()))).get(ManageOrderViewModel.class);
        manageOrderViewModel.createView(this);
        manageOrderViewModel.orderResourcesResponse()
                .observe(this, orderResourcesResponse());
        manageOrderViewModel.mBaseResponse()
                .observe(this, saveOrderResponse());

        binding.txtNext.setOnClickListener(this);
        binding.imgBack.setOnClickListener(this);
        binding.edtSelectDate.setOnClickListener(this);
        binding.edtSelectTime.setOnClickListener(this);
        binding.txtChangeAddress.setOnClickListener(this);

        getIntentData();
    }

    public void getIntentData() {
        if (getIntent().getExtras() != null
                && Parcels.unwrap(getIntent().getParcelableExtra(AppConstant.IntentKey.DASHBOARD_DATA)) != null) {
            setDashBoardData(Parcels.unwrap(getIntent().getParcelableExtra(AppConstant.IntentKey.DASHBOARD_DATA)));
            setSelectedItemsAdapter();
            manageOrderViewModel.getOrderResourcesRequest();
        } else {
            finish();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtNext:
                if (validate()) {
                    manageOrderViewModel.saveAddressRequest();
                }
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
                if (getOrderData() != null
                        && getOrderData().getPickup_hours() != null
                        && getOrderData().getPickup_hours().size() > 0) {
                    showSelectTimeDialog(AppConstant.DialogIdentifier.SELECT_TIME, v);
                } else {
                    ToastHelper.error(mContext, getString(R.string.msg_select_address_first), Toast.LENGTH_SHORT, false);
                }
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

    public Observer orderResourcesResponse() {
        return (Observer<OrderResourcesResponse>) response -> {
            try {
                if (response == null) {
                    AlertDialogHelper.showDialog(mContext, null,
                            mContext.getString(R.string.error_unknown), mContext.getString(R.string.ok),
                            null, false, null, 0);
                    return;
                }
                if (response.isSuccess()) {
                    setOrderData(response);
                    binding.edtSelectTime.setText("");
                    manageOrderViewModel.getSaveOrderRequest().setPickup_hour_id(0);
                    if (getOrderData().getPickup_hours() != null && getOrderData().getInfo().getId() != 0) {
                        binding.txtAddress.setText(getOrderData().getInfo().getAddress());
                        manageOrderViewModel.getSaveOrderRequest().setAddress_id(getOrderData().getInfo().getId());
                    } else {
                        binding.txtAddress.setText("");
                        manageOrderViewModel.getSaveOrderRequest().setAddress_id(0);
                    }
                } else {
                    AppUtils.handleUnauthorized(mContext, response);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    public Observer saveOrderResponse() {
        return (Observer<BaseResponse>) response -> {
            try {
                if (response == null) {
                    AlertDialogHelper.showDialog(mContext, null,
                            mContext.getString(R.string.error_unknown), mContext.getString(R.string.ok),
                            null, false, null, 0);
                    return;
                }
                if (response.isSuccess()) {
                    ToastHelper.success(mContext, getString(R.string.msg_order_successfully_placed), Toast.LENGTH_SHORT, false);
                    moveActivity(mContext, DashBoardActivity.class, true, true, null);
                } else {
                    AppUtils.handleUnauthorized(mContext, response);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    @Override
    public void onSelectServiceItem(int rootPosition, int itemPosition, int quantity) {
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
            manageOrderViewModel.getSaveOrderRequest().setPickup_date(dateFormat1.format(dobDate.getTime()));
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

    public void showSelectTimeDialog(int dialogIdentifier, View v) {
        List<ModuleInfo> list = new ArrayList<>();
        ModuleInfo moduleInfo = null;
        for (PickUpTimeInfo info : getOrderData().getPickup_hours()) {
            moduleInfo = new ModuleInfo();
            moduleInfo.setId(info.getId());
            moduleInfo.setName(String.format(getString(R.string.lbl_display_time), info.getStart_time(), info.getEnd_time()));
            list.add(moduleInfo);
        }
        PopupMenuHelper.showPopupMenu(mContext, v, list, dialogIdentifier);
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
            moveActivityForResult(mContext, SelectAddressActivity.class, false, false, AppConstant.IntentKey.CHANGE_ADDRESS, null);
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
        moveActivityForResult(mContext, SelectAddressActivity.class, false, false, AppConstant.IntentKey.CHANGE_ADDRESS, null);
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case AppConstant.IntentKey.CHANGE_ADDRESS:
                if (resultCode == 1)
                    manageOrderViewModel.getOrderResourcesRequest();
                break;
            default:
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ModuleSelection moduleInfo) {
        if (moduleInfo != null) {
            if (moduleInfo.getType() == AppConstant.DialogIdentifier.SELECT_TIME) {
                binding.edtSelectTime.setText(moduleInfo.getInfo().getName());
                manageOrderViewModel.getSaveOrderRequest().setPickup_hour_id(moduleInfo.getInfo().getId());
            }
        }
    }

    private boolean validate() {
        boolean valid = true;
        if (ValidationUtil.isEmptyEditText(manageOrderViewModel.getSaveOrderRequest().getPickup_date())) {
            ToastHelper.error(mContext, getString(R.string.error_empty_order_date), Toast.LENGTH_LONG, false);
            valid = false;
            return valid;
        }
        if (manageOrderViewModel.getSaveOrderRequest().getPickup_hour_id() == 0) {
            ToastHelper.error(mContext, getString(R.string.error_empty_order_time), Toast.LENGTH_LONG, false);
            valid = false;
            return valid;
        }
        if (manageOrderViewModel.getSaveOrderRequest().getAddress_id() == 0) {
            ToastHelper.error(mContext, getString(R.string.error_empty_address), Toast.LENGTH_LONG, false);
            valid = false;
            return valid;
        }

        List<ServiceItemInfo> order = new ArrayList<>();
        for (int i = 0; i < getDashBoardData().getInfo().size(); i++) {
            for (int j = 0; j < getDashBoardData().getInfo().get(i).getService_item().size(); j++) {
                if (getDashBoardData().getInfo().get(i).getService_item().get(j).getQuantity() > 0) {
                    order.add(getDashBoardData().getInfo().get(i).getService_item().get(j));
                }
            }
        }
        manageOrderViewModel.getSaveOrderRequest().setOrder(order);

        if (manageOrderViewModel.getSaveOrderRequest().getOrder().size() == 0) {
            ToastHelper.error(mContext, getString(R.string.error_select_at_least_one_item), Toast.LENGTH_LONG, false);
            valid = false;
            return valid;
        }

        return valid;
    }

    public ClientDashBoardResponse getDashBoardData() {
        return dashBoardData;
    }

    public void setDashBoardData(ClientDashBoardResponse dashBoardData) {
        this.dashBoardData = dashBoardData;
    }

    public OrderResourcesResponse getOrderData() {
        return orderData;
    }

    public void setOrderData(OrderResourcesResponse orderData) {
        this.orderData = orderData;
    }
}

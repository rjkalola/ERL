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
import com.app.erl.databinding.ActivityConfirmOrderBinding;
import com.app.erl.databinding.ActivityCreateOrderBinding;
import com.app.erl.model.entity.info.ItemInfo;
import com.app.erl.model.entity.info.ModuleInfo;
import com.app.erl.model.entity.info.ModuleSelection;
import com.app.erl.model.entity.info.PickUpTimeInfo;
import com.app.erl.model.entity.info.ServiceItemInfo;
import com.app.erl.model.entity.response.BaseResponse;
import com.app.erl.model.entity.response.OrderResourcesResponse;
import com.app.erl.util.AppConstant;
import com.app.erl.util.AppUtils;
import com.app.erl.util.LoginViewModelFactory;
import com.app.erl.util.PopupMenuHelper;
import com.app.erl.util.ResourceProvider;
import com.app.erl.view.dialog.SelectTimeDialog;
import com.app.erl.viewModel.ManageOrderViewModel;
import com.app.utilities.callbacks.DialogButtonClickListener;
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
import java.util.Date;
import java.util.List;
import java.util.Locale;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class ConfirmOrderActivity extends BaseActivity implements View.OnClickListener
        , SelectedServiceItemListener, DialogButtonClickListener
        , EasyPermissions.PermissionCallbacks {
    private ActivityConfirmOrderBinding binding;
    private Context mContext;
    private String fromTime, toTime;
    private int serviceHourTypeId = 0, orderType = 0;
    private ServiceSelectedItemsTitleListAdapter adapter;
    private String DATE_PICKER = "DATE_PICKER", DELIVER_DATE_PICKER = "DELIVER_DATE_PICKER";
    private String[] LOCATION_PERMISSION = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
    private ManageOrderViewModel manageOrderViewModel;
    private OrderResourcesResponse orderData;
    private List<ItemInfo> listItems;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_confirm_order);
        mContext = this;
        listItems = new ArrayList<>();
        manageOrderViewModel = ViewModelProviders.of(this, new LoginViewModelFactory(new ResourceProvider(getResources()))).get(ManageOrderViewModel.class);
        manageOrderViewModel.createView(this);
        manageOrderViewModel.orderResourcesResponse()
                .observe(this, orderResourcesResponse());
        manageOrderViewModel.mBaseResponse()
                .observe(this, saveOrderResponse());

        binding.txtNext.setOnClickListener(this);
        binding.imgBack.setOnClickListener(this);
        binding.txtChangeAddress.setOnClickListener(this);

        getIntentData();
    }

    public void getIntentData() {
        if (getIntent().getExtras() != null) {
            if (Parcels.unwrap(getIntent().getParcelableExtra(AppConstant.IntentKey.ITEMS_LIST)) != null)
                listItems = Parcels.unwrap(getIntent().getParcelableExtra(AppConstant.IntentKey.ITEMS_LIST));

            serviceHourTypeId = getIntent().getIntExtra(AppConstant.IntentKey.SERVICE_HOUR_TYPE_ID, 0);
            orderType = getIntent().getIntExtra(AppConstant.IntentKey.ORDER_TYPE, 0);
            manageOrderViewModel.getSaveOrderRequest().setLu_service_hour_type_id(serviceHourTypeId);
            manageOrderViewModel.getSaveOrderRequest().setType(orderType);

            setSelectedItemsAdapter();

            manageOrderViewModel.getOrderResourcesRequest();
        } else {
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        Calendar c = Calendar.getInstance();
        switch (v.getId()) {
            case R.id.txtNext:
                if (validate()) {
//                    AlertDialogHelper.showDialog(mContext, null, getString(R.string.msg_place_order), getString(R.string.yes), getString(R.string.no), true, this, AppConstant.DialogIdentifier.PLACE_ORDER);
                }
                break;
            case R.id.imgBack:
                finish();
                break;
            case R.id.txtChangeAddress:
                checkPermission();
                break;
        }
    }

    private void setSelectedItemsAdapter() {
        if (listItems.size() > 0) {
            binding.routOrderList.setVisibility(View.VISIBLE);
            binding.rvSelectedItems.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            binding.rvSelectedItems.setHasFixedSize(true);
            adapter = new ServiceSelectedItemsTitleListAdapter(mContext, listItems, this, true);
            binding.rvSelectedItems.setAdapter(adapter);
        } else {
            binding.routOrderList.setVisibility(View.GONE);
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
                    moveActivity(mContext, OrderCompletedActivity.class, true, true, null);
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
        listItems.get(rootPosition).getServiceList().get(itemPosition).setQuantity(quantity);
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

        if (orderType == 0) {
            List<ServiceItemInfo> order = new ArrayList<>();
            for (int i = 0; i < listItems.size(); i++) {
                for (int j = 0; j < listItems.get(i).getServiceList().size(); j++) {
                    ServiceItemInfo info = listItems.get(i).getServiceList().get(j);
                    if (info.getQuantity() > 0) {
                        ServiceItemInfo serviceItemInfo = new ServiceItemInfo(listItems.get(i).getId(), info.getId(), info.getQuantity());
                        order.add(serviceItemInfo);
                    }
                }
            }
            manageOrderViewModel.getSaveOrderRequest().setOrder(order);

            if (manageOrderViewModel.getSaveOrderRequest().getOrder().size() == 0) {
                ToastHelper.error(mContext, getString(R.string.error_select_at_least_one_item), Toast.LENGTH_LONG, false);
                valid = false;
                return valid;
            }
        }

        return valid;
    }

    @Override
    public void onPositiveButtonClicked(int dialogIdentifier) {
        if (dialogIdentifier == AppConstant.DialogIdentifier.PLACE_ORDER) {
            manageOrderViewModel.getSaveOrderRequest().setDelivery_note(binding.edtNote.getText().toString().trim());
            manageOrderViewModel.saveAddressRequest();
        }
    }

    @Override
    public void onNegativeButtonClicked(int dialogIdentifier) {

    }

    public OrderResourcesResponse getOrderData() {
        return orderData;
    }

    public void setOrderData(OrderResourcesResponse orderData) {
        this.orderData = orderData;
    }
}

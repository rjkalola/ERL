package com.app.erl.view.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.TextView;
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
import com.app.erl.model.entity.info.ItemInfo;
import com.app.erl.model.entity.info.ModuleInfo;
import com.app.erl.model.entity.info.ModuleSelection;
import com.app.erl.model.entity.info.PickUpTimeInfo;
import com.app.erl.model.entity.info.ServiceItemInfo;
import com.app.erl.model.entity.response.BaseResponse;
import com.app.erl.model.entity.response.OrderResourcesResponse;
import com.app.erl.model.entity.response.PromoCodeResponse;
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

public class CreateOrderActivity extends BaseActivity implements View.OnClickListener
        , SelectedServiceItemListener, OnDateSetListener, DialogButtonClickListener
        , SelectTimeListener, EasyPermissions.PermissionCallbacks {
    private ActivityCreateOrderBinding binding;
    private Context mContext;
    private String fromTime, toTime;
    private int serviceHourTypeId = 0, orderType = 0, totalAmount;
    private ServiceSelectedItemsTitleListAdapter adapter;
    private String DATE_PICKER = "DATE_PICKER", DELIVER_DATE_PICKER = "DELIVER_DATE_PICKER";
    private String[] LOCATION_PERMISSION = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
    private ManageOrderViewModel manageOrderViewModel;
    private OrderResourcesResponse orderData;
    private List<ItemInfo> listItems;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_order);
        mContext = this;
        listItems = new ArrayList<>();
        manageOrderViewModel = ViewModelProviders.of(this, new LoginViewModelFactory(new ResourceProvider(getResources()))).get(ManageOrderViewModel.class);
        manageOrderViewModel.createView(this);
        manageOrderViewModel.orderResourcesResponse()
                .observe(this, orderResourcesResponse());
        manageOrderViewModel.mBaseResponse()
                .observe(this, saveOrderResponse());
        manageOrderViewModel.mPromoCodeResponse()
                .observe(this, checkPromoCodeResponse());

        binding.txtNext.setOnClickListener(this);
        binding.imgBack.setOnClickListener(this);
        binding.edtSelectDate.setOnClickListener(this);
        binding.edtSelectTime.setOnClickListener(this);
        binding.edtSelectDeliverDate.setOnClickListener(this);
        binding.edtSelectDeliverTime.setOnClickListener(this);
        binding.txtChangeAddress.setOnClickListener(this);
        binding.txtApplyNow.setOnClickListener(this);

        binding.cbWalletBalance.setOnCheckedChangeListener((buttonView, isChecked) -> {
            manageOrderViewModel.getSaveOrderRequest().setDeduct_wallet(isChecked);
            if (isChecked)
                binding.routWalletBalance.setVisibility(View.VISIBLE);
            else
                binding.routWalletBalance.setVisibility(View.GONE);
            calculateTotalAmount();
        });

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

            if (orderType == 0) {
                binding.routOrderList.setVisibility(View.VISIBLE);
                binding.routManageAmount.setVisibility(View.VISIBLE);
                setSelectedItemsAdapter();
            } else {
                binding.routOrderList.setVisibility(View.GONE);
                binding.routManageAmount.setVisibility(View.GONE);
            }

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
                    manageOrderViewModel.getSaveOrderRequest().setDelivery_note(binding.edtNote.getText().toString().trim());
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(AppConstant.IntentKey.ITEMS_LIST, Parcels.wrap(listItems));
                    bundle.putParcelable(AppConstant.IntentKey.ORDER_DATA, Parcels.wrap(manageOrderViewModel.getSaveOrderRequest()));
                    bundle.putInt(AppConstant.IntentKey.SERVICE_HOUR_TYPE_ID, serviceHourTypeId);
                    bundle.putInt(AppConstant.IntentKey.ORDER_TYPE, orderType);

                    Intent intent = new Intent(mContext, ConfirmOrderActivity.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, AppConstant.IntentKey.VIEW_CART);
                }
                break;
            case R.id.imgBack:
                finish();
                break;
            case R.id.edtSelectDate:
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
                    ToastHelper.error(mContext, getString(R.string.msg_currently_no_service_available), Toast.LENGTH_SHORT, false);
                }
                break;
            case R.id.edtSelectDeliverDate:
                String date = DateHelper.changeDateFormat(binding.edtSelectDate.getText().toString(), DateFormatsConstants.DD_MMM_YYYY_SPACE, DateFormatsConstants.DD_MM_YYYY_DASH);
                if (!StringHelper.isEmpty(date)) {
                    try {
                        c.setTime(DateHelper.stringToDate(date, DateFormatsConstants.DD_MM_YYYY_DASH));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    int day = 0;
                    switch (serviceHourTypeId) {
                        case 1:
                            day = 3;
                            break;
                        case 2:
                            day = 2;
                            break;
                        case 3:
                            day = 0;
                            break;
                    }
                    c.add(Calendar.DAY_OF_WEEK, day);
                    Date newDate = c.getTime();

                    if (!StringHelper.isEmpty(binding.edtSelectDate.getText().toString())) {
                        String date_ = DateHelper.changeDateFormat(binding.edtSelectDate.getText().toString(), DateFormatsConstants.DD_MMM_YYYY_SPACE, DateFormatsConstants.DD_MM_YYYY_DASH);
                        showDatePicker(newDate.getTime(), 0, DELIVER_DATE_PICKER, date_);
                    } else {
                        showDatePicker(newDate.getTime(), 0, DELIVER_DATE_PICKER, null);
                    }
                } else {
                    ToastHelper.error(mContext, getString(R.string.error_empty_order_date), Toast.LENGTH_LONG, false);
                }
                break;
            case R.id.edtSelectDeliverTime:
                if (!StringHelper.isEmpty(binding.edtSelectDeliverDate.getText().toString().trim())) {
                    if (getOrderData() != null
                            && getOrderData().getPickup_hours() != null
                            && getOrderData().getPickup_hours().size() > 0) {
                        showSelectTimeDialog(AppConstant.DialogIdentifier.SELECT_DELIVER_TIME, v);
                    } else {
                        ToastHelper.error(mContext, getString(R.string.msg_currently_no_service_available), Toast.LENGTH_SHORT, false);
                    }
                } else {
                    ToastHelper.error(mContext, getString(R.string.error_empty_delivery_date), Toast.LENGTH_SHORT, false);
                }
                break;
            case R.id.txtChangeAddress:
                checkPermission();
                break;
            case R.id.txtApplyNow:
                if (manageOrderViewModel.getSaveOrderRequest().getPromo_amount() == 0) {
                    if (!StringHelper.isEmpty(binding.edtPromoCode.getText().toString()))
                        manageOrderViewModel.checkPromoCodeRequest(binding.edtPromoCode.getText().toString().trim());
                    else
                        ToastHelper.error(mContext, getString(R.string.error_empty_promo_code), Toast.LENGTH_SHORT, false);
                } else {
                    manageOrderViewModel.getSaveOrderRequest().setPromo_amount(0);
                    manageOrderViewModel.getSaveOrderRequest().setPromo_code("");

                    binding.edtPromoCode.setEnabled(true);
                    binding.txtPromoCodeApplied.setVisibility(View.GONE);
                    binding.txtApplyNow.setText(getString(R.string.lbl_apply_now));
                    binding.edtPromoCode.setText("");
                    calculateTotalAmount();
                }
                break;
        }
    }

    private void setSelectedItemsAdapter() {
        if (listItems.size() > 0) {
            binding.routOrderList.setVisibility(View.VISIBLE);
            binding.rvSelectedItems.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            binding.rvSelectedItems.setHasFixedSize(true);
            adapter = new ServiceSelectedItemsTitleListAdapter(mContext, listItems, this, false);
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
                    binding.edtSelectTime.setText("");
                    manageOrderViewModel.getSaveOrderRequest().setPickup_hour_id(0);
                    if (getOrderData().getPickup_hours() != null && getOrderData().getInfo().getId() != 0) {
                        binding.txtAddress.setText(getOrderData().getInfo().getAddress());
                        manageOrderViewModel.getSaveOrderRequest().setAddress(getOrderData().getInfo().getAddress());
                        manageOrderViewModel.getSaveOrderRequest().setAddress_id(getOrderData().getInfo().getId());
                    } else {
                        binding.txtAddress.setText("");
                        manageOrderViewModel.getSaveOrderRequest().setAddress("");
                        manageOrderViewModel.getSaveOrderRequest().setAddress_id(0);
                    }

                    if (orderType == 0) {
                        manageOrderViewModel.getSaveOrderRequest().setWallet_balance(response.getWallet());
                        binding.cbWalletBalance.setText(String.format(mContext.getString(R.string.lbl_display_wallet_balance), String.valueOf(response.getWallet())));
                        calculateTotalAmount();
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

    public Observer checkPromoCodeResponse() {
        return (Observer<PromoCodeResponse>) response -> {
            try {
                if (response == null) {
                    AlertDialogHelper.showDialog(mContext, null,
                            mContext.getString(R.string.error_unknown), mContext.getString(R.string.ok),
                            null, false, null, 0);
                    return;
                }
                if (response.isSuccess()) {
                    manageOrderViewModel.getSaveOrderRequest().setPromo_code(binding.edtPromoCode.getText().toString().trim());
                    manageOrderViewModel.getSaveOrderRequest().setPromo_amount(response.getAmount());

                    binding.edtPromoCode.setEnabled(false);
                    binding.txtPromoCodeApplied.setVisibility(View.VISIBLE);
                    binding.txtApplyNow.setText(getString(R.string.lbl_remove));

                    calculateTotalAmount();
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
        Log.e("test", "rootPosition:" + rootPosition + "  " + "itemPosition:" + itemPosition + "  " + "quantity:" + quantity);
        listItems.get(rootPosition).getServiceList().get(itemPosition).setQuantity(quantity);
        calculateTotalAmount();
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

            binding.edtSelectDeliverDate.setText("");
            manageOrderViewModel.getSaveOrderRequest().setDeliver_date("");

            binding.edtSelectDeliverTime.setText("");
            manageOrderViewModel.getSaveOrderRequest().setDeliver_hour_id(0);

        } else if (view.getTag().toString().equals(DELIVER_DATE_PICKER)) {
            Calendar dobDate = Calendar.getInstance();
            dobDate.set(year, month, day);
            SimpleDateFormat dateFormat = new SimpleDateFormat(DateFormatsConstants.DD_MMMM_YYYY_SPACE, Locale.US);
            binding.edtSelectDeliverDate.setText(dateFormat.format(dobDate.getTime()));
            SimpleDateFormat dateFormat1 = new SimpleDateFormat(DateFormatsConstants.YYYY_MM_DD_DASH, Locale.US);
            manageOrderViewModel.getSaveOrderRequest().setDeliver_date(dateFormat1.format(dobDate.getTime()));
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
                manageOrderViewModel.getSaveOrderRequest().setPickup_hour(moduleInfo.getInfo().getName());
            } else if (moduleInfo.getType() == AppConstant.DialogIdentifier.SELECT_DELIVER_TIME) {
                binding.edtSelectDeliverTime.setText(moduleInfo.getInfo().getName());
                manageOrderViewModel.getSaveOrderRequest().setDeliver_hour_id(moduleInfo.getInfo().getId());
                manageOrderViewModel.getSaveOrderRequest().setDeliver_hour(moduleInfo.getInfo().getName());
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
        if (ValidationUtil.isEmptyEditText(manageOrderViewModel.getSaveOrderRequest().getDeliver_date())) {
            ToastHelper.error(mContext, getString(R.string.error_empty_delivery_date), Toast.LENGTH_LONG, false);
            valid = false;
            return valid;
        }
        if (manageOrderViewModel.getSaveOrderRequest().getDeliver_hour_id() == 0) {
            ToastHelper.error(mContext, getString(R.string.error_empty_delivery_time), Toast.LENGTH_LONG, false);
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

    public void calculateTotalAmount() {
        boolean isValid = false;
        totalAmount = 0;
        for (int i = 0; i < listItems.size(); i++) {
            for (int j = 0; j < listItems.get(i).getServiceList().size(); j++) {
                ServiceItemInfo info = listItems.get(i).getServiceList().get(j);
                if (info.getQuantity() > 0) {
                    isValid = true;
                    totalAmount = totalAmount + info.getPrice() * info.getQuantity();
                }
            }
        }

        if (isValid) {
            setPrice(binding.txtFinalAmount, String.valueOf(totalAmount));

            int tax = Math.round((float) totalAmount * 5 / 100);
            setPrice(binding.txtTotalTax, String.valueOf(tax));
            totalAmount = totalAmount + tax;

            setPrice(binding.txtTotalAmount, String.valueOf(totalAmount));

            if (manageOrderViewModel.getSaveOrderRequest().getPromo_amount() != 0) {
                binding.routPromoCode.setVisibility(View.VISIBLE);
                int promoDiscount = Math.round((float) totalAmount * manageOrderViewModel.getSaveOrderRequest().getPromo_amount() / 100);
                setPrice(binding.txtPromoCode, String.valueOf(promoDiscount));
                totalAmount = totalAmount - promoDiscount;
            } else {
                binding.routPromoCode.setVisibility(View.GONE);
            }

            if (binding.cbWalletBalance.isChecked()) {
                if (totalAmount >= getOrderData().getWallet()) {
                    totalAmount = totalAmount - getOrderData().getWallet();
                    setPrice(binding.txtWalletBalance, String.valueOf(getOrderData().getWallet()));
                } else {
                    setPrice(binding.txtWalletBalance, String.valueOf(totalAmount));
                    totalAmount = 0;
                }
            }

            if (manageOrderViewModel.getSaveOrderRequest().getPromo_amount() != 0 || binding.cbWalletBalance.isChecked())
                binding.routPromoWalletAmountView.setVisibility(View.VISIBLE);
            else
                binding.routPromoWalletAmountView.setVisibility(View.GONE);

            setPrice(binding.txtTotalPayableAmount, String.valueOf(totalAmount));
        } else {
            setPrice(binding.txtTotalAmount, String.valueOf(0));
            setPrice(binding.txtFinalAmount, String.valueOf(0));
            setPrice(binding.txtTotalTax, String.valueOf(0));
            setPrice(binding.txtTotalPayableAmount, String.valueOf(0));
            binding.routPromoWalletAmountView.setVisibility(View.GONE);
        }
    }

    public void setPrice(TextView textView, String price) {
        textView.setText(String.format(mContext.getString(R.string.lbl_display_price), price));
    }

    public OrderResourcesResponse getOrderData() {
        return orderData;
    }

    public void setOrderData(OrderResourcesResponse orderData) {
        this.orderData = orderData;
    }
}

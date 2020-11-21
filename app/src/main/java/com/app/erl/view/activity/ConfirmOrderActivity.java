package com.app.erl.view.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.erl.R;
import com.app.erl.adapter.ServiceSelectedItemsTitleListAdapter;
import com.app.erl.callback.SelectedServiceItemListener;
import com.app.erl.databinding.ActivityConfirmOrderBinding;
import com.app.erl.model.entity.info.ItemInfo;
import com.app.erl.model.entity.info.ServiceItemInfo;
import com.app.erl.model.entity.response.BaseResponse;
import com.app.erl.model.entity.response.OrderResourcesResponse;
import com.app.erl.util.AppConstant;
import com.app.erl.util.AppUtils;
import com.app.erl.util.LoginViewModelFactory;
import com.app.erl.util.ResourceProvider;
import com.app.erl.viewModel.ManageOrderViewModel;
import com.app.utilities.callbacks.DialogButtonClickListener;
import com.app.utilities.utils.AlertDialogHelper;
import com.app.utilities.utils.StringHelper;
import com.app.utilities.utils.ToastHelper;
import com.app.utilities.utils.ValidationUtil;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class ConfirmOrderActivity extends BaseActivity implements View.OnClickListener
        , SelectedServiceItemListener, DialogButtonClickListener
        , EasyPermissions.PermissionCallbacks {
    private ActivityConfirmOrderBinding binding;
    private Context mContext;
    private ServiceSelectedItemsTitleListAdapter adapter;
    private String[] LOCATION_PERMISSION = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
    private ManageOrderViewModel manageOrderViewModel;

    private List<ItemInfo> listItems;
    private int totalAmount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_confirm_order);
        mContext = this;
        listItems = new ArrayList<>();
        manageOrderViewModel = ViewModelProviders.of(this, new LoginViewModelFactory(new ResourceProvider(getResources()))).get(ManageOrderViewModel.class);
        manageOrderViewModel.createView(this);
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

            if (Parcels.unwrap(getIntent().getParcelableExtra(AppConstant.IntentKey.ORDER_DATA)) != null)
                manageOrderViewModel.setSaveOrderRequest(Parcels.unwrap(getIntent().getParcelableExtra(AppConstant.IntentKey.ORDER_DATA)));

            if (manageOrderViewModel.getSaveOrderRequest() != null) {
                Log.e("test", "isDeduct_wallet:" + manageOrderViewModel.getSaveOrderRequest().isDeduct_wallet());
                binding.setSaveOrderRequest(manageOrderViewModel.getSaveOrderRequest());
                binding.txtPickUpTime.setText(manageOrderViewModel.getSaveOrderRequest().getPickup_date() + ", " + manageOrderViewModel.getSaveOrderRequest().getPickup_hour());
                binding.txtDeliverTime.setText(manageOrderViewModel.getSaveOrderRequest().getDeliver_date() + ", " + manageOrderViewModel.getSaveOrderRequest().getDeliver_hour());

                if (!StringHelper.isEmpty(manageOrderViewModel.getSaveOrderRequest().getDelivery_note()))
                    binding.routOrderNote.setVisibility(View.VISIBLE);
                else
                    binding.routOrderNote.setVisibility(View.GONE);

                if (manageOrderViewModel.getSaveOrderRequest().getType() == 0) {
                    binding.routOrderList.setVisibility(View.VISIBLE);
                    binding.routManageAmount.setVisibility(View.VISIBLE);
                    calculateTotalAmount();
                    setSelectedItemsAdapter();
                } else {
                    binding.routOrderList.setVisibility(View.GONE);
                    binding.routManageAmount.setVisibility(View.GONE);
                }
            }
        } else {
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        Calendar c = Calendar.getInstance();
        switch (v.getId()) {
            case R.id.txtNext:
//                if (validate()) {
                AlertDialogHelper.showDialog(mContext, null, getString(R.string.msg_place_order), getString(R.string.yes), getString(R.string.no), true, this, AppConstant.DialogIdentifier.PLACE_ORDER);
//                }
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

        if (manageOrderViewModel.getSaveOrderRequest().getType() == 0) {
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

            if (manageOrderViewModel.getSaveOrderRequest().isDeduct_wallet()) {
                binding.routWalletBalance.setVisibility(View.VISIBLE);
                int walletDiscount = manageOrderViewModel.getSaveOrderRequest().getWallet_balance() * manageOrderViewModel.getSaveOrderRequest().getMax_wallet_deduction() /100 ;
                if (totalAmount >= walletDiscount) {
                    totalAmount = totalAmount - walletDiscount;
                    setPrice(binding.txtWalletBalance, String.valueOf(walletDiscount));
                } else {
                    setPrice(binding.txtWalletBalance, String.valueOf(totalAmount));
                    totalAmount = 0;
                }
            } else {
                binding.routWalletBalance.setVisibility(View.GONE);
            }

            if (manageOrderViewModel.getSaveOrderRequest().getPromo_amount() != 0 || manageOrderViewModel.getSaveOrderRequest().isDeduct_wallet())
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

    @Override
    public void onPositiveButtonClicked(int dialogIdentifier) {
        if (dialogIdentifier == AppConstant.DialogIdentifier.PLACE_ORDER) {
            manageOrderViewModel.saveAddressRequest();
        }
    }

    @Override
    public void onNegativeButtonClicked(int dialogIdentifier) {

    }


}

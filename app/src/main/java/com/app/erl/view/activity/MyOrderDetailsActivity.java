package com.app.erl.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Keep;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.erl.R;
import com.app.erl.adapter.MyOrderServiceItemsListAdapter;
import com.app.erl.databinding.ActivityMyOrderDetailsBinding;
import com.app.erl.model.entity.response.BaseResponse;
import com.app.erl.model.entity.response.OrderDetailsResponse;
import com.app.erl.util.AppConstant;
import com.app.erl.util.AppUtils;
import com.app.erl.util.LoginViewModelFactory;
import com.app.erl.util.ResourceProvider;
import com.app.erl.viewModel.ManageOrderViewModel;
import com.app.utilities.callbacks.DialogButtonClickListener;
import com.app.utilities.utils.AlertDialogHelper;

@Keep
public class MyOrderDetailsActivity extends BaseActivity implements View.OnClickListener, DialogButtonClickListener {
    private ActivityMyOrderDetailsBinding binding;
    private Context mContext;
    private MyOrderServiceItemsListAdapter adapter;
    private ManageOrderViewModel manageOrderViewModel;
    private OrderDetailsResponse orderDetails;
    private int orderId;
    public static final boolean isSecurityEnabled = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_order_details);
        mContext = this;

        manageOrderViewModel = ViewModelProviders.of(this, new LoginViewModelFactory(new ResourceProvider(getResources()))).get(ManageOrderViewModel.class);
        manageOrderViewModel.createView(this);
        manageOrderViewModel.mOrderDetailsResponse()
                .observe(this, orderDetailsResponse());
        manageOrderViewModel.mBaseResponse()
                .observe(this, cancelOrderResponse());

        binding.imgBack.setOnClickListener(this);
        binding.txtPay.setOnClickListener(this);

        getIntentData();
    }

    public void getIntentData() {
        if (getIntent().getExtras() != null && getIntent().hasExtra(AppConstant.IntentKey.ORDER_ID)) {
            orderId = getIntent().getIntExtra(AppConstant.IntentKey.ORDER_ID, 0);
            AppConstant.ORDER_ID = orderId;
            manageOrderViewModel.clientOrderDetailsRequest(orderId);
        } else {
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.txtPay:
//                Bundle bundle = new Bundle();
//                bundle.putBoolean(AppConstant.IntentKey.FROM_PAY, true);
//                moveActivity(mContext, OrderCompletedActivity.class, true, true, bundle);
//                AlertDialogHelper.showDialog(mContext, null, getString(R.string.msg_cancel_order), getString(R.string.yes), getString(R.string.no), true, this, AppConstant.DialogIdentifier.CANCEL_ORDER);
//                sendMessage(getOrderDetails().getAmount_pay());
                AppUtils.sendMessage(mContext, getOrderDetails().getAmount_pay());
                break;
        }
    }

    private void setAddressAdapter() {
        if (getOrderDetails() != null
                && getOrderDetails().getInfo() != null
                && getOrderDetails().getInfo().size() > 0) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            binding.rvOrderItems.setLayoutManager(linearLayoutManager);
            binding.rvOrderItems.setHasFixedSize(true);
            adapter = new MyOrderServiceItemsListAdapter(mContext, getOrderDetails().getInfo());
            binding.rvOrderItems.setAdapter(adapter);
        }
    }

    public Observer cancelOrderResponse() {
        return (Observer<BaseResponse>) response -> {
            try {
                if (response == null) {
                    AlertDialogHelper.showDialog(mContext, null,
                            mContext.getString(R.string.error_unknown), mContext.getString(R.string.ok),
                            null, false, null, 0);
                    return;
                }
                if (response.isSuccess()) {
                    setResult(1);
                    finish();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    public Observer orderDetailsResponse() {
        return (Observer<OrderDetailsResponse>) response -> {
            try {
                if (response == null) {
                    AlertDialogHelper.showDialog(mContext, null,
                            mContext.getString(R.string.error_unknown), mContext.getString(R.string.ok),
                            null, false, null, 0);
                    return;
                }
                if (response.isSuccess()) {
                    setOrderDetails(response);
                    binding.routMainView.setVisibility(View.VISIBLE);
                    if (response.isShow_payment_button())
                        binding.btnPay.setVisibility(View.VISIBLE);
                    binding.txtTotalPrice.setText(String.format(mContext.getString(R.string.lbl_display_price), getOrderDetails().getTotal_price()));
                    binding.txtTotalPayableAmount.setText(String.format(mContext.getString(R.string.lbl_display_price), getOrderDetails().getAmount_pay()));
                    binding.txtAvailableWallet.setText(String.format(mContext.getString(R.string.lbl_display_price), String.valueOf(getOrderDetails().getWallet())));
                    binding.txtOrderNumber.setText(getOrderDetails().getOrder_no());
                    binding.txtServiceType.setText(getOrderDetails().getService_type_name());
                    binding.txtPickUpTime.setText(getOrderDetails().getPickup_date() + "  " + getOrderDetails().getPickup_time());
                    binding.txtDeliverTime.setText(getOrderDetails().getDeliver_date() + "  " + getOrderDetails().getDelivery_time());

                    AppConstant.PAYMENT_CITY = response.getCity_name();
                    AppConstant.PAYMENT_ADDRESS = response.getAddress();

                    setAddressAdapter();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    @Override
    public void onPositiveButtonClicked(int dialogIdentifier) {
        if (dialogIdentifier == AppConstant.DialogIdentifier.CANCEL_ORDER) {
//            manageOrderViewModel.clientCancelOrders(getOrderInfo().getId());
        }
    }

    @Override
    public void onNegativeButtonClicked(int dialogIdentifier) {

    }

    public OrderDetailsResponse getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(OrderDetailsResponse orderDetails) {
        this.orderDetails = orderDetails;
    }

//    public void sendMessage(String amount) {
//        Intent intent = new Intent(MyOrderDetailsActivity.this, WebviewActivity.class);
//        // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
//        intent.putExtra(WebviewActivity.EXTRA_MESSAGE, AppUtils.getMobileRequest(amount));
//        intent.putExtra(WebviewActivity.SUCCESS_ACTIVTY_CLASS_NAME, "com.app.erl.view.activity.SuccessTransactionActivity");
//        intent.putExtra(WebviewActivity.FAILED_ACTIVTY_CLASS_NAME, "com.app.erl.view.activity.FailedTransactionActivity");
//        intent.putExtra(WebviewActivity.IS_SECURITY_ENABLED, isSecurityEnabled);
//        startActivity(intent);
//    }

}

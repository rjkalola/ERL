package com.app.erl.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.erl.R;
import com.app.erl.adapter.MyOrderServiceItemsListAdapter;
import com.app.erl.databinding.ActivityMyOrderDetailsBinding;
import com.app.erl.model.entity.info.OrderInfo;
import com.app.erl.model.entity.response.BaseResponse;
import com.app.erl.model.entity.response.OrderDetailsResponse;
import com.app.erl.util.AppConstant;
import com.app.erl.util.LoginViewModelFactory;
import com.app.erl.util.ResourceProvider;
import com.app.erl.viewModel.ManageOrderViewModel;
import com.app.utilities.callbacks.DialogButtonClickListener;
import com.app.utilities.utils.AlertDialogHelper;

import org.parceler.Parcels;

public class MyOrderDetailsActivity extends BaseActivity implements View.OnClickListener, DialogButtonClickListener {
    private ActivityMyOrderDetailsBinding binding;
    private Context mContext;
    private MyOrderServiceItemsListAdapter adapter;
    private ManageOrderViewModel manageOrderViewModel;
    private OrderInfo orderInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_order_details);
        mContext = this;

        manageOrderViewModel = ViewModelProviders.of(this, new LoginViewModelFactory(new ResourceProvider(getResources()))).get(ManageOrderViewModel.class);
        manageOrderViewModel.createView(this);
        manageOrderViewModel.mBaseResponse()
                .observe(this, cancelOrderResponse());
        manageOrderViewModel.getmOrderDetailsResponse()
                .observe(this, orderDetailsResponse());

        binding.imgBack.setOnClickListener(this);
        binding.txtCancel.setOnClickListener(this);

        getIntentData();
    }

    public void getIntentData() {
        if (getIntent().getExtras() != null && getIntent().hasExtra(AppConstant.IntentKey.ORDER_DATA)) {
            setOrderInfo(Parcels.unwrap(getIntent().getParcelableExtra(AppConstant.IntentKey.ORDER_DATA)));
            binding.setInfo(getOrderInfo());
            binding.txtTotalPaces.setText(String.valueOf(getOrderInfo().getOrder().size()));
            binding.txtTotalPrice.setText(String.format(mContext.getString(R.string.lbl_display_price), getOrderInfo().getTotal_price()));
            if (getOrderInfo().getStatus_id() == 1)
                binding.btnCancel.setVisibility(View.VISIBLE);
            else
                binding.btnCancel.setVisibility(View.GONE);
            setAddressAdapter();
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
            case R.id.txtCancel:
                AlertDialogHelper.showDialog(mContext, null, getString(R.string.msg_cancel_order), getString(R.string.yes), getString(R.string.no), true, this, AppConstant.DialogIdentifier.CANCEL_ORDER);
                break;
        }
    }

    private void setAddressAdapter() {
        if (getOrderInfo() != null
                && getOrderInfo().getOrder() != null
                && getOrderInfo().getOrder().size() > 0) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            binding.rvOrderItems.setLayoutManager(linearLayoutManager);
            binding.rvOrderItems.setHasFixedSize(true);
            adapter = new MyOrderServiceItemsListAdapter(mContext, getOrderInfo().getOrder());
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

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    @Override
    public void onPositiveButtonClicked(int dialogIdentifier) {
        if (dialogIdentifier == AppConstant.DialogIdentifier.CANCEL_ORDER) {
            manageOrderViewModel.clientCancelOrders(getOrderInfo().getId());
        }
    }

    @Override
    public void onNegativeButtonClicked(int dialogIdentifier) {

    }

    public OrderInfo getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(OrderInfo orderInfo) {
        this.orderInfo = orderInfo;
    }
}

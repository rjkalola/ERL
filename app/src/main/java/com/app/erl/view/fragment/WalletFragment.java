package com.app.erl.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.erl.R;
import com.app.erl.adapter.MyOrderListAdapter;
import com.app.erl.callback.SelectItemListener;
import com.app.erl.databinding.FragmentMyOrderBinding;
import com.app.erl.databinding.FragmentWalletBinding;
import com.app.erl.model.entity.response.OrderListResponse;
import com.app.erl.util.AppConstant;
import com.app.erl.util.AppUtils;
import com.app.erl.util.LoginViewModelFactory;
import com.app.erl.util.ResourceProvider;
import com.app.erl.view.activity.MyOrderDetailsActivity;
import com.app.erl.viewModel.ManageOrderViewModel;
import com.app.utilities.utils.AlertDialogHelper;

public class WalletFragment extends BaseFragment implements View.OnClickListener {
    private final int LAYOUT_ACTIVITY = R.layout.fragment_wallet;
    private FragmentWalletBinding binding;
    private Context mContext;
    private OrderListResponse ordersData;
    private MyOrderListAdapter adapter;
    private ManageOrderViewModel manageOrderViewModel;

    public static final WalletFragment newInstance() {
        return new WalletFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, LAYOUT_ACTIVITY, container, false);
        mContext = getActivity();

        manageOrderViewModel = ViewModelProviders.of(this, new LoginViewModelFactory(new ResourceProvider(getResources()))).get(ManageOrderViewModel.class);
        manageOrderViewModel.createView(this);
        manageOrderViewModel.orderListResponse()
                .observe(this, getOrderListResponse());

        return binding.getRoot();
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        switch (v.getId()) {
//            case R.id.txtStartWork:
//                startStopWork();
//                break;
        }
    }

    public void loadData(boolean showProgress) {
        manageOrderViewModel.getClientOrders(10, 0, showProgress);
    }

    public Observer getOrderListResponse() {
        return (Observer<OrderListResponse>) response -> {
            try {
                if (response == null) {
                    AlertDialogHelper.showDialog(mContext, null,
                            mContext.getString(R.string.error_unknown), mContext.getString(R.string.ok),
                            null, false, null, 0);
                    return;
                }
                if (response.isSuccess()) {

                } else {
                    AppUtils.handleUnauthorized(mContext, response);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    public OrderListResponse getOrdersData() {
        return ordersData;
    }

    public void setOrdersData(OrderListResponse ordersData) {
        this.ordersData = ordersData;
    }
}

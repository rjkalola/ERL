package com.app.erl.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.erl.R;
import com.app.erl.adapter.MyOrderListAdapter;
import com.app.erl.callback.OnSubmitRateListener;
import com.app.erl.callback.SelectItemListener;
import com.app.erl.databinding.FragmentMyOrderBinding;
import com.app.erl.model.entity.response.BaseResponse;
import com.app.erl.model.entity.response.OrderListResponse;
import com.app.erl.util.AppConstant;
import com.app.erl.util.AppUtils;
import com.app.erl.util.LoginViewModelFactory;
import com.app.erl.util.ResourceProvider;
import com.app.erl.view.activity.MyOrderDetailsActivity;
import com.app.erl.view.dialog.RateUsDialog;
import com.app.erl.viewModel.ManageOrderViewModel;
import com.app.utilities.utils.AlertDialogHelper;

public class MyOrderFragment extends BaseFragment implements View.OnClickListener, SelectItemListener, OnSubmitRateListener {
    private final int LAYOUT_ACTIVITY = R.layout.fragment_my_order;
    private FragmentMyOrderBinding binding;
    private Context mContext;
    private OrderListResponse ordersData;
    private MyOrderListAdapter adapter;
    private ManageOrderViewModel manageOrderViewModel;

    public static final MyOrderFragment newInstance() {
        return new MyOrderFragment();
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
        manageOrderViewModel.mBaseResponse()
                .observe(this, storeRatingResponse());

        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            loadData(false);
        });

        loadData(true);

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

    private void setAddressAdapter() {
        if (getOrdersData() != null
                && getOrdersData().getInfo() != null
                && getOrdersData().getInfo().size() > 0) {
            binding.routDetailsView.setVisibility(View.VISIBLE);
            binding.routEmptyView.setVisibility(View.GONE);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
            binding.rvOrdersList.setLayoutManager(linearLayoutManager);
            binding.rvOrdersList.setHasFixedSize(true);
            adapter = new MyOrderListAdapter(mContext, getOrdersData().getInfo(), this);
            binding.rvOrdersList.setAdapter(adapter);
        } else {
            binding.routDetailsView.setVisibility(View.GONE);
            binding.routEmptyView.setVisibility(View.VISIBLE);
        }
    }

    public Observer getOrderListResponse() {
        return (Observer<OrderListResponse>) response -> {
            binding.swipeRefreshLayout.setRefreshing(false);
            binding.loadMore.setVisibility(View.GONE);
            try {
                if (response == null) {
                    AlertDialogHelper.showDialog(mContext, null,
                            mContext.getString(R.string.error_unknown), mContext.getString(R.string.ok),
                            null, false, null, 0);
                    return;
                }
                if (response.isSuccess()) {
                    setOrdersData(response);
                    setAddressAdapter();
                } else {
                    AppUtils.handleUnauthorized(mContext, response);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    public Observer storeRatingResponse() {
        return (Observer<BaseResponse>) response -> {
            binding.swipeRefreshLayout.setRefreshing(false);
            binding.loadMore.setVisibility(View.GONE);
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

    @Override
    public void onSelectItem(int position, int action) {
        switch (action) {
            case AppConstant.Action.VIEW_ORDER:
                Bundle bundle = new Bundle();
                bundle.putInt(AppConstant.IntentKey.ORDER_ID, adapter.getList().get(adapter.getPosition()).getId());
                Intent i = new Intent(mContext, MyOrderDetailsActivity.class);
                i.putExtras(bundle);
                startActivityForResult(i, AppConstant.IntentKey.VIEW_ORDER);
                break;
            case AppConstant.Action.SHOW_FEEDBACK_DIALOG:
                FragmentManager fm = getActivity().getSupportFragmentManager();
                RateUsDialog rateUsDialog = RateUsDialog.newInstance(mContext, adapter.getList().get(adapter.getPosition()).getId(), this);
                rateUsDialog.show(fm, "rateUsDialog");
                break;
        }
    }

    @Override
    public void onSubmitRate(int orderId, float rating, String comment) {
        manageOrderViewModel.storeFeedbackRequest(orderId, rating, comment);
    }

    public OrderListResponse getOrdersData() {
        return ordersData;
    }

    public void setOrdersData(OrderListResponse ordersData) {
        this.ordersData = ordersData;
    }
}

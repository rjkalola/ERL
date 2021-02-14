package com.app.erl.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.erl.R;
import com.app.erl.adapter.AddressListAdapter;
import com.app.erl.adapter.MyOrderListAdapter;
import com.app.erl.callback.OnSubmitRateListener;
import com.app.erl.callback.SelectItemListener;
import com.app.erl.databinding.ActivityMyOrderListBinding;
import com.app.erl.databinding.ActivitySelectAddressBinding;
import com.app.erl.model.entity.response.AddressListResponse;
import com.app.erl.model.entity.response.BaseResponse;
import com.app.erl.model.entity.response.OrderListResponse;
import com.app.erl.model.entity.response.OrderResourcesResponse;
import com.app.erl.util.AppConstant;
import com.app.erl.util.AppUtils;
import com.app.erl.util.LoginViewModelFactory;
import com.app.erl.util.ResourceProvider;
import com.app.erl.view.dialog.RateUsDialog;
import com.app.erl.view.dialog.SelectTimeDialog;
import com.app.erl.viewModel.ManageAddressViewModel;
import com.app.erl.viewModel.ManageOrderViewModel;
import com.app.utilities.callbacks.DialogButtonClickListener;
import com.app.utilities.utils.AlertDialogHelper;

import org.parceler.Parcels;

public class MyOrderListActivity extends BaseActivity implements View.OnClickListener
        , SelectItemListener, OnSubmitRateListener {
    private ActivityMyOrderListBinding binding;
    private Context mContext;
    private OrderListResponse ordersData;
    private MyOrderListAdapter adapter;
    private ManageOrderViewModel manageOrderViewModel;
    private int pastVisibleItems, visibleItemCount, totalItemCount, offset = 0;
    private boolean loading = true, mIsLastPage = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_order_list);
        mContext = this;

        manageOrderViewModel = ViewModelProviders.of(this, new LoginViewModelFactory(new ResourceProvider(getResources()))).get(ManageOrderViewModel.class);
        manageOrderViewModel.createView(this);
        manageOrderViewModel.orderListResponse()
                .observe(this, getOrderListResponse());
        manageOrderViewModel.mBaseResponse()
                .observe(this, storeRatingResponse());

        binding.imgBack.setOnClickListener(this);

        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            offset = 0;
            loadData(false);
        });

        loadData(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBack:
                finish();
                break;
        }
    }

    public void loadData(boolean showProgress) {
        manageOrderViewModel.getClientOrders(10, offset, showProgress);
    }

    private void setAddressAdapter() {
        if (getOrdersData() != null
                && getOrdersData().getInfo() != null
                && getOrdersData().getInfo().size() > 0) {
            binding.routDetailsView.setVisibility(View.VISIBLE);
            binding.routEmptyView.setVisibility(View.GONE);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            binding.rvOrdersList.setLayoutManager(linearLayoutManager);
            binding.rvOrdersList.setHasFixedSize(true);
            adapter = new MyOrderListAdapter(mContext, getOrdersData().getInfo(), this);
            binding.rvOrdersList.setAdapter(adapter);
            recyclerViewScrollListener(linearLayoutManager);
        } else {
            binding.routDetailsView.setVisibility(View.GONE);
            binding.routEmptyView.setVisibility(View.VISIBLE);
        }
    }

    private void recyclerViewScrollListener(LinearLayoutManager layoutManager) {
        binding.rvOrdersList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    visibleItemCount = recyclerView.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            if (!mIsLastPage) {
                                loading = false;
                                binding.loadMore.setVisibility(View.VISIBLE);
                                loadData(false);
                            }
                        }
                    }
                }
            }
        });
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
                    if (offset == 0) {
                        setAddressAdapter();
                    } else if (response.getInfo() != null && !response.getInfo().isEmpty()) {
                        if (adapter != null) {
                            adapter.addData(response.getInfo());
                            binding.loadMore.setVisibility(View.GONE);
                            loading = true;
                        }
                    } else if (response.getOffset() == 0) {
                        binding.loadMore.setVisibility(View.GONE);
                        loading = true;
                    }
                    offset = response.getOffset();

                    if (offset == 0)
                        mIsLastPage = true;
                    else
                        mIsLastPage = false;
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
                moveActivityForResult(mContext, MyOrderDetailsActivity.class, false, false, AppConstant.IntentKey.VIEW_ORDER, bundle);
                break;
            case AppConstant.Action.SHOW_FEEDBACK_DIALOG:
                FragmentManager fm = getSupportFragmentManager();
                RateUsDialog rateUsDialog = RateUsDialog.newInstance(mContext, adapter.getList().get(adapter.getPosition()).getId(), this);
                rateUsDialog.show(fm, "rateUsDialog");
                break;
        }
    }

    @Override
    public void onSubmitRate(int orderId, float rating, String comment) {
        manageOrderViewModel.storeFeedbackRequest(orderId, rating, comment);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case AppConstant.IntentKey.VIEW_ORDER:
                if (resultCode == 1) {
                    offset = 0;
                    loadData(true);
                }
                break;
            default:
                break;
        }
    }

    public OrderListResponse getOrdersData() {
        return ordersData;
    }

    public void setOrdersData(OrderListResponse ordersData) {
        this.ordersData = ordersData;
    }
}

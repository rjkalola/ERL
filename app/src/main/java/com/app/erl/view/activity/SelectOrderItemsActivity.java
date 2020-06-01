package com.app.erl.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.app.erl.R;
import com.app.erl.adapter.ServiceItemsTabAdapter;
import com.app.erl.adapter.ViewPagerAdapter;
import com.app.erl.callback.SelectItemListener;
import com.app.erl.databinding.ActivitySelectOrderItemsBinding;
import com.app.erl.model.entity.info.ClientDashBoardInfo;
import com.app.erl.model.entity.info.ServiceItemInfo;
import com.app.erl.model.entity.response.ClientDashBoardResponse;
import com.app.erl.util.AppConstant;
import com.app.erl.view.fragment.OrderItemsTabFragment;
import com.app.utilities.utils.ToastHelper;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SelectOrderItemsActivity extends BaseActivity implements View.OnClickListener, SelectItemListener {
    private ActivitySelectOrderItemsBinding binding;
    private Context mContext;
    private int position;
    private ClientDashBoardResponse dashBoardData;
    private ViewPagerAdapter pagerAdapter;
    private ServiceItemsTabAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_select_order_items);
        mContext = this;

        binding.vpOrderItems.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (adapter != null)
                    adapter.refreshAdapter(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        binding.txtContinue.setOnClickListener(this);
        binding.imgBack.setOnClickListener(this);

        getIntentData();
    }

    public void getIntentData() {
        if (getIntent().getExtras() != null
                && Parcels.unwrap(getIntent().getParcelableExtra(AppConstant.IntentKey.DASHBOARD_DATA)) != null) {
            setDashBoardData(Parcels.unwrap(getIntent().getParcelableExtra(AppConstant.IntentKey.DASHBOARD_DATA)));
            position = getIntent().getIntExtra(AppConstant.IntentKey.POSITION, 0);
            setTabsAdapter();
            setupViewPager(binding.vpOrderItems);
        } else {
            finish();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtContinue:
                boolean isContinue = false;

                ClientDashBoardResponse cartData = new ClientDashBoardResponse();
                List<ClientDashBoardInfo> cartList = new ArrayList<>();
                for (int i = 0; i < getDashBoardData().getInfo().size(); i++) {
                    ClientDashBoardInfo cartInfo = new ClientDashBoardInfo(getDashBoardData().getInfo().get(i).getId(), getDashBoardData().getInfo().get(i).getName(), getDashBoardData().getInfo().get(i).getImage(), getDashBoardData().getInfo().get(i).getCount_order_items());
                    List<ServiceItemInfo> serviceItemsList = new ArrayList<>();
                    for (int j = 0; j < getDashBoardData().getInfo().get(i).getService_item().size(); j++) {
                        if (getDashBoardData().getInfo().get(i).getService_item().get(j).getQuantity() > 0) {
                            isContinue = true;
                            ServiceItemInfo cartServiceItemsInfo = new ServiceItemInfo(getDashBoardData().getInfo().get(i).getService_item().get(j).getId()
                                    , getDashBoardData().getInfo().get(i).getService_item().get(j).getName()
                                    , getDashBoardData().getInfo().get(i).getService_item().get(j).getPrice()
                                    , getDashBoardData().getInfo().get(i).getService_item().get(j).getQuantity()
                                    , getDashBoardData().getInfo().get(i).getService_item().get(j).getImage());
                            serviceItemsList.add(cartServiceItemsInfo);
                        }
                    }
                    cartInfo.setService_item(serviceItemsList);
                    if (serviceItemsList.size() > 0)
                        cartList.add(cartInfo);
                }
                cartData.setInfo(cartList);

                if (isContinue) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(AppConstant.IntentKey.POSITION, position);
                    bundle.putParcelable(AppConstant.IntentKey.DASHBOARD_DATA, Parcels.wrap(cartData));
                    moveActivity(mContext, CreateOrderActivity.class, false, false, bundle);
                } else {
                    ToastHelper.error(mContext, getString(R.string.error_select_at_least_one_item), Toast.LENGTH_SHORT, false);
                }

                break;
            case R.id.imgBack:
                finish();
                break;
        }
    }

    private void setTabsAdapter() {
        if (getDashBoardData() != null && getDashBoardData().getInfo() != null && getDashBoardData().getInfo().size() > 0) {
            binding.rvTabs.setVisibility(View.VISIBLE);
            binding.rvTabs.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            binding.rvTabs.setHasFixedSize(true);
            adapter = new ServiceItemsTabAdapter(mContext, getDashBoardData().getInfo(), position, this);
            binding.rvTabs.setAdapter(adapter);
        } else {
            binding.rvTabs.setVisibility(View.GONE);
        }

    }

    private void setupViewPager(ViewPager viewPager) {
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        for (int i = 0; i < getDashBoardData().getInfo().size(); i++) {
            Bundle bundle = new Bundle();
            bundle.putInt(AppConstant.IntentKey.POSITION, i);
            bundle.putParcelable(AppConstant.IntentKey.SERVICE_ITEMS_DATA, Parcels.wrap(getDashBoardData().getInfo().get(i).getService_item()));
            pagerAdapter.addFrag(OrderItemsTabFragment.newInstance(bundle), "");
        }
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(position);
        viewPager.setOffscreenPageLimit(2);
    }

    public void setItemsQuantity(int itemPosition, int quantity) {
        getDashBoardData().getInfo().get(binding.vpOrderItems.getCurrentItem()).getService_item().get(itemPosition).setQuantity(quantity);
        int count = 0;
        for (ServiceItemInfo info : getDashBoardData().getInfo().get(binding.vpOrderItems.getCurrentItem()).getService_item()) {
            if (info.getQuantity() > 0) {
                count = count + 1;
            }
        }
        adapter.refreshTabCount(binding.vpOrderItems.getCurrentItem(), count);
    }

    @Override
    public void onSelectItem(int position, int action) {
        binding.vpOrderItems.setCurrentItem(position);
    }

    public ClientDashBoardResponse getDashBoardData() {
        return dashBoardData;
    }

    public void setDashBoardData(ClientDashBoardResponse dashBoardData) {
        this.dashBoardData = dashBoardData;
    }
}

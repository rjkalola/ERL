package com.app.erl.view.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.app.erl.R;
import com.app.erl.adapter.ServiceHourTypeListAdapter;
import com.app.erl.adapter.ViewPagerAdapter;
import com.app.erl.callback.SelectItemListener;
import com.app.erl.databinding.FragmentPriceListBinding;
import com.app.erl.model.entity.response.ServiceItemsResponse;
import com.app.erl.util.AppConstant;
import com.app.erl.util.AppUtils;
import com.app.erl.util.LoginViewModelFactory;
import com.app.erl.util.ResourceProvider;
import com.app.erl.viewModel.DashBoardViewModel;
import com.app.utilities.utils.AlertDialogHelper;

import org.parceler.Parcels;

public class PriceListFragment extends BaseFragment implements View.OnClickListener, SelectItemListener {
    private final int LAYOUT_ACTIVITY = R.layout.fragment_price_list;
    private FragmentPriceListBinding binding;
    private Context mContext;
    private DashBoardViewModel dashBoardViewModel;
    private Menu mMenu;
    private ServiceItemsResponse serviceItemsData;
    private ServiceHourTypeListAdapter serviceHourTypeListAdapter;
    private ViewPagerAdapter pagerAdapter;
    private int selectedHourTypePosition = 0;

    public static final PriceListFragment newInstance() {
        return new PriceListFragment();
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
        dashBoardViewModel = ViewModelProviders.of(this, new LoginViewModelFactory(new ResourceProvider(getResources()))).get(DashBoardViewModel.class);
        dashBoardViewModel.createView(this);
        dashBoardViewModel.serviceItemsResponse()
                .observe(this, serviceItemsResponse());
        dashBoardViewModel.getServiceItemsRequest();
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

    public Observer serviceItemsResponse() {
        return (Observer<ServiceItemsResponse>) response -> {
            try {
                if (response == null) {
                    AlertDialogHelper.showDialog(mContext, null,
                            mContext.getString(R.string.error_unknown), mContext.getString(R.string.ok),
                            null, false, null, 0);
                    return;
                }
                if (response.isSuccess()) {
                    setServiceItemsData(response);
                    setServiceHourTypeListAdapter();
                } else {
                    AppUtils.handleUnauthorized(mContext, response);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    private void setServiceHourTypeListAdapter() {
        if (getServiceItemsData() != null
                && getServiceItemsData().getInfo() != null
                && getServiceItemsData().getInfo().size() > 0) {
            binding.rvServiceHourType.setVisibility(View.VISIBLE);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
            binding.rvServiceHourType.setLayoutManager(linearLayoutManager);
            binding.rvServiceHourType.setHasFixedSize(true);
            serviceHourTypeListAdapter = new ServiceHourTypeListAdapter(mContext, getServiceItemsData().getInfo(), this);
            binding.rvServiceHourType.setAdapter(serviceHourTypeListAdapter);

            setupViewPager(selectedHourTypePosition);
        } else {
            binding.rvServiceHourType.setVisibility(View.GONE);
        }
    }

    private void setupViewPager(int position) {
        selectedHourTypePosition = position;
        if (getServiceItemsData() != null
                && getServiceItemsData().getInfo().get(position).getPriceList() != null
                && getServiceItemsData().getInfo().get(position).getPriceList().size() > 0) {
            Log.e("test", "Price List Size:" + getServiceItemsData().getInfo().get(position).getPriceList().size());
            binding.routPriceView.setVisibility(View.VISIBLE);
            pagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
            for (int i = 0; i < getServiceItemsData().getInfo().get(position).getPriceList().size(); i++) {
                Bundle bundle = new Bundle();
                bundle.putInt(AppConstant.IntentKey.POSITION, i);
                bundle.putParcelable(AppConstant.IntentKey.SERVICE_ITEMS_DATA, Parcels.wrap(getServiceItemsData().getInfo().get(position).getPriceList().get(i).getItems()));
                pagerAdapter.addFrag(OrderItemsTabFragment.newInstance(bundle), getServiceItemsData().getInfo().get(position).getPriceList().get(i).getName());
            }
            binding.vpOrderItems.setAdapter(pagerAdapter);
            binding.vpOrderItems.setCurrentItem(0);
            binding.vpOrderItems.setOffscreenPageLimit(getServiceItemsData().getInfo().get(position).getPriceList().size());
            binding.tabs.setupWithViewPager(binding.vpOrderItems);
            binding.txtSelectedItemName.setText(getServiceItemsData().getInfo().get(position).getPriceList().get(0).getName());

            binding.vpOrderItems.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    binding.txtSelectedItemName.setText(getServiceItemsData().getInfo().get(selectedHourTypePosition).getPriceList().get(position).getName());
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        } else {
            binding.routPriceView.setVisibility(View.GONE);
        }

    }

    @Override
    public void onSelectItem(int position, int action) {
        if (action == AppConstant.Action.SELECT_SERVICE_HOUR_TYPE) {
            setupViewPager(position);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.dashboard_menu, menu);
        mMenu = menu;
        menu.findItem(R.id.actionArchived).setVisible(false);
        menu.findItem(R.id.actionNotification).setVisible(false);
        menu.findItem(R.id.actionSelectedItemCount).setVisible(true);
        menu.findItem(R.id.actionSearch).setVisible(true);

        setCartCountMenu(0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void setCartCountMenu(int cartCount) {
        if (mMenu == null) return;
        MenuItem menuItem = mMenu.findItem(R.id.actionSelectedItemCount);
        RelativeLayout mContainer = (RelativeLayout) menuItem.setActionView(R.layout.layout_toolbar_item_count).getActionView();
        TextView mBadgeCount = mContainer.findViewById(R.id.txtItemCount);
        ImageView mIconBadge = mContainer.findViewById(R.id.icon_badge);
        mIconBadge.setImageResource(R.drawable.ic_selected_item_count);
        Drawable countIcon = getResources().getDrawable(R.drawable.img_white_circle);
        mBadgeCount.setBackground(countIcon);

        mContainer.setOnClickListener(v -> onOptionsItemSelected(menuItem));

        if (cartCount > 0) {
            mBadgeCount.setVisibility(View.VISIBLE);
            mBadgeCount.setText(String.valueOf(cartCount));
        } else {
            mBadgeCount.setVisibility(View.INVISIBLE);
        }
    }

    public ServiceItemsResponse getServiceItemsData() {
        return serviceItemsData;
    }

    public void setServiceItemsData(ServiceItemsResponse serviceItemsData) {
        this.serviceItemsData = serviceItemsData;
    }
}

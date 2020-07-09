package com.app.erl.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.app.erl.R;
import com.app.erl.adapter.DashboardOfferPagerAdapter;
import com.app.erl.adapter.DashboardOfferPagerDotsAdapter;
import com.app.erl.databinding.FragmentHomeBinding;
import com.app.erl.model.entity.response.ClientDashBoardResponse;
import com.app.erl.util.AppConstant;
import com.app.erl.util.AppUtils;
import com.app.erl.util.LoginViewModelFactory;
import com.app.erl.util.ResourceProvider;
import com.app.erl.view.activity.ChatActivity;
import com.app.erl.view.activity.ContactUsActivity;
import com.app.erl.view.activity.DashBoardActivity;
import com.app.erl.view.activity.StoreLocatorActivity;
import com.app.erl.viewModel.DashBoardViewModel;
import com.app.utilities.utils.AlertDialogHelper;
import com.app.utilities.utils.StringHelper;

public class HomeFragment extends BaseFragment implements View.OnClickListener {
    private final int LAYOUT_ACTIVITY = R.layout.fragment_home;
    private FragmentHomeBinding binding;
    private Context mContext;
    private DashBoardViewModel dashBoardViewModel;
    private Menu mMenu;
    private ClientDashBoardResponse dashBoardData;
    private DashboardOfferPagerDotsAdapter adapterDots;

    public static final HomeFragment newInstance() {
        return new HomeFragment();
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
        dashBoardViewModel.clientDashBoardResponse()
                .observe(this, getClientDashBoardResponse());
        dashBoardViewModel.getClientDashboardRequest();

        binding.routLiveSupport.setOnClickListener(this);
        binding.routStoreLocator.setOnClickListener(this);
        binding.routPlaceOrder.setOnClickListener(this);
        binding.routContactUs.setOnClickListener(this);

        return binding.getRoot();
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.routLiveSupport:
                moveActivity(mContext, ChatActivity.class, false, false, null);
                break;
            case R.id.routStoreLocator:
                moveActivity(mContext, StoreLocatorActivity.class, false, false, null);
                break;
            case R.id.routPlaceOrder:
                try {
                    ((DashBoardActivity) getActivity()).setupTab(1);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.routContactUs:
                if (getDashBoardData() != null) {
                    bundle.putString(AppConstant.IntentKey.EMAIL, !StringHelper.isEmpty(getDashBoardData().getContact_email()) ? getDashBoardData().getContact_email() : "");
                    bundle.putString(AppConstant.IntentKey.PHONE, !StringHelper.isEmpty(getDashBoardData().getContact_number()) ? getDashBoardData().getContact_number() : "");
                }

                moveActivity(mContext, ContactUsActivity.class, false, false, bundle);
                break;
        }
    }

    public void setOfferPagerAdapter() {
        if (getDashBoardData() != null && getDashBoardData().getInfo() != null && getDashBoardData().getInfo().size() > 0) {
            binding.routPagerView.setVisibility(View.VISIBLE);
            DashboardOfferPagerAdapter adapterPager = new DashboardOfferPagerAdapter(mContext, getDashBoardData().getInfo());
            binding.vpOffers.setAdapter(adapterPager);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
            binding.rvDots.setLayoutManager(linearLayoutManager);
            adapterDots = new DashboardOfferPagerDotsAdapter(mContext, getDashBoardData().getInfo().size());
            binding.rvDots.setAdapter(adapterDots);

            binding.vpOffers.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    if (adapterDots != null)
                        adapterDots.setSelectedDot(position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

        } else {
            binding.routPagerView.setVisibility(View.GONE);
        }
    }

    public Observer getClientDashBoardResponse() {
        return (Observer<ClientDashBoardResponse>) response -> {
            try {
                if (response == null) {
                    AlertDialogHelper.showDialog(mContext, null,
                            mContext.getString(R.string.error_unknown), mContext.getString(R.string.ok),
                            null, false, null, 0);
                    return;
                }
                if (response.isSuccess()) {
                    setDashBoardData(response);
                    setOfferPagerAdapter();
                } else {
                    AppUtils.handleUnauthorized(mContext, response);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.dashboard_menu, menu);

        menu.findItem(R.id.actionArchived).setVisible(true);
        menu.findItem(R.id.actionNotification).setVisible(true);
        menu.findItem(R.id.actionSelectedItemCount).setVisible(false);
        menu.findItem(R.id.actionSearch).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public ClientDashBoardResponse getDashBoardData() {
        return dashBoardData;
    }

    public void setDashBoardData(ClientDashBoardResponse dashBoardData) {
        this.dashBoardData = dashBoardData;
    }
}

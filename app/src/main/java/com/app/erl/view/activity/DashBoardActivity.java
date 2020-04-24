package com.app.erl.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.erl.ERLApp;
import com.app.erl.R;
import com.app.erl.adapter.ClientDashBoardAdapter;
import com.app.erl.adapter.NavigationItemsListAdapter;
import com.app.erl.callback.SelectINavigationItemListener;
import com.app.erl.callback.SelectItemListener;
import com.app.erl.databinding.ActivityDashboardBinding;
import com.app.erl.databinding.NavHeaderDashboardBinding;
import com.app.erl.model.entity.response.ClientDashBoardResponse;
import com.app.erl.util.AppConstant;
import com.app.erl.util.AppUtils;
import com.app.erl.util.LoginViewModelFactory;
import com.app.erl.util.ResourceProvider;
import com.app.erl.viewModel.DashBoardViewModel;
import com.app.utilities.callbacks.DialogButtonClickListener;
import com.app.utilities.utils.AlertDialogHelper;

import org.parceler.Parcels;

public class DashBoardActivity extends BaseActivity implements View.OnClickListener, SelectINavigationItemListener, DialogButtonClickListener, SelectItemListener {
    private ActivityDashboardBinding binding;
    private NavHeaderDashboardBinding bindingNavHeader;
    private DashBoardViewModel dashBoardViewModel;
    private Context mContext;
    private ClientDashBoardResponse dashBoardData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);
//        bindingNavHeader = DataBindingUtil.bind(binding.navView.getHeaderView(0));
        mContext = this;
        dashBoardViewModel = ViewModelProviders.of(this, new LoginViewModelFactory(new ResourceProvider(getResources()))).get(DashBoardViewModel.class);
        dashBoardViewModel.createView(this);
        dashBoardViewModel.clientDashBoardResponse()
                .observe(this, getClientDashBoardResponse());
        binding.appBarLayout.imgNavigationMenu.setOnClickListener(this);

        dashBoardViewModel.getClientDashboardRequest();
        setNavigationItemAdapter();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgNavigationMenu:
                binding.drawerLayout.openDrawer(GravityCompat.END);
                break;
        }
    }

    private void setNavigationItemAdapter() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        binding.rvNavigationItems.setLayoutManager(linearLayoutManager);
        binding.rvNavigationItems.setHasFixedSize(true);
        NavigationItemsListAdapter adapter = new NavigationItemsListAdapter(mContext, this);
        binding.rvNavigationItems.setAdapter(adapter);
    }

    private void setDashboardAdapter() {
        if (getDashBoardData() != null && getDashBoardData().getInfo() != null && getDashBoardData().getInfo().size() > 0) {
            binding.appBarLayout.rvDashBoardItems.setVisibility(View.VISIBLE);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
            binding.appBarLayout.rvDashBoardItems.setLayoutManager(linearLayoutManager);
            binding.appBarLayout.rvDashBoardItems.setHasFixedSize(true);
            ClientDashBoardAdapter adapter = new ClientDashBoardAdapter(mContext, getDashBoardData().getInfo(), this);
            binding.appBarLayout.rvDashBoardItems.setAdapter(adapter);
        } else {
            binding.appBarLayout.rvDashBoardItems.setVisibility(View.GONE);
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
                    setDashboardAdapter();
                } else {
                    AppUtils.handleUnauthorized(mContext, response);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    @Override
    public void onSelectItem(int position, String item) {
        if (item.equals(getString(R.string.my_profile))) {

        } else if (item.equals(getString(R.string.my_order))) {

        } else if (item.equals(getString(R.string.logout))) {
            AlertDialogHelper.showDialog(mContext, "", getString(R.string.logout_msg), getString(R.string.yes), getString(R.string.no), false, this, AppConstant.DialogIdentifier.LOGOUT);
        }
        binding.drawerLayout.closeDrawer(GravityCompat.END);
    }

    public void moveOrderItemsList(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt(AppConstant.IntentKey.POSITION, position);
        bundle.putParcelable(AppConstant.IntentKey.DASHBOARD_DATA, Parcels.wrap(getDashBoardData()));
        moveActivity(mContext, SelectOrderItemsActivity.class, false, false,bundle );
    }

    @Override
    public void onPositiveButtonClicked(int dialogIdentifier) {
        if (dialogIdentifier == AppConstant.DialogIdentifier.LOGOUT) {
            ERLApp.get().clearData();
            moveActivity(mContext, LoginActivity.class, true, true, null);
        }
    }

    @Override
    public void onNegativeButtonClicked(int dialogIdentifier) {

    }

    @Override
    public void onSelectItem(int position, int action) {

    }

    public ClientDashBoardResponse getDashBoardData() {
        return dashBoardData;
    }

    public void setDashBoardData(ClientDashBoardResponse dashBoardData) {
        this.dashBoardData = dashBoardData;
    }
}

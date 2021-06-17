package com.app.erl.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.app.erl.R;
import com.app.erl.adapter.MyOrderListAdapter;
import com.app.erl.databinding.FragmentWalletBinding;
import com.app.erl.model.entity.response.OrderListResponse;
import com.app.erl.model.entity.response.PromoCodeResponse;
import com.app.erl.util.AppUtils;
import com.app.erl.util.LoginViewModelFactory;
import com.app.erl.util.ResourceProvider;
import com.app.erl.view.activity.TransactionHistoryActivity;
import com.app.erl.viewModel.ManageOrderViewModel;
import com.app.utilities.utils.AlertDialogHelper;
import com.app.utilities.utils.StringHelper;
import com.app.utilities.utils.ToastHelper;

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
        manageOrderViewModel.mPromoCodeResponse()
                .observe(this, promoCodeResponse());

        binding.txtOk.setOnClickListener(this);
        binding.txtViewTransactionHistory.setOnClickListener(this);

        return binding.getRoot();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtOk:
                if (!StringHelper.isEmpty(binding.edtCouponCode.getText().toString().trim())) {
                    manageOrderViewModel.addCouponCodeRequest(binding.edtCouponCode.getText().toString().trim());
                } else {
                    ToastHelper.normal(mContext, getString(R.string.error_empty_coupon_code), Toast.LENGTH_SHORT, false);
                }
                break;
            case R.id.txtViewTransactionHistory:
                moveActivity(mContext, TransactionHistoryActivity.class, false, false, null);
                break;
        }
    }

    public Observer promoCodeResponse() {
        return (Observer<PromoCodeResponse>) response -> {
            try {
                if (response == null) {
                    AlertDialogHelper.showDialog(mContext, null,
                            mContext.getString(R.string.error_unknown), mContext.getString(R.string.ok),
                            null, false, null, 0);
                    return;
                }
                if (response.isSuccess()) {
                    binding.edtCouponCode.setText("");
                    setWalletBalance(response.getAmount());
                } else {
                    AppUtils.handleUnauthorized(mContext, response);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    public void setWalletBalance(double walletBalance) {
        binding.txtWalletBalance.setText(String.format(mContext.getString(R.string.lbl_display_price), Double.toString(walletBalance)));
    }

}

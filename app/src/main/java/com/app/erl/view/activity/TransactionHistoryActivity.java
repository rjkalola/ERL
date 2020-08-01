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
import com.app.erl.adapter.TransactionHistoryListAdapter;
import com.app.erl.databinding.ActivityTransactionHistoryBinding;
import com.app.erl.model.entity.info.TransactionHistoryInfo;
import com.app.erl.model.entity.response.BaseResponse;
import com.app.erl.model.entity.response.TransactionHistoryResponse;
import com.app.erl.util.AppUtils;
import com.app.erl.util.LoginViewModelFactory;
import com.app.erl.util.ResourceProvider;
import com.app.erl.viewModel.ManageOrderViewModel;
import com.app.utilities.utils.AlertDialogHelper;

import java.util.List;

public class TransactionHistoryActivity extends BaseActivity implements View.OnClickListener {
    private ActivityTransactionHistoryBinding binding;
    private Context mContext;
    private TransactionHistoryListAdapter adapter;
    private ManageOrderViewModel manageOrderViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_transaction_history);
        mContext = this;

        manageOrderViewModel = ViewModelProviders.of(this, new LoginViewModelFactory(new ResourceProvider(getResources()))).get(ManageOrderViewModel.class);
        manageOrderViewModel.createView(this);
        manageOrderViewModel.mmTransactionHistoryResponse()
                .observe(this, getTransactionHistoryResponse());

        binding.imgBack.setOnClickListener(this);

        manageOrderViewModel.transactionHistoryRequest();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBack:
                finish();
                break;
        }
    }

    private void setTransactionHistoryAdapter(List<TransactionHistoryInfo> list) {
        if (list != null && list.size() > 0) {
            binding.rvTransactionHistoryList.setVisibility(View.VISIBLE);
            binding.routEmptyView.setVisibility(View.GONE);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            binding.rvTransactionHistoryList.setLayoutManager(linearLayoutManager);
            adapter = new TransactionHistoryListAdapter(mContext, list);
            binding.rvTransactionHistoryList.setAdapter(adapter);
        } else {
            binding.rvTransactionHistoryList.setVisibility(View.GONE);
            binding.routEmptyView.setVisibility(View.VISIBLE);
        }
    }

    public Observer getTransactionHistoryResponse() {
        return (Observer<TransactionHistoryResponse>) response -> {
            try {
                if (response == null) {
                    AlertDialogHelper.showDialog(mContext, null,
                            mContext.getString(R.string.error_unknown), mContext.getString(R.string.ok),
                            null, false, null, 0);
                    return;
                }
                if (response.isSuccess()) {
                    setTransactionHistoryAdapter(response.getInfo());
                } else {
                    AppUtils.handleUnauthorized(mContext, response);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }
}

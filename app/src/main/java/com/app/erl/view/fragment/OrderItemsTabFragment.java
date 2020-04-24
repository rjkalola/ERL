package com.app.erl.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.erl.R;
import com.app.erl.adapter.ServiceItemsListAdapter;
import com.app.erl.callback.SelectItemListener;
import com.app.erl.databinding.FragmentOrderItemsBinding;
import com.app.erl.model.entity.info.ServiceItemInfo;
import com.app.erl.util.AppConstant;
import com.app.erl.view.activity.SelectOrderItemsActivity;

import org.parceler.Parcels;

import java.util.List;


public class OrderItemsTabFragment extends BaseFragment implements View.OnClickListener, SelectItemListener {
    private final int LAYOUT_ACTIVITY = R.layout.fragment_order_items;
    private FragmentOrderItemsBinding binding;
    private Context mContext;
    private ServiceItemsListAdapter adapter;

    public static final OrderItemsTabFragment newInstance(Bundle bundle) {
        OrderItemsTabFragment fragment = new OrderItemsTabFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, LAYOUT_ACTIVITY, container, false);
        mContext = getActivity();

        getBundleData();

        return binding.getRoot();
    }

    private void getBundleData() {
        if (getArguments() != null) {
            if (Parcels.unwrap(getArguments().getParcelable(AppConstant.IntentKey.SERVICE_ITEMS_DATA)) != null) {
                List<ServiceItemInfo> list = Parcels.unwrap(getArguments().getParcelable(AppConstant.IntentKey.SERVICE_ITEMS_DATA));
                Log.e("test", "Fragment Size:" + list.size());
                setAdapter(list);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    private void setAdapter(List<ServiceItemInfo> list) {
        if (list != null && list.size() > 0) {
            binding.rvOrderItemsList.setVisibility(View.VISIBLE);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
            binding.rvOrderItemsList.setLayoutManager(linearLayoutManager);
            binding.rvOrderItemsList.setHasFixedSize(true);
            adapter = new ServiceItemsListAdapter(mContext, list, this);
            binding.rvOrderItemsList.setAdapter(adapter);
        } else {
            binding.rvOrderItemsList.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSelectItem(int position, int quantity) {
        if (getActivity() != null)
            ((SelectOrderItemsActivity) getActivity()).setItemsQuantity(position,quantity);
    }
}

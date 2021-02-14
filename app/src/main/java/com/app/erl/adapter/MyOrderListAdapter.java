package com.app.erl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.erl.R;
import com.app.erl.callback.SelectItemListener;
import com.app.erl.databinding.RowMyOrderListBinding;
import com.app.erl.model.entity.info.OrderInfo;
import com.app.erl.util.AppConstant;
import com.app.erl.util.AppUtils;

import java.util.List;

public class MyOrderListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<OrderInfo> list;
    private SelectItemListener listener;
    private int position;

    public MyOrderListAdapter(Context context, List<OrderInfo> list, SelectItemListener listener) {
        this.mContext = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_my_order_list, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;

        OrderInfo info = list.get(position);
        itemViewHolder.getData(info);

        itemViewHolder.binding.txtPrice.setText(String.format(mContext.getString(R.string.lbl_display_price), info.getTotal_price()));

        if (info.isShow_payment_button() || info.isShow_feedback()) {
            itemViewHolder.binding.routButtonsView.setVisibility(View.VISIBLE);

            if (info.isShow_payment_button())
                itemViewHolder.binding.txtPay.setVisibility(View.VISIBLE);
            else
                itemViewHolder.binding.txtPay.setVisibility(View.GONE);

            if (info.isShow_feedback())
                itemViewHolder.binding.txtGiveFeedback.setVisibility(View.VISIBLE);
            else
                itemViewHolder.binding.txtGiveFeedback.setVisibility(View.GONE);

            if (info.isShow_payment_button() && info.isShow_feedback())
                itemViewHolder.binding.dividerButtons.setVisibility(View.VISIBLE);
            else
                itemViewHolder.binding.dividerButtons.setVisibility(View.GONE);

        } else {
            itemViewHolder.binding.routButtonsView.setVisibility(View.GONE);
        }

        itemViewHolder.binding.routMainView.setOnClickListener(v -> {
            if (listener != null) {
                setPosition(position);
                listener.onSelectItem(position, AppConstant.Action.VIEW_ORDER);
            }
        });

        itemViewHolder.binding.txtPay.setOnClickListener(v -> {
            AppConstant.ORDER_ID = info.getId();
            AppConstant.PAYMENT_CITY = info.getCity_name();
            AppConstant.PAYMENT_ADDRESS = info.getAddress();
            AppUtils.sendMessage(mContext, info.getAmount_pay());
        });

        itemViewHolder.binding.txtGiveFeedback.setOnClickListener(v -> {
            if (listener != null) {
                setPosition(position);
                listener.onSelectItem(position, AppConstant.Action.SHOW_FEEDBACK_DIALOG);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private RowMyOrderListBinding binding;

        public void getData(OrderInfo info) {
            binding.setInfo(info);
        }

        public ItemViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

    public void addData(List<OrderInfo> data) {
        list.addAll(data);
        notifyDataSetChanged();
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public List<OrderInfo> getList() {
        return list;
    }

    public void setList(List<OrderInfo> list) {
        this.list = list;
    }
}
